/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.network.INetHandler
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayClient
 *  net.minecraft.network.play.INetHandlerPlayServer
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
import me.utils.PacketUtils;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntityVelocity;
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
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

@ModuleInfo(name="GrimVelocity2", description="GrimAC Full Velocity Custom", category=ModuleCategory.COMBAT)
public final class GrimVelocity2
extends Module {
    private final IntegerValue cancelPacketValue = new IntegerValue("GroundTicks", 6, 0, 100);
    private final IntegerValue AirCancelPacketValue = new IntegerValue("AirTicks", 6, 0, 100);
    private final BoolValue OnlyGround = new BoolValue("OnlyGround", false);
    private final BoolValue OnlyMove = new BoolValue("OnlyMove", false);
    private final BoolValue TestNoMove = new BoolValue("TestNoMove", true);
    private final BoolValue CancelS12 = new BoolValue("CancelS12", true);
    private final BoolValue CancelSpacket = new BoolValue("CancelSpacket", false);
    private final BoolValue CancelSpacket1 = new BoolValue("CancelSpacket1", false);
    private final BoolValue CancelCpacket = new BoolValue("CancelCpacket", false);
    private final BoolValue TestValue = new BoolValue("Test", false);
    private final IntegerValue TestValue1 = new IntegerValue("TestValue", 4, 0, 100);
    private final BoolValue Safe = new BoolValue("SafeMode", false);
    private final ListValue AutoDisable = new ListValue("AutoDisable", new String[]{"Normal", "Silent"}, "Silent");
    private final IntegerValue SilentTicks = new IntegerValue("AutoDisableSilentTicks", 4, 0, 100);
    private final BoolValue DeBug = new BoolValue("Debug", false);
    private int resetPersec = 8;
    private int grimTCancel;
    private int updates;
    private int S08;
    private final LinkedList<Packet<INetHandlerPlayClient>> inBus = new LinkedList();
    private final LinkedList<Packet<INetHandlerPlayServer>> outBus = new LinkedList();

    @Override
    public void onEnable() {
        this.inBus.clear();
        this.outBus.clear();
        this.grimTCancel = 0;
        this.S08 = 0;
    }

    @Override
    public void onDisable() {
        while (this.inBus.size() > 0) {
            Packet<INetHandlerPlayClient> packet = this.inBus.poll();
            if (packet == null) continue;
            NetHandlerPlayClient netHandlerPlayClient = MinecraftInstance.mc2.func_147114_u();
            if (netHandlerPlayClient == null) {
                Intrinsics.throwNpe();
            }
            packet.func_148833_a((INetHandler)netHandlerPlayClient);
        }
        while (this.outBus.size() > 0) {
            Packet<INetHandlerPlayServer> upPacket;
            if (this.outBus.poll() == null) {
                continue;
            }
            PacketUtils.sendPacketNoEvent(upPacket);
            if (!((Boolean)this.DeBug.get()).booleanValue()) continue;
            ClientUtils.displayChatMessage("S12 Cancelled");
        }
        this.S08 = 0;
        this.outBus.clear();
        this.inBus.clear();
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
        Object packet1;
        block45: {
            block46: {
                block47: {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        return;
                    }
                    IEntityPlayerSP thePlayer = iEntityPlayerSP;
                    IPacket packet = event.getPacket();
                    IPacket $this$unwrap$iv = event.getPacket();
                    boolean $i$f$unwrap = false;
                    packet1 = ((PacketImpl)$this$unwrap$iv).getWrapped();
                    if (packet1 instanceof SPacketPlayerPosLook) {
                        if (StringsKt.equals((String)((String)this.AutoDisable.get()), (String)"Normal", (boolean)true)) {
                            this.setState(false);
                        }
                        if (StringsKt.equals((String)((String)this.AutoDisable.get()), (String)"Silent", (boolean)true)) {
                            this.S08 = ((Number)this.SilentTicks.get()).intValue();
                        }
                    }
                    if ((Boolean)this.OnlyGround.get() != false && !thePlayer.getOnGround() || (Boolean)this.OnlyMove.get() != false && !MovementUtils.isMoving() || this.S08 != 0) {
                        return;
                    }
                    if (MinecraftInstance.classProvider.isSPacketEntityVelocity(packet)) {
                        ISPacketEntityVelocity packetEntityVelocity = packet.asSPacketEntityVelocity();
                        Object object = MinecraftInstance.mc.getTheWorld();
                        if (object == null || (object = object.getEntityByID(packetEntityVelocity.getEntityID())) == null) {
                            return;
                        }
                        if (object.equals(thePlayer) ^ true || ((Boolean)this.Safe.get()).booleanValue() && this.grimTCancel != 0) {
                            return;
                        }
                        if (((Boolean)this.TestNoMove.get()).booleanValue()) {
                            if (((Boolean)this.CancelS12.get()).booleanValue()) {
                                if (MovementUtils.isMoving()) {
                                    if (((Boolean)this.DeBug.get()).booleanValue()) {
                                        ClientUtils.displayChatMessage("S12 Cancelled");
                                    }
                                    event.cancelEvent();
                                } else if (thePlayer.getOnGround()) {
                                    if (((Boolean)this.DeBug.get()).booleanValue()) {
                                        ClientUtils.displayChatMessage("S12 Changed");
                                    }
                                    packetEntityVelocity.setMotionX(0);
                                    packetEntityVelocity.setMotionY(0);
                                    packetEntityVelocity.setMotionZ(0);
                                } else {
                                    if (((Boolean)this.DeBug.get()).booleanValue()) {
                                        ClientUtils.displayChatMessage("S12 Cancelled");
                                    }
                                    event.cancelEvent();
                                }
                            }
                        } else if (((Boolean)this.CancelS12.get()).booleanValue()) {
                            if (((Boolean)this.DeBug.get()).booleanValue()) {
                                ClientUtils.displayChatMessage("S12 Cancelled");
                            }
                            event.cancelEvent();
                        }
                        this.grimTCancel = thePlayer.getOnGround() ? ((Number)this.cancelPacketValue.get()).intValue() : ((Number)this.AirCancelPacketValue.get()).intValue();
                    }
                    if (((Boolean)this.CancelSpacket.get()).booleanValue()) {
                        if (!(packet1 instanceof SPacketConfirmTransaction)) {
                            Class<?> clazz = packet1.getClass();
                            if (clazz == null) {
                                Intrinsics.throwNpe();
                            }
                            if (StringsKt.startsWith((String)clazz.getSimpleName(), (String)"S", (boolean)true) && this.grimTCancel > 0) {
                                Object object = MinecraftInstance.mc.getTheWorld();
                                if (object == null || (object = object.getEntityByID(packet.asSPacketEntityVelocity().getEntityID())) == null) {
                                    return;
                                }
                                if (object.equals(thePlayer)) {
                                    return;
                                }
                                event.cancelEvent();
                                Object t = packet1;
                                if (t == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.Packet<net.minecraft.network.play.INetHandlerPlayClient>");
                                }
                                this.inBus.add((Packet<INetHandlerPlayClient>)t);
                                int n = this.grimTCancel;
                                this.grimTCancel = n + -1;
                            }
                        }
                        if (packet1 instanceof SPacketConfirmTransaction && this.grimTCancel > 0) {
                            event.cancelEvent();
                            if (((Boolean)this.DeBug.get()).booleanValue()) {
                                ClientUtils.displayChatMessage("S32 Cancelled " + this.grimTCancel);
                            }
                        }
                    }
                    if (!((Boolean)this.CancelSpacket1.get()).booleanValue()) break block45;
                    if (this.grimTCancel <= 0) break block46;
                    if (packet1 instanceof SPacketEntity.S17PacketEntityLookMove || packet1 instanceof SPacketEntity.S16PacketEntityLook || packet1 instanceof SPacketEntity.S15PacketEntityRelMove || packet1 instanceof SPacketEntityAttach || packet1 instanceof SPacketEntityTeleport || packet1 instanceof SPacketEntity) break block47;
                    if (!(packet1 instanceof SPacketEntityVelocity)) break block46;
                    Object object = MinecraftInstance.mc.getTheWorld();
                    if (object == null || (object = object.getEntityByID(new SPacketEntityVelocity().func_149412_c())) == null) {
                        return;
                    }
                    if (!(object.equals(thePlayer) ^ true)) break block46;
                }
                event.cancelEvent();
                this.inBus.add((Packet<INetHandlerPlayClient>)packet1);
            }
            if (packet1 instanceof SPacketConfirmTransaction && this.grimTCancel > 0) {
                event.cancelEvent();
                if (((Boolean)this.TestValue.get()).booleanValue() && this.grimTCancel <= ((Number)this.TestValue1.get()).intValue()) {
                    this.inBus.add((Packet<INetHandlerPlayClient>)packet1);
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
            if (!(packet1 instanceof CPacketConfirmTransaction)) {
                Class<?> clazz = packet1.getClass();
                if (clazz == null) {
                    Intrinsics.throwNpe();
                }
                if (StringsKt.startsWith((String)clazz.getSimpleName(), (String)"C", (boolean)true) && this.grimTCancel > 0) {
                    event.cancelEvent();
                    int n = this.grimTCancel;
                    this.grimTCancel = n + -1;
                    Object t = packet1;
                    if (t == null) {
                        throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.Packet<net.minecraft.network.play.INetHandlerPlayServer>");
                    }
                    this.outBus.add((Packet<INetHandlerPlayServer>)t);
                }
            }
            if (packet1 instanceof SPacketConfirmTransaction && this.grimTCancel > 0) {
                event.cancelEvent();
                if (((Boolean)this.DeBug.get()).booleanValue()) {
                    ClientUtils.displayChatMessage("S32 Cancelled " + this.grimTCancel);
                }
            }
        }
    }

    @EventTarget
    public final void onWorld(WorldEvent event) {
        this.outBus.clear();
        this.inBus.clear();
    }

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
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
                Packet<INetHandlerPlayClient> packet = this.inBus.poll();
                if (packet != null) {
                    packet.func_148833_a((INetHandler)MinecraftInstance.mc2.func_147114_u());
                }
                if (!((Boolean)this.DeBug.get()).booleanValue()) continue;
                ClientUtils.displayChatMessage("SPacket");
            }
        }
        if (!this.outBus.isEmpty() && this.grimTCancel == 0) {
            while (this.outBus.size() > 0) {
                Packet<INetHandlerPlayServer> upPacket;
                if (this.outBus.poll() == null) {
                    continue;
                }
                PacketUtils.sendPacketNoEvent(upPacket);
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

