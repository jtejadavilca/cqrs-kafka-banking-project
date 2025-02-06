package com.banking.account.query.api.queries;

import com.banking.account.query.api.dto.EqualityType;
import com.banking.account.query.domain.AccountRepository;
import com.banking.account.query.domain.BankAccount;
import com.banking.cqrs.core.domain.BaseEntity;
import com.banking.cqrs.core.exceptions.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AccountQueryHandler implements QueryHandler {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {
        Iterable<BankAccount> bankAccounts = accountRepository.findAll();
        return StreamSupport
                .stream(bankAccounts.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery query) {
        BankAccount bankAccount = accountRepository.findById(query.getId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        return List.of(bankAccount);
    }

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        BankAccount bankAccount = accountRepository.findByAccountHolder(query.getAccountHolder())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        return List.of(bankAccount);
    }

    @Override
    public List<BaseEntity> handle(FindAccountWithBalanceQuery query) {
        List<BankAccount> bankAccounts = query.getEqualityType() == EqualityType.GREATER_THAN
                ? accountRepository.findByBalanceGreaterThan(query.getBalance())
                : accountRepository.findByBalanceLessThan(query.getBalance());
        return new ArrayList<>(bankAccounts);
    }
}
