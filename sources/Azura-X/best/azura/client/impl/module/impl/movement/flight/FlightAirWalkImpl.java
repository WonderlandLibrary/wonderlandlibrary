package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventUpdate;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import net.minecraft.block.BlockAir;

public class FlightAirWalkImpl implements ModeImpl<Flight> {

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Air Walk";
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

    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = e -> BlockAir.collisionMaxY = (int) mc.thePlayer.posY;
}