// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.toasts;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;

public class TutorialToast implements IToast
{
    private final Icons icon;
    private final String title;
    private final String subtitle;
    private Visibility visibility;
    private long lastDelta;
    private float displayedProgress;
    private float currentProgress;
    private final boolean hasProgressBar;
    
    public TutorialToast(final Icons iconIn, final ITextComponent titleComponent, @Nullable final ITextComponent subtitleComponent, final boolean drawProgressBar) {
        this.visibility = Visibility.SHOW;
        this.icon = iconIn;
        this.title = titleComponent.getFormattedText();
        this.subtitle = ((subtitleComponent == null) ? null : subtitleComponent.getFormattedText());
        this.hasProgressBar = drawProgressBar;
    }
    
    @Override
    public Visibility draw(final GuiToast toastGui, final long delta) {
        toastGui.getMinecraft().getTextureManager().bindTexture(TutorialToast.TEXTURE_TOASTS);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        toastGui.drawTexturedModalRect(0, 0, 0, 96, 160, 32);
        this.icon.draw(toastGui, 6, 6);
        if (this.subtitle == null) {
            toastGui.getMinecraft().fontRenderer.drawString(this.title, 30, 12, -11534256);
        }
        else {
            toastGui.getMinecraft().fontRenderer.drawString(this.title, 30, 7, -11534256);
            toastGui.getMinecraft().fontRenderer.drawString(this.subtitle, 30, 18, -16777216);
        }
        if (this.hasProgressBar) {
            Gui.drawRect(3.0f, 28.0f, 157.0f, 29.0f, -1);
            final float f = (float)MathHelper.clampedLerp(this.displayedProgress, this.currentProgress, (delta - this.lastDelta) / 100.0f);
            int i;
            if (this.currentProgress >= this.displayedProgress) {
                i = -16755456;
            }
            else {
                i = -11206656;
            }
            Gui.drawRect(3.0f, 28.0f, (float)(int)(3.0f + 154.0f * f), 29.0f, i);
            this.displayedProgress = f;
            this.lastDelta = delta;
        }
        return this.visibility;
    }
    
    public void hide() {
        this.visibility = Visibility.HIDE;
    }
    
    public void setProgress(final float progress) {
        this.currentProgress = progress;
    }
    
    public enum Icons
    {
        MOVEMENT_KEYS(0, 0), 
        MOUSE(1, 0), 
        TREE(2, 0), 
        RECIPE_BOOK(0, 1), 
        WOODEN_PLANKS(1, 1);
        
        private final int column;
        private final int row;
        
        private Icons(final int columnIn, final int rowIn) {
            this.column = columnIn;
            this.row = rowIn;
        }
        
        public void draw(final Gui guiIn, final int x, final int y) {
            GlStateManager.enableBlend();
            guiIn.drawTexturedModalRect(x, y, 176 + this.column * 20, this.row * 20, 20, 20);
            GlStateManager.enableBlend();
        }
    }
}
