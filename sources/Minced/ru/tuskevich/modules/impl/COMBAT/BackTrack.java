// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.COMBAT;

import java.util.ArrayList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import ru.tuskevich.event.events.impl.EventPacket;
import ru.tuskevich.event.events.impl.EventRender;
import ru.tuskevich.event.EventTarget;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.util.movement.BackEntityCord;
import java.util.List;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "BackTrack", desc = "BackTrack", type = Type.COMBAT)
public class BackTrack extends Module
{
    long id;
    short tsid;
    int twid;
    public static List<BackEntityCord> backEntityCoords;
    public SliderSetting range;
    
    public BackTrack() {
        this.range = new SliderSetting("Range", 5.0f, 1.0f, 15.0f, 1.0f);
        this.add(this.range);
    }
    
    @EventTarget
    public void onEvent(final EventUpdate event) {
        BackTrack.backEntityCoords.forEach(BackEntityCord::tick);
        for (final BackEntityCord backEntityCoord2 : BackTrack.backEntityCoords) {
            backEntityCoord2.positions.forEach(BackEntityCord.Point::update);
            backEntityCoord2.positions.removeIf(point -> point.ticks > this.range.getIntValue());
        }
        for (final EntityPlayer entity2 : BackTrack.mc.world.playerEntities) {
            if (!entity2.isDead && !entity2.isInvisible() && entity2.isEntityAlive()) {
                if (entity2.getHealth() <= 0.0f) {
                    continue;
                }
                if (entity2.isInvisible() || !entity2.isEntityAlive() || entity2.getHealth() <= 0.0f) {
                    BackTrack.backEntityCoords.clear();
                }
                if (!BackTrack.backEntityCoords.stream().map(backEntityCoord -> backEntityCoord.entity).noneMatch(entity1 -> entity1 == entity2)) {
                    continue;
                }
                BackTrack.backEntityCoords.add(new BackEntityCord(entity2));
            }
        }
    }
    
    @EventTarget
    public void onRender(final EventRender e) {
        BackTrack.backEntityCoords.forEach(BackEntityCord::renderPositions);
    }
    
    @EventTarget
    public void onPacket(final EventPacket e) {
        if (e.getPacket() instanceof CPacketConfirmTransaction) {
            if (this.tsid == ((CPacketConfirmTransaction)e.getPacket()).getUid() && this.twid == ((CPacketConfirmTransaction)e.getPacket()).getWindowId()) {
                return;
            }
            e.cancel();
            final Minecraft mc;
            Minecraft mc2;
            Minecraft mc3;
            new Thread(() -> {
                try {
                    Thread.sleep(this.range.getIntValue() * 100);
                }
                catch (InterruptedException var5) {
                    var5.printStackTrace();
                }
                this.tsid = ((CPacketConfirmTransaction)e.getPacket()).getUid();
                this.twid = ((CPacketConfirmTransaction)e.getPacket()).getWindowId();
                mc = BackTrack.mc;
                if (Minecraft.player != null) {
                    mc2 = BackTrack.mc;
                    if (Minecraft.player.connection != null) {
                        mc3 = BackTrack.mc;
                        Minecraft.player.connection.sendPacket(e.getPacket());
                    }
                }
                return;
            }).start();
        }
        if (e.getPacket() instanceof CPacketKeepAlive) {
            if (this.id == ((CPacketKeepAlive)e.getPacket()).getKey()) {
                return;
            }
            e.cancel();
            final Minecraft mc4;
            new Thread(() -> {
                try {
                    Thread.sleep(this.range.getIntValue() * 100);
                }
                catch (InterruptedException var6) {
                    var6.printStackTrace();
                }
                this.id = ((CPacketKeepAlive)e.getPacket()).getKey();
                mc4 = BackTrack.mc;
                Minecraft.player.connection.sendPacket(e.getPacket());
            }).start();
        }
    }
    
    public static Vec3d nearestPosition(final BackEntityCord entityCoord) {
        final Vec3d vec3d;
        final Minecraft mc;
        final Vec3d vec3d2;
        final Minecraft mc2;
        final double n;
        return entityCoord.positions.stream().min((o1, o2) -> {
            vec3d = o1.vec3d;
            mc = BackTrack.mc;
            vec3d.distanceTo(Minecraft.player.getPositionVector());
            vec3d2 = o2.vec3d;
            mc2 = BackTrack.mc;
            return (int)(n - vec3d2.distanceTo(Minecraft.player.getPositionVector()));
        }).get().vec3d;
    }
    
    public static BackEntityCord getEntity(final Entity e) {
        for (final BackEntityCord backEntityCoord : BackTrack.backEntityCoords) {
            if (backEntityCoord.entity == e) {
                return backEntityCoord;
            }
        }
        return null;
    }
    
    static {
        BackTrack.backEntityCoords = new ArrayList<BackEntityCord>();
    }
}
