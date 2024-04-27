package me.aquavit.liquidsense.module.modules.render;

import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.FloatValue;

@ModuleInfo(name = "EveryThingBlock", description = "EveryThingBlock", category = ModuleCategory.RENDER)
public class EveryThingBlock extends Module {

    private final FloatValue x = new FloatValue("X", 0.0f, -1.0f, 1.0f);
    private final FloatValue y = new FloatValue("Y", 0.0f, -1.0f, 1.0f);
    private final FloatValue z = new FloatValue("Z", 0.0f, -1.0f, 1.0f);

    public FloatValue getX() {
        return x;
    }
    public FloatValue getY() {
        return y;
    }
    public FloatValue getZ() {
        return z;
    }

}

