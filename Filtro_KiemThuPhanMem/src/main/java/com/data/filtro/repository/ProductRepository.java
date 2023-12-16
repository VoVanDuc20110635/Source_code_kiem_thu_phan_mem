package com.data.filtro.repository;

import com.data.filtro.model.Account;
import com.data.filtro.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    @Query("select p from Product p where p.id =:id")
    Product findById(@Param("id") int id);

    @Query("select p from Product p order by p.sold desc limit 8")
    List<Product> findTop8SellingProducts();

    @Query("select p from Product p order by p.createdDate desc limit 6")
    List<Product> find6thProducts();

    @Query("select p from Product p order by p.discount desc limit 4")
    List<Product> findTop4MostDiscountProducts();

    Page<Product> findAll(Pageable pageable);

    @Query("select p from Product p where p.category.id = :categoryId")
    Page<Product> findProductsByCategory(@Param("categoryId") Integer id, Pageable pageable);

    @Query("select p from Product p where p.origin.id = :flavorId")
    Page<Product> findProductsByFlavor(@Param("flavorId") Integer id, Pageable pageable);

    @Query("select p from Product p where p.origin.id = :flavorId and p.id != :currentProductId order by p.productName limit 4")
    List<Product> findTop4ProductsByFlavor(@Param("flavorId") Integer id, @Param("currentProductId") Integer currentProductId);

    @Query("select  p from  Product  p where lower(p.productName) like %:name%")
    Page<Product> findProducsByName(@Param("name") String name, Pageable pageable);

    @Query("select p from Product p where p.price >= :priceLow and p.price <= :priceHigh order by p.price asc ")
    List<Product> findProductByPrice(@Param("priceLow") Integer priceLow, @Param("priceHigh") Integer priceHigh);

    @Query("select p from Product p where p.price >= :lowPrice AND p.price <= :highPrice AND p.origin.id = :originId")
    Page<Product> getProductByPriceAndMaterial(@Param("lowPrice") Integer lowPrice,
                                               @Param("highPrice") Integer highPrice,
                                               @Param("originId") Integer originId,
                                               Pageable pageable);

    @Query("select p from Product p where p.price >= :lowPrice AND p.price <= :highPrice")
    Page<Product> getProductByPrice(@Param("lowPrice") Integer lowPrice,
                                    @Param("highPrice") Integer highPrice,
                                    Pageable pageable);

    @Query("select p from Product p where p.origin.id = :originId")
    Page<Product> getProductByOrigin(@Param("originId") Integer originId, Pageable pageable);

    @Query("select p from Product p where p.category.id = :categoryId AND p.price >= :lowPrice AND p.price <= :highPrice AND p.origin.id = :originId")
    Page<Product> getProductByCategoryAndPriceAndMaterial(@Param("categoryId") Integer categoryId,
                                                          @Param("lowPrice") Integer lowPrice,
                                                          @Param("highPrice") Integer highPrice,
                                                          @Param("originId") Integer originId,
                                                          Pageable pageable);

    @Query("select p from Product p where p.category.id = :categoryId AND p.price >= :lowPrice AND p.price <= :highPrice")
    Page<Product> getProductByCategoryAndPrice(@Param("categoryId") Integer categoryId,
                                               @Param("lowPrice") Integer lowPrice,
                                               @Param("highPrice") Integer highPrice,
                                               Pageable pageable);

    @Query("select p from Product p where p.category.id = :categoryId AND p.origin.id = :originId")
    Page<Product> getProductByCategoryAndMaterial(@Param("categoryId") Integer categoryId,
                                                  @Param("originId") Integer originId,
                                                  Pageable pageable);

    @Query("select a from Product a where a.status = :status")
    List<Product> availableProducts(@Param("status") int status);
    @Query("select a from Product a where a.discount > 0")
    List<Product> discountProducts();

}
