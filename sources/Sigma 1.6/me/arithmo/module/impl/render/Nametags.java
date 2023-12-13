package me.arithmo.module.impl.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.arithmo.event.Event;
import me.arithmo.event.EventSystem;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventNametagRender;
import me.arithmo.event.impl.EventRender3D;
import me.arithmo.event.impl.EventRenderGui;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.RotationUtils;
import me.arithmo.util.render.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Timer;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Nametags
  extends Module
{
  private boolean hideInvisibles;
  private double gradualFOVModifier;
  private Character formatChar = new Character('§');
  public static Map<EntityLivingBase, double[]> entityPositions = new HashMap();
  public static String ARMOR = "ARMOR";
  public static String HEALTH = "HEALTH";
  public static String IMASPECIALCUNT = "SCALE";
  private final String INVISIBLES = "INVISIBLES";
  
  public Nametags(ModuleData data)
  {
    super(data);
    this.settings.put(ARMOR, new Setting(ARMOR, Boolean.valueOf(true), "Show armor when not hovering."));
    this.settings.put(HEALTH, new Setting(HEALTH, Boolean.valueOf(false), "Show health when not hovering."));
    this.settings.put(IMASPECIALCUNT, new Setting(IMASPECIALCUNT, Double.valueOf(12.5D), "Scale factor."));
    this.settings.put("INVISIBLES", new Setting("INVISIBLES", Boolean.valueOf(false), "Show invisibles."));
  }
  
  @RegisterEvent(events={EventRender3D.class, EventRenderGui.class, EventNametagRender.class})
  public void onEvent(Event event)
  {
    if ((event instanceof EventRender3D)) {
      try
      {
        updatePositions();
      }
      catch (Exception localException) {}
    }
    if ((event instanceof EventRenderGui))
    {
      EventRenderGui er = (EventRenderGui)event;
      GlStateManager.pushMatrix();
      ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
      double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0D);
      GlStateManager.scale(twoDscale, twoDscale, twoDscale);
      for (Entity ent : entityPositions.keySet()) {
        if (((ent != mc.thePlayer) && (((Boolean)((Setting)this.settings.get("INVISIBLES")).getValue()).booleanValue())) || (!ent.isInvisible()))
        {
          GlStateManager.pushMatrix();
          ItemStack stack;
          int x;
          if ((ent instanceof EntityPlayer))
          {
            String str = ent.getDisplayName().getFormattedText();
            
            str = str.replace(ent.getDisplayName().getFormattedText(), "§f" + ent.getDisplayName().getFormattedText());
            if (((EntityPlayer)ent).isMurderer) {
              str = str.replace(ent.getDisplayName().getFormattedText(), "§5[M] " + ent.getName());
            }
            double[] renderPositions = (double[])entityPositions.get(ent);
            if ((renderPositions[3] < 0.0D) || (renderPositions[3] >= 1.0D))
            {
              GlStateManager.popMatrix();
              continue;
            }
            GlStateManager.translate(renderPositions[0], renderPositions[1], 0.0D);
            scale(ent);
            String healthInfo = (int)((EntityLivingBase)ent).getHealth() + "?";
            GlStateManager.translate(0.0D, -2.5D, 0.0D);
            int strWidth = mc.fontRendererObj.getStringWidth(str);
            int strWidth2 = mc.fontRendererObj.getStringWidth(healthInfo);
            RenderingUtil.rectangleBordered(-strWidth / 2 - 3, -12.0D, strWidth / 2 + 3, 1.0D, 0.7D, 
              Colors.getColor(40, 40, 40, 190), Colors.getColor(0));
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            
            int x3 = (int)(renderPositions[0] + -strWidth / 2 - 3.0D) / 2 - 26;
            int x4 = (int)(renderPositions[0] + strWidth / 2 + 3.0D) / 2 + 20;
            int y1 = (int)(renderPositions[1] + -30.0D) / 2;
            int y2 = (int)(renderPositions[1] + 11.0D) / 2;
            int mouseY = er.getResolution().getScaledHeight() / 2;
            int mouseX = er.getResolution().getScaledWidth() / 2;
            
            mc.fontRendererObj.drawStringWithShadow(str, -strWidth / 2, -9.0F, 
              Colors.getColor(255, 50, 50));
            boolean healthOption = ((Boolean)((Setting)this.settings.get(HEALTH)).getValue()).booleanValue();
            boolean armor = ((Boolean)((Setting)this.settings.get(ARMOR)).getValue()).booleanValue();
            if (((healthOption) && (x3 < mouseX) && (mouseX < x4) && (y1 < mouseY) && (mouseY < y2)) || (!healthOption))
            {
              float health = ((EntityPlayer)ent).getHealth();
              float[] fractions = { 0.0F, 0.5F, 1.0F };
              Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
              float progress = health * 5.0F * 0.01F;
              Color customColor = ESP2D.blendColors(fractions, colors, progress).brighter();
              try
              {
                RenderingUtil.rectangleBordered(strWidth / 2 + 4, -12.0D, strWidth / 2 + 6 + strWidth2, 1.0D, 0.7D, 
                  Colors.getColor(40, 40, 40, 190), Colors.getColor(0));
                mc.fontRendererObj.drawStringWithShadow(healthInfo, strWidth / 2 + 6, -9.0F, customColor
                  .getRGB());
              }
              catch (Exception localException1) {}
            }
            if (((armor) && (x3 < mouseX) && (mouseX < x4) && (y1 < mouseY) && (mouseY < y2)) || (!armor))
            {
              GlStateManager.color(1.0F, 1.0F, 1.0F);
              List<ItemStack> itemsToRender = new ArrayList();
              for (int i = 0; i < 5; i++)
              {
                stack = ((EntityPlayer)ent).getEquipmentInSlot(i);
                if (stack != null) {
                  itemsToRender.add(stack);
                }
              }
              x = -(itemsToRender.size() * 9);
              for (ItemStack stack1 : itemsToRender)
              {
                RenderHelper.enableGUIStandardItemLighting();
                RenderingUtil.rectangleBordered(x - 1, -32.0D, x + 17, -13.0D, 0.7D, 
                  Colors.getColor(255, 255, 255, 60), Colors.getColor(40, 40, 40, 190));
                mc.getRenderItem().remderItemIntoGUI(stack1, x, -30);
                mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack1, x, -30);
                x += 3;
                RenderHelper.disableStandardItemLighting();
                
                String text = "";
                if (stack1 != null)
                {
                  int y = 21;
                  int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack1);
                  
                  int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack1);
                  
                  int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack1);
                  if (sLevel > 0)
                  {
                    drawEnchantTag("Sh" + getColor(sLevel) + sLevel, x, y);
                    y -= 9;
                  }
                  if (fLevel > 0)
                  {
                    drawEnchantTag("Fir" + getColor(fLevel) + fLevel, x, y);
                    y -= 9;
                  }
                  if (kLevel > 0)
                  {
                    drawEnchantTag("Kb" + getColor(kLevel) + kLevel, x, y);
                  }
                  else if ((stack1.getItem() instanceof ItemArmor))
                  {
                    int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack1);
                    int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack1);
                    
                    int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack1);
                    if (pLevel > 0)
                    {
                      drawEnchantTag("P" + getColor(pLevel) + pLevel, x, y);
                      y -= 9;
                    }
                    if (tLevel > 0)
                    {
                      drawEnchantTag("Th" + getColor(tLevel) + tLevel, x, y);
                      y -= 9;
                    }
                    if (uLevel > 0) {
                      drawEnchantTag("Unb" + getColor(uLevel) + uLevel, x, y);
                    }
                  }
                  else if ((stack1.getItem() instanceof ItemBow))
                  {
                    int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack1);
                    
                    int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack1);
                    
                    int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack1);
                    if (powLevel > 0)
                    {
                      drawEnchantTag("Pow" + getColor(powLevel) + powLevel, x, y);
                      y -= 9;
                    }
                    if (punLevel > 0)
                    {
                      drawEnchantTag("Pun" + getColor(punLevel) + punLevel, x, y);
                      y -= 9;
                    }
                    if (fireLevel > 0) {
                      drawEnchantTag("Fir" + getColor(fireLevel) + fireLevel, x, y);
                    }
                  }
                  else if (stack1.getRarity() == EnumRarity.EPIC)
                  {
                    drawEnchantTag("§6§lGod", x, y);
                  }
                  int var7 = (int)Math.round(255.0D - stack1
                    .getItemDamage() * 255.0D / stack1.getMaxDamage());
                  int var10 = 255 - var7 << 16 | var7 << 8;
                  Color customColor = new Color(var10).brighter();
                  
                  int x2 = (int)(x * 1.75D);
                  if (stack1.getMaxDamage() - stack1.getItemDamage() > 0)
                  {
                    GlStateManager.pushMatrix();
                    GlStateManager.disableDepth();
                    GL11.glScalef(0.57F, 0.57F, 0.57F);
                    mc.fontRendererObj.drawStringWithShadow("" + (stack1.getMaxDamage() - stack1.getItemDamage()), x2, -69.0F, customColor.getRGB());
                    GlStateManager.enableDepth();
                    GlStateManager.popMatrix();
                  }
                  y = -53;
                  for (Object o : ((EntityPlayer)ent).getActivePotionEffects())
                  {
                    PotionEffect pot = (PotionEffect)o;
                    String potName = StringUtils.capitalize(pot.getEffectName().substring(pot.getEffectName().lastIndexOf(".") + 1));
                    int XD = pot.getDuration() / 20;
                    SimpleDateFormat df = new SimpleDateFormat("m:ss");
                    String time = df.format(Integer.valueOf(XD * 1000));
                    mc.fontRendererObj.drawStringWithShadow(XD > 0 ? potName + " " + time : "", -30.0F, y, -1);
                    y -= 8;
                  }
                  x += 16;
                }
              }
            }
          }
          GlStateManager.popMatrix();
        }
      }
      GlStateManager.popMatrix();
    }
    Event enr = EventSystem.getInstance(EventNametagRender.class);
    enr.setCancelled(true);
  }
  
  private String getColor(int level)
  {
    if (level != 1)
    {
      if (level == 2) {
        return "§a";
      }
      if (level == 3) {
        return "§3";
      }
      if (level == 4) {
        return "§4";
      }
      if (level >= 5) {
        return "§6";
      }
    }
    return "§f";
  }
  
  private static void drawEnchantTag(String text, int x, int y)
  {
    GlStateManager.pushMatrix();
    GlStateManager.disableDepth();
    x = (int)(x * 1.75D);
    y -= 4;
    GL11.glScalef(0.57F, 0.57F, 0.57F);
    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, -36 - y, Colors.getColor(255));
    GlStateManager.enableDepth();
    GlStateManager.popMatrix();
  }
  
  private void scale(Entity ent)
  {
    float scale = ((Number)((Setting)this.settings.get(IMASPECIALCUNT)).getValue()).floatValue() / 10.0F;
    
    float target = scale * (mc.gameSettings.fovSetting / (mc.gameSettings.fovSetting * mc.thePlayer.func_175156_o()));
    if ((this.gradualFOVModifier == 0.0D) || (Double.isNaN(this.gradualFOVModifier))) {
      this.gradualFOVModifier = target;
    }
    this.gradualFOVModifier += (target - this.gradualFOVModifier) / (Minecraft.debugFPS * 0.7D);
    
    scale = (float)(scale * this.gradualFOVModifier);
    
    scale *= ((mc.currentScreen == null) && (GameSettings.isKeyDown(mc.gameSettings.ofKeyBindZoom)) ? 3 : 1);
    GlStateManager.scale(scale, scale, scale);
  }
  
  private void updatePositions()
  {
    entityPositions.clear();
    float pTicks = mc.timer.renderPartialTicks;
    for (Object o : mc.theWorld.loadedEntityList)
    {
      Entity ent = (Entity)o;
      if ((ent != mc.thePlayer) && ((ent instanceof EntityPlayer)) && (
        (!ent.isInvisible()) || (!this.hideInvisibles)))
      {
        double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - mc.getRenderManager().viewerPosX;
        double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - mc.getRenderManager().viewerPosY;
        double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - mc.getRenderManager().viewerPosZ;
        y += ent.height + 0.2D;
        if ((convertTo2D(x, y, z)[2] >= 0.0D) && (convertTo2D(x, y, z)[2] < 1.0D)) {
          entityPositions.put((EntityPlayer)ent, new double[] {
            convertTo2D(x, y, z)[0], convertTo2D(x, y, z)[1], 
            Math.abs(convertTo2D(x, y + 1.0D, z, ent)[1] - convertTo2D(x, y, z, ent)[1]), 
            convertTo2D(x, y, z)[2] });
        }
      }
    }
  }
  
  private double[] convertTo2D(double x, double y, double z, Entity ent)
  {
    float pTicks = mc.timer.renderPartialTicks;
    float prevYaw = mc.thePlayer.rotationYaw;
    float prevPrevYaw = mc.thePlayer.prevRotationYaw;
    float[] rotations = RotationUtils.getRotationFromPosition(ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks, ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks, ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - 1.6D);
    
    mc.getRenderViewEntity().rotationYaw = (mc.getRenderViewEntity().prevRotationYaw = rotations[0]);
    mc.entityRenderer.setupCameraTransform(pTicks, 0);
    double[] convertedPoints = convertTo2D(x, y, z);
    mc.getRenderViewEntity().rotationYaw = prevYaw;
    mc.getRenderViewEntity().prevRotationYaw = prevPrevYaw;
    mc.entityRenderer.setupCameraTransform(pTicks, 0);
    return convertedPoints;
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
