/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="Animations", description="Blocking animations", category=ModuleCategory.RENDER)
public class Animations
extends Module {
    public static final FloatValue xValue = new FloatValue("Blocking-X", 0.0f, -2.0f, 2.0f);
    public static final FloatValue yValue = new FloatValue("Blocking-Y", 0.0f, -2.0f, 2.0f);
    public static final FloatValue zValue = new FloatValue("Blocking-Z", 0.0f, -2.0f, 2.0f);
    public static final FloatValue scaleValue = new FloatValue("Blocking-scale", 0.8f, 0.1f, 1.0f);
    public static final FloatValue xhValue = new FloatValue("Held-X", 0.0f, -2.0f, 2.0f);
    public static final FloatValue yhValue = new FloatValue("Held-Y", 0.0f, -2.0f, 2.0f);
    public static final FloatValue zhValue = new FloatValue("Held-Z", 0.0f, -2.0f, 2.0f);
    public static final FloatValue scalehValue = new FloatValue("Held-scale", 0.8f, 0.1f, 1.0f);
    public static final BoolValue heldValue = new BoolValue("Held", true);
    public static final BoolValue SPValue = new BoolValue("Progress", true);
    public static final BoolValue oldSPValue = new BoolValue("Progress1.8", true);
    public static final FloatValue SpeedRotate = new FloatValue("Rotate-Speed", 1.0f, 0.0f, 10.0f);
    public static final ListValue transformFirstPersonRotate = new ListValue("RotateMode", new String[]{"RotateY", "RotateXY", "Custom", "None"}, "RotateY");
    public static final FloatValue customRotate1 = new FloatValue("CustomRotateXAxis", 0.0f, -180.0f, 180.0f);
    public static final FloatValue customRotate2 = new FloatValue("CustomRotateYAxis", 0.0f, -180.0f, 180.0f);
    public static final FloatValue customRotate3 = new FloatValue("CustomRotateZAxis", 0.0f, -180.0f, 180.0f);
    public static final IntegerValue SpeedSwing = new IntegerValue("Swing-Speed", 4, 0, 20);
    public static final ListValue Sword = new ListValue("Sword", new String[]{"Old", "1.7", "WindMill", "Push", "Smooth", "SigmaOld", "BigGod", "Jello"}, "1.7");

    @Override
    public String getTag() {
        return (String)Sword.get();
    }
}

