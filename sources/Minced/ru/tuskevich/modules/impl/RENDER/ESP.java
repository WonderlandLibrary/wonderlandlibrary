// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.EventTarget;
import java.util.Iterator;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import ru.tuskevich.modules.impl.HUD.Hud;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.util.font.Fonts;
import net.minecraft.entity.EntityLivingBase;
import java.awt.Color;
import com.jhlabs.vecmath.Vector4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.AxisAlignedBB;
import ru.tuskevich.util.math.MathUtility;
import ru.tuskevich.util.render.RenderUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import ru.tuskevich.event.events.impl.EventDisplay;
import ru.tuskevich.ui.dropui.setting.Setting;
import net.minecraft.client.renderer.GLAllocation;
import java.util.ArrayList;
import ru.tuskevich.ui.dropui.setting.imp.ColorSetting;
import ru.tuskevich.ui.dropui.setting.imp.MultiBoxSetting;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.entity.Entity;
import java.util.List;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "EntityESP", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd", type = Type.RENDER)
public class ESP extends Module
{
    public final List<Entity> collectedEntities;
    private final IntBuffer viewport;
    private final FloatBuffer modelview;
    private final FloatBuffer projection;
    private final FloatBuffer vector;
    public MultiBoxSetting elements;
    public static ColorSetting customColor;
    
    public ESP() {
        this.elements = new MultiBoxSetting("Elements Selection", new String[] { "Box", "Name", "Item", "Health", "Health Bar" });
        this.collectedEntities = new ArrayList<Entity>();
        this.viewport = GLAllocation.createDirectIntBuffer(16);
        this.modelview = GLAllocation.createDirectFloatBuffer(16);
        this.projection = GLAllocation.createDirectFloatBuffer(16);
        this.vector = GLAllocation.createDirectFloatBuffer(4);
        this.add(this.elements, ESP.customColor);
    }
    
    @EventTarget
    public void onRender2D(final EventDisplay event) {
        GL11.glPushMatrix();
        this.collectEntities();
        final float partialTicks = event.ticks;
        final int scaleFactor = ScaledResolution.getScaleFactor();
        final double scaling = scaleFactor / Math.pow(scaleFactor, 2.0);
        GL11.glScaled(scaling, scaling, scaling);
        final RenderManager renderMng = ESP.mc.getRenderManager();
        final EntityRenderer entityRenderer = ESP.mc.entityRenderer;
        final List<Entity> collectedEntities = this.collectedEntities;
        for (final Entity entity : collectedEntities) {
            if (entity instanceof EntityPlayer && RenderUtility.isInViewFrustum(entity)) {
                final double x = MathUtility.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
                final double y = MathUtility.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
                final double z = MathUtility.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
                final double width = entity.width / 1.5;
                final double n = entity.height + 0.2f - (entity.isSneaking() ? 0.2f : 0.0f);
                final AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + n, z + width);
                final Vec3d[] vectors = { new Vec3d(aabb.minX, aabb.minY, aabb.minZ), new Vec3d(aabb.minX, aabb.maxY, aabb.minZ), new Vec3d(aabb.maxX, aabb.minY, aabb.minZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vec3d(aabb.minX, aabb.minY, aabb.maxZ), new Vec3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vec3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ) };
                entityRenderer.setupCameraTransform(partialTicks, 0);
                Vector4f position = null;
                for (Vec3d vector : vectors) {
                    vector = this.project2D(scaleFactor, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
                    if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                        if (position == null) {
                            position = new Vector4f((float)vector.x, (float)vector.y, (float)vector.z, 1.0f);
                        }
                        position.x = (float)Math.min(vector.x, position.x);
                        position.y = (float)Math.min(vector.y, position.y);
                        position.z = (float)Math.max(vector.x, position.z);
                        position.w = (float)Math.max(vector.y, position.w);
                    }
                }
                if (position == null) {
                    continue;
                }
                entityRenderer.setupOverlayRendering();
                final double posX = position.x;
                final double posY = position.y;
                final double endPosX = position.z;
                final double endPosY = position.w;
                if (this.elements.get(0)) {
                    RenderUtility.drawRectNotWH(posX - 1.0, posY, posX + 0.5, endPosY + 0.5, Color.black.getRGB());
                    RenderUtility.drawRectNotWH(posX - 1.0, posY - 0.5, endPosX + 0.5, posY + 0.5 + 0.5, Color.black.getRGB());
                    RenderUtility.drawRectNotWH(endPosX - 0.5 - 0.5, posY, endPosX + 0.5, endPosY + 0.5, Color.black.getRGB());
                    RenderUtility.drawRectNotWH(posX - 1.0, endPosY - 0.5 - 0.5, endPosX + 0.5, endPosY + 0.5, Color.black.getRGB());
                    RenderUtility.drawRectNotWH(posX - 0.5, posY, posX + 0.5 - 0.5, endPosY, ESP.customColor.getColorValue());
                    RenderUtility.drawRectNotWH(posX, endPosY - 0.5, endPosX, endPosY, ESP.customColor.getColorValue());
                    RenderUtility.drawRectNotWH(posX - 0.5, posY, endPosX, posY + 0.5, ESP.customColor.getColorValue());
                    RenderUtility.drawRectNotWH(endPosX - 0.5, posY, endPosX, endPosY, ESP.customColor.getColorValue());
                }
                final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
                final double hpPercentage = entityLivingBase.getHealth() / entityLivingBase.getMaxHealth();
                final double hpHeight2 = (endPosY - posY) * hpPercentage;
                final double hpHeight3 = endPosY - posY;
                final double dif = (endPosX - posX) / 2.0;
                final double textWidth = Fonts.MCR8.getStringWidth(entityLivingBase.getName());
                if (entityLivingBase.getHealth() > 0.0f) {
                    if (this.elements.get(1)) {
                        Fonts.MCR8.drawStringWithOutline(ChatFormatting.stripFormatting(entity.getName()), (float)(posX + dif - textWidth / 2.0) - 1.0f, (float)posY - 20.0f + 15.0f, -1);
                    }
                    if (this.elements.get(4)) {
                        RenderUtility.drawRectNotWH((float)(posX - 3.5), (float)(endPosY + 0.5), (float)(posX - 1.5), (float)(endPosY - hpHeight3 - 0.5), new Color(0, 0, 0, 255).getRGB());
                        RenderUtility.drawVGradientRect((float)(posX - 3.0), (float)endPosY, (float)(posX - 2.0), (float)(endPosY - hpHeight3), Hud.getColor(1).getRGB(), new Color(255, 255, 255).getRGB());
                        RenderUtility.drawRectNotWH(posX - 3.5, posY, posX - 1.5, endPosY - hpHeight2, new Color(0, 0, 0, 255).getRGB());
                    }
                }
                if (!entityLivingBase.getHeldItemMainhand().isEmpty() && this.elements.get(2)) {
                    Fonts.MCR8.drawCenteredStringWithOutline(ChatFormatting.stripFormatting(entityLivingBase.getHeldItemMainhand().getDisplayName()), (float)(posX + (endPosX - posX) / 2.0), (float)(endPosY + 0.5) + 4.0f, -1);
                }
                if (!this.elements.get(3)) {
                    continue;
                }
                Fonts.MCR8.drawStringWithOutline((int)entityLivingBase.getHealth() + "HP", (float)(posX - 4.5) - Fonts.MCR8.getStringWidth((int)entityLivingBase.getHealth() + "HP"), (float)endPosY - hpHeight2 + 4.0, getHealthColor(entityLivingBase, new Color(255, 86, 86).getRGB(), new Color(86, 255, 125).getRGB()));
            }
        }
        GL11.glPopMatrix();
    }
    
    public static int getHealthColor(final EntityLivingBase entity, final int c1, final int c2) {
        final float health = entity.getHealth();
        final float maxHealth = entity.getMaxHealth();
        final float hpPercentage = health / maxHealth;
        final int red = (int)((c2 >> 16 & 0xFF) * hpPercentage + (c1 >> 16 & 0xFF) * (1.0f - hpPercentage));
        final int green = (int)((c2 >> 8 & 0xFF) * hpPercentage + (c1 >> 8 & 0xFF) * (1.0f - hpPercentage));
        final int blue = (int)((c2 & 0xFF) * hpPercentage + (c1 & 0xFF) * (1.0f - hpPercentage));
        return new Color(red, green, blue).getRGB();
    }
    
    private boolean isValid(final Entity entity) {
        final Minecraft mc = ESP.mc;
        return (entity != Minecraft.player || ESP.mc.gameSettings.thirdPersonView != 0) && !entity.isDead && entity instanceof EntityPlayer;
    }
    
    private void collectEntities() {
        this.collectedEntities.clear();
        final List<Entity> playerEntities = ESP.mc.world.loadedEntityList;
        for (final Entity entity : playerEntities) {
            if (this.isValid(entity)) {
                this.collectedEntities.add(entity);
            }
        }
    }
    
    private Vec3d project2D(final int scaleFactor, final double x, final double y, final double z) {
        GL11.glGetFloat(2982, this.modelview);
        GL11.glGetFloat(2983, this.projection);
        GL11.glGetInteger(2978, this.viewport);
        if (GLU.gluProject((float)x, (float)y, (float)z, this.modelview, this.projection, this.viewport, this.vector)) {
            return new Vec3d(this.vector.get(0) / scaleFactor, (Display.getHeight() - this.vector.get(1)) / scaleFactor, this.vector.get(2));
        }
        return null;
    }
    
    static {
        ESP.customColor = new ColorSetting("Custom Color", new Color(16777215).getRGB());
    }
}
