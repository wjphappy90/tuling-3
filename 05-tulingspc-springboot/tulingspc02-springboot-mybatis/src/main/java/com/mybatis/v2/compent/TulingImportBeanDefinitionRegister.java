package com.mybatis.v2.compent;

import com.mybatis.v2.anno.EnableMapperScanner;
import com.mybatis.v2.factorybean.TulingMapperFactorybean;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Set;

/**
 * [来个全套]
 *
 * @slogan: 高于生活，源于生活
 * @Description: TODO
 * @author: smlz
 * @date 2020/5/5 14:29
 */
public class TulingImportBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {

	private static Class targetClass = TulingMapperFactorybean.class;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

		AnnotationAttributes attributes = (AnnotationAttributes) importingClassMetadata.getAnnotationAttributes(EnableMapperScanner.class.getName());

		//配置了EnableMapperScanner 注解
		if(attributes ==null) {
			return;
		}
		String basePackage = attributes.getString("basePackage");

		//扫描bean定义
		TulingClassPathMapperScanner mapperScanner = new TulingClassPathMapperScanner(registry);

		mapperScanner.addIncludeFilter(new TypeFilter() {
			@Override
			public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
				return true;
			}
		});


		Set<BeanDefinitionHolder> scannedBds =  mapperScanner.doScan(basePackage);

		for (BeanDefinitionHolder bdh:scannedBds) {

			GenericBeanDefinition beanDefinition = (GenericBeanDefinition) bdh.getBeanDefinition();
			String  sourceClass = beanDefinition.getBeanClassName();
			System.out.println("原生接口的class类型:"+sourceClass);
			beanDefinition.setBeanClass(targetClass);
			beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(sourceClass);
		}

		System.out.println(scannedBds);
	}
}
