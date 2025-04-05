package com.tiktokus.tiktokus.Service;

import com.tiktokus.tiktokus.Convert.ProductConvert;
import com.tiktokus.tiktokus.DTO.ProductDTO;
import com.tiktokus.tiktokus.Entity.Product;
import com.tiktokus.tiktokus.Entity.User;
import com.tiktokus.tiktokus.Entity.UserProduct;
import com.tiktokus.tiktokus.Enum.OrderStatus;
import com.tiktokus.tiktokus.Enum.ProductStatus;
import com.tiktokus.tiktokus.Enum.RoleUser;
import com.tiktokus.tiktokus.Repository.ProductRepository;
import com.tiktokus.tiktokus.Repository.UserProductRepository;
import com.tiktokus.tiktokus.Specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserProductRepository userProductRepository;

    @Autowired
    UserService userService;

    public Product getById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }


    public List<ProductDTO> getAll(){
        return ProductConvert.convertListToDTO(productRepository.findAll());
    }

    public List<ProductDTO> getListAllProductByStatus(List<ProductStatus> statuses){
        User user = userService.getUseLogin();
        if(user.getRole() == RoleUser.STAFF){
            List<Product> productsPage = productRepository
                    .findProductsByUserIdAndProductStatuses(user.getId(),statuses);
            return ProductConvert.convertListToDTO(productsPage);

        }
        List<Product> productsPage = productRepository.findByStatusIn(statuses);
        return ProductConvert.convertListToDTO(productsPage);
    }

    public Page<ProductDTO> getAllv2 (String keyword,
                                      String categoryName,
                                      List<ProductStatus> statuses,
                                      int page,
                                      int size
    ){
        Sort sort =  Sort.by(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, size,sort);

        User user = userService.getUseLogin();
        if(user.getRole() == RoleUser.STAFF){
            Specification<Product> specification = Specification.where(ProductSpecification.hasUser(user.getId()))
                    .and(ProductSpecification.hasNameOrSku(keyword))
                    .and(ProductSpecification.hasCategoryName(categoryName))
                    .and(ProductSpecification.hasStatuses(statuses));
            Page<Product> productsPage = productRepository.findAll(specification, pageable);
            return productsPage.map(ProductConvert::convertToDTO);
        }
        Specification<Product> specification = ProductSpecification.searchProducts(keyword, categoryName, statuses);
        Page<Product> productsPage = productRepository.findAll(specification, pageable);
        return productsPage.map(ProductConvert::convertToDTO);
    }

    public Page<ProductDTO>  getProductByUser(List<ProductStatus> statuses, Long userId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = productRepository
                .findProductsByUserIdAndProductStatuses(userId,statuses, pageable);
        return productsPage.map(ProductConvert::convertToDTO);
    }
    public List<Product> getProductByUser(List<ProductStatus> statuses, User user) {
        if(user.getRole() == RoleUser.STAFF){
            Specification<Product> spec = Specification.where(ProductSpecification.hasUser(user.getId()))
                    .and(ProductSpecification.hasStatuses(statuses));
            return productRepository.findAll(spec);
        }
        return productRepository.findByStatusIn(statuses);
    }

    @Transactional
    public ProductDTO create(ProductDTO productDTO){
        Product p = ProductConvert.convertToEntity(productDTO);
        if(!productRepository.findBySku(productDTO.getSku()).isEmpty()){
            throw new RuntimeException("SKU đã tồn tại");
        }
        p.setGroupId(userService.getUseLogin().getGroupId());
        p.setSku(productDTO.getSku());
        productRepository.save(p);
        User user = userService.getUseLogin();

        UserProduct userProduct = new UserProduct();
        userProduct.setProduct(p);
        userProduct.setUser(user);
        userProductRepository.save(userProduct);
        return ProductConvert.convertToDTO(p);
    }


    @Transactional
    public ProductDTO update(ProductDTO productDTO, long id){
        Product p = getById(id);
        p.setQuantity(productDTO.getQuantity());
        p.setUrlImage(productDTO.getUrlImage());
        p.setProductName(productDTO.getProductName());
        p.setPrice(productDTO.getPrice());
        p.setSku(productDTO.getSku());
        p.setStatus(productDTO.getStatus());
        productRepository.save(p);
        return ProductConvert.convertToDTO(p);
    }

    @Transactional
    public ProductDTO delete(Long id){
        Product p = getById(id);
        p.setStatus(ProductStatus.DELETE);
        productRepository.save(p);
        return ProductConvert.convertToDTO(p);
    }

    public List<ProductDTO> searchInOrder(String name){
        User user = userService.getUseLogin();
        List<Product> productList = new ArrayList<>();
        if(user.getRole() == RoleUser.STAFF) {
            productList = productRepository.findProductsByUserIdAndProductNameOrSkuAndStatuses(
                    user.getId(),
                    name,
                    List.of(ProductStatus.AVAILABLE)
            );
        }else{
            productList = productRepository.findProductsByProductNameOrSkuAndStatuses(
                    name,
                    List.of(ProductStatus.AVAILABLE)
            );
        }
        return ProductConvert.convertListToDTO(productList);
    }

    public Page<ProductDTO> search(String name, int page, int size){
        Sort sort =  Sort.by(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, size, sort);
        User user = userService.getUseLogin();
        Page<Product> productPage =  productRepository.findProductsByUserIdAndProductNameOrSkuAndStatuses(
                user.getId(),
                name,
                List.of(ProductStatus.AVAILABLE, ProductStatus.DISCONNECT),
                pageable);
        return productPage.map(ProductConvert::convertToDTO);
    }

    @Transactional
    public List<ProductDTO> updateUserProduct(Long userId, List<ProductDTO> products){
        User user = userService.getById(userId);
        userProductRepository.deleteByUser(user);
        products.forEach(item ->{
            Product p = getById(item.getId());
            userProductRepository.save(UserProduct
                            .builder()
                            .user(user)
                            .product(p)
                            .build());
        });
        return products;
    }

    public List<Product> findActiveProductsByUserAndSkus(List<String> skus) {
        List<String> trimSkus = skus.stream().map(String:: trim).toList();
        Set<String> uniqueSet = new HashSet<>(trimSkus);
        boolean hasDuplicates = uniqueSet.size() < trimSkus.size();
        if(hasDuplicates){
            throw new RuntimeException("Duplicates sku");
        }
        Long userId = userService.getUseLogin().getId();
        Specification<Product> spec = Specification.where(ProductSpecification.hasUser(userId))
                .and(ProductSpecification.hasSkus(trimSkus))
                .and(ProductSpecification.hasStatus(ProductStatus.AVAILABLE));
        return productRepository.findAll(spec);
    }
}
