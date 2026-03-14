package port.sm.erp.service;

/*NOT_FOUND
BAD_REQUEST
CONFLICT
FORBIDDEN
UNAUTHORIZED*/
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import port.sm.erp.common.Yn;
import port.sm.erp.dto.ItemRequest;
import port.sm.erp.dto.ItemResponse;
import port.sm.erp.entity.Item;
import port.sm.erp.entity.ItemFieldDefinition;
import port.sm.erp.entity.ItemFieldValue;
import port.sm.erp.entity.ItemType;
import port.sm.erp.repository.ItemFieldDefinitionRepository;
import port.sm.erp.repository.ItemFieldValueRepository;
import port.sm.erp.repository.ItemRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepo;
    private final ItemFieldDefinitionRepository defRepo;
    private final ItemFieldValueRepository valRepo;

    public Page<ItemResponse> list(String q, boolean includeStopped, int page, int size, String sortKey, String dir) {
        Pageable pageable = PageRequest.of(page, size, buildSort(sortKey, dir));

        Page<Item> items = itemRepo.search(q, includeStopped, pageable);

        List<Long> ids = items.getContent().stream().map(Item::getId).toList();
        Map<Long, Map<String, String>> extraByItemId = loadExtraFieldsMap(ids);

        return items.map(i -> toResponse(i, extraByItemId.getOrDefault(i.getId(), Collections.emptyMap())));
    }

    public ItemResponse get(Long id) {
        Item item = itemRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "품목이 없습니다. id=" + id));

        Map<Long, Map<String, String>> extraById = loadExtraFieldsMap(List.of(id));

        return toResponse(item, extraById.getOrDefault(id, Collections.emptyMap()));
    }

    @Transactional
    public ItemResponse create(ItemRequest req) {
        validateRequired(req);

        if (itemRepo.existsByItemCode(req.getItemCode())) {
            throw new ResponseStatusException(CONFLICT, "이미 존재 하는 품목 코드입니다:  " + req.getItemCode());
        }

        Item item = new Item();
        applyToEntity(item, req);

        if (item.getItemType() == null) item.setItemType(ItemType.PRODUCT);

        itemRepo.save(item);

        upsertExtraFields(item, req.getExtraFields());

        Map<Long, Map<String, String>> extraById = loadExtraFieldsMap(List.of(item.getId()));

        return toResponse(item, extraById.getOrDefault(item.getId(), Collections.emptyMap()));
    }

    @Transactional
    public ItemResponse update(Long id, ItemRequest req) {
        validateRequiredForUpdate(req);

        Item item = itemRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "품목이 없습니다. id=" + id));

        applyToEntity(item, req);

        valRepo.deleteByItem_Id(id);
        upsertExtraFields(item, req.getExtraFields());

        Map<Long, Map<String, String>> extraById = loadExtraFieldsMap(List.of(id));
        return toResponse(item, extraById.getOrDefault(id, Collections.emptyMap()));
    }

    @Transactional
    public void delete(Long id) {
        Item item = itemRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(NOT_FOUND, "품목이 없습니다. id=" + id));

        // 물리삭제 대신 사용중단 처리
        item.setUseYn("N");
    }

    private void validateRequired(ItemRequest req) {
        if (req.getItemCode() == null || req.getItemCode().isBlank())
            throw new ResponseStatusException(BAD_REQUEST, "품목코드는 필수입니다");
        if (req.getItemName() == null || req.getItemName().isBlank())
            throw new ResponseStatusException(BAD_REQUEST, "품목명은 필수입니다");
        if (req.getItemType() == null)
            throw new ResponseStatusException(BAD_REQUEST, "품목구분은 필수입니다");
    }

    private void validateRequiredForUpdate(ItemRequest req) {
        if (req.getItemName() == null || req.getItemName().isBlank())
            throw new ResponseStatusException(BAD_REQUEST, "품목명은 필수입니다");
        if (req.getItemType() == null)
            throw new ResponseStatusException(BAD_REQUEST, "품목구분은 필수입니다");
    }

    private void applyToEntity(Item item, ItemRequest req) {
        if (item.getId() == null) {
            item.setItemCode(req.getItemCode());
        }

        item.setItemName(req.getItemName());
        item.setItemGroup(req.getItemGroup());
        item.setSpec(req.getSpec());
        item.setSpecMode(req.getSpecMode());
        item.setUnit(req.getUnit());
        item.setBarcode(req.getBarcode());
        item.setProcess(req.getProcess());

        item.setItemType(req.getItemType());

        item.setIsSetYn(Yn.toYn(req.isSet()));
        item.setInPrice(req.getInPrice());
        item.setInVatIncludedYn(Yn.toYn(req.isInVatIncluded()));

        item.setOutPrice(req.getOutPrice());
        item.setOutVatIncludedYn(Yn.toYn(req.isOutVatIncluded()));

        item.setImageUrl(req.getImageUrl());
        item.setUseYn(Yn.toYn(req.isUseYn()));
        if (item.getId() == null && "N".equals(item.getUseYn())) {
            item.setUseYn("Y");
        }
    }

    private ItemResponse toResponse(Item item, Map<String, String> extraFields) {
        return ItemResponse.builder()
                .id(item.getId())
                .itemCode(item.getItemCode())
                .itemName(item.getItemName())
                .itemGroup(item.getItemGroup())
                .spec(item.getSpec())
                .specMode(item.getSpecMode())
                .unit(item.getUnit())
                .barcode(item.getBarcode())
                .process(item.getProcess())
                .itemType(item.getItemType())
                .isSet(Yn.toBool(item.getIsSetYn()))
                .inPrice(item.getInPrice())
                .inVatIncluded(Yn.toBool(item.getInVatIncludedYn()))
                .outPrice(item.getOutPrice())
                .outVatIncluded(Yn.toBool(item.getOutVatIncludedYn()))
                .imageUrl(item.getImageUrl())
                .useYn(Yn.toBool(item.getUseYn()))
                .extraFields(extraFields)
                .build();
    }

    private void upsertExtraFields(Item item, Map<String, String> extraFields) {
        if (extraFields == null || extraFields.isEmpty()) return;

        for (Map.Entry<String, String> e : extraFields.entrySet()) {
            String fieldKey = safeKey(e.getKey());
            String value = e.getValue();

            if (fieldKey.isBlank()) continue;

            ItemFieldDefinition def = defRepo.findByFieldKey(fieldKey)
                    .orElseGet(() -> defRepo.save(ItemFieldDefinition.builder()
                            .fieldKey(fieldKey)
                            .label(fieldKey)
                            .fieldType("TEXT")
                            .useYn("Y")
                            .build()));

            ItemFieldValue val = ItemFieldValue.builder()
                    .item(item)
                    .fieldDef(def)
                    .fieldValue(value)
                    .build();

            valRepo.save(val);
        }
    }

    private Map<Long, Map<String, String>> loadExtraFieldsMap(List<Long> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) return Map.of();

        List<ItemFieldValue> vals = valRepo.findAllByItemIdsWithDef(itemIds);

        Map<Long, Map<String, String>> result = new HashMap<>();

        for (ItemFieldValue v : vals) {
            Long itemId = v.getItem().getId();
            String key = v.getFieldDef().getFieldKey();
            String value = v.getFieldValue();

            result.computeIfAbsent(itemId, k -> new LinkedHashMap<>()).put(key, value);
        }

        return result;
    }

    private Sort buildSort(String sortKey, String dir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Map<String, String> map = new HashMap<>();
        map.put("itemCode", "itemCode");
        map.put("itemName", "itemName");
        map.put("itemGroup", "itemGroup");
        map.put("spec", "spec");
        map.put("barcode", "barcode");
        map.put("inPrice", "inPrice");
        map.put("outPrice", "outPrice");
        map.put("itemType", "itemType");
        map.put("image", "imageUrl");
        map.put("imageUrl", "imageUrl");
        map.put("useYn", "useYn");

        String mapped = (sortKey == null) ? null : map.get(sortKey);

        if (mapped == null || mapped.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "id");
        }

        return Sort.by(direction, mapped).and(Sort.by(Sort.Direction.DESC, "id"));
    }

    private String safeKey(String s) {
        if (s == null) return "";
        return s.trim();
    }
}