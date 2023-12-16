package wtf.expensive.modules.impl.util;

import net.minecraft.network.play.client.CCloseWindowPacket;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;

@FunctionAnnotation(name = "XCarry", type = Type.Util)
public class XCarry extends Function {


    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket) {
            if (((EventPacket) event).getPacket() instanceof CCloseWindowPacket) {
                event.setCancel(true);
            }
        }
    }
}
