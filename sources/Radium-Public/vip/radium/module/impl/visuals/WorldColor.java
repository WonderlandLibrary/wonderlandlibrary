package vip.radium.module.impl.visuals;

import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.Property;

@ModuleInfo(label = "World Color", category = ModuleCategory.VISUALS)
public final class WorldColor extends Module {

    public final Property<Integer> lightMapColorProperty = new Property<>("Light Map", 0xFFFF8080);

}
