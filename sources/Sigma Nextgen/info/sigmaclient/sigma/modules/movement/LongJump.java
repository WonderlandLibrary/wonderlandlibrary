package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.render.ESP;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class LongJump extends Module {
    public ModeValue type = new ModeValue("Type", "Hycraft", new String[]{"Hycraft", "Vanilla", "NCP"});
    public NumberValue motion = new NumberValue("Motion", 1, 0, 10, NumberValue.NUMBER_TYPE.FLOAT){
        @Override
        public boolean isHidden() {
            return !type.is("Vanilla") && !type.is("NCP");
        }
    };
    public BooleanValue autoDisable = new BooleanValue("Auto Disable", true);
//    public BooleanValue autoJump = new BooleanValue("Auto Jump", true);
    int airTicks = 0;
    boolean jumped = false;
    public LongJump() {
        super("LongJump", Category.Movement, "Jump but infinite blocks");
     registerValue(type);
     registerValue(motion);
     registerValue(autoDisable);
    }

    @Override
    public void onEnable() {
        jumped= false;
        airTicks = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            if(autoDisable.isEnable()){
                if(mc.player.onGround && jumped){
                    silentDisable();
                    return;
                }
            }
            // calc
            if(mc.player.onGround){
                airTicks = 0;
            }else airTicks ++;

            switch (type.getValue()){
                case "Vanilla":
                    if(mc.player.onGround) {
                        mc.player.jump();
                        MovementUtils.strafing(motion.getValue().doubleValue());
                        jumped = true;
                    }
                    break;
                case "NCP":
                    if(mc.player.onGround) {
                        mc.player.jump();
                        jumped = true;
                    }else{
                        if(mc.player.collidedVertically){
                            MovementUtils.strafing(motion.getValue().doubleValue());
                        }
                    }
                    break;
                case "Hycraft":
                    if(mc.player.onGround) {
                        mc.player.jump();
                        jumped = true;
                    }else{
                        if(airTicks == 1){
//                            mc.player.getMotion().y += 0.1;
                        }else{
                            mc.player.getMotion().y *= 0.8;
                        }
                    }
                    break;
            }
        }
        super.onUpdateEvent(event);
    }
}
