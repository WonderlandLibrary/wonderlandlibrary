package tech.drainwalk.client.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.events.Player.EventUpdate;
import tech.drainwalk.utility.Helper;
import tech.drainwalk.utility.movement.MoveUtility;

public class Sprint extends Module {
    public Sprint() {
        super ("SprintModule", Category.MOVEMENT);
    }
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (Helper.mc.player.getFoodStats().getFoodLevel() / 2 > 3) {
            Helper.mc.player.setSprinting(MoveUtility.isMoving());
        }
    }

    @Override
    public void onDisable() {
        Helper.mc.player.setSprinting(false);
        super.onDisable();
    }
}

