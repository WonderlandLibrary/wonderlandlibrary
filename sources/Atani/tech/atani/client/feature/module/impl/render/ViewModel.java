package tech.atani.client.feature.module.impl.render;

import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.SliderValue;

//Hooked in ItemRenderer class
@ModuleData(name = "ViewModel", description = "Changes your hand item size & position", category = Category.RENDER)
public class ViewModel extends Module {

    public final SliderValue<Float> xPos = new SliderValue<Float>("X Position", "Where should the item go on the X axis?", this, 0.56f, 0.10f, 1f, 2);
    public final SliderValue<Float> yPos = new SliderValue<Float>("Y Position", "Where should the item go on the Y axis?", this, 0.52f, 0.10f, 1f, 2);
    public final SliderValue<Float> scale = new SliderValue<Float>("Item Scale", "What size do you want the item to be?", this, 0.4f, 0.01f, 1f, 2);

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}