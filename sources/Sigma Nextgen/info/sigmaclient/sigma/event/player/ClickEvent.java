package info.sigmaclient.sigma.event.player;

import info.sigmaclient.sigma.event.Event;
import info.sigmaclient.sigma.modules.player.OldHitting;
import info.sigmaclient.sigma.sigma5.SelfDestructManager;
import info.sigmaclient.sigma.utils.player.RotationUtils;

import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;

import static info.sigmaclient.sigma.modules.Module.mc;
import static info.sigmaclient.sigma.utils.RandomUtil.nextFloat;

public class ClickEvent extends Event {
    public ClickEvent(){
        this.eventID = 3;
        if(SelfDestructManager.destruct) return;
        if(mc.player == null) return;
        OldHitting.blocking = (mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem && mc.gameSettings.keyBindUseItem.pressed);
        if (RotationUtils.random.nextGaussian() > 0.5) {
            RotationUtils.lastRandomDeltaRotation[0] = RotationUtils.lastRandomDeltaRotation[0] * 0.1f + nextFloat(-1f, 1);
            RotationUtils.lastRandomDeltaRotation[1] = RotationUtils.lastRandomDeltaRotation[1] * 0.1f + nextFloat(-1f, 1);
        }
    }
}
