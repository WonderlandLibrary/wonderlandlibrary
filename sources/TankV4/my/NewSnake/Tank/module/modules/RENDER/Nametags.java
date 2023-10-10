package my.NewSnake.Tank.module.modules.RENDER;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import my.NewSnake.Tank.friend.FriendManager;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.NametagRenderEvent;
import my.NewSnake.event.events.Render2DEvent;
import my.NewSnake.event.events.Render3DEvent;
import my.NewSnake.utils.ClientUtils;
import my.NewSnake.utils.MathUtils;
import my.NewSnake.utils.RenderUtils;
import my.NewSnake.utils.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

@Module.Mod
public class Nametags extends Module {
   private double gradualFOVModifier;
   @Option.Op
   private boolean armor = true;
   @Option.Op
   private boolean hideInvisibles;
   public static Map entityPositions = new HashMap();
   @Option.Op(
      min = 0.0D,
      max = 2.0D,
      increment = 0.1D
   )
   private double scale = 1.2D;
   @Option.Op(
      min = 1.0D,
      max = 20.0D,
      increment = 1.0D
   )
   private double distance = 8.0D;
   private Character formatChar = new Character('§');

   @EventTarget
   private void onRender3DEvent(Render3DEvent var1) {
      this.updatePositions();
   }

   private void scale(Entity var1) {
      float var2 = (float)this.scale;
      float var3 = var2 * (ClientUtils.gamesettings().fovSetting / (ClientUtils.gamesettings().fovSetting * ClientUtils.player().getFovModifier()));
      if (this.gradualFOVModifier == 0.0D || Double.isNaN(this.gradualFOVModifier)) {
         this.gradualFOVModifier = (double)var3;
      }

      double var4 = this.gradualFOVModifier;
      double var6 = (double)var3 - this.gradualFOVModifier;
      ClientUtils.mc();
      this.gradualFOVModifier = var4 + var6 / ((double)Minecraft.debugFPS * 0.7D);
      var2 *= (float)this.gradualFOVModifier;
      var2 *= (float)(ClientUtils.mc().currentScreen == null && GameSettings.isKeyDown(ClientUtils.gamesettings().ofKeyBindZoom) ? 3 : 1);
      GlStateManager.scale(var2, var2, var2);
   }

   private double[] convertTo2D(double var1, double var3, double var5) {
      FloatBuffer var7 = BufferUtils.createFloatBuffer(3);
      IntBuffer var8 = BufferUtils.createIntBuffer(16);
      FloatBuffer var9 = BufferUtils.createFloatBuffer(16);
      FloatBuffer var10 = BufferUtils.createFloatBuffer(16);
      GL11.glGetFloat(2982, var9);
      GL11.glGetFloat(2983, var10);
      GL11.glGetInteger(2978, var8);
      boolean var11 = GLU.gluProject((float)var1, (float)var3, (float)var5, var9, var10, var8, var7);
      return var11 ? new double[]{(double)var7.get(0), (double)((float)Display.getHeight() - var7.get(1)), (double)var7.get(2)} : null;
   }

   private double[] convertTo2D(double var1, double var3, double var5, Entity var7) {
      float var8 = ClientUtils.mc().timer.renderPartialTicks;
      float var9 = ClientUtils.player().rotationYaw;
      float var10 = ClientUtils.player().prevRotationYaw;
      float[] var11 = RotationUtils.getRotationFromPosition(var7.lastTickPosX + (var7.posX - var7.lastTickPosX) * (double)var8, var7.lastTickPosZ + (var7.posZ - var7.lastTickPosZ) * (double)var8, var7.lastTickPosY + (var7.posY - var7.lastTickPosY) * (double)var8 - 1.6D);
      Entity var12 = ClientUtils.mc().getRenderViewEntity();
      Entity var13 = ClientUtils.mc().getRenderViewEntity();
      float var14 = var11[0];
      var13.prevRotationYaw = var14;
      var12.rotationYaw = var14;
      ClientUtils.mc().entityRenderer.setupCameraTransform(var8, 0);
      double[] var15 = this.convertTo2D(var1, var3, var5);
      ClientUtils.mc().getRenderViewEntity().rotationYaw = var9;
      ClientUtils.mc().getRenderViewEntity().prevRotationYaw = var10;
      ClientUtils.mc().entityRenderer.setupCameraTransform(var8, 0);
      return var15;
   }

   private static void drawEnchantTag(String var0, int var1, int var2) {
      GlStateManager.pushMatrix();
      GlStateManager.disableDepth();
      var1 *= 1;
      var2 -= 4;
      GL11.glScalef(0.57F, 0.57F, 0.57F);
      ClientUtils.clientFont().drawStringWithShadow(var0, (double)var1, (double)(-36 - var2), -1286);
      GlStateManager.enableDepth();
      GlStateManager.popMatrix();
   }

   private void updatePositions() {
      entityPositions.clear();
      float var1 = ClientUtils.mc().timer.renderPartialTicks;
      Iterator var3 = ClientUtils.world().loadedEntityList.iterator();

      while(true) {
         Entity var4;
         do {
            do {
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  Object var2 = var3.next();
                  var4 = (Entity)var2;
               } while(var4 == ClientUtils.player());
            } while(!(var4 instanceof EntityPlayer));
         } while(var4.isInvisible() && this.hideInvisibles);

         double var10000 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)var1;
         ClientUtils.mc().getRenderManager();
         double var5 = var10000 - RenderManager.viewerPosX;
         var10000 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)var1;
         ClientUtils.mc().getRenderManager();
         double var7 = var10000 - RenderManager.viewerPosY;
         var10000 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)var1;
         ClientUtils.mc().getRenderManager();
         double var9 = var10000 - RenderManager.viewerPosZ;
         var7 += (double)var4.height + 0.2D;
         if (!(this.convertTo2D(var5, var7, var9)[2] < 0.0D) && !(this.convertTo2D(var5, var7, var9)[2] >= 1.0D)) {
            entityPositions.put((EntityPlayer)var4, new double[]{this.convertTo2D(var5, var7, var9)[0], this.convertTo2D(var5, var7, var9)[1], Math.abs(this.convertTo2D(var5, var7 + 1.0D, var9, var4)[1] - this.convertTo2D(var5, var7, var9, var4)[1]), this.convertTo2D(var5, var7, var9)[2]});
         }
      }
   }

   @EventTarget
   private void onRender2DEvent(Render2DEvent var1) {
      GlStateManager.pushMatrix();
      ScaledResolution var2 = new ScaledResolution(ClientUtils.mc());
      double var3 = (double)var2.getScaleFactor() / Math.pow((double)var2.getScaleFactor(), 2.0D);
      GlStateManager.scale(var3, var3, var3);
      Iterator var6 = entityPositions.keySet().iterator();

      while(true) {
         while(true) {
            Entity var5;
            do {
               if (!var6.hasNext()) {
                  GlStateManager.popMatrix();
                  return;
               }

               var5 = (Entity)var6.next();
               GlStateManager.pushMatrix();
            } while(var5 == ClientUtils.player());

            if (var5 instanceof EntityPlayer) {
               String var7 = var5.getDisplayName().getFormattedText();
               if (FriendManager.isFriend(var5.getName())) {
                  var7 = var7.replace(var5.getDisplayName().getFormattedText(), "§b" + FriendManager.getAliasName(var5.getName()));
               }

               String var8 = this.formatChar.toString();
               double var9 = MathUtils.roundToPlace((double)(((EntityPlayer)var5).getHealth() / 2.0F), 2);
               if (var9 >= 6.0D) {
                  var8 = String.valueOf(var8) + "f";
               } else if (var9 >= 3.5D) {
                  var8 = String.valueOf(var8) + "6";
               } else {
                  var8 = String.valueOf(var8) + "4";
               }

               var7 = String.valueOf(var7) + " " + var8 + var9 + " §c❤";
               double[] var11 = (double[])entityPositions.get(var5);
               if (var11[3] < 0.0D || var11[3] >= 1.0D) {
                  GlStateManager.popMatrix();
                  continue;
               }

               GlStateManager.translate(var11[0], var11[1], 0.0D);
               this.scale(var5);
               GlStateManager.translate(0.0D, -2.5D, 0.0D);
               int var12 = ClientUtils.clientFont().getStringWidth(var7);
               RenderUtils.rectangle((double)(-var12 / 2 - 3), -12.0D, (double)(var12 / 2 + 3), 1.0D, -1191182336);
               GlStateManager.color(0.0F, 0.0F, 0.0F);
               ClientUtils.clientFont().drawString(var7, (double)(-var12 / 2), -9.0D, -1);
               GlStateManager.color(1.0F, 1.0F, 1.0F);
               if (this.armor) {
                  ArrayList var13 = new ArrayList();

                  int var14;
                  for(var14 = 0; var14 < 5; ++var14) {
                     ItemStack var15 = ((EntityPlayer)var5).getEquipmentInSlot(var14);
                     if (var15 != null) {
                        var13.add(var15);
                     }
                  }

                  var14 = -(var13.size() * 9);
                  Iterator var25 = var13.iterator();

                  while(var25.hasNext()) {
                     ItemStack var16 = (ItemStack)var25.next();
                     RenderHelper.enableGUIStandardItemLighting();
                     ClientUtils.mc().getRenderItem().renderItemIntoGUI(var16, var14, -30);
                     RenderItem var10000 = ClientUtils.mc().getRenderItem();
                     ClientUtils.mc();
                     var10000.renderItemOverlays(Minecraft.fontRendererObj, var16, var14, -30);
                     var14 += 2;
                     RenderHelper.disableStandardItemLighting();
                     String var17 = "";
                     if (var16 != null) {
                        int var18 = 21;
                        int var19 = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, var16);
                        int var20 = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, var16);
                        int var21 = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, var16);
                        if (var19 > 0) {
                           drawEnchantTag("Sh" + var19, var14, var18);
                           var18 -= 9;
                        }

                        if (var20 > 0) {
                           drawEnchantTag("Fir" + var20, var14, var18);
                           var18 -= 9;
                        }

                        if (var21 > 0) {
                           drawEnchantTag("Kb" + var21, var14, var18);
                        } else {
                           int var22;
                           int var23;
                           int var24;
                           if (var16.getItem() instanceof ItemArmor) {
                              var22 = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, var16);
                              var23 = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, var16);
                              var24 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, var16);
                              if (var22 > 0) {
                                 drawEnchantTag("P" + var22, var14, var18);
                                 var18 -= 9;
                              }

                              if (var23 > 0) {
                                 drawEnchantTag("Th" + var23, var14, var18);
                                 var18 -= 9;
                              }

                              if (var24 > 0) {
                                 drawEnchantTag("Unb" + var24, var14, var18);
                              }
                           } else if (var16.getItem() instanceof ItemBow) {
                              var22 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, var16);
                              var23 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, var16);
                              var24 = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, var16);
                              if (var22 > 0) {
                                 drawEnchantTag("Pow" + var22, var14, var18);
                                 var18 -= 9;
                              }

                              if (var23 > 0) {
                                 drawEnchantTag("Pun" + var23, var14, var18);
                                 var18 -= 9;
                              }

                              if (var24 > 0) {
                                 drawEnchantTag("Fir" + var24, var14, var18);
                              }
                           } else if (var16.getRarity() == EnumRarity.EPIC) {
                              drawEnchantTag("§lGod", var14, var18);
                           }
                        }

                        var14 += 16;
                     }
                  }
               }
            }

            GlStateManager.popMatrix();
         }
      }
   }

   @EventTarget
   private void onNametagRender(NametagRenderEvent var1) {
      var1.setCancelled(true);
   }
}
