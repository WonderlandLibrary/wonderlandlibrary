/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.player;

import markgg.events.Event;
import markgg.events.listeners.EventMotion;
import markgg.modules.Module;
import markgg.settings.NumberSetting;
import markgg.utilities.TimerUtil;
import net.minecraft.util.AxisAlignedBB;

public class AntiVoid
extends Module {
    public NumberSetting distance = new NumberSetting("Fall Distance", 5.0, 1.0, 12.0, 1.0);
    public NumberSetting speed = new NumberSetting("Speed", 0.5, 0.1, 2.0, 0.1);
    public TimerUtil timer = new TimerUtil();

    public AntiVoid() {
        super("AntiVoid", 0, Module.Category.PLAYER);
        this.addSettings(this.distance, this.speed);
    }

    @Override
    public void onEnable() {
        this.timer.reset();
    }

    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
    }

    public boolean isBlockUnder() {
        int offset = 0;
        while ((double)offset < this.mc.thePlayer.posY - 10.0) {
            AxisAlignedBB boundingBox = this.mc.thePlayer.getEntityBoundingBox().offset(0.0, -offset, 0.0);
            if (!this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, boundingBox).isEmpty()) {
                return true;
            }
            offset += 2;
        }
        return false;
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventMotion && e.isPre() && !this.isBlockUnder() && (double)this.mc.thePlayer.fallDistance > this.distance.getValue() && this.mc.thePlayer.ticksExisted % 5 == 0 && !this.mc.thePlayer.isDead && !(this.mc.thePlayer.posY > 100.0) && !(this.mc.thePlayer.posY < 50.0)) {
            this.mc.thePlayer.motionY = this.speed.getValue();
        }
    }
}

