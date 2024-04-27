/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.JvmName
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
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

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0094\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u0006\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000e\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u001e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\bf\u0018\u00002\u00020\u0001J\n\u0010\u0088\u0001\u001a\u00030\u0089\u0001H&J\n\u0010\u008a\u0001\u001a\u00030\u008b\u0001H&J\n\u0010\u008c\u0001\u001a\u00030\u008d\u0001H&J\t\u0010\u008e\u0001\u001a\u00020\u0003H&J\t\u0010\u008f\u0001\u001a\u00020\u0003H&J\u0014\u0010\u0090\u0001\u001a\u00030\u0091\u00012\b\u0010\u0092\u0001\u001a\u00030\u0093\u0001H&J$\u0010\u0094\u0001\u001a\u00020=2\u0007\u0010\u0095\u0001\u001a\u00020=2\u0007\u0010\u0096\u0001\u001a\u00020=2\u0007\u0010\u0097\u0001\u001a\u00020=H&J\u0012\u0010\u0098\u0001\u001a\u00020=2\u0007\u0010\u0099\u0001\u001a\u00020dH&J\u0012\u0010\u009a\u0001\u001a\u00020=2\u0007\u0010\u009b\u0001\u001a\u00020\u0000H&J\u0012\u0010\u009c\u0001\u001a\u00020\u00072\u0007\u0010\u009b\u0001\u001a\u00020\u0000H&J\u0012\u0010\u009d\u0001\u001a\u00020E2\u0007\u0010\u009e\u0001\u001a\u00020\u0007H&J\u0012\u0010\u009f\u0001\u001a\u00020E2\u0007\u0010\u009e\u0001\u001a\u00020\u0007H&J\u0013\u0010\u00a0\u0001\u001a\u00020\u00032\b\u0010\u00a1\u0001\u001a\u00030\u00a2\u0001H&J%\u0010\u00a3\u0001\u001a\u00030\u0091\u00012\u0007\u0010\u0095\u0001\u001a\u00020=2\u0007\u0010\u0096\u0001\u001a\u00020=2\u0007\u0010\u0097\u0001\u001a\u00020=H&J\u001e\u0010\u00a4\u0001\u001a\u0005\u0018\u00010\u00a5\u00012\u0007\u0010\u00a6\u0001\u001a\u00020=2\u0007\u0010\u009e\u0001\u001a\u00020\u0007H&J%\u0010\u00a7\u0001\u001a\u00030\u0091\u00012\u0007\u0010\u0095\u0001\u001a\u00020=2\u0007\u0010\u0096\u0001\u001a\u00020=2\u0007\u0010\u0097\u0001\u001a\u00020=H&J5\u0010\u00a8\u0001\u001a\u00030\u0091\u00012\u0007\u0010\u00a9\u0001\u001a\u00020=2\u0007\u0010\u00aa\u0001\u001a\u00020=2\u0007\u0010\u00ab\u0001\u001a\u00020=2\u0006\u0010u\u001a\u00020\u00072\u0006\u0010r\u001a\u00020\u0007H&J\"\u0010\u00ac\u0001\u001a\u00030\u0091\u00012\u0006\u0010\\\u001a\u00020=2\u0006\u0010^\u001a\u00020=2\u0006\u0010a\u001a\u00020=H&R\u0014\u0010\u0002\u001a\u00020\u00038gX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u0004\u0018\u00010\u000bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0018\u0010\u000e\u001a\u00020\u0007X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u000f\u0010\t\"\u0004\b\u0010\u0010\u0011R\u0018\u0010\u0012\u001a\u00020\u0007X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u0013\u0010\t\"\u0004\b\u0014\u0010\u0011R\u0014\u0010\u0015\u001a\u00020\u00038gX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0005R\u0018\u0010\u0017\u001a\u00020\u0018X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u0012\u0010\u001d\u001a\u00020\u001eX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001f\u0010 R\u0012\u0010!\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\"\u0010\tR\u0018\u0010#\u001a\u00020\u0007X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b$\u0010\t\"\u0004\b%\u0010\u0011R\u0012\u0010&\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b'\u0010\tR\u0012\u0010(\u001a\u00020)X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b*\u0010+R\u0012\u0010,\u001a\u00020\u001eX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b-\u0010 R\u0014\u0010.\u001a\u00020\u00038gX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b/\u0010\u0005R\u0018\u00100\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b0\u0010\u0005\"\u0004\b1\u00102R\u0012\u00103\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b3\u0010\u0005R\u0012\u00104\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b4\u0010\u0005R\u0018\u00105\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b5\u0010\u0005\"\u0004\b6\u00102R\u0012\u00107\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b7\u0010\u0005R\u0012\u00108\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b8\u0010\u0005R\u0018\u00109\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b9\u0010\u0005\"\u0004\b:\u00102R\u0014\u0010;\u001a\u00020\u00038gX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b;\u0010\u0005R\u0012\u0010<\u001a\u00020=X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b>\u0010?R\u0012\u0010@\u001a\u00020=X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bA\u0010?R\u0012\u0010B\u001a\u00020=X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bC\u0010?R\u0014\u0010D\u001a\u0004\u0018\u00010EX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bF\u0010GR\u0018\u0010H\u001a\u00020=X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\bI\u0010?\"\u0004\bJ\u0010KR\u0018\u0010L\u001a\u00020=X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\bM\u0010?\"\u0004\bN\u0010KR\u0018\u0010O\u001a\u00020=X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\bP\u0010?\"\u0004\bQ\u0010KR\u0014\u0010R\u001a\u0004\u0018\u00010SX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bT\u0010UR\u0018\u0010V\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\bW\u0010\u0005\"\u0004\bX\u00102R\u0018\u0010Y\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\bZ\u0010\u0005\"\u0004\b[\u00102R\u0012\u0010\\\u001a\u00020=X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b]\u0010?R\u0018\u0010^\u001a\u00020=X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b_\u0010?\"\u0004\b`\u0010KR\u0012\u0010a\u001a\u00020=X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bb\u0010?R\u0012\u0010c\u001a\u00020dX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\be\u0010fR\u0012\u0010g\u001a\u00020EX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bh\u0010GR\u0012\u0010i\u001a\u00020=X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bj\u0010?R\u0012\u0010k\u001a\u00020=X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bl\u0010?R\u0012\u0010m\u001a\u00020=X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bn\u0010?R\u0014\u0010o\u001a\u0004\u0018\u00010\u0000X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bp\u0010qR\u0018\u0010r\u001a\u00020\u0007X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\bs\u0010\t\"\u0004\bt\u0010\u0011R\u0018\u0010u\u001a\u00020\u0007X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\bv\u0010\t\"\u0004\bw\u0010\u0011R\u0014\u0010x\u001a\u00020\u00038gX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\by\u0010\u0005R\u0018\u0010z\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b{\u0010\u0005\"\u0004\b|\u00102R\u0018\u0010}\u001a\u00020\u0007X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b~\u0010\t\"\u0004\b\u007f\u0010\u0011R\u0014\u0010\u0080\u0001\u001a\u00020\u001eX\u00a6\u0004\u00a2\u0006\u0007\u001a\u0005\b\u0081\u0001\u0010 R\u0016\u0010\u0082\u0001\u001a\u00030\u0083\u0001X\u00a6\u0004\u00a2\u0006\b\u001a\u0006\b\u0084\u0001\u0010\u0085\u0001R\u0014\u0010\u0086\u0001\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0007\u001a\u0005\b\u0087\u0001\u0010\t\u00a8\u0006\u00ad\u0001"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "", "burning", "", "isBurning", "()Z", "collisionBorderSize", "", "getCollisionBorderSize", "()F", "displayName", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "getDisplayName", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "distanceWalkedModified", "getDistanceWalkedModified", "setDistanceWalkedModified", "(F)V", "distanceWalkedOnStepModified", "getDistanceWalkedOnStepModified", "setDistanceWalkedOnStepModified", "entityAlive", "isEntityAlive", "entityBoundingBox", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "getEntityBoundingBox", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "setEntityBoundingBox", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;)V", "entityId", "", "getEntityId", "()I", "eyeHeight", "getEyeHeight", "fallDistance", "getFallDistance", "setFallDistance", "height", "getHeight", "horizontalFacing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "getHorizontalFacing", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "hurtResistantTime", "getHurtResistantTime", "invisible", "isInvisible", "isAirBorne", "setAirBorne", "(Z)V", "isCollidedHorizontally", "isCollidedVertically", "isDead", "setDead", "isInLava", "isInWater", "isInWeb", "setInWeb", "isRiding", "lastTickPosX", "", "getLastTickPosX", "()D", "lastTickPosY", "getLastTickPosY", "lastTickPosZ", "getLastTickPosZ", "lookVec", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "getLookVec", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "motionX", "getMotionX", "setMotionX", "(D)V", "motionY", "getMotionY", "setMotionY", "motionZ", "getMotionZ", "setMotionZ", "name", "", "getName", "()Ljava/lang/String;", "noClip", "getNoClip", "setNoClip", "onGround", "getOnGround", "setOnGround", "posX", "getPosX", "posY", "getPosY", "setPosY", "posZ", "getPosZ", "position", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getPosition", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "positionVector", "getPositionVector", "prevPosX", "getPrevPosX", "prevPosY", "getPrevPosY", "prevPosZ", "getPrevPosZ", "ridingEntity", "getRidingEntity", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "rotationPitch", "getRotationPitch", "setRotationPitch", "rotationYaw", "getRotationYaw", "setRotationYaw", "sneaking", "isSneaking", "sprinting", "getSprinting", "setSprinting", "stepHeight", "getStepHeight", "setStepHeight", "ticksExisted", "getTicksExisted", "uniqueID", "Ljava/util/UUID;", "getUniqueID", "()Ljava/util/UUID;", "width", "getWidth", "asEntityLivingBase", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "asEntityPlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityPlayer;", "asEntityTNTPrimed", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityTNTPrimed;", "canBeCollidedWith", "canRiderInteract", "copyLocationAndAnglesFrom", "", "player", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "getDistance", "x", "y", "z", "getDistanceSq", "blockPos", "getDistanceSqToEntity", "it", "getDistanceToEntity", "getLook", "partialTicks", "getPositionEyes", "isInsideOfMaterial", "material", "Lnet/ccbluex/liquidbounce/api/minecraft/block/material/IMaterial;", "moveEntity", "rayTrace", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "range", "setPosition", "setPositionAndRotation", "oldX", "oldY", "oldZ", "setPositionAndUpdate", "LiKingSense"})
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

