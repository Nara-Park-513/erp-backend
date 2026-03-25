package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.StockHistoryDto;
import port.sm.erp.entity.StockHistory;
import port.sm.erp.repository.StockHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockHistoryService {

    private final StockHistoryRepository stockHistoryRepository;

    @Transactional(readOnly = true)
    public List<StockHistoryDto> findAll(String q) {
        Sort sort = Sort.by(Sort.Direction.DESC, "changedAt");

        List<StockHistory> list;

        if (q == null || q.isBlank()) {
            list = stockHistoryRepository.findAll(sort);
        } else {
            list = stockHistoryRepository
                    .findByItemCodeContainingIgnoreCaseOrItemNameContainingIgnoreCaseOrRefNoContainingIgnoreCaseOrChangeTypeContainingIgnoreCase(
                            q, q, q, q, sort
                    );
        }

        return list.stream()
                .map(StockHistoryDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public StockHistoryDto findById(Long id) {
        StockHistory entity = stockHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("재고변동이력을 찾을 수 없습니다. id=" + id));
        return StockHistoryDto.fromEntity(entity);
    }

    public StockHistoryDto create(StockHistoryDto dto) {
        StockHistory entity = dto.toEntity();

        if (entity.getChangedAt() == null) {
            entity.setChangedAt(LocalDateTime.now());
        }

        StockHistory saved = stockHistoryRepository.save(entity);
        return StockHistoryDto.fromEntity(saved);
    }

    public StockHistoryDto update(Long id, StockHistoryDto dto) {
        StockHistory entity = stockHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("재고변동이력을 찾을 수 없습니다. id=" + id));

        entity.setItemId(dto.getItemId());
        entity.setItemCode(dto.getItemCode());
        entity.setItemName(dto.getItemName());
        entity.setChangeType(dto.getChangeType());
        entity.setChangeQty(dto.getChangeQty());
        entity.setBeforeQty(dto.getBeforeQty());
        entity.setAfterQty(dto.getAfterQty());
        entity.setRefNo(dto.getRefNo());
        entity.setRemark(dto.getRemark());
        entity.setChangedAt(dto.getChangedAt() != null ? dto.getChangedAt() : entity.getChangedAt());

        return StockHistoryDto.fromEntity(entity);
    }

    public void delete(Long id) {
        if (!stockHistoryRepository.existsById(id)) {
            throw new IllegalArgumentException("재고변동이력을 찾을 수 없습니다. id=" + id);
        }
        stockHistoryRepository.deleteById(id);
    }
}