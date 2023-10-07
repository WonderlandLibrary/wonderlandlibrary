// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.other;

import exhibition.event.RegisterEvent;
import java.util.Iterator;
import exhibition.util.misc.ChatUtil;
import exhibition.management.friend.Friend;
import exhibition.management.friend.FriendManager;
import net.minecraft.network.play.server.S02PacketChat;
import exhibition.event.impl.EventPacket;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class AutoTPA extends Module
{
    public AutoTPA(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventPacket.class })
    @Override
    public void onEvent(final Event event) {
        final EventPacket ep = (EventPacket)event;
        if (ep.isIncoming() && ep.getPacket() instanceof S02PacketChat) {
            final S02PacketChat s02PacketChat = (S02PacketChat)ep.getPacket();
            if (s02PacketChat.func_148915_c().getFormattedText().contains("has requested to teleport to you") || s02PacketChat.func_148915_c().getFormattedText().contains("has requested that you teleport to them")) {
                for (final Friend friend : FriendManager.friendsList) {
                    if (s02PacketChat.func_148915_c().getFormattedText().contains(friend.name) || s02PacketChat.func_148915_c().getFormattedText().contains(friend.alias.toString())) {
                        ChatUtil.sendChat_NoFilter("/tpaccept");
                        break;
                    }
                }
            }
        }
    }
}
