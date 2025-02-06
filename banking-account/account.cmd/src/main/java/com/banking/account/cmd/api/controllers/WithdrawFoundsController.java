package com.banking.account.cmd.api.controllers;

import com.banking.account.cmd.api.command.DepositFoundsCommand;
import com.banking.account.cmd.api.command.WithdrawFoundsCommand;
import com.banking.account.common.dto.BaseResponse;
import com.banking.cqrs.core.exceptions.AggregateNotFoundException;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/withdraw-founds")
public class WithdrawFoundsController {
    @Autowired
    private CommandDispatcher commandDispatcher;

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> withdrawFounds(
            @PathVariable String id,
            @RequestBody WithdrawFoundsCommand command
    ) {
        try {
            command.setId(id);
            commandDispatcher.send(command);
            return ResponseEntity.ok(new BaseResponse("Founds successfully withdrawn"));
        } catch (IllegalStateException | IllegalArgumentException | AggregateNotFoundException ex) {
            log.warn("Error processing request: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage()));
        } catch (Exception ex) {
            log.error("An error occurred while processing the request {}", id, ex);
            return ResponseEntity.internalServerError().body(new BaseResponse("An error occurred while processing the request"));
        }
    }
}
