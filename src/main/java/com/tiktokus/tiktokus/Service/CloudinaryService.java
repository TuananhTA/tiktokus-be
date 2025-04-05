package com.tiktokus.tiktokus.Service;

import com.cloudinary.Cloudinary;
import com.tiktokus.tiktokus.DTO.CloudinaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Transactional
    public CloudinaryResponse uploadFile(final MultipartFile file) {
        try {
            final Map result = this.cloudinary
                    .uploader()
                    .upload(file.getBytes(), Map.of("public_id", "tiktok-order" + UUID.randomUUID().toString()));
            final String url= (String) result.get("secure_url");
            final String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder().publicId(publicId).url(url)
                    .build();
        } catch (final Exception e) {
            throw new RuntimeException("Failed to upload file");
        }
    }
}
