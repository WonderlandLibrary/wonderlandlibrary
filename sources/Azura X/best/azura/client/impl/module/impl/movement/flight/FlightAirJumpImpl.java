package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventUpdate;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import net.minecraft.block.BlockAir;

public class FlightAirJumpImpl implements ModeImpl<Flight> {

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Air Jump";
    }

    @EventHandler
    public void handle(final Event event) {
        if (mc.thePlayer == null || mc.theWorld == null) {
            getParent().setEnabled(false);
            return;
        }
        if (event instanceof EventUpdate) {
            BlockAir.collision = true;
            BlockAir.collisionMaxY = (int) mc.thePlayer.posY;
            if (!mc.gameSettings.keyBindJump.pressed && (mc.thePlayer.fallDistance < (mc.gameSettings.keyBindSneak.pressed ? 2 : 1))) BlockAir.collision = false;
            if (mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.pressed && !mc.gameSettings.keyBindSneak.pressed) {
                mc.thePlayer.jump();
            }
        }
    }

    @Override
    public void onEnable() {
        BlockAir.collision = true;
    }

    @Override
    public void onDeselect() {
        BlockAir.collision = false;
        BlockAir.collisionMaxY = -1;
    }

    @Override
    public void onDisable() {
        BlockAir.collision = false;
        BlockAir.collisionMaxY = -1;
    }

}