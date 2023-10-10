package tech.atani.client.feature.module.impl.hud;

import com.google.common.base.Supplier;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;

@ModuleData(name = "PostProcessing", description = "Cool little shaders", category = Category.HUD)
public class PostProcessing extends Module {

    public CheckBoxValue bloom = new CheckBoxValue("Bloom", "Render bloom shaders?", this, true);
    public SliderValue<Integer> radius = new SliderValue<>("Radius", "With how much radius will the bloom render?", this, 10, 0, 20, 0, new Supplier[]{() -> bloom.getValue()});
    public CheckBoxValue blur = new CheckBoxValue("Blur", "Render blur shaders?", this, true);

    public PostProcessing() {
        this.setEnabled(true);
    }

    public static PostProcessing getInstance() {
        return ModuleStorage.getInstance().getByClass(PostProcessing.class);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}
