// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import org.lwjgl.input.Mouse;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;

public abstract class GuiSlot
{
    protected final Minecraft mc;
    protected int width;
    protected int height;
    protected int top;
    protected int bottom;
    protected int right;
    protected int left;
    protected final int slotHeight;
    private int scrollUpButtonID;
    private int scrollDownButtonID;
    protected int mouseX;
    protected int mouseY;
    protected boolean field_148163_i;
    protected float initialClickY;
    protected float scrollMultiplier;
    protected float amountScrolled;
    protected int selectedElement;
    protected long lastClicked;
    protected boolean drawBackground;
    protected boolean showSelectionBox;
    protected boolean hasListHeader;
    protected int headerPadding;
    private boolean enabled;
    private static final String __OBFID = "CL_00000679";
    
    public GuiSlot(final Minecraft mcIn, final int width, final int height, final int p_i1052_4_, final int p_i1052_5_, final int p_i1052_6_) {
        this.field_148163_i = true;
        this.initialClickY = -2.0f;
        this.selectedElement = -1;
        this.drawBackground = true;
        this.showSelectionBox = true;
        this.enabled = true;
        this.mc = mcIn;
        this.width = width;
        this.height = height;
        this.top = p_i1052_4_;
        this.bottom = p_i1052_5_;
        this.slotHeight = p_i1052_6_;
        this.left = 0;
        this.right = width;
    }
    
    public void setDimensions(final int p_148122_1_, final int p_148122_2_, final int p_148122_3_, final int p_148122_4_) {
        this.width = p_148122_1_;
        this.height = p_148122_2_;
        this.top = p_148122_3_;
        this.bottom = p_148122_4_;
        this.left = 0;
        this.right = p_148122_1_;
    }
    
    public void setShowSelectionBox(final boolean p_148130_1_) {
        this.showSelectionBox = p_148130_1_;
    }
    
    protected void setHasListHeader(final boolean p_148133_1_, final int p_148133_2_) {
        this.hasListHeader = p_148133_1_;
        this.headerPadding = p_148133_2_;
        if (!p_148133_1_) {
            this.headerPadding = 0;
        }
    }
    
    protected abstract int getSize();
    
    protected abstract void elementClicked(final int p0, final boolean p1, final int p2, final int p3);
    
    protected abstract boolean isSelected(final int p0);
    
    protected int getContentHeight() {
        return this.getSize() * this.slotHeight + this.headerPadding;
    }
    
    protected abstract void drawBackground();
    
    protected void func_178040_a(final int p_178040_1_, final int p_178040_2_, final int p_178040_3_) {
    }
    
    protected abstract void drawSlot(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    
    protected void drawListHeader(final int p_148129_1_, final int p_148129_2_, final Tessellator p_148129_3_) {
    }
    
    protected void func_148132_a(final int p_148132_1_, final int p_148132_2_) {
    }
    
    protected void func_148142_b(final int p_148142_1_, final int p_148142_2_) {
    }
    
    public int getSlotIndexFromScreenCoords(final int p_148124_1_, final int p_148124_2_) {
        final int var3 = this.left + this.width / 2 - this.getListWidth() / 2;
        final int var4 = this.left + this.width / 2 + this.getListWidth() / 2;
        final int var5 = p_148124_2_ - this.top - this.headerPadding + (int)this.amountScrolled - 4;
        final int var6 = var5 / this.slotHeight;
        return (p_148124_1_ < this.getScrollBarX() && p_148124_1_ >= var3 && p_148124_1_ <= var4 && var6 >= 0 && var5 >= 0 && var6 < this.getSize()) ? var6 : -1;
    }
    
    public void registerScrollButtons(final int p_148134_1_, final int p_148134_2_) {
        this.scrollUpButtonID = p_148134_1_;
        this.scrollDownButtonID = p_148134_2_;
    }
    
    protected void bindAmountScrolled() {
        int var1 = this.func_148135_f();
        if (var1 < 0) {
            var1 /= 2;
        }
        if (!this.field_148163_i && var1 < 0) {
            var1 = 0;
        }
        this.amountScrolled = MathHelper.clamp_float(this.amountScrolled, 0.0f, var1);
    }
    
    public int func_148135_f() {
        return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
    }
    
    public int getAmountScrolled() {
        return (int)this.amountScrolled;
    }
    
    public boolean isMouseYWithinSlotBounds(final int p_148141_1_) {
        return p_148141_1_ >= this.top && p_148141_1_ <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }
    
    public void scrollBy(final int p_148145_1_) {
        this.amountScrolled += p_148145_1_;
        this.bindAmountScrolled();
        this.initialClickY = -2.0f;
    }
    
    public void actionPerformed(final GuiButton p_148147_1_) {
        if (p_148147_1_.enabled) {
            if (p_148147_1_.id == this.scrollUpButtonID) {
                this.amountScrolled -= this.slotHeight * 2 / 3;
                this.initialClickY = -2.0f;
                this.bindAmountScrolled();
            }
            else if (p_148147_1_.id == this.scrollDownButtonID) {
                this.amountScrolled += this.slotHeight * 2 / 3;
                this.initialClickY = -2.0f;
                this.bindAmountScrolled();
            }
        }
    }
    
    public void drawScreen(final int x, final int y, final float partial) {
        if (this.drawBackground) {
            this.mouseX = x;
            this.mouseY = y;
            this.drawBackground();
            final int var4 = this.getScrollBarX();
            final int var5 = var4 + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            final Tessellator var6 = Tessellator.getInstance();
            final WorldRenderer var7 = var6.getWorldRenderer();
            this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final float var8 = 32.0f;
            var7.startDrawingQuads();
            var7.func_178991_c(2105376);
            var7.addVertexWithUV(this.left, this.bottom, 0.0, this.left / var8, (this.bottom + (int)this.amountScrolled) / var8);
            var7.addVertexWithUV(this.right, this.bottom, 0.0, this.right / var8, (this.bottom + (int)this.amountScrolled) / var8);
            var7.addVertexWithUV(this.right, this.top, 0.0, this.right / var8, (this.top + (int)this.amountScrolled) / var8);
            var7.addVertexWithUV(this.left, this.top, 0.0, this.left / var8, (this.top + (int)this.amountScrolled) / var8);
            var6.draw();
            final int var9 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            final int var10 = this.top + 4 - (int)this.amountScrolled;
            if (this.hasListHeader) {
                this.drawListHeader(var9, var10, var6);
            }
            this.drawSelectionBox(var9, var10, x, y);
            GlStateManager.disableDepth();
            final byte var11 = 4;
            this.overlayBackground(0, this.top, 255, 255);
            this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTextures();
            var7.startDrawingQuads();
            var7.drawColor(0, 0);
            var7.addVertexWithUV(this.left, this.top + var11, 0.0, 0.0, 1.0);
            var7.addVertexWithUV(this.right, this.top + var11, 0.0, 1.0, 1.0);
            var7.drawColor(0, 255);
            var7.addVertexWithUV(this.right, this.top, 0.0, 1.0, 0.0);
            var7.addVertexWithUV(this.left, this.top, 0.0, 0.0, 0.0);
            var6.draw();
            var7.startDrawingQuads();
            var7.drawColor(0, 255);
            var7.addVertexWithUV(this.left, this.bottom, 0.0, 0.0, 1.0);
            var7.addVertexWithUV(this.right, this.bottom, 0.0, 1.0, 1.0);
            var7.drawColor(0, 0);
            var7.addVertexWithUV(this.right, this.bottom - var11, 0.0, 1.0, 0.0);
            var7.addVertexWithUV(this.left, this.bottom - var11, 0.0, 0.0, 0.0);
            var6.draw();
            final int var12 = this.func_148135_f();
            if (var12 > 0) {
                int var13 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                var13 = MathHelper.clamp_int(var13, 32, this.bottom - this.top - 8);
                int var14 = (int)this.amountScrolled * (this.bottom - this.top - var13) / var12 + this.top;
                if (var14 < this.top) {
                    var14 = this.top;
                }
                var7.startDrawingQuads();
                var7.drawColor(0, 255);
                var7.addVertexWithUV(var4, this.bottom, 0.0, 0.0, 1.0);
                var7.addVertexWithUV(var5, this.bottom, 0.0, 1.0, 1.0);
                var7.addVertexWithUV(var5, this.top, 0.0, 1.0, 0.0);
                var7.addVertexWithUV(var4, this.top, 0.0, 0.0, 0.0);
                var6.draw();
                var7.startDrawingQuads();
                var7.drawColor(8421504, 255);
                var7.addVertexWithUV(var4, var14 + var13, 0.0, 0.0, 1.0);
                var7.addVertexWithUV(var5, var14 + var13, 0.0, 1.0, 1.0);
                var7.addVertexWithUV(var5, var14, 0.0, 1.0, 0.0);
                var7.addVertexWithUV(var4, var14, 0.0, 0.0, 0.0);
                var6.draw();
                var7.startDrawingQuads();
                var7.drawColor(12632256, 255);
                var7.addVertexWithUV(var4, var14 + var13 - 1, 0.0, 0.0, 1.0);
                var7.addVertexWithUV(var5 - 1, var14 + var13 - 1, 0.0, 1.0, 1.0);
                var7.addVertexWithUV(var5 - 1, var14, 0.0, 1.0, 0.0);
                var7.addVertexWithUV(var4, var14, 0.0, 0.0, 0.0);
                var6.draw();
            }
            this.func_148142_b(x, y);
            GlStateManager.enableTextures();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }
    
    public void handleMouseScrolling() {
        if (this.isMouseYWithinSlotBounds(this.mouseY)) {
            if (Mouse.isButtonDown(0) && this.getEnabled()) {
                if (this.initialClickY == -1.0f) {
                    boolean var1 = true;
                    if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
                        final int var2 = this.width / 2 - this.getListWidth() / 2;
                        final int var3 = this.width / 2 + this.getListWidth() / 2;
                        final int var4 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                        final int var5 = var4 / this.slotHeight;
                        if (this.mouseX >= var2 && this.mouseX <= var3 && var5 >= 0 && var4 >= 0 && var5 < this.getSize()) {
                            final boolean var6 = var5 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked(var5, var6, this.mouseX, this.mouseY);
                            this.selectedElement = var5;
                            this.lastClicked = Minecraft.getSystemTime();
                        }
                        else if (this.mouseX >= var2 && this.mouseX <= var3 && var4 < 0) {
                            this.func_148132_a(this.mouseX - var2, this.mouseY - this.top + (int)this.amountScrolled - 4);
                            var1 = false;
                        }
                        final int var7 = this.getScrollBarX();
                        final int var8 = var7 + 6;
                        if (this.mouseX >= var7 && this.mouseX <= var8) {
                            this.scrollMultiplier = -1.0f;
                            int var9 = this.func_148135_f();
                            if (var9 < 1) {
                                var9 = 1;
                            }
                            int var10 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                            var10 = MathHelper.clamp_int(var10, 32, this.bottom - this.top - 8);
                            this.scrollMultiplier /= (this.bottom - this.top - var10) / var9;
                        }
                        else {
                            this.scrollMultiplier = 1.0f;
                        }
                        if (var1) {
                            this.initialClickY = this.mouseY;
                        }
                        else {
                            this.initialClickY = -2.0f;
                        }
                    }
                    else {
                        this.initialClickY = -2.0f;
                    }
                }
                else if (this.initialClickY >= 0.0f) {
                    this.amountScrolled -= (this.mouseY - this.initialClickY) * this.scrollMultiplier;
                    this.initialClickY = this.mouseY;
                }
            }
            else {
                this.initialClickY = -1.0f;
            }
            int var11 = Mouse.getEventDWheel();
            if (var11 != 0) {
                if (var11 > 0) {
                    var11 = -1;
                }
                else if (var11 < 0) {
                    var11 = 1;
                }
                this.amountScrolled += var11 * this.slotHeight / 2;
            }
        }
    }
    
    public void setEnabled(final boolean p_148143_1_) {
        this.enabled = p_148143_1_;
    }
    
    public boolean getEnabled() {
        return this.enabled;
    }
    
    public int getListWidth() {
        return 220;
    }
    
    protected void drawSelectionBox(final int p_148120_1_, final int p_148120_2_, final int p_148120_3_, final int p_148120_4_) {
        final int var5 = this.getSize();
        final Tessellator var6 = Tessellator.getInstance();
        final WorldRenderer var7 = var6.getWorldRenderer();
        for (int var8 = 0; var8 < var5; ++var8) {
            final int var9 = p_148120_2_ + var8 * this.slotHeight + this.headerPadding;
            final int var10 = this.slotHeight - 4;
            if (var9 > this.bottom || var9 + var10 < this.top) {
                this.func_178040_a(var8, p_148120_1_, var9);
            }
            if (this.showSelectionBox && this.isSelected(var8)) {
                final int var11 = this.left + (this.width / 2 - this.getListWidth() / 2);
                final int var12 = this.left + this.width / 2 + this.getListWidth() / 2;
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableTextures();
                var7.startDrawingQuads();
                var7.func_178991_c(8421504);
                var7.addVertexWithUV(var11, var9 + var10 + 2, 0.0, 0.0, 1.0);
                var7.addVertexWithUV(var12, var9 + var10 + 2, 0.0, 1.0, 1.0);
                var7.addVertexWithUV(var12, var9 - 2, 0.0, 1.0, 0.0);
                var7.addVertexWithUV(var11, var9 - 2, 0.0, 0.0, 0.0);
                var7.func_178991_c(0);
                var7.addVertexWithUV(var11 + 1, var9 + var10 + 1, 0.0, 0.0, 1.0);
                var7.addVertexWithUV(var12 - 1, var9 + var10 + 1, 0.0, 1.0, 1.0);
                var7.addVertexWithUV(var12 - 1, var9 - 1, 0.0, 1.0, 0.0);
                var7.addVertexWithUV(var11 + 1, var9 - 1, 0.0, 0.0, 0.0);
                var6.draw();
                GlStateManager.enableTextures();
            }
            this.drawSlot(var8, p_148120_1_, var9, var10, p_148120_3_, p_148120_4_);
        }
    }
    
    protected int getScrollBarX() {
        return this.width / 2 + 124;
    }
    
    protected void overlayBackground(final int p_148136_1_, final int p_148136_2_, final int p_148136_3_, final int p_148136_4_) {
        final Tessellator var5 = Tessellator.getInstance();
        final WorldRenderer var6 = var5.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final float var7 = 32.0f;
        var6.startDrawingQuads();
        var6.drawColor(4210752, p_148136_4_);
        var6.addVertexWithUV(this.left, p_148136_2_, 0.0, 0.0, p_148136_2_ / var7);
        var6.addVertexWithUV(this.left + this.width, p_148136_2_, 0.0, this.width / var7, p_148136_2_ / var7);
        var6.drawColor(4210752, p_148136_3_);
        var6.addVertexWithUV(this.left + this.width, p_148136_1_, 0.0, this.width / var7, p_148136_1_ / var7);
        var6.addVertexWithUV(this.left, p_148136_1_, 0.0, 0.0, p_148136_1_ / var7);
        var5.draw();
    }
    
    public void setSlotXBoundsFromLeft(final int p_148140_1_) {
        this.left = p_148140_1_;
        this.right = p_148140_1_ + this.width;
    }
    
    public int getSlotHeight() {
        return this.slotHeight;
    }
}
