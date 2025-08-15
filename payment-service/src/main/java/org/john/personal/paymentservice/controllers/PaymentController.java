package org.john.personal.paymentservice.controllers;

import org.john.personal.paymentservice.services.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initiatePayment/{orderId}")
    public String initiatePayment(@PathVariable Long orderId) {
        System.out.println("Reached=========");
        return paymentService.initiatePayment(orderId);
    }

    @GetMapping("/success")
    public String paymentIsSuccessful(@RequestParam Long orderId){
        return "Successfully completed to your order with id " + orderId;
    }
}
