package tech.atani.client.feature.theme.impl.element.watermark;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.module.impl.hud.WaterMark;
import tech.atani.client.feature.theme.impl.element.DraggableElement;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.theme.data.ThemeObjectInfo;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ThemeObjectType;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.utility.render.color.ColorUtil;
import tech.atani.client.utility.render.shader.render.ingame.RenderableShaders;
import tech.atani.client.utility.render.shader.shaders.RoundedShader;

import java.awt.*;

@ThemeObjectInfo(name = "Atani Modern", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.WATERMARK)
public class ModernWatermark extends DraggableElement {

    private SliderValue<Integer> red = new SliderValue<>("Red", "What'll be the red of the color?", this, 255, 0, 255, 0);
    private SliderValue<Integer> green = new SliderValue<>("Green", "What'll be the green of the color?", this, 255, 0, 255, 0);
    private SliderValue<Integer> blue = new SliderValue<>("Blue", "What'll be the blue of the color?", this, 255, 0, 255, 0);

    public ModernWatermark() {
        super(9, 9, 0, 0, null, WaterMark.class);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY) {
        String text =  CLIENT_NAME + " | " + mc.getSession().getUsername() + " | " + mc.getDebugFPS() + " FPS";
        FontRenderer fontRenderer = FontStorage.getInstance().findFont("Greycliff Medium", 21);
        float outlineWidth = 1;
        Color firstColor = new Color(red.getValue(), green.getValue(), blue.getValue());
        int color = ColorUtil.fadeBetween(firstColor.getRGB(), firstColor.brighter().getRGB(), 150L);
        this.getWidth().setValue(fontRenderer.getStringWidthInt(text) + 8 + outlineWidth * 2);
        this.getHeight().setValue(fontRenderer.FONT_HEIGHT + 4 + outlineWidth * 2);
        RenderableShaders.renderAndRun(() -> {
            RoundedShader.drawRoundOutline(this.getPosX().getValue(), this.getPosY().getValue(), this.getWidth().getValue(), this.getHeight().getValue(), 5, outlineWidth, new Color(20, 20, 20), new Color(color));
        });
        fontRenderer.drawString(text, 5 + getPosX().getValue(), getPosY().getValue() + 2.5f, -1);
        if(this.getLocked().getValue())
            leftY.set(20 + fontRenderer.FONT_HEIGHT + 4 + outlineWidth * 2);
    }


}
