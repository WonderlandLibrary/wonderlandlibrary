/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.Timer
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 *  org.lwjgl.util.vector.Matrix4f
 *  org.lwjgl.util.vector.Vector2f
 *  org.lwjgl.util.vector.Vector3f
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.render.NameTags;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.Color.Colors;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtil;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.ESPUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils3;
import net.ccbluex.liquidbounce.utils.render.WorldToScreen;
import net.ccbluex.liquidbounce.utils.render.shader.FramebufferShader;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

@ModuleInfo(name="ESP", description="Allows you to see targets through walls.", category=ModuleCategory.RENDER)
public class ESP
extends Module {
    private DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private DecimalFormat Y_FORMAT = new DecimalFormat("0.000000");
    public static Map<EntityLivingBase, double[]> entityPositionstop = new HashMap<EntityLivingBase, double[]>();
    public static Map<EntityLivingBase, double[]> entityPositionsbottom = new HashMap<EntityLivingBase, double[]>();
    public static boolean renderNameTags = true;
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Box", "OtherBox", "WireFrame", "2D", "NewBox", "Real2D", "2DBox", "Outline", "CSGO"}, "Box");
    public final FloatValue outlineWidth = new FloatValue("Outline-Width", 3.0f, 0.5f, 5.0f);
    public final FloatValue wireframeWidth = new FloatValue("WireFrame-Width", 2.0f, 0.5f, 5.0f);
    private final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private final IntegerValue slicesValue = new IntegerValue("S", 8, 2, 64);
    private final IntegerValue rangeValue = new IntegerValue("Range", 1, -3, 3);
    public final BoolValue real2dcsgo = new BoolValue("2D-CSGOStyle", true);
    public final BoolValue real2dShowHealth = new BoolValue("2D-ShowHealth", true);
    public final BoolValue real2dShowHeldItem = new BoolValue("2D-ShowHeldItem", true);
    public final BoolValue real2dShowName = new BoolValue("2D-ShowEntityName", true);
    public final BoolValue real2dOutline = new BoolValue("2D-Outline", true);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    private final BoolValue colorTeam = new BoolValue("Team", false);
    private final BoolValue armor = new BoolValue("Armor Bar", true);
    private final BoolValue names = new BoolValue("Names", true);
    public int[] ColorCode = new int[32];
    private double gradualFOVModifier;

    public static double getIncremental(double val, double inc) {
        double one = 1.0 / inc;
        return (double)Math.round(val * one) / one;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        Color color = null;
        if (fractions == null) throw new IllegalArgumentException("Fractions can't be null");
        if (colors == null) throw new IllegalArgumentException("Colours can't be null");
        if (fractions.length != colors.length) throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        int[] indicies = ESP.getFractionIndicies(fractions, progress);
        if (indicies[0] < 0) return colors[0];
        if (indicies[0] >= fractions.length) return colors[0];
        if (indicies[1] < 0) return colors[0];
        if (indicies[1] >= fractions.length) {
            return colors[0];
        }
        float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
        Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
        float max = range[1] - range[0];
        float value = progress - range[0];
        float weight = value / max;
        return ESP.blend(colorRange[0], colorRange[1], 1.0f - weight);
    }

    public static int[] getFractionIndicies(float[] fractions, float progress) {
        int startPoint;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color = null;
        try {
            color = new Color(red, green, blue);
        }
        catch (IllegalArgumentException exp) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            System.out.println(nf.format(red) + "; " + nf.format(green) + "; " + nf.format(blue));
            exp.printStackTrace();
        }
        return color;
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        boolean csgo2;
        String mode = (String)this.modeValue.get();
        Matrix4f mvMatrix = WorldToScreen.getMatrix(2982);
        Matrix4f projectionMatrix = WorldToScreen.getMatrix(2983);
        entityPositionstop.clear();
        entityPositionsbottom.clear();
        float pTicks = ESP.mc.field_71428_T.field_74281_c;
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
        if (csgo2 = mode.equalsIgnoreCase("csgo")) {
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
            if (entity == null || entity == ESP.mc.field_71439_g || !EntityUtils.isSelected(entity, false)) continue;
            double posX = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)event.getPartialTicks() - ESP.mc.func_175598_ae().field_78725_b;
            double posY = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)event.getPartialTicks() - ESP.mc.func_175598_ae().field_78726_c;
            double posZ = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)event.getPartialTicks() - ESP.mc.func_175598_ae().field_78723_d;
            EntityLivingBase entityLiving = (EntityLivingBase)entity;
            Color color = this.getColor((Entity)entityLiving);
            switch (mode.toLowerCase()) {
                case "box": 
                case "milins": {
                    ESPUtils.cylinder(entity, posX, posY, posZ, ((Integer)this.rangeValue.get()).intValue(), (Integer)this.slicesValue.get());
                    ESPUtils.shadow(entity, posX, posY, posZ, ((Integer)this.rangeValue.get()).intValue(), (Integer)this.slicesValue.get());
                    break;
                }
                case "otherbox": {
                    RenderUtils.drawEntityBox(entity, this.getColor((Entity)entityLiving), !mode.equalsIgnoreCase("otherbox"));
                    break;
                }
                case "2d": {
                    RenderManager renderManager = mc.func_175598_ae();
                    Timer timer = ESP.mc.field_71428_T;
                    double posXa = entityLiving.field_70142_S + (entityLiving.field_70165_t - entityLiving.field_70142_S) * (double)timer.field_74281_c - renderManager.field_78725_b;
                    double posYa = entityLiving.field_70137_T + (entityLiving.field_70163_u - entityLiving.field_70137_T) * (double)timer.field_74281_c - renderManager.field_78726_c;
                    double posZa = entityLiving.field_70136_U + (entityLiving.field_70161_v - entityLiving.field_70136_U) * (double)timer.field_74281_c - renderManager.field_78723_d;
                    RenderUtils.draw2D(entityLiving, posXa, posYa, posZa, this.getColor((Entity)entityLiving).getRGB(), Color.BLACK.getRGB());
                    break;
                }
                case "csgo": {
                    Vector2f screenPos;
                    RenderManager renderManager = mc.func_175598_ae();
                    Timer timer = ESP.mc.field_71428_T;
                    AxisAlignedBB bb = entityLiving.func_174813_aQ().func_72317_d(-entityLiving.field_70165_t, -entityLiving.field_70163_u, -entityLiving.field_70161_v).func_72317_d(entityLiving.field_70142_S + (entityLiving.field_70165_t - entityLiving.field_70142_S) * (double)timer.field_74281_c, entityLiving.field_70137_T + (entityLiving.field_70163_u - entityLiving.field_70137_T) * (double)timer.field_74281_c, entityLiving.field_70136_U + (entityLiving.field_70161_v - entityLiving.field_70136_U) * (double)timer.field_74281_c).func_72317_d(-renderManager.field_78725_b, -renderManager.field_78726_c, -renderManager.field_78723_d);
                    double[][] boxVertices = new double[][]{{bb.field_72340_a, bb.field_72338_b, bb.field_72339_c}, {bb.field_72340_a, bb.field_72337_e, bb.field_72339_c}, {bb.field_72336_d, bb.field_72337_e, bb.field_72339_c}, {bb.field_72336_d, bb.field_72338_b, bb.field_72339_c}, {bb.field_72340_a, bb.field_72338_b, bb.field_72334_f}, {bb.field_72340_a, bb.field_72337_e, bb.field_72334_f}, {bb.field_72336_d, bb.field_72337_e, bb.field_72334_f}, {bb.field_72336_d, bb.field_72338_b, bb.field_72334_f}};
                    float minX = ESP.mc.field_71443_c;
                    float minY = ESP.mc.field_71440_d;
                    float maxX = 0.0f;
                    float maxY = 0.0f;
                    for (double[] boxVertex : boxVertices) {
                        screenPos = WorldToScreen.worldToScreen(new Vector3f((float)boxVertex[0], (float)boxVertex[1], (float)boxVertex[2]), mvMatrix, projectionMatrix, ESP.mc.field_71443_c, ESP.mc.field_71440_d);
                        if (screenPos == null) continue;
                        minX = Math.min(screenPos.x, minX);
                        minY = Math.min(screenPos.y, minY);
                        maxX = Math.max(screenPos.x, maxX);
                        maxY = Math.max(screenPos.y, maxY);
                    }
                    if (minX >= (float)ESP.mc.field_71443_c || minY >= (float)ESP.mc.field_71440_d || maxX <= 0.0f || maxY <= 0.0f) break;
                    if (((Boolean)this.real2dOutline.get()).booleanValue()) {
                        GL11.glLineWidth((float)1.5f);
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
                        GL11.glVertex2f((float)(maxX + 3.0f), (float)maxY);
                        GL11.glVertex2f((float)(maxX + 3.0f), (float)(minY + barHeight));
                        GL11.glEnd();
                        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                        GL11.glEnable((int)3553);
                        GL11.glEnable((int)2929);
                        ESP.mc.field_71466_p.func_175063_a(this.decimalFormat.format(entityLiving.func_110143_aJ()) + "\u00a7c\u2764", maxX + 4.0f, minY + barHeight, -1);
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
                case "2dbox": {
                    GL11.glPushMatrix();
                    GL11.glColor4d((double)0.75, (double)0.0, (double)0.0, (double)0.0);
                    double size = 0.5;
                    double boundindY = entity.func_174813_aQ().field_72337_e - entity.func_174813_aQ().field_72338_b + 0.2;
                    ESPUtils.drawBoundingBox(new AxisAlignedBB(posX - size, posY, posZ - size, posX + size, posY + boundindY, posZ + size));
                    ESPUtils.renderOne();
                    ESPUtils.drawBoundingBox(new AxisAlignedBB(posX - size, posY, posZ - size, posX + size, posY + boundindY, posZ + size));
                    GL11.glStencilFunc((int)512, (int)0, (int)15);
                    GL11.glStencilOp((int)7681, (int)7681, (int)7681);
                    GL11.glPolygonMode((int)1032, (int)6914);
                    ESPUtils.drawBoundingBox(new AxisAlignedBB(posX - size, posY, posZ - size, posX + size, posY + boundindY, posZ + size));
                    GL11.glStencilFunc((int)514, (int)1, (int)15);
                    GL11.glStencilOp((int)7680, (int)7680, (int)7680);
                    GL11.glPolygonMode((int)1032, (int)6913);
                    ESPUtils.setColor((EntityLivingBase)entity);
                    ESPUtils.drawBoundingBox(new AxisAlignedBB(posX - size, posY, posZ - size, posX + size, posY + boundindY, posZ + size));
                    GL11.glPolygonOffset((float)1.0f, (float)2000000.0f);
                    GL11.glDisable((int)10754);
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                    GL11.glDisable((int)2960);
                    GL11.glDisable((int)2848);
                    GL11.glHint((int)3154, (int)4352);
                    GL11.glEnable((int)3042);
                    GL11.glEnable((int)2896);
                    GL11.glEnable((int)3553);
                    GL11.glEnable((int)3008);
                    GL11.glPopAttrib();
                    GL11.glColor4d((double)0.6, (double)0.0, (double)0.0, (double)1.0);
                    GL11.glPopMatrix();
                }
                case "newbox": {
                    double x = entityLiving.field_70142_S + (entityLiving.field_70165_t + 10.0 - (entityLiving.field_70142_S + 10.0)) * (double)pTicks - ESP.mc.func_175598_ae().field_78730_l;
                    double y = entityLiving.field_70137_T + (entityLiving.field_70163_u - entityLiving.field_70137_T) * (double)pTicks - ESP.mc.func_175598_ae().field_78731_m;
                    double z = entityLiving.field_70136_U + (entityLiving.field_70161_v + 10.0 - (entityLiving.field_70136_U + 10.0)) * (double)pTicks - ESP.mc.func_175598_ae().field_78728_n;
                    double[] convertedPoints = this.convertTo2D(x, y += (double)entityLiving.field_70131_O + 0.5, z);
                    double xd = Math.abs(this.convertTo2D(x, y + 1.0, z, (Entity)entityLiving)[1] - this.convertTo2D(x, y, z, (Entity)entityLiving)[1]);
                    assert (convertedPoints != null);
                    if (!(convertedPoints[2] >= 0.0) || !(convertedPoints[2] < 1.0)) break;
                    entityPositionstop.put(entityLiving, new double[]{convertedPoints[0], convertedPoints[1], xd, convertedPoints[2]});
                    y = entityLiving.field_70137_T + (entityLiving.field_70163_u - 2.2 - (entityLiving.field_70137_T - 2.2)) * (double)pTicks - ESP.mc.func_175598_ae().field_78731_m;
                    entityPositionsbottom.put(entityLiving, new double[]{this.convertTo2D(x, y, z)[0], this.convertTo2D(x, y, z)[1], xd, this.convertTo2D(x, y, z)[2]});
                    break;
                }
                case "real2d": {
                    Vector2f screenPos;
                    RenderManager renderManager = mc.func_175598_ae();
                    Timer timer = ESP.mc.field_71428_T;
                    AxisAlignedBB bb = entityLiving.func_174813_aQ().func_72317_d(-entityLiving.field_70165_t, -entityLiving.field_70163_u, -entityLiving.field_70161_v).func_72317_d(entityLiving.field_70142_S + (entityLiving.field_70165_t - entityLiving.field_70142_S) * (double)timer.field_74281_c, entityLiving.field_70137_T + (entityLiving.field_70163_u - entityLiving.field_70137_T) * (double)timer.field_74281_c, entityLiving.field_70136_U + (entityLiving.field_70161_v - entityLiving.field_70136_U) * (double)timer.field_74281_c).func_72317_d(-renderManager.field_78725_b, -renderManager.field_78726_c, -renderManager.field_78723_d);
                    double[][] boxVertices = new double[][]{{bb.field_72340_a, bb.field_72338_b, bb.field_72339_c}, {bb.field_72340_a, bb.field_72337_e, bb.field_72339_c}, {bb.field_72336_d, bb.field_72337_e, bb.field_72339_c}, {bb.field_72336_d, bb.field_72338_b, bb.field_72339_c}, {bb.field_72340_a, bb.field_72338_b, bb.field_72334_f}, {bb.field_72340_a, bb.field_72337_e, bb.field_72334_f}, {bb.field_72336_d, bb.field_72337_e, bb.field_72334_f}, {bb.field_72336_d, bb.field_72338_b, bb.field_72334_f}};
                    float minX = Float.MAX_VALUE;
                    float minY = Float.MAX_VALUE;
                    float maxX = -1.0f;
                    float maxY = -1.0f;
                    for (double[] boxVertex : boxVertices) {
                        screenPos = WorldToScreen.worldToScreen(new Vector3f((float)boxVertex[0], (float)boxVertex[1], (float)boxVertex[2]), mvMatrix, projectionMatrix, ESP.mc.field_71443_c, ESP.mc.field_71440_d);
                        if (screenPos == null) continue;
                        minX = Math.min(screenPos.x, minX);
                        minY = Math.min(screenPos.y, minY);
                        maxX = Math.max(screenPos.x, maxX);
                        maxY = Math.max(screenPos.y, maxY);
                    }
                    if (!(minX > 0.0f || minY > 0.0f || maxX <= (float)ESP.mc.field_71443_c) && !(maxY <= (float)ESP.mc.field_71443_c)) break;
                    GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)1.0f);
                    GL11.glBegin((int)2);
                    GL11.glVertex2f((float)minX, (float)minY);
                    GL11.glVertex2f((float)minX, (float)maxY);
                    GL11.glVertex2f((float)maxX, (float)maxY);
                    GL11.glVertex2f((float)maxX, (float)minY);
                    GL11.glEnd();
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
        String mode = (String)this.modeValue.get();
        switch (mode.toLowerCase()) {
            case "newbox": {
                this.renderOther2d();
            }
        }
    }

    @EventTarget
    private void renderShaderEsp(Render2DEvent event, FramebufferShader shader, float radius) {
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
        shader.stopDraw(this.getColor(null), radius, 1.0f);
    }

    private void renderOther2d() {
        GlStateManager.func_179094_E();
        ScaledResolution scaledRes = new ScaledResolution(mc);
        double twoDscale = (double)scaledRes.func_78325_e() / Math.pow(scaledRes.func_78325_e(), 2.0);
        GL11.glScaled((double)twoDscale, (double)twoDscale, (double)twoDscale);
        for (EntityLivingBase ent : entityPositionstop.keySet()) {
            if (ent == null || !(ent instanceof EntityLivingBase) || ent == ESP.mc.field_71439_g || !EntityUtils.isSelected((Entity)ent, false)) continue;
            this.renderOther2dEntity(ent);
        }
        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179121_F();
    }

    private void renderOther2dEntity(EntityLivingBase ent) {
        double[] renderPositions = entityPositionstop.get(ent);
        double[] renderPositionsBottom = entityPositionsbottom.get(ent);
        if (renderPositions[3] > 0.0 || renderPositions[3] <= 1.0) {
            GlStateManager.func_179094_E();
            this.scale((Entity)ent);
            float y = (float)renderPositions[1];
            float endy = (float)renderPositionsBottom[1];
            float meme = endy - y;
            float x = (float)renderPositions[0] - meme / 4.0f;
            float endx = (float)renderPositionsBottom[0] + meme / 4.0f;
            if (x > endx) {
                endx = x;
                x = (float)renderPositionsBottom[0] + meme / 4.0f;
            }
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
            GlStateManager.func_179121_F();
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            int color = Color.white.getRGB();
            double xDiff = (double)(endx - x) / 1.0;
            double x2Diff = (double)(endx - x) / 1.1;
            double yDiff = (double)(endy - y) / 1.0;
            this.renderOtherBox(y, endy, x, endx, color, x2Diff, yDiff);
            if (ent instanceof EntityPlayer) {
                if (!LiquidBounce.moduleManager.getModule(NameTags.class).getState() && ((Boolean)this.names.get()).booleanValue() || !LiquidBounce.moduleManager.getModule(NameTags.class).getState() && ((Boolean)this.names.get()).booleanValue()) {
                    GlStateManager.func_179094_E();
                    String renderName = ent.func_70005_c_();
                    FontRenderer font = ESP.mc.field_71466_p;
                    float meme2 = (endx - x) / 2.0f - (float)font.func_78256_a(renderName) / 2.0f;
                    font.func_175063_a(renderName + " " + (int)ESP.mc.field_71439_g.func_70032_d((Entity)ent) + "m", x + meme2, y - (float)font.field_78288_b - 5.0f, -1);
                    GlStateManager.func_179121_F();
                }
                if (((Boolean)this.armor.get()).booleanValue()) {
                    this.renderOtherArmor((EntityPlayer)ent, y, endy, endx);
                }
            }
            float health = ent.func_110143_aJ();
            float[] fractions = new float[]{0.0f, 0.4f, 0.9f};
            Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
            float progress = health / ent.func_110138_aP();
            Color customColor = health >= 0.0f ? ESP.blendColors(fractions, colors, progress).brighter() : Color.RED;
            double difference = (double)(y - endy) + 0.5;
            double healthLocation = (double)endy + difference * (double)progress;
            RenderUtils3.drawRectBordered((double)x - 6.5, (double)y - 0.5, (double)x - 2.5, endy, 1.0, Colors.getColor(0, 100), Colors.getColor(0, 150));
            RenderUtils3.rectangle((double)x - 5.5, (double)endy - 1.0, (double)x - 3.5, healthLocation, customColor.getRGB());
            if (-difference > 50.0) {
                for (int i = 1; i < 10; ++i) {
                    double dThing = difference / 10.0 * (double)i;
                    RenderUtils3.rectangle((double)x - 6.5, (double)endy - 0.5 + dThing, (double)x - 2.5, (double)endy - 0.5 + dThing - 1.0, Colors.getColor(0));
                }
            }
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a((float)1.0f, (float)1.0f, (float)1.0f);
            String nigger = (int)ESP.getIncremental(progress * 20.0f, 1.0) + "\u00a7c\u2764";
            ESP.mc.field_71466_p.func_175063_a(nigger + "", x - 30.0f, (float)healthLocation - (float)(ESP.mc.field_71466_p.field_78288_b / 2), -1);
            GlStateManager.func_179121_F();
            GlStateManager.func_179121_F();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    private void renderOtherArmor(EntityPlayer ent, float y, float endy, float endx) {
        ItemStack stack4;
        ItemStack stack3;
        ItemStack stack2;
        float var1 = (endy - y) / 4.0f;
        ItemStack stack = ent.func_71124_b(4);
        if (stack != null) {
            RenderUtils3.drawRectBordered(endx + 1.0f, y + 1.0f, endx + 6.0f, y + var1, 1.0, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
            float diff1 = y + var1 - 1.0f - (y + 2.0f);
            double percent = 1.0 - (double)stack.func_77952_i() / (double)stack.func_77958_k();
            RenderUtils3.rectangle(endx + 2.0f, y + var1 - 1.0f, endx + 5.0f, (double)(y + var1 - 1.0f) - (double)diff1 * percent, Colors.getColor(78, 206, 229));
            ESP.mc.field_71466_p.func_175063_a(stack.func_77958_k() - stack.func_77952_i() + "", endx + 7.0f, y + var1 - 1.0f - diff1 / 2.0f - (float)(ESP.mc.field_71466_p.field_78288_b / 2), -1);
        }
        if ((stack2 = ent.func_71124_b(3)) != null) {
            RenderUtils3.drawRectBordered(endx + 1.0f, y + var1, endx + 6.0f, y + var1 * 2.0f, 1.0, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
            float diff1 = y + var1 * 2.0f - (y + var1 + 2.0f);
            double percent = 1.0 - (double)stack2.func_77952_i() * 1.0 / (double)stack2.func_77958_k();
            RenderUtils3.rectangle(endx + 2.0f, y + var1 * 2.0f, endx + 5.0f, (double)(y + var1 * 2.0f) - (double)diff1 * percent, Colors.getColor(78, 206, 229));
            ESP.mc.field_71466_p.func_175063_a(stack2.func_77958_k() - stack2.func_77952_i() + "", endx + 7.0f, y + var1 * 2.0f - diff1 / 2.0f - (float)(ESP.mc.field_71466_p.field_78288_b / 2), -1);
        }
        if ((stack3 = ent.func_71124_b(2)) != null) {
            RenderUtils3.drawRectBordered(endx + 1.0f, y + var1 * 2.0f, endx + 6.0f, y + var1 * 3.0f, 1.0, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
            float diff1 = y + var1 * 3.0f - (y + var1 * 2.0f + 2.0f);
            double percent = 1.0 - (double)stack3.func_77952_i() * 1.0 / (double)stack3.func_77958_k();
            RenderUtils3.rectangle(endx + 2.0f, y + var1 * 3.0f, endx + 5.0f, (double)(y + var1 * 3.0f) - (double)diff1 * percent, Colors.getColor(78, 206, 229));
            ESP.mc.field_71466_p.func_175063_a(stack3.func_77958_k() - stack3.func_77952_i() + "", endx + 7.0f, y + var1 * 3.0f - diff1 / 2.0f - (float)(ESP.mc.field_71466_p.field_78288_b / 2), -1);
        }
        if ((stack4 = ent.func_71124_b(1)) != null) {
            RenderUtils3.drawRectBordered(endx + 1.0f, y + var1 * 3.0f, endx + 6.0f, y + var1 * 4.0f, 1.0, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
            float diff1 = y + var1 * 4.0f - (y + var1 * 3.0f + 2.0f);
            double percent = 1.0 - (double)stack4.func_77952_i() * 1.0 / (double)stack4.func_77958_k();
            RenderUtils3.rectangle(endx + 2.0f, y + var1 * 4.0f - 1.0f, endx + 5.0f, (double)(y + var1 * 4.0f) - (double)diff1 * percent, Colors.getColor(78, 206, 229));
            ESP.mc.field_71466_p.func_175063_a(stack4.func_77958_k() - stack4.func_77952_i() + "", endx + 7.0f, y + var1 * 4.0f - diff1 / 2.0f - (float)(ESP.mc.field_71466_p.field_78288_b / 2), -1);
        }
    }

    private void renderOtherBox(float y, float endy, float x, float endx, int color, double x2Diff, double yDiff) {
        RenderUtils3.rectangle((double)x + 0.5, (double)y + 0.5, (double)x + 1.5, (double)y + yDiff + 0.5, color);
        RenderUtils3.rectangle((double)x + 0.5, (double)endy - 0.5, (double)x + 1.5, (double)endy - yDiff - 0.5, color);
        RenderUtils3.rectangle((double)x - 0.5, (double)y + 0.5, (double)x + 0.5, (double)y + yDiff + 0.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)x + 1.5, (double)y + 2.5, (double)x + 2.5, (double)y + yDiff + 0.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)x - 0.5, (double)y + yDiff + 0.5, (double)x + 2.5, (double)y + yDiff + 1.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)x - 0.5, (double)endy - 0.5, (double)x + 0.5, (double)endy - yDiff - 0.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)x + 1.5, (double)endy - 2.5, (double)x + 2.5, (double)endy - yDiff - 0.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)x - 0.5, (double)endy - yDiff - 0.5, (double)x + 2.5, (double)endy - yDiff - 1.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)x + 1.0, (double)y + 0.5, (double)x + x2Diff, (double)y + 1.5, color);
        RenderUtils3.rectangle((double)x - 0.5, (double)y - 0.5, (double)x + x2Diff, (double)y + 0.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)x + 1.5, (double)y + 1.5, (double)x + x2Diff, (double)y + 2.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)x + x2Diff, (double)y - 0.5, (double)x + x2Diff + 1.0, (double)y + 2.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)x + 1.0, (double)endy - 0.5, (double)x + x2Diff, (double)endy - 1.5, color);
        RenderUtils3.rectangle((double)x - 0.5, (double)endy + 0.5, (double)x + x2Diff, (double)endy - 0.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)x + 1.5, (double)endy - 1.5, (double)x + x2Diff, (double)endy - 2.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)x + x2Diff, (double)endy + 0.5, (double)x + x2Diff + 1.0, (double)endy - 2.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)endx - 0.5, (double)y + 0.5, (double)endx - 1.5, (double)y + yDiff + 0.5, color);
        RenderUtils3.rectangle((double)endx - 0.5, (double)endy - 0.5, (double)endx - 1.5, (double)endy - yDiff - 0.5, color);
        RenderUtils3.rectangle((double)endx + 0.5, (double)y + 0.5, (double)endx - 0.5, (double)y + yDiff + 0.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)endx - 1.5, (double)y + 2.5, (double)endx - 2.5, (double)y + yDiff + 0.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)endx + 0.5, (double)y + yDiff + 0.5, (double)endx - 2.5, (double)y + yDiff + 1.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)endx + 0.5, (double)endy - 0.5, (double)endx - 0.5, (double)endy - yDiff - 0.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)endx - 1.5, (double)endy - 2.5, (double)endx - 2.5, (double)endy - yDiff - 0.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)endx + 0.5, (double)endy - yDiff - 0.5, (double)endx - 2.5, (double)endy - yDiff - 1.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)endx - 1.0, (double)y + 0.5, (double)endx - x2Diff, (double)y + 1.5, color);
        RenderUtils3.rectangle((double)endx + 0.5, (double)y - 0.5, (double)endx - x2Diff, (double)y + 0.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)endx - 1.5, (double)y + 1.5, (double)endx - x2Diff, (double)y + 2.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)endx - x2Diff, (double)y - 0.5, (double)endx - x2Diff - 1.0, (double)y + 2.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)endx - 1.0, (double)endy - 0.5, (double)endx - x2Diff, (double)endy - 1.5, color);
        RenderUtils3.rectangle((double)endx + 0.5, (double)endy + 0.5, (double)endx - x2Diff, (double)endy - 0.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)endx - 1.5, (double)endy - 1.5, (double)endx - x2Diff, (double)endy - 2.5, Colors.getColor(0, 150));
        RenderUtils3.rectangle((double)endx - x2Diff, (double)endy + 0.5, (double)endx - x2Diff - 1.0, (double)endy - 2.5, Colors.getColor(0, 150));
    }

    private int parseEspColorFromNameTag(EntityLivingBase ent) {
        String text = ent.func_145748_c_().func_150254_d();
        if (Character.toLowerCase(text.charAt(0)) == '\u00a7') {
            char oneMore = Character.toLowerCase(text.charAt(1));
            int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
            if (colorCode < 16) {
                try {
                    int newColor = this.ColorCode[colorCode];
                    return Colors.getColor(newColor >> 16, newColor >> 8 & 0xFF, newColor & 0xFF, 255);
                }
                catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
            }
        } else {
            return Colors.getColor(255, 255, 255, 255);
        }
        if (ent instanceof EntityLivingBase && AntiBot.isBot(ent)) {
            return Colors.getColor(100, 100, 100, 255);
        }
        return 0;
    }

    private double[] convertTo2D(double x, double y, double z, Entity ent) {
        return this.convertTo2D(x, y, z);
    }

    private double[] convertTo2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer((int)3);
        IntBuffer viewport = BufferUtils.createIntBuffer((int)16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer((int)16);
        FloatBuffer projection = BufferUtils.createFloatBuffer((int)16);
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        boolean result = GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)modelView, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)screenCoords);
        if (result) {
            return new double[]{screenCoords.get(0), (float)Display.getHeight() - screenCoords.get(1), screenCoords.get(2)};
        }
        return null;
    }

    private void scale(Entity ent) {
        float scale = 1.0f;
        float target = scale * (ESP.mc.field_71474_y.field_74334_X / ESP.mc.field_71474_y.field_74334_X);
        if (this.gradualFOVModifier == 0.0 || Double.isNaN(this.gradualFOVModifier)) {
            this.gradualFOVModifier = target;
        }
        this.gradualFOVModifier += ((double)target - this.gradualFOVModifier) / ((double)mc.func_175610_ah() * 0.7);
        scale = (float)((double)scale * this.gradualFOVModifier);
        GlStateManager.func_179152_a((float)scale, (float)scale, (float)scale);
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public Color getColor(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            if (entityLivingBase.field_70737_aN > 0) {
                return Color.RED;
            }
            if (EntityUtils.isFriend((Entity)entityLivingBase)) {
                return Color.BLUE;
            }
            if (((Boolean)this.colorTeam.get()).booleanValue()) {
                char[] chars = entityLivingBase.func_145748_c_().func_150254_d().toCharArray();
                int color = Integer.MAX_VALUE;
                String colors = "0123456789abcdef";
                for (int i = 0; i < chars.length; ++i) {
                    int index;
                    if (chars[i] != '\u00a7' || i + 1 >= chars.length || (index = "0123456789abcdef".indexOf(chars[i + 1])) == -1) continue;
                    color = ColorUtils.hexColors[index];
                    break;
                }
                return new Color(color);
            }
        }
        return (Boolean)this.colorRainbow.get() != false ? ColorUtil.rainbow(15, 1, 1.0f, 1.0f, 1.0f) : new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get());
    }
}

