import java.util.*;

interface Notification {
    void send(String msg);
}

class EmailNotification implements Notification {
    @Override
    public void send(String msg) {
        System.out.println("Email: " + msg);
    }
}

class SMSNotification implements Notification {
    @Override 
    public void send(String msg) {
        System.out.println("SMS: "+ msg);
    }
}

// Factory class responsible for creating objects.
/**
 * it breaks the OCP principle, when we try to support new type, we need to modify this, hence a better solution called, FactoryMethod pattern.
 * 
 */
class NotificationFactory {
    // optimization: in case if dont want to repeatly create new objects, type registry (only create in case not present) concept. 
    // private static Map<String, Notification> registry = new HashMap<>();

    public static  Notification createNotification(String type) {
        return switch(type) {
            case "EMAIL" -> new EmailNotification();
            case "SMS" -> new SMSNotification();
            default -> throw new IllegalArgumentException("unsupported type: " + type);
        };
    }
}


// Service
class NotificationService {

    public void notifyUser(String type, String msg) {
        Notification notification = NotificationFactory.createNotification(type);
        notification.send(msg);
    }
}

public class _3_1_Factory {
    public static void main(String[] args) {
        NotificationService service = new NotificationService();
        service.notifyUser("EMAIL", "hello world");
        service.notifyUser("SMS", "hello world");
        service.notifyUser("FAX", "hello world");
    }
}
