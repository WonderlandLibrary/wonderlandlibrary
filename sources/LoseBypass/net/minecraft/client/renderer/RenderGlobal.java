/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.vector.Matrix4f
 *  org.lwjgl.util.vector.Vector3f
 *  org.lwjgl.util.vector.Vector4f
 */
package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;
import com.wallhacks.losebypass.systems.module.modules.misc.FreeLook;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderList;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VboRenderList;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemRecord;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vector3d;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class RenderGlobal
implements IWorldAccess,
IResourceManagerReloadListener {
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
    private static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation locationForcefieldPng = new ResourceLocation("textures/misc/forcefield.png");
    private final Minecraft mc;
    private final TextureManager renderEngine;
    private final RenderManager renderManager;
    private WorldClient theWorld;
    private Set<RenderChunk> chunksToUpdate = Sets.newLinkedHashSet();
    private List<ContainerLocalRenderInformation> renderInfos = Lists.newArrayListWithCapacity(69696);
    private final Set<TileEntity> field_181024_n = Sets.newHashSet();
    private ViewFrustum viewFrustum;
    private int starGLCallList = -1;
    private int glSkyList = -1;
    private int glSkyList2 = -1;
    private VertexFormat vertexBufferFormat;
    private VertexBuffer starVBO;
    private VertexBuffer skyVBO;
    private VertexBuffer sky2VBO;
    private int cloudTickCounter;
    private final Map<Integer, DestroyBlockProgress> damagedBlocks = Maps.newHashMap();
    private final Map<BlockPos, ISound> mapSoundPositions = Maps.newHashMap();
    private final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];
    private Framebuffer entityOutlineFramebuffer;
    private ShaderGroup entityOutlineShader;
    private double frustumUpdatePosX = Double.MIN_VALUE;
    private double frustumUpdatePosY = Double.MIN_VALUE;
    private double frustumUpdatePosZ = Double.MIN_VALUE;
    private int frustumUpdatePosChunkX = Integer.MIN_VALUE;
    private int frustumUpdatePosChunkY = Integer.MIN_VALUE;
    private int frustumUpdatePosChunkZ = Integer.MIN_VALUE;
    private double lastViewEntityX = Double.MIN_VALUE;
    private double lastViewEntityY = Double.MIN_VALUE;
    private double lastViewEntityZ = Double.MIN_VALUE;
    private double lastViewEntityPitch = Double.MIN_VALUE;
    private double lastViewEntityYaw = Double.MIN_VALUE;
    private final ChunkRenderDispatcher renderDispatcher = new ChunkRenderDispatcher();
    private ChunkRenderContainer renderContainer;
    private int renderDistanceChunks = -1;
    private int renderEntitiesStartupCounter = 2;
    private int countEntitiesTotal;
    private int countEntitiesRendered;
    private int countEntitiesHidden;
    private boolean debugFixTerrainFrustum = false;
    private ClippingHelper debugFixedClippingHelper;
    private final Vector4f[] debugTerrainMatrix = new Vector4f[8];
    private final Vector3d debugTerrainFrustumPosition = new Vector3d();
    private boolean vboEnabled = false;
    IRenderChunkFactory renderChunkFactory;
    private double prevRenderSortX;
    private double prevRenderSortY;
    private double prevRenderSortZ;
    private boolean displayListEntitiesDirty = true;

    public RenderGlobal(Minecraft mcIn) {
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.renderEngine = mcIn.getTextureManager();
        this.renderEngine.bindTexture(locationForcefieldPng);
        GL11.glTexParameteri((int)3553, (int)10242, (int)10497);
        GL11.glTexParameteri((int)3553, (int)10243, (int)10497);
        GlStateManager.bindTexture(0);
        this.updateDestroyBlockIcons();
        this.vboEnabled = OpenGlHelper.useVbo();
        if (this.vboEnabled) {
            this.renderContainer = new VboRenderList();
            this.renderChunkFactory = new VboChunkFactory();
        } else {
            this.renderContainer = new RenderList();
            this.renderChunkFactory = new ListChunkFactory();
        }
        this.vertexBufferFormat = new VertexFormat();
        this.vertexBufferFormat.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
        this.generateStars();
        this.generateSky();
        this.generateSky2();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.updateDestroyBlockIcons();
    }

    private void updateDestroyBlockIcons() {
        TextureMap texturemap = this.mc.getTextureMapBlocks();
        int i = 0;
        while (i < this.destroyBlockIcons.length) {
            this.destroyBlockIcons[i] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + i);
            ++i;
        }
    }

    public void makeEntityOutlineShader() {
        if (!OpenGlHelper.shadersSupported) {
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
            return;
        }
        if (ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
        }
        ResourceLocation resourcelocation = new ResourceLocation("shaders/post/entity_outline.json");
        try {
            this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), resourcelocation);
            this.entityOutlineShader.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
            return;
        }
        catch (IOException ioexception) {
            logger.warn("Failed to load shader: " + resourcelocation, (Throwable)ioexception);
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
            return;
        }
        catch (JsonSyntaxException jsonsyntaxexception) {
            logger.warn("Failed to load shader: " + resourcelocation, (Throwable)jsonsyntaxexception);
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
            return;
        }
    }

    public void renderEntityOutlineFramebuffer() {
        if (!this.isRenderEntityOutlines()) return;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
        GlStateManager.disableBlend();
    }

    protected boolean isRenderEntityOutlines() {
        if (this.entityOutlineFramebuffer == null) return false;
        if (this.entityOutlineShader == null) return false;
        if (this.mc.thePlayer == null) return false;
        if (!this.mc.thePlayer.isSpectator()) return false;
        if (!this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown()) return false;
        return true;
    }

    private void generateSky2() {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        if (this.sky2VBO != null) {
            this.sky2VBO.deleteGlBuffers();
        }
        if (this.glSkyList2 >= 0) {
            GLAllocation.deleteDisplayLists(this.glSkyList2);
            this.glSkyList2 = -1;
        }
        if (this.vboEnabled) {
            this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(worldrenderer, -16.0f, true);
            worldrenderer.finishDrawing();
            worldrenderer.reset();
            this.sky2VBO.func_181722_a(worldrenderer.getByteBuffer());
            return;
        }
        this.glSkyList2 = GLAllocation.generateDisplayLists(1);
        GL11.glNewList((int)this.glSkyList2, (int)4864);
        this.renderSky(worldrenderer, -16.0f, true);
        tessellator.draw();
        GL11.glEndList();
    }

    private void generateSky() {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        if (this.skyVBO != null) {
            this.skyVBO.deleteGlBuffers();
        }
        if (this.glSkyList >= 0) {
            GLAllocation.deleteDisplayLists(this.glSkyList);
            this.glSkyList = -1;
        }
        if (this.vboEnabled) {
            this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(worldrenderer, 16.0f, false);
            worldrenderer.finishDrawing();
            worldrenderer.reset();
            this.skyVBO.func_181722_a(worldrenderer.getByteBuffer());
            return;
        }
        this.glSkyList = GLAllocation.generateDisplayLists(1);
        GL11.glNewList((int)this.glSkyList, (int)4864);
        this.renderSky(worldrenderer, 16.0f, false);
        tessellator.draw();
        GL11.glEndList();
    }

    private void renderSky(WorldRenderer worldRendererIn, float p_174968_2_, boolean p_174968_3_) {
        int i = 64;
        int j = 6;
        worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
        int k = -384;
        while (k <= 384) {
            for (int l = -384; l <= 384; l += 64) {
                float f = k;
                float f1 = k + 64;
                if (p_174968_3_) {
                    f1 = k;
                    f = k + 64;
                }
                worldRendererIn.pos(f, p_174968_2_, l).endVertex();
                worldRendererIn.pos(f1, p_174968_2_, l).endVertex();
                worldRendererIn.pos(f1, p_174968_2_, l + 64).endVertex();
                worldRendererIn.pos(f, p_174968_2_, l + 64).endVertex();
            }
            k += 64;
        }
    }

    private void generateStars() {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        if (this.starVBO != null) {
            this.starVBO.deleteGlBuffers();
        }
        if (this.starGLCallList >= 0) {
            GLAllocation.deleteDisplayLists(this.starGLCallList);
            this.starGLCallList = -1;
        }
        if (this.vboEnabled) {
            this.starVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderStars(worldrenderer);
            worldrenderer.finishDrawing();
            worldrenderer.reset();
            this.starVBO.func_181722_a(worldrenderer.getByteBuffer());
            return;
        }
        this.starGLCallList = GLAllocation.generateDisplayLists(1);
        GlStateManager.pushMatrix();
        GL11.glNewList((int)this.starGLCallList, (int)4864);
        this.renderStars(worldrenderer);
        tessellator.draw();
        GL11.glEndList();
        GlStateManager.popMatrix();
    }

    private void renderStars(WorldRenderer worldRendererIn) {
        Random random = new Random(10842L);
        worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
        int i = 0;
        while (i < 1500) {
            double d0 = random.nextFloat() * 2.0f - 1.0f;
            double d1 = random.nextFloat() * 2.0f - 1.0f;
            double d2 = random.nextFloat() * 2.0f - 1.0f;
            double d3 = 0.15f + random.nextFloat() * 0.1f;
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d4 < 1.0 && d4 > 0.01) {
                d4 = 1.0 / Math.sqrt(d4);
                double d5 = (d0 *= d4) * 100.0;
                double d6 = (d1 *= d4) * 100.0;
                double d7 = (d2 *= d4) * 100.0;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * Math.PI * 2.0;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);
                for (int j = 0; j < 4; ++j) {
                    double d17 = 0.0;
                    double d18 = (double)((j & 2) - 1) * d3;
                    double d19 = (double)((j + 1 & 2) - 1) * d3;
                    double d20 = 0.0;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + 0.0 * d13;
                    double d24 = 0.0 * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    worldRendererIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
                }
            }
            ++i;
        }
    }

    public void setWorldAndLoadRenderers(WorldClient worldClientIn) {
        if (this.theWorld != null) {
            this.theWorld.removeWorldAccess(this);
        }
        this.frustumUpdatePosX = Double.MIN_VALUE;
        this.frustumUpdatePosY = Double.MIN_VALUE;
        this.frustumUpdatePosZ = Double.MIN_VALUE;
        this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
        this.renderManager.set(worldClientIn);
        this.theWorld = worldClientIn;
        if (worldClientIn == null) return;
        worldClientIn.addWorldAccess(this);
        this.loadRenderers();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void loadRenderers() {
        Entity entity;
        if (this.theWorld == null) return;
        this.displayListEntitiesDirty = true;
        Blocks.leaves.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
        Blocks.leaves2.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
        this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
        boolean flag = this.vboEnabled;
        this.vboEnabled = OpenGlHelper.useVbo();
        if (flag && !this.vboEnabled) {
            this.renderContainer = new RenderList();
            this.renderChunkFactory = new ListChunkFactory();
        } else if (!flag && this.vboEnabled) {
            this.renderContainer = new VboRenderList();
            this.renderChunkFactory = new VboChunkFactory();
        }
        if (flag != this.vboEnabled) {
            this.generateStars();
            this.generateSky();
            this.generateSky2();
        }
        if (this.viewFrustum != null) {
            this.viewFrustum.deleteGlResources();
        }
        this.stopChunkUpdates();
        Set<TileEntity> set = this.field_181024_n;
        synchronized (set) {
            this.field_181024_n.clear();
        }
        this.viewFrustum = new ViewFrustum(this.theWorld, this.mc.gameSettings.renderDistanceChunks, this, this.renderChunkFactory);
        if (this.theWorld != null && (entity = this.mc.getRenderViewEntity()) != null) {
            this.viewFrustum.updateChunkPositions(entity.posX, entity.posZ);
        }
        this.renderEntitiesStartupCounter = 2;
    }

    protected void stopChunkUpdates() {
        this.chunksToUpdate.clear();
        this.renderDispatcher.stopChunkUpdates();
    }

    public void createBindEntityOutlineFbs(int p_72720_1_, int p_72720_2_) {
        if (!OpenGlHelper.shadersSupported) return;
        if (this.entityOutlineShader == null) return;
        this.entityOutlineShader.createBindFramebuffers(p_72720_1_, p_72720_2_);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Could not resolve type clashes
     * Unable to fully structure code
     */
    public void renderEntities(Entity renderViewEntity, ICamera camera, float partialTicks) {
        if (this.renderEntitiesStartupCounter > 0) {
            --this.renderEntitiesStartupCounter;
            return;
        }
        d0 = renderViewEntity.prevPosX + (renderViewEntity.posX - renderViewEntity.prevPosX) * (double)partialTicks;
        d1 = renderViewEntity.prevPosY + (renderViewEntity.posY - renderViewEntity.prevPosY) * (double)partialTicks;
        d2 = renderViewEntity.prevPosZ + (renderViewEntity.posZ - renderViewEntity.prevPosZ) * (double)partialTicks;
        this.theWorld.theProfiler.startSection("prepare");
        TileEntityRendererDispatcher.instance.cacheActiveRenderInfo(this.theWorld, this.mc.getTextureManager(), this.mc.fontRendererObj, this.mc.getRenderViewEntity(), partialTicks);
        this.renderManager.cacheActiveRenderInfo(this.theWorld, this.mc.fontRendererObj, this.mc.getRenderViewEntity(), this.mc.pointedEntity, this.mc.gameSettings, partialTicks);
        this.countEntitiesTotal = 0;
        this.countEntitiesRendered = 0;
        this.countEntitiesHidden = 0;
        entity = this.mc.getRenderViewEntity();
        d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        d5 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        TileEntityRendererDispatcher.staticPlayerX = d3;
        TileEntityRendererDispatcher.staticPlayerY = d4;
        TileEntityRendererDispatcher.staticPlayerZ = d5;
        this.renderManager.setRenderPosition(d3, d4, d5);
        this.mc.entityRenderer.enableLightmap();
        this.theWorld.theProfiler.endStartSection("global");
        list = this.theWorld.getLoadedEntityList();
        this.countEntitiesTotal = list.size();
        for (i = 0; i < this.theWorld.weatherEffects.size(); ++i) {
            entity1 = (Entity)this.theWorld.weatherEffects.get(i);
            ++this.countEntitiesRendered;
            if (!entity1.isInRangeToRender3d(d0, d1, d2)) continue;
            this.renderManager.renderEntitySimple(entity1, partialTicks);
        }
        if (this.isRenderEntityOutlines()) {
            GlStateManager.depthFunc(519);
            GlStateManager.disableFog();
            this.entityOutlineFramebuffer.framebufferClear();
            this.entityOutlineFramebuffer.bindFramebuffer(false);
            this.theWorld.theProfiler.endStartSection("entityOutlines");
            RenderHelper.disableStandardItemLighting();
            this.renderManager.setRenderOutlines(true);
            for (j = 0; j < list.size(); ++j) {
                entity3 = list.get(j);
                flag = this.mc.getRenderViewEntity() instanceof EntityLivingBase != false && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping() != false;
                v0 = flag1 = entity3.isInRangeToRender3d(d0, d1, d2) != false && (entity3.ignoreFrustumCheck != false || camera.isBoundingBoxInFrustum(entity3.getEntityBoundingBox()) != false || entity3.riddenByEntity == this.mc.thePlayer) && entity3 instanceof EntityPlayer != false;
                if (entity3 == this.mc.getRenderViewEntity() && FreeLook.getPerspective() == 0 && !flag || !flag1) continue;
                this.renderManager.renderEntitySimple(entity3, partialTicks);
            }
            this.renderManager.setRenderOutlines(false);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.depthMask(false);
            this.entityOutlineShader.loadShaderGroup(partialTicks);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            this.mc.getFramebuffer().bindFramebuffer(false);
            GlStateManager.enableFog();
            GlStateManager.enableBlend();
            GlStateManager.enableColorMaterial();
            GlStateManager.depthFunc(515);
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
        }
        this.theWorld.theProfiler.endStartSection("entities");
        var18_13 = this.renderInfos.iterator();
        while (true) {
            if (var18_13.hasNext()) {
                renderglobal$containerlocalrenderinformation = var18_13.next();
                chunk = this.theWorld.getChunkFromBlockCoords(renderglobal$containerlocalrenderinformation.renderChunk.getPosition());
                classinheritancemultimap = chunk.getEntityLists()[renderglobal$containerlocalrenderinformation.renderChunk.getPosition().getY() / 16];
                if (classinheritancemultimap.isEmpty()) continue;
            } else {
                this.theWorld.theProfiler.endStartSection("blockentities");
                RenderHelper.enableStandardItemLighting();
                for (Object renderglobal$containerlocalrenderinformation1 : this.renderInfos) {
                    list1 = renderglobal$containerlocalrenderinformation1.renderChunk.getCompiledChunk().getTileEntities();
                    if (list1.isEmpty()) continue;
                    for (TileEntity tileentity2 : list1) {
                        TileEntityRendererDispatcher.instance.renderTileEntity(tileentity2, partialTicks, -1);
                    }
                }
                var18_13 = this.field_181024_n;
                synchronized (var18_13) {
                    for (TileEntity tileentity : this.field_181024_n) {
                        TileEntityRendererDispatcher.instance.renderTileEntity(tileentity, partialTicks, -1);
                    }
                    break;
                }
            }
            iterator = classinheritancemultimap.iterator();
            while (true) {
                if (!iterator.hasNext()) ** break;
                entity2 = iterator.next();
                v1 = flag2 = this.renderManager.shouldRender(entity2, camera, d0, d1, d2) != false || entity2.riddenByEntity == this.mc.thePlayer;
                if (flag2) {
                    v2 = flag3 = this.mc.getRenderViewEntity() instanceof EntityLivingBase != false ? ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping() : false;
                    if (entity2 == this.mc.getRenderViewEntity() && FreeLook.getPerspective() == 0 && !flag3 || !(entity2.posY < 0.0) && !(entity2.posY >= 256.0) && !this.theWorld.isBlockLoaded(new BlockPos(entity2))) continue;
                    ++this.countEntitiesRendered;
                    this.renderManager.renderEntitySimple(entity2, partialTicks);
                }
                if (flag2 || !(entity2 instanceof EntityWitherSkull)) continue;
                this.mc.getRenderManager().renderWitherSkull(entity2, partialTicks);
            }
            break;
        }
        this.preRenderDamagedBlocks();
        var18_13 = this.damagedBlocks.values().iterator();
        while (true) {
            if (!var18_13.hasNext()) {
                this.postRenderDamagedBlocks();
                this.mc.entityRenderer.disableLightmap();
                this.mc.mcProfiler.endSection();
                return;
            }
            destroyblockprogress = var18_13.next();
            blockpos = destroyblockprogress.getPosition();
            tileentity1 = this.theWorld.getTileEntity(blockpos);
            if (tileentity1 instanceof TileEntityChest) {
                tileentitychest = (TileEntityChest)tileentity1;
                if (tileentitychest.adjacentChestXNeg != null) {
                    blockpos = blockpos.offset(EnumFacing.WEST);
                    tileentity1 = this.theWorld.getTileEntity(blockpos);
                } else if (tileentitychest.adjacentChestZNeg != null) {
                    blockpos = blockpos.offset(EnumFacing.NORTH);
                    tileentity1 = this.theWorld.getTileEntity(blockpos);
                }
            }
            block = this.theWorld.getBlockState(blockpos).getBlock();
            if (tileentity1 == null || !(block instanceof BlockChest) && !(block instanceof BlockEnderChest) && !(block instanceof BlockSign) && !(block instanceof BlockSkull)) continue;
            TileEntityRendererDispatcher.instance.renderTileEntity(tileentity1, partialTicks, destroyblockprogress.getPartialBlockDamage());
        }
    }

    public String getDebugInfoRenders() {
        int i = this.viewFrustum.renderChunks.length;
        int j = 0;
        for (ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos) {
            CompiledChunk compiledchunk = renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk;
            if (compiledchunk == CompiledChunk.DUMMY || compiledchunk.isEmpty()) continue;
            ++j;
        }
        return String.format("C: %d/%d %sD: %d, %s", j, i, this.mc.renderChunksMany ? "(s) " : "", this.renderDistanceChunks, this.renderDispatcher.getDebugInfo());
    }

    public String getDebugInfoEntities() {
        return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
    }

    public void setupTerrain(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator) {
        boolean flag;
        if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks) {
            this.loadRenderers();
        }
        this.theWorld.theProfiler.startSection("camera");
        double d0 = viewEntity.posX - this.frustumUpdatePosX;
        double d1 = viewEntity.posY - this.frustumUpdatePosY;
        double d2 = viewEntity.posZ - this.frustumUpdatePosZ;
        if (this.frustumUpdatePosChunkX != viewEntity.chunkCoordX || this.frustumUpdatePosChunkY != viewEntity.chunkCoordY || this.frustumUpdatePosChunkZ != viewEntity.chunkCoordZ || d0 * d0 + d1 * d1 + d2 * d2 > 16.0) {
            this.frustumUpdatePosX = viewEntity.posX;
            this.frustumUpdatePosY = viewEntity.posY;
            this.frustumUpdatePosZ = viewEntity.posZ;
            this.frustumUpdatePosChunkX = viewEntity.chunkCoordX;
            this.frustumUpdatePosChunkY = viewEntity.chunkCoordY;
            this.frustumUpdatePosChunkZ = viewEntity.chunkCoordZ;
            this.viewFrustum.updateChunkPositions(viewEntity.posX, viewEntity.posZ);
        }
        this.theWorld.theProfiler.endStartSection("renderlistcamera");
        double d3 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
        double d4 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
        double d5 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
        this.renderContainer.initialize(d3, d4, d5);
        this.theWorld.theProfiler.endStartSection("cull");
        if (this.debugFixedClippingHelper != null) {
            Frustum frustum = new Frustum(this.debugFixedClippingHelper);
            frustum.setPosition(this.debugTerrainFrustumPosition.field_181059_a, this.debugTerrainFrustumPosition.field_181060_b, this.debugTerrainFrustumPosition.field_181061_c);
            camera = frustum;
        }
        this.mc.mcProfiler.endStartSection("culling");
        BlockPos blockpos1 = new BlockPos(d3, d4 + (double)viewEntity.getEyeHeight(), d5);
        RenderChunk renderchunk = this.viewFrustum.getRenderChunk(blockpos1);
        BlockPos blockpos = new BlockPos(MathHelper.floor_double(d3 / 16.0) * 16, MathHelper.floor_double(d4 / 16.0) * 16, MathHelper.floor_double(d5 / 16.0) * 16);
        this.displayListEntitiesDirty = this.displayListEntitiesDirty || !this.chunksToUpdate.isEmpty() || viewEntity.posX != this.lastViewEntityX || viewEntity.posY != this.lastViewEntityY || viewEntity.posZ != this.lastViewEntityZ || (double)viewEntity.rotationPitch != this.lastViewEntityPitch || (double)viewEntity.rotationYaw != this.lastViewEntityYaw;
        this.lastViewEntityX = viewEntity.posX;
        this.lastViewEntityY = viewEntity.posY;
        this.lastViewEntityZ = viewEntity.posZ;
        this.lastViewEntityPitch = viewEntity.rotationPitch;
        this.lastViewEntityYaw = viewEntity.rotationYaw;
        boolean bl = flag = this.debugFixedClippingHelper != null;
        if (!flag && this.displayListEntitiesDirty) {
            this.displayListEntitiesDirty = false;
            this.renderInfos = Lists.newArrayList();
            LinkedList<ContainerLocalRenderInformation> queue = Lists.newLinkedList();
            boolean flag1 = this.mc.renderChunksMany;
            if (renderchunk != null) {
                boolean flag2 = false;
                ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation3 = new ContainerLocalRenderInformation(renderchunk, null, 0);
                Set<EnumFacing> set1 = this.getVisibleFacings(blockpos1);
                if (set1.size() == 1) {
                    Vector3f vector3f = this.getViewVector(viewEntity, partialTicks);
                    EnumFacing enumfacing = EnumFacing.getFacingFromVector(vector3f.x, vector3f.y, vector3f.z).getOpposite();
                    set1.remove(enumfacing);
                }
                if (set1.isEmpty()) {
                    flag2 = true;
                }
                if (flag2 && !playerSpectator) {
                    this.renderInfos.add(renderglobal$containerlocalrenderinformation3);
                } else {
                    if (playerSpectator && this.theWorld.getBlockState(blockpos1).getBlock().isOpaqueCube()) {
                        flag1 = false;
                    }
                    renderchunk.setFrameIndex(frameCount);
                    queue.add(renderglobal$containerlocalrenderinformation3);
                }
            } else {
                int i = blockpos1.getY() > 0 ? 248 : 8;
                for (int j = -this.renderDistanceChunks; j <= this.renderDistanceChunks; ++j) {
                    for (int k = -this.renderDistanceChunks; k <= this.renderDistanceChunks; ++k) {
                        RenderChunk renderchunk1 = this.viewFrustum.getRenderChunk(new BlockPos((j << 4) + 8, i, (k << 4) + 8));
                        if (renderchunk1 == null || !camera.isBoundingBoxInFrustum(renderchunk1.boundingBox)) continue;
                        renderchunk1.setFrameIndex(frameCount);
                        queue.add(new ContainerLocalRenderInformation(renderchunk1, null, 0));
                    }
                }
            }
            while (!queue.isEmpty()) {
                ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1 = (ContainerLocalRenderInformation)queue.poll();
                RenderChunk renderchunk3 = renderglobal$containerlocalrenderinformation1.renderChunk;
                EnumFacing enumfacing2 = renderglobal$containerlocalrenderinformation1.facing;
                BlockPos blockpos2 = renderchunk3.getPosition();
                this.renderInfos.add(renderglobal$containerlocalrenderinformation1);
                for (EnumFacing enumfacing1 : EnumFacing.values()) {
                    RenderChunk renderchunk2 = this.func_181562_a(blockpos, renderchunk3, enumfacing1);
                    if (flag1 && renderglobal$containerlocalrenderinformation1.setFacing.contains(enumfacing1.getOpposite()) || flag1 && enumfacing2 != null && !renderchunk3.getCompiledChunk().isVisible(enumfacing2.getOpposite(), enumfacing1) || renderchunk2 == null || !renderchunk2.setFrameIndex(frameCount) || !camera.isBoundingBoxInFrustum(renderchunk2.boundingBox)) continue;
                    ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = new ContainerLocalRenderInformation(renderchunk2, enumfacing1, renderglobal$containerlocalrenderinformation1.counter + 1);
                    renderglobal$containerlocalrenderinformation.setFacing.addAll(renderglobal$containerlocalrenderinformation1.setFacing);
                    renderglobal$containerlocalrenderinformation.setFacing.add(enumfacing1);
                    queue.add(renderglobal$containerlocalrenderinformation);
                }
            }
        }
        if (this.debugFixTerrainFrustum) {
            this.fixTerrainFrustum(d3, d4, d5);
            this.debugFixTerrainFrustum = false;
        }
        this.renderDispatcher.clearChunkUpdates();
        Set<RenderChunk> set = this.chunksToUpdate;
        this.chunksToUpdate = Sets.newLinkedHashSet();
        Iterator<ContainerLocalRenderInformation> iterator = this.renderInfos.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.chunksToUpdate.addAll(set);
                this.mc.mcProfiler.endSection();
                return;
            }
            ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation2 = iterator.next();
            RenderChunk renderchunk4 = renderglobal$containerlocalrenderinformation2.renderChunk;
            if (!renderchunk4.isNeedsUpdate() && !set.contains(renderchunk4)) continue;
            this.displayListEntitiesDirty = true;
            if (this.isPositionInRenderChunk(blockpos, renderglobal$containerlocalrenderinformation2.renderChunk)) {
                this.mc.mcProfiler.startSection("build near");
                this.renderDispatcher.updateChunkNow(renderchunk4);
                renderchunk4.setNeedsUpdate(false);
                this.mc.mcProfiler.endSection();
                continue;
            }
            this.chunksToUpdate.add(renderchunk4);
        }
    }

    private boolean isPositionInRenderChunk(BlockPos pos, RenderChunk renderChunkIn) {
        BlockPos blockpos = renderChunkIn.getPosition();
        if (MathHelper.abs_int(pos.getX() - blockpos.getX()) > 16) {
            return false;
        }
        if (MathHelper.abs_int(pos.getY() - blockpos.getY()) > 16) {
            return false;
        }
        if (MathHelper.abs_int(pos.getZ() - blockpos.getZ()) > 16) return false;
        return true;
    }

    private Set<EnumFacing> getVisibleFacings(BlockPos pos) {
        VisGraph visgraph = new VisGraph();
        BlockPos blockpos = new BlockPos(pos.getX() >> 4 << 4, pos.getY() >> 4 << 4, pos.getZ() >> 4 << 4);
        Chunk chunk = this.theWorld.getChunkFromBlockCoords(blockpos);
        Iterator<BlockPos.MutableBlockPos> iterator = BlockPos.getAllInBoxMutable(blockpos, blockpos.add(15, 15, 15)).iterator();
        while (iterator.hasNext()) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = iterator.next();
            if (!chunk.getBlock(blockpos$mutableblockpos).isOpaqueCube()) continue;
            visgraph.func_178606_a(blockpos$mutableblockpos);
        }
        return visgraph.func_178609_b(pos);
    }

    private RenderChunk func_181562_a(BlockPos p_181562_1_, RenderChunk p_181562_2_, EnumFacing p_181562_3_) {
        BlockPos blockpos = p_181562_2_.func_181701_a(p_181562_3_);
        if (MathHelper.abs_int(p_181562_1_.getX() - blockpos.getX()) > this.renderDistanceChunks * 16) {
            return null;
        }
        if (blockpos.getY() < 0) return null;
        if (blockpos.getY() >= 256) return null;
        if (MathHelper.abs_int(p_181562_1_.getZ() - blockpos.getZ()) > this.renderDistanceChunks * 16) {
            return null;
        }
        RenderChunk renderChunk = this.viewFrustum.getRenderChunk(blockpos);
        return renderChunk;
    }

    private void fixTerrainFrustum(double x, double y, double z) {
        this.debugFixedClippingHelper = new ClippingHelperImpl();
        ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
        net.minecraft.util.Matrix4f matrix4f = new net.minecraft.util.Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
        matrix4f.transpose();
        net.minecraft.util.Matrix4f matrix4f1 = new net.minecraft.util.Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
        matrix4f1.transpose();
        net.minecraft.util.Matrix4f matrix4f2 = new net.minecraft.util.Matrix4f();
        net.minecraft.util.Matrix4f.mul((Matrix4f)matrix4f1, (Matrix4f)matrix4f, (Matrix4f)matrix4f2);
        matrix4f2.invert();
        this.debugTerrainFrustumPosition.field_181059_a = x;
        this.debugTerrainFrustumPosition.field_181060_b = y;
        this.debugTerrainFrustumPosition.field_181061_c = z;
        this.debugTerrainMatrix[0] = new Vector4f(-1.0f, -1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[1] = new Vector4f(1.0f, -1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[2] = new Vector4f(1.0f, 1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[3] = new Vector4f(-1.0f, 1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[4] = new Vector4f(-1.0f, -1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[5] = new Vector4f(1.0f, -1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[6] = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[7] = new Vector4f(-1.0f, 1.0f, 1.0f, 1.0f);
        int i = 0;
        while (i < 8) {
            net.minecraft.util.Matrix4f.transform((Matrix4f)matrix4f2, (Vector4f)this.debugTerrainMatrix[i], (Vector4f)this.debugTerrainMatrix[i]);
            this.debugTerrainMatrix[i].x /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].y /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].z /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].w = 1.0f;
            ++i;
        }
    }

    protected Vector3f getViewVector(Entity entityIn, double partialTicks) {
        float f = (float)((double)entityIn.prevRotationPitch + (double)(entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks);
        float f1 = (float)((double)entityIn.prevRotationYaw + (double)(entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks);
        if (FreeLook.getPerspective() == 2) {
            f += 180.0f;
        }
        float f2 = MathHelper.cos(-f1 * ((float)Math.PI / 180) - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float)Math.PI / 180) - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * ((float)Math.PI / 180));
        float f5 = MathHelper.sin(-f * ((float)Math.PI / 180));
        return new Vector3f(f3 * f4, f5, f2 * f4);
    }

    public int renderBlockLayer(EnumWorldBlockLayer blockLayerIn, double partialTicks, int pass, Entity entityIn) {
        RenderHelper.disableStandardItemLighting();
        if (blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT) {
            this.mc.mcProfiler.startSection("translucent_sort");
            double d0 = entityIn.posX - this.prevRenderSortX;
            double d1 = entityIn.posY - this.prevRenderSortY;
            double d2 = entityIn.posZ - this.prevRenderSortZ;
            if (d0 * d0 + d1 * d1 + d2 * d2 > 1.0) {
                this.prevRenderSortX = entityIn.posX;
                this.prevRenderSortY = entityIn.posY;
                this.prevRenderSortZ = entityIn.posZ;
                int k = 0;
                for (ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos) {
                    if (!renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk.isLayerStarted(blockLayerIn) || k++ >= 15) continue;
                    this.renderDispatcher.updateTransparencyLater(renderglobal$containerlocalrenderinformation.renderChunk);
                }
            }
            this.mc.mcProfiler.endSection();
        }
        this.mc.mcProfiler.startSection("filterempty");
        int l = 0;
        boolean flag = blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT;
        int i1 = flag ? this.renderInfos.size() - 1 : 0;
        int i = flag ? -1 : this.renderInfos.size();
        int j1 = flag ? -1 : 1;
        int j = i1;
        while (true) {
            if (j == i) {
                this.mc.mcProfiler.endStartSection("render_" + (Object)((Object)blockLayerIn));
                this.renderBlockLayer(blockLayerIn);
                this.mc.mcProfiler.endSection();
                return l;
            }
            RenderChunk renderchunk = this.renderInfos.get((int)j).renderChunk;
            if (!renderchunk.getCompiledChunk().isLayerEmpty(blockLayerIn)) {
                ++l;
                this.renderContainer.addRenderChunk(renderchunk, blockLayerIn);
            }
            j += j1;
        }
    }

    private void renderBlockLayer(EnumWorldBlockLayer blockLayerIn) {
        this.mc.entityRenderer.enableLightmap();
        if (OpenGlHelper.useVbo()) {
            GL11.glEnableClientState((int)32884);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glEnableClientState((int)32888);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glEnableClientState((int)32888);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glEnableClientState((int)32886);
        }
        this.renderContainer.renderChunkLayer(blockLayerIn);
        if (OpenGlHelper.useVbo()) {
            block5: for (VertexFormatElement vertexformatelement : DefaultVertexFormats.BLOCK.getElements()) {
                VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                int i = vertexformatelement.getIndex();
                switch (2.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumUsage[vertexformatelement$enumusage.ordinal()]) {
                    case 1: {
                        GL11.glDisableClientState((int)32884);
                        break;
                    }
                    case 2: {
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + i);
                        GL11.glDisableClientState((int)32888);
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                        break;
                    }
                    case 3: {
                        GL11.glDisableClientState((int)32886);
                        GlStateManager.resetColor();
                        continue block5;
                    }
                }
            }
        }
        this.mc.entityRenderer.disableLightmap();
    }

    private void cleanupDamagedBlocks(Iterator<DestroyBlockProgress> iteratorIn) {
        while (iteratorIn.hasNext()) {
            DestroyBlockProgress destroyblockprogress = iteratorIn.next();
            int i = destroyblockprogress.getCreationCloudUpdateTick();
            if (this.cloudTickCounter - i <= 400) continue;
            iteratorIn.remove();
        }
    }

    public void updateClouds() {
        ++this.cloudTickCounter;
        if (this.cloudTickCounter % 20 != 0) return;
        this.cleanupDamagedBlocks(this.damagedBlocks.values().iterator());
    }

    private void renderSkyEnd() {
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.depthMask(false);
        this.renderEngine.bindTexture(locationEndSkyPng);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 0;
        while (true) {
            if (i >= 6) {
                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GlStateManager.enableAlpha();
                return;
            }
            GlStateManager.pushMatrix();
            if (i == 1) {
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            }
            if (i == 2) {
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            }
            if (i == 3) {
                GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
            }
            if (i == 4) {
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            }
            if (i == 5) {
                GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
            }
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos(-100.0, -100.0, -100.0).tex(0.0, 0.0).color(40, 40, 40, 255).endVertex();
            worldrenderer.pos(-100.0, -100.0, 100.0).tex(0.0, 16.0).color(40, 40, 40, 255).endVertex();
            worldrenderer.pos(100.0, -100.0, 100.0).tex(16.0, 16.0).color(40, 40, 40, 255).endVertex();
            worldrenderer.pos(100.0, -100.0, -100.0).tex(16.0, 0.0).color(40, 40, 40, 255).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            ++i;
        }
    }

    public void renderSky(float partialTicks, int pass) {
        if (this.mc.theWorld.provider.getDimensionId() == 1) {
            this.renderSkyEnd();
            return;
        }
        if (!this.mc.theWorld.provider.isSurfaceWorld()) return;
        GlStateManager.disableTexture2D();
        Vec3 vec3 = this.theWorld.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
        float f = (float)vec3.xCoord;
        float f1 = (float)vec3.yCoord;
        float f2 = (float)vec3.zCoord;
        if (pass != 2) {
            float f3 = (f * 30.0f + f1 * 59.0f + f2 * 11.0f) / 100.0f;
            float f4 = (f * 30.0f + f1 * 70.0f) / 100.0f;
            float f5 = (f * 30.0f + f2 * 70.0f) / 100.0f;
            f = f3;
            f1 = f4;
            f2 = f5;
        }
        GlStateManager.color(f, f1, f2);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.depthMask(false);
        GlStateManager.enableFog();
        GlStateManager.color(f, f1, f2);
        if (this.vboEnabled) {
            this.skyVBO.bindBuffer();
            GL11.glEnableClientState((int)32884);
            GL11.glVertexPointer((int)3, (int)5126, (int)12, (long)0L);
            this.skyVBO.drawArrays(7);
            this.skyVBO.unbindBuffer();
            GL11.glDisableClientState((int)32884);
        } else {
            GlStateManager.callList(this.glSkyList);
        }
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.disableStandardItemLighting();
        float[] afloat = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(partialTicks), partialTicks);
        if (afloat != null) {
            GlStateManager.disableTexture2D();
            GlStateManager.shadeModel(7425);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(MathHelper.sin(this.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0f ? 180.0f : 0.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            float f6 = afloat[0];
            float f7 = afloat[1];
            float f8 = afloat[2];
            if (pass != 2) {
                float f9 = (f6 * 30.0f + f7 * 59.0f + f8 * 11.0f) / 100.0f;
                float f10 = (f6 * 30.0f + f7 * 70.0f) / 100.0f;
                float f11 = (f6 * 30.0f + f8 * 70.0f) / 100.0f;
                f6 = f9;
                f7 = f10;
                f8 = f11;
            }
            worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos(0.0, 100.0, 0.0).color(f6, f7, f8, afloat[3]).endVertex();
            int j = 16;
            for (int l = 0; l <= 16; ++l) {
                float f21 = (float)l * (float)Math.PI * 2.0f / 16.0f;
                float f12 = MathHelper.sin(f21);
                float f13 = MathHelper.cos(f21);
                worldrenderer.pos(f12 * 120.0f, f13 * 120.0f, -f13 * 40.0f * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0f).endVertex();
            }
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.shadeModel(7424);
        }
        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        GlStateManager.pushMatrix();
        float f16 = 1.0f - this.theWorld.getRainStrength(partialTicks);
        GlStateManager.color(1.0f, 1.0f, 1.0f, f16);
        GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(this.theWorld.getCelestialAngle(partialTicks) * 360.0f, 1.0f, 0.0f, 0.0f);
        float f17 = 30.0f;
        this.renderEngine.bindTexture(locationSunPng);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-f17, 100.0, -f17).tex(0.0, 0.0).endVertex();
        worldrenderer.pos(f17, 100.0, -f17).tex(1.0, 0.0).endVertex();
        worldrenderer.pos(f17, 100.0, f17).tex(1.0, 1.0).endVertex();
        worldrenderer.pos(-f17, 100.0, f17).tex(0.0, 1.0).endVertex();
        tessellator.draw();
        f17 = 20.0f;
        this.renderEngine.bindTexture(locationMoonPhasesPng);
        int i = this.theWorld.getMoonPhase();
        int k = i % 4;
        int i1 = i / 4 % 2;
        float f22 = (float)(k + 0) / 4.0f;
        float f23 = (float)(i1 + 0) / 2.0f;
        float f24 = (float)(k + 1) / 4.0f;
        float f14 = (float)(i1 + 1) / 2.0f;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-f17, -100.0, f17).tex(f24, f14).endVertex();
        worldrenderer.pos(f17, -100.0, f17).tex(f22, f14).endVertex();
        worldrenderer.pos(f17, -100.0, -f17).tex(f22, f23).endVertex();
        worldrenderer.pos(-f17, -100.0, -f17).tex(f24, f23).endVertex();
        tessellator.draw();
        GlStateManager.disableTexture2D();
        float f15 = this.theWorld.getStarBrightness(partialTicks) * f16;
        if (f15 > 0.0f) {
            GlStateManager.color(f15, f15, f15, f15);
            if (this.vboEnabled) {
                this.starVBO.bindBuffer();
                GL11.glEnableClientState((int)32884);
                GL11.glVertexPointer((int)3, (int)5126, (int)12, (long)0L);
                this.starVBO.drawArrays(7);
                this.starVBO.unbindBuffer();
                GL11.glDisableClientState((int)32884);
            } else {
                GlStateManager.callList(this.starGLCallList);
            }
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableFog();
        GlStateManager.popMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        double d0 = this.mc.thePlayer.getPositionEyes((float)partialTicks).yCoord - this.theWorld.getHorizon();
        if (d0 < 0.0) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 12.0f, 0.0f);
            if (this.vboEnabled) {
                this.sky2VBO.bindBuffer();
                GL11.glEnableClientState((int)32884);
                GL11.glVertexPointer((int)3, (int)5126, (int)12, (long)0L);
                this.sky2VBO.drawArrays(7);
                this.sky2VBO.unbindBuffer();
                GL11.glDisableClientState((int)32884);
            } else {
                GlStateManager.callList(this.glSkyList2);
            }
            GlStateManager.popMatrix();
            float f18 = 1.0f;
            float f19 = -((float)(d0 + 65.0));
            float f20 = -1.0f;
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos(-1.0, f19, 1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(1.0, f19, 1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(1.0, f19, -1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(-1.0, f19, -1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(1.0, f19, 1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(1.0, f19, -1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(-1.0, f19, -1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(-1.0, f19, 1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
        }
        if (this.theWorld.provider.isSkyColored()) {
            GlStateManager.color(f * 0.2f + 0.04f, f1 * 0.2f + 0.04f, f2 * 0.6f + 0.1f);
        } else {
            GlStateManager.color(f, f1, f2);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, -((float)(d0 - 16.0)), 0.0f);
        GlStateManager.callList(this.glSkyList2);
        GlStateManager.popMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
    }

    public void renderClouds(float partialTicks, int pass) {
        if (!this.mc.theWorld.provider.isSurfaceWorld()) return;
        if (this.mc.gameSettings.func_181147_e() == 2) {
            this.renderCloudsFancy(partialTicks, pass);
            return;
        }
        GlStateManager.disableCull();
        float f = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * (double)partialTicks);
        int i = 32;
        int j = 8;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.renderEngine.bindTexture(locationCloudsPng);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Vec3 vec3 = this.theWorld.getCloudColour(partialTicks);
        float f1 = (float)vec3.xCoord;
        float f2 = (float)vec3.yCoord;
        float f3 = (float)vec3.zCoord;
        if (pass != 2) {
            float f4 = (f1 * 30.0f + f2 * 59.0f + f3 * 11.0f) / 100.0f;
            float f5 = (f1 * 30.0f + f2 * 70.0f) / 100.0f;
            float f6 = (f1 * 30.0f + f3 * 70.0f) / 100.0f;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }
        float f10 = 4.8828125E-4f;
        double d2 = (float)this.cloudTickCounter + partialTicks;
        double d0 = this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * (double)partialTicks + d2 * (double)0.03f;
        double d1 = this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * (double)partialTicks;
        int k = MathHelper.floor_double(d0 / 2048.0);
        int l = MathHelper.floor_double(d1 / 2048.0);
        float f7 = this.theWorld.provider.getCloudHeight() - f + 0.33f;
        float f8 = (float)((d0 -= (double)(k * 2048)) * 4.8828125E-4);
        float f9 = (float)((d1 -= (double)(l * 2048)) * 4.8828125E-4);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        int i1 = -256;
        while (true) {
            if (i1 >= 256) {
                tessellator.draw();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableBlend();
                GlStateManager.enableCull();
                return;
            }
            for (int j1 = -256; j1 < 256; j1 += 32) {
                worldrenderer.pos(i1 + 0, f7, j1 + 32).tex((float)(i1 + 0) * 4.8828125E-4f + f8, (float)(j1 + 32) * 4.8828125E-4f + f9).color(f1, f2, f3, 0.8f).endVertex();
                worldrenderer.pos(i1 + 32, f7, j1 + 32).tex((float)(i1 + 32) * 4.8828125E-4f + f8, (float)(j1 + 32) * 4.8828125E-4f + f9).color(f1, f2, f3, 0.8f).endVertex();
                worldrenderer.pos(i1 + 32, f7, j1 + 0).tex((float)(i1 + 32) * 4.8828125E-4f + f8, (float)(j1 + 0) * 4.8828125E-4f + f9).color(f1, f2, f3, 0.8f).endVertex();
                worldrenderer.pos(i1 + 0, f7, j1 + 0).tex((float)(i1 + 0) * 4.8828125E-4f + f8, (float)(j1 + 0) * 4.8828125E-4f + f9).color(f1, f2, f3, 0.8f).endVertex();
            }
            i1 += 32;
        }
    }

    public boolean hasCloudFog(double x, double y, double z, float partialTicks) {
        return false;
    }

    private void renderCloudsFancy(float partialTicks, int pass) {
        GlStateManager.disableCull();
        float f = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * (double)partialTicks);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f1 = 12.0f;
        float f2 = 4.0f;
        double d0 = (float)this.cloudTickCounter + partialTicks;
        double d1 = (this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * (double)partialTicks + d0 * (double)0.03f) / 12.0;
        double d2 = (this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * (double)partialTicks) / 12.0 + (double)0.33f;
        float f3 = this.theWorld.provider.getCloudHeight() - f + 0.33f;
        int i = MathHelper.floor_double(d1 / 2048.0);
        int j = MathHelper.floor_double(d2 / 2048.0);
        d1 -= (double)(i * 2048);
        d2 -= (double)(j * 2048);
        this.renderEngine.bindTexture(locationCloudsPng);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Vec3 vec3 = this.theWorld.getCloudColour(partialTicks);
        float f4 = (float)vec3.xCoord;
        float f5 = (float)vec3.yCoord;
        float f6 = (float)vec3.zCoord;
        if (pass != 2) {
            float f7 = (f4 * 30.0f + f5 * 59.0f + f6 * 11.0f) / 100.0f;
            float f8 = (f4 * 30.0f + f5 * 70.0f) / 100.0f;
            float f9 = (f4 * 30.0f + f6 * 70.0f) / 100.0f;
            f4 = f7;
            f5 = f8;
            f6 = f9;
        }
        float f26 = f4 * 0.9f;
        float f27 = f5 * 0.9f;
        float f28 = f6 * 0.9f;
        float f10 = f4 * 0.7f;
        float f11 = f5 * 0.7f;
        float f12 = f6 * 0.7f;
        float f13 = f4 * 0.8f;
        float f14 = f5 * 0.8f;
        float f15 = f6 * 0.8f;
        float f16 = 0.00390625f;
        float f17 = (float)MathHelper.floor_double(d1) * 0.00390625f;
        float f18 = (float)MathHelper.floor_double(d2) * 0.00390625f;
        float f19 = (float)(d1 - (double)MathHelper.floor_double(d1));
        float f20 = (float)(d2 - (double)MathHelper.floor_double(d2));
        int k = 8;
        int l = 4;
        float f21 = 9.765625E-4f;
        GlStateManager.scale(12.0f, 1.0f, 12.0f);
        int i1 = 0;
        block5: while (true) {
            if (i1 >= 2) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableBlend();
                GlStateManager.enableCull();
                return;
            }
            if (i1 == 0) {
                GlStateManager.colorMask(false, false, false, false);
            } else {
                switch (pass) {
                    case 0: {
                        GlStateManager.colorMask(false, true, true, true);
                        break;
                    }
                    case 1: {
                        GlStateManager.colorMask(true, false, false, true);
                        break;
                    }
                    case 2: {
                        GlStateManager.colorMask(true, true, true, true);
                        break;
                    }
                }
            }
            int j1 = -3;
            while (true) {
                if (j1 <= 4) {
                } else {
                    ++i1;
                    continue block5;
                }
                for (int k1 = -3; k1 <= 4; ++k1) {
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                    float f22 = j1 * 8;
                    float f23 = k1 * 8;
                    float f24 = f22 - f19;
                    float f25 = f23 - f20;
                    if (f3 > -5.0f) {
                        worldrenderer.pos(f24 + 0.0f, f3 + 0.0f, f25 + 8.0f).tex((f22 + 0.0f) * 0.00390625f + f17, (f23 + 8.0f) * 0.00390625f + f18).color(f10, f11, f12, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f24 + 8.0f, f3 + 0.0f, f25 + 8.0f).tex((f22 + 8.0f) * 0.00390625f + f17, (f23 + 8.0f) * 0.00390625f + f18).color(f10, f11, f12, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f24 + 8.0f, f3 + 0.0f, f25 + 0.0f).tex((f22 + 8.0f) * 0.00390625f + f17, (f23 + 0.0f) * 0.00390625f + f18).color(f10, f11, f12, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f24 + 0.0f, f3 + 0.0f, f25 + 0.0f).tex((f22 + 0.0f) * 0.00390625f + f17, (f23 + 0.0f) * 0.00390625f + f18).color(f10, f11, f12, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                    }
                    if (f3 <= 5.0f) {
                        worldrenderer.pos(f24 + 0.0f, f3 + 4.0f - 9.765625E-4f, f25 + 8.0f).tex((f22 + 0.0f) * 0.00390625f + f17, (f23 + 8.0f) * 0.00390625f + f18).color(f4, f5, f6, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f24 + 8.0f, f3 + 4.0f - 9.765625E-4f, f25 + 8.0f).tex((f22 + 8.0f) * 0.00390625f + f17, (f23 + 8.0f) * 0.00390625f + f18).color(f4, f5, f6, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f24 + 8.0f, f3 + 4.0f - 9.765625E-4f, f25 + 0.0f).tex((f22 + 8.0f) * 0.00390625f + f17, (f23 + 0.0f) * 0.00390625f + f18).color(f4, f5, f6, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f24 + 0.0f, f3 + 4.0f - 9.765625E-4f, f25 + 0.0f).tex((f22 + 0.0f) * 0.00390625f + f17, (f23 + 0.0f) * 0.00390625f + f18).color(f4, f5, f6, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                    }
                    if (j1 > -1) {
                        for (int l1 = 0; l1 < 8; ++l1) {
                            worldrenderer.pos(f24 + (float)l1 + 0.0f, f3 + 0.0f, f25 + 8.0f).tex((f22 + (float)l1 + 0.5f) * 0.00390625f + f17, (f23 + 8.0f) * 0.00390625f + f18).color(f26, f27, f28, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f24 + (float)l1 + 0.0f, f3 + 4.0f, f25 + 8.0f).tex((f22 + (float)l1 + 0.5f) * 0.00390625f + f17, (f23 + 8.0f) * 0.00390625f + f18).color(f26, f27, f28, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f24 + (float)l1 + 0.0f, f3 + 4.0f, f25 + 0.0f).tex((f22 + (float)l1 + 0.5f) * 0.00390625f + f17, (f23 + 0.0f) * 0.00390625f + f18).color(f26, f27, f28, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f24 + (float)l1 + 0.0f, f3 + 0.0f, f25 + 0.0f).tex((f22 + (float)l1 + 0.5f) * 0.00390625f + f17, (f23 + 0.0f) * 0.00390625f + f18).color(f26, f27, f28, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                        }
                    }
                    if (j1 <= 1) {
                        for (int i2 = 0; i2 < 8; ++i2) {
                            worldrenderer.pos(f24 + (float)i2 + 1.0f - 9.765625E-4f, f3 + 0.0f, f25 + 8.0f).tex((f22 + (float)i2 + 0.5f) * 0.00390625f + f17, (f23 + 8.0f) * 0.00390625f + f18).color(f26, f27, f28, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f24 + (float)i2 + 1.0f - 9.765625E-4f, f3 + 4.0f, f25 + 8.0f).tex((f22 + (float)i2 + 0.5f) * 0.00390625f + f17, (f23 + 8.0f) * 0.00390625f + f18).color(f26, f27, f28, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f24 + (float)i2 + 1.0f - 9.765625E-4f, f3 + 4.0f, f25 + 0.0f).tex((f22 + (float)i2 + 0.5f) * 0.00390625f + f17, (f23 + 0.0f) * 0.00390625f + f18).color(f26, f27, f28, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f24 + (float)i2 + 1.0f - 9.765625E-4f, f3 + 0.0f, f25 + 0.0f).tex((f22 + (float)i2 + 0.5f) * 0.00390625f + f17, (f23 + 0.0f) * 0.00390625f + f18).color(f26, f27, f28, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                        }
                    }
                    if (k1 > -1) {
                        for (int j2 = 0; j2 < 8; ++j2) {
                            worldrenderer.pos(f24 + 0.0f, f3 + 4.0f, f25 + (float)j2 + 0.0f).tex((f22 + 0.0f) * 0.00390625f + f17, (f23 + (float)j2 + 0.5f) * 0.00390625f + f18).color(f13, f14, f15, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            worldrenderer.pos(f24 + 8.0f, f3 + 4.0f, f25 + (float)j2 + 0.0f).tex((f22 + 8.0f) * 0.00390625f + f17, (f23 + (float)j2 + 0.5f) * 0.00390625f + f18).color(f13, f14, f15, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            worldrenderer.pos(f24 + 8.0f, f3 + 0.0f, f25 + (float)j2 + 0.0f).tex((f22 + 8.0f) * 0.00390625f + f17, (f23 + (float)j2 + 0.5f) * 0.00390625f + f18).color(f13, f14, f15, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            worldrenderer.pos(f24 + 0.0f, f3 + 0.0f, f25 + (float)j2 + 0.0f).tex((f22 + 0.0f) * 0.00390625f + f17, (f23 + (float)j2 + 0.5f) * 0.00390625f + f18).color(f13, f14, f15, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                        }
                    }
                    if (k1 <= 1) {
                        for (int k2 = 0; k2 < 8; ++k2) {
                            worldrenderer.pos(f24 + 0.0f, f3 + 4.0f, f25 + (float)k2 + 1.0f - 9.765625E-4f).tex((f22 + 0.0f) * 0.00390625f + f17, (f23 + (float)k2 + 0.5f) * 0.00390625f + f18).color(f13, f14, f15, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            worldrenderer.pos(f24 + 8.0f, f3 + 4.0f, f25 + (float)k2 + 1.0f - 9.765625E-4f).tex((f22 + 8.0f) * 0.00390625f + f17, (f23 + (float)k2 + 0.5f) * 0.00390625f + f18).color(f13, f14, f15, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            worldrenderer.pos(f24 + 8.0f, f3 + 0.0f, f25 + (float)k2 + 1.0f - 9.765625E-4f).tex((f22 + 8.0f) * 0.00390625f + f17, (f23 + (float)k2 + 0.5f) * 0.00390625f + f18).color(f13, f14, f15, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            worldrenderer.pos(f24 + 0.0f, f3 + 0.0f, f25 + (float)k2 + 1.0f - 9.765625E-4f).tex((f22 + 0.0f) * 0.00390625f + f17, (f23 + (float)k2 + 0.5f) * 0.00390625f + f18).color(f13, f14, f15, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                        }
                    }
                    tessellator.draw();
                }
                ++j1;
            }
            break;
        }
    }

    public void updateChunks(long finishTimeNano) {
        long i;
        this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(finishTimeNano);
        if (this.chunksToUpdate.isEmpty()) return;
        Iterator<RenderChunk> iterator = this.chunksToUpdate.iterator();
        do {
            if (!iterator.hasNext()) return;
            RenderChunk renderchunk = iterator.next();
            if (!this.renderDispatcher.updateChunkLater(renderchunk)) {
                return;
            }
            renderchunk.setNeedsUpdate(false);
            iterator.remove();
        } while ((i = finishTimeNano - System.nanoTime()) >= 0L);
    }

    public void renderWorldBorder(Entity p_180449_1_, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        WorldBorder worldborder = this.theWorld.getWorldBorder();
        double d0 = this.mc.gameSettings.renderDistanceChunks * 16;
        if (!(p_180449_1_.posX >= worldborder.maxX() - d0 || p_180449_1_.posX <= worldborder.minX() + d0 || p_180449_1_.posZ >= worldborder.maxZ() - d0)) {
            if (!(p_180449_1_.posZ <= worldborder.minZ() + d0)) return;
        }
        double d1 = 1.0 - worldborder.getClosestDistance(p_180449_1_) / d0;
        d1 = Math.pow(d1, 4.0);
        double d2 = p_180449_1_.lastTickPosX + (p_180449_1_.posX - p_180449_1_.lastTickPosX) * (double)partialTicks;
        double d3 = p_180449_1_.lastTickPosY + (p_180449_1_.posY - p_180449_1_.lastTickPosY) * (double)partialTicks;
        double d4 = p_180449_1_.lastTickPosZ + (p_180449_1_.posZ - p_180449_1_.lastTickPosZ) * (double)partialTicks;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        this.renderEngine.bindTexture(locationForcefieldPng);
        GlStateManager.depthMask(false);
        GlStateManager.pushMatrix();
        int i = worldborder.getStatus().getID();
        float f = (float)(i >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(i >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(i & 0xFF) / 255.0f;
        GlStateManager.color(f, f1, f2, (float)d1);
        GlStateManager.doPolygonOffset(-3.0f, -3.0f);
        GlStateManager.enablePolygonOffset();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        float f3 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        float f6 = 128.0f;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.setTranslation(-d2, -d3, -d4);
        double d5 = Math.max((double)MathHelper.floor_double(d4 - d0), worldborder.minZ());
        double d6 = Math.min((double)MathHelper.ceiling_double_int(d4 + d0), worldborder.maxZ());
        if (d2 > worldborder.maxX() - d0) {
            float f7 = 0.0f;
            for (double d7 = d5; d7 < d6; d7 += 1.0, f7 += 0.5f) {
                double d8 = Math.min(1.0, d6 - d7);
                float f8 = (float)d8 * 0.5f;
                worldrenderer.pos(worldborder.maxX(), 256.0, d7).tex(f3 + f7, f3 + 0.0f).endVertex();
                worldrenderer.pos(worldborder.maxX(), 256.0, d7 + d8).tex(f3 + f8 + f7, f3 + 0.0f).endVertex();
                worldrenderer.pos(worldborder.maxX(), 0.0, d7 + d8).tex(f3 + f8 + f7, f3 + 128.0f).endVertex();
                worldrenderer.pos(worldborder.maxX(), 0.0, d7).tex(f3 + f7, f3 + 128.0f).endVertex();
            }
        }
        if (d2 < worldborder.minX() + d0) {
            float f9 = 0.0f;
            for (double d9 = d5; d9 < d6; d9 += 1.0, f9 += 0.5f) {
                double d12 = Math.min(1.0, d6 - d9);
                float f12 = (float)d12 * 0.5f;
                worldrenderer.pos(worldborder.minX(), 256.0, d9).tex(f3 + f9, f3 + 0.0f).endVertex();
                worldrenderer.pos(worldborder.minX(), 256.0, d9 + d12).tex(f3 + f12 + f9, f3 + 0.0f).endVertex();
                worldrenderer.pos(worldborder.minX(), 0.0, d9 + d12).tex(f3 + f12 + f9, f3 + 128.0f).endVertex();
                worldrenderer.pos(worldborder.minX(), 0.0, d9).tex(f3 + f9, f3 + 128.0f).endVertex();
            }
        }
        d5 = Math.max((double)MathHelper.floor_double(d2 - d0), worldborder.minX());
        d6 = Math.min((double)MathHelper.ceiling_double_int(d2 + d0), worldborder.maxX());
        if (d4 > worldborder.maxZ() - d0) {
            float f10 = 0.0f;
            for (double d10 = d5; d10 < d6; d10 += 1.0, f10 += 0.5f) {
                double d13 = Math.min(1.0, d6 - d10);
                float f13 = (float)d13 * 0.5f;
                worldrenderer.pos(d10, 256.0, worldborder.maxZ()).tex(f3 + f10, f3 + 0.0f).endVertex();
                worldrenderer.pos(d10 + d13, 256.0, worldborder.maxZ()).tex(f3 + f13 + f10, f3 + 0.0f).endVertex();
                worldrenderer.pos(d10 + d13, 0.0, worldborder.maxZ()).tex(f3 + f13 + f10, f3 + 128.0f).endVertex();
                worldrenderer.pos(d10, 0.0, worldborder.maxZ()).tex(f3 + f10, f3 + 128.0f).endVertex();
            }
        }
        if (d4 < worldborder.minZ() + d0) {
            float f11 = 0.0f;
            for (double d11 = d5; d11 < d6; d11 += 1.0, f11 += 0.5f) {
                double d14 = Math.min(1.0, d6 - d11);
                float f14 = (float)d14 * 0.5f;
                worldrenderer.pos(d11, 256.0, worldborder.minZ()).tex(f3 + f11, f3 + 0.0f).endVertex();
                worldrenderer.pos(d11 + d14, 256.0, worldborder.minZ()).tex(f3 + f14 + f11, f3 + 0.0f).endVertex();
                worldrenderer.pos(d11 + d14, 0.0, worldborder.minZ()).tex(f3 + f14 + f11, f3 + 128.0f).endVertex();
                worldrenderer.pos(d11, 0.0, worldborder.minZ()).tex(f3 + f11, f3 + 128.0f).endVertex();
            }
        }
        tessellator.draw();
        worldrenderer.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.enableCull();
        GlStateManager.disableAlpha();
        GlStateManager.doPolygonOffset(0.0f, 0.0f);
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
    }

    private void preRenderDamagedBlocks() {
        GlStateManager.tryBlendFuncSeparate(774, 768, 1, 0);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
        GlStateManager.doPolygonOffset(-3.0f, -3.0f);
        GlStateManager.enablePolygonOffset();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
    }

    private void postRenderDamagedBlocks() {
        GlStateManager.disableAlpha();
        GlStateManager.doPolygonOffset(0.0f, 0.0f);
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    public void drawBlockDamageTexture(Tessellator tessellatorIn, WorldRenderer worldRendererIn, Entity entityIn, float partialTicks) {
        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        if (this.damagedBlocks.isEmpty()) return;
        this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        this.preRenderDamagedBlocks();
        worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
        worldRendererIn.setTranslation(-d0, -d1, -d2);
        worldRendererIn.markDirty();
        Iterator<DestroyBlockProgress> iterator = this.damagedBlocks.values().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                tessellatorIn.draw();
                worldRendererIn.setTranslation(0.0, 0.0, 0.0);
                this.postRenderDamagedBlocks();
                return;
            }
            DestroyBlockProgress destroyblockprogress = iterator.next();
            BlockPos blockpos = destroyblockprogress.getPosition();
            double d3 = (double)blockpos.getX() - d0;
            double d4 = (double)blockpos.getY() - d1;
            double d5 = (double)blockpos.getZ() - d2;
            Block block = this.theWorld.getBlockState(blockpos).getBlock();
            if (block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSign || block instanceof BlockSkull) continue;
            if (d3 * d3 + d4 * d4 + d5 * d5 > 1024.0) {
                iterator.remove();
                continue;
            }
            IBlockState iblockstate = this.theWorld.getBlockState(blockpos);
            if (iblockstate.getBlock().getMaterial() == Material.air) continue;
            int i = destroyblockprogress.getPartialBlockDamage();
            TextureAtlasSprite textureatlassprite = this.destroyBlockIcons[i];
            BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
            blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, this.theWorld);
        }
    }

    public void drawSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int p_72731_3_, float partialTicks) {
        if (p_72731_3_ != 0) return;
        if (movingObjectPositionIn.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.4f);
        GL11.glLineWidth((float)2.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        float f = 0.002f;
        BlockPos blockpos = movingObjectPositionIn.getBlockPos();
        Block block = this.theWorld.getBlockState(blockpos).getBlock();
        if (block.getMaterial() != Material.air && this.theWorld.getWorldBorder().contains(blockpos)) {
            block.setBlockBoundsBasedOnState(this.theWorld, blockpos);
            double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
            double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
            double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
            RenderGlobal.func_181561_a(block.getSelectedBoundingBox(this.theWorld, blockpos).expand(0.002f, 0.002f, 0.002f).offset(-d0, -d1, -d2));
        }
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void func_181561_a(AxisAlignedBB p_181561_0_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(1, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();
    }

    public static void func_181563_a(AxisAlignedBB p_181563_0_, int p_181563_1_, int p_181563_2_, int p_181563_3_, int p_181563_4_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        tessellator.draw();
        worldrenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        tessellator.draw();
    }

    private void markBlocksForUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
        this.viewFrustum.markBlocksForUpdate(x1, y1, z1, x2, y2, z2);
    }

    @Override
    public void markBlockForUpdate(BlockPos pos) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        this.markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
    }

    @Override
    public void notifyLightSet(BlockPos pos) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        this.markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
    }

    @Override
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
        this.markBlocksForUpdate(x1 - 1, y1 - 1, z1 - 1, x2 + 1, y2 + 1, z2 + 1);
    }

    @Override
    public void playRecord(String recordName, BlockPos blockPosIn) {
        ISound isound = this.mapSoundPositions.get(blockPosIn);
        if (isound != null) {
            this.mc.getSoundHandler().stopSound(isound);
            this.mapSoundPositions.remove(blockPosIn);
        }
        if (recordName == null) return;
        ItemRecord itemrecord = ItemRecord.getRecord(recordName);
        if (itemrecord != null) {
            this.mc.ingameGUI.setRecordPlayingMessage(itemrecord.getRecordNameLocal());
        }
        PositionedSoundRecord positionedsoundrecord = PositionedSoundRecord.create(new ResourceLocation(recordName), blockPosIn.getX(), blockPosIn.getY(), blockPosIn.getZ());
        this.mapSoundPositions.put(blockPosIn, positionedsoundrecord);
        this.mc.getSoundHandler().playSound(positionedsoundrecord);
    }

    @Override
    public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {
    }

    @Override
    public void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float pitch) {
    }

    @Override
    public void spawnParticle(int particleID, boolean ignoreRange, final double xCoord, final double yCoord, final double zCoord, double xOffset, double yOffset, double zOffset, int ... p_180442_15_) {
        try {
            this.spawnEntityFX(particleID, ignoreRange, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, p_180442_15_);
            return;
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while adding particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being added");
            crashreportcategory.addCrashSection("ID", particleID);
            if (p_180442_15_ != null) {
                crashreportcategory.addCrashSection("Parameters", p_180442_15_);
            }
            crashreportcategory.addCrashSectionCallable("Position", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(xCoord, yCoord, zCoord);
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    private void spawnParticle(EnumParticleTypes particleIn, double p_174972_2_, double p_174972_4_, double p_174972_6_, double p_174972_8_, double p_174972_10_, double p_174972_12_, int ... p_174972_14_) {
        this.spawnParticle(particleIn.getParticleID(), particleIn.getShouldIgnoreRange(), p_174972_2_, p_174972_4_, p_174972_6_, p_174972_8_, p_174972_10_, p_174972_12_, p_174972_14_);
    }

    private EntityFX spawnEntityFX(int p_174974_1_, boolean ignoreRange, double p_174974_3_, double p_174974_5_, double p_174974_7_, double p_174974_9_, double p_174974_11_, double p_174974_13_, int ... p_174974_15_) {
        if (this.mc == null) return null;
        if (this.mc.getRenderViewEntity() == null) return null;
        if (this.mc.effectRenderer == null) return null;
        int i = this.mc.gameSettings.particleSetting;
        if (i == 1 && this.theWorld.rand.nextInt(3) == 0) {
            i = 2;
        }
        double d0 = this.mc.getRenderViewEntity().posX - p_174974_3_;
        double d1 = this.mc.getRenderViewEntity().posY - p_174974_5_;
        double d2 = this.mc.getRenderViewEntity().posZ - p_174974_7_;
        if (ignoreRange) {
            return this.mc.effectRenderer.spawnEffectParticle(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
        }
        double d3 = 16.0;
        if (d0 * d0 + d1 * d1 + d2 * d2 > 256.0) {
            return null;
        }
        if (i > 1) {
            return null;
        }
        EntityFX entityFX = this.mc.effectRenderer.spawnEffectParticle(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
        return entityFX;
    }

    @Override
    public void onEntityAdded(Entity entityIn) {
    }

    @Override
    public void onEntityRemoved(Entity entityIn) {
    }

    public void deleteAllDisplayLists() {
    }

    @Override
    public void broadcastSound(int p_180440_1_, BlockPos p_180440_2_, int p_180440_3_) {
        switch (p_180440_1_) {
            case 1013: 
            case 1018: {
                if (this.mc.getRenderViewEntity() == null) return;
                double d0 = (double)p_180440_2_.getX() - this.mc.getRenderViewEntity().posX;
                double d1 = (double)p_180440_2_.getY() - this.mc.getRenderViewEntity().posY;
                double d2 = (double)p_180440_2_.getZ() - this.mc.getRenderViewEntity().posZ;
                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                double d4 = this.mc.getRenderViewEntity().posX;
                double d5 = this.mc.getRenderViewEntity().posY;
                double d6 = this.mc.getRenderViewEntity().posZ;
                if (d3 > 0.0) {
                    d4 += d0 / d3 * 2.0;
                    d5 += d1 / d3 * 2.0;
                    d6 += d2 / d3 * 2.0;
                }
                if (p_180440_1_ == 1013) {
                    this.theWorld.playSound(d4, d5, d6, "mob.wither.spawn", 1.0f, 1.0f, false);
                    return;
                }
                this.theWorld.playSound(d4, d5, d6, "mob.enderdragon.end", 5.0f, 1.0f, false);
                return;
            }
        }
    }

    @Override
    public void playAuxSFX(EntityPlayer player, int sfxType, BlockPos blockPosIn, int p_180439_4_) {
        Random random = this.theWorld.rand;
        switch (sfxType) {
            case 1000: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0f, 1.0f, false);
                return;
            }
            case 1001: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0f, 1.2f, false);
                return;
            }
            case 1002: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.bow", 1.0f, 1.2f, false);
                return;
            }
            case 1003: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.door_open", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                return;
            }
            case 1004: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.fizz", 0.5f, 2.6f + (random.nextFloat() - random.nextFloat()) * 0.8f, false);
                return;
            }
            case 1005: {
                if (Item.getItemById(p_180439_4_) instanceof ItemRecord) {
                    this.theWorld.playRecord(blockPosIn, "records." + ((ItemRecord)Item.getItemById((int)p_180439_4_)).recordName);
                    return;
                }
                this.theWorld.playRecord(blockPosIn, null);
                return;
            }
            case 1006: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.door_close", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                return;
            }
            case 1007: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.charge", 10.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                return;
            }
            case 1008: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 10.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                return;
            }
            case 1009: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                return;
            }
            case 1010: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.wood", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                return;
            }
            case 1011: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.metal", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                return;
            }
            case 1012: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.woodbreak", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                return;
            }
            case 1014: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.wither.shoot", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                return;
            }
            case 1015: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.bat.takeoff", 0.05f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                return;
            }
            case 1016: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.infect", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                return;
            }
            case 1017: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.unfect", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                return;
            }
            case 1020: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_break", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                return;
            }
            case 1021: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_use", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                return;
            }
            case 1022: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_land", 0.3f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                return;
            }
            case 2000: {
                int l = p_180439_4_ % 3 - 1;
                int i = p_180439_4_ / 3 % 3 - 1;
                double d15 = (double)blockPosIn.getX() + (double)l * 0.6 + 0.5;
                double d17 = (double)blockPosIn.getY() + 0.5;
                double d19 = (double)blockPosIn.getZ() + (double)i * 0.6 + 0.5;
                int k1 = 0;
                while (k1 < 10) {
                    double d20 = random.nextDouble() * 0.2 + 0.01;
                    double d21 = d15 + (double)l * 0.01 + (random.nextDouble() - 0.5) * (double)i * 0.5;
                    double d4 = d17 + (random.nextDouble() - 0.5) * 0.5;
                    double d6 = d19 + (double)i * 0.01 + (random.nextDouble() - 0.5) * (double)l * 0.5;
                    double d8 = (double)l * d20 + random.nextGaussian() * 0.01;
                    double d10 = -0.03 + random.nextGaussian() * 0.01;
                    double d12 = (double)i * d20 + random.nextGaussian() * 0.01;
                    this.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d21, d4, d6, d8, d10, d12, new int[0]);
                    ++k1;
                }
                return;
            }
            case 2001: {
                Block block = Block.getBlockById(p_180439_4_ & 0xFFF);
                if (block.getMaterial() != Material.air) {
                    this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(block.stepSound.getBreakSound()), (block.stepSound.getVolume() + 1.0f) / 2.0f, block.stepSound.getFrequency() * 0.8f, (float)blockPosIn.getX() + 0.5f, (float)blockPosIn.getY() + 0.5f, (float)blockPosIn.getZ() + 0.5f));
                }
                this.mc.effectRenderer.addBlockDestroyEffects(blockPosIn, block.getStateFromMeta(p_180439_4_ >> 12 & 0xFF));
                return;
            }
            case 2002: {
                double d13 = blockPosIn.getX();
                double d14 = blockPosIn.getY();
                double d16 = blockPosIn.getZ();
                for (int i1 = 0; i1 < 8; ++i1) {
                    this.spawnParticle(EnumParticleTypes.ITEM_CRACK, d13, d14, d16, random.nextGaussian() * 0.15, random.nextDouble() * 0.2, random.nextGaussian() * 0.15, Item.getIdFromItem(Items.potionitem), p_180439_4_);
                }
                int j1 = Items.potionitem.getColorFromDamage(p_180439_4_);
                float f = (float)(j1 >> 16 & 0xFF) / 255.0f;
                float f1 = (float)(j1 >> 8 & 0xFF) / 255.0f;
                float f2 = (float)(j1 >> 0 & 0xFF) / 255.0f;
                EnumParticleTypes enumparticletypes = EnumParticleTypes.SPELL;
                if (Items.potionitem.isEffectInstant(p_180439_4_)) {
                    enumparticletypes = EnumParticleTypes.SPELL_INSTANT;
                }
                int l1 = 0;
                while (true) {
                    if (l1 >= 100) {
                        this.theWorld.playSoundAtPos(blockPosIn, "game.potion.smash", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                        return;
                    }
                    double d22 = random.nextDouble() * 4.0;
                    double d23 = random.nextDouble() * Math.PI * 2.0;
                    double d24 = Math.cos(d23) * d22;
                    double d9 = 0.01 + random.nextDouble() * 0.5;
                    double d11 = Math.sin(d23) * d22;
                    EntityFX entityfx = this.spawnEntityFX(enumparticletypes.getParticleID(), enumparticletypes.getShouldIgnoreRange(), d13 + d24 * 0.1, d14 + 0.3, d16 + d11 * 0.1, d24, d9, d11, new int[0]);
                    if (entityfx != null) {
                        float f3 = 0.75f + random.nextFloat() * 0.25f;
                        entityfx.setRBGColorF(f * f3, f1 * f3, f2 * f3);
                        entityfx.multiplyVelocity((float)d22);
                    }
                    ++l1;
                }
            }
            case 2003: {
                double d0 = (double)blockPosIn.getX() + 0.5;
                double d1 = blockPosIn.getY();
                double d2 = (double)blockPosIn.getZ() + 0.5;
                for (int j = 0; j < 8; ++j) {
                    this.spawnParticle(EnumParticleTypes.ITEM_CRACK, d0, d1, d2, random.nextGaussian() * 0.15, random.nextDouble() * 0.2, random.nextGaussian() * 0.15, Item.getIdFromItem(Items.ender_eye));
                }
                double d18 = 0.0;
                while (d18 < Math.PI * 2) {
                    this.spawnParticle(EnumParticleTypes.PORTAL, d0 + Math.cos(d18) * 5.0, d1 - 0.4, d2 + Math.sin(d18) * 5.0, Math.cos(d18) * -5.0, 0.0, Math.sin(d18) * -5.0, new int[0]);
                    this.spawnParticle(EnumParticleTypes.PORTAL, d0 + Math.cos(d18) * 5.0, d1 - 0.4, d2 + Math.sin(d18) * 5.0, Math.cos(d18) * -7.0, 0.0, Math.sin(d18) * -7.0, new int[0]);
                    d18 += 0.15707963267948966;
                }
                return;
            }
            case 2004: {
                int k = 0;
                while (k < 20) {
                    double d3 = (double)blockPosIn.getX() + 0.5 + ((double)this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    double d5 = (double)blockPosIn.getY() + 0.5 + ((double)this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    double d7 = (double)blockPosIn.getZ() + 0.5 + ((double)this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    this.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d5, d7, 0.0, 0.0, 0.0, new int[0]);
                    this.theWorld.spawnParticle(EnumParticleTypes.FLAME, d3, d5, d7, 0.0, 0.0, 0.0, new int[0]);
                    ++k;
                }
                return;
            }
            case 2005: {
                ItemDye.spawnBonemealParticles(this.theWorld, blockPosIn, p_180439_4_);
                return;
            }
        }
    }

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
        if (progress >= 0 && progress < 10) {
            DestroyBlockProgress destroyblockprogress = this.damagedBlocks.get(breakerId);
            if (destroyblockprogress == null || destroyblockprogress.getPosition().getX() != pos.getX() || destroyblockprogress.getPosition().getY() != pos.getY() || destroyblockprogress.getPosition().getZ() != pos.getZ()) {
                destroyblockprogress = new DestroyBlockProgress(breakerId, pos);
                this.damagedBlocks.put(breakerId, destroyblockprogress);
            }
            destroyblockprogress.setPartialBlockDamage(progress);
            destroyblockprogress.setCloudUpdateTick(this.cloudTickCounter);
            return;
        }
        this.damagedBlocks.remove(breakerId);
    }

    public void setDisplayListEntitiesDirty() {
        this.displayListEntitiesDirty = true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void func_181023_a(Collection<TileEntity> p_181023_1_, Collection<TileEntity> p_181023_2_) {
        Set<TileEntity> set = this.field_181024_n;
        synchronized (set) {
            this.field_181024_n.removeAll(p_181023_1_);
            this.field_181024_n.addAll(p_181023_2_);
            return;
        }
    }

    class ContainerLocalRenderInformation {
        final RenderChunk renderChunk;
        final EnumFacing facing;
        final Set<EnumFacing> setFacing = EnumSet.noneOf(EnumFacing.class);
        final int counter;

        private ContainerLocalRenderInformation(RenderChunk renderChunkIn, EnumFacing facingIn, int counterIn) {
            this.renderChunk = renderChunkIn;
            this.facing = facingIn;
            this.counter = counterIn;
        }
    }
}

