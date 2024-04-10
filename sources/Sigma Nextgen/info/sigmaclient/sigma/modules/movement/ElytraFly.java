package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.network.play.client.CEntityActionPacket;

import static net.minecraft.network.play.client.CEntityActionPacket.Action.START_FALL_FLYING;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class ElytraFly extends Module {
    public NumberValue vanillaSpeed = new NumberValue("Speed", 7, 0, 9, NumberValue.NUMBER_TYPE.FLOAT);
    public NumberValue fallMotion = new NumberValue("FallMotion", 0, 0, 0.4, NumberValue.NUMBER_TYPE.FLOAT);
    public BooleanValue ncp = new BooleanValue("NCP", false);
    public BooleanValue stop = new BooleanValue("Stop", true);
    public ElytraFly() {
        super("ElytraFly", Category.Movement, "Better elytra flying");
     registerValue(vanillaSpeed);
     registerValue(fallMotion);
     registerValue(ncp);
     registerValue(stop);
    }
    boolean jump = false;
    @Override
    public void onEnable() {
        jump = false;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if(stop.isEnable())
            mc.player.setFlag(7, false);
        super.onDisable();
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            if(mc.player.onGround && !jump && mc.player.fallDistance == 0){
                mc.player.jump();
                jump = true;
            }
            if(mc.player.fallDistance > 0.08){
                if(!mc.player.isElytraFlying()) {
                    mc.getConnection().sendPacket(new CEntityActionPacket(mc.player, START_FALL_FLYING));
                    mc.player.setFlag(7, true);
                }
            }else{
                mc.player.setFlag(7, false);
            }
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        if(mc.player.isElytraFlying()){
            MovementUtils.strafing(event, vanillaSpeed.getValue().floatValue());
            if(ncp.isEnable() && event.getY() > 0){
                event.setY(0);
            }else{
                event.setY(-fallMotion.getValue().floatValue());
            }
        }
        super.onMoveEvent(event);
    }
}
