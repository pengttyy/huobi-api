package com.pengttyy.rest.service.impl;/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/7 21:34
 */

import com.pengttyy.rest.client.HuobiRestTemplate;
import com.pengttyy.rest.client.Result;
import com.pengttyy.rest.entity.account.Account;
import com.pengttyy.rest.entity.account.AccountCurrency;
import com.pengttyy.rest.entity.account.Currency;
import com.pengttyy.rest.service.IAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Optional;

public class AccountServiceImpl implements IAccountService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HuobiRestTemplate restTemplate;

    @Override
    public List<Account> getAccounts() {
        Result<List<Account>> accounts = this.restTemplate.getForList("/v1/account/accounts", new ParameterizedTypeReference<Result<List<Account>>>() {
        });
        return accounts.getData().get();
    }

    @Override
    public Account findAccountByType(String type) {
        Optional<Account> first = this.getAccounts()
                .stream()
                .filter(t -> type.equals(t.getType()))
                .findFirst();
        return first.get();
    }

    @Override
    public String getAccountBalance(String accountId, String currency) {
        Result<AccountCurrency> accountCurrencyResult = this.restTemplate.getForBean("/v1/account/accounts/{account-id}/balance", new ParameterizedTypeReference<Result<AccountCurrency>>() {
        }, accountId);

        AccountCurrency accountCurrency = accountCurrencyResult.getData().get();
        Optional<Currency> first = accountCurrency.getList().stream()
                .filter(t -> currency.equals(t.getCurrency()) && "trade".equals(t.getType()))
                .findFirst();
        return first.get().getBalance();
    }
}
