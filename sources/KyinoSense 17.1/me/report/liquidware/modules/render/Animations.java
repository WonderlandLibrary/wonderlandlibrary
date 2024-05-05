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

@ModuleInfo(name="Animations", description="Animations", category=ModuleCategory.RENDER, array=false)
public class Animations
extends Module {
    public static BoolValue rotatePlayer = new BoolValue("RotatePlayer", false);
    public static final ListValue getModeValue = new ListValue("Mode", new String[]{"Slide2", "Swong", "Zoom", "MeMe", "IDK", "HRotate", "1.7", "Remix", "Lucky", "Rotate", "Exhibition", "OldExhibition", "Tifality", "Swank", "Slide", "Push", "Stella", "Flux", "Hit1Beta", "Hit2Beta", "Hit3Beta"}, "Slide");
    public static ListValue guiAnimations = new ListValue("Container-Animation", new String[]{"None", "Zoom", "Slide"}, "None");
    public static FloatValue itemPosX = new FloatValue("ItemPos-X", 0.0f, -1.0f, 1.0f);
    public static FloatValue itemPosY = new FloatValue("ItemPos-Y", 0.0f, -1.0f, 1.0f);
    public static FloatValue itemPosZ = new FloatValue("ItemPos-Z", 0.0f, -1.0f, 1.0f);
    public static FloatValue Scale = new FloatValue("Scale", 1.0f, 0.0f, 2.0f);
    public static final FloatValue SpeedRotate = new FloatValue("Speed-Rotate", 1.0f, 0.0f, 10.0f);
    public static final IntegerValue Speed = new IntegerValue("Speed-Animations", 10, 1, 50);
    public static final FloatValue bobbing = new FloatValue("Speed-Bobbing", 0.3f, 0.3f, 10.0f);
    public static final IntegerValue SpeedSwing = new IntegerValue("Speed-Swing", 15, 0, 20);
    public static final BoolValue fakeBlock = new BoolValue("Fake-Block", false);
    public static final BoolValue blockEverything = new BoolValue("Block-Everything", false);

    @Override
    public String getTag() {
        return (String)getModeValue.get();
    }
}

