package com.tuling.spring5.compent;



/**
 * 测试tulingDao
 * Created by smlz on 2019/7/7.
 */

public class TulingDao {

	private TulingDataSource tulingDataSource;

	public TulingDataSource getTulingDataSource() {
		return tulingDataSource;
	}

	public void setTulingDataSource(TulingDataSource tulingDataSource) {
		this.tulingDataSource = tulingDataSource;
	}

	public TulingDao(TulingDataSource tulingDataSource) {
		this.tulingDataSource = tulingDataSource;
		System.out.println("我是TulingDao的构造方法");
	}

	public TulingDao() {
		System.out.println("我是TulingDao的构造方法");
	}

}
