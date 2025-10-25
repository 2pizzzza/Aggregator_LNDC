package de.fixiki.Aggregator.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDTO {

    String title;

    BigDecimal price;

    String img_url, ref_item, category;

    Byte sex;

    String color;

    List<String> size;
}
