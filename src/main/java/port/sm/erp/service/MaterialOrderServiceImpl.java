package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.MaterialOrderLineDto;
import port.sm.erp.dto.MaterialOrderRequestDto;
import port.sm.erp.dto.MaterialOrderResponseDto;
import port.sm.erp.entity.MaterialOrder;
import port.sm.erp.entity.MaterialOrderLine;
import port.sm.erp.repository.MaterialOrderRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialOrderServiceImpl implements MaterialOrderService {

    private static final String DELETED = "DELETED";
    private static final String DEFAULT_STATUS = "발주요청";

    private final MaterialOrderRepository materialOrderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MaterialOrderResponseDto> findAll() {
        return materialOrderRepository.findByStatusNotOrderByIdDesc(DELETED)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MaterialOrderResponseDto findById(Long id) {
        MaterialOrder materialOrder = getActiveEntity(id);
        return toResponseDto(materialOrder);
    }

    @Override
    public MaterialOrderResponseDto create(MaterialOrderRequestDto requestDto) {
        normalizeAndValidate(requestDto, null);

        String orderNo = normalizeOrderNo(requestDto.getOrderNo());

        if (orderNo == null || orderNo.isBlank()) {
            orderNo = generateOrderNo();
        }

        if (materialOrderRepository.existsByOrderNo(orderNo)) {
            throw new IllegalArgumentException("이미 존재하는 발주번호입니다. orderNo=" + orderNo);
        }

        MaterialOrder materialOrder = MaterialOrder.builder()
                .orderNo(orderNo)
                .orderDate(requestDto.getOrderDate())
                .customerId(requestDto.getCustomerId())
                .customerName(blankToNull(requestDto.getCustomerName()))
                .remark(blankToNull(requestDto.getRemark()))
                .status(normalizeStatus(requestDto.getStatus()))
                .totalAmount(calculateTotalAmount(requestDto))
                .build();

        applyLines(materialOrder, requestDto.getLines());

        MaterialOrder saved = materialOrderRepository.save(materialOrder);
        return toResponseDto(saved);
    }

    @Override
    public MaterialOrderResponseDto update(Long id, MaterialOrderRequestDto requestDto) {
        normalizeAndValidate(requestDto, id);

        MaterialOrder materialOrder = getActiveEntity(id);

        String orderNo = normalizeOrderNo(requestDto.getOrderNo());
        if (orderNo == null || orderNo.isBlank()) {
            orderNo = materialOrder.getOrderNo();
        }

        if (materialOrderRepository.existsByOrderNoAndIdNot(orderNo, id)) {
            throw new IllegalArgumentException("이미 존재하는 발주번호입니다. orderNo=" + orderNo);
        }

        materialOrder.setOrderNo(orderNo);
        materialOrder.setOrderDate(requestDto.getOrderDate());
        materialOrder.setCustomerId(requestDto.getCustomerId());
        materialOrder.setCustomerName(blankToNull(requestDto.getCustomerName()));
        materialOrder.setRemark(blankToNull(requestDto.getRemark()));
        materialOrder.setStatus(normalizeStatus(requestDto.getStatus()));
        materialOrder.setTotalAmount(calculateTotalAmount(requestDto));

        materialOrder.clearLines();
        applyLines(materialOrder, requestDto.getLines());

        return toResponseDto(materialOrder);
    }

    @Override
    public void delete(Long id) {
        MaterialOrder materialOrder = getActiveEntity(id);
        materialOrder.setStatus(DELETED);
    }

    private MaterialOrder getActiveEntity(Long id) {
        return materialOrderRepository.findByIdAndStatusNot(id, DELETED)
                .orElseThrow(() -> new IllegalArgumentException("해당 발주가 없습니다. id=" + id));
    }

    private void normalizeAndValidate(MaterialOrderRequestDto requestDto, Long id) {
        if (requestDto.getOrderDate() == null) {
            throw new IllegalArgumentException("발주일자는 필수입니다.");
        }

        if (requestDto.getCustomerId() == null) {
            throw new IllegalArgumentException("공급업체는 필수입니다.");
        }

        if (requestDto.getLines() == null || requestDto.getLines().isEmpty()) {
            throw new IllegalArgumentException("자재목록은 최소 1개 이상이어야 합니다.");
        }

        for (int i = 0; i < requestDto.getLines().size(); i++) {
            MaterialOrderLineDto line = requestDto.getLines().get(i);

            if (line.getItemName() == null || line.getItemName().isBlank()) {
                throw new IllegalArgumentException((i + 1) + "번째 자재명이 비어 있습니다.");
            }

            if (line.getQty() == null || line.getQty() < 0) {
                line.setQty(0);
            }

            if (line.getPrice() == null || line.getPrice() < 0) {
                line.setPrice(0);
            }

            line.setAmount(line.getQty() * line.getPrice());
            line.setRemark(blankToNull(line.getRemark()));
        }

        requestDto.setOrderNo(normalizeOrderNo(requestDto.getOrderNo()));
        requestDto.setCustomerName(blankToNull(requestDto.getCustomerName()));
        requestDto.setRemark(blankToNull(requestDto.getRemark()));
        requestDto.setStatus(normalizeStatus(requestDto.getStatus()));
        requestDto.setTotalAmount(calculateTotalAmount(requestDto));
    }

    private void applyLines(MaterialOrder materialOrder, List<MaterialOrderLineDto> lines) {
        if (lines == null || lines.isEmpty()) {
            return;
        }

        for (MaterialOrderLineDto lineDto : lines) {
            materialOrder.addLine(toEntity(lineDto));
        }
    }

    private Integer calculateTotalAmount(MaterialOrderRequestDto requestDto) {
        if (requestDto.getLines() == null) {
            return 0;
        }

        return requestDto.getLines().stream()
                .map(line -> {
                    int qty = line.getQty() == null ? 0 : line.getQty();
                    int price = line.getPrice() == null ? 0 : line.getPrice();
                    return qty * price;
                })
                .reduce(0, Integer::sum);
    }

    private MaterialOrderLine toEntity(MaterialOrderLineDto dto) {
        int qty = dto.getQty() == null ? 0 : dto.getQty();
        int price = dto.getPrice() == null ? 0 : dto.getPrice();

        return MaterialOrderLine.builder()
                .itemId(dto.getItemId())
                .itemName(dto.getItemName().trim())
                .qty(qty)
                .price(price)
                .amount(qty * price)
                .remark(blankToNull(dto.getRemark()))
                .build();
    }

    private MaterialOrderResponseDto toResponseDto(MaterialOrder entity) {
        List<MaterialOrderLineDto> lineDtos = entity.getLines().stream()
                .map(line -> MaterialOrderLineDto.builder()
                        .id(line.getId())
                        .itemId(line.getItemId())
                        .itemName(line.getItemName())
                        .qty(line.getQty())
                        .price(line.getPrice())
                        .amount(line.getAmount())
                        .remark(line.getRemark())
                        .build())
                .toList();

        return MaterialOrderResponseDto.builder()
                .id(entity.getId())
                .orderNo(entity.getOrderNo())
                .orderDate(entity.getOrderDate())
                .customerId(entity.getCustomerId())
                .customerName(entity.getCustomerName())
                .remark(entity.getRemark())
                .status(entity.getStatus())
                .totalAmount(entity.getTotalAmount())
                .lines(lineDtos)
                .build();
    }

    private String normalizeOrderNo(String orderNo) {
        if (orderNo == null) return null;
        String value = orderNo.trim();
        return value.isBlank() ? null : value;
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return DEFAULT_STATUS;
        }
        return status.trim();
    }

    private String blankToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isBlank() ? null : trimmed;
    }

    private String generateOrderNo() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        long millis = System.currentTimeMillis() % 100000;
        return String.format("MO-%s-%05d", date, millis);
    }
}