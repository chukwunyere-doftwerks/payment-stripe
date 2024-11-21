package payment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDto {
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
}