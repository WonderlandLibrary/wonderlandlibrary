/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.entity.boss;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityDragon
extends EntityLiving
implements IEntityMultiPart,
IMob,
IBossDisplayData {
    public double targetZ;
    public EntityDragonPart[] dragonPartArray;
    public EntityDragonPart dragonPartWing2;
    public boolean forceNewTarget;
    public float prevAnimTime;
    public EntityDragonPart dragonPartTail2;
    public int deathTicks;
    public int ringBufferIndex = -1;
    public double targetX;
    public EntityDragonPart dragonPartHead;
    public boolean slowed;
    public double targetY;
    private Entity target;
    public EntityDragonPart dragonPartBody;
    public EntityDragonPart dragonPartWing1;
    public EntityDragonPart dragonPartTail1;
    public EntityDragonPart dragonPartTail3;
    public float animTime;
    public double[][] ringBuffer = new double[64][3];
    public EntityEnderCrystal healingEnderCrystal;

    private void generatePortal(BlockPos blockPos) {
        int n = 4;
        double d = 12.25;
        double d2 = 6.25;
        int n2 = -1;
        while (n2 <= 32) {
            int n3 = -4;
            while (n3 <= 4) {
                int n4 = -4;
                while (n4 <= 4) {
                    double d3 = n3 * n3 + n4 * n4;
                    if (d3 <= 12.25) {
                        BlockPos blockPos2 = blockPos.add(n3, n2, n4);
                        if (n2 < 0) {
                            if (d3 <= 6.25) {
                                this.worldObj.setBlockState(blockPos2, Blocks.bedrock.getDefaultState());
                            }
                        } else if (n2 > 0) {
                            this.worldObj.setBlockState(blockPos2, Blocks.air.getDefaultState());
                        } else if (d3 > 6.25) {
                            this.worldObj.setBlockState(blockPos2, Blocks.bedrock.getDefaultState());
                        } else {
                            this.worldObj.setBlockState(blockPos2, Blocks.end_portal.getDefaultState());
                        }
                    }
                    ++n4;
                }
                ++n3;
            }
            ++n2;
        }
        this.worldObj.setBlockState(blockPos, Blocks.bedrock.getDefaultState());
        this.worldObj.setBlockState(blockPos.up(), Blocks.bedrock.getDefaultState());
        BlockPos blockPos3 = blockPos.up(2);
        this.worldObj.setBlockState(blockPos3, Blocks.bedrock.getDefaultState());
        this.worldObj.setBlockState(blockPos3.west(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST));
        this.worldObj.setBlockState(blockPos3.east(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST));
        this.worldObj.setBlockState(blockPos3.north(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH));
        this.worldObj.setBlockState(blockPos3.south(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH));
        this.worldObj.setBlockState(blockPos.up(3), Blocks.bedrock.getDefaultState());
        this.worldObj.setBlockState(blockPos.up(4), Blocks.dragon_egg.getDefaultState());
    }

    private void collideWithEntities(List<Entity> list) {
        double d = (this.dragonPartBody.getEntityBoundingBox().minX + this.dragonPartBody.getEntityBoundingBox().maxX) / 2.0;
        double d2 = (this.dragonPartBody.getEntityBoundingBox().minZ + this.dragonPartBody.getEntityBoundingBox().maxZ) / 2.0;
        for (Entity entity : list) {
            if (!(entity instanceof EntityLivingBase)) continue;
            double d3 = entity.posX - d;
            double d4 = entity.posZ - d2;
            double d5 = d3 * d3 + d4 * d4;
            entity.addVelocity(d3 / d5 * 4.0, 0.2f, d4 / d5 * 4.0);
        }
    }

    public EntityDragon(World world) {
        super(world);
        this.dragonPartHead = new EntityDragonPart(this, "head", 6.0f, 6.0f);
        this.dragonPartBody = new EntityDragonPart(this, "body", 8.0f, 8.0f);
        this.dragonPartTail1 = new EntityDragonPart(this, "tail", 4.0f, 4.0f);
        this.dragonPartTail2 = new EntityDragonPart(this, "tail", 4.0f, 4.0f);
        this.dragonPartTail3 = new EntityDragonPart(this, "tail", 4.0f, 4.0f);
        this.dragonPartWing1 = new EntityDragonPart(this, "wing", 4.0f, 4.0f);
        this.dragonPartWing2 = new EntityDragonPart(this, "wing", 4.0f, 4.0f);
        this.dragonPartArray = new EntityDragonPart[]{this.dragonPartHead, this.dragonPartBody, this.dragonPartTail1, this.dragonPartTail2, this.dragonPartTail3, this.dragonPartWing1, this.dragonPartWing2};
        this.setHealth(this.getMaxHealth());
        this.setSize(16.0f, 8.0f);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.targetY = 100.0;
        this.ignoreFrustumCheck = true;
    }

    @Override
    public Entity[] getParts() {
        return this.dragonPartArray;
    }

    private void attackEntitiesInList(List<Entity> list) {
        int n = 0;
        while (n < list.size()) {
            Entity entity = list.get(n);
            if (entity instanceof EntityLivingBase) {
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0f);
                this.applyEnchantments(this, entity);
            }
            ++n;
        }
    }

    @Override
    protected float getSoundVolume() {
        return 5.0f;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (damageSource instanceof EntityDamageSource && ((EntityDamageSource)damageSource).getIsThornsDamage()) {
            this.attackDragonFrom(damageSource, f);
        }
        return false;
    }

    private boolean destroyBlocksInAABB(AxisAlignedBB axisAlignedBB) {
        int n = MathHelper.floor_double(axisAlignedBB.minX);
        int n2 = MathHelper.floor_double(axisAlignedBB.minY);
        int n3 = MathHelper.floor_double(axisAlignedBB.minZ);
        int n4 = MathHelper.floor_double(axisAlignedBB.maxX);
        int n5 = MathHelper.floor_double(axisAlignedBB.maxY);
        int n6 = MathHelper.floor_double(axisAlignedBB.maxZ);
        boolean bl = false;
        boolean bl2 = false;
        int n7 = n;
        while (n7 <= n4) {
            int n8 = n2;
            while (n8 <= n5) {
                int n9 = n3;
                while (n9 <= n6) {
                    BlockPos blockPos = new BlockPos(n7, n8, n9);
                    Block block = this.worldObj.getBlockState(blockPos).getBlock();
                    if (block.getMaterial() != Material.air) {
                        if (block != Blocks.barrier && block != Blocks.obsidian && block != Blocks.end_stone && block != Blocks.bedrock && block != Blocks.command_block && this.worldObj.getGameRules().getBoolean("mobGriefing")) {
                            bl2 = this.worldObj.setBlockToAir(blockPos) || bl2;
                        } else {
                            bl = true;
                        }
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
        if (bl2) {
            double d = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) * (double)this.rand.nextFloat();
            double d2 = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) * (double)this.rand.nextFloat();
            double d3 = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) * (double)this.rand.nextFloat();
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d, d2, d3, 0.0, 0.0, 0.0, new int[0]);
        }
        return bl;
    }

    @Override
    public void onKillCommand() {
        this.setDead();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    protected boolean attackDragonFrom(DamageSource damageSource, float f) {
        return super.attackEntityFrom(damageSource, f);
    }

    private float simplifyAngle(double d) {
        return (float)MathHelper.wrapAngleTo180_double(d);
    }

    @Override
    public void onLivingUpdate() {
        float f;
        float f2;
        if (this.worldObj.isRemote) {
            f2 = MathHelper.cos(this.animTime * (float)Math.PI * 2.0f);
            f = MathHelper.cos(this.prevAnimTime * (float)Math.PI * 2.0f);
            if (f <= -0.3f && f2 >= -0.3f && !this.isSilent()) {
                this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.enderdragon.wings", 5.0f, 0.8f + this.rand.nextFloat() * 0.3f, false);
            }
        }
        this.prevAnimTime = this.animTime;
        if (this.getHealth() <= 0.0f) {
            f2 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            f = (this.rand.nextFloat() - 0.5f) * 4.0f;
            float f3 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + (double)f2, this.posY + 2.0 + (double)f, this.posZ + (double)f3, 0.0, 0.0, 0.0, new int[0]);
        } else {
            this.updateDragonEnderCrystal();
            f2 = 0.2f / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0f + 1.0f);
            this.animTime = this.slowed ? (this.animTime += f2 * 0.5f) : (this.animTime += (f2 *= (float)Math.pow(2.0, this.motionY)));
            this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
            if (this.isAIDisabled()) {
                this.animTime = 0.5f;
            } else {
                float f4;
                float f5;
                double d;
                double d2;
                double d3;
                if (this.ringBufferIndex < 0) {
                    int n = 0;
                    while (n < this.ringBuffer.length) {
                        this.ringBuffer[n][0] = this.rotationYaw;
                        this.ringBuffer[n][1] = this.posY;
                        ++n;
                    }
                }
                if (++this.ringBufferIndex == this.ringBuffer.length) {
                    this.ringBufferIndex = 0;
                }
                this.ringBuffer[this.ringBufferIndex][0] = this.rotationYaw;
                this.ringBuffer[this.ringBufferIndex][1] = this.posY;
                if (this.worldObj.isRemote) {
                    if (this.newPosRotationIncrements > 0) {
                        double d4 = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
                        d3 = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
                        d2 = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;
                        d = MathHelper.wrapAngleTo180_double(this.newRotationYaw - (double)this.rotationYaw);
                        this.rotationYaw = (float)((double)this.rotationYaw + d / (double)this.newPosRotationIncrements);
                        this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
                        --this.newPosRotationIncrements;
                        this.setPosition(d4, d3, d2);
                        this.setRotation(this.rotationYaw, this.rotationPitch);
                    }
                } else {
                    double d5;
                    double d6 = this.targetX - this.posX;
                    d3 = this.targetY - this.posY;
                    d2 = this.targetZ - this.posZ;
                    d = d6 * d6 + d3 * d3 + d2 * d2;
                    if (this.target != null) {
                        this.targetX = this.target.posX;
                        this.targetZ = this.target.posZ;
                        double d7 = this.targetX - this.posX;
                        double d8 = this.targetZ - this.posZ;
                        double d9 = Math.sqrt(d7 * d7 + d8 * d8);
                        d5 = (double)0.4f + d9 / 80.0 - 1.0;
                        if (d5 > 10.0) {
                            d5 = 10.0;
                        }
                        this.targetY = this.target.getEntityBoundingBox().minY + d5;
                    } else {
                        this.targetX += this.rand.nextGaussian() * 2.0;
                        this.targetZ += this.rand.nextGaussian() * 2.0;
                    }
                    if (this.forceNewTarget || d < 100.0 || d > 22500.0 || this.isCollidedHorizontally || this.isCollidedVertically) {
                        this.setNewTarget();
                    }
                    d3 /= (double)MathHelper.sqrt_double(d6 * d6 + d2 * d2);
                    f5 = 0.6f;
                    d3 = MathHelper.clamp_double(d3, -f5, f5);
                    this.motionY += d3 * (double)0.1f;
                    this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
                    double d10 = 180.0 - MathHelper.func_181159_b(d6, d2) * 180.0 / Math.PI;
                    double d11 = MathHelper.wrapAngleTo180_double(d10 - (double)this.rotationYaw);
                    if (d11 > 50.0) {
                        d11 = 50.0;
                    }
                    if (d11 < -50.0) {
                        d11 = -50.0;
                    }
                    Vec3 vec3 = new Vec3(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ).normalize();
                    d5 = -MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f);
                    Vec3 vec32 = new Vec3(MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0f), this.motionY, d5).normalize();
                    f4 = ((float)vec32.dotProduct(vec3) + 0.5f) / 1.5f;
                    if (f4 < 0.0f) {
                        f4 = 0.0f;
                    }
                    this.randomYawVelocity *= 0.8f;
                    float f6 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0f + 1.0f;
                    double d12 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0 + 1.0;
                    if (d12 > 40.0) {
                        d12 = 40.0;
                    }
                    this.randomYawVelocity = (float)((double)this.randomYawVelocity + d11 * ((double)0.7f / d12 / (double)f6));
                    this.rotationYaw += this.randomYawVelocity * 0.1f;
                    float f7 = (float)(2.0 / (d12 + 1.0));
                    float f8 = 0.06f;
                    this.moveFlying(0.0f, -1.0f, f8 * (f4 * f7 + (1.0f - f7)));
                    if (this.slowed) {
                        this.moveEntity(this.motionX * (double)0.8f, this.motionY * (double)0.8f, this.motionZ * (double)0.8f);
                    } else {
                        this.moveEntity(this.motionX, this.motionY, this.motionZ);
                    }
                    Vec3 vec33 = new Vec3(this.motionX, this.motionY, this.motionZ).normalize();
                    float f9 = ((float)vec33.dotProduct(vec32) + 1.0f) / 2.0f;
                    f9 = 0.8f + 0.15f * f9;
                    this.motionX *= (double)f9;
                    this.motionZ *= (double)f9;
                    this.motionY *= (double)0.91f;
                }
                this.renderYawOffset = this.rotationYaw;
                this.dragonPartHead.height = 3.0f;
                this.dragonPartHead.width = 3.0f;
                this.dragonPartTail1.height = 2.0f;
                this.dragonPartTail1.width = 2.0f;
                this.dragonPartTail2.height = 2.0f;
                this.dragonPartTail2.width = 2.0f;
                this.dragonPartTail3.height = 2.0f;
                this.dragonPartTail3.width = 2.0f;
                this.dragonPartBody.height = 3.0f;
                this.dragonPartBody.width = 5.0f;
                this.dragonPartWing1.height = 2.0f;
                this.dragonPartWing1.width = 4.0f;
                this.dragonPartWing2.height = 3.0f;
                this.dragonPartWing2.width = 4.0f;
                f = (float)(this.getMovementOffsets(5, 1.0f)[1] - this.getMovementOffsets(10, 1.0f)[1]) * 10.0f / 180.0f * (float)Math.PI;
                float f10 = MathHelper.cos(f);
                float f11 = -MathHelper.sin(f);
                float f12 = this.rotationYaw * (float)Math.PI / 180.0f;
                float f13 = MathHelper.sin(f12);
                float f14 = MathHelper.cos(f12);
                this.dragonPartBody.onUpdate();
                this.dragonPartBody.setLocationAndAngles(this.posX + (double)(f13 * 0.5f), this.posY, this.posZ - (double)(f14 * 0.5f), 0.0f, 0.0f);
                this.dragonPartWing1.onUpdate();
                this.dragonPartWing1.setLocationAndAngles(this.posX + (double)(f14 * 4.5f), this.posY + 2.0, this.posZ + (double)(f13 * 4.5f), 0.0f, 0.0f);
                this.dragonPartWing2.onUpdate();
                this.dragonPartWing2.setLocationAndAngles(this.posX - (double)(f14 * 4.5f), this.posY + 2.0, this.posZ - (double)(f13 * 4.5f), 0.0f, 0.0f);
                if (!this.worldObj.isRemote && this.hurtTime == 0) {
                    this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.getEntityBoundingBox().expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0)));
                    this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.getEntityBoundingBox().expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0)));
                    this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getEntityBoundingBox().expand(1.0, 1.0, 1.0)));
                }
                double[] dArray = this.getMovementOffsets(5, 1.0f);
                double[] dArray2 = this.getMovementOffsets(0, 1.0f);
                f5 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0f - this.randomYawVelocity * 0.01f);
                float f15 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f - this.randomYawVelocity * 0.01f);
                this.dragonPartHead.onUpdate();
                this.dragonPartHead.setLocationAndAngles(this.posX + (double)(f5 * 5.5f * f10), this.posY + (dArray2[1] - dArray[1]) * 1.0 + (double)(f11 * 5.5f), this.posZ - (double)(f15 * 5.5f * f10), 0.0f, 0.0f);
                int n = 0;
                while (n < 3) {
                    EntityDragonPart entityDragonPart = null;
                    if (n == 0) {
                        entityDragonPart = this.dragonPartTail1;
                    }
                    if (n == 1) {
                        entityDragonPart = this.dragonPartTail2;
                    }
                    if (n == 2) {
                        entityDragonPart = this.dragonPartTail3;
                    }
                    double[] dArray3 = this.getMovementOffsets(12 + n * 2, 1.0f);
                    float f16 = this.rotationYaw * (float)Math.PI / 180.0f + this.simplifyAngle(dArray3[0] - dArray[0]) * (float)Math.PI / 180.0f * 1.0f;
                    float f17 = MathHelper.sin(f16);
                    float f18 = MathHelper.cos(f16);
                    float f19 = 1.5f;
                    f4 = (float)(n + 1) * 2.0f;
                    entityDragonPart.onUpdate();
                    entityDragonPart.setLocationAndAngles(this.posX - (double)((f13 * f19 + f17 * f4) * f10), this.posY + (dArray3[1] - dArray[1]) * 1.0 - (double)((f4 + f19) * f11) + 1.5, this.posZ + (double)((f14 * f19 + f18 * f4) * f10), 0.0f, 0.0f);
                    ++n;
                }
                if (!this.worldObj.isRemote) {
                    this.slowed = this.destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox());
                }
            }
        }
    }

    @Override
    public World getWorld() {
        return this.worldObj;
    }

    @Override
    protected void onDeathUpdate() {
        ++this.deathTicks;
        if (this.deathTicks >= 180 && this.deathTicks <= 200) {
            float f = (this.rand.nextFloat() - 0.5f) * 8.0f;
            float f2 = (this.rand.nextFloat() - 0.5f) * 4.0f;
            float f3 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + (double)f, this.posY + 2.0 + (double)f2, this.posZ + (double)f3, 0.0, 0.0, 0.0, new int[0]);
        }
        boolean bl = this.worldObj.getGameRules().getBoolean("doMobLoot");
        if (!this.worldObj.isRemote) {
            if (this.deathTicks > 150 && this.deathTicks % 5 == 0 && bl) {
                int n = 1000;
                while (n > 0) {
                    int n2 = EntityXPOrb.getXPSplit(n);
                    n -= n2;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, n2));
                }
            }
            if (this.deathTicks == 1) {
                this.worldObj.playBroadcastSound(1018, new BlockPos(this), 0);
            }
        }
        this.moveEntity(0.0, 0.1f, 0.0);
        this.renderYawOffset = this.rotationYaw += 20.0f;
        if (this.deathTicks == 200 && !this.worldObj.isRemote) {
            if (bl) {
                int n = 2000;
                while (n > 0) {
                    int n3 = EntityXPOrb.getXPSplit(n);
                    n -= n3;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, n3));
                }
            }
            this.generatePortal(new BlockPos(this.posX, 64.0, this.posZ));
            this.setDead();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0);
    }

    public double[] getMovementOffsets(int n, float f) {
        if (this.getHealth() <= 0.0f) {
            f = 0.0f;
        }
        f = 1.0f - f;
        int n2 = this.ringBufferIndex - n * 1 & 0x3F;
        int n3 = this.ringBufferIndex - n * 1 - 1 & 0x3F;
        double[] dArray = new double[3];
        double d = this.ringBuffer[n2][0];
        double d2 = MathHelper.wrapAngleTo180_double(this.ringBuffer[n3][0] - d);
        dArray[0] = d + d2 * (double)f;
        d = this.ringBuffer[n2][1];
        d2 = this.ringBuffer[n3][1] - d;
        dArray[1] = d + d2 * (double)f;
        dArray[2] = this.ringBuffer[n2][2] + (this.ringBuffer[n3][2] - this.ringBuffer[n2][2]) * (double)f;
        return dArray;
    }

    @Override
    protected String getLivingSound() {
        return "mob.enderdragon.growl";
    }

    @Override
    protected void despawnEntity() {
    }

    @Override
    protected String getHurtSound() {
        return "mob.enderdragon.hit";
    }

    private void updateDragonEnderCrystal() {
        if (this.healingEnderCrystal != null) {
            if (this.healingEnderCrystal.isDead) {
                if (!this.worldObj.isRemote) {
                    this.attackEntityFromPart(this.dragonPartHead, DamageSource.setExplosionSource(null), 10.0f);
                }
                this.healingEnderCrystal = null;
            } else if (this.ticksExisted % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
                this.setHealth(this.getHealth() + 1.0f);
            }
        }
        if (this.rand.nextInt(10) == 0) {
            float f = 32.0f;
            List<EntityEnderCrystal> list = this.worldObj.getEntitiesWithinAABB(EntityEnderCrystal.class, this.getEntityBoundingBox().expand(f, f, f));
            EntityEnderCrystal entityEnderCrystal = null;
            double d = Double.MAX_VALUE;
            for (EntityEnderCrystal entityEnderCrystal2 : list) {
                double d2 = entityEnderCrystal2.getDistanceSqToEntity(this);
                if (!(d2 < d)) continue;
                d = d2;
                entityEnderCrystal = entityEnderCrystal2;
            }
            this.healingEnderCrystal = entityEnderCrystal;
        }
    }

    @Override
    public boolean attackEntityFromPart(EntityDragonPart entityDragonPart, DamageSource damageSource, float f) {
        if (entityDragonPart != this.dragonPartHead) {
            f = f / 4.0f + 1.0f;
        }
        float f2 = this.rotationYaw * (float)Math.PI / 180.0f;
        float f3 = MathHelper.sin(f2);
        float f4 = MathHelper.cos(f2);
        this.targetX = this.posX + (double)(f3 * 5.0f) + (double)((this.rand.nextFloat() - 0.5f) * 2.0f);
        this.targetY = this.posY + (double)(this.rand.nextFloat() * 3.0f) + 1.0;
        this.targetZ = this.posZ - (double)(f4 * 5.0f) + (double)((this.rand.nextFloat() - 0.5f) * 2.0f);
        this.target = null;
        if (damageSource.getEntity() instanceof EntityPlayer || damageSource.isExplosion()) {
            this.attackDragonFrom(damageSource, f);
        }
        return true;
    }

    private void setNewTarget() {
        this.forceNewTarget = false;
        ArrayList arrayList = Lists.newArrayList(this.worldObj.playerEntities);
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            if (!((EntityPlayer)iterator.next()).isSpectator()) continue;
            iterator.remove();
        }
        if (this.rand.nextInt(2) == 0 && !arrayList.isEmpty()) {
            this.target = (Entity)arrayList.get(this.rand.nextInt(arrayList.size()));
        } else {
            double d;
            double d2;
            double d3;
            boolean bl;
            do {
                this.targetX = 0.0;
                this.targetY = 70.0f + this.rand.nextFloat() * 50.0f;
                this.targetZ = 0.0;
                this.targetX += (double)(this.rand.nextFloat() * 120.0f - 60.0f);
                this.targetZ += (double)(this.rand.nextFloat() * 120.0f - 60.0f);
            } while (!(bl = (d3 = this.posX - this.targetX) * d3 + (d2 = this.posY - this.targetY) * d2 + (d = this.posZ - this.targetZ) * d > 100.0));
            this.target = null;
        }
    }
}

