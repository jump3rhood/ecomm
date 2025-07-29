package org.john.personal.paymentservice.services;

import com.stripe.exception.StripeException;
import org.john.personal.paymentservice.paymentgateways.PaymentGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public final PaymentGateway paymentGateway;

    public PaymentService(@Qualifier("stripe") PaymentGateway paymentGateway){
        this.paymentGateway=paymentGateway;
    }

    public String initiatePayment(Long orderId){
        try {
            // Get Order Details
            // Verify order payment status is pending
            // call gateway to get the link to pay
            String link = paymentGateway.initiatePayment(101L, "132493423", 34.50);
            return link;
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
