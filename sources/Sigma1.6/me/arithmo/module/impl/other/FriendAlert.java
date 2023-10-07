/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package me.arithmo.module.impl.other;

import com.mojang.authlib.GameProfile;
import java.util.List;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.management.friend.FriendManager;
import me.arithmo.management.notifications.Notifications;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.util.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S38PacketPlayerListItem;

public class FriendAlert
extends Module {
    private boolean connect;
    private String name;
    private int currentY;
    private int targetY;
    Timer timer = new Timer();

    public FriendAlert(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventPacket.class})
    public void onEvent(Event event) {
        S38PacketPlayerListItem packet;
        EventPacket ep = (EventPacket)event;
        if (ep.isIncoming() && ep.getPacket() instanceof S38PacketPlayerListItem && (packet = (S38PacketPlayerListItem)ep.getPacket()).func_179768_b() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
            for (Object o : packet.func_179767_a()) {
                S38PacketPlayerListItem.AddPlayerData data = (S38PacketPlayerListItem.AddPlayerData)o;
                if (!FriendManager.isFriend(data.field_179964_d.getName())) continue;
                Notifications.getManager().post("Friend Alert", "\u00a7b" + data.field_179964_d.getName() + " has joined!", 2500, Notifications.Type.INFO);
            }
        }
    }
}

