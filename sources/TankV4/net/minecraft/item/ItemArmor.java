package net.minecraft.item;

import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.world.World;

public class ItemArmor extends Item {
   private final ItemArmor.ArmorMaterial material;
   public static final String[] EMPTY_SLOT_NAMES = new String[]{"minecraft:items/empty_armor_slot_helmet", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_boots"};
   public final int damageReduceAmount;
   private static final int[] maxDamageArray = new int[]{11, 16, 15, 13};
   private static final IBehaviorDispenseItem dispenserBehavior = new BehaviorDefaultDispenseItem() {
      protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
         BlockPos var3 = var1.getBlockPos().offset(BlockDispenser.getFacing(var1.getBlockMetadata()));
         int var4 = var3.getX();
         int var5 = var3.getY();
         int var6 = var3.getZ();
         AxisAlignedBB var7 = new AxisAlignedBB((double)var4, (double)var5, (double)var6, (double)(var4 + 1), (double)(var5 + 1), (double)(var6 + 1));
         List var8 = var1.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, var7, Predicates.and(EntitySelectors.NOT_SPECTATING, new EntitySelectors.ArmoredMob(var2)));
         if (var8.size() > 0) {
            EntityLivingBase var9 = (EntityLivingBase)var8.get(0);
            int var10 = var9 instanceof EntityPlayer ? 1 : 0;
            int var11 = EntityLiving.getArmorPosition(var2);
            ItemStack var12 = var2.copy();
            var12.stackSize = 1;
            var9.setCurrentItemOrArmor(var11 - var10, var12);
            if (var9 instanceof EntityLiving) {
               ((EntityLiving)var9).setEquipmentDropChance(var11, 2.0F);
            }

            --var2.stackSize;
            return var2;
         } else {
            return super.dispenseStack(var1, var2);
         }
      }
   };
   public final int armorType;
   public final int renderIndex;

   public int getItemEnchantability() {
      return this.material.getEnchantability();
   }

   public void removeColor(ItemStack var1) {
      if (this.material == ItemArmor.ArmorMaterial.LEATHER) {
         NBTTagCompound var2 = var1.getTagCompound();
         if (var2 != null) {
            NBTTagCompound var3 = var2.getCompoundTag("display");
            if (var3.hasKey("color")) {
               var3.removeTag("color");
            }
         }
      }

   }

   public ItemArmor.ArmorMaterial getArmorMaterial() {
      return this.material;
   }

   public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
      int var4 = EntityLiving.getArmorPosition(var1) - 1;
      ItemStack var5 = var3.getCurrentArmor(var4);
      if (var5 == null) {
         var3.setCurrentItemOrArmor(var4, var1.copy());
         var1.stackSize = 0;
      }

      return var1;
   }

   public boolean hasColor(ItemStack var1) {
      return this.material != ItemArmor.ArmorMaterial.LEATHER ? false : (!var1.hasTagCompound() ? false : (!var1.getTagCompound().hasKey("display", 10) ? false : var1.getTagCompound().getCompoundTag("display").hasKey("color", 3)));
   }

   public int getColor(ItemStack var1) {
      if (this.material != ItemArmor.ArmorMaterial.LEATHER) {
         return -1;
      } else {
         NBTTagCompound var2 = var1.getTagCompound();
         if (var2 != null) {
            NBTTagCompound var3 = var2.getCompoundTag("display");
            if (var3 != null && var3.hasKey("color", 3)) {
               return var3.getInteger("color");
            }
         }

         return 10511680;
      }
   }

   public int getColorFromItemStack(ItemStack var1, int var2) {
      if (var2 > 0) {
         return 16777215;
      } else {
         int var3 = this.getColor(var1);
         if (var3 < 0) {
            var3 = 16777215;
         }

         return var3;
      }
   }

   public void setColor(ItemStack var1, int var2) {
      if (this.material != ItemArmor.ArmorMaterial.LEATHER) {
         throw new UnsupportedOperationException("Can't dye non-leather!");
      } else {
         NBTTagCompound var3 = var1.getTagCompound();
         if (var3 == null) {
            var3 = new NBTTagCompound();
            var1.setTagCompound(var3);
         }

         NBTTagCompound var4 = var3.getCompoundTag("display");
         if (!var3.hasKey("display", 10)) {
            var3.setTag("display", var4);
         }

         var4.setInteger("color", var2);
      }
   }

   public boolean getIsRepairable(ItemStack var1, ItemStack var2) {
      return this.material.getRepairItem() == var2.getItem() ? true : super.getIsRepairable(var1, var2);
   }

   static int[] access$0() {
      return maxDamageArray;
   }

   public ItemArmor(ItemArmor.ArmorMaterial var1, int var2, int var3) {
      this.material = var1;
      this.armorType = var3;
      this.renderIndex = var2;
      this.damageReduceAmount = var1.getDamageReductionAmount(var3);
      this.setMaxDamage(var1.getDurability(var3));
      this.maxStackSize = 1;
      this.setCreativeTab(CreativeTabs.tabCombat);
      BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserBehavior);
   }

   public static enum ArmorMaterial {
      private final String name;
      GOLD("gold", 7, new int[]{2, 5, 3, 1}, 25),
      LEATHER("leather", 5, new int[]{1, 3, 2, 1}, 15);

      private final int enchantability;
      private static final ItemArmor.ArmorMaterial[] ENUM$VALUES = new ItemArmor.ArmorMaterial[]{LEATHER, CHAIN, IRON, GOLD, DIAMOND};
      DIAMOND("diamond", 33, new int[]{3, 8, 6, 3}, 10);

      private final int[] damageReductionAmountArray;
      IRON("iron", 15, new int[]{2, 6, 5, 2}, 9);

      private final int maxDamageFactor;
      CHAIN("chainmail", 15, new int[]{2, 5, 4, 1}, 12);

      public int getDurability(int var1) {
         return ItemArmor.access$0()[var1] * this.maxDamageFactor;
      }

      public int getEnchantability() {
         return this.enchantability;
      }

      public String getName() {
         return this.name;
      }

      public int getDamageReductionAmount(int var1) {
         return this.damageReductionAmountArray[var1];
      }

      public Item getRepairItem() {
         return this == LEATHER ? Items.leather : (this == CHAIN ? Items.iron_ingot : (this == GOLD ? Items.gold_ingot : (this == IRON ? Items.iron_ingot : (this == DIAMOND ? Items.diamond : null))));
      }

      private ArmorMaterial(String var3, int var4, int[] var5, int var6) {
         this.name = var3;
         this.maxDamageFactor = var4;
         this.damageReductionAmountArray = var5;
         this.enchantability = var6;
      }
   }
}
