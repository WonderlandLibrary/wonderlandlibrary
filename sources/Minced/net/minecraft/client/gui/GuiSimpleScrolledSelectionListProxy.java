// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsSimpleScrolledSelectionList;

public class GuiSimpleScrolledSelectionListProxy extends GuiSlot
{
    private final RealmsSimpleScrolledSelectionList realmsScrolledSelectionList;
    
    public GuiSimpleScrolledSelectionListProxy(final RealmsSimpleScrolledSelectionList realmsScrolledSelectionListIn, final int widthIn, final int heightIn, final int topIn, final int bottomIn, final int slotHeightIn) {
        super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.realmsScrolledSelectionList = realmsScrolledSelectionListIn;
    }
    
    @Override
    protected int getSize() {
        return this.realmsScrolledSelectionList.getItemCount();
    }
    
    @Override
    protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        this.realmsScrolledSelectionList.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return this.realmsScrolledSelectionList.isSelectedItem(slotIndex);
    }
    
    @Override
    protected void drawBackground() {
        this.realmsScrolledSelectionList.renderBackground();
    }
    
    @Override
    protected void drawSlot(final int slotIndex, final int xPos, final int yPos, final int heightIn, final int mouseXIn, final int mouseYIn, final float partialTicks) {
        this.realmsScrolledSelectionList.renderItem(slotIndex, xPos, yPos, heightIn, mouseXIn, mouseYIn);
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getMouseY() {
        return this.mouseY;
    }
    
    public int getMouseX() {
        return this.mouseX;
    }
    
    @Override
    protected int getContentHeight() {
        return this.realmsScrolledSelectionList.getMaxPosition();
    }
    
    @Override
    protected int getScrollBarX() {
        return this.realmsScrolledSelectionList.getScrollbarPosition();
    }
    
    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
    }
    
    @Override
    public void drawScreen(final int mouseXIn, final int mouseYIn, final float partialTicks) {
        if (this.visible) {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
            this.drawBackground();
            final int i = this.getScrollBarX();
            final int j = i + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            final int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            final int l = this.top + 4 - (int)this.amountScrolled;
            if (this.hasListHeader) {
                this.drawListHeader(k, l, tessellator);
            }
            this.drawSelectionBox(k, l, mouseXIn, mouseYIn, partialTicks);
            GlStateManager.disableDepth();
            this.overlayBackground(0, this.top, 255, 255);
            this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            final int i2 = this.getMaxScroll();
            if (i2 > 0) {
                int j2 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                j2 = MathHelper.clamp(j2, 32, this.bottom - this.top - 8);
                int k2 = (int)this.amountScrolled * (this.bottom - this.top - j2) / i2 + this.top;
                if (k2 < this.top) {
                    k2 = this.top;
                }
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos(i, this.bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(j, this.bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(j, this.top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(i, this.top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos(i, k2 + j2, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos(j, k2 + j2, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos(j, k2, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos(i, k2, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos(i, k2 + j2 - 1, 0.0).tex(0.0, 1.0).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos(j - 1, k2 + j2 - 1, 0.0).tex(1.0, 1.0).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos(j - 1, k2, 0.0).tex(1.0, 0.0).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos(i, k2, 0.0).tex(0.0, 0.0).color(192, 192, 192, 255).endVertex();
                tessellator.draw();
            }
            this.renderDecorations(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }
}
