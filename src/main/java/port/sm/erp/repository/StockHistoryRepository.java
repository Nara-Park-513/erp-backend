package port.sm.erp.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import port.sm.erp.entity.StockHistory;

import java.util.List;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {

    List<StockHistory> findByItemCodeContainingIgnoreCaseOrItemNameContainingIgnoreCaseOrRefNoContainingIgnoreCaseOrChangeTypeContainingIgnoreCase(
            String itemCode,
            String itemName,
            String refNo,
            String changeType,
            Sort sort
    );
}