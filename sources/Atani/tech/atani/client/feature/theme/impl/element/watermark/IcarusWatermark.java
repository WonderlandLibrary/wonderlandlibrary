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

@ThemeObjectInfo(name = "Icarus", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.WATERMARK)
public class IcarusWatermark extends DraggableElement {

    public IcarusWatermark() {
        super(8, 0, 0, 0, null, WaterMark.class);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY) {
        FontRenderer fontRenderer = FontStorage.getInstance().findFont("Pangram Bold", 80);
        this.getHeight().setValue((float) (fontRenderer.FONT_HEIGHT + 8));
        this.getWidth().setValue(fontRenderer.getStringWidth(CLIENT_NAME));
        fontRenderer.drawStringWithShadow(CLIENT_NAME, this.getPosX().getValue(), this.getPosY().getValue(), -1);
        if(this.getLocked().getValue())
            leftY.set(fontRenderer.FONT_HEIGHT + 8);
    }

}
