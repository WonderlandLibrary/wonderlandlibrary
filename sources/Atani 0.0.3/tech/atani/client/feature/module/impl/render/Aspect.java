package tech.atani.client.feature.module.impl.render;

import tech.atani.client.listener.event.minecraft.render.PerspectiveEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;

@ModuleData(name = "Aspect", description = "Simulate chaning your aspect ratio", category = Category.RENDER)
public class Aspect extends Module {

    private final SliderValue<Float> aspect = new SliderValue<>("Aspect", "What'll the aspect be?", this, 1.0f, 0.1f, 5.0f, 1);
    private final CheckBoxValue hands = new CheckBoxValue("Hands", "Affect Hands?", this, true);

    @Listen
    public void onPerspective(PerspectiveEvent perspectiveEvent) {
        if(!perspectiveEvent.isHand() || hands.getValue()) {
            perspectiveEvent.setAspect(aspect.getValue());
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
