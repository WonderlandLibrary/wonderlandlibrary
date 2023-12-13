// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import java.util.List;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.util.XuluTessellator;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraftforge.fml.client.config.GuiUtils;
import java.util.ArrayList;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderTooltipEvent;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class ToolTips extends Module
{
    public static Value<Boolean> cf;
    public static Value<Boolean> cb;
    public static Value<Boolean> outline;
    public static Value<Integer> r;
    public static Value<Integer> g;
    public static Value<Integer> b;
    public static Value<Integer> Or;
    public static Value<Integer> Og;
    public static Value<Integer> Ob;
    
    public ToolTips() {
        super("ToolTips", "Custom ToolTips", 0, Category.RENDER, true);
        ToolTips.cf = this.register(new Value<Boolean>("Custom Font", this, true));
        ToolTips.cb = this.register(new Value<Boolean>("Custom Background", this, true));
        ToolTips.outline = this.register(new Value<Boolean>("Outline", this, true));
        ToolTips.r = this.register(new Value<Integer>("Red", this, 7, 0, 255));
        ToolTips.g = this.register(new Value<Integer>("Green", this, 12, 0, 255));
        ToolTips.b = this.register(new Value<Integer>("Blue", this, 17, 0, 255));
        ToolTips.Or = this.register(new Value<Integer>("Outline Red", this, 10, 0, 255));
        ToolTips.Og = this.register(new Value<Integer>("Outline Green", this, 12, 0, 255));
        ToolTips.Ob = this.register(new Value<Integer>("Outline Blue", this, 43, 0, 255));
    }
    
    @Override
    public void onEnable() {
        ToolTips.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        ToolTips.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onToolText(final RenderTooltipEvent.Pre event) {
        final int mouseX = event.getX();
        final int mouseY = event.getY();
        final int screenWidth = event.getScreenWidth();
        final int screenHeight = event.getScreenHeight();
        final int maxTextWidth = event.getMaxWidth();
        List<String> textLines = (List<String>)event.getLines();
        final FontRenderer font = event.getFontRenderer();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        int tooltipTextWidth = 0;
        for (final String textLine : textLines) {
            final int textLineWidth = font.getStringWidth(textLine);
            if (textLineWidth > tooltipTextWidth) {
                tooltipTextWidth = textLineWidth;
            }
        }
        boolean needsWrap = false;
        int titleLinesCount = 1;
        int tooltipX = mouseX + 12;
        if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
            tooltipX = mouseX - 16 - tooltipTextWidth;
            if (tooltipX < 4) {
                if (mouseX > screenWidth / 2) {
                    tooltipTextWidth = mouseX - 12 - 8;
                }
                else {
                    tooltipTextWidth = screenWidth - 16 - mouseX;
                }
                needsWrap = true;
            }
        }
        if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
            tooltipTextWidth = maxTextWidth;
            needsWrap = true;
        }
        if (needsWrap) {
            int wrappedTooltipWidth = 0;
            final List<String> wrappedTextLines = new ArrayList<String>();
            for (int i = 0; i < textLines.size(); ++i) {
                final String textLine2 = textLines.get(i);
                final List<String> wrappedLine = (List<String>)font.listFormattedStringToWidth(textLine2, tooltipTextWidth);
                if (i == 0) {
                    titleLinesCount = wrappedLine.size();
                }
                for (final String line : wrappedLine) {
                    final int lineWidth = font.getStringWidth(line);
                    if (lineWidth > wrappedTooltipWidth) {
                        wrappedTooltipWidth = lineWidth;
                    }
                    wrappedTextLines.add(line);
                }
            }
            tooltipTextWidth = wrappedTooltipWidth;
            textLines = wrappedTextLines;
            if (mouseX > screenWidth / 2) {
                tooltipX = mouseX - 16 - tooltipTextWidth;
            }
            else {
                tooltipX = mouseX + 12;
            }
        }
        int tooltipY = mouseY - 12;
        int tooltipHeight = 8;
        if (textLines.size() > 1) {
            tooltipHeight += (textLines.size() - 1) * 10;
            if (textLines.size() > titleLinesCount) {
                tooltipHeight += 2;
            }
        }
        if (tooltipY < 4) {
            tooltipY = 4;
        }
        else if (tooltipY + tooltipHeight + 4 > screenHeight) {
            tooltipY = screenHeight - tooltipHeight - 4;
        }
        if (!ToolTips.cb.getValue()) {
            final int zLevel = 300;
            final int backgroundColor = -267386864;
            final int borderColorStart = 1347420415;
            final int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | (borderColorStart & 0xFF000000);
            GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(300, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(300, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            GuiUtils.drawGradientRect(300, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
            GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);
        }
        else {
            Gui.drawRect(tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, new Color(ToolTips.r.getValue(), ToolTips.g.getValue(), ToolTips.b.getValue()).getRGB());
            if (ToolTips.outline.getValue()) {
                XuluTessellator.drawRectOutline(tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, 1.0, new Color(ToolTips.Or.getValue(), ToolTips.Og.getValue(), ToolTips.Ob.getValue()).getRGB());
            }
        }
        for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber) {
            final String line2 = textLines.get(lineNumber);
            if (ToolTips.cf.getValue()) {
                Xulu.cFontRenderer.drawStringWithShadow(line2, (float)tooltipX, (float)tooltipY, -1);
            }
            else {
                font.drawStringWithShadow(line2, (float)tooltipX, (float)tooltipY, -1);
            }
            if (lineNumber + 1 == titleLinesCount) {
                tooltipY += 2;
            }
            tooltipY += 10;
        }
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        event.setCanceled(true);
    }
}
