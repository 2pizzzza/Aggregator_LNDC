package de.fixiki.Aggregator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fixiki.Aggregator.entity.Item;
import de.fixiki.Aggregator.entity.Shop;
import de.fixiki.Aggregator.repository.ItemRepository;
import de.fixiki.Aggregator.repository.ShopRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonToPostgresNikeFemale {

    private final ItemRepository itemRepository;
    private final ShopRepository shopRepository;

    @PostConstruct
    public void importData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("nike_products_f.json");
            if (inputStream == null) {
                log.error("JSON file not found!");
                return;
            }

            List<ItemJson> items = mapper.readValue(inputStream, new TypeReference<>() {});
            log.info("Loaded {} items from JSON", items.size());

            for (ItemJson itemJson : items) {
                Optional<Shop> existingShop = shopRepository.findAll().stream()
                        .filter(s -> "Nike".equals(s.getTitle()))
                        .findFirst();

                Shop shop = existingShop.orElseGet(() -> {
                    Shop newShop = new Shop();
                    newShop.setTitle("Nike");
                    return shopRepository.save(newShop);
                });

                BigDecimal price = parsePrice(itemJson.price);

                Item item = new Item();
                item.setTitle(itemJson.title);
                item.setCategory(itemJson.category);
                item.setImg_url(itemJson.img_url);
                item.setRef_item(itemJson.ref_item);
                item.setSex((int) 0); // женская коллекция
                item.setColor(itemJson.color);
                item.setPrice(price);
                item.setShop(shop);
                item.setSize(itemJson.size);

                itemRepository.save(item);
            }

            log.info("Successfully imported all Nike female items to PostgreSQL");

        } catch (Exception e) {
            log.error("Error importing Nike female JSON data: ", e);
        }
    }

    private BigDecimal parsePrice(String priceStr) {
        if (priceStr == null || priceStr.isEmpty()) return BigDecimal.ZERO;
        try {
            // Убираем все лишние символы, оставляем только число
            priceStr = priceStr.replaceAll("[^0-9,\\.]", "").replace(",", ".");
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            DecimalFormat df = new DecimalFormat("#0.00", symbols);
            return new BigDecimal(df.parse(priceStr).toString());
        } catch (Exception e) {
            log.warn("Could not parse price: {}", priceStr);
            return BigDecimal.ZERO;
        }
    }

    private static class ItemJson {
        public String title;
        public String price;
        public String img_url;
        public String ref_item;
        public String category;
        public Integer sex;
        public Integer shop_id;
        public String color;
        public List<String> size;
    }
}
