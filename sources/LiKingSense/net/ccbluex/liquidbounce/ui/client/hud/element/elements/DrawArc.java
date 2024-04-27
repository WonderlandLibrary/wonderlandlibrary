/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import org.lwjgl.opengl.GL11;

public class DrawArc {
    public static String helper = "";

    public static void drawArc(float n, float n2, double n3, int n4, int n5, double n6, float n7) {
        n3 *= 2.0;
        n *= 2.0f;
        n2 *= 2.0f;
        float n8 = (float)(n4 >> 24 & 0xFF) / 255.0f;
        float n9 = (float)(n4 >> 16 & 0xFF) / 255.0f;
        float n10 = (float)(n4 >> 8 & 0xFF) / 255.0f;
        float n11 = (float)(n4 & 0xFF) / 255.0f;
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n12 = 0;
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        int n13 = 0;
        int n14 = 0;
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glLineWidth((float)n7);
        GL11.glEnable((int)2848);
        GL11.glColor4f((float)n9, (float)n10, (float)n11, (float)n8);
        GL11.glBegin((int)3);
        int n122 = n5;
        while ((double)n122 <= n6) {
            GL11.glVertex2d((double)((double)n + Math.sin((double)n122 * Math.PI / 180.0) * n3), (double)((double)n2 + Math.cos((double)n122 * Math.PI / 180.0) * n3));
            ++n122;
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        int n15 = 0;
        int n16 = 0;
    }
}

