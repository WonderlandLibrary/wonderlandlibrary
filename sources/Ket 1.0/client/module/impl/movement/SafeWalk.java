package client.module.impl.movement;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MoveEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.value.impl.BooleanValue;

@ModuleInfo(name = "Safe Walk", description = "Safes you on block corner", category = Category.MOVEMENT)
public class SafeWalk extends Module {
    private final BooleanValue ground = new BooleanValue("Ground", this, false);
    @EventLink
    public final Listener<MoveEvent> onMove = event -> {
        if (!ground.getValue() || mc.thePlayer.onGround) event.setSafeWalk(true);
    };
}
