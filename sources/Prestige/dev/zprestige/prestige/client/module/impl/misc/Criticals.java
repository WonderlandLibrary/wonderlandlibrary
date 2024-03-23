package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.api.mixin.IPlayerInteractEntityC2SPacket;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.PacketSendEvent;
import dev.zprestige.prestige.client.event.impl.SwingHandEvent;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.ModeSetting;
import dev.zprestige.prestige.client.util.impl.PacketUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class Criticals extends Module {

    public ModeSetting mode;
    public boolean bruh;
    public PlayerInteractEntityC2SPacket packet;
    public int time;

    public Criticals() {
        super("Criticals", Category.Misc, "Turns hits into critical hits");
        mode = setting("Mode", "Legit", new String[]{"Legit", "Minimal", "Silent"}).description("Criticals modes: Legit = jump, Minimal = minimal jump, Silent = no jump");
    }

    @EventListener
    public void event(TickEvent event) {
        if (packet != null) {
            if (time <= 0) {
                bruh = true;
                HitResult hitResult = getMc().crosshairTarget;
                if (hitResult.getType() == HitResult.Type.ENTITY) {
                    PacketUtil.INSTANCE.sendPacket(packet);
                    getMc().player.swingHand(Hand.MAIN_HAND);
                }
                bruh = false;
                packet = null;
            } else {
                time--;
            }

        }
    }

    @EventListener
    public void event(PacketSendEvent event) {
        if (!canCrit() || bruh) {
            return;
        }
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket packet && ((IPlayerInteractEntityC2SPacket)packet).getType().getType() == PlayerInteractEntityC2SPacket.InteractType.ATTACK) {
            Entity entity = getMc().world.getEntityById(((IPlayerInteractEntityC2SPacket)packet).getEntityId());
            if (entity == null) {
                return;
            }
            if (entity instanceof PlayerEntity) {
                if (mode.getObject().equals("Silent")) {
                    sendPacket(0.0625f);
                    sendPacket(0.0f);
                } else {
                    this.packet = packet;
                    if (mode.getObject().equals("Legit")) {
                        getMc().player.jump();
                    } else {
                        getMc().player.setVelocity(new Vec3d(getMc().player.getVelocity().x, getMc().player.getVelocity().y + 0.15, getMc().player.getVelocity().z));
                    }
                    time = mode.getObject().equals("Legit") ? 6 : 1;
                    event.setCancelled();
                }
            }
        }
        if (event.getPacket() instanceof HandSwingC2SPacket && !mode.getObject().equals("Silent") && packet != null) {
            event.setCancelled();
        }
    }

    @EventListener
    public void event(SwingHandEvent event) {
        if (packet != null && time > 0) {
            event.setCancelled();
        }
    }

    @Override
    public String method224() {
        return this.mode.getObject();
    }

    private boolean canCrit() {
        return this.getMc().player != null && getMc().player.isOnGround() && !getMc().player.isSubmergedInWater() && !getMc().player.isInLava() && !getMc().player.isClimbing();
    }

    private void sendPacket(float f) {
        PacketUtil.INSTANCE.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(getMc().player.getX(), getMc().player.getY() + f, getMc().player.getZ(), false));
    }
}
