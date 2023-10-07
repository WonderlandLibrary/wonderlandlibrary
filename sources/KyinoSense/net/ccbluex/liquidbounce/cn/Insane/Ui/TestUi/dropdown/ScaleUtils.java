/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 */
package net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class ScaleUtils {
    public static int[] getScaledMouseCoordinates(Minecraft mc, int mouseX, int mouseY) {
        int x = mouseX;
        int y = mouseY;
        switch (mc.field_71474_y.field_74335_Z) {
            case 0: {
                x *= 2;
                y *= 2;
                break;
            }
            case 1: {
                x = (int)((double)x * 0.5);
                y = (int)((double)y * 0.5);
                break;
            }
            case 3: {
                x = (int)((double)x * 1.5);
                y = (int)((double)y * 1.5);
            }
        }
        return new int[]{x, y};
    }

    public static double[] getScaledMouseCoordinates(Minecraft mc, double mouseX, double mouseY) {
        double x = mouseX;
        double y = mouseY;
        switch (mc.field_71474_y.field_74335_Z) {
            case 0: {
                x *= 2.0;
                y *= 2.0;
                break;
            }
            case 1: {
                x *= 0.5;
                y *= 0.5;
                break;
            }
            case 3: {
                x *= 1.5;
                y *= 1.5;
            }
        }
        return new double[]{x, y};
    }

    public static void scale(Minecraft mc) {
        switch (mc.field_71474_y.field_74335_Z) {
            case 0: {
                GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
                break;
            }
            case 1: {
                GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
                break;
            }
            case 3: {
                GlStateManager.func_179139_a((double)0.6666666666666667, (double)0.6666666666666667, (double)0.6666666666666667);
            }
        }
    }
}

