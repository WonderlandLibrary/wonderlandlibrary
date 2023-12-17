// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.recipebook;

import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import net.minecraft.stats.RecipeBook;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.client.util.RecipeBookClient;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.gui.GuiButtonToggle;

public class GuiButtonRecipeTab extends GuiButtonToggle
{
    private final CreativeTabs category;
    private float animationTime;
    
    public GuiButtonRecipeTab(final int p_i47588_1_, final CreativeTabs p_i47588_2_) {
        super(p_i47588_1_, 0, 0, 35, 27, false);
        this.category = p_i47588_2_;
        this.initTextureValues(153, 2, 35, 0, GuiRecipeBook.RECIPE_BOOK);
    }
    
    public void startAnimation(final Minecraft p_193918_1_) {
        final RecipeBook recipebook = Minecraft.player.getRecipeBook();
        for (final RecipeList recipelist : RecipeBookClient.RECIPES_BY_TAB.get(this.category)) {
            for (final IRecipe irecipe : recipelist.getRecipes(recipebook.isFilteringCraftable())) {
                if (recipebook.isNew(irecipe)) {
                    this.animationTime = 15.0f;
                }
            }
        }
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
        if (this.visible) {
            if (this.animationTime > 0.0f) {
                final float f = 1.0f + 0.1f * (float)Math.sin(this.animationTime / 15.0f * 3.1415927f);
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(this.x + 8), (float)(this.y + 12), 0.0f);
                GlStateManager.scale(1.0f, f, 1.0f);
                GlStateManager.translate((float)(-(this.x + 8)), (float)(-(this.y + 12)), 0.0f);
            }
            this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
            mc.getTextureManager().bindTexture(this.resourceLocation);
            GlStateManager.disableDepth();
            int k = this.xTexStart;
            int i = this.yTexStart;
            if (this.stateTriggered) {
                k += this.xDiffTex;
            }
            if (this.hovered) {
                i += this.yDiffTex;
            }
            int j = this.x;
            if (this.stateTriggered) {
                j -= 2;
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(j, this.y, k, i, this.width, this.height);
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            this.renderIcon(mc.getRenderItem());
            GlStateManager.enableLighting();
            RenderHelper.disableStandardItemLighting();
            if (this.animationTime > 0.0f) {
                GlStateManager.popMatrix();
                this.animationTime -= partialTicks;
            }
        }
    }
    
    private void renderIcon(final RenderItem p_193920_1_) {
        final ItemStack itemstack = this.category.getIcon();
        if (this.category == CreativeTabs.TOOLS) {
            p_193920_1_.renderItemAndEffectIntoGUI(itemstack, this.x + 3, this.y + 5);
            p_193920_1_.renderItemAndEffectIntoGUI(CreativeTabs.COMBAT.getIcon(), this.x + 14, this.y + 5);
        }
        else if (this.category == CreativeTabs.MISC) {
            p_193920_1_.renderItemAndEffectIntoGUI(itemstack, this.x + 3, this.y + 5);
            p_193920_1_.renderItemAndEffectIntoGUI(CreativeTabs.FOOD.getIcon(), this.x + 14, this.y + 5);
        }
        else {
            p_193920_1_.renderItemAndEffectIntoGUI(itemstack, this.x + 9, this.y + 5);
        }
    }
    
    public CreativeTabs getCategory() {
        return this.category;
    }
    
    public boolean updateVisibility() {
        final List<RecipeList> list = RecipeBookClient.RECIPES_BY_TAB.get(this.category);
        this.visible = false;
        for (final RecipeList recipelist : list) {
            if (recipelist.isNotEmpty() && recipelist.containsValidRecipes()) {
                this.visible = true;
                break;
            }
        }
        return this.visible;
    }
}
