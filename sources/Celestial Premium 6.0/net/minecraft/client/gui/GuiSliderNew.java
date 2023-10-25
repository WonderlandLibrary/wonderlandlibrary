/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiSliderNew
extends GuiButton {
    public double sliderValue = 1.0;
    public String dispString = "";
    public boolean dragging = false;
    public boolean showDecimal = true;
    public double minValue = 0.0;
    public double maxValue = 5.0;
    public int precision = 1;
    @Nullable
    public ISlider parent = null;
    public String suffix = "";
    public boolean drawString = true;

    public GuiSliderNew(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr) {
        this(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, null);
    }

    public GuiSliderNew(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, @Nullable ISlider par) {
        super(id, xPos, yPos, width, height, prefix);
        String val;
        this.minValue = minVal;
        this.maxValue = maxVal;
        this.sliderValue = (currentVal - this.minValue) / (this.maxValue - this.minValue);
        this.dispString = prefix;
        this.parent = par;
        this.suffix = suf;
        this.showDecimal = showDec;
        if (this.showDecimal) {
            val = Double.toString(this.sliderValue * (this.maxValue - this.minValue) + this.minValue);
            this.precision = Math.min(val.substring(val.indexOf(".") + 1).length(), 4);
        } else {
            val = Integer.toString((int)Math.round(this.sliderValue * (this.maxValue - this.minValue) + this.minValue));
            this.precision = 0;
        }
        this.displayString = this.dispString + val + this.suffix;
        this.drawString = drawStr;
        if (!this.drawString) {
            this.displayString = "";
        }
    }

    public GuiSliderNew(int id, int xPos, int yPos, String displayStr, double minVal, double maxVal, double currentVal, ISlider par) {
        this(id, xPos, yPos, 150, 20, displayStr, "", minVal, maxVal, currentVal, true, true, par);
    }

    @Override
    public int getHoverState(boolean par1) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);
                this.updateSlider();
            }
            GuiSliderNew.drawContinuousTexturedBox(BUTTON_TEXTURES, this.xPosition + (int)(this.sliderValue * (double)(this.width - 8)), this.yPosition, 0, 66, 8, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
        }
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
        if (super.mousePressed(par1Minecraft, par2, par3)) {
            this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);
            this.updateSlider();
            this.dragging = true;
            return true;
        }
        return false;
    }

    public void updateSlider() {
        String val;
        if (this.sliderValue < 0.0) {
            this.sliderValue = 0.0;
        }
        if (this.sliderValue > 1.0) {
            this.sliderValue = 1.0;
        }
        if (this.showDecimal) {
            val = Double.toString(this.sliderValue * (this.maxValue - this.minValue) + this.minValue);
            if (val.substring(val.indexOf(".") + 1).length() > this.precision) {
                if ((val = val.substring(0, val.indexOf(".") + this.precision + 1)).endsWith(".")) {
                    val = val.substring(0, val.indexOf(".") + this.precision);
                }
            } else {
                while (val.substring(val.indexOf(".") + 1).length() < this.precision) {
                    val = val + "0";
                }
            }
        } else {
            val = Integer.toString((int)Math.round(this.sliderValue * (this.maxValue - this.minValue) + this.minValue));
        }
        if (this.drawString) {
            this.displayString = this.dispString + val + this.suffix;
        }
        if (this.parent != null) {
            this.parent.onChangeSliderValue(this);
        }
    }

    @Override
    public void mouseReleased(int par1, int par2) {
        this.dragging = false;
    }

    public int getValueInt() {
        return (int)Math.round(this.sliderValue * (this.maxValue - this.minValue) + this.minValue);
    }

    public double getValue() {
        return this.sliderValue * (this.maxValue - this.minValue) + this.minValue;
    }

    public void setValue(double d) {
        this.sliderValue = (d - this.minValue) / (this.maxValue - this.minValue);
    }

    public static void drawContinuousTexturedBox(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int borderSize, float zLevel) {
        GuiSliderNew.drawContinuousTexturedBox(x, y, u, v, width, height, textureWidth, textureHeight, borderSize, borderSize, borderSize, borderSize, zLevel);
    }

    public static void drawContinuousTexturedBox(ResourceLocation res, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int borderSize, float zLevel) {
        GuiSliderNew.drawContinuousTexturedBox(res, x, y, u, v, width, height, textureWidth, textureHeight, borderSize, borderSize, borderSize, borderSize, zLevel);
    }

    public static void drawContinuousTexturedBox(ResourceLocation res, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(res);
        GuiSliderNew.drawContinuousTexturedBox(x, y, u, v, width, height, textureWidth, textureHeight, topBorder, bottomBorder, leftBorder, rightBorder, zLevel);
    }

    public static void drawContinuousTexturedBox(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.2f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int fillerWidth = textureWidth - leftBorder - rightBorder;
        int fillerHeight = textureHeight - topBorder - bottomBorder;
        int canvasWidth = width - leftBorder - rightBorder;
        int canvasHeight = height - topBorder - bottomBorder;
        int xPasses = canvasWidth / fillerWidth;
        int remainderWidth = canvasWidth % fillerWidth;
        int yPasses = canvasHeight / fillerHeight;
        int remainderHeight = canvasHeight % fillerHeight;
        GuiSliderNew.drawTexturedModalRect(x, y, u, v, leftBorder, topBorder, zLevel);
        GuiSliderNew.drawTexturedModalRect(x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder, zLevel);
        GuiSliderNew.drawTexturedModalRect(x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder, zLevel);
        GuiSliderNew.drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder, zLevel);
        for (int i = 0; i < xPasses + (remainderWidth > 0 ? 1 : 0); ++i) {
            GuiSliderNew.drawTexturedModalRect(x + leftBorder + i * fillerWidth, y, u + leftBorder, v, i == xPasses ? remainderWidth : fillerWidth, topBorder, zLevel);
            GuiSliderNew.drawTexturedModalRect(x + leftBorder + i * fillerWidth, y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, i == xPasses ? remainderWidth : fillerWidth, bottomBorder, zLevel);
            for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); ++j) {
                GuiSliderNew.drawTexturedModalRect(x + leftBorder + i * fillerWidth, y + topBorder + j * fillerHeight, u + leftBorder, v + topBorder, i == xPasses ? remainderWidth : fillerWidth, j == yPasses ? remainderHeight : fillerHeight, zLevel);
            }
        }
        for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); ++j) {
            GuiSliderNew.drawTexturedModalRect(x, y + topBorder + j * fillerHeight, u, v + topBorder, leftBorder, j == yPasses ? remainderHeight : fillerHeight, zLevel);
            GuiSliderNew.drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + j * fillerHeight, u + leftBorder + fillerWidth, v + topBorder, rightBorder, j == yPasses ? remainderHeight : fillerHeight, zLevel);
        }
    }

    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float zLevel) {
        float uScale = 0.00390625f;
        float vScale = 0.00390625f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuffer();
        wr.begin(7, DefaultVertexFormats.POSITION_TEX);
        wr.pos(x, y + height, zLevel).tex((float)u * 0.00390625f, (float)(v + height) * 0.00390625f).endVertex();
        wr.pos(x + width, y + height, zLevel).tex((float)(u + width) * 0.00390625f, (float)(v + height) * 0.00390625f).endVertex();
        wr.pos(x + width, y, zLevel).tex((float)(u + width) * 0.00390625f, (float)v * 0.00390625f).endVertex();
        wr.pos(x, y, zLevel).tex((float)u * 0.00390625f, (float)v * 0.00390625f).endVertex();
        tessellator.draw();
    }

    public static interface ISlider {
        public void onChangeSliderValue(GuiSliderNew var1);
    }
}

