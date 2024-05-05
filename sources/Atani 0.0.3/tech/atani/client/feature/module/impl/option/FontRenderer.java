package tech.atani.client.feature.module.impl.option;

import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.StringBoxValue;

@ModuleData(name = "FontRenderer", description = "Customize the font renderer", category = Category.OPTIONS, frozenState = true, enabled = true)
public class FontRenderer extends Module {

    public final StringBoxValue mode = new StringBoxValue("Mode", "What font renderer will the client use?", this, new String[] {"Modern", "Classic"});

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
