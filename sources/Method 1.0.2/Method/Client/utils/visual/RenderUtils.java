package Method.Client.utils.visual;

import Method.Client.module.misc.ModSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

import static Method.Client.utils.visual.ColorUtils.colorcalc;
import static net.minecraft.client.Minecraft.getMinecraft;
import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {
    protected static Minecraft mc = getMinecraft();
    private static final ICamera camera = new Frustum();

    public static void OpenGl() {
        // Contained you are here and really lost on some render stuff you can run the mc profiler to show potential lag stuff
        //   mc.profiler.startSection("Start-Method");

        // Repeat 2d Render no Lighting!

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();

        GlStateManager.depthMask(false);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL32.GL_DEPTH_CLAMP);

        // I don't need this, but you may
        // GlStateManager.disableAlpha();
    }

    public static void ReleaseGl() {
        //mc.profiler.endSection();

        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL32.GL_DEPTH_CLAMP);
        GlStateManager.shadeModel(GL_FLAT);
        // I don't need this, but you may
        // GlStateManager.enableAlpha();
    }

    public static void rotateGlsetup(double x, double y, double z) {
        GlStateManager.pushMatrix();
        RenderManager renderManager = mc.getRenderManager();
        GlStateManager.translate(x - mc.getRenderManager().viewerPosX, y - mc.getRenderManager().viewerPosY, z - mc.getRenderManager().viewerPosZ);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(mc.gameSettings.thirdPersonView == 2 ? -renderManager.playerViewX : renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.disableLighting();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
    }

    public static void rotateglCleanup() {
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GlStateManager.enableDepth();
        GL11.glTranslatef(-.5f, 0, 0);
        GlStateManager.popMatrix();
    }

    public static void RenderBlock(String s, final AxisAlignedBB bb, int c, Double width) {
        if (!s.equalsIgnoreCase("Tracer")) {
            if (!ModSettings.Rendernonsee.getValBoolean()) {
                camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
                if (!camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + mc.getRenderManager().viewerPosX, -10, bb.minZ + mc.getRenderManager().viewerPosZ, bb.maxX + mc.getRenderManager().viewerPosX, 2500, bb.maxZ + mc.getRenderManager().viewerPosZ)))
                    return;
            }
        }

        OpenGl();
        GlStateManager.glLineWidth((float) (1.5f * (width + .0001)));

        float a = colorcalc(c, 24);
        float r = colorcalc(c, 16);
        float g = colorcalc(c, 8);
        float b = colorcalc(c, 0);

        switch (s) {
            case "Shape":
                Sphere sph = new Sphere();

                sph.setDrawStyle(GLU.GLU_SILHOUETTE);
                GlStateManager.color(r, g, b, a);
                GlStateManager.translate((bb.maxX + bb.minX) / 2, (bb.maxY + bb.minY) / 2, (bb.maxZ + bb.minZ) / 2);
                sph.draw(1, (int) ModSettings.Spherelines.getValDouble(), (int) ModSettings.SphereDist.getValDouble());
                break;
            case "Flat":
                RenderGlobal.renderFilledBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.minY, bb.maxZ, r, g, b, a);
                break;
            case "FlatOutline":
                RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.minY, bb.maxZ, r, g, b, a);
                break;
            case "Full":
                RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, r, g, b, a);
                RenderGlobal.renderFilledBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, r, g, b, a);
                break;
            case "Outline":
                RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, r, g, b, a);
                break;
            case "Beacon":
                RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, r, g, b, a);
                RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, (0 - bb.maxY) + 255, bb.maxZ, r, g, b, a / 2);
                RenderGlobal.renderFilledBox(bb.minX, bb.minY, bb.minZ, bb.maxX, (0 - bb.maxY) + 255, bb.maxZ, r, g, b, a / 4);
                break;
            case "Tracer":
                GlStateManager.glBegin(GL11.GL_LINES);
                GlStateManager.color(r, g, b, a);
                Vec3d eyes = ActiveRenderInfo.getCameraPosition();
                GL11.glVertex3d(eyes.x, eyes.y, eyes.z);
                GL11.glVertex3d(bb.getCenter().x, bb.getCenter().y, bb.getCenter().z);
                GlStateManager.glEnd();
                break;
            case "Xspot":
                BufferBuilder BBuild2 = Tessellator.getInstance().getBuffer();

                BBuild2.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
                BBuild2.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
                BBuild2.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
                BBuild2.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, 0).endVertex();
                BBuild2.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();

                Tessellator.getInstance().draw();
                break;
            default:
                break;
        }
        ReleaseGl();
    }

    public static double simpleScale(boolean b) {
        switch (new ScaledResolution(mc).getScaleFactor()) {
            case 1:
                return 2;
            case 3:
                return 1.0 / 1.5;
            case 4:
                return 1.0 / 2.0;
            default:
                return 1;
        }

        /*
        public static double simpleScale(boolean b) {
        switch (new ScaledResolution(mc).getScaleFactor()) {
            case 1:
                return 2 * (b ?  (mc.displayHeight / 1027.0) : (mc.displayWidth / 1920.0));
            case 3:
                return (1.0 / 1.5) * (b ?  (mc.displayHeight / 1027.0) : (mc.displayWidth / 1920.0));
            case 4:
                return (1.0 / 2.0) * (b ?  (mc.displayHeight / 1027.0) : (mc.displayWidth / 1920.0));
            default:
                return 1 * (b ?  (mc.displayHeight / 1027.0) : (mc.displayWidth / 1920.0));
        }
    }
         */
    }

    public static Double[] Scaledxy(double x, double y) {
        double xf = x * (mc.displayWidth / 1920.0);
        double yf = y * (mc.displayHeight / 1027.0);
        return new Double[]{xf, yf};
    }

    public static void scaledScissors(int x, int y, int width, int height) {
        double xf = x * (mc.displayWidth / 1920.0);
        double yf = y * (mc.displayHeight / 1027.0);
        double wf = width * (mc.displayWidth / 1920.0);
        double hf = height * (mc.displayHeight / 1027.0);
        GL11.glScissor((int) xf, (int) yf, (int) wf, (int) hf);
    }

    public static void scaledText(String s, double x, double y, int color, boolean b) {
        // split decimal into integer and fractional parts for x and y
        double xf = x * (mc.displayWidth / 1920.0);
        double yf = y * (mc.displayHeight / 1027.0);
        GlStateManager.pushMatrix();
        mc.fontRenderer.drawString(s, (int) (xf), (int) (yf), color, b);
        GlStateManager.popMatrix();
    }

    // do the same as above for Gui.drawRect
    public static void scaledRect(double x, double y, double width, double height, int color) {
        // split decimal into integer and fractional parts for x and y
        double xf = x * (mc.displayWidth / 1920.0);
        double yf = y * (mc.displayHeight / 1027.0);
        double widthf = width * (mc.displayWidth / 1920.0);
        double heightf = height * (mc.displayHeight / 1027.0);

        Gui.drawRect((int) (xf), (int) (yf), (int) (xf + widthf), (int) (yf + heightf), color);
    }

    // do the same for the drawRectOutline method
    public static void scaledRectOutline(int left, int top, int right, int bottom, double width, int color) {
        // split decimal into integer and fractional parts for x and y
        double leftf = left * (mc.displayWidth / 1920.0);
        double topf = top * (mc.displayHeight / 1027.0);
        double rightf = right * (mc.displayWidth / 1920.0);
        double bottomf = bottom * (mc.displayHeight / 1027.0);
        drawRectOutline((int) (leftf), (int) (topf), (int) (rightf), (int) (bottomf), (int) (width), color);
    }


    public static AxisAlignedBB Standardbb(BlockPos pos) {
        double renderPosX = pos.getX() - mc.getRenderManager().viewerPosX;
        double renderPosY = pos.getY() - mc.getRenderManager().viewerPosY;
        double renderPosZ = pos.getZ() - mc.getRenderManager().viewerPosZ;
        return new AxisAlignedBB(renderPosX, renderPosY, renderPosZ, renderPosX + 1.0, renderPosY + 1.0, renderPosZ + 1.0);
    }

    public static AxisAlignedBB Boundingbb(Entity entity, double x, double y, double z, double x1, double y1, double z1) {
        double renderPosX = entity.getEntityBoundingBox().minX - mc.getRenderManager().viewerPosX;
        double renderPosY = entity.getEntityBoundingBox().minY - mc.getRenderManager().viewerPosY;
        double renderPosZ = entity.getEntityBoundingBox().minZ - mc.getRenderManager().viewerPosZ;
        double renderPosX2 = entity.getEntityBoundingBox().maxX - mc.getRenderManager().viewerPosX;
        double renderPosY2 = entity.getEntityBoundingBox().maxY - mc.getRenderManager().viewerPosY;
        double renderPosZ2 = entity.getEntityBoundingBox().maxZ - mc.getRenderManager().viewerPosZ;

        return new AxisAlignedBB(renderPosX + x, renderPosY + y, renderPosZ + z, renderPosX2 + x1, renderPosY2 + y1, renderPosZ2 + z1);
    }


    public static AxisAlignedBB Modifiedbb(BlockPos pos, int x, int y, int z) {
        double renderPosX = pos.getX() - mc.getRenderManager().viewerPosX;
        double renderPosY = pos.getY() - mc.getRenderManager().viewerPosY;
        double renderPosZ = pos.getZ() - mc.getRenderManager().viewerPosZ;
        return new AxisAlignedBB(renderPosX + x, renderPosY + y, renderPosZ + z, renderPosX + 1.0 + x, renderPosY + 1.0 + y, renderPosZ + 1.0 + z);
    }

    // Render a 3d line from each point in the given List Vec3d
    public static void RenderLine(ArrayList<Vec3d> List, int Color, double width, boolean valBoolean) {
        OpenGl();
        GL11.glEnable(GL13.GL_MULTISAMPLE);
        GL11.glLineWidth((float) width);
        ColorUtils.glColor(Color);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        RenderManager renderManager = mc.getRenderManager();

        for (Vec3d blockPos : List) {
            if (valBoolean) {
                BlockPos snap = new BlockPos(blockPos);
                GL11.glVertex3d(snap.x - renderManager.viewerPosX, snap.y - renderManager.viewerPosY, snap.z - renderManager.viewerPosZ);
            } else
                GL11.glVertex3d(blockPos.x - renderManager.viewerPosX, blockPos.y - renderManager.viewerPosY, blockPos.z - renderManager.viewerPosZ);
        }

        GL11.glEnd();
        RenderUtils.ReleaseGl();
        GL11.glDisable(GL13.GL_MULTISAMPLE);
    }


    public static Vec3d getInterpolatedRenderPos(Vec3d pos) {
        return new Vec3d(pos.x, pos.y, pos.z).subtract(mc.getRenderManager().renderPosX, mc.getRenderManager().renderPosY, mc.getRenderManager().renderPosZ);
    }

    public static void SimpleNametag(Vec3d pos, String str) {
        RenderUtils.OpenGl();

        boolean isThirdPersonFrontal = mc.getRenderManager().options.thirdPersonView == 2;
        Vec3d interp = getInterpolatedRenderPos(pos);
        GlStateManager.translate(interp.x, interp.y + 0.75F, interp.z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (isThirdPersonFrontal ? -1 : 1) * mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);

        float m = (float) (mc.player.getDistance(pos.x, pos.y + 0.5F, pos.z));
        m = (float) (m - (m / 1.2));
        GlStateManager.scale(m * -0.025F, m * -0.025F, m * 0.025F);


        int i = mc.fontRenderer.getStringWidth(str) / 2;

        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.enableTexture2D();
        mc.fontRenderer.drawStringWithShadow(str, -i, 9, Color.WHITE.getRGB());
        GlStateManager.disableTexture2D();
        GlStateManager.glNormal3f(0.0F, 0.0F, 0.0F);
        RenderUtils.ReleaseGl();
        GlStateManager.scale(1, 1, 1);
    }

    public static void drawRectOutline(int left, int top, int right, int bottom, double width, int color) {
        int l = (int) (left - width);
        int t = (int) (top - width);
        int r = (int) (right + width);
        int b = (int) (bottom + width);
        Gui.drawRect(l, t, r, top, color);
        Gui.drawRect(right, top, r, b, color);
        Gui.drawRect(l, bottom, right, b, color);
        Gui.drawRect(l, top, left, bottom, color);
    }

    public static void drawRectTopBottom(int left, int top, int right, int bottom, double width, int color) {
        int l = (int) (left - width);
        int t = (int) (top - width);
        int r = (int) (right + width);
        int b = (int) (bottom + width);
        Gui.drawRect(l, t, r, top, color);
        //  Gui.drawRect(right, top, r, b, color);
        Gui.drawRect(l, bottom, right, b, color);
        // Gui.drawRect(l, top, left, bottom, color);
    }

    public static void drawline(int top, int x, int bottom, double width, int color) {
        int r = (int) (x + width);
        int b = (int) (bottom + width);
       Gui.drawRect(x, top, r, b, color);
    }


    public static void drawCirc(double x, double y, double Radius, int Color, double startangle, double endangle) {
        // start = 0 end = 2*pi
        // Repeat Left Math.PI/2, Math.PI + Math.PI/2
        // Repeat bottom 0 - pi
        // Right - (Math.PI / 2), Math.PI / 2
        double increment = 2 * Math.PI / 50;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        ColorUtils.glColor(Color);
        for (double angle = startangle; angle < endangle; angle += increment) {
            GL11.glBegin(GL11.GL_LINE_LOOP);
            // Polygon
            GL11.glVertex2d(x + Math.cos(angle) * Radius, y + Math.sin(angle) * Radius);
            GL11.glVertex2d(x + Math.cos(angle + increment) * Radius, y + Math.sin(angle + increment) * Radius);
            GL11.glEnd();
        }
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawCircfilled(double x, double y, double Radius, int Color, double xStretch, double yStretch) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ONE);
        glEnable(GL_LINE_SMOOTH);
        ColorUtils.glColor(Color);
        GL11.glBegin(GL_TRIANGLE_FAN);
        for (int i = 0; i <= 360; i++) {
            GL11.glVertex2d(x + (Math.sin((i * Math.PI / 180)) * (Radius * xStretch)), y + (Math.cos((i * Math.PI / 180)) * (Radius * yStretch)));
        }
        GL11.glEnd();
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
