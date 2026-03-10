package port.sm.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import port.sm.erp.entity.MaterialOrder;

import java.util.Optional;

public interface MaterialOrderRepository extends JpaRepository<MaterialOrder, Long> {
    Optional<MaterialOrder> findByOrderNo(String orderNo);
}