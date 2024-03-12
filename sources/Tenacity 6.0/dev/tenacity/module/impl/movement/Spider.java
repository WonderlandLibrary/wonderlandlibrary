// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.movement;

import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class Spider extends Module
{
    private final ModeSetting mode;
    
    public Spider() {
        super("Spider", "Spider", Category.MOVEMENT, "Climbs you up walls like a spider");
        this.mode = new ModeSetting("Mode", "Vanilla", new String[] { "Vanilla", "Verus" });
        this.addSettings(this.mode);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        this.setSuffix(this.mode.getMode());
        if (Spider.mc.thePlayer.isCollidedHorizontally) {
            if (!Spider.mc.thePlayer.onGround && Spider.mc.thePlayer.isCollidedVertically) {
                return;
            }
            final String mode = this.mode.getMode();
            switch (mode) {
                case "Vanilla": {
                    Spider.mc.thePlayer.jump();
                    break;
                }
                case "Verus": {
                    if (Spider.mc.thePlayer.ticksExisted % 3 == 0) {
                        Spider.mc.thePlayer.motionY = 0.41999998688697815;
                        break;
                    }
                    break;
                }
            }
        }
    }
}
