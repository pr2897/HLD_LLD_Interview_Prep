import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * TRACK A · Feature A1 — Menu cache (cache-aside).          Redis type: STRING + TTL
 *
 * Problem: every app open loads a restaurant's menu. The DB query takes 300 ms (FakeDb.getMenu).
 * Menus change maybe once a day. Reading them from Postgres 10,000 times a minute is waste.
 *
 * Keys you own:   menu:{restaurantId}   -> the menu as a JSON string, expires after ~60 s
 *
 *   GET /restaurants/{id}/menu                       -> header X-Cache: HIT or MISS
 *   PUT /restaurants/{id}/menu/{itemId}  {"price":99} -> updates DB, invalidates cache
 *
 * Try it:  redis-cli GET menu:r1     redis-cli TTL menu:r1
 */
final class MenuCache {

    static final int TTL_SECONDS = 60;
    static final int TTL_JITTER_SECONDS = 10;

    /** json = the menu as JSON, hit = whether it came from Redis. */
    record Result(String json, boolean hit) {}

    static String key(String restaurantId) { return "menu:" + restaurantId; }

    /**
     * TODO(1): Cache-aside read.
     *   1. GET menu:{id}. Found? -> return new Result(value, true)
     *   2. Not found -> FakeDb.getMenu(id)   (slow!)   -> null means unknown restaurant: return null
     *   3. json = Json.write(items); SET it with an expiry of TTL_SECONDS + random(0..TTL_JITTER_SECONDS)
     *      (one command: SET key value EX seconds)
     *   4. return new Result(json, false)
     *
     *   Why the random jitter? See docs/TRACK-A.md, question 2.
     */
    static Result getMenu(String restaurantId) {
        String key = key(restaurantId);
        var value = Redis.call("GET", key);
        if(value == null) {
            List<FakeDb.MenuItem> menuItems = FakeDb.getMenu(restaurantId);
            if(menuItems == null) return null;
            String json = Json.write(menuItems);
            Redis.call("SET", key, json, "EX", TTL_SECONDS + ThreadLocalRandom.current().nextInt(0, TTL_JITTER_SECONDS));
            return new Result(json, false);
        }
        return new Result((String)value, true);
    }

    /**
     * TODO(2): Write path. A restaurant owner changes a price.
     *   1. Update the source of truth first: FakeDb.updatePrice(...)  (false -> return false)
     *   2. Then DELETE menu:{id}, so the next read loads the fresh menu.
     *   Why delete instead of SET-ing the new menu into the cache? See docs/TRACK-A.md, question 3.
     */
    static boolean updatePrice(String restaurantId, String itemId, int newPrice) {
        boolean updated = FakeDb.updatePrice(restaurantId, itemId, newPrice);
        if (!updated) return false;
        String key = key(restaurantId);
        Redis.call("DEL", key);
        return true;
    }

    /**
     * STRETCH(3): Stampede protection. When a hot key expires, 500 requests miss at the same
     * moment and ALL of them hit the DB. Make only ONE of them rebuild the cache:
     *   - on a miss, try  SET lock:menu:{id} 1 NX PX 2000
     *   - won the lock  -> load from DB, SET the cache, DEL the lock
     *   - lost the lock -> sleep 50 ms and try getMenu again (max ~40 times)
     *   The checker has a [STRETCH] test that fires 30 concurrent misses and counts DB reads.
     */
    static Result getMenuProtected(String restaurantId) {
        String key = key(restaurantId);
        var value = Redis.call("GET", key);
        if(value != null) return new Result((String)value, true);
        
        String lockKey = "lock:menu:"+restaurantId;
        
        for(int i=0;i<40;i++) { // retry max 40 times
            value = Redis.call("GET", key);
            if (value != null) return new Result((String) value, true);

            var lockAcquired = Redis.call("SET", lockKey, "1", "NX", "PX", 2000);
            if(lockAcquired != null) {
                var menuItems = FakeDb.getMenu(restaurantId);
                value = Json.write(menuItems);
                Redis.call("SET", key, value, "EX", TTL_SECONDS + ThreadLocalRandom.current().nextInt(0, TTL_JITTER_SECONDS));
                Redis.call("DEL", lockKey);
                return new Result((String)value, false);
            } else {
                try {
                    Thread.sleep(50);    
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }

        return new Result(null, false);
    }

    private MenuCache() {}
}
