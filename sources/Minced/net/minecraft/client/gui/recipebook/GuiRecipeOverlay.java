// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.recipebook;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import java.util.Collections;
import net.minecraft.stats.RecipeBook;
import com.google.common.collect.Lists;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;

public class GuiRecipeOverlay extends Gui
{
    private static final ResourceLocation RECIPE_BOOK_TEXTURE;
    private final List<Button> buttonList;
    private boolean visible;
    private int x;
    private int y;
    private Minecraft mc;
    private RecipeList recipeList;
    private IRecipe lastRecipeClicked;
    private float time;
    
    public GuiRecipeOverlay() {
        this.buttonList = (List<Button>)Lists.newArrayList();
    }
    
    public void init(final Minecraft mcIn, final RecipeList recipeListIn, final int p_191845_3_, final int p_191845_4_, final int p_191845_5_, final int p_191845_6_, final float p_191845_7_, final RecipeBook p_191845_8_) {
        this.mc = mcIn;
        this.recipeList = recipeListIn;
        final boolean flag = p_191845_8_.isFilteringCraftable();
        final List<IRecipe> list = recipeListIn.getDisplayRecipes(true);
        final List<IRecipe> list2 = flag ? Collections.emptyList() : recipeListIn.getDisplayRecipes(false);
        final int i = list.size();
        final int j = i + list2.size();
        final int k = (j <= 16) ? 4 : 5;
        final int l = (int)Math.ceil(j / (float)k);
        this.x = p_191845_3_;
        this.y = p_191845_4_;
        final int i2 = 25;
        final float f = (float)(this.x + Math.min(j, k) * 25);
        final float f2 = (float)(p_191845_5_ + 50);
        if (f > f2) {
            this.x -= (int)(p_191845_7_ * (int)((f - f2) / p_191845_7_));
        }
        final float f3 = (float)(this.y + l * 25);
        final float f4 = (float)(p_191845_6_ + 50);
        if (f3 > f4) {
            this.y -= (int)(p_191845_7_ * MathHelper.ceil((f3 - f4) / p_191845_7_));
        }
        final float f5 = (float)this.y;
        final float f6 = (float)(p_191845_6_ - 100);
        if (f5 < f6) {
            this.y -= (int)(p_191845_7_ * MathHelper.ceil((f5 - f6) / p_191845_7_));
        }
        this.visible = true;
        this.buttonList.clear();
        for (int j2 = 0; j2 < j; ++j2) {
            final boolean flag2 = j2 < i;
            this.buttonList.add(new Button(this.x + 4 + 25 * (j2 % k), this.y + 5 + 25 * (j2 / k), flag2 ? list.get(j2) : list2.get(j2 - i), flag2));
        }
        this.lastRecipeClicked = null;
    }
    
    public RecipeList getRecipeList() {
        return this.recipeList;
    }
    
    public IRecipe getLastRecipeClicked() {
        return this.lastRecipeClicked;
    }
    
    public boolean buttonClicked(final int p_193968_1_, final int p_193968_2_, final int p_193968_3_) {
        if (p_193968_3_ != 0) {
            return false;
        }
        for (final Button guirecipeoverlay$button : this.buttonList) {
            if (guirecipeoverlay$button.mousePressed(this.mc, p_193968_1_, p_193968_2_)) {
                this.lastRecipeClicked = guirecipeoverlay$button.recipe;
                return true;
            }
        }
        return false;
    }
    
    public void render(final int p_191842_1_, final int p_191842_2_, final float p_191842_3_) {
        if (this.visible) {
            this.time += p_191842_3_;
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(GuiRecipeOverlay.RECIPE_BOOK_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, 170.0f);
            final int i = (this.buttonList.size() <= 16) ? 4 : 5;
            final int j = Math.min(this.buttonList.size(), i);
            final int k = MathHelper.ceil(this.buttonList.size() / (float)i);
            final int l = 24;
            final int i2 = 4;
            final int j2 = 82;
            final int k2 = 208;
            this.nineInchSprite(j, k, 24, 4, 82, 208);
            GlStateManager.disableBlend();
            RenderHelper.disableStandardItemLighting();
            for (final Button guirecipeoverlay$button : this.buttonList) {
                guirecipeoverlay$button.drawButton(this.mc, p_191842_1_, p_191842_2_, p_191842_3_);
            }
            GlStateManager.popMatrix();
        }
    }
    
    private void nineInchSprite(final int p_191846_1_, final int p_191846_2_, final int p_191846_3_, final int p_191846_4_, final int p_191846_5_, final int p_191846_6_) {
        this.drawTexturedModalRect(this.x, this.y, p_191846_5_, p_191846_6_, p_191846_4_, p_191846_4_);
        this.drawTexturedModalRect(this.x + p_191846_4_ * 2 + p_191846_1_ * p_191846_3_, this.y, p_191846_5_ + p_191846_3_ + p_191846_4_, p_191846_6_, p_191846_4_, p_191846_4_);
        this.drawTexturedModalRect(this.x, this.y + p_191846_4_ * 2 + p_191846_2_ * p_191846_3_, p_191846_5_, p_191846_6_ + p_191846_3_ + p_191846_4_, p_191846_4_, p_191846_4_);
        this.drawTexturedModalRect(this.x + p_191846_4_ * 2 + p_191846_1_ * p_191846_3_, this.y + p_191846_4_ * 2 + p_191846_2_ * p_191846_3_, p_191846_5_ + p_191846_3_ + p_191846_4_, p_191846_6_ + p_191846_3_ + p_191846_4_, p_191846_4_, p_191846_4_);
        for (int i = 0; i < p_191846_1_; ++i) {
            this.drawTexturedModalRect(this.x + p_191846_4_ + i * p_191846_3_, this.y, p_191846_5_ + p_191846_4_, p_191846_6_, p_191846_3_, p_191846_4_);
            this.drawTexturedModalRect(this.x + p_191846_4_ + (i + 1) * p_191846_3_, this.y, p_191846_5_ + p_191846_4_, p_191846_6_, p_191846_4_, p_191846_4_);
            for (int j = 0; j < p_191846_2_; ++j) {
                if (i == 0) {
                    this.drawTexturedModalRect(this.x, this.y + p_191846_4_ + j * p_191846_3_, p_191846_5_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_3_);
                    this.drawTexturedModalRect(this.x, this.y + p_191846_4_ + (j + 1) * p_191846_3_, p_191846_5_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_4_);
                }
                this.drawTexturedModalRect(this.x + p_191846_4_ + i * p_191846_3_, this.y + p_191846_4_ + j * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_3_, p_191846_3_);
                this.drawTexturedModalRect(this.x + p_191846_4_ + (i + 1) * p_191846_3_, this.y + p_191846_4_ + j * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_3_);
                this.drawTexturedModalRect(this.x + p_191846_4_ + i * p_191846_3_, this.y + p_191846_4_ + (j + 1) * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_3_, p_191846_4_);
                this.drawTexturedModalRect(this.x + p_191846_4_ + (i + 1) * p_191846_3_ - 1, this.y + p_191846_4_ + (j + 1) * p_191846_3_ - 1, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_4_ + 1, p_191846_4_ + 1);
                if (i == p_191846_1_ - 1) {
                    this.drawTexturedModalRect(this.x + p_191846_4_ * 2 + p_191846_1_ * p_191846_3_, this.y + p_191846_4_ + j * p_191846_3_, p_191846_5_ + p_191846_3_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_3_);
                    this.drawTexturedModalRect(this.x + p_191846_4_ * 2 + p_191846_1_ * p_191846_3_, this.y + p_191846_4_ + (j + 1) * p_191846_3_, p_191846_5_ + p_191846_3_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_4_);
                }
            }
            this.drawTexturedModalRect(this.x + p_191846_4_ + i * p_191846_3_, this.y + p_191846_4_ * 2 + p_191846_2_ * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_3_ + p_191846_4_, p_191846_3_, p_191846_4_);
            this.drawTexturedModalRect(this.x + p_191846_4_ + (i + 1) * p_191846_3_, this.y + p_191846_4_ * 2 + p_191846_2_ * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_3_ + p_191846_4_, p_191846_4_, p_191846_4_);
        }
    }
    
    public void setVisible(final boolean p_192999_1_) {
        this.visible = p_192999_1_;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    static {
        RECIPE_BOOK_TEXTURE = new ResourceLocation("textures/gui/recipe_book.png");
    }
    
    class Button extends GuiButton
    {
        private final IRecipe recipe;
        private final boolean isCraftable;
        
        public Button(final int p_i47594_2_, final int p_i47594_3_, final IRecipe p_i47594_4_, final boolean p_i47594_5_) {
            super(0, p_i47594_2_, p_i47594_3_, "");
            this.width = 24;
            this.height = 24;
            this.recipe = p_i47594_4_;
            this.isCraftable = p_i47594_5_;
        }
        
        @Override
        public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableAlpha();
            mc.getTextureManager().bindTexture(GuiRecipeOverlay.RECIPE_BOOK_TEXTURE);
            this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
            int i = 152;
            if (!this.isCraftable) {
                i += 26;
            }
            int j = 78;
            if (this.hovered) {
                j += 26;
            }
            this.drawTexturedModalRect(this.x, this.y, i, j, this.width, this.height);
            int k = 3;
            int l = 3;
            if (this.recipe instanceof ShapedRecipes) {
                final ShapedRecipes shapedrecipes = (ShapedRecipes)this.recipe;
                k = shapedrecipes.getWidth();
                l = shapedrecipes.getHeight();
            }
            final Iterator<Ingredient> iterator = this.recipe.getIngredients().iterator();
            for (int i2 = 0; i2 < l; ++i2) {
                final int j2 = 3 + i2 * 7;
                for (int k2 = 0; k2 < k; ++k2) {
                    if (iterator.hasNext()) {
                        final ItemStack[] aitemstack = iterator.next().getMatchingStacks();
                        if (aitemstack.length != 0) {
                            final int l2 = 3 + k2 * 7;
                            GlStateManager.pushMatrix();
                            final float f = 0.42f;
                            final int i3 = (int)((this.x + l2) / 0.42f - 3.0f);
                            final int j3 = (int)((this.y + j2) / 0.42f - 3.0f);
                            GlStateManager.scale(0.42f, 0.42f, 1.0f);
                            GlStateManager.enableLighting();
                            mc.getRenderItem().renderItemAndEffectIntoGUI(aitemstack[MathHelper.floor(GuiRecipeOverlay.this.time / 30.0f) % aitemstack.length], i3, j3);
                            GlStateManager.disableLighting();
                            GlStateManager.popMatrix();
                        }
                    }
                }
            }
            GlStateManager.disableAlpha();
            RenderHelper.disableStandardItemLighting();
        }
    }
}
