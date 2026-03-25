package port.sm.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import port.sm.erp.entity.OrderProgress;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderProgressRepository extends JpaRepository<OrderProgress, Long> {

    Optional<OrderProgress> findByIdAndStatus(Long id, String status);

    Optional<OrderProgress> findByOrderNoAndStatus(String orderNo, String status);

    List<OrderProgress> findByOrderNameContainingAndStatus(String keyword, String status);

    Page<OrderProgress> findByOrderNameContainingAndStatus(String keyword, String status, Pageable pageable);

    List<OrderProgress> findByProgressTextAndStatus(String progressText, String status);

    List<OrderProgress> findByMember_IdAndStatus(Long memberId, String status);

    List<OrderProgress> findByStatus(String status);
}