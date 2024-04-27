/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.EnumHandSide
 */
package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class BlockAnimationUtils
extends MinecraftInstance {
    public static Boolean thePlayerisBlocking = false;

    public static void doOld(EnumHandSide enumHandSide, float f, float f2) {
        int n = enumHandSide == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)n * 0.56), (double)(-0.52 + (double)f * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)n * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double d = Math.sin((double)(f2 * f2) * Math.PI);
        double d2 = Math.sin(Math.sqrt(f2) * Math.PI);
        GlStateManager.func_179114_b((float)((float)(d * -20.0)), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)(d2 * -20.0)), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(d2 * -80.0)), (float)1.0f, (float)0.0f, (float)0.0f);
    }
}

