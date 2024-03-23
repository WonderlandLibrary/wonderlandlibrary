package dev.zprestige.prestige.client.managers;

import dev.zprestige.prestige.api.mixin.IPlayerInteractEntityC2SPacket;
import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.PacketSendEvent;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.util.MC;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;

public class TargetManager implements MC {
    public long ms;
    public PlayerEntity target;

    public TargetManager() {
        Prestige.Companion.getEventBus().registerListener(this);
    }

    @EventListener
    public void event(PacketSendEvent event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
            PlayerInteractEntityC2SPacket.InteractType interactType = ((IPlayerInteractEntityC2SPacket)event.getPacket()).getType().getType();
            if (interactType == PlayerInteractEntityC2SPacket.InteractType.ATTACK) {
                Entity entity = this.getMc().world.getEntityById(((IPlayerInteractEntityC2SPacket)event.getPacket()).getEntityId());
                if (entity == null) {
                    return;
                }
                if (entity instanceof PlayerEntity) {
                    setTarget((PlayerEntity)entity);
                }
            }
        }
    }

    @EventListener
    public void event(TickEvent event) {
        if (this.target != null) {
            if (getMc().world != null) {
                if (target.isAlive() && target.distanceTo(getMc().player) <= 15 && System.currentTimeMillis() - this.ms <= 5000L) {
                    return;
                }
            }
            target = null;
        }
    }

    @Override
    public MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }

    public PlayerEntity getTarget() {
        return this.target;
    }

    public void setTarget(PlayerEntity player) {
        SocialsManager socialsManager = Prestige.Companion.getSocialsManager();
        if (socialsManager.isFriend(player.getEntityName())) {
            return;
        }
        this.target = player;
        this.ms = System.currentTimeMillis();
    }
}
