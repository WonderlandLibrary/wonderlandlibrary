package net.ccbluex.liquidbounce.api.minecraft.client.entity;

import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import net.ccbluex.liquidbounce.api.minecraft.block.material.IMaterial;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityTNTPrimed;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\b\f\n\n\b\n\b\n\b\n\n\n\b\n\n\b\n\n\b\r\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\b\n\n\b\n\n\b\b\bf\u000020J\n0H&J\n0H&J\n0H&J\t0H&J\t0H&J02\b0H&J$0=20=20=20=H&J0=20dH&J0=20\u0000H&J020\u0000H&J02\n0H&J0E20H&J 0E20H&J¡02\b¢0£H&J%¤020=20=20=H&J¥0¦2§0=20H&J%¨020=20=20=H&J5©02ª0=2«0=2¬0=2u02r0H&J\"­02\\\u001a0=2^0=2a0=H&R08gX¦¢\bR0X¦¢\b\b\tR\n0X¦¢\b\f\rR0X¦¢\f\b\t\"\bR0X¦¢\f\b\t\"\bR08gX¦¢\bR0X¦¢\f\b\"\bR0X¦¢\b R!0X¦¢\b\"\tR#0X¦¢\f\b$\t\"\b%R&0X¦¢\b'\tR(0)X¦¢\b*+R,0X¦¢\b- R.08gX¦¢\b/R00X¦¢\f\b0\"\b12R30X¦¢\b3R40X¦¢\b4R50X¦¢\f\b5\"\b62R70X¦¢\b7R80X¦¢\b8R90X¦¢\f\b9\"\b:2R;08gX¦¢\b;R<0=X¦¢\b>?R@0=X¦¢\bA?RB0=X¦¢\bC?RD0EX¦¢\bFGRH0=X¦¢\f\bI?\"\bJKRL0=X¦¢\f\bM?\"\bNKRO0=X¦¢\f\bP?\"\bQKRR0SX¦¢\bTURV0X¦¢\f\bW\"\bX2RY0X¦¢\f\bZ\"\b[2R\\\u001a0=X¦¢\b]?R^0=X¦¢\f\b_?\"\b`KRa0=X¦¢\bb?Rc0dX¦¢\befRg0EX¦¢\bhGRi0=X¦¢\bj?Rk0=X¦¢\bl?Rm0=X¦¢\bn?Ro0\u0000X¦¢\bpqRr0X¦¢\f\bs\t\"\btRu0X¦¢\f\bv\t\"\bwRx08gX¦¢\byRz0X¦¢\f\b{\"\b|2R}0X¦¢\f\b~\t\"\bR0X¦¢\b R0X¦¢\b\bR0X¦¢\b\t¨®"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "", "burning", "", "isBurning", "()Z", "collisionBorderSize", "", "getCollisionBorderSize", "()F", "displayName", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "getDisplayName", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "distanceWalkedModified", "getDistanceWalkedModified", "setDistanceWalkedModified", "(F)V", "distanceWalkedOnStepModified", "getDistanceWalkedOnStepModified", "setDistanceWalkedOnStepModified", "entityAlive", "isEntityAlive", "entityBoundingBox", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "getEntityBoundingBox", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "setEntityBoundingBox", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;)V", "entityId", "", "getEntityId", "()I", "eyeHeight", "getEyeHeight", "fallDistance", "getFallDistance", "setFallDistance", "height", "getHeight", "horizontalFacing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "getHorizontalFacing", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "hurtResistantTime", "getHurtResistantTime", "invisible", "isInvisible", "isAirBorne", "setAirBorne", "(Z)V", "isCollidedHorizontally", "isCollidedVertically", "isDead", "setDead", "isInLava", "isInWater", "isInWeb", "setInWeb", "isRiding", "lastTickPosX", "", "getLastTickPosX", "()D", "lastTickPosY", "getLastTickPosY", "lastTickPosZ", "getLastTickPosZ", "lookVec", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "getLookVec", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "motionX", "getMotionX", "setMotionX", "(D)V", "motionY", "getMotionY", "setMotionY", "motionZ", "getMotionZ", "setMotionZ", "name", "", "getName", "()Ljava/lang/String;", "noClip", "getNoClip", "setNoClip", "onGround", "getOnGround", "setOnGround", "posX", "getPosX", "posY", "getPosY", "setPosY", "posZ", "getPosZ", "position", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getPosition", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "positionVector", "getPositionVector", "prevPosX", "getPrevPosX", "prevPosY", "getPrevPosY", "prevPosZ", "getPrevPosZ", "ridingEntity", "getRidingEntity", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "rotationPitch", "getRotationPitch", "setRotationPitch", "rotationYaw", "getRotationYaw", "setRotationYaw", "sneaking", "isSneaking", "sprinting", "getSprinting", "setSprinting", "stepHeight", "getStepHeight", "setStepHeight", "ticksExisted", "getTicksExisted", "uniqueID", "Ljava/util/UUID;", "getUniqueID", "()Ljava/util/UUID;", "width", "getWidth", "asEntityLivingBase", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "asEntityPlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityPlayer;", "asEntityTNTPrimed", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityTNTPrimed;", "canBeCollidedWith", "canRiderInteract", "copyLocationAndAnglesFrom", "", "player", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "getDistance", "x", "y", "z", "getDistanceSq", "blockPos", "getDistanceSqToEntity", "it", "getDistanceToEntity", "getDistanceToEntity2", "getLook", "partialTicks", "getPositionEyes", "isInsideOfMaterial", "material", "Lnet/ccbluex/liquidbounce/api/minecraft/block/material/IMaterial;", "moveEntity", "rayTrace", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "range", "setPosition", "setPositionAndRotation", "oldX", "oldY", "oldZ", "setPositionAndUpdate", "Pride"})
public interface IEntity {
    public float getDistanceWalkedOnStepModified();

    public void setDistanceWalkedOnStepModified(float var1);

    public float getDistanceWalkedModified();

    public void setDistanceWalkedModified(float var1);

    @JvmName(name="isSneaking")
    public boolean isSneaking();

    public float getStepHeight();

    public void setStepHeight(float var1);

    @NotNull
    public IEnumFacing getHorizontalFacing();

    @Nullable
    public WVec3 getLookVec();

    public boolean isDead();

    public void setDead(boolean var1);

    public boolean isCollidedVertically();

    public boolean isCollidedHorizontally();

    public boolean isAirBorne();

    public void setAirBorne(boolean var1);

    public int getHurtResistantTime();

    public boolean getNoClip();

    public void setNoClip(boolean var1);

    public boolean getSprinting();

    public void setSprinting(boolean var1);

    @NotNull
    public WVec3 getPositionVector();

    @JvmName(name="isRiding")
    public boolean isRiding();

    @NotNull
    public WBlockPos getPosition();

    @JvmName(name="isBurning")
    public boolean isBurning();

    public float getFallDistance();

    public void setFallDistance(float var1);

    public boolean isInWater();

    public boolean isInWeb();

    public void setInWeb(boolean var1);

    public boolean isInLava();

    public float getWidth();

    public float getHeight();

    public boolean getOnGround();

    public void setOnGround(boolean var1);

    @Nullable
    public IEntity getRidingEntity();

    public float getCollisionBorderSize();

    public double getMotionX();

    public void setMotionX(double var1);

    public double getMotionY();

    public void setMotionY(double var1);

    public double getMotionZ();

    public void setMotionZ(double var1);

    public float getEyeHeight();

    @NotNull
    public IAxisAlignedBB getEntityBoundingBox();

    public void setEntityBoundingBox(@NotNull IAxisAlignedBB var1);

    public double getPosX();

    public double getPosY();

    public void setPosY(double var1);

    public double getPosZ();

    public double getLastTickPosX();

    public double getLastTickPosY();

    public double getLastTickPosZ();

    public double getPrevPosX();

    public double getPrevPosY();

    public double getPrevPosZ();

    public float getRotationYaw();

    public void setRotationYaw(float var1);

    public float getRotationPitch();

    public void setRotationPitch(float var1);

    public int getEntityId();

    @Nullable
    public IIChatComponent getDisplayName();

    @NotNull
    public UUID getUniqueID();

    @Nullable
    public String getName();

    public int getTicksExisted();

    @JvmName(name="isEntityAlive")
    public boolean isEntityAlive();

    @JvmName(name="isInvisible")
    public boolean isInvisible();

    @NotNull
    public WVec3 getPositionEyes(float var1);

    public boolean canBeCollidedWith();

    public boolean canRiderInteract();

    public void moveEntity(double var1, double var3, double var5);

    public float getDistanceToEntity(@NotNull IEntity var1);

    public float getDistanceToEntity2(@Nullable IEntityLivingBase var1);

    public double getDistanceSqToEntity(@NotNull IEntity var1);

    @NotNull
    public IEntityPlayer asEntityPlayer();

    @NotNull
    public IEntityLivingBase asEntityLivingBase();

    @NotNull
    public IEntityTNTPrimed asEntityTNTPrimed();

    public double getDistance(double var1, double var3, double var5);

    public void setPosition(double var1, double var3, double var5);

    public double getDistanceSq(@NotNull WBlockPos var1);

    public void setPositionAndUpdate(double var1, double var3, double var5);

    @Nullable
    public IMovingObjectPosition rayTrace(double var1, float var3);

    @NotNull
    public WVec3 getLook(float var1);

    public boolean isInsideOfMaterial(@NotNull IMaterial var1);

    public void copyLocationAndAnglesFrom(@NotNull IEntityPlayerSP var1);

    public void setPositionAndRotation(double var1, double var3, double var5, float var7, float var8);
}
