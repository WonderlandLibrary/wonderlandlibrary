package wtf.expensive.modules.impl.player;

import wtf.expensive.events.Event;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;

@FunctionAnnotation(name = "NoCommands", type = Type.Player)
public class NoCommands extends Function {
    @Override
    public void onEvent(Event event) {

    }
}
