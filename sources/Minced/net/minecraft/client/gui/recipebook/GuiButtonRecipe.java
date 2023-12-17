// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.recipebook;

import net.minecraft.client.resources.I18n;
import java.util.Collection;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonRecipe extends GuiButton
{
    private static final ResourceLocation RECIPE_BOOK;
    private RecipeBook book;
    private RecipeList list;
    private float time;
    private float animationTime;
    private int currentIndex;
    
    public GuiButtonRecipe() {
        super(0, 0, 0, 25, 25, "");
    }
    
    public void init(final RecipeList p_193928_1_, final RecipeBookPage p_193928_2_, final RecipeBook p_193928_3_) {
        this.list = p_193928_1_;
        this.book = p_193928_3_;
        final List<IRecipe> list = p_193928_1_.getRecipes(p_193928_3_.isFilteringCraftable());
        for (final IRecipe irecipe : list) {
            if (p_193928_3_.isNew(irecipe)) {
                p_193928_2_.recipesShown(list);
                this.animationTime = 15.0f;
                break;
            }
        }
    }
    
    public RecipeList getList() {
        return this.list;
    }
    
    public void setPosition(final int p_191770_1_, final int p_191770_2_) {
        this.x = p_191770_1_;
        this.y = p_191770_2_;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
        if (this.visible) {
            if (!GuiScreen.isCtrlKeyDown()) {
                this.time += partialTicks;
            }
            this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
            RenderHelper.enableGUIStandardItemLighting();
            mc.getTextureManager().bindTexture(GuiButtonRecipe.RECIPE_BOOK);
            GlStateManager.disableLighting();
            int i = 29;
            if (!this.list.containsCraftableRecipes()) {
                i += 25;
            }
            int j = 206;
            if (this.list.getRecipes(this.book.isFilteringCraftable()).size() > 1) {
                j += 25;
            }
            final boolean flag = this.animationTime > 0.0f;
            if (flag) {
                final float f = 1.0f + 0.1f * (float)Math.sin(this.animationTime / 15.0f * 3.1415927f);
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(this.x + 8), (float)(this.y + 12), 0.0f);
                GlStateManager.scale(f, f, 1.0f);
                GlStateManager.translate((float)(-(this.x + 8)), (float)(-(this.y + 12)), 0.0f);
                this.animationTime -= partialTicks;
            }
            this.drawTexturedModalRect(this.x, this.y, i, j, this.width, this.height);
            final List<IRecipe> list = this.getOrderedRecipes();
            this.currentIndex = MathHelper.floor(this.time / 30.0f) % list.size();
            final ItemStack itemstack = list.get(this.currentIndex).getRecipeOutput();
            int k = 4;
            if (this.list.hasSingleResultItem() && this.getOrderedRecipes().size() > 1) {
                mc.getRenderItem().renderItemAndEffectIntoGUI(itemstack, this.x + k + 1, this.y + k + 1);
                --k;
            }
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemstack, this.x + k, this.y + k);
            if (flag) {
                GlStateManager.popMatrix();
            }
            GlStateManager.enableLighting();
            RenderHelper.disableStandardItemLighting();
        }
    }
    
    private List<IRecipe> getOrderedRecipes() {
        final List<IRecipe> list = this.list.getDisplayRecipes(true);
        if (!this.book.isFilteringCraftable()) {
            list.addAll(this.list.getDisplayRecipes(false));
        }
        return list;
    }
    
    public boolean isOnlyOption() {
        return this.getOrderedRecipes().size() == 1;
    }
    
    public IRecipe getRecipe() {
        final List<IRecipe> list = this.getOrderedRecipes();
        return list.get(this.currentIndex);
    }
    
    public List<String> getToolTipText(final GuiScreen p_191772_1_) {
        final ItemStack itemstack = this.getOrderedRecipes().get(this.currentIndex).getRecipeOutput();
        final List<String> list = p_191772_1_.getItemToolTip(itemstack);
        if (this.list.getRecipes(this.book.isFilteringCraftable()).size() > 1) {
            list.add(I18n.format("gui.recipebook.moreRecipes", new Object[0]));
        }
        return list;
    }
    
    @Override
    public int getButtonWidth() {
        return 25;
    }
    
    static {
        RECIPE_BOOK = new ResourceLocation("textures/gui/recipe_book.png");
    }
}
