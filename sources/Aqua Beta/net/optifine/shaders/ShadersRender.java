package net.optifine.shaders;

import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.shaders.ClippingHelperShadow;
import net.optifine.shaders.Program;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ShadersRender {
    private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");

    public static void setFrustrumPosition(ICamera frustum, double x, double y, double z) {
        frustum.setPosition(x, y, z);
    }

    public static void setupTerrain(RenderGlobal renderGlobal, Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator) {
        renderGlobal.setupTerrain(viewEntity, partialTicks, camera, frameCount, playerSpectator);
    }

    public static void beginTerrainSolid() {
        if (Shaders.isRenderingWorld) {
            Shaders.fogEnabled = true;
            Shaders.useProgram((Program)Shaders.ProgramTerrain);
        }
    }

    public static void beginTerrainCutoutMipped() {
        if (Shaders.isRenderingWorld) {
            Shaders.useProgram((Program)Shaders.ProgramTerrain);
        }
    }

    public static void beginTerrainCutout() {
        if (Shaders.isRenderingWorld) {
            Shaders.useProgram((Program)Shaders.ProgramTerrain);
        }
    }

    public static void endTerrain() {
        if (Shaders.isRenderingWorld) {
            Shaders.useProgram((Program)Shaders.ProgramTexturedLit);
        }
    }

    public static void beginTranslucent() {
        if (Shaders.isRenderingWorld) {
            if (Shaders.usedDepthBuffers >= 2) {
                GlStateManager.setActiveTexture((int)33995);
                Shaders.checkGLError((String)"pre copy depth");
                GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)Shaders.renderWidth, (int)Shaders.renderHeight);
                Shaders.checkGLError((String)"copy depth");
                GlStateManager.setActiveTexture((int)33984);
            }
            Shaders.useProgram((Program)Shaders.ProgramWater);
        }
    }

    public static void endTranslucent() {
        if (Shaders.isRenderingWorld) {
            Shaders.useProgram((Program)Shaders.ProgramTexturedLit);
        }
    }

    public static void renderHand0(EntityRenderer er, float par1, int par2) {
        if (!Shaders.isShadowPass) {
            boolean flag = Shaders.isItemToRenderMainTranslucent();
            boolean flag1 = Shaders.isItemToRenderOffTranslucent();
            if (!flag || !flag1) {
                Shaders.readCenterDepth();
                Shaders.beginHand((boolean)false);
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                Shaders.setSkipRenderHands((boolean)flag, (boolean)flag1);
                er.renderHand(par1, par2, true, false, false);
                Shaders.endHand();
                Shaders.setHandsRendered((!flag ? 1 : 0) != 0, (!flag1 ? 1 : 0) != 0);
                Shaders.setSkipRenderHands((boolean)false, (boolean)false);
            }
        }
    }

    public static void renderHand1(EntityRenderer er, float par1, int par2) {
        if (!Shaders.isShadowPass && !Shaders.isBothHandsRendered()) {
            Shaders.readCenterDepth();
            GlStateManager.enableBlend();
            Shaders.beginHand((boolean)true);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Shaders.setSkipRenderHands((boolean)Shaders.isHandRenderedMain(), (boolean)Shaders.isHandRenderedOff());
            er.renderHand(par1, par2, true, false, true);
            Shaders.endHand();
            Shaders.setHandsRendered((boolean)true, (boolean)true);
            Shaders.setSkipRenderHands((boolean)false, (boolean)false);
        }
    }

    public static void renderItemFP(ItemRenderer itemRenderer, float par1, boolean renderTranslucent) {
        Shaders.setRenderingFirstPersonHand((boolean)true);
        GlStateManager.depthMask((boolean)true);
        if (renderTranslucent) {
            GlStateManager.depthFunc((int)519);
            GL11.glPushMatrix();
            IntBuffer intbuffer = Shaders.activeDrawBuffers;
            Shaders.setDrawBuffers((IntBuffer)Shaders.drawBuffersNone);
            Shaders.renderItemKeepDepthMask = true;
            itemRenderer.renderItemInFirstPerson(par1);
            Shaders.renderItemKeepDepthMask = false;
            Shaders.setDrawBuffers((IntBuffer)intbuffer);
            GL11.glPopMatrix();
        }
        GlStateManager.depthFunc((int)515);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        itemRenderer.renderItemInFirstPerson(par1);
        Shaders.setRenderingFirstPersonHand((boolean)false);
    }

    public static void renderFPOverlay(EntityRenderer er, float par1, int par2) {
        if (!Shaders.isShadowPass) {
            Shaders.beginFPOverlay();
            er.renderHand(par1, par2, false, true, false);
            Shaders.endFPOverlay();
        }
    }

    public static void beginBlockDamage() {
        if (Shaders.isRenderingWorld) {
            Shaders.useProgram((Program)Shaders.ProgramDamagedBlock);
            if (Shaders.ProgramDamagedBlock.getId() == Shaders.ProgramTerrain.getId()) {
                Shaders.setDrawBuffers((IntBuffer)Shaders.drawBuffersColorAtt0);
                GlStateManager.depthMask((boolean)false);
            }
        }
    }

    public static void endBlockDamage() {
        if (Shaders.isRenderingWorld) {
            GlStateManager.depthMask((boolean)true);
            Shaders.useProgram((Program)Shaders.ProgramTexturedLit);
        }
    }

    public static void renderShadowMap(EntityRenderer entityRenderer, int pass, float partialTicks, long finishTimeNano) {
        if (Shaders.usedShadowDepthBuffers > 0 && --Shaders.shadowPassCounter <= 0) {
            Minecraft minecraft = Minecraft.getMinecraft();
            minecraft.mcProfiler.endStartSection("shadow pass");
            RenderGlobal renderglobal = minecraft.renderGlobal;
            Shaders.isShadowPass = true;
            Shaders.shadowPassCounter = Shaders.shadowPassInterval;
            Shaders.preShadowPassThirdPersonView = minecraft.gameSettings.thirdPersonView;
            minecraft.gameSettings.thirdPersonView = 1;
            Shaders.checkGLError((String)"pre shadow");
            GL11.glMatrixMode((int)5889);
            GL11.glPushMatrix();
            GL11.glMatrixMode((int)5888);
            GL11.glPushMatrix();
            minecraft.mcProfiler.endStartSection("shadow clear");
            EXTFramebufferObject.glBindFramebufferEXT((int)36160, (int)Shaders.sfb);
            Shaders.checkGLError((String)"shadow bind sfb");
            minecraft.mcProfiler.endStartSection("shadow camera");
            entityRenderer.setupCameraTransform(partialTicks, 2);
            Shaders.setCameraShadow((float)partialTicks);
            Shaders.checkGLError((String)"shadow camera");
            Shaders.useProgram((Program)Shaders.ProgramShadow);
            GL20.glDrawBuffers((IntBuffer)Shaders.sfbDrawBuffers);
            Shaders.checkGLError((String)"shadow drawbuffers");
            GL11.glReadBuffer((int)0);
            Shaders.checkGLError((String)"shadow readbuffer");
            EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)36096, (int)3553, (int)Shaders.sfbDepthTextures.get(0), (int)0);
            if (Shaders.usedShadowColorBuffers != 0) {
                EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)36064, (int)3553, (int)Shaders.sfbColorTextures.get(0), (int)0);
            }
            Shaders.checkFramebufferStatus((String)"shadow fb");
            GL11.glClearColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glClear((int)(Shaders.usedShadowColorBuffers != 0 ? 16640 : 256));
            Shaders.checkGLError((String)"shadow clear");
            minecraft.mcProfiler.endStartSection("shadow frustum");
            ClippingHelper clippinghelper = ClippingHelperShadow.getInstance();
            minecraft.mcProfiler.endStartSection("shadow culling");
            Frustum frustum = new Frustum(clippinghelper);
            Entity entity = minecraft.getRenderViewEntity();
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
            frustum.setPosition(d0, d1, d2);
            GlStateManager.shadeModel((int)7425);
            GlStateManager.enableDepth();
            GlStateManager.depthFunc((int)515);
            GlStateManager.depthMask((boolean)true);
            GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
            GlStateManager.disableCull();
            minecraft.mcProfiler.endStartSection("shadow prepareterrain");
            minecraft.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            minecraft.mcProfiler.endStartSection("shadow setupterrain");
            int i = 0;
            i = entityRenderer.frameCount;
            entityRenderer.frameCount = i + 1;
            renderglobal.setupTerrain(entity, (double)partialTicks, (ICamera)frustum, i, minecraft.thePlayer.isSpectator());
            minecraft.mcProfiler.endStartSection("shadow updatechunks");
            minecraft.mcProfiler.endStartSection("shadow terrain");
            GlStateManager.matrixMode((int)5888);
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            renderglobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, (double)partialTicks, 2, entity);
            Shaders.checkGLError((String)"shadow terrain solid");
            GlStateManager.enableAlpha();
            renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double)partialTicks, 2, entity);
            Shaders.checkGLError((String)"shadow terrain cutoutmipped");
            minecraft.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
            renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, (double)partialTicks, 2, entity);
            Shaders.checkGLError((String)"shadow terrain cutout");
            minecraft.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
            GlStateManager.shadeModel((int)7424);
            GlStateManager.alphaFunc((int)516, (float)0.1f);
            GlStateManager.matrixMode((int)5888);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            minecraft.mcProfiler.endStartSection("shadow entities");
            if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                Reflector.callVoid((ReflectorMethod)Reflector.ForgeHooksClient_setRenderPass, (Object[])new Object[]{0});
            }
            renderglobal.renderEntities(entity, (ICamera)frustum, partialTicks);
            Shaders.checkGLError((String)"shadow entities");
            GlStateManager.matrixMode((int)5888);
            GlStateManager.popMatrix();
            GlStateManager.depthMask((boolean)true);
            GlStateManager.disableBlend();
            GlStateManager.enableCull();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
            GlStateManager.alphaFunc((int)516, (float)0.1f);
            if (Shaders.usedShadowDepthBuffers >= 2) {
                GlStateManager.setActiveTexture((int)33989);
                Shaders.checkGLError((String)"pre copy shadow depth");
                GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)Shaders.shadowMapWidth, (int)Shaders.shadowMapHeight);
                Shaders.checkGLError((String)"copy shadow depth");
                GlStateManager.setActiveTexture((int)33984);
            }
            GlStateManager.disableBlend();
            GlStateManager.depthMask((boolean)true);
            minecraft.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            GlStateManager.shadeModel((int)7425);
            Shaders.checkGLError((String)"shadow pre-translucent");
            GL20.glDrawBuffers((IntBuffer)Shaders.sfbDrawBuffers);
            Shaders.checkGLError((String)"shadow drawbuffers pre-translucent");
            Shaders.checkFramebufferStatus((String)"shadow pre-translucent");
            if (Shaders.isRenderShadowTranslucent()) {
                minecraft.mcProfiler.endStartSection("shadow translucent");
                renderglobal.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double)partialTicks, 2, entity);
                Shaders.checkGLError((String)"shadow translucent");
            }
            if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                RenderHelper.enableStandardItemLighting();
                Reflector.call((ReflectorMethod)Reflector.ForgeHooksClient_setRenderPass, (Object[])new Object[]{1});
                renderglobal.renderEntities(entity, (ICamera)frustum, partialTicks);
                Reflector.call((ReflectorMethod)Reflector.ForgeHooksClient_setRenderPass, (Object[])new Object[]{-1});
                RenderHelper.disableStandardItemLighting();
                Shaders.checkGLError((String)"shadow entities 1");
            }
            GlStateManager.shadeModel((int)7424);
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GL11.glFlush();
            Shaders.checkGLError((String)"shadow flush");
            Shaders.isShadowPass = false;
            minecraft.gameSettings.thirdPersonView = Shaders.preShadowPassThirdPersonView;
            minecraft.mcProfiler.endStartSection("shadow postprocess");
            if (Shaders.hasGlGenMipmap) {
                if (Shaders.usedShadowDepthBuffers >= 1) {
                    if (Shaders.shadowMipmapEnabled[0]) {
                        GlStateManager.setActiveTexture((int)33988);
                        GlStateManager.bindTexture((int)Shaders.sfbDepthTextures.get(0));
                        GL30.glGenerateMipmap((int)3553);
                        GL11.glTexParameteri((int)3553, (int)10241, (int)(Shaders.shadowFilterNearest[0] ? 9984 : 9987));
                    }
                    if (Shaders.usedShadowDepthBuffers >= 2 && Shaders.shadowMipmapEnabled[1]) {
                        GlStateManager.setActiveTexture((int)33989);
                        GlStateManager.bindTexture((int)Shaders.sfbDepthTextures.get(1));
                        GL30.glGenerateMipmap((int)3553);
                        GL11.glTexParameteri((int)3553, (int)10241, (int)(Shaders.shadowFilterNearest[1] ? 9984 : 9987));
                    }
                    GlStateManager.setActiveTexture((int)33984);
                }
                if (Shaders.usedShadowColorBuffers >= 1) {
                    if (Shaders.shadowColorMipmapEnabled[0]) {
                        GlStateManager.setActiveTexture((int)33997);
                        GlStateManager.bindTexture((int)Shaders.sfbColorTextures.get(0));
                        GL30.glGenerateMipmap((int)3553);
                        GL11.glTexParameteri((int)3553, (int)10241, (int)(Shaders.shadowColorFilterNearest[0] ? 9984 : 9987));
                    }
                    if (Shaders.usedShadowColorBuffers >= 2 && Shaders.shadowColorMipmapEnabled[1]) {
                        GlStateManager.setActiveTexture((int)33998);
                        GlStateManager.bindTexture((int)Shaders.sfbColorTextures.get(1));
                        GL30.glGenerateMipmap((int)3553);
                        GL11.glTexParameteri((int)3553, (int)10241, (int)(Shaders.shadowColorFilterNearest[1] ? 9984 : 9987));
                    }
                    GlStateManager.setActiveTexture((int)33984);
                }
            }
            Shaders.checkGLError((String)"shadow postprocess");
            EXTFramebufferObject.glBindFramebufferEXT((int)36160, (int)Shaders.dfb);
            GL11.glViewport((int)0, (int)0, (int)Shaders.renderWidth, (int)Shaders.renderHeight);
            Shaders.activeDrawBuffers = null;
            minecraft.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            Shaders.useProgram((Program)Shaders.ProgramTerrain);
            GL11.glMatrixMode((int)5888);
            GL11.glPopMatrix();
            GL11.glMatrixMode((int)5889);
            GL11.glPopMatrix();
            GL11.glMatrixMode((int)5888);
            Shaders.checkGLError((String)"shadow end");
        }
    }

    public static void preRenderChunkLayer(EnumWorldBlockLayer blockLayerIn) {
        if (Shaders.isRenderBackFace((EnumWorldBlockLayer)blockLayerIn)) {
            GlStateManager.disableCull();
        }
        if (OpenGlHelper.useVbo()) {
            GL11.glEnableClientState((int)32885);
            GL20.glEnableVertexAttribArray((int)Shaders.midTexCoordAttrib);
            GL20.glEnableVertexAttribArray((int)Shaders.tangentAttrib);
            GL20.glEnableVertexAttribArray((int)Shaders.entityAttrib);
        }
    }

    public static void postRenderChunkLayer(EnumWorldBlockLayer blockLayerIn) {
        if (OpenGlHelper.useVbo()) {
            GL11.glDisableClientState((int)32885);
            GL20.glDisableVertexAttribArray((int)Shaders.midTexCoordAttrib);
            GL20.glDisableVertexAttribArray((int)Shaders.tangentAttrib);
            GL20.glDisableVertexAttribArray((int)Shaders.entityAttrib);
        }
        if (Shaders.isRenderBackFace((EnumWorldBlockLayer)blockLayerIn)) {
            GlStateManager.enableCull();
        }
    }

    public static void setupArrayPointersVbo() {
        int i = 14;
        GL11.glVertexPointer((int)3, (int)5126, (int)56, (long)0L);
        GL11.glColorPointer((int)4, (int)5121, (int)56, (long)12L);
        GL11.glTexCoordPointer((int)2, (int)5126, (int)56, (long)16L);
        OpenGlHelper.setClientActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GL11.glTexCoordPointer((int)2, (int)5122, (int)56, (long)24L);
        OpenGlHelper.setClientActiveTexture((int)OpenGlHelper.defaultTexUnit);
        GL11.glNormalPointer((int)5120, (int)56, (long)28L);
        GL20.glVertexAttribPointer((int)Shaders.midTexCoordAttrib, (int)2, (int)5126, (boolean)false, (int)56, (long)32L);
        GL20.glVertexAttribPointer((int)Shaders.tangentAttrib, (int)4, (int)5122, (boolean)false, (int)56, (long)40L);
        GL20.glVertexAttribPointer((int)Shaders.entityAttrib, (int)3, (int)5122, (boolean)false, (int)56, (long)48L);
    }

    public static void beaconBeamBegin() {
        Shaders.useProgram((Program)Shaders.ProgramBeaconBeam);
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
        Shaders.useProgram((Program)Shaders.ProgramArmorGlint);
    }

    public static void renderEnchantedGlintEnd() {
        if (Shaders.isRenderingWorld) {
            if (Shaders.isRenderingFirstPersonHand() && Shaders.isRenderBothHands()) {
                Shaders.useProgram((Program)Shaders.ProgramHand);
            } else {
                Shaders.useProgram((Program)Shaders.ProgramEntities);
            }
        } else {
            Shaders.useProgram((Program)Shaders.ProgramNone);
        }
    }

    public static boolean renderEndPortal(TileEntityEndPortal te, double x, double y, double z, float partialTicks, int destroyStage, float offset) {
        if (!Shaders.isShadowPass && Shaders.activeProgram.getId() == 0) {
            return false;
        }
        GlStateManager.disableLighting();
        Config.getTextureManager().bindTexture(END_PORTAL_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
        float f = 0.5f;
        float f1 = f * 0.15f;
        float f2 = f * 0.3f;
        float f3 = f * 0.4f;
        float f4 = 0.0f;
        float f5 = 0.2f;
        float f6 = (float)(System.currentTimeMillis() % 100000L) / 100000.0f;
        int i = 240;
        worldrenderer.pos(x, y + (double)offset, z + 1.0).color(f1, f2, f3, 1.0f).tex((double)(f4 + f6), (double)(f4 + f6)).lightmap(i, i).endVertex();
        worldrenderer.pos(x + 1.0, y + (double)offset, z + 1.0).color(f1, f2, f3, 1.0f).tex((double)(f4 + f6), (double)(f5 + f6)).lightmap(i, i).endVertex();
        worldrenderer.pos(x + 1.0, y + (double)offset, z).color(f1, f2, f3, 1.0f).tex((double)(f5 + f6), (double)(f5 + f6)).lightmap(i, i).endVertex();
        worldrenderer.pos(x, y + (double)offset, z).color(f1, f2, f3, 1.0f).tex((double)(f5 + f6), (double)(f4 + f6)).lightmap(i, i).endVertex();
        tessellator.draw();
        GlStateManager.enableLighting();
        return true;
    }
}
