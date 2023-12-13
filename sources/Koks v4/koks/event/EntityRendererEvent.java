package koks.event;

import koks.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;

@AllArgsConstructor @Getter @Setter
public class EntityRendererEvent extends Event {
    Entity entity;
}
