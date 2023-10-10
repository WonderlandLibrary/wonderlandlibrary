package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public abstract class Enchantment {
   public EnumEnchantmentType type;
   private static final Map locationEnchantments = Maps.newHashMap();
   public static final Enchantment aquaAffinity = new EnchantmentWaterWorker(6, new ResourceLocation("aqua_affinity"), 2);
   public static final Enchantment fireAspect = new EnchantmentFireAspect(20, new ResourceLocation("fire_aspect"), 2);
   public static final Enchantment respiration = new EnchantmentOxygen(5, new ResourceLocation("respiration"), 2);
   public static final Enchantment unbreaking;
   public static final Enchantment featherFalling = new EnchantmentProtection(2, new ResourceLocation("feather_falling"), 5, 2);
   public static final Enchantment lure;
   public static final Enchantment projectileProtection = new EnchantmentProtection(4, new ResourceLocation("projectile_protection"), 5, 4);
   public static final Enchantment protection = new EnchantmentProtection(0, new ResourceLocation("protection"), 10, 0);
   public static final Enchantment punch;
   public static final Enchantment baneOfArthropods = new EnchantmentDamage(18, new ResourceLocation("bane_of_arthropods"), 5, 2);
   public static final Enchantment blastProtection = new EnchantmentProtection(3, new ResourceLocation("blast_protection"), 2, 3);
   public static final Enchantment looting;
   public static final Enchantment flame;
   private static final Enchantment[] enchantmentsList = new Enchantment[256];
   public final int effectId;
   public static final Enchantment silkTouch;
   public static final Enchantment smite = new EnchantmentDamage(17, new ResourceLocation("smite"), 5, 1);
   public static final Enchantment thorns = new EnchantmentThorns(7, new ResourceLocation("thorns"), 1);
   public static final Enchantment sharpness = new EnchantmentDamage(16, new ResourceLocation("sharpness"), 10, 0);
   public static final Enchantment depthStrider = new EnchantmentWaterWalker(8, new ResourceLocation("depth_strider"), 2);
   public static final Enchantment fortune;
   public static final Enchantment infinity;
   public static final Enchantment efficiency;
   private final int weight;
   public static final Enchantment luckOfTheSea;
   public static final Enchantment power;
   protected String name;
   public static final Enchantment knockback = new EnchantmentKnockback(19, new ResourceLocation("knockback"), 5);
   public static final Enchantment[] enchantmentsBookList;
   public static final Enchantment fireProtection = new EnchantmentProtection(1, new ResourceLocation("fire_protection"), 5, 1);

   public boolean canApplyTogether(Enchantment var1) {
      return this != var1;
   }

   public float calcDamageByCreature(int var1, EnumCreatureAttribute var2) {
      return 0.0F;
   }

   public String getTranslatedName(int var1) {
      String var2 = StatCollector.translateToLocal(this.getName());
      return var2 + " " + StatCollector.translateToLocal("enchantment.level." + var1);
   }

   public void onUserHurt(EntityLivingBase var1, Entity var2, int var3) {
   }

   public int getMinEnchantability(int var1) {
      return 1 + var1 * 10;
   }

   static {
      looting = new EnchantmentLootBonus(21, new ResourceLocation("looting"), 2, EnumEnchantmentType.WEAPON);
      efficiency = new EnchantmentDigging(32, new ResourceLocation("efficiency"), 10);
      silkTouch = new EnchantmentUntouching(33, new ResourceLocation("silk_touch"), 1);
      unbreaking = new EnchantmentDurability(34, new ResourceLocation("unbreaking"), 5);
      fortune = new EnchantmentLootBonus(35, new ResourceLocation("fortune"), 2, EnumEnchantmentType.DIGGER);
      power = new EnchantmentArrowDamage(48, new ResourceLocation("power"), 10);
      punch = new EnchantmentArrowKnockback(49, new ResourceLocation("punch"), 2);
      flame = new EnchantmentArrowFire(50, new ResourceLocation("flame"), 2);
      infinity = new EnchantmentArrowInfinite(51, new ResourceLocation("infinity"), 1);
      luckOfTheSea = new EnchantmentLootBonus(61, new ResourceLocation("luck_of_the_sea"), 2, EnumEnchantmentType.FISHING_ROD);
      lure = new EnchantmentFishingSpeed(62, new ResourceLocation("lure"), 2, EnumEnchantmentType.FISHING_ROD);
      ArrayList var0 = Lists.newArrayList();
      Enchantment[] var4;
      int var3 = (var4 = enchantmentsList).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         Enchantment var1 = var4[var2];
         if (var1 != null) {
            var0.add(var1);
         }
      }

      enchantmentsBookList = (Enchantment[])var0.toArray(new Enchantment[var0.size()]);
   }

   public int getMinLevel() {
      return 1;
   }

   public void onEntityDamaged(EntityLivingBase var1, Entity var2, int var3) {
   }

   public int calcModifierDamage(int var1, DamageSource var2) {
      return 0;
   }

   public static Set func_181077_c() {
      return locationEnchantments.keySet();
   }

   public static Enchantment getEnchantmentById(int var0) {
      return var0 >= 0 && var0 < enchantmentsList.length ? enchantmentsList[var0] : null;
   }

   protected Enchantment(int var1, ResourceLocation var2, int var3, EnumEnchantmentType var4) {
      this.effectId = var1;
      this.weight = var3;
      this.type = var4;
      if (enchantmentsList[var1] != null) {
         throw new IllegalArgumentException("Duplicate enchantment id!");
      } else {
         enchantmentsList[var1] = this;
         locationEnchantments.put(var2, this);
      }
   }

   public String getName() {
      return "enchantment." + this.name;
   }

   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(var1) + 5;
   }

   public int getWeight() {
      return this.weight;
   }

   public int getMaxLevel() {
      return 1;
   }

   public boolean canApply(ItemStack var1) {
      return this.type.canEnchantItem(var1.getItem());
   }

   public static Enchantment getEnchantmentByLocation(String var0) {
      return (Enchantment)locationEnchantments.get(new ResourceLocation(var0));
   }

   public Enchantment setName(String var1) {
      this.name = var1;
      return this;
   }
}
