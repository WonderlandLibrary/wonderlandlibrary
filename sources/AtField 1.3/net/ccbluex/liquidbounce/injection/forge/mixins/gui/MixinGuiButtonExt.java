/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraftforge.fml.client.config.GuiButtonExt
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import java.awt.Color;
import net.ccbluex.liquidbounce.injection.backend.FontRendererImpl;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
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

    @Overwrite
    public void func_191745_a(Minecraft minecraft, int n, int n2, float f) {
        if (this.field_146125_m) {
            FontRenderer fontRenderer = minecraft.func_135016_M().func_135042_a() ? minecraft.field_71466_p : ((FontRendererImpl)Fonts.roboto35).getWrapped();
            this.field_146123_n = n >= this.field_146128_h && n2 >= this.field_146129_i && n < this.field_146128_h + this.field_146120_f && n2 < this.field_146129_i + this.field_146121_g;
            int n3 = RenderUtils.deltaTime;
            if (this.field_146124_l && this.field_146123_n) {
                this.cut += 0.05f * (float)n3;
                if (this.cut >= 4.0f) {
                    this.cut = 4.0f;
                }
                this.alpha += 0.3f * (float)n3;
                if (this.alpha >= 210.0f) {
                    this.alpha = 210.0f;
                }
            } else {
                this.cut -= 0.05f * (float)n3;
                if (this.cut <= 0.0f) {
                    this.cut = 0.0f;
                }
                this.alpha -= 0.3f * (float)n3;
                if (this.alpha <= 120.0f) {
                    this.alpha = 120.0f;
                }
            }
            Gui.func_73734_a((int)(this.field_146128_h + (int)this.cut), (int)this.field_146129_i, (int)(this.field_146128_h + this.field_146120_f - (int)this.cut), (int)(this.field_146129_i + this.field_146121_g), (int)(this.field_146124_l ? new Color(0.0f, 0.0f, 0.0f, this.alpha / 255.0f).getRGB() : new Color(0.5f, 0.5f, 0.5f, 0.5f).getRGB()));
            minecraft.func_110434_K().func_110577_a(field_146122_a);
            this.func_146119_b(minecraft, n, n2);
            fontRenderer.func_175063_a(this.field_146126_j, (float)(this.field_146128_h + this.field_146120_f / 2 - fontRenderer.func_78256_a(this.field_146126_j) / 2), (float)this.field_146129_i + (float)(this.field_146121_g - 5) / 2.0f, 0xE0E0E0);
            GlStateManager.func_179117_G();
        }
    }

    public MixinGuiButtonExt(int n, int n2, int n3, String string) {
        super(n, n2, n3, string);
    }

    public MixinGuiButtonExt(int n, int n2, int n3, int n4, int n5, String string) {
        super(n, n2, n3, n4, n5, string);
    }
}

