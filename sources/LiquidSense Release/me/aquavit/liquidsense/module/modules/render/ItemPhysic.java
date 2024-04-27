package me.aquavit.liquidsense.module.modules.render;

import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;

@ModuleInfo(name = "ItemPhysic", description = "Item Physic", category = ModuleCategory.RENDER)
public class ItemPhysic extends Module {
    public static BoolValue rotateX = new BoolValue("Rotate X", true);
    public static BoolValue rotateY = new BoolValue("Rotate Y", false);
    public static BoolValue rotateZ = new BoolValue("Rotate Z", true);
    public static BoolValue nohover = new BoolValue("No Hover", true);
}
