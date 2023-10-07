package cc.novoline.modules.move;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.events.events.PlayerUpdateEvent;
import cc.novoline.events.events.TickUpdateEvent;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import org.jetbrains.annotations.NotNull;

public class PigFly extends AbstractModule {

    private Entity entity;

    public PigFly(@NotNull ModuleManager novoline) {
        super(novoline, EnumModuleType.MOVEMENT, "PigFly", "Pig Fly");
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.OUTGOING)) {
            if (event.getPacket() instanceof C02PacketUseEntity) {
                C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();

                if (packet.getAction() == C02PacketUseEntity.Action.INTERACT) {
                    if (packet.getEntityFromWorld(mc.world) instanceof EntityPig) {
                        entity = packet.getEntityFromWorld(mc.world);
                    }
                }
            }

        } else {

        }
    }

    @EventTarget
    public void onTick(TickUpdateEvent event) {

    }

    @EventTarget
    public void onMotion(MotionUpdateEvent event) {
        if (entity != null && event.getState().equals(MotionUpdateEvent.State.PRE)) {
            sendPacketNoEvent(new CPacketVehicleMove(entity));
            event.setOnGround(false);
        }
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent event) {

    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        entity = null;
    }
}
