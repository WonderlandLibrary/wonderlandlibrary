/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class NpcMerchant
implements IMerchant {
    private MerchantRecipeList recipeList;
    private InventoryMerchant theMerchantInventory;
    private EntityPlayer customer;
    private IChatComponent field_175548_d;

    @Override
    public IChatComponent getDisplayName() {
        return this.field_175548_d != null ? this.field_175548_d : new ChatComponentTranslation("entity.Villager.name", new Object[0]);
    }

    @Override
    public EntityPlayer getCustomer() {
        return this.customer;
    }

    @Override
    public void setRecipes(MerchantRecipeList merchantRecipeList) {
        this.recipeList = merchantRecipeList;
    }

    @Override
    public void useRecipe(MerchantRecipe merchantRecipe) {
        merchantRecipe.incrementToolUses();
    }

    @Override
    public MerchantRecipeList getRecipes(EntityPlayer entityPlayer) {
        return this.recipeList;
    }

    @Override
    public void verifySellingItem(ItemStack itemStack) {
    }

    @Override
    public void setCustomer(EntityPlayer entityPlayer) {
    }

    public NpcMerchant(EntityPlayer entityPlayer, IChatComponent iChatComponent) {
        this.customer = entityPlayer;
        this.field_175548_d = iChatComponent;
        this.theMerchantInventory = new InventoryMerchant(entityPlayer, this);
    }
}

