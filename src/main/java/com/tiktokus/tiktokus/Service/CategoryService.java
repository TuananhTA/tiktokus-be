package com.tiktokus.tiktokus.Service;

import com.tiktokus.tiktokus.Convert.ProductConvert;
import com.tiktokus.tiktokus.DTO.ProductDTO;
import com.tiktokus.tiktokus.Entity.Category;
import com.tiktokus.tiktokus.Entity.Product;
import com.tiktokus.tiktokus.Entity.User;
import com.tiktokus.tiktokus.Enum.ProductStatus;
import com.tiktokus.tiktokus.Repository.CategoryRepository;
import com.tiktokus.tiktokus.util.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class CategoryService {


    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;


    public Category create(String name){
        String newName = StringFormatter.formatString(name);
        if(newName.trim().isEmpty())
            throw new RuntimeException("Tên danh mục không được bỏ trống!");
        if(isNameExits(newName))
            throw new RuntimeException("Danh mục đã tồn tại!");
        return categoryRepository.save(Category.builder()
                        .categoryName(newName)
                        .products(new HashSet<>())
                        .build());
    }

    public boolean isNameExits(String name){
       return categoryRepository.findByCategoryName(name).isPresent();
    }

    public boolean isIdExits(Long id){
        return categoryRepository.findById(id).isPresent();
    }

    public Category getById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new  RuntimeException("Danh mục không tồn tại!"));
    }
    public Category getByName(String name){
        return categoryRepository.findByCategoryName(name)
                .orElseThrow(() -> new  RuntimeException("Danh mục không tồn tại!"));
    }


    public List<Category> getAll(){
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        return categoryRepository.findAll(sort);
    }

    @Transactional
    public List<Category> deleteById(Long id){
        Category category = getById(id);
        category.setProducts(new HashSet<>());
        categoryRepository.save(category);
        categoryRepository.delete(category);
        return getAll();
    }

    @Transactional
    public List<Category> addProductInCategory(List<ProductDTO> productDTOS, Long categoryId){
        HashSet<Product> products = new HashSet<>();
        for(ProductDTO productDTO : productDTOS){
            Product product = productService.getById(productDTO.getId());
            products.add(product);
        }
        Category category = getById(categoryId);
        System.out.println(products.size());
        category.setProducts(products);
        categoryRepository.save(category);
        return getAll();
    }

    @Transactional
    public List<Category> changName(String name, Long id){
        name = StringFormatter.formatString(name);
        if(name.isEmpty())
            throw new RuntimeException("Tên danh mục không được bỏ trống!");
        if(isNameExits(name))
            throw new RuntimeException("Tên danh mục đã tồn tại!");
        Category category = getById(id);
        category.setCategoryName(name);
        return getAll();
    }

    public HashSet<Category> getCategoryInProduct (){
        User userLogin = userService.getUseLogin();
        List<ProductStatus> statuses = List.of(ProductStatus.AVAILABLE, ProductStatus.DISCONNECT, ProductStatus.OUT_OF_STOCK);
        List<Product> products = productService.getProductByUser(statuses, userLogin);
        return categoryRepository.findDistinctCategoriesByProducts(products);
    }

}
