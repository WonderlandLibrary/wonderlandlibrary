/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;

@ModuleInfo(name="CustomFont", description="CustomFont", category=ModuleCategory.RENDER)
public class CustomFont
extends Module {
    public static final IntegerValue fontWidthValue;
    public static final FloatValue shadowstrenge;
    public static final ListValue shadowValue;
    public static final TextValue displayString;

    static {
        shadowValue = new ListValue("ShadowMode", new String[]{"LiquidBounce", "Outline", "Default", "Custom"}, "Default");
        displayString = new TextValue("DisplayText", "AtField");
        fontWidthValue = new IntegerValue("FontWidth", 7, 5, 10);
        shadowstrenge = new FloatValue("ShadowStrengh", 0.1f, 0.1f, 1.0f);
    }
}

