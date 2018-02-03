package com.pengttyy.rest.service;

import com.pengttyy.rest.entity.account.Account;

import java.util.List;

/**
 * 账户信息相关接口
 *
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/7 21:27
 */
public interface IAccountService {

    /**
     * 获取所有账户信息
     *
     * @return
     */
    List<Account> getAccounts();

    /**
     * 根据类型查询账户
     *
     * @param type 账户类型
     * @return
     */
    Account findAccountByType(String type);

    /**
     * 获取账户余额
     *
     * @param accountId 账户id
     * @param currency  币种
     * @return
     */
    String getAccountBalance(String accountId, String currency);

}
