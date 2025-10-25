package de.fixiki.Aggregator.repository;

import de.fixiki.Aggregator.entity.Shop;

import java.math.BigDecimal;
import java.util.List;

public interface CustomShopRepository {

    List<Shop> findFilteredShops(
            String title,
            BigDecimal priceFrom,
            BigDecimal priceTo,
            String size,
            String color,
            Integer sex,
            Double userLat,
            Double userLon,
            Integer radiusKm
    );
}
