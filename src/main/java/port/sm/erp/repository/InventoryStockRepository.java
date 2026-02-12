package port.sm.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import port.sm.erp.entity.InventoryStock;

public interface InventoryStockRepository extends JpaRepository<InventoryStock, Long> {
    boolean existsByItem_Id(Long itemId);

    @Query("""
            select s
            from InventoryStock s
            join s.item i
            where lower(i.itemCode) like lower(concat('%', :q, '%'))
            or lower(i.itemName) like lower(concat('%', :q, '%'))
            """)
    Page<InventoryStock> search(@Param("q") String q, Pageable pageable);
}
