package koks.event;

import koks.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Getter @Setter @AllArgsConstructor
public class MouseOverEvent extends Event {
    double range;
    boolean rangeCheck;
    final Entity entity;
}
