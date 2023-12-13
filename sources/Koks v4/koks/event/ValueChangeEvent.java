package koks.event;

import koks.api.event.Event;
import koks.api.manager.value.Value;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */


@Getter @Setter @AllArgsConstructor
public class ValueChangeEvent extends Event {
    private final Value<?> value;
}