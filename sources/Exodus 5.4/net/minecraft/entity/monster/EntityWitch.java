/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.monster;

import java.util.List;
import java.util.UUID;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityWitch
extends EntityMob
implements IRangedAttackMob {
    private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    private int witchAttackTimer;
    private static final Item[] witchDrops;
    private static final AttributeModifier MODIFIER;

    @Override
    protected String getLivingSound() {
        return null;
    }

    @Override
    protected float applyPotionDamageCalculations(DamageSource damageSource, float f) {
        f = super.applyPotionDamageCalculations(damageSource, f);
        if (damageSource.getEntity() == this) {
            f = 0.0f;
        }
        if (damageSource.isMagicDamage()) {
            f = (float)((double)f * 0.15);
        }
        return f;
    }

    @Override
    protected String getDeathSound() {
        return null;
    }

    public void setAggressive(boolean bl) {
        this.getDataWatcher().updateObject(21, (byte)(bl ? 1 : 0));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataWatcher().addObject(21, (byte)0);
    }

    static {
        MODIFIER = new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25, 0).setSaved(false);
        witchDrops = new Item[]{Items.glowstone_dust, Items.sugar, Items.redstone, Items.spider_eye, Items.glass_bottle, Items.gunpowder, Items.stick, Items.stick};
    }

    @Override
    public void onLivingUpdate() {
        if (!this.worldObj.isRemote) {
            if (this.getAggressive()) {
                if (this.witchAttackTimer-- <= 0) {
                    List<PotionEffect> list;
                    this.setAggressive(false);
                    ItemStack itemStack = this.getHeldItem();
                    this.setCurrentItemOrArmor(0, null);
                    if (itemStack != null && itemStack.getItem() == Items.potionitem && (list = Items.potionitem.getEffects(itemStack)) != null) {
                        for (PotionEffect potionEffect : list) {
                            this.addPotionEffect(new PotionEffect(potionEffect));
                        }
                    }
                    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(MODIFIER);
                }
            } else {
                int n = -1;
                if (this.rand.nextFloat() < 0.15f && this.isInsideOfMaterial(Material.water) && !this.isPotionActive(Potion.waterBreathing)) {
                    n = 8237;
                } else if (this.rand.nextFloat() < 0.15f && this.isBurning() && !this.isPotionActive(Potion.fireResistance)) {
                    n = 16307;
                } else if (this.rand.nextFloat() < 0.05f && this.getHealth() < this.getMaxHealth()) {
                    n = 16341;
                } else if (this.rand.nextFloat() < 0.25f && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0) {
                    n = 16274;
                } else if (this.rand.nextFloat() < 0.25f && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0) {
                    n = 16274;
                }
                if (n > -1) {
                    this.setCurrentItemOrArmor(0, new ItemStack(Items.potionitem, 1, n));
                    this.witchAttackTimer = this.getHeldItem().getMaxItemUseDuration();
                    this.setAggressive(true);
                    IAttributeInstance iAttributeInstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
                    iAttributeInstance.removeModifier(MODIFIER);
                    iAttributeInstance.applyModifier(MODIFIER);
                }
            }
            if (this.rand.nextFloat() < 7.5E-4f) {
                this.worldObj.setEntityState(this, (byte)15);
            }
        }
        super.onLivingUpdate();
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        int n2 = this.rand.nextInt(3) + 1;
        int n3 = 0;
        while (n3 < n2) {
            int n4 = this.rand.nextInt(3);
            Item item = witchDrops[this.rand.nextInt(witchDrops.length)];
            if (n > 0) {
                n4 += this.rand.nextInt(n + 1);
            }
            int n5 = 0;
            while (n5 < n4) {
                this.dropItem(item, 1);
                ++n5;
            }
            ++n3;
        }
    }

    public boolean getAggressive() {
        return this.getDataWatcher().getWatchableObjectByte(21) == 1;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entityLivingBase, float f) {
        if (!this.getAggressive()) {
            EntityPotion entityPotion = new EntityPotion(this.worldObj, (EntityLivingBase)this, 32732);
            double d = entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() - (double)1.1f;
            entityPotion.rotationPitch -= -20.0f;
            double d2 = entityLivingBase.posX + entityLivingBase.motionX - this.posX;
            double d3 = d - this.posY;
            double d4 = entityLivingBase.posZ + entityLivingBase.motionZ - this.posZ;
            float f2 = MathHelper.sqrt_double(d2 * d2 + d4 * d4);
            if (f2 >= 8.0f && !entityLivingBase.isPotionActive(Potion.moveSlowdown)) {
                entityPotion.setPotionDamage(32698);
            } else if (entityLivingBase.getHealth() >= 8.0f && !entityLivingBase.isPotionActive(Potion.poison)) {
                entityPotion.setPotionDamage(32660);
            } else if (f2 <= 3.0f && !entityLivingBase.isPotionActive(Potion.weakness) && this.rand.nextFloat() < 0.25f) {
                entityPotion.setPotionDamage(32696);
            }
            entityPotion.setThrowableHeading(d2, d3 + (double)(f2 * 0.2f), d4, 0.75f, 8.0f);
            this.worldObj.spawnEntityInWorld(entityPotion);
        }
    }

    @Override
    protected String getHurtSound() {
        return null;
    }

    public EntityWitch(World world) {
        super(world);
        this.setSize(0.6f, 1.95f);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0, 60, 10.0f));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>((EntityCreature)this, EntityPlayer.class, true));
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 15) {
            int n = 0;
            while (n < this.rand.nextInt(35) + 10) {
                this.worldObj.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * (double)0.13f, this.getEntityBoundingBox().maxY + 0.5 + this.rand.nextGaussian() * (double)0.13f, this.posZ + this.rand.nextGaussian() * (double)0.13f, 0.0, 0.0, 0.0, new int[0]);
                ++n;
            }
        } else {
            super.handleStatusUpdate(by);
        }
    }

    @Override
    public float getEyeHeight() {
        return 1.62f;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(26.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
    }
}

