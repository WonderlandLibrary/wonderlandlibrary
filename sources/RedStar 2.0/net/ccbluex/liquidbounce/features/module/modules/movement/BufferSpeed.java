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
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="BufferSpeed", description="Allows you to walk faster on slabs and stairs.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000N\n\n\n\b\n\n\b\n\n\b\n\n\b\n\n\n\b\n\n\b\b\n\n\n\b\n\n\u0000\n\n\b\b\u000020B¢J&0'2&0(H\bJ\b)0'HJ\b*0'HJ+0'2,0-HJ.0'2\b,0/HJ\b00'HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0X¢\n\u0000R\t0X¢\n\u0000R\n0X¢\n\u0000R0\fX¢\n\u0000R\r0X¢\n\u0000R0\fX¢\n\u0000R0X¢\n\u0000R08BX¢\bR0X¢\n\u0000R0\fX¢\n\u0000R0X¢\n\u0000R0\fX¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0\fX¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R 0\fX¢\n\u0000R!0X¢\n\u0000R\"0X¢\n\u0000R#0\fX¢\n\u0000R$0X¢\n\u0000R%0X¢\n\u0000¨1"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/BufferSpeed;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "airStrafeValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "bufferValue", "down", "", "fastHop", "forceDown", "hadFastHop", "headBlockBoostValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "headBlockValue", "iceBoostValue", "iceValue", "isNearBlock", "()Z", "legitHop", "maxSpeedValue", "noHurtValue", "slabsBoostValue", "slabsModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "slabsValue", "slimeValue", "snowBoostValue", "snowPortValue", "snowValue", "speed", "", "speedLimitValue", "stairsBoostValue", "stairsModeValue", "stairsValue", "wallBoostValue", "wallModeValue", "wallValue", "boost", "", "", "onDisable", "onEnable", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "reset", "Pride"})
public final class BufferSpeed
extends Module {
    private final BoolValue speedLimitValue = new BoolValue("SpeedLimit", true);
    private final FloatValue maxSpeedValue = new FloatValue("MaxSpeed", 2.0f, 1.0f, 5.0f);
    private final BoolValue bufferValue = new BoolValue("Buffer", true);
    private final BoolValue stairsValue = new BoolValue("Stairs", true);
    private final FloatValue stairsBoostValue = new FloatValue("StairsBoost", 1.87f, 1.0f, 2.0f);
    private final ListValue stairsModeValue = new ListValue("StairsMode", new String[]{"Old", "New"}, "New");
    private final BoolValue slabsValue = new BoolValue("Slabs", true);
    private final FloatValue slabsBoostValue = new FloatValue("SlabsBoost", 1.87f, 1.0f, 2.0f);
    private final ListValue slabsModeValue = new ListValue("SlabsMode", new String[]{"Old", "New"}, "New");
    private final BoolValue iceValue = new BoolValue("Ice", false);
    private final FloatValue iceBoostValue = new FloatValue("IceBoost", 1.342f, 1.0f, 2.0f);
    private final BoolValue snowValue = new BoolValue("Snow", true);
    private final FloatValue snowBoostValue = new FloatValue("SnowBoost", 1.87f, 1.0f, 2.0f);
    private final BoolValue snowPortValue = new BoolValue("SnowPort", true);
    private final BoolValue wallValue = new BoolValue("Wall", true);
    private final FloatValue wallBoostValue = new FloatValue("WallBoost", 1.87f, 1.0f, 2.0f);
    private final ListValue wallModeValue = new ListValue("WallMode", new String[]{"Old", "New"}, "New");
    private final BoolValue headBlockValue = new BoolValue("HeadBlock", true);
    private final FloatValue headBlockBoostValue = new FloatValue("HeadBlockBoost", 1.87f, 1.0f, 2.0f);
    private final BoolValue slimeValue = new BoolValue("Slime", true);
    private final BoolValue airStrafeValue = new BoolValue("AirStrafe", false);
    private final BoolValue noHurtValue = new BoolValue("NoHurt", true);
    private double speed;
    private boolean down;
    private boolean forceDown;
    private boolean fastHop;
    private boolean hadFastHop;
    private boolean legitHop;

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        block71: {
            block64: {
                block69: {
                    block70: {
                        block67: {
                            block68: {
                                block65: {
                                    block66: {
                                        v0 = MinecraftInstance.mc.getThePlayer();
                                        if (v0 == null) {
                                            return;
                                        }
                                        thePlayer = v0;
                                        v1 = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
                                        if (v1 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        if (v1.getState() || ((Boolean)this.noHurtValue.get()).booleanValue() && thePlayer.getHurtTime() > 0) {
                                            this.reset();
                                            return;
                                        }
                                        blockPos = new WBlockPos(thePlayer.getPosX(), thePlayer.getEntityBoundingBox().getMinY(), thePlayer.getPosZ());
                                        if (this.forceDown || this.down && thePlayer.getMotionY() == 0.0) {
                                            thePlayer.setMotionY(-1.0);
                                            this.down = false;
                                            this.forceDown = false;
                                        }
                                        if (this.fastHop) {
                                            thePlayer.setSpeedInAir(0.0211f);
                                            this.hadFastHop = true;
                                        } else if (this.hadFastHop) {
                                            thePlayer.setSpeedInAir(0.02f);
                                            this.hadFastHop = false;
                                        }
                                        if (!MovementUtils.isMoving() || thePlayer.isSneaking() || thePlayer.isInWater() || MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
                                            this.reset();
                                            return;
                                        }
                                        if (!thePlayer.getOnGround()) break block64;
                                        this.fastHop = false;
                                        if (!((Boolean)this.slimeValue.get()).booleanValue()) break block65;
                                        var4_4 = blockPos.down();
                                        var9_16 = MinecraftInstance.classProvider;
                                        $i$f$getBlock = false;
                                        v2 = MinecraftInstance.mc.getTheWorld();
                                        v3 = v2 != null && (v2 = v2.getBlockState((WBlockPos)blockPos$iv)) != null ? v2.getBlock() : (var10_25 = null);
                                        if (var9_16.isBlockSlime(var10_25)) break block66;
                                        var9_16 = MinecraftInstance.classProvider;
                                        $i$f$getBlock = false;
                                        v4 = MinecraftInstance.mc.getTheWorld();
                                        v5 = v4 != null && (v4 = v4.getBlockState(blockPos)) != null ? v4.getBlock() : (var10_25 = null);
                                        if (!var9_16.isBlockSlime(var10_25)) break block65;
                                    }
                                    thePlayer.jump();
                                    thePlayer.setMotionX(thePlayer.getMotionY() * 1.132);
                                    thePlayer.setMotionY(0.08);
                                    thePlayer.setMotionZ(thePlayer.getMotionY() * 1.132);
                                    this.down = true;
                                    return;
                                }
                                if (((Boolean)this.slabsValue.get()).booleanValue()) {
                                    var9_16 = MinecraftInstance.classProvider;
                                    $i$f$getBlock = false;
                                    v6 = MinecraftInstance.mc.getTheWorld();
                                    v7 = v6 != null && (v6 = v6.getBlockState(blockPos)) != null ? v6.getBlock() : (var10_25 = null);
                                    if (var9_16.isBlockSlab(var10_25)) {
                                        $i$f$getBlock = (String)this.slabsModeValue.get();
                                        $i$f$getBlock = false;
                                        v8 = $i$f$getBlock;
                                        if (v8 == null) {
                                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                        }
                                        v9 = v8.toLowerCase();
                                        Intrinsics.checkExpressionValueIsNotNull(v9, "(this as java.lang.String).toLowerCase()");
                                        $i$f$getBlock = v9;
                                        switch ($i$f$getBlock.hashCode()) {
                                            case 108960: {
                                                if (!$i$f$getBlock.equals("new")) ** break;
                                                break;
                                            }
                                            case 110119: {
                                                if (!$i$f$getBlock.equals("old")) ** break;
                                                $i$f$getBlock = this;
                                                boost$iv = ((Number)this.slabsBoostValue.get()).floatValue();
                                                $i$f$boost = false;
                                                v10 = MinecraftInstance.mc.getThePlayer();
                                                if (v10 == null) {
                                                    Intrinsics.throwNpe();
                                                }
                                                thePlayer$iv = v10;
                                                thePlayer$iv.setMotionX(thePlayer$iv.getMotionX() * (double)boost$iv);
                                                thePlayer$iv.setMotionZ(thePlayer$iv.getMotionX() * (double)boost$iv);
                                                BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, MovementUtils.INSTANCE.getSpeed());
                                                if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)this_$iv).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)this_$iv) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).doubleValue()) {
                                                    BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).floatValue());
                                                }
                                                return;
                                            }
                                        }
                                        this.fastHop = true;
                                        if (this.legitHop) {
                                            thePlayer.jump();
                                            thePlayer.setOnGround(false);
                                            this.legitHop = false;
                                            return;
                                        }
                                        thePlayer.setOnGround(false);
                                        MovementUtils.strafe(0.375f);
                                        thePlayer.jump();
                                        thePlayer.setMotionY(0.41);
                                        return;
                                    }
                                }
                                if (!((Boolean)this.stairsValue.get()).booleanValue()) break block67;
                                $i$f$getBlock = blockPos.down();
                                var9_16 = MinecraftInstance.classProvider;
                                $i$f$getBlock = false;
                                v11 = MinecraftInstance.mc.getTheWorld();
                                v12 = v11 != null && (v11 = v11.getBlockState((WBlockPos)blockPos$iv)) != null ? v11.getBlock() : (var10_25 = null);
                                if (var9_16.isBlockStairs(var10_25)) break block68;
                                var9_16 = MinecraftInstance.classProvider;
                                $i$f$getBlock = false;
                                v13 = MinecraftInstance.mc.getTheWorld();
                                v14 = v13 != null && (v13 = v13.getBlockState(blockPos)) != null ? v13.getBlock() : (var10_25 = null);
                                if (!var9_16.isBlockStairs(var10_25)) break block67;
                            }
                            $i$f$getBlock = (String)this.stairsModeValue.get();
                            $i$f$getBlock = false;
                            v15 = $i$f$getBlock;
                            if (v15 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                            }
                            v16 = v15.toLowerCase();
                            Intrinsics.checkExpressionValueIsNotNull(v16, "(this as java.lang.String).toLowerCase()");
                            $i$f$getBlock = v16;
                            switch ($i$f$getBlock.hashCode()) {
                                case 108960: {
                                    if (!$i$f$getBlock.equals("new")) ** break;
                                    break;
                                }
                                case 110119: {
                                    if (!$i$f$getBlock.equals("old")) ** break;
                                    $i$f$getBlock = this;
                                    boost$iv = ((Number)this.stairsBoostValue.get()).floatValue();
                                    $i$f$boost = false;
                                    v17 = MinecraftInstance.mc.getThePlayer();
                                    if (v17 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    thePlayer$iv = v17;
                                    thePlayer$iv.setMotionX(thePlayer$iv.getMotionX() * (double)boost$iv);
                                    thePlayer$iv.setMotionZ(thePlayer$iv.getMotionX() * (double)boost$iv);
                                    BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, MovementUtils.INSTANCE.getSpeed());
                                    if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)this_$iv).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)this_$iv) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).doubleValue()) {
                                        BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).floatValue());
                                    }
                                    return;
                                }
                            }
                            this.fastHop = true;
                            if (this.legitHop) {
                                thePlayer.jump();
                                thePlayer.setOnGround(false);
                                this.legitHop = false;
                                return;
                            }
                            thePlayer.setOnGround(false);
                            MovementUtils.strafe(0.375f);
                            thePlayer.jump();
                            thePlayer.setMotionY(0.41);
                            return;
                        }
                        this.legitHop = true;
                        if (((Boolean)this.headBlockValue.get()).booleanValue()) {
                            blockPos$iv = blockPos.up(2);
                            $i$f$getBlock = false;
                            v18 = MinecraftInstance.mc.getTheWorld();
                            if (Intrinsics.areEqual(v18 != null && (v18 = v18.getBlockState((WBlockPos)blockPos$iv)) != null ? v18.getBlock() : null, MinecraftInstance.classProvider.getBlockEnum(BlockType.AIR))) {
                                blockPos$iv = this;
                                boost$iv = ((Number)this.headBlockBoostValue.get()).floatValue();
                                $i$f$boost = false;
                                v19 = MinecraftInstance.mc.getThePlayer();
                                if (v19 == null) {
                                    Intrinsics.throwNpe();
                                }
                                thePlayer$iv = v19;
                                thePlayer$iv.setMotionX(thePlayer$iv.getMotionX() * (double)boost$iv);
                                thePlayer$iv.setMotionZ(thePlayer$iv.getMotionX() * (double)boost$iv);
                                BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, MovementUtils.INSTANCE.getSpeed());
                                if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)this_$iv).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)this_$iv) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).doubleValue()) {
                                    BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).floatValue());
                                }
                                return;
                            }
                        }
                        if (!((Boolean)this.iceValue.get()).booleanValue()) break block69;
                        blockPos$iv = blockPos.down();
                        $i$f$getBlock = false;
                        v20 = MinecraftInstance.mc.getTheWorld();
                        if (Intrinsics.areEqual(v20 != null && (v20 = v20.getBlockState((WBlockPos)blockPos$iv)) != null ? v20.getBlock() : null, MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE))) break block70;
                        blockPos$iv = blockPos.down();
                        $i$f$getBlock = false;
                        v21 = MinecraftInstance.mc.getTheWorld();
                        if (!Intrinsics.areEqual(v21 != null && (v21 = v21.getBlockState((WBlockPos)blockPos$iv)) != null ? v21.getBlock() : null, MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED))) break block69;
                    }
                    blockPos$iv = this;
                    boost$iv = ((Number)this.iceBoostValue.get()).floatValue();
                    $i$f$boost = false;
                    v22 = MinecraftInstance.mc.getThePlayer();
                    if (v22 == null) {
                        Intrinsics.throwNpe();
                    }
                    thePlayer$iv = v22;
                    thePlayer$iv.setMotionX(thePlayer$iv.getMotionX() * (double)boost$iv);
                    thePlayer$iv.setMotionZ(thePlayer$iv.getMotionX() * (double)boost$iv);
                    BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, MovementUtils.INSTANCE.getSpeed());
                    if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)this_$iv).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)this_$iv) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).doubleValue()) {
                        BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).floatValue());
                    }
                    return;
                }
                if (((Boolean)this.snowValue.get()).booleanValue()) {
                    $i$f$getBlock = false;
                    v23 = MinecraftInstance.mc.getTheWorld();
                    if (Intrinsics.areEqual(v23 != null && (v23 = v23.getBlockState(blockPos)) != null ? v23.getBlock() : null, MinecraftInstance.classProvider.getBlockEnum(BlockType.SNOW_LAYER)) && (((Boolean)this.snowPortValue.get()).booleanValue() || thePlayer.getPosY() - (double)((int)thePlayer.getPosY()) >= 0.125)) {
                        if (thePlayer.getPosY() - (double)((int)thePlayer.getPosY()) >= 0.125) {
                            $i$f$getBlock = this;
                            boost$iv = ((Number)this.snowBoostValue.get()).floatValue();
                            $i$f$boost = false;
                            v24 = MinecraftInstance.mc.getThePlayer();
                            if (v24 == null) {
                                Intrinsics.throwNpe();
                            }
                            thePlayer$iv = v24;
                            thePlayer$iv.setMotionX(thePlayer$iv.getMotionX() * (double)boost$iv);
                            thePlayer$iv.setMotionZ(thePlayer$iv.getMotionX() * (double)boost$iv);
                            BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, MovementUtils.INSTANCE.getSpeed());
                            if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)this_$iv).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)this_$iv) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).doubleValue()) {
                                BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).floatValue());
                            }
                        } else {
                            thePlayer.jump();
                            this.forceDown = true;
                        }
                        return;
                    }
                }
                if (((Boolean)this.wallValue.get()).booleanValue()) {
                    this_$iv = (String)this.wallModeValue.get();
                    boost$iv = false;
                    v25 = this_$iv;
                    if (v25 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    v26 = v25.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull(v26, "(this as java.lang.String).toLowerCase()");
                    this_$iv = v26;
                    switch (this_$iv.hashCode()) {
                        case 108960: {
                            if (!this_$iv.equals("new")) ** break;
                            break;
                        }
                        case 110119: {
                            if (!this_$iv.equals("old")) ** break;
                            if (!thePlayer.isCollidedVertically() || !this.isNearBlock()) {
                                boost$iv = new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + 2.0, thePlayer.getPosZ());
                                var9_16 = MinecraftInstance.classProvider;
                                $i$f$getBlock = false;
                                v27 = MinecraftInstance.mc.getTheWorld();
                                v28 = v27 != null && (v27 = v27.getBlockState((WBlockPos)blockPos$iv)) != null ? v27.getBlock() : (var10_25 = null);
                                if (var9_16.isBlockAir(var10_25)) ** break;
                            }
                            blockPos$iv = this;
                            boost$iv = ((Number)this.wallBoostValue.get()).floatValue();
                            $i$f$boost = false;
                            v29 = MinecraftInstance.mc.getThePlayer();
                            if (v29 == null) {
                                Intrinsics.throwNpe();
                            }
                            thePlayer$iv = v29;
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
                        this.down = true;
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
                break block71;
            }
            this.speed = 0.0;
            if (((Boolean)this.airStrafeValue.get()).booleanValue()) {
                MovementUtils.strafe$default(0.0f, 1, null);
            }
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
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

    private final void reset() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        this.legitHop = true;
        this.speed = 0.0;
        if (this.hadFastHop) {
            thePlayer.setSpeedInAir(0.02f);
            this.hadFastHop = false;
        }
    }

    private final void boost(float boost) {
        int $i$f$boost = 0;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        thePlayer.setMotionX(thePlayer.getMotionX() * (double)boost);
        thePlayer.setMotionZ(thePlayer.getMotionX() * (double)boost);
        this.speed = MovementUtils.INSTANCE.getSpeed();
        if (((Boolean)this.speedLimitValue.get()).booleanValue() && this.speed > ((Number)this.maxSpeedValue.get()).doubleValue()) {
            this.speed = ((Number)this.maxSpeedValue.get()).floatValue();
        }
    }

    private final boolean isNearBlock() {
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        IWorldClient theWorld = MinecraftInstance.mc.getTheWorld();
        List blocks = new ArrayList();
        IEntityPlayerSP iEntityPlayerSP = thePlayer;
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        blocks.add(new WBlockPos(iEntityPlayerSP.getPosX(), thePlayer.getPosY() + 1.0, thePlayer.getPosZ() - 0.7));
        blocks.add(new WBlockPos(thePlayer.getPosX() + 0.7, thePlayer.getPosY() + 1.0, thePlayer.getPosZ()));
        blocks.add(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + 1.0, thePlayer.getPosZ() + 0.7));
        blocks.add(new WBlockPos(thePlayer.getPosX() - 0.7, thePlayer.getPosY() + 1.0, thePlayer.getPosZ()));
        for (WBlockPos blockPos : blocks) {
            IIBlockState blockState;
            IAxisAlignedBB collisionBoundingBox;
            IWorldClient iWorldClient = theWorld;
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            if (((collisionBoundingBox = (blockState = iWorldClient.getBlockState(blockPos)).getBlock().getCollisionBoundingBox(theWorld, blockPos, blockState)) != null && collisionBoundingBox.getMaxX() != collisionBoundingBox.getMinY() + 1.0 || blockState.getBlock().isTranslucent(blockState) || !Intrinsics.areEqual(blockState.getBlock(), MinecraftInstance.classProvider.getBlockEnum(BlockType.WATER)) || MinecraftInstance.classProvider.isBlockSlab(blockState.getBlock())) && !Intrinsics.areEqual(blockState.getBlock(), MinecraftInstance.classProvider.getBlockEnum(BlockType.BARRIER))) continue;
            return true;
        }
        return false;
    }
}
