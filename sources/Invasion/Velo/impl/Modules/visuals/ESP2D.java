package Velo.impl.Modules.visuals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import com.ibm.icu.text.NumberFormat;

import Velo.api.Module.Module;
import Velo.api.Module.Module.Category;
import Velo.api.Util.Other.ColorManager;
import Velo.api.Util.Render.RenderUtil;
import Velo.api.Util.Render.RenderUtils;
import Velo.api.Util.fontRenderer.Fonts;
import Velo.api.Util.fontRenderer.Utils.Tff;
import Velo.impl.Event.EventRender;
import Velo.impl.Event.EventRender3D;
import Velo.impl.Event.EventRenderNameTag;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;



public class ESP2D extends Module
{
	
	
	public static boolean isEnabled = false;
	   public final List collectedEntities;

  public ESP2D() {
		super("ESP", "ESP", 0, Category.VISUALS);
		this.loadSettings(mode, Entitys, Players, red , blue, green);
		 this.collectedEntities = new ArrayList();
	      this.viewport = GLAllocation.createDirectIntBuffer(16);
	      this.modelview = GLAllocation.createDirectFloatBuffer(16);
	      this.projection = GLAllocation.createDirectFloatBuffer(16);
	      this.vector = GLAllocation.createDirectFloatBuffer(4);
	}


private double gradualFOVModifier;



@Override
public void onRenderNametag(EventRenderNameTag event) {
	event.setCancelled(true);
	super.onRenderNametag(event);
}

@Override
public void onRenderUpdate(EventRender event) {
	if(mode.equalsIgnorecase("2D")) {
	  GL11.glPushMatrix();
      this.collectEntities();
      float partialTicks = mc.timer.renderPartialTicks;
      ScaledResolution scaledResolution = event.getScaledResolution();
      int scaleFactor = scaledResolution.getScaleFactor();
      double scaling = (double)scaleFactor / Math.pow((double)scaleFactor, 2.0D);
      GL11.glScaled(scaling, scaling, scaling);
      int black = (new Color(0, 0, 0, 120)).getRGB();
      int background = (new Color(0, 0, 0, 120)).getRGB();;
      float scale = 0.65F;
      float upscale = 1.0F / scale;
      RenderManager renderMng = this.mc.getRenderManager();
      EntityRenderer entityRenderer = this.mc.entityRenderer;
      boolean tag = this.NAME.enabled;
      boolean outline = true;
      boolean health = this.HEALTH.enabled;
      boolean armor = this.ARMOR.enabled;
      List collectedEntities = this.collectedEntities;
      int i = 0;
  

      for(int collectedEntitiesSize = collectedEntities.size(); i < collectedEntitiesSize; ++i) {
         Entity entity = (Entity)collectedEntities.get(i);
         if (this.isValid(entity) && RenderUtil.isInViewFrustrum(entity)) {
            int healthColor = RenderUtil.drawHealth((EntityLivingBase)entity);
            int color1 = (new Color(healthColor)).getRed();
            int color2 = (new Color(healthColor)).getGreen();
            int color3 = (new Color(healthColor)).getBlue();
            int color = (new Color(color1, color2, color3, 135)).getRGB();
            double x = RenderUtil.interpolateScale(entity.posX, entity.lastTickPosX, (double)partialTicks);
            double y = RenderUtil.interpolateScale(entity.posY, entity.lastTickPosY, (double)partialTicks);
            double z = RenderUtil.interpolateScale(entity.posZ, entity.lastTickPosZ, (double)partialTicks);
            double width = (double)entity.width / 1.5D;
            double height = (double)entity.height + (entity.isSneaking() ? -0.3D : 0.2D);
            AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
            List vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
            entityRenderer.setupCameraTransform(partialTicks, 0);
            Vector4d position = null;
            Iterator var38 = vectors.iterator();
            Tff fr = Fonts.nametags;

            while(var38.hasNext()) {
               Vector3d vector = (Vector3d)var38.next();
               vector = this.project2D(scaleFactor, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
               if (vector != null && vector.z >= 0.0D && vector.z < 1.0D) {
                  if (position == null) {
                     position = new Vector4d(vector.x, vector.y, vector.z, 0.0D);
                  }

                  position.x = Math.min(vector.x, position.x);
                  position.y = Math.min(vector.y, position.y);
                  position.z = Math.max(vector.x, position.z);
                  position.w = Math.max(vector.y, position.w);
               }
            }

            if (position != null) {
               entityRenderer.setupOverlayRendering();
               double posX = position.x;
               double posY = position.y;
               double endPosX = position.z;
               double endPosY = position.w;
               
             
             
                     Gui.drawRect(posX - 1.0D, posY, posX + 0.5D, endPosY + 0.5D, black);
                     Gui.drawRect(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 0.5D + 0.5D, black);
                     Gui.drawRect(endPosX - 0.5D - 0.5D, posY, endPosX + 0.5D, endPosY + 0.5D, black);
                     Gui.drawRect(posX - 1.0D, endPosY - 0.5D - 0.5D, endPosX + 0.5D, endPosY + 0.5D, black);
                     Gui.drawRect(posX - 0.5D, posY, posX + 0.5D - 0.5D, endPosY, color);
                     Gui.drawRect(posX, endPosY - 0.5D, endPosX, endPosY, color);
                     Gui.drawRect(posX - 0.5D, posY, endPosX, posY + 0.5D, color);
                     Gui.drawRect(endPosX - 0.5D, posY, endPosX, endPosY, color);
               
                     Gui.drawRect(posX + 0.5D, posY, posX - 1.0D, posY + (endPosY - posY) / 4.0D + 0.5D, black);
                     Gui.drawRect(posX - 1.0D, endPosY, posX + 0.5D, endPosY - (endPosY - posY) / 4.0D - 0.5D, black);
                     Gui.drawRect(posX - 1.0D, posY - 0.5D, posX + (endPosX - posX) / 3.0D + 0.5D, posY + 1.0D, black);
                     Gui.drawRect(endPosX - (endPosX - posX) / 3.0D - 0.5D, posY - 0.5D, endPosX, posY + 1.0D, black);
                     Gui.drawRect(endPosX - 1.0D, posY, endPosX + 0.5D, posY + (endPosY - posY) / 4.0D + 0.5D, black);
                     Gui.drawRect(endPosX - 1.0D, endPosY, endPosX + 0.5D, endPosY - (endPosY - posY) / 4.0D - 0.5D, black);
                     Gui.drawRect(posX - 1.0D, endPosY - 1.0D, posX + (endPosX - posX) / 3.0D + 0.5D, endPosY + 0.5D, black);
                     Gui.drawRect(endPosX - (endPosX - posX) / 3.0D - 0.5D, endPosY - 1.0D, endPosX + 0.5D, endPosY + 0.5D, black);
                     Gui.drawRect(posX, posY, posX - 0.5D, posY + (endPosY - posY) / 4.0D, color);
                     Gui.drawRect(posX, endPosY, posX - 0.5D, endPosY - (endPosY - posY) / 4.0D, color);
                     Gui.drawRect(posX - 0.5D, posY, posX + (endPosX - posX) / 3.0D, posY + 0.5D, color);
                     Gui.drawRect(endPosX - (endPosX - posX) / 3.0D, posY, endPosX, posY + 0.5D, color);
                     Gui.drawRect(endPosX - 0.5D, posY, endPosX, posY + (endPosY - posY) / 4.0D, color);
                     Gui.drawRect(endPosX - 0.5D, endPosY, endPosX, endPosY - (endPosY - posY) / 4.0D, color);
                     Gui.drawRect(posX, endPosY - 0.5D, posX + (endPosX - posX) / 3.0D, endPosY, color);
                     Gui.drawRect(endPosX - (endPosX - posX) / 3.0D, endPosY - 0.5D, endPosX - 0.5D, endPosY, color);
            

               boolean living = entity instanceof EntityLivingBase;
               EntityLivingBase entityLivingBase;
               float armorValue;
               double durabilityWidth;
               double textWidth;
               float tagY;
               if (living) {
                  entityLivingBase = (EntityLivingBase)entity;
                  if (health) {
                     armorValue = entityLivingBase.getHealth();
                     float itemDurability = entityLivingBase.getMaxHealth();
                     if (armorValue > itemDurability) {
                        armorValue = itemDurability;
                     }

                     durabilityWidth = (double)(armorValue / itemDurability);
                     textWidth = (endPosY - posY) * durabilityWidth;
                     Gui.drawRect(posX - 3.5D, posY - 0.5D, posX - 1.5D, endPosY + 0.5D, background);
                     if (armorValue > 0.0F) {
                        Gui.drawRect((double)((float)posX - 3.0F), (double)((float)endPosY), (double)((float)posX - 2.0F), (double)((float)endPosY - (float)textWidth), healthColor);
                        tagY = entityLivingBase.getAbsorptionAmount();
                        if (tagY > 0.0F) {
                           Gui.drawRect(posX - 5.0D, endPosY, posX - 4.0D, endPosY - (endPosY - posY) / 6.0D * (double)tagY / 2.0D, (new Color(Potion.absorption.getLiquidColor())).getRGB());
                        }
                     }
                  }
               }

               if (tag && entity instanceof EntityPlayer) {
                  float scaledHeight = 10.0F;
                  EntityPlayer ent = (EntityPlayer)entity;
                  String name = ent.getGameProfile().getName();
                  durabilityWidth = (endPosX - posX) / 2.0D;
                  textWidth = (double)(fr.getStringWidth(name) * scale);
                  float tagX = (float)((posX + durabilityWidth - textWidth / 2.0D) * (double)upscale);
                  tagY = (float)(posY * (double)upscale) - scaledHeight;
                  GL11.glPushMatrix();
                  GL11.glScalef(scale, scale, scale);
                  int bg = (new Color(1, 1, 1)).getRGB();
                  fr.drawStringWithShadow(name, tagX + 1.0F, tagY - 5.0F, bg);
                  fr.drawStringWithShadow(name, tagX - 1.0F, tagY - 5.0F, bg);
                  fr.drawStringWithShadow(name, tagX, tagY - 5.0F - 1.0F, bg);
                  fr.drawStringWithShadow(name, tagX, tagY - 5.0F + 1.0F, bg);
                  fr.drawStringWithShadow(name, tagX, tagY - 5.0F, -1);
                  EntityLivingBase entityLiving = (EntityLivingBase)entity;
                  GL11.glPopMatrix();
               }

               if (armor && living) {
                  entityLivingBase = (EntityLivingBase)entity;
                  armorValue = (float)entityLivingBase.getTotalArmorValue();
                  double armorWidth = (endPosX - posX) * (double)armorValue / 20.0D;
                  Gui.drawRect(posX - 0.5D, endPosY + 1.0D, posX - 0.5D + endPosX - posX + 1.0D, endPosY + 3.5D, background);
                  RenderUtil.drawBorderedRect1(posX - 0.5D, endPosY + 4.3D, endPosX - posX + 1.0D, 1.5D, 0.5D, (new Color(28, 28, 28, 255)).getRGB(), -1);
                  if (armorValue > 0.0F) {
                     Gui.drawRect(posX, endPosY + 2.0D, posX + armorWidth, endPosY + 3.0D, 16777215);
                  }
               }
            }
         }
      }

      GL11.glPopMatrix();
      GlStateManager.enableBlend();
      entityRenderer.setupOverlayRendering();
	super.onRenderUpdate(event);
	}
}

public static double pspin = 0, pcumsize = 0, pamount = 0;
public boolean panimation = false;
  @Override
  public void onRender3DUpdate(EventRender3D event) {
	  
	  
	  if(mode.equalsIgnorecase("Penis")) {

	        for (final Object o : mc.theWorld.loadedEntityList) {
	            if (o instanceof EntityPlayer) {
	                final EntityPlayer player = (EntityPlayer)o;
	                final double n = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.timer.renderPartialTicks;
	                mc.getRenderManager();
	                final double x = n - RenderManager.renderPosX;
	                final double n2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.timer.renderPartialTicks;
	                mc.getRenderManager();
	                final double y = n2 - RenderManager.renderPosY;
	                final double n3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.timer.renderPartialTicks;
	                mc.getRenderManager();
	                final double z = n3 - RenderManager.renderPosZ;
	                GL11.glPushMatrix();
	                RenderHelper.disableStandardItemLighting();
	                this.esp(player, x, y, z);
	                RenderHelper.enableStandardItemLighting();
	                GL11.glPopMatrix();
	            }
	            if (panimation) {
	                ++pamount;
	                if (pamount > 25) {
	                    ++pspin;
	                    if (pspin > 50.0f) {
	                    	pspin = -50.0f;
	                    }
	                    else if (pspin < -50.0f) {
	                    	pspin = 50.0f;
	                    }
	                    pamount = 0;
	                }
	                ++pcumsize;
	                if (pcumsize > 180.0f) {
	                	pcumsize = -180.0f;
	                }
	                else {
	                    if (pcumsize >= -180.0f) {
	                        continue;
	                    }
	                    pcumsize = 180.0f;
	                }
	            }
	            else {
	            	pcumsize = 0.0f;
	                pamount = 0;
	                pspin = 0.0f;
	            }
	        }
	  }
	  

	  
	 this.setDisplayName("ESP"); 
	  if(!this.isEnabled())
		  return;
	  
	  Color color = null;

	    if (theme.equalsIgnorecase("Custom Color")) {
	        color = new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue());
	    }

	    if (theme.equalsIgnorecase("Chill Rainbow")) {
	        color = ColorManager.rainbow(100, (int) rainbowSpeed.getValue(), 0.5f, 1, 0.8f);
	    }

	    if (theme.equalsIgnorecase("Rainbow")) {
	        color = ColorManager.rainbow(100, (int) rainbowSpeed.getValue());
	    }
	  if(mode.equalsIgnorecase("Box")) {
  	    for (Object theObject : mc.theWorld.loadedEntityList) {
  	    //	EntityLivingBase entity;
  	    	 Entity entity = (Entity) theObject;
  	    	double xPos = (entity.lastTickPosX +(entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX);
  	    	double yPos = (entity.lastTickPosY +(entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY);
  	    	double zPos = (entity.lastTickPosZ +(entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ);

              if (entity instanceof EntityHanging) {
                  continue;
              }
              if (entity == mc.thePlayer) {
                  continue;
              }
              if (((Entity) entity).isInvisible()) {
                  continue;
              }
              if (!Players.isEnabled() && entity instanceof EntityPlayer) {
                  continue;
              }
              if (!ITEMS.isEnabled() && entity instanceof EntityItem || entity instanceof EntityXPOrb) {
                  continue;
              }
              
              

           entityESPBox(entity, 0, color);

          }
	  }
	
	}
  
  
  @Override
	public void onEnable() {
	  isEnabled = true;
		super.onEnable();
	}
  
  @Override
public void onDisable() {
	  isEnabled = false;
	super.onDisable();
}
  public static Minecraft mc = Minecraft.getMinecraft();

  public static void entityESPBox(Entity entity, int mode, Color color) {
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glLineWidth(2);
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      GL11.glDisable(GL11.GL_DEPTH_TEST);
      GL11.glDepthMask(false);


      double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) mc.timer.renderPartialTicks;
      double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) mc.timer.renderPartialTicks;
      double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) mc.timer.renderPartialTicks;
      float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * mc.timer.renderPartialTicks;
      GlStateManager.translate(x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ);
      GlStateManager.rotate(-yaw, 0, 1, 0);
      GlStateManager.translate(-(x - mc.getRenderManager().renderPosX), -(y - mc.getRenderManager().renderPosY), -(z - mc.getRenderManager().renderPosZ));
      GL11.glEnable(GL11.GL_LINE_SMOOTH);


      GlStateManager.color(Objects.requireNonNull(color).getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
      RenderGlobal.func_181561_a(
              new AxisAlignedBB(
                      x - entity.width / 2 - 0.05 - x + (x - mc.getRenderManager().renderPosX),
                      y - y + (y - mc.getRenderManager().renderPosY),
                      z - entity.width / 2 - 0.05 - z + (z - mc.getRenderManager().renderPosZ),
                      x + entity.width / 2 + 0.05 - x + (x - mc.getRenderManager().renderPosX),
                      y + entity.height + 0.1 - y + (y - mc.getRenderManager().renderPosY),
                      z + entity.width / 2 + 0.05 - z + (z - mc.getRenderManager().renderPosZ)
              ));
      GlStateManager.translate(x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ);
      GlStateManager.rotate(yaw, 0, 1, 0);
      GlStateManager.translate(-(x - mc.getRenderManager().renderPosX), -(y - mc.getRenderManager().renderPosY), -(z - mc.getRenderManager().renderPosZ));
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      GL11.glDepthMask(true);
      GL11.glDisable(GL11.GL_BLEND);
  }
  
  public BooleanSetting TEAM = new BooleanSetting("Team", true);
  public BooleanSetting INVISIBLES = new BooleanSetting("Invisibles", true);
  public BooleanSetting CUSTOMTAG = new BooleanSetting("CustomTag", true);
  public BooleanSetting ITEMS = new BooleanSetting("Items", true);
  public BooleanSetting HEALTH = new BooleanSetting("Health", true);
  public BooleanSetting ARMOR = new BooleanSetting("Armor", true);
  public BooleanSetting MOBS = new BooleanSetting("Mobs", true);
  public BooleanSetting NAME = new BooleanSetting("Name", true);
  public static BooleanSetting Players = new BooleanSetting("Players", true);
  public static BooleanSetting Entitys = new BooleanSetting("Entitys", true);
  public static ModeSetting mode = new ModeSetting("Mode", "Outline", "Outline", "Box", "Glow", "Shader", "2D", "Penis"),  theme = new ModeSetting("Color", "Custom Color", "Custom Color", "Chill Rainbow", "Rainbow");




  public static NumberSetting rainbowSpeed = new NumberSetting("Rainbow Speed", 30, 1, 100, 1);
public static NumberSetting red = new NumberSetting("Red", 255, 1, 255, 1);
public static NumberSetting green = new NumberSetting("Green", 255, 1, 255, 1);
public static NumberSetting blue = new NumberSetting("Blue", 255, 1, 255, 1);

  
  public static Map<EntityLivingBase, double[]> entityPositionstop = new HashMap();
  public static Map<EntityLivingBase, double[]> entityPositionsbottom = new HashMap();

  

  
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
  
  private boolean isFriendly(EntityPlayer player) {
      return this.mc.thePlayer.isOnSameTeam(player);
   }

   private void collectEntities() {
      this.collectedEntities.clear();
      List playerEntities = this.mc.theWorld.loadedEntityList;
      int i = 0;

      for(int playerEntitiesSize = playerEntities.size(); i < playerEntitiesSize; ++i) {
         Entity entity = (Entity)playerEntities.get(i);
         if (this.isValid(entity)) {
            this.collectedEntities.add(entity);
         }
      }

   }
   private final IntBuffer viewport;
   private final FloatBuffer modelview;
   private final FloatBuffer projection;
   private final FloatBuffer vector;

   private Vector3d project2D(int scaleFactor, double x, double y, double z) {
      GL11.glGetFloat(2982, this.modelview);
      GL11.glGetFloat(2983, this.projection);
      GL11.glGetInteger(2978, this.viewport);
      return GLU.gluProject((float)x, (float)y, (float)z, this.modelview, this.projection, this.viewport, this.vector) ? new Vector3d((double)(this.vector.get(0) / (float)scaleFactor), (double)(((float)Display.getHeight() - this.vector.get(1)) / (float)scaleFactor), (double)this.vector.get(2)) : null;
   }
   

   public void esp(final EntityPlayer player, final double x, final double y, final double z) {
       GL11.glDisable(2896);
       GL11.glDisable(3553);
       GL11.glEnable(3042);
       GL11.glBlendFunc(770, 771);
       GL11.glDisable(2929);
       GL11.glEnable(2848);
       GL11.glDepthMask(true);
       GL11.glLineWidth(1.0f);
       GL11.glTranslated(x, y, z);
       GL11.glRotatef(-player.rotationYaw, 0.0f, player.height, 0.0f);
       GL11.glTranslated(-x, -y, -z);
       GL11.glTranslated(x, y + player.height / 2.0f - 0.22499999403953552, z);
       GL11.glColor4f(1.38f, 0.55f, 2.38f, 1.0f);
    //   GL11.glRotated((player.isSneaking() ? 35 : 0) + pspin, 1.0f + pspin, 0.0f, pcumsize);
       GL11.glTranslated(0.0, 0.0, 0.07500000298023224);
       final Cylinder shaft = new Cylinder();
       shaft.setDrawStyle(100013);
       shaft.draw(0.1f, 0.11f, 0.4f, 901, 901);
       GL11.glColor4f(1.38f, 0.85f, 1.38f, 1.0f);
       GL11.glTranslated(0.0, 0.0, -0.12500000298023223);
       GL11.glTranslated(-0.09000000074505805, 0.0, 0.0);
       final Sphere right = new Sphere();
       right.setDrawStyle(100013);
       right.draw(0.14f, 10, 901);
       GL11.glTranslated(0.16000000149011612, 0.0, 0.0);
       final Sphere left = new Sphere();
       left.setDrawStyle(100013);
       left.draw(0.14f, 10, 901);
       GL11.glColor4f(1.35f, 0.0f, 0.0f, 1.0f);
       GL11.glTranslated(-0.07000000074505806, 0.0, 0.589999952316284);
       final Sphere tip = new Sphere();
  
       tip.setDrawStyle(100013);
       tip.draw(0.13f, 15, 901);
       GL11.glDepthMask(true);
       GL11.glDisable(2848);
       GL11.glEnable(2929);
       GL11.glDisable(3042);
       GL11.glEnable(2896);
       GL11.glEnable(3553);
   }

   private boolean isValid(Entity entity) {
      if (entity == this.mc.thePlayer && (this.mc.gameSettings.thirdPersonView == 0)) {
         return false;
      } else if (entity.isDead) {
         return false;
      } else if (!(Boolean)this.Entitys.isEnabled() && entity.isInvisible()) {
         return false;
      } else if ((Boolean)this.Entitys.isEnabled() && entity instanceof EntityAnimal) {
         return true;
      } else if ((Boolean)this.Players.isEnabled() && entity instanceof EntityPlayer) {
         return true;
      } else {
         return (Boolean)this.Entitys.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityDragon || entity instanceof EntityGolem);
      }
   }
  

  
}
