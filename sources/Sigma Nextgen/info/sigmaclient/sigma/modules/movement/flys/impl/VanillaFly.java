package info.sigmaclient.sigma.modules.movement.flys.impl;

import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.modules.movement.Fly;
import info.sigmaclient.sigma.modules.movement.flys.FlyModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class VanillaFly extends FlyModule {
    public VanillaFly(Fly fly) {
        super("Vanilla", "Fly for Vanilla", fly);
    }

    @Override
    public void onDisable() {
        mc.player.getMotion().y = 0;
        mc.player.getMotion().x = 0;
        mc.player.getMotion().z = 0;
        super.onDisable();
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        MovementUtils.strafing(event, parent.vanillaSpeed.getValue().floatValue());
        event.setY(mc.player.getMotion().y = mc.gameSettings.keyBindJump.pressed ? parent.vanillaUpSpeed.getValue().doubleValue() : (mc.gameSettings.keyBindSneak.pressed ? -parent.vanillaUpSpeed.getValue().doubleValue() : 0));
        super.onMoveEvent(event);
    }
}
