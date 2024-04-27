/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.play.server.SPacketConfirmTransaction
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntityVelocity;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Velocity", description="Edit your velocity", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020@H\u0007J\b\u0010A\u001a\u00020>H\u0016J\b\u0010B\u001a\u00020>H\u0016J\u0010\u0010C\u001a\u00020>2\u0006\u0010?\u001a\u00020DH\u0007J\u0010\u0010E\u001a\u00020>2\u0006\u0010?\u001a\u00020FH\u0007J\u0010\u0010G\u001a\u00020>2\u0006\u0010?\u001a\u00020HH\u0007J\u0010\u0010I\u001a\u00020>2\u0006\u0010?\u001a\u00020JH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001d\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u0014\"\u0004\b\u001f\u0010\u0016R\u000e\u0010 \u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010)\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\u0014\"\u0004\b+\u0010\u0016R\u000e\u0010,\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010/\u001a\u0002008VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b1\u00102R\u001a\u00103\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b4\u0010\u0014\"\u0004\b5\u0010\u0016R\u000e\u00106\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u000209X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020;X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006K"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/Velocity;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aac4XZReducerValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "aacPushXZReducerValue", "aacPushYReducerValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "block", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "getBlock", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "setBlock", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;)V", "canCancelJump", "", "canCleanJump", "cancelPacket", "", "getCancelPacket", "()I", "setCancelPacket", "(I)V", "cobwebValue", "customC06FakeLag", "customX", "customY", "customYStart", "customZ", "grimTCancel", "getGrimTCancel", "setGrimTCancel", "horizontalValue", "hytGround", "hytpacketaset", "hytpacketbset", "jump", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "newaac4XZReducerValue", "noFireValue", "resetPersec", "getResetPersec", "setResetPersec", "reverse2StrengthValue", "reverseHurt", "reverseStrengthValue", "tag", "", "getTag", "()Ljava/lang/String;", "updates", "getUpdates", "setUpdates", "velocityInput", "velocityTick", "velocityTickValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "velocityTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "verticalValue", "onBlockBB", "", "event", "Lnet/ccbluex/liquidbounce/event/BlockBBEvent;", "onDisable", "onEnable", "onJump", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "PridePlus"})
public final class Velocity
extends Module {
    public final FloatValue horizontalValue;
    public final FloatValue verticalValue;
    public final ListValue modeValue;
    public final FloatValue aac4XZReducerValue;
    public final FloatValue newaac4XZReducerValue;
    public final IntegerValue velocityTickValue;
    public final FloatValue reverseStrengthValue;
    public final FloatValue reverse2StrengthValue;
    public final FloatValue hytpacketaset;
    public final FloatValue hytpacketbset;
    public final FloatValue aacPushXZReducerValue;
    public final BoolValue aacPushYReducerValue;
    @Nullable
    public IBlock block;
    public final BoolValue noFireValue;
    public final BoolValue cobwebValue;
    public final BoolValue hytGround;
    public final FloatValue customX;
    public final BoolValue customYStart;
    public final FloatValue customY;
    public final FloatValue customZ;
    public final BoolValue customC06FakeLag;
    public int cancelPacket;
    public int resetPersec;
    public int grimTCancel;
    public int updates;
    public MSTimer velocityTimer;
    public boolean velocityInput;
    public boolean canCleanJump;
    public int velocityTick;
    public boolean reverseHurt;
    public boolean jump;
    public boolean canCancelJump;

    @Nullable
    public final IBlock getBlock() {
        return this.block;
    }

    public final void setBlock(@Nullable IBlock iBlock) {
        this.block = iBlock;
    }

    public final int getCancelPacket() {
        return this.cancelPacket;
    }

    public final void setCancelPacket(int n) {
        this.cancelPacket = n;
    }

    public final int getResetPersec() {
        return this.resetPersec;
    }

    public final void setResetPersec(int n) {
        this.resetPersec = n;
    }

    public final int getGrimTCancel() {
        return this.grimTCancel;
    }

    public final void setGrimTCancel(int n) {
        this.grimTCancel = n;
    }

    public final int getUpdates() {
        return this.updates;
    }

    public final void setUpdates(int n) {
        this.updates = n;
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @Override
    public void onDisable() {
        block0: {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) break block0;
            iEntityPlayerSP.setSpeedInAir(0.02f);
        }
    }

    @Override
    public void onEnable() {
        this.updates = 0;
        super.onEnable();
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        String string;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        int n = this.updates;
        this.updates = n + 1;
        if (this.resetPersec > 0 && (this.updates >= 0 || this.updates >= this.resetPersec)) {
            this.updates = 0;
            if (this.grimTCancel > 0) {
                n = this.grimTCancel;
                this.grimTCancel = n + -1;
            }
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (thePlayer.isInWater() || thePlayer.isInLava() || thePlayer.isInWeb()) {
            return;
        }
        if (((Boolean)this.noFireValue.get()).booleanValue() && MinecraftInstance.mc.getThePlayer().isBurning()) {
            return;
        }
        String string2 = string = (String)this.modeValue.get();
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "jump-motiony": {
                MinecraftInstance.mc.getThePlayer().setMotionY(0.42);
                break;
            }
            case "newhyt": {
                if (!MinecraftInstance.mc.getThePlayer().getOnGround()) break;
                if (MinecraftInstance.mc.getThePlayer().getHurtTime() > 0 && MinecraftInstance.mc.getThePlayer().getMotionX() != 0.0 && MinecraftInstance.mc.getThePlayer().getMotionZ() != 0.0) {
                    MinecraftInstance.mc.getThePlayer().setOnGround(true);
                }
                if (MinecraftInstance.mc.getThePlayer().getHurtResistantTime() > 0) {
                    MinecraftInstance.mc.getThePlayer().setMotionY(MinecraftInstance.mc.getThePlayer().getMotionY() - 0.011999993);
                }
                if (MinecraftInstance.mc.getThePlayer().getHurtResistantTime() < 19) break;
                MinecraftInstance.mc.getThePlayer().setMotionX(MinecraftInstance.mc.getThePlayer().getMotionX() / (double)1.5f);
                MinecraftInstance.mc.getThePlayer().setMotionZ(MinecraftInstance.mc.getThePlayer().getMotionZ() / (double)1.6f);
                break;
            }
            case "newgrimac": {
                if (thePlayer.getHurtTime() > 0) {
                    IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                    iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() + -1.0E-7);
                    IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                    iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() + -1.0E-7);
                }
                if (thePlayer.getHurtTime() <= 5) break;
                IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                iEntityPlayerSP4.setMotionX(iEntityPlayerSP4.getMotionX() + -1.5E-7);
                IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                iEntityPlayerSP5.setMotionZ(iEntityPlayerSP5.getMotionZ() + -1.5E-7);
                break;
            }
            case "grimreduce": {
                if (thePlayer.getHurtTime() <= 0) break;
                IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
                iEntityPlayerSP6.setMotionX(iEntityPlayerSP6.getMotionX() + -1.0E-7);
                IEntityPlayerSP iEntityPlayerSP7 = thePlayer;
                iEntityPlayerSP7.setMotionZ(iEntityPlayerSP7.getMotionZ() + -1.0E-7);
                thePlayer.setAirBorne(true);
                break;
            }
            case "grim-motion": {
                if (thePlayer.getHurtTime() <= 0) break;
                IEntityPlayerSP iEntityPlayerSP8 = thePlayer;
                iEntityPlayerSP8.setMotionX(iEntityPlayerSP8.getMotionX() + -1.1E-7);
                IEntityPlayerSP iEntityPlayerSP9 = thePlayer;
                iEntityPlayerSP9.setMotionZ(iEntityPlayerSP9.getMotionZ() + -1.2E-7);
                thePlayer.setAirBorne(true);
                break;
            }
            case "jump": {
                if (thePlayer.getHurtTime() <= 0 || !thePlayer.getOnGround()) break;
                thePlayer.setMotionY(0.42);
                float yaw = thePlayer.getRotationYaw() * ((float)Math.PI / 180);
                IEntityPlayerSP iEntityPlayerSP10 = thePlayer;
                double d = iEntityPlayerSP10.getMotionX();
                IEntityPlayerSP iEntityPlayerSP11 = iEntityPlayerSP10;
                float f = (float)Math.sin(yaw);
                iEntityPlayerSP11.setMotionX(d - (double)f * 0.2);
                IEntityPlayerSP iEntityPlayerSP12 = thePlayer;
                d = iEntityPlayerSP12.getMotionZ();
                iEntityPlayerSP11 = iEntityPlayerSP12;
                f = (float)Math.cos(yaw);
                iEntityPlayerSP11.setMotionZ(d + (double)f * 0.2);
                break;
            }
            case "latesttesthyt": {
                if (((Boolean)this.hytGround.get()).booleanValue()) {
                    if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5 || !thePlayer.getOnGround()) break;
                    IEntityPlayerSP iEntityPlayerSP13 = thePlayer;
                    iEntityPlayerSP13.setMotionX(iEntityPlayerSP13.getMotionX() * 0.35);
                    IEntityPlayerSP iEntityPlayerSP14 = thePlayer;
                    iEntityPlayerSP14.setMotionZ(iEntityPlayerSP14.getMotionZ() * 0.35);
                    IEntityPlayerSP iEntityPlayerSP15 = thePlayer;
                    iEntityPlayerSP15.setMotionY(iEntityPlayerSP15.getMotionY() * 0.001);
                    IEntityPlayerSP iEntityPlayerSP16 = thePlayer;
                    iEntityPlayerSP16.setMotionY(iEntityPlayerSP16.getMotionY() / (double)0.01f);
                    break;
                }
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP17 = thePlayer;
                iEntityPlayerSP17.setMotionX(iEntityPlayerSP17.getMotionX() * 0.35);
                IEntityPlayerSP iEntityPlayerSP18 = thePlayer;
                iEntityPlayerSP18.setMotionZ(iEntityPlayerSP18.getMotionZ() * 0.35);
                IEntityPlayerSP iEntityPlayerSP19 = thePlayer;
                iEntityPlayerSP19.setMotionY(iEntityPlayerSP19.getMotionY() * 0.001);
                IEntityPlayerSP iEntityPlayerSP20 = thePlayer;
                iEntityPlayerSP20.setMotionY(iEntityPlayerSP20.getMotionY() / (double)0.01f);
                break;
            }
            case "glitch": {
                thePlayer.setNoClip(this.velocityInput);
                if (thePlayer.getHurtTime() == 7) {
                    thePlayer.setMotionY(0.4);
                }
                this.velocityInput = 0;
                break;
            }
            case "feile": {
                if (!thePlayer.getOnGround()) break;
                this.canCleanJump = 1;
                thePlayer.setMotionY(1.5);
                thePlayer.setMotionZ(1.2);
                thePlayer.setMotionX(1.5);
                if (!thePlayer.getOnGround() || this.velocityTick <= 2) break;
                this.velocityInput = 0;
                break;
            }
            case "aac5reduce": {
                if (thePlayer.getHurtTime() > 1 && this.velocityInput) {
                    IEntityPlayerSP iEntityPlayerSP21 = thePlayer;
                    iEntityPlayerSP21.setMotionX(iEntityPlayerSP21.getMotionX() * 0.81);
                    IEntityPlayerSP iEntityPlayerSP22 = thePlayer;
                    iEntityPlayerSP22.setMotionZ(iEntityPlayerSP22.getMotionZ() * 0.81);
                }
                if (!this.velocityInput || thePlayer.getHurtTime() >= 5 && !thePlayer.getOnGround() || !this.velocityTimer.hasTimePassed(23L - 37L + 27L - 6L + 113L)) break;
                this.velocityInput = 0;
                break;
            }
            case "hyttick": {
                if (this.velocityTick > ((Number)this.velocityTickValue.get()).intValue()) {
                    if (thePlayer.getMotionY() > (double)0) {
                        thePlayer.setMotionY(0.0);
                    }
                    thePlayer.setMotionX(0.0);
                    thePlayer.setMotionZ(0.0);
                    thePlayer.setJumpMovementFactor(-1.0E-5f);
                    this.velocityInput = 0;
                }
                if (!thePlayer.getOnGround() || this.velocityTick <= 1) break;
                this.velocityInput = 0;
                break;
            }
            case "reverse": {
                if (!this.velocityInput) {
                    return;
                }
                if (!thePlayer.getOnGround()) {
                    MovementUtils.strafe(MovementUtils.INSTANCE.getSpeed() * ((Number)this.reverseStrengthValue.get()).floatValue());
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(154L - 160L + 62L - 44L + 68L)) break;
                this.velocityInput = 0;
                break;
            }
            case "newaac4": {
                if (thePlayer.getHurtTime() <= 0 || thePlayer.getOnGround()) break;
                float reduce = ((Number)this.newaac4XZReducerValue.get()).floatValue();
                IEntityPlayerSP iEntityPlayerSP23 = thePlayer;
                iEntityPlayerSP23.setMotionX(iEntityPlayerSP23.getMotionX() * (double)reduce);
                IEntityPlayerSP iEntityPlayerSP24 = thePlayer;
                iEntityPlayerSP24.setMotionZ(iEntityPlayerSP24.getMotionZ() * (double)reduce);
                break;
            }
            case "smoothreverse": {
                if (!this.velocityInput) {
                    thePlayer.setSpeedInAir(0.02f);
                    return;
                }
                if (thePlayer.getHurtTime() > 0) {
                    this.reverseHurt = 1;
                }
                if (!thePlayer.getOnGround()) {
                    if (!this.reverseHurt) break;
                    thePlayer.setSpeedInAir(((Number)this.reverse2StrengthValue.get()).floatValue());
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(154L - 213L + 47L + 92L)) break;
                this.velocityInput = 0;
                this.reverseHurt = 0;
                break;
            }
            case "hytpacket": {
                if (((Boolean)this.hytGround.get()).booleanValue()) {
                    if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5 || !thePlayer.getOnGround()) break;
                    IEntityPlayerSP iEntityPlayerSP25 = thePlayer;
                    iEntityPlayerSP25.setMotionX(iEntityPlayerSP25.getMotionX() * 0.5);
                    IEntityPlayerSP iEntityPlayerSP26 = thePlayer;
                    iEntityPlayerSP26.setMotionZ(iEntityPlayerSP26.getMotionZ() * 0.5);
                    IEntityPlayerSP iEntityPlayerSP27 = thePlayer;
                    iEntityPlayerSP27.setMotionY(iEntityPlayerSP27.getMotionY() / (double)1.781145f);
                    break;
                }
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP28 = thePlayer;
                iEntityPlayerSP28.setMotionX(iEntityPlayerSP28.getMotionX() * 0.5);
                IEntityPlayerSP iEntityPlayerSP29 = thePlayer;
                iEntityPlayerSP29.setMotionZ(iEntityPlayerSP29.getMotionZ() * 0.5);
                IEntityPlayerSP iEntityPlayerSP30 = thePlayer;
                iEntityPlayerSP30.setMotionY(iEntityPlayerSP30.getMotionY() / (double)1.781145f);
                break;
            }
            case "hytmotion": {
                if (((Boolean)this.hytGround.get()).booleanValue()) {
                    if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5 || !thePlayer.getOnGround()) break;
                    IEntityPlayerSP iEntityPlayerSP31 = thePlayer;
                    iEntityPlayerSP31.setMotionX(iEntityPlayerSP31.getMotionX() * 0.4);
                    IEntityPlayerSP iEntityPlayerSP32 = thePlayer;
                    iEntityPlayerSP32.setMotionZ(iEntityPlayerSP32.getMotionZ() * 0.4);
                    IEntityPlayerSP iEntityPlayerSP33 = thePlayer;
                    iEntityPlayerSP33.setMotionY(iEntityPlayerSP33.getMotionY() * (double)0.381145f);
                    IEntityPlayerSP iEntityPlayerSP34 = thePlayer;
                    iEntityPlayerSP34.setMotionY(iEntityPlayerSP34.getMotionY() / (double)1.781145f);
                    break;
                }
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getHurtTime() > 5) break;
                IEntityPlayerSP iEntityPlayerSP35 = thePlayer;
                iEntityPlayerSP35.setMotionX(iEntityPlayerSP35.getMotionX() * 0.4);
                IEntityPlayerSP iEntityPlayerSP36 = thePlayer;
                iEntityPlayerSP36.setMotionZ(iEntityPlayerSP36.getMotionZ() * 0.4);
                IEntityPlayerSP iEntityPlayerSP37 = thePlayer;
                iEntityPlayerSP37.setMotionY(iEntityPlayerSP37.getMotionY() * (double)0.381145f);
                IEntityPlayerSP iEntityPlayerSP38 = thePlayer;
                iEntityPlayerSP38.setMotionY(iEntityPlayerSP38.getMotionY() / (double)1.781145f);
                break;
            }
            case "hytmotionb": {
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getOnGround() || thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) break;
                IEntityPlayerSP iEntityPlayerSP39 = thePlayer;
                iEntityPlayerSP39.setMotionX(iEntityPlayerSP39.getMotionX() * (double)0.451145f);
                IEntityPlayerSP iEntityPlayerSP40 = thePlayer;
                iEntityPlayerSP40.setMotionZ(iEntityPlayerSP40.getMotionZ() * (double)0.451145f);
                break;
            }
            case "newhytmotion": {
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || thePlayer.getOnGround()) break;
                if (!thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                    IEntityPlayerSP iEntityPlayerSP41 = thePlayer;
                    iEntityPlayerSP41.setMotionX(iEntityPlayerSP41.getMotionX() * 0.47188);
                    IEntityPlayerSP iEntityPlayerSP42 = thePlayer;
                    iEntityPlayerSP42.setMotionZ(iEntityPlayerSP42.getMotionZ() * 0.47188);
                    if (thePlayer.getMotionY() != 0.42 && !(thePlayer.getMotionY() > 0.42)) break;
                    IEntityPlayerSP iEntityPlayerSP43 = thePlayer;
                    iEntityPlayerSP43.setMotionY(iEntityPlayerSP43.getMotionY() * 0.4);
                    break;
                }
                IEntityPlayerSP iEntityPlayerSP44 = thePlayer;
                iEntityPlayerSP44.setMotionX(iEntityPlayerSP44.getMotionX() * 0.65025);
                IEntityPlayerSP iEntityPlayerSP45 = thePlayer;
                iEntityPlayerSP45.setMotionZ(iEntityPlayerSP45.getMotionZ() * 0.65025);
                if (thePlayer.getMotionY() != 0.42 && !(thePlayer.getMotionY() > 0.42)) break;
                IEntityPlayerSP iEntityPlayerSP46 = thePlayer;
                iEntityPlayerSP46.setMotionY(iEntityPlayerSP46.getMotionY() * 0.4);
                break;
            }
            case "aacpush": {
                if (this.jump) {
                    if (thePlayer.getOnGround()) {
                        this.jump = 0;
                    }
                } else {
                    if (thePlayer.getHurtTime() > 0 && thePlayer.getMotionX() != 0.0 && thePlayer.getMotionZ() != 0.0) {
                        thePlayer.setOnGround(true);
                    }
                    if (thePlayer.getHurtResistantTime() > 0 && ((Boolean)this.aacPushYReducerValue.get()).booleanValue() && !LiquidBounce.INSTANCE.getModuleManager().get(Speed.class).getState()) {
                        IEntityPlayerSP iEntityPlayerSP47 = thePlayer;
                        iEntityPlayerSP47.setMotionY(iEntityPlayerSP47.getMotionY() - 0.014999993);
                    }
                }
                if (thePlayer.getHurtResistantTime() < 19) break;
                float reduce = ((Number)this.aacPushXZReducerValue.get()).floatValue();
                IEntityPlayerSP iEntityPlayerSP48 = thePlayer;
                iEntityPlayerSP48.setMotionX(iEntityPlayerSP48.getMotionX() / (double)reduce);
                IEntityPlayerSP iEntityPlayerSP49 = thePlayer;
                iEntityPlayerSP49.setMotionZ(iEntityPlayerSP49.getMotionZ() / (double)reduce);
                break;
            }
            case "custom": {
                if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || MinecraftInstance.mc.getThePlayer().isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED)) || MinecraftInstance.mc.getThePlayer().isInWater()) break;
                IEntityPlayerSP iEntityPlayerSP50 = thePlayer;
                iEntityPlayerSP50.setMotionX(iEntityPlayerSP50.getMotionX() * ((Number)this.customX.get()).doubleValue());
                IEntityPlayerSP iEntityPlayerSP51 = thePlayer;
                iEntityPlayerSP51.setMotionZ(iEntityPlayerSP51.getMotionZ() * ((Number)this.customZ.get()).doubleValue());
                if (((Boolean)this.customYStart.get()).booleanValue()) {
                    IEntityPlayerSP iEntityPlayerSP52 = thePlayer;
                    iEntityPlayerSP52.setMotionY(iEntityPlayerSP52.getMotionY() / ((Number)this.customY.get()).doubleValue());
                }
                if (!((Boolean)this.customC06FakeLag.get()).booleanValue()) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosLook(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), thePlayer.getRotationYaw(), thePlayer.getRotationPitch(), thePlayer.getOnGround()));
                break;
            }
            case "aaczero": {
                if (thePlayer.getHurtTime() > 0) {
                    if (!this.velocityInput || thePlayer.getOnGround() || thePlayer.getFallDistance() > 2.0f) {
                        return;
                    }
                    IEntityPlayerSP iEntityPlayerSP53 = thePlayer;
                    iEntityPlayerSP53.setMotionY(iEntityPlayerSP53.getMotionY() - 1.0);
                    thePlayer.setAirBorne(true);
                    thePlayer.setOnGround(true);
                    break;
                }
                this.velocityInput = 0;
                break;
            }
        }
    }

    @EventTarget
    public final void onBlockBB(@NotNull BlockBBEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        this.block = event.getBlock();
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isSPacketEntityVelocity(packet)) {
            String string;
            ISPacketEntityVelocity packetEntityVelocity = packet.asSPacketEntityVelocity();
            if (((Boolean)this.noFireValue.get()).booleanValue() && MinecraftInstance.mc.getThePlayer().isBurning()) {
                return;
            }
            Object object = MinecraftInstance.mc.getTheWorld();
            if (object == null || (object = object.getEntityByID(packetEntityVelocity.getEntityID())) == null) {
                return;
            }
            if ((Intrinsics.areEqual((Object)object, (Object)thePlayer) ^ 1) != 0) {
                return;
            }
            this.velocityTimer.reset();
            String string2 = string = (String)this.modeValue.get();
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
            switch (string3) {
                case "grimac-c03": {
                    if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || MinecraftInstance.mc.getThePlayer().isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED)) || MinecraftInstance.mc.getThePlayer().isInWater()) break;
                    if (MinecraftInstance.classProvider.isCPacketPlayer(packet)) {
                        event.cancelEvent();
                    }
                    ISPacketEntityVelocity iSPacketEntityVelocity = packetEntityVelocity;
                    iSPacketEntityVelocity.setMotionX(iSPacketEntityVelocity.getMotionX() * 0);
                    ISPacketEntityVelocity iSPacketEntityVelocity2 = packetEntityVelocity;
                    iSPacketEntityVelocity2.setMotionY(iSPacketEntityVelocity2.getMotionY() * 0);
                    ISPacketEntityVelocity iSPacketEntityVelocity3 = packetEntityVelocity;
                    iSPacketEntityVelocity3.setMotionZ(iSPacketEntityVelocity3.getMotionZ() * 0);
                    break;
                }
                case "grimac-fdp": {
                    ISPacketEntityVelocity packetentityvelocity = packet.asSPacketEntityVelocity();
                    if (thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || MinecraftInstance.mc.getThePlayer().isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED)) || MinecraftInstance.mc.getThePlayer().isInWater() || !MinecraftInstance.mc.getThePlayer().getOnGround()) break;
                    if (packet instanceof SPacketConfirmTransaction && this.grimTCancel > 0) {
                        event.cancelEvent();
                        this.grimTCancel = this.cancelPacket;
                    }
                    if (packetentityvelocity.getEntityID() != MinecraftInstance.mc.getThePlayer().getEntityId()) break;
                    event.cancelEvent();
                    int n = this.grimTCancel;
                    this.grimTCancel = n + -1;
                    break;
                }
                case "vanilla": {
                    event.cancelEvent();
                    break;
                }
                case "simple": {
                    float horizontal = ((Number)this.horizontalValue.get()).floatValue();
                    float vertical = ((Number)this.verticalValue.get()).floatValue();
                    if (horizontal == 0.0f && vertical == 0.0f) {
                        event.cancelEvent();
                    }
                    packetEntityVelocity.setMotionX((int)((float)packetEntityVelocity.getMotionX() * horizontal));
                    packetEntityVelocity.setMotionY((int)((float)packetEntityVelocity.getMotionY() * vertical));
                    packetEntityVelocity.setMotionZ((int)((float)packetEntityVelocity.getMotionZ() * horizontal));
                    break;
                }
                case "hytpacketfix": {
                    if (!(thePlayer.getHurtTime() <= 0 || thePlayer.isDead() || MinecraftInstance.mc.getThePlayer().isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED)) || MinecraftInstance.mc.getThePlayer().isInWater())) {
                        IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                        iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() * 0.4);
                        IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                        iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() * 0.4);
                        IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                        iEntityPlayerSP4.setMotionY(iEntityPlayerSP4.getMotionY() / (double)1.45f);
                    }
                    if (thePlayer.getHurtTime() < 2) {
                        packetEntityVelocity.setMotionY(0);
                    }
                    if (thePlayer.getHurtTime() >= 6) break;
                    packetEntityVelocity.setMotionX(0);
                    packetEntityVelocity.setMotionZ(0);
                    break;
                }
                case "hyttest": {
                    if (!thePlayer.getOnGround()) break;
                    this.canCancelJump = 0;
                    packetEntityVelocity.setMotionX((int)0.985114);
                    packetEntityVelocity.setMotionY((int)0.885113);
                    packetEntityVelocity.setMotionZ((int)0.785112);
                    IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                    iEntityPlayerSP5.setMotionX(iEntityPlayerSP5.getMotionX() / 1.75);
                    IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
                    iEntityPlayerSP6.setMotionZ(iEntityPlayerSP6.getMotionZ() / 1.75);
                    break;
                }
                case "hytnewtest": {
                    if (!thePlayer.getOnGround()) break;
                    this.velocityInput = 1;
                    float yaw = thePlayer.getRotationYaw() * ((float)Math.PI / 180);
                    packetEntityVelocity.setMotionX((int)((double)packetEntityVelocity.getMotionX() * 0.75));
                    packetEntityVelocity.setMotionZ((int)((double)packetEntityVelocity.getMotionZ() * 0.75));
                    IEntityPlayerSP iEntityPlayerSP7 = thePlayer;
                    double d = iEntityPlayerSP7.getMotionX();
                    IEntityPlayerSP iEntityPlayerSP8 = iEntityPlayerSP7;
                    float f = (float)Math.sin(yaw);
                    iEntityPlayerSP8.setMotionX(d - (double)f * 0.2);
                    IEntityPlayerSP iEntityPlayerSP9 = thePlayer;
                    d = iEntityPlayerSP9.getMotionZ();
                    iEntityPlayerSP8 = iEntityPlayerSP9;
                    f = (float)Math.cos(yaw);
                    iEntityPlayerSP8.setMotionZ(d + (double)f * 0.2);
                    break;
                }
                case "hytpacketa": {
                    packetEntityVelocity.setMotionX((int)((double)((float)packetEntityVelocity.getMotionX() * ((Number)this.hytpacketaset.get()).floatValue()) / 1.5));
                    packetEntityVelocity.setMotionY((int)0.7);
                    packetEntityVelocity.setMotionZ((int)((double)((float)packetEntityVelocity.getMotionZ() * ((Number)this.hytpacketaset.get()).floatValue()) / 1.5));
                    event.cancelEvent();
                    break;
                }
                case "hytpacketb": {
                    packetEntityVelocity.setMotionX((int)((double)((float)packetEntityVelocity.getMotionX() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
                    packetEntityVelocity.setMotionY((int)((double)((float)packetEntityVelocity.getMotionY() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
                    packetEntityVelocity.setMotionZ((int)((double)((float)packetEntityVelocity.getMotionZ() * ((Number)this.hytpacketbset.get()).floatValue()) / 2.5));
                    break;
                }
                case "aac": 
                case "aaczero": 
                case "reverse": 
                case "aac4": 
                case "smoothreverse": 
                case "aac5reduce": {
                    this.velocityInput = 1;
                    break;
                }
                case "hyttick": {
                    this.velocityInput = 1;
                    float horizontal = 0.0f;
                    float vertical = 0.0f;
                    event.cancelEvent();
                    break;
                }
                case "glitch": {
                    if (!thePlayer.getOnGround()) {
                        return;
                    }
                    this.velocityInput = 1;
                    event.cancelEvent();
                    break;
                }
            }
        }
    }

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        String string;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        String string2 = string = (String)this.modeValue.get();
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case 477609814: {
                if (!string.equals("grimac-fdp") || MinecraftInstance.mc.getThePlayer().getHurtTime() <= 0 || MinecraftInstance.mc.getThePlayer().isDead() || MinecraftInstance.mc.getThePlayer().isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED)) || MinecraftInstance.mc.getThePlayer().isInWater() || !MinecraftInstance.mc.getThePlayer().getOnGround()) break;
                event.zero();
                break;
            }
        }
    }

    @EventTarget
    public final void onJump(@NotNull JumpEvent event) {
        String string;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        if (thePlayer == null || thePlayer.isInWater() || thePlayer.isInLava() || thePlayer.isInWeb()) {
            return;
        }
        String string2 = string = (String)this.modeValue.get();
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "aacpush": {
                this.jump = 1;
                if (thePlayer.isCollidedVertically()) break;
                event.cancelEvent();
                break;
            }
            case "aac4": {
                if (thePlayer.getHurtTime() <= 0) break;
                event.cancelEvent();
                break;
            }
            case "aaczero": {
                if (thePlayer.getHurtTime() <= 0) break;
                event.cancelEvent();
                break;
            }
        }
    }

    /*
     * Exception decompiling
     */
    public Velocity() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl174 : PUTFIELD - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }
}

