// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import vip.Resolute.util.render.RenderUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import vip.Resolute.events.impl.EventRender3D;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import vip.Resolute.settings.Setting;
import java.util.concurrent.CopyOnWriteArrayList;
import vip.Resolute.util.misc.TimerUtil;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.BooleanSetting;
import net.minecraft.util.Vec3;
import java.util.List;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import vip.Resolute.modules.Module;

public class Blink extends Module
{
    private ArrayList<Packet> packetList;
    private List<Vec3> crumbs;
    private BooleanSetting trail;
    private BooleanSetting blinklag;
    private NumberSetting delay;
    private TimerUtil timer;
    
    public Blink() {
        super("Blink", 0, "Cancels movement packets", Category.PLAYER);
        this.packetList = new ArrayList<Packet>();
        this.crumbs = new CopyOnWriteArrayList<Vec3>();
        this.trail = new BooleanSetting("Trail", true);
        this.blinklag = new BooleanSetting("BlinkLag", false);
        this.delay = new NumberSetting("Blink Delay", 5.0, 2.0, 30.0, 1.0);
        this.timer = new TimerUtil();
        this.addSettings(this.trail, this.blinklag, this.delay);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.crumbs.clear();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.crumbs.clear();
        try {
            for (final Packet packets : this.packetList) {
                Blink.mc.getNetHandler().sendPacketNoEvent(packets);
            }
            this.packetList.clear();
        }
        catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventPacket && (((EventPacket)e).getPacket() instanceof C0APacketAnimation || ((EventPacket)e).getPacket() instanceof C03PacketPlayer || ((EventPacket)e).getPacket() instanceof C07PacketPlayerDigging || ((EventPacket)e).getPacket() instanceof C08PacketPlayerBlockPlacement)) {
            if (this.blinklag.isEnabled()) {
                if (Blink.mc.thePlayer.ticksExisted % this.delay.getValue() == 0.0) {
                    try {
                        for (final Packet packets : this.packetList) {
                            Blink.mc.getNetHandler().sendPacketNoEvent(packets);
                        }
                        this.packetList.clear();
                        this.crumbs.clear();
                    }
                    catch (ConcurrentModificationException exception) {
                        exception.printStackTrace();
                    }
                }
                else {
                    e.setCancelled(true);
                    this.packetList.add(((EventPacket)e).getPacket());
                }
            }
            else {
                e.setCancelled(true);
                this.packetList.add(((EventPacket)e).getPacket());
            }
        }
        if (e instanceof EventRender3D) {
            if (this.timer.hasElapsed(10L)) {
                this.crumbs.add(new Vec3(Blink.mc.thePlayer.posX, Blink.mc.thePlayer.posY, Blink.mc.thePlayer.posZ));
                this.timer.reset();
            }
            if (!this.crumbs.isEmpty() && this.crumbs.size() > 2) {
                for (int i = 1; i < this.crumbs.size(); ++i) {
                    final Vec3 vecBegin = this.crumbs.get(i - 1);
                    final Vec3 vecEnd = this.crumbs.get(i);
                    final int color = getColor(255, 255, 255);
                    final float beginX = (float)((float)vecBegin.xCoord - RenderManager.renderPosX);
                    final float beginY = (float)((float)vecBegin.yCoord - RenderManager.renderPosY);
                    final float beginZ = (float)((float)vecBegin.zCoord - RenderManager.renderPosZ);
                    final float endX = (float)((float)vecEnd.xCoord - RenderManager.renderPosX);
                    final float endY = (float)((float)vecEnd.yCoord - RenderManager.renderPosY);
                    final float endZ = (float)((float)vecEnd.zCoord - RenderManager.renderPosZ);
                    final boolean bobbing = Blink.mc.gameSettings.viewBobbing;
                    Blink.mc.gameSettings.viewBobbing = false;
                    RenderUtils.drawLine3D(beginX, beginY, beginZ, endX, endY, endZ, color);
                    Blink.mc.gameSettings.viewBobbing = bobbing;
                }
            }
        }
    }
    
    public static int getColor(final int red, final int green, final int blue) {
        return getColor(red, green, blue, 255);
    }
    
    public static int getColor(final int red, final int green, final int blue, final int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        color |= blue;
        return color;
    }
}
