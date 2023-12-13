/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.movement;

import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class Sprint
extends Module {
    public Sprint() {
        super("Sprint", "Toggle sprint but for hacked clients", 0, Category.MOVEMENT);
    }

    @Subscribe
    public void onTick(WorldTickEvent event) {
        if (this.mc.thePlayer.moveForward > 0.0f && !this.mc.thePlayer.isSneaking()) {
            this.mc.thePlayer.setSprinting(true);
        }
    }
}

