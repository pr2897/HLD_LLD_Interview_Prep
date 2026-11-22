class ConfigurationManager {
    private static volatile ConfigurationManager instance;

    private ConfigurationManager() {}

    public static ConfigurationManager getInstance() {
        if (instance == null) {

            synchronized(ConfigurationManager.class) {
                if (instance == null) { 
                    System.out.println("Creating instance");
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }
}

public class _1_Singleton {
    public static void main(String[] args) {
        
        for(int i=0;i<100;i++) {
            Thread t = new Thread(() -> {
                ConfigurationManager.getInstance();
            });
            t.start();
        }
    }
}