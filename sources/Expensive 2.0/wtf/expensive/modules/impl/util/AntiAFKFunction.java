package wtf.expensive.modules.impl.util;

import net.minecraft.client.util.InputMappings;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.glfw.GLFW;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.misc.TimerUtil;
import wtf.expensive.util.movement.MoveUtil;

/**
 * @author dedinside
 * @since 12.06.2023
 */
@FunctionAnnotation(name = "AntiAFK", type = Type.Util)
public class AntiAFKFunction extends Function {

    private final TimerUtil timerUtil = new TimerUtil();

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {

            if (!MoveUtil.isMoving()) {
                if (timerUtil.hasTimeElapsed(15000)) {
                    mc.player.sendChatMessage("/BEFF LOX");
                    timerUtil.reset();
                }
            } else {
                timerUtil.reset();
            }
        }
    }
}
