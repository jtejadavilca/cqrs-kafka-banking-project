package com.banking.account.common.events;

import com.banking.cqrs.core.events.BaseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class AccountClosedEvent extends BaseEvent {
}
