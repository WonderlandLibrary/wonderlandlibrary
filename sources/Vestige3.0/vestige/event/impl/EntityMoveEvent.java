package vestige.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.Entity;
import vestige.event.Event;

@Getter
@AllArgsConstructor
public class EntityMoveEvent extends Event {

    private Entity entity;

}