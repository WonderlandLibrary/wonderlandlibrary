package info.sigmaclient.sigma.modules.movement.flys.impl;

import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.movement.Fly;
import info.sigmaclient.sigma.modules.movement.flys.FlyModule;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class SpartanFly extends FlyModule {
    public SpartanFly(Fly fly) {
        super("Spartan", "Fly for Spartan", fly);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) return;
//        mc.player.getPositionVec().y = parent.cacheY;
        if (mc.player.onGround) {
            mc.player.jump();
        } else {
            if (mc.player.getBoundingBox().minY < parent.cacheY) {
                event.y = parent.cacheY;
                event.onGround = true;
                mc.player.setPosition(event.x, event.y, event.z);
                mc.player.jump();
            }
        }
        super.onUpdateEvent(event);
    }
}
