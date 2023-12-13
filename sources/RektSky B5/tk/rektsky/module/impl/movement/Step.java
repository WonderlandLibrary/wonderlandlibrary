/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.movement;

import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.Client;
import tk.rektsky.event.impl.MotionUpdateEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.DoubleSetting;

public class Step
extends Module {
    DoubleSetting stepHeight = new DoubleSetting("Step height", 0.5, 10.0, 1.0);

    public Step() {
        super("Step", "Makes you step up blocks", Category.MOVEMENT, false);
    }

    @Subscribe
    public void onMotionUpdate(MotionUpdateEvent event) {
        if (this.mc.thePlayer.isCollidedHorizontally) {
            event.setOnGround(true);
            Client.mc.thePlayer.motionY = 0.4;
            event.setPosY(event.getPosY() - event.getPosY() % 0.015625);
        }
    }

    @Override
    public void onDisable() {
        Client.mc.thePlayer.stepHeight = 0.5f;
    }
}

