package com.tiktokus.tiktokus.config;


import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary getCloudinary(){
        Map config = new HashMap();
        config.put("cloud_name", "ddnasugap");
        config.put("api_key", "826733223926353");
        config.put("api_secret", "zhlnnMMaTOpfDDQ_gipFEVcWDSg");
        return new Cloudinary(config);
    }

}
