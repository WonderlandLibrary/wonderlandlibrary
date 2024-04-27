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
import liying.utils.PacketUtils;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
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

@ModuleInfo(name="VelocityPlus", description="Skid By Day", category=ModuleCategory.COMBAT)
public final class VelocityPlus
extends Module {
    private final BoolValue TestValue;
    private final BoolValue TestNoMove;
    private final BoolValue OnlyMove;
    private final BoolValue CancelS12;
    private final ListValue AutoDisable;
    private final IntegerValue TestValue1;
    private final BoolValue DeBug;
    private final BoolValue Safe;
    private final LinkedList inBus;
    private int resetPersec = 8;
    private int S08;
    private int updates;
    private final LinkedList outBus;
    private int grimTCancel;
    private final IntegerValue AirCancelPacketValue;
    private final BoolValue CancelCpacket;
    private final IntegerValue cancelPacketValue = new IntegerValue("GroundTicks", 6, 0, 100);
    private final BoolValue OnlyGround;
    private final IntegerValue SilentTicks;
    private final BoolValue CancelSpacket;
    private final BoolValue CancelSpacket1;

    @Override
    public void onEnable() {
        this.inBus.clear();
        this.outBus.clear();
        this.grimTCancel = 0;
        this.S08 = 0;
    }

    @Override
    public void onDisable() {
        Packet packet;
        while (this.inBus.size() > 0) {
            packet = (Packet)this.inBus.poll();
            if (packet == null) continue;
            IMinecraft iMinecraft = MinecraftInstance.mc;
            packet.func_148833_a((INetHandler)iMinecraft.getNetHandler2());
        }
        while (this.outBus.size() > 0) {
            packet = null;
            if (this.outBus.poll() == null) continue;
            PacketUtils.sendPacketNoEvent(packet);
            if (!((Boolean)this.DeBug.get()).booleanValue()) continue;
            ClientUtils.displayChatMessage("S12 Cancelled");
        }
        this.S08 = 0;
        this.outBus.clear();
        this.inBus.clear();
    }

    @EventTarget
    public final void onWorld(WorldEvent worldEvent) {
        this.outBus.clear();
        this.inBus.clear();
    }

    public static final IMinecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        Packet packet;
        IPacket iPacket;
        Object object;
        Class<?> clazz;
        block46: {
            block47: {
                block48: {
                    Object object2;
                    Object object3;
                    clazz = VelocityPlus.access$getMc$p$s1046033730().getThePlayer();
                    if (clazz == null) {
                        return;
                    }
                    object = clazz;
                    IPacket iPacket2 = packetEvent.getPacket();
                    iPacket = packetEvent.getPacket();
                    boolean bl = false;
                    IPacket iPacket3 = iPacket;
                    if (iPacket3 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.PacketImpl<*>");
                    }
                    packet = ((PacketImpl)iPacket3).getWrapped();
                    if (packet instanceof SPacketPlayerPosLook) {
                        if (StringsKt.equals((String)((String)this.AutoDisable.get()), (String)"Normal", (boolean)true)) {
                            this.setState(false);
                        }
                        if (StringsKt.equals((String)((String)this.AutoDisable.get()), (String)"Silent", (boolean)true)) {
                            this.S08 = ((Number)this.SilentTicks.get()).intValue();
                        }
                    }
                    if ((Boolean)this.OnlyGround.get() != false && !object.getOnGround() || (Boolean)this.OnlyMove.get() != false && !MovementUtils.isMoving() || this.S08 != 0) {
                        return;
                    }
                    if (MinecraftInstance.classProvider.isSPacketEntityVelocity(iPacket2)) {
                        object3 = iPacket2.asSPacketEntityVelocity();
                        object2 = VelocityPlus.access$getMc$p$s1046033730().getTheWorld();
                        if (object2 == null || (object2 = VelocityPlus.access$getMc$p$s1046033730().getTheWorld().getEntityByID(object3.getEntityID())) == null) {
                            return;
                        }
                        if (object2.equals(object) ^ true || ((Boolean)this.Safe.get()).booleanValue() && this.grimTCancel != 0) {
                            return;
                        }
                        if (((Boolean)this.TestNoMove.get()).booleanValue()) {
                            if (((Boolean)this.CancelS12.get()).booleanValue()) {
                                if (MovementUtils.isMoving()) {
                                    if (((Boolean)this.DeBug.get()).booleanValue()) {
                                        ClientUtils.displayChatMessage("S12 Cancelled");
                                    }
                                    packetEvent.cancelEvent();
                                } else if (object.getOnGround()) {
                                    if (((Boolean)this.DeBug.get()).booleanValue()) {
                                        ClientUtils.displayChatMessage("S12 Changed");
                                    }
                                    object3.setMotionX(0);
                                    object3.setMotionY(0);
                                    object3.setMotionZ(0);
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
                        int n = this.grimTCancel = object.getOnGround() ? ((Number)this.cancelPacketValue.get()).intValue() : ((Number)this.AirCancelPacketValue.get()).intValue();
                    }
                    if (((Boolean)this.CancelSpacket.get()).booleanValue()) {
                        if (!(packet instanceof SPacketConfirmTransaction)) {
                            object3 = packet.getClass();
                            if (object3 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (StringsKt.startsWith((String)(object2 = ((Class)object3).getSimpleName()), (String)"S", (boolean)true) && this.grimTCancel > 0) {
                                Object object4 = VelocityPlus.access$getMc$p$s1046033730().getTheWorld();
                                if (object4 == null || (object4 = VelocityPlus.access$getMc$p$s1046033730().getTheWorld().getEntityByID(iPacket2.asSPacketEntityVelocity().getEntityID())) == null) {
                                    return;
                                }
                                if (object4.equals(object)) {
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
                    object3 = VelocityPlus.access$getMc$p$s1046033730().getTheWorld();
                    if (object3 == null || (object3 = VelocityPlus.access$getMc$p$s1046033730().getTheWorld().getEntityByID(new SPacketEntityVelocity().func_149412_c())) == null) {
                        return;
                    }
                    if (!(object3.equals(object) ^ true)) break block47;
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
                clazz = packet.getClass();
                if (clazz == null) {
                    Intrinsics.throwNpe();
                }
                if (StringsKt.startsWith((String)(object = clazz.getSimpleName()), (String)"C", (boolean)true) && this.grimTCancel > 0) {
                    packetEvent.cancelEvent();
                    int n = this.grimTCancel;
                    this.grimTCancel = n + -1;
                    iPacket = packet;
                    if (iPacket == null) {
                        throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.Packet<net.minecraft.network.play.INetHandlerPlayServer>");
                    }
                    this.outBus.add((Packet)iPacket);
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

    public VelocityPlus() {
        this.AirCancelPacketValue = new IntegerValue("AirTicks", 6, 0, 100);
        this.OnlyGround = new BoolValue("OnlyGround", false);
        this.OnlyMove = new BoolValue("OnlyMove", false);
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

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        Packet packet;
        int n;
        if (this.S08 > 0) {
            if (((Boolean)this.DeBug.get()).booleanValue()) {
                ClientUtils.displayChatMessage("Off " + this.S08);
            }
            n = this.S08;
            this.S08 = n + -1;
        }
        if (VelocityPlus.access$getMc$p$s1046033730().getNetHandler() == null) {
            return;
        }
        if (!this.inBus.isEmpty() && this.grimTCancel == 0 || this.S08 > 0) {
            while (this.inBus.size() > 0) {
                packet = (Packet)this.inBus.poll();
                if (packet != null) {
                    IMinecraft iMinecraft = MinecraftInstance.mc;
                    packet.func_148833_a((INetHandler)iMinecraft.getNetHandler2());
                }
                if (!((Boolean)this.DeBug.get()).booleanValue()) continue;
                ClientUtils.displayChatMessage("SPacket");
            }
        }
        if (!this.outBus.isEmpty() && this.grimTCancel == 0) {
            while (this.outBus.size() > 0) {
                packet = null;
                if (this.outBus.poll() == null) continue;
                PacketUtils.sendPacketNoEvent(packet);
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
}

