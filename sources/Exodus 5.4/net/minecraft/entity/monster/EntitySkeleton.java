/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.monster;

import java.util.Calendar;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;

public class EntitySkeleton
extends EntityMob
implements IRangedAttackMob {
    private EntityAIAttackOnCollide aiAttackOnCollide;
    private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0, 20, 60, 15.0f);

    @Override
    protected void dropFewItems(boolean bl, int n) {
        int n2;
        int n3;
        if (this.getSkeletonType() == 1) {
            n3 = this.rand.nextInt(3 + n) - 1;
            n2 = 0;
            while (n2 < n3) {
                this.dropItem(Items.coal, 1);
                ++n2;
            }
        } else {
            n3 = this.rand.nextInt(3 + n);
            n2 = 0;
            while (n2 < n3) {
                this.dropItem(Items.arrow, 1);
                ++n2;
            }
        }
        n3 = this.rand.nextInt(3 + n);
        n2 = 0;
        while (n2 < n3) {
            this.dropItem(Items.bone, 1);
            ++n2;
        }
    }

    @Override
    protected String getDeathSound() {
        return "mob.skeleton.death";
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(13, new Byte(0));
    }

    @Override
    public double getYOffset() {
        return this.isChild() ? 0.0 : -0.35;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultyInstance, IEntityLivingData iEntityLivingData) {
        Calendar calendar;
        iEntityLivingData = super.onInitialSpawn(difficultyInstance, iEntityLivingData);
        if (this.worldObj.provider instanceof WorldProviderHell && this.getRNG().nextInt(5) > 0) {
            this.tasks.addTask(4, this.aiAttackOnCollide);
            this.setSkeletonType(1);
            this.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
            this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0);
        } else {
            this.tasks.addTask(4, this.aiArrowAttack);
            this.setEquipmentBasedOnDifficulty(difficultyInstance);
            this.setEnchantmentBasedOnDifficulty(difficultyInstance);
        }
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55f * difficultyInstance.getClampedAdditionalDifficulty());
        if (this.getEquipmentInSlot(4) == null && (calendar = this.worldObj.getCurrentDate()).get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25f) {
            this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1f ? Blocks.lit_pumpkin : Blocks.pumpkin));
            this.equipmentDropChances[4] = 0.0f;
        }
        return iEntityLivingData;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (super.attackEntityAsMob(entity)) {
            if (this.getSkeletonType() == 1 && entity instanceof EntityLivingBase) {
                ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.wither.id, 200));
            }
            return true;
        }
        return false;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entityLivingBase, float f) {
        EntityArrow entityArrow = new EntityArrow(this.worldObj, this, entityLivingBase, 1.6f, 14 - this.worldObj.getDifficulty().getDifficultyId() * 4);
        int n = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
        int n2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
        entityArrow.setDamage((double)(f * 2.0f) + this.rand.nextGaussian() * 0.25 + (double)((float)this.worldObj.getDifficulty().getDifficultyId() * 0.11f));
        if (n > 0) {
            entityArrow.setDamage(entityArrow.getDamage() + (double)n * 0.5 + 0.5);
        }
        if (n2 > 0) {
            entityArrow.setKnockbackStrength(n2);
        }
        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 || this.getSkeletonType() == 1) {
            entityArrow.setFire(100);
        }
        this.playSound("random.bow", 1.0f, 1.0f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
        this.worldObj.spawnEntityInWorld(entityArrow);
    }

    @Override
    public void updateRidden() {
        super.updateRidden();
        if (this.ridingEntity instanceof EntityCreature) {
            EntityCreature entityCreature = (EntityCreature)this.ridingEntity;
            this.renderYawOffset = entityCreature.renderYawOffset;
        }
    }

    @Override
    protected String getLivingSound() {
        return "mob.skeleton.say";
    }

    @Override
    protected String getHurtSound() {
        return "mob.skeleton.hurt";
    }

    @Override
    public void setCurrentItemOrArmor(int n, ItemStack itemStack) {
        super.setCurrentItemOrArmor(n, itemStack);
        if (!this.worldObj.isRemote && n == 0) {
            this.setCombatTask();
        }
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    protected void addRandomDrop() {
        if (this.getSkeletonType() == 1) {
            this.entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0f);
        }
    }

    @Override
    public float getEyeHeight() {
        return this.getSkeletonType() == 1 ? super.getEyeHeight() : 1.74f;
    }

    public int getSkeletonType() {
        return this.dataWatcher.getWatchableObjectByte(13);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
    }

    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isDaytime() && !this.worldObj.isRemote) {
            float f = this.getBrightness(1.0f);
            BlockPos blockPos = new BlockPos(this.posX, Math.round(this.posY), this.posZ);
            if (f > 0.5f && this.rand.nextFloat() * 30.0f < (f - 0.4f) * 2.0f && this.worldObj.canSeeSky(blockPos)) {
                boolean bl = true;
                ItemStack itemStack = this.getEquipmentInSlot(4);
                if (itemStack != null) {
                    if (itemStack.isItemStackDamageable()) {
                        itemStack.setItemDamage(itemStack.getItemDamage() + this.rand.nextInt(2));
                        if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) {
                            this.renderBrokenItemStack(itemStack);
                            this.setCurrentItemOrArmor(4, null);
                        }
                    }
                    bl = false;
                }
                if (bl) {
                    this.setFire(8);
                }
            }
        }
        if (this.worldObj.isRemote && this.getSkeletonType() == 1) {
            this.setSize(0.72f, 2.535f);
        }
        super.onLivingUpdate();
    }

    @Override
    protected void playStepSound(BlockPos blockPos, Block block) {
        this.playSound("mob.skeleton.step", 0.15f, 1.0f);
    }

    public EntitySkeleton(World world) {
        super(world);
        this.aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2, false);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0));
        this.tasks.addTask(3, new EntityAIAvoidEntity<EntityWolf>(this, EntityWolf.class, 6.0f, 1.0, 1.2));
        this.tasks.addTask(4, new EntityAIWander(this, 1.0));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>((EntityCreature)this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityIronGolem>((EntityCreature)this, EntityIronGolem.class, true));
        if (world != null && !world.isRemote) {
            this.setCombatTask();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setByte("SkeletonType", (byte)this.getSkeletonType());
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultyInstance) {
        super.setEquipmentBasedOnDifficulty(difficultyInstance);
        this.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
    }

    public void setSkeletonType(int n) {
        this.dataWatcher.updateObject(13, (byte)n);
        boolean bl = this.isImmuneToFire = n == 1;
        if (n == 1) {
            this.setSize(0.72f, 2.535f);
        } else {
            this.setSize(0.6f, 1.95f);
        }
    }

    public void setCombatTask() {
        this.tasks.removeTask(this.aiAttackOnCollide);
        this.tasks.removeTask(this.aiArrowAttack);
        ItemStack itemStack = this.getHeldItem();
        if (itemStack != null && itemStack.getItem() == Items.bow) {
            this.tasks.addTask(4, this.aiArrowAttack);
        } else {
            this.tasks.addTask(4, this.aiAttackOnCollide);
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (damageSource.getSourceOfDamage() instanceof EntityArrow && damageSource.getEntity() instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer)damageSource.getEntity();
            double d = entityPlayer.posX - this.posX;
            double d2 = entityPlayer.posZ - this.posZ;
            if (d * d + d2 * d2 >= 2500.0) {
                entityPlayer.triggerAchievement(AchievementList.snipeSkeleton);
            }
        } else if (damageSource.getEntity() instanceof EntityCreeper && ((EntityCreeper)damageSource.getEntity()).getPowered() && ((EntityCreeper)damageSource.getEntity()).isAIEnabled()) {
            ((EntityCreeper)damageSource.getEntity()).func_175493_co();
            this.entityDropItem(new ItemStack(Items.skull, 1, this.getSkeletonType() == 1 ? 1 : 0), 0.0f);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        if (nBTTagCompound.hasKey("SkeletonType", 99)) {
            byte by = nBTTagCompound.getByte("SkeletonType");
            this.setSkeletonType(by);
        }
        this.setCombatTask();
    }

    @Override
    protected Item getDropItem() {
        return Items.arrow;
    }
}

