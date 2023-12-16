package wtf.expensive.modules.impl.util;

import wtf.expensive.events.Event;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;

/**
 * @author dedinside
 * @since 12.07.2023
 */
@FunctionAnnotation(name = "NoCommands", type = Type.Util)
public class NoCommands extends Function {
    @Override
    public void onEvent(Event event) {

    }
}
