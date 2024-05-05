package me.earth.earthhack.impl.modules.client.pingbypass.serializer.friend;

import me.earth.earthhack.api.event.bus.SubscriberImpl;
import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.event.events.client.FriendEvent;
import me.earth.earthhack.impl.event.events.client.FriendType;
import me.earth.earthhack.impl.modules.client.pingbypass.serializer.Serializer;
import me.earth.earthhack.impl.services.client.FriendManager;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Invokes the friend command on the
 * PingBypass.
 */
public class FriendSerializer extends SubscriberImpl implements Serializer<FriendEvent>, Globals
{
    private final Set<FriendEvent> changed  = new LinkedHashSet<>();
    private boolean cleared;

    public FriendSerializer()
    {
        this.listeners.add(new FriendListener(this));
        this.listeners.add(new TickListener(this));
        this.listeners.add(new DisconnectListener(this));
    }

    public void clear()
    {
        synchronized (changed)
        {
            changed.clear();
            FriendManager.getInstance().getFriendsWithUUID().forEach((k, v) ->
            {
                FriendEvent event = new FriendEvent(FriendType.ADD, k, v);
                changed.add(event);
            });
            cleared = true;
        }
    }

    protected void onChange(FriendEvent event)
    {
        if (!event.isCancelled())
        {
            changed.add(event);
        }
    }

    protected void onTick()
    {
        if (mc.player != null && mc.getConnection() != null && !changed.isEmpty())
        {
            if (cleared)
            {
                mc.getConnection().sendPacket(new CPacketChatMessage("@ServerFriend clear"));
                cleared = false;
            }

            FriendEvent friend = pollFriend();
            if (friend != null)
            {
                serializeAndSend(friend);
            }
        }
    }

    @Override
    public void serializeAndSend(FriendEvent event)
    {
        String command = "@ServerFriend";
        if (event.getType() == FriendType.ADD)
        {
            command += " add " + event.getName() + " " + event.getUuid();
        }
        else
        {
            command += " del " + event.getName();
        }

        Earthhack.logger.info(command);
        CPacketChatMessage packet = new CPacketChatMessage(command);
        Objects.requireNonNull(mc.getConnection()).sendPacket(packet);
    }

    private FriendEvent pollFriend()
    {
        if (!changed.isEmpty())
        {
            FriendEvent friend = changed.iterator().next();
            changed.remove(friend);
            return friend;
        }

        return null;
    }

}
