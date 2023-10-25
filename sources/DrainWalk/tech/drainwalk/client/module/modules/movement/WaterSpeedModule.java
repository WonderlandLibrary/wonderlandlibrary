package tech.drainwalk.client.module.modules.movement;

import net.minecraft.init.MobEffects;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.events.EventTarget;
import tech.drainwalk.events.Player.EventUpdate;
import tech.drainwalk.utility.BooleanSetting;
import tech.drainwalk.utility.Helper;
import tech.drainwalk.utility.NumberSetting;
import tech.drainwalk.utility.movement.MoveUtility;

public class WaterSpeedModule extends Module {
    private final BooleanSetting speedCheck = null;
    private final NumberSetting speed = null;
    public WaterSpeedModule(){
        super("WaterSpeedModule", Category.MOVEMENT);
    }
    @EventTarget
    public void onUpdate(EventUpdate event){
     if(!Helper.mc.player.isPotionActive(MobEffects.SPEED) && speedCheck.getCurrentValue()) {
         return;
     }
     if (Helper.mc.player.isInWater() && Helper.mc.player.isInLava()){
            MoveUtility.setSpeed(speed.getCurrentValue());
        }
    }
}
