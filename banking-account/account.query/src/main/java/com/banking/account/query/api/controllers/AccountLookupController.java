package com.banking.account.query.api.controllers;

import com.banking.account.query.api.dto.AccountLookupResponse;
import com.banking.account.query.api.dto.EqualityType;
import com.banking.account.query.api.queries.FindAccountByHolderQuery;
import com.banking.account.query.api.queries.FindAccountByIdQuery;
import com.banking.account.query.api.queries.FindAccountWithBalanceQuery;
import com.banking.account.query.api.queries.FindAllAccountsQuery;
import com.banking.account.query.domain.BankAccount;
import com.banking.cqrs.core.infrastructure.QueryDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountLookupController {

    @Autowired
    private QueryDispatcher queryDispatcher;

    @GetMapping
    public ResponseEntity<AccountLookupResponse> getAllAccounts() {
        log.info("Get all accounts");
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAllAccountsQuery());
            AccountLookupResponse response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("All accounts")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<AccountLookupResponse> getById(@PathVariable("id") String id) {
        log.info("Get accounts by id: {}", id);
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByIdQuery(id));
            AccountLookupResponse response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(String.format("Account by id: %s", id))
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/by-holder/{holder}")
    public ResponseEntity<AccountLookupResponse> getByOwner(@PathVariable("holder") String holder) {
        log.info("Get accounts by holder: {}", holder);
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByHolderQuery(holder));
            AccountLookupResponse response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(String.format("Account by holder: %s", holder))
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/balance/{equalityType}")
    public ResponseEntity<AccountLookupResponse> getBalance(@PathVariable("equalityType") EqualityType equalityType, @RequestParam("balance") double balance) {
        log.info("Get accounts by balance: {} and equality {}", balance, equalityType);
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountWithBalanceQuery(balance, equalityType));
            AccountLookupResponse response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Accounts by balance")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

}
