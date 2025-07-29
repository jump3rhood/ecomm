package org.john.personal.paymentservice.paymentgateways;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Qualifier("stripe")
public class StripePaymentGateway implements PaymentGateway {
    @Value("${stripe.key}")
    private String apiKey;

    @Override
    public String initiatePayment(Long orderId, String phoneNumber, double amount) throws StripeException {
        Stripe.apiKey = apiKey;

        ProductCreateParams productCreateParams =
                ProductCreateParams.builder().setName("Cool Hoodie").build();
        Product product = Product.create(productCreateParams);

        PriceCreateParams priceCreateParams =
                PriceCreateParams.builder()
                        .setCurrency("inr")
                        .setUnitAmount(100*1000L)
                        .setProduct(product.getId())
                        .build();

        Price price = Price.create(priceCreateParams);
        PaymentLinkCreateParams params =
                PaymentLinkCreateParams.builder()
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(price.getId())
                                        .setQuantity(1L)
                                        .build()
                        ).setAfterCompletion(
                                PaymentLinkCreateParams.AfterCompletion.builder()
                                        .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                        .setRedirect(
                                                PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                        .setUrl("http://localhost:8082/payments/success?orderId="+orderId)
                                                        .build()
                        ).build())
                        .build();

        PaymentLink paymentLink = PaymentLink.create(params);
        return paymentLink.getUrl();
    }
}
