package info.sigmaclient.module.impl.render;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventNametagRender;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.event.impl.EventRenderGui;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.MathUtils;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class ESP2D extends Module {

    public static String TEAM = "TEAM";
    private final String INVISIBLES = "INVISIBLES";
    private String CUSTOMTAG = "ITEMTAG";
    private String ITEMS = "ITEMS";
    private String HEALTH = "HEALTH";
    private String ARMOR = "ARMOR";
    private String NAME = "NAMES";

    public ESP2D(ModuleData data) {
        super(data);
        settings.put(NAME, new Setting<>(NAME, false, "Renders player name."));
        settings.put(TEAM, new Setting<>(TEAM, false, "Team colors."));
        settings.put(INVISIBLES, new Setting<>(INVISIBLES, false, "Show invisibles."));
        settings.put(HEALTH, new Setting<>(HEALTH, true, "Renders in small text entity HP."));
        settings.put(ITEMS, new Setting<>(ITEMS, true, "Shows player's current item."));
        settings.put(CUSTOMTAG, new Setting<>(CUSTOMTAG, false, "Shows the custom name the item has."));
        settings.put(ARMOR, new Setting<>(ARMOR, false, "Shows a Aimware like armor bar(s) on the left."));
    }

    private double gradualFOVModifier;
    public static Map<EntityLivingBase, double[]> entityPositionstop = new HashMap();
    public static Map<EntityLivingBase, double[]> entityPositionsbottom = new HashMap();

    @Override
    @RegisterEvent(events = {EventRender3D.class, EventRenderGui.class, EventNametagRender.class})
    public void onEvent(Event event) {
        if (event instanceof EventNametagRender) {
            event.setCancelled(true);
        }
        if (event instanceof EventRender3D) {
            try {
                updatePositions();
            } catch (Exception e) {
            }
        }
        if (event instanceof EventRenderGui) {
            EventRenderGui er = (EventRenderGui) event;
            GlStateManager.pushMatrix();
            ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0D);
            GlStateManager.scale(twoDscale, twoDscale, twoDscale);
            for (Entity ent : entityPositionstop.keySet()) {
                double[] renderPositions = entityPositionstop.get(ent);
                double[] renderPositionsBottom = entityPositionsbottom.get(ent);
                if ((renderPositions[3] > 0.0D) || (renderPositions[3] <= 1.0D)) {
                    GlStateManager.pushMatrix();
                    if ((((Boolean)settings.get(INVISIBLES).getValue()) || !ent.isInvisible()) && ent instanceof EntityPlayer && !(ent instanceof EntityPlayerSP)) {
                        scale(ent);
                        try {
                            float y = (float) renderPositions[1];
                            float endy = (float) renderPositionsBottom[1];
                            float meme = endy - y;
                            float x = (float) renderPositions[0] - (meme / 4f);
                            float endx = (float) renderPositionsBottom[0] + (meme / 4f);
                            if (x > endx) {
                                endx = x;
                                x = (float) renderPositionsBottom[0] + (meme / 4f);
                            }
                            GlStateManager.pushMatrix();
                            GlStateManager.scale(2, 2, 2);
                            GlStateManager.popMatrix();
                            GL11.glEnable(GL11.GL_BLEND);
                            GL11.glDisable(GL11.GL_TEXTURE_2D);
                            int color = Colors.getColor(255,0,0);
                            if (FriendManager.isFriend(ent.getName())) {
                                color =  Colors.getColor(0,255,0);
                            } else if (!mc.thePlayer.canEntityBeSeen(ent)) {
                                color = Colors.getColor(255,255,0);
                            }
                            if (((Boolean) settings.get(TEAM).getValue())) {
                                String text = ent.getDisplayName().getFormattedText();
                                for (int i = 0; i < text.length(); i++)
                                    if ((text.charAt(i) == (char) 0x00A7) && (i + 1 < text.length())) {
                                        char oneMore = Character.toLowerCase(text.charAt(i + 1));
                                        int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
                                        if (colorCode < 16) {
                                            try {
                                                Color axd = new Color(mc.fontRendererObj.colorCode[colorCode]);
                                                color = Colors.getColor(axd.getRed(),axd.getGreen(), axd.getBlue(),255);
                                            } catch (ArrayIndexOutOfBoundsException exception) {
                                            }
                                        }
                                    }
                            }
                            if(((EntityPlayer)ent).isMurderer) {
                                color = Colors.getColor(189, 44, 221);
                            }
                            RenderingUtil.rectangleBordered(x, y, endx, endy, 2.25, Colors.getColor(0, 0, 0, 0), color);
                            RenderingUtil.rectangleBordered(x - 0.5, y - 0.5, endx + 0.5, endy + 0.5, 0.9, Colors.getColor(0, 0), Colors.getColor(0));
                            RenderingUtil.rectangleBordered(x + 2.5, y + 2.5, endx - 2.5, endy - 2.5, 0.9, Colors.getColor(0, 0), Colors.getColor(0));
                            RenderingUtil.rectangleBordered(x - 5, y - 1, x - 1, endy, 1, Colors.getColor(0, 100), Colors.getColor(0, 255));
                            if (!Client.getModuleManager().get(Nametags.class).isEnabled() && ((Boolean)settings.get(NAME).getValue())) {
                                GlStateManager.pushMatrix();
                                String renderName = FriendManager.isFriend(ent.getName()) ? FriendManager.getAlias(ent.getName()) : ent.getName();
                                TTFFontRenderer font = Client.fm.getFont("Verdana Bold 16");
                                float meme2 = ((endx - x) / 2 - (font.getWidth(renderName) / 2f));
                                font.drawStringWithShadow(renderName + " " + (int) mc.thePlayer.getDistanceToEntity(ent) + "m", (x + meme2), (y - font.getHeight(renderName) - 5), FriendManager.isFriend(ent.getName()) ? Colors.getColor(192,80,64) : -1);
                                GlStateManager.popMatrix();
                            }
                            if (((EntityPlayer) ent).getCurrentEquippedItem() != null && ((Boolean) settings.get(ITEMS).getValue())) {
                                GlStateManager.pushMatrix();
                                GlStateManager.scale(2, 2, 2);
                                ItemStack stack = ((EntityPlayer) ent).getCurrentEquippedItem();
                                String customName = ((Boolean) settings.get(CUSTOMTAG).getValue()) ? ((EntityPlayer) ent).getCurrentEquippedItem().getDisplayName() : ((EntityPlayer) ent).getCurrentEquippedItem().getItem().getItemStackDisplayName(stack);
                                TTFFontRenderer font = Client.fm.getFont("Verdana 12");
                                float meme2 = ((endx - x) / 2 - (font.getWidth(customName) / 1f));
                                font.drawStringWithShadow(customName, (x + meme2) / 2f, (endy + font.getHeight(customName) / 2 * 2f) / 2f, -1);
                                GlStateManager.popMatrix();
                            }
                            if ((Boolean) settings.get(ARMOR).getValue()) {
                                float var1 = (endy - y) / 4;
                                ItemStack stack = ((EntityPlayer) ent).getEquipmentInSlot(4);
                                if (stack != null) {
                                    RenderingUtil.rectangleBordered(endx + 1, y + 1, endx + 6, y + var1, 1, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                                    float diff1 = (y + var1 - 1) - (y + 2);
                                    double percent = 1 - (double) stack.getItemDamage() / (double) stack.getMaxDamage();
                                    RenderingUtil.rectangle(endx + 2, y + var1 - 1, endx + 5, y + var1 - 1 - (diff1 * percent), Colors.getColor(78, 206, 229));
                                    mc.fontRendererObj.drawStringWithShadow(stack.getMaxDamage() - stack.getItemDamage() + "", endx + 7, (y + var1 - 1 - (diff1 / 2)) - (mc.fontRendererObj.FONT_HEIGHT/2), -1);
                                }
                                ItemStack stack2 = ((EntityPlayer) ent).getEquipmentInSlot(3);
                                if (stack2 != null) {
                                    RenderingUtil.rectangleBordered(endx + 1, y + var1, endx + 6, y + var1 * 2, 1, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                                    float diff1 = (y + var1 * 2) - (y + var1 + 2);
                                    double percent = 1 - (double) stack2.getItemDamage() * 1 / (double) stack2.getMaxDamage();
                                    RenderingUtil.rectangle(endx + 2, (y + var1 * 2), endx + 5, (y + var1 * 2) - (diff1 * percent), Colors.getColor(78, 206, 229));
                                    mc.fontRendererObj.drawStringWithShadow(stack2.getMaxDamage() - stack2.getItemDamage() + "", endx + 7, ((y + var1 * 2) - (diff1 / 2)) - (mc.fontRendererObj.FONT_HEIGHT/2), -1);
                                }
                                ItemStack stack3 = ((EntityPlayer) ent).getEquipmentInSlot(2);
                                if (stack3 != null) {
                                    RenderingUtil.rectangleBordered(endx + 1, y + var1 * 2, endx + 6, y + var1 * 3, 1, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                                    float diff1 = (y + var1 * 3) - (y + var1 * 2 + 2);
                                    double percent = 1 - (double) stack3.getItemDamage() * 1 / (double) stack3.getMaxDamage();
                                    RenderingUtil.rectangle(endx + 2, (y + var1 * 3), endx + 5, (y + var1 * 3) - (diff1 * percent), Colors.getColor(78, 206, 229));
                                    mc.fontRendererObj.drawStringWithShadow(stack3.getMaxDamage() - stack3.getItemDamage() + "", endx + 7, ((y + var1 * 3) - (diff1 / 2)) - (mc.fontRendererObj.FONT_HEIGHT/2), -1);
                                }
                                ItemStack stack4 = ((EntityPlayer) ent).getEquipmentInSlot(1);
                                if (stack4 != null) {
                                    RenderingUtil.rectangleBordered(endx + 1, y + var1 * 3, endx + 6, y + var1 * 4, 1, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                                    float diff1 = (y + var1 * 4) - (y + var1 * 3 + 2);
                                    double percent = 1 - (double) stack4.getItemDamage() * 1 / (double) stack4.getMaxDamage();
                                    RenderingUtil.rectangle(endx + 2, (y + var1 * 4) - 1, endx + 5, (y + var1 * 4) - (diff1 * percent), Colors.getColor(78, 206, 229));
                                    mc.fontRendererObj.drawStringWithShadow(stack4.getMaxDamage() - stack4.getItemDamage() + "", endx + 7, ((y + var1 * 4) - (diff1 / 2)) - (mc.fontRendererObj.FONT_HEIGHT/2), -1);
                                }
                            }
                            float health = ((EntityPlayer) ent).getHealth();
                            float[] fractions = new float[]{0f, 0.5f, 1f};
                            Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
                            float progress = (health * 5) * 0.01f;
                            Color customColor = blendColors(fractions, colors, progress).brighter();
                            double healthLocation = endy + (y - endy) * ((health * 5) * 0.01f);
                            RenderingUtil.rectangle(x - 4, endy - 1, x - 2, healthLocation, customColor.getRGB());
                            if ((int) MathUtils.getIncremental(health * 5, 1) != 100 && ((Boolean) settings.get(HEALTH).getValue())) {
                                GlStateManager.pushMatrix();
                                GlStateManager.scale(2, 2, 2);
                                String nigger = (int) MathUtils.getIncremental(health * 5, 1) + "HP";
                                TTFFontRenderer font = Client.fm.getFont("Verdana 12");
                                font.drawStringWithShadow(nigger, (x - 5 - font.getWidth(nigger) * 2) / 2, ((float) ((int) healthLocation) + font.getHeight(nigger) / 2) / 2, -1);
                                GlStateManager.popMatrix();
                            }
                        } catch (Exception e) {
                        }
                    }
                    GlStateManager.popMatrix();
                    GL11.glColor4f(1, 1, 1, 1);
                }
            }
            GL11.glScalef(1, 1, 1);
            GL11.glColor4f(1, 1, 1, 1);
            GlStateManager.popMatrix();
        }
    }

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        Color color = null;
        if (fractions != null) {
            if (colors != null) {
                if (fractions.length == colors.length) {
                    int[] indicies = getFractionIndicies(fractions, progress);

                    float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
                    Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};

                    float max = range[1] - range[0];
                    float value = progress - range[0];
                    float weight = value / max;

                    color = blend(colorRange[0], colorRange[1], 1f - weight);
                } else {
                    throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
                }
            } else {
                throw new IllegalArgumentException("Colours can't be null");
            }
        } else {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        return color;
    }

    public static int[] getFractionIndicies(float[] fractions, float progress) {
        int[] range = new int[2];

        int startPoint = 0;
        while (startPoint < fractions.length && fractions[startPoint] <= progress) {
            startPoint++;
        }

        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }

        range[0] = startPoint - 1;
        range[1] = startPoint;

        return range;
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float) ratio;
        float ir = (float) 1.0 - r;

        float rgb1[] = new float[3];
        float rgb2[] = new float[3];

        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);

        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;

        if (red < 0) {
            red = 0;
        } else if (red > 255) {
            red = 255;
        }
        if (green < 0) {
            green = 0;
        } else if (green > 255) {
            green = 255;
        }
        if (blue < 0) {
            blue = 0;
        } else if (blue > 255) {
            blue = 255;
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

    private void updatePositions() {
        entityPositionstop.clear();
        entityPositionsbottom.clear();
        float pTicks = mc.timer.renderPartialTicks;
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityPlayer) {
                EntityPlayer ent = (EntityPlayer) o;
                double x;
                double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - mc.getRenderManager().viewerPosY;
                double z;
                x = ent.lastTickPosX + ((ent.posX + 10) - (ent.lastTickPosX + 10)) * pTicks - mc.getRenderManager().viewerPosX;
                z = ent.lastTickPosZ + ((ent.posZ + 10) - (ent.lastTickPosZ + 10)) * pTicks - mc.getRenderManager().viewerPosZ;
                y += ent.height + 0.2D;
                double[] convertedPoints = convertTo2D(x, y, z);
                double xd = Math.abs(convertTo2D(x, y + 1.0D, z, ent)[1] - convertTo2D(x, y, z, ent)[1]);
                assert convertedPoints != null;
                if ((convertedPoints[2] >= 0.0D) && (convertedPoints[2] < 1.0D)) {
                    entityPositionstop.put(ent, new double[]{convertedPoints[0], convertedPoints[1], xd, convertedPoints[2]});
                    y = ent.lastTickPosY + ((ent.posY - 2.2) - (ent.lastTickPosY - 2.2)) * pTicks - mc.getRenderManager().viewerPosY;
                    entityPositionsbottom.put(ent, new double[]{convertTo2D(x, y, z)[0], convertTo2D(x, y, z)[1], xd, convertTo2D(x, y, z)[2]});
                }
            }
        }
    }

    private double[] convertTo2D(double x, double y, double z, Entity ent) {
        return convertTo2D(x, y, z);
    }

    private void scale(Entity ent) {
        float scale = (float) 1;
        float target = scale * (mc.gameSettings.fovSetting
                / (mc.gameSettings.fovSetting/*
                                                 * *
												 * mc.thePlayer.getFovModifier()
												 *//* .func_175156_o() */));
        if ((this.gradualFOVModifier == 0.0D) || (Double.isNaN(this.gradualFOVModifier))) {
            this.gradualFOVModifier = target;
        }
        this.gradualFOVModifier += (target - this.gradualFOVModifier) / (Minecraft.debugFPS * 0.7D);

        scale = (float) (scale * this.gradualFOVModifier);

        GlStateManager.scale(scale, scale, scale);
    }

    private double[] convertTo2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);
        if (result) {
            return new double[]{screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2)};
        }
        return null;
    }

}
