// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.toasts;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.GlStateManager;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import java.util.List;

public class RecipeToast implements IToast
{
    private final List<ItemStack> recipesOutputs;
    private long firstDrawTime;
    private boolean hasNewOutputs;
    
    public RecipeToast(final ItemStack p_i47489_1_) {
        (this.recipesOutputs = (List<ItemStack>)Lists.newArrayList()).add(p_i47489_1_);
    }
    
    @Override
    public Visibility draw(final GuiToast toastGui, final long delta) {
        if (this.hasNewOutputs) {
            this.firstDrawTime = delta;
            this.hasNewOutputs = false;
        }
        if (this.recipesOutputs.isEmpty()) {
            return Visibility.HIDE;
        }
        toastGui.getMinecraft().getTextureManager().bindTexture(RecipeToast.TEXTURE_TOASTS);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        toastGui.drawTexturedModalRect(0, 0, 0, 32, 160, 32);
        toastGui.getMinecraft().fontRenderer.drawString(I18n.format("recipe.toast.title", new Object[0]), 30, 7, -11534256);
        toastGui.getMinecraft().fontRenderer.drawString(I18n.format("recipe.toast.description", new Object[0]), 30, 18, -16777216);
        RenderHelper.enableGUIStandardItemLighting();
        toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(null, this.recipesOutputs.get((int)(delta / (5000L / this.recipesOutputs.size()) % this.recipesOutputs.size())), 8, 8);
        return (delta - this.firstDrawTime >= 5000L) ? Visibility.HIDE : Visibility.SHOW;
    }
    
    public void addRecipeOutput(final ItemStack output) {
        if (this.recipesOutputs.add(output)) {
            this.hasNewOutputs = true;
        }
    }
    
    public static void addOrUpdate(final GuiToast p_193665_0_, final IRecipe p_193665_1_) {
        final RecipeToast recipetoast = p_193665_0_.getToast((Class<? extends RecipeToast>)RecipeToast.class, RecipeToast.NO_TOKEN);
        if (recipetoast == null) {
            p_193665_0_.add(new RecipeToast(p_193665_1_.getRecipeOutput()));
        }
        else {
            recipetoast.addRecipeOutput(p_193665_1_.getRecipeOutput());
        }
    }
}
