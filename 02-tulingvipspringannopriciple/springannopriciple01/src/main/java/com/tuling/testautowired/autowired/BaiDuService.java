package com.tuling.testautowired.autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * Created by smlz on 2019/6/4.
 */
@Service
public class BaiDuService {

    //@Qualifier(value = "tulingDao5")
    @Autowired
    private TulingDao tulingDao3;

    @Override
    public String toString() {
        return "BaiDuService{" +
                "tulingDao=" + tulingDao3 +
                '}';
    }

    public TulingDao getTulingDao() {
        return tulingDao3;
    }

    public void setTulingDao(TulingDao tulingDao2) {
        this.tulingDao3 = tulingDao2;
    }
}
