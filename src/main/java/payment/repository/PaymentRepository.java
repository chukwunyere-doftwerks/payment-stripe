package payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import payment.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT CASE WHEN COUNT (p) > 0 THEN true ELSE false END FROM Payment p WHERE p.stripePaymentId = :paymentStripeId")
    boolean existsByPaymentStripeId(String paymentStripeId);
}