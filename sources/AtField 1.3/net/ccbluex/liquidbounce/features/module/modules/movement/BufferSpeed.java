/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
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
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="BufferSpeed", description="Allows you to walk faster on slabs and stairs.", category=ModuleCategory.MOVEMENT)
public final class BufferSpeed
extends Module {
    private final FloatValue snowBoostValue;
    private final FloatValue iceBoostValue;
    private final ListValue slabsModeValue;
    private double speed;
    private final FloatValue headBlockBoostValue;
    private boolean legitHop;
    private final FloatValue maxSpeedValue;
    private final BoolValue slabsValue;
    private final ListValue stairsModeValue;
    private final BoolValue airStrafeValue;
    private final BoolValue slimeValue;
    private final BoolValue snowPortValue;
    private final BoolValue wallValue;
    private final BoolValue headBlockValue;
    private boolean hadFastHop;
    private final BoolValue noHurtValue;
    private final BoolValue stairsValue;
    private final BoolValue iceValue;
    private final FloatValue stairsBoostValue;
    private boolean forceDown;
    private boolean fastHop;
    private final BoolValue snowValue;
    private final ListValue wallModeValue;
    private final FloatValue slabsBoostValue;
    private final BoolValue speedLimitValue = new BoolValue("SpeedLimit", true);
    private final BoolValue bufferValue;
    private final FloatValue wallBoostValue;
    private boolean down;

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isSPacketPlayerPosLook(iPacket)) {
            this.speed = 0.0;
        }
    }

    public BufferSpeed() {
        this.maxSpeedValue = new FloatValue("MaxSpeed", 2.0f, 1.0f, 5.0f);
        this.bufferValue = new BoolValue("Buffer", true);
        this.stairsValue = new BoolValue("Stairs", true);
        this.stairsBoostValue = new FloatValue("StairsBoost", 1.87f, 1.0f, 2.0f);
        this.stairsModeValue = new ListValue("StairsMode", new String[]{"Old", "New"}, "New");
        this.slabsValue = new BoolValue("Slabs", true);
        this.slabsBoostValue = new FloatValue("SlabsBoost", 1.87f, 1.0f, 2.0f);
        this.slabsModeValue = new ListValue("SlabsMode", new String[]{"Old", "New"}, "New");
        this.iceValue = new BoolValue("Ice", false);
        this.iceBoostValue = new FloatValue("IceBoost", 1.342f, 1.0f, 2.0f);
        this.snowValue = new BoolValue("Snow", true);
        this.snowBoostValue = new FloatValue("SnowBoost", 1.87f, 1.0f, 2.0f);
        this.snowPortValue = new BoolValue("SnowPort", true);
        this.wallValue = new BoolValue("Wall", true);
        this.wallBoostValue = new FloatValue("WallBoost", 1.87f, 1.0f, 2.0f);
        this.wallModeValue = new ListValue("WallMode", new String[]{"Old", "New"}, "New");
        this.headBlockValue = new BoolValue("HeadBlock", true);
        this.headBlockBoostValue = new FloatValue("HeadBlockBoost", 1.87f, 1.0f, 2.0f);
        this.slimeValue = new BoolValue("Slime", true);
        this.airStrafeValue = new BoolValue("AirStrafe", false);
        this.noHurtValue = new BoolValue("NoHurt", true);
    }

    public static final double access$getSpeed$p(BufferSpeed bufferSpeed) {
        return bufferSpeed.speed;
    }

    public static final FloatValue access$getMaxSpeedValue$p(BufferSpeed bufferSpeed) {
        return bufferSpeed.maxSpeedValue;
    }

    @Override
    public void onDisable() {
        this.reset();
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent var1_1) {
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            return;
        }
        var2_2 = v0;
        v1 = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
        if (v1 == null) {
            Intrinsics.throwNpe();
        }
        if (v1.getState() || ((Boolean)this.noHurtValue.get()).booleanValue() && var2_2.getHurtTime() > 0) {
            this.reset();
            return;
        }
        var3_3 = new WBlockPos(var2_2.getPosX(), var2_2.getEntityBoundingBox().getMinY(), var2_2.getPosZ());
        if (this.forceDown || this.down && var2_2.getMotionY() == 0.0) {
            var2_2.setMotionY(-1.0);
            this.down = false;
            this.forceDown = false;
        }
        if (this.fastHop) {
            var2_2.setSpeedInAir(0.0211f);
            this.hadFastHop = true;
        } else if (this.hadFastHop) {
            var2_2.setSpeedInAir(0.02f);
            this.hadFastHop = false;
        }
        if (!MovementUtils.isMoving() || var2_2.isSneaking() || var2_2.isInWater() || MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
            this.reset();
            return;
        }
        if (var2_2.getOnGround()) {
            this.fastHop = false;
            if (((Boolean)this.slimeValue.get()).booleanValue() && (MinecraftInstance.classProvider.isBlockSlime(BlockUtils.getBlock(var3_3.down())) || MinecraftInstance.classProvider.isBlockSlime(BlockUtils.getBlock(var3_3)))) {
                var2_2.jump();
                var2_2.setMotionX(var2_2.getMotionY() * 1.132);
                var2_2.setMotionY(0.08);
                var2_2.setMotionZ(var2_2.getMotionY() * 1.132);
                this.down = true;
                return;
            }
            if (((Boolean)this.slabsValue.get()).booleanValue() && MinecraftInstance.classProvider.isBlockSlab(BlockUtils.getBlock(var3_3))) {
                var4_4 = (String)this.slabsModeValue.get();
                var5_6 = false;
                v2 = var4_4;
                if (v2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                var4_4 = v2.toLowerCase();
                switch (var4_4.hashCode()) {
                    case 108960: {
                        if (!var4_4.equals("new")) ** break;
                        break;
                    }
                    case 110119: {
                        if (!var4_4.equals("old")) ** break;
                        var5_7 = this;
                        var6_13 = ((Number)this.slabsBoostValue.get()).floatValue();
                        var7_19 = false;
                        v3 = MinecraftInstance.mc.getThePlayer();
                        if (v3 == null) {
                            Intrinsics.throwNpe();
                        }
                        var8_25 = v3;
                        var8_25.setMotionX(var8_25.getMotionX() * (double)var6_13);
                        var8_25.setMotionZ(var8_25.getMotionX() * (double)var6_13);
                        BufferSpeed.access$setSpeed$p(var5_7, MovementUtils.INSTANCE.getSpeed());
                        if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p(var5_7).get()).booleanValue() && BufferSpeed.access$getSpeed$p(var5_7) > ((Number)BufferSpeed.access$getMaxSpeedValue$p(var5_7).get()).doubleValue()) {
                            BufferSpeed.access$setSpeed$p(var5_7, ((Number)BufferSpeed.access$getMaxSpeedValue$p(var5_7).get()).floatValue());
                        }
                        return;
                    }
                }
                this.fastHop = true;
                if (this.legitHop) {
                    var2_2.jump();
                    var2_2.setOnGround(false);
                    this.legitHop = false;
                    return;
                }
                var2_2.setOnGround(false);
                MovementUtils.strafe(0.375f);
                var2_2.jump();
                var2_2.setMotionY(0.41);
                return;
            }
            if (((Boolean)this.stairsValue.get()).booleanValue() && (MinecraftInstance.classProvider.isBlockStairs(BlockUtils.getBlock(var3_3.down())) || MinecraftInstance.classProvider.isBlockStairs(BlockUtils.getBlock(var3_3)))) {
                var4_4 = (String)this.stairsModeValue.get();
                var5_6 = false;
                v4 = var4_4;
                if (v4 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                var4_4 = v4.toLowerCase();
                switch (var4_4.hashCode()) {
                    case 108960: {
                        if (!var4_4.equals("new")) ** break;
                        break;
                    }
                    case 110119: {
                        if (!var4_4.equals("old")) ** break;
                        var5_8 = this;
                        var6_14 = ((Number)this.stairsBoostValue.get()).floatValue();
                        var7_20 = false;
                        v5 = MinecraftInstance.mc.getThePlayer();
                        if (v5 == null) {
                            Intrinsics.throwNpe();
                        }
                        var8_26 = v5;
                        var8_26.setMotionX(var8_26.getMotionX() * (double)var6_14);
                        var8_26.setMotionZ(var8_26.getMotionX() * (double)var6_14);
                        BufferSpeed.access$setSpeed$p(var5_8, MovementUtils.INSTANCE.getSpeed());
                        if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p(var5_8).get()).booleanValue() && BufferSpeed.access$getSpeed$p(var5_8) > ((Number)BufferSpeed.access$getMaxSpeedValue$p(var5_8).get()).doubleValue()) {
                            BufferSpeed.access$setSpeed$p(var5_8, ((Number)BufferSpeed.access$getMaxSpeedValue$p(var5_8).get()).floatValue());
                        }
                        return;
                    }
                }
                this.fastHop = true;
                if (this.legitHop) {
                    var2_2.jump();
                    var2_2.setOnGround(false);
                    this.legitHop = false;
                    return;
                }
                var2_2.setOnGround(false);
                MovementUtils.strafe(0.375f);
                var2_2.jump();
                var2_2.setMotionY(0.41);
                return;
            }
            this.legitHop = true;
            if (((Boolean)this.headBlockValue.get()).booleanValue() && BlockUtils.getBlock(var3_3.up(2)).equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.AIR))) {
                var4_4 = this;
                var5_9 = ((Number)this.headBlockBoostValue.get()).floatValue();
                var6_15 = false;
                v6 = MinecraftInstance.mc.getThePlayer();
                if (v6 == null) {
                    Intrinsics.throwNpe();
                }
                var7_21 = v6;
                var7_21.setMotionX(var7_21.getMotionX() * (double)var5_9);
                var7_21.setMotionZ(var7_21.getMotionX() * (double)var5_9);
                BufferSpeed.access$setSpeed$p((BufferSpeed)var4_4, MovementUtils.INSTANCE.getSpeed());
                if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)var4_4).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)var4_4) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)var4_4).get()).doubleValue()) {
                    BufferSpeed.access$setSpeed$p((BufferSpeed)var4_4, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)var4_4).get()).floatValue());
                }
                return;
            }
            if (((Boolean)this.iceValue.get()).booleanValue() && (BlockUtils.getBlock(var3_3.down()).equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE)) || BlockUtils.getBlock(var3_3.down()).equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED)))) {
                var4_4 = this;
                var5_10 = ((Number)this.iceBoostValue.get()).floatValue();
                var6_16 = false;
                v7 = MinecraftInstance.mc.getThePlayer();
                if (v7 == null) {
                    Intrinsics.throwNpe();
                }
                var7_22 = v7;
                var7_22.setMotionX(var7_22.getMotionX() * (double)var5_10);
                var7_22.setMotionZ(var7_22.getMotionX() * (double)var5_10);
                BufferSpeed.access$setSpeed$p((BufferSpeed)var4_4, MovementUtils.INSTANCE.getSpeed());
                if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)var4_4).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)var4_4) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)var4_4).get()).doubleValue()) {
                    BufferSpeed.access$setSpeed$p((BufferSpeed)var4_4, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)var4_4).get()).floatValue());
                }
                return;
            }
            if (((Boolean)this.snowValue.get()).booleanValue() && BlockUtils.getBlock(var3_3).equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.SNOW_LAYER)) && (((Boolean)this.snowPortValue.get()).booleanValue() || var2_2.getPosY() - (double)((int)var2_2.getPosY()) >= 0.125)) {
                if (var2_2.getPosY() - (double)((int)var2_2.getPosY()) >= 0.125) {
                    var4_4 = this;
                    var5_11 = ((Number)this.snowBoostValue.get()).floatValue();
                    var6_17 = false;
                    v8 = MinecraftInstance.mc.getThePlayer();
                    if (v8 == null) {
                        Intrinsics.throwNpe();
                    }
                    var7_23 = v8;
                    var7_23.setMotionX(var7_23.getMotionX() * (double)var5_11);
                    var7_23.setMotionZ(var7_23.getMotionX() * (double)var5_11);
                    BufferSpeed.access$setSpeed$p((BufferSpeed)var4_4, MovementUtils.INSTANCE.getSpeed());
                    if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)var4_4).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)var4_4) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)var4_4).get()).doubleValue()) {
                        BufferSpeed.access$setSpeed$p((BufferSpeed)var4_4, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)var4_4).get()).floatValue());
                    }
                } else {
                    var2_2.jump();
                    this.forceDown = true;
                }
                return;
            }
            if (((Boolean)this.wallValue.get()).booleanValue()) {
                var4_4 = (String)this.wallModeValue.get();
                var5_6 = false;
                v9 = var4_4;
                if (v9 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                var4_4 = v9.toLowerCase();
                switch (var4_4.hashCode()) {
                    case 108960: {
                        if (!var4_4.equals("new")) ** break;
                        break;
                    }
                    case 110119: {
                        if (!var4_4.equals("old") || (!var2_2.isCollidedVertically() || this == null) && MinecraftInstance.classProvider.isBlockAir(BlockUtils.getBlock(new WBlockPos(var2_2.getPosX(), var2_2.getPosY() + 2.0, var2_2.getPosZ())))) ** break;
                        var5_12 = this;
                        var6_18 = ((Number)this.wallBoostValue.get()).floatValue();
                        var7_24 = false;
                        v10 = MinecraftInstance.mc.getThePlayer();
                        if (v10 == null) {
                            Intrinsics.throwNpe();
                        }
                        var8_27 = v10;
                        var8_27.setMotionX(var8_27.getMotionX() * (double)var6_18);
                        var8_27.setMotionZ(var8_27.getMotionX() * (double)var6_18);
                        BufferSpeed.access$setSpeed$p(var5_12, MovementUtils.INSTANCE.getSpeed());
                        if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p(var5_12).get()).booleanValue() && BufferSpeed.access$getSpeed$p(var5_12) > ((Number)BufferSpeed.access$getMaxSpeedValue$p(var5_12).get()).doubleValue()) {
                            BufferSpeed.access$setSpeed$p(var5_12, ((Number)BufferSpeed.access$getMaxSpeedValue$p(var5_12).get()).floatValue());
                        }
                        return;
                    }
                }
                if (this == null && !var2_2.getMovementInput().getJump()) {
                    var2_2.jump();
                    var2_2.setMotionY(0.08);
                    var2_2.setMotionX(var2_2.getMotionX() * 0.99);
                    var2_2.setMotionZ(var2_2.getMotionX() * 0.99);
                    this.down = true;
                    return;
                }
            }
            if (this.speed < (double)(var4_5 = MovementUtils.INSTANCE.getSpeed())) {
                this.speed = var4_5;
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

    @Override
    public void onEnable() {
        this.reset();
    }

    public static final BoolValue access$getSpeedLimitValue$p(BufferSpeed bufferSpeed) {
        return bufferSpeed.speedLimitValue;
    }

    private final void boost(float f) {
        boolean bl = false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() * (double)f);
        iEntityPlayerSP2.setMotionZ(iEntityPlayerSP2.getMotionX() * (double)f);
        BufferSpeed.access$setSpeed$p(this, MovementUtils.INSTANCE.getSpeed());
        if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p(this).get()).booleanValue() && BufferSpeed.access$getSpeed$p(this) > ((Number)BufferSpeed.access$getMaxSpeedValue$p(this).get()).doubleValue()) {
            BufferSpeed.access$setSpeed$p(this, ((Number)BufferSpeed.access$getMaxSpeedValue$p(this).get()).floatValue());
        }
    }

    private final void reset() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        this.legitHop = true;
        this.speed = 0.0;
        if (this.hadFastHop) {
            iEntityPlayerSP2.setSpeedInAir(0.02f);
            this.hadFastHop = false;
        }
    }

    public static final void access$setSpeed$p(BufferSpeed bufferSpeed, double d) {
        bufferSpeed.speed = d;
    }
}

