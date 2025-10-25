package de.fixiki.Aggregator.service.impl;

import de.fixiki.Aggregator.dto.BranchDTO;
import de.fixiki.Aggregator.dto.ShopDTO;
import de.fixiki.Aggregator.entity.Item;
import de.fixiki.Aggregator.entity.Shop;
import de.fixiki.Aggregator.repository.ShopRepository;
import de.fixiki.Aggregator.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

    @Override
    public List<ShopDTO> getShops(
            String title,
            BigDecimal priceFrom,
            BigDecimal priceTo,
            String size,
            String color,
            Integer sex,
            BigDecimal userLat,
            BigDecimal userLon,
            Integer radius
    ) {
        List<Shop> shops = shopRepository.findFilteredShops(
                title,
                priceFrom,
                priceTo,
                size,
                color,
                sex,
                userLat != null ? userLat.doubleValue() : null,
                userLon != null ? userLon.doubleValue() : null,
                radius
        );

        return shops.stream().map(shop -> {
                    List<BigDecimal> prices = shop.getItems().stream()
                            .map(Item::getPrice)
                            .sorted()
                            .toList();

                    BigDecimal min = prices.isEmpty() ? BigDecimal.ZERO : prices.get(0);
                    BigDecimal max = prices.isEmpty() ? BigDecimal.ZERO : prices.get(prices.size() - 1);
                    BigDecimal median = prices.isEmpty()
                            ? BigDecimal.ZERO
                            : prices.get(prices.size() / 2);

                    List<BranchDTO> branchDTOs = shop.getBranches().stream()
                            .filter(branch -> {
                                if (userLat == null || userLon == null || radius == null) return true;
                                if (branch.getLatitude() == null || branch.getLongitude() == null) return false;

                                double distance = haversine(
                                        userLat.doubleValue(),
                                        userLon.doubleValue(),
                                        branch.getLatitude().doubleValue(),
                                        branch.getLongitude().doubleValue()
                                );
                                return distance <= radius;
                            })
                            .map(b -> new BranchDTO(b.getLatitude(), b.getLongitude(), b.getAddress()))
                            .toList();

                    if (branchDTOs.isEmpty()) return null;

                    return new ShopDTO(
                            shop.getId(),
                            shop.getTitle(),
                            min,
                            max,
                            median,
                            branchDTOs
                    );
                }).filter(dto -> dto != null)
                .toList();
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
