package wtf.expensive.modules.impl.render;

import net.minecraft.network.play.server.SUpdateTimePacket;
import net.minecraft.util.math.MathHelper;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.*;
import wtf.expensive.util.animations.Animation;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.render.animation.AnimationMath;

/**
 * @author dedinside
 * @since 26.06.2023
 */
@FunctionAnnotation(name = "Custom World", type = Type.Render)
public class CustomWorld extends Function {

    public MultiBoxSetting modes = new MultiBoxSetting("Изменять",
            new BooleanOption("Время", false),
            new BooleanOption("Туман", false));

    private ModeSetting timeOfDay = new ModeSetting("Время суток", "Ночь", "День", "Закат", "Рассвет", "Ночь", "Полночь", "Полдень").setVisible(() -> modes.get(0));

    public ColorSetting colorFog = new ColorSetting("Цвет тумана", -1).setVisible(() -> modes.get(1));
    public SliderSetting distanceFog = new SliderSetting("Дальность тумана", 4.0F, 1.1f, 30.0F, 0.01f).setVisible(() -> modes.get(1));

    public CustomWorld() {
        addSettings(modes, timeOfDay, colorFog, distanceFog);
    }

    float time;

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket eventPacket && ((EventPacket) event).isReceivePacket()) {
            if (eventPacket.getPacket() instanceof SUpdateTimePacket) {
                if (modes.get(0)) {
                    eventPacket.setCancel(true);
                }
            }
        }
        if (event instanceof EventUpdate) {
            if (modes.get(0)) {
                float time = 0;
                switch (timeOfDay.get()) {
                    case "День" -> time = 1000;
                    case "Закат" -> time = 12000;
                    case "Рассвет" -> time = 23000;
                    case "Ночь" -> time = 13000;
                    case "Полночь" -> time = 18000;
                    case "Полдень" -> time = 6000;
                }
                mc.world.setDayTime((long) time);
            }
        }
    }
}
