package com.tiktokus.tiktokus.Restcontroller;

import com.tiktokus.tiktokus.DTO.ApiResponse;
import com.tiktokus.tiktokus.DTO.ProductDTO;
import com.tiktokus.tiktokus.Service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/private/category")
public class CategoryRest {


    @Autowired
    CategoryService categoryService;

    @PostMapping("/create-category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCategory(@RequestParam(defaultValue = "", name = "categoryName") String categoryName){
        try {
            return ResponseEntity.ok(ApiResponse.success(categoryService.create(categoryName), "Tạo danh mục thành công!"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping("/get-all-category")
    public ResponseEntity<?> getAllCategory(){
        try {
            return ResponseEntity.ok(ApiResponse.success(categoryService.getAll(), "Lấy danh mục thành công!"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping("/get-in-product")
    public ResponseEntity<?> getInProduct(){
        try {
            return ResponseEntity.ok(ApiResponse.success(categoryService.getCategoryInProduct(), "Lấy danh mục thành công!"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @DeleteMapping("/delete-category/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(@Valid @PathVariable("id") long id){
        try {
            return ResponseEntity.ok(ApiResponse.success(categoryService.deleteById(id), "Xóa danh mục thành công!"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PostMapping("/add-product-in-category/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addProductInCategory(@Valid @RequestBody List<ProductDTO> productDTOS,
                                                  @PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(ApiResponse.success(categoryService.addProductInCategory(productDTOS,id),
                    "Chỉnh sửa thành công!"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }

    }
    @PutMapping("/change-name/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeName(@RequestParam(defaultValue = "", name = "categoryName") String categoryName,
                                                  @PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(ApiResponse.success(categoryService.changName(categoryName,id),
                    "Chỉnh sửa thành công!"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }

    }
}
