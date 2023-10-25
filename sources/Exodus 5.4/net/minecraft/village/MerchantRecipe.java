/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.village;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MerchantRecipe {
    private ItemStack itemToSell;
    private int toolUses;
    private ItemStack itemToBuy;
    private boolean rewardsExp;
    private ItemStack secondItemToBuy;
    private int maxTradeUses;

    public void readFromTags(NBTTagCompound nBTTagCompound) {
        NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("buy");
        this.itemToBuy = ItemStack.loadItemStackFromNBT(nBTTagCompound2);
        NBTTagCompound nBTTagCompound3 = nBTTagCompound.getCompoundTag("sell");
        this.itemToSell = ItemStack.loadItemStackFromNBT(nBTTagCompound3);
        if (nBTTagCompound.hasKey("buyB", 10)) {
            this.secondItemToBuy = ItemStack.loadItemStackFromNBT(nBTTagCompound.getCompoundTag("buyB"));
        }
        if (nBTTagCompound.hasKey("uses", 99)) {
            this.toolUses = nBTTagCompound.getInteger("uses");
        }
        this.maxTradeUses = nBTTagCompound.hasKey("maxUses", 99) ? nBTTagCompound.getInteger("maxUses") : 7;
        this.rewardsExp = nBTTagCompound.hasKey("rewardExp", 1) ? nBTTagCompound.getBoolean("rewardExp") : true;
    }

    public int getToolUses() {
        return this.toolUses;
    }

    public boolean isRecipeDisabled() {
        return this.toolUses >= this.maxTradeUses;
    }

    public ItemStack getItemToBuy() {
        return this.itemToBuy;
    }

    public void compensateToolUses() {
        this.toolUses = this.maxTradeUses;
    }

    public MerchantRecipe(ItemStack itemStack, Item item) {
        this(itemStack, new ItemStack(item));
    }

    public ItemStack getItemToSell() {
        return this.itemToSell;
    }

    public void increaseMaxTradeUses(int n) {
        this.maxTradeUses += n;
    }

    public void incrementToolUses() {
        ++this.toolUses;
    }

    public ItemStack getSecondItemToBuy() {
        return this.secondItemToBuy;
    }

    public boolean hasSecondItemToBuy() {
        return this.secondItemToBuy != null;
    }

    public MerchantRecipe(ItemStack itemStack, ItemStack itemStack2, ItemStack itemStack3, int n, int n2) {
        this.itemToBuy = itemStack;
        this.secondItemToBuy = itemStack2;
        this.itemToSell = itemStack3;
        this.toolUses = n;
        this.maxTradeUses = n2;
        this.rewardsExp = true;
    }

    public MerchantRecipe(ItemStack itemStack, ItemStack itemStack2) {
        this(itemStack, null, itemStack2);
    }

    public NBTTagCompound writeToTags() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        nBTTagCompound.setTag("buy", this.itemToBuy.writeToNBT(new NBTTagCompound()));
        nBTTagCompound.setTag("sell", this.itemToSell.writeToNBT(new NBTTagCompound()));
        if (this.secondItemToBuy != null) {
            nBTTagCompound.setTag("buyB", this.secondItemToBuy.writeToNBT(new NBTTagCompound()));
        }
        nBTTagCompound.setInteger("uses", this.toolUses);
        nBTTagCompound.setInteger("maxUses", this.maxTradeUses);
        nBTTagCompound.setBoolean("rewardExp", this.rewardsExp);
        return nBTTagCompound;
    }

    public MerchantRecipe(ItemStack itemStack, ItemStack itemStack2, ItemStack itemStack3) {
        this(itemStack, itemStack2, itemStack3, 0, 7);
    }

    public boolean getRewardsExp() {
        return this.rewardsExp;
    }

    public int getMaxTradeUses() {
        return this.maxTradeUses;
    }

    public MerchantRecipe(NBTTagCompound nBTTagCompound) {
        this.readFromTags(nBTTagCompound);
    }
}

