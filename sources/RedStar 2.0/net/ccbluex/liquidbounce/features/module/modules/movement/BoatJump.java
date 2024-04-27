package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketInput;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="BoatJump", category=ModuleCategory.MOVEMENT, description="nnn")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000H\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\b\n\b\n\n\b\n\n\b\n\n\u0000\b\u000020B¢J\b0HJ\b0HJ020HR0X¢\n\u0000R0X¢\n\u0000R0\bX¢\n\u0000R\t0\nX¢\n\u0000R0\fX¢\n\u0000R\r0X¢\n\u0000R0\nX¢\n\u0000R0\bX¢\n\u0000R0\bX¢\n\u0000R0\bX¢\n\u0000R0X¢\n\u0000R0\fX¢\n\u0000R0\bX¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/BoatJump;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "autoHitValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "delayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "hBoostValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "hasStopped", "", "hitTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "jumpState", "", "lastRide", "launchRadiusValue", "matrixTimerAirValue", "matrixTimerStartValue", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "timer", "vBoostValue", "onDisable", "", "onEnable", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class BoatJump
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Boost", "Launch", "Matrix"}, "Boost");
    private final FloatValue hBoostValue = new FloatValue("HBoost", 2.0f, 0.0f, 6.0f);
    private final FloatValue vBoostValue = new FloatValue("VBoost", 2.0f, 0.0f, 6.0f);
    private final FloatValue matrixTimerStartValue = new FloatValue("MatrixTimerStart", 0.3f, 0.1f, 1.0f);
    private final FloatValue matrixTimerAirValue = new FloatValue("MatrixTimerAir", 0.5f, 0.1f, 1.5f);
    private final FloatValue launchRadiusValue = new FloatValue("LaunchRadius", 4.0f, 3.0f, 10.0f);
    private final IntegerValue delayValue = new IntegerValue("Delay", 200, 100, 500);
    private final BoolValue autoHitValue = new BoolValue("AutoHit", true);
    private int jumpState = 1;
    private final MSTimer timer = new MSTimer();
    private final MSTimer hitTimer = new MSTimer();
    private boolean lastRide;
    private boolean hasStopped;

    @Override
    public void onEnable() {
        this.jumpState = 1;
        this.lastRide = false;
    }

    @Override
    public void onDisable() {
        this.hasStopped = false;
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP.setSpeedInAir(0.02f);
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getOnGround()) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP2.isRiding()) {
                this.hasStopped = false;
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP3.setSpeedInAir(0.02f);
            }
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case -1081239615: {
                if (!string.equals("matrix")) break;
                if (this.hasStopped) {
                    MinecraftInstance.mc.getTimer().setTimerSpeed(((Number)this.matrixTimerAirValue.get()).floatValue());
                    break;
                }
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                break;
            }
        }
        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP4.isRiding() && this.jumpState == 1) {
            if (!this.lastRide) {
                this.timer.reset();
            }
            if (this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
                this.jumpState = 2;
                string = (String)this.modeValue.get();
                bl = false;
                String string4 = string;
                if (string4 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String string5 = string4.toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull(string5, "(this as java.lang.String).toLowerCase()");
                string = string5;
                switch (string.hashCode()) {
                    case -1081239615: {
                        if (string.equals("matrix")) {
                            MinecraftInstance.mc.getTimer().setTimerSpeed(((Number)this.matrixTimerStartValue.get()).floatValue());
                            Minecraft minecraft = MinecraftInstance.mc2;
                            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc2");
                            NetHandlerPlayClient netHandlerPlayClient = minecraft.getConnection();
                            if (netHandlerPlayClient == null) {
                                Intrinsics.throwNpe();
                            }
                            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP5 == null) {
                                Intrinsics.throwNpe();
                            }
                            float f = iEntityPlayerSP5.getMoveStrafing();
                            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP6 == null) {
                                Intrinsics.throwNpe();
                            }
                            netHandlerPlayClient.sendPacket((Packet)new CPacketInput(f, iEntityPlayerSP6.getMoveForward(), false, true));
                            break;
                        }
                    }
                    default: {
                        Minecraft minecraft = MinecraftInstance.mc2;
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc2");
                        NetHandlerPlayClient netHandlerPlayClient = minecraft.getConnection();
                        if (netHandlerPlayClient == null) {
                            Intrinsics.throwNpe();
                        }
                        IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP7 == null) {
                            Intrinsics.throwNpe();
                        }
                        float f = iEntityPlayerSP7.getMoveStrafing();
                        IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP8 == null) {
                            Intrinsics.throwNpe();
                        }
                        netHandlerPlayClient.sendPacket((Packet)new CPacketInput(f, iEntityPlayerSP8.getMoveForward(), false, true));
                        break;
                    }
                }
            }
        } else if (this.jumpState == 2) {
            IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP9 == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP9.isRiding()) {
                IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP10 == null) {
                    Intrinsics.throwNpe();
                }
                double radiansYaw = (double)iEntityPlayerSP10.getRotationYaw() * Math.PI / (double)180;
                String string6 = (String)this.modeValue.get();
                boolean bl2 = false;
                String string7 = string6;
                if (string7 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String string8 = string7.toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull(string8, "(this as java.lang.String).toLowerCase()");
                switch (string8) {
                    case "boost": {
                        IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP11 == null) {
                            Intrinsics.throwNpe();
                        }
                        double d = ((Number)this.hBoostValue.get()).doubleValue();
                        IEntityPlayerSP iEntityPlayerSP12 = iEntityPlayerSP11;
                        bl2 = false;
                        double d2 = Math.sin(radiansYaw);
                        iEntityPlayerSP12.setMotionX(d * -d2);
                        IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP13 == null) {
                            Intrinsics.throwNpe();
                        }
                        d = ((Number)this.hBoostValue.get()).doubleValue();
                        iEntityPlayerSP12 = iEntityPlayerSP13;
                        bl2 = false;
                        d2 = Math.cos(radiansYaw);
                        iEntityPlayerSP12.setMotionZ(d * d2);
                        IEntityPlayerSP iEntityPlayerSP14 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP14 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP14.setMotionY(((Number)this.vBoostValue.get()).floatValue());
                        this.jumpState = 1;
                        break;
                    }
                    case "launch": {
                        IEntityPlayerSP iEntityPlayerSP15 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP15 == null) {
                            Intrinsics.throwNpe();
                        }
                        double d2 = ((Number)this.hBoostValue.get()).doubleValue() * 0.1;
                        double d = iEntityPlayerSP15.getMotionX();
                        IEntityPlayerSP iEntityPlayerSP12 = iEntityPlayerSP15;
                        bl2 = false;
                        double d3 = Math.sin(radiansYaw);
                        iEntityPlayerSP12.setMotionX(d + d2 * -d3);
                        IEntityPlayerSP iEntityPlayerSP16 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP16 == null) {
                            Intrinsics.throwNpe();
                        }
                        d2 = ((Number)this.hBoostValue.get()).doubleValue() * 0.1;
                        d = iEntityPlayerSP16.getMotionZ();
                        iEntityPlayerSP12 = iEntityPlayerSP16;
                        bl2 = false;
                        d3 = Math.cos(radiansYaw);
                        iEntityPlayerSP12.setMotionZ(d + d2 * d3);
                        IEntityPlayerSP iEntityPlayerSP17 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP17 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP17.setMotionY(iEntityPlayerSP17.getMotionY() + ((Number)this.vBoostValue.get()).doubleValue() * 0.1);
                        boolean hasBoat = false;
                        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                        if (iWorldClient == null) {
                            Intrinsics.throwNpe();
                        }
                        for (IEntity entity : iWorldClient.getLoadedEntityList()) {
                            if (!MinecraftInstance.classProvider.isEntityBoat(entity)) continue;
                            IEntityPlayerSP iEntityPlayerSP18 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP18 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (!(iEntityPlayerSP18.getDistanceToEntity(entity) < ((Number)this.launchRadiusValue.get()).floatValue())) continue;
                            hasBoat = true;
                            break;
                        }
                        if (hasBoat) break;
                        this.jumpState = 1;
                        break;
                    }
                    case "matrix": {
                        this.hasStopped = true;
                        MinecraftInstance.mc.getTimer().setTimerSpeed(((Number)this.matrixTimerAirValue.get()).floatValue());
                        IEntityPlayerSP iEntityPlayerSP19 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP19 == null) {
                            Intrinsics.throwNpe();
                        }
                        double d = ((Number)this.hBoostValue.get()).doubleValue();
                        IEntityPlayerSP iEntityPlayerSP12 = iEntityPlayerSP19;
                        bl2 = false;
                        double d2 = Math.sin(radiansYaw);
                        iEntityPlayerSP12.setMotionX(d * -d2);
                        IEntityPlayerSP iEntityPlayerSP20 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP20 == null) {
                            Intrinsics.throwNpe();
                        }
                        d = ((Number)this.hBoostValue.get()).doubleValue();
                        iEntityPlayerSP12 = iEntityPlayerSP20;
                        bl2 = false;
                        d2 = Math.cos(radiansYaw);
                        iEntityPlayerSP12.setMotionZ(d * d2);
                        IEntityPlayerSP iEntityPlayerSP21 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP21 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP21.setMotionY(((Number)this.vBoostValue.get()).floatValue());
                        this.jumpState = 1;
                        break;
                    }
                }
                this.timer.reset();
                this.hitTimer.reset();
            }
        }
        IEntityPlayerSP iEntityPlayerSP22 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP22 == null) {
            Intrinsics.throwNpe();
        }
        this.lastRide = iEntityPlayerSP22.isRiding();
        if (((Boolean)this.autoHitValue.get()).booleanValue()) {
            IEntityPlayerSP iEntityPlayerSP23 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP23 == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP23.isRiding() && this.hitTimer.hasTimePassed(1500L)) {
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient == null) {
                    Intrinsics.throwNpe();
                }
                for (IEntity entity : iWorldClient.getLoadedEntityList()) {
                    if (!(entity instanceof EntityBoat)) continue;
                    IEntityPlayerSP iEntityPlayerSP24 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP24 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!(iEntityPlayerSP24.getDistanceToEntity(entity) < (float)3)) continue;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(entity, new WVec3(0.5, 0.5, 0.5)));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(entity, ICPacketUseEntity.WAction.INTERACT));
                    this.hitTimer.reset();
                }
            }
        }
    }
}
