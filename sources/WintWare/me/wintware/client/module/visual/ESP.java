package me.wintware.client.module.visual;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventRender2D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.module.world.NameProtect;
import me.wintware.client.utils.font.FontRenderer;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class ESP extends Module {
   public final List<Entity> collectedEntities = new ArrayList();
   private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
   private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
   private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
   private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
   private final int color;
   private final int backgroundColor;
   private final int black;

   public ESP() {
      super("ESP", Category.Visuals);
      this.color = Color.WHITE.getRGB();
      this.backgroundColor = (new Color(0, 0, 0, 120)).getRGB();
      this.black = Color.BLACK.getRGB();
      ArrayList<String> options = new ArrayList();
      options.add("Box");
      options.add("Corner");
      options.add("Apex");
      Main.instance.setmgr.rSetting(new Setting("2D Mode", this, "Corner", options));
      Main.instance.setmgr.rSetting(new Setting("HealthRect", this, true));
      Main.instance.setmgr.rSetting(new Setting("Armor", this, true));
      Main.instance.setmgr.rSetting(new Setting("Border", this, true));
      Main.instance.setmgr.rSetting(new Setting("Tags", this, true));
      Main.instance.setmgr.rSetting(new Setting("Items", this, true));
   }

   @EventTarget
   public void onRender2D(EventRender2D event) {
      String mode = Main.instance.setmgr.getSettingByName("2D Mode").getValString();
      GL11.glPushMatrix();
      this.collectEntities();
      float partialTicks = event.getPartialTicks();
      ScaledResolution scaledResolution = event.getResolution();
      int scaleFactor = ScaledResolution.getScaleFactor();
      double scaling = (double)scaleFactor / Math.pow(scaleFactor, 2.0D);
      GL11.glScaled(scaling, scaling, scaling);
      int black = this.black;
      int color = this.color;
      int background = this.backgroundColor;
      float scale = 1.0F;
      float upscale = 1.0F / scale;
      RenderManager renderMng = mc.getRenderManager();
      EntityRenderer entityRenderer = mc.entityRenderer;
      List<Entity> collectedEntities = this.collectedEntities;
      int i = 0;

      for(int collectedEntitiesSize = collectedEntities.size(); i < collectedEntitiesSize; ++i) {
         Entity entity = collectedEntities.get(i);
         if (this.isValid(entity) && RenderUtil.isInViewFrustrum(entity)) {
            double var10000;
            Minecraft var10001;
            Minecraft var10002;
            double x;
            double y;
            double z;
            double width;
            double var55;
            label191: {
               x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
               y = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
               z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
               width = (double)entity.width / 1.5D;
               var10000 = entity.height;
               if (!entity.isSneaking()) {
                  label188: {
                     var10002 = mc;
                     if (entity == Minecraft.player) {
                        var10001 = mc;
                        if (Minecraft.player.isSneaking()) {
                           break label188;
                        }
                     }

                     var55 = 0.2D;
                     break label191;
                  }
               }

               var55 = -0.3D;
            }

            double height = var10000 + var55;
            AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
            Vector3d[] vectors = new Vector3d[]{new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ)};
            entityRenderer.setupCameraTransform(partialTicks, 0);
            Vector4d position = null;
            Vector3d[] var32 = vectors;
            int var33 = vectors.length;

            for(int var34 = 0; var34 < var33; ++var34) {
               Vector3d vector = var32[var34];
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
               if (mode.equalsIgnoreCase("Box") && Main.instance.setmgr.getSettingByName("Border").getValue()) {
                  RenderUtil.drawNewRect(posX - 1.0D, posY, posX + 0.5D, endPosY + 0.5D, black);
                  RenderUtil.drawNewRect(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 0.5D + 0.5D, black);
                  RenderUtil.drawNewRect(endPosX - 0.5D - 0.5D, posY, endPosX + 0.5D, endPosY + 0.5D, black);
                  RenderUtil.drawNewRect(posX - 1.0D, endPosY - 0.5D - 0.5D, endPosX + 0.5D, endPosY + 0.5D, black);
                  RenderUtil.drawNewRect(posX - 0.5D, posY, posX + 0.5D - 0.5D, endPosY, color);
                  RenderUtil.drawNewRect(posX, endPosY - 0.5D, endPosX, endPosY, color);
                  RenderUtil.drawNewRect(posX - 0.5D, posY, endPosX, posY + 0.5D, color);
                  RenderUtil.drawNewRect(endPosX - 0.5D, posY, endPosX, endPosY, color);
               }

               if (mode.equalsIgnoreCase("Apex") && Main.instance.setmgr.getSettingByName("Border").getValue()) {
                  RenderUtil.drawNewRect(endPosX - 0.5D - 0.5D, posY, endPosX + 0.5D, endPosY + 0.5D, black);
                  RenderUtil.drawNewRect(posX - 1.0D, posY, posX + 0.5D, endPosY + 0.5D, black);
                  RenderUtil.drawNewRect(posX - 1.0D, endPosY - 1.0D, posX + (endPosX - posX) / 4.0D + 0.5D, endPosY + 0.5D, black);
                  RenderUtil.drawNewRect(endPosX - 1.0D, endPosY - 1.0D, endPosX + (posX - endPosX) / 4.0D - 0.5D, endPosY + 0.5D, black);
                  RenderUtil.drawNewRect(posX - 1.0D, posY - 0.5D, posX + (endPosX - posX) / 4.0D + 0.5D, posY + 1.0D, black);
                  RenderUtil.drawNewRect(endPosX, posY - 0.5D, endPosX + (posX - endPosX) / 4.0D - 0.5D, posY + 1.0D, black);
                  RenderUtil.drawNewRect(posX - 0.5D, posY, posX + 0.5D - 0.5D, endPosY, color);
                  RenderUtil.drawNewRect(endPosX - 0.5D, posY, endPosX, endPosY, color);
                  RenderUtil.drawNewRect(posX, endPosY - 0.5D, posX + (endPosX - posX) / 4.0D, endPosY, color);
                  RenderUtil.drawNewRect(endPosX, endPosY - 0.5D, endPosX + (posX - endPosX) / 4.0D, endPosY, color);
                  RenderUtil.drawNewRect(posX - 0.5D, posY, posX + (endPosX - posX) / 4.0D, posY + 0.5D, color);
                  RenderUtil.drawNewRect(endPosX, posY, endPosX + (posX - endPosX) / 4.0D, posY + 0.5D, color);
               }

               if (mode.equalsIgnoreCase("Corner") && Main.instance.setmgr.getSettingByName("Border").getValue()) {
                  RenderUtil.drawNewRect(posX + 0.5D, posY, posX - 1.0D, posY + (endPosY - posY) / 4.0D + 0.5D, black);
                  RenderUtil.drawNewRect(posX - 1.0D, endPosY, posX + 0.5D, endPosY - (endPosY - posY) / 4.0D - 0.5D, black);
                  RenderUtil.drawNewRect(posX - 1.0D, posY - 0.5D, posX + (endPosX - posX) / 3.0D + 0.5D, posY + 1.0D, black);
                  RenderUtil.drawNewRect(endPosX - (endPosX - posX) / 3.0D - 0.5D, posY - 0.5D, endPosX, posY + 1.0D, black);
                  RenderUtil.drawNewRect(endPosX - 1.0D, posY, endPosX + 0.5D, posY + (endPosY - posY) / 4.0D + 0.5D, black);
                  RenderUtil.drawNewRect(endPosX - 1.0D, endPosY, endPosX + 0.5D, endPosY - (endPosY - posY) / 4.0D - 0.5D, black);
                  RenderUtil.drawNewRect(posX - 1.0D, endPosY - 1.0D, posX + (endPosX - posX) / 3.0D + 0.5D, endPosY + 0.5D, black);
                  RenderUtil.drawNewRect(endPosX - (endPosX - posX) / 3.0D - 0.5D, endPosY - 1.0D, endPosX + 0.5D, endPosY + 0.5D, black);
                  RenderUtil.drawNewRect(posX, posY, posX - 0.5D, posY + (endPosY - posY) / 4.0D, color);
                  RenderUtil.drawNewRect(posX, endPosY, posX - 0.5D, endPosY - (endPosY - posY) / 4.0D, color);
                  RenderUtil.drawNewRect(posX - 0.5D, posY, posX + (endPosX - posX) / 3.0D, posY + 0.5D, color);
                  RenderUtil.drawNewRect(endPosX - (endPosX - posX) / 3.0D, posY, endPosX, posY + 0.5D, color);
                  RenderUtil.drawNewRect(endPosX - 0.5D, posY, endPosX, posY + (endPosY - posY) / 4.0D, color);
                  RenderUtil.drawNewRect(endPosX - 0.5D, endPosY, endPosX, endPosY - (endPosY - posY) / 4.0D, color);
                  RenderUtil.drawNewRect(posX, endPosY - 0.5D, posX + (endPosX - posX) / 3.0D, endPosY, color);
                  RenderUtil.drawNewRect(endPosX - (endPosX - posX) / 3.0D, endPosY - 0.5D, endPosX - 0.5D, endPosY, color);
               }

               boolean living = entity instanceof EntityLivingBase;
               float itemDurability;
               double durabilityWidth;
               double diff1;
               float tagX;
               if (living && Main.instance.setmgr.getSettingByName("HealthRect").getValue()) {
                  EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
                  float hp2 = entityLivingBase.getHealth();
                  itemDurability = entityLivingBase.getMaxHealth();
                  durabilityWidth = hp2 / itemDurability;
                  diff1 = (endPosY - posY) * durabilityWidth;
                  RenderUtil.drawNewRect(posX - 3.5D, posY - 0.5D, posX - 1.5D, endPosY + 0.5D, background);
                  if (hp2 > 0.0F) {
                     tagX = entityLivingBase.getAbsorptionAmount();
                     int healthColor2 = (int)hp2;
                     if (hp2 > 12.0F) {
                        healthColor2 = (new Color(0, 251, 0)).getRGB();
                     } else if (hp2 < 12.0F) {
                        healthColor2 = (new Color(199, 101, 0)).getRGB();
                     }

                     RenderUtil.drawNewRect(posX - 3.0D, endPosY, posX - 5.0D, endPosY - diff1, healthColor2);
                  }
               }

               int maxDamage;
               if (living && Main.instance.setmgr.getSettingByName("Tags").getValue() && !Main.instance.moduleManager.getModuleByClass(NameTags.class).getState()) {
                  float scaledHeight = 20.0F;
                  boolean texcolor = true;
                  String name = entity.getName();
                  if (Main.instance.moduleManager.getModuleByClass(NameProtect.class).getState()) {
                     var10001 = mc;
                     if (entity == Minecraft.player) {
                        name = "Protect";
                     }
                  }

                  if (entity instanceof EntityItem) {
                     name = ((EntityItem)entity).getEntityItem().getDisplayName();
                  }

                  maxDamage = (new Color(255, 255, 255)).getRGB();
                  durabilityWidth = (endPosX - posX) / 2.0D;
                  FontRenderer var53 = mc.fontRenderer;
                  StringBuilder var56 = (new StringBuilder()).append(name).append(" §7");
                  var10002 = mc;
                  diff1 = (float)var53.getStringWidth(var56.append((int)Minecraft.player.getDistanceToEntity(entity)).append("m").toString()) * scale;
                  tagX = (float)((posX + durabilityWidth - diff1 / 2.0D) * (double)upscale);
                  float tagY = (float)(posY * (double)upscale) - scaledHeight;
                  GL11.glPushMatrix();
                  GL11.glScalef(scale, scale, scale);
                  if (living) {
                     RenderUtil.drawNewRect(tagX - 2.0F, tagY - 2.0F, (double)tagX + diff1 * (double)upscale + 2.0D, tagY + 9.0F, (new Color(0, 0, 0, 140)).getRGB());
                  }

                  var53 = mc.fontRenderer;
                  var56 = (new StringBuilder()).append(name).append(" §7");
                  var10002 = mc;
                  var53.drawStringWithShadow(var56.append((int)Minecraft.player.getDistanceToEntity(entity)).append("m").toString(), (double)tagX + 0.5D, tagY + 0.5F, maxDamage);
                  GL11.glPopMatrix();
               }

               if (living && Main.instance.setmgr.getSettingByName("Armor").getValue()) {
                  if (entity instanceof EntityPlayer) {
                     EntityPlayer player = (EntityPlayer)entity;
                     double ydiff = (endPosY - posY) / 4.0D;
                     ItemStack stack = player.getEquipmentInSlot(4);
                     Minecraft var54;
                     if (stack != null) {
                        RenderUtil.drawNewRect(endPosX + 3.5D, posY - 0.5D, endPosX + 1.5D, posY + ydiff, background);
                        diff1 = posY + ydiff - 1.0D - (posY + 2.0D);
                        diff1 = 1.0D - (double)stack.getItemDamage() / (double)stack.getMaxDamage();
                        RenderUtil.drawNewRect(endPosX + 3.0D, posY + ydiff, endPosX + 2.0D, posY + ydiff - 3.0D - diff1 * diff1, (new Color(78, 206, 229)).getRGB());
                        String stackname = stack.getDisplayName().equalsIgnoreCase("Air") ? "0" : (!(stack.getItem() instanceof ItemArmor) ? stack.getDisplayName() : stack.getMaxDamage() - stack.getItemDamage() + "");
                        var54 = mc;
                        if (Minecraft.player.getDistanceToEntity(player) < 20.0F) {
                           RenderUtil.renderItem(stack, (int)endPosX + 4, (int)posY + (int)ydiff - 1 - (int)(diff1 / 2.0D) - 18);
                           mc.clickguismall.drawStringWithShadow(stackname, (float)endPosX + 5.0F, (float)(posY + ydiff - 1.0D - diff1 / 2.0D) - (float)(mc.fontRenderer.getStringHeight(stack.getMaxDamage() - stack.getItemDamage() + "") / 2), -1);
                        }
                     }

                     ItemStack stack2 = player.getEquipmentInSlot(3);
                     if (stack2 != null) {
                        RenderUtil.drawNewRect(endPosX + 3.5D, posY + ydiff, endPosX + 1.5D, posY + ydiff * 2.0D, background);
                        diff1 = posY + ydiff * 2.0D - (posY + ydiff + 2.0D);
                        diff1 = 1.0D - (double)stack2.getItemDamage() * 1.0D / (double)stack2.getMaxDamage();
                        RenderUtil.drawNewRect(endPosX + 3.0D, posY + ydiff * 2.0D, endPosX + 2.0D, posY + ydiff * 2.0D - 1.0D - diff1 * diff1, (new Color(78, 206, 229)).getRGB());
                        String stackname = stack.getDisplayName().equalsIgnoreCase("Air") ? "0" : (!(stack2.getItem() instanceof ItemArmor) ? stack2.getDisplayName() : stack2.getMaxDamage() - stack2.getItemDamage() + "");
                        var54 = mc;
                        if (Minecraft.player.getDistanceToEntity(player) < 20.0F) {
                           RenderUtil.renderItem(stack2, (int)endPosX + 4, (int)(posY + ydiff * 2.0D) - (int)(diff1 / 2.0D) - 18);
                           mc.clickguismall.drawStringWithShadow(stackname, (float)endPosX + 5.0F, (float)(posY + ydiff * 2.0D - diff1 / 2.0D) - (float)(mc.fontRenderer.getStringHeight(stack2.getMaxDamage() - stack2.getItemDamage() + "") / 2), -1);
                        }
                     }

                     ItemStack stack3 = player.getEquipmentInSlot(2);
                     if (stack3 != null) {
                        RenderUtil.drawNewRect(endPosX + 3.5D, posY + ydiff * 2.0D, endPosX + 1.5D, posY + ydiff * 3.0D, background);
                        diff1 = posY + ydiff * 3.0D - (posY + ydiff * 2.0D + 2.0D);
                        double percent = 1.0D - (double)stack3.getItemDamage() * 1.0D / (double)stack3.getMaxDamage();
                        RenderUtil.drawNewRect(endPosX + 3.0D, posY + ydiff * 3.0D, endPosX + 2.0D, posY + ydiff * 3.0D - 1.0D - diff1 * percent, (new Color(78, 206, 229)).getRGB());
                        String stackname = stack.getDisplayName().equalsIgnoreCase("Air") ? "0" : (!(stack3.getItem() instanceof ItemArmor) ? stack3.getDisplayName() : stack3.getMaxDamage() - stack3.getItemDamage() + "");
                        var54 = mc;
                        if (Minecraft.player.getDistanceToEntity(player) < 20.0F) {
                           RenderUtil.renderItem(stack3, (int)endPosX + 4, (int)(posY + ydiff * 3.0D) - (int)(diff1 / 2.0D) - 18);
                           mc.clickguismall.drawStringWithShadow(stackname, (float)endPosX + 5.0F, (float)(posY + ydiff * 3.0D - diff1 / 2.0D) - (float)(mc.fontRenderer.getStringHeight(stack3.getMaxDamage() - stack3.getItemDamage() + "") / 2), -1);
                        }
                     }

                     ItemStack stack4 = player.getEquipmentInSlot(1);
                     if (stack4 != null) {
                        RenderUtil.drawNewRect(endPosX + 3.5D, posY + ydiff * 3.0D, endPosX + 1.5D, posY + ydiff * 4.0D + 0.5D, background);
                        diff1 = posY + ydiff * 4.0D - (posY + ydiff * 3.0D + 2.0D);
                        double percent = 1.0D - (double)stack4.getItemDamage() * 1.0D / (double)stack4.getMaxDamage();
                        RenderUtil.drawNewRect(endPosX + 3.0D, posY + ydiff * 4.0D, endPosX + 2.0D, posY + ydiff * 4.0D - 1.0D - diff1 * percent, (new Color(78, 206, 229)).getRGB());
                        String stackname = stack.getDisplayName().equalsIgnoreCase("Air") ? "0" : (!(stack4.getItem() instanceof ItemArmor) ? stack4.getDisplayName() : stack4.getMaxDamage() - stack4.getItemDamage() + "");
                        var54 = mc;
                        if (Minecraft.player.getDistanceToEntity(player) < 20.0F) {
                           RenderUtil.renderItem(stack4, (int)endPosX + 4, (int)(posY + ydiff * 4.0D) - (int)(diff1 / 2.0D) - 18);
                           mc.clickguismall.drawStringWithShadow(stackname, (float)endPosX + 5.0F, (float)(posY + ydiff * 4.0D - diff1 / 2.0D) - (float)(mc.fontRenderer.getStringHeight(stack4.getMaxDamage() - stack4.getItemDamage() + "") / 2), -1);
                        }
                     }
                  }
               } else if (entity instanceof EntityItem) {
                  ItemStack itemStack = ((EntityItem)entity).getEntityItem();
                  if (itemStack.isItemStackDamageable()) {
                     maxDamage = itemStack.getMaxDamage();
                     itemDurability = (float)(maxDamage - itemStack.getItemDamage());
                     durabilityWidth = (endPosX - posX) * (double)itemDurability / (double)maxDamage;
                     RenderUtil.drawNewRect(posX, endPosY + 2.0D, posX + durabilityWidth, endPosY + 3.0D, 16777215);
                  }
               }
            }
         }
      }

      GL11.glPopMatrix();
      GL11.glEnable(2929);
      GlStateManager.enableBlend();
      entityRenderer.setupOverlayRendering();
   }

   private boolean isValid(Entity entity) {
      Minecraft var10001;
      if (mc.gameSettings.thirdPersonView == 0) {
         var10001 = mc;
         if (entity == Minecraft.player) {
            return false;
         }
      }

      if (entity.isDead) {
         return false;
      } else if (entity instanceof EntityItem && Main.instance.setmgr.getSettingByName("Items").getValue()) {
         return true;
      } else if (entity instanceof EntityAnimal) {
         return false;
      } else if (entity instanceof EntityPlayer) {
         return true;
      } else if (entity instanceof EntityArmorStand) {
         return false;
      } else if (entity instanceof IAnimals) {
         return false;
      } else if (entity instanceof EntityItemFrame) {
         return false;
      } else if (!(entity instanceof EntityArrow) && !(entity instanceof EntitySpectralArrow)) {
         if (entity instanceof EntityMinecart) {
            return false;
         } else if (entity instanceof EntityBoat) {
            return false;
         } else if (entity instanceof EntityDragonFireball) {
            return false;
         } else if (entity instanceof EntityXPOrb) {
            return false;
         } else if (entity instanceof EntityMinecartChest) {
            return false;
         } else if (entity instanceof EntityTNTPrimed) {
            return false;
         } else if (entity instanceof EntityMinecartTNT) {
            return false;
         } else if (entity instanceof EntityVillager) {
            return false;
         } else if (entity instanceof EntityExpBottle) {
            return false;
         } else if (entity instanceof EntityLightningBolt) {
            return false;
         } else if (entity instanceof EntityPotion) {
            return false;
         } else if (entity instanceof Entity) {
            return false;
         } else if (!(entity instanceof EntityMob) && !(entity instanceof EntitySlime) && !(entity instanceof EntityDragon) && !(entity instanceof EntityGolem)) {
            var10001 = mc;
            return entity != Minecraft.player;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void collectEntities() {
      this.collectedEntities.clear();
      List<Entity> playerEntities = mc.world.loadedEntityList;
      int i = 0;

      for(int playerEntitiesSize = playerEntities.size(); i < playerEntitiesSize; ++i) {
         Entity entity = playerEntities.get(i);
         if (this.isValid(entity)) {
            this.collectedEntities.add(entity);
         }
      }

   }

   private Vector3d project2D(int scaleFactor, double x, double y, double z) {
      GL11.glGetFloat(2982, this.modelview);
      GL11.glGetFloat(2983, this.projection);
      GL11.glGetInteger(2978, this.viewport);
      return GLU.gluProject((float)x, (float)y, (float)z, this.modelview, this.projection, this.viewport, this.vector) ? new Vector3d(this.vector.get(0) / (float)scaleFactor, ((float)Display.getHeight() - this.vector.get(1)) / (float)scaleFactor, this.vector.get(2)) : null;
   }
}
