package com.banking.cqrs.core.domain;

import com.banking.cqrs.core.events.BaseEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public abstract class AggregateRoot {
    protected String id;
    private int version = -1;

    private final List<BaseEvent> changes = new ArrayList<>();

    public void setVersion(int version) {
        this.version = version;
    }

    public List<BaseEvent> getUncommittedChanges() {
        return changes;
    }

    public void markChangesAsCommitted() {
        changes.clear();
    }

    protected void applyChange(BaseEvent event, Boolean isNewEvent) {
        try {
            var method = this.getClass().getMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (NoSuchMethodException e) {
            log.warn("Apply method does not exits: {}", event.getClass().getName());
        } catch (Exception e) {
            log.error("Error applying event: {}", e.getMessage());
        } finally {
            if (isNewEvent) {
                changes.add(event);
            }
        }
    }

    public void raiseEvent(BaseEvent event) {
        applyChange(event, true);
    }

    public void replayEvent(Iterable<BaseEvent> events) {
        events.forEach(event -> applyChange(event, false));
    }
}
