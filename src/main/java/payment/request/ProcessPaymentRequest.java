package payment.request;

import lombok.Data;

@Data
public class ProcessPaymentRequest {
    private String paymentIntentId;
    private String paymentMethodId;
}