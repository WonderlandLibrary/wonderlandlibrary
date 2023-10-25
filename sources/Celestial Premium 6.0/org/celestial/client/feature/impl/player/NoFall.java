/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.movement.Flight;
import org.celestial.client.feature.impl.movement.Speed;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.helpers.player.InventoryHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.lwjgl.input.Mouse;

public class NoFall
extends Feature {
    public ListSetting noFallMode = new ListSetting("NoFall Mode", "Matrix Infinity", () -> true, "Matrix Infinity", "OldPosition", "Sunrise New", "Sunrise", "SunriseOneTick", "MLG", "Vanilla");
    public NumberSetting fallDelay = new NumberSetting("Fall Delay", 0.0f, 0.0f, 5.0f, 1.0f, () -> this.noFallMode.currentMode.equals("Matrix Infinity"));
    private boolean canFall;
    private int fallTicks;

    public NoFall() {
        super("NoFall", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u043c\u0435\u043d\u044c\u0448\u0438\u0439 \u0434\u0430\u043c\u0430\u0433 \u043f\u0440\u0438 \u043f\u0430\u0434\u0435\u043d\u0438\u0438", Type.Player);
        this.addSettings(this.noFallMode, this.fallDelay);
    }

    @Override
    public void onEnable() {
        if (this.noFallMode.currentMode.equals("OldPosition")) {
            ChatHelper.addChatMessage("Please press mouse middle button for better effect!");
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (this.noFallMode.currentMode.equals("OldPosition")) {
            NoFall.mc.player.connection.sendPacket(new CPacketPlayer.Position(NoFall.mc.player.posX, NoFall.mc.player.posY + 2.0, NoFall.mc.player.posZ, true));
        }
        super.onDisable();
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = this.noFallMode.getOptions();
        this.setSuffix(mode);
        if (NoFall.mc.player == null || NoFall.mc.world == null) {
            return;
        }
        if (Celestial.instance.featureManager.getFeatureByClass(Flight.class).getState()) {
            return;
        }
        if (Celestial.instance.featureManager.getFeatureByClass(Speed.class).getState()) {
            return;
        }
        if (MovementHelper.isUnderBedrock()) {
            return;
        }
        if (mode.equalsIgnoreCase("Vanilla")) {
            if (NoFall.mc.player.fallDistance > 3.0f) {
                NoFall.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(NoFall.mc.player.posX, NoFall.mc.player.posY, NoFall.mc.player.posZ, NoFall.mc.player.rotationYaw, NoFall.mc.player.rotationPitch, true));
                NoFall.mc.player.fallDistance = 0.0f;
            }
        } else if (mode.equalsIgnoreCase("SunriseOneTick")) {
            if (!NoFall.mc.player.onGround && NoFall.mc.player.fallDistance >= 2.5f) {
                NoFall.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(NoFall.mc.player.posX, NoFall.mc.player.posY, NoFall.mc.player.posZ, NoFall.mc.player.rotationYaw, NoFall.mc.player.rotationPitch, false));
                event.setOnGround(true);
                NoFall.mc.player.fallDistance = 0.0f;
                if (NoFall.mc.player.hurtTime > 0) {
                    event.setOnGround(false);
                    NoFall.mc.player.connection.sendPacket(new CPacketKeepAlive((long)(-MathematicHelper.randomizeFloat(44555.0f, 2.3233224E7f))));
                    NoFall.mc.player.connection.sendPacket(new CPacketSpectate(NoFall.mc.player.getUniqueID()));
                    event.setOnGround(true);
                    NoFall.mc.player.motionY = -5000.0;
                }
            }
        } else if (mode.equalsIgnoreCase("Sunrise New")) {
            if (!NoFall.mc.player.onGround && NoFall.mc.player.fallDistance >= 4.0f) {
                NoFall.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                NoFall.mc.player.motionY = 0.0;
                event.setOnGround(true);
                NoFall.mc.player.fallDistance = 0.0f;
            }
        } else if (mode.equalsIgnoreCase("Sunrise")) {
            if (!NoFall.mc.player.onGround && NoFall.mc.player.fallDistance >= 2.5f) {
                event.setOnGround(false);
                event.setPosY(NoFall.mc.player.ticksExisted % 2 != 1 ? event.getPosY() + (double)0.14f : event.getPosY() + (double)0.09f);
            }
        } else if (mode.equalsIgnoreCase("OldPosition")) {
            event.setOnGround(false);
            if (Mouse.isButtonDown(2)) {
                NoFall.mc.player.motionY = NoFall.mc.player.ticksExisted % 2 != 1 ? NoFall.mc.player.motionY + (double)0.08f : NoFall.mc.player.motionZ + (double)0.05f;
            }
        } else if (mode.equalsIgnoreCase("Matrix Infinity")) {
            ++this.fallTicks;
            if (NoFall.mc.player.fallDistance >= 4.0f && !this.canFall && this.fallTicks > 0) {
                NoFall.mc.player.motionX *= 0.0;
                NoFall.mc.player.motionZ *= 0.0;
                NoFall.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(NoFall.mc.player.posX, NoFall.mc.player.posY, NoFall.mc.player.posZ, NoFall.mc.player.rotationYaw, NoFall.mc.player.rotationPitch, true));
                NoFall.mc.player.motionY *= 0.0;
                NoFall.mc.player.fallDistance = -this.fallDelay.getCurrentValue();
                this.fallTicks = 0;
                this.canFall = true;
            } else {
                this.canFall = false;
            }
        } else if (mode.equalsIgnoreCase("MLG")) {
            RayTraceResult traceResult;
            int oldSlot = NoFall.mc.player.inventory.currentItem;
            if (NoFall.mc.player.fallDistance > 5.0f && (traceResult = NoFall.mc.world.rayTraceBlocks(NoFall.mc.player.getPositionVector(), NoFall.mc.player.getPositionVector().add(0.0, -15.0, 0.0), true, false, false)) != null && InventoryHelper.findWaterBucket() > 0 && traceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                event.setPitch(86.0f);
                NoFall.mc.player.inventory.currentItem = InventoryHelper.findWaterBucket();
                for (int i = 0; i < 5; ++i) {
                    NoFall.mc.playerController.processRightClick(NoFall.mc.player, NoFall.mc.world, EnumHand.MAIN_HAND);
                }
                NoFall.mc.player.inventory.currentItem = oldSlot;
            }
        }
    }
}

