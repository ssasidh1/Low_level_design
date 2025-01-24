public abstract class Payment {
    protected double amount;
    protected PaymentStatus status;

    public final void processPayment() {
        initiatePayment();
        confirmPayment();
        updatePaymentStatus();
    }

    protected abstract void initiatePayment();
    private void confirmPayment() {
        System.out.println("Payment confirmed.");
    }

    private void updatePaymentStatus() {
        this.status = PaymentStatus.COMPLETED;
        System.out.println("Payment status updated to COMPLETED.");
    }
}


public class CreditCardPayment extends Payment {
    @Override
    protected void initiatePayment() {
        System.out.println("Processing credit card payment of amount: " + amount);
    }
}


// main
Payment payment = new CreditCardPayment(); 
payment.processPayment();
