package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="WorldAnim", description="Animation for blocking.", category=ModuleCategory.RENDER)
public class WolrdAnim
extends Module {
    public static final ListValue animmode = new ListValue("Mode", new String[]{"UP", "Down", "Slide"}, "UP");
    public static final IntegerValue chunkAnimationDurationValue = new IntegerValue("ChunkAnimationDuration", 2000, 500, 5000);
    public static final IntegerValue chunkPositionValue = new IntegerValue("ChunkPosition", 50, 10, 256);
}
