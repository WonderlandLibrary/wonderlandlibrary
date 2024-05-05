package tech.atani.client.feature.theme.impl.element.watermark;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import tech.atani.client.feature.module.impl.hud.WaterMark;
import tech.atani.client.feature.theme.data.ThemeObjectInfo;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ThemeObjectType;
import tech.atani.client.feature.theme.impl.element.DraggableElement;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.utility.render.RenderUtil;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@ThemeObjectInfo(name = "Augustus 2.6", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.WATERMARK)
public class OldAugustusWatermark extends DraggableElement {

    public OldAugustusWatermark() {
        super(0, 0, 0, 0, null, WaterMark.class);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date now = new Date();
        String time = dateFormat.format(now);
        String text = String.format("%s b%s" + ChatFormatting.GRAY + " (%s)", CLIENT_NAME, CLIENT_VERSION, time);
        this.getWidth().setValue((float) (2 + mc.fontRendererObj.getStringWidthInt(text)));
        this.getHeight().setValue((float) (2 + mc.fontRendererObj.FONT_HEIGHT));
        RenderUtil.drawRect(0, 0, 2 + mc.fontRendererObj.getStringWidthInt(text), 2 + mc.fontRendererObj.FONT_HEIGHT, new Color(0, 0, 0, 100).getRGB());
        mc.fontRendererObj.drawStringWithShadow(text, 1, 2, -1);
        if(this.getLocked().getValue())
            leftY.set(2 + mc.fontRendererObj.FONT_HEIGHT);
    }

}
