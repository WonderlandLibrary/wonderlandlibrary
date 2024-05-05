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
import tech.atani.client.utility.render.shader.render.ingame.RenderableShaders;
import tech.atani.client.utility.render.shader.shaders.GradientShader;

import java.awt.*;

@ThemeObjectInfo(name = "Atani Golden", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.WATERMARK)
public class GoldenWatermark extends DraggableElement {

    public GoldenWatermark() {
        super(7, 5, 0, 0, null, WaterMark.class);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY) {
        RenderableShaders.renderAndRun(() -> {
            String text = CLIENT_NAME + " v" + CLIENT_VERSION + " | " + mc.getDebugFPS() + " fps";
            FontRenderer roboto17 = FontStorage.getInstance().findFont("Roboto", 17);
            float length = roboto17.getStringWidthInt(text);
            this.getWidth().setValue(length + 5);
            this.getHeight().setValue((float) (FontStorage.getInstance().findFont("Roboto", 17).FONT_HEIGHT + 6));
            float x = this.getPosX().getValue(), y = this.getPosY().getValue(), lineHeight = 2;
            GradientShader.drawGradientLR(x, y, length + 5, lineHeight, 1, new Color(GOLDEN_FIRST), new Color(GOLDEN_SECOND));
            RenderUtil.drawRect(x - 2, y, 2, roboto17.FONT_HEIGHT + 4, new Color(255, 202, 3).getRGB());
            RenderUtil.drawRect(x, y + lineHeight, length + 5, roboto17.FONT_HEIGHT + 4 - lineHeight, BACK_GRAY_20);
            roboto17.drawStringWithShadow(text, x + 2.5f, y + lineHeight + 2, -1);
            if(this.getLocked().getValue())
                leftY.set(y + lineHeight + roboto17.FONT_HEIGHT + 4 + y);
        });
    }
    
}
