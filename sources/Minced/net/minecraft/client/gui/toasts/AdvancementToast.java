// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.toasts;

import java.util.Iterator;
import java.util.List;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.advancements.Advancement;

public class AdvancementToast implements IToast
{
    private final Advancement advancement;
    private boolean hasPlayedSound;
    
    public AdvancementToast(final Advancement advancementIn) {
        this.hasPlayedSound = false;
        this.advancement = advancementIn;
    }
    
    @Override
    public Visibility draw(final GuiToast toastGui, final long delta) {
        toastGui.getMinecraft().getTextureManager().bindTexture(AdvancementToast.TEXTURE_TOASTS);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        final DisplayInfo displayinfo = this.advancement.getDisplay();
        toastGui.drawTexturedModalRect(0, 0, 0, 0, 160, 32);
        if (displayinfo != null) {
            final List<String> list = toastGui.getMinecraft().fontRenderer.listFormattedStringToWidth(displayinfo.getTitle().getFormattedText(), 125);
            final int i = (displayinfo.getFrame() == FrameType.CHALLENGE) ? 16746751 : 16776960;
            if (list.size() == 1) {
                toastGui.getMinecraft().fontRenderer.drawString(I18n.format("advancements.toast." + displayinfo.getFrame().getName(), new Object[0]), 30, 7, i | 0xFF000000);
                toastGui.getMinecraft().fontRenderer.drawString(displayinfo.getTitle().getFormattedText(), 30, 18, -1);
            }
            else {
                final int j = 1500;
                final float f = 300.0f;
                if (delta < 1500L) {
                    final int k = MathHelper.floor(MathHelper.clamp((1500L - delta) / 300.0f, 0.0f, 1.0f) * 255.0f) << 24 | 0x4000000;
                    toastGui.getMinecraft().fontRenderer.drawString(I18n.format("advancements.toast." + displayinfo.getFrame().getName(), new Object[0]), 30, 11, i | k);
                }
                else {
                    final int i2 = MathHelper.floor(MathHelper.clamp((delta - 1500L) / 300.0f, 0.0f, 1.0f) * 252.0f) << 24 | 0x4000000;
                    int l = 16 - list.size() * toastGui.getMinecraft().fontRenderer.FONT_HEIGHT / 2;
                    for (final String s : list) {
                        toastGui.getMinecraft().fontRenderer.drawString(s, 30, l, 0xFFFFFF | i2);
                        l += toastGui.getMinecraft().fontRenderer.FONT_HEIGHT;
                    }
                }
            }
            if (!this.hasPlayedSound && delta > 0L) {
                this.hasPlayedSound = true;
                if (displayinfo.getFrame() == FrameType.CHALLENGE) {
                    toastGui.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f));
                }
            }
            RenderHelper.enableGUIStandardItemLighting();
            toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(null, displayinfo.getIcon(), 8, 8);
            return (delta >= 5000L) ? Visibility.HIDE : Visibility.SHOW;
        }
        return Visibility.HIDE;
    }
}
