/*
 * Decompiled with CFR 0.152.
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
    private static final ModifierDamage enchantmentModifierDamage = new ModifierDamage();
    private static final ModifierLiving enchantmentModifierLiving = new ModifierLiving();
    private static final HurtIterator ENCHANTMENT_ITERATOR_HURT = new HurtIterator();
    private static final DamageIterator ENCHANTMENT_ITERATOR_DAMAGE = new DamageIterator();

    public static int getEnchantmentLevel(int enchID, ItemStack stack) {
        if (stack == null) {
            return 0;
        }
        NBTTagList nbttaglist = stack.getEnchantmentTagList();
        if (nbttaglist == null) {
            return 0;
        }
        int i = 0;
        while (i < nbttaglist.tagCount()) {
            short j = nbttaglist.getCompoundTagAt(i).getShort("id");
            short k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
            if (j == enchID) {
                return k;
            }
            ++i;
        }
        return 0;
    }

    public static Map<Integer, Integer> getEnchantments(ItemStack stack) {
        LinkedHashMap<Integer, Integer> map = Maps.newLinkedHashMap();
        NBTTagList nbttaglist = stack.getItem() == Items.enchanted_book ? Items.enchanted_book.getEnchantments(stack) : stack.getEnchantmentTagList();
        if (nbttaglist == null) return map;
        int i = 0;
        while (i < nbttaglist.tagCount()) {
            short j = nbttaglist.getCompoundTagAt(i).getShort("id");
            short k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
            map.put(Integer.valueOf(j), Integer.valueOf(k));
            ++i;
        }
        return map;
    }

    public static void setEnchantments(Map<Integer, Integer> enchMap, ItemStack stack) {
        NBTTagList nbttaglist = new NBTTagList();
        for (int i : enchMap.keySet()) {
            Enchantment enchantment = Enchantment.getEnchantmentById(i);
            if (enchantment == null) continue;
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setShort("id", (short)i);
            nbttagcompound.setShort("lvl", (short)enchMap.get(i).intValue());
            nbttaglist.appendTag(nbttagcompound);
            if (stack.getItem() != Items.enchanted_book) continue;
            Items.enchanted_book.addEnchantment(stack, new EnchantmentData(enchantment, enchMap.get(i)));
        }
        if (nbttaglist.tagCount() > 0) {
            if (stack.getItem() == Items.enchanted_book) return;
            stack.setTagInfo("ench", nbttaglist);
            return;
        }
        if (!stack.hasTagCompound()) return;
        stack.getTagCompound().removeTag("ench");
    }

    public static int getMaxEnchantmentLevel(int enchID, ItemStack[] stacks) {
        if (stacks == null) {
            return 0;
        }
        int i = 0;
        ItemStack[] itemStackArray = stacks;
        int n = itemStackArray.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack itemstack = itemStackArray[n2];
            int j = EnchantmentHelper.getEnchantmentLevel(enchID, itemstack);
            if (j > i) {
                i = j;
            }
            ++n2;
        }
        return i;
    }

    private static void applyEnchantmentModifier(IModifier modifier, ItemStack stack) {
        if (stack == null) return;
        NBTTagList nbttaglist = stack.getEnchantmentTagList();
        if (nbttaglist == null) return;
        int i = 0;
        while (i < nbttaglist.tagCount()) {
            short j = nbttaglist.getCompoundTagAt(i).getShort("id");
            short k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
            if (Enchantment.getEnchantmentById(j) != null) {
                modifier.calculateModifier(Enchantment.getEnchantmentById(j), k);
            }
            ++i;
        }
    }

    private static void applyEnchantmentModifierArray(IModifier modifier, ItemStack[] stacks) {
        ItemStack[] itemStackArray = stacks;
        int n = itemStackArray.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack itemstack = itemStackArray[n2];
            EnchantmentHelper.applyEnchantmentModifier(modifier, itemstack);
            ++n2;
        }
    }

    public static int getEnchantmentModifierDamage(ItemStack[] stacks, DamageSource source) {
        EnchantmentHelper.enchantmentModifierDamage.damageModifier = 0;
        EnchantmentHelper.enchantmentModifierDamage.source = source;
        EnchantmentHelper.applyEnchantmentModifierArray(enchantmentModifierDamage, stacks);
        if (EnchantmentHelper.enchantmentModifierDamage.damageModifier > 25) {
            EnchantmentHelper.enchantmentModifierDamage.damageModifier = 25;
            return (EnchantmentHelper.enchantmentModifierDamage.damageModifier + 1 >> 1) + enchantmentRand.nextInt((EnchantmentHelper.enchantmentModifierDamage.damageModifier >> 1) + 1);
        }
        if (EnchantmentHelper.enchantmentModifierDamage.damageModifier >= 0) return (EnchantmentHelper.enchantmentModifierDamage.damageModifier + 1 >> 1) + enchantmentRand.nextInt((EnchantmentHelper.enchantmentModifierDamage.damageModifier >> 1) + 1);
        EnchantmentHelper.enchantmentModifierDamage.damageModifier = 0;
        return (EnchantmentHelper.enchantmentModifierDamage.damageModifier + 1 >> 1) + enchantmentRand.nextInt((EnchantmentHelper.enchantmentModifierDamage.damageModifier >> 1) + 1);
    }

    public static float func_152377_a(ItemStack p_152377_0_, EnumCreatureAttribute p_152377_1_) {
        EnchantmentHelper.enchantmentModifierLiving.livingModifier = 0.0f;
        EnchantmentHelper.enchantmentModifierLiving.entityLiving = p_152377_1_;
        EnchantmentHelper.applyEnchantmentModifier(enchantmentModifierLiving, p_152377_0_);
        return EnchantmentHelper.enchantmentModifierLiving.livingModifier;
    }

    public static void applyThornEnchantments(EntityLivingBase p_151384_0_, Entity p_151384_1_) {
        EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT.attacker = p_151384_1_;
        EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT.user = p_151384_0_;
        if (p_151384_0_ != null) {
            EnchantmentHelper.applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getInventory());
        }
        if (!(p_151384_1_ instanceof EntityPlayer)) return;
        EnchantmentHelper.applyEnchantmentModifier(ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getHeldItem());
    }

    public static void applyArthropodEnchantments(EntityLivingBase p_151385_0_, Entity p_151385_1_) {
        EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE.user = p_151385_0_;
        EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE.target = p_151385_1_;
        if (p_151385_0_ != null) {
            EnchantmentHelper.applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getInventory());
        }
        if (!(p_151385_0_ instanceof EntityPlayer)) return;
        EnchantmentHelper.applyEnchantmentModifier(ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getHeldItem());
    }

    public static int getKnockbackModifier(EntityLivingBase player) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, player.getHeldItem());
    }

    public static int getFireAspectModifier(EntityLivingBase player) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, player.getHeldItem());
    }

    public static int getRespiration(Entity player) {
        return EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.respiration.effectId, player.getInventory());
    }

    public static int getDepthStriderModifier(Entity player) {
        return EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.depthStrider.effectId, player.getInventory());
    }

    public static int getEfficiencyModifier(EntityLivingBase player) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, player.getHeldItem());
    }

    public static boolean getSilkTouchModifier(EntityLivingBase player) {
        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, player.getHeldItem()) <= 0) return false;
        return true;
    }

    public static int getFortuneModifier(EntityLivingBase player) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, player.getHeldItem());
    }

    public static int getLuckOfSeaModifier(EntityLivingBase player) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.luckOfTheSea.effectId, player.getHeldItem());
    }

    public static int getLureModifier(EntityLivingBase player) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.lure.effectId, player.getHeldItem());
    }

    public static int getLootingModifier(EntityLivingBase player) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.looting.effectId, player.getHeldItem());
    }

    public static boolean getAquaAffinityModifier(EntityLivingBase player) {
        if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.aquaAffinity.effectId, player.getInventory()) <= 0) return false;
        return true;
    }

    public static ItemStack getEnchantedItem(Enchantment p_92099_0_, EntityLivingBase p_92099_1_) {
        ItemStack[] itemStackArray = p_92099_1_.getInventory();
        int n = itemStackArray.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack itemstack = itemStackArray[n2];
            if (itemstack != null && EnchantmentHelper.getEnchantmentLevel(p_92099_0_.effectId, itemstack) > 0) {
                return itemstack;
            }
            ++n2;
        }
        return null;
    }

    public static int calcItemStackEnchantability(Random p_77514_0_, int p_77514_1_, int p_77514_2_, ItemStack p_77514_3_) {
        int n;
        Item item = p_77514_3_.getItem();
        int i = item.getItemEnchantability();
        if (i <= 0) {
            return 0;
        }
        if (p_77514_2_ > 15) {
            p_77514_2_ = 15;
        }
        int j = p_77514_0_.nextInt(8) + 1 + (p_77514_2_ >> 1) + p_77514_0_.nextInt(p_77514_2_ + 1);
        if (p_77514_1_ == 0) {
            n = Math.max(j / 3, 1);
            return n;
        }
        if (p_77514_1_ == 1) {
            n = j * 2 / 3 + 1;
            return n;
        }
        n = Math.max(j, p_77514_2_ * 2);
        return n;
    }

    public static ItemStack addRandomEnchantment(Random p_77504_0_, ItemStack p_77504_1_, int p_77504_2_) {
        boolean flag;
        List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(p_77504_0_, p_77504_1_, p_77504_2_);
        boolean bl = flag = p_77504_1_.getItem() == Items.book;
        if (flag) {
            p_77504_1_.setItem(Items.enchanted_book);
        }
        if (list == null) return p_77504_1_;
        Iterator<EnchantmentData> iterator = list.iterator();
        while (iterator.hasNext()) {
            EnchantmentData enchantmentdata = iterator.next();
            if (flag) {
                Items.enchanted_book.addEnchantment(p_77504_1_, enchantmentdata);
                continue;
            }
            p_77504_1_.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
        }
        return p_77504_1_;
    }

    public static List<EnchantmentData> buildEnchantmentList(Random randomIn, ItemStack itemStackIn, int p_77513_2_) {
        float f;
        Item item = itemStackIn.getItem();
        int i = item.getItemEnchantability();
        if (i <= 0) {
            return null;
        }
        i /= 2;
        int j = (i = 1 + randomIn.nextInt((i >> 1) + 1) + randomIn.nextInt((i >> 1) + 1)) + p_77513_2_;
        int k = (int)((float)j * (1.0f + (f = (randomIn.nextFloat() + randomIn.nextFloat() - 1.0f) * 0.15f)) + 0.5f);
        if (k < 1) {
            k = 1;
        }
        ArrayList<EnchantmentData> list = null;
        Map<Integer, EnchantmentData> map = EnchantmentHelper.mapEnchantmentData(k, itemStackIn);
        if (map == null) return list;
        if (map.isEmpty()) return list;
        EnchantmentData enchantmentdata = WeightedRandom.getRandomItem(randomIn, map.values());
        if (enchantmentdata == null) return list;
        list = Lists.newArrayList();
        list.add(enchantmentdata);
        int l = k;
        while (randomIn.nextInt(50) <= l) {
            Iterator<Integer> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                Integer integer = iterator.next();
                boolean flag = true;
                for (EnchantmentData enchantmentdata1 : list) {
                    if (enchantmentdata1.enchantmentobj.canApplyTogether(Enchantment.getEnchantmentById(integer))) continue;
                    flag = false;
                    break;
                }
                if (flag) continue;
                iterator.remove();
            }
            if (!map.isEmpty()) {
                EnchantmentData enchantmentdata2 = WeightedRandom.getRandomItem(randomIn, map.values());
                list.add(enchantmentdata2);
            }
            l >>= 1;
        }
        return list;
    }

    public static Map<Integer, EnchantmentData> mapEnchantmentData(int p_77505_0_, ItemStack p_77505_1_) {
        Item item = p_77505_1_.getItem();
        HashMap<Integer, EnchantmentData> map = null;
        boolean flag = p_77505_1_.getItem() == Items.book;
        Enchantment[] enchantmentArray = Enchantment.enchantmentsBookList;
        int n = enchantmentArray.length;
        int n2 = 0;
        while (n2 < n) {
            Enchantment enchantment = enchantmentArray[n2];
            if (enchantment != null && (enchantment.type.canEnchantItem(item) || flag)) {
                for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
                    if (p_77505_0_ < enchantment.getMinEnchantability(i) || p_77505_0_ > enchantment.getMaxEnchantability(i)) continue;
                    if (map == null) {
                        map = Maps.newHashMap();
                    }
                    map.put(enchantment.effectId, new EnchantmentData(enchantment, i));
                }
            }
            ++n2;
        }
        return map;
    }

    static final class ModifierLiving
    implements IModifier {
        public float livingModifier;
        public EnumCreatureAttribute entityLiving;

        private ModifierLiving() {
        }

        @Override
        public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
            this.livingModifier += enchantmentIn.calcDamageByCreature(enchantmentLevel, this.entityLiving);
        }
    }

    static final class ModifierDamage
    implements IModifier {
        public int damageModifier;
        public DamageSource source;

        private ModifierDamage() {
        }

        @Override
        public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
            this.damageModifier += enchantmentIn.calcModifierDamage(enchantmentLevel, this.source);
        }
    }

    static interface IModifier {
        public void calculateModifier(Enchantment var1, int var2);
    }

    static final class HurtIterator
    implements IModifier {
        public EntityLivingBase user;
        public Entity attacker;

        private HurtIterator() {
        }

        @Override
        public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
            enchantmentIn.onUserHurt(this.user, this.attacker, enchantmentLevel);
        }
    }

    static final class DamageIterator
    implements IModifier {
        public EntityLivingBase user;
        public Entity target;

        private DamageIterator() {
        }

        @Override
        public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
            enchantmentIn.onEntityDamaged(this.user, this.target, enchantmentLevel);
        }
    }
}

