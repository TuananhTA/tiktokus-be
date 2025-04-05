package com.tiktokus.tiktokus.Restcontroller;

import com.tiktokus.tiktokus.DTO.ApiResponse;
import com.tiktokus.tiktokus.DTO.Customer;
import com.tiktokus.tiktokus.DTO.Label;
import com.tiktokus.tiktokus.Entity.Orders;
import com.tiktokus.tiktokus.Enum.OrderStatus;
import com.tiktokus.tiktokus.Service.OrderService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/private/order")
public class OrdersRest {
    @Autowired
    OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<ApiResponse<Orders>> createOrder(@Valid @RequestBody Orders orders){

        try{
            return ResponseEntity.ok(ApiResponse.success(orderService.createOrder(orders), "Create orders successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(HttpStatus.CONFLICT.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }

    }
    @PostMapping("/create-order-by-file")
    public ResponseEntity<?> createOrder(@RequestBody List<Orders> ordersList){
        try{
            orderService.createByList(ordersList);
            return ResponseEntity.ok(ApiResponse.success("Thành công!", "Create orders successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(HttpStatus.CONFLICT.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }

    }


    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('ADMIN','ORDER','PACKER')")
    public ResponseEntity<ApiResponse<Page<Orders>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Long userId
    ){
        try{
            if(userId == null) userId =0L;
            return ResponseEntity.ok(ApiResponse.success(orderService.getAll(
                    page,
                    size,
                    search,
                    status,
                    startDate,
                    endDate,
                    userId
            ), "Get orders successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(HttpStatus.CONFLICT.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }

    }

    @GetMapping("/get-all-by-user")
    public ResponseEntity<ApiResponse<Page<Orders>>> getAllByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate
    ){
        try{
            return ResponseEntity.ok(ApiResponse.success(orderService.getAllO(page, size, search,status,startDate,endDate), "Get orders successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(HttpStatus.CONFLICT.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }

    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ApiResponse<Orders>> getOrderByIdAndUser(@PathVariable("id") long id){
        try{
            return ResponseEntity.ok(ApiResponse.success(orderService.getByIdOfO(id), "Get order successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }

    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORDER')")
    public ResponseEntity<ApiResponse<Orders>> updateOrder(@RequestBody Orders orders,
                                                           @PathVariable("id") long id)
    {
        try{
            return ResponseEntity.ok(orderService.update(orders,id));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @PutMapping("/update-label/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORDER')")
    public ResponseEntity<ApiResponse<Orders>> updateLabel(@RequestBody Label label,
                                                           @PathVariable("id") long id)
    {
        try{
            return ResponseEntity.ok(ApiResponse.success(
                    orderService.updateInfoLabel(label,id),
                    "Chỉnh sửa thành công"
            ));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @PutMapping("/update-customer/{id}")
    public ResponseEntity<ApiResponse<Orders>> updateLabel(@RequestBody Customer customer,
                                                           @PathVariable("id") long id)
    {
        try{
            return ResponseEntity.ok(ApiResponse.success(
                    orderService.updateInfoCustomer(customer,id),
                    "Chỉnh sửa thành công"
            ));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @PutMapping("/change-status/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORDER','PACKER')")
    public ResponseEntity<ApiResponse<Orders>> changeStatus(@RequestParam("status") OrderStatus status,
                                                            @PathVariable("id") Long id)
    {
        try{
            return ResponseEntity.ok(ApiResponse.success(orderService.changeStatus(id, status), "Đổi trạng thái thành công"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }



    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<ApiResponse<Orders>> cancelOrder(@PathVariable("orderId") long id)
    {
        try{
            return ResponseEntity.ok(ApiResponse.success(orderService.cancel(id),"Cancel successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

}
