/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.concurrent.GenericFutureListener
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.INetHandler
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C00PacketKeepAlive
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.network.play.client.C0BPacketEntityAction$Action
 *  net.minecraft.network.play.client.C0CPacketInput
 *  net.minecraft.network.play.client.C0FPacketConfirmTransaction
 *  net.minecraft.network.play.client.C18PacketSpectate
 *  net.minecraft.network.play.server.S07PacketRespawn
 *  net.minecraft.util.MathHelper
 */
package me.report.liquidware.modules.fun;

import io.netty.util.concurrent.GenericFutureListener;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.util.MathHelper;

@ModuleInfo(name="BlocksmcDisabler", description="Some Disabler / Report.1337 ", category=ModuleCategory.FUN)
public class BlocksmcDisabler
extends Module {
    EntityPlayerSP entityPlayerSP;
    private final Queue<Packet<?>> packet = new ConcurrentLinkedDeque();
    float voidTP;

    @Override
    public void onEnable() {
        if (mc.func_71356_B()) {
            return;
        }
        if (BlocksmcDisabler.mc.field_71439_g != null) {
            BlocksmcDisabler.mc.field_71439_g.field_70173_aa = 0;
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> p = event.getPacket();
        if (BlocksmcDisabler.mc.field_71439_g.field_70173_aa < 250 && p instanceof S07PacketRespawn) {
            this.packet.clear();
            return;
        }
        if (p instanceof C0BPacketEntityAction) {
            C0BPacketEntityAction c0B = (C0BPacketEntityAction)p;
            if (c0B.func_180764_b().equals((Object)C0BPacketEntityAction.Action.START_SPRINTING)) {
                if (this.entityPlayerSP.field_175171_bO) {
                    this.sendPacketSilent((Packet)new C0BPacketEntityAction((Entity)BlocksmcDisabler.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    this.entityPlayerSP.field_175171_bO = false;
                }
                event.cancelEvent();
            }
            if (c0B.func_180764_b().equals((Object)C0BPacketEntityAction.Action.STOP_SPRINTING)) {
                event.cancelEvent();
            }
        }
        if (p instanceof C00PacketKeepAlive || p instanceof C0FPacketConfirmTransaction) {
            this.packet.add(p);
            event.cancelEvent();
            if (this.packet.size() > 500) {
                this.sendPacketSilent(this.packet.poll());
            }
        }
        if (p instanceof C03PacketPlayer) {
            if (BlocksmcDisabler.mc.field_71439_g.field_70173_aa % 50 == 0) {
                BlocksmcDisabler.sendPacketUnlogged((Packet<? extends INetHandler>)new C18PacketSpectate(UUID.randomUUID()));
                this.voidTP = (float)MathHelper.func_82716_a((Random)new Random(), (double)0.78, (double)0.98);
                BlocksmcDisabler.sendPacketUnlogged((Packet<? extends INetHandler>)new C0CPacketInput(this.voidTP, this.voidTP, false, false));
            }
            if (BlocksmcDisabler.mc.field_71439_g.field_70173_aa % 120 == 0) {
                this.voidTP = (float)MathHelper.func_82716_a((Random)new Random(), (double)0.01, (double)20.0);
            }
        }
    }

    public static void sendPacketUnlogged(Packet<? extends INetHandler> packet) {
        mc.func_147114_u().func_147298_b().func_179290_a(packet);
    }

    public void sendPacketSilent(Packet packet) {
        mc.func_147114_u().func_147298_b().func_179288_a(packet, null, new GenericFutureListener[0]);
    }
}

