package com.banking.account.query.infrastructure.handlers;

import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FoundsDepositedEvent;
import com.banking.account.common.events.FoundsWithdrawnEvent;
import com.banking.account.query.domain.AccountRepository;
import com.banking.account.query.domain.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEventHandler implements EventHandler{

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        var bankAccount = BankAccount
                .builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .creationDate(event.getCreatedDate())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .build();

        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FoundsDepositedEvent event) {
        var bankAccount = accountRepository.findById(event.getId()).orElseThrow();
        bankAccount.setBalance(bankAccount.getBalance() + event.getAmount());

        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FoundsWithdrawnEvent event) {
        var bankAccount = accountRepository.findById(event.getId()).orElseThrow();

        bankAccount.setBalance(bankAccount.getBalance() - event.getAmount());

        accountRepository.save(bankAccount);
    }

    @Override
    public void on(AccountClosedEvent event) {
        accountRepository.deleteById(event.getId());
    }
}
