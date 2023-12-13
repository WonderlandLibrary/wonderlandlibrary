package me.nyan.flush.module.impl.movement;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventSafewalk;
import me.nyan.flush.module.Module;

public class Safewalk extends Module {
    public Safewalk() {
        super("Safewalk", Category.MOVEMENT);
    }

    @SubscribeEvent
    public void onSafewalk(EventSafewalk e) {
        e.cancel();
    }
}
