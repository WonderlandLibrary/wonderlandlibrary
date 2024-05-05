/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.EntityLivingBase
 *  org.lwjgl.opengl.GL11
 */
package me.report.liquidware.modules.render;

import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Crosshair", description="Crosshair", category=ModuleCategory.RENDER)
public class Crosshair
extends Module {
    public ListValue colorModeValue = new ListValue("Color", new String[]{"Custom", "Fade"}, "Custom");
    public IntegerValue colorRedValue = new IntegerValue("Red", 0, 0, 255);
    public IntegerValue colorGreenValue = new IntegerValue("Green", 0, 0, 255);
    public IntegerValue colorBlueValue = new IntegerValue("Blue", 0, 0, 255);
    public IntegerValue colorAlphaValue = new IntegerValue("Alpha", 255, 0, 255);
    private final FloatValue saturationValue = new FloatValue("Saturation", 1.0f, 0.0f, 1.0f);
    private final FloatValue brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
    private final IntegerValue mixerSecondsValue = new IntegerValue("Seconds", 2, 1, 10);
    public FloatValue widthVal = new FloatValue("Width", 2.0f, 0.25f, 10.0f);
    public FloatValue sizeVal = new FloatValue("Size-Length", 7.0f, 0.25f, 15.0f);
    public FloatValue gapVal = new FloatValue("Gap", 5.0f, 0.25f, 15.0f);
    public BoolValue dynamicVal = new BoolValue("Dynamic", true);
    public BoolValue hitMarkerVal = new BoolValue("HitMarker", true);
    public BoolValue noVanillaCH = new BoolValue("NoVanillaCrossHair", true);

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        ScaledResolution scaledRes = new ScaledResolution(mc);
        float width = ((Float)this.widthVal.get()).floatValue();
        float size = ((Float)this.sizeVal.get()).floatValue();
        float gap = ((Float)this.gapVal.get()).floatValue();
        GL11.glPushMatrix();
        RenderUtils.drawBorderedRect((float)scaledRes.func_78326_a() / 2.0f - width, (float)scaledRes.func_78328_b() / 2.0f - gap - size - (float)(this.isMoving() ? 2 : 0), (float)scaledRes.func_78326_a() / 2.0f + 1.0f + width, (float)scaledRes.func_78328_b() / 2.0f - gap - (float)(this.isMoving() ? 2 : 0), 0.5f, new Color(0, 0, 0, (Integer)this.colorAlphaValue.get()).getRGB(), this.getCrosshairColor().getRGB());
        RenderUtils.drawBorderedRect((float)scaledRes.func_78326_a() / 2.0f - width, (float)scaledRes.func_78328_b() / 2.0f + gap + 1.0f + (float)(this.isMoving() ? 2 : 0) - 0.15f, (float)scaledRes.func_78326_a() / 2.0f + 1.0f + width, (float)scaledRes.func_78328_b() / 2.0f + 1.0f + gap + size + (float)(this.isMoving() ? 2 : 0) - 0.15f, 0.5f, new Color(0, 0, 0, (Integer)this.colorAlphaValue.get()).getRGB(), this.getCrosshairColor().getRGB());
        RenderUtils.drawBorderedRect((float)scaledRes.func_78326_a() / 2.0f - gap - size - (float)(this.isMoving() ? 2 : 0) + 0.15f, (float)scaledRes.func_78328_b() / 2.0f - width, (float)scaledRes.func_78326_a() / 2.0f - gap - (float)(this.isMoving() ? 2 : 0) + 0.15f, (float)(scaledRes.func_78328_b() / 2) + 1.0f + width, 0.5f, new Color(0, 0, 0, (Integer)this.colorAlphaValue.get()).getRGB(), this.getCrosshairColor().getRGB());
        RenderUtils.drawBorderedRect((float)scaledRes.func_78326_a() / 2.0f + 1.0f + gap + (float)(this.isMoving() ? 2 : 0), (float)scaledRes.func_78328_b() / 2.0f - width, (float)scaledRes.func_78326_a() / 2.0f + size + gap + 1.0f + (float)(this.isMoving() ? 2 : 0), (float)(scaledRes.func_78328_b() / 2) + 1.0f + width, 0.5f, new Color(0, 0, 0, (Integer)this.colorAlphaValue.get()).getRGB(), this.getCrosshairColor().getRGB());
        GL11.glPopMatrix();
        GlStateManager.func_179117_G();
        EntityLivingBase target = ((KillAura)LiquidBounce.moduleManager.getModule(KillAura.class)).getTarget();
        if (((Boolean)this.hitMarkerVal.get()).booleanValue() && target != null && target.field_70737_aN > 0) {
            GL11.glPushMatrix();
            GlStateManager.func_179147_l();
            GlStateManager.func_179090_x();
            GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)((float)target.field_70737_aN / (float)target.field_70738_aO));
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)1.0f);
            GL11.glBegin((int)3);
            GL11.glVertex2f((float)((float)scaledRes.func_78326_a() / 2.0f + gap), (float)((float)scaledRes.func_78328_b() / 2.0f + gap));
            GL11.glVertex2f((float)((float)scaledRes.func_78326_a() / 2.0f + gap + size), (float)((float)scaledRes.func_78328_b() / 2.0f + gap + size));
            GL11.glEnd();
            GL11.glBegin((int)3);
            GL11.glVertex2f((float)((float)scaledRes.func_78326_a() / 2.0f - gap), (float)((float)scaledRes.func_78328_b() / 2.0f - gap));
            GL11.glVertex2f((float)((float)scaledRes.func_78326_a() / 2.0f - gap - size), (float)((float)scaledRes.func_78328_b() / 2.0f - gap - size));
            GL11.glEnd();
            GL11.glBegin((int)3);
            GL11.glVertex2f((float)((float)scaledRes.func_78326_a() / 2.0f - gap), (float)((float)scaledRes.func_78328_b() / 2.0f + gap));
            GL11.glVertex2f((float)((float)scaledRes.func_78326_a() / 2.0f - gap - size), (float)((float)scaledRes.func_78328_b() / 2.0f + gap + size));
            GL11.glEnd();
            GL11.glBegin((int)3);
            GL11.glVertex2f((float)((float)scaledRes.func_78326_a() / 2.0f + gap), (float)((float)scaledRes.func_78328_b() / 2.0f - gap));
            GL11.glVertex2f((float)((float)scaledRes.func_78326_a() / 2.0f + gap + size), (float)((float)scaledRes.func_78328_b() / 2.0f - gap - size));
            GL11.glEnd();
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
            GL11.glPopMatrix();
        }
    }

    private boolean isMoving() {
        return (Boolean)this.dynamicVal.get() != false && MovementUtils.isMoving();
    }

    private Color getCrosshairColor() {
        switch ((String)this.colorModeValue.get()) {
            case "Custom": {
                return new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get(), (Integer)this.colorAlphaValue.get());
            }
            case "Rainbow": {
                return new Color(RenderUtils.getRainbowOpaque((Integer)this.mixerSecondsValue.get(), ((Float)this.saturationValue.get()).floatValue(), ((Float)this.brightnessValue.get()).floatValue(), 0));
            }
            case "LiquidSlowly": {
                return ColorUtils.reAlpha(ColorUtils.LiquidSlowly(System.nanoTime(), 0, ((Float)this.saturationValue.get()).floatValue(), ((Float)this.brightnessValue.get()).floatValue()), (Integer)this.colorAlphaValue.get());
            }
        }
        return ColorUtils.reAlpha(ColorUtils.fade(new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get()), 0, 100), (Integer)this.colorAlphaValue.get());
    }
}

