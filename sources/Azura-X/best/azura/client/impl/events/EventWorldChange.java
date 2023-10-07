package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;
import net.minecraft.world.World;

public class EventWorldChange implements NamedEvent {
    public World lastWorld, newWorld;
    public EventWorldChange(World lastWorld, World newWorld) {
        this.lastWorld = lastWorld;
        this.newWorld = newWorld;
    }

    @Override
    public String name() {
        return "worldChange";
    }
}