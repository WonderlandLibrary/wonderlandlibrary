package io.github.raze.utilities.collection.visual.esp;

import io.github.raze.utilities.system.Methods;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class EntityESPUtil implements Methods {

    public static void renderImageESP(EntityPlayer player, ResourceLocation image, double renderPosX, double renderPosY, double renderPosZ) {
        GlStateManager.pushMatrix();
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GL11.glDisable(2929);
        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(player), 20.0f,
                Float.MAX_VALUE);
        final double scale = 0.005 * distance;

        GlStateManager.translate(renderPosX, renderPosY, renderPosZ);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.1, -0.1, 0.0);

        mc.getTextureManager().bindTexture(image);

        Gui.drawScaledCustomSizeModalRect(player.width / 2.0f - distance / 3.0f, -player.height - distance,
                0.0f, 0.0f, 1.0, 1.0, 252.0 * (scale / 2.0), 476.0 * (scale / 2.0), 1.0f, 1.0f);
        GL11.glEnable(2929);

        GlStateManager.popMatrix();
    }

    public static void renderBoxESP(EntityLivingBase player, double renderPosX, double renderPosY, double renderPosZ, float redColor, float greenColor, float blueColor, float alphaColor) {
        double width = player.width;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(3.0F);

        GlStateManager.pushMatrix();
        GlStateManager.translate(renderPosX, renderPosY, renderPosZ);
        GlStateManager.rotate(-player.rotationYaw, 0.0F, 1.0F, 0.0F);
		
        double boxLeft = -width / 2.0;
        double boxRight = width / 2.0;
        double boxTop = player.height;
        double boxBottom = 0;
        double boxFront = -width / 2.0;
        double boxBack = width / 2.0;

        GlStateManager.color(redColor, greenColor, blueColor, alphaColor);
        GL11.glBegin(GL11.GL_QUADS);
        // Front face
        GL11.glVertex3d(boxLeft, boxTop, boxFront);
        GL11.glVertex3d(boxRight, boxTop, boxFront);
        GL11.glVertex3d(boxRight, boxBottom, boxFront);
        GL11.glVertex3d(boxLeft, boxBottom, boxFront);
        // Back face
        GL11.glVertex3d(boxLeft, boxTop, boxBack);
        GL11.glVertex3d(boxRight, boxTop, boxBack);
        GL11.glVertex3d(boxRight, boxBottom, boxBack);
        GL11.glVertex3d(boxLeft, boxBottom, boxBack);
        // Top face
        GL11.glVertex3d(boxLeft, boxTop, boxFront);
        GL11.glVertex3d(boxRight, boxTop, boxFront);
        GL11.glVertex3d(boxRight, boxTop, boxBack);
        GL11.glVertex3d(boxLeft, boxTop, boxBack);
        // Bottom face
        GL11.glVertex3d(boxLeft, boxBottom, boxFront);
        GL11.glVertex3d(boxRight, boxBottom, boxFront);
        GL11.glVertex3d(boxRight, boxBottom, boxBack);
        GL11.glVertex3d(boxLeft, boxBottom, boxBack);
        // Left face
        GL11.glVertex3d(boxLeft, boxTop, boxFront);
        GL11.glVertex3d(boxLeft, boxTop, boxBack);
        GL11.glVertex3d(boxLeft, boxBottom, boxBack);
        GL11.glVertex3d(boxLeft, boxBottom, boxFront);
        // Right face
        GL11.glVertex3d(boxRight, boxTop, boxFront);
        GL11.glVertex3d(boxRight, boxTop, boxBack);
        GL11.glVertex3d(boxRight, boxBottom, boxBack);
        GL11.glVertex3d(boxRight, boxBottom, boxFront);
        GL11.glEnd();

        GlStateManager.enableDepth();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    public static void renderOtherBoxESP(EntityLivingBase player, double renderPosX, double renderPosY, double renderPosZ, float redColor, float greenColor, float blueColor) {
        double width = player.width;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2.0F);

        GlStateManager.pushMatrix();
        GlStateManager.translate(renderPosX, renderPosY, renderPosZ);
        GlStateManager.rotate(-player.rotationYaw, 0.0F, 1.0F, 0.0F);
        double boxLeft = -width / 2.0;
        double boxRight = width / 2.0;
        double boxTop = player.height;
        double boxBottom = 0;
        double boxFront = -width / 2.0;
        double boxBack = width / 2.0;

        GlStateManager.color(redColor, greenColor, blueColor, 1.0f);
        GL11.glBegin(GL11.GL_LINES);

        // Draw the outline
        GL11.glVertex3d(boxLeft, boxTop, boxFront);
        GL11.glVertex3d(boxRight, boxTop, boxFront);

        GL11.glVertex3d(boxRight, boxTop, boxFront);
        GL11.glVertex3d(boxRight, boxBottom, boxFront);

        GL11.glVertex3d(boxRight, boxBottom, boxFront);
        GL11.glVertex3d(boxLeft, boxBottom, boxFront);

        GL11.glVertex3d(boxLeft, boxBottom, boxFront);
        GL11.glVertex3d(boxLeft, boxTop, boxFront);

        GL11.glVertex3d(boxLeft, boxTop, boxBack);
        GL11.glVertex3d(boxRight, boxTop, boxBack);

        GL11.glVertex3d(boxRight, boxTop, boxBack);
        GL11.glVertex3d(boxRight, boxBottom, boxBack);

        GL11.glVertex3d(boxRight, boxBottom, boxBack);
        GL11.glVertex3d(boxLeft, boxBottom, boxBack);

        GL11.glVertex3d(boxLeft, boxBottom, boxBack);
        GL11.glVertex3d(boxLeft, boxTop, boxBack);

        GL11.glVertex3d(boxLeft, boxTop, boxFront);
        GL11.glVertex3d(boxLeft, boxTop, boxBack);

        GL11.glVertex3d(boxRight, boxTop, boxFront);
        GL11.glVertex3d(boxRight, boxTop, boxBack);

        GL11.glVertex3d(boxRight, boxBottom, boxFront);
        GL11.glVertex3d(boxRight, boxBottom, boxBack);

        GL11.glVertex3d(boxLeft, boxBottom, boxFront);
        GL11.glVertex3d(boxLeft, boxBottom, boxBack);

        GL11.glEnd();

        GlStateManager.enableDepth();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    public static void render2DESP(EntityLivingBase player, double renderPosX, double renderPosY, double renderPosZ, float redColor, float greenColor, float blueColor) {
        double width = player.width + 0.5;
        double height = player.height + 0.14;

        double outerThickness = 0.001;
        double innerThickness = 0.001;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();

        GlStateManager.translate(renderPosX, renderPosY, renderPosZ);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);

        GL11.glLineWidth(2.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        float prevRed = GL11.glGetFloat(GL11.GL_CURRENT_COLOR);
        float prevGreen = GL11.glGetFloat(GL11.GL_CURRENT_COLOR);
        float prevBlue = GL11.glGetFloat(GL11.GL_CURRENT_COLOR);

        // Draw outer black box
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2d(-width / 2 - outerThickness, -outerThickness);
        GL11.glVertex2d(-width / 2 - outerThickness, height + outerThickness);
        GL11.glVertex2d(width / 2 + outerThickness, height + outerThickness);
        GL11.glVertex2d(width / 2 + outerThickness, -outerThickness);
        GL11.glEnd();

        // Draw inner black box
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2d(-width / 2 + innerThickness, innerThickness);
        GL11.glVertex2d(-width / 2 + innerThickness, height - innerThickness);
        GL11.glVertex2d(width / 2 - innerThickness, height - innerThickness);
        GL11.glVertex2d(width / 2 - innerThickness, innerThickness);
        GL11.glEnd();

        // Draw main box
        GL11.glColor4f(redColor, greenColor, blueColor, 1.0f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2d(-width / 2, 0);
        GL11.glVertex2d(-width / 2, height);
        GL11.glVertex2d(width / 2, height);
        GL11.glVertex2d(width / 2, 0);
        GL11.glEnd();

        GL11.glColor4f(prevRed, prevGreen, prevBlue, 1.0f);

        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
