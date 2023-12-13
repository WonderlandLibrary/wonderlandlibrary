package koks.manager.module.impl.player;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventBlockReach;
import koks.manager.event.impl.EventMouseOver;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

/**
 * @author kroko
 * @created on 02.12.2020 : 12:17
 */

@ModuleInfo(name = "ItemTP", description = "You can teleport items to you", category = Module.Category.PLAYER)
public class ItemTP extends Module {

    public Setting reach = new Setting("Reach", 50, 500, 250, true, this);

    @Override
    public void onEvent(Event event) {
        if(!this.isToggled())
            return;

        if(event instanceof EventBlockReach) {
            ((EventBlockReach) event).setReach(reach.getCurrentValue());
        }

        if(event instanceof EventMouseOver) {
            ((EventMouseOver) event).setDistanceCheck(false);
            ((EventMouseOver) event).setReach(reach.getCurrentValue());
        }

        if(event instanceof EventUpdate) {
            if(getGameSettings().keyBindAttack.pressed) {

                MovingObjectPosition ray = rayCastUtil.rayTrace(getPlayer(), getPlayer().rotationYaw, getPlayer().rotationPitch, reach.getCurrentValue());

                BlockPos position = getPosition();
                BlockPos location = ray.getBlockPos().add(0, 1,0);

                setPosition(location.getX(), location.getY(), location.getZ(), true);

                getGameSettings().keyBindAttack.pressed = false;
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
