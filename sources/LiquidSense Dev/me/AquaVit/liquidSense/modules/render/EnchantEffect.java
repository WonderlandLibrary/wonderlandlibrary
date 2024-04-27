package me.AquaVit.liquidSense.modules.render;


import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;



@ModuleInfo(name = "EnchantEffect", description = "EnchantEffect", category = ModuleCategory.RENDER)
public class EnchantEffect extends Module {

    private final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 160, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private final IntegerValue alphaValue = new IntegerValue("Alpha", 255, 0, 255);
    private final BoolValue rainbow = new BoolValue("RainBow", false);

    public IntegerValue getRedValue() {
        return colorRedValue;
    }

    public BoolValue getRainbow() {
        return rainbow;
    }

    public IntegerValue getGreenValue() {
        return colorGreenValue;
    }

    public IntegerValue getBlueValue() {
        return colorBlueValue;
    }

    public IntegerValue getalphaValue() {
        return alphaValue;
    }

}

