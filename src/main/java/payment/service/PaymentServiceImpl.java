package payment.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentUpdateParams;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import payment.model.Payment;
import payment.repository.PaymentRepository;

import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @SneakyThrows
    @Override
    public PaymentIntent createPaymentIntent(BigDecimal amount, String currency) {
        Stripe.apiKey = "sk_test_51QNIEkRqDp6OF8YwXH4Bqnc4uoeX8eIvaAwC6MVIoOJN8rERqXJFk5MDg36PS9oFf2EWxkudND1AtbXpIbnm4qnn00kEoYtiSA";
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount.longValue())
                .setCurrency(currency)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                            .setEnabled(true)
                            .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                            .build()
                )
                .build();

        return PaymentIntent.create(params);
    }

    @SneakyThrows
    @Override
    public Payment processPayment(String paymentIntentId, String paymentMethodId) {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        PaymentIntentUpdateParams params = PaymentIntentUpdateParams.builder()
                .setPaymentMethod(paymentMethodId)
                .build();
        paymentIntent.update(params);

        PaymentIntentConfirmParams confirmParams = PaymentIntentConfirmParams.builder().build();

        boolean existingPaymentStripeId = paymentRepository.existsByPaymentStripeId(paymentIntentId);
        if (existingPaymentStripeId) {
            throw new IllegalArgumentException("Payment already exists, please use a different payment method");
        }
        PaymentIntent confirmIntent = paymentIntent.confirm(confirmParams);

        if (confirmIntent.getStatus().equals("succeeded")) {
            Payment payment = new Payment();
            payment.setStripePaymentId(confirmIntent.getId());
            payment.setAmount(BigDecimal.valueOf(confirmIntent.getAmountReceived()));
            payment.setStatus(confirmIntent.getStatus());
            payment.setCurrency(confirmIntent.getCurrency());
            return paymentRepository.save(payment);
        }
        throw new IllegalArgumentException("Payment not successful!");
    }
}