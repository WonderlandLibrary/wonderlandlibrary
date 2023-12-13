package me.earth.earthhack.impl.util.minecraft;

import com.mojang.authlib.GameProfile;
import me.earth.earthhack.api.util.Globals;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

//TODO: stuff from 1.8 client that gives fake player some movement.
public class PlayerUtil implements Globals
{
    private static final Map<Integer, EntityOtherPlayerMP> fakePlayers = new HashMap<>();

    public static EntityOtherPlayerMP createFakePlayerAndAddToWorld(GameProfile profile)
    {
        EntityOtherPlayerMP fakePlayer = createFakePlayer(profile);
        int randomID = -1000;
        while (fakePlayers.containsKey(randomID) || mc.world.getEntityByID(randomID) != null)
        {
            randomID = ThreadLocalRandom.current().nextInt(-100000, -100);
        }

        fakePlayers.put(randomID, fakePlayer);
        mc.world.addEntityToWorld(randomID, fakePlayer);
        return fakePlayer;
    }

    public static EntityOtherPlayerMP createFakePlayer(GameProfile profile)
    {
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.world, profile);
        fakePlayer.inventory.copyInventory(mc.player.inventory);
        fakePlayer.copyLocationAndAnglesFrom(mc.player);
        fakePlayer.setHealth(mc.player.getHealth());
        return fakePlayer;
    }

    /**
     * Removes the given fakeplayer.
     *
     * @param fakePlayer the fakeplayer to remove.
     */
    public static void removeFakePlayer(EntityOtherPlayerMP fakePlayer)
    {
        if (fakePlayer != null && fakePlayers.containsKey(fakePlayer.getEntityId()))
        {
            fakePlayers.remove(fakePlayer.getEntityId());
            if (mc.world != null)
            {
                mc.world.removeEntity(fakePlayer);
            }
        }
    }

    public static boolean isFakePlayer(Entity entity)
    {
        return entity != null && fakePlayers.containsKey(entity.getEntityId());
    }

    public static boolean isOtherFakePlayer(Entity entity)
    {
        return entity != null && entity.getEntityId() < 0;
    }

}
