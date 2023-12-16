package wtf.expensive.modules.impl.render;

import wtf.expensive.events.Event;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.SliderSetting;

@FunctionAnnotation(name = "HitColor", type = Type.Render)
public class HitColor extends Function {

    public SliderSetting intensivity = new SliderSetting("Интенсивность", 0.3f, 0.1f, 1, 0.1f);

    public HitColor() {
        super();
        addSettings(intensivity);
    }

    @Override
    public void onEvent(Event event) {

    }
}
