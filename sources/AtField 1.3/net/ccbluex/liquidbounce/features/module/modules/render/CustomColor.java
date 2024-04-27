/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

@ModuleInfo(name="CustomColor", description="CustomColor", category=ModuleCategory.RENDER)
public class CustomColor
extends Module {
    public static final BoolValue hueInterpolation;
    private float tempY = 65.0f;
    public static final IntegerValue b2;
    public static final IntegerValue gradientSpeed;
    public static final IntegerValue a2;
    public static final IntegerValue g;
    private float tempHeight = 65.0f;
    public static final BoolValue chatPosition;
    public static final IntegerValue a;
    public static final IntegerValue g2;
    public static final IntegerValue r2;
    public static final FloatValue ra;
    public static final IntegerValue b;
    public static final IntegerValue r;

    @EventTarget
    public void onRender2D(Render2DEvent render2DEvent) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
    }

    static {
        r = new IntegerValue("R", 0, 0, 255);
        g = new IntegerValue("G", 255, 0, 255);
        b = new IntegerValue("B", 255, 0, 255);
        r2 = new IntegerValue("R2", 255, 0, 255);
        g2 = new IntegerValue("G2", 255, 0, 255);
        b2 = new IntegerValue("B2", 255, 0, 255);
        a = new IntegerValue("A", 100, 0, 255);
        a2 = new IntegerValue("A2", 100, 0, 255);
        ra = new FloatValue("Radius", 4.5f, 0.1f, 8.0f);
        gradientSpeed = new IntegerValue("ColorSpeed", 100, 10, 1000);
        hueInterpolation = new BoolValue("Interpolate", false);
        chatPosition = new BoolValue("chatPosition", false);
    }
}

