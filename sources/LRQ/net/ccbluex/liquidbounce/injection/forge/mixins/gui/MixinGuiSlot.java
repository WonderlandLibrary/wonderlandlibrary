/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiSlot
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.injection.implementations.IMixinGuiSlot;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiSlot.class})
public abstract class MixinGuiSlot
implements IMixinGuiSlot {
    @Shadow
    public int field_148152_e;
    @Shadow
    public int field_148153_b;
    @Shadow
    public int field_148155_a;
    @Shadow
    public int field_148151_d;
    @Shadow
    public int field_148154_c;
    @Shadow
    public int field_148158_l;
    @Shadow
    protected int field_148150_g;
    @Shadow
    protected int field_148162_h;
    @Shadow
    protected float field_148169_q;
    @Shadow
    protected boolean field_148165_u;
    @Shadow
    @Final
    protected Minecraft field_148161_k;
    @Shadow
    protected boolean field_178041_q;
    private int listWidth = 220;
    private boolean enableScissor = false;

    @Shadow
    protected abstract void func_148123_a();

    @Shadow
    protected abstract void func_148121_k();

    @Shadow
    protected abstract void func_148129_a(int var1, int var2, Tessellator var3);

    @Shadow
    protected abstract int func_148138_e();

    @Shadow
    protected abstract void func_192638_a(int var1, int var2, int var3, int var4, float var5);

    @Shadow
    protected abstract void func_148136_c(int var1, int var2, int var3, int var4);

    @Shadow
    public abstract int func_148135_f();

    @Shadow
    protected abstract void drawContainerBackground(Tessellator var1);

    @Shadow
    protected abstract void func_148142_b(int var1, int var2);

    @Overwrite
    public void func_148128_a(int mouseXIn, int mouseYIn, float partialTicks) {
        if (this.field_178041_q) {
            this.field_148150_g = mouseXIn;
            this.field_148162_h = mouseYIn;
            this.func_148123_a();
            int i = this.func_148137_d();
            int j = i + 6;
            this.func_148121_k();
            GlStateManager.func_179140_f();
            GlStateManager.func_179106_n();
            Tessellator tessellator = Tessellator.func_178181_a();
            BufferBuilder bufferbuilder = tessellator.func_178180_c();
            int k = this.field_148152_e + this.field_148155_a / 2 - this.func_148139_c() / 2 + 2;
            int l = this.field_148153_b + 4 - (int)this.field_148169_q;
            if (this.field_148165_u) {
                this.func_148129_a(k, l, tessellator);
            }
            RenderUtils.makeScissorBox(this.field_148152_e, this.field_148153_b, this.field_148151_d, this.field_148154_c);
            GL11.glEnable((int)3089);
            this.func_192638_a(k, l, mouseXIn, mouseYIn, partialTicks);
            GL11.glDisable((int)3089);
            ScaledResolution scaledResolution = new ScaledResolution(this.field_148161_k);
            GlStateManager.func_179097_i();
            Gui.func_73734_a((int)0, (int)0, (int)scaledResolution.func_78326_a(), (int)this.field_148153_b, (int)Integer.MIN_VALUE);
            Gui.func_73734_a((int)0, (int)this.field_148154_c, (int)scaledResolution.func_78326_a(), (int)this.field_148158_l, (int)Integer.MIN_VALUE);
            GL11.glEnable((int)3042);
            GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
            GlStateManager.func_179118_c();
            GlStateManager.func_179103_j((int)7425);
            GlStateManager.func_179090_x();
            int i1 = 4;
            bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
            bufferbuilder.func_181662_b((double)this.field_148152_e, (double)(this.field_148153_b + i1), 0.0).func_181669_b(0, 0, 0, 0).func_181675_d();
            bufferbuilder.func_181662_b((double)this.field_148151_d, (double)(this.field_148153_b + i1), 0.0).func_181669_b(0, 0, 0, 0).func_181675_d();
            bufferbuilder.func_181662_b((double)this.field_148151_d, (double)this.field_148153_b, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
            bufferbuilder.func_181662_b((double)this.field_148152_e, (double)this.field_148153_b, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
            tessellator.func_78381_a();
            bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
            bufferbuilder.func_181662_b((double)this.field_148152_e, (double)this.field_148154_c, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
            bufferbuilder.func_181662_b((double)this.field_148151_d, (double)this.field_148154_c, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
            bufferbuilder.func_181662_b((double)this.field_148151_d, (double)(this.field_148154_c - i1), 0.0).func_181669_b(0, 0, 0, 0).func_181675_d();
            bufferbuilder.func_181662_b((double)this.field_148152_e, (double)(this.field_148154_c - i1), 0.0).func_181669_b(0, 0, 0, 0).func_181675_d();
            tessellator.func_78381_a();
            int j1 = this.func_148135_f();
            if (j1 > 0) {
                int k1 = (this.field_148154_c - this.field_148153_b) * (this.field_148154_c - this.field_148153_b) / this.func_148138_e();
                int l1 = (int)this.field_148169_q * (this.field_148154_c - this.field_148153_b - (k1 = MathHelper.func_76125_a((int)k1, (int)32, (int)(this.field_148154_c - this.field_148153_b - 8)))) / j1 + this.field_148153_b;
                if (l1 < this.field_148153_b) {
                    l1 = this.field_148153_b;
                }
                bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181709_i);
                bufferbuilder.func_181662_b((double)i, (double)this.field_148154_c, 0.0).func_187315_a(0.0, 1.0).func_181669_b(0, 0, 0, 255).func_181675_d();
                bufferbuilder.func_181662_b((double)j, (double)this.field_148154_c, 0.0).func_187315_a(1.0, 1.0).func_181669_b(0, 0, 0, 255).func_181675_d();
                bufferbuilder.func_181662_b((double)j, (double)this.field_148153_b, 0.0).func_187315_a(1.0, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
                bufferbuilder.func_181662_b((double)i, (double)this.field_148153_b, 0.0).func_187315_a(0.0, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
                tessellator.func_78381_a();
                bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181709_i);
                bufferbuilder.func_181662_b((double)i, (double)(l1 + k1), 0.0).func_187315_a(0.0, 1.0).func_181669_b(128, 128, 128, 255).func_181675_d();
                bufferbuilder.func_181662_b((double)j, (double)(l1 + k1), 0.0).func_187315_a(1.0, 1.0).func_181669_b(128, 128, 128, 255).func_181675_d();
                bufferbuilder.func_181662_b((double)j, (double)l1, 0.0).func_187315_a(1.0, 0.0).func_181669_b(128, 128, 128, 255).func_181675_d();
                bufferbuilder.func_181662_b((double)i, (double)l1, 0.0).func_187315_a(0.0, 0.0).func_181669_b(128, 128, 128, 255).func_181675_d();
                tessellator.func_78381_a();
                bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181709_i);
                bufferbuilder.func_181662_b((double)i, (double)(l1 + k1 - 1), 0.0).func_187315_a(0.0, 1.0).func_181669_b(192, 192, 192, 255).func_181675_d();
                bufferbuilder.func_181662_b((double)(j - 1), (double)(l1 + k1 - 1), 0.0).func_187315_a(1.0, 1.0).func_181669_b(192, 192, 192, 255).func_181675_d();
                bufferbuilder.func_181662_b((double)(j - 1), (double)l1, 0.0).func_187315_a(1.0, 0.0).func_181669_b(192, 192, 192, 255).func_181675_d();
                bufferbuilder.func_181662_b((double)i, (double)l1, 0.0).func_187315_a(0.0, 0.0).func_181669_b(192, 192, 192, 255).func_181675_d();
                tessellator.func_78381_a();
            }
            this.func_148142_b(mouseXIn, mouseYIn);
            GlStateManager.func_179098_w();
            GlStateManager.func_179103_j((int)7424);
            GlStateManager.func_179141_d();
            GlStateManager.func_179084_k();
        }
    }

    @Overwrite
    protected int func_148137_d() {
        return this.field_148155_a - 5;
    }

    @Override
    public void setEnableScissor(boolean enableScissor) {
        this.enableScissor = enableScissor;
    }

    @Overwrite
    public int func_148139_c() {
        return this.listWidth;
    }

    @Override
    public void setListWidth(int listWidth) {
        this.listWidth = listWidth;
    }
}

