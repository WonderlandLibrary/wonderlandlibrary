package info.sigmaclient.sigma.modules.world;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.util.math.MathHelper;
import top.fl0wowp4rty.phantomshield.annotations.Native;

public class Timer extends Module {
    public static NumberValue timer = new NumberValue("Timer", 1.0, 0.1, 10.0, NumberValue.NUMBER_TYPE.FLOAT);
    public static BooleanValue matrix = new BooleanValue("Matrix Bypass", false);
    public static NumberValue multi = new NumberValue("Store Ticks", 40, 10, 100, NumberValue.NUMBER_TYPE.FLOAT);
    public Timer() {
        super("Timer", Category.World, "Changes game speed.");
     registerValue(timer);
     registerValue(matrix);
     registerValue(multi);
    }
    float lTimer = 0;
    public static float violation = 0;
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            if(!matrix.isEnable()){
                return;
            }
            if (violation < multi.getValue().floatValue() / timer.getValue().floatValue()) {
                violation += 1;
                violation = MathHelper.clamp(violation, 0.0f, multi.getValue().floatValue() / timer.getValue().floatValue());
            } else {
                flagDisable();
            }
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onEnable() {
        mc.timer.setTimerSpeed(timer.getValue().floatValue());
        lTimer = mc.timer.getTimerSpeed();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if(lTimer == mc.timer.getTimerSpeed()) {
            mc.timer.setTimerSpeed(1.0f);
        }
        super.onDisable();
    }
}
