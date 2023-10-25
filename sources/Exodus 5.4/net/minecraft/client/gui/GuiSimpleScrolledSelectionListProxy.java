/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.realms.RealmsSimpleScrolledSelectionList;
import net.minecraft.util.MathHelper;

public class GuiSimpleScrolledSelectionListProxy
extends GuiSlot {
    private final RealmsSimpleScrolledSelectionList field_178050_u;

    @Override
    protected boolean isSelected(int n) {
        return this.field_178050_u.isSelectedItem(n);
    }

    @Override
    protected void drawBackground() {
        this.field_178050_u.renderBackground();
    }

    public int getMouseX() {
        return this.mouseX;
    }

    public GuiSimpleScrolledSelectionListProxy(RealmsSimpleScrolledSelectionList realmsSimpleScrolledSelectionList, int n, int n2, int n3, int n4, int n5) {
        super(Minecraft.getMinecraft(), n, n2, n3, n4, n5);
        this.field_178050_u = realmsSimpleScrolledSelectionList;
    }

    @Override
    protected void elementClicked(int n, boolean bl, int n2, int n3) {
        this.field_178050_u.selectItem(n, bl, n2, n3);
    }

    public int getMouseY() {
        return this.mouseY;
    }

    @Override
    protected int getScrollBarX() {
        return this.field_178050_u.getScrollbarPosition();
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
    }

    @Override
    protected void drawSlot(int n, int n2, int n3, int n4, int n5, int n6) {
        this.field_178050_u.renderItem(n, n2, n3, n4, n5, n6);
    }

    public int getWidth() {
        return this.width;
    }

    @Override
    protected int getSize() {
        return this.field_178050_u.getItemCount();
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        if (this.field_178041_q) {
            this.mouseX = n;
            this.mouseY = n2;
            this.drawBackground();
            int n3 = this.getScrollBarX();
            int n4 = n3 + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            int n5 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int n6 = this.top + 4 - (int)this.amountScrolled;
            if (this.hasListHeader) {
                this.drawListHeader(n5, n6, tessellator);
            }
            this.drawSelectionBox(n5, n6, n, n2);
            GlStateManager.disableDepth();
            int n7 = 4;
            this.overlayBackground(0, this.top, 255, 255);
            this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            int n8 = this.func_148135_f();
            if (n8 > 0) {
                int n9 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                int n10 = (int)this.amountScrolled * (this.bottom - this.top - (n9 = MathHelper.clamp_int(n9, 32, this.bottom - this.top - 8))) / n8 + this.top;
                if (n10 < this.top) {
                    n10 = this.top;
                }
                worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldRenderer.pos(n3, this.bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(n4, this.bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(n4, this.top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(n3, this.top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldRenderer.pos(n3, n10 + n9, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
                worldRenderer.pos(n4, n10 + n9, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
                worldRenderer.pos(n4, n10, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
                worldRenderer.pos(n3, n10, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldRenderer.pos(n3, n10 + n9 - 1, 0.0).tex(0.0, 1.0).color(192, 192, 192, 255).endVertex();
                worldRenderer.pos(n4 - 1, n10 + n9 - 1, 0.0).tex(1.0, 1.0).color(192, 192, 192, 255).endVertex();
                worldRenderer.pos(n4 - 1, n10, 0.0).tex(1.0, 0.0).color(192, 192, 192, 255).endVertex();
                worldRenderer.pos(n3, n10, 0.0).tex(0.0, 0.0).color(192, 192, 192, 255).endVertex();
                tessellator.draw();
            }
            this.func_148142_b(n, n2);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }

    @Override
    protected int getContentHeight() {
        return this.field_178050_u.getMaxPosition();
    }
}

