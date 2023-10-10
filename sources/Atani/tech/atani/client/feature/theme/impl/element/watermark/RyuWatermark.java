package tech.atani.client.feature.theme.impl.element.watermark;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.module.impl.hud.WaterMark;
import tech.atani.client.feature.theme.data.ThemeObjectInfo;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ThemeObjectType;
import tech.atani.client.feature.theme.impl.element.DraggableElement;
import tech.atani.client.utility.java.StringUtil;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.utility.render.shader.render.ingame.RenderableShaders;

import java.awt.*;

@ThemeObjectInfo(name = "Ryu", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.WATERMARK)
public class RyuWatermark extends DraggableElement {

    public RyuWatermark() {
        super(3, 3, 0, 0, null, WaterMark.class);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY) {
        FontRenderer fontRenderer = FontStorage.getInstance().findFont("Roboto Medium", 17);
        String text = CLIENT_NAME + " " + ChatFormatting.GRAY + " # " + ChatFormatting.WHITE + " " + mc.getDebugFPS() + "fps";
        this.getWidth().setValue(fontRenderer.getStringWidth(text));
        this.getHeight().setValue((float) fontRenderer.FONT_HEIGHT);
        // Rendering bloom on text is really performance fucking due to all the geometry around letter so we're trying to not hurt it as much
        // by disabling the blur shader
        RenderableShaders.renderAndRun(true, false, () -> {
            // Rendering with the regular version of the font to fix a weird bug with the medium font where it looks shitty
            FontStorage.getInstance().findFont("Roboto", 17).drawString(StringUtil.removeFormattingCodes(text), getPosX().getValue(), getPosY().getValue(), new Color(0, 0, 0).getRGB());
        });
        fontRenderer.drawString(text, 3, 3, RYU);
        if(this.getLocked().getValue())
            leftY.set(fontRenderer.FONT_HEIGHT + 6);
    }

}
