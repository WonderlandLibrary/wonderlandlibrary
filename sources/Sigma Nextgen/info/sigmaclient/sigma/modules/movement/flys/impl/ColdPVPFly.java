package info.sigmaclient.sigma.modules.movement.flys.impl;

import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.modules.movement.Fly;
import info.sigmaclient.sigma.modules.movement.flys.FlyModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.network.play.client.CPlayerPacket;
import top.fl0wowp4rty.phantomshield.annotations.Native;

@Native
public class ColdPVPFly extends FlyModule {
    public ColdPVPFly(Fly fly) {
        super("ColdPVP", "Fly for ColdPVP", fly);
    }
    boolean damage = false;
    int flyTicks = 0;

    @Override
    public void onEnable() {
//        mc.getConnection().sendPacket(new CPlayerPacket.PositionPacket(mc.player.getPosX(), mc.player.getPosY() + 4, mc.player.getPosZ(), false));
//        mc.getConnection().sendPacket(new CPlayerPacket.PositionPacket(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ(), false));
//        mc.getConnection().sendPacket(new CPlayerPacket.PositionPacket(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ(), true));
        flyTicks = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        if(mc.player.hurtTime == 9) {
            flyTicks = 14;
        }
        if(flyTicks > 0){
            flyTicks --;
            mc.timer.setTimerSpeed(parent.codePVPTimer.getValue().floatValue());
            MovementUtils.strafing(event, parent.codePVPSpeed.getValue().floatValue());
            event.setY(mc.player.getMotion().y = mc.player.getMotion().y > 0 ? mc.player.getMotion().y + 0.1 : mc.player.getMotion().y * 0.8);
        }
        super.onMoveEvent(event);
    }
}
