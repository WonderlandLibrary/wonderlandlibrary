/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import org.celestial.client.cmd.impl.ClipCommand;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.input.EventMouse;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.visuals.EntityESP;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;
import org.lwjgl.input.Mouse;

public class ClipHelper
extends Feature {
    public static NumberSetting maxDistance;
    public static BooleanSetting disableBlockLight;
    public static BooleanSetting sunriseBypass;
    private boolean wasClick;

    public ClipHelper() {
        super("ClipHelper", "\u041a\u043b\u0438\u043f\u0430\u0435\u0442\u0441\u044f \u043f\u043e Y \u043e\u0441\u0438 \u043f\u0440\u0438 \u043d\u0430\u0436\u0430\u0442\u0438\u0438 \u043d\u0430 \u043a\u043e\u043b\u0435\u0441\u043e \u043c\u044b\u0448\u0438 \u043f\u043e \u0438\u0433\u0440\u043e\u043a\u0443", Type.Player);
        maxDistance = new NumberSetting("Max Distance", 50.0f, 5.0f, 200.0f, 1.0f, () -> true);
        disableBlockLight = new BooleanSetting("Disable block light", true, () -> true);
        sunriseBypass = new BooleanSetting("Sunrise Bypass", true, () -> true);
        this.addSettings(maxDistance, disableBlockLight, sunriseBypass);
    }

    @Override
    public void onDisable() {
        ClipHelper.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (ClipHelper.mc.player == null || ClipHelper.mc.world == null) {
            return;
        }
        if (!sunriseBypass.getCurrentValue()) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            if (RotationHelper.isLookingAtEntity(false, ClipHelper.mc.player.rotationYaw, ClipHelper.mc.player.rotationPitch, 0.2f, 0.2f, 0.2f, EntityESP.clipEntity, maxDistance.getCurrentValue())) {
                if (Mouse.isButtonDown(2)) {
                    ClipHelper.mc.player.posY += ClipHelper.mc.player.ticksExisted % 2 == 0 ? 0.2 : -0.1;
                    packet.x = EntityESP.clipEntity.posX;
                    packet.y = EntityESP.clipEntity.posY;
                    packet.z = EntityESP.clipEntity.posZ;
                }
                if (this.wasClick) {
                    NotificationManager.publicity("Click TP", (Object)((Object)ChatFormatting.GREEN) + "Teleporting " + (Object)((Object)ChatFormatting.WHITE) + "to " + (Object)((Object)ChatFormatting.RED) + EntityESP.clipEntity.getName() + "...", 4, NotificationType.INFO);
                    this.wasClick = false;
                }
            }
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (ClipCommand.canClip) {
            if (!MovementHelper.airBlockAbove2()) {
                ClipCommand.canClip = false;
                NotificationManager.publicity("Clip Helper", "Please " + (Object)((Object)ChatFormatting.RED) + "destroy " + (Object)((Object)ChatFormatting.WHITE) + "block above " + (Object)((Object)ChatFormatting.GREEN) + "your head!", 5, NotificationType.ERROR);
                ChatHelper.addChatMessage("Please " + (Object)((Object)ChatFormatting.RED) + "destroy " + (Object)((Object)ChatFormatting.WHITE) + "block above " + (Object)((Object)ChatFormatting.GREEN) + "your head!");
                ClipHelper.mc.timer.timerSpeed = 1.0f;
                return;
            }
            if (ClipHelper.mc.player.hurtTime <= 0 && ClipHelper.mc.player.posY != ClipCommand.playerPosY) {
                int i;
                ClipHelper.mc.timer.timerSpeed = 0.3f;
                for (i = 0; i < 2; ++i) {
                    ClipHelper.mc.player.connection.sendPacket(new CPacketEntityAction(ClipHelper.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                }
                event.setOnGround(false);
                ClipHelper.mc.player.connection.sendPacket(new CPacketPlayer.Position(ClipHelper.mc.player.posX, ClipHelper.mc.player.posY, ClipHelper.mc.player.posZ, false));
                for (i = 0; i < 8; ++i) {
                    ClipHelper.mc.player.connection.sendPacket(new CPacketPlayer.Position(ClipHelper.mc.player.posX, ClipHelper.mc.player.posY + (double)0.399f, ClipHelper.mc.player.posZ, false));
                    ClipHelper.mc.player.connection.sendPacket(new CPacketPlayer.Position(ClipHelper.mc.player.posX, ClipHelper.mc.player.posY, ClipHelper.mc.player.posZ, false));
                }
                for (i = 0; i < 8; ++i) {
                    ClipHelper.mc.player.connection.sendPacket(new CPacketPlayer.Position(ClipHelper.mc.player.posX, ClipHelper.mc.player.posY, ClipHelper.mc.player.posZ, true));
                }
                ClipHelper.mc.player.setPositionAndUpdate(ClipHelper.mc.player.posX, ClipCommand.playerPosY, ClipHelper.mc.player.posZ);
            }
            ClipCommand.canClip = false;
            ClipHelper.mc.timer.timerSpeed = 1.0f;
        }
    }

    @EventTarget
    public void onMouse(EventMouse event) {
        if (ClipHelper.mc.player == null || ClipHelper.mc.world == null) {
            return;
        }
        Entity entity = EntityESP.clipEntity;
        if (RotationHelper.isLookingAtEntity(false, ClipHelper.mc.player.rotationYaw, ClipHelper.mc.player.rotationPitch, 0.2f, 0.2f, 0.2f, entity, maxDistance.getCurrentValue())) {
            int findToClip = (int)entity.posY;
            if (ClipHelper.mc.gameSettings.thirdPersonView == 0) {
                if (event.key == 2) {
                    this.wasClick = true;
                    ClipHelper.mc.player.setPositionAndUpdate(entity.posX, (double)findToClip + 0.05, entity.posZ);
                    ClipHelper.mc.player.setPositionAndUpdate(ClipHelper.mc.player.posX, (double)findToClip + 0.05, ClipHelper.mc.player.posZ);
                }
                if (!sunriseBypass.getCurrentValue()) {
                    ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "clipped to entity " + (Object)((Object)ChatFormatting.RED) + entity.getName() + (Object)((Object)ChatFormatting.RED) + " on X: " + (Object)((Object)ChatFormatting.RED) + (int)entity.posX + " Y: " + (Object)((Object)ChatFormatting.RED) + findToClip + " Z: " + (int)entity.posZ);
                }
            }
        }
    }
}

