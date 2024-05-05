/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;

@ModuleInfo(name="PlayerEdit", description="PlayerEdit", category=ModuleCategory.RENDER, array=false)
public class PlayerEdit
extends Module {
    public static final FloatValue bipedHeadRotateAngleX = new FloatValue("Biped-Head-RotateAngle-X", 0.5f, -0.1f, 10.0f);
    public static final FloatValue bipedHeadRotateAngleY = new FloatValue("Biped-Head-RotateAngle-Y", 0.75f, -0.1f, 10.0f);
    public static final FloatValue bipedRightArmrotateAngleX = new FloatValue("Biped-Right-ArmrotateAngle-X", 4.75f, -0.1f, 10.0f);
    public static final FloatValue bipedRightArmrotateAngleY = new FloatValue("Biped-Right-ArmrotateAngle-Y", -1.0f, -0.1f, 10.0f);
    public static final FloatValue BipedLeftArmrotateAngleX = new FloatValue("Biped-Left-ArmrotateAngle-X", 4.5f, -0.1f, 10.0f);
    public static final FloatValue bipedLeftArmrotateAngleY = new FloatValue("Biped-Left-ArmrotateAngle-Y", -1.25f, -0.1f, 10.0f);
}

