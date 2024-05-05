/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.ArrayList;
import java.util.List;
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
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="BufferSpeed", description="Allows you to walk faster on slabs and stairs.", category=ModuleCategory.MOVEMENT)
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
        if (thePlayer.getOnGround()) {
            this.fastHop = false;
            if (((Boolean)this.slimeValue.get()).booleanValue() && (MinecraftInstance.classProvider.isBlockSlime(BlockUtils.getBlock(blockPos.down())) || MinecraftInstance.classProvider.isBlockSlime(BlockUtils.getBlock(blockPos)))) {
                thePlayer.jump();
                thePlayer.setMotionX(thePlayer.getMotionY() * 1.132);
                thePlayer.setMotionY(0.08);
                thePlayer.setMotionZ(thePlayer.getMotionY() * 1.132);
                this.down = true;
                return;
            }
            if (((Boolean)this.slabsValue.get()).booleanValue() && MinecraftInstance.classProvider.isBlockSlab(BlockUtils.getBlock(blockPos))) {
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
                        boost$iv = ((Number)this.slabsBoostValue.get()).floatValue();
                        $i$f$boost = false;
                        v3 = MinecraftInstance.mc.getThePlayer();
                        if (v3 == null) {
                            Intrinsics.throwNpe();
                        }
                        thePlayer$iv = v3;
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
            if (((Boolean)this.stairsValue.get()).booleanValue() && (MinecraftInstance.classProvider.isBlockStairs(BlockUtils.getBlock(blockPos.down())) || MinecraftInstance.classProvider.isBlockStairs(BlockUtils.getBlock(blockPos)))) {
                var4_4 = (String)this.stairsModeValue.get();
                this_$iv = false;
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
                        this_$iv = this;
                        boost$iv = ((Number)this.stairsBoostValue.get()).floatValue();
                        $i$f$boost = false;
                        v5 = MinecraftInstance.mc.getThePlayer();
                        if (v5 == null) {
                            Intrinsics.throwNpe();
                        }
                        thePlayer$iv = v5;
                        thePlayer$iv.setMotionX(thePlayer$iv.getMotionX() * (double)boost$iv);
                        thePlayer$iv.setMotionZ(thePlayer$iv.getMotionX() * (double)boost$iv);
                        BufferSpeed.access$setSpeed$p(this_$iv, MovementUtils.INSTANCE.getSpeed());
                        if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p(this_$iv).get()).booleanValue() && BufferSpeed.access$getSpeed$p(this_$iv) > ((Number)BufferSpeed.access$getMaxSpeedValue$p(this_$iv).get()).doubleValue()) {
                            BufferSpeed.access$setSpeed$p(this_$iv, ((Number)BufferSpeed.access$getMaxSpeedValue$p(this_$iv).get()).floatValue());
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
            if (((Boolean)this.headBlockValue.get()).booleanValue() && BlockUtils.getBlock(blockPos.up(2)).equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.AIR))) {
                var4_4 = this;
                boost$iv = ((Number)this.headBlockBoostValue.get()).floatValue();
                $i$f$boost = false;
                v6 = MinecraftInstance.mc.getThePlayer();
                if (v6 == null) {
                    Intrinsics.throwNpe();
                }
                thePlayer$iv = v6;
                thePlayer$iv.setMotionX(thePlayer$iv.getMotionX() * (double)boost$iv);
                thePlayer$iv.setMotionZ(thePlayer$iv.getMotionX() * (double)boost$iv);
                BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, MovementUtils.INSTANCE.getSpeed());
                if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)this_$iv).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)this_$iv) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).doubleValue()) {
                    BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).floatValue());
                }
                return;
            }
            if (((Boolean)this.iceValue.get()).booleanValue() && (BlockUtils.getBlock(blockPos.down()).equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE)) || BlockUtils.getBlock(blockPos.down()).equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED)))) {
                this_$iv = this;
                boost$iv = ((Number)this.iceBoostValue.get()).floatValue();
                $i$f$boost = false;
                v7 = MinecraftInstance.mc.getThePlayer();
                if (v7 == null) {
                    Intrinsics.throwNpe();
                }
                thePlayer$iv = v7;
                thePlayer$iv.setMotionX(thePlayer$iv.getMotionX() * (double)boost$iv);
                thePlayer$iv.setMotionZ(thePlayer$iv.getMotionX() * (double)boost$iv);
                BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, MovementUtils.INSTANCE.getSpeed());
                if (((Boolean)BufferSpeed.access$getSpeedLimitValue$p((BufferSpeed)this_$iv).get()).booleanValue() && BufferSpeed.access$getSpeed$p((BufferSpeed)this_$iv) > ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).doubleValue()) {
                    BufferSpeed.access$setSpeed$p((BufferSpeed)this_$iv, ((Number)BufferSpeed.access$getMaxSpeedValue$p((BufferSpeed)this_$iv).get()).floatValue());
                }
                return;
            }
            if (((Boolean)this.snowValue.get()).booleanValue() && BlockUtils.getBlock(blockPos).equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.SNOW_LAYER)) && (((Boolean)this.snowPortValue.get()).booleanValue() || thePlayer.getPosY() - (double)((int)thePlayer.getPosY()) >= 0.125)) {
                if (thePlayer.getPosY() - (double)((int)thePlayer.getPosY()) >= 0.125) {
                    this_$iv = this;
                    boost$iv = ((Number)this.snowBoostValue.get()).floatValue();
                    $i$f$boost = false;
                    v8 = MinecraftInstance.mc.getThePlayer();
                    if (v8 == null) {
                        Intrinsics.throwNpe();
                    }
                    thePlayer$iv = v8;
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
            if (((Boolean)this.wallValue.get()).booleanValue()) {
                this_$iv = (String)this.wallModeValue.get();
                boost$iv = false;
                v9 = this_$iv;
                if (v9 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                this_$iv = v9.toLowerCase();
                switch (this_$iv.hashCode()) {
                    case 108960: {
                        if (!this_$iv.equals("new")) ** break;
                        break;
                    }
                    case 110119: {
                        if (!this_$iv.equals("old") || (!thePlayer.isCollidedVertically() || !this.isNearBlock()) && MinecraftInstance.classProvider.isBlockAir(BlockUtils.getBlock(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + 2.0, thePlayer.getPosZ())))) ** break;
                        boost$iv = this;
                        boost$iv = ((Number)this.wallBoostValue.get()).floatValue();
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
        } else {
            this.speed = 0.0;
            if (((Boolean)this.airStrafeValue.get()).booleanValue()) {
                MovementUtils.strafe$default(0.0f, 1, null);
            }
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
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
            if (((collisionBoundingBox = (blockState = iWorldClient.getBlockState(blockPos)).getBlock().getCollisionBoundingBox(theWorld, blockPos, blockState)) != null && collisionBoundingBox.getMaxX() != collisionBoundingBox.getMinY() + 1.0 || blockState.getBlock().isTranslucent(blockState) || !blockState.getBlock().equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.WATER)) || MinecraftInstance.classProvider.isBlockSlab(blockState.getBlock())) && !blockState.getBlock().equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.BARRIER))) continue;
            return true;
        }
        return false;
    }
}

