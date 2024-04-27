/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.AxisAlignedBB
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.modules.module.modules.render;

import java.awt.Color;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render3DEvent;
import net.dev.important.event.TickEvent;
import net.dev.important.gui.font.GameFontRenderer;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.color.ColorMixer;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.modules.module.modules.render.Tracers;
import net.dev.important.utils.AnimationUtils;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

@Info(name="TargetMark", spacedName="Target Mark", description="Displays your KillAura's target in 3D.", category=Category.RENDER, cnName="\u76ee\u6807\u6807\u8bb0")
public class TargetMark
extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Default", "Box", "Jello", "Tracers"}, "Default");
    private final ListValue colorModeValue = new ListValue("Color", new String[]{"Custom", "Rainbow", "Sky", "LiquidSlowly", "Fade", "Mixer", "Health"}, "Custom");
    private final IntegerValue colorRedValue = new IntegerValue("Red", 255, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("Green", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("Blue", 255, 0, 255);
    private final IntegerValue colorAlphaValue = new IntegerValue("Alpha", 255, 0, 255);
    private final FloatValue jelloAlphaValue = new FloatValue("JelloEndAlphaPercent", 0.4f, 0.0f, 1.0f, "x", () -> ((String)this.modeValue.get()).equalsIgnoreCase("jello"));
    private final FloatValue jelloWidthValue = new FloatValue("JelloCircleWidth", 3.0f, 0.01f, 5.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("jello"));
    private final FloatValue jelloGradientHeightValue = new FloatValue("JelloGradientHeight", 3.0f, 1.0f, 8.0f, "m", () -> ((String)this.modeValue.get()).equalsIgnoreCase("jello"));
    private final FloatValue jelloFadeSpeedValue = new FloatValue("JelloFadeSpeed", 0.1f, 0.01f, 0.5f, "x", () -> ((String)this.modeValue.get()).equalsIgnoreCase("jello"));
    private final FloatValue saturationValue = new FloatValue("Saturation", 1.0f, 0.0f, 1.0f);
    private final FloatValue brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
    private final IntegerValue mixerSecondsValue = new IntegerValue("Seconds", 2, 1, 10);
    public final FloatValue moveMarkValue = new FloatValue("MoveMarkY", 0.6f, 0.0f, 2.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("default"));
    private final FloatValue thicknessValue = new FloatValue("Thickness", 1.0f, 0.1f, 5.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("tracers"));
    private final BoolValue colorTeam = new BoolValue("Team", false);
    private EntityLivingBase entity;
    private double direction = 1.0;
    private double yPos;
    private double progress = 0.0;
    private float al = 0.0f;
    private AxisAlignedBB bb;
    private KillAura aura;
    private long lastMS = System.currentTimeMillis();
    private long lastDeltaMS = 0L;

    @Override
    public void onInitialize() {
        this.aura = (KillAura)Client.moduleManager.getModule(KillAura.class);
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (((String)this.modeValue.get()).equalsIgnoreCase("jello") && !((String)this.aura.getTargetModeValue().get()).equalsIgnoreCase("multi")) {
            this.al = AnimationUtils.changer(this.al, this.aura.getTarget() != null ? ((Float)this.jelloFadeSpeedValue.get()).floatValue() : -((Float)this.jelloFadeSpeedValue.get()).floatValue(), 0.0f, (float)((Integer)this.colorAlphaValue.get()).intValue() / 255.0f);
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (((String)this.modeValue.get()).equalsIgnoreCase("jello") && !((String)this.aura.getTargetModeValue().get()).equalsIgnoreCase("multi")) {
            double lastY = this.yPos;
            if (this.al > 0.0f) {
                if (System.currentTimeMillis() - this.lastMS >= 1000L) {
                    this.direction = -this.direction;
                    this.lastMS = System.currentTimeMillis();
                }
                long weird = this.direction > 0.0 ? System.currentTimeMillis() - this.lastMS : 1000L - (System.currentTimeMillis() - this.lastMS);
                this.progress = (double)weird / 1000.0;
                this.lastDeltaMS = System.currentTimeMillis() - this.lastMS;
            } else {
                this.lastMS = System.currentTimeMillis() - this.lastDeltaMS;
            }
            if (this.aura.getTarget() != null) {
                this.entity = this.aura.getTarget();
                this.bb = this.entity.func_174813_aQ();
            }
            if (this.bb == null || this.entity == null) {
                return;
            }
            double radius = this.bb.field_72336_d - this.bb.field_72340_a;
            double height = this.bb.field_72337_e - this.bb.field_72338_b;
            double posX = this.entity.field_70142_S + (this.entity.field_70165_t - this.entity.field_70142_S) * (double)TargetMark.mc.field_71428_T.field_74281_c;
            double posY = this.entity.field_70137_T + (this.entity.field_70163_u - this.entity.field_70137_T) * (double)TargetMark.mc.field_71428_T.field_74281_c;
            double posZ = this.entity.field_70136_U + (this.entity.field_70161_v - this.entity.field_70136_U) * (double)TargetMark.mc.field_71428_T.field_74281_c;
            this.yPos = this.easeInOutQuart(this.progress) * height;
            double deltaY = (this.direction > 0.0 ? this.yPos - lastY : lastY - this.yPos) * -this.direction * (double)((Float)this.jelloGradientHeightValue.get()).floatValue();
            if (this.al <= 0.0f && this.entity != null) {
                this.entity = null;
                return;
            }
            Color colour = this.getColor((Entity)this.entity);
            float r = (float)colour.getRed() / 255.0f;
            float g = (float)colour.getGreen() / 255.0f;
            float b = (float)colour.getBlue() / 255.0f;
            TargetMark.pre3D();
            GL11.glTranslated((double)(-TargetMark.mc.func_175598_ae().field_78730_l), (double)(-TargetMark.mc.func_175598_ae().field_78731_m), (double)(-TargetMark.mc.func_175598_ae().field_78728_n));
            GL11.glBegin((int)8);
            for (int i = 0; i <= 360; ++i) {
                double calc = (double)i * Math.PI / 180.0;
                double posX2 = posX - Math.sin(calc) * radius;
                double posZ2 = posZ + Math.cos(calc) * radius;
                GL11.glColor4f((float)r, (float)g, (float)b, (float)0.0f);
                GL11.glVertex3d((double)posX2, (double)(posY + this.yPos + deltaY), (double)posZ2);
                GL11.glColor4f((float)r, (float)g, (float)b, (float)(this.al * ((Float)this.jelloAlphaValue.get()).floatValue()));
                GL11.glVertex3d((double)posX2, (double)(posY + this.yPos), (double)posZ2);
            }
            GL11.glEnd();
            this.drawCircle(posX, posY + this.yPos, posZ, ((Float)this.jelloWidthValue.get()).floatValue(), radius, r, g, b, this.al);
            TargetMark.post3D();
        } else if (((String)this.modeValue.get()).equalsIgnoreCase("default")) {
            if (!((String)this.aura.getTargetModeValue().get()).equalsIgnoreCase("multi") && this.aura.getTarget() != null) {
                RenderUtils.drawPlatform((Entity)this.aura.getTarget(), this.aura.getHitable() ? ColorUtils.reAlpha(this.getColor((Entity)this.entity), (Integer)this.colorAlphaValue.get()) : new Color(255, 0, 0, (Integer)this.colorAlphaValue.get()));
            }
        } else if (((String)this.modeValue.get()).equalsIgnoreCase("tracers")) {
            if (!((String)this.aura.getTargetModeValue().get()).equalsIgnoreCase("multi") && this.aura.getTarget() != null) {
                Tracers tracers = (Tracers)Client.moduleManager.getModule(Tracers.class);
                if (tracers == null) {
                    return;
                }
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glEnable((int)3042);
                GL11.glEnable((int)2848);
                GL11.glLineWidth((float)((Float)this.thicknessValue.get()).floatValue());
                GL11.glDisable((int)3553);
                GL11.glDisable((int)2929);
                GL11.glDepthMask((boolean)false);
                GL11.glBegin((int)1);
                int dist = (int)(TargetMark.mc.field_71439_g.func_70032_d((Entity)this.aura.getTarget()) * 2.0f);
                if (dist > 255) {
                    dist = 255;
                }
                tracers.drawTraces((Entity)this.aura.getTarget(), this.getColor((Entity)this.aura.getTarget()), false);
                GL11.glEnd();
                GL11.glEnable((int)3553);
                GL11.glDisable((int)2848);
                GL11.glEnable((int)2929);
                GL11.glDepthMask((boolean)true);
                GL11.glDisable((int)3042);
                GlStateManager.func_179117_G();
            }
        } else if (!((String)this.aura.getTargetModeValue().get()).equalsIgnoreCase("multi") && this.aura.getTarget() != null) {
            RenderUtils.drawEntityBox((Entity)this.aura.getTarget(), this.aura.getHitable() ? ColorUtils.reAlpha(this.getColor((Entity)this.entity), (Integer)this.colorAlphaValue.get()) : new Color(255, 0, 0, (Integer)this.colorAlphaValue.get()), false);
        }
    }

    public final Color getColor(Entity ent) {
        if (ent instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)ent;
            if (((Boolean)this.colorTeam.get()).booleanValue()) {
                char[] chars = entityLivingBase.func_145748_c_().func_150254_d().toCharArray();
                int color = Integer.MAX_VALUE;
                for (int i = 0; i < chars.length; ++i) {
                    int index;
                    if (chars[i] != '\u00a7' || i + 1 >= chars.length || (index = GameFontRenderer.getColorIndex(chars[i + 1])) < 0 || index > 15) continue;
                    color = ColorUtils.hexColors[index];
                    break;
                }
                return new Color(color);
            }
        }
        switch ((String)this.colorModeValue.get()) {
            case "Custom": {
                return new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get());
            }
            case "Rainbow": {
                return new Color(RenderUtils.getRainbowOpaque((Integer)this.mixerSecondsValue.get(), ((Float)this.saturationValue.get()).floatValue(), ((Float)this.brightnessValue.get()).floatValue(), 0));
            }
            case "Sky": {
                return RenderUtils.skyRainbow(0, ((Float)this.saturationValue.get()).floatValue(), ((Float)this.brightnessValue.get()).floatValue());
            }
            case "LiquidSlowly": {
                return ColorUtils.LiquidSlowly(System.nanoTime(), 0, ((Float)this.saturationValue.get()).floatValue(), ((Float)this.brightnessValue.get()).floatValue());
            }
            case "Mixer": {
                return ColorMixer.getMixedColor(0, (Integer)this.mixerSecondsValue.get());
            }
        }
        return ColorUtils.fade(new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get()), 0, 100);
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

    private void drawCircle(double x, double y, double z, float width, double radius, float red2, float green2, float blue2, float alp) {
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)2);
        GL11.glColor4f((float)red2, (float)green2, (float)blue2, (float)alp);
        for (int i = 0; i <= 360; ++i) {
            double posX = x - Math.sin((double)i * Math.PI / 180.0) * radius;
            double posZ = z + Math.cos((double)i * Math.PI / 180.0) * radius;
            GL11.glVertex3d((double)posX, (double)y, (double)posZ);
        }
        GL11.glEnd();
    }

    private double easeInOutQuart(double x) {
        return x < 0.5 ? 8.0 * x * x * x * x : 1.0 - Math.pow(-2.0 * x + 2.0, 4.0) / 2.0;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

