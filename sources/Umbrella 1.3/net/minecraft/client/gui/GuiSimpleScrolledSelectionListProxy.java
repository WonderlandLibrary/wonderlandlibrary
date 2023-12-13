/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.realms.RealmsSimpleScrolledSelectionList;
import net.minecraft.util.MathHelper;

public class GuiSimpleScrolledSelectionListProxy
extends GuiSlot {
    private final RealmsSimpleScrolledSelectionList field_178050_u;
    private static final String __OBFID = "CL_00001938";

    public GuiSimpleScrolledSelectionListProxy(RealmsSimpleScrolledSelectionList p_i45525_1_, int p_i45525_2_, int p_i45525_3_, int p_i45525_4_, int p_i45525_5_, int p_i45525_6_) {
        super(Minecraft.getMinecraft(), p_i45525_2_, p_i45525_3_, p_i45525_4_, p_i45525_5_, p_i45525_6_);
        this.field_178050_u = p_i45525_1_;
    }

    @Override
    protected int getSize() {
        return this.field_178050_u.getItemCount();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        this.field_178050_u.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }

    @Override
    protected boolean isSelected(int slotIndex) {
        return this.field_178050_u.isSelectedItem(slotIndex);
    }

    @Override
    protected void drawBackground() {
        this.field_178050_u.renderBackground();
    }

    @Override
    protected void drawSlot(int p_180791_1_, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_) {
        this.field_178050_u.renderItem(p_180791_1_, p_180791_2_, p_180791_3_, p_180791_4_, p_180791_5_, p_180791_6_);
    }

    public int func_178048_e() {
        return this.width;
    }

    public int func_178047_f() {
        return this.mouseY;
    }

    public int func_178049_g() {
        return this.mouseX;
    }

    @Override
    protected int getContentHeight() {
        return this.field_178050_u.getMaxPosition();
    }

    @Override
    protected int getScrollBarX() {
        return this.field_178050_u.getScrollbarPosition();
    }

    @Override
    public void func_178039_p() {
        super.func_178039_p();
    }

    @Override
    public void drawScreen(int p_148128_1_, int p_148128_2_, float p_148128_3_) {
        if (this.field_178041_q) {
            this.mouseX = p_148128_1_;
            this.mouseY = p_148128_2_;
            this.drawBackground();
            int var4 = this.getScrollBarX();
            int var5 = var4 + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator var6 = Tessellator.getInstance();
            WorldRenderer var7 = var6.getWorldRenderer();
            int var8 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int var9 = this.top + 4 - (int)this.amountScrolled;
            if (this.hasListHeader) {
                this.drawListHeader(var8, var9, var6);
            }
            this.drawSelectionBox(var8, var9, p_148128_1_, p_148128_2_);
            GlStateManager.disableDepth();
            boolean var10 = true;
            this.overlayBackground(0, this.top, 255, 255);
            this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.func_179090_x();
            int var11 = this.func_148135_f();
            if (var11 > 0) {
                int var12 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                int var13 = (int)this.amountScrolled * (this.bottom - this.top - (var12 = MathHelper.clamp_int(var12, 32, this.bottom - this.top - 8))) / var11 + this.top;
                if (var13 < this.top) {
                    var13 = this.top;
                }
                var7.startDrawingQuads();
                var7.func_178974_a(0, 255);
                var7.addVertexWithUV(var4, this.bottom, 0.0, 0.0, 1.0);
                var7.addVertexWithUV(var5, this.bottom, 0.0, 1.0, 1.0);
                var7.addVertexWithUV(var5, this.top, 0.0, 1.0, 0.0);
                var7.addVertexWithUV(var4, this.top, 0.0, 0.0, 0.0);
                var6.draw();
                var7.startDrawingQuads();
                var7.func_178974_a(0x808080, 255);
                var7.addVertexWithUV(var4, var13 + var12, 0.0, 0.0, 1.0);
                var7.addVertexWithUV(var5, var13 + var12, 0.0, 1.0, 1.0);
                var7.addVertexWithUV(var5, var13, 0.0, 1.0, 0.0);
                var7.addVertexWithUV(var4, var13, 0.0, 0.0, 0.0);
                var6.draw();
                var7.startDrawingQuads();
                var7.func_178974_a(0xC0C0C0, 255);
                var7.addVertexWithUV(var4, var13 + var12 - 1, 0.0, 0.0, 1.0);
                var7.addVertexWithUV(var5 - 1, var13 + var12 - 1, 0.0, 1.0, 1.0);
                var7.addVertexWithUV(var5 - 1, var13, 0.0, 1.0, 0.0);
                var7.addVertexWithUV(var4, var13, 0.0, 0.0, 0.0);
                var6.draw();
            }
            this.func_148142_b(p_148128_1_, p_148128_2_);
            GlStateManager.func_179098_w();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }
}

