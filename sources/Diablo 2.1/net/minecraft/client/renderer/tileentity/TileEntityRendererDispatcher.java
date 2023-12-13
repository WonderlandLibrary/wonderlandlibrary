package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;
import org.lwjgl.opengl.GL11;
import wtf.diablo.module.ModuleManager;
import wtf.diablo.module.impl.Render.ChestESP;
import wtf.diablo.module.impl.Render.Glow;
import wtf.diablo.utils.render.RenderUtil;

public class TileEntityRendererDispatcher
{
    private Map < Class <? extends TileEntity > , TileEntitySpecialRenderer <? extends TileEntity >> mapSpecialRenderers = Maps. < Class <? extends TileEntity > , TileEntitySpecialRenderer <? extends TileEntity >> newHashMap();
    public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
    private FontRenderer fontRenderer;

    /** The player's current X position (same as playerX) */
    public static double staticPlayerX;

    /** The player's current Y position (same as playerY) */
    public static double staticPlayerY;

    /** The player's current Z position (same as playerZ) */
    public static double staticPlayerZ;
    public TextureManager renderEngine;
    public World worldObj;
    public Entity entity;
    public float entityYaw;
    public float entityPitch;
    public double entityX;
    public double entityY;
    public double entityZ;

    private TileEntityRendererDispatcher()
    {
        this.mapSpecialRenderers.put(TileEntitySign.class, new TileEntitySignRenderer());
        this.mapSpecialRenderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        this.mapSpecialRenderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
        this.mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRenderer());
        this.mapSpecialRenderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
        this.mapSpecialRenderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
        this.mapSpecialRenderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
        this.mapSpecialRenderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
        this.mapSpecialRenderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
        this.mapSpecialRenderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());

        for (TileEntitySpecialRenderer<?> tileentityspecialrenderer : this.mapSpecialRenderers.values())
        {
            tileentityspecialrenderer.setRendererDispatcher(this);
        }
    }

    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererByClass(Class <? extends TileEntity > teClass)
    {
        TileEntitySpecialRenderer <? extends TileEntity > tileentityspecialrenderer = (TileEntitySpecialRenderer)this.mapSpecialRenderers.get(teClass);

        if (tileentityspecialrenderer == null && teClass != TileEntity.class)
        {
            tileentityspecialrenderer = this.<TileEntity>getSpecialRendererByClass((Class <? extends TileEntity >)teClass.getSuperclass());
            this.mapSpecialRenderers.put(teClass, tileentityspecialrenderer);
        }

        return (TileEntitySpecialRenderer<T>)tileentityspecialrenderer;
    }

    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(TileEntity tileEntityIn)
    {
        return (TileEntitySpecialRenderer<T>)(tileEntityIn == null ? null : this.getSpecialRendererByClass(tileEntityIn.getClass()));
    }

    public void cacheActiveRenderInfo(World worldIn, TextureManager textureManagerIn, FontRenderer fontrendererIn, Entity entityIn, float partialTicks)
    {
        if (this.worldObj != worldIn)
        {
            this.setWorld(worldIn);
        }

        this.renderEngine = textureManagerIn;
        this.entity = entityIn;
        this.fontRenderer = fontrendererIn;
        this.entityYaw = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
        this.entityPitch = entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks;
        this.entityX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        this.entityY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        this.entityZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
    }

    public void renderTileEntity(TileEntity tileentityIn, float partialTicks, int destroyStage)
    {
        if (tileentityIn.getDistanceSq(this.entityX, this.entityY, this.entityZ) < tileentityIn.getMaxRenderDistanceSquared())
        {
            int i = this.worldObj.getCombinedLight(tileentityIn.getPos(), 0);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            BlockPos blockpos = tileentityIn.getPos();
            this.renderTileEntityAt(tileentityIn, (double)blockpos.getX() - staticPlayerX, (double)blockpos.getY() - staticPlayerY, (double)blockpos.getZ() - staticPlayerZ, partialTicks, destroyStage);
        }
    }

    /**
     * Render this TileEntity at a given set of coordinates
     */
    public void renderTileEntityAt(TileEntity tileEntityIn, double x, double y, double z, float partialTicks)
    {
        this.renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, -1);
    }

    public void renderTileEntityAt(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage)
    {
        TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = this.<TileEntity>getSpecialRenderer(tileEntityIn);

        if (tileentityspecialrenderer != null)
        {
            try
            {
                if(Objects.equals(ChestESP.mode.getMode(), "Outline") && ModuleManager.getModule(ChestESP.class).isToggled()) {
                    double lineWidth = ChestESP.size.getValue();

                    tileentityspecialrenderer.renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, destroyStage);
                    Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
                    if (fbo != null) {
                        if (fbo.depthBuffer > -1) {
                            EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
                            int sdb = EXTFramebufferObject.glGenRenderbuffersEXT();
                            EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, sdb);
                            EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
                            EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, sdb);
                            EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, sdb);
                            fbo.depthBuffer = -1;
                        }
                    }
                    GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glLineWidth(Math.round(lineWidth));
                    GL11.glEnable(GL11.GL_LINE_SMOOTH);
                    GL11.glEnable(GL11.GL_STENCIL_TEST);
                    GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
                    GL11.glClearStencil(0xF);
                    GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xF);
                    GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
                    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

                    tileentityspecialrenderer.renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, destroyStage);
                    GL11.glStencilFunc(GL11.GL_NEVER, 0, 0xF);
                    GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
                    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                    tileentityspecialrenderer.renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, destroyStage);
                    GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xF);
                    GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
                    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                    GL11.glColor4d(255, 255, 255, 255);
                    GL11.glDepthMask(false);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
                    GL11.glPolygonOffset(1.0F, -2000000F);
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
                    RenderUtil.glColor(ChestESP.getColor(), 1F);
                    tileentityspecialrenderer.renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, destroyStage);
                    GL11.glPolygonOffset(1.0F, 2000000F);
                    GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glDepthMask(true);
                    GL11.glDisable(GL11.GL_STENCIL_TEST);
                    GL11.glDisable(GL11.GL_LINE_SMOOTH);
                    GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                    GL11.glPopAttrib();
                } else {
                    tileentityspecialrenderer.renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, destroyStage);
                }
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block Entity Details");
                tileEntityIn.addInfoToCrashReport(crashreportcategory);
                throw new ReportedException(crashreport);
            }
        }
    }

    public void setWorld(World worldIn)
    {
        this.worldObj = worldIn;
    }

    public FontRenderer getFontRenderer()
    {
        return this.fontRenderer;
    }
}
