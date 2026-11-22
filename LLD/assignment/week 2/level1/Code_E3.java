import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

enum Channel {
    EMAIL,
    SMS,
    PUSH,
    WHATSAPP
}

// TODO: Task 1
interface Notification {
    void send(String to, String message);
}

class EmailNotification implements Notification {
    @Override
    public void send(String to, String message) {
        System.out.printf("EMAIL (%s): %s\n", to, message);   
    }
}
class SMSNotification implements Notification {
    @Override
    public void send(String to, String message) {
        System.out.printf("SMS (%s): %s\n", to, message);   
    }
}
class PushNotification implements Notification {
    @Override
    public void send(String to, String message) {
        System.out.printf("PUSH (%s): %s\n", to, message);   
    }
}

interface NotificationChannel {
    Notification createNotification();
}

class EmailChannel implements NotificationChannel {
    @Override
    public Notification createNotification() {
        return new EmailNotification();
    }
}
class SMSChannel implements NotificationChannel {
    @Override
    public Notification createNotification() {
        return new SMSNotification();
    }
}
class PushChannel implements NotificationChannel {
    @Override
    public Notification createNotification() {
        return new PushNotification();
    }
}

// TODO: Task 3 (i had to edit: Channel enum, Notification factory, so existing file i had to edit.)
class WhatsappNotification implements Notification {
    @Override
    public void send(String to, String message) {
        System.out.printf("WHATSAPP (%s): %s\n", to, message);   
    }
}
class WhatsappChannel implements NotificationChannel {
    @Override
    public Notification createNotification() {
        return new WhatsappNotification();
    }
}


// TODO: Task 2
class NotificationFactory {

    public static NotificationChannel create(Channel c) {
        return switch (c) {
            case SMS -> new SMSChannel();
            case EMAIL -> new EmailChannel();
            case PUSH -> new PushChannel();
            case WHATSAPP -> new WhatsappChannel();
            default -> throw new IllegalArgumentException("invalid type");
        };
    }


    // TODO: Task 4
    /**
     * It improves the Open-Closed Principle (OCP) by allowing new channel implementations to be registered without
     * modifying the factory's creation logic.
     * 
     * Tradeoff: Introduces indirection and runtime mapping errors; unlike an exhaustive switch, the compiler cannot 
     * guarantee every enum value is registered.
     */


    // private static Map<Channel, Supplier<NotificationChannel>> registry = new HashMap<>();

    // static  {
    //     registry.put(Channel.EMAIL, EmailChannel::new);
    //     registry.put(Channel.PUSH, PushChannel::new);
    //     registry.put(Channel.SMS, SMSChannel::new);
    //     registry.put(Channel.WHATSAPP, WhatsappChannel::new);
    // }

    // public static NotificationChannel create(Channel c) {
    //     Supplier<NotificationChannel> supplier = registry.get(c);
    //     if (supplier == null) throw new IllegalArgumentException("Unsupported channel: " + c);
    //     return supplier.get();
    // }
}

public class Code_E3 {
    public static void main(String[] args) {
        NotificationChannel channel = NotificationFactory.create(Channel.WHATSAPP);
        Notification notification = channel.createNotification();
        notification.send("test", "hello world");
    }
}
