package com.codesqaude.cocomarco.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextServe implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
        logger.debug(context.toString());
        logger.debug(applicationContext.toString());
    }

}