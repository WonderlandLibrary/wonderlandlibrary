package koks.module.player;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.event.PacketEvent;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Blink", description = "You can blink", category = Module.Category.PLAYER)
public class Blink extends Module {

    private final ArrayList<Packet<?>> packets = new ArrayList<>();

    @Value(name = "DismountVehicle")
    public boolean dismountVehicle = true;

    @Value(name = "OnlyMovePackets")
    public boolean onlyMovePackets = true;

    @Override
    @Event.Info(priority = Event.Priority.EXTREME)
    public void onEvent(Event event) {
        if (event instanceof final PacketEvent packetEvent) {
            if (packetEvent.getType() == PacketEvent.Type.SEND) {
                final Packet<?> packet = packetEvent.getPacket();
                if (!(packet instanceof C00PacketKeepAlive) && (!this.onlyMovePackets || packet instanceof C03PacketPlayer)) {
                    packets.add(packet);
                    event.setCanceled(true);
                }
            }
        }
    }

    static EntityOtherPlayerMP fakeEntity;

    @Override
    public void onEnable() {
        packets.clear();
        fakeEntity = createCopy(getPlayer());
        getWorld().addEntityToWorld((int) UUID.randomUUID().getMostSignificantBits(), fakeEntity);
        if(dismountVehicle && getPlayer().ridingEntity != null)
            getPlayer().ridingEntity = null;
    }

    @Override
    public void onDisable() {
        packets.forEach(this::sendPacketUnlogged);

        packets.clear();
        if (fakeEntity != null)
            mc.theWorld.removeEntity(fakeEntity);
    }
}
