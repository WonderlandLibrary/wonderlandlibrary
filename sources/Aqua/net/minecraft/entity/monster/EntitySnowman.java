package net.minecraft.entity.monster;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
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
    public EntitySnowman(World worldIn) {
        super(worldIn);
        this.setSize(0.7f, 1.9f);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(1, (EntityAIBase)new EntityAIArrowAttack((IRangedAttackMob)this, 1.25, 20, 10.0f));
        this.tasks.addTask(2, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0));
        this.tasks.addTask(3, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(4, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
        this.targetTasks.addTask(1, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityLiving.class, 10, true, false, IMob.mobSelector));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((double)0.2f);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.worldObj.isRemote) {
            int i = MathHelper.floor_double((double)this.posX);
            int j = MathHelper.floor_double((double)this.posY);
            int k = MathHelper.floor_double((double)this.posZ);
            if (this.isWet()) {
                this.attackEntityFrom(DamageSource.drown, 1.0f);
            }
            BlockPos blockPos = new BlockPos(i, 0, k);
            BlockPos blockPos2 = new BlockPos(i, j, k);
            if (this.worldObj.getBiomeGenForCoords(blockPos).getFloatTemperature(blockPos2) > 1.0f) {
                this.attackEntityFrom(DamageSource.onFire, 1.0f);
            }
            for (int l = 0; l < 4; ++l) {
                i = MathHelper.floor_double((double)(this.posX + (double)((float)(l % 2 * 2 - 1) * 0.25f)));
                BlockPos blockpos = new BlockPos(i, j = MathHelper.floor_double((double)this.posY), k = MathHelper.floor_double((double)(this.posZ + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25f))));
                if (this.worldObj.getBlockState(blockpos).getBlock().getMaterial() != Material.air) continue;
                BlockPos blockPos3 = new BlockPos(i, 0, k);
                if (!(this.worldObj.getBiomeGenForCoords(blockPos3).getFloatTemperature(blockpos) < 0.8f) || !Blocks.snow_layer.canPlaceBlockAt(this.worldObj, blockpos)) continue;
                this.worldObj.setBlockState(blockpos, Blocks.snow_layer.getDefaultState());
            }
        }
    }

    protected Item getDropItem() {
        return Items.snowball;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        int i = this.rand.nextInt(16);
        for (int j = 0; j < i; ++j) {
            this.dropItem(Items.snowball, 1);
        }
    }

    public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) {
        EntitySnowball entitysnowball = new EntitySnowball(this.worldObj, (EntityLivingBase)this);
        double d0 = target.posY + (double)target.getEyeHeight() - (double)1.1f;
        double d1 = target.posX - this.posX;
        double d2 = d0 - entitysnowball.posY;
        double d3 = target.posZ - this.posZ;
        float f = MathHelper.sqrt_double((double)(d1 * d1 + d3 * d3)) * 0.2f;
        entitysnowball.setThrowableHeading(d1, d2 + (double)f, d3, 1.6f, 12.0f);
        this.playSound("random.bow", 1.0f, 1.0f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
        this.worldObj.spawnEntityInWorld((Entity)entitysnowball);
    }

    public float getEyeHeight() {
        return 1.7f;
    }
}
