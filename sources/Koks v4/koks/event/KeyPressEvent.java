package koks.event;

import koks.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Getter @Setter @AllArgsConstructor
public class KeyPressEvent extends Event {
    final int key;
}
