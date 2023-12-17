// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.recipebook;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import javax.annotation.Nullable;
import net.minecraft.item.crafting.Ingredient;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.item.crafting.IRecipe;

public class GhostRecipe
{
    private IRecipe recipe;
    private final List<GhostIngredient> ingredients;
    private float time;
    
    public GhostRecipe() {
        this.ingredients = (List<GhostIngredient>)Lists.newArrayList();
    }
    
    public void clear() {
        this.recipe = null;
        this.ingredients.clear();
        this.time = 0.0f;
    }
    
    public void addIngredient(final Ingredient p_194187_1_, final int p_194187_2_, final int p_194187_3_) {
        this.ingredients.add(new GhostIngredient(p_194187_1_, p_194187_2_, p_194187_3_));
    }
    
    public GhostIngredient get(final int p_192681_1_) {
        return this.ingredients.get(p_192681_1_);
    }
    
    public int size() {
        return this.ingredients.size();
    }
    
    @Nullable
    public IRecipe getRecipe() {
        return this.recipe;
    }
    
    public void setRecipe(final IRecipe p_192685_1_) {
        this.recipe = p_192685_1_;
    }
    
    public void render(final Minecraft p_194188_1_, final int p_194188_2_, final int p_194188_3_, final boolean p_194188_4_, final float p_194188_5_) {
        if (!GuiScreen.isCtrlKeyDown()) {
            this.time += p_194188_5_;
        }
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        for (int i = 0; i < this.ingredients.size(); ++i) {
            final GhostIngredient ghostrecipe$ghostingredient = this.ingredients.get(i);
            final int j = ghostrecipe$ghostingredient.getX() + p_194188_2_;
            final int k = ghostrecipe$ghostingredient.getY() + p_194188_3_;
            if (i == 0 && p_194188_4_) {
                Gui.drawRect((float)(j - 4), (float)(k - 4), (float)(j + 20), (float)(k + 20), 822018048);
            }
            else {
                Gui.drawRect((float)j, (float)k, (float)(j + 16), (float)(k + 16), 822018048);
            }
            GlStateManager.disableLighting();
            final ItemStack itemstack = ghostrecipe$ghostingredient.getItem();
            final RenderItem renderitem = p_194188_1_.getRenderItem();
            renderitem.renderItemAndEffectIntoGUI(Minecraft.player, itemstack, j, k);
            GlStateManager.depthFunc(516);
            Gui.drawRect((float)j, (float)k, (float)(j + 16), (float)(k + 16), 822083583);
            GlStateManager.depthFunc(515);
            if (i == 0) {
                renderitem.renderItemOverlays(p_194188_1_.fontRenderer, itemstack, j, k);
            }
            GlStateManager.enableLighting();
        }
        RenderHelper.disableStandardItemLighting();
    }
    
    public class GhostIngredient
    {
        private final Ingredient ingredient;
        private final int x;
        private final int y;
        
        public GhostIngredient(final Ingredient p_i47604_2_, final int p_i47604_3_, final int p_i47604_4_) {
            this.ingredient = p_i47604_2_;
            this.x = p_i47604_3_;
            this.y = p_i47604_4_;
        }
        
        public int getX() {
            return this.x;
        }
        
        public int getY() {
            return this.y;
        }
        
        public ItemStack getItem() {
            final ItemStack[] aitemstack = this.ingredient.getMatchingStacks();
            return aitemstack[MathHelper.floor(GhostRecipe.this.time / 30.0f) % aitemstack.length];
        }
    }
}
