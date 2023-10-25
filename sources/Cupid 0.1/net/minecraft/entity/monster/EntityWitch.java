package net.minecraft.entity.monster;

import java.util.List;
import java.util.UUID;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
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

public class EntityWitch extends EntityMob implements IRangedAttackMob {
  private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
  
  private static final AttributeModifier MODIFIER = (new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
  
  private static final Item[] witchDrops = new Item[] { Items.glowstone_dust, Items.sugar, Items.redstone, Items.spider_eye, Items.glass_bottle, Items.gunpowder, Items.stick, Items.stick };
  
  private int witchAttackTimer;
  
  public EntityWitch(World worldIn) {
    super(worldIn);
    setSize(0.6F, 1.95F);
    this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIArrowAttack(this, 1.0D, 60, 10.0F));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIWander(this, 1.0D));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(3, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
  }
  
  protected void entityInit() {
    super.entityInit();
    getDataWatcher().addObject(21, Byte.valueOf((byte)0));
  }
  
  protected String getLivingSound() {
    return null;
  }
  
  protected String getHurtSound() {
    return null;
  }
  
  protected String getDeathSound() {
    return null;
  }
  
  public void setAggressive(boolean aggressive) {
    getDataWatcher().updateObject(21, Byte.valueOf((byte)(aggressive ? 1 : 0)));
  }
  
  public boolean getAggressive() {
    return (getDataWatcher().getWatchableObjectByte(21) == 1);
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(26.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
  }
  
  public void onLivingUpdate() {
    if (!this.worldObj.isRemote) {
      if (getAggressive()) {
        if (this.witchAttackTimer-- <= 0) {
          setAggressive(false);
          ItemStack itemstack = getHeldItem();
          setCurrentItemOrArmor(0, (ItemStack)null);
          if (itemstack != null && itemstack.getItem() == Items.potionitem) {
            List<PotionEffect> list = Items.potionitem.getEffects(itemstack);
            if (list != null)
              for (PotionEffect potioneffect : list)
                addPotionEffect(new PotionEffect(potioneffect));  
          } 
          getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(MODIFIER);
        } 
      } else {
        int i = -1;
        if (this.rand.nextFloat() < 0.15F && isInsideOfMaterial(Material.water) && !isPotionActive(Potion.waterBreathing)) {
          i = 8237;
        } else if (this.rand.nextFloat() < 0.15F && isBurning() && !isPotionActive(Potion.fireResistance)) {
          i = 16307;
        } else if (this.rand.nextFloat() < 0.05F && getHealth() < getMaxHealth()) {
          i = 16341;
        } else if (this.rand.nextFloat() < 0.25F && getAttackTarget() != null && !isPotionActive(Potion.moveSpeed) && getAttackTarget().getDistanceSqToEntity((Entity)this) > 121.0D) {
          i = 16274;
        } else if (this.rand.nextFloat() < 0.25F && getAttackTarget() != null && !isPotionActive(Potion.moveSpeed) && getAttackTarget().getDistanceSqToEntity((Entity)this) > 121.0D) {
          i = 16274;
        } 
        if (i > -1) {
          setCurrentItemOrArmor(0, new ItemStack((Item)Items.potionitem, 1, i));
          this.witchAttackTimer = getHeldItem().getMaxItemUseDuration();
          setAggressive(true);
          IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
          iattributeinstance.removeModifier(MODIFIER);
          iattributeinstance.applyModifier(MODIFIER);
        } 
      } 
      if (this.rand.nextFloat() < 7.5E-4F)
        this.worldObj.setEntityState((Entity)this, (byte)15); 
    } 
    super.onLivingUpdate();
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 15) {
      for (int i = 0; i < this.rand.nextInt(35) + 10; i++)
        this.worldObj.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * 0.12999999523162842D, (getEntityBoundingBox()).maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]); 
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  protected float applyPotionDamageCalculations(DamageSource source, float damage) {
    damage = super.applyPotionDamageCalculations(source, damage);
    if (source.getEntity() == this)
      damage = 0.0F; 
    if (source.isMagicDamage())
      damage = (float)(damage * 0.15D); 
    return damage;
  }
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
    int i = this.rand.nextInt(3) + 1;
    for (int j = 0; j < i; j++) {
      int k = this.rand.nextInt(3);
      Item item = witchDrops[this.rand.nextInt(witchDrops.length)];
      if (p_70628_2_ > 0)
        k += this.rand.nextInt(p_70628_2_ + 1); 
      for (int l = 0; l < k; l++)
        dropItem(item, 1); 
    } 
  }
  
  public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
    if (!getAggressive()) {
      EntityPotion entitypotion = new EntityPotion(this.worldObj, (EntityLivingBase)this, 32732);
      double d0 = p_82196_1_.posY + p_82196_1_.getEyeHeight() - 1.100000023841858D;
      entitypotion.rotationPitch -= -20.0F;
      double d1 = p_82196_1_.posX + p_82196_1_.motionX - this.posX;
      double d2 = d0 - this.posY;
      double d3 = p_82196_1_.posZ + p_82196_1_.motionZ - this.posZ;
      float f = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
      if (f >= 8.0F && !p_82196_1_.isPotionActive(Potion.moveSlowdown)) {
        entitypotion.setPotionDamage(32698);
      } else if (p_82196_1_.getHealth() >= 8.0F && !p_82196_1_.isPotionActive(Potion.poison)) {
        entitypotion.setPotionDamage(32660);
      } else if (f <= 3.0F && !p_82196_1_.isPotionActive(Potion.weakness) && this.rand.nextFloat() < 0.25F) {
        entitypotion.setPotionDamage(32696);
      } 
      entitypotion.setThrowableHeading(d1, d2 + (f * 0.2F), d3, 0.75F, 8.0F);
      this.worldObj.spawnEntityInWorld((Entity)entitypotion);
    } 
  }
  
  public float getEyeHeight() {
    return 1.62F;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\monster\EntityWitch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */