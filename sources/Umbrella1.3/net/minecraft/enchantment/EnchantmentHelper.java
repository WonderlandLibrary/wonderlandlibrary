/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 */
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
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
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
    private static final Random enchantmentRand = new Random();
    private static final ModifierDamage enchantmentModifierDamage = new ModifierDamage(null);
    private static final ModifierLiving enchantmentModifierLiving = new ModifierLiving(null);
    private static final HurtIterator field_151388_d = new HurtIterator(null);
    private static final DamageIterator field_151389_e = new DamageIterator(null);
    private static final String __OBFID = "CL_00000107";

    public static int getEnchantmentLevel(int p_77506_0_, ItemStack p_77506_1_) {
        if (p_77506_1_ == null) {
            return 0;
        }
        NBTTagList var2 = p_77506_1_.getEnchantmentTagList();
        if (var2 == null) {
            return 0;
        }
        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            short var4 = var2.getCompoundTagAt(var3).getShort("id");
            short var5 = var2.getCompoundTagAt(var3).getShort("lvl");
            if (var4 != p_77506_0_) continue;
            return var5;
        }
        return 0;
    }

    public static Map getEnchantments(ItemStack p_82781_0_) {
        NBTTagList var2;
        LinkedHashMap var1 = Maps.newLinkedHashMap();
        NBTTagList nBTTagList = var2 = p_82781_0_.getItem() == Items.enchanted_book ? Items.enchanted_book.func_92110_g(p_82781_0_) : p_82781_0_.getEnchantmentTagList();
        if (var2 != null) {
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                short var4 = var2.getCompoundTagAt(var3).getShort("id");
                short var5 = var2.getCompoundTagAt(var3).getShort("lvl");
                var1.put(Integer.valueOf(var4), Integer.valueOf(var5));
            }
        }
        return var1;
    }

    public static void setEnchantments(Map p_82782_0_, ItemStack p_82782_1_) {
        NBTTagList var2 = new NBTTagList();
        Iterator var3 = p_82782_0_.keySet().iterator();
        while (var3.hasNext()) {
            int var4 = (Integer)var3.next();
            Enchantment var5 = Enchantment.func_180306_c(var4);
            if (var5 == null) continue;
            NBTTagCompound var6 = new NBTTagCompound();
            var6.setShort("id", (short)var4);
            var6.setShort("lvl", (short)((Integer)p_82782_0_.get(var4)).intValue());
            var2.appendTag(var6);
            if (p_82782_1_.getItem() != Items.enchanted_book) continue;
            Items.enchanted_book.addEnchantment(p_82782_1_, new EnchantmentData(var5, (Integer)p_82782_0_.get(var4)));
        }
        if (var2.tagCount() > 0) {
            if (p_82782_1_.getItem() != Items.enchanted_book) {
                p_82782_1_.setTagInfo("ench", var2);
            }
        } else if (p_82782_1_.hasTagCompound()) {
            p_82782_1_.getTagCompound().removeTag("ench");
        }
    }

    public static int getMaxEnchantmentLevel(int p_77511_0_, ItemStack[] p_77511_1_) {
        if (p_77511_1_ == null) {
            return 0;
        }
        int var2 = 0;
        ItemStack[] var3 = p_77511_1_;
        int var4 = p_77511_1_.length;
        for (int var5 = 0; var5 < var4; ++var5) {
            ItemStack var6 = var3[var5];
            int var7 = EnchantmentHelper.getEnchantmentLevel(p_77511_0_, var6);
            if (var7 <= var2) continue;
            var2 = var7;
        }
        return var2;
    }

    private static void applyEnchantmentModifier(IModifier p_77518_0_, ItemStack p_77518_1_) {
        NBTTagList var2;
        if (p_77518_1_ != null && (var2 = p_77518_1_.getEnchantmentTagList()) != null) {
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                short var4 = var2.getCompoundTagAt(var3).getShort("id");
                short var5 = var2.getCompoundTagAt(var3).getShort("lvl");
                if (Enchantment.func_180306_c(var4) == null) continue;
                p_77518_0_.calculateModifier(Enchantment.func_180306_c(var4), var5);
            }
        }
    }

    private static void applyEnchantmentModifierArray(IModifier p_77516_0_, ItemStack[] p_77516_1_) {
        ItemStack[] var2 = p_77516_1_;
        int var3 = p_77516_1_.length;
        for (int var4 = 0; var4 < var3; ++var4) {
            ItemStack var5 = var2[var4];
            EnchantmentHelper.applyEnchantmentModifier(p_77516_0_, var5);
        }
    }

    public static int getEnchantmentModifierDamage(ItemStack[] p_77508_0_, DamageSource p_77508_1_) {
        EnchantmentHelper.enchantmentModifierDamage.damageModifier = 0;
        EnchantmentHelper.enchantmentModifierDamage.source = p_77508_1_;
        EnchantmentHelper.applyEnchantmentModifierArray(enchantmentModifierDamage, p_77508_0_);
        if (EnchantmentHelper.enchantmentModifierDamage.damageModifier > 25) {
            EnchantmentHelper.enchantmentModifierDamage.damageModifier = 25;
        }
        return (EnchantmentHelper.enchantmentModifierDamage.damageModifier + 1 >> 1) + enchantmentRand.nextInt((EnchantmentHelper.enchantmentModifierDamage.damageModifier >> 1) + 1);
    }

    public static float func_152377_a(ItemStack p_152377_0_, EnumCreatureAttribute p_152377_1_) {
        EnchantmentHelper.enchantmentModifierLiving.livingModifier = 0.0f;
        EnchantmentHelper.enchantmentModifierLiving.entityLiving = p_152377_1_;
        EnchantmentHelper.applyEnchantmentModifier(enchantmentModifierLiving, p_152377_0_);
        return EnchantmentHelper.enchantmentModifierLiving.livingModifier;
    }

    public static void func_151384_a(EntityLivingBase p_151384_0_, Entity p_151384_1_) {
        EnchantmentHelper.field_151388_d.field_151363_b = p_151384_1_;
        EnchantmentHelper.field_151388_d.field_151364_a = p_151384_0_;
        if (p_151384_0_ != null) {
            EnchantmentHelper.applyEnchantmentModifierArray(field_151388_d, p_151384_0_.getInventory());
        }
        if (p_151384_1_ instanceof EntityPlayer) {
            EnchantmentHelper.applyEnchantmentModifier(field_151388_d, p_151384_0_.getHeldItem());
        }
    }

    public static void func_151385_b(EntityLivingBase p_151385_0_, Entity p_151385_1_) {
        EnchantmentHelper.field_151389_e.field_151366_a = p_151385_0_;
        EnchantmentHelper.field_151389_e.field_151365_b = p_151385_1_;
        if (p_151385_0_ != null) {
            EnchantmentHelper.applyEnchantmentModifierArray(field_151389_e, p_151385_0_.getInventory());
        }
        if (p_151385_0_ instanceof EntityPlayer) {
            EnchantmentHelper.applyEnchantmentModifier(field_151389_e, p_151385_0_.getHeldItem());
        }
    }

    public static int getRespiration(EntityLivingBase p_77501_0_) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180313_o.effectId, p_77501_0_.getHeldItem());
    }

    public static int getFireAspectModifier(EntityLivingBase p_90036_0_) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, p_90036_0_.getHeldItem());
    }

    public static int func_180319_a(Entity p_180319_0_) {
        return EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.field_180317_h.effectId, p_180319_0_.getInventory());
    }

    public static int func_180318_b(Entity p_180318_0_) {
        return EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.field_180316_k.effectId, p_180318_0_.getInventory());
    }

    public static int getEfficiencyModifier(EntityLivingBase p_77509_0_) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, p_77509_0_.getHeldItem());
    }

    public static boolean getSilkTouchModifier(EntityLivingBase p_77502_0_) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, p_77502_0_.getHeldItem()) > 0;
    }

    public static int getFortuneModifier(EntityLivingBase p_77517_0_) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, p_77517_0_.getHeldItem());
    }

    public static int func_151386_g(EntityLivingBase p_151386_0_) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.luckOfTheSea.effectId, p_151386_0_.getHeldItem());
    }

    public static int func_151387_h(EntityLivingBase p_151387_0_) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.lure.effectId, p_151387_0_.getHeldItem());
    }

    public static int getLootingModifier(EntityLivingBase p_77519_0_) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.looting.effectId, p_77519_0_.getHeldItem());
    }

    public static boolean getAquaAffinityModifier(EntityLivingBase p_77510_0_) {
        return EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.aquaAffinity.effectId, p_77510_0_.getInventory()) > 0;
    }

    public static ItemStack func_92099_a(Enchantment p_92099_0_, EntityLivingBase p_92099_1_) {
        for (ItemStack var5 : p_92099_1_.getInventory()) {
            if (var5 == null || EnchantmentHelper.getEnchantmentLevel(p_92099_0_.effectId, var5) <= 0) continue;
            return var5;
        }
        return null;
    }

    public static int calcItemStackEnchantability(Random p_77514_0_, int p_77514_1_, int p_77514_2_, ItemStack p_77514_3_) {
        Item var4 = p_77514_3_.getItem();
        int var5 = var4.getItemEnchantability();
        if (var5 <= 0) {
            return 0;
        }
        if (p_77514_2_ > 15) {
            p_77514_2_ = 15;
        }
        int var6 = p_77514_0_.nextInt(8) + 1 + (p_77514_2_ >> 1) + p_77514_0_.nextInt(p_77514_2_ + 1);
        return p_77514_1_ == 0 ? Math.max(var6 / 3, 1) : (p_77514_1_ == 1 ? var6 * 2 / 3 + 1 : Math.max(var6, p_77514_2_ * 2));
    }

    public static ItemStack addRandomEnchantment(Random p_77504_0_, ItemStack p_77504_1_, int p_77504_2_) {
        boolean var4;
        List var3 = EnchantmentHelper.buildEnchantmentList(p_77504_0_, p_77504_1_, p_77504_2_);
        boolean bl = var4 = p_77504_1_.getItem() == Items.book;
        if (var4) {
            p_77504_1_.setItem(Items.enchanted_book);
        }
        if (var3 != null) {
            for (EnchantmentData var6 : var3) {
                if (var4) {
                    Items.enchanted_book.addEnchantment(p_77504_1_, var6);
                    continue;
                }
                p_77504_1_.addEnchantment(var6.enchantmentobj, var6.enchantmentLevel);
            }
        }
        return p_77504_1_;
    }

    public static List buildEnchantmentList(Random p_77513_0_, ItemStack p_77513_1_, int p_77513_2_) {
        EnchantmentData var10;
        float var6;
        Item var3 = p_77513_1_.getItem();
        int var4 = var3.getItemEnchantability();
        if (var4 <= 0) {
            return null;
        }
        var4 /= 2;
        int var5 = (var4 = 1 + p_77513_0_.nextInt((var4 >> 1) + 1) + p_77513_0_.nextInt((var4 >> 1) + 1)) + p_77513_2_;
        int var7 = (int)((float)var5 * (1.0f + (var6 = (p_77513_0_.nextFloat() + p_77513_0_.nextFloat() - 1.0f) * 0.15f)) + 0.5f);
        if (var7 < 1) {
            var7 = 1;
        }
        ArrayList var8 = null;
        Map var9 = EnchantmentHelper.mapEnchantmentData(var7, p_77513_1_);
        if (var9 != null && !var9.isEmpty() && (var10 = (EnchantmentData)WeightedRandom.getRandomItem(p_77513_0_, var9.values())) != null) {
            var8 = Lists.newArrayList();
            var8.add(var10);
            for (int var11 = var7; p_77513_0_.nextInt(50) <= var11; var11 >>= 1) {
                Iterator var12 = var9.keySet().iterator();
                while (var12.hasNext()) {
                    Integer var13 = (Integer)var12.next();
                    boolean var14 = true;
                    for (EnchantmentData var16 : var8) {
                        if (var16.enchantmentobj.canApplyTogether(Enchantment.func_180306_c(var13))) continue;
                        var14 = false;
                        break;
                    }
                    if (var14) continue;
                    var12.remove();
                }
                if (var9.isEmpty()) continue;
                EnchantmentData var17 = (EnchantmentData)WeightedRandom.getRandomItem(p_77513_0_, var9.values());
                var8.add(var17);
            }
        }
        return var8;
    }

    public static Map mapEnchantmentData(int p_77505_0_, ItemStack p_77505_1_) {
        Item var2 = p_77505_1_.getItem();
        HashMap var3 = null;
        boolean var4 = p_77505_1_.getItem() == Items.book;
        for (Enchantment var8 : Enchantment.enchantmentsList) {
            if (var8 == null || !var8.type.canEnchantItem(var2) && !var4) continue;
            for (int var9 = var8.getMinLevel(); var9 <= var8.getMaxLevel(); ++var9) {
                if (p_77505_0_ < var8.getMinEnchantability(var9) || p_77505_0_ > var8.getMaxEnchantability(var9)) continue;
                if (var3 == null) {
                    var3 = Maps.newHashMap();
                }
                var3.put(var8.effectId, new EnchantmentData(var8, var9));
            }
        }
        return var3;
    }

    static final class DamageIterator
    implements IModifier {
        public EntityLivingBase field_151366_a;
        public Entity field_151365_b;
        private static final String __OBFID = "CL_00000109";

        private DamageIterator() {
        }

        @Override
        public void calculateModifier(Enchantment p_77493_1_, int p_77493_2_) {
            p_77493_1_.func_151368_a(this.field_151366_a, this.field_151365_b, p_77493_2_);
        }

        DamageIterator(Object p_i45359_1_) {
            this();
        }
    }

    static final class HurtIterator
    implements IModifier {
        public EntityLivingBase field_151364_a;
        public Entity field_151363_b;
        private static final String __OBFID = "CL_00000110";

        private HurtIterator() {
        }

        @Override
        public void calculateModifier(Enchantment p_77493_1_, int p_77493_2_) {
            p_77493_1_.func_151367_b(this.field_151364_a, this.field_151363_b, p_77493_2_);
        }

        HurtIterator(Object p_i45360_1_) {
            this();
        }
    }

    static interface IModifier {
        public void calculateModifier(Enchantment var1, int var2);
    }

    static final class ModifierDamage
    implements IModifier {
        public int damageModifier;
        public DamageSource source;
        private static final String __OBFID = "CL_00000114";

        private ModifierDamage() {
        }

        @Override
        public void calculateModifier(Enchantment p_77493_1_, int p_77493_2_) {
            this.damageModifier += p_77493_1_.calcModifierDamage(p_77493_2_, this.source);
        }

        ModifierDamage(Object p_i1929_1_) {
            this();
        }
    }

    static final class ModifierLiving
    implements IModifier {
        public float livingModifier;
        public EnumCreatureAttribute entityLiving;
        private static final String __OBFID = "CL_00000112";

        private ModifierLiving() {
        }

        @Override
        public void calculateModifier(Enchantment p_77493_1_, int p_77493_2_) {
            this.livingModifier += p_77493_1_.func_152376_a(p_77493_2_, this.entityLiving);
        }

        ModifierLiving(Object p_i1928_1_) {
            this();
        }
    }
}

