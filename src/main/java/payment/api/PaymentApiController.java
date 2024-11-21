package payment.api;

import com.stripe.model.PaymentIntent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import payment.model.Payment;
import payment.request.PaymentRequest;
import payment.request.ProcessPaymentRequest;
import payment.response.CustomResponse;
import payment.service.PaymentService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment/")
public class PaymentApiController {
    private final PaymentService paymentService;

    public PaymentApiController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-payment-intent")
    public CustomResponse<?> createPaymentIntent(@RequestBody PaymentRequest request) {
        try {
            PaymentIntent paymentIntent = paymentService.createPaymentIntent(request.getAmount(), request.getCurrency());
            Map<String, Object> response = new HashMap<>();
            response.put("paymentIntentId", paymentIntent.getId());
            response.put("clientSecret", paymentIntent.getClientSecret());
            return new CustomResponse<>(CustomResponse.ResponseStatus.SUCCESS, "Payment Intent Generated", response);
        } catch (Exception e) {
            return new CustomResponse<>(CustomResponse.ResponseStatus.ERROR, e.getMessage());
        }
    }

    @PostMapping("/process-payment")
    public CustomResponse<?> processPayment(@RequestBody ProcessPaymentRequest request) {
        try {
            Payment payment = paymentService.processPayment(request.getPaymentIntentId(), request.getPaymentMethodId());
            return new CustomResponse<>(CustomResponse.ResponseStatus.SUCCESS, "Payment Successful", payment);
        } catch (Exception e) {
            return new CustomResponse<>(CustomResponse.ResponseStatus.ERROR, e.getMessage());
        }
    }
}