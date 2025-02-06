package com.banking.account.cmd.domain;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FoundsDepositedEvent;
import com.banking.account.common.events.FoundsWithdrawnEvent;
import com.banking.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private boolean active;
    private double balance;

    public AccountAggregate(OpenAccountCommand command) {
        raiseEvent(
                AccountOpenedEvent.builder()
                        .id(command.getId())
                        .accountHolder(command.getAccountHolder())
                        .accountType(command.getAccountType())
                        .openingBalance(command.getOpeningBalance())
                        .createdDate(new Date())
                        .build()
        );
    }

    public void apply(AccountOpenedEvent event) {
        this.id = event.getId();
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    public void depositFounds(double amount) {
        if(!active) {
            throw new IllegalStateException("Account is not active");
        }

        if(amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }

        raiseEvent(
                FoundsDepositedEvent.
                        builder()
                        .id(id)
                        .amount(amount)
                        .build()
        );
    }

    public void apply(FoundsDepositedEvent event) {
        this.id = event.getId();
        this.balance += event.getAmount();
    }

    public void withdrawFounds(double amount) {
        if(!active) {
            throw new IllegalStateException("Account is not active");
        }

        if(amount <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be greater than 0");
        }

        if(balance - amount < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        raiseEvent(
                FoundsDepositedEvent.
                        builder()
                        .id(id)
                        .amount(-amount)
                        .build()
        );
    }

    public void apply(FoundsWithdrawnEvent event) {
        this.id = event.getId();
        this.balance -= event.getAmount();
    }

    public void closeAccount() {
        if(!active) {
            throw new IllegalStateException("Account is not active");
        }

        if(balance > 0) {
            throw new IllegalStateException("Account balance must be 0 to close account");
        }

        raiseEvent(
                AccountClosedEvent.builder()
                        .id(id)
                        .build()
        );
    }

    public void apply(AccountClosedEvent event) {
        this.id = event.getId();
        this.active = false;
    }

}
