package com.data.filtro.api;

import com.data.filtro.model.ErrorResponse;
import com.data.filtro.model.Product;
import com.data.filtro.service.CategoryService;
import com.data.filtro.service.OriginService;
import com.data.filtro.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@Slf4j
public class ProductAPIController {


    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    OriginService flavorService;

    @GetMapping("/getProductList")
    public ResponseEntity<List<Product>> getProductList() {

        List<Product> productList = productService.getAll();
        productList.forEach(s -> log.info(s.getProductName()));
        if (productList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("find/{id}")
    public ResponseEntity<?> find(@PathVariable int id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            String message = "No product found!";
            ErrorResponse errorResponse = new ErrorResponse(message, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/getbycate/{id}")
    public ResponseEntity<List<Product>> getProductListbycate(@PathVariable int id) {

        Pageable pageable = Pageable.unpaged();
        Page<Product> productList = productService.getProductByCategory(id, pageable);
        productList.forEach(s -> log.info(s.getProductName()));
        if (productList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productList.getContent(), HttpStatus.OK);
    }

    @GetMapping("/getbyflavor/{id}")
    public ResponseEntity<List<Product>> getProductListbyflavor(@PathVariable int id) {

        Pageable pageable = Pageable.unpaged();
        Page<Product> productList = productService.getProductsByFlavorId(id, pageable);
        productList.forEach(s -> log.info(s.getProductName()));
        if (productList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productList.getContent(), HttpStatus.OK);
    }

    @PostMapping("/addnewproduct")
    public ResponseEntity<?> addnewproduct(@RequestParam String nameproduct,
                                           @RequestParam int soluong,
                                           @RequestParam int price,
                                           @RequestParam int cateId,
                                           @RequestParam int flavorId,
                                           @RequestParam String des,
                                           @RequestParam String image

    ) {
        try {
            Date now = new Date(System.currentTimeMillis());

            Product product = new Product();
            product.setCreatedDate(now);
            product.setProductName(nameproduct);
            product.setQuantity(soluong);
            product.setPrice(price);
            product.setCategory(categoryService.getCategoryById(cateId));
            product.setOrigin(flavorService.getMaterialById(flavorId));
            product.setDescription(des);
            product.setImage(image);
            productService.save(product);
            return new ResponseEntity<>("Đã thêm sản phẩm vào kho", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Xảy ra lỗi" + e.toString(), HttpStatus.OK);

        }


    }

    @PostMapping("/editproduct")
    public ResponseEntity<?> editproduct(@RequestParam String nameproduct,
                                           @RequestParam int soluong,
                                           @RequestParam int price,
                                           @RequestParam String des,
                                           @RequestParam int productId,
                                         @RequestParam int status

    ) {
        try {
            Product product = productService.getProductById(productId);
            product.setProductName(nameproduct);
            product.setQuantity(soluong);
            product.setPrice(price);
            product.setDescription(des);
            product.setStatus(status);
            productService.save(product);
            return new ResponseEntity<>("Đã sửa sản phẩm", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Xảy ra lỗi" + e.toString(), HttpStatus.OK);

        }


    }
}
