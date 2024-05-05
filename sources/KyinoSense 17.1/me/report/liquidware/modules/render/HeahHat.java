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
package me.report.liquidware.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="HeahHat", description="Yupi.", category=ModuleCategory.RENDER)
public class HeahHat
extends Module {
    private final ListValue colorModeValue = new ListValue("Color", new String[]{"Custom", "Rainbow", "LiquidSense"}, "Custom");
    private final IntegerValue colorRedValue = new IntegerValue("Red", 255, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("Green", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("Blue", 255, 0, 255);
    private final IntegerValue colorAlphaValue = new IntegerValue("Alpha", 255, 0, 255);
    private final IntegerValue colorEndAlphaValue = new IntegerValue("EndAlpha", 255, 0, 255);
    private final FloatValue saturationValue = new FloatValue("Saturation", 1.0f, 0.0f, 1.0f);
    private final FloatValue brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
    private final IntegerValue mixerSecondsValue = new IntegerValue("Seconds", 2, 1, 10);
    private final IntegerValue spaceValue = new IntegerValue("ColorSpace", 0, 0, 200);
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
        EntityPlayerSP entity = HeahHat.mc.field_71439_g;
        if (entity == null || ((Boolean)this.noFirstPerson.get()).booleanValue() && HeahHat.mc.field_71474_y.field_74320_O == 0) {
            return;
        }
        AxisAlignedBB bb = entity.func_174813_aQ();
        double radius = bb.field_72336_d - bb.field_72340_a;
        double height = bb.field_72337_e - bb.field_72338_b;
        double posX = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)HeahHat.mc.field_71428_T.field_74281_c;
        double posY = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)HeahHat.mc.field_71428_T.field_74281_c;
        double posZ = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)HeahHat.mc.field_71428_T.field_74281_c;
        Color colour = this.getColor((Entity)entity, 0);
        float r = (float)colour.getRed() / 255.0f;
        float g = (float)colour.getGreen() / 255.0f;
        float b = (float)colour.getBlue() / 255.0f;
        float al = (float)((Integer)this.colorAlphaValue.get()).intValue() / 255.0f;
        float Eal = (float)((Integer)this.colorEndAlphaValue.get()).intValue() / 255.0f;
        double viewX = -HeahHat.mc.func_175598_ae().field_78730_l;
        double viewY = -HeahHat.mc.func_175598_ae().field_78731_m;
        double viewZ = -HeahHat.mc.func_175598_ae().field_78728_n;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        this.checkPosition(radius);
        HeahHat.pre3D();
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
        HeahHat.post3D();
    }

    public final Color getColor(Entity ent, int index) {
        switch ((String)this.colorModeValue.get()) {
            case "Custom": {
                return new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get());
            }
            case "Rainbow": {
                return new Color(RenderUtils.getRainbowOpaque((Integer)this.mixerSecondsValue.get(), ((Float)this.saturationValue.get()).floatValue(), ((Float)this.brightnessValue.get()).floatValue(), index));
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

