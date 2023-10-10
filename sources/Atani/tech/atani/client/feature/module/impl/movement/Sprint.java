package tech.atani.client.feature.module.impl.movement;

import net.minecraft.client.settings.KeyBinding;
import tech.atani.client.listener.event.minecraft.player.movement.DirectionSprintCheckEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.player.movement.MoveUtil;
import tech.atani.client.feature.value.impl.CheckBoxValue;

@ModuleData(name = "Sprint", description = "Makes you sprint automatically.", category = Category.MOVEMENT)
public class Sprint extends Module {
    public CheckBoxValue legit = new CheckBoxValue("Legit", "Sprint legit?", this, false),
            omni = new CheckBoxValue("All Directions", "Sprint in all directions?", this, false);

    @Listen
    public final void onMotion(UpdateMotionEvent updateMotionEvent) {
        if (legit.getValue()) {
            getGameSettings().keyBindSprint.pressed = true;
        } else {
            if (MoveUtil.getSpeed() != 0) {
                getPlayer().setSprinting(true);
            }
        }
    }

    @Listen
    public final void onOmniCheck(DirectionSprintCheckEvent directionSprintCheckEvent) {
        if(omni.getValue()) {
            if(MoveUtil.getSpeed() != 0) {
                directionSprintCheckEvent.setSprintCheck(false);
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() { KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false); }
}
