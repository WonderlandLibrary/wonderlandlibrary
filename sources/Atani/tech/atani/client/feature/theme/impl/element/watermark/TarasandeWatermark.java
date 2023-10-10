package tech.atani.client.feature.theme.impl.element.watermark;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import tech.atani.client.feature.module.impl.hud.WaterMark;
import tech.atani.client.feature.theme.data.ThemeObjectInfo;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ThemeObjectType;
import tech.atani.client.feature.theme.impl.element.DraggableElement;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.utility.render.RenderUtil;

import java.awt.*;

@ThemeObjectInfo(name = "Tarasande", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.WATERMARK)
public class TarasandeWatermark extends DraggableElement {

    public TarasandeWatermark() {
        super(0, 0, 100, mc.fontRendererObj.FONT_HEIGHT, null, WaterMark.class);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY) {
        FontRenderer fontRenderer = mc.fontRendererObj;
        int color = TARASANDE;
        float x = getPosX().getValue(), y = getPosY().getValue(), panelHeight = 50, panelWidth = 100, titleBarHeight = fontRenderer.FONT_HEIGHT;
        RenderUtil.drawRect(x, y, panelWidth, panelHeight, new Color(0, 0, 0, 76).getRGB());
        RenderUtil.drawRect(x, y, panelWidth, titleBarHeight, color);
        fontRenderer.drawStringWithShadow("Watermark", x + 0.5f, y + 0.5f, -1);
        double xScale = (panelWidth - 4) / (fontRenderer.getStringWidthInt(CLIENT_NAME_JAPANASE) + 1.0);
        double yScale = (panelHeight - 4) / (fontRenderer.FONT_HEIGHT + 3.0);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 1, y + titleBarHeight + 1, 0.0);
        GlStateManager.scale((float) xScale, (float) yScale, 1.0F);
        GlStateManager.translate(-(x + 1), -(y + titleBarHeight + 1), 0.0);

        fontRenderer.drawStringWithShadow(CLIENT_NAME_JAPANASE, (float) (x + 1), (float) (y + titleBarHeight + 1), color);
        GlStateManager.popMatrix();
        fontRenderer.drawStringWithShadow(CLIENT_NAME.toLowerCase(), x + 0.5f, y + panelHeight - fontRenderer.FONT_HEIGHT - 0.5f, color);
        if(this.getLocked().getValue())
            leftY.set(y * 2 + panelHeight);
    }

}
