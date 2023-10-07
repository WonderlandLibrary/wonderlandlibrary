package koks.event;

import koks.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */

@Getter @Setter @AllArgsConstructor
public class MoveEntityEvent extends Event {
    double x,y,z;
    Entity entity;
}
