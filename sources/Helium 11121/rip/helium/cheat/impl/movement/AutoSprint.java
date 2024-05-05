package rip.helium.cheat.impl.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerUpdateEvent;

/**
 * @author antja03
 */
public class AutoSprint extends Cheat {

    private boolean shouldSkipNextUpdate;

    public AutoSprint() {
        super("Sprint", "Automatically sprints while still following the games sprinting mechanics (i.e food level)", CheatCategory.MOVEMENT);
        shouldSkipNextUpdate = false;
    }


    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent playerUpdateEvent) {
        if (!shouldSkipNextUpdate) {
            getPlayer().setSprinting(
                    !getPlayer().isCollidedHorizontally
                            && !getPlayer().isSneaking()
                            && getPlayer().getFoodStats().getFoodLevel() > 5
                            && getGameSettings().keyBindForward.pressed);
        } else {
            shouldSkipNextUpdate = false;
        }
    }

}
