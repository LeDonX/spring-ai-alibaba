package org.ledon.springai.tools.exception;

import cn.dev33.satoken.util.SaResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 全局异常拦截 
    @ExceptionHandler
    public String handlerException(Exception e) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = null;
        try {
            json = mapper.readTree(SaResult.error(e.getMessage()).toString());
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        String code = json.get("code").asText();
        if (code.equals("500")) {
            return "请求失败！";
        }
        return e.getMessage();
    }
}
