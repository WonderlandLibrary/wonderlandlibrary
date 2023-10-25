/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.monster;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySnowman
extends EntityGolem
implements IRangedAttackMob {
    @Override
    protected void dropFewItems(boolean bl, int n) {
        int n2 = this.rand.nextInt(16);
        int n3 = 0;
        while (n3 < n2) {
            this.dropItem(Items.snowball, 1);
            ++n3;
        }
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entityLivingBase, float f) {
        EntitySnowball entitySnowball = new EntitySnowball(this.worldObj, this);
        double d = entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() - (double)1.1f;
        double d2 = entityLivingBase.posX - this.posX;
        double d3 = d - entitySnowball.posY;
        double d4 = entityLivingBase.posZ - this.posZ;
        float f2 = MathHelper.sqrt_double(d2 * d2 + d4 * d4) * 0.2f;
        entitySnowball.setThrowableHeading(d2, d3 + (double)f2, d4, 1.6f, 12.0f);
        this.playSound("random.bow", 1.0f, 1.0f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
        this.worldObj.spawnEntityInWorld(entitySnowball);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.worldObj.isRemote) {
            int n = MathHelper.floor_double(this.posX);
            int n2 = MathHelper.floor_double(this.posY);
            int n3 = MathHelper.floor_double(this.posZ);
            if (this.isWet()) {
                this.attackEntityFrom(DamageSource.drown, 1.0f);
            }
            BlockPos blockPos = new BlockPos(n, 0, n3);
            BlockPos blockPos2 = new BlockPos(n, n2, n3);
            if (this.worldObj.getBiomeGenForCoords(blockPos).getFloatTemperature(blockPos2) > 1.0f) {
                this.attackEntityFrom(DamageSource.onFire, 1.0f);
            }
            int n4 = 0;
            while (n4 < 4) {
                n = MathHelper.floor_double(this.posX + (double)((float)(n4 % 2 * 2 - 1) * 0.25f));
                BlockPos blockPos3 = new BlockPos(n, n2 = MathHelper.floor_double(this.posY), n3 = MathHelper.floor_double(this.posZ + (double)((float)(n4 / 2 % 2 * 2 - 1) * 0.25f)));
                if (this.worldObj.getBlockState(blockPos3).getBlock().getMaterial() == Material.air) {
                    BlockPos blockPos4 = new BlockPos(n, 0, n3);
                    if (this.worldObj.getBiomeGenForCoords(blockPos4).getFloatTemperature(blockPos3) < 0.8f && Blocks.snow_layer.canPlaceBlockAt(this.worldObj, blockPos3)) {
                        this.worldObj.setBlockState(blockPos3, Blocks.snow_layer.getDefaultState());
                    }
                }
                ++n4;
            }
        }
    }

    public EntitySnowman(World world) {
        super(world);
        this.setSize(0.7f, 1.9f);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAIArrowAttack(this, 1.25, 20, 10.0f));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<Entity>(this, EntityLiving.class, 10, true, false, IMob.mobSelector));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2f);
    }

    @Override
    protected Item getDropItem() {
        return Items.snowball;
    }

    @Override
    public float getEyeHeight() {
        return 1.7f;
    }
}

