package com.mybatis.v1.service;

import com.mybatis.v1.dao.AccountMapper;
import com.mybatis.v1.entity.AccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * [来个全套]
 *
 * @slogan: 高于生活，源于生活
 * @Description: TODO
 * @author: smlz
 * @date 2020/5/4 20:11
 */
@Service
public class AccountServiceImpl {

	@Autowired(required = false)
	private AccountMapper accountMapper;

	public AccountInfo qryById(String accountId) {
		System.out.println(accountMapper);
		return accountMapper.qryById(accountId);
	}
}
