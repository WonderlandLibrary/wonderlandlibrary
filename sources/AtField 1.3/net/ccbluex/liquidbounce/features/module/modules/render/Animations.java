/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

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
    public static final FloatValue customRotate3;
    public static final FloatValue zValue;
    public static final ListValue transformFirstPersonRotate;
    public static final FloatValue customRotate1;
    public static final BoolValue SPValue;
    public static final BoolValue oldSPValue;
    public static final FloatValue scaleValue;
    public static final FloatValue yhValue;
    public static final BoolValue heldValue;
    public static final FloatValue xValue;
    public static final ListValue Sword;
    public static final FloatValue yValue;
    public static final FloatValue SpeedRotate;
    public static final FloatValue zhValue;
    public static final FloatValue scalehValue;
    public static final IntegerValue SpeedSwing;
    public static final FloatValue customRotate2;
    public static final FloatValue xhValue;

    @Override
    public String getTag() {
        return (String)Sword.get();
    }

    static {
        xValue = new FloatValue("Blocking-X", 0.0f, -2.0f, 2.0f);
        yValue = new FloatValue("Blocking-Y", 0.0f, -2.0f, 2.0f);
        zValue = new FloatValue("Blocking-Z", 0.0f, -2.0f, 2.0f);
        scaleValue = new FloatValue("Blocking-scale", 0.8f, 0.1f, 1.0f);
        xhValue = new FloatValue("Held-X", 0.0f, -2.0f, 2.0f);
        yhValue = new FloatValue("Held-Y", 0.0f, -2.0f, 2.0f);
        zhValue = new FloatValue("Held-Z", 0.0f, -2.0f, 2.0f);
        scalehValue = new FloatValue("Held-scale", 0.8f, 0.1f, 1.0f);
        heldValue = new BoolValue("Held", true);
        SPValue = new BoolValue("Progress", true);
        oldSPValue = new BoolValue("Progress1.8", true);
        SpeedRotate = new FloatValue("Rotate-Speed", 1.0f, 0.0f, 10.0f);
        transformFirstPersonRotate = new ListValue("RotateMode", new String[]{"RotateY", "RotateXY", "Custom", "None"}, "RotateY");
        customRotate1 = new FloatValue("CustomRotateXAxis", 0.0f, -180.0f, 180.0f);
        customRotate2 = new FloatValue("CustomRotateYAxis", 0.0f, -180.0f, 180.0f);
        customRotate3 = new FloatValue("CustomRotateZAxis", 0.0f, -180.0f, 180.0f);
        SpeedSwing = new IntegerValue("Swing-Speed", 4, 0, 20);
        Sword = new ListValue("Sword", new String[]{"Old", "1.7", "WindMill", "Push", "Smooth", "SigmaOld", "BigGod", "Jello"}, "1.7");
    }
}

