// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.potion.PotionType;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.MobEffects;
import net.minecraft.block.material.Material;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;
import net.minecraft.entity.IRangedAttackMob;

public class EntityWitch extends EntityMob implements IRangedAttackMob
{
    private static final UUID MODIFIER_UUID;
    private static final AttributeModifier MODIFIER;
    private static final DataParameter<Boolean> IS_DRINKING;
    private int potionUseTimer;
    
    public EntityWitch(final World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 1.95f);
    }
    
    public static void registerFixesWitch(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityWitch.class);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0, 60, 10.0f));
        this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, (Class<?>[])new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(EntityWitch.IS_DRINKING, false);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITCH_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_WITCH_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITCH_DEATH;
    }
    
    public void setDrinkingPotion(final boolean drinkingPotion) {
        this.getDataManager().set(EntityWitch.IS_DRINKING, drinkingPotion);
    }
    
    public boolean isDrinkingPotion() {
        return this.getDataManager().get(EntityWitch.IS_DRINKING);
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
                if (this.potionUseTimer-- <= 0) {
                    this.setDrinkingPotion(false);
                    final ItemStack itemstack = this.getHeldItemMainhand();
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    if (itemstack.getItem() == Items.POTIONITEM) {
                        final List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);
                        if (list != null) {
                            for (final PotionEffect potioneffect : list) {
                                this.addPotionEffect(new PotionEffect(potioneffect));
                            }
                        }
                    }
                    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(EntityWitch.MODIFIER);
                }
            }
            else {
                PotionType potiontype = null;
                if (this.rand.nextFloat() < 0.15f && this.isInsideOfMaterial(Material.WATER) && !this.isPotionActive(MobEffects.WATER_BREATHING)) {
                    potiontype = PotionTypes.WATER_BREATHING;
                }
                else if (this.rand.nextFloat() < 0.15f && (this.isBurning() || (this.getLastDamageSource() != null && this.getLastDamageSource().isFireDamage())) && !this.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                    potiontype = PotionTypes.FIRE_RESISTANCE;
                }
                else if (this.rand.nextFloat() < 0.05f && this.getHealth() < this.getMaxHealth()) {
                    potiontype = PotionTypes.HEALING;
                }
                else if (this.rand.nextFloat() < 0.5f && this.getAttackTarget() != null && !this.isPotionActive(MobEffects.SPEED) && this.getAttackTarget().getDistanceSq(this) > 121.0) {
                    potiontype = PotionTypes.SWIFTNESS;
                }
                if (potiontype != null) {
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), potiontype));
                    this.potionUseTimer = this.getHeldItemMainhand().getMaxItemUseDuration();
                    this.setDrinkingPotion(true);
                    this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_DRINK, this.getSoundCategory(), 1.0f, 0.8f + this.rand.nextFloat() * 0.4f);
                    final IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                    iattributeinstance.removeModifier(EntityWitch.MODIFIER);
                    iattributeinstance.applyModifier(EntityWitch.MODIFIER);
                }
            }
            if (this.rand.nextFloat() < 7.5E-4f) {
                this.world.setEntityState(this, (byte)15);
            }
        }
        super.onLivingUpdate();
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 15) {
            for (int i = 0; i < this.rand.nextInt(35) + 10; ++i) {
                this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * 0.12999999523162842, this.getEntityBoundingBox().maxY + 0.5 + this.rand.nextGaussian() * 0.12999999523162842, this.posZ + this.rand.nextGaussian() * 0.12999999523162842, 0.0, 0.0, 0.0, new int[0]);
            }
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    @Override
    protected float applyPotionDamageCalculations(final DamageSource source, float damage) {
        damage = super.applyPotionDamageCalculations(source, damage);
        if (source.getTrueSource() == this) {
            damage = 0.0f;
        }
        if (source.isMagicDamage()) {
            damage *= (float)0.15;
        }
        return damage;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_WITCH;
    }
    
    @Override
    public void attackEntityWithRangedAttack(final EntityLivingBase target, final float distanceFactor) {
        if (!this.isDrinkingPotion()) {
            final double d0 = target.posY + target.getEyeHeight() - 1.100000023841858;
            final double d2 = target.posX + target.motionX - this.posX;
            final double d3 = d0 - this.posY;
            final double d4 = target.posZ + target.motionZ - this.posZ;
            final float f = MathHelper.sqrt(d2 * d2 + d4 * d4);
            PotionType potiontype = PotionTypes.HARMING;
            if (f >= 8.0f && !target.isPotionActive(MobEffects.SLOWNESS)) {
                potiontype = PotionTypes.SLOWNESS;
            }
            else if (target.getHealth() >= 8.0f && !target.isPotionActive(MobEffects.POISON)) {
                potiontype = PotionTypes.POISON;
            }
            else if (f <= 3.0f && !target.isPotionActive(MobEffects.WEAKNESS) && this.rand.nextFloat() < 0.25f) {
                potiontype = PotionTypes.WEAKNESS;
            }
            final EntityPotion entityPotion;
            final EntityPotion entitypotion = entityPotion = new EntityPotion(this.world, this, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potiontype));
            entityPotion.rotationPitch += 20.0f;
            entitypotion.shoot(d2, d3 + f * 0.2f, d4, 0.75f, 8.0f);
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0f, 0.8f + this.rand.nextFloat() * 0.4f);
            this.world.spawnEntity(entitypotion);
        }
    }
    
    @Override
    public float getEyeHeight() {
        return 1.62f;
    }
    
    @Override
    public void setSwingingArms(final boolean swingingArms) {
    }
    
    static {
        MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
        MODIFIER = new AttributeModifier(EntityWitch.MODIFIER_UUID, "Drinking speed penalty", -0.25, 0).setSaved(false);
        IS_DRINKING = EntityDataManager.createKey(EntityWitch.class, DataSerializers.BOOLEAN);
    }
}
