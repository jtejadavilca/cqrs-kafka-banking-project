package com.banking.account.cmd.handlers;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.cqrs.core.domain.AggregateRoot;
import com.banking.cqrs.core.events.BaseEvent;
import com.banking.cqrs.core.handlers.EventSourcingHandler;
import com.banking.cqrs.core.infrastructure.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {
    @Autowired
    private EventStore eventStore;

    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
        aggregate.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        var aggregate = new AccountAggregate();
        var events = eventStore.getEvents(id);

        aggregate.replayEvent(events);
        var latestVersion = events.stream().mapToInt(BaseEvent::getVersion).max().orElse(0);
        aggregate.setVersion(latestVersion);


        return aggregate;
    }
}
