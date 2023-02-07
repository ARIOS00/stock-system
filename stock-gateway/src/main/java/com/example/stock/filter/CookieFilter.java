package com.example.stock.filter;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class CookieFilter extends AbstractPreZuulFilter {
//    @Override
//    protected Object cRun() {
//        // context is already initialized in run()
//        HttpServletRequest request = context.getRequest();
//        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURI()));
//        return success();
//    }

    private final RedisTemplate redisTemplate;

    @Autowired
    public CookieFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object run() throws ZuulException {
        context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURI()));
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            if(StringUtils.equals("userTicket", cookie.getName())) {
                log.info("get cookie successfully! {} : {}", cookie.getName(), cookie.getValue());
                Object user = redisTemplate.opsForValue().get("user:" + cookie);
                context.set("user", user);
                break;
            }
            log.warn("cannot get cookie!");
        }
        return success();
    }

    // the smaller, the more prior
    @Override
    public int filterOrder() {
        return 1;
    }
}
