package me.AquaVit.liquidSense.modules.movement;

import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.item.ItemBlock;

@ModuleInfo(name = "AutoJump", description = ":/", category = ModuleCategory.MOVEMENT)
public class AutoJump extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[] {"OnGround","Motion"}, "Motion");
    private final BoolValue strafe = new BoolValue("Strafe", false);
    @EventTarget
    public void onMotion(MotionEvent event){
        final EventState eventState = event.getEventState();
        if(strafe.get()){
            if (eventState == EventState.PRE) {
                return;
            }
            MovementUtils.strafe();
        }
        if(modeValue.get().equalsIgnoreCase("OnGround")){
            if(mc.thePlayer.isInWater())
                return;
            if(MovementUtils.isMoving()) {
                if(mc.thePlayer.onGround){
                    mc.thePlayer.jump();
                }
            }
        }
        if(modeValue.get().equalsIgnoreCase("Motion")){
            if(mc.thePlayer.isInWater())
                return;
            if(MovementUtils.isMoving()) {
                if(mc.thePlayer.onGround){
                    mc.thePlayer.motionY = 0.42;
                }
            }
        }

    }
    @Override
    public String getTag() {
        return modeValue.get();
    }
}
