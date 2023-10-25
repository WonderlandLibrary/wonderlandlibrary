/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import baritone.events.events.player.EventUpdate;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class StaffAlert
extends Feature {
    private boolean isJoined;

    public StaffAlert() {
        super("StaffAlert", "\u0423\u0432\u0435\u0434\u043e\u043c\u043b\u044f\u0435\u0442 \u0432\u0430\u0441, \u043a\u043e\u0433\u0434\u0430 \u0445\u0435\u043b\u043f\u0435\u0440 \u0438\u043b\u0438 \u043c\u043e\u0434\u0435\u0440\u0430\u0442\u043e\u0440 \u0437\u0430\u0448\u0435\u043b \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440", Type.Misc);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        SPacketPlayerListItem packetPlayInPlayerListItem;
        if (event.getPacket() instanceof SPacketPlayerListItem && (packetPlayInPlayerListItem = (SPacketPlayerListItem)event.getPacket()).getAction() == SPacketPlayerListItem.Action.UPDATE_LATENCY) {
            this.isJoined = true;
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        for (EntityPlayer staffPlayer : GuiPlayerTabOverlay.getPlayers()) {
            if (staffPlayer == null || staffPlayer == StaffAlert.mc.player || !staffPlayer.getDisplayName().getUnformattedText().contains("HELPER") && !staffPlayer.getDisplayName().getUnformattedText().contains("SHELPER-1") && !staffPlayer.getDisplayName().getUnformattedText().contains("SHELPER") && !staffPlayer.getDisplayName().getUnformattedText().contains("SHELPER-2") && !staffPlayer.getDisplayName().getUnformattedText().contains("MODER") && !staffPlayer.getDisplayName().getUnformattedText().contains("J.MODER") || staffPlayer.ticksExisted >= 10 || !this.isJoined) continue;
            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Staff " + (Object)((Object)ChatFormatting.UNDERLINE) + staffPlayer.getName() + (Object)((Object)ChatFormatting.WHITE) + " was connected to server, or teleported!");
            NotificationManager.publicity("Staff Alert", (Object)((Object)ChatFormatting.RED) + "Staff " + (Object)((Object)ChatFormatting.UNDERLINE) + staffPlayer.getName() + (Object)((Object)ChatFormatting.WHITE) + " was connected to server, or teleported!", 10, NotificationType.WARNING);
            this.isJoined = false;
        }
    }
}

