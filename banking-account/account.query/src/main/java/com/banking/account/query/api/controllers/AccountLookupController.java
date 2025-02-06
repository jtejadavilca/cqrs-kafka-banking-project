package com.banking.account.query.api.controllers;

import com.banking.account.query.api.dto.AccountLookupResponse;
import com.banking.account.query.api.queries.FindAllAccountsQuery;
import com.banking.account.query.domain.BankAccount;
import com.banking.cqrs.core.infrastructure.QueryDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
