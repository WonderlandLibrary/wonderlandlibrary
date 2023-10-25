package fun.expensive.client.feature.impl.movement;

import fun.rich.client.Rich;
import fun.rich.client.draggable.component.impl.DraggableTimer;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.event.events.impl.render.EventRender2D;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.math.MathematicHelper;
import fun.rich.client.utils.movement.MovementUtils;
import fun.rich.client.utils.render.ClientHelper;
import fun.rich.client.utils.render.RenderUtils;

import java.awt.*;

public class Timer extends Feature {
    public int ticks = 0;
    public boolean active;
    private final NumberSetting timerSpeed = new NumberSetting("Timer Amount", 2.0F, 0.1F, 10.0F, 0.1F, () -> true);
    public static BooleanSetting smart = new BooleanSetting("Smart", false, () -> true);

    public Timer() {
        super("Timer", "Увеличивает скорость игры", FeatureCategory.Player);
        addSettings(timerSpeed, smart);
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion preMotion) {
        if (!smart.getBoolValue()) {
            mc.timer.timerSpeed = timerSpeed.getNumberValue();
        }
        if (smart.getBoolValue()) {
            if (ticks <= 50 && !active && MovementUtils.isMoving()) {
                    ticks++;
                mc.timer.timerSpeed = timerSpeed.getNumberValue();
            }
            if (ticks == 50) {
                active = true;
            }
            if (active) {
                mc.timer.timerSpeed = 1;
                if (!MovementUtils.isMoving())
                    ticks--;
            }
            if (ticks == 0) {
                active = false;
            }
        }
    }

    @EventTarget
    public void onRender(EventRender2D event2D) {
        if (smart.getBoolValue()) {
            DraggableTimer dt = (DraggableTimer) Rich.instance.draggableHUD.getDraggableComponentByClass(DraggableTimer.class);
            dt.setWidth(150);
            dt.setHeight(25);
            RenderUtils.drawBlurredShadow(dt.getX() - 52, dt.getY() - 14, 105, 23, 8,new Color(17,17,17,170));
            mc.sfui18.drawCenteredString(MathematicHelper.round(100 - (ticks * 2f),1) + "%", (float) dt.getX(), (float) (dt.getY() - 10), -1);
            RenderUtils.drawRect2(dt.getX() - 50, dt.getY(), (100 - (ticks *  2f)), 5, ClientHelper.getClientColor().getRGB());
        }
    }

    public void onDisable() {
        super.onDisable();
        this.mc.timer.timerSpeed = 1.0f;
    }

}