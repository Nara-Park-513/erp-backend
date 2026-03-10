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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialOrderServiceImpl implements MaterialOrderService {

    private final MaterialOrderRepository materialOrderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MaterialOrderResponseDto> findAll() {
        return materialOrderRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MaterialOrderResponseDto findById(Long id) {
        MaterialOrder materialOrder = materialOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 발주가 없습니다. id=" + id));
        return toResponseDto(materialOrder);
    }

    @Override
    public MaterialOrderResponseDto create(MaterialOrderRequestDto requestDto) {
        validate(requestDto);

        String orderNo = requestDto.getOrderNo();
        if (materialOrderRepository.findByOrderNo(orderNo).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 발주번호입니다. orderNo=" + orderNo);
        }

        MaterialOrder materialOrder = MaterialOrder.builder()
                .orderNo(requestDto.getOrderNo())
                .orderDate(requestDto.getOrderDate())
                .customerId(requestDto.getCustomerId())
                .customerName(requestDto.getCustomerName())
                .remark(requestDto.getRemark())
                .status(requestDto.getStatus())
                .totalAmount(calculateTotalAmount(requestDto))
                .build();

        if (requestDto.getLines() != null) {
            for (MaterialOrderLineDto lineDto : requestDto.getLines()) {
                materialOrder.addLine(toEntity(lineDto));
            }
        }

        MaterialOrder saved = materialOrderRepository.save(materialOrder);
        return toResponseDto(saved);
    }

    @Override
    public MaterialOrderResponseDto update(Long id, MaterialOrderRequestDto requestDto) {
        validate(requestDto);

        MaterialOrder materialOrder = materialOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 발주가 없습니다. id=" + id));

        materialOrder.setOrderNo(requestDto.getOrderNo());
        materialOrder.setOrderDate(requestDto.getOrderDate());
        materialOrder.setCustomerId(requestDto.getCustomerId());
        materialOrder.setCustomerName(requestDto.getCustomerName());
        materialOrder.setRemark(requestDto.getRemark());
        materialOrder.setStatus(requestDto.getStatus());
        materialOrder.setTotalAmount(calculateTotalAmount(requestDto));

        materialOrder.clearLines();
        if (requestDto.getLines() != null) {
            for (MaterialOrderLineDto lineDto : requestDto.getLines()) {
                materialOrder.addLine(toEntity(lineDto));
            }
        }

        return toResponseDto(materialOrder);
    }

    @Override
    public void delete(Long id) {
        MaterialOrder materialOrder = materialOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 발주가 없습니다. id=" + id));
        materialOrderRepository.delete(materialOrder);
    }

    private void validate(MaterialOrderRequestDto requestDto) {
        if (requestDto.getOrderNo() == null || requestDto.getOrderNo().isBlank()) {
            throw new IllegalArgumentException("발주번호는 필수입니다.");
        }

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

            if (line.getQty() == null) {
                line.setQty(1);
            }

            if (line.getPrice() == null) {
                line.setPrice(0);
            }

            line.setAmount((line.getQty() == null ? 0 : line.getQty()) * (line.getPrice() == null ? 0 : line.getPrice()));
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
                .itemName(dto.getItemName())
                .qty(qty)
                .price(price)
                .amount(qty * price)
                .remark(dto.getRemark())
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
}