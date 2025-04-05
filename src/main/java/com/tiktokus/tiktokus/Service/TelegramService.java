package com.tiktokus.tiktokus.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;

import java.util.Map;

@Service
public class TelegramService {


    @Value("${bot.api-key}")
    String apikey;

    @Value("${bot.chat-id}")
    String chatId;


    public boolean sendMessage(String text) {
        String url = String.format(
                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s&parse_mode=HTML",
                apikey, chatId, text
        );
        try{
            RestTemplate restTemplate = new RestTemplate();
            String responseEntity =  restTemplate.getForObject(url, String.class);
            return parseResponse(responseEntity);
        } catch (Exception e) {
            return false;
        }
    }

    private Boolean parseResponse(String responseBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = objectMapper.readValue(
                responseBody,
                new TypeReference<Map<String, Object>>() {}
        );
        Object okeValue = responseMap.get("ok");
        boolean code;
        if (okeValue == null) {
            code = false; // Nếu không có "oke", coi như thất bại
        } else if (okeValue instanceof Boolean) {
            code = (boolean) okeValue; // Nếu là Boolean, ép kiểu trực tiếp
        } else if (okeValue instanceof String) {
            code = Boolean.parseBoolean((String) okeValue); // Nếu là String, parse thành boolean
        } else {
            code = false; // Các trường hợp khác, mặc định thất bại
        }
        return code;
    }
}
