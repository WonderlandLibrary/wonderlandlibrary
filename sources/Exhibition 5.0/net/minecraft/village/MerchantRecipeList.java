// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.village;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import java.util.ArrayList;

public class MerchantRecipeList extends ArrayList
{
    private static final long serialVersionUID = 1L;
    private static final String __OBFID = "CL_00000127";
    
    public MerchantRecipeList() {
    }
    
    public MerchantRecipeList(final NBTTagCompound p_i1944_1_) {
        this.readRecipiesFromTags(p_i1944_1_);
    }
    
    public MerchantRecipe canRecipeBeUsed(final ItemStack p_77203_1_, final ItemStack p_77203_2_, final int p_77203_3_) {
        if (p_77203_3_ > 0 && p_77203_3_ < this.size()) {
            final MerchantRecipe var6 = this.get(p_77203_3_);
            return (ItemStack.areItemsEqual(p_77203_1_, var6.getItemToBuy()) && ((p_77203_2_ == null && !var6.hasSecondItemToBuy()) || (var6.hasSecondItemToBuy() && ItemStack.areItemsEqual(p_77203_2_, var6.getSecondItemToBuy()))) && p_77203_1_.stackSize >= var6.getItemToBuy().stackSize && (!var6.hasSecondItemToBuy() || p_77203_2_.stackSize >= var6.getSecondItemToBuy().stackSize)) ? var6 : null;
        }
        for (int var7 = 0; var7 < this.size(); ++var7) {
            final MerchantRecipe var8 = this.get(var7);
            if (ItemStack.areItemsEqual(p_77203_1_, var8.getItemToBuy()) && p_77203_1_.stackSize >= var8.getItemToBuy().stackSize && ((!var8.hasSecondItemToBuy() && p_77203_2_ == null) || (var8.hasSecondItemToBuy() && ItemStack.areItemsEqual(p_77203_2_, var8.getSecondItemToBuy()) && p_77203_2_.stackSize >= var8.getSecondItemToBuy().stackSize))) {
                return var8;
            }
        }
        return null;
    }
    
    public void func_151391_a(final PacketBuffer p_151391_1_) {
        p_151391_1_.writeByte((byte)(this.size() & 0xFF));
        for (int var2 = 0; var2 < this.size(); ++var2) {
            final MerchantRecipe var3 = this.get(var2);
            p_151391_1_.writeItemStackToBuffer(var3.getItemToBuy());
            p_151391_1_.writeItemStackToBuffer(var3.getItemToSell());
            final ItemStack var4 = var3.getSecondItemToBuy();
            p_151391_1_.writeBoolean(var4 != null);
            if (var4 != null) {
                p_151391_1_.writeItemStackToBuffer(var4);
            }
            p_151391_1_.writeBoolean(var3.isRecipeDisabled());
            p_151391_1_.writeInt(var3.func_180321_e());
            p_151391_1_.writeInt(var3.func_180320_f());
        }
    }
    
    public static MerchantRecipeList func_151390_b(final PacketBuffer p_151390_0_) throws IOException {
        final MerchantRecipeList var1 = new MerchantRecipeList();
        for (int var2 = p_151390_0_.readByte() & 0xFF, var3 = 0; var3 < var2; ++var3) {
            final ItemStack var4 = p_151390_0_.readItemStackFromBuffer();
            final ItemStack var5 = p_151390_0_.readItemStackFromBuffer();
            ItemStack var6 = null;
            if (p_151390_0_.readBoolean()) {
                var6 = p_151390_0_.readItemStackFromBuffer();
            }
            final boolean var7 = p_151390_0_.readBoolean();
            final int var8 = p_151390_0_.readInt();
            final int var9 = p_151390_0_.readInt();
            final MerchantRecipe var10 = new MerchantRecipe(var4, var6, var5, var8, var9);
            if (var7) {
                var10.func_82785_h();
            }
            var1.add(var10);
        }
        return var1;
    }
    
    public void readRecipiesFromTags(final NBTTagCompound p_77201_1_) {
        final NBTTagList var2 = p_77201_1_.getTagList("Recipes", 10);
        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            this.add(new MerchantRecipe(var4));
        }
    }
    
    public NBTTagCompound getRecipiesAsTags() {
        final NBTTagCompound var1 = new NBTTagCompound();
        final NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.size(); ++var3) {
            final MerchantRecipe var4 = this.get(var3);
            var2.appendTag(var4.writeToTags());
        }
        var1.setTag("Recipes", var2);
        return var1;
    }
}
