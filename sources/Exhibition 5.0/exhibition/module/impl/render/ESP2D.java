// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.render;

import java.util.HashMap;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.BufferUtils;
import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import java.text.NumberFormat;
import exhibition.event.RegisterEvent;
import net.minecraft.item.ItemStack;
import exhibition.management.ColorObject;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import exhibition.util.MathUtils;
import java.awt.Color;
import exhibition.Client;
import exhibition.util.RenderingUtil;
import exhibition.util.render.Colors;
import exhibition.util.TeamUtils;
import exhibition.management.friend.FriendManager;
import exhibition.management.ColorManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import exhibition.event.impl.EventRenderGui;
import exhibition.event.impl.EventRender3D;
import exhibition.event.impl.EventNametagRender;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import net.minecraft.entity.EntityLivingBase;
import java.util.Map;
import exhibition.module.Module;

public class ESP2D extends Module
{
    public static String TEAM;
    private final String INVISIBLES = "INVISIBLES";
    private String CUSTOMTAG;
    private String ITEMS;
    private String HEALTH;
    private double gradualFOVModifier;
    private Character formatChar;
    public static Map<EntityLivingBase, double[]> entityPositionstop;
    public static Map<EntityLivingBase, double[]> entityPositionsbottom;
    
    public ESP2D(final ModuleData data) {
        super(data);
        this.CUSTOMTAG = "ITEMTAG";
        this.ITEMS = "ITEMS";
        this.HEALTH = "HEALTH";
        this.formatChar = new Character('§');
        ((HashMap<String, Setting<Boolean>>)this.settings).put(ESP2D.TEAM, new Setting<Boolean>(ESP2D.TEAM, false, "Team colors."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("INVISIBLES", new Setting<Boolean>("INVISIBLES", false, "Show invisibles."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.HEALTH, new Setting<Boolean>(this.HEALTH, true, "Renders in small text entity HP."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.ITEMS, new Setting<Boolean>(this.ITEMS, true, "Shows player's current item."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.CUSTOMTAG, new Setting<Boolean>(this.CUSTOMTAG, false, "Shows the custom name the item has. Requires ITEMS set to true."));
    }
    
    @RegisterEvent(events = { EventRender3D.class, EventRenderGui.class, EventNametagRender.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventNametagRender) {
            event.setCancelled(true);
        }
        if (event instanceof EventRender3D) {
            try {
                this.updatePositions();
            }
            catch (Exception ex) {}
        }
        if (event instanceof EventRenderGui) {
            final EventRenderGui er = (EventRenderGui)event;
            GlStateManager.pushMatrix();
            final ScaledResolution scaledRes = new ScaledResolution(ESP2D.mc, ESP2D.mc.displayWidth, ESP2D.mc.displayHeight);
            final double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0);
            GlStateManager.scale(twoDscale, twoDscale, twoDscale);
            for (final Entity ent : ESP2D.entityPositionstop.keySet()) {
                final double[] renderPositions = ESP2D.entityPositionstop.get(ent);
                final double[] renderPositionsBottom = ESP2D.entityPositionsbottom.get(ent);
                if (renderPositions[3] > 0.0 || renderPositions[3] <= 1.0) {
                    GlStateManager.pushMatrix();
                    if (((HashMap<K, Setting<Boolean>>)this.settings).get("INVISIBLES").getValue() || (!ent.isInvisible() && ent instanceof EntityPlayer && !(ent instanceof EntityPlayerSP))) {
                        this.scale();
                        try {
                            final float y = (float)renderPositions[1];
                            final float endy = (float)renderPositionsBottom[1];
                            final float meme = endy - y;
                            float x = (float)renderPositions[0] - meme / 4.0f;
                            float endx = (float)renderPositionsBottom[0] + meme / 4.0f;
                            if (x > endx) {
                                endx = x;
                                x = (float)renderPositionsBottom[0] + meme / 4.0f;
                            }
                            GlStateManager.pushMatrix();
                            GlStateManager.scale(2.0f, 2.0f, 2.0f);
                            GlStateManager.popMatrix();
                            GL11.glEnable(3042);
                            GL11.glDisable(3553);
                            int color = ColorManager.getEnemyVisible().getColorInt();
                            if (FriendManager.isFriend(ent.getName())) {
                                color = (ESP2D.mc.thePlayer.canEntityBeSeen(ent) ? ColorManager.getFriendlyVisible().getColorInt() : ColorManager.getFriendlyInvisible().getColorInt());
                            }
                            else if (!ESP2D.mc.thePlayer.canEntityBeSeen(ent)) {
                                color = ColorManager.getEnemyInvisible().getColorInt();
                            }
                            if (((HashMap<K, Setting<Boolean>>)this.settings).get(ESP2D.TEAM).getValue()) {
                                if (TeamUtils.isTeam(ESP2D.mc.thePlayer, (EntityPlayer)ent)) {
                                    color = Colors.getColor(20, 40, 255, 255);
                                }
                                else {
                                    color = Colors.getColor(255, 40, 20, 255);
                                }
                            }
                            RenderingUtil.rectangleBordered(x, y, endx, endy, 2.25, Colors.getColor(0, 0, 0, 0), color);
                            RenderingUtil.rectangleBordered(x - 0.5, y - 0.5, endx + 0.5, endy + 0.5, 0.9, Colors.getColor(0, 0), Colors.getColor(0));
                            RenderingUtil.rectangleBordered(x + 2.5, y + 2.5, endx - 2.5, endy - 2.5, 0.9, Colors.getColor(0, 0), Colors.getColor(0));
                            RenderingUtil.rectangleBordered(x - 5.0f, y - 1.0f, x - 1.0f, endy, 1.0, Colors.getColor(0, 100), Colors.getColor(0, 255));
                            if (!Client.getModuleManager().get(Tags.class).isEnabled() && !Client.getModuleManager().get(Nametags.class).isEnabled()) {
                                GlStateManager.pushMatrix();
                                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                                final String renderName = FriendManager.isFriend(ent.getName()) ? FriendManager.getAlias(ent.getName()) : ent.getName();
                                final float meme2 = (endx - x) / 2.0f - Client.fs.getWidth(renderName) / 1.0f;
                                final ColorObject temp = ColorManager.getFriendlyVisible();
                                Client.fs.drawStringWithShadow(renderName + " " + (int)ESP2D.mc.thePlayer.getDistanceToEntity(ent) + "m", (x + meme2) / 2.0f, (y - Client.fs.getHeight(renderName) / 1.5f * 2.0f) / 2.0f, FriendManager.isFriend(ent.getName()) ? Colors.getColor(temp.red, temp.green, temp.blue) : -1);
                                GlStateManager.popMatrix();
                            }
                            if (((EntityPlayer)ent).getCurrentEquippedItem() != null && ((HashMap<K, Setting<Boolean>>)this.settings).get(this.ITEMS).getValue()) {
                                GlStateManager.pushMatrix();
                                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                                final ItemStack stack = ((EntityPlayer)ent).getCurrentEquippedItem();
                                final String customName = ((HashMap<K, Setting<Boolean>>)this.settings).get(this.CUSTOMTAG).getValue() ? ((EntityPlayer)ent).getCurrentEquippedItem().getDisplayName() : ((EntityPlayer)ent).getCurrentEquippedItem().getItem().getItemStackDisplayName(stack);
                                final float meme3 = (endx - x) / 2.0f - Client.fss.getWidth(customName) / 1.0f;
                                Client.fss.drawStringWithShadow(customName, (x + meme3) / 2.0f, (endy + Client.fss.getHeight(customName) / 2.0f * 2.0f) / 2.0f, -1);
                                GlStateManager.popMatrix();
                            }
                            final float var1 = (endy - y) / 4.0f;
                            final ItemStack stack2 = ((EntityPlayer)ent).getEquipmentInSlot(4);
                            if (stack2 != null) {
                                RenderingUtil.rectangleBordered(endx + 1.0f, y + 1.0f, endx + 6.0f, y + var1, 1.0, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                                final float diff1 = y + var1 - 1.0f - (y + 2.0f);
                                final double percent = 1.0 - stack2.getItemDamage() / stack2.getMaxDamage();
                                RenderingUtil.rectangle(endx + 2.0f, y + var1 - 1.0f, endx + 5.0f, y + var1 - 1.0f - diff1 * percent, Colors.getColor(78, 206, 229));
                                ESP2D.mc.fontRendererObj.drawStringWithShadow(stack2.getMaxDamage() - stack2.getItemDamage() + "", endx + 7.0f, y + var1 - 1.0f - diff1 / 2.0f, -1);
                            }
                            final ItemStack stack3 = ((EntityPlayer)ent).getEquipmentInSlot(3);
                            if (stack3 != null) {
                                RenderingUtil.rectangleBordered(endx + 1.0f, y + var1, endx + 6.0f, y + var1 * 2.0f, 1.0, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                                final float diff2 = y + var1 * 2.0f - (y + var1 + 2.0f);
                                final double percent2 = 1.0 - stack3.getItemDamage() * 1.0 / stack3.getMaxDamage();
                                RenderingUtil.rectangle(endx + 2.0f, y + var1 * 2.0f, endx + 5.0f, y + var1 * 2.0f - diff2 * percent2, Colors.getColor(78, 206, 229));
                                ESP2D.mc.fontRendererObj.drawStringWithShadow(stack3.getMaxDamage() - stack3.getItemDamage() + "", endx + 7.0f, y + var1 * 2.0f - diff2 / 2.0f, -1);
                            }
                            final ItemStack stack4 = ((EntityPlayer)ent).getEquipmentInSlot(2);
                            if (stack4 != null) {
                                RenderingUtil.rectangleBordered(endx + 1.0f, y + var1 * 2.0f, endx + 6.0f, y + var1 * 3.0f, 1.0, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                                final float diff3 = y + var1 * 3.0f - (y + var1 * 2.0f + 2.0f);
                                final double percent3 = 1.0 - stack4.getItemDamage() * 1.0 / stack4.getMaxDamage();
                                RenderingUtil.rectangle(endx + 2.0f, y + var1 * 3.0f, endx + 5.0f, y + var1 * 3.0f - diff3 * percent3, Colors.getColor(78, 206, 229));
                                ESP2D.mc.fontRendererObj.drawStringWithShadow(stack4.getMaxDamage() - stack4.getItemDamage() + "", endx + 7.0f, y + var1 * 3.0f - diff3 / 2.0f, -1);
                            }
                            final ItemStack stack5 = ((EntityPlayer)ent).getEquipmentInSlot(1);
                            if (stack5 != null) {
                                RenderingUtil.rectangleBordered(endx + 1.0f, y + var1 * 3.0f, endx + 6.0f, y + var1 * 4.0f, 1.0, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                                final float diff4 = y + var1 * 4.0f - (y + var1 * 3.0f + 2.0f);
                                final double percent4 = 1.0 - stack5.getItemDamage() * 1.0 / stack5.getMaxDamage();
                                RenderingUtil.rectangle(endx + 2.0f, y + var1 * 4.0f - 1.0f, endx + 5.0f, y + var1 * 4.0f - diff4 * percent4, Colors.getColor(78, 206, 229));
                                ESP2D.mc.fontRendererObj.drawStringWithShadow(stack5.getMaxDamage() - stack5.getItemDamage() + "", endx + 7.0f, y + var1 * 4.0f - diff4 / 2.0f, -1);
                            }
                            final float health = ((EntityPlayer)ent).getHealth();
                            final float[] fractions = { 0.0f, 0.5f, 1.0f };
                            final Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
                            final float progress = health * 5.0f * 0.01f;
                            final Color customColor = blendColors(fractions, colors, progress).brighter();
                            final double healthLocation = endy + (y - endy) * (health * 5.0f * 0.01f);
                            RenderingUtil.rectangle(x - 4.0f, endy - 1.0f, x - 2.0f, healthLocation, customColor.getRGB());
                            if ((int)MathUtils.getIncremental(health * 5.0f, 1.0) != 100 && ((HashMap<K, Setting<Boolean>>)this.settings).get(this.HEALTH).getValue()) {
                                GlStateManager.pushMatrix();
                                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                                final String nigger = (int)MathUtils.getIncremental(health * 5.0f, 1.0) + "HP";
                                Client.fss.drawStringWithShadow(nigger, (x - 5.0f - Client.fss.getWidth(nigger) * 2.0f) / 2.0f, ((int)healthLocation + Client.fss.getHeight(nigger) / 2.0f) / 2.0f, -1);
                                GlStateManager.popMatrix();
                            }
                        }
                        catch (Exception ex2) {}
                    }
                    GlStateManager.popMatrix();
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
            GL11.glScalef(1.0f, 1.0f, 1.0f);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
    
    public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
        Color color = null;
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fractions.length == colors.length) {
            final int[] indicies = getFractionIndicies(fractions, progress);
            final float[] range = { fractions[indicies[0]], fractions[indicies[1]] };
            final Color[] colorRange = { colors[indicies[0]], colors[indicies[1]] };
            final float max = range[1] - range[0];
            final float value = progress - range[0];
            final float weight = value / max;
            color = blend(colorRange[0], colorRange[1], 1.0f - weight);
            return color;
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }
    
    public static int[] getFractionIndicies(final float[] fractions, final float progress) {
        final int[] range = new int[2];
        int startPoint;
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {}
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }
    
    public static Color blend(final Color color1, final Color color2, final double ratio) {
        final float r = (float)ratio;
        final float ir = 1.0f - r;
        final float[] rgb1 = new float[3];
        final float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        }
        else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        }
        else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        }
        else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red, green, blue);
        }
        catch (IllegalArgumentException exp) {
            final NumberFormat nf = NumberFormat.getNumberInstance();
            System.out.println(nf.format(red) + "; " + nf.format(green) + "; " + nf.format(blue));
            exp.printStackTrace();
        }
        return color3;
    }
    
    private void updatePositions() {
        ESP2D.entityPositionstop.clear();
        ESP2D.entityPositionsbottom.clear();
        final float pTicks = ESP2D.mc.timer.renderPartialTicks;
        for (final Object o : ESP2D.mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityPlayer) {
                final EntityPlayer ent = (EntityPlayer)o;
                double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - ESP2D.mc.getRenderManager().viewerPosY;
                final double x = ent.lastTickPosX + (ent.posX + 10.0 - (ent.lastTickPosX + 10.0)) * pTicks - ESP2D.mc.getRenderManager().viewerPosX;
                final double z = ent.lastTickPosZ + (ent.posZ + 10.0 - (ent.lastTickPosZ + 10.0)) * pTicks - ESP2D.mc.getRenderManager().viewerPosZ;
                y += ent.height + 0.2;
                final double[] convertedPoints = this.convertTo2D(x, y, z);
                final double xd = Math.abs(this.convertTo2D(x, y + 1.0, z, ent)[1] - this.convertTo2D(x, y, z, ent)[1]);
                if (convertedPoints[2] < 0.0 || convertedPoints[2] >= 1.0) {
                    continue;
                }
                ESP2D.entityPositionstop.put(ent, new double[] { convertedPoints[0], convertedPoints[1], xd, convertedPoints[2] });
                y = ent.lastTickPosY + (ent.posY - 2.2 - (ent.lastTickPosY - 2.2)) * pTicks - ESP2D.mc.getRenderManager().viewerPosY;
                ESP2D.entityPositionsbottom.put(ent, new double[] { this.convertTo2D(x, y, z)[0], this.convertTo2D(x, y, z)[1], xd, this.convertTo2D(x, y, z)[2] });
            }
        }
    }
    
    public static float[] getRotationFromPosition(final double x, final double z, final double y) {
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        final double yDiff = y - Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight();
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    private double[] convertTo2D(final double x, final double y, final double z, final Entity ent) {
        final double[] convertedPoints = this.convertTo2D(x, y, z);
        return convertedPoints;
    }
    
    private void scale() {
        float scale = 1.0f;
        final float target = scale * (ESP2D.mc.gameSettings.fovSetting / ESP2D.mc.gameSettings.fovSetting);
        if (this.gradualFOVModifier == 0.0 || Double.isNaN(this.gradualFOVModifier)) {
            this.gradualFOVModifier = target;
        }
        this.gradualFOVModifier += (target - this.gradualFOVModifier) / (Minecraft.debugFPS * 0.7);
        scale *= (float)this.gradualFOVModifier;
        GlStateManager.scale(scale, scale, scale);
    }
    
    private double[] convertTo2D(final double x, final double y, final double z) {
        final FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        final IntBuffer viewport = BufferUtils.createIntBuffer(16);
        final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        final FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        final boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
        if (result) {
            return new double[] { screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2) };
        }
        return null;
    }
    
    static {
        ESP2D.TEAM = "TEAM";
        ESP2D.entityPositionstop = new HashMap<EntityLivingBase, double[]>();
        ESP2D.entityPositionsbottom = new HashMap<EntityLivingBase, double[]>();
    }
}
