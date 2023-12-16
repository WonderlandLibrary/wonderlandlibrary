package wtf.expensive.modules.impl.util;

import wtf.expensive.events.Event;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.util.ClientUtil;

@FunctionAnnotation(name = "DiscordRPC", type = Type.Util)
public class DiscordRPC extends Function {

    @Override
    protected void onDisable() {
        super.onDisable();

    }

    @Override
    public void onEvent(Event event) {

    }
}
