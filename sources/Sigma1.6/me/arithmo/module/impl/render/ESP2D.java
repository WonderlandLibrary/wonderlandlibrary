package me.arithmo.module.impl.render;

import java.awt.Color;
import java.io.PrintStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import me.arithmo.Client;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventNametagRender;
import me.arithmo.event.impl.EventRender3D;
import me.arithmo.event.impl.EventRenderEntity;
import me.arithmo.event.impl.EventRenderGui;
import me.arithmo.management.FontManager;
import me.arithmo.management.friend.FriendManager;
import me.arithmo.module.Module;
import me.arithmo.module.ModuleManager;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.MathUtils;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.render.Colors;
import me.arithmo.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class ESP2D
  extends Module
{
  public static String TEAM = "TEAM";
  private final String INVISIBLES = "INVISIBLES";
  private String CUSTOMTAG = "ITEMTAG";
  private String ITEMS = "ITEMS";
  private String HEALTH = "HEALTH";
  private String ARMOR = "ARMOR";
  private String MOBS = "MOBS";
  private String NAME = "NAMES";
  private double gradualFOVModifier;
  
  public ESP2D(ModuleData data)
  {
    super(data);
    this.settings.put(this.NAME, new Setting(this.NAME, Boolean.valueOf(false), "Renders player name."));
    this.settings.put(TEAM, new Setting(TEAM, Boolean.valueOf(false), "Team colors."));
    this.settings.put("INVISIBLES", new Setting("INVISIBLES", Boolean.valueOf(false), "Show invisibles."));
    this.settings.put(this.HEALTH, new Setting(this.HEALTH, Boolean.valueOf(true), "Renders in small text entity HP."));
    this.settings.put(this.ITEMS, new Setting(this.ITEMS, Boolean.valueOf(true), "Shows player's current item."));
    this.settings.put(this.CUSTOMTAG, new Setting(this.CUSTOMTAG, Boolean.valueOf(false), "Shows the custom name the item has."));
    this.settings.put(this.ARMOR, new Setting(this.ARMOR, Boolean.valueOf(false), "Shows a Aimware like armor bar(s) on the left."));
    this.settings.put(this.MOBS, new Setting(this.MOBS, Boolean.valueOf(false), "Shows ESP on mobs."));
  }
  
  public static Map<EntityLivingBase, double[]> entityPositionstop = new HashMap();
  public static Map<EntityLivingBase, double[]> entityPositionsbottom = new HashMap();
  
  @RegisterEvent(events={EventRender3D.class, EventRenderEntity.class, EventRenderGui.class, EventNametagRender.class})
  public void onEvent(Event event)
  {
    if ((event instanceof EventNametagRender)) {
      event.setCancelled(true);
    }
    if ((event instanceof EventRender3D)) {
      try
      {
        updatePositions();
      }
      catch (Exception localException) {}
    }
    if ((event instanceof EventRenderGui) || (event instanceof EventRenderEntity))
    {
      EventRenderGui er = (EventRenderGui)event;
      GlStateManager.pushMatrix();
      ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
      double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0D);
      GlStateManager.scale(twoDscale, twoDscale, twoDscale);
      for (Entity ent : entityPositionstop.keySet())
      {
        double[] renderPositions = (double[])entityPositionstop.get(ent);
        double[] renderPositionsBottom = (double[])entityPositionsbottom.get(ent);
        if ((renderPositions[3] > 0.0D) || (renderPositions[3] <= 1.0D))
        {
          GlStateManager.pushMatrix();
          if ((((Boolean)((Setting)this.settings.get("INVISIBLES")).getValue()).booleanValue()) || ((!ent.isInvisible()) && ((ent instanceof EntityPlayer)) && (!(ent instanceof EntityPlayerSP))))
          {
            scale(ent);
            try
            {
              float y = (float)renderPositions[1];
              float endy = (float)renderPositionsBottom[1];
              float meme = endy - y;
              float x = (float)renderPositions[0] - meme / 4.0F;
              float endx = (float)renderPositionsBottom[0] + meme / 4.0F;
              if (x > endx)
              {
                endx = x;
                x = (float)renderPositionsBottom[0] + meme / 4.0F;
              }
              GlStateManager.pushMatrix();
              GlStateManager.scale(2.0F, 2.0F, 2.0F);
              GlStateManager.popMatrix();
              GL11.glEnable(3042);
              GL11.glDisable(3553);
              int color = Colors.getColor(255, 0, 0);
              if (FriendManager.isFriend(ent.getName())) {
                color = Colors.getColor(0, 255, 0);
              } else if (!mc.thePlayer.canEntityBeSeen(ent)) {
                color = Colors.getColor(255, 255, 0);
              }
              if (((Boolean)((Setting)this.settings.get(TEAM)).getValue()).booleanValue())
              {
                String text = ent.getDisplayName().getFormattedText();
                for (int i = 0; i < text.length(); i++) {
                  if ((text.charAt(i) == '§') && (i + 1 < text.length()))
                  {
                    char oneMore = Character.toLowerCase(text.charAt(i + 1));
                    int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
                    if (colorCode < 16) {
                      try
                      {
                        Color axd = new Color(mc.fontRendererObj.colorCode[colorCode]);
                        color = Colors.getColor(axd.getRed(), axd.getGreen(), axd.getBlue(), 255);
                      }
                      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {}
                    }
                  }
                }
              }
              if (((EntityPlayer)ent).isMurderer) {
                color = Colors.getColor(189, 44, 221);
              }
              RenderingUtil.rectangleBordered(x, y, endx, endy, 2.25D, Colors.getColor(0, 0, 0, 0), color);
              RenderingUtil.rectangleBordered(x - 0.5D, y - 0.5D, endx + 0.5D, endy + 0.5D, 0.9D, Colors.getColor(0, 0), Colors.getColor(0));
              RenderingUtil.rectangleBordered(x + 2.5D, y + 2.5D, endx - 2.5D, endy - 2.5D, 0.9D, Colors.getColor(0, 0), Colors.getColor(0));
              RenderingUtil.rectangleBordered(x - 5.0F, y - 1.0F, x - 1.0F, endy, 1.0D, Colors.getColor(0, 100), Colors.getColor(0, 255));
              if ((!((Module)Client.getModuleManager().get(Nametags.class)).isEnabled()) && (((Boolean)((Setting)this.settings.get(this.NAME)).getValue()).booleanValue()))
              {
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                String renderName = FriendManager.isFriend(ent.getName()) ? FriendManager.getAlias(ent.getName()) : ent.getName();
                TTFFontRenderer font = Client.fm.getFont("Verdana Bold 16");
                float meme2 = (endx - x) / 2.0F - font.getWidth(renderName) / 1.0F;
                font.drawStringWithShadow(renderName + " " + (int)mc.thePlayer.getDistanceToEntity(ent) + "m", (x + meme2) / 2.0F, (y - font.getHeight(renderName) - 10.0F) / 2.0F, FriendManager.isFriend(ent.getName()) ? Colors.getColor(192, 80, 64) : -1);
                GlStateManager.popMatrix();
              }
              if ((((EntityPlayer)ent).getCurrentEquippedItem() != null) && (((Boolean)((Setting)this.settings.get(this.ITEMS)).getValue()).booleanValue()))
              {
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                ItemStack stack = ((EntityPlayer)ent).getCurrentEquippedItem();
                String customName = ((Boolean)((Setting)this.settings.get(this.CUSTOMTAG)).getValue()).booleanValue() ? ((EntityPlayer)ent).getCurrentEquippedItem().getDisplayName() : ((EntityPlayer)ent).getCurrentEquippedItem().getItem().getItemStackDisplayName(stack);
                TTFFontRenderer font = Client.fm.getFont("Verdana 12");
                float meme2 = (endx - x) / 2.0F - font.getWidth(customName) / 1.0F;
                font.drawStringWithShadow(customName, (x + meme2) / 2.0F, (endy + font.getHeight(customName) / 2.0F * 2.0F) / 2.0F, -1);
                GlStateManager.popMatrix();
              }
              if (((Boolean)((Setting)this.settings.get(this.ARMOR)).getValue()).booleanValue())
              {
                float var1 = (endy - y) / 4.0F;
                ItemStack stack = ((EntityPlayer)ent).getEquipmentInSlot(4);
                if (stack != null)
                {
                  RenderingUtil.rectangleBordered(endx + 1.0F, y + 1.0F, endx + 6.0F, y + var1, 1.0D, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                  float diff1 = y + var1 - 1.0F - (y + 2.0F);
                  double percent = 1.0D - stack.getItemDamage() / stack.getMaxDamage();
                  RenderingUtil.rectangle(endx + 2.0F, y + var1 - 1.0F, endx + 5.0F, y + var1 - 1.0F - diff1 * percent, Colors.getColor(78, 206, 229));
                  mc.fontRendererObj.drawStringWithShadow(stack.getMaxDamage() - stack.getItemDamage() + "", endx + 7.0F, y + var1 - 1.0F - diff1 / 2.0F - mc.fontRendererObj.FONT_HEIGHT / 2, -1);
                }
                ItemStack stack2 = ((EntityPlayer)ent).getEquipmentInSlot(3);
                if (stack2 != null)
                {
                  RenderingUtil.rectangleBordered(endx + 1.0F, y + var1, endx + 6.0F, y + var1 * 2.0F, 1.0D, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                  float diff1 = y + var1 * 2.0F - (y + var1 + 2.0F);
                  double percent = 1.0D - stack2.getItemDamage() * 1.0D / stack2.getMaxDamage();
                  RenderingUtil.rectangle(endx + 2.0F, y + var1 * 2.0F, endx + 5.0F, y + var1 * 2.0F - diff1 * percent, Colors.getColor(78, 206, 229));
                  mc.fontRendererObj.drawStringWithShadow(stack2.getMaxDamage() - stack2.getItemDamage() + "", endx + 7.0F, y + var1 * 2.0F - diff1 / 2.0F - mc.fontRendererObj.FONT_HEIGHT / 2, -1);
                }
                ItemStack stack3 = ((EntityPlayer)ent).getEquipmentInSlot(2);
                if (stack3 != null)
                {
                  RenderingUtil.rectangleBordered(endx + 1.0F, y + var1 * 2.0F, endx + 6.0F, y + var1 * 3.0F, 1.0D, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                  float diff1 = y + var1 * 3.0F - (y + var1 * 2.0F + 2.0F);
                  double percent = 1.0D - stack3.getItemDamage() * 1.0D / stack3.getMaxDamage();
                  RenderingUtil.rectangle(endx + 2.0F, y + var1 * 3.0F, endx + 5.0F, y + var1 * 3.0F - diff1 * percent, Colors.getColor(78, 206, 229));
                  mc.fontRendererObj.drawStringWithShadow(stack3.getMaxDamage() - stack3.getItemDamage() + "", endx + 7.0F, y + var1 * 3.0F - diff1 / 2.0F - mc.fontRendererObj.FONT_HEIGHT / 2, -1);
                }
                ItemStack stack4 = ((EntityPlayer)ent).getEquipmentInSlot(1);
                if (stack4 != null)
                {
                  RenderingUtil.rectangleBordered(endx + 1.0F, y + var1 * 3.0F, endx + 6.0F, y + var1 * 4.0F, 1.0D, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                  float diff1 = y + var1 * 4.0F - (y + var1 * 3.0F + 2.0F);
                  double percent = 1.0D - stack4.getItemDamage() * 1.0D / stack4.getMaxDamage();
                  RenderingUtil.rectangle(endx + 2.0F, y + var1 * 4.0F - 1.0F, endx + 5.0F, y + var1 * 4.0F - diff1 * percent, Colors.getColor(78, 206, 229));
                  mc.fontRendererObj.drawStringWithShadow(stack4.getMaxDamage() - stack4.getItemDamage() + "", endx + 7.0F, y + var1 * 4.0F - diff1 / 2.0F - mc.fontRendererObj.FONT_HEIGHT / 2, -1);
                }
              }
              float health = ((EntityPlayer)ent).getHealth();
              float[] fractions = { 0.0F, 0.5F, 1.0F };
              Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
              float progress = health * 5.0F * 0.01F;
              Color customColor = blendColors(fractions, colors, progress).brighter();
              double healthLocation = endy + (y - endy) * (health * 5.0F * 0.01F);
              RenderingUtil.rectangle(x - 4.0F, endy - 1.0F, x - 2.0F, healthLocation, customColor.getRGB());
              if (((int)MathUtils.getIncremental(health * 5.0F, 1.0D) != 100) && (((Boolean)((Setting)this.settings.get(this.HEALTH)).getValue()).booleanValue()))
              {
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                String nigger = (int)MathUtils.getIncremental(health * 5.0F, 1.0D) + "HP";
                TTFFontRenderer font = Client.fm.getFont("Verdana 12");
                font.drawStringWithShadow(nigger, (x - 5.0F - font.getWidth(nigger) * 2.0F) / 2.0F, ((int)healthLocation + font.getHeight(nigger) / 2.0F) / 2.0F, -1);
                GlStateManager.popMatrix();
              }
            }
            catch (Exception localException1) {}
          }
          GlStateManager.popMatrix();
          GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
      }
      GL11.glScalef(1.0F, 1.0F, 1.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.popMatrix();
    }
  }
  
  public static Color blendColors(float[] fractions, Color[] colors, float progress)
  {
    Color color = null;
    if (fractions != null)
    {
      if (colors != null)
      {
        if (fractions.length == colors.length)
        {
          int[] indicies = getFractionIndicies(fractions, progress);
          
          float[] range = { fractions[indicies[0]], fractions[indicies[1]] };
          Color[] colorRange = { colors[indicies[0]], colors[indicies[1]] };
          
          float max = range[1] - range[0];
          float value = progress - range[0];
          float weight = value / max;
          
          color = blend(colorRange[0], colorRange[1], 1.0F - weight);
        }
        else
        {
          throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        }
      }
      else {
        throw new IllegalArgumentException("Colours can't be null");
      }
    }
    else {
      throw new IllegalArgumentException("Fractions can't be null");
    }
    return color;
  }
  
  public static int[] getFractionIndicies(float[] fractions, float progress)
  {
    int[] range = new int[2];
    
    int startPoint = 0;
    while ((startPoint < fractions.length) && (fractions[startPoint] <= progress)) {
      startPoint++;
    }
    if (startPoint >= fractions.length) {
      startPoint = fractions.length - 1;
    }
    range[0] = (startPoint - 1);
    range[1] = startPoint;
    
    return range;
  }
  
  public static Color blend(Color color1, Color color2, double ratio)
  {
    float r = (float)ratio;
    float ir = 1.0F - r;
    
    float[] rgb1 = new float[3];
    float[] rgb2 = new float[3];
    
    color1.getColorComponents(rgb1);
    color2.getColorComponents(rgb2);
    
    float red = rgb1[0] * r + rgb2[0] * ir;
    float green = rgb1[1] * r + rgb2[1] * ir;
    float blue = rgb1[2] * r + rgb2[2] * ir;
    if (red < 0.0F) {
      red = 0.0F;
    } else if (red > 255.0F) {
      red = 255.0F;
    }
    if (green < 0.0F) {
      green = 0.0F;
    } else if (green > 255.0F) {
      green = 255.0F;
    }
    if (blue < 0.0F) {
      blue = 0.0F;
    } else if (blue > 255.0F) {
      blue = 255.0F;
    }
    Color color = null;
    try
    {
      color = new Color(red, green, blue);
    }
    catch (IllegalArgumentException exp)
    {
      NumberFormat nf = NumberFormat.getNumberInstance();
      System.out.println(nf.format(red) + "; " + nf.format(green) + "; " + nf.format(blue));
      exp.printStackTrace();
    }
    return color;
  }
  
  private void updatePositions()
  {
    entityPositionstop.clear();
    entityPositionsbottom.clear();
    float pTicks = mc.timer.renderPartialTicks;
    for (Object o : mc.theWorld.getLoadedEntityList()) {
      if ((o instanceof EntityPlayer))
      {
        EntityPlayer ent = (EntityPlayer)o;
        
        double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - mc.getRenderManager().viewerPosY;
        
        double x = ent.lastTickPosX + (ent.posX + 10.0D - (ent.lastTickPosX + 10.0D)) * pTicks - mc.getRenderManager().viewerPosX;
        double z = ent.lastTickPosZ + (ent.posZ + 10.0D - (ent.lastTickPosZ + 10.0D)) * pTicks - mc.getRenderManager().viewerPosZ;
        y += ent.height + 0.2D;
        double[] convertedPoints = convertTo2D(x, y, z);
        double xd = Math.abs(convertTo2D(x, y + 1.0D, z, ent)[1] - convertTo2D(x, y, z, ent)[1]);
        assert (convertedPoints != null);
        if ((convertedPoints[2] >= 0.0D) && (convertedPoints[2] < 1.0D))
        {
          entityPositionstop.put(ent, new double[] { convertedPoints[0], convertedPoints[1], xd, convertedPoints[2] });
          y = ent.lastTickPosY + (ent.posY - 2.2D - (ent.lastTickPosY - 2.2D)) * pTicks - mc.getRenderManager().viewerPosY;
          entityPositionsbottom.put(ent, new double[] { convertTo2D(x, y, z)[0], convertTo2D(x, y, z)[1], xd, convertTo2D(x, y, z)[2] });
        }
      }
    }
  }
  
  public static float[] getRotationFromPosition(double x, double z, double y)
  {
    double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
    double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
    double yDiff = y - Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight();
    
    double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
    float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
    float pitch = (float)-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
    return new float[] { yaw, pitch };
  }
  
  private double[] convertTo2D(double x, double y, double z, Entity ent)
  {
    return convertTo2D(x, y, z);
  }
  
  private void scale(Entity ent)
  {
    float scale = 1.0F;
    float target = scale * (mc.gameSettings.fovSetting / mc.gameSettings.fovSetting);
    if ((this.gradualFOVModifier == 0.0D) || (Double.isNaN(this.gradualFOVModifier))) {
      this.gradualFOVModifier = target;
    }
    this.gradualFOVModifier += (target - this.gradualFOVModifier) / (Minecraft.debugFPS * 0.7D);
    
    scale = (float)(scale * this.gradualFOVModifier);
    
    GlStateManager.scale(scale, scale, scale);
  }
  
  private double[] convertTo2D(double x, double y, double z)
  {
    FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
    IntBuffer viewport = BufferUtils.createIntBuffer(16);
    FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
    FloatBuffer projection = BufferUtils.createFloatBuffer(16);
    GL11.glGetFloat(2982, modelView);
    GL11.glGetFloat(2983, projection);
    GL11.glGetInteger(2978, viewport);
    boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
    if (result) {
      return new double[] { screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2) };
    }
    return null;
  }
}
