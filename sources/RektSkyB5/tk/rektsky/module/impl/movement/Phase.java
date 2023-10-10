/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.movement;

import net.minecraft.util.AxisAlignedBB;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class Phase
extends Module {
    AxisAlignedBB bb;

    public Phase() {
        super("VClip", "Go down", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 4.0, this.mc.thePlayer.posZ);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof WorldTickEvent) {
            this.setToggled(false);
        }
    }

    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0;
    }
}

