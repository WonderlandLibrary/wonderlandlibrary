package summer.cheat.cheats.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import summer.Summer;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.RenderUtils;
import summer.base.utilities.RotationUtils;
import summer.cheat.cheats.combat.AntiBot;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.render.EventRender2D;
import summer.cheat.eventsystem.events.render.EventRender3D;
import summer.cheat.guiutil.Setting;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class ESP extends Cheats {
    public static String espMode;
    private static Map<EntityPlayer, float[][]> entities = new HashMap<>();

    private final FloatBuffer windowPosition = BufferUtils.createFloatBuffer(4);
    private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final FloatBuffer modelMatrix = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projectionMatrix = GLAllocation.createDirectFloatBuffer(16);
    private final Map<EntityPlayer, float[]> entityPosMap = new HashMap<>();

    private final Setting box;
    private final Setting healthBar;
    private final Setting healthBarCustom;
    public static Setting nametags;
    private final Setting nametagsCustom;
    private final Setting skeletal;
    public static Setting outline;
    private final Setting nametagsHue;
    private final Setting nametagsSaturation;
    public static Setting colorRed;
    public static Setting colorGreen;
    public static Setting colorBlue;
    private final Setting healthBarHue;
    private final Setting healthBarSaturation;
    public static Setting outlineMode;
    public static Setting friendColors;
    public static Setting friendHue;

    public ESP() {
        super("ESP", "Allows you to see entities through walls", Selection.RENDER, false);

        Summer.INSTANCE.settingsManager.Property(box = new Setting("2D Box", this, true));
        Summer.INSTANCE.settingsManager.Property(healthBar = new Setting("Health Bar", this, true));
        Summer.INSTANCE.settingsManager.Property(healthBarCustom = new Setting("Custom Health Color", this, false, healthBar::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(healthBarHue = new Setting("Health Hue", this, 0.8F, 0F, 1.0F, false, healthBarCustom::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(healthBarSaturation = new Setting("Health Sat", this, 1.0F, 0F, 1.0F, false, healthBarCustom::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(nametags = new Setting("Nametags", this, false));
        Summer.INSTANCE.settingsManager.Property(nametagsCustom = new Setting("Custom Name Color", this, false, nametags::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(nametagsHue = new Setting("Nametags Hue", this, 0.8F, 0F, 1.0F, false, nametagsCustom::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(nametagsSaturation = new Setting("Nametags Sat", this, 1.0F, 0F, 1.0F, false, nametagsCustom::getValBoolean));
        ArrayList espType = new ArrayList();
        espType.add("Outline");
        espType.add("Shader");
        Summer.INSTANCE.settingsManager.Property(outline = new Setting("Outlines", this, false));
        Summer.INSTANCE.settingsManager.Property(outlineMode = new Setting("Outline Type", this, "Outline", espType, outline::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(skeletal = new Setting("Skeletal", this, false));
        Summer.INSTANCE.settingsManager.Property(colorRed = new Setting("Red", this, 249, 0, 255, true));
        Summer.INSTANCE.settingsManager.Property(colorGreen = new Setting("Green", this, 255, 0, 255, true));
        Summer.INSTANCE.settingsManager.Property(colorBlue = new Setting("Blue", this, 0, 0, 255, true));
        Summer.INSTANCE.settingsManager.Property(friendColors = new Setting("Friend Colors", this, false));
        Summer.INSTANCE.settingsManager.Property(friendHue = new Setting("Friend Hue", this, 0.8F, 0F, 1.0F, false, friendColors::getValBoolean));
    }

    @EventTarget
    public void onRender2DEvent(EventRender2D event) {
        for (EntityPlayer player : entityPosMap.keySet()) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            float middleX = sr.getScaledWidth() / 2.0F;
            float middleY = sr.getScaledHeight() / 2.0F;
            glPushMatrix();
            float[] positions = entityPosMap.get(player);
            float x = positions[0];
            float x2 = positions[1];
            float y = positions[2];
            float y2 = positions[3];
            if (healthBar.getValBoolean()) {
                Gui.drawRect(x - 2.5, y - 0.5F, x - 0.5F, y2 + 0.5F, 0x96000000);
                float health = player.getHealth();
                float maxHealth = player.getMaxHealth();
                float healthPercentage = health / maxHealth;
                boolean needScissor = health < maxHealth;
                float heightDif = y - y2;
                float healthBarHeight = heightDif * healthPercentage;
                if (needScissor)
                    startScissorBox(event.sr, (int) x - 2, (int) (y2 + healthBarHeight), 2, (int) -healthBarHeight + 1);
                int col = RenderUtils.getColorFromPercentage(health, maxHealth);
                if (healthBarCustom.getValBoolean()) {
                    String mode = HUD.hudStyle.getValString();
                    float sat = healthBarSaturation.getValFloat();
                    Color c = Color.getHSBColor(healthBarHue.getValFloat(), sat, 1.0F);
                    Gui.drawRect(x - 2, y, x - 1, y2, c.getRGB());
                } else Gui.drawRect(x - 2, y, x - 1, y2, col);
                if (needScissor)
                    endScissorBox();
            }
            if (nametags.getValBoolean()) {
                String text = player.getDisplayName().getUnformattedText();
                float xDif = x2 - x;
                float minScale = 0.65F;
                float scale = Math.max(minScale,
                        Math.min(1.0F, 1.0F - (Minecraft.thePlayer.getDistanceToEntity(player) / 100.0F)));
                float yOff = Math.max(0.0F,
                        Math.min(1.0F, Minecraft.thePlayer.getDistanceToEntity(player) / 12.0F));
                float upscale = 1.0F / scale;
                glPushMatrix();
                GL11.glScalef(scale, scale, scale);
                if (nametagsCustom.getValBoolean()) {
                    String text2 = EnumChatFormatting.getTextWithoutFormattingCodes(player.getDisplayName().getUnformattedText());
                    float sat = nametagsSaturation.getValFloat();
                    Color c = Color.getHSBColor(nametagsHue.getValFloat(), sat, 1.0F);
                    if (Summer.INSTANCE.friendManager.isFriend(player.getName()) && friendColors.getValBoolean()) {
                        Color c2 = Color.getHSBColor(friendHue.getValFloat(), sat, 1.0F);
                        Minecraft.fontRendererObj.drawStringWithShadow(text2, (x + xDif / 2.0F) * upscale - Minecraft.fontRendererObj.getStringWidth(text) / 2.0F, (y - 9 + yOff) * upscale, c2.getRGB());
                    } else
                        Minecraft.fontRendererObj.drawStringWithShadow(text2, (x + xDif / 2.0F) * upscale - Minecraft.fontRendererObj.getStringWidth(text) / 2.0F, (y - 9 + yOff) * upscale, c.getRGB());
                } else
                    Minecraft.fontRendererObj.drawStringWithShadow(text, (x + xDif / 2.0F) * upscale - Minecraft.fontRendererObj.getStringWidth(text) / 2.0F, (y - 9 + yOff) * upscale, -1);
                GL11.glScalef(1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
            if (box.getValBoolean()) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                enableAlpha();
                GL11.glLineWidth(1.3F);
                if (Summer.INSTANCE.friendManager.isFriend(player.getName()) && friendColors.getValBoolean()) {
                    Color c2 = Color.getHSBColor(friendHue.getValFloat(), 1.0F, 1.0F);
                    GL11.glColor4f(c2.getRed() / 255F,
                            c2.getGreen() / 255F,
                            c2.getBlue() / 255F, 1.0F);
                } else {
                    GL11.glColor4f(colorRed.getValFloat() / 255F,
                            colorGreen.getValFloat() / 255F,
                            colorBlue.getValFloat() / 255F, 1.0F);
                }
                GL11.glBegin(GL11.GL_LINE_LOOP);
                GL11.glVertex2f(x, y);
                GL11.glVertex2f(x, y2);
                GL11.glVertex2f(x2, y2);
                GL11.glVertex2f(x2, y);
                GL11.glEnd();
                disableAlpha();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
            GL11.glPopMatrix();
        }
    }

    public static void startScissorBox(ScaledResolution sr, int x, int y, int width, int height) {
        int sf = sr.getScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x * sf, (sr.getScaledHeight() - (y + height)) * sf, width * sf, height * sf);
    }

    public static void endScissorBox() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void enableAlpha() {
        GL11.glEnable(GL11.GL_BLEND);
        GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
    }

    public static void disableAlpha() {
        GL11.glDisable(GL11.GL_BLEND);
    }

    @EventTarget
    public void onEventRender3D(EventRender3D e) {
        entities.keySet().removeIf(player -> !Minecraft.theWorld.playerEntities.contains(player));
        if (!entityPosMap.isEmpty())
            entityPosMap.clear();
        if (box.getValBoolean() || healthBar.getValBoolean() || nametags.getValBoolean()) {
            int scaleFactor = e.scaledResolution.getScaleFactor();
            float partialTicks = e.getPartialTicks();
            for (EntityPlayer player : Minecraft.theWorld.playerEntities) {
                if (AntiBot.isBot(player) || player.getDistanceToEntity(Minecraft.thePlayer) < 1.0F)
                    continue;
                glPushMatrix();
                Vec3 vec3 = getVec3(player);
                float posX = (float) (vec3.xCoord - RenderManager.viewerPosX);
                float posY = (float) (vec3.yCoord - RenderManager.viewerPosY);
                float posZ = (float) (vec3.zCoord - RenderManager.viewerPosZ);
                double halfWidth = player.width / 2.0D + 0.18F;
                AxisAlignedBB bb = new AxisAlignedBB(posX - halfWidth, posY, posZ - halfWidth, posX + halfWidth,
                        posY + player.height + 0.18D, posZ + halfWidth);
                double[][] vectors = {{bb.minX, bb.minY, bb.minZ}, {bb.minX, bb.maxY, bb.minZ},
                        {bb.minX, bb.maxY, bb.maxZ}, {bb.minX, bb.minY, bb.maxZ}, {bb.maxX, bb.minY, bb.minZ},
                        {bb.maxX, bb.maxY, bb.minZ}, {bb.maxX, bb.maxY, bb.maxZ}, {bb.maxX, bb.minY, bb.maxZ}};
                Vector3f projection;
                Vector4f position = new Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, -1.0F, -1.0F);
                for (double[] vec : vectors) {
                    projection = project2D((float) vec[0], (float) vec[1], (float) vec[2], scaleFactor);
                    if (projection != null && projection.z >= 0.0F && projection.z < 1.0F) {
                        position.x = Math.min(position.x, projection.x);
                        position.y = Math.min(position.y, projection.y);
                        position.z = Math.max(position.z, projection.x);
                        position.w = Math.max(position.w, projection.y);
                    }
                }
                entityPosMap.put(player, new float[]{position.x, position.z, position.y, position.w});
                GL11.glPopMatrix();
            }
        }

        if (skeletal.getValBoolean()) {
            this.startEnd(true);
            GL11.glEnable(2903);
            GL11.glDisable(2848);
            for (EntityPlayer ent : Minecraft.theWorld.playerEntities) {
                if (AntiBot.isBot(ent) || ent.getDistanceToEntity(Minecraft.thePlayer) < 1.0F)
                    continue;
                drawSkeleton((EventRender3D) e, ent);
            }
            this.startEnd(false);
        }
    }

    private Vector3f project2D(float x, float y, float z, int scaleFactor) {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projectionMatrix);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
        if (GLU.gluProject(x, y, z, modelMatrix, projectionMatrix, viewport, windowPosition)) {
            return new Vector3f(windowPosition.get(0) / scaleFactor,
                    (mc.displayHeight - windowPosition.get(1)) / scaleFactor, windowPosition.get(2));
        }

        return null;
    }

    public static void addEntity(final EntityPlayer e, final ModelPlayer model) {
        entities.put(e, new float[][]{
                {model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ},
                {model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY,
                        model.bipedRightArm.rotateAngleZ},
                {model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ},
                {model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY,
                        model.bipedRightLeg.rotateAngleZ},
                {model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY,
                        model.bipedLeftLeg.rotateAngleZ}});
    }

    private void drawSkeleton(final EventRender3D event, final EntityPlayer e) {
        final Color color = new Color(
                e.getName().equalsIgnoreCase(Minecraft.thePlayer.getName()) ? -6684775 : new Color(16775672).getRGB());
        if (!e.isInvisible()) {
            final float[][] entPos = entities.get(e);
            if (entPos != null && e.isEntityAlive() && !e.isDead && e != Minecraft.thePlayer && !e.isPlayerSleeping()) {
                glPushMatrix();
                GL11.glLineWidth(1.3F);
                if (e.hurtTime > 0) {
                    GL11.glColor3f(150f, 0f, 0f);
                } else
                    GlStateManager.color((float) (color.getRed() / 255), (float) (color.getGreen() / 255),
                            (float) (color.getBlue() / 255), 1.0f);
                final Vec3 vec = this.getVec3(e);
                final double x = vec.xCoord - RenderManager.renderPosX;
                final double y = vec.yCoord - RenderManager.renderPosY;
                final double z = vec.zCoord - RenderManager.renderPosZ;
                GL11.glTranslated(x, y, z);
                final float xOff = e.prevRenderYawOffset
                        + (e.renderYawOffset - e.prevRenderYawOffset) * event.getPartialTicks();
                GL11.glRotatef(-xOff, 0.0f, 1.0f, 0.0f);
                GL11.glTranslated(0.0, 0.0, e.isSneaking() ? -0.235 : 0.0);
                final float yOff = e.isSneaking() ? 0.6f : 0.75f;
                glPushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glTranslated(-0.125, yOff, 0.0);
                if (entPos[3][0] != 0.0f) {
                    GL11.glRotatef(entPos[3][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
                }
                if (entPos[3][1] != 0.0f) {
                    GL11.glRotatef(entPos[3][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
                }
                if (entPos[3][2] != 0.0f) {
                    GL11.glRotatef(entPos[3][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, -yOff, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                glPushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glTranslated(0.125, yOff, 0.0);
                if (entPos[4][0] != 0.0f) {
                    GL11.glRotatef(entPos[4][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
                }
                if (entPos[4][1] != 0.0f) {
                    GL11.glRotatef(entPos[4][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
                }
                if (entPos[4][2] != 0.0f) {
                    GL11.glRotatef(entPos[4][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, -yOff, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glTranslated(0.0, 0.0, e.isSneaking() ? 0.25 : 0.0);
                glPushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glTranslated(0.0, e.isSneaking() ? -0.05 : 0.0, e.isSneaking() ? -0.01725 : 0.0);
                glPushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glTranslated(-0.375, yOff + 0.55, 0.0);
                if (entPos[1][0] != 0.0f) {
                    GL11.glRotatef(entPos[1][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
                }
                if (entPos[1][1] != 0.0f) {
                    GL11.glRotatef(entPos[1][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
                }
                if (entPos[1][2] != 0.0f) {
                    GL11.glRotatef(-entPos[1][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, -0.5, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                glPushMatrix();
                GL11.glTranslated(0.375, yOff + 0.55, 0.0);
                if (entPos[2][0] != 0.0f) {
                    GL11.glRotatef(entPos[2][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
                }
                if (entPos[2][1] != 0.0f) {
                    GL11.glRotatef(entPos[2][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
                }
                if (entPos[2][2] != 0.0f) {
                    GL11.glRotatef(-entPos[2][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, -0.5, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glRotatef(xOff - e.rotationYawHead, 0.0f, 1.0f, 0.0f);
                glPushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glTranslated(0.0, yOff + 0.55, 0.0);
                if (entPos[0][0] != 0.0f) {
                    GL11.glRotatef(entPos[0][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, 0.3, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                GL11.glRotatef(e.isSneaking() ? 25.0f : 0.0f, 1.0f, 0.0f, 0.0f);
                GL11.glTranslated(0.0, e.isSneaking() ? -0.16175 : 0.0, e.isSneaking() ? -0.48025 : 0.0);
                glPushMatrix();
                GL11.glTranslated(0.0, yOff, 0.0);
                GL11.glBegin(3);
                GL11.glVertex3d(-0.125, 0.0, 0.0);
                GL11.glVertex3d(0.125, 0.0, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                glPushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glTranslated(0.0, yOff, 0.0);
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, 0.55, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                glPushMatrix();
                GL11.glTranslated(0.0, yOff + 0.55, 0.0);
                GL11.glBegin(3);
                GL11.glVertex3d(-0.375, 0.0, 0.0);
                GL11.glVertex3d(0.375, 0.0, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }

    private Vec3 getVec3(final EntityPlayer var0) {
        final float timer = mc.timer.renderPartialTicks;
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
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GlStateManager.blendFunc(770, 771);
            GL11.glHint(3154, 4354);
        } else {
            GlStateManager.disableBlend();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
        GlStateManager.depthMask(!revert);
    }

    public void updateSettings() {
        espMode = outlineMode.getValString();
    }

}
