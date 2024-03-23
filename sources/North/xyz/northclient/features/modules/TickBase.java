package xyz.northclient.features.modules;

import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.PostMotionEvent;

@ModuleInfo(name = "TickBase", description = "", category = Category.COMBAT)
public class TickBase extends AbstractModule {
    public boolean stop;
    private int tickCounter = -1;

    @Override
    public void onEnable() {
        super.onEnable();
        tickCounter = -1;
        stop = false;
    }

    @EventLink
    public void onPost(PostMotionEvent event) {
        int tick = getTicks();
        boolean skip = false;

        if (tick == -1) {
            skip = true;
        } else {
            if (tick > 0) {
                stop = true;
            }
        }

        if (stop && !skip) {
            try {
                Thread.sleep(180);
            } catch (Exception ignored) {
            }
        }
    }

    public int getTicks() {
        if (tickCounter-- > 0) {
            return -1;
        } else {
            stop = false;
        }

        if (KillAura.target != null && !KillAura.target.isDead && KillAura.target.getDistanceToEntity(mc.thePlayer) > 3 && KillAura.target.getDistanceToEntity(mc.thePlayer) < 5 && mc.thePlayer.hurtTime <= 2) {
            return tickCounter = 8;
        }
        return 0;
    }
}
