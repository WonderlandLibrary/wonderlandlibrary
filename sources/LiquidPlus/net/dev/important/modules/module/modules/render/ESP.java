/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.Timer
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.vector.Matrix4f
 *  org.lwjgl.util.vector.Vector2f
 *  org.lwjgl.util.vector.Vector3f
 */
package net.dev.important.modules.module.modules.render;

import java.awt.Color;
import java.text.DecimalFormat;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render2DEvent;
import net.dev.important.event.Render3DEvent;
import net.dev.important.gui.font.GameFontRenderer;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.color.ColorMixer;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.render.BlendUtils;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.utils.render.WorldToScreen;
import net.dev.important.utils.render.shader.FramebufferShader;
import net.dev.important.utils.render.shader.shaders.GlowShader;
import net.dev.important.utils.render.shader.shaders.OutlineShader;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

@Info(name="ESP", description="Allows you to see targets through walls.", category=Category.RENDER, cnName="\u900f\u89c6")
public class ESP
extends Module {
    private DecimalFormat decimalFormat = new DecimalFormat("0.0");
    public static boolean renderNameTags = true;
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Box", "OtherBox", "WireFrame", "2D", "Real2D", "Outline", "ShaderOutline", "ShaderGlow"}, "Box");
    public final BoolValue real2dcsgo = new BoolValue("2D-CSGOStyle", true, () -> ((String)this.modeValue.get()).equalsIgnoreCase("real2d"));
    public final BoolValue real2dShowHealth = new BoolValue("2D-ShowHealth", true, () -> ((String)this.modeValue.get()).equalsIgnoreCase("real2d"));
    public final BoolValue real2dShowHeldItem = new BoolValue("2D-ShowHeldItem", true, () -> ((String)this.modeValue.get()).equalsIgnoreCase("real2d"));
    public final BoolValue real2dShowName = new BoolValue("2D-ShowEntityName", true, () -> ((String)this.modeValue.get()).equalsIgnoreCase("real2d"));
    public final BoolValue real2dOutline = new BoolValue("2D-Outline", true, () -> ((String)this.modeValue.get()).equalsIgnoreCase("real2d"));
    public final FloatValue outlineWidth = new FloatValue("Outline-Width", 3.0f, 0.5f, 5.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("outline"));
    public final FloatValue wireframeWidth = new FloatValue("WireFrame-Width", 2.0f, 0.5f, 5.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("wireframe"));
    private final FloatValue shaderOutlineRadius = new FloatValue("ShaderOutline-Radius", 1.35f, 1.0f, 2.0f, "x", () -> ((String)this.modeValue.get()).equalsIgnoreCase("shaderoutline"));
    private final FloatValue shaderGlowRadius = new FloatValue("ShaderGlow-Radius", 2.3f, 2.0f, 3.0f, "x", () -> ((String)this.modeValue.get()).equalsIgnoreCase("shaderglow"));
    private final ListValue colorModeValue = new ListValue("Color", new String[]{"Custom", "Health", "Rainbow", "Sky", "LiquidSlowly", "Fade", "Mixer"}, "Custom");
    private final IntegerValue colorRedValue = new IntegerValue("Red", 255, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("Green", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("Blue", 255, 0, 255);
    private final FloatValue saturationValue = new FloatValue("Saturation", 1.0f, 0.0f, 1.0f);
    private final FloatValue brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
    private final IntegerValue mixerSecondsValue = new IntegerValue("Seconds", 2, 1, 10);
    private final BoolValue colorTeam = new BoolValue("Team", false);

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        String mode = (String)this.modeValue.get();
        Matrix4f mvMatrix = WorldToScreen.getMatrix(2982);
        Matrix4f projectionMatrix = WorldToScreen.getMatrix(2983);
        boolean real2d = mode.equalsIgnoreCase("real2d");
        if (real2d) {
            GL11.glPushAttrib((int)8192);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glMatrixMode((int)5889);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glOrtho((double)0.0, (double)ESP.mc.field_71443_c, (double)ESP.mc.field_71440_d, (double)0.0, (double)-1.0, (double)1.0);
            GL11.glMatrixMode((int)5888);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glDisable((int)2929);
            GL11.glBlendFunc((int)770, (int)771);
            GlStateManager.func_179098_w();
            GlStateManager.func_179132_a((boolean)true);
            GL11.glLineWidth((float)1.0f);
        }
        for (Entity entity : ESP.mc.field_71441_e.field_72996_f) {
            if (entity == null || entity == ESP.mc.field_71439_g || !EntityUtils.isSelected(entity, false) || !RenderUtils.isInViewFrustrum(entity)) continue;
            EntityLivingBase entityLiving = (EntityLivingBase)entity;
            Color color = this.getColor((Entity)entityLiving);
            switch (mode.toLowerCase()) {
                case "box": 
                case "otherbox": {
                    RenderUtils.drawEntityBox(entity, color, !mode.equalsIgnoreCase("otherbox"));
                    break;
                }
                case "2d": {
                    RenderManager renderManager = mc.func_175598_ae();
                    Timer timer = ESP.mc.field_71428_T;
                    double posX = entityLiving.field_70142_S + (entityLiving.field_70165_t - entityLiving.field_70142_S) * (double)timer.field_74281_c - renderManager.field_78725_b;
                    double posY = entityLiving.field_70137_T + (entityLiving.field_70163_u - entityLiving.field_70137_T) * (double)timer.field_74281_c - renderManager.field_78726_c;
                    double posZ = entityLiving.field_70136_U + (entityLiving.field_70161_v - entityLiving.field_70136_U) * (double)timer.field_74281_c - renderManager.field_78723_d;
                    RenderUtils.draw2D(entityLiving, posX, posY, posZ, color.getRGB(), Color.BLACK.getRGB());
                    break;
                }
                case "real2d": {
                    RenderManager renderManager = mc.func_175598_ae();
                    Timer timer = ESP.mc.field_71428_T;
                    AxisAlignedBB bb = entityLiving.func_174813_aQ().func_72317_d(-entityLiving.field_70165_t, -entityLiving.field_70163_u, -entityLiving.field_70161_v).func_72317_d(entityLiving.field_70142_S + (entityLiving.field_70165_t - entityLiving.field_70142_S) * (double)timer.field_74281_c, entityLiving.field_70137_T + (entityLiving.field_70163_u - entityLiving.field_70137_T) * (double)timer.field_74281_c, entityLiving.field_70136_U + (entityLiving.field_70161_v - entityLiving.field_70136_U) * (double)timer.field_74281_c).func_72317_d(-renderManager.field_78725_b, -renderManager.field_78726_c, -renderManager.field_78723_d);
                    double[][] boxVertices = new double[][]{{bb.field_72340_a, bb.field_72338_b, bb.field_72339_c}, {bb.field_72340_a, bb.field_72337_e, bb.field_72339_c}, {bb.field_72336_d, bb.field_72337_e, bb.field_72339_c}, {bb.field_72336_d, bb.field_72338_b, bb.field_72339_c}, {bb.field_72340_a, bb.field_72338_b, bb.field_72334_f}, {bb.field_72340_a, bb.field_72337_e, bb.field_72334_f}, {bb.field_72336_d, bb.field_72337_e, bb.field_72334_f}, {bb.field_72336_d, bb.field_72338_b, bb.field_72334_f}};
                    float minX = ESP.mc.field_71443_c;
                    float minY = ESP.mc.field_71440_d;
                    float maxX = 0.0f;
                    float maxY = 0.0f;
                    for (double[] boxVertex : boxVertices) {
                        Vector2f screenPos = WorldToScreen.worldToScreen(new Vector3f((float)boxVertex[0], (float)boxVertex[1], (float)boxVertex[2]), mvMatrix, projectionMatrix, ESP.mc.field_71443_c, ESP.mc.field_71440_d);
                        if (screenPos == null) continue;
                        minX = Math.min(screenPos.x, minX);
                        minY = Math.min(screenPos.y, minY);
                        maxX = Math.max(screenPos.x, maxX);
                        maxY = Math.max(screenPos.y, maxY);
                    }
                    if (minX >= (float)ESP.mc.field_71443_c || minY >= (float)ESP.mc.field_71440_d || maxX <= 0.0f || maxY <= 0.0f) break;
                    if (((Boolean)this.real2dOutline.get()).booleanValue()) {
                        GL11.glLineWidth((float)2.0f);
                        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                        if (((Boolean)this.real2dcsgo.get()).booleanValue()) {
                            float distX = (maxX - minX) / 3.0f;
                            float distY = (maxY - minY) / 3.0f;
                            GL11.glBegin((int)3);
                            GL11.glVertex2f((float)minX, (float)(minY + distY));
                            GL11.glVertex2f((float)minX, (float)minY);
                            GL11.glVertex2f((float)(minX + distX), (float)minY);
                            GL11.glEnd();
                            GL11.glBegin((int)3);
                            GL11.glVertex2f((float)minX, (float)(maxY - distY));
                            GL11.glVertex2f((float)minX, (float)maxY);
                            GL11.glVertex2f((float)(minX + distX), (float)maxY);
                            GL11.glEnd();
                            GL11.glBegin((int)3);
                            GL11.glVertex2f((float)(maxX - distX), (float)minY);
                            GL11.glVertex2f((float)maxX, (float)minY);
                            GL11.glVertex2f((float)maxX, (float)(minY + distY));
                            GL11.glEnd();
                            GL11.glBegin((int)3);
                            GL11.glVertex2f((float)(maxX - distX), (float)maxY);
                            GL11.glVertex2f((float)maxX, (float)maxY);
                            GL11.glVertex2f((float)maxX, (float)(maxY - distY));
                            GL11.glEnd();
                        } else {
                            GL11.glBegin((int)2);
                            GL11.glVertex2f((float)minX, (float)minY);
                            GL11.glVertex2f((float)minX, (float)maxY);
                            GL11.glVertex2f((float)maxX, (float)maxY);
                            GL11.glVertex2f((float)maxX, (float)minY);
                            GL11.glEnd();
                        }
                        GL11.glLineWidth((float)1.0f);
                    }
                    GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)1.0f);
                    if (((Boolean)this.real2dcsgo.get()).booleanValue()) {
                        float distX = (maxX - minX) / 3.0f;
                        float distY = (maxY - minY) / 3.0f;
                        GL11.glBegin((int)3);
                        GL11.glVertex2f((float)minX, (float)(minY + distY));
                        GL11.glVertex2f((float)minX, (float)minY);
                        GL11.glVertex2f((float)(minX + distX), (float)minY);
                        GL11.glEnd();
                        GL11.glBegin((int)3);
                        GL11.glVertex2f((float)minX, (float)(maxY - distY));
                        GL11.glVertex2f((float)minX, (float)maxY);
                        GL11.glVertex2f((float)(minX + distX), (float)maxY);
                        GL11.glEnd();
                        GL11.glBegin((int)3);
                        GL11.glVertex2f((float)(maxX - distX), (float)minY);
                        GL11.glVertex2f((float)maxX, (float)minY);
                        GL11.glVertex2f((float)maxX, (float)(minY + distY));
                        GL11.glEnd();
                        GL11.glBegin((int)3);
                        GL11.glVertex2f((float)(maxX - distX), (float)maxY);
                        GL11.glVertex2f((float)maxX, (float)maxY);
                        GL11.glVertex2f((float)maxX, (float)(maxY - distY));
                        GL11.glEnd();
                    } else {
                        GL11.glBegin((int)2);
                        GL11.glVertex2f((float)minX, (float)minY);
                        GL11.glVertex2f((float)minX, (float)maxY);
                        GL11.glVertex2f((float)maxX, (float)maxY);
                        GL11.glVertex2f((float)maxX, (float)minY);
                        GL11.glEnd();
                    }
                    if (((Boolean)this.real2dShowHealth.get()).booleanValue()) {
                        float barHeight = (maxY - minY) * (1.0f - entityLiving.func_110143_aJ() / entityLiving.func_110138_aP());
                        GL11.glColor4f((float)0.1f, (float)1.0f, (float)0.1f, (float)1.0f);
                        GL11.glBegin((int)7);
                        GL11.glVertex2f((float)(maxX + 2.0f), (float)(minY + barHeight));
                        GL11.glVertex2f((float)(maxX + 2.0f), (float)maxY);
                        GL11.glVertex2f((float)(maxX + 4.0f), (float)maxY);
                        GL11.glVertex2f((float)(maxX + 4.0f), (float)(minY + barHeight));
                        GL11.glEnd();
                        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                        GL11.glEnable((int)3553);
                        GL11.glEnable((int)2929);
                        ESP.mc.field_71466_p.func_175063_a(this.decimalFormat.format(entityLiving.func_110143_aJ()) + " HP", maxX + 4.0f, minY + barHeight, -1);
                        GL11.glDisable((int)3553);
                        GL11.glDisable((int)2929);
                        GlStateManager.func_179117_G();
                    }
                    if (((Boolean)this.real2dShowHeldItem.get()).booleanValue() && entityLiving.func_70694_bm() != null && entityLiving.func_70694_bm().func_77973_b() != null) {
                        GL11.glEnable((int)3553);
                        GL11.glEnable((int)2929);
                        int stringWidth = ESP.mc.field_71466_p.func_78256_a(entityLiving.func_70694_bm().func_82833_r());
                        ESP.mc.field_71466_p.func_175063_a(entityLiving.func_70694_bm().func_82833_r(), minX + (maxX - minX) / 2.0f - (float)(stringWidth / 2), maxY + 2.0f, -1);
                        GL11.glDisable((int)3553);
                        GL11.glDisable((int)2929);
                    }
                    if (!((Boolean)this.real2dShowName.get()).booleanValue()) break;
                    GL11.glEnable((int)3553);
                    GL11.glEnable((int)2929);
                    int stringWidth = ESP.mc.field_71466_p.func_78256_a(entityLiving.func_145748_c_().func_150254_d());
                    ESP.mc.field_71466_p.func_175063_a(entityLiving.func_145748_c_().func_150254_d(), minX + (maxX - minX) / 2.0f - (float)(stringWidth / 2), minY - 12.0f, -1);
                    GL11.glDisable((int)3553);
                    GL11.glDisable((int)2929);
                    break;
                }
            }
        }
        if (real2d) {
            GL11.glEnable((int)2929);
            GL11.glMatrixMode((int)5889);
            GL11.glPopMatrix();
            GL11.glMatrixMode((int)5888);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        OutlineShader shader;
        String mode = ((String)this.modeValue.get()).toLowerCase();
        FramebufferShader framebufferShader = mode.equalsIgnoreCase("shaderoutline") ? OutlineShader.OUTLINE_SHADER : (shader = mode.equalsIgnoreCase("shaderglow") ? GlowShader.GLOW_SHADER : null);
        if (shader == null) {
            return;
        }
        shader.startDraw(event.getPartialTicks());
        renderNameTags = false;
        try {
            for (Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (!EntityUtils.isSelected(entity, false)) continue;
                mc.func_175598_ae().func_147936_a(entity, ESP.mc.field_71428_T.field_74281_c, true);
            }
        }
        catch (Exception ex) {
            ClientUtils.getLogger().error("An error occurred while rendering all entities for shader esp", (Throwable)ex);
        }
        renderNameTags = true;
        float radius = mode.equalsIgnoreCase("shaderoutline") ? ((Float)this.shaderOutlineRadius.get()).floatValue() : (mode.equalsIgnoreCase("shaderglow") ? ((Float)this.shaderGlowRadius.get()).floatValue() : 1.0f);
        shader.stopDraw(this.getColor(null), radius, 1.0f);
    }

    public final Color getColor(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            if (((String)this.colorModeValue.get()).equalsIgnoreCase("Health")) {
                return BlendUtils.getHealthColor(entityLivingBase.func_110143_aJ(), entityLivingBase.func_110138_aP());
            }
            if (entityLivingBase.field_70737_aN > 0) {
                return Color.RED;
            }
            if (EntityUtils.isFriend((Entity)entityLivingBase)) {
                return Color.BLUE;
            }
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
            case "Fade": {
                return ColorUtils.fade(new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get()), 0, 100);
            }
        }
        return Color.white;
    }
}

