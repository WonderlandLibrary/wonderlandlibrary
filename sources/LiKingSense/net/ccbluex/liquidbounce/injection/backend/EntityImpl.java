/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.MoverType
 *  net.minecraft.entity.item.EntityTNTPrimed
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.text.ITextComponent
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.UUID;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.block.material.IMaterial;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
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
import net.ccbluex.liquidbounce.injection.backend.AxisAlignedBBImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityLivingBaseImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityPlayerImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityPlayerSPImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityTNTPrimedImpl;
import net.ccbluex.liquidbounce.injection.backend.EnumFacingImpl;
import net.ccbluex.liquidbounce.injection.backend.IChatComponentImpl;
import net.ccbluex.liquidbounce.injection.backend.MaterialImpl;
import net.ccbluex.liquidbounce.injection.backend.MovingObjectPositionImpl;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u00a4\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u0006\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000e\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u001e\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u0016\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0005J\n\u0010\u0090\u0001\u001a\u00030\u0091\u0001H\u0016J\n\u0010\u0092\u0001\u001a\u00030\u0093\u0001H\u0016J\n\u0010\u0094\u0001\u001a\u00030\u0095\u0001H\u0016J\t\u0010\u0096\u0001\u001a\u00020\u0007H\u0016J\t\u0010\u0097\u0001\u001a\u00020\u0007H\u0016J\u0014\u0010\u0098\u0001\u001a\u00030\u0099\u00012\b\u0010\u009a\u0001\u001a\u00030\u009b\u0001H\u0016J\u0016\u0010\u009c\u0001\u001a\u00020\u00072\n\u0010\u009d\u0001\u001a\u0005\u0018\u00010\u009e\u0001H\u0096\u0002J$\u0010\u009f\u0001\u001a\u00020B2\u0007\u0010\u00a0\u0001\u001a\u00020B2\u0007\u0010\u00a1\u0001\u001a\u00020B2\u0007\u0010\u00a2\u0001\u001a\u00020BH\u0016J\u0012\u0010\u00a3\u0001\u001a\u00020B2\u0007\u0010\u00a4\u0001\u001a\u00020iH\u0016J\u0012\u0010\u00a5\u0001\u001a\u00020B2\u0007\u0010\u00a6\u0001\u001a\u00020\u0003H\u0016J\u0012\u0010\u00a7\u0001\u001a\u00020\u000b2\u0007\u0010\u00a6\u0001\u001a\u00020\u0003H\u0016J\u0012\u0010\u00a8\u0001\u001a\u00020J2\u0007\u0010\u00a9\u0001\u001a\u00020\u000bH\u0016J\u0012\u0010\u00aa\u0001\u001a\u00020J2\u0007\u0010\u00a9\u0001\u001a\u00020\u000bH\u0016J\u0013\u0010\u00ab\u0001\u001a\u00020\u00072\b\u0010\u00ac\u0001\u001a\u00030\u00ad\u0001H\u0016J%\u0010\u00ae\u0001\u001a\u00030\u0099\u00012\u0007\u0010\u00a0\u0001\u001a\u00020B2\u0007\u0010\u00a1\u0001\u001a\u00020B2\u0007\u0010\u00a2\u0001\u001a\u00020BH\u0016J\u001e\u0010\u00af\u0001\u001a\u0005\u0018\u00010\u00b0\u00012\u0007\u0010\u00b1\u0001\u001a\u00020B2\u0007\u0010\u00a9\u0001\u001a\u00020\u000bH\u0016J%\u0010\u00b2\u0001\u001a\u00030\u0099\u00012\u0007\u0010\u00a0\u0001\u001a\u00020B2\u0007\u0010\u00a1\u0001\u001a\u00020B2\u0007\u0010\u00a2\u0001\u001a\u00020BH\u0016J5\u0010\u00b3\u0001\u001a\u00030\u0099\u00012\u0007\u0010\u00b4\u0001\u001a\u00020B2\u0007\u0010\u00b5\u0001\u001a\u00020B2\u0007\u0010\u00b6\u0001\u001a\u00020B2\u0006\u0010z\u001a\u00020\u000b2\u0006\u0010w\u001a\u00020\u000bH\u0016J\"\u0010\u00b7\u0001\u001a\u00030\u0099\u00012\u0006\u0010a\u001a\u00020B2\u0006\u0010c\u001a\u00020B2\u0006\u0010f\u001a\u00020BH\u0016R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u000b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0016\u0010\u000e\u001a\u0004\u0018\u00010\u000f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R$\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u000b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0014\u0010\r\"\u0004\b\u0015\u0010\u0016R$\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u000b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0018\u0010\r\"\u0004\b\u0019\u0010\u0016R\u0014\u0010\u001a\u001a\u00020\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\tR$\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u0012\u001a\u00020\u001c8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u0014\u0010\"\u001a\u00020#8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b$\u0010%R\u0014\u0010&\u001a\u00020\u000b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b'\u0010\rR$\u0010(\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u000b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b)\u0010\r\"\u0004\b*\u0010\u0016R\u0014\u0010+\u001a\u00020\u000b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b,\u0010\rR\u0014\u0010-\u001a\u00020.8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b/\u00100R\u0014\u00101\u001a\u00020#8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b2\u0010%R\u0014\u00103\u001a\u00020\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b4\u0010\tR$\u00105\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u00078V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b5\u0010\t\"\u0004\b6\u00107R\u0014\u00108\u001a\u00020\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b8\u0010\tR\u0014\u00109\u001a\u00020\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b9\u0010\tR$\u0010:\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u00078V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b:\u0010\t\"\u0004\b;\u00107R\u0014\u0010<\u001a\u00020\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b<\u0010\tR\u0014\u0010=\u001a\u00020\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b=\u0010\tR$\u0010>\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u00078V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b>\u0010\t\"\u0004\b?\u00107R\u0014\u0010@\u001a\u00020\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b@\u0010\tR\u0014\u0010A\u001a\u00020B8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bC\u0010DR\u0014\u0010E\u001a\u00020B8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bF\u0010DR\u0014\u0010G\u001a\u00020B8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bH\u0010DR\u0016\u0010I\u001a\u0004\u0018\u00010J8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bK\u0010LR$\u0010M\u001a\u00020B2\u0006\u0010\u0012\u001a\u00020B8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\bN\u0010D\"\u0004\bO\u0010PR$\u0010Q\u001a\u00020B2\u0006\u0010\u0012\u001a\u00020B8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\bR\u0010D\"\u0004\bS\u0010PR$\u0010T\u001a\u00020B2\u0006\u0010\u0012\u001a\u00020B8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\bU\u0010D\"\u0004\bV\u0010PR\u0016\u0010W\u001a\u0004\u0018\u00010X8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bY\u0010ZR$\u0010[\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u00078V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\\\u0010\t\"\u0004\b]\u00107R$\u0010^\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u00078V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b_\u0010\t\"\u0004\b`\u00107R\u0014\u0010a\u001a\u00020B8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bb\u0010DR$\u0010c\u001a\u00020B2\u0006\u0010\u0012\u001a\u00020B8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\bd\u0010D\"\u0004\be\u0010PR\u0014\u0010f\u001a\u00020B8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bg\u0010DR\u0014\u0010h\u001a\u00020i8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bj\u0010kR\u0014\u0010l\u001a\u00020J8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bm\u0010LR\u0014\u0010n\u001a\u00020B8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bo\u0010DR\u0014\u0010p\u001a\u00020B8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bq\u0010DR\u0014\u0010r\u001a\u00020B8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bs\u0010DR\u0016\u0010t\u001a\u0004\u0018\u00010\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bu\u0010vR$\u0010w\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u000b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\bx\u0010\r\"\u0004\by\u0010\u0016R$\u0010z\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u000b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b{\u0010\r\"\u0004\b|\u0010\u0016R\u0014\u0010}\u001a\u00020\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b~\u0010\tR&\u0010\u007f\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u00078V@VX\u0096\u000e\u00a2\u0006\u000e\u001a\u0005\b\u0080\u0001\u0010\t\"\u0005\b\u0081\u0001\u00107R'\u0010\u0082\u0001\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u000b8V@VX\u0096\u000e\u00a2\u0006\u000e\u001a\u0005\b\u0083\u0001\u0010\r\"\u0005\b\u0084\u0001\u0010\u0016R\u0016\u0010\u0085\u0001\u001a\u00020#8VX\u0096\u0004\u00a2\u0006\u0007\u001a\u0005\b\u0086\u0001\u0010%R\u0018\u0010\u0087\u0001\u001a\u00030\u0088\u00018VX\u0096\u0004\u00a2\u0006\b\u001a\u0006\b\u0089\u0001\u0010\u008a\u0001R\u0016\u0010\u008b\u0001\u001a\u00020\u000b8VX\u0096\u0004\u00a2\u0006\u0007\u001a\u0005\b\u008c\u0001\u0010\rR\u0016\u0010\u0004\u001a\u00028\u0000\u00a2\u0006\r\n\u0003\u0010\u008f\u0001\u001a\u0006\b\u008d\u0001\u0010\u008e\u0001\u00a8\u0006\u00b8\u0001"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/EntityImpl;", "T", "Lnet/minecraft/entity/Entity;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "wrapped", "(Lnet/minecraft/entity/Entity;)V", "burning", "", "getBurning", "()Z", "collisionBorderSize", "", "getCollisionBorderSize", "()F", "displayName", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "getDisplayName", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "value", "distanceWalkedModified", "getDistanceWalkedModified", "setDistanceWalkedModified", "(F)V", "distanceWalkedOnStepModified", "getDistanceWalkedOnStepModified", "setDistanceWalkedOnStepModified", "entityAlive", "getEntityAlive", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "entityBoundingBox", "getEntityBoundingBox", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "setEntityBoundingBox", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;)V", "entityId", "", "getEntityId", "()I", "eyeHeight", "getEyeHeight", "fallDistance", "getFallDistance", "setFallDistance", "height", "getHeight", "horizontalFacing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "getHorizontalFacing", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "hurtResistantTime", "getHurtResistantTime", "invisible", "getInvisible", "isAirBorne", "setAirBorne", "(Z)V", "isCollidedHorizontally", "isCollidedVertically", "isDead", "setDead", "isInLava", "isInWater", "isInWeb", "setInWeb", "isRiding", "lastTickPosX", "", "getLastTickPosX", "()D", "lastTickPosY", "getLastTickPosY", "lastTickPosZ", "getLastTickPosZ", "lookVec", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "getLookVec", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "motionX", "getMotionX", "setMotionX", "(D)V", "motionY", "getMotionY", "setMotionY", "motionZ", "getMotionZ", "setMotionZ", "name", "", "getName", "()Ljava/lang/String;", "noClip", "getNoClip", "setNoClip", "onGround", "getOnGround", "setOnGround", "posX", "getPosX", "posY", "getPosY", "setPosY", "posZ", "getPosZ", "position", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getPosition", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "positionVector", "getPositionVector", "prevPosX", "getPrevPosX", "prevPosY", "getPrevPosY", "prevPosZ", "getPrevPosZ", "ridingEntity", "getRidingEntity", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "rotationPitch", "getRotationPitch", "setRotationPitch", "rotationYaw", "getRotationYaw", "setRotationYaw", "sneaking", "getSneaking", "sprinting", "getSprinting", "setSprinting", "stepHeight", "getStepHeight", "setStepHeight", "ticksExisted", "getTicksExisted", "uniqueID", "Ljava/util/UUID;", "getUniqueID", "()Ljava/util/UUID;", "width", "getWidth", "getWrapped", "()Lnet/minecraft/entity/Entity;", "Lnet/minecraft/entity/Entity;", "asEntityLivingBase", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "asEntityPlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityPlayer;", "asEntityTNTPrimed", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityTNTPrimed;", "canBeCollidedWith", "canRiderInteract", "copyLocationAndAnglesFrom", "", "player", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "equals", "other", "", "getDistance", "x", "y", "z", "getDistanceSq", "blockPos", "getDistanceSqToEntity", "it", "getDistanceToEntity", "getLook", "partialTicks", "getPositionEyes", "isInsideOfMaterial", "material", "Lnet/ccbluex/liquidbounce/api/minecraft/block/material/IMaterial;", "moveEntity", "rayTrace", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "range", "setPosition", "setPositionAndRotation", "oldX", "oldY", "oldZ", "setPositionAndUpdate", "LiKingSense"})
public class EntityImpl<T extends Entity>
implements IEntity {
    @NotNull
    private final T wrapped;

    @Override
    public float getDistanceWalkedOnStepModified() {
        return ((Entity)this.wrapped).field_82151_R;
    }

    @Override
    public void setDistanceWalkedOnStepModified(float value) {
        ((Entity)this.wrapped).field_82151_R = value;
    }

    @Override
    public float getDistanceWalkedModified() {
        return ((Entity)this.wrapped).field_70140_Q;
    }

    @Override
    public void setDistanceWalkedModified(float value) {
        ((Entity)this.wrapped).field_70140_Q = value;
    }

    public boolean getSneaking() {
        return this.wrapped.func_70093_af();
    }

    @Override
    public float getStepHeight() {
        return ((Entity)this.wrapped).field_70138_W;
    }

    @Override
    public void setStepHeight(float value) {
        ((Entity)this.wrapped).field_70138_W = value;
    }

    @Override
    @NotNull
    public IEnumFacing getHorizontalFacing() {
        EnumFacing enumFacing = this.wrapped.func_174811_aO();
        Intrinsics.checkExpressionValueIsNotNull((Object)enumFacing, (String)"wrapped.horizontalFacing");
        EnumFacing $this$wrap$iv = enumFacing;
        boolean $i$f$wrap = false;
        return new EnumFacingImpl($this$wrap$iv);
    }

    @Override
    @Nullable
    public WVec3 getLookVec() {
        Vec3d vec3d = this.wrapped.func_70040_Z();
        Intrinsics.checkExpressionValueIsNotNull((Object)vec3d, (String)"wrapped.lookVec");
        Vec3d $this$wrap$iv = vec3d;
        boolean $i$f$wrap = false;
        return new WVec3($this$wrap$iv.field_72450_a, $this$wrap$iv.field_72448_b, $this$wrap$iv.field_72449_c);
    }

    @Override
    public boolean isDead() {
        return ((Entity)this.wrapped).field_70128_L;
    }

    @Override
    public void setDead(boolean value) {
        ((Entity)this.wrapped).field_70128_L = value;
    }

    @Override
    public boolean isCollidedVertically() {
        return ((Entity)this.wrapped).field_70124_G;
    }

    @Override
    public boolean isCollidedHorizontally() {
        return ((Entity)this.wrapped).field_70123_F;
    }

    @Override
    public boolean isAirBorne() {
        return ((Entity)this.wrapped).field_70160_al;
    }

    @Override
    public void setAirBorne(boolean value) {
        ((Entity)this.wrapped).field_70160_al = value;
    }

    @Override
    public int getHurtResistantTime() {
        return ((Entity)this.wrapped).field_70172_ad;
    }

    @Override
    public boolean getNoClip() {
        return ((Entity)this.wrapped).field_70145_X;
    }

    @Override
    public void setNoClip(boolean value) {
        ((Entity)this.wrapped).field_70145_X = value;
    }

    @Override
    public boolean getSprinting() {
        return this.wrapped.func_70051_ag();
    }

    @Override
    public void setSprinting(boolean value) {
        this.wrapped.func_70031_b(value);
    }

    @Override
    @NotNull
    public WVec3 getPositionVector() {
        Vec3d vec3d = this.wrapped.func_174791_d();
        Intrinsics.checkExpressionValueIsNotNull((Object)vec3d, (String)"wrapped.positionVector");
        Vec3d $this$wrap$iv = vec3d;
        boolean $i$f$wrap = false;
        return new WVec3($this$wrap$iv.field_72450_a, $this$wrap$iv.field_72448_b, $this$wrap$iv.field_72449_c);
    }

    @Override
    public boolean isRiding() {
        return this.wrapped.func_184218_aH();
    }

    @Override
    @NotNull
    public WBlockPos getPosition() {
        BlockPos blockPos = this.wrapped.func_180425_c();
        Intrinsics.checkExpressionValueIsNotNull((Object)blockPos, (String)"wrapped.position");
        BlockPos $this$wrap$iv = blockPos;
        boolean $i$f$wrap = false;
        return new WBlockPos($this$wrap$iv.func_177958_n(), $this$wrap$iv.func_177956_o(), $this$wrap$iv.func_177952_p());
    }

    public boolean getBurning() {
        return this.wrapped.func_70027_ad();
    }

    @Override
    public float getFallDistance() {
        return ((Entity)this.wrapped).field_70143_R;
    }

    @Override
    public void setFallDistance(float value) {
        ((Entity)this.wrapped).field_70143_R = value;
    }

    @Override
    public boolean isInWater() {
        return this.wrapped.func_70090_H();
    }

    @Override
    public boolean isInWeb() {
        return ((Entity)this.wrapped).field_70134_J;
    }

    @Override
    public void setInWeb(boolean value) {
        ((Entity)this.wrapped).field_70134_J = value;
    }

    @Override
    public boolean isInLava() {
        return this.wrapped.func_180799_ab();
    }

    @Override
    public float getWidth() {
        return ((Entity)this.wrapped).field_70130_N;
    }

    @Override
    public float getHeight() {
        return ((Entity)this.wrapped).field_70131_O;
    }

    @Override
    public boolean getOnGround() {
        return ((Entity)this.wrapped).field_70122_E;
    }

    @Override
    public void setOnGround(boolean value) {
        ((Entity)this.wrapped).field_70122_E = value;
    }

    @Override
    @Nullable
    public IEntity getRidingEntity() {
        IEntity iEntity;
        Entity entity = ((Entity)this.wrapped).field_184239_as;
        if (entity != null) {
            Entity $this$wrap$iv = entity;
            boolean $i$f$wrap = false;
            iEntity = new EntityImpl<Entity>($this$wrap$iv);
        } else {
            iEntity = null;
        }
        return iEntity;
    }

    @Override
    public float getCollisionBorderSize() {
        return this.wrapped.func_70111_Y();
    }

    @Override
    public double getMotionX() {
        return ((Entity)this.wrapped).field_70159_w;
    }

    @Override
    public void setMotionX(double value) {
        ((Entity)this.wrapped).field_70159_w = value;
    }

    @Override
    public double getMotionY() {
        return ((Entity)this.wrapped).field_70181_x;
    }

    @Override
    public void setMotionY(double value) {
        ((Entity)this.wrapped).field_70181_x = value;
    }

    @Override
    public double getMotionZ() {
        return ((Entity)this.wrapped).field_70179_y;
    }

    @Override
    public void setMotionZ(double value) {
        ((Entity)this.wrapped).field_70179_y = value;
    }

    @Override
    public float getEyeHeight() {
        return this.wrapped.func_70047_e();
    }

    @Override
    @NotNull
    public IAxisAlignedBB getEntityBoundingBox() {
        AxisAlignedBB axisAlignedBB = this.wrapped.func_174813_aQ();
        Intrinsics.checkExpressionValueIsNotNull((Object)axisAlignedBB, (String)"wrapped.entityBoundingBox");
        AxisAlignedBB $this$wrap$iv = axisAlignedBB;
        boolean $i$f$wrap = false;
        return new AxisAlignedBBImpl($this$wrap$iv);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void setEntityBoundingBox(@NotNull IAxisAlignedBB value) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)value, (String)"value");
        IAxisAlignedBB iAxisAlignedBB = value;
        T t2 = this.wrapped;
        boolean $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        t2.func_174826_a(axisAlignedBB);
    }

    @Override
    public double getPosX() {
        return ((Entity)this.wrapped).field_70165_t;
    }

    @Override
    public double getPosY() {
        return ((Entity)this.wrapped).field_70163_u;
    }

    @Override
    public void setPosY(double value) {
        ((Entity)this.wrapped).field_70163_u = value;
    }

    @Override
    public double getPosZ() {
        return ((Entity)this.wrapped).field_70161_v;
    }

    @Override
    public double getLastTickPosX() {
        return ((Entity)this.wrapped).field_70142_S;
    }

    @Override
    public double getLastTickPosY() {
        return ((Entity)this.wrapped).field_70137_T;
    }

    @Override
    public double getLastTickPosZ() {
        return ((Entity)this.wrapped).field_70136_U;
    }

    @Override
    public double getPrevPosX() {
        return ((Entity)this.wrapped).field_70169_q;
    }

    @Override
    public double getPrevPosY() {
        return ((Entity)this.wrapped).field_70167_r;
    }

    @Override
    public double getPrevPosZ() {
        return ((Entity)this.wrapped).field_70166_s;
    }

    @Override
    public float getRotationYaw() {
        return ((Entity)this.wrapped).field_70177_z;
    }

    @Override
    public void setRotationYaw(float value) {
        ((Entity)this.wrapped).field_70177_z = value;
    }

    @Override
    public float getRotationPitch() {
        return ((Entity)this.wrapped).field_70127_C;
    }

    @Override
    public void setRotationPitch(float value) {
        ((Entity)this.wrapped).field_70125_A = value;
    }

    @Override
    public int getEntityId() {
        return this.wrapped.func_145782_y();
    }

    @Override
    @Nullable
    public IIChatComponent getDisplayName() {
        ITextComponent iTextComponent = this.wrapped.func_145748_c_();
        Intrinsics.checkExpressionValueIsNotNull((Object)iTextComponent, (String)"wrapped.displayName");
        ITextComponent $this$wrap$iv = iTextComponent;
        boolean $i$f$wrap = false;
        return new IChatComponentImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public UUID getUniqueID() {
        UUID uUID = this.wrapped.func_110124_au();
        Intrinsics.checkExpressionValueIsNotNull((Object)uUID, (String)"wrapped.uniqueID");
        return uUID;
    }

    @Override
    @Nullable
    public String getName() {
        return this.wrapped.func_70005_c_();
    }

    @Override
    public int getTicksExisted() {
        return ((Entity)this.wrapped).field_70173_aa;
    }

    public boolean getEntityAlive() {
        return this.wrapped.func_70089_S();
    }

    public boolean getInvisible() {
        return this.wrapped.func_82150_aj();
    }

    @Override
    @NotNull
    public WVec3 getPositionEyes(float partialTicks) {
        Vec3d vec3d = this.wrapped.func_174824_e(partialTicks);
        Intrinsics.checkExpressionValueIsNotNull((Object)vec3d, (String)"wrapped.getPositionEyes(partialTicks)");
        Vec3d $this$wrap$iv = vec3d;
        boolean $i$f$wrap = false;
        return new WVec3($this$wrap$iv.field_72450_a, $this$wrap$iv.field_72448_b, $this$wrap$iv.field_72449_c);
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.wrapped.func_70067_L();
    }

    @Override
    public boolean canRiderInteract() {
        return this.wrapped.canRiderInteract();
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        this.wrapped.func_70091_d(MoverType.PLAYER, x, y, z);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public float getDistanceToEntity(@NotNull IEntity it) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)it, (String)"it");
        IEntity iEntity = it;
        T t2 = this.wrapped;
        boolean $i$f$unwrap = false;
        T t3 = ((EntityImpl)$this$unwrap$iv).getWrapped();
        return t2.func_70032_d(t3);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public double getDistanceSqToEntity(@NotNull IEntity it) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)it, (String)"it");
        IEntity iEntity = it;
        T t2 = this.wrapped;
        boolean $i$f$unwrap = false;
        T t3 = ((EntityImpl)$this$unwrap$iv).getWrapped();
        return t2.func_70068_e(t3);
    }

    @Override
    @NotNull
    public IEntityPlayer asEntityPlayer() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.player.EntityPlayer");
        }
        return new EntityPlayerImpl<EntityPlayer>((EntityPlayer)t2);
    }

    @Override
    @NotNull
    public IEntityLivingBase asEntityLivingBase() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.EntityLivingBase");
        }
        return new EntityLivingBaseImpl<EntityLivingBase>((EntityLivingBase)t2);
    }

    @Override
    @NotNull
    public IEntityTNTPrimed asEntityTNTPrimed() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.item.EntityTNTPrimed");
        }
        return new EntityTNTPrimedImpl((EntityTNTPrimed)t2);
    }

    @Override
    public double getDistance(double x, double y, double z) {
        return this.wrapped.func_70011_f(x, y, z);
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.wrapped.func_70107_b(x, y, z);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public double getDistanceSq(@NotNull WBlockPos blockPos) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)blockPos, (String)"blockPos");
        WBlockPos wBlockPos = blockPos;
        T t2 = this.wrapped;
        boolean $i$f$unwrap = false;
        BlockPos blockPos2 = new BlockPos($this$unwrap$iv.getX(), $this$unwrap$iv.getY(), $this$unwrap$iv.getZ());
        return t2.func_174818_b(blockPos2);
    }

    @Override
    public void setPositionAndUpdate(double posX, double posY, double posZ) {
        this.wrapped.func_70634_a(posX, posY, posZ);
    }

    @Override
    @Nullable
    public IMovingObjectPosition rayTrace(double range, float partialTicks) {
        IMovingObjectPosition iMovingObjectPosition;
        RayTraceResult rayTraceResult = this.wrapped.func_174822_a(range, partialTicks);
        if (rayTraceResult != null) {
            RayTraceResult $this$wrap$iv = rayTraceResult;
            boolean $i$f$wrap = false;
            iMovingObjectPosition = new MovingObjectPositionImpl($this$wrap$iv);
        } else {
            iMovingObjectPosition = null;
        }
        return iMovingObjectPosition;
    }

    @Override
    @NotNull
    public WVec3 getLook(float partialTicks) {
        Vec3d vec3d = this.wrapped.func_70676_i(partialTicks);
        Intrinsics.checkExpressionValueIsNotNull((Object)vec3d, (String)"wrapped.getLook(partialTicks)");
        Vec3d $this$wrap$iv = vec3d;
        boolean $i$f$wrap = false;
        return new WVec3($this$wrap$iv.field_72450_a, $this$wrap$iv.field_72448_b, $this$wrap$iv.field_72449_c);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean isInsideOfMaterial(@NotNull IMaterial material) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)material, (String)"material");
        IMaterial iMaterial = material;
        T t2 = this.wrapped;
        boolean $i$f$unwrap = false;
        Material material2 = ((MaterialImpl)$this$unwrap$iv).getWrapped();
        return t2.func_70055_a(material2);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void copyLocationAndAnglesFrom(@NotNull IEntityPlayerSP player) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)player, (String)"player");
        IEntityPlayerSP iEntityPlayerSP = player;
        T t2 = this.wrapped;
        boolean $i$f$unwrap = false;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)((EntityPlayerSPImpl)$this$unwrap$iv).getWrapped();
        t2.func_82149_j((Entity)entityPlayerSP);
    }

    @Override
    public void setPositionAndRotation(double oldX, double oldY, double oldZ, float rotationYaw, float rotationPitch) {
        this.wrapped.func_70080_a(oldX, oldY, oldZ, rotationYaw, rotationPitch);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof EntityImpl && Intrinsics.areEqual(((EntityImpl)other).wrapped, this.wrapped);
    }

    @NotNull
    public final T getWrapped() {
        return this.wrapped;
    }

    public EntityImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

