package wtf.expensive.modules.impl.player;

import wtf.expensive.events.Event;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.SliderSetting;

/**
 * @author dedinside
 * @since 25.06.2023
 */

@FunctionAnnotation(name = "ItemScroller", type = Type.Player)
public class ItemScroller extends Function {

    public SliderSetting delay = new SliderSetting("Задержка", 80, 0, 1000, 1);


    public ItemScroller() {
        addSettings(delay);
    }

    @Override
    public void onEvent(Event event) {

    }
}
