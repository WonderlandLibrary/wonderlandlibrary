// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.misc;

import java.util.Iterator;
import ru.fluger.client.ui.notification.NotificationManager;
import ru.fluger.client.ui.notification.NotificationType;
import ru.fluger.client.helpers.misc.ChatHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.packet.EventReceivePacket;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.Feature;

public class StaffAlert extends Feature
{
    private boolean isJoined;
    
    public StaffAlert() {
        super("StaffAlert", "", Type.Misc);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        if (event.getPacket() instanceof jp && ((jp)event.getPacket()).b() == jp.a.c) {
            this.isJoined = true;
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        for (final aed staffPlayer : bjq.getPlayers()) {
            if (staffPlayer != null && staffPlayer != StaffAlert.mc.h && (staffPlayer.i_().c().contains("HELPER") || staffPlayer.i_().c().contains("ST.HELPER") || staffPlayer.i_().c().contains("MODER") || staffPlayer.i_().c().contains("ST.MODER") || staffPlayer.i_().c().contains("ADMIN") || staffPlayer.i_().c().contains("\u0410\u0434\u043c\u0438\u043d") || staffPlayer.i_().c().contains("\u0425\u0435\u043b\u043f\u0435\u0440") || staffPlayer.i_().c().contains("\u041c\u043e\u0434\u0435\u0440")) && staffPlayer.T < 10) {
                if (!this.isJoined) {
                    continue;
                }
                ChatHelper.addChatMessage(ChatFormatting.WHITE + "" + ChatFormatting.RESET + staffPlayer.i_().c() + ChatFormatting.WHITE + " ");
                NotificationManager.publicity("Staff Alert", ChatFormatting.WHITE + " " + ChatFormatting.RESET + staffPlayer.i_().c() + ChatFormatting.WHITE + " ", 5, NotificationType.WARNING);
                this.isJoined = false;
            }
        }
    }
}
