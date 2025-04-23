package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import exercise.model.Product;

import org.springframework.data.domain.Sort;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // BEGIN
    List<Product> findAllByPriceBetween(int minPrice, int maxPrice, Sort sort);

    List<Product> findAllByPriceGreaterThanEqual(int minPrice, Sort sort);

    List<Product> findAllByPriceLessThanEqual(int maxPrice, Sort sort);
    // END
}
