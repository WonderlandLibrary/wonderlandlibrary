/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
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
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.UUID;
import kotlin.TypeCastException;
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
import org.jetbrains.annotations.Nullable;

public class EntityImpl
implements IEntity {
    private final Entity wrapped;

    public boolean getInvisible() {
        return this.wrapped.func_82150_aj();
    }

    @Override
    public float getEyeHeight() {
        return this.wrapped.func_70047_e();
    }

    @Override
    public void setDistanceWalkedOnStepModified(float f) {
        this.wrapped.field_82151_R = f;
    }

    public boolean getBurning() {
        return this.wrapped.func_70027_ad();
    }

    @Override
    public double getDistance(double d, double d2, double d3) {
        return this.wrapped.func_70011_f(d, d2, d3);
    }

    @Override
    public float getHeight() {
        return this.wrapped.field_70131_O;
    }

    @Override
    public void setDead(boolean bl) {
        this.wrapped.field_70128_L = bl;
    }

    @Override
    public IAxisAlignedBB getEntityBoundingBox() {
        AxisAlignedBB axisAlignedBB = this.wrapped.func_174813_aQ();
        boolean bl = false;
        return new AxisAlignedBBImpl(axisAlignedBB);
    }

    @Override
    public int getEntityId() {
        return this.wrapped.func_145782_y();
    }

    @Override
    public float getDistanceWalkedOnStepModified() {
        return this.wrapped.field_82151_R;
    }

    @Override
    public void moveEntity(double d, double d2, double d3) {
        this.wrapped.func_70091_d(MoverType.PLAYER, d, d2, d3);
    }

    @Override
    public double getPosX() {
        return this.wrapped.field_70165_t;
    }

    @Override
    public void setPosition(double d, double d2, double d3) {
        this.wrapped.func_70107_b(d, d2, d3);
    }

    @Override
    public boolean isInLava() {
        return this.wrapped.func_180799_ab();
    }

    @Override
    public float getStepHeight() {
        return this.wrapped.field_70138_W;
    }

    @Override
    public boolean isCollidedHorizontally() {
        return this.wrapped.field_70123_F;
    }

    @Override
    public WVec3 getPositionEyes(float f) {
        Vec3d vec3d = this.wrapped.func_174824_e(f);
        boolean bl = false;
        return new WVec3(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
    }

    @Override
    public WBlockPos getPosition() {
        BlockPos blockPos = this.wrapped.func_180425_c();
        boolean bl = false;
        return new WBlockPos(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p());
    }

    @Override
    public boolean isInWeb() {
        return this.wrapped.field_70134_J;
    }

    @Override
    public void setDistanceWalkedModified(float f) {
        this.wrapped.field_70140_Q = f;
    }

    @Override
    public void setMotionZ(double d) {
        this.wrapped.field_70179_y = d;
    }

    @Override
    public double getDistanceSqToEntity(IEntity iEntity) {
        IEntity iEntity2 = iEntity;
        Entity entity = this.wrapped;
        boolean bl = false;
        Entity entity2 = ((EntityImpl)iEntity2).getWrapped();
        return entity.func_70068_e(entity2);
    }

    @Override
    public float getDistanceToEntity(IEntity iEntity) {
        IEntity iEntity2 = iEntity;
        Entity entity = this.wrapped;
        boolean bl = false;
        Entity entity2 = ((EntityImpl)iEntity2).getWrapped();
        return entity.func_70032_d(entity2);
    }

    @Override
    public int getTicksExisted() {
        return this.wrapped.field_70173_aa;
    }

    @Override
    public boolean isAirBorne() {
        return this.wrapped.field_70160_al;
    }

    @Override
    public IEntity getRidingEntity() {
        IEntity iEntity;
        Entity entity = this.wrapped.field_184239_as;
        if (entity != null) {
            Entity entity2 = entity;
            boolean bl = false;
            iEntity = new EntityImpl(entity2);
        } else {
            iEntity = null;
        }
        return iEntity;
    }

    @Override
    public double getLastTickPosZ() {
        return this.wrapped.field_70136_U;
    }

    @Override
    public double getPrevPosX() {
        return this.wrapped.field_70169_q;
    }

    @Override
    public boolean getSprinting() {
        return this.wrapped.func_70051_ag();
    }

    @Override
    public boolean isBurning() {
        return this.getBurning();
    }

    @Override
    public boolean isSneaking() {
        return this.getSneaking();
    }

    @Override
    public WVec3 getLook(float f) {
        Vec3d vec3d = this.wrapped.func_70676_i(f);
        boolean bl = false;
        return new WVec3(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
    }

    @Override
    public void setInWeb(boolean bl) {
        this.wrapped.field_70134_J = bl;
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.wrapped.func_70067_L();
    }

    @Override
    public boolean isCollidedVertically() {
        return this.wrapped.field_70124_G;
    }

    @Override
    public float getRotationYaw() {
        return this.wrapped.field_70177_z;
    }

    @Override
    public void setEntityBoundingBox(IAxisAlignedBB iAxisAlignedBB) {
        IAxisAlignedBB iAxisAlignedBB2 = iAxisAlignedBB;
        Entity entity = this.wrapped;
        boolean bl = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)iAxisAlignedBB2).getWrapped();
        entity.func_174826_a(axisAlignedBB);
    }

    @Override
    public IEntityTNTPrimed asEntityTNTPrimed() {
        Entity entity = this.wrapped;
        if (entity == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.item.EntityTNTPrimed");
        }
        return new EntityTNTPrimedImpl((EntityTNTPrimed)entity);
    }

    @Override
    public void setPositionAndRotation(double d, double d2, double d3, float f, float f2) {
        this.wrapped.func_70080_a(d, d2, d3, f, f2);
    }

    @Override
    public boolean isInvisible() {
        return this.getInvisible();
    }

    @Override
    public void setSprinting(boolean bl) {
        this.wrapped.func_70031_b(bl);
    }

    @Override
    public void setAirBorne(boolean bl) {
        this.wrapped.field_70160_al = bl;
    }

    @Override
    public double getMotionY() {
        return this.wrapped.field_70181_x;
    }

    @Override
    public double getMotionZ() {
        return this.wrapped.field_70179_y;
    }

    @Override
    public float getCollisionBorderSize() {
        return this.wrapped.func_70111_Y();
    }

    @Override
    public double getLastTickPosY() {
        return this.wrapped.field_70137_T;
    }

    @Override
    public boolean isRiding() {
        return this.wrapped.func_184218_aH();
    }

    @Override
    public IEnumFacing getHorizontalFacing() {
        EnumFacing enumFacing = this.wrapped.func_174811_aO();
        boolean bl = false;
        return new EnumFacingImpl(enumFacing);
    }

    @Override
    public IMovingObjectPosition rayTrace(double d, float f) {
        IMovingObjectPosition iMovingObjectPosition;
        RayTraceResult rayTraceResult = this.wrapped.func_174822_a(d, f);
        if (rayTraceResult != null) {
            RayTraceResult rayTraceResult2 = rayTraceResult;
            boolean bl = false;
            iMovingObjectPosition = new MovingObjectPositionImpl(rayTraceResult2);
        } else {
            iMovingObjectPosition = null;
        }
        return iMovingObjectPosition;
    }

    @Override
    public double getDistanceSq(WBlockPos wBlockPos) {
        WBlockPos wBlockPos2 = wBlockPos;
        Entity entity = this.wrapped;
        boolean bl = false;
        BlockPos blockPos = new BlockPos(wBlockPos2.getX(), wBlockPos2.getY(), wBlockPos2.getZ());
        return entity.func_174818_b(blockPos);
    }

    @Override
    public boolean isEntityAlive() {
        return this.getEntityAlive();
    }

    @Override
    public boolean isInsideOfMaterial(IMaterial iMaterial) {
        IMaterial iMaterial2 = iMaterial;
        Entity entity = this.wrapped;
        boolean bl = false;
        Material material = ((MaterialImpl)iMaterial2).getWrapped();
        return entity.func_70055_a(material);
    }

    @Override
    public IIChatComponent getDisplayName() {
        ITextComponent iTextComponent = this.wrapped.func_145748_c_();
        boolean bl = false;
        return new IChatComponentImpl(iTextComponent);
    }

    @Override
    public float getDistanceWalkedModified() {
        return this.wrapped.field_70140_Q;
    }

    @Override
    public UUID getUniqueID() {
        return this.wrapped.func_110124_au();
    }

    public EntityImpl(Entity entity) {
        this.wrapped = entity;
    }

    @Override
    public void setPositionAndUpdate(double d, double d2, double d3) {
        this.wrapped.func_70634_a(d, d2, d3);
    }

    @Override
    public IEntityLivingBase asEntityLivingBase() {
        Entity entity = this.wrapped;
        if (entity == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.EntityLivingBase");
        }
        return new EntityLivingBaseImpl((EntityLivingBase)entity);
    }

    @Override
    public void setRotationPitch(float f) {
        this.wrapped.field_70125_A = f;
    }

    @Override
    public void setNoClip(boolean bl) {
        this.wrapped.field_70145_X = bl;
    }

    @Override
    public double getPrevPosZ() {
        return this.wrapped.field_70166_s;
    }

    @Override
    public String getName() {
        return this.wrapped.func_70005_c_();
    }

    @Override
    public WVec3 getPositionVector() {
        Vec3d vec3d = this.wrapped.func_174791_d();
        boolean bl = false;
        return new WVec3(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
    }

    @Override
    public void setMotionX(double d) {
        this.wrapped.field_70159_w = d;
    }

    @Override
    public double getPrevPosY() {
        return this.wrapped.field_70167_r;
    }

    @Override
    public void copyLocationAndAnglesFrom(IEntityPlayerSP iEntityPlayerSP) {
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        Entity entity = this.wrapped;
        boolean bl = false;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)((EntityPlayerSPImpl)iEntityPlayerSP2).getWrapped();
        entity.func_82149_j((Entity)entityPlayerSP);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public IEntityPlayer asEntityPlayer() {
        Entity entity = this.wrapped;
        if (entity == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.player.EntityPlayer");
        }
        return new EntityPlayerImpl((EntityPlayer)entity);
    }

    @Override
    public void setFallDistance(float f) {
        this.wrapped.field_70143_R = f;
    }

    @Override
    public double getLastTickPosX() {
        return this.wrapped.field_70142_S;
    }

    @Override
    public double getMotionX() {
        return this.wrapped.field_70159_w;
    }

    @Override
    public boolean isDead() {
        return this.wrapped.field_70128_L;
    }

    @Override
    public void setMotionY(double d) {
        this.wrapped.field_70181_x = d;
    }

    @Override
    public boolean getNoClip() {
        return this.wrapped.field_70145_X;
    }

    @Override
    public void setOnGround(boolean bl) {
        this.wrapped.field_70122_E = bl;
    }

    @Override
    public int getHurtResistantTime() {
        return this.wrapped.field_70172_ad;
    }

    @Override
    public double getPosY() {
        return this.wrapped.field_70163_u;
    }

    @Override
    public double getPosZ() {
        return this.wrapped.field_70161_v;
    }

    @Override
    public float getRotationPitch() {
        return this.wrapped.field_70127_C;
    }

    @Override
    public boolean canRiderInteract() {
        return this.wrapped.canRiderInteract();
    }

    @Override
    public boolean isInWater() {
        return this.wrapped.func_70090_H();
    }

    @Override
    public void setRotationYaw(float f) {
        this.wrapped.field_70177_z = f;
    }

    @Override
    public void setPosY(double d) {
        this.wrapped.field_70163_u = d;
    }

    public boolean getSneaking() {
        return this.wrapped.func_70093_af();
    }

    @Override
    public WVec3 getLookVec() {
        Vec3d vec3d = this.wrapped.func_70040_Z();
        boolean bl = false;
        return new WVec3(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
    }

    @Override
    public float getWidth() {
        return this.wrapped.field_70130_N;
    }

    public final Entity getWrapped() {
        return this.wrapped;
    }

    @Override
    public void setStepHeight(float f) {
        this.wrapped.field_70138_W = f;
    }

    @Override
    public float getFallDistance() {
        return this.wrapped.field_70143_R;
    }

    @Override
    public boolean getOnGround() {
        return this.wrapped.field_70122_E;
    }

    public boolean getEntityAlive() {
        return this.wrapped.func_70089_S();
    }
}

