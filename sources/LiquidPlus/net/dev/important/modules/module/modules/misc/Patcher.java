/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.modules.module.modules.misc;

import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.value.BoolValue;

@Info(name="DisableDevice", description="Bring many Patcher mod features into LiquidBounce+. (see settings for more info)", category=Category.MISC, canEnable=false, cnName="\u6e38\u620f\u8865\u4e01")
public class Patcher
extends Module {
    public static final BoolValue betterFontRenderer = new BoolValue("BetterVanillaFontRenderer", false);
    public static final BoolValue betterFontRendererStringCache = new BoolValue("BVFR-StringCache", false);
    public static final BoolValue keepShadersOnPerspectiveChange = new BoolValue("KeepShadersOnPerspectiveChange", false);
    public static final BoolValue optimizedWorldSwapping = new BoolValue("OptimizedWorldSnapping", true);
    public static final BoolValue batchModelRendering = new BoolValue("BatchModelRendering", false);
    public static final BoolValue labyModMoment = new BoolValue("LabyMod-Moment", false);
    public static final BoolValue lowAnimationTick = new BoolValue("LowAnimationTick", false);
    public static final BoolValue chatPosition = new BoolValue("ChatPosition1.12", true);
    public static final BoolValue silentNPESP = new BoolValue("SilentNPE-SpawnPlayer", true);
}

