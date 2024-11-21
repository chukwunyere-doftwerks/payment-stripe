package payment.service;

import com.stripe.model.PaymentIntent;
import payment.model.Payment;

import java.math.BigDecimal;

public interface PaymentService {
    PaymentIntent createPaymentIntent(BigDecimal amount, String currency);
    Payment processPayment(String paymentIntentId, String paymentMethodId);
}