package wtf.expensive.modules.impl.movement;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventInput;
import wtf.expensive.events.impl.player.EventMotion;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BindSetting;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.util.movement.MoveUtil;

/**
 * @author dedinside
 * @since 03.06.2023
 */
@FunctionAnnotation(name = "Sprint", type = Type.Movement)
public class SprintFunction extends Function {

    public BooleanOption keepSprint = new BooleanOption("Keep Sprint", true);

    public SprintFunction() {
        addSettings(keepSprint);
    }

    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventUpdate) {
            // Если игрок не присев и не столкнулся с препятствием по горизонтали
            if (!mc.player.isSneaking() && !mc.player.collidedHorizontally)
                // Устанавливаем режим спринта, если игрок движется
                mc.player.setSprinting(MoveUtil.isMoving());
        }

    }
}
