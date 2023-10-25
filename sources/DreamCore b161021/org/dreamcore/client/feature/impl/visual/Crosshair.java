package org.dreamcore.client.feature.impl.visual;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.render.EventRender2D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.player.MovementHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.awt.*;

public class Crosshair extends Feature {

    public static ColorSetting colorGlobal;
    public static ListSetting crosshairMode;
    public BooleanSetting dynamic;
    public NumberSetting width;
    public NumberSetting gap;
    public NumberSetting length;
    public NumberSetting dynamicGap;

    public Crosshair() {
        super("Crosshair", "Изменяет ваш прицел", Type.Visuals);
        crosshairMode = new ListSetting("Crosshair Mode", "Smooth", () -> true, "Smooth", "Border", "Rect");
        dynamic = new BooleanSetting("Dynamic", false, () -> true);
        dynamicGap = new NumberSetting("Dynamic Gap", 3, 1, 20, 1, dynamic::getBoolValue);
        gap = new NumberSetting("Gap", 2, 0F, 10F, 0.1F, () -> true);
        colorGlobal = new ColorSetting("Crosshair Color", new Color(0xFFFFFF).getRGB(), () -> true);
        width = new NumberSetting("Width", 1, 0F, 8, 1F, () -> true);
        length = new NumberSetting("Length", 3, 0.5F, 30F, 1F, () -> true);
        addSettings(crosshairMode, dynamic, dynamicGap, gap, colorGlobal, width, length);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        int crosshairColor = colorGlobal.getColorValue();
        float screenWidth = event.getResolution().getScaledWidth();
        float screenHeight = event.getResolution().getScaledHeight();
        float width = screenWidth / 2;
        float height = screenHeight / 2;
        boolean dyn = dynamic.getBoolValue();
        float dyngap = dynamicGap.getNumberValue();
        float wid = this.width.getNumberValue();
        float len = length.getNumberValue();
        boolean isMoving = dyn && MovementHelper.isMoving();
        float gaps = isMoving ? dyngap : gap.getNumberValue();

        String mode = crosshairMode.getOptions();
        setSuffix(mode);

        if (mode.equalsIgnoreCase("Smooth")) {
            RectHelper.drawSmoothRect(width - gaps - len, height - (wid / 2), width - gaps, height + (wid / 2), crosshairColor);
            RectHelper.drawSmoothRect(width + gaps, height - (wid / 2), width + gaps + len, height + (wid / 2), crosshairColor);
            RectHelper.drawSmoothRect(width - (wid / 2), height - gaps - len, width + (wid / 2), height - gaps, crosshairColor);
            RectHelper.drawSmoothRect(width - (wid / 2), height + gaps, width + (wid / 2), height + gaps + len, crosshairColor);
        } else if (mode.equalsIgnoreCase("Border")) {
            RectHelper.drawBorderedRect(width - gaps - len, height - (wid / 2), width - gaps, height + (wid / 2), 0.5f, Color.WHITE.getRGB(), crosshairColor, true);
            RectHelper.drawBorderedRect(width + gaps, height - (wid / 2), width + gaps + len, height + (wid / 2), 0.5f, Color.WHITE.getRGB(), crosshairColor, true);
            RectHelper.drawBorderedRect(width - (wid / 2F), height - gaps - len, width + (wid / 2), height - gaps, 0.5f, Color.WHITE.getRGB(), crosshairColor, true);
            RectHelper.drawBorderedRect(width - (wid / 2F), height + gaps, width + (wid / 2), height + gaps + len, 0.5f, Color.WHITE.getRGB(), crosshairColor, true);
        } else if (mode.equalsIgnoreCase("Rect")) {
            RectHelper.drawRect(width - gaps - len, height - (wid / 2), width - gaps, height + (wid / 2), crosshairColor);
            RectHelper.drawRect(width + gaps, height - (wid / 2), width + gaps + len, height + (wid / 2), crosshairColor);
            RectHelper.drawRect(width - (wid / 2), height - gaps - len, width + (wid / 2), height - gaps, crosshairColor);
            RectHelper.drawRect(width - (wid / 2), height + gaps, width + (wid / 2), height + gaps + len, crosshairColor);
        }
    }
}
