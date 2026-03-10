package port.sm.erp.service;

import port.sm.erp.dto.MaterialOrderRequestDto;
import port.sm.erp.dto.MaterialOrderResponseDto;

import java.util.List;

public interface MaterialOrderService {

    List<MaterialOrderResponseDto> findAll();

    MaterialOrderResponseDto findById(Long id);

    MaterialOrderResponseDto create(MaterialOrderRequestDto requestDto);

    MaterialOrderResponseDto update(Long id, MaterialOrderRequestDto requestDto);

    void delete(Long id);
}