package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class HighJump extends Module {
    public ModeValue type = new ModeValue("Type", "Vanilla", new String[]{"Vanilla", "Slow", "NCP"});
    public NumberValue motion = new NumberValue("Motion", 1, 0, 3, NumberValue.NUMBER_TYPE.FLOAT){
        @Override
        public boolean isHidden() {
            return !type.is("Vanilla") && !type.is("Slow");
        }
    };
    public BooleanValue autoDisable = new BooleanValue("Auto Disable", false);
    int airTicks = 0;
    boolean jumped = false;
    public HighJump() {
        super("HighJump", Category.Movement, "Jump but infinite blocks");
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
            switch (type.getValue()){
                case "Vanilla":
                    if(mc.player.onGround) {
                        mc.player.getMotion().y = motion.getValue().doubleValue();
                        jumped = true;
                    }
                    break;
                case "Slow":
                    mc.player.getMotion().y = motion.getValue().doubleValue() * 0.5;
                    jumped = true;
                    break;
                case "NCP":
                    if(mc.player.onGround){
                        mc.player.jump();
                        airTicks = 0;
                        jumped = true;
                    }else{
                        airTicks ++;
                        if(airTicks == 2){
                            mc.player.getMotion().y = 0.32;
                        }
                    }
                    break;
            }
        }
        super.onUpdateEvent(event);
    }
}
