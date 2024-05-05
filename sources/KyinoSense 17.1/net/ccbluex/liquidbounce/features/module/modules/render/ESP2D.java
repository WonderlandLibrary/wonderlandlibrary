/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.StringUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.vector.Vector4f
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import me.report.liquidware.modules.render.HudColors;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.cn.Insane.Module.fonts.impl.Fonts;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.utils.math.MathUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtil;
import net.ccbluex.liquidbounce.utils.render.ESPUtil;
import net.ccbluex.liquidbounce.utils.render.GradientUtil;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

@ModuleInfo(name="ESP2D", description="New ESP in 2D fashion.", category=ModuleCategory.RENDER)
public class ESP2D
extends Module {
    public static final BoolValue Players = new BoolValue("Player", true);
    public static final BoolValue Animals = new BoolValue("Animals", false);
    public static final BoolValue Mobs = new BoolValue("Mobs", false);
    public final BoolValue mcfont = new BoolValue("MinecraftFont", false);
    public final BoolValue itemHeld = new BoolValue("ItemHeld", true);
    public final BoolValue boxEsp = new BoolValue("Box", false);
    public static final ListValue boxColorMode = new ListValue("BoxMode", new String[]{"Sync", "Light Rainbow", "Static", "Fade", "Double Color", "Analogous", "Default"}, "Light Rainbow");
    public static final ListValue degree = new ListValue("Degree", new String[]{"30", "-30"}, "30");
    public final BoolValue healthBar = new BoolValue("HealthBar", true);
    public static final ListValue healthBarMode = new ListValue("HealthBarMode", new String[]{"Color", "Health"}, "Color");
    public final BoolValue healthBarText = new BoolValue("HealthBarText", true);
    public final BoolValue nametags = new BoolValue("Tags", true);
    public final BoolValue redTags = new BoolValue("RedTags", true);
    public final FloatValue scale = new FloatValue("TagScale", 0.5f, 0.1f, 10.0f);
    public final BoolValue healthtext = new BoolValue("HealthText", true);
    public final BoolValue Background = new BoolValue("Background", true);
    private final Map<Entity, Vector4f> entityPosition = new HashMap<Entity, Vector4f>();
    private final NumberFormat df = new DecimalFormat("0.#");
    private final Color backgroundColor = new Color(10, 10, 10, 130);
    private Color firstColor = Color.BLACK;
    private Color secondColor = Color.BLACK;
    private Color thirdColor = Color.BLACK;
    private Color fourthColor = Color.BLACK;

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        this.entityPosition.clear();
        for (Entity entity : ESP2D.mc.field_71441_e.field_72996_f) {
            if (!ESP2D.shouldRender(entity) || !ESPUtil.isInView(entity)) continue;
            this.entityPosition.put(entity, ESPUtil.getEntityPositionsOn2D(entity));
        }
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        HUD hudMod = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        HudColors HudColors2 = (HudColors)LiquidBounce.moduleManager.getModule(HudColors.class);
        if (((Boolean)this.boxEsp.get()).booleanValue()) {
            switch (((String)boxColorMode.get()).toLowerCase()) {
                case "sync": {
                    Color[] colors = hudMod.getClientColors();
                    this.firstColor = ColorUtil.interpolateColorsBackAndForth(15, 0, colors[0], colors[1], (Boolean)HUD.hueInterpolation.get());
                    this.secondColor = ColorUtil.interpolateColorsBackAndForth(15, 90, colors[0], colors[1], (Boolean)HUD.hueInterpolation.get());
                    this.thirdColor = ColorUtil.interpolateColorsBackAndForth(15, 180, colors[0], colors[1], (Boolean)HUD.hueInterpolation.get());
                    this.fourthColor = ColorUtil.interpolateColorsBackAndForth(15, 270, colors[0], colors[1], (Boolean)HUD.hueInterpolation.get());
                    break;
                }
                case "light rainbow": {
                    this.firstColor = ColorUtil.rainbow(15, 0, ((Float)HudColors2.getSaturationValue().get()).floatValue(), 1.0f, 1.0f);
                    this.secondColor = ColorUtil.rainbow(15, 90, ((Float)HudColors2.getSaturationValue().get()).floatValue(), 1.0f, 1.0f);
                    this.thirdColor = ColorUtil.rainbow(15, 180, ((Float)HudColors2.getSaturationValue().get()).floatValue(), 1.0f, 1.0f);
                    this.fourthColor = ColorUtil.rainbow(15, 270, ((Float)HudColors2.getSaturationValue().get()).floatValue(), 1.0f, 1.0f);
                    break;
                }
                case "rainbow": {
                    this.firstColor = ColorUtil.rainbow(15, 0, ((Float)HudColors2.getSaturationValue().get()).floatValue(), 1.0f, 1.0f);
                    this.secondColor = ColorUtil.rainbow(15, 90, ((Float)HudColors2.getSaturationValue().get()).floatValue(), 1.0f, 1.0f);
                    this.thirdColor = ColorUtil.rainbow(15, 180, ((Float)HudColors2.getSaturationValue().get()).floatValue(), 1.0f, 1.0f);
                    this.fourthColor = ColorUtil.rainbow(15, 270, ((Float)HudColors2.getSaturationValue().get()).floatValue(), 1.0f, 1.0f);
                    break;
                }
                case "static": {
                    this.secondColor = this.firstColor = Color.PINK;
                    this.thirdColor = this.firstColor;
                    this.fourthColor = this.firstColor;
                    break;
                }
                case "fade": {
                    this.firstColor = ColorUtil.fade(15, 0, Color.PINK, 1.0f);
                    this.secondColor = ColorUtil.fade(15, 90, Color.PINK, 1.0f);
                    this.thirdColor = ColorUtil.fade(15, 180, Color.PINK, 1.0f);
                    this.fourthColor = ColorUtil.fade(15, 270, Color.PINK, 1.0f);
                    break;
                }
                case "double color": {
                    this.firstColor = ColorUtil.interpolateColorsBackAndForth(15, 0, Color.PINK, Color.BLUE, (Boolean)HUD.hueInterpolation.get());
                    this.secondColor = ColorUtil.interpolateColorsBackAndForth(15, 90, Color.PINK, Color.BLUE, (Boolean)HUD.hueInterpolation.get());
                    this.thirdColor = ColorUtil.interpolateColorsBackAndForth(15, 180, Color.PINK, Color.BLUE, (Boolean)HUD.hueInterpolation.get());
                    this.fourthColor = ColorUtil.interpolateColorsBackAndForth(15, 270, Color.PINK, Color.BLUE, (Boolean)HUD.hueInterpolation.get());
                    break;
                }
                case "analogous": {
                    int val = ((String)degree.get()).equals("30") ? 0 : 1;
                    Color analogous = ColorUtil.getAnalogousColor(Color.BLUE)[val];
                    this.firstColor = ColorUtil.interpolateColorsBackAndForth(15, 0, Color.BLUE, analogous, (Boolean)HUD.hueInterpolation.get());
                    this.secondColor = ColorUtil.interpolateColorsBackAndForth(15, 90, Color.BLUE, analogous, (Boolean)HUD.hueInterpolation.get());
                    this.thirdColor = ColorUtil.interpolateColorsBackAndForth(15, 180, Color.BLUE, analogous, (Boolean)HUD.hueInterpolation.get());
                    this.fourthColor = ColorUtil.interpolateColorsBackAndForth(15, 270, Color.BLUE, analogous, (Boolean)HUD.hueInterpolation.get());
                }
            }
        }
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        for (Entity entity : this.entityPosition.keySet()) {
            EntityLivingBase entityLivingBase;
            Color healthColor;
            float healthValue;
            EntityLivingBase renderingEntity;
            Vector4f pos = this.entityPosition.get(entity);
            float x = pos.getX();
            float y = pos.getY();
            float right = pos.getZ();
            float bottom = pos.getW();
            if (((Boolean)this.nametags.get()).booleanValue() && entity instanceof EntityLivingBase) {
                double fontHeight;
                renderingEntity = (EntityLivingBase)entity;
                healthValue = renderingEntity.func_110143_aJ() / renderingEntity.func_110138_aP();
                healthColor = (double)healthValue > 0.75 ? new Color(66, 246, 123) : ((double)healthValue > 0.5 ? new Color(228, 255, 105) : ((double)healthValue > 0.35 ? new Color(236, 100, 64) : new Color(255, 65, 68)));
                StringBuilder text = new StringBuilder(((Boolean)this.redTags.get() != false ? "\u00a7c" : "\u00a7f") + StringUtils.func_76338_a((String)renderingEntity.func_145748_c_().func_150260_c()));
                if (((Boolean)this.healthtext.get()).booleanValue()) {
                    text.append(String.format(" \u00a77[\u00a7r%s HP\u00a77]", this.df.format(renderingEntity.func_110143_aJ())));
                }
                double fontScale = ((Float)this.scale.getValue()).floatValue();
                float middle = x + (right - x) / 2.0f;
                float textWidth = 0.0f;
                if (((Boolean)this.mcfont.get()).booleanValue()) {
                    textWidth = ESP2D.mc.field_71466_p.func_78256_a(text.toString());
                    middle = (float)((double)middle - (double)textWidth * fontScale / 2.0);
                    fontHeight = (double)ESP2D.mc.field_71466_p.field_78288_b * fontScale;
                } else {
                    textWidth = Fonts.SFBOLD.SFBOLD_20.SFBOLD_20.stringWidth(text.toString());
                    middle = (float)((double)middle - (double)textWidth * fontScale / 2.0);
                    fontHeight = (double)Fonts.SFBOLD.SFBOLD_20.SFBOLD_20.getHeight() * fontScale;
                }
                GL11.glPushMatrix();
                GL11.glTranslated((double)middle, (double)((double)y - (fontHeight + 2.0)), (double)0.0);
                GL11.glScaled((double)fontScale, (double)fontScale, (double)1.0);
                GL11.glTranslated((double)(-middle), (double)(-((double)y - (fontHeight + 2.0))), (double)0.0);
                if (((Boolean)this.Background.get()).booleanValue()) {
                    RoundedUtil.drawRound(middle - 3.0f, (float)((double)y - (fontHeight + 7.0)), textWidth + 6.0f, (float)(fontHeight / fontScale + 4.0), 4.0f, this.backgroundColor);
                }
                GlStateManager.func_179144_i((int)0);
                GlStateManager.func_179117_G();
                if (((Boolean)this.mcfont.get()).booleanValue()) {
                    ESP2D.mc.field_71466_p.func_175063_a(text.toString(), middle, (float)((double)y - (fontHeight + 4.0)), healthColor.getRGB());
                } else {
                    Fonts.SFBOLD.SFBOLD_20.SFBOLD_20.drawString((CharSequence)text.toString(), middle, (float)((double)y - (fontHeight + 5.0)), healthColor.getRGB());
                }
                GL11.glPopMatrix();
            }
            if (((Boolean)this.itemHeld.get()).booleanValue() && entity instanceof EntityLivingBase && (entityLivingBase = (EntityLivingBase)entity).func_70694_bm() != null) {
                double fontHeight;
                double fontScale = 0.5;
                float middle = x + (right - x) / 2.0f;
                float textWidth = 0.0f;
                String text = entityLivingBase.func_70694_bm().func_82833_r();
                if (((Boolean)this.mcfont.get()).booleanValue()) {
                    textWidth = ESP2D.mc.field_71466_p.func_78256_a(text);
                    middle = (float)((double)middle - (double)textWidth * fontScale / 2.0);
                    fontHeight = (double)ESP2D.mc.field_71466_p.field_78288_b * fontScale;
                } else {
                    textWidth = Fonts.SFBOLD.SFBOLD_20.SFBOLD_20.stringWidth(text);
                    middle = (float)((double)middle - (double)textWidth * fontScale / 2.0);
                    fontHeight = (double)Fonts.SFBOLD.SFBOLD_20.SFBOLD_20.getHeight() * fontScale;
                }
                GL11.glPushMatrix();
                GL11.glTranslated((double)middle, (double)(bottom + 4.0f), (double)0.0);
                GL11.glScaled((double)fontScale, (double)fontScale, (double)1.0);
                GL11.glTranslated((double)(-middle), (double)(-(bottom + 4.0f)), (double)0.0);
                GlStateManager.func_179144_i((int)0);
                GlStateManager.func_179117_G();
                if (((Boolean)this.mcfont.get()).booleanValue()) {
                    ESP2D.mc.field_71466_p.func_175063_a(text, middle, bottom + 4.0f, -1);
                } else {
                    Fonts.SFBOLD.SFBOLD_20.SFBOLD_20.drawString((CharSequence)text.toString(), middle, bottom + 4.0f, -1, true);
                }
                GL11.glPopMatrix();
            }
            if (((Boolean)this.healthBar.get()).booleanValue() && entity instanceof EntityLivingBase) {
                renderingEntity = (EntityLivingBase)entity;
                healthValue = renderingEntity.func_110143_aJ() / renderingEntity.func_110138_aP();
                healthColor = (double)healthValue > 0.75 ? new Color(66, 246, 123) : ((double)healthValue > 0.5 ? new Color(228, 255, 105) : ((double)healthValue > 0.35 ? new Color(236, 100, 64) : new Color(255, 65, 68)));
                float height = bottom - y + 1.0f;
                if (((String)healthBarMode.get()).equals("Color")) {
                    GradientUtil.drawGradientTB(right + 3.0f, y + (height - height * healthValue), 1.0f, height * healthValue, 1.0f, this.secondColor, this.thirdColor);
                }
                if (((Boolean)this.healthBarText.get()).booleanValue()) {
                    String health = String.valueOf(MathUtils.round(healthValue *= 100.0f, 1.0)).substring(0, healthValue == 100.0f ? 3 : 2);
                    String text = health + "%";
                    double fontScale = 0.5;
                    float textX = right + 8.0f;
                    float fontHeight = (Boolean)this.mcfont.get() != false ? (float)((double)ESP2D.mc.field_71466_p.field_78288_b * fontScale) : (float)((double)Fonts.SFBOLD.SFBOLD_20.SFBOLD_20.getHeight() * fontScale);
                    float newHeight = height - fontHeight;
                    float textY = y + (newHeight - newHeight * (healthValue / 100.0f));
                    GL11.glPushMatrix();
                    GL11.glTranslated((double)(textX - 5.0f), (double)textY, (double)1.0);
                    GL11.glScaled((double)fontScale, (double)fontScale, (double)1.0);
                    GL11.glTranslated((double)(-(textX - 5.0f)), (double)(-textY), (double)1.0);
                    if (((Boolean)this.mcfont.get()).booleanValue()) {
                        ESP2D.mc.field_71466_p.func_175063_a(text, textX, textY, -1);
                    } else {
                        Fonts.SFBOLD.SFBOLD_20.SFBOLD_20.drawString((CharSequence)text, textX, textY, -1, true);
                    }
                    GL11.glPopMatrix();
                }
            }
            if (!((Boolean)this.boxEsp.get()).booleanValue()) continue;
            float outlineThickness = 0.5f;
            GlStateManager.func_179117_G();
            GradientUtil.drawGradientLR(x, y, right - x, 1.0f, 1.0f, this.firstColor, this.secondColor);
            GradientUtil.drawGradientTB(x, y, 1.0f, bottom - y, 1.0f, this.firstColor, this.fourthColor);
            GradientUtil.drawGradientLR(x, bottom, right - x, 1.0f, 1.0f, this.fourthColor, this.thirdColor);
            GradientUtil.drawGradientTB(right, y, 1.0f, bottom - y + 1.0f, 1.0f, this.secondColor, this.thirdColor);
        }
    }

    public static boolean shouldRender(Entity entity) {
        if (entity.field_70128_L || entity.func_82150_aj()) {
            return false;
        }
        if (((Boolean)Players.get()).booleanValue() && entity instanceof EntityPlayer) {
            if (entity == ESP2D.mc.field_71439_g) {
                return ESP2D.mc.field_71474_y.field_74320_O != 0;
            }
            return true;
        }
        if (((Boolean)Animals.get()).booleanValue() && entity instanceof EntityAnimal) {
            return true;
        }
        return (Boolean)Mobs.get() != false && entity instanceof EntityMob;
    }
}

