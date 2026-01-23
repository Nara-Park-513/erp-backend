package port.sm.erp.dto;

import java.math.BigDecimal;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import port.sm.erp.entity.ItemType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ItemResponse {
private Long id;

private String item_code, item_name, item_group, spec, spec_mode, unit, barcode, process, image_url;
private ItemType item_type;

private BigDecimal in_price, out_price;
private boolean in_vat_included, out_vat_included, use_yn, is_set;
private Map<String, String> extraFields;
}
