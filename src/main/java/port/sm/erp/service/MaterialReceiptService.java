package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.MaterialReceiptDto;
import port.sm.erp.entity.MaterialReceipt;
import port.sm.erp.repository.MaterialReceiptRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialReceiptService {

    private final MaterialReceiptRepository materialReceiptRepository;

    @Transactional(readOnly = true)
    public List<MaterialReceiptDto> findAll() {
        return materialReceiptRepository.findAll()
                .stream()
                .map(MaterialReceiptDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public MaterialReceiptDto findById(Long id) {
        MaterialReceipt entity = materialReceiptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("입고 정보를 찾을 수 없습니다. id=" + id));
        return MaterialReceiptDto.fromEntity(entity);
    }

    public MaterialReceiptDto create(MaterialReceiptDto dto) {
        MaterialReceipt entity = dto.toEntity();

        if (entity.getStatus() == null || entity.getStatus().isBlank()) {
            entity.setStatus("입고대기");
        }

        MaterialReceipt saved = materialReceiptRepository.save(entity);
        return MaterialReceiptDto.fromEntity(saved);
    }

    public MaterialReceiptDto update(Long id, MaterialReceiptDto dto) {
        MaterialReceipt entity = materialReceiptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("입고 정보를 찾을 수 없습니다. id=" + id));

        entity.setReceiptNo(dto.getReceiptNo());
        entity.setReceiptDate(dto.getReceiptDate());
        entity.setOrderNo(dto.getOrderNo());
        entity.setItemName(dto.getItemName());
        entity.setQty(dto.getQty());
        entity.setStatus(dto.getStatus());
        entity.setSupplierId(dto.getSupplierId());
        entity.setSupplierName(dto.getSupplierName());
        entity.setWarehouse(dto.getWarehouse());
        entity.setRemark(dto.getRemark());

        return MaterialReceiptDto.fromEntity(entity);
    }

    public void delete(Long id) {
        if (!materialReceiptRepository.existsById(id)) {
            throw new IllegalArgumentException("입고 정보를 찾을 수 없습니다. id=" + id);
        }
        materialReceiptRepository.deleteById(id);
    }
}