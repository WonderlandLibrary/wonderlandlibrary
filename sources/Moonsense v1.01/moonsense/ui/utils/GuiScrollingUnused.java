// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.utils;

import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;

public abstract class GuiScrollingUnused extends GuiSlot
{
    public GuiScrollingUnused(final Minecraft mcIn, final int width, final int height, final int topIn, final int bottomIn, final int slotHeightIn) {
        super(mcIn, width, height, topIn, bottomIn, slotHeightIn);
    }
    
    @Override
    public void setDimensions(final int widthIn, final int heightIn, final int topIn, final int bottomIn) {
        this.width = widthIn;
        this.height = heightIn;
        this.top = topIn;
        this.bottom = bottomIn;
        this.left = 0;
        this.right = widthIn;
    }
    
    @Override
    public void setShowSelectionBox(final boolean showSelectionBoxIn) {
        this.showSelectionBox = showSelectionBoxIn;
    }
    
    @Override
    protected void setHasListHeader(final boolean hasListHeaderIn, final int headerPaddingIn) {
        this.hasListHeader = hasListHeaderIn;
        this.headerPadding = headerPaddingIn;
        if (!hasListHeaderIn) {
            this.headerPadding = 0;
        }
    }
    
    @Override
    protected abstract int getSize();
    
    @Override
    protected abstract void elementClicked(final int p0, final boolean p1, final int p2, final int p3);
    
    @Override
    protected abstract boolean isSelected(final int p0);
    
    public int getContentHeight() {
        return this.getSize() * this.slotHeight + this.headerPadding;
    }
    
    @Override
    protected abstract void drawBackground();
    
    @Override
    protected void func_178040_a(final int p_178040_1_, final int p_178040_2_, final int p_178040_3_) {
    }
    
    @Override
    protected abstract void drawSlot(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    
    @Override
    protected void drawListHeader(final int p_148129_1_, final int p_148129_2_, final Tessellator p_148129_3_) {
    }
    
    @Override
    protected void func_148132_a(final int p_148132_1_, final int p_148132_2_) {
    }
    
    public void func_148142_b(final int p_148142_1_, final int p_148142_2_) {
    }
    
    @Override
    public int getSlotIndexFromScreenCoords(final int p_148124_1_, final int p_148124_2_) {
        final int i = this.left + this.width / 2 - this.getListWidth() / 2;
        final int j = this.left + this.width / 2 + this.getListWidth() / 2;
        final int k = p_148124_2_ - this.top - this.headerPadding + (int)this.amountScrolled - 4;
        final int l = k / this.slotHeight;
        return (p_148124_1_ < this.getScrollBarX() && p_148124_1_ >= i && p_148124_1_ <= j && l >= 0 && k >= 0 && l < this.getSize()) ? l : -1;
    }
    
    @Override
    public int func_148135_f() {
        return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
    }
    
    @Override
    public int getAmountScrolled() {
        return (int)this.amountScrolled;
    }
    
    @Override
    public boolean isMouseYWithinSlotBounds(final int p_148141_1_) {
        return p_148141_1_ >= this.top && p_148141_1_ <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }
    
    @Override
    public void scrollBy(final int amount) {
        this.amountScrolled += amount;
        this.bindAmountScrolled();
        this.initialClickY = -2.0f;
    }
    
    @Override
    public void drawScreen(final int mouseXIn, final int mouseYIn, final float p_148128_3_) {
        if (this.field_178041_q) {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
            this.drawBackground();
            final int i = this.getScrollBarX();
            final int j = i + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            this.overlayBackground(this.top, this.bottom, 100, 100);
            final int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            final int l = this.top + 4 - (int)this.amountScrolled;
            if (this.hasListHeader) {
                this.drawListHeader(k, l, tessellator);
            }
            this.drawSelectionBox(k, l, mouseXIn, mouseYIn);
            GlStateManager.disableDepth();
            final int i2 = 4;
            this.overlayBackground(0, this.top, 255, 255);
            this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            worldrenderer.startDrawing(7);
            worldrenderer.color(0, 0, 0, 0);
            worldrenderer.addVertexWithUV(this.left, this.top + 4, 0.0, 0.0, 1.0);
            worldrenderer.addVertexWithUV(this.right, this.top + 4, 0.0, 1.0, 1.0);
            worldrenderer.color(0, 0, 0, 255);
            worldrenderer.addVertexWithUV(this.right, this.top, 0.0, 1.0, 0.0);
            worldrenderer.addVertexWithUV(this.left, this.top, 0.0, 0.0, 0.0);
            tessellator.draw();
            worldrenderer.startDrawing(7);
            worldrenderer.color(0, 0, 0, 255);
            worldrenderer.addVertexWithUV(this.left, this.bottom, 0.0, 0.0, 1.0);
            worldrenderer.addVertexWithUV(this.right, this.bottom, 0.0, 1.0, 1.0);
            worldrenderer.color(0, 0, 0, 0);
            worldrenderer.addVertexWithUV(this.right, this.bottom - 4, 0.0, 1.0, 0.0);
            worldrenderer.addVertexWithUV(this.left, this.bottom - 4, 0.0, 0.0, 0.0);
            tessellator.draw();
            final int j2 = this.func_148135_f();
            this.drawScroll(i, j);
        }
        this.func_148142_b(mouseXIn, mouseYIn);
    }
    
    public void drawScroll(final int i, final int j) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer buffer = tessellator.getWorldRenderer();
        final int scrollbarPositionMinX = this.getScrollBarX();
        final int scrollbarPositionMaxX = scrollbarPositionMinX + 6;
        final int maxScroll = this.func_148135_f();
        final int contentHeight = this.getContentHeight();
        if (maxScroll > 0) {
            int height = (this.bottom - this.top) * (this.bottom - this.top) / contentHeight;
            height = MathHelper.clamp_int(height, 32, this.bottom - this.top - 8);
            height -= (int)Math.min((this.amountScrolled < 0.0) ? ((double)(int)(-this.amountScrolled)) : ((double)((this.amountScrolled > this.func_148135_f()) ? ((int)this.amountScrolled - this.func_148135_f()) : 0)), height * 0.75);
            final int minY = Math.min(Math.max(this.getAmountScrolled() * (this.bottom - this.top - height) / maxScroll + this.top, this.top), this.bottom - height);
            buffer.startDrawing(7);
            buffer.color(0, 0, 0, 255);
            buffer.addVertexWithUV(scrollbarPositionMinX, this.bottom, 0.0, 0.0, 1.0);
            buffer.addVertexWithUV(scrollbarPositionMaxX, this.bottom, 0.0, 1.0, 1.0);
            buffer.addVertexWithUV(scrollbarPositionMaxX, this.top, 0.0, 1.0, 0.0);
            buffer.addVertexWithUV(scrollbarPositionMinX, this.top, 0.0, 0.0, 0.0);
            tessellator.draw();
            buffer.startDrawing(7);
            buffer.color(128, 128, 128, 255);
            buffer.addVertexWithUV(scrollbarPositionMinX, minY + height, 0.0, 0.0, 1.0);
            buffer.addVertexWithUV(scrollbarPositionMaxX, minY + height, 0.0, 1.0, 1.0);
            buffer.addVertexWithUV(scrollbarPositionMaxX, minY, 0.0, 1.0, 0.0);
            buffer.addVertexWithUV(scrollbarPositionMinX, minY, 0.0, 0.0, 0.0);
            tessellator.draw();
            buffer.startDrawing(7);
            buffer.color(192, 192, 192, 255);
            buffer.addVertexWithUV(scrollbarPositionMinX, minY + height - 1, 0.0, 0.0, 1.0);
            buffer.addVertexWithUV(scrollbarPositionMaxX - 1, minY + height - 1, 0.0, 1.0, 1.0);
            buffer.addVertexWithUV(scrollbarPositionMaxX - 1, minY, 0.0, 1.0, 0.0);
            buffer.addVertexWithUV(scrollbarPositionMinX, minY, 0.0, 0.0, 0.0);
            tessellator.draw();
        }
        this.func_148142_b(this.mouseX, this.mouseY);
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }
    
    public void handleMouseInput() {
        if (this.isMouseYWithinSlotBounds(this.mouseY)) {
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom) {
                final int i2 = (this.width - this.getListWidth()) / 2;
                final int j2 = (this.width + this.getListWidth()) / 2;
                final int k2 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                final int l2 = k2 / this.slotHeight;
                if (l2 < this.getSize() && this.mouseX >= i2 && this.mouseX <= j2 && l2 >= 0 && k2 >= 0) {
                    this.elementClicked(l2, false, this.mouseX, this.mouseY);
                    this.selectedElement = l2;
                }
                else if (this.mouseX >= i2 && this.mouseX <= j2 && k2 < 0) {
                    this.func_148132_a(this.mouseX - i2, this.mouseY - this.top + (int)this.amountScrolled - 4);
                }
            }
            if (Mouse.isButtonDown(0) && this.getEnabled()) {
                if (this.initialClickY != -1.0f) {
                    if (this.initialClickY >= 0.0f) {
                        this.amountScrolled -= (this.mouseY - this.initialClickY) * this.scrollMultiplier;
                        this.initialClickY = (float)this.mouseY;
                    }
                }
                else {
                    boolean flag1 = true;
                    if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
                        final int j2 = (this.width - this.getListWidth()) / 2;
                        final int k2 = (this.width + this.getListWidth()) / 2;
                        final int l2 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                        final int i3 = l2 / this.slotHeight;
                        if (i3 < this.getSize() && this.mouseX >= j2 && this.mouseX <= k2 && i3 >= 0 && l2 >= 0) {
                            final boolean flag2 = i3 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked(i3, flag2, this.mouseX, this.mouseY);
                            this.selectedElement = i3;
                            this.lastClicked = Minecraft.getSystemTime();
                        }
                        else if (this.mouseX >= j2 && this.mouseX <= k2 && l2 < 0) {
                            this.func_148132_a(this.mouseX - j2, this.mouseY - this.top + (int)this.amountScrolled - 4);
                            flag1 = false;
                        }
                        final int i4 = this.getScrollBarX();
                        final int j3 = i4 + 6;
                        if (this.mouseX >= i4 && this.mouseX <= j3) {
                            this.scrollMultiplier = -1.0f;
                            int k3 = this.func_148135_f();
                            if (k3 < 1) {
                                k3 = 1;
                            }
                            int l3 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                            l3 = MathHelper.clamp_int(l3, 32, this.bottom - this.top - 8);
                            this.scrollMultiplier /= (this.bottom - this.top - l3) / k3;
                        }
                        else {
                            this.scrollMultiplier = 1.0f;
                        }
                        if (flag1) {
                            this.initialClickY = (float)this.mouseY;
                        }
                        else {
                            this.initialClickY = -2.0f;
                        }
                    }
                    else {
                        this.initialClickY = -2.0f;
                    }
                }
            }
            else {
                this.initialClickY = -1.0f;
            }
            if (!Mouse.isButtonDown(0)) {
                int wheel = Mouse.getEventDWheel();
                if (wheel != 0) {
                    if (wheel > 0) {
                        wheel = -1;
                    }
                    else if (wheel < 0) {
                        wheel = 1;
                    }
                }
            }
            int i2 = Mouse.getEventDWheel();
            if (i2 != 0) {
                if (i2 > 0) {
                    i2 = -1;
                }
                else if (i2 < 0) {
                    i2 = 1;
                }
                this.amountScrolled += i2 * this.slotHeight / 2;
            }
        }
    }
    
    @Override
    public int getListWidth() {
        return 220;
    }
    
    @Override
    protected void drawSelectionBox(final int p_148120_1_, final int p_148120_2_, final int mouseXIn, final int mouseYIn) {
        final int i = this.getSize();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        for (int j = 0; j < i; ++j) {
            final int k = p_148120_2_ + j * this.slotHeight + this.headerPadding;
            final int l = this.slotHeight - 4;
            if (k > this.bottom || k + l < this.top) {
                this.func_178040_a(j, p_148120_1_, k);
            }
            if (this.showSelectionBox && this.isSelected(j)) {
                final int i2 = this.left + (this.width / 2 - this.getListWidth() / 2);
                final int j2 = this.left + this.width / 2 + this.getListWidth() / 2;
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableTexture2D();
                worldrenderer.startDrawing(7);
                worldrenderer.color(128, 128, 128, 255);
                worldrenderer.addVertexWithUV(i2, k + l + 2, 0.0, 0.0, 1.0);
                worldrenderer.addVertexWithUV(j2, k + l + 2, 0.0, 1.0, 1.0);
                worldrenderer.addVertexWithUV(j2, k - 2, 0.0, 1.0, 0.0);
                worldrenderer.addVertexWithUV(i2, k - 2, 0.0, 0.0, 0.0);
                worldrenderer.color(0, 0, 0, 255);
                worldrenderer.addVertexWithUV(i2 + 1, k + l + 1, 0.0, 0.0, 1.0);
                worldrenderer.addVertexWithUV(j2 - 1, k + l + 1, 0.0, 1.0, 1.0);
                worldrenderer.addVertexWithUV(j2 - 1, k - 1, 0.0, 1.0, 0.0);
                worldrenderer.addVertexWithUV(i2 + 1, k - 1, 0.0, 0.0, 0.0);
                tessellator.draw();
                GlStateManager.enableTexture2D();
            }
            this.drawSlot(j, p_148120_1_, k, l, mouseXIn, mouseYIn);
        }
    }
    
    public int getScrollBarX() {
        return this.width / 2 + 124;
    }
    
    @Override
    protected void overlayBackground(final int startY, final int endY, final int startAlpha, final int endAlpha) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final float f = 32.0f;
        worldrenderer.startDrawing(7);
        worldrenderer.color(64, 64, 64, endAlpha);
        worldrenderer.addVertexWithUV(this.left, endY, 0.0, 0.0, endY / 32.0f);
        worldrenderer.addVertexWithUV(this.left + this.width, endY, 0.0, this.width / 32.0f, endY / 32.0f);
        worldrenderer.color(64, 64, 64, startAlpha);
        worldrenderer.addVertexWithUV(this.left + this.width, startY, 0.0, this.width / 32.0f, startY / 32.0f);
        worldrenderer.addVertexWithUV(this.left, startY, 0.0, 0.0, startY / 32.0f);
        tessellator.draw();
    }
    
    @Override
    public void setSlotXBoundsFromLeft(final int leftIn) {
        this.left = leftIn;
        this.right = leftIn + this.width;
    }
    
    @Override
    public int getSlotHeight() {
        return this.slotHeight;
    }
}
