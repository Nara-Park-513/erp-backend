package port.sm.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import port.sm.erp.entity.MaterialOrder;

import java.util.List;
import java.util.Optional;

public interface MaterialOrderRepository extends JpaRepository<MaterialOrder, Long> {

    Optional<MaterialOrder> findByOrderNo(String orderNo);

    Optional<MaterialOrder> findByIdAndStatusNot(Long id, String status);

    List<MaterialOrder> findByStatusNotOrderByIdDesc(String status);

    boolean existsByOrderNo(String orderNo);

    boolean existsByOrderNoAndIdNot(String orderNo, Long id);
}