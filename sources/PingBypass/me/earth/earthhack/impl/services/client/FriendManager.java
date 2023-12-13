package me.earth.earthhack.impl.services.client;

import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.event.events.client.FriendEvent;
import me.earth.earthhack.impl.event.events.client.FriendType;
import me.earth.earthhack.impl.services.thread.LookUp;
import me.earth.earthhack.impl.services.thread.LookUpManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//TODO: config save this
public class FriendManager implements Globals
{
    private static final FriendManager INSTANCE = new FriendManager();

    private final Map<String, UUID> friends = new ConcurrentHashMap<>();

    private FriendManager() { /* This is a Singleton. */ }

    public static FriendManager getInstance()
    {
        return INSTANCE;
    }

    public boolean isFriend(EntityPlayer player)
    {
        return isFriend(player.getName());
    }

    public boolean isFriend(String name)
    {
        return friends.containsKey(name);
    }

    public void addFriend(EntityPlayer player)
    {
        addFriend(player.getName(), player.getUniqueID());
    }

    public void addFriend(String name)
    {
        LookUpManager.getInstance().doLookUp(new LookUp(LookUp.Type.UUID, name)
        {
            @Override
            public void onSuccess()
            {
                addFriend(name, uuid);
            }

            @Override
            public void onFailure()
            {
                /* Nothing */
            }
        });
    }

    public void addFriend(String name, UUID uuid)
    {
        FriendEvent event = new FriendEvent(FriendType.ADD, name, uuid);
        Bus.EVENT_BUS.post(event);

        if (!event.isCancelled())
        {
            friends.put(name, uuid);
        }
    }

    public void removeFriend(Entity player)
    {
        if (player instanceof EntityPlayer)
        {
            removeFriend(player.getName());
        }
    }

    public void removeFriend(String name)
    {
        UUID uuid = friends.get(name);
        FriendEvent event = new FriendEvent(FriendType.REMOVE, name, uuid);
        Bus.EVENT_BUS.post(event);

        if (!event.isCancelled())
        {
            friends.remove(name);
        }
    }

    public Set<String> getFriends()
    {
        return friends.keySet();
    }

    public Map<String, UUID> getFriendsWithUUID()
    {
        return friends;
    }

}
