package com.tiktokus.tiktokus.Service;


import com.tiktokus.tiktokus.DTO.ApiResponse;
import com.tiktokus.tiktokus.DTO.Customer;
import com.tiktokus.tiktokus.DTO.Label;
import com.tiktokus.tiktokus.Entity.*;
import com.tiktokus.tiktokus.Enum.OrderStatus;
import com.tiktokus.tiktokus.Enum.OrderType;
import com.tiktokus.tiktokus.Enum.ProductStatus;
import com.tiktokus.tiktokus.Enum.RoleUser;
import com.tiktokus.tiktokus.Repository.OrderDetailsRepository;
import com.tiktokus.tiktokus.Repository.OrderRepository;
import com.tiktokus.tiktokus.Repository.ProductRepository;
import com.tiktokus.tiktokus.Repository.UserRepository;
import com.tiktokus.tiktokus.Specification.OrderSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OrderService  {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;
    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    @Lazy
    ExpenseService expenseService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TelegramService telegrambot;

    @Autowired
    TransactionService transactionService;

    @Transactional
    public Orders createOrder(Orders orders){
        long quantity = 0;
        float total = 0;
        User user = userService.getUseLogin();
        for (OrderDetails item : orders.getOrderDetailsList()){
            Long id = item.getProduct().getId();
            Product p = productService.getById(id);
            if(p.getQuantity() < item.getQuantity() ){
                throw new RuntimeException("OUT OF STOCK " + p.getProductName());
            }
            if(p.getStatus() == ProductStatus.DISCONNECT || p.getStatus() == ProductStatus.DELETE){
                throw new RuntimeException("INACTIVE " + p.getProductName());
            }

            item.setCurPrice(p.getPrice());
            item.setTotal(item.getCurPrice() * item.getQuantity());
            quantity +=item.getQuantity();
            total += item.getTotal();
            if(user.getRole() != RoleUser.ORDER){
                p.setQuantity(p.getQuantity() - item.getQuantity());
                productRepository.save(p);
            }
        }
        Expense expense = expenseService.get();
        int coefficient = (int) Math.ceil((double) quantity / expense.getStep());
        log.info("fee: " + coefficient);
        orders.setUser(user);
        orders.setCreatedBy(user.getFullName());
        orders.setGroupId(user.getId());
        orders.setQuantity(quantity);
        orders.setExpense(expense.getExpense() * coefficient);
        if(orders.getType() == OrderType.By_Seller){
            orders.setExpenseLable(expense.getLableCosts());
        }
        else{
            orders.setExpenseLable(0);
        }

        orders.generateOrderCode();
        if(user.getRole() != RoleUser.ORDER){
            orders.setPreProduct(total);
            orders.setTotal(total + orders.getExpense() + orders.getExpenseLable());
        }else{
            orders.setPreProduct(0);
            orders.setTotal(orders.getExpense() + orders.getExpenseLable());
        }
        float amount = -orders.getTotal();
        user.setBalance(user.getBalance() - orders.getTotal());
        userRepository.save(user);
        orderRepository.save(orders);
        transactionService.create(user.getId(),amount,user.getBalance(),"Trừ tiền hóa đơn " + orders.getId());
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(now);
        if(user.getRole() != RoleUser.ADMIN){
            String mess = user.getFullName() + " đã đặt 1 đơn hàng!"
                          + "\nMã hóa đơn: " + orders.getId()
                          + "\nTổng tiền: "+ orders.getTotal()
                          + "\nThời gian: " + formattedDate;
            telegrambot.sendMessage(mess);

        }
        return orderRepository.findById(orders.getId()).get();
    }

    @Transactional
    public void createByList(List<Orders> ordersList){
        ordersList.forEach(item ->{
            try {
                createOrder(item);
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    public Page<Orders> getAll(int page,
                               int size,
                               String search,
                               OrderStatus status,
                               LocalDateTime startDate,
                               LocalDateTime endDate,
                               Long userId){
        Sort sort =  Sort.by(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, size, sort);
        Long id;
        try {
            id = Long.parseLong(search);
        }catch (Exception e) {
            id = null;
        }

        Specification<Orders> spec = Specification
                .where(OrderSpecification.hasExtraId(search))
                .or(OrderSpecification.hasOrderId(id))
                .or(OrderSpecification.hasPhone(search))
                .or(OrderSpecification.hasCustomerName(search))
                .and(OrderSpecification.hasStatus(status))
                .and(OrderSpecification.hasDateBetween(startDate, endDate));
        if(userId == -3){
           spec = spec.and(OrderSpecification.hasUserRole(RoleUser.ORDER));
        }else if(userId == -1){
            spec = spec.and(OrderSpecification.hasUserRole(RoleUser.STAFF));
        }else{
            spec = spec.and(OrderSpecification.hasGroupId(userId));
        }
        return orderRepository.findAll(spec,pageable);
    }
    public Page<Orders> getAllO(int page,
                                int size,
                                String search,
                                OrderStatus status,
                                LocalDateTime startDate,
                                LocalDateTime endDate){
        Sort sort =  Sort.by(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, size, sort);
        User user = userService.getUseLogin();
        Long id;
        try {
            id = Long.parseLong(search);
        }catch (Exception e){
            id = null;
        }

        Specification<Orders> spec = Specification
                .where(OrderSpecification.hasExtraId(search))
                .or(OrderSpecification.hasOrderId(id))
                .or(OrderSpecification.hasPhone(search))
                .or(OrderSpecification.hasCustomerName(search))
                .and(OrderSpecification.hasStatus(status))
                .and(OrderSpecification.hasGroupId(user.getId()))
                .and(OrderSpecification.hasDateBetween(startDate, endDate));

        return orderRepository.findAll(spec, pageable);
    }

    public Orders getByIdOfO(Long id){
        Orders orders = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not Found Order"));
        return orders;
    }

    @Transactional
    public ApiResponse<Orders> update(Orders orders, long idOrder) {
        Orders odlOrders = getByIdOfO(idOrder);
        User u = odlOrders.getUser();
        List<OrderDetails> orderDetailsList = odlOrders.getOrderDetailsList()
                .stream()
                .map(OrderDetails::clone)
                .toList();
        // trả lại sản phẩm vào kho
       if(odlOrders.getStatus() != OrderStatus.CANCELED && u.getRole() != RoleUser.ORDER) {
           orderDetailsList.forEach((item) -> {
               Long id = item.getProduct().getId();
               Long quantity = item.getQuantity();
               Product p = productService.getById(id);
               p.setQuantity(p.getQuantity() + quantity);
               productRepository.save(p);
           });
       }
        float amount =  odlOrders.getTotal();
        u.setBalance(u.getBalance() + odlOrders.getTotal());
        transactionService.create(u.getId(), amount, u.getBalance(), "Trả tiền khi chỉnh sửa hóa đơn "+ odlOrders.getId());
        StringBuilder outOfStockProducts = new StringBuilder();
        outOfStockProducts.append("Update order successfully").append(",");
        odlOrders.getOrderDetailsList().clear();
        long quantity = 0;
        float total = 0;

        for (OrderDetails item : orders.getOrderDetailsList()) {

            Long id = item.getProduct().getId();
            Product p = productService.getById(id);
            if (
                    p.getStatus() == ProductStatus.DISCONNECT
                    || p.getStatus() == ProductStatus.DELETE
                    || p.getQuantity() < item.getQuantity())
            {
                outOfStockProducts.append(p.getProductName() + " không thể chỉnh sửa").append(",");
                OrderDetails oldItem = orderDetailsList.stream()
                        .filter(o -> o.getProduct().getId().equals(id))
                        .findFirst()
                        .orElse(null);
                if (oldItem != null) {
                   item.setQuantity(oldItem.getQuantity());
                }
            }
            item.setCurPrice(p.getPrice());
            item.setTotal(item.getCurPrice() * item.getQuantity());
            quantity += item.getQuantity();
            total += item.getTotal();
            item.setOrders(odlOrders);

            // lấy sản phẩm trong kho
            if(u.getRole() != RoleUser.ORDER){
                p.setQuantity(p.getQuantity() - item.getQuantity());
                productRepository.save(p);
            }
        }
        Expense expense = expenseService.get();
        int coefficient = (int) Math.ceil((double) quantity / expense.getStep());
        log.info("fee: " + coefficient);

        User user = userService.getUseLogin();
        odlOrders.setUpdatedBy(user.getFullName());

        odlOrders.setQuantity(quantity);
        odlOrders.setExpense(expense.getExpense() * coefficient);
        odlOrders.setType(orders.getType());
        if(odlOrders.getType() == OrderType.By_Seller){
            odlOrders.setExpenseLable(expense.getLableCosts());
        }
        else{
            odlOrders.setExpenseLable(0);
        }
        odlOrders.generateOrderCode();
        if(u.getRole() != RoleUser.ORDER){
            odlOrders.setPreProduct(total);
            odlOrders.setTotal(total + odlOrders.getExpense() + odlOrders.getExpenseLable());
        }else{
            odlOrders.setPreProduct(0);
            odlOrders.setTotal(odlOrders.getExpense() + odlOrders.getExpenseLable());
        }

        odlOrders.setStatus(orders.getStatus());
        odlOrders.setCity(orders.getCity());
        odlOrders.setDesgin(orders.getDesgin());
        odlOrders.setMockup(orders.getMockup());
        odlOrders.setCountry(orders.getCountry());
        odlOrders.setCustomName(orders.getCustomName());
        odlOrders.setPhone(orders.getPhone());
        odlOrders.setEmail(orders.getEmail());
        odlOrders.setZipCode(orders.getZipCode());
        odlOrders.setState(orders.getState());
        odlOrders.setStreet(orders.getStreet());
        odlOrders.setStreetTwo(orders.getStreetTwo());
        odlOrders.setLabelUrl(orders.getLabelUrl());
        odlOrders.setTracking(orders.getTracking());
        odlOrders.setExtraId(orders.getExtraId());
        odlOrders.getOrderDetailsList().addAll(orders.getOrderDetailsList());
        amount = -odlOrders.getTotal();
        System.out.println("amount" + amount);
        u.setBalance(u.getBalance() - odlOrders.getTotal());
        userRepository.save(u);
        transactionService.create(u.getId(),amount,u.getBalance(),"Trừ tiền sau khi chỉnh sửa hóa đơn "+ odlOrders.getId());
        return ApiResponse.success(orderRepository.save(odlOrders), outOfStockProducts.toString());
    }

    @Transactional
    public Orders updateInfoLabel(Label label, Long id){
        Expense expense =expenseService.get();
        if(label.getType() == null)
            throw new RuntimeException("Ship type không được để trống!");
        Orders orders = getByIdOfO(id);
        if(orders.getType() == OrderType.By_Seller && label.getType() == OrderType.By_TikTok){
            orders.setTotal(orders.getTotal() - orders.getExpenseLable());
            User  u = orders.getUser();
            u.setBalance(u.getBalance()  + orders.getExpenseLable());
            orders.setExpenseLable(0);
            userRepository.save(u);
        }else if(orders.getType() == OrderType.By_TikTok && label.getType() == OrderType.By_Seller){
            orders.setExpenseLable(expense.getLableCosts());
            orders.setTotal(orders.getTotal() +  orders.getExpenseLable());
            User  u = orders.getUser();
            u.setBalance(u.getBalance()  + orders.getExpenseLable());
            userRepository.save(u);
        }
        orders.setLabelUrl(label.getLabelUrl());
        orders.setType(label.getType());
        orders.setTracking(label.getTracking());
        return orderRepository.save(orders);
    }

    @Transactional
    public Orders updateInfoCustomer(Customer customer, Long id){
        Orders orders = getByIdOfO(id);
        if (customer == null) {
            throw new RuntimeException("Thông tin không đuược thiếu");
        }

        // Kiểm tra thông tin khách hàng có rỗng không
        if (!StringUtils.hasText(customer.getCustomName())) {
            throw new RuntimeException("Tên khách hàng bắt buộc!");
        }

        if (!StringUtils.hasText(customer.getPhone())) {
            throw new RuntimeException("Số điện thoại bắt buộc!");
        }

        if (!StringUtils.hasText(customer.getStreet())) {
            throw new RuntimeException("Địa chỉ đường phố bắt buộc!");
        }

        if (!StringUtils.hasText(customer.getCity())) {
            throw new RuntimeException("Thành phố bắt buộc!");
        }

        if (!StringUtils.hasText(customer.getState())) {
            throw new RuntimeException("Bang bắt buộc!");
        }

        if (!StringUtils.hasText(customer.getCountry())) {
            throw new RuntimeException("Quốc gia bắt buộc!");
        }

        if (!StringUtils.hasText(customer.getZipCode())) {
            throw new RuntimeException("Mã bưu chính bắt buộc!");
        }
        orders.setCustomName(customer.getCustomName());
        orders.setPhone(customer.getPhone());
        orders.setStreet(customer.getStreet());
        orders.setStreetTwo(customer.getStreetTwo());
        orders.setCity(customer.getCity());
        orders.setState(customer.getState());
        orders.setCountry(customer.getCountry());
        orders.setZipCode(customer.getZipCode());
        orders.setEmail(customer.getEmail());
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(now);
        if(orders.getUser().getRole() != RoleUser.ADMIN){
            String mess = orders.getUser().getFullName() + " đã thay đổi thông tin hóa đơn!"
                    + "\nMã hóa đơn: " + orders.getId()
                    + "\nTổng tiền: "+ orders.getTotal()
                    + "\nThời gian: " + formattedDate;
            telegrambot.sendMessage(mess);
        }
        return orderRepository.save(orders);
    }
    @Transactional
    public  Orders cancel(Long id){
        Orders orders = getByIdOfO(id);
        User userLogin  = userService.getUseLogin();
        if(orders.getStatus() != OrderStatus.NEW && userLogin.getRole() == RoleUser.STAFF){
            throw new RuntimeException("Chỉ cancel khi ở trạng trái new!");
        }
        if(orders.getStatus() == OrderStatus.CANCELED)
            throw new RuntimeException("Hóa đơn đã cancel!");
        orders.setStatus(OrderStatus.CANCELED);
        orders.getOrderDetailsList().forEach(item ->{
            Product p = item.getProduct();
            p.setQuantity(p.getQuantity() + item.getQuantity());
            productRepository.save(p);
        });
        User u = orders.getUser();
        float amount = orders.getTotal();
        u.setBalance(u.getBalance() + orders.getTotal());
        userRepository.save(u);
        transactionService.create(u.getId(),amount,u.getBalance(),"Trả lại tiền hóa đơn " + orders.getId());
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(now);
        if(userLogin.getRole() != RoleUser.ADMIN){
            String mess = userLogin.getFullName() + " đã hủy đơn!"
                    + "\nMã hóa đơn: " + orders.getId()
                    + "\nTổng tiền: "+ orders.getTotal()
                    + "\nThời gian: " + formattedDate;
            telegrambot.sendMessage(mess);
        }
        return orderRepository.save(orders);
    }


    public  Orders changeStatus(Long id, OrderStatus status){
        if(status == null) throw new RuntimeException("Status không được để trống!");
        Orders orders = getByIdOfO(id);
        User userLogin  = userService.getUseLogin();
        if(userLogin.getRole() == RoleUser.STAFF){
            throw new RuntimeException("Bạn không có quyền!");
        }
        if(orders.getStatus() == OrderStatus.CANCELED)
            throw new RuntimeException("Hóa đơn đã cancel!");
        orders.setStatus(status);
        if(status == OrderStatus.CANCELED){
            orders.getOrderDetailsList().forEach(item ->{
                Product p = item.getProduct();
                p.setQuantity(p.getQuantity() + item.getQuantity());
                productRepository.save(p);
            });
            User u = orders.getUser();
            float amount = orders.getTotal();
            u.setBalance(u.getBalance() + orders.getTotal());
            userRepository.save(u);
            transactionService.create(u.getId(),amount,u.getBalance(),"Trả lại tiền hóa đơn " + orders.getId());
        }
        return orderRepository.save(orders);
    }

}
