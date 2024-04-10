package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.entity.Entity;

import static net.minecraft.util.math.MathHelper.cos;
import static net.minecraft.util.math.MathHelper.sin;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class EntitySpeed extends Module {
    NumberValue speed = new NumberValue("Speed", 5, 0, 10, NumberValue.NUMBER_TYPE.FLOAT);
    public EntitySpeed() {
        super("EntitySpeed", Category.Movement, "Add riding speed");
     registerValue(speed);
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if (event.isPre()) {
            if(mc.player.isRidingHorse()) {
                Entity vehicle = mc.player.getRidingEntity();
                float x = -sin((float) MovementUtils.direction()) * speed.getValue().floatValue();
                float z = cos((float) MovementUtils.direction()) * speed.getValue().floatValue();

                vehicle.getMotion().x = x;
                vehicle.getMotion().z = z;
            }
        }
        super.onUpdateEvent(event);
    }
}
