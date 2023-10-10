package me.AveReborn.util;

import com.mojang.authlib.GameProfile;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import me.AveReborn.events.EventRender;
import me.AveReborn.util.Box;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

public enum RenderUtil {
   INSTANCE;

   public static Minecraft mc = Minecraft.getMinecraft();
   public static float delta;

   public static void enableGL3D(float lineWidth) {
      GL11.glDisable(3008);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glEnable(2884);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
      GL11.glLineWidth(lineWidth);
   }

   public static void entityESPBox(Entity e, Color color, EventRender event) {
      double var10000 = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)event.getPartialTicks();
      Minecraft.getMinecraft().getRenderManager();
      double posX = var10000 - RenderManager.renderPosX;
      var10000 = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)event.getPartialTicks();
      Minecraft.getMinecraft().getRenderManager();
      double posY = var10000 - RenderManager.renderPosY;
      var10000 = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)event.getPartialTicks();
      Minecraft.getMinecraft().getRenderManager();
      double posZ = var10000 - RenderManager.renderPosZ;
      AxisAlignedBB box = AxisAlignedBB.fromBounds(posX - (double)e.width, posY, posZ - (double)e.width, posX + (double)e.width, posY + (double)e.height + 0.2D, posZ + (double)e.width);
      if(e instanceof EntityLivingBase) {
         box = AxisAlignedBB.fromBounds(posX - (double)e.width + 0.2D, posY, posZ - (double)e.width + 0.2D, posX + (double)e.width - 0.2D, posY + (double)e.height + (e.isSneaking()?0.02D:0.2D), posZ + (double)e.width - 0.2D);
      }

      GL11.glLineWidth(1.0F);
      GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 1.0F);
      drawOutlinedBoundingBox(box);
   }

   public static int rainbow(int delay) {
      double rainbow = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 10.0D);
      return Color.getHSBColor((float)((rainbow %= 360.0D) / 360.0D), 0.5F, 1.0F).getRGB();
   }

   public static void disableGL3D() {
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDisable(3042);
      GL11.glEnable(3008);
      GL11.glDepthMask(true);
      GL11.glCullFace(1029);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
   }

   public static void drawCircle(double x, double y, double radius, int c) {
      float f2 = (float)(c >> 24 & 255) / 255.0F;
      float f22 = (float)(c >> 16 & 255) / 255.0F;
      float f3 = (float)(c >> 8 & 255) / 255.0F;
      float f4 = (float)(c & 255) / 255.0F;
      GlStateManager.alphaFunc(516, 0.001F);
      GlStateManager.color(f22, f3, f4, f2);
      GlStateManager.enableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      Tessellator tes = Tessellator.getInstance();

      for(double i = 0.0D; i < 360.0D; ++i) {
         double f5 = Math.sin(i * 3.141592653589793D / 180.0D) * radius;
         double f6 = Math.cos(i * 3.141592653589793D / 180.0D) * radius;
         GL11.glVertex2d((double)f3 + x, (double)f4 + y);
      }

      GlStateManager.disableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.enableTexture2D();
      GlStateManager.alphaFunc(516, 0.1F);
   }

   public static void drawFilledCircle(double x, double y, double r, int c, int id) {
      float f = (float)(c >> 24 & 255) / 255.0F;
      float f1 = (float)(c >> 16 & 255) / 255.0F;
      float f2 = (float)(c >> 8 & 255) / 255.0F;
      float f3 = (float)(c & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glBegin(9);
      int i;
      double x2;
      double y2;
      if(id == 1) {
         GL11.glVertex2d(x, y);

         for(i = 0; i <= 90; ++i) {
            x2 = Math.sin((double)i * 3.141526D / 180.0D) * r;
            y2 = Math.cos((double)i * 3.141526D / 180.0D) * r;
            GL11.glVertex2d(x - x2, y - y2);
         }
      } else if(id == 2) {
         GL11.glVertex2d(x, y);

         for(i = 90; i <= 180; ++i) {
            x2 = Math.sin((double)i * 3.141526D / 180.0D) * r;
            y2 = Math.cos((double)i * 3.141526D / 180.0D) * r;
            GL11.glVertex2d(x - x2, y - y2);
         }
      } else if(id == 3) {
         GL11.glVertex2d(x, y);

         for(i = 270; i <= 360; ++i) {
            x2 = Math.sin((double)i * 3.141526D / 180.0D) * r;
            y2 = Math.cos((double)i * 3.141526D / 180.0D) * r;
            GL11.glVertex2d(x - x2, y - y2);
         }
      } else if(id == 4) {
         GL11.glVertex2d(x, y);

         for(i = 180; i <= 270; ++i) {
            x2 = Math.sin((double)i * 3.141526D / 180.0D) * r;
            y2 = Math.cos((double)i * 3.141526D / 180.0D) * r;
            GL11.glVertex2d(x - x2, y - y2);
         }
      } else {
         for(i = 0; i <= 360; ++i) {
            x2 = Math.sin((double)i * 3.141526D / 180.0D) * r;
            y2 = Math.cos((double)i * 3.141526D / 180.0D) * r;
            GL11.glVertex2f((float)(x - x2), (float)(y - y2));
         }
      }

      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
   }

   public static void drawFullCircle(int cx, int cy, double r, int segments, float lineWidth, int part, int c) {
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      r *= 2.0D;
      cx *= 2;
      cy *= 2;
      float f2 = (float)(c >> 24 & 255) / 255.0F;
      float f22 = (float)(c >> 16 & 255) / 255.0F;
      float f3 = (float)(c >> 8 & 255) / 255.0F;
      float f4 = (float)(c & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glLineWidth(lineWidth);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(f22, f3, f4, f2);
      GL11.glBegin(3);

      for(int i = segments - part; i <= segments; ++i) {
         double x = Math.sin((double)i * 3.141592653589793D / 180.0D) * r;
         double y = Math.cos((double)i * 3.141592653589793D / 180.0D) * r;
         GL11.glVertex2d((double)cx + x, (double)cy + y);
      }

      GL11.glEnd();
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
   }

   public static void drawBox(Box box) {
      if(box != null) {
         GL11.glBegin(7);
         GL11.glVertex3d(box.minX, box.minY, box.maxZ);
         GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
         GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
         GL11.glVertex3d(box.minX, box.minY, box.maxZ);
         GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(box.minX, box.minY, box.minZ);
         GL11.glVertex3d(box.minX, box.minY, box.maxZ);
         GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
         GL11.glVertex3d(box.minX, box.maxY, box.minZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(box.minX, box.minY, box.maxZ);
         GL11.glVertex3d(box.minX, box.minY, box.minZ);
         GL11.glVertex3d(box.minX, box.maxY, box.minZ);
         GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
         GL11.glVertex3d(box.maxX, box.minY, box.minZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(box.maxX, box.minY, box.minZ);
         GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(box.minX, box.minY, box.minZ);
         GL11.glVertex3d(box.maxX, box.minY, box.minZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
         GL11.glVertex3d(box.minX, box.maxY, box.minZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(box.maxX, box.minY, box.minZ);
         GL11.glVertex3d(box.minX, box.minY, box.minZ);
         GL11.glVertex3d(box.minX, box.maxY, box.minZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(box.minX, box.maxY, box.minZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
         GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
         GL11.glVertex3d(box.minX, box.maxY, box.minZ);
         GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(box.minX, box.minY, box.minZ);
         GL11.glVertex3d(box.maxX, box.minY, box.minZ);
         GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
         GL11.glVertex3d(box.minX, box.minY, box.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(box.maxX, box.minY, box.minZ);
         GL11.glVertex3d(box.minX, box.minY, box.minZ);
         GL11.glVertex3d(box.minX, box.minY, box.maxZ);
         GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
         GL11.glEnd();
      }
   }

   public static void glColor(int hex) {
      float alpha = (float)(hex >> 24 & 255) / 255.0F;
      float red = (float)(hex >> 16 & 255) / 255.0F;
      float green = (float)(hex >> 8 & 255) / 255.0F;
      float blue = (float)(hex & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
   }

   public static double interpolate(double newPos, double oldPos) {
      return oldPos + (newPos - oldPos) * (double)Minecraft.getMinecraft().timer.renderPartialTicks;
   }

   public static Color rainbowEffect(int delay) {
      float hue = (float)(System.nanoTime() + (long)delay) / 2.0E10F % 1.0F;
      Color color = new Color((int)Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()), 16));
      return new Color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
   }

   public static void drawFullscreenImage(ResourceLocation image) {
      ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3008);
      Minecraft.getMinecraft().getTextureManager().bindTexture(image);
      Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), (float)scaledResolution.getScaledWidth(), (float)scaledResolution.getScaledHeight());
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GL11.glEnable(3008);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void drawPlayerHead(String playerName, int x, int y, int width, int height) {
      Iterator var6 = Minecraft.getMinecraft().theWorld.getLoadedEntityList().iterator();

      while(var6.hasNext()) {
         Object player = var6.next();
         if(player instanceof EntityPlayer) {
            EntityPlayer ply = (EntityPlayer)player;
            if(playerName.equalsIgnoreCase(ply.getName())) {
               GameProfile profile = new GameProfile(ply.getUniqueID(), ply.getName());
               NetworkPlayerInfo networkplayerinfo1 = new NetworkPlayerInfo(profile);
               new ScaledResolution(Minecraft.getMinecraft());
               GL11.glDisable(2929);
               GL11.glEnable(3042);
               GL11.glDepthMask(false);
               OpenGlHelper.glBlendFunc(770, 771, 1, 0);
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               Minecraft.getMinecraft().getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
               Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, (float)width, (float)height);
               GL11.glDepthMask(true);
               GL11.glDisable(3042);
               GL11.glEnable(2929);
            }
         }
      }

   }

   public static double getAnimationState(double animation, double finalState, double speed) {
      float add = (float)(0.01D * speed);
      if(animation < finalState) {
         if(animation + (double)add < finalState) {
            animation += (double)add;
         } else {
            animation = finalState;
         }
      } else if(animation - (double)add > finalState) {
         animation -= (double)add;
      } else {
         animation = finalState;
      }

      return animation;
   }

   public static String getShaderCode(InputStreamReader file) {
      String shaderSource = "";

      try {
         String e;
         BufferedReader reader;
         for(reader = new BufferedReader(file); (e = reader.readLine()) != null; shaderSource = String.valueOf(shaderSource) + e + "\n") {
            ;
         }

         reader.close();
      } catch (IOException var4) {
         var4.printStackTrace();
         System.exit(-1);
      }

      return shaderSource.toString();
   }

   public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
      new ScaledResolution(Minecraft.getMinecraft());
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(image);
      Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, (float)width, (float)height);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
   }

   public static void drawOutlinedRect(int x, int y, int width, int height, int lineSize, Color lineColor, Color backgroundColor) {
      drawRect((float)x, (float)y, (float)width, (float)height, backgroundColor.getRGB());
      drawRect((float)x, (float)y, (float)width, (float)(y + lineSize), lineColor.getRGB());
      drawRect((float)x, (float)(height - lineSize), (float)width, (float)height, lineColor.getRGB());
      drawRect((float)x, (float)(y + lineSize), (float)(x + lineSize), (float)(height - lineSize), lineColor.getRGB());
      drawRect((float)(width - lineSize), (float)(y + lineSize), (float)width, (float)(height - lineSize), lineColor.getRGB());
   }

   public static void drawImage(ResourceLocation image, int x, int y, int width, int height, Color color) {
      new ScaledResolution(Minecraft.getMinecraft());
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getRed() / 255.0F, 1.0F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(image);
      Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, (float)width, (float)height);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
   }

   public static void doGlScissor(int x, int y, int width, int height) {
      Minecraft mc = Minecraft.getMinecraft();
      int scaleFactor = 1;
      int k = mc.gameSettings.guiScale;
      if(k == 0) {
         k = 1000;
      }

      while(scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
         ++scaleFactor;
      }

      GL11.glScissor(x * scaleFactor, mc.displayHeight - (y + height) * scaleFactor, width * scaleFactor, height * scaleFactor);
   }

   public static void drawRect(float x1, float y1, float x2, float y2, int color) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      color(color);
      GL11.glBegin(7);
      GL11.glVertex2d((double)x2, (double)y1);
      GL11.glVertex2d((double)x1, (double)y1);
      GL11.glVertex2d((double)x1, (double)y2);
      GL11.glVertex2d((double)x2, (double)y2);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void color(int color) {
      float f = (float)(color >> 24 & 255) / 255.0F;
      float f1 = (float)(color >> 16 & 255) / 255.0F;
      float f2 = (float)(color >> 8 & 255) / 255.0F;
      float f3 = (float)(color & 255) / 255.0F;
      GL11.glColor4f(f1, f2, f3, f);
   }

   public static int createShader(String shaderCode, int shaderType) throws Exception {
      byte shader = 0;

      int shader1;
      try {
         shader1 = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
         if(shader1 == 0) {
            return 0;
         }
      } catch (Exception var4) {
         ARBShaderObjects.glDeleteObjectARB(shader);
         throw var4;
      }

      ARBShaderObjects.glShaderSourceARB(shader1, shaderCode);
      ARBShaderObjects.glCompileShaderARB(shader1);
      if(ARBShaderObjects.glGetObjectParameteriARB(shader1, '\u8b81') == 0) {
         throw new RuntimeException("Error creating shader:");
      } else {
         return shader1;
      }
   }

   public void drawCircle(int x, int y, float radius, int color) {
      float alpha = (float)(color >> 24 & 255) / 255.0F;
      float red = (float)(color >> 16 & 255) / 255.0F;
      float green = (float)(color >> 8 & 255) / 255.0F;
      float blue = (float)(color & 255) / 255.0F;
      boolean blend = GL11.glIsEnabled(3042);
      boolean line = GL11.glIsEnabled(2848);
      boolean texture = GL11.glIsEnabled(3553);
      if(!blend) {
         GL11.glEnable(3042);
      }

      if(!line) {
         GL11.glEnable(2848);
      }

      if(texture) {
         GL11.glDisable(3553);
      }

      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(red, green, blue, alpha);
      GL11.glBegin(9);

      for(int i = 0; i <= 360; ++i) {
         GL11.glVertex2d((double)x + Math.sin((double)i * 3.141526D / 180.0D) * (double)radius, (double)y + Math.cos((double)i * 3.141526D / 180.0D) * (double)radius);
      }

      GL11.glEnd();
      if(texture) {
         GL11.glEnable(3553);
      }

      if(!line) {
         GL11.glDisable(2848);
      }

      if(!blend) {
         GL11.glDisable(3042);
      }

   }

   public static void renderOne(float width) {
      checkSetupFBO();
      GL11.glPushAttrib(1048575);
      GL11.glDisable(3008);
      GL11.glDisable(3553);
      GL11.glDisable(2896);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(width);
      GL11.glEnable(2848);
      GL11.glEnable(2960);
      GL11.glClear(1024);
      GL11.glClearStencil(15);
      GL11.glStencilFunc(512, 1, 15);
      GL11.glStencilOp(7681, 7681, 7681);
      GL11.glPolygonMode(1032, 6913);
   }

   public static void renderTwo() {
      GL11.glStencilFunc(512, 0, 15);
      GL11.glStencilOp(7681, 7681, 7681);
      GL11.glPolygonMode(1032, 6914);
   }

   public static void renderThree() {
      GL11.glStencilFunc(514, 1, 15);
      GL11.glStencilOp(7680, 7680, 7680);
      GL11.glPolygonMode(1032, 6913);
   }

   public static void renderFour() {
      setColor(new Color(255, 255, 255));
      GL11.glDepthMask(false);
      GL11.glDisable(2929);
      GL11.glEnable(10754);
      GL11.glPolygonOffset(1.0F, -2000000.0F);
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
   }

   public static void renderFive() {
      GL11.glPolygonOffset(1.0F, 2000000.0F);
      GL11.glDisable(10754);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(2960);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glEnable(3042);
      GL11.glEnable(2896);
      GL11.glEnable(3553);
      GL11.glEnable(3008);
      GL11.glPopAttrib();
   }

   public static void setColor(Color c) {
      GL11.glColor4d((double)((float)c.getRed() / 255.0F), (double)((float)c.getGreen() / 255.0F), (double)((float)c.getBlue() / 255.0F), (double)((float)c.getAlpha() / 255.0F));
   }

   public static void checkSetupFBO() {
      Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
      if(fbo != null && fbo.depthBuffer > -1) {
         setupFBO(fbo);
         fbo.depthBuffer = -1;
      }

   }

   public static void setupFBO(Framebuffer fbo) {
      EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
      int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
      EXTFramebufferObject.glBindRenderbufferEXT('\u8d41', stencil_depth_buffer_ID);
      EXTFramebufferObject.glRenderbufferStorageEXT('\u8d41', '\u84f9', Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
      EXTFramebufferObject.glFramebufferRenderbufferEXT('\u8d40', '\u8d20', '\u8d41', stencil_depth_buffer_ID);
      EXTFramebufferObject.glFramebufferRenderbufferEXT('\u8d40', '\u8d00', '\u8d41', stencil_depth_buffer_ID);
   }

   public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.begin(3, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(3, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(1, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      tessellator.draw();
   }

   public static void drawBoundingBox(AxisAlignedBB aa) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      tessellator.draw();
   }

   public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glLineWidth(lineWidth);
      GL11.glColor4f(red, green, blue, alpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(2896);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glLineWidth(lineWidth);
      GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2896);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue, float alpha) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawOutlinedEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawSolidEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glLineWidth(lineWdith);
      GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   private static void glColor(Color color) {
      GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
   }

   public static void drawFilledBox(AxisAlignedBB mask) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(mask.minX, mask.minY, mask.minZ).endVertex();
      worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
      tessellator.draw();
   }

   public static void drawRoundedRect(float x, float y, float x2, float y2, float round, int color) {
      x += (float)((double)(round / 2.0F) + 0.5D);
      y += (float)((double)(round / 2.0F) + 0.5D);
      x2 -= (float)((double)(round / 2.0F) + 0.5D);
      y2 -= (float)((double)(round / 2.0F) + 0.5D);
      Gui.drawRect((int)x, (int)y, (int)x2, (int)y2, color);
      circle(x2 - round / 2.0F, y + round / 2.0F, round, color);
      circle(x + round / 2.0F, y2 - round / 2.0F, round, color);
      circle(x + round / 2.0F, y + round / 2.0F, round, color);
      circle(x2 - round / 2.0F, y2 - round / 2.0F, round, color);
      Gui.drawRect((int)(x - round / 2.0F - 0.5F), (int)(y + round / 2.0F), (int)x2, (int)(y2 - round / 2.0F), color);
      Gui.drawRect((int)x, (int)(y + round / 2.0F), (int)(x2 + round / 2.0F + 0.5F), (int)(y2 - round / 2.0F), color);
      Gui.drawRect((int)(x + round / 2.0F), (int)(y - round / 2.0F - 0.5F), (int)(x2 - round / 2.0F), (int)(y2 - round / 2.0F), color);
      Gui.drawRect((int)(x + round / 2.0F), (int)y, (int)(x2 - round / 2.0F), (int)(y2 + round / 2.0F + 0.5F), color);
   }

   public static void circle(float x, float y, float radius, int fill) {
      arc(x, y, 0.0F, 360.0F, radius, fill);
   }

   public static void circle(float x, float y, float radius, Color fill) {
      arc(x, y, 0.0F, 360.0F, radius, fill);
   }

   public static void arc(float x, float y, float start, float end, float radius, int color) {
      arcEllipse(x, y, start, end, radius, radius, color);
   }

   public static void arc(float x, float y, float start, float end, float radius, Color color) {
      arcEllipse(x, y, start, end, radius, radius, color);
   }

   public static void arcEllipse(float x, float y, float start, float end, float w, float h, int color) {
      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
      float temp = 0.0F;
      if(start > end) {
         temp = end;
         end = start;
         start = temp;
      }

      float var11 = (float)(color >> 24 & 255) / 255.0F;
      float var12 = (float)(color >> 16 & 255) / 255.0F;
      float var13 = (float)(color >> 8 & 255) / 255.0F;
      float var14 = (float)(color & 255) / 255.0F;
      Tessellator var15 = Tessellator.getInstance();
      WorldRenderer var16 = var15.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(var12, var13, var14, var11);
      float i;
      float ldx;
      float ldy;
      if(var11 > 0.5F) {
         GL11.glEnable(2848);
         GL11.glLineWidth(2.0F);
         GL11.glBegin(3);

         for(i = end; i >= start; i -= 4.0F) {
            ldx = (float)Math.cos((double)i * 3.141592653589793D / 180.0D) * w * 1.001F;
            ldy = (float)Math.sin((double)i * 3.141592653589793D / 180.0D) * h * 1.001F;
            GL11.glVertex2f(x + ldx, y + ldy);
         }

         GL11.glEnd();
         GL11.glDisable(2848);
      }

      GL11.glBegin(6);

      for(i = end; i >= start; i -= 4.0F) {
         ldx = (float)Math.cos((double)i * 3.141592653589793D / 180.0D) * w;
         ldy = (float)Math.sin((double)i * 3.141592653589793D / 180.0D) * h;
         GL11.glVertex2f(x + ldx, y + ldy);
      }

      GL11.glEnd();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void arcEllipse(float x, float y, float start, float end, float w, float h, Color color) {
      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
      float temp = 0.0F;
      if(start > end) {
         temp = end;
         end = start;
         start = temp;
      }

      Tessellator var9 = Tessellator.getInstance();
      WorldRenderer var10 = var9.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
      float i;
      float ldx;
      float ldy;
      if((float)color.getAlpha() > 0.5F) {
         GL11.glEnable(2848);
         GL11.glLineWidth(2.0F);
         GL11.glBegin(3);

         for(i = end; i >= start; i -= 4.0F) {
            ldx = (float)Math.cos((double)i * 3.141592653589793D / 180.0D) * w * 1.001F;
            ldy = (float)Math.sin((double)i * 3.141592653589793D / 180.0D) * h * 1.001F;
            GL11.glVertex2f(x + ldx, y + ldy);
         }

         GL11.glEnd();
         GL11.glDisable(2848);
      }

      GL11.glBegin(6);

      for(i = end; i >= start; i -= 4.0F) {
         ldx = (float)Math.cos((double)i * 3.141592653589793D / 180.0D) * w;
         ldy = (float)Math.sin((double)i * 3.141592653589793D / 180.0D) * h;
         GL11.glVertex2f(x + ldx, y + ldy);
      }

      GL11.glEnd();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }
}
