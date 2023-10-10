package tech.atani.client.feature.theme.impl.element.watermark;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.module.impl.hud.WaterMark;
import tech.atani.client.feature.theme.data.ThemeObjectInfo;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ThemeObjectType;
import tech.atani.client.feature.theme.impl.element.DraggableElement;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.utility.render.RenderUtil;

import java.awt.*;

@ThemeObjectInfo(name = "Xave", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.WATERMARK)
public class XaveWatermark extends DraggableElement {

    public XaveWatermark() {
        super(-1337, -1337, 0, 0, null, WaterMark.class);
    }

    @Override
    public void onDraw(ScaledResolution sr, float partialTicks, AtomicFloat leftY, AtomicFloat rightY) {
        FontRenderer fontRenderer = FontStorage.getInstance().findFont("ESP", 80);
        String text = CLIENT_NAME.toUpperCase() + "+";
        if(this.getPosX().getValue() == -1337 || this.getPosY().getValue() == -1337) {
            getPosX().setValue((float) (sr.getScaledWidth() - fontRenderer.getStringWidthInt(text)));
            getPosY().setValue((float) 0);
        }
        this.getHeight().setValue((float) (fontRenderer.FONT_HEIGHT - 4));
        this.getWidth().setValue((float) fontRenderer.getStringWidthInt(text));
        setStartX((float) (sr.getScaledWidth() - fontRenderer.getStringWidthInt(text)));
        setStartY((float) 0);
        RenderUtil.drawRect(getPosX().getValue(), getPosY().getValue(), getWidth().getValue(), getHeight().getValue(), new Color(0, 0, 0, 180).getRGB());
        fontRenderer.drawStringWithShadow(text, getPosX().getValue() + 3, getPosY().getValue() + 4, -1);
        if(this.getLocked().getValue())
            rightY.set(fontRenderer.FONT_HEIGHT - 8);
    }
    
}
