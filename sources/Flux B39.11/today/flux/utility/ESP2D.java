package today.flux.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Render.ESP;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public enum ESP2D {
    INSTANCE;

    public Map<EntityLivingBase, double[]> entityConvertedPointsMap = new HashMap<>();

    public void renderBox(Minecraft mc) {
        GlStateManager.pushMatrix();
        ScaledResolution scaledRes = new ScaledResolution(mc);
        double twoDscale = (double) scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0);
        GlStateManager.scale(twoDscale, twoDscale, twoDscale);
        for (Entity entity : this.entityConvertedPointsMap.keySet()) {
            boolean shouldRender;
            EntityPlayer ent = (EntityPlayer) entity;
            double[] renderPositions = this.entityConvertedPointsMap.get(entity);
            double[] renderPositionsBottom = new double[]{renderPositions[4], renderPositions[5],
                    renderPositions[6]};
            double[] renderPositionsX = new double[]{renderPositions[7], renderPositions[8], renderPositions[9]};
            double[] renderPositionsX1 = new double[]{renderPositions[10], renderPositions[11], renderPositions[12]};
            double[] renderPositionsZ = new double[]{renderPositions[13], renderPositions[14], renderPositions[15]};
            double[] renderPositionsZ1 = new double[]{renderPositions[16], renderPositions[17], renderPositions[18]};
            double[] renderPositionsTop1 = new double[]{renderPositions[19], renderPositions[20],
                    renderPositions[21]};
            double[] renderPositionsTop2 = new double[]{renderPositions[22], renderPositions[23],
                    renderPositions[24]};
            shouldRender = renderPositions[3] > 0.0 && renderPositions[3] <= 1.0 && renderPositionsBottom[2] > 0.0
                    && renderPositionsBottom[2] <= 1.0 && renderPositionsX[2] > 0.0 && renderPositionsX[2] <= 1.0
                    && renderPositionsX1[2] > 0.0 && renderPositionsX1[2] <= 1.0 && renderPositionsZ[2] > 0.0
                    && renderPositionsZ[2] <= 1.0 && renderPositionsZ1[2] > 0.0 && renderPositionsZ1[2] <= 1.0
                    && renderPositionsTop1[2] > 0.0 && renderPositionsTop1[2] <= 1.0 && renderPositionsTop2[2] > 0.0
                    && renderPositionsTop2[2] <= 1.0;
            if ((double) mc.thePlayer.getDistanceToEntity(ent) < 2.5 && renderPositionsTop1[1] < 0.0) {
                shouldRender = false;
            }
            if (!shouldRender)
                continue;

            if (!ESP.invisible.getValueState()) {
                if (ent.isInvisible())
                    continue;
            }

            GlStateManager.pushMatrix();
            if (ent instanceof EntityPlayer && !(ent instanceof EntityPlayerSP)) {

                try {
                    GL11.glEnable((int) 3042);
                    GL11.glDisable((int) 3553);
                    this.rectangle(0.0f, 0.0f, 0.0f, 0.0f, this.getColor(0, 0));
                    double[] xValues = new double[]{renderPositions[0], renderPositionsBottom[0], renderPositionsX[0],
                            renderPositionsX1[0], renderPositionsZ[0], renderPositionsZ1[0], renderPositionsTop1[0],
                            renderPositionsTop2[0]};
                    double[] yValues = new double[]{renderPositions[1], renderPositionsBottom[1], renderPositionsX[1],
                            renderPositionsX1[1], renderPositionsZ[1], renderPositionsZ1[1], renderPositionsTop1[1],
                            renderPositionsTop2[1]};
                    double x = renderPositions[0];
                    double y = renderPositions[1];
                    double endx = renderPositionsBottom[0];
                    double endy = renderPositionsBottom[1];
                    for (double bdubs : xValues) {
                        if (!(bdubs < x))
                            continue;
                        x = bdubs;
                    }
                    for (double bdubs : xValues) {
                        if (!(bdubs > endx))
                            continue;
                        endx = bdubs;
                    }
                    for (double bdubs : yValues) {
                        if (!(bdubs < y))
                            continue;
                        y = bdubs;
                    }
                    for (double bdubs : yValues) {
                        if (!(bdubs > endy))
                            continue;
                        endy = bdubs;
                    }

                    // TODO Box Render Start

                    final int color = ColorUtils.fadeBetween(ESP.espColours.getColorInt(), ESP.espColours.getColorInt());

                    if(ESP.box.getValue()) {
                        String text = ent.getDisplayName().getFormattedText();
                        for (int i = 0; i < text.length(); i++) {
                            if ((text.charAt(i) == (char) 0x00A7) && (i + 1 < text.length())) {
                                char oneMore = Character.toLowerCase(text.charAt(i + 1));
                            }
                        }

                    rectangleBordered(x + 0.5, y + 0.5, endx - 0.5, endy - 0.5, 1.0, getColor(0, 0, 0, 0), color);
                    rectangleBordered(x - 0.5, y - 0.5, endx + 0.5, endy + 0.5, 1.0, getColor(0, 0), getColor(0, 150));
                    rectangleBordered(x + 1.5, y + 1.5, endx - 1.5, endy - 1.5, 1.0, getColor(0, 0), getColor(0, 150));
                    }
                    // TODO Box Render End

                    // TODO Health Render Start
                    if (ESP.health.getValue()) {
                        float health = ent.getHealth();
                        float[] fractions = new float[]{0.0f, 0.5f, 1.0f};
                        Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
                        float progress = health / ent.getMaxHealth();
                        Color customColor = health >= 0.0f ? blendColors(fractions, colors, progress).brighter()
                                : Color.RED;
                        if(ESP.healthColorMode.isCurrentMode("Static"))
                            customColor = ESP.healthColors.getColor();
                        double difference = y - endy + 0.5;
                        double healthLocation = endy + difference * (double) progress;
                        double strHealthLocation = endy - 5 + difference * (double) progress;
                        rectangleBordered(x - 6.5, y - 0.5, x - 2.5, endy, 1.0, getColor(0, 100), getColor(0, 150));
                        rectangle(x - 5.5, endy - 1.0, x - 3.5, healthLocation, customColor.getRGB());
                        if (-difference > 50.0) {
                            for (int i = 1; i < 10; ++i) {
                                double dThing = difference / 10.0 * (double) i;
                                rectangle(x - 6.5, endy - 0.5 + dThing, x - 2.5, endy - 0.5 + dThing - 1.0, getColor(0));
                            }
                        }
                        if ((int) this.getIncremental(progress * 100.0f, 1.0) <= 100) {
                            GlStateManager.pushMatrix();
                            GlStateManager.scale(2f, 2f, 2f);
                            if (ESP.tags.getValue()) {
                                String nigger = (int) getIncremental(health * 5.0f, 1.0) + "HP";
                                FontManager.tahoma13.drawString(nigger, (float) (x - (double) (FontManager.tahoma13.getStringWidth(nigger) * 2.0f)) / 2.0f - 5, ((float) (strHealthLocation) + FontManager.tahoma13.getHeight() / 2.0f) / 2.0f - 2, -1);
                                FontManager.tahoma13.drawStringWithShadow(nigger, (float) (x - (double) (FontManager.tahoma13.getStringWidth(nigger) * 2.0f)) / 2.0f - 5, ((float) (strHealthLocation) + FontManager.tahoma13.getHeight() / 2.0f) / 2.0f - 2, -1);
                            }
                            GlStateManager.popMatrix();

                        }
                    }
                    // TODO Health Render End

                    // TODO ARMOR RENDER START
                    float var1 = (float) ((endy - y) / 4);
                    ItemStack stack1 = ((EntityPlayer) ent).getEquipmentInSlot(4);
                    if (ESP.armor.getValue()) {
                        if (stack1 != null) {
                            rectangleBordered(endx + 1, y + 1, endx + 6, y + var1, 1, getColor(28, 156, 179, 100),
                                    getColor(0, 255));
                            float diff1 = (float) ((y + var1 - 1) - (y + 2));
                            double percent = 1 - (double) stack1.getItemDamage() / (double) stack1.getMaxDamage();
                            rectangle(endx + 2, y + var1 - 1, endx + 5, y + var1 - 1 - (diff1 * percent),
                                    getColor(78, 206, 229));
                            if (ESP.tags.getValue()) {
                                this.drawStringWithShadow(stack1.getMaxDamage() - stack1.getItemDamage() + "", endx + 7, (y + var1 - 1 - (diff1 / 2)) - (mc.fontRendererObj.FONT_HEIGHT / 2), -1);
                            }
                        }
                        ItemStack stack2 = ((EntityPlayer) ent).getEquipmentInSlot(3);
                        if (stack2 != null) {
                            rectangleBordered(endx + 1, y + var1, endx + 6, y + var1 * 2, 1, getColor(28, 156, 179, 100),
                                    getColor(0, 255));
                            float diff1 = (float) ((y + var1 * 2) - (y + var1 + 2));
                            double percent = 1 - (double) stack2.getItemDamage() * 1 / (double) stack2.getMaxDamage();
                            rectangle(endx + 2, (y + var1 * 2), endx + 5, (y + var1 * 2) - (diff1 * percent),
                                    getColor(78, 206, 229));
                            if (ESP.tags.getValue()) {
                                this.drawStringWithShadow(stack2.getMaxDamage() - stack2.getItemDamage() + "", endx + 7,
                                        ((y + var1 * 2) - (diff1 / 2)) - (mc.fontRendererObj.FONT_HEIGHT / 2), -1);
                            }
                        }
                        ItemStack stack3 = ((EntityPlayer) ent).getEquipmentInSlot(2);
                        if (stack3 != null) {
                            rectangleBordered(endx + 1, y + var1 * 2, endx + 6, y + var1 * 3, 1,
                                    getColor(28, 156, 179, 100), getColor(0, 255));
                            float diff1 = (float) ((y + var1 * 3) - (y + var1 * 2 + 2));
                            double percent = 1 - (double) stack3.getItemDamage() * 1 / (double) stack3.getMaxDamage();
                            rectangle(endx + 2, (y + var1 * 3), endx + 5, (y + var1 * 3) - (diff1 * percent),
                                    getColor(78, 206, 229));
                            if (ESP.tags.getValue()) {
                                this.drawStringWithShadow(stack3.getMaxDamage() - stack3.getItemDamage() + "", endx + 7,
                                        ((y + var1 * 3) - (diff1 / 2)) - (mc.fontRendererObj.FONT_HEIGHT / 2), -1);
                            }
                        }
                        ItemStack stack4 = ((EntityPlayer) ent).getEquipmentInSlot(1);
                        if (stack4 != null) {
                            rectangleBordered(endx + 1, y + var1 * 3, endx + 6, y + var1 * 4, 1,
                                    getColor(28, 156, 179, 100), getColor(0, 255));
                            float diff1 = (float) ((y + var1 * 4) - (y + var1 * 3 + 2));
                            double percent = 1 - (double) stack4.getItemDamage() * 1 / (double) stack4.getMaxDamage();
                            rectangle(endx + 2, (y + var1 * 4) - 1, endx + 5, (y + var1 * 4) - (diff1 * percent),
                                    getColor(78, 206, 229));
                            if (ESP.tags.getValue()) {
                                this.drawStringWithShadow(stack4.getMaxDamage() - stack4.getItemDamage() + "", endx + 7,
                                        ((y + var1 * 4) - (diff1 / 2)) - (mc.fontRendererObj.FONT_HEIGHT / 2), -1);
                            }
                        }
                    }
                    // TODO ARMOR RENDER END
                } catch (Exception ex) {
                }
            }

            GlStateManager.popMatrix();
            GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
        }
        GL11.glScalef((float) 1.0f, (float) 1.0f, (float) 1.0f);
        GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
        GlStateManager.popMatrix();
        rectangle(0.0, 0.0, 0.0, 0.0, -1);
    }

    public void drawStringWithShadow(String text, double x, double y, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, (float) x, (float) y, color);
    }

    public double getIncremental(double val, double inc) {
        double one = 1.0D / inc;
        return (double) Math.round(val * one) / one;
    }

    public Color blendColors(float[] fractions, Color[] colors, float progress) {
        if (fractions == null)
            throw new IllegalArgumentException("Fractions can't be null");
        if (colors == null)
            throw new IllegalArgumentException("Colours can't be null");
        if (fractions.length != colors.length)
            throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        int[] indicies = getFractionIndicies(fractions, progress);
        float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
        Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
        float max = range[1] - range[0];
        float value = progress - range[0];
        float weight = value / max;
        return blend(colorRange[0], colorRange[1], 1.0f - weight);
    }

    public int[] getFractionIndicies(float[] fractions, float progress) {
        int startPoint;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }

    public Color blend(Color color1, Color color2, double ratio) {
        float r = (float) ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color = null;
        try {
            color = new Color(red, green, blue);
        } catch (IllegalArgumentException exp) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            System.out.println(nf.format(red) + "; " + nf.format(green) + "; " + nf.format(blue));
            exp.printStackTrace();
        }
        return color;
    }

    public void updatePositions(Minecraft mc) {
        this.entityConvertedPointsMap.clear();
        float pTicks = mc.timer.renderPartialTicks;
        for (Object e2 : mc.theWorld.getLoadedEntityList()) {
            EntityPlayer ent;
            if (!(e2 instanceof EntityPlayer) || (ent = (EntityPlayer) e2) == mc.thePlayer)
                continue;
            if (ModuleManager.noRenderMod.isEnabled() && ModuleManager.noRenderMod.players.getValueState() && ent instanceof EntityPlayer && ent != mc.thePlayer)
                continue;
            double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double) pTicks
                    - mc.getRenderManager().viewerPosX + 0.36;
            double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double) pTicks
                    - mc.getRenderManager().viewerPosY;
            double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double) pTicks
                    - mc.getRenderManager().viewerPosZ + 0.36;
            double topY = y += (double) ent.height + 0.15;
            double[] convertedPoints = convertTo2D(x, y, z);
            double[] convertedPoints22 = convertTo2D(x - 0.36, y, z - 0.36);
            double xd = 0.0;
            if (!(convertedPoints22[2] >= 0.0) || !(convertedPoints22[2] < 1.0))
                continue;
            x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double) pTicks - mc.getRenderManager().viewerPosX
                    - 0.36;
            z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double) pTicks - mc.getRenderManager().viewerPosZ
                    - 0.36;
            double[] convertedPointsBottom = convertTo2D(x, y, z);
            y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double) pTicks - mc.getRenderManager().viewerPosY
                    - 0.05;
            double[] convertedPointsx = convertTo2D(x, y, z);
            x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double) pTicks - mc.getRenderManager().viewerPosX
                    - 0.36;
            z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double) pTicks - mc.getRenderManager().viewerPosZ
                    + 0.36;
            double[] convertedPointsTop1 = convertTo2D(x, topY, z);
            double[] convertedPointsx1 = convertTo2D(x, y, z);
            x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double) pTicks - mc.getRenderManager().viewerPosX
                    + 0.36;
            z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double) pTicks - mc.getRenderManager().viewerPosZ
                    + 0.36;
            double[] convertedPointsz = convertTo2D(x, y, z);
            x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double) pTicks - mc.getRenderManager().viewerPosX
                    + 0.36;
            z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double) pTicks - mc.getRenderManager().viewerPosZ
                    - 0.36;
            double[] convertedPointsTop2 = convertTo2D(x, topY, z);
            double[] convertedPointsz1 = convertTo2D(x, y, z);
            this.entityConvertedPointsMap.put(ent,
                    new double[]{convertedPoints[0], convertedPoints[1], xd, convertedPoints[2],
                            convertedPointsBottom[0], convertedPointsBottom[1], convertedPointsBottom[2],
                            convertedPointsx[0], convertedPointsx[1], convertedPointsx[2], convertedPointsx1[0],
                            convertedPointsx1[1], convertedPointsx1[2], convertedPointsz[0], convertedPointsz[1],
                            convertedPointsz[2], convertedPointsz1[0], convertedPointsz1[1], convertedPointsz1[2],
                            convertedPointsTop1[0], convertedPointsTop1[1], convertedPointsTop1[2],
                            convertedPointsTop2[0], convertedPointsTop2[1], convertedPointsTop2[2]});
        }
    }

    public double[] convertTo2D(double x, double y, double z) {
        double[] convertedPoints = convertTo2D2(x, y, z);
        return convertedPoints;
    }

    public double[] convertTo2D2(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);
        return result
                ? new double[]{(double) screenCoords.get(0),
                (double) ((float) Display.getHeight() - screenCoords.get(1)), (double) screenCoords.get(2)}
                : null;
    }

    public void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor,
                                  int borderColor) {
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

    public void rectangle(double left, double top, double right, double bottom, int color) {

        double var5;

        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }

        if (top < bottom) {
            var5 = top;
            top = bottom;
            bottom = var5;
        }

        float var11 = (float) (color >> 24 & 255) / 255.0F;
        float var6 = (float) (color >> 16 & 255) / 255.0F;
        float var7 = (float) (color >> 8 & 255) / 255.0F;
        float var8 = (float) (color & 255) / 255.0F;
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0.0D).endVertex();
        worldRenderer.pos(right, bottom, 0.0D).endVertex();
        worldRenderer.pos(right, top, 0.0D).endVertex();
        worldRenderer.pos(left, top, 0.0D).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public int getColor(int red, int green, int blue, int alpha) {
        int color = MathHelper.clamp_int(alpha, 0, 255) << 24;
        color |= MathHelper.clamp_int(red, 0, 255) << 16;
        color |= MathHelper.clamp_int(green, 0, 255) << 8;
        color |= MathHelper.clamp_int(blue, 0, 255);
        return color;
    }

    public int getColor(int red, int green, int blue) {
        return getColor(red, green, blue, 255);
    }

    public int getColor(int brightness) {
        return getColor(brightness, brightness, brightness, 255);
    }

    public int getColor(int brightness, int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }
}
