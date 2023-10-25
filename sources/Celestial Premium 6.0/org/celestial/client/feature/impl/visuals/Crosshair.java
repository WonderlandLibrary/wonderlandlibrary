/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import baritone.events.events.player.EventUpdate;
import java.awt.Color;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.render.EventRender2D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class Crosshair
extends Feature {
    public static ColorSetting colorGlobal;
    public static ListSetting crosshairMode;
    public BooleanSetting dynamic;
    public BooleanSetting noThirdPerson;
    public BooleanSetting noTop;
    public NumberSetting width;
    public NumberSetting gap;
    public NumberSetting length;
    public NumberSetting dynamicGap;

    public Crosshair() {
        super("Crosshair", "\u0418\u0437\u043c\u0435\u043d\u044f\u0435\u0442 \u0432\u0430\u0448 \u043f\u0440\u0438\u0446\u0435\u043b", Type.Visuals);
        crosshairMode = new ListSetting("Crosshair Mode", "Rect", () -> true, "Rect", "Smooth", "Border");
        this.dynamic = new BooleanSetting("Dynamic", false, () -> true);
        this.dynamicGap = new NumberSetting("Dynamic Gap", 3.0f, 1.0f, 20.0f, 1.0f, this.dynamic::getCurrentValue);
        this.gap = new NumberSetting("Gap", 2.0f, 0.0f, 10.0f, 0.1f, () -> true);
        colorGlobal = new ColorSetting("Crosshair Color", Color.BLACK.getRGB(), () -> true);
        this.width = new NumberSetting("Width", 1.0f, 0.0f, 8.0f, 1.0f, () -> true);
        this.length = new NumberSetting("Length", 3.0f, 0.5f, 30.0f, 1.0f, () -> true);
        this.noTop = new BooleanSetting("No Top", false, () -> true);
        this.noThirdPerson = new BooleanSetting("No Third Person", true, () -> true);
        this.addSettings(crosshairMode, this.dynamic, this.dynamicGap, this.gap, colorGlobal, this.width, this.length, this.noTop, this.noThirdPerson);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = crosshairMode.getOptions();
        this.setSuffix(mode);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        float gaps;
        String mode = crosshairMode.getOptions();
        if (Crosshair.mc.gameSettings.thirdPersonView != 0 && this.noThirdPerson.getCurrentValue()) {
            return;
        }
        int crosshairColor = colorGlobal.getColor();
        float screenWidth = event.getResolution().getScaledWidth();
        float screenHeight = event.getResolution().getScaledHeight();
        float width = screenWidth / 2.0f;
        float height = screenHeight / 2.0f;
        boolean dyn = this.dynamic.getCurrentValue();
        float dyngap = this.dynamicGap.getCurrentValue();
        float wid = this.width.getCurrentValue();
        float len = this.length.getCurrentValue();
        boolean isMoving = dyn && MovementHelper.isMoving();
        float f = gaps = isMoving ? dyngap : this.gap.getCurrentValue();
        if (mode.equalsIgnoreCase("Smooth")) {
            RectHelper.drawSmoothRect(width - gaps - len, height - wid / 2.0f, width - gaps, height + wid / 2.0f, crosshairColor);
            RectHelper.drawSmoothRect(width + gaps, height - wid / 2.0f, width + gaps + len, height + wid / 2.0f, crosshairColor);
            if (!this.noTop.getCurrentValue()) {
                RectHelper.drawSmoothRect(width - wid / 2.0f, height - gaps - len, width + wid / 2.0f, height - gaps, crosshairColor);
            }
            RectHelper.drawSmoothRect(width - wid / 2.0f, height + gaps, width + wid / 2.0f, height + gaps + len, crosshairColor);
        } else if (mode.equalsIgnoreCase("Border")) {
            RectHelper.drawBorderedRect(width - gaps - len, height - wid / 2.0f, width - gaps, height + wid / 2.0f, 0.5, Color.WHITE.getRGB(), crosshairColor, true);
            RectHelper.drawBorderedRect(width + gaps, height - wid / 2.0f, width + gaps + len, height + wid / 2.0f, 0.5, Color.WHITE.getRGB(), crosshairColor, true);
            if (!this.noTop.getCurrentValue()) {
                RectHelper.drawBorderedRect(width - wid / 2.0f, height - gaps - len, width + wid / 2.0f, height - gaps, 0.5, Color.WHITE.getRGB(), crosshairColor, true);
            }
            RectHelper.drawBorderedRect(width - wid / 2.0f, height + gaps, width + wid / 2.0f, height + gaps + len, 0.5, Color.WHITE.getRGB(), crosshairColor, true);
        } else if (mode.equalsIgnoreCase("Rect")) {
            RectHelper.drawRect(width - gaps - len, height - wid / 2.0f, width - gaps, height + wid / 2.0f, crosshairColor);
            RectHelper.drawRect(width + gaps, height - wid / 2.0f, width + gaps + len, height + wid / 2.0f, crosshairColor);
            if (!this.noTop.getCurrentValue()) {
                RectHelper.drawRect(width - wid / 2.0f, height - gaps - len, width + wid / 2.0f, height - gaps, crosshairColor);
            }
            RectHelper.drawRect(width - wid / 2.0f, height + gaps, width + wid / 2.0f, height + gaps + len, crosshairColor);
        }
    }
}

