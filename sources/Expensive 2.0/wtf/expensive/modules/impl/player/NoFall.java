package wtf.expensive.modules.impl.player;

import net.minecraft.network.play.client.CPlayerPacket;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventMotion;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;

import java.util.concurrent.ThreadLocalRandom;

@FunctionAnnotation(name = "NoFall", type = Type.Player)
public class NoFall extends Function {


    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMotion e) {
            if (mc.player.ticksExisted % 3 == 0 && mc.player.fallDistance > 3) {
                e.setY(e.getY() + 0.2f);
            }
        }
    }
}
