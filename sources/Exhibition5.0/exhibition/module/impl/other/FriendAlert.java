// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.other;

import exhibition.event.RegisterEvent;
import java.util.Iterator;
import exhibition.management.notifications.Notifications;
import exhibition.management.friend.FriendManager;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import exhibition.event.impl.EventPacket;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import exhibition.module.Module;

public class FriendAlert extends Module
{
    private boolean connect;
    private String name;
    private int currentY;
    private int targetY;
    Timer timer;
    
    public FriendAlert(final ModuleData data) {
        super(data);
        this.timer = new Timer();
    }
    
    @RegisterEvent(events = { EventPacket.class })
    @Override
    public void onEvent(final Event event) {
        final EventPacket ep = (EventPacket)event;
        if (ep.isIncoming() && ep.getPacket() instanceof S38PacketPlayerListItem) {
            final S38PacketPlayerListItem packet = (S38PacketPlayerListItem)ep.getPacket();
            if (packet.func_179768_b() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
                for (final Object o : packet.func_179767_a()) {
                    final S38PacketPlayerListItem.AddPlayerData data = (S38PacketPlayerListItem.AddPlayerData)o;
                    if (FriendManager.isFriend(data.field_179964_d.getName())) {
                        Notifications.getManager().post("Friend Alert", "§b" + data.field_179964_d.getName() + " has joined!", 0L, 2500L, Notifications.Type.INFO);
                    }
                }
            }
        }
    }
}
