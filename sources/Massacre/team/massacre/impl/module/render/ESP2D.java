package team.massacre.impl.module.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import team.massacre.Massacre;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventRender2D;
import team.massacre.impl.event.EventRenderNametag;
import team.massacre.utils.GuiUtils;
import team.massacre.utils.TTFFontRenderer;

public final class ESP2D extends Module {
   public final Property<Boolean> outline = new Property("Outline", true);
   public final EnumProperty<ESP2D.BoxMode> boxMode;
   public final Property<Boolean> tag;
   public final Property<Boolean> healthBar;
   public final Property<Boolean> armorBar;
   public final Property<Boolean> localPlayer;
   public final Property<Boolean> players;
   public final Property<Boolean> invisibles;
   public final Property<Boolean> mobs;
   public final Property<Boolean> animals;
   public final List collectedEntities;
   private final IntBuffer viewport;
   private final FloatBuffer modelview;
   private final FloatBuffer projection;
   private final FloatBuffer vector;
   private int color;
   private final int backgroundColor;
   private final int black;

   public ESP2D() {
      super("ESP2D", 0, Category.RENDER);
      this.boxMode = new EnumProperty("Mode", ESP2D.BoxMode.Box);
      this.tag = new Property("Nametag", true);
      this.healthBar = new Property("Health Bar", true);
      this.armorBar = new Property("Armor Bar", true);
      this.localPlayer = new Property("You", true);
      this.players = new Property("Players", true);
      this.invisibles = new Property("Invisibles", false);
      this.mobs = new Property("Monsters", true);
      this.animals = new Property("Animals", false);
      this.collectedEntities = new ArrayList();
      this.viewport = GLAllocation.createDirectIntBuffer(16);
      this.modelview = GLAllocation.createDirectFloatBuffer(16);
      this.projection = GLAllocation.createDirectFloatBuffer(16);
      this.vector = GLAllocation.createDirectFloatBuffer(4);
      this.color = Color.WHITE.getRGB();
      this.backgroundColor = (new Color(0, 0, 0, 120)).getRGB();
      this.black = Color.BLACK.getRGB();
      this.addValues(new Property[]{this.boxMode, this.outline, this.tag, this.healthBar, this.armorBar, this.localPlayer, this.players, this.invisibles, this.mobs, this.animals});
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @Handler
   public void onRenderNametag(EventRenderNametag event) {
      event.setCancelled(true);
   }

   @Handler
   public void onRender2D(EventRender2D event) {
      GL11.glPushMatrix();
      this.collectEntities();
      float partialTicks = event.getPartialTicks();
      ScaledResolution scaledResolution = event.getScaledResolution();
      int scaleFactor = scaledResolution.getScaleFactor();
      double scaling = (double)scaleFactor / Math.pow((double)scaleFactor, 2.0D);
      GL11.glScaled(scaling, scaling, scaling);
      int black = (new Color(0, 0, 0, 120)).getRGB();
      int background = this.backgroundColor;
      float scale = 0.65F;
      float upscale = 1.0F / scale;
      RenderManager renderMng = this.mc.getRenderManager();
      EntityRenderer entityRenderer = this.mc.entityRenderer;
      boolean tag = (Boolean)this.tag.getValue();
      boolean outline = (Boolean)this.outline.getValue();
      boolean health = (Boolean)this.healthBar.getValue();
      boolean armor = (Boolean)this.armorBar.getValue();
      List collectedEntities = this.collectedEntities;
      int i = 0;
      int healthcolor = false;

      for(int collectedEntitiesSize = collectedEntities.size(); i < collectedEntitiesSize; ++i) {
         Entity entity = (Entity)collectedEntities.get(i);
         if (this.isValid(entity) && GuiUtils.isInViewFrustrum(entity)) {
            int healthColor = GuiUtils.drawHealth((EntityLivingBase)entity);
            int color1 = (new Color(healthColor)).getRed();
            int color2 = (new Color(healthColor)).getGreen();
            int color3 = (new Color(healthColor)).getBlue();
            int color = (new Color(color1, color2, color3, 135)).getRGB();
            double x = GuiUtils.interpolateScale(entity.posX, entity.lastTickPosX, (double)partialTicks);
            double y = GuiUtils.interpolateScale(entity.posY, entity.lastTickPosY, (double)partialTicks);
            double z = GuiUtils.interpolateScale(entity.posZ, entity.lastTickPosZ, (double)partialTicks);
            double width = (double)entity.width / 1.5D;
            double height = (double)entity.height + (entity.isSneaking() ? -0.3D : 0.2D);
            AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
            List vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
            entityRenderer.setupCameraTransform(partialTicks, 0);
            Vector4d position = null;
            Iterator var38 = vectors.iterator();
            TTFFontRenderer fr = Massacre.INSTANCE.getFontManager().getLatoRegularMedium();

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
               if (outline) {
                  if (((ESP2D.BoxMode)this.boxMode.getValue()).equals(ESP2D.BoxMode.Box)) {
                     Gui.drawRect(posX - 1.0D, posY, posX + 0.5D, endPosY + 0.5D, black);
                     Gui.drawRect(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 0.5D + 0.5D, black);
                     Gui.drawRect(endPosX - 0.5D - 0.5D, posY, endPosX + 0.5D, endPosY + 0.5D, black);
                     Gui.drawRect(posX - 1.0D, endPosY - 0.5D - 0.5D, endPosX + 0.5D, endPosY + 0.5D, black);
                     Gui.drawRect(posX - 0.5D, posY, posX + 0.5D - 0.5D, endPosY, color);
                     Gui.drawRect(posX, endPosY - 0.5D, endPosX, endPosY, color);
                     Gui.drawRect(posX - 0.5D, posY, endPosX, posY + 0.5D, color);
                     Gui.drawRect(endPosX - 0.5D, posY, endPosX, endPosY, color);
                  } else {
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
                  }
               }

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
                  textWidth = (double)(fr.getWidth(name) * scale);
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
                  GuiUtils.drawBorderedRect(posX - 0.5D, endPosY + 4.3D, endPosX - posX + 1.0D, 1.5D, 0.5D, (new Color(28, 28, 28, 255)).getRGB(), -1);
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

   private Vector3d project2D(int scaleFactor, double x, double y, double z) {
      GL11.glGetFloat(2982, this.modelview);
      GL11.glGetFloat(2983, this.projection);
      GL11.glGetInteger(2978, this.viewport);
      return GLU.gluProject((float)x, (float)y, (float)z, this.modelview, this.projection, this.viewport, this.vector) ? new Vector3d((double)(this.vector.get(0) / (float)scaleFactor), (double)(((float)Display.getHeight() - this.vector.get(1)) / (float)scaleFactor), (double)this.vector.get(2)) : null;
   }

   private boolean isValid(Entity entity) {
      if (entity == this.mc.thePlayer && (!(Boolean)this.localPlayer.getValue() || this.mc.gameSettings.thirdPersonView == 0)) {
         return false;
      } else if (entity.isDead) {
         return false;
      } else if (!(Boolean)this.invisibles.getValue() && entity.isInvisible()) {
         return false;
      } else if ((Boolean)this.animals.getValue() && entity instanceof EntityAnimal) {
         return true;
      } else if ((Boolean)this.players.getValue() && entity instanceof EntityPlayer) {
         return true;
      } else {
         return (Boolean)this.mobs.getValue() && (entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityDragon || entity instanceof EntityGolem);
      }
   }

   public static enum BoxMode {
      Box,
      Corner;
   }
}
