// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.village;

import net.minecraft.nbt.NBTTagList;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import java.util.ArrayList;

public class MerchantRecipeList extends ArrayList<MerchantRecipe>
{
    public MerchantRecipeList() {
    }
    
    public MerchantRecipeList(final NBTTagCompound compound) {
        this.readRecipiesFromTags(compound);
    }
    
    @Nullable
    public MerchantRecipe canRecipeBeUsed(final ItemStack stack0, final ItemStack stack1, final int index) {
        if (index > 0 && index < this.size()) {
            final MerchantRecipe merchantrecipe1 = this.get(index);
            return (!this.areItemStacksExactlyEqual(stack0, merchantrecipe1.getItemToBuy()) || ((!stack1.isEmpty() || merchantrecipe1.hasSecondItemToBuy()) && (!merchantrecipe1.hasSecondItemToBuy() || !this.areItemStacksExactlyEqual(stack1, merchantrecipe1.getSecondItemToBuy()))) || stack0.getCount() < merchantrecipe1.getItemToBuy().getCount() || (merchantrecipe1.hasSecondItemToBuy() && stack1.getCount() < merchantrecipe1.getSecondItemToBuy().getCount())) ? null : merchantrecipe1;
        }
        for (int i = 0; i < this.size(); ++i) {
            final MerchantRecipe merchantrecipe2 = this.get(i);
            if (this.areItemStacksExactlyEqual(stack0, merchantrecipe2.getItemToBuy()) && stack0.getCount() >= merchantrecipe2.getItemToBuy().getCount() && ((!merchantrecipe2.hasSecondItemToBuy() && stack1.isEmpty()) || (merchantrecipe2.hasSecondItemToBuy() && this.areItemStacksExactlyEqual(stack1, merchantrecipe2.getSecondItemToBuy()) && stack1.getCount() >= merchantrecipe2.getSecondItemToBuy().getCount()))) {
                return merchantrecipe2;
            }
        }
        return null;
    }
    
    private boolean areItemStacksExactlyEqual(final ItemStack stack1, final ItemStack stack2) {
        return ItemStack.areItemsEqual(stack1, stack2) && (!stack2.hasTagCompound() || (stack1.hasTagCompound() && NBTUtil.areNBTEquals(stack2.getTagCompound(), stack1.getTagCompound(), false)));
    }
    
    public void writeToBuf(final PacketBuffer buffer) {
        buffer.writeByte((byte)(this.size() & 0xFF));
        for (int i = 0; i < this.size(); ++i) {
            final MerchantRecipe merchantrecipe = this.get(i);
            buffer.writeItemStack(merchantrecipe.getItemToBuy());
            buffer.writeItemStack(merchantrecipe.getItemToSell());
            final ItemStack itemstack = merchantrecipe.getSecondItemToBuy();
            buffer.writeBoolean(!itemstack.isEmpty());
            if (!itemstack.isEmpty()) {
                buffer.writeItemStack(itemstack);
            }
            buffer.writeBoolean(merchantrecipe.isRecipeDisabled());
            buffer.writeInt(merchantrecipe.getToolUses());
            buffer.writeInt(merchantrecipe.getMaxTradeUses());
        }
    }
    
    public static MerchantRecipeList readFromBuf(final PacketBuffer buffer) throws IOException {
        final MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
        for (int i = buffer.readByte() & 0xFF, j = 0; j < i; ++j) {
            final ItemStack itemstack = buffer.readItemStack();
            final ItemStack itemstack2 = buffer.readItemStack();
            ItemStack itemstack3 = ItemStack.EMPTY;
            if (buffer.readBoolean()) {
                itemstack3 = buffer.readItemStack();
            }
            final boolean flag = buffer.readBoolean();
            final int k = buffer.readInt();
            final int l = buffer.readInt();
            final MerchantRecipe merchantrecipe = new MerchantRecipe(itemstack, itemstack3, itemstack2, k, l);
            if (flag) {
                merchantrecipe.compensateToolUses();
            }
            merchantrecipelist.add(merchantrecipe);
        }
        return merchantrecipelist;
    }
    
    public void readRecipiesFromTags(final NBTTagCompound compound) {
        final NBTTagList nbttaglist = compound.getTagList("Recipes", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            this.add(new MerchantRecipe(nbttagcompound));
        }
    }
    
    public NBTTagCompound getRecipiesAsTags() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.size(); ++i) {
            final MerchantRecipe merchantrecipe = this.get(i);
            nbttaglist.appendTag(merchantrecipe.writeToTags());
        }
        nbttagcompound.setTag("Recipes", nbttaglist);
        return nbttagcompound;
    }
}
