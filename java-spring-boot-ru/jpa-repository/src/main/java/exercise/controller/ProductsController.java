package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @GetMapping(path = "")
    public List<Product> showAll(@RequestParam Optional<Integer> min, @RequestParam Optional<Integer> max) {
        Sort sort = Sort.by(Sort.Order.asc("price"));

        if (min.isPresent() && max.isPresent()) {
            return productRepository.findAllByPriceBetween(min.get(), max.get(), sort);
        }

        if (min.isPresent()) {
            return productRepository.findAllByPriceGreaterThanEqual(min.get(), sort);
        }

        if (max.isPresent()) {
            return productRepository.findAllByPriceLessThanEqual(max.get(), sort);
        }

        return productRepository.findAll();
    }
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
