/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraftforge.fml.client.config.GuiButtonExt
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.dev.important.injection.forge.mixins.gui;

import java.awt.Color;
import net.dev.important.gui.font.Fonts;
import net.dev.important.gui.font.GameFontRenderer;
import net.dev.important.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiButtonExt.class})
public abstract class MixinGuiButtonExt
extends GuiButton {
    private float cut;
    private float alpha;

    public MixinGuiButtonExt(int p_i1020_1_, int p_i1020_2_, int p_i1020_3_, String p_i1020_4_) {
        super(p_i1020_1_, p_i1020_2_, p_i1020_3_, p_i1020_4_);
    }

    public MixinGuiButtonExt(int p_i46323_1_, int p_i46323_2_, int p_i46323_3_, int p_i46323_4_, int p_i46323_5_, String p_i46323_6_) {
        super(p_i46323_1_, p_i46323_2_, p_i46323_3_, p_i46323_4_, p_i46323_5_, p_i46323_6_);
    }

    @Overwrite
    public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
        if (this.field_146125_m) {
            GameFontRenderer fontRenderer = mc.func_135016_M().func_135042_a() ? mc.field_71466_p : Fonts.Poppins;
            this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
            int delta = RenderUtils.deltaTime;
            if (this.field_146124_l && this.field_146123_n) {
                this.cut += 0.05f * (float)delta;
                if (this.cut >= 4.0f) {
                    this.cut = 4.0f;
                }
                this.alpha += 0.3f * (float)delta;
                if (this.alpha >= 210.0f) {
                    this.alpha = 210.0f;
                }
            } else {
                this.cut -= 0.05f * (float)delta;
                if (this.cut <= 0.0f) {
                    this.cut = 0.0f;
                }
                this.alpha -= 0.3f * (float)delta;
                if (this.alpha <= 120.0f) {
                    this.alpha = 120.0f;
                }
            }
            Gui.func_73734_a((int)(this.field_146128_h + (int)this.cut), (int)this.field_146129_i, (int)(this.field_146128_h + this.field_146120_f - (int)this.cut), (int)(this.field_146129_i + this.field_146121_g), (int)(this.field_146124_l ? new Color(0.0f, 0.0f, 0.0f, this.alpha / 255.0f).getRGB() : new Color(0.5f, 0.5f, 0.5f, 0.5f).getRGB()));
            mc.func_110434_K().func_110577_a(field_146122_a);
            this.func_146119_b(mc, mouseX, mouseY);
            fontRenderer.func_175063_a(this.field_146126_j, this.field_146128_h + this.field_146120_f / 2 - fontRenderer.func_78256_a(this.field_146126_j) / 2, (float)this.field_146129_i + (float)(this.field_146121_g - 5) / 2.0f, 0xE0E0E0);
            GlStateManager.func_179117_G();
        }
    }
}

