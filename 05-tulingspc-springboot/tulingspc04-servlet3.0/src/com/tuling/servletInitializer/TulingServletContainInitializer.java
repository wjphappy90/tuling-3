package com.tuling.servletInitializer;

import com.tuling.filter.AngleFilter;
import com.tuling.handletypes.ITuling;
import com.tuling.listener.AngleListener;
import com.tuling.servlet.AngleServlet;

import javax.servlet.*;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:这个特性是servlet3.0规范8.24小节:Shared libraries / runtimes pluggability
 *       规则①:在我们的Servlet容器启动的时候,他会读取我们classpath下的jar包的所有ServletContainerInitializer
 *       的实现类的onStartup方法,但是ServletContainerInitializer的实现类必须要绑定在META-INF/services
 *       目标下,文件名称为:javax.servlet.ServletContainerInitializer
 *       内容:ServletContainerInitializer实现类的全类名路径
 *
 *       规则②:我们通过HandlesTypes来指定传入的类型到onStartup方法.
* @author: smlz
* @createDate: 2019/7/31 17:19
* @version: 1.0
*/

@HandlesTypes(value ={ITuling.class} )
public class TulingServletContainInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {

        List<ITuling> tulingList = new ArrayList<>();

        if(null!=set) {
            for(Class itulingClass:set) {
                if(!itulingClass.isInterface() && !Modifier.isAbstract(itulingClass.getModifiers()) && ITuling.class.isAssignableFrom(itulingClass)) {
                    try {
                        tulingList.add((ITuling) itulingClass.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        for(ITuling iTuling:tulingList) {
            iTuling.sayHello();
        }


        //通过ServletContext来注册我们的servlet listner
        servletContext.addListener(AngleListener.class);

        //注册Servlet
        ServletRegistration.Dynamic servletRegistration = servletContext.addServlet("angleServlet",new AngleServlet());
        servletRegistration.addMapping("/angleServlet");

        //注册Filter
        FilterRegistration.Dynamic angleFilter =servletContext.addFilter("angleFilter", AngleFilter.class);
        angleFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),true,"/*");
    }
}
