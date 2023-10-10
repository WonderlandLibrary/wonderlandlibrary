package net.minecraft.entity.passive;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntitySheep extends EntityAnimal {
   private final InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container(this) {
      final EntitySheep this$0;

      public boolean canInteractWith(EntityPlayer var1) {
         return false;
      }

      {
         this.this$0 = var1;
      }
   }, 2, 1);
   private static final Map DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);
   private EntityAIEatGrass entityAIEatGrass = new EntityAIEatGrass(this);
   private int sheepTimer;

   protected void dropFewItems(boolean var1, int var2) {
      if (this != false) {
         this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.getFleeceColor().getMetadata()), 0.0F);
      }

      int var3 = this.rand.nextInt(2) + 1 + this.rand.nextInt(1 + var2);

      for(int var4 = 0; var4 < var3; ++var4) {
         if (this.isBurning()) {
            this.dropItem(Items.cooked_mutton, 1);
         } else {
            this.dropItem(Items.mutton, 1);
         }
      }

   }

   public EntitySheep createChild(EntityAgeable var1) {
      EntitySheep var2 = (EntitySheep)var1;
      EntitySheep var3 = new EntitySheep(this.worldObj);
      var3.setFleeceColor(this.getDyeColorMixFromParents(this, var2));
      return var3;
   }

   public void onLivingUpdate() {
      if (this.worldObj.isRemote) {
         this.sheepTimer = Math.max(0, this.sheepTimer - 1);
      }

      super.onLivingUpdate();
   }

   public boolean interact(EntityPlayer var1) {
      ItemStack var2 = var1.inventory.getCurrentItem();
      if (var2 != null && var2.getItem() == Items.shears && this != false && !this.isChild()) {
         if (!this.worldObj.isRemote) {
            this.setSheared(true);
            int var3 = 1 + this.rand.nextInt(3);

            for(int var4 = 0; var4 < var3; ++var4) {
               EntityItem var5 = this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.getFleeceColor().getMetadata()), 1.0F);
               var5.motionY += (double)(this.rand.nextFloat() * 0.05F);
               var5.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
               var5.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
            }
         }

         var2.damageItem(1, var1);
         this.playSound("mob.sheep.shear", 1.0F, 1.0F);
      }

      return super.interact(var1);
   }

   public void handleStatusUpdate(byte var1) {
      if (var1 == 10) {
         this.sheepTimer = 40;
      } else {
         super.handleStatusUpdate(var1);
      }

   }

   protected Item getDropItem() {
      return Item.getItemFromBlock(Blocks.wool);
   }

   public float getHeadRotationPointY(float var1) {
      return this.sheepTimer <= 0 ? 0.0F : (this.sheepTimer >= 4 && this.sheepTimer <= 36 ? 1.0F : (this.sheepTimer < 4 ? ((float)this.sheepTimer - var1) / 4.0F : -((float)(this.sheepTimer - 40) - var1) / 4.0F));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
   }

   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, IEntityLivingData var2) {
      var2 = super.onInitialSpawn(var1, var2);
      this.setFleeceColor(getRandomSheepColor(this.worldObj.rand));
      return var2;
   }

   public float getHeadRotationAngleX(float var1) {
      if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
         float var2 = ((float)(this.sheepTimer - 4) - var1) / 32.0F;
         return 0.62831855F + 0.2199115F * MathHelper.sin(var2 * 28.7F);
      } else {
         return this.sheepTimer > 0 ? 0.62831855F : this.rotationPitch / 57.295776F;
      }
   }

   public EntityAgeable createChild(EntityAgeable var1) {
      return this.createChild(var1);
   }

   public void setFleeceColor(EnumDyeColor var1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      this.dataWatcher.updateObject(16, (byte)(var2 & 240 | var1.getMetadata() & 15));
   }

   public static float[] func_175513_a(EnumDyeColor var0) {
      return (float[])DYE_TO_RGB.get(var0);
   }

   public void setSheared(boolean var1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (var1) {
         this.dataWatcher.updateObject(16, (byte)(var2 | 16));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var2 & -17));
      }

   }

   private EnumDyeColor getDyeColorMixFromParents(EntityAnimal var1, EntityAnimal var2) {
      int var3 = ((EntitySheep)var1).getFleeceColor().getDyeDamage();
      int var4 = ((EntitySheep)var2).getFleeceColor().getDyeDamage();
      this.inventoryCrafting.getStackInSlot(0).setItemDamage(var3);
      this.inventoryCrafting.getStackInSlot(1).setItemDamage(var4);
      ItemStack var5 = CraftingManager.getInstance().findMatchingRecipe(this.inventoryCrafting, ((EntitySheep)var1).worldObj);
      int var6;
      if (var5 != null && var5.getItem() == Items.dye) {
         var6 = var5.getMetadata();
      } else {
         var6 = this.worldObj.rand.nextBoolean() ? var3 : var4;
      }

      return EnumDyeColor.byDyeDamage(var6);
   }

   public EnumDyeColor getFleeceColor() {
      return EnumDyeColor.byMetadata(this.dataWatcher.getWatchableObjectByte(16) & 15);
   }

   static {
      DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[]{1.0F, 1.0F, 1.0F});
      DYE_TO_RGB.put(EnumDyeColor.ORANGE, new float[]{0.85F, 0.5F, 0.2F});
      DYE_TO_RGB.put(EnumDyeColor.MAGENTA, new float[]{0.7F, 0.3F, 0.85F});
      DYE_TO_RGB.put(EnumDyeColor.LIGHT_BLUE, new float[]{0.4F, 0.6F, 0.85F});
      DYE_TO_RGB.put(EnumDyeColor.YELLOW, new float[]{0.9F, 0.9F, 0.2F});
      DYE_TO_RGB.put(EnumDyeColor.LIME, new float[]{0.5F, 0.8F, 0.1F});
      DYE_TO_RGB.put(EnumDyeColor.PINK, new float[]{0.95F, 0.5F, 0.65F});
      DYE_TO_RGB.put(EnumDyeColor.GRAY, new float[]{0.3F, 0.3F, 0.3F});
      DYE_TO_RGB.put(EnumDyeColor.SILVER, new float[]{0.6F, 0.6F, 0.6F});
      DYE_TO_RGB.put(EnumDyeColor.CYAN, new float[]{0.3F, 0.5F, 0.6F});
      DYE_TO_RGB.put(EnumDyeColor.PURPLE, new float[]{0.5F, 0.25F, 0.7F});
      DYE_TO_RGB.put(EnumDyeColor.BLUE, new float[]{0.2F, 0.3F, 0.7F});
      DYE_TO_RGB.put(EnumDyeColor.BROWN, new float[]{0.4F, 0.3F, 0.2F});
      DYE_TO_RGB.put(EnumDyeColor.GREEN, new float[]{0.4F, 0.5F, 0.2F});
      DYE_TO_RGB.put(EnumDyeColor.RED, new float[]{0.6F, 0.2F, 0.2F});
      DYE_TO_RGB.put(EnumDyeColor.BLACK, new float[]{0.1F, 0.1F, 0.1F});
   }

   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound("mob.sheep.step", 0.15F, 1.0F);
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.setSheared(var1.getBoolean("Sheared"));
      this.setFleeceColor(EnumDyeColor.byMetadata(var1.getByte("Color")));
   }

   public void eatGrassBonus() {
      this.setSheared(false);
      if (this.isChild()) {
         this.addGrowth(60);
      }

   }

   protected String getLivingSound() {
      return "mob.sheep.say";
   }

   public float getEyeHeight() {
      return 0.95F * this.height;
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setBoolean("Sheared", this.getSheared());
      var1.setByte("Color", (byte)this.getFleeceColor().getMetadata());
   }

   public EntitySheep(World var1) {
      super(var1);
      this.setSize(0.9F, 1.3F);
      ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
      this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
      this.tasks.addTask(3, new EntityAITempt(this, 1.1D, Items.wheat, false));
      this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
      this.tasks.addTask(5, this.entityAIEatGrass);
      this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
      this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.dye, 1, 0));
      this.inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.dye, 1, 0));
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, new Byte((byte)0));
   }

   protected String getDeathSound() {
      return "mob.sheep.say";
   }

   protected String getHurtSound() {
      return "mob.sheep.say";
   }

   public static EnumDyeColor getRandomSheepColor(Random var0) {
      int var1 = var0.nextInt(100);
      return var1 < 5 ? EnumDyeColor.BLACK : (var1 < 10 ? EnumDyeColor.GRAY : (var1 < 15 ? EnumDyeColor.SILVER : (var1 < 18 ? EnumDyeColor.BROWN : (var0.nextInt(500) == 0 ? EnumDyeColor.PINK : EnumDyeColor.WHITE))));
   }

   protected void updateAITasks() {
      this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
      super.updateAITasks();
   }
}
