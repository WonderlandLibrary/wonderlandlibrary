/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.other;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.event.impl.EventRender3D;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.PlayerUtil;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.Timer;
import me.arithmo.util.render.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;

public class Blink
extends Module {
    private List<Packet> packets = new CopyOnWriteArrayList<Packet>();
    private List<Vec3> crumbs = new CopyOnWriteArrayList<Vec3>();
    private String BREADCRUMBS = "CRUMBS";
    private Timer timer = new Timer();

    public Blink(ModuleData data) {
        super(data);
        this.settings.put(this.BREADCRUMBS, new Setting<Boolean>(this.BREADCRUMBS, true, "Draws a line on your blink path."));
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
        for (Packet packet : this.packets) {
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packet);
        }
        this.packets.clear();
    }

    @RegisterEvent(events={EventPacket.class, EventRender3D.class})
    public void onEvent(Event event) {
        this.setSuffix((PlayerUtil.isMoving() ? "\u00a79" : "\u00a78") + this.packets.size());
        if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket)event;
            if (ep.isOutgoing() && ep.getPacket() instanceof C03PacketPlayer && PlayerUtil.isMoving()) {
                this.packets.add(ep.getPacket());
            }
            event.setCancelled(true);
        }
        if (event instanceof EventRender3D && ((Boolean)((Setting)this.settings.get(this.BREADCRUMBS)).getValue()).booleanValue()) {
            if (this.timer.delay(50.0f)) {
                this.crumbs.add(new Vec3(Blink.mc.thePlayer.posX, Blink.mc.thePlayer.posY, Blink.mc.thePlayer.posZ));
                this.timer.reset();
            }
            if (!this.crumbs.isEmpty() && this.crumbs.size() > 2) {
                for (int i = 1; i < this.crumbs.size(); ++i) {
                    Vec3 vecBegin = this.crumbs.get(i - 1);
                    Vec3 vecEnd = this.crumbs.get(i);
                    int color = Colors.getColor(164, 24, 188);
                    float beginX = (float)((double)((float)vecBegin.xCoord) - RenderManager.renderPosX);
                    float beginY = (float)((double)((float)vecBegin.yCoord) - RenderManager.renderPosY);
                    float beginZ = (float)((double)((float)vecBegin.zCoord) - RenderManager.renderPosZ);
                    float endX = (float)((double)((float)vecEnd.xCoord) - RenderManager.renderPosX);
                    float endY = (float)((double)((float)vecEnd.yCoord) - RenderManager.renderPosY);
                    float endZ = (float)((double)((float)vecEnd.zCoord) - RenderManager.renderPosZ);
                    boolean bobbing = Blink.mc.gameSettings.viewBobbing;
                    Blink.mc.gameSettings.viewBobbing = false;
                    RenderingUtil.drawLine3D(beginX, beginY, beginZ, endX, endY, endZ, color);
                    Blink.mc.gameSettings.viewBobbing = bobbing;
                }
            }
        }
    }
}

