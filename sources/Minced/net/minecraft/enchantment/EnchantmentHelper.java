// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.util.Util;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.MathHelper;
import net.minecraft.item.Item;
import java.util.Random;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Enchantments;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.DamageSource;
import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.init.Items;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;

public class EnchantmentHelper
{
    private static final ModifierDamage ENCHANTMENT_MODIFIER_DAMAGE;
    private static final ModifierLiving ENCHANTMENT_MODIFIER_LIVING;
    private static final HurtIterator ENCHANTMENT_ITERATOR_HURT;
    private static final DamageIterator ENCHANTMENT_ITERATOR_DAMAGE;
    
    public static int getEnchantmentLevel(final Enchantment enchID, final ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        final NBTTagList nbttaglist = stack.getEnchantmentTagList();
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final Enchantment enchantment = Enchantment.getEnchantmentByID(nbttagcompound.getShort("id"));
            final int j = nbttagcompound.getShort("lvl");
            if (enchantment == enchID) {
                return j;
            }
        }
        return 0;
    }
    
    public static Map<Enchantment, Integer> getEnchantments(final ItemStack stack) {
        final Map<Enchantment, Integer> map = (Map<Enchantment, Integer>)Maps.newLinkedHashMap();
        final NBTTagList nbttaglist = (stack.getItem() == Items.ENCHANTED_BOOK) ? ItemEnchantedBook.getEnchantments(stack) : stack.getEnchantmentTagList();
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final Enchantment enchantment = Enchantment.getEnchantmentByID(nbttagcompound.getShort("id"));
            final int j = nbttagcompound.getShort("lvl");
            map.put(enchantment, j);
        }
        return map;
    }
    
    public static void setEnchantments(final Map<Enchantment, Integer> enchMap, final ItemStack stack) {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final Map.Entry<Enchantment, Integer> entry : enchMap.entrySet()) {
            final Enchantment enchantment = entry.getKey();
            if (enchantment != null) {
                final int i = entry.getValue();
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setShort("id", (short)Enchantment.getEnchantmentID(enchantment));
                nbttagcompound.setShort("lvl", (short)i);
                nbttaglist.appendTag(nbttagcompound);
                if (stack.getItem() != Items.ENCHANTED_BOOK) {
                    continue;
                }
                ItemEnchantedBook.addEnchantment(stack, new EnchantmentData(enchantment, i));
            }
        }
        if (nbttaglist.isEmpty()) {
            if (stack.hasTagCompound()) {
                stack.getTagCompound().removeTag("ench");
            }
        }
        else if (stack.getItem() != Items.ENCHANTED_BOOK) {
            stack.setTagInfo("ench", nbttaglist);
        }
    }
    
    private static void applyEnchantmentModifier(final IModifier modifier, final ItemStack stack) {
        if (!stack.isEmpty()) {
            final NBTTagList nbttaglist = stack.getEnchantmentTagList();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final int j = nbttaglist.getCompoundTagAt(i).getShort("id");
                final int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
                if (Enchantment.getEnchantmentByID(j) != null) {
                    modifier.calculateModifier(Enchantment.getEnchantmentByID(j), k);
                }
            }
        }
    }
    
    private static void applyEnchantmentModifierArray(final IModifier modifier, final Iterable<ItemStack> stacks) {
        for (final ItemStack itemstack : stacks) {
            applyEnchantmentModifier(modifier, itemstack);
        }
    }
    
    public static int getEnchantmentModifierDamage(final Iterable<ItemStack> stacks, final DamageSource source) {
        EnchantmentHelper.ENCHANTMENT_MODIFIER_DAMAGE.damageModifier = 0;
        EnchantmentHelper.ENCHANTMENT_MODIFIER_DAMAGE.source = source;
        applyEnchantmentModifierArray(EnchantmentHelper.ENCHANTMENT_MODIFIER_DAMAGE, stacks);
        return EnchantmentHelper.ENCHANTMENT_MODIFIER_DAMAGE.damageModifier;
    }
    
    public static float getModifierForCreature(final ItemStack stack, final EnumCreatureAttribute creatureAttribute) {
        EnchantmentHelper.ENCHANTMENT_MODIFIER_LIVING.livingModifier = 0.0f;
        EnchantmentHelper.ENCHANTMENT_MODIFIER_LIVING.entityLiving = creatureAttribute;
        applyEnchantmentModifier(EnchantmentHelper.ENCHANTMENT_MODIFIER_LIVING, stack);
        return EnchantmentHelper.ENCHANTMENT_MODIFIER_LIVING.livingModifier;
    }
    
    public static float getSweepingDamageRatio(final EntityLivingBase p_191527_0_) {
        final int i = getMaxEnchantmentLevel(Enchantments.SWEEPING, p_191527_0_);
        return (i > 0) ? EnchantmentSweepingEdge.getSweepingDamageRatio(i) : 0.0f;
    }
    
    public static void applyThornEnchantments(final EntityLivingBase p_151384_0_, final Entity p_151384_1_) {
        EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT.attacker = p_151384_1_;
        EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT.user = p_151384_0_;
        if (p_151384_0_ != null) {
            applyEnchantmentModifierArray(EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getEquipmentAndArmor());
        }
        if (p_151384_1_ instanceof EntityPlayer) {
            applyEnchantmentModifier(EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getHeldItemMainhand());
        }
    }
    
    public static void applyArthropodEnchantments(final EntityLivingBase p_151385_0_, final Entity p_151385_1_) {
        EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE.user = p_151385_0_;
        EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE.target = p_151385_1_;
        if (p_151385_0_ != null) {
            applyEnchantmentModifierArray(EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getEquipmentAndArmor());
        }
        if (p_151385_0_ instanceof EntityPlayer) {
            applyEnchantmentModifier(EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getHeldItemMainhand());
        }
    }
    
    public static int getMaxEnchantmentLevel(final Enchantment p_185284_0_, final EntityLivingBase p_185284_1_) {
        final Iterable<ItemStack> iterable = p_185284_0_.getEntityEquipment(p_185284_1_);
        if (iterable == null) {
            return 0;
        }
        int i = 0;
        for (final ItemStack itemstack : iterable) {
            final int j = getEnchantmentLevel(p_185284_0_, itemstack);
            if (j > i) {
                i = j;
            }
        }
        return i;
    }
    
    public static int getKnockbackModifier(final EntityLivingBase player) {
        return getMaxEnchantmentLevel(Enchantments.KNOCKBACK, player);
    }
    
    public static int getFireAspectModifier(final EntityLivingBase player) {
        return getMaxEnchantmentLevel(Enchantments.FIRE_ASPECT, player);
    }
    
    public static int getRespirationModifier(final EntityLivingBase p_185292_0_) {
        return getMaxEnchantmentLevel(Enchantments.RESPIRATION, p_185292_0_);
    }
    
    public static int getDepthStriderModifier(final EntityLivingBase p_185294_0_) {
        return getMaxEnchantmentLevel(Enchantments.DEPTH_STRIDER, p_185294_0_);
    }
    
    public static int getEfficiencyModifier(final EntityLivingBase p_185293_0_) {
        return getMaxEnchantmentLevel(Enchantments.EFFICIENCY, p_185293_0_);
    }
    
    public static int getFishingLuckBonus(final ItemStack p_191529_0_) {
        return getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA, p_191529_0_);
    }
    
    public static int getFishingSpeedBonus(final ItemStack p_191528_0_) {
        return getEnchantmentLevel(Enchantments.LURE, p_191528_0_);
    }
    
    public static int getLootingModifier(final EntityLivingBase p_185283_0_) {
        return getMaxEnchantmentLevel(Enchantments.LOOTING, p_185283_0_);
    }
    
    public static boolean getAquaAffinityModifier(final EntityLivingBase p_185287_0_) {
        return getMaxEnchantmentLevel(Enchantments.AQUA_AFFINITY, p_185287_0_) > 0;
    }
    
    public static boolean hasFrostWalkerEnchantment(final EntityLivingBase player) {
        return getMaxEnchantmentLevel(Enchantments.FROST_WALKER, player) > 0;
    }
    
    public static boolean hasBindingCurse(final ItemStack p_190938_0_) {
        return getEnchantmentLevel(Enchantments.BINDING_CURSE, p_190938_0_) > 0;
    }
    
    public static boolean hasVanishingCurse(final ItemStack p_190939_0_) {
        return getEnchantmentLevel(Enchantments.VANISHING_CURSE, p_190939_0_) > 0;
    }
    
    public static ItemStack getEnchantedItem(final Enchantment p_92099_0_, final EntityLivingBase p_92099_1_) {
        final List<ItemStack> list = p_92099_0_.getEntityEquipment(p_92099_1_);
        if (list.isEmpty()) {
            return ItemStack.EMPTY;
        }
        final List<ItemStack> list2 = (List<ItemStack>)Lists.newArrayList();
        for (final ItemStack itemstack : list) {
            if (!itemstack.isEmpty() && getEnchantmentLevel(p_92099_0_, itemstack) > 0) {
                list2.add(itemstack);
            }
        }
        return list2.isEmpty() ? ItemStack.EMPTY : list2.get(p_92099_1_.getRNG().nextInt(list2.size()));
    }
    
    public static int calcItemStackEnchantability(final Random rand, final int enchantNum, int power, final ItemStack stack) {
        final Item item = stack.getItem();
        final int i = item.getItemEnchantability();
        if (i <= 0) {
            return 0;
        }
        if (power > 15) {
            power = 15;
        }
        final int j = rand.nextInt(8) + 1 + (power >> 1) + rand.nextInt(power + 1);
        if (enchantNum == 0) {
            return Math.max(j / 3, 1);
        }
        return (enchantNum == 1) ? (j * 2 / 3 + 1) : Math.max(j, power * 2);
    }
    
    public static ItemStack addRandomEnchantment(final Random random, ItemStack stack, final int level, final boolean allowTreasure) {
        final List<EnchantmentData> list = buildEnchantmentList(random, stack, level, allowTreasure);
        final boolean flag = stack.getItem() == Items.BOOK;
        if (flag) {
            stack = new ItemStack(Items.ENCHANTED_BOOK);
        }
        for (final EnchantmentData enchantmentdata : list) {
            if (flag) {
                ItemEnchantedBook.addEnchantment(stack, enchantmentdata);
            }
            else {
                stack.addEnchantment(enchantmentdata.enchantment, enchantmentdata.enchantmentLevel);
            }
        }
        return stack;
    }
    
    public static List<EnchantmentData> buildEnchantmentList(final Random randomIn, final ItemStack itemStackIn, int level, final boolean allowTreasure) {
        final List<EnchantmentData> list = (List<EnchantmentData>)Lists.newArrayList();
        final Item item = itemStackIn.getItem();
        final int i = item.getItemEnchantability();
        if (i <= 0) {
            return list;
        }
        level = level + 1 + randomIn.nextInt(i / 4 + 1) + randomIn.nextInt(i / 4 + 1);
        final float f = (randomIn.nextFloat() + randomIn.nextFloat() - 1.0f) * 0.15f;
        level = MathHelper.clamp(Math.round(level + level * f), 1, Integer.MAX_VALUE);
        final List<EnchantmentData> list2 = getEnchantmentDatas(level, itemStackIn, allowTreasure);
        if (!list2.isEmpty()) {
            list.add(WeightedRandom.getRandomItem(randomIn, list2));
            while (randomIn.nextInt(50) <= level) {
                removeIncompatible(list2, Util.getLastElement(list));
                if (list2.isEmpty()) {
                    break;
                }
                list.add(WeightedRandom.getRandomItem(randomIn, list2));
                level /= 2;
            }
        }
        return list;
    }
    
    public static void removeIncompatible(final List<EnchantmentData> p_185282_0_, final EnchantmentData p_185282_1_) {
        final Iterator<EnchantmentData> iterator = p_185282_0_.iterator();
        while (iterator.hasNext()) {
            if (!p_185282_1_.enchantment.isCompatibleWith(iterator.next().enchantment)) {
                iterator.remove();
            }
        }
    }
    
    public static List<EnchantmentData> getEnchantmentDatas(final int p_185291_0_, final ItemStack p_185291_1_, final boolean allowTreasure) {
        final List<EnchantmentData> list = (List<EnchantmentData>)Lists.newArrayList();
        final Item item = p_185291_1_.getItem();
        final boolean flag = p_185291_1_.getItem() == Items.BOOK;
        for (final Enchantment enchantment : Enchantment.REGISTRY) {
            if ((!enchantment.isTreasureEnchantment() || allowTreasure) && (enchantment.type.canEnchantItem(item) || flag)) {
                for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                    if (p_185291_0_ >= enchantment.getMinEnchantability(i) && p_185291_0_ <= enchantment.getMaxEnchantability(i)) {
                        list.add(new EnchantmentData(enchantment, i));
                        break;
                    }
                }
            }
        }
        return list;
    }
    
    static {
        ENCHANTMENT_MODIFIER_DAMAGE = new ModifierDamage();
        ENCHANTMENT_MODIFIER_LIVING = new ModifierLiving();
        ENCHANTMENT_ITERATOR_HURT = new HurtIterator();
        ENCHANTMENT_ITERATOR_DAMAGE = new DamageIterator();
    }
    
    static final class DamageIterator implements IModifier
    {
        public EntityLivingBase user;
        public Entity target;
        
        private DamageIterator() {
        }
        
        @Override
        public void calculateModifier(final Enchantment enchantmentIn, final int enchantmentLevel) {
            enchantmentIn.onEntityDamaged(this.user, this.target, enchantmentLevel);
        }
    }
    
    static final class HurtIterator implements IModifier
    {
        public EntityLivingBase user;
        public Entity attacker;
        
        private HurtIterator() {
        }
        
        @Override
        public void calculateModifier(final Enchantment enchantmentIn, final int enchantmentLevel) {
            enchantmentIn.onUserHurt(this.user, this.attacker, enchantmentLevel);
        }
    }
    
    static final class ModifierDamage implements IModifier
    {
        public int damageModifier;
        public DamageSource source;
        
        private ModifierDamage() {
        }
        
        @Override
        public void calculateModifier(final Enchantment enchantmentIn, final int enchantmentLevel) {
            this.damageModifier += enchantmentIn.calcModifierDamage(enchantmentLevel, this.source);
        }
    }
    
    static final class ModifierLiving implements IModifier
    {
        public float livingModifier;
        public EnumCreatureAttribute entityLiving;
        
        private ModifierLiving() {
        }
        
        @Override
        public void calculateModifier(final Enchantment enchantmentIn, final int enchantmentLevel) {
            this.livingModifier += enchantmentIn.calcDamageByCreature(enchantmentLevel, this.entityLiving);
        }
    }
    
    interface IModifier
    {
        void calculateModifier(final Enchantment p0, final int p1);
    }
}
