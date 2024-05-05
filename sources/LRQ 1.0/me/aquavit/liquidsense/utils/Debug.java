/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.EnumHandSide
 */
package me.aquavit.liquidsense.utils;

import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class Debug
extends MinecraftInstance {
    public static Boolean thePlayerisBlocking = false;

    public static void transformSideFirstPersonBlock(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)side * 0.56), (double)(-0.52 + (double)equippedProg * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)side * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double f = Math.sin((double)(swingProgress * swingProgress) * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        GlStateManager.func_179114_b((float)((float)(f * -20.0)), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -20.0)), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -80.0)), (float)1.0f, (float)0.0f, (float)0.0f);
    }

    public static void WindMill(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)side * 0.56), (double)(-0.52 + (double)equippedProg * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)side * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double f = Math.sin((double)(swingProgress * swingProgress) * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        GlStateManager.func_179114_b((float)((float)(f * -20.0)), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -20.0)), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -80.0)), (float)1.0f, (float)0.0f, (float)0.0f);
    }

    public static void Push(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)side * 0.56), (double)(-0.52 + (double)equippedProg * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)side * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double f = Math.sin((double)(swingProgress * swingProgress) * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        GlStateManager.func_179114_b((float)((float)(f * -10.0)), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -10.0)), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -10.0)), (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void LiquidSense(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)side * 0.56), (double)(-0.52 + (double)equippedProg * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)side * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double f = Math.sin((double)(swingProgress * swingProgress) * Math.PI);
        double f1 = Math.sin(Math.sqrt(f) * 3.1415927410125732);
        GlStateManager.func_179114_b((float)((float)(-f1 * 35.0)), (float)-8.0f, (float)-0.0f, (float)9.0f);
        GlStateManager.func_179114_b((float)((float)(-f1 * 10.0)), (float)1.0f, (float)-0.4f, (float)-0.5f);
    }
}

