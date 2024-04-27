/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import java.awt.Color;
import liying.utils.SuperLib;
import liying.utils.animation.AnimationUtil;
import net.ccbluex.liquidbounce.ui.font.FontDrawer;
import net.ccbluex.liquidbounce.ui.font.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiButton.class})
public abstract class MixinGuiButton
extends Gui {
    @Shadow
    public int field_146129_i;
    @Shadow
    @Final
    protected static ResourceLocation field_146122_a;
    private float alpha;
    @Shadow
    protected boolean field_146123_n;
    private double animation = 0.1f;
    private float cut;
    @Shadow
    public int field_146121_g;
    @Shadow
    public boolean field_146124_l;
    @Shadow
    public boolean field_146125_m;
    @Shadow
    public int field_146128_h;
    @Shadow
    public String field_146126_j;
    @Shadow
    public int field_146120_f;

    @Overwrite
    public void func_191745_a(Minecraft minecraft, int n, int n2, float f) {
        if (this.field_146125_m) {
            FontDrawer fontDrawer = FontLoaders.F18;
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.field_146123_n = n >= this.field_146128_h && n2 >= this.field_146129_i && n < this.field_146128_h + this.field_146120_f && n2 < this.field_146129_i + this.field_146121_g;
            GlStateManager.func_179147_l();
            this.animation = AnimationUtil.moveUD((float)this.animation, this.field_146123_n ? 0.3f : 0.1f, 10.0f / (float)Minecraft.func_175610_ah(), 4.0f / (float)Minecraft.func_175610_ah());
            if (this.field_146124_l) {
                RenderUtils.drawGradientSideways(this.field_146128_h, (float)(this.field_146129_i + this.field_146121_g) - 1.5f, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, SuperLib.reAlpha(new Color(90, 184, 255).getRGB(), 0.95f), SuperLib.reAlpha(new Color(90, 184, 255).getRGB(), 0.95f));
            } else {
                RenderUtils.drawGradientSideways(this.field_146128_h, (float)(this.field_146129_i + this.field_146121_g) - 1.5f, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, SuperLib.reAlpha(new Color(90, 184, 255).getRGB(), 0.95f), SuperLib.reAlpha(new Color(90, 184, 255).getRGB(), 0.95f));
            }
            RenderUtils.drawRect((float)this.field_146128_h, (float)this.field_146129_i, (float)(this.field_146128_h + this.field_146120_f), (float)(this.field_146129_i + this.field_146121_g) - 1.5f, new Color(0, 0, 0, 180).getRGB());
            if (this.field_146124_l) {
                RenderUtils.drawRect((float)this.field_146128_h, (float)this.field_146129_i, (float)(this.field_146128_h + this.field_146120_f), (float)(this.field_146129_i + this.field_146121_g), SuperLib.reAlpha(new Color(225, 225, 225).getRGB(), (float)this.animation));
            } else {
                RenderUtils.drawRect((float)this.field_146128_h, (float)this.field_146129_i, (float)(this.field_146128_h + this.field_146120_f), (float)(this.field_146129_i + this.field_146121_g), SuperLib.reAlpha(new Color(255, 255, 255).getRGB(), 0.1f));
            }
            this.func_146119_b(minecraft, n, n2);
            fontDrawer.drawStringWithShadow(this.field_146126_j, this.field_146128_h + this.field_146120_f / 2 - fontDrawer.getStringWidth(this.field_146126_j) / 2, (float)this.field_146129_i + (float)(this.field_146121_g - 5) / 2.0f - 1.0f, Color.WHITE.getRGB());
        }
    }

    @Shadow
    protected abstract void func_146119_b(Minecraft var1, int var2, int var3);
}

