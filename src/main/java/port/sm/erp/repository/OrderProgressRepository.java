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

    /** 오더관리번호로 단건 조회 */
    Optional<OrderProgress> findByOrderNo(String orderNo);

    /** 오더명 LIKE 검색 */
    List<OrderProgress> findByOrderNameContaining(String keyword);

    /** 상태값으로 조회 */
    List<OrderProgress> findByStatus(String status);

    /** 작성자 기준 조회 */
    List<OrderProgress> findByMember_Id(Long memberId);

    /** 오더명 LIKE + 페이징 */
    Page<OrderProgress> findByOrderNameContaining(String keyword, Pageable pageable);
}