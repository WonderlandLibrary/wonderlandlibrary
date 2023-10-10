package net.minecraft.entity.monster;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.material.Material;
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
   private int witchAttackTimer;
   private static final Item[] witchDrops;
   private static final AttributeModifier MODIFIER;
   private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");

   protected String getDeathSound() {
      return null;
   }

   public float getEyeHeight() {
      return 1.62F;
   }

   public void handleStatusUpdate(byte var1) {
      if (var1 == 15) {
         for(int var2 = 0; var2 < this.rand.nextInt(35) + 10; ++var2) {
            this.worldObj.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * 0.12999999523162842D, this.getEntityBoundingBox().maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D);
         }
      } else {
         super.handleStatusUpdate(var1);
      }

   }

   public void attackEntityWithRangedAttack(EntityLivingBase var1, float var2) {
      if (this != false) {
         EntityPotion var3 = new EntityPotion(this.worldObj, this, 32732);
         double var4 = var1.posY + (double)var1.getEyeHeight() - 1.100000023841858D;
         var3.rotationPitch -= -20.0F;
         double var6 = var1.posX + var1.motionX - this.posX;
         double var8 = var4 - this.posY;
         double var10 = var1.posZ + var1.motionZ - this.posZ;
         float var12 = MathHelper.sqrt_double(var6 * var6 + var10 * var10);
         if (var12 >= 8.0F && !var1.isPotionActive(Potion.moveSlowdown)) {
            var3.setPotionDamage(32698);
         } else if (var1.getHealth() >= 8.0F && !var1.isPotionActive(Potion.poison)) {
            var3.setPotionDamage(32660);
         } else if (var12 <= 3.0F && !var1.isPotionActive(Potion.weakness) && this.rand.nextFloat() < 0.25F) {
            var3.setPotionDamage(32696);
         }

         var3.setThrowableHeading(var6, var8 + (double)(var12 * 0.2F), var10, 0.75F, 8.0F);
         this.worldObj.spawnEntityInWorld(var3);
      }

   }

   public void onLivingUpdate() {
      if (!this.worldObj.isRemote) {
         if (this != false) {
            if (this.witchAttackTimer-- <= 0) {
               this.setAggressive(false);
               ItemStack var7 = this.getHeldItem();
               this.setCurrentItemOrArmor(0, (ItemStack)null);
               if (var7 != null && var7.getItem() == Items.potionitem) {
                  List var6 = Items.potionitem.getEffects(var7);
                  if (var6 != null) {
                     Iterator var4 = var6.iterator();

                     while(var4.hasNext()) {
                        PotionEffect var3 = (PotionEffect)var4.next();
                        this.addPotionEffect(new PotionEffect(var3));
                     }
                  }
               }

               this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(MODIFIER);
            }
         } else {
            short var1 = -1;
            if (this.rand.nextFloat() < 0.15F && this.isInsideOfMaterial(Material.water) && !this.isPotionActive(Potion.waterBreathing)) {
               var1 = 8237;
            } else if (this.rand.nextFloat() < 0.15F && this.isBurning() && !this.isPotionActive(Potion.fireResistance)) {
               var1 = 16307;
            } else if (this.rand.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
               var1 = 16341;
            } else if (this.rand.nextFloat() < 0.25F && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
               var1 = 16274;
            } else if (this.rand.nextFloat() < 0.25F && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
               var1 = 16274;
            }

            if (var1 > -1) {
               this.setCurrentItemOrArmor(0, new ItemStack(Items.potionitem, 1, var1));
               this.witchAttackTimer = this.getHeldItem().getMaxItemUseDuration();
               this.setAggressive(true);
               IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
               var2.removeModifier(MODIFIER);
               var2.applyModifier(MODIFIER);
            }
         }

         if (this.rand.nextFloat() < 7.5E-4F) {
            this.worldObj.setEntityState(this, (byte)15);
         }
      }

      super.onLivingUpdate();
   }

   protected void dropFewItems(boolean var1, int var2) {
      int var3 = this.rand.nextInt(3) + 1;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = this.rand.nextInt(3);
         Item var6 = witchDrops[this.rand.nextInt(witchDrops.length)];
         if (var2 > 0) {
            var5 += this.rand.nextInt(var2 + 1);
         }

         for(int var7 = 0; var7 < var5; ++var7) {
            this.dropItem(var6, 1);
         }
      }

   }

   protected String getLivingSound() {
      return null;
   }

   protected void entityInit() {
      super.entityInit();
      this.getDataWatcher().addObject(21, (byte)0);
   }

   static {
      MODIFIER = (new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
      witchDrops = new Item[]{Items.glowstone_dust, Items.sugar, Items.redstone, Items.spider_eye, Items.glass_bottle, Items.gunpowder, Items.stick, Items.stick};
   }

   public void setAggressive(boolean var1) {
      this.getDataWatcher().updateObject(21, (byte)(var1 ? 1 : 0));
   }

   public EntityWitch(World var1) {
      super(var1);
      this.setSize(0.6F, 1.95F);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 60, 10.0F));
      this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
      this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(3, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
   }

   protected float applyPotionDamageCalculations(DamageSource var1, float var2) {
      var2 = super.applyPotionDamageCalculations(var1, var2);
      if (var1.getEntity() == this) {
         var2 = 0.0F;
      }

      if (var1.isMagicDamage()) {
         var2 = (float)((double)var2 * 0.15D);
      }

      return var2;
   }

   protected String getHurtSound() {
      return null;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(26.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
   }
}
