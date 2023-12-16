package wtf.expensive.modules.impl.render;

import wtf.expensive.events.Event;
import wtf.expensive.events.impl.render.EventRender;
import wtf.expensive.modules.Function;

public class RegionRender extends Function {
    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender e) {
            if (e.isRender3D()) {

            }
        }
    }
}
