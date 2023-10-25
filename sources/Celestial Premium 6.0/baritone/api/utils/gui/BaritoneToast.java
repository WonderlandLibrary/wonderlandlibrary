/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils.gui;

import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BaritoneToast
implements IToast {
    private String title;
    private String subtitle;
    private long firstDrawTime;
    private boolean newDisplay;
    private long totalShowTime;

    public BaritoneToast(ITextComponent titleComponent, ITextComponent subtitleComponent, long totalShowTime) {
        this.title = titleComponent.getFormattedText();
        this.subtitle = subtitleComponent == null ? null : subtitleComponent.getFormattedText();
        this.totalShowTime = totalShowTime;
    }

    @Override
    public IToast.Visibility draw(GuiToast toastGui, long delta) {
        if (this.newDisplay) {
            this.firstDrawTime = delta;
            this.newDisplay = false;
        }
        toastGui.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/toasts.png"));
        GlStateManager.color(1.0f, 1.0f, 1.0f, 255.0f);
        toastGui.drawTexturedModalRect(0, 0, 0, 32, 160, 32);
        if (this.subtitle == null) {
            toastGui.getMinecraft().fontRenderer.drawString(this.title, 18.0f, 12.0f, -11534256);
        } else {
            toastGui.getMinecraft().fontRenderer.drawString(this.title, 18.0f, 7.0f, -11534256);
            toastGui.getMinecraft().fontRenderer.drawString(this.subtitle, 18.0f, 18.0f, -16777216);
        }
        return delta - this.firstDrawTime < this.totalShowTime ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
    }

    public void setDisplayedText(ITextComponent titleComponent, ITextComponent subtitleComponent) {
        this.title = titleComponent.getFormattedText();
        this.subtitle = subtitleComponent == null ? null : subtitleComponent.getFormattedText();
        this.newDisplay = true;
    }

    public static void addOrUpdate(GuiToast toast, ITextComponent title, ITextComponent subtitle, long totalShowTime) {
        BaritoneToast baritonetoast = toast.getToast(BaritoneToast.class, new Object());
        if (baritonetoast == null) {
            toast.add(new BaritoneToast(title, subtitle, totalShowTime));
        } else {
            baritonetoast.setDisplayedText(title, subtitle);
        }
    }
}

