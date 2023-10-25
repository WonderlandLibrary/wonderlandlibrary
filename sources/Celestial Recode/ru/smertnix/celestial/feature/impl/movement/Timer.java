package ru.smertnix.celestial.feature.impl.movement;

import java.awt.*;

import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.draggable.component.impl.DraggableTimer;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.event.events.impl.render.EventRender2D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.math.MathematicHelper;
import ru.smertnix.celestial.utils.movement.MovementUtils;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.RenderUtils;

public class Timer extends Feature {
    public static int ticks = 0;
    public static boolean active;
    private final NumberSetting timerSpeed = new NumberSetting("Timer Amount", 2.0F, 1.1F, 5.0F, 0.1F, () -> true);
    public static BooleanSetting smart = new BooleanSetting("Smart", true, () -> true);

    public Timer() {
        super("Timer", "Ускоряет мир вокруг вас", FeatureCategory.Movement);
        addSettings(timerSpeed, smart);
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion preMotion) {
        if (!smart.getBoolValue()) {
            mc.timer.timerSpeed = timerSpeed.getNumberValue();
        }
        if (smart.getBoolValue()) {
            if (ticks <= 47 && !active) {
                ticks+=1;
                if (!(Celestial.instance.featureManager.getFeature(Speed.class).isEnabled() && Speed.speedMode.getOptions().equalsIgnoreCase("ReallyWorld2"))) {
                	mc.timer.timerSpeed = timerSpeed.getNumberValue();
                }
            }
            if (ticks == 47) {
                active = true;
                toggle();
            }
            if (ticks == 0) {
                active = false;
            }
        }
    }

    @EventTarget
    public void onRender(EventRender2D event2D) {
        if (smart.getBoolValue()) {
        	/*
            DraggableTimer dt = (DraggableTimer) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableTimer.class);
            dt.setWidth(150);
            dt.setHeight(25);
            RenderUtils.drawBlurredShadow(dt.getX() - 52, dt.getY() - 14, 105, 23, 8,new Color(17,17,17,170));
            mc.sfui18.drawCenteredString(MathematicHelper.round(100 - (ticks * 2f),1) + "%", (float) dt.getX(), (float) (dt.getY() - 10), -1);
            RenderUtils.drawRect2(dt.getX() - 50, dt.getY(), (100 - (ticks *  2f)), 5, ClientHelper.getClientColor().getRGB());
        */
        }
    }

    public void onDisable() {
        super.onDisable();
        this.mc.timer.timerSpeed = 1.0f;
    }

}