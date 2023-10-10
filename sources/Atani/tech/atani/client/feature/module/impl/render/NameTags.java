package tech.atani.client.feature.module.impl.render;

import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.StringBoxValue;

// Hooked in Render.java & RendererLivingEntity.java
@ModuleData(name = "NameTags", description = "Draws better nametags than the original ones", category = Category.RENDER)
public class NameTags extends Module {

    public final StringBoxValue nameTagMode = new StringBoxValue("Mode", "Which mode will the module use?", this, new String[] {"Vanilla"});

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}