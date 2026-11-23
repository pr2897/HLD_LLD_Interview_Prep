interface Main { void serve(); }
interface Side { void serve(); }
interface Drink { void serve(); }

class Biryani implements Main {
    @Override
    public void serve() {
        System.out.println("Serving Biryani...");
    }
}
class Pasta implements Main {
    @Override
    public void serve() {
        System.out.println("Serving Pasta...");
    }
}
class Raita implements Side {
    @Override
    public void serve() {
        System.out.println("Serving Raita...");
    }
}
class GarlicBread implements Side {
    @Override
    public void serve() {
        System.out.println("Serving GarlicBread...");
    }
}
class Lassi implements Drink {
    @Override
    public void serve() {
        System.out.println("Serving Lassi...");
    }
}
class Lemonade implements Drink {
    @Override
    public void serve() {
        System.out.println("Serving Lemonade...");
    }
}


class MealOrder {
    private CuisineFactory cuisine;
    private Main main;
    private Side side;
    private Drink drink;

    private int spiceLevel;
    private boolean extraCheese;
    private boolean noOnion;
    private String couponCode;

    private MealOrder(Builder b) {
        main = b.cuisine.createMain();
        side = b.cuisine.createSide();
        drink = b.cuisine.createDrink();

        spiceLevel = b.spiceLevel;
        extraCheese = b.extraCheese;
        noOnion = b.noOnion;
        couponCode = b.couponCode;
    }

    public void serve() {
        main.serve();
        side.serve();
        drink.serve();
        System.out.printf("Personalisation: [spiceLevel: %d], [extraCheese: %b], [noOnion: %b], [couponCode: %s]\n", spiceLevel, extraCheese, noOnion, couponCode);
    }

    
    public static class Builder {
        private CuisineFactory cuisine;
        private int spiceLevel;
        private boolean extraCheese;
        private boolean noOnion;
        private String couponCode;

        public Builder(CuisineFactory cuisine) {
            this.cuisine = cuisine;
        }
        public Builder spiceLevel(int level) {
            this.spiceLevel = level;
            return this;
        }
        public Builder extraCheese(boolean extraCheese) {
            this.extraCheese = extraCheese;
            return this;
        }
        public Builder noOnion(boolean noOnion) {
            this.noOnion = noOnion;
            return this;
        }
        public Builder couponCode(String code) {
            this.couponCode = code;
            return this;
        }

        public MealOrder build() {
            if(!cuisine.isValid(this)) throw new IllegalStateException("invalid configuration for combo");
            return new MealOrder(this);
        }
        
        public int getSpiceLevel() {
            return spiceLevel;
        }
        public boolean isExtraCheese() {
            return extraCheese;
        }
        public boolean isNoOnion() {
            return noOnion;
        }
        public String getCouponCode() {
            return couponCode;
        }
    }

}


interface CuisineFactory {
    Main createMain();
    Side createSide();
    Drink createDrink();
    boolean isValid(MealOrder.Builder meal);
}

class IndianCuisineFactory implements CuisineFactory {

    @Override public Drink createDrink() { return new Lassi(); }

    @Override public Main createMain() { return new Biryani(); }

    @Override public Side createSide() { return new Raita();  }

    @Override
    public boolean isValid(MealOrder.Builder order) {
        return !order.isExtraCheese();
    }    
}

class ItalianCuisineFactory implements CuisineFactory {

    @Override public Drink createDrink() { return new Lemonade(); }

    @Override public Main createMain() { return new Pasta(); }

    @Override public Side createSide() { return new GarlicBread();  }

    @Override
    public boolean isValid(MealOrder.Builder order) {
        return order.getSpiceLevel() <= 0;
    }    
}

class ChineeseCuisineFactory implements CuisineFactory {

    @Override
    public Drink createDrink() {
        return new Lemonade();
    }

    @Override
    public Main createMain() {
        return new Biryani();
    }

    @Override
    public Side createSide() {
        return new GarlicBread();
    }

    @Override
    public boolean isValid(MealOrder.Builder meal) {
        return meal.isExtraCheese() == true && meal.isNoOnion();
    }
}

public class Code_M3 {
    public static void main(String[] args) {
        // CuisineFactory indianCuisine = new IndianCuisineFactory();
        // CuisineFactory chineeseCuisine = new ChineeseCuisineFactory();
        CuisineFactory italianCuisine = new ItalianCuisineFactory();
        MealOrder order = new MealOrder.Builder(italianCuisine)
                        .extraCheese(true)
                        .noOnion(true)
                        .build();
        order.serve();
    }   
}
