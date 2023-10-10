// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.sponge.listeners.protocol1_9to1_8;

import com.viaversion.viaversion.sponge.listeners.protocol1_9to1_8.sponge5.Sponge5ItemGrabber;
import com.viaversion.viaversion.sponge.listeners.protocol1_9to1_8.sponge4.Sponge4ItemGrabber;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.InvocationTargetException;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import org.spongepowered.api.item.inventory.ItemStack;
import java.util.Iterator;
import java.util.List;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.Sponge;
import java.util.Collection;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import com.viaversion.viaversion.api.minecraft.item.Item;
import java.util.UUID;
import java.util.Map;

public class HandItemCache implements Runnable
{
    public static boolean CACHE;
    private static Map<UUID, Item> handCache;
    private static Field GET_DAMAGE;
    private static Method GET_ID;
    private static ItemGrabber grabber;
    
    public static Item getHandItem(final UUID player) {
        return HandItemCache.handCache.get(player);
    }
    
    @Override
    public void run() {
        final List<UUID> players = new ArrayList<UUID>(HandItemCache.handCache.keySet());
        for (final Player p : Sponge.getServer().getOnlinePlayers()) {
            HandItemCache.handCache.put(p.getUniqueId(), convert(HandItemCache.grabber.getItem(p)));
            players.remove(p.getUniqueId());
        }
        for (final UUID uuid : players) {
            HandItemCache.handCache.remove(uuid);
        }
    }
    
    public static Item convert(final ItemStack itemInHand) {
        if (itemInHand == null) {
            return new DataItem(0, (byte)0, (short)0, null);
        }
        if (HandItemCache.GET_DAMAGE == null) {
            try {
                (HandItemCache.GET_DAMAGE = itemInHand.getClass().getDeclaredField("field_77991_e")).setAccessible(true);
            }
            catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if (HandItemCache.GET_ID == null) {
            try {
                HandItemCache.GET_ID = Class.forName("net.minecraft.item.Item").getDeclaredMethod("func_150891_b", Class.forName("net.minecraft.item.Item"));
            }
            catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            }
            catch (ClassNotFoundException e3) {
                e3.printStackTrace();
            }
        }
        int id = 0;
        if (HandItemCache.GET_ID != null) {
            try {
                id = (int)HandItemCache.GET_ID.invoke(null, itemInHand.getItem());
            }
            catch (IllegalAccessException e4) {
                e4.printStackTrace();
            }
            catch (InvocationTargetException e5) {
                e5.printStackTrace();
            }
        }
        int damage = 0;
        if (HandItemCache.GET_DAMAGE != null) {
            try {
                damage = (int)HandItemCache.GET_DAMAGE.get(itemInHand);
            }
            catch (IllegalAccessException e6) {
                e6.printStackTrace();
            }
        }
        return new DataItem(id, (byte)itemInHand.getQuantity(), (short)damage, null);
    }
    
    static {
        HandItemCache.CACHE = false;
        HandItemCache.handCache = new ConcurrentHashMap<UUID, Item>();
        try {
            Class.forName("org.spongepowered.api.event.entity.DisplaceEntityEvent");
            HandItemCache.grabber = new Sponge4ItemGrabber();
        }
        catch (ClassNotFoundException e) {
            HandItemCache.grabber = new Sponge5ItemGrabber();
        }
    }
}
