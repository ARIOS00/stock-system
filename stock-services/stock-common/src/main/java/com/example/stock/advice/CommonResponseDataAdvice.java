package com.example.stock.advice;

import com.example.stock.annotation.IgnoreResponseAdivce;
import com.example.stock.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.xml.ws.Response;
// enhance rest response (postprocess)
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    // judge if it needs postprocess
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // if the class of the method is annotated with @IgnoreResponseAdvice, then need not postprocess
        if(methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdivce.class)) return false;
        // if the method is annotated with @IgnoreResponseAdvice, then need not postprocess
        if(methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdivce.class)) return false;
        return true;
    }

    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // define final return obj
        CommonResponse<Object> response = new CommonResponse<>(0,"");
        if(o == null) return response;
        else if(o instanceof CommonResponse) {
            response = (CommonResponse<Object>) o;
        } else {
            response.setContent(o);
        }
        return response;
    }
}