package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.InventoryStockRequest;
import port.sm.erp.dto.InventoryStockResponse;
import port.sm.erp.entity.InventoryStock;
import port.sm.erp.entity.Item;
import port.sm.erp.repository.InventoryStockRepository;
import port.sm.erp.repository.ItemRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryStockService {

    private final InventoryStockRepository stockRepository;
    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public Page<InventoryStockResponse> list(Pageable pageable, String q) {
        Page<InventoryStock> page =
                (q == null || q.trim().isEmpty())
                        ? stockRepository.findAll(pageable)
                        : stockRepository.search(q.trim(), pageable);

        return page.map(InventoryStockService::toResponse);
    }

    @Transactional(readOnly = true)
    public InventoryStockResponse get(Long id) {
        InventoryStock stock = stockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("재고 없음: " + id));

        return toResponse(stock);
    }

    public InventoryStockResponse create(InventoryStockRequest req) {
        if (req.getItemId() == null) {
            throw new IllegalArgumentException("itemId는 필수입니다.");
        }

        Item item = itemRepository.findById(req.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("품목 없음: " + req.getItemId()));

        if (stockRepository.existsByItem_Id(req.getItemId())) {
            throw new IllegalStateException("이미 재고가 등록된 품목입니다. itemId=" + req.getItemId());
        }

        InventoryStock stock = InventoryStock.builder()
                .item(item)
                .onHandQty(nvl(req.getOnHandQty()))
                .reservedQty(nvl(req.getReservedQty()))
                .safetyQty(nvl(req.getSafetyQty()))
                .build();

        InventoryStock saved = stockRepository.save(stock);
        return toResponse(saved);
    }

    public InventoryStockResponse update(Long id, InventoryStockRequest req) {
        InventoryStock stock = stockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("재고 없음: " + id));

        if (req.getItemId() == null) {
            throw new IllegalArgumentException("itemId는 필수입니다.");
        }

        if (!stock.getItem().getId().equals(req.getItemId())) {
            throw new IllegalArgumentException("재고 수정 시 품목 변경은 허용되지 않습니다.");
        }

        if (req.getOnHandQty() != null) {
            stock.setOnHandQty(nvl(req.getOnHandQty()));
        }

        if (req.getReservedQty() != null) {
            stock.setReservedQty(nvl(req.getReservedQty()));
        }

        if (req.getSafetyQty() != null) {
            stock.setSafetyQty(nvl(req.getSafetyQty()));
        }

        return toResponse(stock);
    }

    public void delete(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new IllegalArgumentException("재고 없음: " + id);
        }
        stockRepository.deleteById(id);
    }

    public static InventoryStockResponse toResponse(InventoryStock stock) {
        return InventoryStockResponse.builder()
                .id(stock.getId())
                .itemId(stock.getItem().getId())
                .itemCode(stock.getItem().getItemCode())
                .itemName(stock.getItem().getItemName())
                .onHandQty(stock.getOnHandQty())
                .reservedQty(stock.getReservedQty())
                .availableQty(stock.getAvailableQty())
                .safetyQty(stock.getSafetyQty())
                .lastMovedAt(stock.getLastMovedAt())
                .createdAt(stock.getCreatedAt())
                .updatedAt(stock.getUpdatedAt())
                .build();
    }

    private static long nvl(Long value) {
        return value == null ? 0L : value;
    }
}