package com.banking.account.query.infrastructure;

import com.banking.cqrs.core.domain.BaseEntity;
import com.banking.cqrs.core.infrastructure.QueryDispatcher;
import com.banking.cqrs.core.queries.BaseQuery;
import com.banking.cqrs.core.queries.QueryHandlerMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountQueryDispatcher implements QueryDispatcher {
    private final Map<Class<? extends BaseQuery>, List<QueryHandlerMethod>> routes = new HashMap<>();


    @Override
    public <T extends BaseQuery> void registerHandler(Class<T> queryType, QueryHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(queryType, k -> new ArrayList<>());
        handlers.add(handler);
    }

    @Override
    public <U extends BaseEntity> List<U> send(BaseQuery query) {
        var handlers = routes.get(query.getClass());
        if (handlers == null || handlers.isEmpty()) {
            throw new IllegalArgumentException("No handler for " + query.getClass());
        }

        if(handlers.size() > 1) {
            throw new IllegalArgumentException("Multiple handlers for " + query.getClass());
        }

        var handler = handlers.get(0);

        return handler.handle(query);
    }
}
