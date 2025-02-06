package com.banking.account.cmd.api.command;

import com.banking.cqrs.core.commands.BaseCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class DepositFoundsCommand extends BaseCommand {
    private String Id;
    private double amount;
}
