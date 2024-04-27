/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 */
package net.dev.important.injection.forge.mixins.gui;

import java.awt.Color;
import net.dev.important.gui.font.Fonts;
import net.dev.important.utils.render.EaseUtils;
import net.dev.important.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={GuiButton.class})
public abstract class MixinGuiButton
extends Gui {
    @Shadow
    public int field_146128_h;
    @Shadow
    public int field_146129_i;
    @Shadow
    public int field_146120_f;
    @Shadow
    public int field_146121_g;
    @Shadow
    protected boolean field_146123_n;
    @Shadow
    public boolean field_146124_l;
    @Shadow
    public boolean field_146125_m;
    @Shadow
    public String field_146126_j;
    private double animation = 0.0;
    private long lastUpdate = System.currentTimeMillis();

    @Shadow
    protected abstract void func_146119_b(Minecraft var1, int var2, int var3);

    @Overwrite
    public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
        if (!this.field_146125_m) {
            return;
        }
        this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
        this.func_146119_b(mc, mouseX, mouseY);
        long time = System.currentTimeMillis();
        double pct = (double)(time - this.lastUpdate) / 500.0;
        if (this.field_146123_n) {
            if (this.animation < 1.0) {
                this.animation += pct;
            }
            if (this.animation > 1.0) {
                this.animation = 1.0;
            }
        } else {
            if (this.animation > 0.0) {
                this.animation -= pct;
            }
            if (this.animation < 0.0) {
                this.animation = 0.0;
            }
        }
        double percent = EaseUtils.easeInOutQuad(this.animation);
        RenderUtils.drawRect((float)this.field_146128_h, (float)this.field_146129_i, (float)(this.field_146128_h + this.field_146120_f), (float)(this.field_146129_i + this.field_146121_g), new Color(31, 31, 31, 150).getRGB());
        double half = (double)this.field_146120_f / 2.0;
        double center = (double)this.field_146128_h + half;
        if (this.field_146124_l) {
            RenderUtils.drawRect(center - percent * half, (double)(this.field_146129_i + this.field_146121_g - 1), center + percent * half, (double)(this.field_146129_i + this.field_146121_g), Color.WHITE.getRGB());
        }
        Fonts.Poppins.drawCenteredString(this.field_146126_j, (float)this.field_146128_h + (float)this.field_146120_f / 2.0f, (float)this.field_146129_i + (float)this.field_146121_g / 2.0f - (float)Fonts.Poppins.getHeight() / 2.0f + 1.0f, this.field_146125_m ? Color.WHITE.getRGB() : Color.GRAY.getRGB(), false);
        this.lastUpdate = time;
    }
}

