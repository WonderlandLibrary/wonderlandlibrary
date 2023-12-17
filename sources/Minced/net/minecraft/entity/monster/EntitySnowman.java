// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.SoundEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.SharedMonsterAttributes;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.IRangedAttackMob;

public class EntitySnowman extends EntityGolem implements IRangedAttackMob
{
    private static final DataParameter<Byte> PUMPKIN_EQUIPPED;
    
    public EntitySnowman(final World worldIn) {
        super(worldIn);
        this.setSize(0.7f, 1.9f);
    }
    
    public static void registerFixesSnowman(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntitySnowman.class);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAIAttackRanged(this, 1.25, 20, 10.0f));
        this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0, 1.0000001E-5f));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<Object>(this, EntityLiving.class, 10, true, false, IMob.MOB_SELECTOR));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntitySnowman.PUMPKIN_EQUIPPED, (Byte)16);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Pumpkin", this.isPumpkinEquipped());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
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
                this.attackEntityFrom(DamageSource.DROWN, 1.0f);
            }
            if (this.world.getBiome(new BlockPos(i, 0, k)).getTemperature(new BlockPos(i, j, k)) > 1.0f) {
                this.attackEntityFrom(DamageSource.ON_FIRE, 1.0f);
            }
            if (!this.world.getGameRules().getBoolean("mobGriefing")) {
                return;
            }
            for (int l = 0; l < 4; ++l) {
                i = MathHelper.floor(this.posX + (l % 2 * 2 - 1) * 0.25f);
                j = MathHelper.floor(this.posY);
                k = MathHelper.floor(this.posZ + (l / 2 % 2 * 2 - 1) * 0.25f);
                final BlockPos blockpos = new BlockPos(i, j, k);
                if (this.world.getBlockState(blockpos).getMaterial() == Material.AIR && this.world.getBiome(blockpos).getTemperature(blockpos) < 0.8f && Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, blockpos)) {
                    this.world.setBlockState(blockpos, Blocks.SNOW_LAYER.getDefaultState());
                }
            }
        }
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SNOWMAN;
    }
    
    @Override
    public void attackEntityWithRangedAttack(final EntityLivingBase target, final float distanceFactor) {
        final EntitySnowball entitysnowball = new EntitySnowball(this.world, this);
        final double d0 = target.posY + target.getEyeHeight() - 1.100000023841858;
        final double d2 = target.posX - this.posX;
        final double d3 = d0 - entitysnowball.posY;
        final double d4 = target.posZ - this.posZ;
        final float f = MathHelper.sqrt(d2 * d2 + d4 * d4) * 0.2f;
        entitysnowball.shoot(d2, d3 + f, d4, 1.6f, 12.0f);
        this.playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0f, 1.0f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
        this.world.spawnEntity(entitysnowball);
    }
    
    @Override
    public float getEyeHeight() {
        return 1.7f;
    }
    
    @Override
    protected boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.SHEARS && this.isPumpkinEquipped() && !this.world.isRemote) {
            this.setPumpkinEquipped(false);
            itemstack.damageItem(1, player);
        }
        return super.processInteract(player, hand);
    }
    
    public boolean isPumpkinEquipped() {
        return (this.dataManager.get(EntitySnowman.PUMPKIN_EQUIPPED) & 0x10) != 0x0;
    }
    
    public void setPumpkinEquipped(final boolean pumpkinEquipped) {
        final byte b0 = this.dataManager.get(EntitySnowman.PUMPKIN_EQUIPPED);
        if (pumpkinEquipped) {
            this.dataManager.set(EntitySnowman.PUMPKIN_EQUIPPED, (byte)(b0 | 0x10));
        }
        else {
            this.dataManager.set(EntitySnowman.PUMPKIN_EQUIPPED, (byte)(b0 & 0xFFFFFFEF));
        }
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SNOWMAN_AMBIENT;
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_SNOWMAN_HURT;
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SNOWMAN_DEATH;
    }
    
    @Override
    public void setSwingingArms(final boolean swingingArms) {
    }
    
    static {
        PUMPKIN_EQUIPPED = EntityDataManager.createKey(EntitySnowman.class, DataSerializers.BYTE);
    }
}
