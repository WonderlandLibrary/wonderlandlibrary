/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.network.INetHandler
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketConfirmTransaction
 *  net.minecraft.network.play.server.SPacketConfirmTransaction
 *  net.minecraft.network.play.server.SPacketEntity
 *  net.minecraft.network.play.server.SPacketEntity$S15PacketEntityRelMove
 *  net.minecraft.network.play.server.SPacketEntity$S16PacketEntityLook
 *  net.minecraft.network.play.server.SPacketEntity$S17PacketEntityLookMove
 *  net.minecraft.network.play.server.SPacketEntityAttach
 *  net.minecraft.network.play.server.SPacketEntityTeleport
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.LinkedList;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

@ModuleInfo(name="GrimFull", description="\u5168\u53cd", category=ModuleCategory.COMBAT)
public final class GrimFull
extends Module {
    private final LinkedList outBus;
    private final BoolValue Safe;
    private final IntegerValue AirCancelPacketValue;
    private int S08;
    private int grimTCancel;
    private final BoolValue DeBug;
    private final BoolValue TestNoMove;
    private final BoolValue CancelS12;
    private final BoolValue noMove;
    private final ListValue AutoDisable;
    private final IntegerValue TestValue1;
    private final BoolValue CancelCpacket;
    private final BoolValue OnlyGround;
    private final IntegerValue cancelPacketValue = new IntegerValue("GroundTicks", 6, 0, 100);
    private final IntegerValue SilentTicks;
    private final BoolValue TestValue;
    private int resetPersec = 8;
    private int updates;
    private final LinkedList inBus;
    private final BoolValue OnlyMove;
    private final BoolValue CancelSpacket1;
    private final BoolValue CancelSpacket;

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        Packet packet;
        block46: {
            block47: {
                block48: {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        return;
                    }
                    IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
                    IPacket iPacket = packetEvent.getPacket();
                    Object object = packetEvent.getPacket();
                    boolean bl = false;
                    packet = ((PacketImpl)object).getWrapped();
                    if (packet instanceof SPacketPlayerPosLook) {
                        if (StringsKt.equals((String)((String)this.AutoDisable.get()), (String)"Normal", (boolean)true)) {
                            this.setState(false);
                        }
                        if (StringsKt.equals((String)((String)this.AutoDisable.get()), (String)"Silent", (boolean)true)) {
                            this.S08 = ((Number)this.SilentTicks.get()).intValue();
                        }
                    }
                    if ((Boolean)this.OnlyGround.get() != false && !iEntityPlayerSP2.getOnGround() || (Boolean)this.OnlyMove.get() != false && !MovementUtils.isMoving() || this.S08 != 0) {
                        return;
                    }
                    if (MinecraftInstance.classProvider.isSPacketEntityVelocity(iPacket)) {
                        Object object2;
                        object = iPacket.asSPacketEntityVelocity();
                        if (((Boolean)this.noMove.get()).booleanValue() && MovementUtils.isMoving()) {
                            packetEvent.cancelEvent();
                        }
                        if ((object2 = MinecraftInstance.mc.getTheWorld()) == null || (object2 = object2.getEntityByID(object.getEntityID())) == null) {
                            return;
                        }
                        if (object2.equals(iEntityPlayerSP2) ^ true || ((Boolean)this.Safe.get()).booleanValue() && this.grimTCancel != 0) {
                            return;
                        }
                        if (((Boolean)this.TestNoMove.get()).booleanValue()) {
                            if (((Boolean)this.CancelS12.get()).booleanValue()) {
                                if (MovementUtils.isMoving()) {
                                    if (((Boolean)this.DeBug.get()).booleanValue()) {
                                        ClientUtils.displayChatMessage("S12 Cancelled");
                                    }
                                    packetEvent.cancelEvent();
                                } else if (iEntityPlayerSP2.getOnGround()) {
                                    if (((Boolean)this.DeBug.get()).booleanValue()) {
                                        ClientUtils.displayChatMessage("S12 Changed");
                                    }
                                    object.setMotionX(0);
                                    object.setMotionY(0);
                                    object.setMotionZ(0);
                                } else {
                                    if (((Boolean)this.DeBug.get()).booleanValue()) {
                                        ClientUtils.displayChatMessage("S12 Cancelled");
                                    }
                                    packetEvent.cancelEvent();
                                }
                            }
                        } else if (((Boolean)this.CancelS12.get()).booleanValue()) {
                            if (((Boolean)this.DeBug.get()).booleanValue()) {
                                ClientUtils.displayChatMessage("S12 Cancelled");
                            }
                            packetEvent.cancelEvent();
                        }
                        this.grimTCancel = iEntityPlayerSP2.getOnGround() ? ((Number)this.cancelPacketValue.get()).intValue() : ((Number)this.AirCancelPacketValue.get()).intValue();
                    }
                    if (((Boolean)this.CancelSpacket.get()).booleanValue()) {
                        if (!(packet instanceof SPacketConfirmTransaction)) {
                            Class<?> clazz = packet.getClass();
                            if (clazz == null) {
                                Intrinsics.throwNpe();
                            }
                            if (StringsKt.startsWith((String)clazz.getSimpleName(), (String)"S", (boolean)true) && this.grimTCancel > 0) {
                                Object object3 = MinecraftInstance.mc.getTheWorld();
                                if (object3 == null || (object3 = object3.getEntityByID(iPacket.asSPacketEntityVelocity().getEntityID())) == null) {
                                    return;
                                }
                                if (object3.equals(iEntityPlayerSP2)) {
                                    return;
                                }
                                packetEvent.cancelEvent();
                                Packet packet2 = packet;
                                if (packet2 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.Packet<net.minecraft.network.play.INetHandlerPlayClient>");
                                }
                                this.inBus.add(packet2);
                                int n = this.grimTCancel;
                                this.grimTCancel = n + -1;
                            }
                        }
                        if (packet instanceof SPacketConfirmTransaction && this.grimTCancel > 0) {
                            packetEvent.cancelEvent();
                            if (((Boolean)this.DeBug.get()).booleanValue()) {
                                ClientUtils.displayChatMessage("S32 Cancelled " + this.grimTCancel);
                            }
                        }
                    }
                    if (!((Boolean)this.CancelSpacket1.get()).booleanValue()) break block46;
                    if (this.grimTCancel <= 0) break block47;
                    if (packet instanceof SPacketEntity.S17PacketEntityLookMove || packet instanceof SPacketEntity.S16PacketEntityLook || packet instanceof SPacketEntity.S15PacketEntityRelMove || packet instanceof SPacketEntityAttach || packet instanceof SPacketEntityTeleport || packet instanceof SPacketEntity) break block48;
                    if (!(packet instanceof SPacketEntityVelocity)) break block47;
                    Object object4 = MinecraftInstance.mc.getTheWorld();
                    if (object4 == null || (object4 = object4.getEntityByID(new SPacketEntityVelocity().func_149412_c())) == null) {
                        return;
                    }
                    if (!(object4.equals(iEntityPlayerSP2) ^ true)) break block47;
                }
                packetEvent.cancelEvent();
                this.inBus.add(packet);
            }
            if (packet instanceof SPacketConfirmTransaction && this.grimTCancel > 0) {
                packetEvent.cancelEvent();
                if (((Boolean)this.TestValue.get()).booleanValue() && this.grimTCancel <= ((Number)this.TestValue1.get()).intValue()) {
                    this.inBus.add(packet);
                    if (((Boolean)this.DeBug.get()).booleanValue()) {
                        ClientUtils.displayChatMessage("S32 Test");
                    }
                }
                int n = this.grimTCancel;
                this.grimTCancel = n + -1;
                if (((Boolean)this.DeBug.get()).booleanValue()) {
                    ClientUtils.displayChatMessage("S32 Cancelled " + this.grimTCancel);
                }
            }
        }
        if (((Boolean)this.CancelCpacket.get()).booleanValue()) {
            if (!(packet instanceof CPacketConfirmTransaction)) {
                Class<?> clazz = packet.getClass();
                if (clazz == null) {
                    Intrinsics.throwNpe();
                }
                if (StringsKt.startsWith((String)clazz.getSimpleName(), (String)"C", (boolean)true) && this.grimTCancel > 0) {
                    packetEvent.cancelEvent();
                    int n = this.grimTCancel;
                    this.grimTCancel = n + -1;
                    Packet packet3 = packet;
                    if (packet3 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.Packet<net.minecraft.network.play.INetHandlerPlayServer>");
                    }
                    this.outBus.add(packet3);
                }
            }
            if (packet instanceof SPacketConfirmTransaction && this.grimTCancel > 0) {
                packetEvent.cancelEvent();
                if (((Boolean)this.DeBug.get()).booleanValue()) {
                    ClientUtils.displayChatMessage("S32 Cancelled " + this.grimTCancel);
                }
            }
        }
    }

    public GrimFull() {
        this.AirCancelPacketValue = new IntegerValue("AirTicks", 6, 0, 100);
        this.OnlyGround = new BoolValue("OnlyGround", false);
        this.OnlyMove = new BoolValue("OnlyMove", false);
        this.noMove = new BoolValue("noMove", false);
        this.TestNoMove = new BoolValue("TestNoMove", true);
        this.CancelS12 = new BoolValue("CancelS12", true);
        this.CancelSpacket = new BoolValue("CancelSpacket", false);
        this.CancelSpacket1 = new BoolValue("CancelSpacket1", false);
        this.CancelCpacket = new BoolValue("CancelCpacket", false);
        this.TestValue = new BoolValue("Test", false);
        this.TestValue1 = new IntegerValue("TestValue", 4, 0, 100);
        this.Safe = new BoolValue("SafeMode", false);
        this.AutoDisable = new ListValue("AutoDisable", new String[]{"Normal", "Silent"}, "Silent");
        this.SilentTicks = new IntegerValue("AutoDisableSilentTicks", 4, 0, 100);
        this.DeBug = new BoolValue("Debug", false);
        this.inBus = new LinkedList();
        this.outBus = new LinkedList();
    }

    @Override
    public void onDisable() {
        while (this.inBus.size() > 0) {
            Packet packet = (Packet)this.inBus.poll();
            if (packet == null) continue;
            packet.func_148833_a((INetHandler)MinecraftInstance.mc2.func_147114_u());
        }
        while (this.outBus.size() > 0) {
            Packet packet;
            if ((Packet)this.outBus.poll() == null) {
                continue;
            }
            PacketUtils.INSTANCE.sendPacketNoEvent(packet);
            if (!((Boolean)this.DeBug.get()).booleanValue()) continue;
            ClientUtils.displayChatMessage("S12 Cancelled");
        }
        this.S08 = 0;
        this.outBus.clear();
        this.inBus.clear();
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        int n;
        if (this.S08 > 0) {
            if (((Boolean)this.DeBug.get()).booleanValue()) {
                ClientUtils.displayChatMessage("Off " + this.S08);
            }
            n = this.S08;
            this.S08 = n + -1;
        }
        if (MinecraftInstance.mc.getNetHandler() == null) {
            return;
        }
        if (!this.inBus.isEmpty() && this.grimTCancel == 0 || this.S08 > 0) {
            while (this.inBus.size() > 0) {
                Packet packet = (Packet)this.inBus.poll();
                if (packet != null) {
                    packet.func_148833_a((INetHandler)MinecraftInstance.mc2.func_147114_u());
                }
                if (!((Boolean)this.DeBug.get()).booleanValue()) continue;
                ClientUtils.displayChatMessage("SPacket");
            }
        }
        if (!this.outBus.isEmpty() && this.grimTCancel == 0) {
            while (this.outBus.size() > 0) {
                Packet packet;
                if ((Packet)this.outBus.poll() == null) {
                    continue;
                }
                PacketUtils.INSTANCE.sendPacketNoEvent(packet);
                if (!((Boolean)this.DeBug.get()).booleanValue()) continue;
                ClientUtils.displayChatMessage("CPacket");
            }
        }
        n = this.updates;
        this.updates = n + 1;
        if (this.resetPersec > 0 && (this.updates >= 0 || this.updates >= this.resetPersec)) {
            this.updates = 0;
            if (this.grimTCancel > 0) {
                n = this.grimTCancel;
                this.grimTCancel = n + -1;
            }
        }
    }

    @EventTarget
    public final void onWorld(WorldEvent worldEvent) {
        this.outBus.clear();
        this.inBus.clear();
    }

    @Override
    public void onEnable() {
        this.inBus.clear();
        this.outBus.clear();
        this.grimTCancel = 0;
        this.S08 = 0;
    }
}

