package com.tiktokus.tiktokus.Restcontroller;

import com.tiktokus.tiktokus.Entity.ExcelFile;
import com.tiktokus.tiktokus.Repository.ExcelFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/private/csv")
public class ExcelFileRest {

    @Autowired
    private ExcelFileRepository uploadedFileRepository;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public String uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            // Đọc nội dung file vào byte array
            byte[] fileData = file.getBytes();

            // Tạo entity UploadedFile và lưu vào cơ sở dữ liệu
            ExcelFile uploadedFile = new ExcelFile();
            uploadedFile.setFileData(fileData);
            uploadedFileRepository.save(uploadedFile);
            return "File uploaded successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error uploading file.";
        }catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
