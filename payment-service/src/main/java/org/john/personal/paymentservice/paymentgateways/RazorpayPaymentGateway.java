package org.john.personal.paymentservice.paymentgateways;

import org.springframework.stereotype.Component;

@Component
public class RazorpayPaymentGateway implements PaymentGateway {
    @Override
    public String initiatePayment(Long orderId, String phoneNumber, double amount) {
        return "razorpay";
    }
}
