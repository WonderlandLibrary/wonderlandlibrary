package io.github.raze.utilities.collection.visual.esp;

import io.github.raze.utilities.system.Methods;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class ChestESPUtil implements Methods {

    public static void renderBoxESP(double renderPosX, double renderPosY, double renderPosZ, float redColor, float greenColor, float blueColor, float alphaColor) {
        double width = 1.0;
        double height = 0.875;
        double depth = 1.0;

        double x = renderPosX + 0.5;
        double z = renderPosZ + 0.5;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(3.0F);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, renderPosY, z);
        GlStateManager.scale(1.0, 1.0, 1.0);
        double boxLeft = -width / 2.0;
        double boxRight = width / 2.0;
        double boxBottom = 0;
        double boxFront = -depth / 2.0;
        double boxBack = depth / 2.0;

        GlStateManager.color(redColor, greenColor, blueColor, alphaColor);
        GL11.glBegin(GL11.GL_QUADS);
        // Front face
        GL11.glVertex3f((float) boxLeft, (float) height, (float) boxFront);
        GL11.glVertex3d(boxRight, height, boxFront);
        GL11.glVertex3d(boxRight, boxBottom, boxFront);
        GL11.glVertex3d(boxLeft, boxBottom, boxFront);
        // Back face
        GL11.glVertex3d(boxLeft, height, boxBack);
        GL11.glVertex3d(boxRight, height, boxBack);
        GL11.glVertex3d(boxRight, boxBottom, boxBack);
        GL11.glVertex3d(boxLeft, boxBottom, boxBack);
        // Top face
        GL11.glVertex3f((float) boxLeft, (float) height, (float) boxFront);
        GL11.glVertex3d(boxRight, height, boxFront);
        GL11.glVertex3d(boxRight, height, boxBack);
        GL11.glVertex3d(boxLeft, height, boxBack);
        // Bottom face
        GL11.glVertex3d(boxLeft, boxBottom, boxFront);
        GL11.glVertex3d(boxRight, boxBottom, boxFront);
        GL11.glVertex3d(boxRight, boxBottom, boxBack);
        GL11.glVertex3d(boxLeft, boxBottom, boxBack);
        // Left face
        GL11.glVertex3f((float) boxLeft, (float) height, (float) boxFront);
        GL11.glVertex3d(boxLeft, height, boxBack);
        GL11.glVertex3d(boxLeft, boxBottom, boxBack);
        GL11.glVertex3d(boxLeft, boxBottom, boxFront);
        // Right face
        GL11.glVertex3d(boxRight, height, boxFront);
        GL11.glVertex3d(boxRight, height, boxBack);
        GL11.glVertex3d(boxRight, boxBottom, boxBack);
        GL11.glVertex3d(boxRight, boxBottom, boxFront);
        GL11.glEnd();

        GlStateManager.color(redColor, greenColor, blueColor, 1.0f);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3f((float) boxLeft, (float) height, (float) boxFront);
        GL11.glVertex3d(boxRight, height, boxFront);

        GL11.glVertex3d(boxRight, height, boxFront);
        GL11.glVertex3d(boxRight, boxBottom, boxFront);

        GL11.glVertex3d(boxRight, boxBottom, boxFront);
        GL11.glVertex3d(boxLeft, boxBottom, boxFront);

        GL11.glVertex3d(boxLeft, boxBottom, boxFront);
        GL11.glVertex3f((float) boxLeft, (float) height, (float) boxFront);

        GL11.glVertex3d(boxLeft, height, boxBack);
        GL11.glVertex3d(boxRight, height, boxBack);

        GL11.glVertex3d(boxRight, height, boxBack);
        GL11.glVertex3d(boxRight, boxBottom, boxBack);

        GL11.glVertex3d(boxRight, boxBottom, boxBack);
        GL11.glVertex3d(boxLeft, boxBottom, boxBack);

        GL11.glVertex3d(boxLeft, boxBottom, boxBack);
        GL11.glVertex3d(boxLeft, height, boxBack);

        GL11.glVertex3f((float) boxLeft, (float) height, (float) boxFront);
        GL11.glVertex3d(boxLeft, height, boxBack);

        GL11.glVertex3d(boxRight, height, boxFront);
        GL11.glVertex3d(boxRight, height, boxBack);

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

    public static void renderOtherBoxESP(double renderPosX, double renderPosY, double renderPosZ, float redColor, float greenColor, float blueColor) {
        double width = 1.0;
        double height = 0.875;
        double depth = 1.0;

        double x = renderPosX + 0.5;
        double z = renderPosZ + 0.5;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2.0F);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, renderPosY, z);
        GlStateManager.scale(1.0, 1.0, 1.0);
        double boxLeft = -width / 2.0;
        double boxRight = width / 2.0;
        double boxBottom = 0;
        double boxFront = -depth / 2.0;
        double boxBack = depth / 2.0;

        GlStateManager.color(redColor, greenColor, blueColor, 1.0f);
        GL11.glBegin(GL11.GL_LINES);
        // Draw the outline
        GL11.glVertex3f((float) boxLeft, (float) height, (float) boxFront);
        GL11.glVertex3d(boxRight, height, boxFront);

        GL11.glVertex3d(boxRight, height, boxFront);
        GL11.glVertex3d(boxRight, boxBottom, boxFront);

        GL11.glVertex3d(boxRight, boxBottom, boxFront);
        GL11.glVertex3d(boxLeft, boxBottom, boxFront);

        GL11.glVertex3d(boxLeft, boxBottom, boxFront);
        GL11.glVertex3f((float) boxLeft, (float) height, (float) boxFront);

        GL11.glVertex3d(boxLeft, height, boxBack);
        GL11.glVertex3d(boxRight, height, boxBack);

        GL11.glVertex3d(boxRight, height, boxBack);
        GL11.glVertex3d(boxRight, boxBottom, boxBack);

        GL11.glVertex3d(boxRight, boxBottom, boxBack);
        GL11.glVertex3d(boxLeft, boxBottom, boxBack);

        GL11.glVertex3d(boxLeft, boxBottom, boxBack);
        GL11.glVertex3d(boxLeft, height, boxBack);

        GL11.glVertex3f((float) boxLeft, (float) height, (float) boxFront);
        GL11.glVertex3d(boxLeft, height, boxBack);

        GL11.glVertex3d(boxRight, height, boxFront);
        GL11.glVertex3d(boxRight, height, boxBack);

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

    public static void render2DESP(double renderPosX, double renderPosY, double renderPosZ, float redColor, float greenColor, float blueColor) {
        double width = 1.0;
        double height = 0.875;

        double outerThickness = 0.001;
        double innerThickness = 0.001;

        double x = renderPosX + 0.5;
        double z = renderPosZ + 0.5;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();

        GlStateManager.translate(x, renderPosY, z);
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
