/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

public abstract class GuiSlot {
    protected int bottom;
    protected final int slotHeight;
    protected int width;
    protected int mouseX;
    protected long lastClicked;
    protected boolean field_148163_i = true;
    protected int initialClickY = -2;
    private int scrollUpButtonID;
    private int scrollDownButtonID;
    protected boolean showSelectionBox = true;
    protected int top;
    private boolean enabled = true;
    protected int height;
    protected float amountScrolled;
    protected int right;
    protected boolean field_178041_q = true;
    protected boolean hasListHeader;
    protected final Minecraft mc;
    protected int mouseY;
    protected float scrollMultiplier;
    protected int headerPadding;
    protected int selectedElement = -1;
    protected int left;

    public int getListWidth() {
        return 220;
    }

    public void registerScrollButtons(int n, int n2) {
        this.scrollUpButtonID = n;
        this.scrollDownButtonID = n2;
    }

    protected void setHasListHeader(boolean bl, int n) {
        this.hasListHeader = bl;
        this.headerPadding = n;
        if (!bl) {
            this.headerPadding = 0;
        }
    }

    public void setDimensions(int n, int n2, int n3, int n4) {
        this.width = n;
        this.height = n2;
        this.top = n3;
        this.bottom = n4;
        this.left = 0;
        this.right = n;
    }

    public boolean isMouseYWithinSlotBounds(int n) {
        return n >= this.top && n <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }

    public int getAmountScrolled() {
        return (int)this.amountScrolled;
    }

    protected void drawListHeader(int n, int n2, Tessellator tessellator) {
    }

    public void setEnabled(boolean bl) {
        this.enabled = bl;
    }

    public GuiSlot(Minecraft minecraft, int n, int n2, int n3, int n4, int n5) {
        this.mc = minecraft;
        this.width = n;
        this.height = n2;
        this.top = n3;
        this.bottom = n4;
        this.slotHeight = n5;
        this.left = 0;
        this.right = n;
    }

    protected abstract int getSize();

    protected void func_148132_a(int n, int n2) {
    }

    protected void func_148142_b(int n, int n2) {
    }

    protected abstract void drawBackground();

    public void setShowSelectionBox(boolean bl) {
        this.showSelectionBox = bl;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void handleMouseInput() {
        if (this.isMouseYWithinSlotBounds(this.mouseY)) {
            int n;
            int n2;
            int n3;
            int n4;
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom) {
                n4 = (this.width - this.getListWidth()) / 2;
                n3 = (this.width + this.getListWidth()) / 2;
                n2 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                n = n2 / this.slotHeight;
                if (n < this.getSize() && this.mouseX >= n4 && this.mouseX <= n3 && n >= 0 && n2 >= 0) {
                    this.elementClicked(n, false, this.mouseX, this.mouseY);
                    this.selectedElement = n;
                } else if (this.mouseX >= n4 && this.mouseX <= n3 && n2 < 0) {
                    this.func_148132_a(this.mouseX - n4, this.mouseY - this.top + (int)this.amountScrolled - 4);
                }
            }
            if (Mouse.isButtonDown((int)0) && this.getEnabled()) {
                if (this.initialClickY == -1) {
                    n4 = 1;
                    if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
                        int n5;
                        n3 = (this.width - this.getListWidth()) / 2;
                        n2 = (this.width + this.getListWidth()) / 2;
                        n = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                        int n6 = n / this.slotHeight;
                        if (n6 < this.getSize() && this.mouseX >= n3 && this.mouseX <= n2 && n6 >= 0 && n >= 0) {
                            n5 = n6 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L ? 1 : 0;
                            this.elementClicked(n6, n5 != 0, this.mouseX, this.mouseY);
                            this.selectedElement = n6;
                            this.lastClicked = Minecraft.getSystemTime();
                        } else if (this.mouseX >= n3 && this.mouseX <= n2 && n < 0) {
                            this.func_148132_a(this.mouseX - n3, this.mouseY - this.top + (int)this.amountScrolled - 4);
                            n4 = 0;
                        }
                        n5 = this.getScrollBarX();
                        int n7 = n5 + 6;
                        if (this.mouseX >= n5 && this.mouseX <= n7) {
                            this.scrollMultiplier = -1.0f;
                            int n8 = this.func_148135_f();
                            if (n8 < 1) {
                                n8 = 1;
                            }
                            int n9 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getContentHeight());
                            n9 = MathHelper.clamp_int(n9, 32, this.bottom - this.top - 8);
                            this.scrollMultiplier /= (float)(this.bottom - this.top - n9) / (float)n8;
                        } else {
                            this.scrollMultiplier = 1.0f;
                        }
                        this.initialClickY = n4 != 0 ? this.mouseY : -2;
                    } else {
                        this.initialClickY = -2;
                    }
                } else if (this.initialClickY >= 0) {
                    this.amountScrolled -= (float)(this.mouseY - this.initialClickY) * this.scrollMultiplier;
                    this.initialClickY = this.mouseY;
                }
            } else {
                this.initialClickY = -1;
            }
            n4 = Mouse.getEventDWheel();
            if (n4 != 0) {
                if (n4 > 0) {
                    n4 = -1;
                } else if (n4 < 0) {
                    n4 = 1;
                }
                this.amountScrolled += (float)(n4 * this.slotHeight / 2);
            }
        }
    }

    protected abstract void elementClicked(int var1, boolean var2, int var3, int var4);

    protected void overlayBackground(int n, int n2, int n3, int n4) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        float f = 32.0f;
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldRenderer.pos(this.left, n2, 0.0).tex(0.0, (float)n2 / 32.0f).color(64, 64, 64, n4).endVertex();
        worldRenderer.pos(this.left + this.width, n2, 0.0).tex((float)this.width / 32.0f, (float)n2 / 32.0f).color(64, 64, 64, n4).endVertex();
        worldRenderer.pos(this.left + this.width, n, 0.0).tex((float)this.width / 32.0f, (float)n / 32.0f).color(64, 64, 64, n3).endVertex();
        worldRenderer.pos(this.left, n, 0.0).tex(0.0, (float)n / 32.0f).color(64, 64, 64, n3).endVertex();
        tessellator.draw();
    }

    protected void drawSelectionBox(int n, int n2, int n3, int n4) {
        int n5 = this.getSize();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        int n6 = 0;
        while (n6 < n5) {
            int n7 = n2 + n6 * this.slotHeight + this.headerPadding;
            int n8 = this.slotHeight - 4;
            if (n7 > this.bottom || n7 + n8 < this.top) {
                this.func_178040_a(n6, n, n7);
            }
            if (this.showSelectionBox && this.isSelected(n6)) {
                int n9 = this.left + (this.width / 2 - this.getListWidth() / 2);
                int n10 = this.left + this.width / 2 + this.getListWidth() / 2;
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableTexture2D();
                worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldRenderer.pos(n9, n7 + n8 + 2, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
                worldRenderer.pos(n10, n7 + n8 + 2, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
                worldRenderer.pos(n10, n7 - 2, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
                worldRenderer.pos(n9, n7 - 2, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
                worldRenderer.pos(n9 + 1, n7 + n8 + 1, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(n10 - 1, n7 + n8 + 1, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(n10 - 1, n7 - 1, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(n9 + 1, n7 - 1, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
            }
            this.drawSlot(n6, n, n7, n8, n3, n4);
            ++n6;
        }
    }

    protected void bindAmountScrolled() {
        this.amountScrolled = MathHelper.clamp_float(this.amountScrolled, 0.0f, this.func_148135_f());
    }

    protected abstract boolean isSelected(int var1);

    public int func_148135_f() {
        return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
    }

    protected void func_178040_a(int n, int n2, int n3) {
    }

    public int getSlotIndexFromScreenCoords(int n, int n2) {
        int n3 = this.left + this.width / 2 - this.getListWidth() / 2;
        int n4 = this.left + this.width / 2 + this.getListWidth() / 2;
        int n5 = n2 - this.top - this.headerPadding + (int)this.amountScrolled - 4;
        int n6 = n5 / this.slotHeight;
        return n < this.getScrollBarX() && n >= n3 && n <= n4 && n6 >= 0 && n5 >= 0 && n6 < this.getSize() ? n6 : -1;
    }

    public void actionPerformed(GuiButton guiButton) {
        if (guiButton.enabled) {
            if (guiButton.id == this.scrollUpButtonID) {
                this.amountScrolled -= (float)(this.slotHeight * 2 / 3);
                this.initialClickY = -2;
                this.bindAmountScrolled();
            } else if (guiButton.id == this.scrollDownButtonID) {
                this.amountScrolled += (float)(this.slotHeight * 2 / 3);
                this.initialClickY = -2;
                this.bindAmountScrolled();
            }
        }
    }

    public int getSlotHeight() {
        return this.slotHeight;
    }

    protected int getContentHeight() {
        return this.getSize() * this.slotHeight + this.headerPadding;
    }

    public void setSlotXBoundsFromLeft(int n) {
        this.left = n;
        this.right = n + this.width;
    }

    public void scrollBy(int n) {
        this.amountScrolled += (float)n;
        this.bindAmountScrolled();
        this.initialClickY = -2;
    }

    protected abstract void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6);

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
            this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            float f2 = 32.0f;
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldRenderer.pos(this.left, this.bottom, 0.0).tex((float)this.left / f2, (float)(this.bottom + (int)this.amountScrolled) / f2).color(32, 32, 32, 255).endVertex();
            worldRenderer.pos(this.right, this.bottom, 0.0).tex((float)this.right / f2, (float)(this.bottom + (int)this.amountScrolled) / f2).color(32, 32, 32, 255).endVertex();
            worldRenderer.pos(this.right, this.top, 0.0).tex((float)this.right / f2, (float)(this.top + (int)this.amountScrolled) / f2).color(32, 32, 32, 255).endVertex();
            worldRenderer.pos(this.left, this.top, 0.0).tex((float)this.left / f2, (float)(this.top + (int)this.amountScrolled) / f2).color(32, 32, 32, 255).endVertex();
            tessellator.draw();
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
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldRenderer.pos(this.left, this.top + n7, 0.0).tex(0.0, 1.0).color(0, 0, 0, 0).endVertex();
            worldRenderer.pos(this.right, this.top + n7, 0.0).tex(1.0, 1.0).color(0, 0, 0, 0).endVertex();
            worldRenderer.pos(this.right, this.top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
            worldRenderer.pos(this.left, this.top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldRenderer.pos(this.left, this.bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
            worldRenderer.pos(this.right, this.bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
            worldRenderer.pos(this.right, this.bottom - n7, 0.0).tex(1.0, 0.0).color(0, 0, 0, 0).endVertex();
            worldRenderer.pos(this.left, this.bottom - n7, 0.0).tex(0.0, 0.0).color(0, 0, 0, 0).endVertex();
            tessellator.draw();
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

    protected int getScrollBarX() {
        return this.width / 2 + 124;
    }
}

