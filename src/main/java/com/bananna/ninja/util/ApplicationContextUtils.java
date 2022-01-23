package com.bananna.ninja.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtils extends ApplicationObjectSupport {
    private static ApplicationContext context = null;

    @Override
    protected void initApplicationContext(ApplicationContext context) throws BeansException {
        super.initApplicationContext(context);
        this.context = context;
    }

    public static ApplicationContext context(){
        return context;
    }
}
