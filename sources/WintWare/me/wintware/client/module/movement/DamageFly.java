package me.wintware.client.module.movement;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class DamageFly
extends Module {
    public DamageFly() {
        super("DamageFly", Category.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    }
}

