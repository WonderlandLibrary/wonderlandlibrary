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
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.utility.render.color.ColorUtil;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@ThemeObjectInfo(name = "OHareWare", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.WATERMARK)
public class OHareWareWatermark extends DraggableElement {

    private SliderValue<Integer> red = new SliderValue<>("Red", "What'll be the red of the color?", this, 255, 0, 255, 0);
    private SliderValue<Integer> green = new SliderValue<>("Green", "What'll be the green of the color?", this, 255, 0, 255, 0);
    private SliderValue<Integer> blue = new SliderValue<>("Blue", "What'll be the blue of the color?", this, 255, 0, 255, 0);

    public OHareWareWatermark() {
        super(2, 2, 0, 0, null, WaterMark.class);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY) {
        FontRenderer fontRenderer = FontStorage.getInstance().findFont("Tahoma", 19);
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        String date = dateFormat.format(new Date());
        Color firstColor = new Color(red.getValue(), green.getValue(), blue.getValue());
        this.getWidth().setValue(Math.max(fontRenderer.getStringWidth("AtaniWare"), fontRenderer.getStringWidth(String.format("(%s)", date))));
        this.getHeight().setValue((float) (2 + fontRenderer.FONT_HEIGHT * 2 + 2));
        fontRenderer.drawStringWithShadow("AtaniWare", getPosX().getValue(), getPosY().getValue(), ColorUtil.fadeBetween(firstColor.getRGB(), ColorUtil.darken(firstColor.getRGB(), 0.49F), 250L));
        fontRenderer.drawStringWithShadow(String.format(ChatFormatting.GRAY.toString() + "(%s)", date), getPosX().getValue(), getPosY().getValue() + fontRenderer.FONT_HEIGHT + 2, -1);
        if(this.getLocked().getValue())
            leftY.set(this.getHeight().getValue() + 2);
    }

}
