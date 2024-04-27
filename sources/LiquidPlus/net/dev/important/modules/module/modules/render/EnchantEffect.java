/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.modules.module.modules.render;

import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;

@Info(name="EnchantEffect", spacedName="Enchant Effect", description="qwq", category=Category.RENDER, cnName="\u66f4\u597d\u7684\u9644\u9b54")
public class EnchantEffect
extends Module {
    public IntegerValue redValue = new IntegerValue("Red", 255, 0, 255);
    public IntegerValue greenValue = new IntegerValue("Green", 0, 0, 255);
    public IntegerValue blueValue = new IntegerValue("Blue", 0, 0, 255);
    public ListValue modeValue = new ListValue("Mode", new String[]{"Custom", "Rainbow", "Sky", "Mixer"}, "Custom");
    public IntegerValue rainbowSpeedValue = new IntegerValue("Seconds", 1, 1, 6);
    public IntegerValue rainbowDelayValue = new IntegerValue("Delay", 5, 0, 10);
    public FloatValue rainbowSatValue = new FloatValue("Saturation", 1.0f, 0.0f, 1.0f);
    public FloatValue rainbowBrgValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
}

