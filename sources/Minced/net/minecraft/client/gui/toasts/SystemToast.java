// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.toasts;

import net.minecraft.client.renderer.GlStateManager;
import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;

public class SystemToast implements IToast
{
    private final Type type;
    private String title;
    private String subtitle;
    private long firstDrawTime;
    private boolean newDisplay;
    
    public SystemToast(final Type typeIn, final ITextComponent titleComponent, @Nullable final ITextComponent subtitleComponent) {
        this.type = typeIn;
        this.title = titleComponent.getUnformattedText();
        this.subtitle = ((subtitleComponent == null) ? null : subtitleComponent.getUnformattedText());
    }
    
    @Override
    public Visibility draw(final GuiToast toastGui, final long delta) {
        if (this.newDisplay) {
            this.firstDrawTime = delta;
            this.newDisplay = false;
        }
        toastGui.getMinecraft().getTextureManager().bindTexture(SystemToast.TEXTURE_TOASTS);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        toastGui.drawTexturedModalRect(0, 0, 0, 64, 160, 32);
        if (this.subtitle == null) {
            toastGui.getMinecraft().fontRenderer.drawString(this.title, 18, 12, -256);
        }
        else {
            toastGui.getMinecraft().fontRenderer.drawString(this.title, 18, 7, -256);
            toastGui.getMinecraft().fontRenderer.drawString(this.subtitle, 18, 18, -1);
        }
        return (delta - this.firstDrawTime < 5000L) ? Visibility.SHOW : Visibility.HIDE;
    }
    
    public void setDisplayedText(final ITextComponent titleComponent, @Nullable final ITextComponent subtitleComponent) {
        this.title = titleComponent.getUnformattedText();
        this.subtitle = ((subtitleComponent == null) ? null : subtitleComponent.getUnformattedText());
        this.newDisplay = true;
    }
    
    @Override
    public Type getType() {
        return this.type;
    }
    
    public static void addOrUpdate(final GuiToast p_193657_0_, final Type p_193657_1_, final ITextComponent p_193657_2_, @Nullable final ITextComponent p_193657_3_) {
        final SystemToast systemtoast = p_193657_0_.getToast((Class<? extends SystemToast>)SystemToast.class, (Object)p_193657_1_);
        if (systemtoast == null) {
            p_193657_0_.add(new SystemToast(p_193657_1_, p_193657_2_, p_193657_3_));
        }
        else {
            systemtoast.setDisplayedText(p_193657_2_, p_193657_3_);
        }
    }
    
    public enum Type
    {
        TUTORIAL_HINT;
    }
}
