// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.render;

import Lavish.utils.GL.GLUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelPlayer;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Vector3f;
import net.minecraft.util.Vec3;
import java.util.Iterator;
import org.lwjgl.util.vector.Vector4f;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import Lavish.event.events.EventRender3D;
import net.minecraft.entity.Entity;
import java.awt.Color;
import Lavish.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import Lavish.event.events.EventRenderGUI;
import Lavish.event.Event;
import net.minecraft.client.renderer.GLAllocation;
import java.util.WeakHashMap;
import org.lwjgl.BufferUtils;
import Lavish.modules.Category;
import java.util.HashMap;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import Lavish.modules.Module;

public class ESP extends Module
{
    public static String espMode;
    private static Map<EntityPlayer, float[][]> entities;
    private final FloatBuffer windowPosition;
    private final Map playerRotationMap;
    private final IntBuffer viewport;
    private final FloatBuffer modelMatrix;
    private final FloatBuffer projectionMatrix;
    private final Map<EntityPlayer, float[]> entityPosMap;
    public int colorastolfo;
    private final boolean box = true;
    private final boolean healthBar = true;
    private final boolean healthBarCustom = false;
    public static boolean nametags;
    private final boolean nametagsCustom = false;
    private final boolean skeletal = true;
    public static boolean outline;
    private final float nametagsHue = 0.8f;
    private final float nametagsSaturation = 1.0f;
    public static float colorRed;
    public static float colorGreen;
    public static float colorBlue;
    private final float healthBarHue = 0.8f;
    private final float healthBarSaturation = 1.0f;
    public static boolean outlineMode;
    public static boolean friendColors;
    public static float friendHue;
    
    static {
        ESP.entities = new HashMap<EntityPlayer, float[][]>();
        ESP.nametags = true;
        ESP.outline = true;
        ESP.colorRed = 200.0f;
        ESP.colorGreen = 0.0f;
        ESP.colorBlue = 255.0f;
        ESP.outlineMode = false;
        ESP.friendColors = true;
        ESP.friendHue = 0.8f;
    }
    
    public ESP() {
        super("ESP", 0, true, Category.Render, "See players through walls");
        this.windowPosition = BufferUtils.createFloatBuffer(4);
        this.playerRotationMap = new WeakHashMap();
        this.viewport = GLAllocation.createDirectIntBuffer(16);
        this.modelMatrix = GLAllocation.createDirectFloatBuffer(16);
        this.projectionMatrix = GLAllocation.createDirectFloatBuffer(16);
        this.entityPosMap = new HashMap<EntityPlayer, float[]>();
    }
    
    @Override
    public void onEvent(final Event e) {
        if (ESP.mc.thePlayer == null) {
            return;
        }
        if (e instanceof EventRenderGUI) {
            for (final EntityPlayer player2 : this.entityPosMap.keySet()) {
                final ScaledResolution sr = new ScaledResolution(ESP.mc);
                final float middleX = sr.getScaledWidth() / 2.0f;
                final float middleY = sr.getScaledHeight() / 2.0f;
                GL11.glPushMatrix();
                final float[] positions = this.entityPosMap.get(player2);
                final float x = positions[0];
                final float x2 = positions[1];
                final float y = positions[2];
                final float y2 = positions[3];
                Gui.drawRect(x - 2.5, y - 0.5f, x - 0.5f, y2 + 0.5f, -16711936);
                final float health = player2.getHealth();
                final float maxHealth = player2.getMaxHealth();
                final float healthPercentage = health / maxHealth;
                final boolean needScissor = health < maxHealth;
                final float heightDif = y - y2;
                final float healthBarHeight = heightDif * healthPercentage;
                if (needScissor) {
                    startScissorBox(((EventRenderGUI)e).sr, (int)x - 2, (int)(y2 + healthBarHeight), 2, (int)(-healthBarHeight) + 1);
                }
                final int col = RenderUtils.getColorFromPercentage(health, maxHealth);
                final float sat = 1.0f;
                final Color c = Color.getHSBColor(0.8f, sat, -1.6711936E7f);
                Gui.drawRect(x - 2.0f, y, x - 1.0f, y2, -16711936);
                if (needScissor) {
                    endScissorBox();
                }
                if (ESP.nametags) {
                    final String text = player2.getDisplayName().getUnformattedText();
                    final float xDif = x2 - x;
                    final float minScale = 0.65f;
                    final float scale = Math.max(minScale, Math.min(1.0f, 1.0f - ESP.mc.thePlayer.getDistanceToEntity(player2) / 100.0f));
                    final float yOff = Math.max(0.0f, Math.min(1.0f, ESP.mc.thePlayer.getDistanceToEntity(player2) / 12.0f));
                    final float upscale = 1.0f / scale;
                    GL11.glPushMatrix();
                    GL11.glScalef(scale, scale, scale);
                    ESP.mc.fontRendererObj.drawStringWithShadow(text, (x + xDif / 2.0f) * upscale - ESP.mc.fontRendererObj.getStringWidth(text) / 2.0f, (y - 9.0f + yOff) * upscale, -1);
                    GL11.glScalef(1.0f, 1.0f, 1.0f);
                    GL11.glPopMatrix();
                }
                GL11.glDisable(3553);
                enableAlpha();
                GL11.glLineWidth(1.3f);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glBegin(2);
                GL11.glVertex2f(x, y);
                GL11.glVertex2f(x, y2);
                GL11.glVertex2f(x2, y2);
                GL11.glVertex2f(x2, y);
                GL11.glEnd();
                disableAlpha();
                GL11.glEnable(3553);
                GL11.glPopMatrix();
            }
        }
        if (e instanceof EventRender3D) {
            ESP.entities.keySet().removeIf(player -> !ESP.mc.theWorld.playerEntities.contains(player));
            if (!this.entityPosMap.isEmpty()) {
                this.entityPosMap.clear();
            }
            final int scaleFactor = ((EventRender3D)e).scaledResolution.getScaleFactor();
            final float partialTicks = ((EventRender3D)e).getPartialTicks();
            for (final EntityPlayer player3 : ESP.mc.theWorld.playerEntities) {
                if (player3 == null || ESP.mc.thePlayer.isDead) {
                    return;
                }
                if (player3.getDistanceToEntity(ESP.mc.thePlayer) < 1.0f) {
                    continue;
                }
                GL11.glPushMatrix();
                final Vec3 vec3 = this.getVec3(player3);
                final float posX = (float)(vec3.xCoord - RenderManager.viewerPosX);
                final float posY = (float)(vec3.yCoord - RenderManager.viewerPosY);
                final float posZ = (float)(vec3.zCoord - RenderManager.viewerPosZ);
                final double halfWidth = player3.width / 2.0 + 0.18000000715255737;
                final AxisAlignedBB bb = new AxisAlignedBB(posX - halfWidth, posY, posZ - halfWidth, posX + halfWidth, posY + player3.height + 0.18, posZ + halfWidth);
                final double[][] vectors = { { bb.minX, bb.minY, bb.minZ }, { bb.minX, bb.maxY, bb.minZ }, { bb.minX, bb.maxY, bb.maxZ }, { bb.minX, bb.minY, bb.maxZ }, { bb.maxX, bb.minY, bb.minZ }, { bb.maxX, bb.maxY, bb.minZ }, { bb.maxX, bb.maxY, bb.maxZ }, { bb.maxX, bb.minY, bb.maxZ } };
                final Vector4f position = new Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, -1.0f, -1.0f);
                double[][] array;
                for (int length = (array = vectors).length, i = 0; i < length; ++i) {
                    final double[] vec4 = array[i];
                    final Vector3f projection = this.project2D((float)vec4[0], (float)vec4[1], (float)vec4[2], scaleFactor);
                    if (projection != null && projection.z >= 0.0f && projection.z < 1.0f) {
                        position.x = Math.min(position.x, projection.x);
                        position.y = Math.min(position.y, projection.y);
                        position.z = Math.max(position.z, projection.x);
                        position.w = Math.max(position.w, projection.y);
                    }
                }
                this.entityPosMap.put(player3, new float[] { position.x, position.z, position.y, position.w });
                GL11.glPopMatrix();
            }
        }
    }
    
    public static void startScissorBox(final ScaledResolution sr, final int x, final int y, final int width, final int height) {
        final int sf = sr.getScaleFactor();
        GL11.glEnable(3089);
        GL11.glScissor(x * sf, (sr.getScaledHeight() - (y + height)) * sf, width * sf, height * sf);
    }
    
    public static void endScissorBox() {
        GL11.glDisable(3089);
    }
    
    public static void enableAlpha() {
        GL11.glEnable(3042);
        GL14.glBlendFuncSeparate(770, 771, 1, 0);
    }
    
    public static void disableAlpha() {
        GL11.glDisable(3042);
    }
    
    private Vector3f project2D(final float x, final float y, final float z, final int scaleFactor) {
        GL11.glGetFloat(2982, this.modelMatrix);
        GL11.glGetFloat(2983, this.projectionMatrix);
        GL11.glGetInteger(2978, this.viewport);
        if (GLU.gluProject(x, y, z, this.modelMatrix, this.projectionMatrix, this.viewport, this.windowPosition)) {
            return new Vector3f(this.windowPosition.get(0) / scaleFactor, (ESP.mc.displayHeight - this.windowPosition.get(1)) / scaleFactor, this.windowPosition.get(2));
        }
        return null;
    }
    
    public static void addEntity(final EntityPlayer e, final ModelPlayer model) {
        ESP.entities.put(e, new float[][] { { model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ }, { model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ }, { model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ }, { model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ }, { model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ } });
    }
    
    private Vec3 getVec3(final EntityPlayer var0) {
        final float timer = ESP.mc.timer.renderPartialTicks;
        final double x = var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * timer;
        final double y = var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * timer;
        final double z = var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * timer;
        return new Vec3(x, y, z);
    }
    
    private void startEnd(final boolean revert) {
        if (revert) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GL11.glEnable(2848);
            GlStateManager.disableDepth();
            GL11.glDisable(3553);
            GlStateManager.blendFunc(770, 771);
            GL11.glHint(3154, 4354);
        }
        else {
            GlStateManager.disableBlend();
            GL11.glEnable(3553);
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
        GlStateManager.depthMask(!revert);
    }
    
    private void setupRender(final boolean start) {
        final boolean smooth = true;
        if (start) {
            if (smooth) {
                GLUtils.startSmooth();
            }
            else {
                GL11.glDisable(2848);
            }
            GL11.glDisable(2929);
            GL11.glDisable(3553);
        }
        else {
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            if (smooth) {
                GLUtils.endSmooth();
            }
        }
        GL11.glDepthMask(!start);
    }
    
    private boolean contain(final EntityPlayer var0) {
        return !ESP.mc.theWorld.playerEntities.contains(var0);
    }
}
