package com.tuling.core;

import com.tuling.anno.EnableRemoteCall;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 用于给扫描容器中的标注了@RemoateCall注解
 * Created by smlz on 2020/4/2.
 */
public class RemoteCallBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //解析EnableRemoteCall注解
        AnnotationAttributes enableRemoteCallAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(EnableRemoteCall.class.getName()));

        if(enableRemoteCallAttrs !=null) {
            registerBeanDefinitions(enableRemoteCallAttrs, registry, generateBaseBeanName(importingClassMetadata, 0));
        }
    }

    private void registerBeanDefinitions(AnnotationAttributes enableRemoteCallAttrs, BeanDefinitionRegistry registry, String beanName) {

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RemoteCallBeanDefinitionRegistryPostProcessor.class);

        //定义扫描的包路径
        List<String> scannerPackages = Arrays.asList(enableRemoteCallAttrs.getStringArray("scannerPackages"));

        builder.addPropertyValue("scannerPackages", StringUtils.collectionToCommaDelimitedString(scannerPackages));

        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }


    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
        return importingClassMetadata.getClassName() + "#" + RemoteCallBeanDefinitionRegistrar.class.getSimpleName() + "#" + index;
    }
}
