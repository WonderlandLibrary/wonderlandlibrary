/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.render;

import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.EventRender3D;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.event.impl.WorldTickPostEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.DoubleSetting;

public class AmbianceControl
extends Module {
    public DoubleSetting time = new DoubleSetting("Time", 1.0, 22000.0, 1.0);

    public AmbianceControl() {
        super("AmbianceControl", "Lets you control the weather and time (client side)", Category.RENDER);
    }

    @Subscribe
    public void onRender(EventRender3D event) {
        if (this.mc.theWorld == null) {
            return;
        }
        this.mc.theWorld.setWorldTime((int)this.time.getValue().doubleValue());
        this.mc.theWorld.setTotalWorldTime(this.mc.theWorld.getWorldTime());
    }

    @Subscribe
    public void onWorldTick(WorldTickEvent event) {
        this.mc.theWorld.setWorldTime((int)this.time.getValue().doubleValue());
    }

    @Subscribe
    public void onWorldPostTick(WorldTickPostEvent event) {
        this.mc.theWorld.setWorldTime((int)this.time.getValue().doubleValue());
    }
}

