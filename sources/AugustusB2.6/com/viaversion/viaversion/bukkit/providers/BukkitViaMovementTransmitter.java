// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.providers;

import org.bukkit.entity.Player;
import java.lang.reflect.InvocationTargetException;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.MovementTracker;
import org.bukkit.Bukkit;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.bukkit.util.NMSUtil;
import com.viaversion.viaversion.api.Via;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;

public class BukkitViaMovementTransmitter extends MovementTransmitterProvider
{
    private static boolean USE_NMS;
    private Object idlePacket;
    private Object idlePacket2;
    private Method getHandle;
    private Field connection;
    private Method handleFlying;
    
    public BukkitViaMovementTransmitter() {
        BukkitViaMovementTransmitter.USE_NMS = Via.getConfig().isNMSPlayerTicking();
        Class<?> idlePacketClass;
        try {
            idlePacketClass = NMSUtil.nms("PacketPlayInFlying");
        }
        catch (ClassNotFoundException e3) {
            return;
        }
        try {
            this.idlePacket = idlePacketClass.newInstance();
            this.idlePacket2 = idlePacketClass.newInstance();
            final Field flying = idlePacketClass.getDeclaredField("f");
            flying.setAccessible(true);
            flying.set(this.idlePacket2, true);
        }
        catch (NoSuchFieldException | InstantiationException | IllegalArgumentException | IllegalAccessException ex5) {
            final Exception ex;
            final Exception e = ex;
            throw new RuntimeException("Couldn't make player idle packet, help!", e);
        }
        if (BukkitViaMovementTransmitter.USE_NMS) {
            try {
                this.getHandle = NMSUtil.obc("entity.CraftPlayer").getDeclaredMethod("getHandle", (Class<?>[])new Class[0]);
            }
            catch (NoSuchMethodException | ClassNotFoundException ex6) {
                final ReflectiveOperationException ex2;
                final ReflectiveOperationException e2 = ex2;
                throw new RuntimeException("Couldn't find CraftPlayer", e2);
            }
            try {
                this.connection = NMSUtil.nms("EntityPlayer").getDeclaredField("playerConnection");
            }
            catch (NoSuchFieldException | ClassNotFoundException ex7) {
                final ReflectiveOperationException ex3;
                final ReflectiveOperationException e2 = ex3;
                throw new RuntimeException("Couldn't find Player Connection", e2);
            }
            try {
                this.handleFlying = NMSUtil.nms("PlayerConnection").getDeclaredMethod("a", idlePacketClass);
            }
            catch (NoSuchMethodException | ClassNotFoundException ex8) {
                final ReflectiveOperationException ex4;
                final ReflectiveOperationException e2 = ex4;
                throw new RuntimeException("Couldn't find CraftPlayer", e2);
            }
        }
    }
    
    @Override
    public Object getFlyingPacket() {
        if (this.idlePacket == null) {
            throw new NullPointerException("Could not locate flying packet");
        }
        return this.idlePacket2;
    }
    
    @Override
    public Object getGroundPacket() {
        if (this.idlePacket == null) {
            throw new NullPointerException("Could not locate flying packet");
        }
        return this.idlePacket;
    }
    
    @Override
    public void sendPlayer(final UserConnection info) {
        if (BukkitViaMovementTransmitter.USE_NMS) {
            final Player player = Bukkit.getPlayer(info.getProtocolInfo().getUuid());
            if (player != null) {
                try {
                    final Object entityPlayer = this.getHandle.invoke(player, new Object[0]);
                    final Object pc = this.connection.get(entityPlayer);
                    if (pc != null) {
                        this.handleFlying.invoke(pc, info.get(MovementTracker.class).isGround() ? this.idlePacket2 : this.idlePacket);
                        info.get(MovementTracker.class).incrementIdlePacket();
                    }
                }
                catch (IllegalAccessException | InvocationTargetException ex2) {
                    final ReflectiveOperationException ex;
                    final ReflectiveOperationException e = ex;
                    e.printStackTrace();
                }
            }
        }
        else {
            super.sendPlayer(info);
        }
    }
    
    static {
        BukkitViaMovementTransmitter.USE_NMS = true;
    }
}
