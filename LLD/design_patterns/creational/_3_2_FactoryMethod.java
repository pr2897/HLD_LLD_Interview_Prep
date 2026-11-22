
// 1. Product interface
interface Notification {
    void send(String msg);
}


// 2. Concrete Products.
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


/** 
 * Factory method pattern: instead of one factory deciding everything, each product gets its own factory.  
 * */


// Product Factory interface
interface NotificationFactory {
    Notification createNotification();
}

// Factory implementation
class EmailNotificationFactory implements NotificationFactory {
    @Override
    public Notification createNotification() {
        return new EmailNotification();
    }
}
class SMSNotificationFactory implements NotificationFactory {
    @Override
    public Notification createNotification() {
        return new SMSNotification();
    }
}


// 4. Service
class NotificationService {
    public void notifyUser(NotificationFactory factory, String msg) {
        Notification notification = factory.createNotification();
        notification.send(msg);
    }
}

public class _3_2_FactoryMethod {
    public static void main(String[] args) {
        NotificationService service = new NotificationService();
        service.notifyUser("EMAIL", "hello world");
        service.notifyUser("SMS", "hello world");
        service.notifyUser("FAX", "hello world");
    }
}
