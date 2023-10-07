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
 *  net.minecraft.world.World
 *  org.lwjgl.opengl.GL11
 */
package me.report.liquidware.modules.misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.Breadcrumbs;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.world.World;
import obfuscator.NativeMethod;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="AAC4Help", description="Cooperate with Speed Flight LongJump Phase.", category=ModuleCategory.PLAYER)
public class AAC437Helper
extends Module {
    private final List<Packet> packets = new ArrayList<Packet>();
    private EntityOtherPlayerMP fakePlayer = null;
    private boolean disableLogger;
    private final LinkedList<double[]> positions = new LinkedList();
    private final MSTimer pulseTimer = new MSTimer();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onEnable() {
        if (AAC437Helper.mc.field_71439_g == null) {
            return;
        }
        this.fakePlayer = new EntityOtherPlayerMP((World)AAC437Helper.mc.field_71441_e, AAC437Helper.mc.field_71439_g.func_146103_bH());
        this.fakePlayer.func_71049_a((EntityPlayer)AAC437Helper.mc.field_71439_g, true);
        this.fakePlayer.func_82149_j((Entity)AAC437Helper.mc.field_71439_g);
        this.fakePlayer.field_70759_as = AAC437Helper.mc.field_71439_g.field_70759_as;
        AAC437Helper.mc.field_71441_e.func_73027_a(-9100, (Entity)this.fakePlayer);
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.add(new double[]{AAC437Helper.mc.field_71439_g.field_70165_t, AAC437Helper.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)(AAC437Helper.mc.field_71439_g.func_70047_e() / 2.0f), AAC437Helper.mc.field_71439_g.field_70161_v});
            this.positions.add(new double[]{AAC437Helper.mc.field_71439_g.field_70165_t, AAC437Helper.mc.field_71439_g.func_174813_aQ().field_72338_b, AAC437Helper.mc.field_71439_g.field_70161_v});
        }
        this.pulseTimer.reset();
    }

    @Override
    public void onDisable() {
        if (AAC437Helper.mc.field_71439_g == null || this.fakePlayer == null) {
            return;
        }
        this.HuaYuTingHelper();
        AAC437Helper.mc.field_71441_e.func_73028_b(this.fakePlayer.func_145782_y());
        this.fakePlayer = null;
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (AAC437Helper.mc.field_71439_g == null || this.disableLogger) {
            return;
        }
        if (packet instanceof C03PacketPlayer) {
            event.cancelEvent();
        }
        if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C0APacketAnimation || packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity) {
            event.cancelEvent();
            this.packets.add(packet);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onUpdate(UpdateEvent event) {
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.add(new double[]{AAC437Helper.mc.field_71439_g.field_70165_t, AAC437Helper.mc.field_71439_g.func_174813_aQ().field_72338_b, AAC437Helper.mc.field_71439_g.field_70161_v});
        }
        if (this.pulseTimer.hasTimePassed(500L)) {
            this.HuaYuTingHelper();
            this.pulseTimer.reset();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onRender3D(Render3DEvent event) {
        Breadcrumbs breadcrumbs = (Breadcrumbs)LiquidBounce.moduleManager.getModule(Breadcrumbs.class);
        Color color = (Boolean)breadcrumbs.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color((Integer)breadcrumbs.colorRedValue.get(), (Integer)breadcrumbs.colorGreenValue.get(), (Integer)breadcrumbs.colorBlueValue.get());
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            GL11.glPushMatrix();
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2929);
            AAC437Helper.mc.field_71460_t.func_175072_h();
            GL11.glBegin((int)3);
            RenderUtils.glColor(color);
            double renderPosX = AAC437Helper.mc.func_175598_ae().field_78730_l;
            double renderPosY = AAC437Helper.mc.func_175598_ae().field_78731_m;
            double renderPosZ = AAC437Helper.mc.func_175598_ae().field_78728_n;
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
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public String getTag() {
        return String.valueOf("New");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void HuaYuTingHelper() {
        try {
            this.disableLogger = true;
            Iterator<Packet> packetIterator = this.packets.iterator();
            while (packetIterator.hasNext()) {
                mc.func_147114_u().func_147297_a(packetIterator.next());
                packetIterator.remove();
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

