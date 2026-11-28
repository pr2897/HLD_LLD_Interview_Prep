import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class CredentialManager {
    private static CredentialManager instance;

    private CredentialManager() { }

    public static CredentialManager getInstance() {
        if(instance == null) {
            synchronized(CredentialManager.class) {
                if(instance == null) instance = new CredentialManager();
            }
        }

        return instance;
    }
}

public class Code_H2 {
    public static void main(String[] args) throws Exception {
        List<Thread> threads = new ArrayList<>();
        Set<CredentialManager> unique = Collections.synchronizedSet(new HashSet<>());
        
        for(int i=0;i<100;i++) {
            threads.add(new Thread(() -> {
                var instance = CredentialManager.getInstance();
                unique.add(instance);
            }));
        }

        // Start all the threads
        for(Thread t: threads) t.start();

        // wait for all the threads
        for(Thread t: threads) t.join();

        System.out.println("Hello world");
        System.out.println(unique.size());

        
        
    
        System.out.println(CredentialManager.class.getDeclaredConstructor().newInstance());
    }
}
