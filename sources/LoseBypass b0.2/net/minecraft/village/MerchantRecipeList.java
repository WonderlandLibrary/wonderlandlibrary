/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.village;

import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.village.MerchantRecipe;

public class MerchantRecipeList
extends ArrayList<MerchantRecipe> {
    public MerchantRecipeList() {
    }

    public MerchantRecipeList(NBTTagCompound compound) {
        this.readRecipiesFromTags(compound);
    }

    public MerchantRecipe canRecipeBeUsed(ItemStack p_77203_1_, ItemStack p_77203_2_, int p_77203_3_) {
        if (p_77203_3_ > 0 && p_77203_3_ < this.size()) {
            MerchantRecipe merchantrecipe1 = (MerchantRecipe)this.get(p_77203_3_);
            if (!this.func_181078_a(p_77203_1_, merchantrecipe1.getItemToBuy())) return null;
            if (p_77203_2_ != null || merchantrecipe1.hasSecondItemToBuy()) {
                if (!merchantrecipe1.hasSecondItemToBuy()) return null;
                if (!this.func_181078_a(p_77203_2_, merchantrecipe1.getSecondItemToBuy())) return null;
            }
            if (p_77203_1_.stackSize < merchantrecipe1.getItemToBuy().stackSize) return null;
            if (merchantrecipe1.hasSecondItemToBuy() && p_77203_2_.stackSize < merchantrecipe1.getSecondItemToBuy().stackSize) {
                return null;
            }
            MerchantRecipe merchantRecipe = merchantrecipe1;
            return merchantRecipe;
        }
        int i = 0;
        while (i < this.size()) {
            MerchantRecipe merchantrecipe = (MerchantRecipe)this.get(i);
            if (this.func_181078_a(p_77203_1_, merchantrecipe.getItemToBuy()) && p_77203_1_.stackSize >= merchantrecipe.getItemToBuy().stackSize) {
                if (!merchantrecipe.hasSecondItemToBuy()) {
                    if (p_77203_2_ == null) return merchantrecipe;
                }
                if (merchantrecipe.hasSecondItemToBuy() && this.func_181078_a(p_77203_2_, merchantrecipe.getSecondItemToBuy()) && p_77203_2_.stackSize >= merchantrecipe.getSecondItemToBuy().stackSize) {
                    return merchantrecipe;
                }
            }
            ++i;
        }
        return null;
    }

    private boolean func_181078_a(ItemStack p_181078_1_, ItemStack p_181078_2_) {
        if (!ItemStack.areItemsEqual(p_181078_1_, p_181078_2_)) return false;
        if (!p_181078_2_.hasTagCompound()) return true;
        if (!p_181078_1_.hasTagCompound()) return false;
        if (!NBTUtil.func_181123_a(p_181078_2_.getTagCompound(), p_181078_1_.getTagCompound(), false)) return false;
        return true;
    }

    public void writeToBuf(PacketBuffer buffer) {
        buffer.writeByte((byte)(this.size() & 0xFF));
        int i = 0;
        while (i < this.size()) {
            MerchantRecipe merchantrecipe = (MerchantRecipe)this.get(i);
            buffer.writeItemStackToBuffer(merchantrecipe.getItemToBuy());
            buffer.writeItemStackToBuffer(merchantrecipe.getItemToSell());
            ItemStack itemstack = merchantrecipe.getSecondItemToBuy();
            buffer.writeBoolean(itemstack != null);
            if (itemstack != null) {
                buffer.writeItemStackToBuffer(itemstack);
            }
            buffer.writeBoolean(merchantrecipe.isRecipeDisabled());
            buffer.writeInt(merchantrecipe.getToolUses());
            buffer.writeInt(merchantrecipe.getMaxTradeUses());
            ++i;
        }
    }

    public static MerchantRecipeList readFromBuf(PacketBuffer buffer) throws IOException {
        MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
        int i = buffer.readByte() & 0xFF;
        int j = 0;
        while (j < i) {
            ItemStack itemstack = buffer.readItemStackFromBuffer();
            ItemStack itemstack1 = buffer.readItemStackFromBuffer();
            ItemStack itemstack2 = null;
            if (buffer.readBoolean()) {
                itemstack2 = buffer.readItemStackFromBuffer();
            }
            boolean flag = buffer.readBoolean();
            int k = buffer.readInt();
            int l = buffer.readInt();
            MerchantRecipe merchantrecipe = new MerchantRecipe(itemstack, itemstack2, itemstack1, k, l);
            if (flag) {
                merchantrecipe.compensateToolUses();
            }
            merchantrecipelist.add(merchantrecipe);
            ++j;
        }
        return merchantrecipelist;
    }

    public void readRecipiesFromTags(NBTTagCompound compound) {
        NBTTagList nbttaglist = compound.getTagList("Recipes", 10);
        int i = 0;
        while (i < nbttaglist.tagCount()) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            this.add(new MerchantRecipe(nbttagcompound));
            ++i;
        }
    }

    public NBTTagCompound getRecipiesAsTags() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        NBTTagList nbttaglist = new NBTTagList();
        int i = 0;
        while (true) {
            if (i >= this.size()) {
                nbttagcompound.setTag("Recipes", nbttaglist);
                return nbttagcompound;
            }
            MerchantRecipe merchantrecipe = (MerchantRecipe)this.get(i);
            nbttaglist.appendTag(merchantrecipe.writeToTags());
            ++i;
        }
    }
}

