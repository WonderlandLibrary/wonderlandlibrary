package shadersmod.client;

import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumWorldBlockLayer;
import optifine.Reflector;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ShadersRender {
   public static void setFrustrumPosition(Frustum var0, double var1, double var3, double var5) {
      var0.setPosition(var1, var3, var5);
   }

   public static void setupTerrain(RenderGlobal var0, Entity var1, double var2, ICamera var4, int var5, boolean var6) {
      var0.setupTerrain(var1, var2, var4, var5, var6);
   }

   public static void beginTerrainSolid() {
      if (Shaders.isRenderingWorld) {
         Shaders.fogEnabled = true;
         Shaders.useProgram(7);
      }

   }

   public static void beginTerrainCutoutMipped() {
      if (Shaders.isRenderingWorld) {
         Shaders.useProgram(7);
      }

   }

   public static void beginTerrainCutout() {
      if (Shaders.isRenderingWorld) {
         Shaders.useProgram(7);
      }

   }

   public static void endTerrain() {
      if (Shaders.isRenderingWorld) {
         Shaders.useProgram(3);
      }

   }

   public static void beginTranslucent() {
      if (Shaders.isRenderingWorld) {
         if (Shaders.usedDepthBuffers >= 2) {
            GlStateManager.setActiveTexture(33995);
            Shaders.checkGLError("pre copy depth");
            GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, Shaders.renderWidth, Shaders.renderHeight);
            Shaders.checkGLError("copy depth");
            GlStateManager.setActiveTexture(33984);
         }

         Shaders.useProgram(12);
      }

   }

   public static void endTranslucent() {
      if (Shaders.isRenderingWorld) {
         Shaders.useProgram(3);
      }

   }

   public static void renderHand0(EntityRenderer var0, float var1, int var2) {
      if (!Shaders.isShadowPass) {
         boolean var3 = Shaders.isItemToRenderMainTranslucent();
         if (!var3) {
            Shaders.readCenterDepth();
            Shaders.beginHand();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            var0.renderHand(var1, var2, true, false, false);
            Shaders.endHand();
            Shaders.setHandRenderedMain(true);
         }
      }

   }

   public static void renderHand1(EntityRenderer var0, float var1, int var2) {
      if (!Shaders.isShadowPass && !Shaders.isHandRenderedMain()) {
         Shaders.readCenterDepth();
         GlStateManager.enableBlend();
         Shaders.beginHand();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         var0.renderHand(var1, var2, true, false, true);
         Shaders.endHand();
         Shaders.setHandRenderedMain(true);
      }

   }

   public static void renderItemFP(ItemRenderer var0, float var1, boolean var2) {
      GlStateManager.depthMask(true);
      if (var2) {
         GlStateManager.depthFunc(519);
         GL11.glPushMatrix();
         IntBuffer var3 = Shaders.activeDrawBuffers;
         Shaders.setDrawBuffers(Shaders.drawBuffersNone);
         Shaders.renderItemKeepDepthMask = true;
         var0.renderItemInFirstPerson(var1);
         Shaders.renderItemKeepDepthMask = false;
         Shaders.setDrawBuffers(var3);
         GL11.glPopMatrix();
      }

      GlStateManager.depthFunc(515);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      var0.renderItemInFirstPerson(var1);
   }

   public static void renderFPOverlay(EntityRenderer var0, float var1, int var2) {
      if (!Shaders.isShadowPass) {
         Shaders.beginFPOverlay();
         var0.renderHand(var1, var2, false, true, false);
         Shaders.endFPOverlay();
      }

   }

   public static void beginBlockDamage() {
      if (Shaders.isRenderingWorld) {
         Shaders.useProgram(11);
         if (Shaders.programsID[11] == Shaders.programsID[7]) {
            Shaders.setDrawBuffers(Shaders.drawBuffersColorAtt0);
            GlStateManager.depthMask(false);
         }
      }

   }

   public static void endBlockDamage() {
      if (Shaders.isRenderingWorld) {
         GlStateManager.depthMask(true);
         Shaders.useProgram(3);
      }

   }

   public static void renderShadowMap(EntityRenderer var0, int var1, float var2, long var3) {
      if (Shaders.usedShadowDepthBuffers > 0 && --Shaders.shadowPassCounter <= 0) {
         Minecraft var5 = Minecraft.getMinecraft();
         var5.mcProfiler.endStartSection("shadow pass");
         RenderGlobal var6 = var5.renderGlobal;
         Shaders.isShadowPass = true;
         Shaders.shadowPassCounter = Shaders.shadowPassInterval;
         Shaders.preShadowPassThirdPersonView = var5.gameSettings.thirdPersonView;
         var5.gameSettings.thirdPersonView = 1;
         Shaders.checkGLError("pre shadow");
         GL11.glMatrixMode(5889);
         GL11.glPushMatrix();
         GL11.glMatrixMode(5888);
         GL11.glPushMatrix();
         var5.mcProfiler.endStartSection("shadow clear");
         EXTFramebufferObject.glBindFramebufferEXT(36160, Shaders.sfb);
         Shaders.checkGLError("shadow bind sfb");
         Shaders.useProgram(30);
         var5.mcProfiler.endStartSection("shadow camera");
         var0.setupCameraTransform(var2, 2);
         Shaders.setCameraShadow(var2);
         ActiveRenderInfo.updateRenderInfo(Minecraft.thePlayer, var5.gameSettings.thirdPersonView == 2);
         Shaders.checkGLError("shadow camera");
         GL20.glDrawBuffers(Shaders.sfbDrawBuffers);
         Shaders.checkGLError("shadow drawbuffers");
         GL11.glReadBuffer(0);
         Shaders.checkGLError("shadow readbuffer");
         EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, Shaders.sfbDepthTextures.get(0), 0);
         if (Shaders.usedShadowColorBuffers != 0) {
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, Shaders.sfbColorTextures.get(0), 0);
         }

         Shaders.checkFramebufferStatus("shadow fb");
         GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glClear(Shaders.usedShadowColorBuffers != 0 ? 16640 : 256);
         Shaders.checkGLError("shadow clear");
         var5.mcProfiler.endStartSection("shadow frustum");
         ClippingHelper var7 = ClippingHelperShadow.getInstance();
         var5.mcProfiler.endStartSection("shadow culling");
         Frustum var8 = new Frustum(var7);
         Entity var9 = var5.getRenderViewEntity();
         double var10 = var9.lastTickPosX + (var9.posX - var9.lastTickPosX) * (double)var2;
         double var12 = var9.lastTickPosY + (var9.posY - var9.lastTickPosY) * (double)var2;
         double var14 = var9.lastTickPosZ + (var9.posZ - var9.lastTickPosZ) * (double)var2;
         var8.setPosition(var10, var12, var14);
         GlStateManager.shadeModel(7425);
         GlStateManager.enableDepth();
         GlStateManager.depthFunc(515);
         GlStateManager.depthMask(true);
         GlStateManager.colorMask(true, true, true, true);
         GlStateManager.disableCull();
         var5.mcProfiler.endStartSection("shadow prepareterrain");
         var5.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
         var5.mcProfiler.endStartSection("shadow setupterrain");
         boolean var16 = false;
         int var17 = var0.frameCount++;
         var6.setupTerrain(var9, (double)var2, var8, var17, Minecraft.thePlayer.isSpectator());
         var5.mcProfiler.endStartSection("shadow updatechunks");
         var5.mcProfiler.endStartSection("shadow terrain");
         GlStateManager.matrixMode(5888);
         GlStateManager.pushMatrix();
         GlStateManager.disableAlpha();
         var6.renderBlockLayer(EnumWorldBlockLayer.SOLID, (double)var2, 2, var9);
         Shaders.checkGLError("shadow terrain solid");
         GlStateManager.enableAlpha();
         var6.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double)var2, 2, var9);
         Shaders.checkGLError("shadow terrain cutoutmipped");
         var5.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
         var6.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, (double)var2, 2, var9);
         Shaders.checkGLError("shadow terrain cutout");
         var5.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
         GlStateManager.shadeModel(7424);
         GlStateManager.alphaFunc(516, 0.1F);
         GlStateManager.matrixMode(5888);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         var5.mcProfiler.endStartSection("shadow entities");
         if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, 0);
         }

         var6.renderEntities(var9, var8, var2);
         Shaders.checkGLError("shadow entities");
         GlStateManager.matrixMode(5888);
         GlStateManager.popMatrix();
         GlStateManager.depthMask(true);
         GlStateManager.disableBlend();
         GlStateManager.enableCull();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.alphaFunc(516, 0.1F);
         if (Shaders.usedShadowDepthBuffers >= 2) {
            GlStateManager.setActiveTexture(33989);
            Shaders.checkGLError("pre copy shadow depth");
            GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, Shaders.shadowMapWidth, Shaders.shadowMapHeight);
            Shaders.checkGLError("copy shadow depth");
            GlStateManager.setActiveTexture(33984);
         }

         GlStateManager.disableBlend();
         GlStateManager.depthMask(true);
         var5.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
         GlStateManager.shadeModel(7425);
         Shaders.checkGLError("shadow pre-translucent");
         GL20.glDrawBuffers(Shaders.sfbDrawBuffers);
         Shaders.checkGLError("shadow drawbuffers pre-translucent");
         Shaders.checkFramebufferStatus("shadow pre-translucent");
         if (Shaders.isRenderShadowTranslucent()) {
            var5.mcProfiler.endStartSection("shadow translucent");
            var6.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double)var2, 2, var9);
            Shaders.checkGLError("shadow translucent");
         }

         if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
            RenderHelper.enableStandardItemLighting();
            Reflector.call(Reflector.ForgeHooksClient_setRenderPass, 1);
            var6.renderEntities(var9, var8, var2);
            Reflector.call(Reflector.ForgeHooksClient_setRenderPass, -1);
            RenderHelper.disableStandardItemLighting();
            Shaders.checkGLError("shadow entities 1");
         }

         GlStateManager.shadeModel(7424);
         GlStateManager.depthMask(true);
         GlStateManager.enableCull();
         GlStateManager.disableBlend();
         GL11.glFlush();
         Shaders.checkGLError("shadow flush");
         Shaders.isShadowPass = false;
         var5.gameSettings.thirdPersonView = Shaders.preShadowPassThirdPersonView;
         var5.mcProfiler.endStartSection("shadow postprocess");
         if (Shaders.hasGlGenMipmap) {
            if (Shaders.usedShadowDepthBuffers >= 1) {
               if (Shaders.shadowMipmapEnabled[0]) {
                  GlStateManager.setActiveTexture(33988);
                  GlStateManager.bindTexture(Shaders.sfbDepthTextures.get(0));
                  GL30.glGenerateMipmap(3553);
                  GL11.glTexParameteri(3553, 10241, Shaders.shadowFilterNearest[0] ? 9984 : 9987);
               }

               if (Shaders.usedShadowDepthBuffers >= 2 && Shaders.shadowMipmapEnabled[1]) {
                  GlStateManager.setActiveTexture(33989);
                  GlStateManager.bindTexture(Shaders.sfbDepthTextures.get(1));
                  GL30.glGenerateMipmap(3553);
                  GL11.glTexParameteri(3553, 10241, Shaders.shadowFilterNearest[1] ? 9984 : 9987);
               }

               GlStateManager.setActiveTexture(33984);
            }

            if (Shaders.usedShadowColorBuffers >= 1) {
               if (Shaders.shadowColorMipmapEnabled[0]) {
                  GlStateManager.setActiveTexture(33997);
                  GlStateManager.bindTexture(Shaders.sfbColorTextures.get(0));
                  GL30.glGenerateMipmap(3553);
                  GL11.glTexParameteri(3553, 10241, Shaders.shadowColorFilterNearest[0] ? 9984 : 9987);
               }

               if (Shaders.usedShadowColorBuffers >= 2 && Shaders.shadowColorMipmapEnabled[1]) {
                  GlStateManager.setActiveTexture(33998);
                  GlStateManager.bindTexture(Shaders.sfbColorTextures.get(1));
                  GL30.glGenerateMipmap(3553);
                  GL11.glTexParameteri(3553, 10241, Shaders.shadowColorFilterNearest[1] ? 9984 : 9987);
               }

               GlStateManager.setActiveTexture(33984);
            }
         }

         Shaders.checkGLError("shadow postprocess");
         EXTFramebufferObject.glBindFramebufferEXT(36160, Shaders.dfb);
         GL11.glViewport(0, 0, Shaders.renderWidth, Shaders.renderHeight);
         Shaders.activeDrawBuffers = null;
         var5.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
         Shaders.useProgram(7);
         GL11.glMatrixMode(5888);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5889);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
         Shaders.checkGLError("shadow end");
      }

   }

   public static void preRenderChunkLayer(EnumWorldBlockLayer var0) {
      if (Shaders.isRenderBackFace(var0)) {
         GlStateManager.disableCull();
      }

      if (OpenGlHelper.useVbo()) {
         GL11.glEnableClientState(32885);
         GL20.glEnableVertexAttribArray(Shaders.midTexCoordAttrib);
         GL20.glEnableVertexAttribArray(Shaders.tangentAttrib);
         GL20.glEnableVertexAttribArray(Shaders.entityAttrib);
      }

   }

   public static void postRenderChunkLayer(EnumWorldBlockLayer var0) {
      if (OpenGlHelper.useVbo()) {
         GL11.glDisableClientState(32885);
         GL20.glDisableVertexAttribArray(Shaders.midTexCoordAttrib);
         GL20.glDisableVertexAttribArray(Shaders.tangentAttrib);
         GL20.glDisableVertexAttribArray(Shaders.entityAttrib);
      }

      if (Shaders.isRenderBackFace(var0)) {
         GlStateManager.enableCull();
      }

   }

   public static void setupArrayPointersVbo() {
      boolean var0 = true;
      GL11.glVertexPointer(3, 5126, 56, 0L);
      GL11.glColorPointer(4, 5121, 56, 12L);
      GL11.glTexCoordPointer(2, 5126, 56, 16L);
      OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
      GL11.glTexCoordPointer(2, 5122, 56, 24L);
      OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
      GL11.glNormalPointer(5120, 56, 28L);
      GL20.glVertexAttribPointer(Shaders.midTexCoordAttrib, 2, 5126, false, 56, 32L);
      GL20.glVertexAttribPointer(Shaders.tangentAttrib, 4, 5122, false, 56, 40L);
      GL20.glVertexAttribPointer(Shaders.entityAttrib, 3, 5122, false, 56, 48L);
   }

   public static void beaconBeamBegin() {
      Shaders.useProgram(14);
   }

   public static void beaconBeamStartQuad1() {
   }

   public static void beaconBeamStartQuad2() {
   }

   public static void beaconBeamDraw1() {
   }

   public static void beaconBeamDraw2() {
      GlStateManager.disableBlend();
   }

   public static void renderEnchantedGlintBegin() {
      Shaders.useProgram(17);
   }

   public static void renderEnchantedGlintEnd() {
      if (Shaders.isRenderingWorld) {
         if (Shaders.isRenderBothHands()) {
            Shaders.useProgram(19);
         } else {
            Shaders.useProgram(16);
         }
      } else {
         Shaders.useProgram(0);
      }

   }
}
