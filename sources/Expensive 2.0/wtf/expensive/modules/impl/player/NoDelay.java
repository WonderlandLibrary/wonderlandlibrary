package wtf.expensive.modules.impl.player;

import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;

@FunctionAnnotation(name = "NoDelay", type = Type.Player)
public class NoDelay extends Function {

    private final MultiBoxSetting actions = new MultiBoxSetting("Действия",
            new BooleanOption("Прыжок", true),
            new BooleanOption("Ставить", false)
    );

    public NoDelay() {
        addSettings(actions);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (actions.get(0)) mc.player.jumpTicks = 0;
            if (actions.get(1)) mc.rightClickDelayTimer = 0;
        }
    }
}
