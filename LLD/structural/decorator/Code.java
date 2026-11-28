interface IPayment {
    void makePayment();
}

class CardPayment implements IPayment {
    @Override
    public void makePayment() {
        System.out.println("Making card payment");
    }
}

abstract class PaymentDecorator implements IPayment {
    protected IPayment wrapped;

    public PaymentDecorator(IPayment payment) {
        this.wrapped = payment;
    }
}

class RetryPaymentDecorator extends PaymentDecorator {
    public RetryPaymentDecorator(IPayment payment) {
        super(payment);
    }

    public void makePayment() {
        System.out.println("Retrying payment...");
        wrapped.makePayment();
    }
}

interface IRateLimiter {
    void limit();
}
class LeakyBucketImpl implements IRateLimiter {
    @Override
    public void limit() {
        System.out.println("Rate limiting usering leaky bucket algo..");    
    }
}

class RateLimitingEnabledPaymentDecorator extends PaymentDecorator {
    private IRateLimiter rateLimiter;

    public RateLimitingEnabledPaymentDecorator(IPayment payment, IRateLimiter limiter) {
        super(payment);
        this.rateLimiter = limiter;
    }

    public void makePayment() {
        rateLimiter.limit();
        wrapped.makePayment();
    }
}


public class Code {

    public static void main(String[] args) {
        PaymentService service = new PaymentService(new RetryPaymentDecorator(new CardPayment()));
        service.makePayment();

        service = new PaymentService(new RateLimitingEnabledPaymentDecorator(new RetryPaymentDecorator(new CardPayment()), new LeakyBucketImpl()));
        service.makePayment();
    }
}

class PaymentService {
    private IPayment payment;
    
    public PaymentService(IPayment payment) {
        this.payment = payment;
    }

    public void makePayment() {
        payment.makePayment();
    }
}