/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="ItemRotate", description="360 Items", category=ModuleCategory.RENDER)
public class ItemRotate
extends Module {
    public static final FloatValue SpeedRotate = new FloatValue("Speed-Rotate", 1.0f, 0.0f, 10.0f);
    public static final IntegerValue customRotate1 = new IntegerValue("Custom-Rotate_X", 0, -360, 360);
    public static final IntegerValue customRotate2 = new IntegerValue("Custom-Rotate_Y", 0, -360, 360);
    public static final IntegerValue customRotate3 = new IntegerValue("Custom-Rotate_Z", 0, -360, 360);
    public static final BoolValue RotateItems = new BoolValue("RotateItems", false);
    public static final ListValue transformFirstPersonRotate = new ListValue("TransformFirstPersonRotate", new String[]{"Rotate1", "Rotate2", "Custom", "None"}, "Rotate1");
    public static final ListValue doBlockTransformationsRotate = new ListValue("DoBlockTransformationsRotate", new String[]{"Rotate1", "Rotate2", "Custom", "None"}, "None");
    public static final ListValue swingMethod = new ListValue("SwingMethod", new String[]{"Swing", "Cancel", "Default"}, "Default");
}

