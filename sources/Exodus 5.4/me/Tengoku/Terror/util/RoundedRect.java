/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.util;

import org.lwjgl.opengl.GL11;

public class RoundedRect {
    public static void setColor(int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
    }

    public static void drawRoundedRect(double d, double d2, double d3, double d4, double d5, int n) {
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        d *= 2.0;
        d2 *= 2.0;
        d3 *= 2.0;
        d4 *= 2.0;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        RoundedRect.setColor(n);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)9);
        int n2 = 0;
        while (n2 <= 90) {
            GL11.glVertex2d((double)(d + d5 + Math.sin((double)n2 * Math.PI / 180.0) * d5 * -1.0), (double)(d2 + d5 + Math.cos((double)n2 * Math.PI / 180.0) * d5 * -1.0));
            n2 += 3;
        }
        n2 = 90;
        while (n2 <= 180) {
            GL11.glVertex2d((double)(d + d5 + Math.sin((double)n2 * Math.PI / 180.0) * d5 * -1.0), (double)(d4 - d5 + Math.cos((double)n2 * Math.PI / 180.0) * d5 * -1.0));
            n2 += 3;
        }
        n2 = 0;
        while (n2 <= 90) {
            GL11.glVertex2d((double)(d3 - d5 + Math.sin((double)n2 * Math.PI / 180.0) * d5), (double)(d4 - d5 + Math.cos((double)n2 * Math.PI / 180.0) * d5));
            n2 += 3;
        }
        n2 = 90;
        while (n2 <= 180) {
            GL11.glVertex2d((double)(d3 - d5 + Math.sin((double)n2 * Math.PI / 180.0) * d5), (double)(d2 + d5 + Math.cos((double)n2 * Math.PI / 180.0) * d5));
            n2 += 3;
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
    }
}

