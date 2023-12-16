package wtf.expensive.modules.impl.util;

import wtf.expensive.events.Event;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;

/**
 * @author dedinside
 * @since 12.06.2023
 */
@FunctionAnnotation(name = "Optimization", type = Type.Util)
public class Optimization extends Function {

    public final MultiBoxSetting optimizeSelection = new MultiBoxSetting("Оптимизировать", new BooleanOption("Освещение",true), new BooleanOption("Партиклы",true), new BooleanOption("Подсветка клиента.", false));

     public Optimization() {
         addSettings(optimizeSelection);
     }

    @Override
    public void onEvent(Event event) {}
}
