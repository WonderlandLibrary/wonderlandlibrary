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
 *  net.minecraft.network.play.client.CPacketConfirmTransaction
 *  net.minecraft.network.play.server.SPacketConfirmTransaction
 *  net.minecraft.network.play.server.SPacketEntity
 *  net.minecraft.network.play.server.SPacketEntity$S15PacketEntityRelMove
 *  net.minecraft.network.play.server.SPacketEntityTeleport
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.network.play.server.SPacketSpawnPlayer
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
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
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSpawnPlayer;

@ModuleInfo(name="GrimVelocity", description="Better", category=ModuleCategory.COMBAT)
public final class GrimVelocity
extends Module {
    private final IntegerValue cancelPacketValue = new IntegerValue("GroundTicks", 6, 0, 100);
    private final IntegerValue AirCancelPacketValue = new IntegerValue("AirTicks", 6, 0, 100);
    private final BoolValue cancelS12PacketValue = new BoolValue("NoS12", true);
    private final ListValue ModeValue = new ListValue("CancelPacket", new String[]{"S32", "C0f", "none"}, "S32");
    private final BoolValue C0fResend = new BoolValue("C0fResend", false);
    private final BoolValue S32Test = new BoolValue("S32Test", false);
    private final BoolValue DelayClientPacket = new BoolValue("DelayClintPacket", false);
    private final BoolValue ServerPacketTest = new BoolValue("ServerPacketTest", false);
    private final BoolValue NoMoveFix = new BoolValue("NoMoveFix", false);
    private final BoolValue OnlyMove = new BoolValue("OnlyMove", false);
    private final BoolValue OnlyGround = new BoolValue("OnlyGround", false);
    private final BoolValue BlinkOnHurt = new BoolValue("BlinkOnHurt", false);
    private final ListValue AutoDisableMode = new ListValue("AutoDisableMode", new String[]{"Safe", "Silent"}, "slient");
    private final IntegerValue AutoSilent = new IntegerValue("AutoSilentTicks", 8, 0, 10);
    private int cancelPackets;
    private int resetPersec = 8;
    private int updates;
    private int S08;
    private final LinkedBlockingQueue<Packet<?>> C0fPacket = new LinkedBlockingQueue();
    private final LinkedList<Packet<INetHandlerPlayClient>> S32Packet = new LinkedList();
    private final LinkedList<Packet<INetHandlerPlayClient>> SPacket = new LinkedList();
    private final BoolValue debugValue = new BoolValue("debug", false);
    private final Module bl1nk = LiquidBounce.INSTANCE.getModuleManager().getModule(Blink.class);

    public final BoolValue getDelayClientPacket() {
        return this.DelayClientPacket;
    }

    public final int getCancelPackets() {
        return this.cancelPackets;
    }

    public final void setCancelPackets(int n) {
        this.cancelPackets = n;
    }

    public final void debug(String s) {
        if (((Boolean)this.debugValue.get()).booleanValue()) {
            ClientUtils.displayChatMessage(s);
        }
    }

    @Override
    public void onEnable() {
        if (((Boolean)this.DelayClientPacket.get()).booleanValue()) {
            Module module = this.bl1nk;
            if (module == null) {
                Intrinsics.throwNpe();
            }
            if (module.getState() && ((Boolean)this.BlinkOnHurt.get()).booleanValue()) {
                Module module2 = this.bl1nk;
                if (module2 == null) {
                    Intrinsics.throwNpe();
                }
                module2.setState(false);
            }
        }
        this.cancelPackets = 0;
        this.S32Packet.clear();
        this.C0fPacket.clear();
    }

    @Override
    public void onDisable() {
        if (((Boolean)this.DelayClientPacket.get()).booleanValue()) {
            Module module = this.bl1nk;
            if (module == null) {
                Intrinsics.throwNpe();
            }
            if (module.getState() && ((Boolean)this.BlinkOnHurt.get()).booleanValue()) {
                Module module2 = this.bl1nk;
                if (module2 == null) {
                    Intrinsics.throwNpe();
                }
                module2.setState(false);
            }
        }
        this.S32Packet.clear();
        this.C0fPacket.clear();
    }

    @EventTarget
    public final void onWorld(WorldEvent event) {
        this.S32Packet.clear();
        this.C0fPacket.clear();
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onPacket(PacketEvent event) {
        block38: {
            block40: {
                block39: {
                    block37: {
                        block36: {
                            if (((Boolean)this.OnlyMove.get()).booleanValue() && !MovementUtils.isMoving()) break block36;
                            if (!((Boolean)this.OnlyGround.get()).booleanValue()) break block37;
                            v0 = MinecraftInstance.mc.getThePlayer();
                            if (v0 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (v0.getOnGround()) break block37;
                        }
                        return;
                    }
                    $this$unwrap$iv = event.getPacket();
                    $i$f$unwrap = false;
                    packet = ((PacketImpl)$this$unwrap$iv).getWrapped();
                    if (this.S08 > 0) {
                        var3_3 = this.S08;
                        this.S08 = var3_3 + -1;
                        this.debug("Off " + this.S08);
                        return;
                    }
                    if (packet instanceof SPacketPlayerPosLook) {
                        if (StringsKt.equals((String)((String)this.AutoDisableMode.get()), (String)"silent", (boolean)true)) {
                            this.S08 = ((Number)this.AutoSilent.get()).intValue();
                        }
                        if (StringsKt.equals((String)((String)this.AutoDisableMode.get()), (String)"safe", (boolean)true)) {
                            v1 = LiquidBounce.INSTANCE.getModuleManager().get(GrimVelocity.class);
                            if (v1 == null) {
                                Intrinsics.throwNpe();
                            }
                            v1.setState(false);
                        }
                    }
                    if (!(packet instanceof SPacketEntityVelocity)) break block38;
                    if (MinecraftInstance.mc.getThePlayer() == null) break block39;
                    v2 = MinecraftInstance.mc.getTheWorld();
                    if (v2 == null || (v2 = v2.getEntityByID(((SPacketEntityVelocity)packet).func_149412_c())) == null) {
                        return;
                    }
                    if (!(v2.equals(MinecraftInstance.mc.getThePlayer()) ^ true)) break block40;
                }
                return;
            }
            if (!((Boolean)this.NoMoveFix.get()).booleanValue() || MovementUtils.isMoving()) ** GOTO lbl-1000
            v3 = MinecraftInstance.mc.getThePlayer();
            if (v3 == null) {
                Intrinsics.throwNpe();
            }
            if (v3.getOnGround()) {
                if (((Boolean)this.cancelS12PacketValue.get()).booleanValue()) {
                    ((SPacketEntityVelocity)packet).field_149415_b = 0;
                    ((SPacketEntityVelocity)packet).field_149416_c = 0;
                    ((SPacketEntityVelocity)packet).field_149414_d = 0;
                }
            } else if (((Boolean)this.cancelS12PacketValue.get()).booleanValue()) {
                event.cancelEvent();
            }
            v4 = MinecraftInstance.mc.getThePlayer();
            if (v4 == null) {
                Intrinsics.throwNpe();
            }
            v5 = this.cancelPackets = v4.getOnGround() != false ? ((Number)this.cancelPacketValue.get()).intValue() : ((Number)this.AirCancelPacketValue.get()).intValue();
        }
        if ((StringsKt.equals((String)((String)this.ModeValue.get()), (String)"c0f", (boolean)true) || packet instanceof SPacketConfirmTransaction && StringsKt.equals((String)((String)this.ModeValue.get()), (String)"s32", (boolean)true) || packet instanceof CPacketConfirmTransaction && StringsKt.equals((String)((String)this.ModeValue.get()), (String)"c0f", (boolean)true)) && this.cancelPackets > 0) {
            if (((Boolean)this.C0fResend.get()).booleanValue() && StringsKt.equals((String)((String)this.ModeValue.get()), (String)"c0f", (boolean)true)) {
                this.C0fPacket.add((Packet<?>)packet);
            }
            if (((Boolean)this.S32Test.get()).booleanValue() && StringsKt.equals((String)((String)this.ModeValue.get()), (String)"s32", (boolean)true)) {
                v6 = packet;
                if (v6 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.Packet<net.minecraft.network.play.INetHandlerPlayClient>");
                }
                this.S32Packet.add((Packet<INetHandlerPlayClient>)v6);
            }
            if (StringsKt.equals((String)((String)this.ModeValue.get()), (String)"s32", (boolean)true)) {
                this.debug("S32");
            } else {
                this.debug("C0f");
            }
            event.cancelEvent();
            var3_4 = this.cancelPackets;
            this.cancelPackets = var3_4 + -1;
        }
        if (this.cancelPackets > 0) {
            if (((Boolean)this.DelayClientPacket.get()).booleanValue() && (MovementUtils.isMoving() || !((Boolean)this.OnlyMove.get()).booleanValue())) {
                v7 = this.bl1nk;
                if (v7 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v7.getState() && ((Boolean)this.BlinkOnHurt.get()).booleanValue()) {
                    v8 = this.bl1nk;
                    if (v8 == null) {
                        Intrinsics.throwNpe();
                    }
                    v8.setState(true);
                }
            }
            if (((Boolean)this.ServerPacketTest.get()).booleanValue() && (packet instanceof SPacketEntityVelocity || packet instanceof SPacketEntity || packet instanceof SPacketSpawnPlayer || packet instanceof SPacketEntityTeleport || packet instanceof SPacketEntity.S15PacketEntityRelMove)) {
                if (packet instanceof SPacketEntityVelocity) {
                    v9 = MinecraftInstance.mc.getTheWorld();
                    if (v9 == null || (v9 = v9.getEntityByID(((SPacketEntityVelocity)packet).func_149412_c())) == null) {
                        return;
                    }
                    if (v9.equals(MinecraftInstance.mc.getThePlayer())) {
                        return;
                    }
                }
                this.SPacket.add((Packet<INetHandlerPlayClient>)packet);
                event.cancelEvent();
            }
        }
    }

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        block21: {
            block20: {
                if (((Boolean)this.OnlyMove.get()).booleanValue() && !MovementUtils.isMoving()) break block20;
                if (!((Boolean)this.OnlyGround.get()).booleanValue()) break block21;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getOnGround()) break block21;
            }
            return;
        }
        int n = this.updates;
        this.updates = n + 1;
        if (this.resetPersec > 0 && (this.updates >= 0 || this.updates >= this.resetPersec)) {
            this.updates = 0;
            if (this.cancelPackets > 0) {
                n = this.cancelPackets;
                this.cancelPackets = n + -1;
            }
        }
        if (this.cancelPackets == 0) {
            if (((Boolean)this.DelayClientPacket.get()).booleanValue()) {
                Module module = this.bl1nk;
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                if (module.getState() && ((Boolean)this.BlinkOnHurt.get()).booleanValue()) {
                    Module module2 = this.bl1nk;
                    if (module2 == null) {
                        Intrinsics.throwNpe();
                    }
                    module2.setState(false);
                }
            }
            if (!this.C0fPacket.isEmpty()) {
                while (!this.C0fPacket.isEmpty()) {
                    NetHandlerPlayClient netHandlerPlayClient = MinecraftInstance.mc2.func_147114_u();
                    if (netHandlerPlayClient == null) {
                        Intrinsics.throwNpe();
                    }
                    netHandlerPlayClient.func_147298_b().func_179290_a(this.C0fPacket.take());
                    this.C0fPacket.clear();
                    this.debug("C0fResend");
                }
            }
            if (!this.S32Packet.isEmpty()) {
                while (this.S32Packet.size() > 0) {
                    Packet<INetHandlerPlayClient> packet = this.S32Packet.poll();
                    if (packet != null) {
                        packet.func_148833_a((INetHandler)MinecraftInstance.mc2.func_147114_u());
                    }
                    this.S32Packet.clear();
                    this.debug("S32Test");
                }
            }
            if (!this.SPacket.isEmpty()) {
                while (this.SPacket.size() > 0) {
                    Packet<INetHandlerPlayClient> packet = this.SPacket.poll();
                    if (packet != null) {
                        packet.func_148833_a((INetHandler)MinecraftInstance.mc2.func_147114_u());
                    }
                    this.SPacket.clear();
                    this.debug("STest");
                }
            }
        }
    }
}

