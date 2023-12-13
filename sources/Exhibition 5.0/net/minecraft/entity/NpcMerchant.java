// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryMerchant;

public class NpcMerchant implements IMerchant
{
    private InventoryMerchant theMerchantInventory;
    private EntityPlayer customer;
    private MerchantRecipeList recipeList;
    private IChatComponent field_175548_d;
    private static final String __OBFID = "CL_00001705";
    
    public NpcMerchant(final EntityPlayer p_i45817_1_, final IChatComponent p_i45817_2_) {
        this.customer = p_i45817_1_;
        this.field_175548_d = p_i45817_2_;
        this.theMerchantInventory = new InventoryMerchant(p_i45817_1_, this);
    }
    
    @Override
    public EntityPlayer getCustomer() {
        return this.customer;
    }
    
    @Override
    public void setCustomer(final EntityPlayer p_70932_1_) {
    }
    
    @Override
    public MerchantRecipeList getRecipes(final EntityPlayer p_70934_1_) {
        return this.recipeList;
    }
    
    @Override
    public void setRecipes(final MerchantRecipeList p_70930_1_) {
        this.recipeList = p_70930_1_;
    }
    
    @Override
    public void useRecipe(final MerchantRecipe p_70933_1_) {
        p_70933_1_.incrementToolUses();
    }
    
    @Override
    public void verifySellingItem(final ItemStack p_110297_1_) {
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return (this.field_175548_d != null) ? this.field_175548_d : new ChatComponentTranslation("entity.Villager.name", new Object[0]);
    }
}
