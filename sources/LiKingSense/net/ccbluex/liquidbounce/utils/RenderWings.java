/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderWings
extends ModelBase {
    Minecraft mc = Minecraft.func_71410_x();
    private ResourceLocation location = new ResourceLocation("marketsafe/wings/wings.png");
    private ModelRenderer wing;
    private ModelRenderer wingTip;
    private boolean playerUsesFullHeight = true;

    public RenderWings() {
        RenderWings renderWings = this;
        String string = "wing.bone";
        int n = 0;
        RenderWings renderWings2 = this;
        String string2 = "wing.skin";
        int n2 = 0;
        this.wing = new ModelRenderer((ModelBase)this, "wing");
        this.wing.func_78793_a(-2.0f, 0.0f, 0.0f);
        0.func_78786_a((String)this.wing, (float)this.wing, (float)"bone", -10.0f, (int)-1.0f, (int)-1.0f, 0);
        this.func_78786_a("wingtip.skin", (float)this.wing, (float)"skin", -10.0f, (int)0.0f, (int)0.5f, 0);
        this.wingTip = new ModelRenderer((ModelBase)this, "wingtip");
        this.wingTip.func_78793_a(-10.0f, 0.0f, 0.0f);
        0.func_78786_a((String)this.wingTip, (float)this.wingTip, (float)"bone", -10.0f, (int)-0.5f, (int)-0.5f, 0);
        this.func_78786_a("wingtip.bone", (float)this.wingTip, (float)"skin", -10.0f, (int)0.0f, (int)0.5f, 0);
        this.wing.func_78792_a(this.wingTip);
    }

    public void renderWings(float partialTicks) {
        boolean per = this.mc.field_71474_y.field_74320_O == 0;
        double scale = 1.0;
        GL11.glPushMatrix();
        GL11.glScaled((double)(-scale), (double)(-scale), (double)scale);
        GL11.glRotated((double)180.0, (double)0.0, (double)1.0, (double)0.0);
        GL11.glTranslated((double)0.0, (double)(-(this.playerUsesFullHeight ? 1.45 : 1.25) / scale), (double)0.0);
        GL11.glTranslated((double)0.0, (double)0.0, (double)(0.2 / scale));
        GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
        this.mc.func_110434_K().func_110577_a(this.location);
        for (int j = 0; j < 2; ++j) {
            GL11.glEnable((int)2884);
            float f11 = (float)(System.currentTimeMillis() % 1000L) / 1000.0f * (float)Math.PI * 2.0f;
            this.wing.field_78795_f = (float)Math.toRadians(-80.0) - (float)Math.cos(f11) * 0.2f;
            this.wing.field_78796_g = (float)Math.toRadians(20.0) + (float)Math.sin(f11) * 0.4f;
            this.wing.field_78808_h = (float)Math.toRadians(20.0);
            this.wingTip.field_78808_h = -((float)(Math.sin(f11 + 2.0f) + 0.5)) * 0.75f;
            this.wing.func_78785_a(0.0625f);
            GL11.glScalef((float)-1.0f, (float)1.0f, (float)1.0f);
            if (j != 0) continue;
            GL11.glCullFace((int)1028);
        }
        GL11.glCullFace((int)1029);
        GL11.glDisable((int)2884);
        GL11.glColor3f((float)255.0f, (float)255.0f, (float)255.0f);
        GL11.glPopMatrix();
    }

    private double interpolate(float yaw1, float yaw2, float percent) {
        double f = (double)(yaw1 + (yaw2 - yaw1) * percent) % 360.0;
        if (f < 0.0) {
            f += 360.0;
        }
        return f;
    }
}

