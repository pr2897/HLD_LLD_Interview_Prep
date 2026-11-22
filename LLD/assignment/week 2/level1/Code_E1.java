import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class AppConfig {
    private static  Map<String, String> secrets;
    private static volatile AppConfig instance;

    private AppConfig() {
        secrets = new HashMap<>();
        
        try {
            Path path = Path.of("/Users/piyush/Desktop/HLD_LLD_Interview_Prep/LLD/assignment/week 2/level1/app.properties");
            List<String> content = Files.readAllLines(path);
            
            for(String row: content) {
                String[] entry = row.split("=");
                secrets.put(entry[0], entry[1]);
            }
        } catch (IOException e) {
            System.out.println("Failed to read file...");
        }
    }

    public static AppConfig getInstance() {
        if(instance == null) {
            synchronized(AppConfig.class) {
                if(instance == null) {
                    instance = new AppConfig();
                }
            }
        }

        return instance;
    }

    public String get(String key) {
        return secrets.getOrDefault(key, null);
    }
}

public class Code_E1 {
    public static void main(String[] args) throws Exception {

        // TODO: Task 1
        AppConfig config = AppConfig.getInstance();
        System.out.println(config.get("db_url"));


        // TODO: Task 2
        for(int i=0;i<10;i++) {
            Thread t = new Thread(() -> {
                AppConfig tmpInstance = AppConfig.getInstance();
                System.out.println(tmpInstance.hashCode());
            });
            t.start();
        }

        // TODO: Task 3
        /**
         * Ans: what will if we remove: 
         *  1. synchronized: in that case two different thread can race to create different instance, if we have instance = null,
         *     and as result, we may see different hashcode, instead of 1. (i.e., multiple objects got created.)
         *  2. volatile: One thread might publish instance before the constructor’s writes are visible to another thread. 
         *     Therefore, another thread could retrieve a reference to an incompletely initialized object or create a second instance.
         */
    }
}
