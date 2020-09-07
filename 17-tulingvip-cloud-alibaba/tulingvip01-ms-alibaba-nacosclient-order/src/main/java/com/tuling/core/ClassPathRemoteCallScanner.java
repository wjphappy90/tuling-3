package com.tuling.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

/**
 * Created by smlz on 2020/4/2.
 */
@Slf4j
public class ClassPathRemoteCallScanner extends ClassPathBeanDefinitionScanner {

    private Class<? extends RemoteCallFactoryBean> remoteCallFactoryBeanClass = RemoteCallFactoryBean.class;


    public ClassPathRemoteCallScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        /**
         * 调用父类ClassPathBeanDefinitionScanner 来进行扫描
         */
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        /**
         * 若扫描后 我们mapper包下有接口类,那么扫描bean定义就不会为空
         */
        if (beanDefinitions.isEmpty()) {
            logger.warn("没有对应的RemoteCall找到");
        } else {
            
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {

        GenericBeanDefinition definition;

        for (BeanDefinitionHolder holder : beanDefinitions) {
            // 获取我们的bean定义
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            // 获取我们的bean定义的名称
            String beanClassName = definition.getBeanClassName();

            //修改具体的bean定义
            definition.setBeanClass(remoteCallFactoryBeanClass);

            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);

            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        }
    }
}
