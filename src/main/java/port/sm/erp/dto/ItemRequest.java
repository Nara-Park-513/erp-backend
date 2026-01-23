package port.sm.erp.dto;

import java.math.BigDecimal;
import java.util.Map;

import lombok.*;
import port.sm.erp.entity.ItemType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequest {
	
 private String item_code;
 private String item_name;
 private String item_group;
 private String spec;
 private String spec_mode;
 private String unit;
 private String barcode;
 private String process;
 
 private ItemType item_type;
 private boolean is_set;
 
 private BigDecimal in_price;
 private boolean in_vat_included;
 
 private BigDecimal out_price;
 private boolean out_vat_included;
 
 private String image_url;
 private boolean use_yn;
 
 private Map<String, String> extraFields;
 
}
