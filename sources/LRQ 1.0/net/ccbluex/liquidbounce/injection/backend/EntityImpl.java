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

public class EntityImpl<T extends Entity>
implements IEntity {
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
    public IEnumFacing getHorizontalFacing() {
        EnumFacing $this$wrap$iv = this.wrapped.func_174811_aO();
        boolean $i$f$wrap = false;
        return new EnumFacingImpl($this$wrap$iv);
    }

    @Override
    public WVec3 getLookVec() {
        Vec3d $this$wrap$iv = this.wrapped.func_70040_Z();
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
    public WVec3 getPositionVector() {
        Vec3d $this$wrap$iv = this.wrapped.func_174791_d();
        boolean $i$f$wrap = false;
        return new WVec3($this$wrap$iv.field_72450_a, $this$wrap$iv.field_72448_b, $this$wrap$iv.field_72449_c);
    }

    @Override
    public boolean isRiding() {
        return this.wrapped.func_184218_aH();
    }

    @Override
    public WBlockPos getPosition() {
        BlockPos $this$wrap$iv = this.wrapped.func_180425_c();
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
    public IAxisAlignedBB getEntityBoundingBox() {
        AxisAlignedBB $this$wrap$iv = this.wrapped.func_174813_aQ();
        boolean $i$f$wrap = false;
        return new AxisAlignedBBImpl($this$wrap$iv);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void setEntityBoundingBox(IAxisAlignedBB value) {
        void $this$unwrap$iv;
        IAxisAlignedBB iAxisAlignedBB = value;
        T t = this.wrapped;
        boolean $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        t.func_174826_a(axisAlignedBB);
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
    public IIChatComponent getDisplayName() {
        ITextComponent $this$wrap$iv = this.wrapped.func_145748_c_();
        boolean $i$f$wrap = false;
        return new IChatComponentImpl($this$wrap$iv);
    }

    @Override
    public UUID getUniqueID() {
        return this.wrapped.func_110124_au();
    }

    @Override
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
    public WVec3 getPositionEyes(float partialTicks) {
        Vec3d $this$wrap$iv = this.wrapped.func_174824_e(partialTicks);
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
    public float getDistanceToEntity(IEntity it) {
        void $this$unwrap$iv;
        IEntity iEntity = it;
        T t = this.wrapped;
        boolean $i$f$unwrap = false;
        T t2 = ((EntityImpl)$this$unwrap$iv).getWrapped();
        return t.func_70032_d(t2);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public double getDistanceSqToEntity(IEntity it) {
        void $this$unwrap$iv;
        IEntity iEntity = it;
        T t = this.wrapped;
        boolean $i$f$unwrap = false;
        T t2 = ((EntityImpl)$this$unwrap$iv).getWrapped();
        return t.func_70068_e(t2);
    }

    @Override
    public IEntityPlayer asEntityPlayer() {
        T t = this.wrapped;
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.player.EntityPlayer");
        }
        return new EntityPlayerImpl<EntityPlayer>((EntityPlayer)t);
    }

    @Override
    public IEntityLivingBase asEntityLivingBase() {
        T t = this.wrapped;
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.EntityLivingBase");
        }
        return new EntityLivingBaseImpl<EntityLivingBase>((EntityLivingBase)t);
    }

    @Override
    public IEntityTNTPrimed asEntityTNTPrimed() {
        T t = this.wrapped;
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.item.EntityTNTPrimed");
        }
        return new EntityTNTPrimedImpl((EntityTNTPrimed)t);
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
    public double getDistanceSq(WBlockPos blockPos) {
        void $this$unwrap$iv;
        WBlockPos wBlockPos = blockPos;
        T t = this.wrapped;
        boolean $i$f$unwrap = false;
        BlockPos blockPos2 = new BlockPos($this$unwrap$iv.getX(), $this$unwrap$iv.getY(), $this$unwrap$iv.getZ());
        return t.func_174818_b(blockPos2);
    }

    @Override
    public void setPositionAndUpdate(double posX, double posY, double posZ) {
        this.wrapped.func_70634_a(posX, posY, posZ);
    }

    @Override
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
    public WVec3 getLook(float partialTicks) {
        Vec3d $this$wrap$iv = this.wrapped.func_70676_i(partialTicks);
        boolean $i$f$wrap = false;
        return new WVec3($this$wrap$iv.field_72450_a, $this$wrap$iv.field_72448_b, $this$wrap$iv.field_72449_c);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean isInsideOfMaterial(IMaterial material) {
        void $this$unwrap$iv;
        IMaterial iMaterial = material;
        T t = this.wrapped;
        boolean $i$f$unwrap = false;
        Material material2 = ((MaterialImpl)$this$unwrap$iv).getWrapped();
        return t.func_70055_a(material2);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void copyLocationAndAnglesFrom(IEntityPlayerSP player) {
        void $this$unwrap$iv;
        IEntityPlayerSP iEntityPlayerSP = player;
        T t = this.wrapped;
        boolean $i$f$unwrap = false;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)((EntityPlayerSPImpl)$this$unwrap$iv).getWrapped();
        t.func_82149_j((Entity)entityPlayerSP);
    }

    @Override
    public void setPositionAndRotation(double oldX, double oldY, double oldZ, float rotationYaw, float rotationPitch) {
        this.wrapped.func_70080_a(oldX, oldY, oldZ, rotationYaw, rotationPitch);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof EntityImpl && ((EntityImpl)other).wrapped.equals(this.wrapped);
    }

    public final T getWrapped() {
        return this.wrapped;
    }

    public EntityImpl(T wrapped) {
        this.wrapped = wrapped;
    }
}

