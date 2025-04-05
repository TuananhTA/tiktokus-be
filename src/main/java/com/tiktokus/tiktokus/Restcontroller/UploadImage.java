package com.tiktokus.tiktokus.Restcontroller;

import com.tiktokus.tiktokus.DTO.CloudinaryResponse;
import com.tiktokus.tiktokus.Service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/public/image")
public class UploadImage {

    @Autowired
    CloudinaryService cloudinaryService;

    @PostMapping ("/upload")
    public ResponseEntity<CloudinaryResponse> upload(@RequestPart final MultipartFile file){
        return ResponseEntity.ok(cloudinaryService.uploadFile(file));
    }

}
