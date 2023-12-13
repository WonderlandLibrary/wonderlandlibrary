package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.WeightedRandom;

public class EnchantmentHelper {
   private static final EnchantmentHelper.ModifierDamage enchantmentModifierDamage = new EnchantmentHelper.ModifierDamage((EnchantmentHelper.ModifierDamage)null);
   private static final EnchantmentHelper.ModifierLiving enchantmentModifierLiving = new EnchantmentHelper.ModifierLiving((EnchantmentHelper.ModifierLiving)null);
   private static final EnchantmentHelper.DamageIterator ENCHANTMENT_ITERATOR_DAMAGE = new EnchantmentHelper.DamageIterator((EnchantmentHelper.DamageIterator)null);
   private static final Random enchantmentRand = new Random();
   private static final EnchantmentHelper.HurtIterator ENCHANTMENT_ITERATOR_HURT = new EnchantmentHelper.HurtIterator((EnchantmentHelper.HurtIterator)null);

   public static int getDepthStriderModifier(Entity var0) {
      return getMaxEnchantmentLevel(Enchantment.depthStrider.effectId, var0.getInventory());
   }

   public static int getRespiration(Entity var0) {
      return getMaxEnchantmentLevel(Enchantment.respiration.effectId, var0.getInventory());
   }

   public static int getFireAspectModifier(EntityLivingBase var0) {
      return getEnchantmentLevel(Enchantment.fireAspect.effectId, var0.getHeldItem());
   }

   public static int getLootingModifier(EntityLivingBase var0) {
      return getEnchantmentLevel(Enchantment.looting.effectId, var0.getHeldItem());
   }

   public static int getEnchantmentModifierDamage(ItemStack[] var0, DamageSource var1) {
      enchantmentModifierDamage.damageModifier = 0;
      enchantmentModifierDamage.source = var1;
      applyEnchantmentModifierArray(enchantmentModifierDamage, var0);
      if (enchantmentModifierDamage.damageModifier > 25) {
         enchantmentModifierDamage.damageModifier = 25;
      } else if (enchantmentModifierDamage.damageModifier < 0) {
         enchantmentModifierDamage.damageModifier = 0;
      }

      return (enchantmentModifierDamage.damageModifier + 1 >> 1) + enchantmentRand.nextInt((enchantmentModifierDamage.damageModifier >> 1) + 1);
   }

   public static ItemStack getEnchantedItem(Enchantment var0, EntityLivingBase var1) {
      ItemStack[] var5;
      int var4 = (var5 = var1.getInventory()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         ItemStack var2 = var5[var3];
         if (var2 != null && getEnchantmentLevel(var0.effectId, var2) > 0) {
            return var2;
         }
      }

      return null;
   }

   public static void setEnchantments(Map var0, ItemStack var1) {
      NBTTagList var2 = new NBTTagList();
      Iterator var3 = var0.keySet().iterator();

      while(var3.hasNext()) {
         int var4 = (Integer)var3.next();
         Enchantment var5 = Enchantment.getEnchantmentById(var4);
         if (var5 != null) {
            NBTTagCompound var6 = new NBTTagCompound();
            var6.setShort("id", (short)var4);
            var6.setShort("lvl", (short)(Integer)var0.get(var4));
            var2.appendTag(var6);
            if (var1.getItem() == Items.enchanted_book) {
               Items.enchanted_book.addEnchantment(var1, new EnchantmentData(var5, (Integer)var0.get(var4)));
            }
         }
      }

      if (var2.tagCount() > 0) {
         if (var1.getItem() != Items.enchanted_book) {
            var1.setTagInfo("ench", var2);
         }
      } else if (var1.hasTagCompound()) {
         var1.getTagCompound().removeTag("ench");
      }

   }

   public static int getLuckOfSeaModifier(EntityLivingBase var0) {
      return getEnchantmentLevel(Enchantment.luckOfTheSea.effectId, var0.getHeldItem());
   }

   public static ItemStack addRandomEnchantment(Random var0, ItemStack var1, int var2) {
      List var3 = buildEnchantmentList(var0, var1, var2);
      boolean var4 = var1.getItem() == Items.book;
      if (var4) {
         var1.setItem(Items.enchanted_book);
      }

      if (var3 != null) {
         Iterator var6 = var3.iterator();

         while(var6.hasNext()) {
            EnchantmentData var5 = (EnchantmentData)var6.next();
            if (var4) {
               Items.enchanted_book.addEnchantment(var1, var5);
            } else {
               var1.addEnchantment(var5.enchantmentobj, var5.enchantmentLevel);
            }
         }
      }

      return var1;
   }

   private static void applyEnchantmentModifierArray(EnchantmentHelper.IModifier var0, ItemStack[] var1) {
      ItemStack[] var5 = var1;
      int var4 = var1.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         ItemStack var2 = var5[var3];
         applyEnchantmentModifier(var0, var2);
      }

   }

   public static boolean getSilkTouchModifier(EntityLivingBase var0) {
      return getEnchantmentLevel(Enchantment.silkTouch.effectId, var0.getHeldItem()) > 0;
   }

   public static int getKnockbackModifier(EntityLivingBase var0) {
      return getEnchantmentLevel(Enchantment.knockback.effectId, var0.getHeldItem());
   }

   public static float func_152377_a(ItemStack var0, EnumCreatureAttribute var1) {
      enchantmentModifierLiving.livingModifier = 0.0F;
      enchantmentModifierLiving.entityLiving = var1;
      applyEnchantmentModifier(enchantmentModifierLiving, var0);
      return enchantmentModifierLiving.livingModifier;
   }

   public static int calcItemStackEnchantability(Random var0, int var1, int var2, ItemStack var3) {
      Item var4 = var3.getItem();
      int var5 = var4.getItemEnchantability();
      if (var5 <= 0) {
         return 0;
      } else {
         if (var2 > 15) {
            var2 = 15;
         }

         int var6 = var0.nextInt(8) + 1 + (var2 >> 1) + var0.nextInt(var2 + 1);
         return var1 == 0 ? Math.max(var6 / 3, 1) : (var1 == 1 ? var6 * 2 / 3 + 1 : Math.max(var6, var2 * 2));
      }
   }

   public static void applyArthropodEnchantments(EntityLivingBase var0, Entity var1) {
      ENCHANTMENT_ITERATOR_DAMAGE.user = var0;
      ENCHANTMENT_ITERATOR_DAMAGE.target = var1;
      if (var0 != null) {
         applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_DAMAGE, var0.getInventory());
      }

      if (var0 instanceof EntityPlayer) {
         applyEnchantmentModifier(ENCHANTMENT_ITERATOR_DAMAGE, var0.getHeldItem());
      }

   }

   public static List buildEnchantmentList(Random var0, ItemStack var1, int var2) {
      Item var3 = var1.getItem();
      int var4 = var3.getItemEnchantability();
      if (var4 <= 0) {
         return null;
      } else {
         var4 /= 2;
         var4 = 1 + var0.nextInt((var4 >> 1) + 1) + var0.nextInt((var4 >> 1) + 1);
         int var5 = var4 + var2;
         float var6 = (var0.nextFloat() + var0.nextFloat() - 1.0F) * 0.15F;
         int var7 = (int)((float)var5 * (1.0F + var6) + 0.5F);
         if (var7 < 1) {
            var7 = 1;
         }

         ArrayList var8 = null;
         Map var9 = mapEnchantmentData(var7, var1);
         if (var9 != null && !var9.isEmpty()) {
            EnchantmentData var10 = (EnchantmentData)WeightedRandom.getRandomItem(var0, var9.values());
            if (var10 != null) {
               var8 = Lists.newArrayList();
               var8.add(var10);

               for(int var11 = var7; var0.nextInt(50) <= var11; var11 >>= 1) {
                  Iterator var12 = var9.keySet().iterator();

                  while(var12.hasNext()) {
                     Integer var13 = (Integer)var12.next();
                     boolean var14 = true;
                     Iterator var16 = var8.iterator();

                     while(var16.hasNext()) {
                        EnchantmentData var15 = (EnchantmentData)var16.next();
                        if (!var15.enchantmentobj.canApplyTogether(Enchantment.getEnchantmentById(var13))) {
                           var14 = false;
                           break;
                        }
                     }

                     if (!var14) {
                        var12.remove();
                     }
                  }

                  if (!var9.isEmpty()) {
                     EnchantmentData var17 = (EnchantmentData)WeightedRandom.getRandomItem(var0, var9.values());
                     var8.add(var17);
                  }
               }
            }
         }

         return var8;
      }
   }

   private static void applyEnchantmentModifier(EnchantmentHelper.IModifier var0, ItemStack var1) {
      if (var1 != null) {
         NBTTagList var2 = var1.getEnchantmentTagList();
         if (var2 != null) {
            for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
               short var4 = var2.getCompoundTagAt(var3).getShort("id");
               short var5 = var2.getCompoundTagAt(var3).getShort("lvl");
               if (Enchantment.getEnchantmentById(var4) != null) {
                  var0.calculateModifier(Enchantment.getEnchantmentById(var4), var5);
               }
            }
         }
      }

   }

   public static boolean getAquaAffinityModifier(EntityLivingBase var0) {
      return getMaxEnchantmentLevel(Enchantment.aquaAffinity.effectId, var0.getInventory()) > 0;
   }

   public static int getEfficiencyModifier(EntityLivingBase var0) {
      return getEnchantmentLevel(Enchantment.efficiency.effectId, var0.getHeldItem());
   }

   public static int getLureModifier(EntityLivingBase var0) {
      return getEnchantmentLevel(Enchantment.lure.effectId, var0.getHeldItem());
   }

   public static int getFortuneModifier(EntityLivingBase var0) {
      return getEnchantmentLevel(Enchantment.fortune.effectId, var0.getHeldItem());
   }

   public static Map getEnchantments(ItemStack var0) {
      LinkedHashMap var1 = Maps.newLinkedHashMap();
      NBTTagList var2 = var0.getItem() == Items.enchanted_book ? Items.enchanted_book.getEnchantments(var0) : var0.getEnchantmentTagList();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
            short var4 = var2.getCompoundTagAt(var3).getShort("id");
            short var5 = var2.getCompoundTagAt(var3).getShort("lvl");
            var1.put(Integer.valueOf(var4), Integer.valueOf(var5));
         }
      }

      return var1;
   }

   public static int getMaxEnchantmentLevel(int var0, ItemStack[] var1) {
      if (var1 == null) {
         return 0;
      } else {
         int var2 = 0;
         ItemStack[] var6 = var1;
         int var5 = var1.length;

         for(int var4 = 0; var4 < var5; ++var4) {
            ItemStack var3 = var6[var4];
            int var7 = getEnchantmentLevel(var0, var3);
            if (var7 > var2) {
               var2 = var7;
            }
         }

         return var2;
      }
   }

   public static Map mapEnchantmentData(int var0, ItemStack var1) {
      Item var2 = var1.getItem();
      HashMap var3 = null;
      boolean var4 = var1.getItem() == Items.book;
      Enchantment[] var8;
      int var7 = (var8 = Enchantment.enchantmentsBookList).length;

      for(int var6 = 0; var6 < var7; ++var6) {
         Enchantment var5 = var8[var6];
         if (var5 != null && (var5.type.canEnchantItem(var2) || var4)) {
            for(int var9 = var5.getMinLevel(); var9 <= var5.getMaxLevel(); ++var9) {
               if (var0 >= var5.getMinEnchantability(var9) && var0 <= var5.getMaxEnchantability(var9)) {
                  if (var3 == null) {
                     var3 = Maps.newHashMap();
                  }

                  var3.put(var5.effectId, new EnchantmentData(var5, var9));
               }
            }
         }
      }

      return var3;
   }

   public static void applyThornEnchantments(EntityLivingBase var0, Entity var1) {
      ENCHANTMENT_ITERATOR_HURT.attacker = var1;
      ENCHANTMENT_ITERATOR_HURT.user = var0;
      if (var0 != null) {
         applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_HURT, var0.getInventory());
      }

      if (var1 instanceof EntityPlayer) {
         applyEnchantmentModifier(ENCHANTMENT_ITERATOR_HURT, var0.getHeldItem());
      }

   }

   public static int getEnchantmentLevel(int var0, ItemStack var1) {
      if (var1 == null) {
         return 0;
      } else {
         NBTTagList var2 = var1.getEnchantmentTagList();
         if (var2 == null) {
            return 0;
         } else {
            for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
               short var4 = var2.getCompoundTagAt(var3).getShort("id");
               short var5 = var2.getCompoundTagAt(var3).getShort("lvl");
               if (var4 == var0) {
                  return var5;
               }
            }

            return 0;
         }
      }
   }

   static final class ModifierLiving implements EnchantmentHelper.IModifier {
      public float livingModifier;
      public EnumCreatureAttribute entityLiving;

      ModifierLiving(EnchantmentHelper.ModifierLiving var1) {
         this();
      }

      public void calculateModifier(Enchantment var1, int var2) {
         this.livingModifier += var1.calcDamageByCreature(var2, this.entityLiving);
      }

      private ModifierLiving() {
      }
   }

   interface IModifier {
      void calculateModifier(Enchantment var1, int var2);
   }

   static final class HurtIterator implements EnchantmentHelper.IModifier {
      public EntityLivingBase user;
      public Entity attacker;

      private HurtIterator() {
      }

      HurtIterator(EnchantmentHelper.HurtIterator var1) {
         this();
      }

      public void calculateModifier(Enchantment var1, int var2) {
         var1.onUserHurt(this.user, this.attacker, var2);
      }
   }

   static final class DamageIterator implements EnchantmentHelper.IModifier {
      public EntityLivingBase user;
      public Entity target;

      DamageIterator(EnchantmentHelper.DamageIterator var1) {
         this();
      }

      private DamageIterator() {
      }

      public void calculateModifier(Enchantment var1, int var2) {
         var1.onEntityDamaged(this.user, this.target, var2);
      }
   }

   static final class ModifierDamage implements EnchantmentHelper.IModifier {
      public DamageSource source;
      public int damageModifier;

      ModifierDamage(EnchantmentHelper.ModifierDamage var1) {
         this();
      }

      public void calculateModifier(Enchantment var1, int var2) {
         this.damageModifier += var1.calcModifierDamage(var2, this.source);
      }

      private ModifierDamage() {
      }
   }
}
