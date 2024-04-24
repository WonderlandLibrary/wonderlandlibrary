
package Method.Client.module.misc;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.font.CFont;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.input.Keyboard;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static Method.Client.Main.setmgr;
import static net.minecraftforge.fml.client.config.GuiUtils.drawGradientRect;

public class ToolTipPlus extends Module {

    /////////////////////

    public ToolTipPlus() {
        super("ToolTipPlus", Keyboard.KEY_NONE, Category.MISC, "ToolTipPlus Item Size");
    }

    Setting TooltipModify = setmgr.add(new Setting("TooltipModify", this, true));
    Setting Customfont = setmgr.add(new Setting("Customfont", this, true, TooltipModify));
    Setting CustomBackground = setmgr.add(new Setting("CustomBackground", this, false, TooltipModify));
    Setting Color = setmgr.add(new Setting("Color", this, .1, 1, .95, .8, TooltipModify));
    Setting Outline = setmgr.add(new Setting("Outline", this, false, TooltipModify));
    Setting OutlineColor = setmgr.add(new Setting("OutlineColor", this, .55, 1, 1, 1, TooltipModify));
    Setting ItemSize = setmgr.add(new Setting("ItemSize", this, true));

    public String bytesToString(int count) {
        if (count >= 1024)
            return String.format("%.2f kb", count / (float) 1024);
        return String.format("%d bytes", count);
    }

    @Subscribe
    public void ItemTooltipEvent(ItemTooltipEvent event) {
        if (!ItemSize.getValBoolean())
            return;
        final String interesting = String.valueOf(event.getItemStack().getTagCompound());
        byte[] utf8Bytes = interesting.getBytes(StandardCharsets.UTF_8);
        event.getToolTip().add(" " + bytesToString(utf8Bytes.length) + " TextSize");

        final String dd = String.valueOf(event.getItemStack().getTextComponent());
        byte[] ee = dd.getBytes(StandardCharsets.UTF_8);
        event.getToolTip().add(" " + bytesToString(ee.length) + " TagSize");

    }

    @Subscribe
    public void RendertooltipPre(RenderTooltipEvent.Pre event) {
        if (!TooltipModify.getValBoolean())
            return;
        int mouseX = event.getX();
        int screenWidth = event.getScreenWidth();
        int screenHeight = event.getScreenHeight();
        int maxTextWidth = event.getMaxWidth();
        List<String> textLines = event.getLines();
        FontRenderer font = event.getFontRenderer();

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        int tooltipTextWidth = 0;

        for (String textLine : textLines) {
            int textLineWidth = font.getStringWidth(textLine);
            if (textLineWidth > tooltipTextWidth) {
                tooltipTextWidth = textLineWidth;
            }
        }

        boolean needsWrap = false;

        int titleLinesCount = 1;
        int tooltipX = mouseX + 12;
        if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
            tooltipX = mouseX - 16 - tooltipTextWidth;
            if (tooltipX < 4) // if the tooltip doesn't fit on the screen
            {
                if (mouseX > screenWidth / 2) {
                    tooltipTextWidth = mouseX - 12 - 8;
                } else {
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
            List<String> wrappedTextLines = new ArrayList<>();
            for (int i = 0; i < textLines.size(); i++) {
                String textLine = textLines.get(i);
                List<String> wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth);
                if (i == 0) {
                    titleLinesCount = wrappedLine.size();
                }

                for (String line : wrappedLine) {
                    int lineWidth = font.getStringWidth(line);
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
            } else {
                tooltipX = mouseX + 12;
            }
        }

        int tooltipY = event.getY() - 12;
        int tooltipHeight = 8;

        if (textLines.size() > 1) {
            tooltipHeight += (textLines.size() - 1) * 10;
            if (textLines.size() > titleLinesCount) {
                tooltipHeight += 2; // gap between title lines and next lines
            }
        }

        if (tooltipY < 4) {
            tooltipY = 4;
        } else if (tooltipY + tooltipHeight + 4 > screenHeight) {
            tooltipY = screenHeight - tooltipHeight - 4;
        }

        if (!CustomBackground.getValBoolean()) {

            final int zLevel = 300;
            int backgroundColor = 0xF0100010;
            int borderColorStart = 0x505000FF;
            int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);
        } else {
            Gui.drawRect(tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, Color.getcolor());
            if (Outline.getValBoolean())
                RenderUtils.drawRectOutline(tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, 1, OutlineColor.getcolor());
        }


        for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber) {
            String line = textLines.get(lineNumber);
            if (Customfont.getValBoolean()) {
                CFont.tfontRenderer22.drawStringWithShadow(line, (float) tooltipX, (float) tooltipY, -1);
            } else {
                font.drawStringWithShadow(line, (float) tooltipX, (float) tooltipY, -1);
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


