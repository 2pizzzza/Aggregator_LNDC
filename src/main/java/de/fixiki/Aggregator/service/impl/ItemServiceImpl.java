package de.fixiki.Aggregator.service.impl;

import de.fixiki.Aggregator.dto.ItemDTO;
import de.fixiki.Aggregator.dto.ShopItemsDTO;
import de.fixiki.Aggregator.entity.Branch;
import de.fixiki.Aggregator.entity.Item;
import de.fixiki.Aggregator.entity.Shop;
import de.fixiki.Aggregator.mapper.BaseMapper;
import de.fixiki.Aggregator.repository.ShopRepository;
import de.fixiki.Aggregator.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ShopRepository shopRepository;
    private final BaseMapper baseMapper;

    @Override
    public ShopItemsDTO getShopItems(
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
    ) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        List<Integer> filteredBranchIds = shop.getBranches().stream()
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
                .map(Branch::getId)
                .toList();

        List<Item> filteredItemsEntities = shop.getItems().stream()
                .filter(item -> title == null || item.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(item -> color == null || color.equals(item.getColor()))
                .filter(item -> sex == null || sex.equals(item.getSex()))
                .filter(item -> size == null || item.getSize().contains(size))
                .toList();

        List<ItemDTO> filteredItems = baseMapper.toDtoList(
                shop.getItems().stream()
                        .filter(item -> title == null || item.getTitle().toLowerCase().contains(title.toLowerCase()))
                        .filter(item -> color == null || color.equals(item.getColor()))
                        .filter(item -> sex == null || sex.equals(item.getSex()))
                        .filter(item -> size == null || item.getSize().contains(size))
                        .toList(),
                ItemDTO.class
        );

        int totalItems = filteredItems.size();
        int fromIndex = Math.min(page * sizePerPage, totalItems);
        int toIndex = Math.min(fromIndex + sizePerPage, totalItems);
        List<ItemDTO> pageItems = filteredItems.subList(fromIndex, toIndex);

        int totalPages = (int) Math.ceil((double) totalItems / sizePerPage);

        return new ShopItemsDTO(shop.getId(), shop.getTitle(), pageItems, page, totalPages, totalItems);

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
