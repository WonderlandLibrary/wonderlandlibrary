package tech.drainwalk.client.module.modules.movement;


import com.darkmagician6.eventapi.EventTarget;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.module.category.Type;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.option.options.SelectOptionValue;
import tech.drainwalk.events.UpdateEvent;
import tech.drainwalk.utility.movement.MoveUtility;


public class JesusModule extends Module {

    public final FloatOption boost = new FloatOption("Boost", 1f, 1f,5f)
            .addSettingDescription("Скорость ускорения");

    public final FloatOption motionup = new FloatOption("Up", 0.1f, 0.1f,0.5f)
            .addSettingDescription("Бусть вверх");

    private final SelectOption typeCombo = new SelectOption("Type", 0,
            new SelectOptionValue("MiniJump"),
            new SelectOptionValue("Matrix Boost"));

    public JesusModule() {
        super("Jesus", Category.MOVEMENT);
        addType(Type.SECONDARY);
        register(
                typeCombo,
                motionup,
                boost
        );
    }

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if (typeCombo.getValueByIndex(0)) {
            if (mc.player.isInWater() && mc.player.motionY < 0) {
                mc.player.jump();
                MoveUtility.setSpeed(boost.getValue());
            }
        } else { }
        if (typeCombo.getValueByIndex(1)) {
            if (mc.player.isInWater() && mc.player.motionY < 0) {
                mc.player.motionY = 0.1f;
                MoveUtility.setSpeed(boost.getValue());
            }
        }
        else { }
    }
}