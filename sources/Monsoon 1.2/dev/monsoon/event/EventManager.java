package dev.monsoon.event;

import dev.monsoon.Monsoon;
import dev.monsoon.module.base.Module;

public class EventManager {

    public static void call(Event e) {
        if(Monsoon.authorized) {
            for (Module m : Monsoon.manager.modules) {
                if (!m.enabled)
                    continue;
                m.onEvent(e);
            }
            Monsoon.event(e);
        }
    }

}
