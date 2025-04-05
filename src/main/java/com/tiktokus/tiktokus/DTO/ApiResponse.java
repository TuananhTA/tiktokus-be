package com.tiktokus.tiktokus.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T>{

    private int status;
    private String message;
    private T data;
    private String error;

    // Phương thức tạo response thành công
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, message, data, null);
    }

    // Phương thức tạo response lỗi
    public static <T> ApiResponse<T> error(int status, String errorMessage) {
        return new ApiResponse<>(status, "Error", null, errorMessage);
    }
}
