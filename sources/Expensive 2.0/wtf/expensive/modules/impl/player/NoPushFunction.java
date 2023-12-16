package wtf.expensive.modules.impl.player;

import wtf.expensive.events.Event;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;

/**
 * @author dedinside
 * @since 04.06.2023
 */
@FunctionAnnotation(name = "NoPush", type = Type.Player)
public class NoPushFunction extends Function {

    public final MultiBoxSetting modes = new MultiBoxSetting("Тип",
            new BooleanOption("Игроки", true),
            new BooleanOption("Блоки", true),
            new BooleanOption("Вода", true));

    public NoPushFunction() {
        addSettings(modes);
    }

    @Override
    public void onEvent(final Event event) {
    }
}
