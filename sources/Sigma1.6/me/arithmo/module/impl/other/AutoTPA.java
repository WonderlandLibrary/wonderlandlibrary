/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.other;

import java.util.ArrayList;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.management.friend.Friend;
import me.arithmo.management.friend.FriendManager;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.util.misc.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;

public class AutoTPA
extends Module {
    public AutoTPA(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventPacket.class})
    public void onEvent(Event event) {
        EventPacket ep = (EventPacket)event;
        if (ep.isIncoming() && ep.getPacket() instanceof S02PacketChat) {
            S02PacketChat s02PacketChat = (S02PacketChat)ep.getPacket();
            if (s02PacketChat.func_148915_c().getFormattedText().contains("has requested to teleport to you") || s02PacketChat.func_148915_c().getFormattedText().contains("has requested that you teleport to them")) {
                for (Friend friend : FriendManager.friendsList) {
                    if (!s02PacketChat.func_148915_c().getFormattedText().contains(friend.name) && !s02PacketChat.func_148915_c().getFormattedText().contains(friend.alias)) continue;
                    ChatUtil.sendChat_NoFilter("/tpaccept");
                    break;
                }
            }
            if (s02PacketChat.func_148915_c().getFormattedText().contains("has invited you to join their party!")) {
                for (Friend friend : FriendManager.friendsList) {
                    if (!s02PacketChat.func_148915_c().getFormattedText().contains(friend.name) && !s02PacketChat.func_148915_c().getFormattedText().contains(friend.alias)) continue;
                    ChatUtil.sendChat_NoFilter("/party accept " + friend.name);
                    break;
                }
            }
        }
    }
}

