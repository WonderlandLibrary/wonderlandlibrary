// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;
import java.util.Random;
import net.minecraft.nbt.NBTBase;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.Enchantment;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemEnchantedBook extends Item
{
    private static final String __OBFID = "CL_00000025";
    
    @Override
    public boolean hasEffect(final ItemStack stack) {
        return true;
    }
    
    @Override
    public boolean isItemTool(final ItemStack stack) {
        return false;
    }
    
    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return (this.func_92110_g(stack).tagCount() > 0) ? EnumRarity.UNCOMMON : super.getRarity(stack);
    }
    
    public NBTTagList func_92110_g(final ItemStack p_92110_1_) {
        final NBTTagCompound var2 = p_92110_1_.getTagCompound();
        return (NBTTagList)((var2 != null && var2.hasKey("StoredEnchantments", 9)) ? var2.getTag("StoredEnchantments") : new NBTTagList());
    }
    
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        final NBTTagList var5 = this.func_92110_g(stack);
        if (var5 != null) {
            for (int var6 = 0; var6 < var5.tagCount(); ++var6) {
                final short var7 = var5.getCompoundTagAt(var6).getShort("id");
                final short var8 = var5.getCompoundTagAt(var6).getShort("lvl");
                if (Enchantment.func_180306_c(var7) != null) {
                    tooltip.add(Enchantment.func_180306_c(var7).getTranslatedName(var8));
                }
            }
        }
    }
    
    public void addEnchantment(final ItemStack p_92115_1_, final EnchantmentData p_92115_2_) {
        final NBTTagList var3 = this.func_92110_g(p_92115_1_);
        boolean var4 = true;
        for (int var5 = 0; var5 < var3.tagCount(); ++var5) {
            final NBTTagCompound var6 = var3.getCompoundTagAt(var5);
            if (var6.getShort("id") == p_92115_2_.enchantmentobj.effectId) {
                if (var6.getShort("lvl") < p_92115_2_.enchantmentLevel) {
                    var6.setShort("lvl", (short)p_92115_2_.enchantmentLevel);
                }
                var4 = false;
                break;
            }
        }
        if (var4) {
            final NBTTagCompound var7 = new NBTTagCompound();
            var7.setShort("id", (short)p_92115_2_.enchantmentobj.effectId);
            var7.setShort("lvl", (short)p_92115_2_.enchantmentLevel);
            var3.appendTag(var7);
        }
        if (!p_92115_1_.hasTagCompound()) {
            p_92115_1_.setTagCompound(new NBTTagCompound());
        }
        p_92115_1_.getTagCompound().setTag("StoredEnchantments", var3);
    }
    
    public ItemStack getEnchantedItemStack(final EnchantmentData p_92111_1_) {
        final ItemStack var2 = new ItemStack(this);
        this.addEnchantment(var2, p_92111_1_);
        return var2;
    }
    
    public void func_92113_a(final Enchantment p_92113_1_, final List p_92113_2_) {
        for (int var3 = p_92113_1_.getMinLevel(); var3 <= p_92113_1_.getMaxLevel(); ++var3) {
            p_92113_2_.add(this.getEnchantedItemStack(new EnchantmentData(p_92113_1_, var3)));
        }
    }
    
    public WeightedRandomChestContent getRandomEnchantedBook(final Random p_92114_1_) {
        return this.func_92112_a(p_92114_1_, 1, 1, 1);
    }
    
    public WeightedRandomChestContent func_92112_a(final Random p_92112_1_, final int p_92112_2_, final int p_92112_3_, final int p_92112_4_) {
        final ItemStack var5 = new ItemStack(Items.book, 1, 0);
        EnchantmentHelper.addRandomEnchantment(p_92112_1_, var5, 30);
        return new WeightedRandomChestContent(var5, p_92112_2_, p_92112_3_, p_92112_4_);
    }
}
