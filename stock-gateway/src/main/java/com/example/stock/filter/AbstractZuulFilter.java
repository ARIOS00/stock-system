package com.example.stock.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

// a general filter class
public abstract class AbstractZuulFilter extends ZuulFilter {

    // sending messages among all kinds of filters
    RequestContext context;
    // whether it should go to the next filter
    private final static String NEXT = "next";
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (boolean) ctx.getOrDefault(NEXT, true);
    }

    // run() defines the major logistics of the filter, it is invoked when shouldFilter() returns true
    @Override
    public Object run() throws ZuulException {
        context = RequestContext.getCurrentContext();
        return null;
    }

//    protected abstract Object cRun();

    Object fail(int code, String msg) {
        context.set(NEXT, false);
        context.setSendZuulResponse(false);
        context.getResponse().setContentType("text/html;charset=UTF-8");
        context.setResponseStatusCode(code);
        context.setResponseBody(String.format(
                "{\"result\":\"%s!\"}", msg
        ));
        return null;
    }

    Object success() {
        context.set(NEXT, true);
        return null;
    }

}