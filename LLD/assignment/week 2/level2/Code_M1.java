import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

record Order(String orderId, double amount, String owner) { }

interface PaymentGateway {
    void pay(Order o);
    void refund(String txnId);
}
class RazorpayPaymentGateway implements PaymentGateway {
    public RazorpayPaymentGateway() {
        System.out.println("[⚙️ Razorpay]");
    }
    @Override
    public void pay(Order o) {
        System.out.printf("[Razorpay] paid: %.2f to %s \n", o.amount(), o.owner());
    }

    @Override
    public void refund(String txnId) {
        System.out.printf("[Razorpay] refund intitiated: for txnId: %s\n", txnId);
    }
}
class StripePaymentGateway implements PaymentGateway {
    public StripePaymentGateway() {
        System.out.println("[⚙️ Stripe]");
    }
    @Override
    public void pay(Order o) {
        System.out.printf("[Stripe] paid: %.2f to %s \n", o.amount(), o.owner());
    }

    @Override
    public void refund(String txnId) {
        System.out.printf("[Stripe] refund intitiated: for txnId: %s\n", txnId);
    }
}
class PayPalPaymentGateway implements PaymentGateway {
    public PayPalPaymentGateway() {
        System.out.println("[⚙️ PayPal]");
    }
    @Override
    public void pay(Order o) {
        System.out.printf("[PayPal] paid: %.2f to %s \n", o.amount(), o.owner());
    }

    @Override
    public void refund(String txnId) {
        System.out.printf("[PayPal] refund intitiated: for txnId: %s\n", txnId);
    }
}

class PaymentGatewayFactory {
    private static  volatile Map<String, PaymentGateway> gatewayMapping = new ConcurrentHashMap<>();
    private static volatile AtomicInteger count = new AtomicInteger();

    public static PaymentGateway forCurrency(String currency) {
        if(gatewayMapping.containsKey(currency)) return gatewayMapping.get(currency);
        else {
            synchronized(PaymentGatewayFactory.class) {
                if(gatewayMapping.containsKey(currency)) return gatewayMapping.get(currency);

                PaymentGateway gateway = createInstance(currency);
                count.incrementAndGet();
                gatewayMapping.put(currency, gateway);
                return gateway;
            }
        }
    }

    public static void getCount() {
        System.out.println("Total instance created: " + count.get());
    }

    private static PaymentGateway createInstance(String currency) {
        return switch(currency) {
            case "INR" -> new RazorpayPaymentGateway();
            case "USD" -> new StripePaymentGateway();
            case "EUR" -> new PayPalPaymentGateway();
            default -> throw new IllegalArgumentException("currency not supported yet!!");
        };
    }
}


public class Code_M1 {
    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for(int i=0;i<1000;i++) {
            String[] currency = {"INR", "USD", "EUR"};
            String orderId = String.format("Order_%d", i);
            Thread t = new Thread(() -> {
                int idx = new Random().nextInt(0, 3);
                PaymentGateway gateway = PaymentGatewayFactory.forCurrency(currency[idx]);
                gateway.pay(new Order(orderId, new Random().nextDouble()*3000, "Piyush"));
            });
            
            threads.add(t);
        }
        for(Thread t: threads) t.start(); // start all threads.
        for(Thread t: threads) t.join(); // wait for all the threads to complete

        PaymentGatewayFactory.getCount();
    }
}