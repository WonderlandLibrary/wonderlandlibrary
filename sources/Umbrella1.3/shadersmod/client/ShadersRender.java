/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.EXTFramebufferObject
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL30
 */
package shadersmod.client;

import java.nio.IntBuffer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumWorldBlockLayer;
import optifine.Reflector;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shadersmod.client.ClippingHelperShadow;
import shadersmod.client.Shaders;

public class ShadersRender {
    public static void setFrustrumPosition(Frustrum frustrum, double x, double y, double z) {
        frustrum.setPosition(x, y, z);
    }

    public static void setupTerrain(RenderGlobal renderGlobal, Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator) {
        renderGlobal.func_174970_a(viewEntity, partialTicks, camera, frameCount, playerSpectator);
    }

    public static void updateChunks(RenderGlobal renderGlobal, long finishTimeNano) {
        renderGlobal.func_174967_a(finishTimeNano);
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
                GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)Shaders.renderWidth, (int)Shaders.renderHeight);
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

    public static void renderHand0(EntityRenderer er, float par1, int par2) {
        if (!Shaders.isShadowPass) {
            Block block;
            Item item = Shaders.itemToRender != null ? Shaders.itemToRender.getItem() : null;
            Block block2 = block = item instanceof ItemBlock ? ((ItemBlock)item).getBlock() : null;
            if (!(item instanceof ItemBlock) || !(block instanceof Block) || block.getBlockLayer() == EnumWorldBlockLayer.SOLID) {
                Shaders.readCenterDepth();
                Shaders.beginHand();
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                er.renderHand(par1, par2);
                Shaders.endHand();
                Shaders.isHandRendered = true;
            }
        }
    }

    public static void renderHand1(EntityRenderer er, float par1, int par2) {
        if (!Shaders.isShadowPass && !Shaders.isHandRendered) {
            Shaders.readCenterDepth();
            GlStateManager.enableBlend();
            Shaders.beginHand();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            er.renderHand(par1, par2);
            Shaders.endHand();
            Shaders.isHandRendered = true;
        }
    }

    public static void renderItemFP(ItemRenderer itemRenderer, float par1) {
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        itemRenderer.renderItemInFirstPerson(par1);
    }

    public static void renderFPOverlay(EntityRenderer er, float par1, int par2) {
        if (!Shaders.isShadowPass) {
            Shaders.beginFPOverlay();
            er.renderHand(par1, par2);
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

    public static void renderShadowMap(EntityRenderer entityRenderer, int pass, float partialTicks, long finishTimeNano) {
        if (Shaders.usedShadowDepthBuffers > 0 && --Shaders.shadowPassCounter <= 0) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.mcProfiler.endStartSection("shadow pass");
            RenderGlobal renderGlobal = mc.renderGlobal;
            Shaders.isShadowPass = true;
            Shaders.shadowPassCounter = Shaders.shadowPassInterval;
            Shaders.preShadowPassThirdPersonView = mc.gameSettings.thirdPersonView;
            mc.gameSettings.thirdPersonView = 1;
            Shaders.checkGLError("pre shadow");
            GL11.glMatrixMode((int)5889);
            GL11.glPushMatrix();
            GL11.glMatrixMode((int)5888);
            GL11.glPushMatrix();
            mc.mcProfiler.endStartSection("shadow clear");
            EXTFramebufferObject.glBindFramebufferEXT((int)36160, (int)Shaders.sfb);
            Shaders.checkGLError("shadow bind sfb");
            Shaders.useProgram(30);
            mc.mcProfiler.endStartSection("shadow camera");
            entityRenderer.setupCameraTransform(partialTicks, 2);
            Shaders.setCameraShadow(partialTicks);
            ActiveRenderInfo.updateRenderInfo(mc.thePlayer, mc.gameSettings.thirdPersonView == 2);
            Shaders.checkGLError("shadow camera");
            GL20.glDrawBuffers((IntBuffer)Shaders.sfbDrawBuffers);
            Shaders.checkGLError("shadow drawbuffers");
            GL11.glReadBuffer((int)0);
            Shaders.checkGLError("shadow readbuffer");
            EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)36096, (int)3553, (int)Shaders.sfbDepthTextures.get(0), (int)0);
            if (Shaders.usedShadowColorBuffers != 0) {
                EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)36064, (int)3553, (int)Shaders.sfbColorTextures.get(0), (int)0);
            }
            Shaders.checkFramebufferStatus("shadow fb");
            GL11.glClearColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glClear((int)(Shaders.usedShadowColorBuffers != 0 ? 16640 : 256));
            Shaders.checkGLError("shadow clear");
            mc.mcProfiler.endStartSection("shadow frustum");
            ClippingHelper clippingHelper = ClippingHelperShadow.getInstance();
            mc.mcProfiler.endStartSection("shadow culling");
            Frustrum frustum = new Frustrum(clippingHelper);
            Entity viewEntity = mc.func_175606_aa();
            double viewPosX = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * (double)partialTicks;
            double viewPosY = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * (double)partialTicks;
            double viewPosZ = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * (double)partialTicks;
            frustum.setPosition(viewPosX, viewPosY, viewPosZ);
            GlStateManager.shadeModel(7425);
            GlStateManager.enableDepth();
            GlStateManager.depthFunc(515);
            GlStateManager.depthMask(true);
            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.disableCull();
            mc.mcProfiler.endStartSection("shadow prepareterrain");
            mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            mc.mcProfiler.endStartSection("shadow setupterrain");
            boolean frameCount = false;
            int var17 = entityRenderer.field_175084_ae;
            entityRenderer.field_175084_ae = var17 + 1;
            renderGlobal.func_174970_a(viewEntity, partialTicks, frustum, var17, mc.thePlayer.func_175149_v());
            mc.mcProfiler.endStartSection("shadow updatechunks");
            mc.mcProfiler.endStartSection("shadow terrain");
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            renderGlobal.func_174977_a(EnumWorldBlockLayer.SOLID, partialTicks, 2, viewEntity);
            Shaders.checkGLError("shadow terrain solid");
            GlStateManager.enableAlpha();
            renderGlobal.func_174977_a(EnumWorldBlockLayer.CUTOUT_MIPPED, partialTicks, 2, viewEntity);
            Shaders.checkGLError("shadow terrain cutoutmipped");
            mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).func_174936_b(false, false);
            renderGlobal.func_174977_a(EnumWorldBlockLayer.CUTOUT, partialTicks, 2, viewEntity);
            Shaders.checkGLError("shadow terrain cutout");
            mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).func_174935_a();
            GlStateManager.shadeModel(7424);
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            mc.mcProfiler.endStartSection("shadow entities");
            if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, 0);
            }
            renderGlobal.func_180446_a(viewEntity, frustum, partialTicks);
            Shaders.checkGLError("shadow entities");
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.enableCull();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.alphaFunc(516, 0.1f);
            if (Shaders.usedShadowDepthBuffers >= 2) {
                GlStateManager.setActiveTexture(33989);
                Shaders.checkGLError("pre copy shadow depth");
                GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)Shaders.shadowMapWidth, (int)Shaders.shadowMapHeight);
                Shaders.checkGLError("copy shadow depth");
                GlStateManager.setActiveTexture(33984);
            }
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            GlStateManager.shadeModel(7425);
            Shaders.checkGLError("shadow pre-translucent");
            GL20.glDrawBuffers((IntBuffer)Shaders.sfbDrawBuffers);
            Shaders.checkGLError("shadow drawbuffers pre-translucent");
            Shaders.checkFramebufferStatus("shadow pre-translucent");
            mc.mcProfiler.endStartSection("shadow translucent");
            renderGlobal.func_174977_a(EnumWorldBlockLayer.TRANSLUCENT, partialTicks, 2, viewEntity);
            Shaders.checkGLError("shadow translucent");
            if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                RenderHelper.enableStandardItemLighting();
                Reflector.call(Reflector.ForgeHooksClient_setRenderPass, 1);
                renderGlobal.func_180446_a(viewEntity, frustum, partialTicks);
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
            mc.gameSettings.thirdPersonView = Shaders.preShadowPassThirdPersonView;
            mc.mcProfiler.endStartSection("shadow postprocess");
            if (Shaders.hasGlGenMipmap) {
                if (Shaders.usedShadowDepthBuffers >= 1) {
                    if (Shaders.shadowMipmapEnabled[0]) {
                        GlStateManager.setActiveTexture(33988);
                        GlStateManager.func_179144_i(Shaders.sfbDepthTextures.get(0));
                        GL30.glGenerateMipmap((int)3553);
                        GL11.glTexParameteri((int)3553, (int)10241, (int)(Shaders.shadowFilterNearest[0] ? 9984 : 9987));
                    }
                    if (Shaders.usedShadowDepthBuffers >= 2 && Shaders.shadowMipmapEnabled[1]) {
                        GlStateManager.setActiveTexture(33989);
                        GlStateManager.func_179144_i(Shaders.sfbDepthTextures.get(1));
                        GL30.glGenerateMipmap((int)3553);
                        GL11.glTexParameteri((int)3553, (int)10241, (int)(Shaders.shadowFilterNearest[1] ? 9984 : 9987));
                    }
                    GlStateManager.setActiveTexture(33984);
                }
                if (Shaders.usedShadowColorBuffers >= 1) {
                    if (Shaders.shadowColorMipmapEnabled[0]) {
                        GlStateManager.setActiveTexture(33997);
                        GlStateManager.func_179144_i(Shaders.sfbColorTextures.get(0));
                        GL30.glGenerateMipmap((int)3553);
                        GL11.glTexParameteri((int)3553, (int)10241, (int)(Shaders.shadowColorFilterNearest[0] ? 9984 : 9987));
                    }
                    if (Shaders.usedShadowColorBuffers >= 2 && Shaders.shadowColorMipmapEnabled[1]) {
                        GlStateManager.setActiveTexture(33998);
                        GlStateManager.func_179144_i(Shaders.sfbColorTextures.get(1));
                        GL30.glGenerateMipmap((int)3553);
                        GL11.glTexParameteri((int)3553, (int)10241, (int)(Shaders.shadowColorFilterNearest[1] ? 9984 : 9987));
                    }
                    GlStateManager.setActiveTexture(33984);
                }
            }
            Shaders.checkGLError("shadow postprocess");
            EXTFramebufferObject.glBindFramebufferEXT((int)36160, (int)Shaders.dfb);
            GL11.glViewport((int)0, (int)0, (int)Shaders.renderWidth, (int)Shaders.renderHeight);
            Shaders.activeDrawBuffers = null;
            mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            Shaders.useProgram(7);
            GL11.glMatrixMode((int)5888);
            GL11.glPopMatrix();
            GL11.glMatrixMode((int)5889);
            GL11.glPopMatrix();
            GL11.glMatrixMode((int)5888);
            Shaders.checkGLError("shadow end");
        }
    }

    public static void preRenderChunkLayer() {
        if (OpenGlHelper.func_176075_f()) {
            GL11.glEnableClientState((int)32885);
            GL20.glEnableVertexAttribArray((int)Shaders.midTexCoordAttrib);
            GL20.glEnableVertexAttribArray((int)Shaders.tangentAttrib);
            GL20.glEnableVertexAttribArray((int)Shaders.entityAttrib);
        }
    }

    public static void postRenderChunkLayer() {
        if (OpenGlHelper.func_176075_f()) {
            GL11.glDisableClientState((int)32885);
            GL20.glDisableVertexAttribArray((int)Shaders.midTexCoordAttrib);
            GL20.glDisableVertexAttribArray((int)Shaders.tangentAttrib);
            GL20.glDisableVertexAttribArray((int)Shaders.entityAttrib);
        }
    }

    public static void setupArrayPointersVbo() {
        boolean vertexSizeI = true;
        GL11.glVertexPointer((int)3, (int)5126, (int)56, (long)0L);
        GL11.glColorPointer((int)4, (int)5121, (int)56, (long)12L);
        GL11.glTexCoordPointer((int)2, (int)5126, (int)56, (long)16L);
        OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glTexCoordPointer((int)2, (int)5122, (int)56, (long)24L);
        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glNormalPointer((int)5120, (int)56, (long)28L);
        GL20.glVertexAttribPointer((int)Shaders.midTexCoordAttrib, (int)2, (int)5126, (boolean)false, (int)56, (long)32L);
        GL20.glVertexAttribPointer((int)Shaders.tangentAttrib, (int)4, (int)5122, (boolean)false, (int)56, (long)40L);
        GL20.glVertexAttribPointer((int)Shaders.entityAttrib, (int)3, (int)5122, (boolean)false, (int)56, (long)48L);
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

    public static void layerArmorBaseDrawEnchantedGlintBegin() {
        Shaders.useProgram(17);
    }

    public static void layerArmorBaseDrawEnchantedGlintEnd() {
        if (Shaders.isRenderingWorld) {
            Shaders.useProgram(16);
        } else {
            Shaders.useProgram(0);
        }
    }
}

