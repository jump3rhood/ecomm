package org.john.personal.paymentservice.paymentgateways;

import com.stripe.exception.StripeException;

public interface PaymentGateway {
    String initiatePayment(Long orderId, String phoneNumber, double amount) throws StripeException;
}
