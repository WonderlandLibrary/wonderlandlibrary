package dev.rise.event.impl.other;

import dev.rise.event.api.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.World;

@Getter
@Setter
@AllArgsConstructor
public class WorldChangedEvent extends Event {
    private World oldWorld, newWorld;
}
