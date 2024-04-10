package info.sigmaclient.sigma.modules.movement.flys.impl;

import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.movement.Fly;
import info.sigmaclient.sigma.modules.movement.flys.FlyModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;

import java.util.Random;
import top.fl0wowp4rty.phantomshield.annotations.Native;

@Native
public class CubecraftFly extends FlyModule {
    public CubecraftFly(Fly fly) {
        super("Cubecraft", "Fly for Cubecraft", fly);
    }
    int flyTicks = 0;
    boolean sendC03 = false;
    boolean phase = false;
    @Override
    public void onEnable() {
        flyTicks = 0;
        sendC03 = false;
        phase = false;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) return;
        double[] motions = new double[]{0.2, 0.31, 0.18, 0.03};
        if(mc.player.ticksExisted % 2 == 0){
            if(mc.gameSettings.keyBindJump.pressed){
                mc.gameSettings.keyBindJump.pressed = false;
                mc.player.setPosition(event.x, event.y + 0.5, event.z);
            }
            if(mc.gameSettings.keyBindSneak.pressed){
                mc.gameSettings.keyBindSneak.pressed = false;
                mc.player.setPosition(event.x, event.y - 0.5, event.z);
            }
        }
        if(flyTicks < 4) {
            event.y += motions[flyTicks % 4];
            flyTicks++;
        }else{
            flyTicks = 0;
            event.onGround = true;
            MovementUtils.strafing(MovementUtils.getBaseMoveSpeed());
        }
        mc.player.getPositionVec().y = event.y;
        super.onUpdateEvent(event);
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
//        if(flyTicks % 4 == 0){
        event.setY(mc.player.getMotion().y = 0);
//        }
        super.onMoveEvent(event);
    }
}
