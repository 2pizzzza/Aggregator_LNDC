package de.fixiki.Aggregator.service;

import de.fixiki.Aggregator.dto.ShopDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ShopService {

    List<ShopDTO> getShops(String title, BigDecimal priceFrom, BigDecimal priceTo, String size, String color, Integer sex, BigDecimal userLat, BigDecimal userLon, Integer radius);
}
