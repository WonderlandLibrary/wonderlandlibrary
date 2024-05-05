package tech.atani.client.feature.module.impl.render;

import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;

//Hooked in ItemRenderer class && EntityLivingBase class
@ModuleData(name = "HitAnimations", description = "Changes your item swinging animation", category = Category.RENDER)
public class HitAnimations extends Module {

    public final SliderValue<Float> swingSpeed = new SliderValue<>("Swing Speed", "How fast should the item swing?", this, 1.2f, 0.1f, 3.5f, 1);
    public final CheckBoxValue smoothSwing = new CheckBoxValue("Smooth Swing", "Should the item swing smoothly?", this, false);

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}