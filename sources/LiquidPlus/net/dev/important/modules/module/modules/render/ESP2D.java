/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 *  javax.vecmath.Vector4d
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.AxisAlignedBB
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package net.dev.important.modules.module.modules.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render2DEvent;
import net.dev.important.gui.font.GameFontRenderer;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.color.ColorMixer;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.render.BlendUtils;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

@Info(name="ESP2D", description="autumn skid.", category=Category.RENDER, cnName="\u5e73\u9762\u900f\u89c6")
public final class ESP2D
extends Module {
    public final BoolValue outline = new BoolValue("Outline", true);
    public final ListValue boxMode = new ListValue("Mode", new String[]{"Box", "Corners"}, "Box");
    public final BoolValue healthBar = new BoolValue("Health-bar", true);
    public final BoolValue armorBar = new BoolValue("Armor-bar", true);
    public final BoolValue details = new BoolValue("Details", true);
    public final BoolValue tagsValue = new BoolValue("Tags", true);
    public final BoolValue itemTagsValue = new BoolValue("Item-Tags", true);
    public final BoolValue clearNameValue = new BoolValue("Use-Clear-Name", false);
    public final BoolValue absorption = new BoolValue("Render-Absorption", true);
    public final BoolValue localPlayer = new BoolValue("Local-Player", true);
    public final BoolValue droppedItems = new BoolValue("Dropped-Items", false);
    private final ListValue colorModeValue = new ListValue("Color", new String[]{"Custom", "Rainbow", "Sky", "LiquidSlowly", "Fade", "Mixer"}, "Custom");
    private final IntegerValue colorRedValue = new IntegerValue("Red", 255, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("Green", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("Blue", 255, 0, 255);
    private final FloatValue saturationValue = new FloatValue("Saturation", 1.0f, 0.0f, 1.0f);
    private final FloatValue brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
    private final IntegerValue mixerSecondsValue = new IntegerValue("Seconds", 2, 1, 10);
    private final FloatValue fontScaleValue = new FloatValue("Font-Scale", 0.5f, 0.0f, 1.0f, "x");
    private final BoolValue colorTeam = new BoolValue("Team", false);
    public static List collectedEntities = new ArrayList();
    private final IntBuffer viewport;
    private final FloatBuffer modelview;
    private final FloatBuffer projection;
    private final FloatBuffer vector;
    private final int backgroundColor;
    private final int black;
    private final DecimalFormat dFormat = new DecimalFormat("0.0");

    public ESP2D() {
        this.viewport = GLAllocation.func_74527_f((int)16);
        this.modelview = GLAllocation.func_74529_h((int)16);
        this.projection = GLAllocation.func_74529_h((int)16);
        this.vector = GLAllocation.func_74529_h((int)4);
        this.backgroundColor = new Color(0, 0, 0, 120).getRGB();
        this.black = Color.BLACK.getRGB();
    }

    public final Color getColor(Entity entity) {
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

    public static boolean shouldCancelNameTag(EntityLivingBase entity) {
        return Client.moduleManager.getModule(ESP2D.class) != null && Client.moduleManager.getModule(ESP2D.class).getState() && (Boolean)((ESP2D)Client.moduleManager.getModule(ESP2D.class)).tagsValue.get() != false && collectedEntities.contains(entity);
    }

    @Override
    public void onDisable() {
        collectedEntities.clear();
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        GL11.glPushMatrix();
        this.collectEntities();
        float partialTicks = event.getPartialTicks();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaleFactor = scaledResolution.func_78325_e();
        double scaling = (double)scaleFactor / Math.pow(scaleFactor, 2.0);
        GL11.glScaled((double)scaling, (double)scaling, (double)scaling);
        int black = this.black;
        int background = this.backgroundColor;
        float scale = 0.65f;
        float upscale = 1.0f / scale;
        FontRenderer fr = ESP2D.mc.field_71466_p;
        RenderManager renderMng = mc.func_175598_ae();
        EntityRenderer entityRenderer = ESP2D.mc.field_71460_t;
        boolean outline = (Boolean)this.outline.get();
        boolean health = (Boolean)this.healthBar.get();
        boolean armor = (Boolean)this.armorBar.get();
        int collectedEntitiesSize = collectedEntities.size();
        for (int i = 0; i < collectedEntitiesSize; ++i) {
            double durabilityWidth;
            float itemDurability;
            float armorValue;
            EntityLivingBase entityLivingBase;
            Entity entity = (Entity)collectedEntities.get(i);
            int color = this.getColor(entity).getRGB();
            if (!RenderUtils.isInViewFrustrum(entity)) continue;
            double x = RenderUtils.interpolate(entity.field_70165_t, entity.field_70142_S, partialTicks);
            double y = RenderUtils.interpolate(entity.field_70163_u, entity.field_70137_T, partialTicks);
            double z = RenderUtils.interpolate(entity.field_70161_v, entity.field_70136_U, partialTicks);
            double width = (double)entity.field_70130_N / 1.5;
            double height = (double)entity.field_70131_O + (entity.func_70093_af() ? -0.3 : 0.2);
            AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
            List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c), new Vector3d(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72339_c), new Vector3d(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72339_c), new Vector3d(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72339_c), new Vector3d(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72334_f), new Vector3d(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72334_f), new Vector3d(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72334_f), new Vector3d(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72334_f));
            entityRenderer.func_78479_a(partialTicks, 0);
            Vector4d position = null;
            for (Vector3d vector : vectors) {
                vector = this.project2D(scaleFactor, vector.x - renderMng.field_78730_l, vector.y - renderMng.field_78731_m, vector.z - renderMng.field_78728_n);
                if (vector == null || !(vector.z >= 0.0) || !(vector.z < 1.0)) continue;
                if (position == null) {
                    position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                }
                position.x = Math.min(vector.x, position.x);
                position.y = Math.min(vector.y, position.y);
                position.z = Math.max(vector.x, position.z);
                position.w = Math.max(vector.y, position.w);
            }
            if (position == null) continue;
            entityRenderer.func_78478_c();
            double posX = position.x;
            double posY = position.y;
            double endPosX = position.z;
            double endPosY = position.w;
            if (outline) {
                if (this.boxMode.get() == "Box") {
                    RenderUtils.drawRect(posX - 1.0, posY, posX + 0.5, endPosY + 0.5, black);
                    RenderUtils.drawRect(posX - 1.0, posY - 0.5, endPosX + 0.5, posY + 0.5 + 0.5, black);
                    RenderUtils.drawRect(endPosX - 0.5 - 0.5, posY, endPosX + 0.5, endPosY + 0.5, black);
                    RenderUtils.drawRect(posX - 1.0, endPosY - 0.5 - 0.5, endPosX + 0.5, endPosY + 0.5, black);
                    RenderUtils.drawRect(posX - 0.5, posY, posX + 0.5 - 0.5, endPosY, color);
                    RenderUtils.drawRect(posX, endPosY - 0.5, endPosX, endPosY, color);
                    RenderUtils.drawRect(posX - 0.5, posY, endPosX, posY + 0.5, color);
                    RenderUtils.drawRect(endPosX - 0.5, posY, endPosX, endPosY, color);
                } else {
                    RenderUtils.drawRect(posX + 0.5, posY, posX - 1.0, posY + (endPosY - posY) / 4.0 + 0.5, black);
                    RenderUtils.drawRect(posX - 1.0, endPosY, posX + 0.5, endPosY - (endPosY - posY) / 4.0 - 0.5, black);
                    RenderUtils.drawRect(posX - 1.0, posY - 0.5, posX + (endPosX - posX) / 3.0 + 0.5, posY + 1.0, black);
                    RenderUtils.drawRect(endPosX - (endPosX - posX) / 3.0 - 0.5, posY - 0.5, endPosX, posY + 1.0, black);
                    RenderUtils.drawRect(endPosX - 1.0, posY, endPosX + 0.5, posY + (endPosY - posY) / 4.0 + 0.5, black);
                    RenderUtils.drawRect(endPosX - 1.0, endPosY, endPosX + 0.5, endPosY - (endPosY - posY) / 4.0 - 0.5, black);
                    RenderUtils.drawRect(posX - 1.0, endPosY - 1.0, posX + (endPosX - posX) / 3.0 + 0.5, endPosY + 0.5, black);
                    RenderUtils.drawRect(endPosX - (endPosX - posX) / 3.0 - 0.5, endPosY - 1.0, endPosX + 0.5, endPosY + 0.5, black);
                    RenderUtils.drawRect(posX, posY, posX - 0.5, posY + (endPosY - posY) / 4.0, color);
                    RenderUtils.drawRect(posX, endPosY, posX - 0.5, endPosY - (endPosY - posY) / 4.0, color);
                    RenderUtils.drawRect(posX - 0.5, posY, posX + (endPosX - posX) / 3.0, posY + 0.5, color);
                    RenderUtils.drawRect(endPosX - (endPosX - posX) / 3.0, posY, endPosX, posY + 0.5, color);
                    RenderUtils.drawRect(endPosX - 0.5, posY, endPosX, posY + (endPosY - posY) / 4.0, color);
                    RenderUtils.drawRect(endPosX - 0.5, endPosY, endPosX, endPosY - (endPosY - posY) / 4.0, color);
                    RenderUtils.drawRect(posX, endPosY - 0.5, posX + (endPosX - posX) / 3.0, endPosY, color);
                    RenderUtils.drawRect(endPosX - (endPosX - posX) / 3.0, endPosY - 0.5, endPosX - 0.5, endPosY, color);
                }
            }
            boolean living = entity instanceof EntityLivingBase;
            boolean isPlayer = entity instanceof EntityPlayer;
            if (living) {
                entityLivingBase = (EntityLivingBase)entity;
                if (health) {
                    armorValue = entityLivingBase.func_110143_aJ();
                    if (armorValue > (itemDurability = entityLivingBase.func_110138_aP())) {
                        armorValue = itemDurability;
                    }
                    durabilityWidth = armorValue / itemDurability;
                    double textWidth = (endPosY - posY) * durabilityWidth;
                    String healthDisplay = this.dFormat.format(armorValue) + "\u00a7c\u2764";
                    if (((Boolean)this.details.get()).booleanValue()) {
                        this.drawScaledString(healthDisplay, posX - 4.0 - (double)((float)ESP2D.mc.field_71466_p.func_78256_a(healthDisplay) * ((Float)this.fontScaleValue.get()).floatValue()), endPosY - textWidth - (double)((float)ESP2D.mc.field_71466_p.field_78288_b / 2.0f * ((Float)this.fontScaleValue.get()).floatValue()), ((Float)this.fontScaleValue.get()).floatValue(), -1);
                    }
                    RenderUtils.drawRect(posX - 3.5, posY - 0.5, posX - 1.5, endPosY + 0.5, background);
                    if (armorValue > 0.0f) {
                        int healthColor = BlendUtils.getHealthColor(armorValue, itemDurability).getRGB();
                        RenderUtils.drawRect(posX - 3.0, endPosY, posX - 2.0, endPosY - textWidth, healthColor);
                        float tagY = entityLivingBase.func_110139_bj();
                        if (((Boolean)this.absorption.get()).booleanValue() && tagY > 0.0f) {
                            RenderUtils.drawRect(posX - 3.0, endPosY, posX - 2.0, endPosY - (endPosY - posY) / 6.0 * (double)tagY / 2.0, new Color(Potion.field_76444_x.func_76401_j()).getRGB());
                        }
                    }
                }
            }
            if (armor) {
                ItemStack itemStack;
                if (living) {
                    entityLivingBase = (EntityLivingBase)entity;
                    armorValue = entityLivingBase.func_70658_aO();
                    double armorWidth = (endPosY - posY) * (double)armorValue / 20.0;
                    RenderUtils.drawRect(endPosX + 1.5, posY - 0.5, endPosX + 3.5, endPosY + 0.5, background);
                    if (armorValue > 0.0f) {
                        RenderUtils.drawRect(endPosX + 2.0, endPosY, endPosX + 3.0, endPosY - armorWidth, new Color(40, 40, 230).getRGB());
                    }
                } else if (entity instanceof EntityItem && (itemStack = ((EntityItem)entity).func_92059_d()).func_77984_f()) {
                    int maxDamage = itemStack.func_77958_k();
                    itemDurability = maxDamage - itemStack.func_77952_i();
                    durabilityWidth = (endPosY - posY) * (double)itemDurability / (double)maxDamage;
                    if (((Boolean)this.details.get()).booleanValue()) {
                        this.drawScaledString((int)itemDurability + "", endPosX + 4.0, endPosY - durabilityWidth - (double)((float)ESP2D.mc.field_71466_p.field_78288_b / 2.0f * ((Float)this.fontScaleValue.get()).floatValue()), ((Float)this.fontScaleValue.get()).floatValue(), -1);
                    }
                    RenderUtils.drawRect(endPosX + 1.5, posY - 0.5, endPosX + 3.5, endPosY + 0.5, background);
                    RenderUtils.drawRect(endPosX + 2.0, endPosY, endPosX + 3.0, endPosY - durabilityWidth, new Color(40, 40, 230).getRGB());
                }
            }
            if (isPlayer && ((Boolean)this.details.get()).booleanValue()) {
                entityLivingBase = (EntityLivingBase)entity;
                EntityPlayer player = (EntityPlayer)entityLivingBase;
                double yDist = (endPosY - posY) / 4.0;
                for (int j = 4; j > 0; --j) {
                    ItemStack armorStack = player.func_71124_b(j);
                    if (armorStack == null || armorStack.func_77973_b() == null) continue;
                    this.renderItemStack(armorStack, endPosX + (armor ? 4.0 : 2.0), posY + yDist * (double)(4 - j) + yDist / 2.0 - 5.0);
                }
            }
            if (living && ((Boolean)this.tagsValue.get()).booleanValue()) {
                entityLivingBase = (EntityLivingBase)entity;
                this.drawScaledCenteredString((Boolean)this.clearNameValue.get() != false ? entityLivingBase.func_70005_c_() : entityLivingBase.func_145748_c_().func_150254_d(), posX + (endPosX - posX) / 2.0, posY - 1.0 - (double)((float)ESP2D.mc.field_71466_p.field_78288_b * ((Float)this.fontScaleValue.get()).floatValue()), ((Float)this.fontScaleValue.get()).floatValue(), -1);
            }
            if (!((Boolean)this.itemTagsValue.get()).booleanValue()) continue;
            if (living) {
                entityLivingBase = (EntityLivingBase)entity;
                if (entityLivingBase.func_70694_bm() == null || entityLivingBase.func_70694_bm().func_77973_b() == null) continue;
                this.drawScaledCenteredString(entityLivingBase.func_70694_bm().func_82833_r(), posX + (endPosX - posX) / 2.0, endPosY + 1.0, ((Float)this.fontScaleValue.get()).floatValue(), -1);
                continue;
            }
            if (!(entity instanceof EntityItem)) continue;
            this.drawScaledCenteredString(((EntityItem)entity).func_92059_d().func_82833_r(), posX + (endPosX - posX) / 2.0, endPosY + 1.0, ((Float)this.fontScaleValue.get()).floatValue(), -1);
        }
        GL11.glPopMatrix();
        GlStateManager.func_179147_l();
        entityRenderer.func_78478_c();
    }

    private void drawScaledString(String text, double x, double y, double scale, int color) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)x, (double)y, (double)x);
        GlStateManager.func_179139_a((double)scale, (double)scale, (double)scale);
        ESP2D.mc.field_71466_p.func_175063_a(text, 0.0f, 0.0f, color);
        GlStateManager.func_179121_F();
    }

    private void drawScaledCenteredString(String text, double x, double y, double scale, int color) {
        this.drawScaledString(text, x - (double)((float)ESP2D.mc.field_71466_p.func_78256_a(text) / 2.0f) * scale, y, scale, color);
    }

    private void renderItemStack(ItemStack stack, double x, double y) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)x, (double)y, (double)x);
        GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
        GlStateManager.func_179091_B();
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderHelper.func_74520_c();
        mc.func_175599_af().func_180450_b(stack, 0, 0);
        mc.func_175599_af().func_175030_a(ESP2D.mc.field_71466_p, stack, 0, 0);
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    private void collectEntities() {
        collectedEntities.clear();
        List playerEntities = ESP2D.mc.field_71441_e.field_72996_f;
        int playerEntitiesSize = playerEntities.size();
        for (int i = 0; i < playerEntitiesSize; ++i) {
            Entity entity = (Entity)playerEntities.get(i);
            if (!EntityUtils.isSelected(entity, false) && (!((Boolean)this.localPlayer.get()).booleanValue() || !(entity instanceof EntityPlayerSP) || ESP2D.mc.field_71474_y.field_74320_O == 0) && (!((Boolean)this.droppedItems.get()).booleanValue() || !(entity instanceof EntityItem))) continue;
            collectedEntities.add(entity);
        }
    }

    private Vector3d project2D(int scaleFactor, double x, double y, double z) {
        GL11.glGetFloat((int)2982, (FloatBuffer)this.modelview);
        GL11.glGetFloat((int)2983, (FloatBuffer)this.projection);
        GL11.glGetInteger((int)2978, (IntBuffer)this.viewport);
        return GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)this.modelview, (FloatBuffer)this.projection, (IntBuffer)this.viewport, (FloatBuffer)this.vector) ? new Vector3d((double)(this.vector.get(0) / (float)scaleFactor), (double)(((float)Display.getHeight() - this.vector.get(1)) / (float)scaleFactor), (double)this.vector.get(2)) : null;
    }
}

