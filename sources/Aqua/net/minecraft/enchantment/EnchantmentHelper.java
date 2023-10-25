package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.WeightedRandom;

public class EnchantmentHelper {
    private static final Random enchantmentRand = new Random();
    private static final ModifierDamage enchantmentModifierDamage = new ModifierDamage(null);
    private static final ModifierLiving enchantmentModifierLiving = new ModifierLiving(null);
    private static final HurtIterator ENCHANTMENT_ITERATOR_HURT = new HurtIterator(null);
    private static final DamageIterator ENCHANTMENT_ITERATOR_DAMAGE = new DamageIterator(null);

    public static int getEnchantmentLevel(int enchID, ItemStack stack) {
        if (stack == null) {
            return 0;
        }
        NBTTagList nbttaglist = stack.getEnchantmentTagList();
        if (nbttaglist == null) {
            return 0;
        }
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            short j = nbttaglist.getCompoundTagAt(i).getShort("id");
            short k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
            if (j != enchID) continue;
            return k;
        }
        return 0;
    }

    public static Map<Integer, Integer> getEnchantments(ItemStack stack) {
        NBTTagList nbttaglist;
        LinkedHashMap map = Maps.newLinkedHashMap();
        NBTTagList nBTTagList = nbttaglist = stack.getItem() == Items.enchanted_book ? Items.enchanted_book.getEnchantments(stack) : stack.getEnchantmentTagList();
        if (nbttaglist != null) {
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                short j = nbttaglist.getCompoundTagAt(i).getShort("id");
                short k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
                map.put((Object)j, (Object)k);
            }
        }
        return map;
    }

    public static void setEnchantments(Map<Integer, Integer> enchMap, ItemStack stack) {
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = enchMap.keySet().iterator();
        while (iterator.hasNext()) {
            int i = (Integer)iterator.next();
            Enchantment enchantment = Enchantment.getEnchantmentById((int)i);
            if (enchantment == null) continue;
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setShort("id", (short)i);
            nbttagcompound.setShort("lvl", (short)((Integer)enchMap.get((Object)i)).intValue());
            nbttaglist.appendTag((NBTBase)nbttagcompound);
            if (stack.getItem() != Items.enchanted_book) continue;
            Items.enchanted_book.addEnchantment(stack, new EnchantmentData(enchantment, ((Integer)enchMap.get((Object)i)).intValue()));
        }
        if (nbttaglist.tagCount() > 0) {
            if (stack.getItem() != Items.enchanted_book) {
                stack.setTagInfo("ench", (NBTBase)nbttaglist);
            }
        } else if (stack.hasTagCompound()) {
            stack.getTagCompound().removeTag("ench");
        }
    }

    public static int getMaxEnchantmentLevel(int enchID, ItemStack[] stacks) {
        if (stacks == null) {
            return 0;
        }
        int i = 0;
        for (ItemStack itemstack : stacks) {
            int j = EnchantmentHelper.getEnchantmentLevel(enchID, itemstack);
            if (j <= i) continue;
            i = j;
        }
        return i;
    }

    private static void applyEnchantmentModifier(IModifier modifier, ItemStack stack) {
        NBTTagList nbttaglist;
        if (stack != null && (nbttaglist = stack.getEnchantmentTagList()) != null) {
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                short j = nbttaglist.getCompoundTagAt(i).getShort("id");
                short k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
                if (Enchantment.getEnchantmentById((int)j) == null) continue;
                modifier.calculateModifier(Enchantment.getEnchantmentById((int)j), (int)k);
            }
        }
    }

    private static void applyEnchantmentModifierArray(IModifier modifier, ItemStack[] stacks) {
        for (ItemStack itemstack : stacks) {
            EnchantmentHelper.applyEnchantmentModifier(modifier, itemstack);
        }
    }

    public static int getEnchantmentModifierDamage(ItemStack[] stacks, DamageSource source) {
        EnchantmentHelper.enchantmentModifierDamage.damageModifier = 0;
        EnchantmentHelper.enchantmentModifierDamage.source = source;
        EnchantmentHelper.applyEnchantmentModifierArray((IModifier)enchantmentModifierDamage, stacks);
        if (EnchantmentHelper.enchantmentModifierDamage.damageModifier > 25) {
            EnchantmentHelper.enchantmentModifierDamage.damageModifier = 25;
        } else if (EnchantmentHelper.enchantmentModifierDamage.damageModifier < 0) {
            EnchantmentHelper.enchantmentModifierDamage.damageModifier = 0;
        }
        return (EnchantmentHelper.enchantmentModifierDamage.damageModifier + 1 >> 1) + enchantmentRand.nextInt((EnchantmentHelper.enchantmentModifierDamage.damageModifier >> 1) + 1);
    }

    public static float getModifierForCreature(ItemStack p_152377_0_, EnumCreatureAttribute p_152377_1_) {
        EnchantmentHelper.enchantmentModifierLiving.livingModifier = 0.0f;
        EnchantmentHelper.enchantmentModifierLiving.entityLiving = p_152377_1_;
        EnchantmentHelper.applyEnchantmentModifier((IModifier)enchantmentModifierLiving, p_152377_0_);
        return EnchantmentHelper.enchantmentModifierLiving.livingModifier;
    }

    public static void applyThornEnchantments(EntityLivingBase p_151384_0_, Entity p_151384_1_) {
        EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT.attacker = p_151384_1_;
        EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT.user = p_151384_0_;
        if (p_151384_0_ != null) {
            EnchantmentHelper.applyEnchantmentModifierArray((IModifier)ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getInventory());
        }
        if (p_151384_1_ instanceof EntityPlayer) {
            EnchantmentHelper.applyEnchantmentModifier((IModifier)ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getHeldItem());
        }
    }

    public static void applyArthropodEnchantments(EntityLivingBase p_151385_0_, Entity p_151385_1_) {
        EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE.user = p_151385_0_;
        EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE.target = p_151385_1_;
        if (p_151385_0_ != null) {
            EnchantmentHelper.applyEnchantmentModifierArray((IModifier)ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getInventory());
        }
        if (p_151385_0_ instanceof EntityPlayer) {
            EnchantmentHelper.applyEnchantmentModifier((IModifier)ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getHeldItem());
        }
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
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, player.getHeldItem()) > 0;
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
        return EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.aquaAffinity.effectId, player.getInventory()) > 0;
    }

    public static ItemStack getEnchantedItem(Enchantment p_92099_0_, EntityLivingBase p_92099_1_) {
        for (ItemStack itemstack : p_92099_1_.getInventory()) {
            if (itemstack == null || EnchantmentHelper.getEnchantmentLevel(p_92099_0_.effectId, itemstack) <= 0) continue;
            return itemstack;
        }
        return null;
    }

    public static int calcItemStackEnchantability(Random rand, int enchantNum, int power, ItemStack stack) {
        Item item = stack.getItem();
        int i = item.getItemEnchantability();
        if (i <= 0) {
            return 0;
        }
        if (power > 15) {
            power = 15;
        }
        int j = rand.nextInt(8) + 1 + (power >> 1) + rand.nextInt(power + 1);
        return enchantNum == 0 ? Math.max((int)(j / 3), (int)1) : (enchantNum == 1 ? j * 2 / 3 + 1 : Math.max((int)j, (int)(power * 2)));
    }

    public static ItemStack addRandomEnchantment(Random p_77504_0_, ItemStack p_77504_1_, int p_77504_2_) {
        boolean flag;
        List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(p_77504_0_, p_77504_1_, p_77504_2_);
        boolean bl = flag = p_77504_1_.getItem() == Items.book;
        if (flag) {
            p_77504_1_.setItem((Item)Items.enchanted_book);
        }
        if (list != null) {
            for (EnchantmentData enchantmentdata : list) {
                if (flag) {
                    Items.enchanted_book.addEnchantment(p_77504_1_, enchantmentdata);
                    continue;
                }
                p_77504_1_.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
            }
        }
        return p_77504_1_;
    }

    public static List<EnchantmentData> buildEnchantmentList(Random randomIn, ItemStack itemStackIn, int p_77513_2_) {
        EnchantmentData enchantmentdata;
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
        ArrayList list = null;
        Map<Integer, EnchantmentData> map = EnchantmentHelper.mapEnchantmentData(k, itemStackIn);
        if (map != null && !map.isEmpty() && (enchantmentdata = (EnchantmentData)WeightedRandom.getRandomItem((Random)randomIn, (Collection)map.values())) != null) {
            list = Lists.newArrayList();
            list.add((Object)enchantmentdata);
            for (int l = k; randomIn.nextInt(50) <= l; l >>= 1) {
                Iterator iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    Integer integer = (Integer)iterator.next();
                    boolean flag = true;
                    for (EnchantmentData enchantmentdata1 : list) {
                        if (enchantmentdata1.enchantmentobj.canApplyTogether(Enchantment.getEnchantmentById((int)integer))) continue;
                        flag = false;
                        break;
                    }
                    if (flag) continue;
                    iterator.remove();
                }
                if (map.isEmpty()) continue;
                EnchantmentData enchantmentdata2 = (EnchantmentData)WeightedRandom.getRandomItem((Random)randomIn, (Collection)map.values());
                list.add((Object)enchantmentdata2);
            }
        }
        return list;
    }

    public static Map<Integer, EnchantmentData> mapEnchantmentData(int p_77505_0_, ItemStack p_77505_1_) {
        Item item = p_77505_1_.getItem();
        HashMap map = null;
        boolean flag = p_77505_1_.getItem() == Items.book;
        for (Enchantment enchantment : Enchantment.enchantmentsBookList) {
            if (enchantment == null || !enchantment.type.canEnchantItem(item) && !flag) continue;
            for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
                if (p_77505_0_ < enchantment.getMinEnchantability(i) || p_77505_0_ > enchantment.getMaxEnchantability(i)) continue;
                if (map == null) {
                    map = Maps.newHashMap();
                }
                map.put((Object)enchantment.effectId, (Object)new EnchantmentData(enchantment, i));
            }
        }
        return map;
    }
}
