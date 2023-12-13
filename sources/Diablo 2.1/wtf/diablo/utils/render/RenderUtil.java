package wtf.diablo.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.ResourceLocation;
import org.json.JSONObject;
import org.lwjgl.opengl.GL11;
import wtf.diablo.Diablo;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;

import static org.lwjgl.opengl.GL11.*;
import static net.minecraft.client.renderer.GlStateManager.*;
import static wtf.diablo.utils.Util.mc;

public class RenderUtil {
    public static boolean isHovered(int mouseX, int mouseY, double x, double y, double width, double height) {
        return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
    }

    public static void prepareScissorBox(double x, double y, double x2, double y2) {
        ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
        int factor = scale.getScaleFactor();
        GL11.glScissor((int) (x * (float) factor), (int) (((float) scale.getScaledHeight() - y2) * (float) factor), (int) ((x2 - x) * (float) factor), (int) ((y2 - y) * (float) factor));
    }

    public static void drawRoundedRectangle(double left, double top, double right, double bottom, double radius, int color) {
        glScaled(0.5D, 0.5D, 0.5D);
        left *= 2.0D;
        top *= 2.0D;
        right *= 2.0D;
        bottom *= 2.0D;
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        enableBlend();
        glColor(color);
        glBegin(9);

        int i;
        for (i = 0; i <= 90; i += 1)
            glVertex2d(left + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, top + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i += 1)
            glVertex2d(left + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, bottom - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i += 1)
            glVertex2d(right - radius + Math.sin(i * Math.PI / 180.0D) * radius, bottom - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i += 1)
            glVertex2d(right - radius + Math.sin(i * Math.PI / 180.0D) * radius, top + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glScaled(2.0D, 2.0D, 2.0D);
        glColor4d(1, 1, 1, 1);
    }

    public static boolean antialiiasing = false;

    public static void f(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        drawRect(x, y, x2, y2, col2);
        float f = (col1 >> 24 & 0xFF) / 255.0f;
        float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static int getRainbow(int speed, int offset, float s) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, s, 1f).getRGB();

    }

    public static void drawRect(float x, float y, float width, float height, int col1) {
        float f = (col1 >> 24 & 0xFF) / 255.0f;
        float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(7);
        GL11.glVertex2d(width, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, height);
        GL11.glVertex2d(width, height);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }


    public static void drawRectAlpha(float g, float h, float i, float j, int col1,float alpha) {
        float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glColor4f(f2, f3, f4, alpha);
        GL11.glBegin(7);
        GL11.glVertex2d(i, h);
        GL11.glVertex2d(g, h);
        GL11.glVertex2d(g, j);
        GL11.glVertex2d(i, j);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawRect(float x, float y, float x1, float y1) {
        GL11.glBegin(7);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
    }

    public static void startDrawing() {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(false);
    }

    public static void stopDrawing() {
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static double interpolate(double prev, double cur, float delta) {
        return prev + (cur - prev) * delta;
    }

    public static void drawBoundingBox(AxisAlignedBB boundingBox) {
        //TODO: FIX THIS
        /*
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer tessellatorRenderer = Tessellator.getInstance().getWorldRenderer();
        tessellatorRenderer.startDrawingQuads();
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        tessellator.draw();
        tessellatorRenderer.startDrawingQuads();
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        tessellator.draw();
        tessellatorRenderer.startDrawingQuads();
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        tessellator.draw();
        tessellatorRenderer.startDrawingQuads();
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        tessellator.draw();
        tessellatorRenderer.startDrawingQuads();
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        tessellator.draw();
        tessellatorRenderer.startDrawingQuads();
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        tessellatorRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        tessellator.draw();

         */
    }

    public static void outlineOne() {
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(3.0f);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glLineWidth(3.8f);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }

    public static Color transparency(int color, double alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * c.getRed();
        float g = 0.003921569f * c.getGreen();
        float b = 0.003921569f * c.getBlue();
        return new Color(r, g, b, (float)alpha);
    }

    public static void outlineTwo() {
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }

    public static void outlineThree() {
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }

    public static void outlineFour() {
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }

    public static void outlineFive() {
        GL11.glPolygonOffset(1.0f, 2000000.0f);
        GL11.glDisable(10754);
        GL11.glDisable(2960);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();
    }

    public static void drawFineBorderedRect(int x, int y, int x1, int y1, int bord, int color) {
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2;
        y *= 2;
        x1 *= 2;
        y1 *= 2;
        drawRect(x + 1, y + 1, x1, y1, color);
        drawVerticalLine(x, y, y1, bord);
        drawVerticalLine(x1, y, y1, bord);
        drawHorizontalLine(x + 1, y, x1, bord);
        drawHorizontalLine(x, y1, x1 + 1, bord);
        GL11.glScaled(2.0, 2.0, 2.0);
    }

    public static void drawVerticalLine(int x, int y, int height, int color) {
        drawRect(x, y, x + 1, height, color);
    }

    public static void filledBox(final AxisAlignedBB aa, final int color) {
        //TODO: FIX THIS
        /*
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        final Tessellator var15 = Tessellator.getInstance();
        final WorldRenderer t = var15.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        disableLighting();
        GlStateManager.color(var12, var13, var14, var11);
        final byte draw = 7;
        t.startDrawing(draw);
        t.addVertex(aa.minX, aa.minY, aa.minZ);
        t.addVertex(aa.minX, aa.maxY, aa.minZ);
        t.addVertex(aa.maxX, aa.minY, aa.minZ);
        t.addVertex(aa.maxX, aa.maxY, aa.minZ);
        t.addVertex(aa.maxX, aa.minY, aa.maxZ);
        t.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        t.addVertex(aa.minX, aa.minY, aa.maxZ);
        t.addVertex(aa.minX, aa.maxY, aa.maxZ);
        var15.draw();
        t.startDrawing(draw);
        t.addVertex(aa.maxX, aa.maxY, aa.minZ);
        t.addVertex(aa.maxX, aa.minY, aa.minZ);
        t.addVertex(aa.minX, aa.maxY, aa.minZ);
        t.addVertex(aa.minX, aa.minY, aa.minZ);
        t.addVertex(aa.minX, aa.maxY, aa.maxZ);
        t.addVertex(aa.minX, aa.minY, aa.maxZ);
        t.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        t.addVertex(aa.maxX, aa.minY, aa.maxZ);
        var15.draw();
        t.startDrawing(draw);
        t.addVertex(aa.minX, aa.maxY, aa.minZ);
        t.addVertex(aa.maxX, aa.maxY, aa.minZ);
        t.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        t.addVertex(aa.minX, aa.maxY, aa.maxZ);
        t.addVertex(aa.minX, aa.maxY, aa.minZ);
        t.addVertex(aa.minX, aa.maxY, aa.maxZ);
        t.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        t.addVertex(aa.maxX, aa.maxY, aa.minZ);
        var15.draw();
        t.startDrawing(draw);
        t.addVertex(aa.minX, aa.minY, aa.minZ);
        t.addVertex(aa.maxX, aa.minY, aa.minZ);
        t.addVertex(aa.maxX, aa.minY, aa.maxZ);
        t.addVertex(aa.minX, aa.minY, aa.maxZ);
        t.addVertex(aa.minX, aa.minY, aa.minZ);
        t.addVertex(aa.minX, aa.minY, aa.maxZ);
        t.addVertex(aa.maxX, aa.minY, aa.maxZ);
        t.addVertex(aa.maxX, aa.minY, aa.minZ);
        var15.draw();
        t.startDrawing(draw);
        t.addVertex(aa.minX, aa.minY, aa.minZ);
        t.addVertex(aa.minX, aa.maxY, aa.minZ);
        t.addVertex(aa.minX, aa.minY, aa.maxZ);
        t.addVertex(aa.minX, aa.maxY, aa.maxZ);
        t.addVertex(aa.maxX, aa.minY, aa.maxZ);
        t.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        t.addVertex(aa.maxX, aa.minY, aa.minZ);
        t.addVertex(aa.maxX, aa.maxY, aa.minZ);
        var15.draw();
        t.startDrawing(draw);
        t.addVertex(aa.minX, aa.maxY, aa.maxZ);
        t.addVertex(aa.minX, aa.minY, aa.maxZ);
        t.addVertex(aa.minX, aa.maxY, aa.minZ);
        t.addVertex(aa.minX, aa.minY, aa.minZ);
        t.addVertex(aa.maxX, aa.maxY, aa.minZ);
        t.addVertex(aa.maxX, aa.minY, aa.minZ);
        t.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        t.addVertex(aa.maxX, aa.minY, aa.maxZ);
        var15.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        enableLighting();
        GlStateManager.depthMask(true);
        */
    }

    public static void drawHorizontalLine(int x, int y, int width, int color) {
        drawRect(x, y, width, y + 1, color);
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
        enableGL2D();
        x *= 2.0f;
        x1 *= 2.0f;
        y *= 2.0f;
        y1 *= 2.0f;
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(x, y, y1, borderC);
        drawVLine(x1 - 1.0f, y, y1, borderC);
        drawHLine(x, x1 - 1.0f, y, borderC);
        drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
        drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }

    public static void drawHLine(float x, float y, float x1, int y1) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }
        drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }

    public static void drawHLine(float x, float y, float x1, int y1, int y2) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
    }

    public static void drawEsp(Entity ent, int color) {
        //TODO: FIX THIS
        //boundingBox(ent, ent.lastTickPosX - Minecraft.getRenderManager().viewerPosX, ent.lastTickPosY -  Minecraft.getRenderManager().viewerPosY, ent.lastTickPosZ -  Minecraft.getRenderManager().viewerPosZ, color);

    }

    public static void boundingBox(Entity entity, double x, double y, double z, int color) {
        GL11.glLineWidth(1.0f);
        AxisAlignedBB var11 = entity.getEntityBoundingBox();
        AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entity.posX + x, var11.minY - entity.posY + y, var11.minZ - entity.posZ + z, var11.maxX - entity.posX + x, var11.maxY - entity.posY + y, var11.maxZ - entity.posZ + z);
        if (color != 0) {
            GlStateManager.disableDepth();
            filledBox(var12, color);
            disableLighting();
            //TODO: FIX THIS
            enableLighting();
            GlStateManager.enableDepth();
        }
    }

    public static void enableLighting() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glMatrixMode(5890);
        GL11.glLoadIdentity();
        final float var3 = 0.0039063f;
        GL11.glScalef(var3, var3, var3);
        GL11.glTranslatef(8.0f, 8.0f, 8.0f);
        GL11.glMatrixMode(5888);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10242, 10496);
        GL11.glTexParameteri(3553, 10243, 10496);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void disableLighting() {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
    }

    public static Color glColor(int color, float alpha) {
        float red = (color >> 16 & 0xFF) / 255.0f;
        float green = (color >> 8 & 0xFF) / 255.0f;
        float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }

    public static void drawImage(double x, double y, int width, int height, ResourceLocation rec) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().getTextureManager().bindTexture(rec);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        Gui.drawModalRectWithCustomSizedTexture(x,y,0,0,width,height,width,height);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    public static void drawImage(double x, double y, int width, int height, ResourceLocation rec, float opacity) {
        GlStateManager.pushMatrix();
        GlStateManager.color(opacity,opacity,opacity);
        Minecraft.getMinecraft().getTextureManager().bindTexture(rec);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        Gui.drawModalRectWithCustomSizedTexture(x,y,0,0,width,height,width,height);
        GlStateManager.popMatrix();
    }
    public static void drawRoundedImage(double x, double y, int width, int height, ResourceLocation rec) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().getTextureManager().bindTexture(rec);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        Gui.drawModalRectWithCustomSizedTexture(x,y,0,0,width,height,width,height);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    public static void drawRoundedImage(double x, double y, int width, int height, ResourceLocation rec, float opacity) {
        GlStateManager.pushMatrix();
        GlStateManager.color(opacity,opacity,opacity);
        Minecraft.getMinecraft().getTextureManager().bindTexture(rec);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        Gui.drawModalRectWithCustomSizedTexture(x,y,0,0,width,height,width,height);
        GlStateManager.popMatrix();
    }



    public static void drawOutlineRect(double x, double y, double x2, double y2, double scale, int color) {
        Gui.drawRect(x, y, x + scale, y2, color);
        Gui.drawRect(x, y, x2, y + scale, color);
        Gui.drawRect(x2, y, x2 + scale, y2, color);
        Gui.drawRect(x, y2, x2 + scale, y2 + scale, color);
    }

    public void glColor(Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    public static void drawGlow(double x, double y, double size, int col, int col2){
        int times = (int) size;
        for(int i = 0; i < times ; i++){
            GlStateManager.pushMatrix();
            GlStateManager.translate(x,y,0);
            glRotated((360f / times) * i,0,0,1);
            drawGradientRect(0,0,(360f/times) * 6, 0 + size, col, col2);
            GlStateManager.translate(-x,-y,0);
            glRotated(-(360f / times) * i,0,0,1);
            GlStateManager.popMatrix();
        }
    }
    public static Color glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 256.0f;
        float red = (hex >> 16 & 0xFF) / 255.0f;
        float green = (hex >> 8 & 0xFF) / 255.0f;
        float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }

    public Color glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
        float red = 0.003921569f * redRGB;
        float green = 0.003921569f * greenRGB;
        float blue = 0.003921569f * blueRGB;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }

    public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        enableGL2D();
        GL11.glShadeModel(GL_SMOOTH);
        GL11.glBegin(7);
        glColor(topColor);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        glColor(bottomColor);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glShadeModel(GL_SMOOTH);
        disableGL2D();
    }
    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        float f3 = (col1 & 0xFF) / 255.0F;

        float f4 = (col2 >> 24 & 0xFF) / 255.0F;
        float f5 = (col2 >> 16 & 0xFF) / 255.0F;
        float f6 = (col2 >> 8 & 0xFF) / 255.0F;
        float f7 = (col2 & 0xFF) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);

        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);

        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        GlStateManager.color(1,1,1);
    }


    public static void drawGradientRect(double d, double e, double f, double g, int topColor, int bottomColor) {
        enableGL2D();
        GL11.glShadeModel(GL_SMOOTH);
        GL11.glBegin(GL_LINE_STRIP);
        glColor(topColor);
        GL11.glVertex2d(d, g);
        GL11.glVertex2d(f, g);
        glColor(bottomColor);
        GL11.glVertex2d(f, e);
        GL11.glVertex2d(d, e);
        GL11.glEnd();
        GL11.glShadeModel(GL_SMOOTH);
        disableGL2D();
    }

    public static void setColor(Color c){
        GL11.glColor4d(c.getRed() / 255f, c.getGreen() / 255f,
                c.getBlue() / 255f, c.getAlpha() / 255f);
    }

    public static void drawBorderedRect(double left, double top, double right, double bottom, float width, int borderColour, int colour) {
        GlStateManager.pushMatrix();
        drawRect(left, top, right, bottom, colour);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(770, 771);
        glColor(borderColour);
        GL11.glEnable(2848);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(right, top);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawRect(double left, double top, double right, double bottom, int colour) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glColor(colour);
        GL11.glBegin(7);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glVertex2d(left, top);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientBorderedRect(float x, float y, float x1, float y1, float lineWidth, int border, int bottom, int top) {
        enableGL2D();
        drawGradientRect(x, y, x1, y1, top, bottom);
        glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        disableGL2D();
    }

    public static void beginGl() {
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        if (antialiiasing)
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1.0f);
    }

    public static void endGl() {
        GL11.glLineWidth(2.0F);
        if (antialiiasing)
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public static void drawLines(AxisAlignedBB boundingBox) {
        GL11.glPushMatrix();
        GL11.glBegin(2);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static void drawBorderedRefinedRect(float x, float y, float x1, float y1, float lineWidth, int inside, int border) {
        enableGL2D();
        drawRect(x, y, x1, y1, inside);
        glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        disableGL2D();
    }

    public static void drawHead(final AbstractClientPlayer target, final int x, final int y, final int width, final int height) {
        final ResourceLocation skin = target.getLocationSkin();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
        drawScaledCustomSizeModalRect(x, y, 8.0f, 8.0f, 8, 8, width, height, 64.0f, 64.0f);
        drawScaledCustomSizeModalRect(x, y, 40.0f, 8.0f, 8, 8, width, height, 64.0f, 64.0f);
    }

    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight)
    {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D).tex(u * f, (v + (float)vHeight) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex((u + (float)uWidth) * f, (v + (float)vHeight) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex((u + (float)uWidth) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }

    public static void drawLineToPosition(double x,double y,double z, int color) {
        Minecraft mc = Minecraft.getMinecraft();
        double renderPosXDelta = x - mc.getRenderManager().viewerPosX;
        double renderPosYDelta = y - mc.getRenderManager().viewerPosY;
        double renderPosZDelta = z - mc.getRenderManager().viewerPosZ;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0F);
        float blockPos9 = (float) (mc.thePlayer.posX -  x);
        float blockPos7 = (float) (mc.thePlayer.posY - y);
        float f = (float) (color >> 16 & 0xFF) / 255.0f;
        float f2 = (float) (color >> 8 & 0xFF) / 255.0f;
        float f3 = (float) (color & 0xFF) / 255.0f;
        float f4 = (float) (color >> 24 & 0xFF) / 255.0f;
        GL11.glColor4f(f, f2, f3, f4);
        GL11.glLoadIdentity();
        boolean previousState = mc.gameSettings.viewBobbing;
        mc.gameSettings.viewBobbing = false;
        mc.entityRenderer.orientCamera(mc.timer.renderPartialTicks);
        GL11.glBegin(3);
        GL11.glVertex3d(0.0D, mc.thePlayer.getEyeHeight(), 0.0D);
        GL11.glVertex3d(renderPosXDelta, renderPosYDelta, renderPosZDelta);
        GL11.glVertex3d(renderPosXDelta, renderPosYDelta, renderPosZDelta);
        GL11.glEnd();
        mc.gameSettings.viewBobbing = previousState;
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    public static void drawFace(ResourceLocation skin, int x, int y, int width, int height) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(skin);
        RenderUtil.drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8, 8, width, height,64F, 64F);
        RenderUtil.drawScaledCustomSizeModalRect(x, y, 40F, 8F, 8, 8, width, height,64F, 64F);
    }
    public static void drawRoundedRect(double x, double y, double width, double height, double radius, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);

        x *= 2;
        y *= 2;
        x1 *= 2;
        y1 *= 2;

        glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f1, f2, f3, f);
        glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glBegin(GL11.GL_POLYGON);

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + +(Math.sin((i * Math.PI / 180)) * (radius * -1)), y + radius + (Math.cos((i * Math.PI / 180)) * (radius * -1)));
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + (Math.sin((i * Math.PI / 180)) * (radius * -1)), y1 - radius + (Math.cos((i * Math.PI / 180)) * (radius * -1)));
        }

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius), y1 - radius + (Math.cos((i * Math.PI / 180)) * radius));
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius), y + radius + (Math.cos((i * Math.PI / 180)) * radius));
        }

        GL11.glEnd();

        glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_LINE_SMOOTH);
        glEnable(GL11.GL_TEXTURE_2D);

        GL11.glScaled(2, 2, 2);

        GL11.glPopAttrib();
        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

    }
    public static void drawOutlinedRoundedRect(double x, double y, double width, double height, double radius, float linewidth, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);

        x *= 2;
        y *= 2;
        x1 *= 2;
        y1 *= 2;
        GL11.glLineWidth(linewidth);

        glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f1, f2, f3, f);
        glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBegin(2);

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + +(Math.sin((i * Math.PI / 180)) * (radius * -1)), y + radius + (Math.cos((i * Math.PI / 180)) * (radius * -1)));
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + (Math.sin((i * Math.PI / 180)) * (radius * -1)), y1 - radius + (Math.cos((i * Math.PI / 180)) * (radius * -1)));
        }

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius), y1 - radius + (Math.cos((i * Math.PI / 180)) * radius));
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius), y + radius + (Math.cos((i * Math.PI / 180)) * radius));
        }

        GL11.glEnd();

        glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_LINE_SMOOTH);
        glEnable(GL11.GL_TEXTURE_2D);

        GL11.glScaled(2, 2, 2);

        GL11.glPopAttrib();
        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

    }
    public static Base64ImageLocation getDiscordProfilePicture(String id){
        try {
            String serv = HttpUtil.get(new URL("http://localhost:3000/?id=" + id));
            JSONObject obj = new JSONObject(serv);
            return new Base64ImageLocation(obj.getString("pfp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
