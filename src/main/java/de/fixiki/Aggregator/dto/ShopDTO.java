package de.fixiki.Aggregator.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopDTO {

    Integer id;

    String title;

    BigDecimal minPrice, maxPrice, medianPrice;

    List<BranchDTO> branches;
}
