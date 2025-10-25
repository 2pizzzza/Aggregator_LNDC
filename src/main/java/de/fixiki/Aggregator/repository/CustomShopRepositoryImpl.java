package de.fixiki.Aggregator.repository;

import de.fixiki.Aggregator.entity.Shop;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomShopRepositoryImpl implements CustomShopRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Shop> findFilteredShops(
            String title,
            BigDecimal priceFrom,
            BigDecimal priceTo,
            String size,
            String color,
            Integer sex,
            Double userLat,
            Double userLon,
            Integer radiusKm
    ) {
        String jpql = """
                    SELECT DISTINCT s
                    FROM Shop s
                    JOIN s.items i
                    LEFT JOIN s.branches b
                    WHERE (:title IS NULL OR LOWER(i.title) LIKE LOWER(CONCAT('%', :title, '%')))
                      AND (:priceFrom IS NULL OR i.price >= :priceFrom)
                      AND (:priceTo IS NULL OR i.price <= :priceTo)
                      AND (:color IS NULL OR i.color = :color)
                      AND (:sex IS NULL OR i.sex = :sex)
                      AND (:size IS NULL OR :size IN elements(i.size))
                """;

        List<Shop> shops = entityManager.createQuery(jpql, Shop.class)
                .setParameter("title", title)
                .setParameter("priceFrom", priceFrom)
                .setParameter("priceTo", priceTo)
                .setParameter("color", color)
                .setParameter("sex", sex)
                .setParameter("size", size)
                .getResultList();

        if (userLat == null || userLon == null || radiusKm == null) {
            return shops;
        }

        return shops.stream()
                .filter(shop -> shop.getBranches().stream().anyMatch(branch -> {
                    if (branch.getLatitude() == null || branch.getLongitude() == null)
                        return false;
                    double distance = haversine(
                            userLat, userLon,
                            branch.getLatitude().doubleValue(),
                            branch.getLongitude().doubleValue()
                    );
                    return distance <= radiusKm;
                }))
                .toList();
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
