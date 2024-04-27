/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="BufferSpeed", description="Allows you to walk faster on slabs and stairs.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0006\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0011\u0010&\u001a\u00020'2\u0006\u0010&\u001a\u00020(H\u0082\bJ\b\u0010)\u001a\u00020'H\u0016J\b\u0010*\u001a\u00020'H\u0016J\u0010\u0010+\u001a\u00020'2\u0006\u0010,\u001a\u00020-H\u0007J\u0012\u0010.\u001a\u00020'2\b\u0010,\u001a\u0004\u0018\u00010/H\u0007J\b\u00100\u001a\u00020'H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\u00020\u00078BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00061"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/BufferSpeed;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "airStrafeValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "bufferValue", "down", "", "fastHop", "forceDown", "hadFastHop", "headBlockBoostValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "headBlockValue", "iceBoostValue", "iceValue", "isNearBlock", "()Z", "legitHop", "maxSpeedValue", "noHurtValue", "slabsBoostValue", "slabsModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "slabsValue", "slimeValue", "snowBoostValue", "snowPortValue", "snowValue", "speed", "", "speedLimitValue", "stairsBoostValue", "stairsModeValue", "stairsValue", "wallBoostValue", "wallModeValue", "wallValue", "boost", "", "", "onDisable", "onEnable", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "reset", "LiKingSense"})
public final class BufferSpeed
extends Module {
    public final BoolValue speedLimitValue = new BoolValue("SpeedLimit", true);
    public final FloatValue maxSpeedValue = new FloatValue("MaxSpeed", 2.0f, 1.0f, 5.0f);
    public final BoolValue bufferValue = new BoolValue("Buffer", true);
    public final BoolValue stairsValue = new BoolValue("Stairs", true);
    public final FloatValue stairsBoostValue = new FloatValue("StairsBoost", 1.87f, 1.0f, 2.0f);
    public final ListValue stairsModeValue = new ListValue("StairsMode", new String[]{"Old", "New"}, "New");
    public final BoolValue slabsValue = new BoolValue("Slabs", true);
    public final FloatValue slabsBoostValue = new FloatValue("SlabsBoost", 1.87f, 1.0f, 2.0f);
    public final ListValue slabsModeValue = new ListValue("SlabsMode", new String[]{"Old", "New"}, "New");
    public final BoolValue iceValue = new BoolValue("Ice", false);
    public final FloatValue iceBoostValue = new FloatValue("IceBoost", 1.342f, 1.0f, 2.0f);
    public final BoolValue snowValue = new BoolValue("Snow", true);
    public final FloatValue snowBoostValue = new FloatValue("SnowBoost", 1.87f, 1.0f, 2.0f);
    public final BoolValue snowPortValue = new BoolValue("SnowPort", true);
    public final BoolValue wallValue = new BoolValue("Wall", true);
    public final FloatValue wallBoostValue = new FloatValue("WallBoost", 1.87f, 1.0f, 2.0f);
    public final ListValue wallModeValue = new ListValue("WallMode", new String[]{"Old", "New"}, "New");
    public final BoolValue headBlockValue = new BoolValue("HeadBlock", true);
    public final FloatValue headBlockBoostValue = new FloatValue("HeadBlockBoost", 1.87f, 1.0f, 2.0f);
    public final BoolValue slimeValue = new BoolValue("Slime", true);
    public final BoolValue airStrafeValue = new BoolValue("AirStrafe", false);
    public final BoolValue noHurtValue = new BoolValue("NoHurt", true);
    public double speed;
    public boolean down;
    public boolean forceDown;
    public boolean fastHop;
    public boolean hadFastHop;
    public boolean legitHop;

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            return;
        }
        thePlayer = v0;
        if (LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class).getState() || ((Boolean)this.noHurtValue.get()).booleanValue() && thePlayer.getHurtTime() > 0) {
            this.reset();
            return;
        }
        blockPos = new WBlockPos(thePlayer.getPosX(), thePlayer.getEntityBoundingBox().getMinY(), thePlayer.getPosZ());
        if (this.forceDown || this.down && thePlayer.getMotionY() == 0.0) {
            thePlayer.setMotionY(-1.0);
            this.down = 0;
            this.forceDown = 0;
        }
        if (this.fastHop) {
            thePlayer.setSpeedInAir(0.0211f);
            this.hadFastHop = 1;
        } else if (this.hadFastHop) {
            thePlayer.setSpeedInAir(0.02f);
            this.hadFastHop = 0;
        }
        if (!MovementUtils.isMoving() || thePlayer.isSneaking() || thePlayer.isInWater() || MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
            this.reset();
            return;
        }
        if (thePlayer.getOnGround()) {
            this.fastHop = 0;
            if (((Boolean)this.slimeValue.get()).booleanValue() && (MinecraftInstance.classProvider.isBlockSlime(BlockUtils.getBlock(blockPos.down())) || MinecraftInstance.classProvider.isBlockSlime(BlockUtils.getBlock(blockPos)))) {
                thePlayer.jump();
                thePlayer.setMotionX(thePlayer.getMotionY() * 1.132);
                thePlayer.setMotionY(0.08);
                thePlayer.setMotionZ(thePlayer.getMotionY() * 1.132);
                this.down = 1;
                return;
            }
            if (((Boolean)this.slabsValue.get()).booleanValue() && MinecraftInstance.classProvider.isBlockSlab(BlockUtils.getBlock(blockPos))) {
                var4_4 = (String)this.slabsModeValue.get();
                v1 = var4_4;
                if (v1 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                v2 = v1.toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull((Object)v2, (String)"(this as java.lang.String).toLowerCase()");
                var4_4 = v2;
                switch (var4_4.hashCode()) {
                    case 108960: {
                        if (!var4_4.equals("new")) ** break;
                        break;
                    }
                    case 110119: {
                        if (!var4_4.equals("old")) ** break;
                        var5_6 = this;
                        boost$iv = ((Number)this.slabsBoostValue.get()).floatValue();
                        thePlayer$iv = MinecraftInstance.mc.getThePlayer();
                        thePlayer$iv.setMotionX(thePlayer$iv.getMotionX() * (double)boost$iv);
                        thePlayer$iv.setMotionZ(thePlayer$iv.getMotionX() * (double)boost$iv);
                        BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, MovementUtils.INSTANCE.getSpeed());
                        if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)this_$iv).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)this_$iv) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).doubleValue()) {
                            BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).floatValue());
                        }
                        return;
                    }
                }
                this.fastHop = 1;
                if (this.legitHop) {
                    thePlayer.jump();
                    thePlayer.setOnGround(false);
                    this.legitHop = 0;
                    return;
                }
                thePlayer.setOnGround(false);
                MovementUtils.strafe(0.375f);
                thePlayer.jump();
                thePlayer.setMotionY(0.41);
                return;
            }
            if (((Boolean)this.stairsValue.get()).booleanValue() && (MinecraftInstance.classProvider.isBlockStairs(BlockUtils.getBlock(blockPos.down())) || MinecraftInstance.classProvider.isBlockStairs(BlockUtils.getBlock(blockPos)))) {
                var4_4 = (String)this.stairsModeValue.get();
                v3 = var4_4;
                if (v3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                v4 = v3.toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull((Object)v4, (String)"(this as java.lang.String).toLowerCase()");
                var4_4 = v4;
                switch (var4_4.hashCode()) {
                    case 108960: {
                        if (!var4_4.equals("new")) ** break;
                        break;
                    }
                    case 110119: {
                        if (!var4_4.equals("old")) ** break;
                        this_$iv = this;
                        boost$iv = ((Number)this.stairsBoostValue.get()).floatValue();
                        thePlayer$iv = MinecraftInstance.mc.getThePlayer();
                        thePlayer$iv.setMotionX(thePlayer$iv.getMotionX() * (double)boost$iv);
                        thePlayer$iv.setMotionZ(thePlayer$iv.getMotionX() * (double)boost$iv);
                        BufferSpeed.access$setSpeed$p(this_$iv, MovementUtils.INSTANCE.getSpeed());
                        if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p(this_$iv).get()).booleanValue() && BufferSpeed.access$getSpeed$p(this_$iv) > ((Number)BufferSpeed.access$getMaxSpeedValue$p(this_$iv).get()).doubleValue()) {
                            BufferSpeed.access$setSpeed$p(this_$iv, ((Number)BufferSpeed.access$getMaxSpeedValue$p(this_$iv).get()).floatValue());
                        }
                        return;
                    }
                }
                this.fastHop = 1;
                if (this.legitHop) {
                    thePlayer.jump();
                    thePlayer.setOnGround(false);
                    this.legitHop = 0;
                    return;
                }
                thePlayer.setOnGround(false);
                MovementUtils.strafe(0.375f);
                thePlayer.jump();
                thePlayer.setMotionY(0.41);
                return;
            }
            this.legitHop = 1;
            if (((Boolean)this.headBlockValue.get()).booleanValue() && Intrinsics.areEqual((Object)BlockUtils.getBlock(blockPos.up(2)), (Object)MinecraftInstance.classProvider.getBlockEnum(BlockType.AIR))) {
                var4_4 = this;
                boost$iv = ((Number)this.headBlockBoostValue.get()).floatValue();
                thePlayer$iv = MinecraftInstance.mc.getThePlayer();
                thePlayer$iv.setMotionX(thePlayer$iv.getMotionX() * (double)boost$iv);
                thePlayer$iv.setMotionZ(thePlayer$iv.getMotionX() * (double)boost$iv);
                BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, MovementUtils.INSTANCE.getSpeed());
                if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)this_$iv).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)this_$iv) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).doubleValue()) {
                    BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).floatValue());
                }
                return;
            }
            if (((Boolean)this.iceValue.get()).booleanValue() && (Intrinsics.areEqual((Object)BlockUtils.getBlock(blockPos.down()), (Object)MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE)) || Intrinsics.areEqual((Object)BlockUtils.getBlock(blockPos.down()), (Object)MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED)))) {
                this_$iv = this;
                boost$iv = ((Number)this.iceBoostValue.get()).floatValue();
                thePlayer$iv = MinecraftInstance.mc.getThePlayer();
                thePlayer$iv.setMotionX(thePlayer$iv.getMotionX() * (double)boost$iv);
                thePlayer$iv.setMotionZ(thePlayer$iv.getMotionX() * (double)boost$iv);
                BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, MovementUtils.INSTANCE.getSpeed());
                if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)this_$iv).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)this_$iv) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).doubleValue()) {
                    BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).floatValue());
                }
                return;
            }
            if (((Boolean)this.snowValue.get()).booleanValue() && Intrinsics.areEqual((Object)BlockUtils.getBlock(blockPos), (Object)MinecraftInstance.classProvider.getBlockEnum(BlockType.SNOW_LAYER)) && (((Boolean)this.snowPortValue.get()).booleanValue() || thePlayer.getPosY() - (double)((int)thePlayer.getPosY()) >= 0.125)) {
                if (thePlayer.getPosY() - (double)((int)thePlayer.getPosY()) >= 0.125) {
                    this_$iv = this;
                    boost$iv = ((Number)this.snowBoostValue.get()).floatValue();
                    thePlayer$iv = MinecraftInstance.mc.getThePlayer();
                    thePlayer$iv.setMotionX(thePlayer$iv.getMotionX() * (double)boost$iv);
                    thePlayer$iv.setMotionZ(thePlayer$iv.getMotionX() * (double)boost$iv);
                    BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, MovementUtils.INSTANCE.getSpeed());
                    if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)this_$iv).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)this_$iv) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).doubleValue()) {
                        BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).floatValue());
                    }
                } else {
                    thePlayer.jump();
                    this.forceDown = 1;
                }
                return;
            }
            if (((Boolean)this.wallValue.get()).booleanValue()) {
                v5 = this_$iv = (String)this.wallModeValue.get();
                if (v5 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                v6 = v5.toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull((Object)v6, (String)"(this as java.lang.String).toLowerCase()");
                this_$iv = v6;
                switch (this_$iv.hashCode()) {
                    case 108960: {
                        if (!this_$iv.equals("new")) ** break;
                        break;
                    }
                    case 110119: {
                        if (!this_$iv.equals("old") || (!thePlayer.isCollidedVertically() || !this.isNearBlock()) && MinecraftInstance.classProvider.isBlockAir(BlockUtils.getBlock(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + 2.0, thePlayer.getPosZ())))) ** break;
                        boost$iv = this;
                        boost$iv = ((Number)this.wallBoostValue.get()).floatValue();
                        thePlayer$iv = MinecraftInstance.mc.getThePlayer();
                        thePlayer$iv.setMotionX(thePlayer$iv.getMotionX() * (double)boost$iv);
                        thePlayer$iv.setMotionZ(thePlayer$iv.getMotionX() * (double)boost$iv);
                        BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, MovementUtils.INSTANCE.getSpeed());
                        if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)this_$iv).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)this_$iv) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).doubleValue()) {
                            BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).floatValue());
                        }
                        return;
                    }
                }
                if (this.isNearBlock() && !thePlayer.getMovementInput().getJump()) {
                    thePlayer.jump();
                    thePlayer.setMotionY(0.08);
                    thePlayer.setMotionX(thePlayer.getMotionX() * 0.99);
                    thePlayer.setMotionZ(thePlayer.getMotionX() * 0.99);
                    this.down = 1;
                    return;
                }
            }
            if (this.speed < (double)(currentSpeed = MovementUtils.INSTANCE.getSpeed())) {
                this.speed = currentSpeed;
            }
            if (((Boolean)this.bufferValue.get()).booleanValue() && this.speed > (double)0.2f) {
                this.speed /= 1.0199999809265137;
                MovementUtils.strafe((float)this.speed);
            }
        } else {
            this.speed = 0.0;
            if (((Boolean)this.airStrafeValue.get()).booleanValue()) {
                MovementUtils.strafe$default(0.0f, 1, null);
            }
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isSPacketPlayerPosLook(packet)) {
            this.speed = 0.0;
        }
    }

    @Override
    public void onEnable() {
        this.reset();
    }

    @Override
    public void onDisable() {
        this.reset();
    }

    public final void reset() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        this.legitHop = 1;
        this.speed = 0.0;
        if (this.hadFastHop) {
            thePlayer.setSpeedInAir(0.02f);
            this.hadFastHop = 0;
        }
    }

    public final void boost(float boost) {
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        thePlayer.setMotionX(thePlayer.getMotionX() * (double)boost);
        thePlayer.setMotionZ(thePlayer.getMotionX() * (double)boost);
        this.speed = MovementUtils.INSTANCE.getSpeed();
        if (((Boolean)this.speedLimitValue.get()).booleanValue() && this.speed > ((Number)this.maxSpeedValue.get()).doubleValue()) {
            this.speed = ((Number)this.maxSpeedValue.get()).floatValue();
        }
    }

    public final boolean isNearBlock() {
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        IWorldClient theWorld = MinecraftInstance.mc.getTheWorld();
        List blocks = new ArrayList();
        blocks.add(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + (double)1, thePlayer.getPosZ() - 0.7));
        blocks.add(new WBlockPos(thePlayer.getPosX() + 0.7, thePlayer.getPosY() + (double)1, thePlayer.getPosZ()));
        blocks.add(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + (double)1, thePlayer.getPosZ() + 0.7));
        blocks.add(new WBlockPos(thePlayer.getPosX() - 0.7, thePlayer.getPosY() + (double)1, thePlayer.getPosZ()));
        for (WBlockPos blockPos : blocks) {
            IIBlockState blockState = theWorld.getBlockState(blockPos);
            IAxisAlignedBB collisionBoundingBox = blockState.getBlock().getCollisionBoundingBox(theWorld, blockPos, blockState);
            if ((collisionBoundingBox != null && collisionBoundingBox.getMaxX() != collisionBoundingBox.getMinY() + (double)1 || blockState.getBlock().isTranslucent(blockState) || !Intrinsics.areEqual((Object)blockState.getBlock(), (Object)MinecraftInstance.classProvider.getBlockEnum(BlockType.WATER)) || MinecraftInstance.classProvider.isBlockSlab(blockState.getBlock())) && !Intrinsics.areEqual((Object)blockState.getBlock(), (Object)MinecraftInstance.classProvider.getBlockEnum(BlockType.BARRIER))) continue;
            return true;
        }
        return false;
    }
}

