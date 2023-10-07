package cc.novoline.utils;

import cc.novoline.Novoline;
import cc.novoline.modules.combat.KillAura;
import cc.novoline.modules.visual.HUD;
import cc.novoline.modules.visual.PlayerESP;
import cc.novoline.utils.fonts.api.FontRenderer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

import static java.awt.Color.BLACK;
import static net.minecraft.client.renderer.GlStateManager.disableBlend;
import static net.minecraft.client.renderer.GlStateManager.enableTexture2D;
import static org.lwjgl.opengl.GL11.*;

public final class RenderUtils {

    public static double healthPercent;
    private static Minecraft mc = Minecraft.getInstance();
    private static double lastP = 0, diffP = 0;

    private RenderUtils() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void setupRender(boolean start) {
        if (start) {
            GlStateManager.enableBlend();
            glEnable(2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTextures();
            GlStateManager.blendFunc(770, 771);
            GL11.glHint(3154, 4354);
        } else {
            disableBlend();
            GlStateManager.enableTextures();
            glDisable(2848);
            GlStateManager.enableDepth();
        }

        GlStateManager.depthMask(!start);
    }


    public static void drawRainbowBox(double x, double y, double x2, double y2, float lw, boolean cornered, boolean astolfo, boolean bordered) {
        start2D();
        GL11.glPushMatrix();
        if (cornered) {
            double width = Math.abs(x2 - x);
            double height = Math.abs(y2 - y);
            double halfWidth = width / 4;
            double halfHeight = height / 4;
            int counter = 0;
            if (width != 0 && height != 0) {
                if (bordered) {
                    GL11.glLineWidth(2.5f);
                    setColor(BLACK);
                    GL11.glBegin(GL_LINE_STRIP);
                    GL11.glVertex2d(x + halfWidth, y);
                    GL11.glVertex2d(x, y);
                    GL11.glVertex2d(x, y + halfHeight);
                    GL11.glEnd();


                    GL11.glBegin(GL_LINE_STRIP);
                    GL11.glVertex2d(x, y + height - halfHeight);
                    GL11.glVertex2d(x, y + height);
                    GL11.glVertex2d(x + halfWidth, y + height);
                    GL11.glEnd();

                    GL11.glBegin(GL_LINE_STRIP);
                    GL11.glVertex2d(x + width - halfWidth, y + height);
                    GL11.glVertex2d(x + width, y + height);
                    GL11.glVertex2d(x + width, y + height - halfHeight);
                    GL11.glEnd();

                    GL11.glBegin(GL_LINE_STRIP);
                    GL11.glVertex2d(x + width, y + halfHeight);
                    GL11.glVertex2d(x + width, y);
                    GL11.glVertex2d(x + width - halfWidth, y);
                    GL11.glEnd();
                }

                GL11.glLineWidth(1.5f);
                GL11.glBegin(GL_LINE_STRIP);

                for (double i = 0; i <= halfWidth; i += halfWidth / 8) {
                    setColor(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)));
                    GL11.glVertex2d(x + halfWidth - i, y);
                    counter++;
                }

                for (double i = 0; i <= halfHeight; i += halfHeight / 8) {
                    setColor(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)));
                    GL11.glVertex2d(x, y + i);
                    counter++;
                }

                GL11.glEnd();
                GL11.glBegin(GL_LINE_STRIP);

                for (double i = 0; i <= halfHeight; i += halfHeight / 8) {
                    setColor(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)));
                    GL11.glVertex2d(x, y + height - halfHeight + i);
                    counter++;
                }

                for (double i = 0; i <= halfWidth; i += halfWidth / 8) {
                    setColor(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)));
                    GL11.glVertex2d(x + i, y + height);
                    counter++;
                }

                GL11.glEnd();
                GL11.glBegin(GL_LINE_STRIP);

                for (double i = 0; i <= halfWidth; i += halfWidth / 8) {
                    setColor(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)));
                    GL11.glVertex2d(x + width - halfWidth + i, y + height);
                    counter++;
                }

                for (double i = 0; i <= halfHeight; i += halfHeight / 8) {
                    setColor(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)));
                    GL11.glVertex2d(x + width, y + height - i);
                    counter++;
                }

                GL11.glEnd();
                GL11.glBegin(GL_LINE_STRIP);

                for (double i = 0; i <= halfHeight; i += halfHeight / 8) {
                    setColor(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)));
                    GL11.glVertex2d(x + width, y + halfHeight - i);
                    counter++;
                }

                for (double i = 0; i <= halfWidth; i += halfWidth / 8) {
                    setColor(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)));
                    GL11.glVertex2d(x + width - i, y);
                    counter++;
                }

                GL11.glEnd();
            }
        } else {
            GL11.glLineWidth(2.5f);
            double x3 = Math.abs(x2 - x);
            double y3 = Math.abs(y2 - y);

            if (bordered) {
                setColor(new Color(0xff000000));

                GL11.glBegin(GL_LINE_STRIP);
                GL11.glVertex2d(x, y);
                GL11.glVertex2d(x + (x2 - x), y);
                GL11.glVertex2d(x + (x2 - x), y + (y2 - y));
                GL11.glVertex2d(x, y + (y2 - y));
                GL11.glVertex2d(x, y);
                GL11.glEnd();
            }
            GL11.glLineWidth(1.5f);
            GL11.glBegin(GL_LINE_STRIP);
            int counter = 0;
            if (y3 != 0 && x3 != 0) {
                for (double i = 0; i <= y3; i += y3 / 10) {
                    setColor(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)));
                    GL11.glVertex2d(x, y + i);
                    counter++;
                }
                for (double i = 0; i <= x3; i += x3 / 10) {
                    setColor(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)));
                    GL11.glVertex2d(x + i, y + y3);
                    counter++;
                }
                for (double i = 0; i <= y3; i += y3 / 10) {
                    setColor(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)));
                    GL11.glVertex2d(x + x3, y + y3 - i);
                    counter++;
                }
                for (double i = 0; i <= x3; i += x3 / 10) {
                    setColor(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)));
                    GL11.glVertex2d(x + x3 - i, y);
                    counter++;
                }
            }
            GL11.glVertex2d(x, y);
            GL11.glEnd();
        }
        GL11.glPopMatrix();
        stop2D();
    }

    public static int getArrayRainbow(int counter, int alpha) {
        final int width = 110;
        PlayerESP playerESP = Novoline.getInstance().getModuleManager().getModule(PlayerESP.class);

        double rainbowState = Math.ceil(System.currentTimeMillis() - (long) counter * width) / 11;
        rainbowState %= 360;

        final float[] colors = playerESP.getColor().getHSB();
        Color color = Color.getHSBColor((float) (rainbowState / 360), colors[1], colors[2]);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }

    public static int getArrayAstolfo(int counter, int alpha) {
        final int width = 110;
        PlayerESP playerESP = Novoline.getInstance().getModuleManager().getModule(PlayerESP.class);

        double rainbowState = Math.ceil(System.currentTimeMillis() - (long) counter * width) / 11;
        rainbowState %= 360;
        final float hue = (float) (rainbowState / 360) < 0.5 ? -((float) (rainbowState / 360)) : (float) (rainbowState / 360);

        final float[] colors = playerESP.getColor().getHSB();
        Color color = Color.getHSBColor(hue, colors[1], colors[2]);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }

    public static void drawSolidBlockESP(double x, double y, double z, int color) {
        double xPos = x - mc.getRenderManager().renderPosX, yPos = y - mc.getRenderManager().renderPosY, zPos = z - mc.getRenderManager().renderPosZ;
        float f = (float) (color >> 16 & 0xFF) / 255.0f;
        float f2 = (float) (color >> 8 & 0xFF) / 255.0f;
        float f3 = (float) (color & 0xFF) / 255.0f;
        float f4 = (float) (color >> 24 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.0f);
        GL11.glColor4f(f, f2, f3, f4);
        drawOutlinedBoundingBox(new AxisAlignedBB(xPos, yPos, zPos, xPos + 1.0, yPos + 1.0, zPos + 1.0));
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawSolidBlockESP(BlockPos pos, int color) {
        double xPos = pos.getX() - mc.getRenderManager().renderPosX, yPos = pos.getY() - mc.getRenderManager().renderPosY, zPos = pos.getZ() - mc.getRenderManager().renderPosZ;
        double height = mc.world.getBlockState(pos).getBlock().getBlockBoundsMaxY() - mc.world.getBlockState(pos).getBlock().getBlockBoundsMinY();
        float f = (float) (color >> 16 & 0xFF) / 255.0f;
        float f2 = (float) (color >> 8 & 0xFF) / 255.0f;
        float f3 = (float) (color & 0xFF) / 255.0f;
        float f4 = (float) (color >> 24 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.0f);
        GL11.glColor4f(f, f2, f3, f4);
        drawOutlinedBoundingBox(new AxisAlignedBB(xPos, yPos, zPos, xPos + 1.0, yPos + height, zPos + 1.0));
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glDisable(3042);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(2929);
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static Vec3 interpolateRender(EntityPlayer player) {
        float part = Minecraft.getInstance().timer.renderPartialTicks;
        double interpX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) part;
        double interpY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) part;
        double interpZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) part;
        return new Vec3(interpX, interpY, interpZ);
    }

    public static float[] getRGBAs(int rgb) {
        return new float[]{(rgb >> 16 & 255) / 255F, (rgb >> 8 & 255) / 255F, (rgb & 255) / 255F, (rgb >> 24 & 255) / 255F};
    }

    public static void drawHorizontal(String s, float x, float y) {
        float posX = x;
        HUD hud = Novoline.getInstance().getModuleManager().getModule(HUD.class);

        for (int i = 0; i < s.length(); i++) {
            Minecraft mc = Minecraft.getInstance();
            int color = hud.getHudMode().equalsIgnoreCase("Astolfo") ? hud.getArrayAstolfo(i, 255) :
                    hud.getHudMode().equalsIgnoreCase("Rainbow") ? hud.getArrayRainbow(i, 255) : hud.getArrayStatic(i, 255);

            mc.fontRendererObj.drawStringWithShadow(String.valueOf(s.charAt(i)), posX, y, color);
            posX += mc.fontRendererObj.getCharWidth(s.charAt(i));
        }
    }

    public static void drawHorizontal(FontRenderer font, String s, float x, float y) {
        float posX = x;
        HUD hud = Novoline.getInstance().getModuleManager().getModule(HUD.class);

        for (int i = 0; i < s.length(); i++) {
            Minecraft mc = Minecraft.getInstance();
            int color = hud.getHudMode().equalsIgnoreCase("Astolfo") ? hud.getArrayAstolfo(i, 255) :
                    hud.getHudMode().equalsIgnoreCase("Rainbow") ? hud.getArrayRainbow(i, 255) : hud.getArrayStatic(i, 255);

            font.drawString(String.valueOf(s.charAt(i)), posX, y, color, true);
            posX += font.stringWidth(String.valueOf(s.charAt(i)));
        }
    }

    private static final char[] RDM = {'a', '1', 'c', '3', 'e', '5', 'g', '7'};


    public static String obfuscateString(String s, String replacement) {
        String n = "";

        for (int i = 0; i < s.length(); i++) {
            char ch = RDM[MathHelper.randomNumber(RDM.length - 1, 0)];
            n = n.concat(String.valueOf(ch));
        }

        if (replacement.contains(s)) {
            return replacement.replace(s, "\u00a7k" + n + "\u00a7r");
        }

        return null;
    }

    public static void rectangle(double left, double top, double right, double bottom, int color) {
        double d;

        if (left < right) {
            d = left;

            left = right;
            right = d;
        }

        if (top < bottom) {
            d = top;

            top = bottom;
            bottom = d;
        }

        final float a = (float) (color >> 24 & 255) / 255.0F, // @off
                r = (float) (color >> 16 & 255) / 255.0F,
                g = (float) (color >> 8 & 255) / 255.0F,
                b = (float) (color & 255) / 255.0F; // @on

        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTextures();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);

        worldRenderer.startDrawingQuads();
        worldRenderer.pos(left, bottom, 0.0D).endVertex();
        worldRenderer.pos(right, bottom, 0.0D).endVertex();
        worldRenderer.pos(right, top, 0.0D).endVertex();
        worldRenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.enableTextures();
        disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static int getRainbow(int speed, int offset) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 0.75f, 1.0f).getRGB();
    }

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }


    public static void drawOutlinedBoundingBox(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }


    public static void drawFilledBox(@NotNull AxisAlignedBB mask) {
        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        final Tessellator tessellator = Tessellator.getInstance();

        {
            worldRenderer.startDrawingQuads();
            worldRenderer.pos(mask.minX, mask.minY, mask.minZ).endVertex();
            worldRenderer.pos(mask.minX, mask.maxY, mask.minZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.minY, mask.minZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.minX, mask.minY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ).endVertex();
            tessellator.draw();
        }

        {
            worldRenderer.startDrawingQuads();
            worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.minY, mask.minZ).endVertex();
            worldRenderer.pos(mask.minX, mask.maxY, mask.minZ).endVertex();
            worldRenderer.pos(mask.minX, mask.minY, mask.minZ).endVertex();
            worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.minX, mask.minY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ).endVertex();
            tessellator.draw();
        }

        {
            worldRenderer.startDrawingQuads();
            worldRenderer.pos(mask.minX, mask.maxY, mask.minZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.minX, mask.maxY, mask.minZ).endVertex();
            worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ).endVertex();
            tessellator.draw();
        }

        {
            worldRenderer.startDrawingQuads();
            worldRenderer.pos(mask.minX, mask.minY, mask.minZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.minY, mask.minZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.minX, mask.minY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.minX, mask.minY, mask.minZ).endVertex();
            worldRenderer.pos(mask.minX, mask.minY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.minY, mask.minZ).endVertex();
            tessellator.draw();
        }

        {
            worldRenderer.startDrawingQuads();
            worldRenderer.pos(mask.minX, mask.minY, mask.minZ).endVertex();
            worldRenderer.pos(mask.minX, mask.maxY, mask.minZ).endVertex();
            worldRenderer.pos(mask.minX, mask.minY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.minY, mask.minZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ).endVertex();
            tessellator.draw();
        }

        {
            worldRenderer.startDrawingQuads();
            worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.minX, mask.minY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.minX, mask.maxY, mask.minZ).endVertex();
            worldRenderer.pos(mask.minX, mask.minY, mask.minZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.minY, mask.minZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ).endVertex();
            worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ).endVertex();
            tessellator.draw();
        }
    }

    public static void enableGL2D() {
        glDisable(2929);
        glEnable(3042);
        glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        glEnable(3553);
        glDisable(3042);
        glEnable(2929);
        glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }


    public static void drawOutlinedEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        glEnable(3042);
        GL11.glBlendFunc(770, 771);

        glDisable(3553);
        glEnable(2848);
        glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        drawFilledBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        glDisable(2848);
        glEnable(3553);

        glEnable(2929);
        GL11.glDepthMask(true);
        glDisable(3042);
        GL11.glPopMatrix();
        enableTexture2D();
        disableBlend();
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void drawEntityOnScreen(int posX, int posY, float scale, EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.color(255, 255, 255);
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        final RenderManager rendermanager = Minecraft.getInstance().getRenderManager();
        rendermanager.setPlayerViewY(1F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    private final static Frustum frustum = new Frustum();

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }


    public static boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getInstance().getRenderViewEntity();
        frustum.setPosition(current.posX, current.posY, current.posZ);
        return frustum.isBoundingBoxInFrustum(bb);
    }

    public static void drawBorderedBox(double x, double y, double x2, double y2, Color color, boolean bordered) {
        start2D();
        GL11.glPushMatrix();

        if (bordered) {
            GL11.glLineWidth(2f);

            setColor(new Color(0xff000000));

            GL11.glBegin(GL_LINE_STRIP);
            GL11.glVertex2d(x, y);
            GL11.glVertex2d(x + (x2 - x), y);
            GL11.glVertex2d(x + (x2 - x), y + (y2 - y));
            GL11.glVertex2d(x, y + (y2 - y));
            GL11.glVertex2d(x, y);
            GL11.glEnd();
        }

        GL11.glLineWidth(1f);
        setColor(color);

        GL11.glBegin(GL_LINE_STRIP);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x + (x2 - x), y);
        GL11.glVertex2d(x + (x2 - x), y + (y2 - y));
        GL11.glVertex2d(x, y + (y2 - y));
        GL11.glVertex2d(x, y);
        GL11.glEnd();

        GL11.glPopMatrix();
        stop2D();
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        drawRect(x, y, x2, y2, col2);

        final float f = (col1 >> 24 & 0xFF) / 255.0F, // @off
                f1 = (col1 >> 16 & 0xFF) / 255.0F,
                f2 = (col1 >> 8 & 0xFF) / 255.0F,
                f3 = (col1 & 0xFF) / 255.0F; // @on

        glEnable(3042);
        glDisable(3553);
        GL11.glBlendFunc(770, 771);
        glEnable(2848);

        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
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
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPopMatrix();

        enableTexture2D();
        disableBlend();
        GL11.glColor4f(1, 1, 1, 255);
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
    }


    public static void drawBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2) {
        drawRect((float) x, (float) y, (float) x2, (float) y2, col2);

        final float f = (col1 >> 24 & 0xFF) / 255.0F, // @off
                f1 = (col1 >> 16 & 0xFF) / 255.0F,
                f2 = (col1 >> 8 & 0xFF) / 255.0F,
                f3 = (col1 & 0xFF) / 255.0F; // @on

        glEnable(3042);
        glDisable(3553);
        GL11.glBlendFunc(770, 771);
        glEnable(2848);

        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
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
        enableTexture2D();
        disableBlend();
        GL11.glPopMatrix();
        GL11.glColor4f(255, 1, 1, 255);
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, int color) {
        float x1 = x + width, // @off
                y1 = y + height;
        final float f = (color >> 24 & 0xFF) / 255.0F,
                f1 = (color >> 16 & 0xFF) / 255.0F,
                f2 = (color >> 8 & 0xFF) / 255.0F,
                f3 = (color & 0xFF) / 255.0F; // @on
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);

        x *= 2;
        y *= 2;
        x1 *= 2;
        y1 *= 2;

        glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f1, f2, f3, f);
        GlStateManager.enableBlend();
        glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glBegin(GL11.GL_POLYGON);
        final double v = Math.PI / 180;

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + MathHelper.sin(i * v) * (radius * -1), y + radius + MathHelper.cos(i * v) * (radius * -1));
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + MathHelper.sin(i * v) * (radius * -1), y1 - radius + MathHelper.cos(i * v) * (radius * -1));
        }

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - radius + MathHelper.sin(i * v) * radius, y1 - radius + MathHelper.cos(i * v) * radius);
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - radius + MathHelper.sin(i * v) * radius, y + radius + MathHelper.cos(i * v) * radius);
        }

        GL11.glEnd();

        glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_LINE_SMOOTH);
        glEnable(GL11.GL_TEXTURE_2D);

        GL11.glScaled(2, 2, 2);

        GL11.glPopAttrib();
        GL11.glColor4f(1, 1, 1, 1);
    }


    public static @NotNull Color blend(@NotNull Color color1, @NotNull Color color2, double ratio) {
        final float r = (float) ratio, //
                ir = 1.0F - r;
        final float[] rgb1 = new float[3], //
                rgb2 = new float[3];

        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);

        return new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
    }

    public static void drawRect(float left, float top, float right, float bottom, int col1) {
        final float f = (col1 >> 24 & 0xFF) / 255.0F, // @off
                f1 = (col1 >> 16 & 0xFF) / 255.0F,
                f2 = (col1 >> 8 & 0xFF) / 255.0F,
                f3 = (col1 & 0xFF) / 255.0F; // @on

        glEnable(3042);
        glDisable(3553);
        GL11.glBlendFunc(770, 771);
        glEnable(2848);

        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(7);
        GL11.glVertex2d(right, top);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glEnd();
        GL11.glPopMatrix();

        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        enableTexture2D();
        disableBlend();
        GL11.glColor4f(1, 1, 1, 1);
    }


    public static void drawBoundingBox(@NotNull AxisAlignedBB aa) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        {
            worldRenderer.begin(3, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
            worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
            worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
            worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
            worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
            worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
            worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
            worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
            tessellator.draw();
        }

        {
            worldRenderer.begin(3, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
            worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
            worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
            worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
            worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
            worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
            worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
            worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
            tessellator.draw();
        }

        {
            worldRenderer.begin(3, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
            worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
            worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
            worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
            worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
            worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
            worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
            worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
            tessellator.draw();
        }

        {
            worldRenderer.begin(3, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
            worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
            worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
            worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
            worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
            worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
            worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
            worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
            tessellator.draw();
        }

        {
            worldRenderer.begin(3, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
            worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
            worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
            worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
            worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
            worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
            worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
            worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
            tessellator.draw();
        }

        {
            worldRenderer.begin(3, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
            worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
            worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
            worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
            worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
            worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
            worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
            worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
            tessellator.draw();
        }
    }

    public static void drawCircle(float cx, float cy, float r, float num_segments, int c) {
        GL11.glPushMatrix();
        cx *= 2.0F;
        cy *= 2.0F;

        final float f0 = (c >> 24 & 0xFF) / 255.0F, // @off
                f1 = (c >> 16 & 0xFF) / 255.0F,
                f2 = (c >> 8 & 0xFF) / 255.0F,
                f3 = (c & 0xFF) / 255.0F,
                theta = 6.2831852F / num_segments,
                p = MathHelper.cos(theta),
                s = MathHelper.sin(theta); // @on
        float x = r * 2.0F, y = 0.0F;

        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glColor4f(f1, f2, f3, f0);
        GL11.glBegin(2);
        int i = 0;

        while (i < num_segments) {
            GL11.glVertex2f(x + cx, y + cy);
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
            i++;
        }

        GL11.glEnd();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
        GlStateManager.color(1, 1, 1, 1);
        GL11.glPopMatrix();
    }

    public static int getHealthColor(@NotNull EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 1.0F) | 0xFF000000;
    }

    public static void drawFilledCircle(float x, float y, float r, int c) {
        final float f0 = (c >> 24 & 0xff) / 255F, // @off
                f1 = (c >> 16 & 0xff) / 255F,
                f2 = (c >> 8 & 0xff) / 255F,
                f3 = (c & 0xff) / 255F; // @on

        glEnable(GL11.GL_BLEND);
        glDisable(GL11.GL_TEXTURE_2D);
        glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f0);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        final float pi = (float) Math.PI;

        for (int i = 0; i <= 450; i++) {
            GL11.glVertex2d(x + MathHelper.sin(i * pi / 180.0F) * r, y + MathHelper.cos(i * pi / 180.0F) * r);
        }

        GL11.glEnd();
        glDisable(GL11.GL_LINE_SMOOTH);
        glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_BLEND);
        enableTexture2D();
        disableBlend();
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void pre3D() {
        GL11.glPushMatrix();
        glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        glDisable(GL11.GL_TEXTURE_2D);
        glEnable(GL11.GL_LINE_SMOOTH);
        glDisable(GL11.GL_DEPTH_TEST);
        glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
    }

    public static void post3D() {
        GL11.glDepthMask(true);
        glEnable(GL11.GL_DEPTH_TEST);
        glDisable(GL11.GL_LINE_SMOOTH);
        glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
    }


    public static void dr(double i, double j, double k, double l, int i1) {
        if (i < k) {
            final double tmp = i;
            i = k;
            k = tmp;
        }

        if (j < l) {
            final double tmp = j;
            j = l;
            l = tmp;
        }

        final float f0 = (i1 >> 24 & 0xff) / 255F, // @off
                f1 = (i1 >> 16 & 0xff) / 255F,
                f2 = (i1 >> 8 & 0xff) / 255F,
                f3 = (i1 & 0xff) / 255F; // @on

        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        glEnable(GL11.GL_BLEND);
        glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f0);

        worldRenderer.startDrawingQuads();
        worldRenderer.pos(i, l, 0.0D);
        worldRenderer.pos(k, l, 0.0D);
        worldRenderer.pos(k, j, 0.0D);
        worldRenderer.pos(i, j, 0.0D);
        tessellator.draw();

        glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_BLEND);
    }

    public static void renderTHUD(KillAura aura, @NotNull EntityPlayer e) {
        GL11.glPushMatrix();
        Minecraft mc = Minecraft.getInstance();
        ScaleUtils.scale(mc);
        float hp = e.getHealth() + e.getAbsorptionAmount();
        final float maxHP = e.getMaxHealth() + e.getAbsorptionAmount() - 0.05f;
        int i = 0;

        for (int b = 0; b < e.inventory.armorInventory.length; b++) {
            final ItemStack armor = e.inventory.armorInventory[b];

            if (armor != null) {
                i++;
            }
        }

        if (e.getCurrentEquippedItem() != null) {
            i++;
        }

        float rectLength = 35 + mc.fontRendererCrack.getStringWidth(e.getName()) + 40, health = (float) (Math.round(hp * 100.0) / 100.0);

        if (health > maxHP) {
            health *= maxHP / health;
        }

        float amplifier = 100 / maxHP, percent = health * amplifier, space = (rectLength - 50) / 100; //
        ScaledResolution sr = new ScaledResolution(mc);

        if (aura.getThx().get() > sr.getScaledWidthStatic(mc) - 50) {
            aura.getThx().set(sr.getScaledWidthStatic(mc) - 50);
        }

        if (aura.getThy().get() > sr.getScaledHeightStatic(mc) - 50) {
            aura.getThy().set(sr.getScaledHeightStatic(mc) - 50);
        }

        final int i2 = aura.getThx().get();
        final int i1 = aura.getThy().get();

        if (percent < lastP) {
            diffP = lastP - percent;
        }
        lastP = percent;
        if (diffP > 0) {
            diffP = diffP + (0 - diffP) * 0.05f;
        }

        diffP = MathHelper.clamp_double(diffP, 0, 100 - percent);

        mc.getTextureManager().bindTexture(((AbstractClientPlayer) e).getLocationSkin());
        RenderUtils.drawBorderedRect(i2, i1 - 1.5f, i2 + rectLength - 6, i1 + 37.5, (float) 1, new Color(0, 0, 0, 50).getRGB(), new Color(29, 29, 29, 130).getRGB());
        Gui.drawRect(i2 + 1, i1, i2 + rectLength - 7, i1 + 36, new Color(40, 40, 40, 130).getRGB());
        GL11.glPushMatrix();
        GL11.glColor4f(1, 1, 1, 1);
        final int l6 = 8;
        final int i6 = 8;
        GL11.glScaled(4.4, 4.4, 4.4);
        Gui.drawScaledCustomSizeModalRect((float) ((i2 + 1.5) / 4.4), (float) ((i1 + 0.2) / 4.4), 8.0f, (float) l6, 8, i6, 8, 8, 64.0f, 64.0f);
        GL11.glPopMatrix();
        int hudColor = Novoline.getInstance().getModuleManager().getModule(HUD.class).getHUDColor();
        Gui.drawRect(i2 + 40, i1 + 16.5, i2 + 40 + 100 * space, i1 + 27.3, new Color(0, 0, 0, 50).getRGB());
        Gui.drawRect(i2 + 40, i1 + 16.5, i2 + 40 + percent * space, i1 + 27.3, hudColor);
        Gui.drawRect(i2 + 40 + percent * space, i1 + 16.5, i2 + 40 + percent * space + diffP * space, i1 + 27.3, new Color(hudColor).darker().getRGB());
        String text = String.format("%.1f", percent) + "%";
        mc.fontRendererCrack.drawString(text, i2 + 40 + 50 * space - mc.fontRendererCrack.getStringWidth(text) / 2,
                i1 + 18f, 0xffffffff, true);
        mc.fontRendererCrack.drawString(e.getName(), i2 + 40, i1 + 4, 0xffffffff, true);
        //   mc.fontRendererCrack.drawString(String.format("%.1f", (e.getHealth() + e.getAbsorptionAmount()) / 2), i2 + 41, i1 + 27, 0xffffffff, true);
        //   mc.fontRendererCrack.drawString(" \u2764", i2 + 40 + mc.fontRendererCrack.getStringWidth(String.format("%.1f", (e.getHealth() + e.getAbsorptionAmount()) / 2.0F)), i1 + 27, hudColor, true);


        GL11.glPopMatrix();
    }

    public static void drawArmorHUD(EntityPlayer player, int x, int y) {
        GL11.glPushMatrix();

        Minecraft mc = Minecraft.getInstance();
        final List<ItemStack> stuff = new ObjectArrayList<>();
        final boolean onWater = mc.player.isEntityAlive() && mc.player.isInsideOfMaterial(Material.water);

        for (int index = 3; index >= 0; --index) {
            final ItemStack armor = player.inventory.armorInventory[index];

            if (armor != null) {
                stuff.add(armor);
            }
        }

        if (player.getCurrentEquippedItem() != null) {
            stuff.add(player.getCurrentEquippedItem());
        }

        int split = -3;
        int split2 = -3;

        for (ItemStack item : stuff) {
            if (mc.world != null) {
                RenderHelper.enableGUIStandardItemLighting();
                split += 16;
            }

            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(256);
            GlStateManager.enableBlend();
            mc.getRenderItem().zLevel = -150.0F;
            mc.getRenderItem().renderItemAndEffectIntoGUI(item, split + x + 18, y + 17);
            mc.getRenderItem().zLevel = 0.0F;
            GlStateManager.enableBlend();
            final float z = 0.5F;
            GlStateManager.scale(z, z, z);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
        }
        GL11.glPopMatrix();
    }

    public static void drawStack(FontRenderer font, boolean renderOverlay, ItemStack stack, float x, float y) {
        GL11.glPushMatrix();

        Minecraft mc = Minecraft.getInstance();

        if (mc.world != null) {
            RenderHelper.enableGUIStandardItemLighting();
        }

        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        GlStateManager.enableBlend();

        mc.getRenderItem().zLevel = -150.0F;
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);

        if (renderOverlay) {
            mc.getRenderItem().renderItemOverlayIntoGUI(font, stack, x, y, String.valueOf(stack.stackSize));
        }

        mc.getRenderItem().zLevel = 0.0F;

        GlStateManager.enableBlend();
        final float z = 0.5F;

        GlStateManager.scale(z, z, z);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();

        GL11.glPopMatrix();
    }

    public static void drawArrow(double x, double y, int lineWidth, int color, double length) {
        start2D();
        GL11.glPushMatrix();
        GL11.glLineWidth(lineWidth);
        setColor(new Color(color));
        GL11.glBegin(GL_LINE_STRIP);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x + 3, y + length);
        GL11.glVertex2d(x + 3 * 2, y);
        GL11.glEnd();
        GL11.glPopMatrix();
        stop2D();
    }


    public static void drawCheck(double x, double y, int lineWidth, int color) {
        start2D();
        GL11.glPushMatrix();
        GL11.glLineWidth(lineWidth);
        setColor(new Color(color));
        GL11.glBegin(GL_LINE_STRIP);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x + 2, y + 3);
        GL11.glVertex2d(x + 6, y - 2);
        GL11.glEnd();
        GL11.glPopMatrix();
        stop2D();
    }

    public static void drawCheckbox(double x, double y, double x2, double y2, double lineWidth, int color) {
        start2D();
        GL11.glPushMatrix();
        GL11.glLineWidth((float) lineWidth);
        setColor(new Color(color));
        GL11.glBegin(GL_LINE_STRIP);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y + (y2 - y));
        GL11.glVertex2d(x + (x2 - x), y + (y2 - y));
        GL11.glVertex2d(x + (x2 - x), y);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
        GL11.glPopMatrix();
        stop2D();
    }

    public static void drawCornerBox(double x, double y, double x2, double y2, double lw, Color color) {
        double width = Math.abs(x2 - x);
        double height = Math.abs(y2 - y);
        double halfWidth = width / 4;
        double halfHeight = height / 4;
        start2D();
        GL11.glPushMatrix();
        GL11.glLineWidth((float) lw);
        setColor(color);

        GL11.glBegin(GL_LINE_STRIP);
        GL11.glVertex2d(x + halfWidth, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y + halfHeight);
        GL11.glEnd();


        GL11.glBegin(GL_LINE_STRIP);
        GL11.glVertex2d(x, y + height - halfHeight);
        GL11.glVertex2d(x, y + height);
        GL11.glVertex2d(x + halfWidth, y + height);
        GL11.glEnd();

        GL11.glBegin(GL_LINE_STRIP);
        GL11.glVertex2d(x + width - halfWidth, y + height);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x + width, y + height - halfHeight);
        GL11.glEnd();

        GL11.glBegin(GL_LINE_STRIP);
        GL11.glVertex2d(x + width, y + halfHeight);
        GL11.glVertex2d(x + width, y);
        GL11.glVertex2d(x + width - halfWidth, y);
        GL11.glEnd();

        GL11.glPopMatrix();
        stop2D();
    }

    public static void start2D() {
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
    }

    public static void stop2D() {
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        enableTexture2D();
        disableBlend();
        glColor4f(1, 1, 1, 1);
    }

    public static void setColor(Color color) {
        float alpha = (color.getRGB() >> 24 & 0xFF) / 255.0F;
        float red = (color.getRGB() >> 16 & 0xFF) / 255.0F;
        float green = (color.getRGB() >> 8 & 0xFF) / 255.0F;
        float blue = (color.getRGB() & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void startDrawing() {
        GL11.glEnable(3042);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(2929);
        mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
    }

    public static void stopDrawing() {
        GL11.glDisable(3042);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(2929);
        GlStateManager.disableBlend();
    }

    public static void drawLine(Entity entity, double[] color, double x, double y, double z) {
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        if (color.length >= 4) {
            if (color[3] <= 0.1) return;
            GL11.glColor4d(color[0], color[1], color[2], color[3]);
        } else {
            GL11.glColor3d(color[0], color[1], color[2]);
        }
        GL11.glLineWidth(1.5f);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0D, mc.player.getEyeHeight(), 0.0D);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public static void drawLine(BlockPos blockPos, int color) {
        Minecraft mc = Minecraft.getInstance();
        double renderPosXDelta = blockPos.getX() - mc.getRenderManager().renderPosX + 0.5D;
        double renderPosYDelta = blockPos.getY() - mc.getRenderManager().renderPosY + 0.5D;
        double renderPosZDelta = blockPos.getZ() - mc.getRenderManager().renderPosZ + 0.5D;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0F);
        float blockPos9 = (float) (mc.player.posX - (double) blockPos.getX());
        float blockPos7 = (float) (mc.player.posY - (double) blockPos.getY());
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
        GL11.glVertex3d(0.0D, mc.player.getEyeHeight(), 0.0D);
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

}