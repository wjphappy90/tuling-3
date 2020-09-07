package com.tuling.testautowired.autowiredinmethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by smlz on 2019/5/24.
 */
@Component
public class TulingAspect {

    private TulingLog tulingLog;

    public TulingLog getTulingLog() {
        return tulingLog;
    }


    public void setTulingLog(TulingLog tulingLog) {
        System.out.println("set注入");
        this.tulingLog = tulingLog;
    }

    @Override
    public String toString() {
        return "TulingAspect{" +
                "tulingLog=" + tulingLog +
                '}';
    }


    public TulingAspect(TulingLog tulingLog) {
        System.out.println("构造方法注入");
        this.tulingLog = tulingLog;
    }

    public TulingAspect() {
        System.out.println("11111111");
    }
}
