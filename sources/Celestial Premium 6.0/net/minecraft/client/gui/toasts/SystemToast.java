/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.toasts;

import javax.annotation.Nullable;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;

public class SystemToast
implements IToast {
    private final Type field_193659_c;
    private String field_193660_d;
    private String field_193661_e;
    private long field_193662_f;
    private boolean field_193663_g;

    public SystemToast(Type p_i47488_1_, ITextComponent p_i47488_2_, @Nullable ITextComponent p_i47488_3_) {
        this.field_193659_c = p_i47488_1_;
        this.field_193660_d = p_i47488_2_.getUnformattedText();
        this.field_193661_e = p_i47488_3_ == null ? null : p_i47488_3_.getUnformattedText();
    }

    @Override
    public IToast.Visibility draw(GuiToast p_193653_1_, long p_193653_2_) {
        if (this.field_193663_g) {
            this.field_193662_f = p_193653_2_;
            this.field_193663_g = false;
        }
        p_193653_1_.getMinecraft().getTextureManager().bindTexture(field_193654_a);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        p_193653_1_.drawTexturedModalRect(0, 0, 0, 64, 160, 32);
        if (this.field_193661_e == null) {
            p_193653_1_.getMinecraft().fontRendererObj.drawString(this.field_193660_d, 18.0f, 12.0f, -256);
        } else {
            p_193653_1_.getMinecraft().fontRendererObj.drawString(this.field_193660_d, 18.0f, 7.0f, -256);
            p_193653_1_.getMinecraft().fontRendererObj.drawString(this.field_193661_e, 18.0f, 18.0f, -1);
        }
        return p_193653_2_ - this.field_193662_f < 5000L ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
    }

    public void func_193656_a(ITextComponent p_193656_1_, @Nullable ITextComponent p_193656_2_) {
        this.field_193660_d = p_193656_1_.getUnformattedText();
        this.field_193661_e = p_193656_2_ == null ? null : p_193656_2_.getUnformattedText();
        this.field_193663_g = true;
    }

    public Type func_193652_b() {
        return this.field_193659_c;
    }

    public static void func_193657_a(GuiToast p_193657_0_, Type p_193657_1_, ITextComponent p_193657_2_, @Nullable ITextComponent p_193657_3_) {
        SystemToast systemtoast = p_193657_0_.getToast(SystemToast.class, (Object)p_193657_1_);
        if (systemtoast == null) {
            p_193657_0_.add(new SystemToast(p_193657_1_, p_193657_2_, p_193657_3_));
        } else {
            systemtoast.func_193656_a(p_193657_2_, p_193657_3_);
        }
    }

    public static enum Type {
        TUTORIAL_HINT,
        NARRATOR_TOGGLE;

    }
}

