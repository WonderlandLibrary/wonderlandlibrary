/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.handshake.client.C00Handshake
 *  net.minecraft.network.play.client.C00PacketKeepAlive
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.network.play.client.C0CPacketInput
 *  net.minecraft.network.play.client.C0FPacketConfirmTransaction
 */
package me.report.liquidware.modules.fun;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import obfuscator.NativeMethod;

@ModuleInfo(name="AAC5Disabler", description="New AAC5Disabler / Report1337 ", category=ModuleCategory.FUN)
public final class AAC5Diabler
extends Module {
    private final Queue<Short> queueID = new ConcurrentLinkedQueue<Short>();
    private final ListValue modeValue = new ListValue("Mode", new String[]{"AAC5Helper", "AAC5Movement"}, "AAC5Movement");

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onWorld(WorldEvent event) {
        if (event.getWorldClient() != null && !this.queueID.isEmpty()) {
            return;
        }
        this.queueID.clear();
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onPacket(PacketEvent event) {
        if (AAC5Diabler.mc.field_71439_g == null || AAC5Diabler.mc.field_71441_e == null) {
            return;
        }
        Packet<?> packet = event.getPacket();
        int uid = -1;
        if (((String)this.modeValue.get()).equals("AAC5Helper")) {
            if (packet instanceof C08PacketPlayerBlockPlacement) {
                mc.func_147114_u().func_147297_a((Packet)new C0FPacketConfirmTransaction());
                mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction());
                mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging());
            }
            if (packet instanceof C0FPacketConfirmTransaction) {
                C0FPacketConfirmTransaction packetConfirmTransaction = (C0FPacketConfirmTransaction)packet;
                if (packetConfirmTransaction.func_149532_c() < 10 && packetConfirmTransaction.func_149532_c() == 20) {
                    event.cancelEvent();
                    mc.func_147114_u().func_147297_a((Packet)new C0FPacketConfirmTransaction(0, this.queueID.isEmpty() ? (short)-1 : this.queueID.poll(), false));
                    this.queueID.offer(packetConfirmTransaction.func_149533_d());
                }
                if (packet instanceof C02PacketUseEntity) {
                    mc.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity());
                }
            }
            if (this.modeValue.equals("AAC5Movement")) {
                if (packet instanceof C0BPacketEntityAction) {
                    event.notify();
                    event.cancelEvent();
                }
                if (packet instanceof C00Handshake) {
                    event.cancelEvent();
                }
                if (packet instanceof C00PacketKeepAlive) {
                    event.cancelEvent();
                }
                if (packet instanceof C0CPacketInput) {
                    ((C0CPacketInput)packet).func_149620_c();
                }
            }
        }
    }
}

