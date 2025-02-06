package com.banking.account.cmd.api.controllers;

import com.banking.account.cmd.api.command.CloseAccountCommand;
import com.banking.account.cmd.api.dto.OpenAccountResponse;
import com.banking.account.common.dto.BaseResponse;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/close-account")
public class CloseAccountController {

    @Autowired
    private CommandDispatcher commandDispatcher;

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> openAccount(@PathVariable String id) {
        try {
            commandDispatcher.send(new CloseAccountCommand(id));
            return new ResponseEntity<>(new OpenAccountResponse("Account successfully closed", id), org.springframework.http.HttpStatus.CREATED);
        } catch (IllegalStateException ex) {
            log.warn("Error processing request: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage()));
        } catch (Exception ex) {
            log.error("An error occurred while processing the request {}", id, ex);
            return ResponseEntity.internalServerError().body(new OpenAccountResponse("An error occurred while processing the request", id));
        }
    }
}
