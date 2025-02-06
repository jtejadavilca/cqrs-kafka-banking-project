package com.banking.account.cmd.infrastructure;

import com.banking.cqrs.core.commands.BaseCommand;
import com.banking.cqrs.core.commands.CommandHandlerMethod;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {

    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();
    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> commandType, CommandHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(commandType, k -> new ArrayList<>());
        handlers.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
        var handlers = routes.get(command.getClass());
        if (handlers == null || handlers.isEmpty()) {
            throw new IllegalArgumentException("No handler for " + command.getClass());
        }

        if(handlers.size() > 1) {
            throw new IllegalArgumentException("Multiple handlers for " + command.getClass());
        }

        handlers.get(0).handle(command);
    }
}
