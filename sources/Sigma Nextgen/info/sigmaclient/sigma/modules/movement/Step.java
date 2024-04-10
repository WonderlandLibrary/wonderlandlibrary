package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.StepEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.stats.Stats;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Step extends Module {
    public ModeValue type = new ModeValue("Type", "Vulcan", new String[]{"Vulcan", "AntiCheat", "Vanilla", "BMC", "Legit"});
    public NumberValue stepHeight = new NumberValue("Step Height", 0.5, 0.5, 3.0, NumberValue.NUMBER_TYPE.FLOAT){
        @Override
        public boolean isHidden() {
            return !(type.is("Vanilla"));
        }
    };
    public NumberValue timer = new NumberValue("Timer", 0.5, 0.1, 1.0, NumberValue.NUMBER_TYPE.FLOAT){
        @Override
        public boolean isHidden() {
            return !(type.is("Vulcan") || type.is("AntiCheat") || type.is("BMC"));
        }
    };
    double preY = 0;
    int keepTicks = 0;
    double y = 0;
    public Step() {
        super("Step", Category.Movement, "Step on 1.5+ block");
     registerValue(type);
     registerValue(stepHeight);
     registerValue(timer);
    }

    @Override
    public void onEnable() {
        mc.player.stepHeight = 0.6f;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.6f;
        super.onDisable();
    }
    private boolean canStep() {
        if (mc.player.collidedVertically && mc.player.onGround && mc.player.getMotion().y < 0.0 && !mc.player.movementInput.jump) {
            return true;
        }
        return false;
    }
    int resetNextTick = 0;
    private void addYPos(double Y, boolean g){
        mc.getConnection().sendPacket(new CPlayerPacket.PositionPacket(mc.player.getPosX(), preY + Y, mc.player.getPosZ(), g));
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.pre){
            if(keepTicks > 0){
                keepTicks --;
                mc.player.getPositionVec().y = y;
            }
        }
        this.suffix = type.getValue();
        if(resetNextTick > 0){
            resetNextTick--;
            if(resetNextTick == 0)
                mc.timer.setTimerSpeed(1f);
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onStepEvent(StepEvent event) {
        if(event.isPre()) {
            preY = mc.player.getPosY();
            if (mc.player.isInWater() || !canStep())
                return;
            mc.player.stepHeight = 1.0f;
            if (type.is("Vanilla")) {
                mc.player.stepHeight = stepHeight.getValue().floatValue();
            }
            if (type.is("Legit")) {
                mc.player.stepHeight = 1f;
            }
        }else{
            switch (type.getValue()) {
                case "Legit":
                    if(mc.player.getBoundingBox().minY - preY != 0.5f) {
                        mc.player.setPosition(mc.player.getPosX(), preY, mc.player.getPosZ());
                        y = preY;
                        keepTicks = 8;
                        mc.player.jump();
                    }
                    return;
            }
            if(mc.player.stepHeight != 1.0f || mc.player.getBoundingBox().minY - preY != 1.0f) {
                mc.player.stepHeight = 0.6f;
                return;
            }
            mc.player.stepHeight = 0.6f;
            switch (type.getValue()){
                case "Vulcan":
                    mc.timer.setTimerSpeed(timer.getValue().floatValue());
                    resetNextTick = 3;
                    addYPos(0.5, true);
                    mc.player.isAirBorne = true;
                    mc.player.addStat(Stats.JUMP);
                    break;
                case "BMC":
                    mc.timer.setTimerSpeed(timer.getValue().floatValue());
                    resetNextTick = 3;
                    mc.player.isAirBorne = true;
                    mc.player.addStat(Stats.JUMP);
                    addYPos(0.41999998688697815, false);
                    addYPos(0.7531999805212024, false);
                    addYPos(1.0, false);
                    break;
                case "AntiCheat":
                    mc.timer.setTimerSpeed(timer.getValue().floatValue());
                    resetNextTick = 3;
                    addYPos(0.41999998688697815, false);
                    addYPos(0.7531999805212024, false);
                    addYPos(1.01, false);
                    break;
            }
        }
        super.onStepEvent(event);
    }
}
