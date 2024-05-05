/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 */
package me.report.liquidware.modules.movement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import obfuscator.NativeMethod;

@ModuleInfo(name="Dash", description="", category=ModuleCategory.MOVEMENT)
public class Dash
extends Module {
    private final List<Packet> packets = new ArrayList<Packet>();
    private final LinkedList<double[]> positions = new LinkedList();
    private boolean disableLogger;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onEnable() {
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.add(new double[]{Dash.mc.field_71439_g.field_70165_t, Dash.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)(Dash.mc.field_71439_g.func_70047_e() / 2.0f), Dash.mc.field_71439_g.field_70161_v});
            this.positions.add(new double[]{Dash.mc.field_71439_g.field_70165_t, Dash.mc.field_71439_g.func_174813_aQ().field_72338_b, Dash.mc.field_71439_g.field_70161_v});
        }
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onDisable() {
        Dash.mc.field_71428_T.field_74278_d = 1.0f;
        if (Dash.mc.field_71439_g == null) {
            return;
        }
        this.blink();
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (Dash.mc.field_71439_g == null || this.disableLogger) {
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

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMotion(MotionEvent e) {
        if (MovementUtils.isMoving()) {
            Dash.mc.field_71428_T.field_74278_d = 1.0f;
            if (Dash.mc.field_71439_g.field_70122_E) {
                MovementUtils.strafe(8.0f);
                Dash.mc.field_71439_g.field_70181_x = 0.42f;
            }
            MovementUtils.strafe(8.0f);
        } else {
            Dash.mc.field_71439_g.field_70179_y = 0.0;
            Dash.mc.field_71439_g.field_70159_w = 0.0;
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
            this.positions.add(new double[]{Dash.mc.field_71439_g.field_70165_t, Dash.mc.field_71439_g.func_174813_aQ().field_72338_b, Dash.mc.field_71439_g.field_70161_v});
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void blink() {
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

