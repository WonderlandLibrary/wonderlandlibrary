/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySnowman
extends EntityGolem
implements IRangedAttackMob {
    private static final DataParameter<Byte> PUMPKIN_EQUIPPED = EntityDataManager.createKey(EntitySnowman.class, DataSerializers.BYTE);

    public EntitySnowman(World worldIn) {
        super(worldIn);
        this.setSize(0.7f, 1.9f);
    }

    public static void registerFixesSnowman(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntitySnowman.class);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAIAttackRanged(this, 1.25, 20, 10.0f));
        this.tasks.addTask(2, new EntityAIWanderAvoidWater((EntityCreature)this, 1.0, 1.0000001E-5f));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<Entity>(this, EntityLiving.class, 10, true, false, IMob.MOB_SELECTOR));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2f);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(PUMPKIN_EQUIPPED, (byte)16);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Pumpkin", this.isPumpkinEquipped());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Pumpkin")) {
            this.setPumpkinEquipped(compound.getBoolean("Pumpkin"));
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.world.isRemote) {
            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.posY);
            int k = MathHelper.floor(this.posZ);
            if (this.isWet()) {
                this.attackEntityFrom(DamageSource.drown, 1.0f);
            }
            BlockPos blockPos = new BlockPos(i, 0, k);
            BlockPos blockPos2 = new BlockPos(i, j, k);
            if (this.world.getBiome(blockPos).getFloatTemperature(blockPos2) > 1.0f) {
                this.attackEntityFrom(DamageSource.onFire, 1.0f);
            }
            if (!this.world.getGameRules().getBoolean("mobGriefing")) {
                return;
            }
            for (int l = 0; l < 4; ++l) {
                i = MathHelper.floor(this.posX + (double)((float)(l % 2 * 2 - 1) * 0.25f));
                BlockPos blockpos = new BlockPos(i, j = MathHelper.floor(this.posY), k = MathHelper.floor(this.posZ + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25f)));
                if (this.world.getBlockState(blockpos).getMaterial() != Material.AIR || !(this.world.getBiome(blockpos).getFloatTemperature(blockpos) < 0.8f) || !Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, blockpos)) continue;
                this.world.setBlockState(blockpos, Blocks.SNOW_LAYER.getDefaultState());
            }
        }
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SNOWMAN;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        EntitySnowball entitysnowball = new EntitySnowball(this.world, this);
        double d0 = target.posY + (double)target.getEyeHeight() - (double)1.1f;
        double d1 = target.posX - this.posX;
        double d2 = d0 - entitysnowball.posY;
        double d3 = target.posZ - this.posZ;
        float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2f;
        entitysnowball.setThrowableHeading(d1, d2 + (double)f, d3, 1.6f, 12.0f);
        this.playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0f, 1.0f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
        this.world.spawnEntityInWorld(entitysnowball);
    }

    @Override
    public float getEyeHeight() {
        return 1.7f;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.SHEARS && this.isPumpkinEquipped() && !this.world.isRemote) {
            this.setPumpkinEquipped(false);
            itemstack.damageItem(1, player);
        }
        return super.processInteract(player, hand);
    }

    public boolean isPumpkinEquipped() {
        return (this.dataManager.get(PUMPKIN_EQUIPPED) & 0x10) != 0;
    }

    public void setPumpkinEquipped(boolean pumpkinEquipped) {
        byte b0 = this.dataManager.get(PUMPKIN_EQUIPPED);
        if (pumpkinEquipped) {
            this.dataManager.set(PUMPKIN_EQUIPPED, (byte)(b0 | 0x10));
        } else {
            this.dataManager.set(PUMPKIN_EQUIPPED, (byte)(b0 & 0xFFFFFFEF));
        }
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SNOWMAN_AMBIENT;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_SNOWMAN_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SNOWMAN_DEATH;
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
    }
}

