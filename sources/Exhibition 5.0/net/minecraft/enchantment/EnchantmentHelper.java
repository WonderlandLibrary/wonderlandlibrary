// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import java.util.HashMap;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.util.WeightedRandom;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.DamageSource;
import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import java.util.LinkedHashMap;
import net.minecraft.init.Items;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import java.util.Random;

public class EnchantmentHelper
{
    private static final Random enchantmentRand;
    private static final ModifierDamage enchantmentModifierDamage;
    private static final ModifierLiving enchantmentModifierLiving;
    private static final HurtIterator field_151388_d;
    private static final DamageIterator field_151389_e;
    private static final String __OBFID = "CL_00000107";
    
    public static int getEnchantmentLevel(final int p_77506_0_, final ItemStack p_77506_1_) {
        if (p_77506_1_ == null) {
            return 0;
        }
        final NBTTagList var2 = p_77506_1_.getEnchantmentTagList();
        if (var2 == null) {
            return 0;
        }
        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            final short var4 = var2.getCompoundTagAt(var3).getShort("id");
            final short var5 = var2.getCompoundTagAt(var3).getShort("lvl");
            if (var4 == p_77506_0_) {
                return var5;
            }
        }
        return 0;
    }
    
    public static Map getEnchantments(final ItemStack p_82781_0_) {
        final LinkedHashMap var1 = Maps.newLinkedHashMap();
        final NBTTagList var2 = (p_82781_0_.getItem() == Items.enchanted_book) ? Items.enchanted_book.func_92110_g(p_82781_0_) : p_82781_0_.getEnchantmentTagList();
        if (var2 != null) {
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                final short var4 = var2.getCompoundTagAt(var3).getShort("id");
                final short var5 = var2.getCompoundTagAt(var3).getShort("lvl");
                var1.put((int)var4, (int)var5);
            }
        }
        return var1;
    }
    
    public static void setEnchantments(final Map p_82782_0_, final ItemStack p_82782_1_) {
        final NBTTagList var2 = new NBTTagList();
        for (final int var4 : p_82782_0_.keySet()) {
            final Enchantment var5 = Enchantment.func_180306_c(var4);
            if (var5 != null) {
                final NBTTagCompound var6 = new NBTTagCompound();
                var6.setShort("id", (short)var4);
                var6.setShort("lvl", (short)(int)p_82782_0_.get(var4));
                var2.appendTag(var6);
                if (p_82782_1_.getItem() != Items.enchanted_book) {
                    continue;
                }
                Items.enchanted_book.addEnchantment(p_82782_1_, new EnchantmentData(var5, p_82782_0_.get(var4)));
            }
        }
        if (var2.tagCount() > 0) {
            if (p_82782_1_.getItem() != Items.enchanted_book) {
                p_82782_1_.setTagInfo("ench", var2);
            }
        }
        else if (p_82782_1_.hasTagCompound()) {
            p_82782_1_.getTagCompound().removeTag("ench");
        }
    }
    
    public static int getMaxEnchantmentLevel(final int p_77511_0_, final ItemStack[] p_77511_1_) {
        if (p_77511_1_ == null) {
            return 0;
        }
        int var2 = 0;
        for (final ItemStack var5 : p_77511_1_) {
            final int var6 = getEnchantmentLevel(p_77511_0_, var5);
            if (var6 > var2) {
                var2 = var6;
            }
        }
        return var2;
    }
    
    private static void applyEnchantmentModifier(final IModifier p_77518_0_, final ItemStack p_77518_1_) {
        if (p_77518_1_ != null) {
            final NBTTagList var2 = p_77518_1_.getEnchantmentTagList();
            if (var2 != null) {
                for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                    final short var4 = var2.getCompoundTagAt(var3).getShort("id");
                    final short var5 = var2.getCompoundTagAt(var3).getShort("lvl");
                    if (Enchantment.func_180306_c(var4) != null) {
                        p_77518_0_.calculateModifier(Enchantment.func_180306_c(var4), var5);
                    }
                }
            }
        }
    }
    
    private static void applyEnchantmentModifierArray(final IModifier p_77516_0_, final ItemStack[] p_77516_1_) {
        for (final ItemStack var5 : p_77516_1_) {
            applyEnchantmentModifier(p_77516_0_, var5);
        }
    }
    
    public static int getEnchantmentModifierDamage(final ItemStack[] p_77508_0_, final DamageSource p_77508_1_) {
        EnchantmentHelper.enchantmentModifierDamage.damageModifier = 0;
        EnchantmentHelper.enchantmentModifierDamage.source = p_77508_1_;
        applyEnchantmentModifierArray(EnchantmentHelper.enchantmentModifierDamage, p_77508_0_);
        if (EnchantmentHelper.enchantmentModifierDamage.damageModifier > 25) {
            EnchantmentHelper.enchantmentModifierDamage.damageModifier = 25;
        }
        return (EnchantmentHelper.enchantmentModifierDamage.damageModifier + 1 >> 1) + EnchantmentHelper.enchantmentRand.nextInt((EnchantmentHelper.enchantmentModifierDamage.damageModifier >> 1) + 1);
    }
    
    public static float func_152377_a(final ItemStack p_152377_0_, final EnumCreatureAttribute p_152377_1_) {
        EnchantmentHelper.enchantmentModifierLiving.livingModifier = 0.0f;
        EnchantmentHelper.enchantmentModifierLiving.entityLiving = p_152377_1_;
        applyEnchantmentModifier(EnchantmentHelper.enchantmentModifierLiving, p_152377_0_);
        return EnchantmentHelper.enchantmentModifierLiving.livingModifier;
    }
    
    public static void func_151384_a(final EntityLivingBase p_151384_0_, final Entity p_151384_1_) {
        EnchantmentHelper.field_151388_d.field_151363_b = p_151384_1_;
        EnchantmentHelper.field_151388_d.field_151364_a = p_151384_0_;
        if (p_151384_0_ != null) {
            applyEnchantmentModifierArray(EnchantmentHelper.field_151388_d, p_151384_0_.getInventory());
        }
        if (p_151384_1_ instanceof EntityPlayer) {
            applyEnchantmentModifier(EnchantmentHelper.field_151388_d, p_151384_0_.getHeldItem());
        }
    }
    
    public static void func_151385_b(final EntityLivingBase p_151385_0_, final Entity p_151385_1_) {
        EnchantmentHelper.field_151389_e.field_151366_a = p_151385_0_;
        EnchantmentHelper.field_151389_e.field_151365_b = p_151385_1_;
        if (p_151385_0_ != null) {
            applyEnchantmentModifierArray(EnchantmentHelper.field_151389_e, p_151385_0_.getInventory());
        }
        if (p_151385_0_ instanceof EntityPlayer) {
            applyEnchantmentModifier(EnchantmentHelper.field_151389_e, p_151385_0_.getHeldItem());
        }
    }
    
    public static int getRespiration(final EntityLivingBase p_77501_0_) {
        return getEnchantmentLevel(Enchantment.knockback.effectId, p_77501_0_.getHeldItem());
    }
    
    public static int getFireAspectModifier(final EntityLivingBase p_90036_0_) {
        return getEnchantmentLevel(Enchantment.fireAspect.effectId, p_90036_0_.getHeldItem());
    }
    
    public static int func_180319_a(final Entity p_180319_0_) {
        return getMaxEnchantmentLevel(Enchantment.field_180317_h.effectId, p_180319_0_.getInventory());
    }
    
    public static int func_180318_b(final Entity p_180318_0_) {
        return getMaxEnchantmentLevel(Enchantment.field_180316_k.effectId, p_180318_0_.getInventory());
    }
    
    public static int getEfficiencyModifier(final EntityLivingBase p_77509_0_) {
        return getEnchantmentLevel(Enchantment.efficiency.effectId, p_77509_0_.getHeldItem());
    }
    
    public static boolean getSilkTouchModifier(final EntityLivingBase p_77502_0_) {
        return getEnchantmentLevel(Enchantment.silkTouch.effectId, p_77502_0_.getHeldItem()) > 0;
    }
    
    public static int getFortuneModifier(final EntityLivingBase p_77517_0_) {
        return getEnchantmentLevel(Enchantment.fortune.effectId, p_77517_0_.getHeldItem());
    }
    
    public static int func_151386_g(final EntityLivingBase p_151386_0_) {
        return getEnchantmentLevel(Enchantment.luckOfTheSea.effectId, p_151386_0_.getHeldItem());
    }
    
    public static int func_151387_h(final EntityLivingBase p_151387_0_) {
        return getEnchantmentLevel(Enchantment.lure.effectId, p_151387_0_.getHeldItem());
    }
    
    public static int getLootingModifier(final EntityLivingBase p_77519_0_) {
        return getEnchantmentLevel(Enchantment.looting.effectId, p_77519_0_.getHeldItem());
    }
    
    public static boolean getAquaAffinityModifier(final EntityLivingBase p_77510_0_) {
        return getMaxEnchantmentLevel(Enchantment.aquaAffinity.effectId, p_77510_0_.getInventory()) > 0;
    }
    
    public static ItemStack func_92099_a(final Enchantment p_92099_0_, final EntityLivingBase p_92099_1_) {
        for (final ItemStack var5 : p_92099_1_.getInventory()) {
            if (var5 != null && getEnchantmentLevel(p_92099_0_.effectId, var5) > 0) {
                return var5;
            }
        }
        return null;
    }
    
    public static int calcItemStackEnchantability(final Random p_77514_0_, final int p_77514_1_, int p_77514_2_, final ItemStack p_77514_3_) {
        final Item var4 = p_77514_3_.getItem();
        final int var5 = var4.getItemEnchantability();
        if (var5 <= 0) {
            return 0;
        }
        if (p_77514_2_ > 15) {
            p_77514_2_ = 15;
        }
        final int var6 = p_77514_0_.nextInt(8) + 1 + (p_77514_2_ >> 1) + p_77514_0_.nextInt(p_77514_2_ + 1);
        return (p_77514_1_ == 0) ? Math.max(var6 / 3, 1) : ((p_77514_1_ == 1) ? (var6 * 2 / 3 + 1) : Math.max(var6, p_77514_2_ * 2));
    }
    
    public static ItemStack addRandomEnchantment(final Random p_77504_0_, final ItemStack p_77504_1_, final int p_77504_2_) {
        final List var3 = buildEnchantmentList(p_77504_0_, p_77504_1_, p_77504_2_);
        final boolean var4 = p_77504_1_.getItem() == Items.book;
        if (var4) {
            p_77504_1_.setItem(Items.enchanted_book);
        }
        if (var3 != null) {
            for (final EnchantmentData var6 : var3) {
                if (var4) {
                    Items.enchanted_book.addEnchantment(p_77504_1_, var6);
                }
                else {
                    p_77504_1_.addEnchantment(var6.enchantmentobj, var6.enchantmentLevel);
                }
            }
        }
        return p_77504_1_;
    }
    
    public static List buildEnchantmentList(final Random p_77513_0_, final ItemStack p_77513_1_, final int p_77513_2_) {
        final Item var3 = p_77513_1_.getItem();
        int var4 = var3.getItemEnchantability();
        if (var4 <= 0) {
            return null;
        }
        var4 /= 2;
        var4 = 1 + p_77513_0_.nextInt((var4 >> 1) + 1) + p_77513_0_.nextInt((var4 >> 1) + 1);
        final int var5 = var4 + p_77513_2_;
        final float var6 = (p_77513_0_.nextFloat() + p_77513_0_.nextFloat() - 1.0f) * 0.15f;
        int var7 = (int)(var5 * (1.0f + var6) + 0.5f);
        if (var7 < 1) {
            var7 = 1;
        }
        ArrayList var8 = null;
        final Map var9 = mapEnchantmentData(var7, p_77513_1_);
        if (var9 != null && !var9.isEmpty()) {
            final EnchantmentData var10 = (EnchantmentData)WeightedRandom.getRandomItem(p_77513_0_, var9.values());
            if (var10 != null) {
                var8 = Lists.newArrayList();
                var8.add(var10);
                for (int var11 = var7; p_77513_0_.nextInt(50) <= var11; var11 >>= 1) {
                    final Iterator var12 = var9.keySet().iterator();
                    while (var12.hasNext()) {
                        final Integer var13 = var12.next();
                        boolean var14 = true;
                        for (final EnchantmentData var16 : var8) {
                            if (var16.enchantmentobj.canApplyTogether(Enchantment.func_180306_c(var13))) {
                                continue;
                            }
                            var14 = false;
                            break;
                        }
                        if (!var14) {
                            var12.remove();
                        }
                    }
                    if (!var9.isEmpty()) {
                        final EnchantmentData var17 = (EnchantmentData)WeightedRandom.getRandomItem(p_77513_0_, var9.values());
                        var8.add(var17);
                    }
                }
            }
        }
        return var8;
    }
    
    public static Map mapEnchantmentData(final int p_77505_0_, final ItemStack p_77505_1_) {
        final Item var2 = p_77505_1_.getItem();
        HashMap var3 = null;
        final boolean var4 = p_77505_1_.getItem() == Items.book;
        for (final Enchantment var8 : Enchantment.enchantmentsList) {
            if (var8 != null && (var8.type.canEnchantItem(var2) || var4)) {
                for (int var9 = var8.getMinLevel(); var9 <= var8.getMaxLevel(); ++var9) {
                    if (p_77505_0_ >= var8.getMinEnchantability(var9) && p_77505_0_ <= var8.getMaxEnchantability(var9)) {
                        if (var3 == null) {
                            var3 = Maps.newHashMap();
                        }
                        var3.put(var8.effectId, new EnchantmentData(var8, var9));
                    }
                }
            }
        }
        return var3;
    }
    
    static {
        enchantmentRand = new Random();
        enchantmentModifierDamage = new ModifierDamage(null);
        enchantmentModifierLiving = new ModifierLiving(null);
        field_151388_d = new HurtIterator(null);
        field_151389_e = new DamageIterator(null);
    }
    
    static final class DamageIterator implements IModifier
    {
        public EntityLivingBase field_151366_a;
        public Entity field_151365_b;
        private static final String __OBFID = "CL_00000109";
        
        private DamageIterator() {
        }
        
        @Override
        public void calculateModifier(final Enchantment p_77493_1_, final int p_77493_2_) {
            p_77493_1_.func_151368_a(this.field_151366_a, this.field_151365_b, p_77493_2_);
        }
        
        DamageIterator(final Object p_i45359_1_) {
            this();
        }
    }
    
    static final class HurtIterator implements IModifier
    {
        public EntityLivingBase field_151364_a;
        public Entity field_151363_b;
        private static final String __OBFID = "CL_00000110";
        
        private HurtIterator() {
        }
        
        @Override
        public void calculateModifier(final Enchantment p_77493_1_, final int p_77493_2_) {
            p_77493_1_.func_151367_b(this.field_151364_a, this.field_151363_b, p_77493_2_);
        }
        
        HurtIterator(final Object p_i45360_1_) {
            this();
        }
    }
    
    static final class ModifierDamage implements IModifier
    {
        public int damageModifier;
        public DamageSource source;
        private static final String __OBFID = "CL_00000114";
        
        private ModifierDamage() {
        }
        
        @Override
        public void calculateModifier(final Enchantment p_77493_1_, final int p_77493_2_) {
            this.damageModifier += p_77493_1_.calcModifierDamage(p_77493_2_, this.source);
        }
        
        ModifierDamage(final Object p_i1929_1_) {
            this();
        }
    }
    
    static final class ModifierLiving implements IModifier
    {
        public float livingModifier;
        public EnumCreatureAttribute entityLiving;
        private static final String __OBFID = "CL_00000112";
        
        private ModifierLiving() {
        }
        
        @Override
        public void calculateModifier(final Enchantment p_77493_1_, final int p_77493_2_) {
            this.livingModifier += p_77493_1_.func_152376_a(p_77493_2_, this.entityLiving);
        }
        
        ModifierLiving(final Object p_i1928_1_) {
            this();
        }
    }
    
    interface IModifier
    {
        void calculateModifier(final Enchantment p0, final int p1);
    }
}
