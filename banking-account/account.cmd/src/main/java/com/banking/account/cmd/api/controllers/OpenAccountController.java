package com.banking.account.cmd.api.controllers;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.cmd.api.dto.OpenAccountResponse;
import com.banking.account.common.dto.BaseResponse;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/open-account")
public class OpenAccountController {

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand command) {
        var id = UUID.randomUUID().toString();
        try {
            command.setId(id);
            commandDispatcher.send(command);
            return new ResponseEntity<>(new OpenAccountResponse("Account successfully created", id), org.springframework.http.HttpStatus.CREATED);
        } catch (IllegalStateException ex) {
            log.warn("Error processing request: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage()));
        } catch (Exception ex) {
            log.error("An error occurred while processing the request {}", id, ex);
            return ResponseEntity.internalServerError().body(new OpenAccountResponse("An error occurred while processing the request", id));
        }
    }
}
