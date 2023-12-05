package koks.manager.module.impl.player;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventBlockReach;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovingObjectPosition;

/**
 * @author kroko
 * @created on 30.09.2020 : 21:58
 */

@ModuleInfo(name = "Teleport", description = "You can teleport you", category = Module.Category.PLAYER)
public class Teleport extends Module {

    public Setting reach = new Setting("Reach", 250, 50, 500, false, this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if(event instanceof EventBlockReach) {
            ((EventBlockReach) event).setReach(reach.getCurrentValue());
        }
        if(event instanceof EventUpdate) {
            if(getGameSettings().keyBindAttack.pressed) {
                MovingObjectPosition position = mc.objectMouseOver;
                if(position.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK)) {

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(position.getBlockPos().getX(), position.getBlockPos().getY() + 1, position.getBlockPos().getZ(), true));
                    getPlayer().setPosition(position.getBlockPos().getX(), position.getBlockPos().getY() + 1, position.getBlockPos().getZ());


                    getGameSettings().keyBindAttack.pressed = false;
                }
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
