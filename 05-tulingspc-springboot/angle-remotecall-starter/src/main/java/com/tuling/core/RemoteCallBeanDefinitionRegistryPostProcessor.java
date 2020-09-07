package com.tuling.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * Created by smlz on 2020/4/2.
 */

public class RemoteCallBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private String scannerPackages;

    public void setScannerPackages(String scannerPackages) {
        this.scannerPackages = scannerPackages;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        ClassPathRemoteCallScanner classPathRemoteCallScanner = new ClassPathRemoteCallScanner(beanDefinitionRegistry);
        classPathRemoteCallScanner.scan(scannerPackages);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }


}
