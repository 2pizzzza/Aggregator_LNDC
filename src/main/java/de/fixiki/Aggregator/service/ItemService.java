package de.fixiki.Aggregator.service;

import de.fixiki.Aggregator.dto.ShopItemsDTO;

import java.math.BigDecimal;

public interface ItemService {
    ShopItemsDTO getShopItems(
            Integer shopId,
            String title,
            String size,
            String color,
            Integer sex,
            BigDecimal userLat,
            BigDecimal userLon,
            Integer radius,
            int page,
            int sizePerPage
    );
}
