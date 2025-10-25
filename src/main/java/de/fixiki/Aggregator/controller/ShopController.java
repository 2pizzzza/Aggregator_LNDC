package de.fixiki.Aggregator.controller;

import de.fixiki.Aggregator.dto.ShopDTO;
import de.fixiki.Aggregator.dto.ShopItemsDTO;
import de.fixiki.Aggregator.service.ItemService;
import de.fixiki.Aggregator.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final ItemService itemService;

    @GetMapping("search/q")
    public ResponseEntity<?> getShops(
            @RequestParam String title,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) BigDecimal priceFrom,
            @RequestParam(required = false) BigDecimal priceTo,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer radius,
            @RequestParam(required = false) Integer sex,
            @RequestParam(required = false) BigDecimal userLat,
            @RequestParam(required = false) BigDecimal userLon
    ) {
        List<ShopDTO> shopDTOList = shopService.getShops(title, priceFrom, priceTo, size, color, sex, userLat, userLon, radius);
        return ResponseEntity.ok(shopDTOList);
    }

    @GetMapping("/shops/{shopId}/items")
    public ResponseEntity<?> getShopItems(
            @PathVariable Integer shopId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer sex,
            @RequestParam(required = false) BigDecimal userLat,
            @RequestParam(required = false) BigDecimal userLon,
            @RequestParam(required = false) Integer radius,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int sizePerPage
    ) {
        ShopItemsDTO dto = itemService.getShopItems(shopId, title, size, color, sex, userLat, userLon, radius, page, sizePerPage);
        return ResponseEntity.ok(dto);
    }
}
