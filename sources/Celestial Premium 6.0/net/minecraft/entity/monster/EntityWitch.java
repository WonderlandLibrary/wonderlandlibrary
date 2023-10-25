/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityWitch
extends EntityMob
implements IRangedAttackMob {
    private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25, 0).setSaved(false);
    private static final DataParameter<Boolean> IS_AGGRESSIVE = EntityDataManager.createKey(EntityWitch.class, DataSerializers.BOOLEAN);
    private int witchAttackTimer;

    public EntityWitch(World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 1.95f);
    }

    public static void registerFixesWitch(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityWitch.class);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0, 60, 10.0f));
        this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>((EntityCreature)this, EntityPlayer.class, true));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(IS_AGGRESSIVE, false);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITCH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_WITCH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITCH_DEATH;
    }

    public void setAggressive(boolean aggressive) {
        this.getDataManager().set(IS_AGGRESSIVE, aggressive);
    }

    public boolean isDrinkingPotion() {
        return this.getDataManager().get(IS_AGGRESSIVE);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(26.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Override
    public void onLivingUpdate() {
        if (!this.world.isRemote) {
            if (this.isDrinkingPotion()) {
                if (this.witchAttackTimer-- <= 0) {
                    List<PotionEffect> list;
                    this.setAggressive(false);
                    ItemStack itemstack = this.getHeldItemMainhand();
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.field_190927_a);
                    if (itemstack.getItem() == Items.POTIONITEM && (list = PotionUtils.getEffectsFromStack(itemstack)) != null) {
                        for (PotionEffect potioneffect : list) {
                            this.addPotionEffect(new PotionEffect(potioneffect));
                        }
                    }
                    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                }
            } else {
                PotionType potiontype = null;
                if (this.rand.nextFloat() < 0.15f && this.isInsideOfMaterial(Material.WATER) && !this.isPotionActive(MobEffects.WATER_BREATHING)) {
                    potiontype = PotionTypes.WATER_BREATHING;
                } else if (this.rand.nextFloat() < 0.15f && (this.isBurning() || this.getLastDamageSource() != null && this.getLastDamageSource().isFireDamage()) && !this.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                    potiontype = PotionTypes.FIRE_RESISTANCE;
                } else if (this.rand.nextFloat() < 0.05f && this.getHealth() < this.getMaxHealth()) {
                    potiontype = PotionTypes.HEALING;
                } else if (this.rand.nextFloat() < 0.5f && this.getAttackTarget() != null && !this.isPotionActive(MobEffects.SPEED) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0) {
                    potiontype = PotionTypes.SWIFTNESS;
                }
                if (potiontype != null) {
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), potiontype));
                    this.witchAttackTimer = this.getHeldItemMainhand().getMaxItemUseDuration();
                    this.setAggressive(true);
                    this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_DRINK, this.getSoundCategory(), 1.0f, 0.8f + this.rand.nextFloat() * 0.4f);
                    IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                    iattributeinstance.removeModifier(MODIFIER);
                    iattributeinstance.applyModifier(MODIFIER);
                }
            }
            if (this.rand.nextFloat() < 7.5E-4f) {
                this.world.setEntityState(this, (byte)15);
            }
        }
        super.onLivingUpdate();
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 15) {
            for (int i = 0; i < this.rand.nextInt(35) + 10; ++i) {
                this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * (double)0.13f, this.getEntityBoundingBox().maxY + 0.5 + this.rand.nextGaussian() * (double)0.13f, this.posZ + this.rand.nextGaussian() * (double)0.13f, 0.0, 0.0, 0.0, new int[0]);
            }
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @Override
    protected float applyPotionDamageCalculations(DamageSource source, float damage) {
        damage = super.applyPotionDamageCalculations(source, damage);
        if (source.getEntity() == this) {
            damage = 0.0f;
        }
        if (source.isMagicDamage()) {
            damage = (float)((double)damage * 0.15);
        }
        return damage;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_WITCH;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        if (!this.isDrinkingPotion()) {
            double d0 = target.posY + (double)target.getEyeHeight() - (double)1.1f;
            double d1 = target.posX + target.motionX - this.posX;
            double d2 = d0 - this.posY;
            double d3 = target.posZ + target.motionZ - this.posZ;
            float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
            PotionType potiontype = PotionTypes.HARMING;
            if (f >= 8.0f && !target.isPotionActive(MobEffects.SLOWNESS)) {
                potiontype = PotionTypes.SLOWNESS;
            } else if (target.getHealth() >= 8.0f && !target.isPotionActive(MobEffects.POISON)) {
                potiontype = PotionTypes.POISON;
            } else if (f <= 3.0f && !target.isPotionActive(MobEffects.WEAKNESS) && this.rand.nextFloat() < 0.25f) {
                potiontype = PotionTypes.WEAKNESS;
            }
            EntityPotion entitypotion = new EntityPotion(this.world, this, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potiontype));
            entitypotion.rotationPitch -= -20.0f;
            entitypotion.setThrowableHeading(d1, d2 + (double)(f * 0.2f), d3, 0.75f, 8.0f);
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0f, 0.8f + this.rand.nextFloat() * 0.4f);
            this.world.spawnEntityInWorld(entitypotion);
        }
    }

    @Override
    public float getEyeHeight() {
        return 1.62f;
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
    }
}

