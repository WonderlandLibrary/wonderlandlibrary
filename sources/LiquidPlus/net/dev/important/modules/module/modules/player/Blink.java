/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.network.play.client.C0FPacketConfirmTransaction
 *  net.minecraft.world.World
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.modules.module.modules.player;

import java.awt.Color;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.Render3DEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.render.Breadcrumbs;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@Info(name="Blink", description="Suspends all movement packets.", category=Category.PLAYER, cnName="\u77ac\u79fb")
public class Blink
extends Module {
    private final LinkedBlockingQueue<Packet> packets = new LinkedBlockingQueue();
    private EntityOtherPlayerMP fakePlayer = null;
    private boolean disableLogger;
    private final LinkedList<double[]> positions = new LinkedList();
    private final BoolValue pulseValue = new BoolValue("Pulse", false);
    private final BoolValue c0FValue = new BoolValue("C0FCancel", false);
    private final IntegerValue pulseDelayValue = new IntegerValue("PulseDelay", 1000, 500, 5000, "ms");
    private final MSTimer pulseTimer = new MSTimer();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onEnable() {
        if (Blink.mc.field_71439_g == null) {
            return;
        }
        if (!((Boolean)this.pulseValue.get()).booleanValue()) {
            this.fakePlayer = new EntityOtherPlayerMP((World)Blink.mc.field_71441_e, Blink.mc.field_71439_g.func_146103_bH());
            this.fakePlayer.func_71049_a((EntityPlayer)Blink.mc.field_71439_g, true);
            this.fakePlayer.func_82149_j((Entity)Blink.mc.field_71439_g);
            this.fakePlayer.field_70759_as = Blink.mc.field_71439_g.field_70759_as;
            Blink.mc.field_71441_e.func_73027_a(-1337, (Entity)this.fakePlayer);
        }
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.add(new double[]{Blink.mc.field_71439_g.field_70165_t, Blink.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)(Blink.mc.field_71439_g.func_70047_e() / 2.0f), Blink.mc.field_71439_g.field_70161_v});
            this.positions.add(new double[]{Blink.mc.field_71439_g.field_70165_t, Blink.mc.field_71439_g.func_174813_aQ().field_72338_b, Blink.mc.field_71439_g.field_70161_v});
        }
        this.pulseTimer.reset();
    }

    @Override
    public void onDisable() {
        if (Blink.mc.field_71439_g == null) {
            return;
        }
        this.blink();
        if (this.fakePlayer != null) {
            Blink.mc.field_71441_e.func_73028_b(this.fakePlayer.func_145782_y());
            this.fakePlayer = null;
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (Blink.mc.field_71439_g == null || this.disableLogger) {
            return;
        }
        if (packet instanceof C03PacketPlayer) {
            event.cancelEvent();
        }
        if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C0APacketAnimation || packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity || ((Boolean)this.c0FValue.get()).booleanValue() && packet instanceof C0FPacketConfirmTransaction) {
            event.cancelEvent();
            this.packets.add(packet);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.add(new double[]{Blink.mc.field_71439_g.field_70165_t, Blink.mc.field_71439_g.func_174813_aQ().field_72338_b, Blink.mc.field_71439_g.field_70161_v});
        }
        if (((Boolean)this.pulseValue.get()).booleanValue() && this.pulseTimer.hasTimePassed(((Integer)this.pulseDelayValue.get()).intValue())) {
            this.blink();
            this.pulseTimer.reset();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onRender3D(Render3DEvent event) {
        Breadcrumbs breadcrumbs = (Breadcrumbs)Client.moduleManager.getModule(Breadcrumbs.class);
        Color color = (Boolean)breadcrumbs.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color((Integer)breadcrumbs.colorRedValue.get(), (Integer)breadcrumbs.colorGreenValue.get(), (Integer)breadcrumbs.colorBlueValue.get());
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            GL11.glPushMatrix();
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2929);
            Blink.mc.field_71460_t.func_175072_h();
            GL11.glBegin((int)3);
            RenderUtils.glColor(color);
            double renderPosX = Blink.mc.func_175598_ae().field_78730_l;
            double renderPosY = Blink.mc.func_175598_ae().field_78731_m;
            double renderPosZ = Blink.mc.func_175598_ae().field_78728_n;
            for (double[] pos : this.positions) {
                GL11.glVertex3d((double)(pos[0] - renderPosX), (double)(pos[1] - renderPosY), (double)(pos[2] - renderPosZ));
            }
            GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
            GL11.glEnd();
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glPopMatrix();
        }
    }

    @Override
    public String getTag() {
        return String.valueOf(this.packets.size());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void blink() {
        try {
            this.disableLogger = true;
            while (!this.packets.isEmpty()) {
                mc.func_147114_u().func_147298_b().func_179290_a(this.packets.take());
            }
            this.disableLogger = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.disableLogger = false;
        }
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.clear();
        }
    }
}

