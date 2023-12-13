// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemPotion;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.EntityLivingBase;

public class TrajectoryCalculator
{
    public static ThrowingType getThrowType(final EntityLivingBase entity) {
        if (entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            return ThrowingType.NONE;
        }
        final ItemStack itemStack = entity.getHeldItem(EnumHand.MAIN_HAND);
        final Item item = itemStack.getItem();
        if (item instanceof ItemPotion) {
            if (itemStack.getItem() instanceof ItemSplashPotion) {
                return ThrowingType.POTION;
            }
        }
        else {
            if (item instanceof ItemBow && entity.isHandActive()) {
                return ThrowingType.BOW;
            }
            if (item instanceof ItemExpBottle) {
                return ThrowingType.EXPERIENCE;
            }
            if (item instanceof ItemSnowball || item instanceof ItemEgg || item instanceof ItemEnderPearl) {
                return ThrowingType.NORMAL;
            }
        }
        return ThrowingType.NONE;
    }
    
    public static double[] interpolate(final Entity entity) {
        final double posX = interpolate(entity.posX, entity.lastTickPosX) - Wrapper.getMinecraft().renderManager.renderPosX;
        final double posY = interpolate(entity.posY, entity.lastTickPosY) - Wrapper.getMinecraft().renderManager.renderPosY;
        final double posZ = interpolate(entity.posZ, entity.lastTickPosZ) - Wrapper.getMinecraft().renderManager.renderPosZ;
        return new double[] { posX, posY, posZ };
    }
    
    public static double interpolate(final double now, final double then) {
        return then + (now - then) * Wrapper.getMinecraft().getRenderPartialTicks();
    }
    
    public static Vec3d mult(final Vec3d factor, final float multiplier) {
        return new Vec3d(factor.x * multiplier, factor.y * multiplier, factor.z * multiplier);
    }
    
    public static Vec3d div(final Vec3d factor, final float divisor) {
        return new Vec3d(factor.x / divisor, factor.y / divisor, factor.z / divisor);
    }
    
    public enum ThrowingType
    {
        NONE, 
        BOW, 
        EXPERIENCE, 
        POTION, 
        NORMAL;
    }
    
    public static final class FlightPath
    {
        private EntityLivingBase shooter;
        public Vec3d position;
        private Vec3d motion;
        private float yaw;
        private float pitch;
        private AxisAlignedBB boundingBox;
        private boolean collided;
        private RayTraceResult target;
        private ThrowingType throwingType;
        
        public FlightPath(final EntityLivingBase entityLivingBase, final ThrowingType throwingType) {
            this.shooter = entityLivingBase;
            this.throwingType = throwingType;
            final double[] ipos = TrajectoryCalculator.interpolate((Entity)this.shooter);
            this.setLocationAndAngles(ipos[0] + Wrapper.getMinecraft().getRenderManager().renderPosX, ipos[1] + this.shooter.getEyeHeight() + Wrapper.getMinecraft().getRenderManager().renderPosY, ipos[2] + Wrapper.getMinecraft().getRenderManager().renderPosZ, this.shooter.rotationYaw, this.shooter.rotationPitch);
            final Vec3d startingOffset = new Vec3d((double)(MathHelper.cos(this.yaw / 180.0f * 3.1415927f) * 0.16f), 0.1, (double)(MathHelper.sin(this.yaw / 180.0f * 3.1415927f) * 0.16f));
            this.setPosition(this.position = this.position.subtract(startingOffset));
            this.setThrowableHeading(this.motion = new Vec3d((double)(-MathHelper.sin(this.yaw / 180.0f * 3.1415927f) * MathHelper.cos(this.pitch / 180.0f * 3.1415927f)), (double)(-MathHelper.sin(this.pitch / 180.0f * 3.1415927f)), (double)(MathHelper.cos(this.yaw / 180.0f * 3.1415927f) * MathHelper.cos(this.pitch / 180.0f * 3.1415927f))), this.getInitialVelocity());
        }
        
        public void onUpdate() {
            Vec3d prediction = this.position.add(this.motion);
            final RayTraceResult blockCollision = this.shooter.getEntityWorld().rayTraceBlocks(this.position, prediction, false, true, false);
            if (blockCollision != null) {
                prediction = blockCollision.hitVec;
            }
            this.onCollideWithEntity(prediction, blockCollision);
            if (this.target != null) {
                this.collided = true;
                this.setPosition(this.target.hitVec);
                return;
            }
            if (this.position.y <= 0.0) {
                this.collided = true;
                return;
            }
            this.position = this.position.add(this.motion);
            float motionModifier = 0.99f;
            if (this.shooter.getEntityWorld().isMaterialInBB(this.boundingBox, Material.WATER)) {
                motionModifier = ((this.throwingType == ThrowingType.BOW) ? 0.6f : 0.8f);
            }
            this.motion = TrajectoryCalculator.mult(this.motion, motionModifier);
            this.motion = this.motion.subtract(0.0, (double)this.getGravityVelocity(), 0.0);
            this.setPosition(this.position);
        }
        
        private void onCollideWithEntity(final Vec3d prediction, final RayTraceResult blockCollision) {
            Entity collidingEntity = null;
            double currentDistance = 0.0;
            final List<Entity> collisionEntities = (List<Entity>)this.shooter.world.getEntitiesWithinAABBExcludingEntity((Entity)this.shooter, this.boundingBox.expand(this.motion.x, this.motion.y, this.motion.z).expand(1.0, 1.0, 1.0));
            for (final Entity entity : collisionEntities) {
                if (!entity.canBeCollidedWith() && entity != this.shooter) {
                    continue;
                }
                final float collisionSize = entity.getCollisionBorderSize();
                final AxisAlignedBB expandedBox = entity.getEntityBoundingBox().expand((double)collisionSize, (double)collisionSize, (double)collisionSize);
                final RayTraceResult objectPosition = expandedBox.calculateIntercept(this.position, prediction);
                if (objectPosition == null) {
                    continue;
                }
                final double distanceTo = this.position.distanceTo(objectPosition.hitVec);
                if (distanceTo >= currentDistance && currentDistance != 0.0) {
                    continue;
                }
                collidingEntity = entity;
                currentDistance = distanceTo;
            }
            if (collidingEntity != null) {
                this.target = new RayTraceResult(collidingEntity);
            }
            else {
                this.target = blockCollision;
            }
        }
        
        private float getInitialVelocity() {
            final Item item = this.shooter.getHeldItem(EnumHand.MAIN_HAND).getItem();
            switch (this.throwingType) {
                case BOW: {
                    final ItemBow bow = (ItemBow)item;
                    final int useDuration = bow.getMaxItemUseDuration(this.shooter.getHeldItem(EnumHand.MAIN_HAND)) - this.shooter.getItemInUseCount();
                    float velocity = useDuration / 20.0f;
                    velocity = (velocity * velocity + velocity * 2.0f) / 3.0f;
                    if (velocity > 1.0f) {
                        velocity = 1.0f;
                    }
                    return velocity * 2.0f * 1.5f;
                }
                case POTION: {
                    return 0.5f;
                }
                case EXPERIENCE: {
                    return 0.7f;
                }
                case NORMAL: {
                    return 1.5f;
                }
                default: {
                    return 1.5f;
                }
            }
        }
        
        private float getGravityVelocity() {
            switch (this.throwingType) {
                case BOW:
                case POTION: {
                    return 0.05f;
                }
                case EXPERIENCE: {
                    return 0.07f;
                }
                case NORMAL: {
                    return 0.03f;
                }
                default: {
                    return 0.03f;
                }
            }
        }
        
        private void setLocationAndAngles(final double x, final double y, final double z, final float yaw, final float pitch) {
            this.position = new Vec3d(x, y, z);
            this.yaw = yaw;
            this.pitch = pitch;
        }
        
        private void setPosition(final Vec3d position) {
            this.position = new Vec3d(position.x, position.y, position.z);
            final double entitySize = ((this.throwingType == ThrowingType.BOW) ? 0.5 : 0.25) / 2.0;
            this.boundingBox = new AxisAlignedBB(position.x - entitySize, position.y - entitySize, position.z - entitySize, position.x + entitySize, position.y + entitySize, position.z + entitySize);
        }
        
        private void setThrowableHeading(final Vec3d motion, final float velocity) {
            this.motion = TrajectoryCalculator.div(motion, (float)motion.length());
            this.motion = TrajectoryCalculator.mult(this.motion, velocity);
        }
        
        public boolean isCollided() {
            return this.collided;
        }
        
        public RayTraceResult getCollidingTarget() {
            return this.target;
        }
    }
}
