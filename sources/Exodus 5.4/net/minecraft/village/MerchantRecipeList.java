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
    public void readRecipiesFromTags(NBTTagCompound nBTTagCompound) {
        NBTTagList nBTTagList = nBTTagCompound.getTagList("Recipes", 10);
        int n = 0;
        while (n < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound2 = nBTTagList.getCompoundTagAt(n);
            this.add(new MerchantRecipe(nBTTagCompound2));
            ++n;
        }
    }

    public NBTTagCompound getRecipiesAsTags() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        NBTTagList nBTTagList = new NBTTagList();
        int n = 0;
        while (n < this.size()) {
            MerchantRecipe merchantRecipe = (MerchantRecipe)this.get(n);
            nBTTagList.appendTag(merchantRecipe.writeToTags());
            ++n;
        }
        nBTTagCompound.setTag("Recipes", nBTTagList);
        return nBTTagCompound;
    }

    public MerchantRecipe canRecipeBeUsed(ItemStack itemStack, ItemStack itemStack2, int n) {
        if (n > 0 && n < this.size()) {
            MerchantRecipe merchantRecipe = (MerchantRecipe)this.get(n);
            return !this.func_181078_a(itemStack, merchantRecipe.getItemToBuy()) || (itemStack2 != null || merchantRecipe.hasSecondItemToBuy()) && (!merchantRecipe.hasSecondItemToBuy() || !this.func_181078_a(itemStack2, merchantRecipe.getSecondItemToBuy())) || itemStack.stackSize < merchantRecipe.getItemToBuy().stackSize || merchantRecipe.hasSecondItemToBuy() && itemStack2.stackSize < merchantRecipe.getSecondItemToBuy().stackSize ? null : merchantRecipe;
        }
        int n2 = 0;
        while (n2 < this.size()) {
            MerchantRecipe merchantRecipe = (MerchantRecipe)this.get(n2);
            if (this.func_181078_a(itemStack, merchantRecipe.getItemToBuy()) && itemStack.stackSize >= merchantRecipe.getItemToBuy().stackSize && (!merchantRecipe.hasSecondItemToBuy() && itemStack2 == null || merchantRecipe.hasSecondItemToBuy() && this.func_181078_a(itemStack2, merchantRecipe.getSecondItemToBuy()) && itemStack2.stackSize >= merchantRecipe.getSecondItemToBuy().stackSize)) {
                return merchantRecipe;
            }
            ++n2;
        }
        return null;
    }

    public void writeToBuf(PacketBuffer packetBuffer) {
        packetBuffer.writeByte((byte)(this.size() & 0xFF));
        int n = 0;
        while (n < this.size()) {
            MerchantRecipe merchantRecipe = (MerchantRecipe)this.get(n);
            packetBuffer.writeItemStackToBuffer(merchantRecipe.getItemToBuy());
            packetBuffer.writeItemStackToBuffer(merchantRecipe.getItemToSell());
            ItemStack itemStack = merchantRecipe.getSecondItemToBuy();
            packetBuffer.writeBoolean(itemStack != null);
            if (itemStack != null) {
                packetBuffer.writeItemStackToBuffer(itemStack);
            }
            packetBuffer.writeBoolean(merchantRecipe.isRecipeDisabled());
            packetBuffer.writeInt(merchantRecipe.getToolUses());
            packetBuffer.writeInt(merchantRecipe.getMaxTradeUses());
            ++n;
        }
    }

    public MerchantRecipeList() {
    }

    public MerchantRecipeList(NBTTagCompound nBTTagCompound) {
        this.readRecipiesFromTags(nBTTagCompound);
    }

    public static MerchantRecipeList readFromBuf(PacketBuffer packetBuffer) throws IOException {
        MerchantRecipeList merchantRecipeList = new MerchantRecipeList();
        int n = packetBuffer.readByte() & 0xFF;
        int n2 = 0;
        while (n2 < n) {
            ItemStack itemStack = packetBuffer.readItemStackFromBuffer();
            ItemStack itemStack2 = packetBuffer.readItemStackFromBuffer();
            ItemStack itemStack3 = null;
            if (packetBuffer.readBoolean()) {
                itemStack3 = packetBuffer.readItemStackFromBuffer();
            }
            boolean bl = packetBuffer.readBoolean();
            int n3 = packetBuffer.readInt();
            int n4 = packetBuffer.readInt();
            MerchantRecipe merchantRecipe = new MerchantRecipe(itemStack, itemStack3, itemStack2, n3, n4);
            if (bl) {
                merchantRecipe.compensateToolUses();
            }
            merchantRecipeList.add(merchantRecipe);
            ++n2;
        }
        return merchantRecipeList;
    }

    private boolean func_181078_a(ItemStack itemStack, ItemStack itemStack2) {
        return ItemStack.areItemsEqual(itemStack, itemStack2) && (!itemStack2.hasTagCompound() || itemStack.hasTagCompound() && NBTUtil.func_181123_a(itemStack2.getTagCompound(), itemStack.getTagCompound(), false));
    }
}

