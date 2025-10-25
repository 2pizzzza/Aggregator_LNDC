//package de.fixiki.Aggregator;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import de.fixiki.Aggregator.entity.Item;
//import de.fixiki.Aggregator.entity.Shop;
//import de.fixiki.Aggregator.repository.ItemRepository;
//import de.fixiki.Aggregator.repository.ShopRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.io.InputStream;
//import java.math.BigDecimal;
//import java.util.List;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class JsonToPostgresImporter {
//
//    private final ItemRepository itemRepository;
//    private final ShopRepository shopRepository;
//
//    @PostConstruct
//    public void importData() {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jdsports_female.json");
//            if (inputStream == null) {
//                log.error("JSON file not found!");
//                return;
//            }
//
//            List<ItemJson> items = mapper.readValue(inputStream, new TypeReference<>() {});
//            log.info("Loaded {} female items from JSON", items.size());
//
//            // Найти JD Sports
//            Shop shop = shopRepository.findAll().stream()
//                    .filter(s -> "JD Sports".equals(s.getTitle()))
//                    .findFirst()
//                    .orElseGet(() -> {
//                        Shop newShop = new Shop();
//                        newShop.setTitle("JD Sports");
//                        return shopRepository.save(newShop);
//                    });
//
//            for (ItemJson itemJson : items) {
//                BigDecimal price = parsePrice(itemJson.price);
//
//                Item item = new Item();
//                item.setTitle(itemJson.title);
//                item.setCategory(itemJson.category);
//                item.setImg_url(itemJson.img_url);
//                item.setRef_item(itemJson.ref_item);
//                item.setSex(itemJson.sex); // здесь будет 0
//                item.setColor(itemJson.color);
//                item.setPrice(price);
//                item.setShop(shop);
//                item.setSize(itemJson.size);
//
//                itemRepository.save(item);
//            }
//
//            log.info("Successfully imported all female items to PostgreSQL");
//
//        } catch (Exception e) {
//            log.error("Error importing JSON data: ", e);
//        }
//    }
//
//    private BigDecimal parsePrice(String priceStr) {
//        if (priceStr == null || priceStr.isEmpty()) return BigDecimal.ZERO;
//
//        try {
//            // Оставляем только цифры, запятую и точку перед символом €
//            // Например: "Jetzt  75,00€   - 21%" -> "75,00"
//            int euroIndex = priceStr.indexOf("€");
//            if (euroIndex == -1) return BigDecimal.ZERO;
//
//            String numberPart = priceStr.substring(0, euroIndex)
//                    .replaceAll("[^0-9,\\.]", "") // удаляем все кроме цифр, точки и запятой
//                    .replace(",", ".");           // меняем запятую на точку
//
//            return new BigDecimal(numberPart);
//        } catch (Exception e) {
//            log.warn("Could not parse price: {}", priceStr);
//            return BigDecimal.ZERO;
//        }
//    }
//
//
//    private static class ItemJson {
//        public String title;
//        public String price;
//        public String img_url;
//        public String ref_item;
//        public String category;
//        public Byte sex;
//        public Integer shop_id;
//        public String color;
//        public List<String> size;
//    }
//}
