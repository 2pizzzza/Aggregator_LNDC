package de.fixiki.Aggregator.repository;

import de.fixiki.Aggregator.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Integer>, CustomShopRepository {

}
