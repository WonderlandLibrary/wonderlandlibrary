package ru.smertnix.celestial.feature.impl.visual;


import java.awt.*;

import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.render.EventRender2D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.movement.MovementUtils;
import ru.smertnix.celestial.utils.render.RenderUtils;

public class Crosshair extends Feature {

    public static ColorSetting colorGlobal;
    public BooleanSetting dynamic;
    public BooleanSetting gap;
    public NumberSetting length;

    public Crosshair() {
        super("Crosshair", "Кастомный прицел", FeatureCategory.Render);
        dynamic = new BooleanSetting("CoolDown", true, () -> true);
        gap = new BooleanSetting("Dot", true, () -> true);
        colorGlobal = new ColorSetting("Color", new Color(0xFFFFFF).getRGB(), () -> true);
        length = new NumberSetting("Radius", 3, 3, 6, 0.5F, () -> true);
        addSettings(gap, dynamic, colorGlobal, length);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        int crosshairColor = colorGlobal.getColorValue();
        float screenWidth = event.getResolution().getScaledWidth();
        float screenHeight = event.getResolution().getScaledHeight();
        float africanec = screenWidth / 2;
        float africanec2 = screenHeight / 2;
        boolean dyn = dynamic.getBoolValue();
        float wid = 1;
        float len = length.getNumberValue();
        boolean isMoving = dyn && MovementUtils.isMoving();
        float africanec3 = 15 - 12 * mc.player.getCooledAttackStrength(1f);
        if ((mc.gameSettings.thirdPersonView == 1 || mc.gameSettings.thirdPersonView == 2)) {
        	return;
        }
        float H20 = 0.5f;
        int a = Color.BLACK.getRGB();
        if (gap.getBoolValue()) {
        	RenderUtils.drawRect(africanec - 0.5 - H20, africanec2 - 0.5 - H20, africanec + 0.5 + H20, africanec2 + 0.5 + H20, a);
        	RenderUtils.drawRect(africanec - 0.5, africanec2 - 0.5, africanec + 0.5, africanec2 + 0.5, crosshairColor);
        }

        RenderUtils.drawRect(africanec - africanec3 - len - H20, africanec2 - (wid / 2) - H20, africanec - africanec3 + H20, africanec2 + (wid / 2) + H20, a);
        RenderUtils.drawRect(africanec + africanec3 - H20, africanec2 - (wid / 2) - H20, africanec + africanec3 + len + H20, africanec2 + (wid / 2) + H20, a);
        RenderUtils.drawRect(africanec - (wid / 2) - H20, africanec2 - africanec3 - len - H20, africanec + (wid / 2) + H20, africanec2 - africanec3 + H20, a);
        RenderUtils.drawRect(africanec - (wid / 2) - H20, africanec2 + africanec3 - H20, africanec + (wid / 2) + H20, africanec2 + africanec3 + len + H20, a);
        
        RenderUtils.drawRect(africanec - africanec3 - len, africanec2 - (wid / 2), africanec - africanec3, africanec2 + (wid / 2), crosshairColor);
        RenderUtils.drawRect(africanec + africanec3, africanec2 - (wid / 2), africanec + africanec3 + len, africanec2 + (wid / 2), crosshairColor);
        RenderUtils.drawRect(africanec - (wid / 2), africanec2 - africanec3 - len, africanec + (wid / 2), africanec2 - africanec3, crosshairColor);
        RenderUtils.drawRect(africanec - (wid / 2), africanec2 + africanec3, africanec + (wid / 2), africanec2 + africanec3 + len, crosshairColor);

    }
}
