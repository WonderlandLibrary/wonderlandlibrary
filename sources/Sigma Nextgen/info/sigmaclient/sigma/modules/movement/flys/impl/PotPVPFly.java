package info.sigmaclient.sigma.modules.movement.flys.impl;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.modules.movement.Fly;
import info.sigmaclient.sigma.modules.movement.flys.FlyModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.network.play.client.CConfirmTransactionPacket;
import net.minecraft.network.play.server.SConfirmTransactionPacket;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class PotPVPFly extends FlyModule {
    public PotPVPFly(Fly fly) {
        super("PotPVP", "Fly for PotPVP", fly);
    }

    @Override
    public void onDisable() {
        mc.player.getMotion().y = 0;
        mc.player.getMotion().x = 0;
        mc.player.getMotion().z = 0;
        super.onDisable();
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        if(event.packet instanceof CConfirmTransactionPacket){
            event.cancelable = true;
        }
        if(event.packet instanceof SConfirmTransactionPacket){
            event.cancelable = true;
        }
        super.onPacketEvent(event);
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        MovementUtils.strafing(event, 1);
        event.setY(mc.player.getMotion().y = mc.gameSettings.keyBindJump.pressed ? 0.5 : (mc.gameSettings.keyBindSneak.pressed ? -0.5 : 0));
        super.onMoveEvent(event);
    }
}
