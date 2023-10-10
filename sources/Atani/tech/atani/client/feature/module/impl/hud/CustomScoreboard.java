package tech.atani.client.feature.module.impl.hud;

import com.google.common.base.Supplier;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.StringBoxValue;

// Hooked in GuiIngame class
@ModuleData(name = "CustomScoreboard", description = "Modifies the scoreboard", category = Category.HUD)
public class CustomScoreboard extends Module {

    public final StringBoxValue background = new StringBoxValue("Background", "Which background should the scoreboard use?", this, new String[] {"Normal", "Off"});
    public final CheckBoxValue customFont = new CheckBoxValue("Custom Font", "Should the scoreboard have a custom font?", this, false);
    public final CheckBoxValue blur = new CheckBoxValue("Blur", "Should the scoreboard have a blur?", this, false, new Supplier[]{() -> background.is("Normal")});
    public final CheckBoxValue bloom = new CheckBoxValue("Bloom", "Should the scoreboard have a bloom?", this, false, new Supplier[]{() -> background.is("Normal")});

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
