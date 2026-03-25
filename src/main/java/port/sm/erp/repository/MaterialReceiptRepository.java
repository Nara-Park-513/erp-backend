package port.sm.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import port.sm.erp.entity.MaterialReceipt;

public interface MaterialReceiptRepository extends JpaRepository<MaterialReceipt, Long> {
}