/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils.render.particle.util;

import org.lwjgl.opengl.GL11;

public class RenderUtils {
    public static void drawCircle(float f, float f2, float f3, int n) {
        float f4 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)((double)f + Math.sin((double)i * Math.PI / 180.0) * (double)f3), (double)((double)f2 + Math.cos((double)i * Math.PI / 180.0) * (double)f3));
        }
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void connectPoints(float f, float f2, float f3, float f4) {
        GL11.glPushMatrix();
        GL11.glEnable((int)2848);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.8f);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)0.5f);
        GL11.glBegin((int)1);
        GL11.glVertex2f((float)f, (float)f2);
        GL11.glVertex2f((float)f3, (float)f4);
        GL11.glEnd();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }
}

