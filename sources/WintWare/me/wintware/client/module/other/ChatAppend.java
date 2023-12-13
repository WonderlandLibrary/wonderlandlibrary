/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.other;

import me.wintware.client.Main;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventSendPacket;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.network.play.client.CPacketChatMessage;

public class ChatAppend
extends Module {
    public ChatAppend() {
        super("ChatAppend", Category.World);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            if (((CPacketChatMessage)event.getPacket()).getMessage().startsWith("/")) {
                return;
            }
            ((CPacketChatMessage)event.getPacket()).message = ((CPacketChatMessage)event.getPacket()).getMessage() + " | " + Main.name;
        }
    }
}

