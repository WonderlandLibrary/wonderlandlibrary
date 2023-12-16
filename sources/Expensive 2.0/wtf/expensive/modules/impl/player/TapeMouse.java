package wtf.expensive.modules.impl.player;

import net.minecraft.util.math.RayTraceResult;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;

@FunctionAnnotation(name = "TapeMouse", type = Type.Player)
public class  TapeMouse extends Function {


    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate e) {
            if (mc.player.getCooledAttackStrength(1f) >= 1) {
                mc.clickMouse();
            }
        }
    }
}
