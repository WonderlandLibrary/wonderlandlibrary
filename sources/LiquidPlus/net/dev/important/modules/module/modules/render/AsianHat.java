/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.AxisAlignedBB
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.modules.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render3DEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.color.ColorMixer;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

@Info(name="AsianHat", spacedName="Asian Hat", description="Yep. China Hat.", category=Category.RENDER, cnName="\u4e2d\u56fd\u5e3d\u5b50")
public class AsianHat
extends Module {
    private final ListValue colorModeValue = new ListValue("Color", new String[]{"Custom", "Rainbow", "Sky", "LiquidSlowly", "Fade", "Mixer"}, "Custom");
    private final IntegerValue colorRedValue = new IntegerValue("Red", 255, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("Green", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("Blue", 255, 0, 255);
    private final IntegerValue colorAlphaValue = new IntegerValue("Alpha", 255, 0, 255);
    private final IntegerValue colorEndAlphaValue = new IntegerValue("EndAlpha", 255, 0, 255);
    private final FloatValue saturationValue = new FloatValue("Saturation", 1.0f, 0.0f, 1.0f);
    private final FloatValue brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
    private final IntegerValue mixerSecondsValue = new IntegerValue("Seconds", 2, 1, 10);
    private final IntegerValue spaceValue = new IntegerValue("Color-Space", 0, 0, 100);
    private final BoolValue noFirstPerson = new BoolValue("NoFirstPerson", true);
    private final BoolValue hatBorder = new BoolValue("HatBorder", true);
    private final IntegerValue borderAlphaValue = new IntegerValue("BorderAlpha", 255, 0, 255);
    private final FloatValue borderWidthValue = new FloatValue("BorderWidth", 1.0f, 0.1f, 4.0f);
    private final List<double[]> positions = new ArrayList<double[]>();
    private double lastRadius = 0.0;

    private void checkPosition(double radius) {
        if (radius != this.lastRadius) {
            this.positions.clear();
            for (int i = 0; i <= 360; ++i) {
                this.positions.add(new double[]{-Math.sin((double)i * Math.PI / 180.0) * radius, Math.cos((double)i * Math.PI / 180.0) * radius});
            }
        }
        this.lastRadius = radius;
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        EntityPlayerSP entity = AsianHat.mc.field_71439_g;
        if (entity == null || ((Boolean)this.noFirstPerson.get()).booleanValue() && AsianHat.mc.field_71474_y.field_74320_O == 0) {
            return;
        }
        AxisAlignedBB bb = entity.func_174813_aQ();
        double radius = bb.field_72336_d - bb.field_72340_a;
        double height = bb.field_72337_e - bb.field_72338_b;
        double posX = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)AsianHat.mc.field_71428_T.field_74281_c;
        double posY = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)AsianHat.mc.field_71428_T.field_74281_c;
        double posZ = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)AsianHat.mc.field_71428_T.field_74281_c;
        Color colour = this.getColor((Entity)entity, 0);
        float r = (float)colour.getRed() / 255.0f;
        float g = (float)colour.getGreen() / 255.0f;
        float b = (float)colour.getBlue() / 255.0f;
        float al = (float)((Integer)this.colorAlphaValue.get()).intValue() / 255.0f;
        float Eal = (float)((Integer)this.colorEndAlphaValue.get()).intValue() / 255.0f;
        float partialTicks = event.getPartialTicks();
        double viewX = -AsianHat.mc.func_175598_ae().field_78730_l;
        double viewY = -AsianHat.mc.func_175598_ae().field_78731_m;
        double viewZ = -AsianHat.mc.func_175598_ae().field_78728_n;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        this.checkPosition(radius);
        AsianHat.pre3D();
        worldrenderer.func_181668_a(9, DefaultVertexFormats.field_181706_f);
        worldrenderer.func_181662_b(viewX + posX, viewY + posY + height + 0.3, viewZ + posZ).func_181666_a(r, g, b, al).func_181675_d();
        int i = 0;
        for (double[] smolPos : this.positions) {
            double posX2 = posX + smolPos[0];
            double posZ2 = posZ + smolPos[1];
            if ((Integer)this.spaceValue.get() > 0 && !((String)this.colorModeValue.get()).equalsIgnoreCase("Custom")) {
                Color colour2 = this.getColor((Entity)entity, i * (Integer)this.spaceValue.get());
                float r2 = (float)colour2.getRed() / 255.0f;
                float g2 = (float)colour2.getGreen() / 255.0f;
                float b2 = (float)colour2.getBlue() / 255.0f;
                worldrenderer.func_181662_b(viewX + posX2, viewY + posY + height, viewZ + posZ2).func_181666_a(r2, g2, b2, Eal).func_181675_d();
            } else {
                worldrenderer.func_181662_b(viewX + posX2, viewY + posY + height, viewZ + posZ2).func_181666_a(r, g, b, Eal).func_181675_d();
            }
            ++i;
        }
        worldrenderer.func_181662_b(viewX + posX, viewY + posY + height + 0.3, viewZ + posZ).func_181666_a(r, g, b, al).func_181675_d();
        tessellator.func_78381_a();
        if (((Boolean)this.hatBorder.get()).booleanValue()) {
            float lineAlp = (float)((Integer)this.borderAlphaValue.get()).intValue() / 255.0f;
            GL11.glLineWidth((float)((Float)this.borderWidthValue.get()).floatValue());
            worldrenderer.func_181668_a(2, DefaultVertexFormats.field_181706_f);
            i = 0;
            for (double[] smolPos : this.positions) {
                double posX2 = posX + smolPos[0];
                double posZ2 = posZ + smolPos[1];
                if ((Integer)this.spaceValue.get() > 0 && !((String)this.colorModeValue.get()).equalsIgnoreCase("Custom")) {
                    Color colour2 = this.getColor((Entity)entity, i * (Integer)this.spaceValue.get());
                    float r2 = (float)colour2.getRed() / 255.0f;
                    float g2 = (float)colour2.getGreen() / 255.0f;
                    float b2 = (float)colour2.getBlue() / 255.0f;
                    worldrenderer.func_181662_b(viewX + posX2, viewY + posY + height, viewZ + posZ2).func_181666_a(r2, g2, b2, lineAlp).func_181675_d();
                } else {
                    worldrenderer.func_181662_b(viewX + posX2, viewY + posY + height, viewZ + posZ2).func_181666_a(r, g, b, lineAlp).func_181675_d();
                }
                ++i;
            }
            tessellator.func_78381_a();
        }
        AsianHat.post3D();
    }

    public final Color getColor(Entity ent, int index) {
        switch ((String)this.colorModeValue.get()) {
            case "Custom": {
                return new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get());
            }
            case "Rainbow": {
                return new Color(RenderUtils.getRainbowOpaque((Integer)this.mixerSecondsValue.get(), ((Float)this.saturationValue.get()).floatValue(), ((Float)this.brightnessValue.get()).floatValue(), index));
            }
            case "Sky": {
                return RenderUtils.skyRainbow(index, ((Float)this.saturationValue.get()).floatValue(), ((Float)this.brightnessValue.get()).floatValue());
            }
            case "LiquidSlowly": {
                return ColorUtils.LiquidSlowly(System.nanoTime(), index, ((Float)this.saturationValue.get()).floatValue(), ((Float)this.brightnessValue.get()).floatValue());
            }
            case "Mixer": {
                return ColorMixer.getMixedColor(index, (Integer)this.mixerSecondsValue.get());
            }
        }
        return ColorUtils.fade(new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get()), index, 100);
    }

    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)2896);
        GL11.glDepthMask((boolean)false);
        GL11.glHint((int)3154, (int)4354);
        GL11.glDisable((int)2884);
    }

    public static void post3D() {
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }
}

