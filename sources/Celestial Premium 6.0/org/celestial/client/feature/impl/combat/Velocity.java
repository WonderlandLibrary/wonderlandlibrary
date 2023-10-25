/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import java.util.Objects;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class Velocity
extends Feature {
    public static BooleanSetting cancelOtherDamage;
    public static ListSetting velocityMode;
    public static NumberSetting verticalPer;
    public static NumberSetting horizontalPer;

    public Velocity() {
        super("Velocity", "\u0423\u043c\u0435\u043d\u044c\u0448\u0430\u0435\u0442 \u043a\u043d\u043e\u043a\u0431\u044d\u043a \u043f\u0440\u0438 \u0443\u0434\u0430\u0440\u0435", Type.Combat);
        velocityMode = new ListSetting("Velocity Mode", "Packet", () -> true, "Packet", "Custom Motion", "Matrix", "AAC4+", "Strafe Cancel");
        cancelOtherDamage = new BooleanSetting("Cancel Other Damage", true, () -> true);
        verticalPer = new NumberSetting("Vertical Percentage", 0.0f, 0.0f, 100.0f, 1.0f, () -> Velocity.velocityMode.currentMode.equals("Custom Motion"));
        horizontalPer = new NumberSetting("Horizontal Percentage", 0.0f, 0.0f, 100.0f, 1.0f, () -> Velocity.velocityMode.currentMode.equals("Custom Motion"));
        this.addSettings(velocityMode, cancelOtherDamage, verticalPer, horizontalPer);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Velocity.velocityMode.currentMode.equals("Custom Motion")) {
            double hori = horizontalPer.getCurrentValue();
            double vert = verticalPer.getCurrentValue();
            this.setSuffix("H: " + hori + "% V: " + vert + "%");
        } else {
            this.setSuffix(Velocity.velocityMode.currentMode);
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        String mode = velocityMode.getOptions();
        if (cancelOtherDamage.getCurrentValue() && Velocity.mc.player.hurtTime > 0 && event.getPacket() instanceof SPacketEntityVelocity && !Velocity.mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE) && (Velocity.mc.player.isPotionActive(MobEffects.POISON) || Velocity.mc.player.isPotionActive(MobEffects.WITHER) || Velocity.mc.player.isBurning())) {
            event.setCancelled(true);
        }
        if (mode.equalsIgnoreCase("Packet")) {
            SPacketEntityVelocity velocity;
            if ((event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) && (velocity = (SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                event.setCancelled(true);
            }
        } else if (mode.equalsIgnoreCase("Strafe Cancel")) {
            if (event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) {
                if (((SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                    event.setCancelled(true);
                }
                if (Velocity.mc.player.hurtTime > 0) {
                    MovementHelper.strafePlayer();
                }
            }
        } else if (mode.equalsIgnoreCase("AAC4+")) {
            if (Velocity.mc.player.hurtTime > 0 && Velocity.mc.player.hurtTime <= 5) {
                MovementHelper.strafePlayer();
                Velocity.mc.player.onGround = true;
                Velocity.mc.player.motionX *= 0.2;
                Velocity.mc.player.motionZ *= 0.2;
            }
        } else if (mode.equalsIgnoreCase("Custom Motion")) {
            double hori = horizontalPer.getCurrentValue();
            double vert = verticalPer.getCurrentValue();
            if (((SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                Entity entity;
                if (event.getPacket() instanceof SPacketEntityVelocity && (entity = Objects.requireNonNull(Velocity.mc.getConnection()).clientWorldController.getEntityByID(((SPacketEntityVelocity)event.getPacket()).getEntityID())) instanceof EntityPlayerSP) {
                    if (hori == 0.0 && vert == 0.0) {
                        event.setCancelled(true);
                        return;
                    }
                    if (hori != 100.0) {
                        ((SPacketEntityVelocity)event.getPacket()).motionX = (int)((double)(((SPacketEntityVelocity)event.getPacket()).motionX / 100) * hori);
                        ((SPacketEntityVelocity)event.getPacket()).motionZ = (int)((double)(((SPacketEntityVelocity)event.getPacket()).motionZ / 100) * hori);
                    }
                    if (vert != 100.0) {
                        ((SPacketEntityVelocity)event.getPacket()).motionY = (int)((double)(((SPacketEntityVelocity)event.getPacket()).motionY / 100) * vert);
                    }
                }
                if (event.getPacket() instanceof SPacketExplosion) {
                    if (hori == 0.0 && vert == 0.0) {
                        event.setCancelled(true);
                        return;
                    }
                    if (hori != 100.0) {
                        ((SPacketExplosion)event.getPacket()).motionX = (int)((double)(((SPacketExplosion)event.getPacket()).motionX / 100.0f) * hori);
                        ((SPacketExplosion)event.getPacket()).motionZ = (int)((double)(((SPacketExplosion)event.getPacket()).motionZ / 100.0f) * hori);
                    }
                    if (vert != 100.0) {
                        ((SPacketExplosion)event.getPacket()).motionY = (int)((double)(((SPacketExplosion)event.getPacket()).motionY / 100.0f) * vert);
                    }
                }
            }
        }
    }
}

