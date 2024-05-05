package io.github.raze.modules.collection.visual;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;

public class ViewableRotations extends AbstractModule {

    public ViewableRotations() {
        super("ViewableRotations", "Changes the default minecraft F5 rotations.", ModuleCategory.VISUAL);
    }

    @Listen
    public void onMotionEvent(EventMotion eventMotion) {
        if(eventMotion.getState() == Event.State.PRE) {
            mc.thePlayer.rotationYawHead = eventMotion.getYaw();
            mc.thePlayer.renderYawOffset = eventMotion.getYaw();
        }
    }

}
