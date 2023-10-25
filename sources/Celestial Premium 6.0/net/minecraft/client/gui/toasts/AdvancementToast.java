/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.toasts;

import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class AdvancementToast
implements IToast {
    private final Advancement field_193679_c;
    private boolean field_194168_d = false;

    public AdvancementToast(Advancement p_i47490_1_) {
        this.field_193679_c = p_i47490_1_;
    }

    @Override
    public IToast.Visibility draw(GuiToast p_193653_1_, long p_193653_2_) {
        p_193653_1_.getMinecraft().getTextureManager().bindTexture(field_193654_a);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        DisplayInfo displayinfo = this.field_193679_c.func_192068_c();
        p_193653_1_.drawTexturedModalRect(0, 0, 0, 0, 160, 32);
        if (displayinfo != null) {
            int i;
            List<String> list = p_193653_1_.getMinecraft().fontRendererObj.listFormattedStringToWidth(displayinfo.func_192297_a().getFormattedText(), 125);
            int n = i = displayinfo.func_192291_d() == FrameType.CHALLENGE ? 0xFF88FF : 0xFFFF00;
            if (list.size() == 1) {
                p_193653_1_.getMinecraft().fontRendererObj.drawString(I18n.format("advancements.toast." + displayinfo.func_192291_d().func_192307_a(), new Object[0]), 30.0f, 7.0f, i | 0xFF000000);
                p_193653_1_.getMinecraft().fontRendererObj.drawString(displayinfo.func_192297_a().getFormattedText(), 30.0f, 18.0f, -1);
            } else {
                int j = 1500;
                float f = 300.0f;
                if (p_193653_2_ < 1500L) {
                    int k = MathHelper.floor(MathHelper.clamp((float)(1500L - p_193653_2_) / 300.0f, 0.0f, 1.0f) * 255.0f) << 24 | 0x4000000;
                    p_193653_1_.getMinecraft().fontRendererObj.drawString(I18n.format("advancements.toast." + displayinfo.func_192291_d().func_192307_a(), new Object[0]), 30.0f, 11.0f, i | k);
                } else {
                    int i1 = MathHelper.floor(MathHelper.clamp((float)(p_193653_2_ - 1500L) / 300.0f, 0.0f, 1.0f) * 252.0f) << 24 | 0x4000000;
                    int l = 16 - list.size() * p_193653_1_.getMinecraft().fontRendererObj.FONT_HEIGHT / 2;
                    for (String s : list) {
                        p_193653_1_.getMinecraft().fontRendererObj.drawString(s, 30.0f, l, 0xFFFFFF | i1);
                        l += p_193653_1_.getMinecraft().fontRendererObj.FONT_HEIGHT;
                    }
                }
            }
            if (!this.field_194168_d && p_193653_2_ > 0L) {
                this.field_194168_d = true;
                if (displayinfo.func_192291_d() == FrameType.CHALLENGE) {
                    p_193653_1_.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_194007_a(SoundEvents.field_194228_if, 1.0f, 1.0f));
                }
            }
            RenderHelper.enableGUIStandardItemLighting();
            p_193653_1_.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(null, displayinfo.func_192298_b(), 8, 8);
            return p_193653_2_ >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
        }
        return IToast.Visibility.HIDE;
    }
}

