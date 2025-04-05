package com.tiktokus.tiktokus.Restcontroller;


import com.tiktokus.tiktokus.DTO.ApiResponse;
import com.tiktokus.tiktokus.DTO.ProductDTO;
import com.tiktokus.tiktokus.Entity.Product;
import com.tiktokus.tiktokus.Enum.ProductStatus;
import com.tiktokus.tiktokus.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/private/product")
public class ProductRest {

    @Autowired
    ProductService productService;

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProduct(){
        try {
            return ResponseEntity.ok(ApiResponse.success(productService.getAll(), "Products found successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "Products not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @GetMapping("/get-all-active")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getAllProductActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "", name = "categoryName") String categoryName
    ){
        try {
            List<ProductStatus> listGet = new ArrayList<>();
            listGet.add(ProductStatus.AVAILABLE);
            listGet.add(ProductStatus.OUT_OF_STOCK);
            listGet.add(ProductStatus.DISCONNECT);
            return ResponseEntity.ok(ApiResponse.success(productService.getAllv2(search,categoryName,listGet, page, size), "Products found successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "Products not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @GetMapping("/get-all-to-excel")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProductExcel(){
        try {
            List<ProductStatus> listGet = new ArrayList<>();
            listGet.add(ProductStatus.AVAILABLE);
            listGet.add(ProductStatus.OUT_OF_STOCK);
            listGet.add(ProductStatus.DISCONNECT);
            return ResponseEntity.ok(ApiResponse.success(productService.getListAllProductByStatus(listGet), "Products found successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "Products not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @GetMapping("/get-all-by-user/{userId}")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getAllProductByUser(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ){
        try {
            List<ProductStatus> listGet = new ArrayList<>();
            listGet.add(ProductStatus.AVAILABLE);
            listGet.add(ProductStatus.OUT_OF_STOCK);
            listGet.add(ProductStatus.DISCONNECT);
            return ResponseEntity.ok(ApiResponse.success(productService.getProductByUser(listGet,userId,page, size), "Products found successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "Products not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @GetMapping("/search-in-order")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> searchInOrder(@RequestParam("search") String name){
        try {
            return ResponseEntity.ok(ApiResponse.success(productService.searchInOrder(name), "Products found successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "Products not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> search(
            @RequestParam("search") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        try {
            return ResponseEntity.ok(ApiResponse.success(productService.search(name, page, size), "Products found successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "Products not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @PostMapping("/create-product")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO product){
        try {
            return ResponseEntity.ok(ApiResponse.success(productService.create(product), "Products create successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @PutMapping("/update-product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@Valid @RequestBody ProductDTO product,
                                                                 @PathVariable("id") long id){
        try {
            return ResponseEntity.ok(ApiResponse.success(productService.update(product,id), "Products create successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @DeleteMapping("/delete-product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDTO>> deleteProduct(@PathVariable("id") long id){
        try {
            return ResponseEntity.ok(ApiResponse.success(productService.delete(id), "Products delete successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @PutMapping("/add-product-for-user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> addProductForUser(@PathVariable("userId") long id,
                                                                     @RequestBody List<ProductDTO> productDTOS){
        try {
            return ResponseEntity.ok(ApiResponse.success(productService.updateUserProduct(id,productDTOS), "Add successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @PostMapping("/get-product-by-skus")
    public ResponseEntity<ApiResponse<List<Product>>> excel(@RequestBody List<String> skus){
        try {
            return ResponseEntity.ok(ApiResponse.success(productService.findActiveProductsByUserAndSkus(skus), "Products found successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

}
