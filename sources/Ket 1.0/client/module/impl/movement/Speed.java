package client.module.impl.movement;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.WorldLoadEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.impl.movement.speed.*;
import client.value.impl.ModeValue;

@ModuleInfo(name = "Speed", description = "Increases your movement speed", category = Category.MOVEMENT)
public class Speed extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new VanillaSpeed("Vanilla", this))
            .add(new VerusSpeed("Verus", this))
            .add(new MushSpeed("Mush", this))
            .add(new HypixelSpeed("Hypixel", this))
            .add(new TestSpeed("Test", this))
            .setDefault("Vanilla");
    @EventLink
    public final Listener<WorldLoadEvent> onWorldLoad = event -> toggle();
}
