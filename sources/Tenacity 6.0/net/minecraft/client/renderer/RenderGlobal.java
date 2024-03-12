// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import net.optifine.reflect.Reflector;
import net.minecraft.item.ItemDye;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.optifine.RandomEntities;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReportCategory;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.AxisAlignedBB;
import net.optifine.model.BlockModelUtils;
import dev.tenacity.module.impl.movement.Flight;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.block.material.Material;
import net.minecraft.world.border.WorldBorder;
import net.optifine.util.RenderChunkUtils;
import net.optifine.CustomSky;
import net.minecraft.world.IBlockAccess;
import net.optifine.CustomColors;
import net.minecraft.util.Vec3;
import net.optifine.shaders.gui.GuiShaderOptions;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import net.optifine.shaders.ShadersRender;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.Matrix4f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.chunk.VisGraph;
import org.lwjgl.util.vector.Vector3f;
import java.util.Collection;
import net.optifine.util.ChunkUtils;
import net.optifine.shaders.ShadowUtils;
import net.optifine.Lagometer;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.block.Block;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.world.chunk.Chunk;
import java.util.Iterator;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.culling.ICamera;
import net.optifine.SmartAnimations;
import net.optifine.shaders.Shaders;
import net.minecraft.block.state.IBlockState;
import net.optifine.render.ChunkVisibility;
import net.optifine.DynamicLights;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.src.Config;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import org.lwjgl.opengl.GL11;
import net.minecraft.init.Blocks;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.optifine.render.RenderEnv;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.chunk.IChunkProvider;
import java.util.Deque;
import net.minecraft.entity.Entity;
import net.optifine.render.CloudRenderer;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.util.Vector3d;
import org.lwjgl.util.vector.Vector4f;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.audio.ISound;
import net.minecraft.util.BlockPos;
import java.util.Map;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.tileentity.TileEntity;
import java.util.List;
import net.minecraft.client.renderer.chunk.RenderChunk;
import java.util.Set;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.world.IWorldAccess;

public class RenderGlobal implements IWorldAccess, IResourceManagerReloadListener
{
    private static final Logger logger;
    private static final ResourceLocation locationMoonPhasesPng;
    private static final ResourceLocation locationSunPng;
    private static final ResourceLocation locationCloudsPng;
    private static final ResourceLocation locationEndSkyPng;
    private static final ResourceLocation locationForcefieldPng;
    public final Minecraft mc;
    private final TextureManager renderEngine;
    private final RenderManager renderManager;
    private WorldClient theWorld;
    private Set<RenderChunk> chunksToUpdate;
    private List<ContainerLocalRenderInformation> renderInfos;
    private final Set<TileEntity> setTileEntities;
    private ViewFrustum viewFrustum;
    private int starGLCallList;
    private int glSkyList;
    private int glSkyList2;
    private VertexFormat vertexBufferFormat;
    private VertexBuffer starVBO;
    private VertexBuffer skyVBO;
    private VertexBuffer sky2VBO;
    private int cloudTickCounter;
    public final Map<Integer, DestroyBlockProgress> damagedBlocks;
    private final Map<BlockPos, ISound> mapSoundPositions;
    private final TextureAtlasSprite[] destroyBlockIcons;
    private Framebuffer entityOutlineFramebuffer;
    private ShaderGroup entityOutlineShader;
    private double frustumUpdatePosX;
    private double frustumUpdatePosY;
    private double frustumUpdatePosZ;
    private int frustumUpdatePosChunkX;
    private int frustumUpdatePosChunkY;
    private int frustumUpdatePosChunkZ;
    private double lastViewEntityX;
    private double lastViewEntityY;
    private double lastViewEntityZ;
    private double lastViewEntityPitch;
    private double lastViewEntityYaw;
    private final ChunkRenderDispatcher renderDispatcher;
    private ChunkRenderContainer renderContainer;
    private int renderDistanceChunks;
    private int renderEntitiesStartupCounter;
    private int countEntitiesTotal;
    private int countEntitiesRendered;
    private int countEntitiesHidden;
    private boolean debugFixTerrainFrustum;
    private ClippingHelper debugFixedClippingHelper;
    private final Vector4f[] debugTerrainMatrix;
    private final Vector3d debugTerrainFrustumPosition;
    private boolean vboEnabled;
    IRenderChunkFactory renderChunkFactory;
    private double prevRenderSortX;
    private double prevRenderSortY;
    private double prevRenderSortZ;
    public boolean displayListEntitiesDirty;
    private CloudRenderer cloudRenderer;
    public Entity renderedEntity;
    public Set chunksToResortTransparency;
    public Set chunksToUpdateForced;
    private Deque visibilityDeque;
    private List renderInfosEntities;
    private List renderInfosTileEntities;
    private List renderInfosNormal;
    private List renderInfosEntitiesNormal;
    private List renderInfosTileEntitiesNormal;
    private List renderInfosShadow;
    private List renderInfosEntitiesShadow;
    private List renderInfosTileEntitiesShadow;
    private int renderDistance;
    private int renderDistanceSq;
    private static final Set SET_ALL_FACINGS;
    private int countTileEntitiesRendered;
    private IChunkProvider worldChunkProvider;
    private LongHashMap worldChunkProviderMap;
    private int countLoadedChunksPrev;
    private RenderEnv renderEnv;
    public boolean renderOverlayDamaged;
    public boolean renderOverlayEyes;
    private boolean firstWorldLoad;
    private static int renderEntitiesCounter;
    
    public RenderGlobal(final Minecraft mcIn) {
        this.chunksToUpdate = (Set<RenderChunk>)Sets.newLinkedHashSet();
        this.renderInfos = (List<ContainerLocalRenderInformation>)Lists.newArrayListWithCapacity(69696);
        this.setTileEntities = (Set<TileEntity>)Sets.newHashSet();
        this.starGLCallList = -1;
        this.glSkyList = -1;
        this.glSkyList2 = -1;
        this.damagedBlocks = (Map<Integer, DestroyBlockProgress>)Maps.newHashMap();
        this.mapSoundPositions = (Map<BlockPos, ISound>)Maps.newHashMap();
        this.destroyBlockIcons = new TextureAtlasSprite[10];
        this.frustumUpdatePosX = Double.MIN_VALUE;
        this.frustumUpdatePosY = Double.MIN_VALUE;
        this.frustumUpdatePosZ = Double.MIN_VALUE;
        this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
        this.lastViewEntityX = Double.MIN_VALUE;
        this.lastViewEntityY = Double.MIN_VALUE;
        this.lastViewEntityZ = Double.MIN_VALUE;
        this.lastViewEntityPitch = Double.MIN_VALUE;
        this.lastViewEntityYaw = Double.MIN_VALUE;
        this.renderDispatcher = new ChunkRenderDispatcher();
        this.renderDistanceChunks = -1;
        this.renderEntitiesStartupCounter = 2;
        this.debugFixTerrainFrustum = false;
        this.debugTerrainMatrix = new Vector4f[8];
        this.debugTerrainFrustumPosition = new Vector3d();
        this.vboEnabled = false;
        this.displayListEntitiesDirty = true;
        this.chunksToResortTransparency = new LinkedHashSet();
        this.chunksToUpdateForced = new LinkedHashSet();
        this.visibilityDeque = new ArrayDeque();
        this.renderInfosEntities = new ArrayList(1024);
        this.renderInfosTileEntities = new ArrayList(1024);
        this.renderInfosNormal = new ArrayList(1024);
        this.renderInfosEntitiesNormal = new ArrayList(1024);
        this.renderInfosTileEntitiesNormal = new ArrayList(1024);
        this.renderInfosShadow = new ArrayList(1024);
        this.renderInfosEntitiesShadow = new ArrayList(1024);
        this.renderInfosTileEntitiesShadow = new ArrayList(1024);
        this.renderDistance = 0;
        this.renderDistanceSq = 0;
        this.worldChunkProvider = null;
        this.worldChunkProviderMap = null;
        this.countLoadedChunksPrev = 0;
        this.renderEnv = new RenderEnv(Blocks.air.getDefaultState(), new BlockPos(0, 0, 0));
        this.renderOverlayDamaged = false;
        this.renderOverlayEyes = false;
        this.firstWorldLoad = false;
        this.cloudRenderer = new CloudRenderer(mcIn);
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        (this.renderEngine = mcIn.getTextureManager()).bindTexture(RenderGlobal.locationForcefieldPng);
        GL11.glTexParameteri(3553, 10242, 10497);
        GL11.glTexParameteri(3553, 10243, 10497);
        GlStateManager.bindTexture(0);
        this.updateDestroyBlockIcons();
        this.vboEnabled = OpenGlHelper.useVbo();
        if (this.vboEnabled) {
            this.renderContainer = new VboRenderList();
            this.renderChunkFactory = new VboChunkFactory();
        }
        else {
            this.renderContainer = new RenderList();
            this.renderChunkFactory = new ListChunkFactory();
        }
        (this.vertexBufferFormat = new VertexFormat()).addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
        this.generateStars();
        this.generateSky();
        this.generateSky2();
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        this.updateDestroyBlockIcons();
    }
    
    private void updateDestroyBlockIcons() {
        final TextureMap texturemap = this.mc.getTextureMapBlocks();
        for (int i = 0; i < this.destroyBlockIcons.length; ++i) {
            this.destroyBlockIcons[i] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + i);
        }
    }
    
    public void makeEntityOutlineShader() {
        if (OpenGlHelper.shadersSupported) {
            if (ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
                ShaderLinkHelper.setNewStaticShaderLinkHelper();
            }
            final ResourceLocation resourcelocation = new ResourceLocation("shaders/post/entity_outline.json");
            try {
                (this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), resourcelocation)).createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
                this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
            }
            catch (IOException ioexception) {
                RenderGlobal.logger.warn("Failed to load shader: " + resourcelocation, (Throwable)ioexception);
                this.entityOutlineShader = null;
                this.entityOutlineFramebuffer = null;
            }
            catch (JsonSyntaxException jsonsyntaxexception) {
                RenderGlobal.logger.warn("Failed to load shader: " + resourcelocation, (Throwable)jsonsyntaxexception);
                this.entityOutlineShader = null;
                this.entityOutlineFramebuffer = null;
            }
        }
        else {
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
        }
    }
    
    public void renderEntityOutlineFramebuffer() {
        if (this.isRenderEntityOutlines()) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
            GlStateManager.disableBlend();
        }
    }
    
    protected boolean isRenderEntityOutlines() {
        return !Config.isShaders() && !Config.isAntialiasing() && this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && this.mc.thePlayer != null && this.mc.thePlayer.isSpectator() && this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown();
    }
    
    private void generateSky2() {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
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
            this.sky2VBO.bufferData(worldrenderer.getByteBuffer());
        }
        else {
            GL11.glNewList(this.glSkyList2 = GLAllocation.generateDisplayLists(1), 4864);
            this.renderSky(worldrenderer, -16.0f, true);
            tessellator.draw();
            GL11.glEndList();
        }
    }
    
    private void generateSky() {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
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
            this.skyVBO.bufferData(worldrenderer.getByteBuffer());
        }
        else {
            GL11.glNewList(this.glSkyList = GLAllocation.generateDisplayLists(1), 4864);
            this.renderSky(worldrenderer, 16.0f, false);
            tessellator.draw();
            GL11.glEndList();
        }
    }
    
    private void renderSky(final WorldRenderer worldRendererIn, final float posY, final boolean reverseX) {
        final int i = 64;
        final int j = 6;
        worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
        for (int k = (this.renderDistance / 64 + 1) * 64 + 64, l = -k; l <= k; l += 64) {
            for (int i2 = -k; i2 <= k; i2 += 64) {
                float f = (float)l;
                float f2 = (float)(l + 64);
                if (reverseX) {
                    f2 = (float)l;
                    f = (float)(l + 64);
                }
                worldRendererIn.pos(f, posY, i2).endVertex();
                worldRendererIn.pos(f2, posY, i2).endVertex();
                worldRendererIn.pos(f2, posY, i2 + 64).endVertex();
                worldRendererIn.pos(f, posY, i2 + 64).endVertex();
            }
        }
    }
    
    private void generateStars() {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
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
            this.starVBO.bufferData(worldrenderer.getByteBuffer());
        }
        else {
            this.starGLCallList = GLAllocation.generateDisplayLists(1);
            GlStateManager.pushMatrix();
            GL11.glNewList(this.starGLCallList, 4864);
            this.renderStars(worldrenderer);
            tessellator.draw();
            GL11.glEndList();
            GlStateManager.popMatrix();
        }
    }
    
    private void renderStars(final WorldRenderer worldRendererIn) {
        final Random random = new Random(10842L);
        worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
        for (int i = 0; i < 1500; ++i) {
            double d0 = random.nextFloat() * 2.0f - 1.0f;
            double d2 = random.nextFloat() * 2.0f - 1.0f;
            double d3 = random.nextFloat() * 2.0f - 1.0f;
            final double d4 = 0.15f + random.nextFloat() * 0.1f;
            double d5 = d0 * d0 + d2 * d2 + d3 * d3;
            if (d5 < 1.0 && d5 > 0.01) {
                d5 = 1.0 / Math.sqrt(d5);
                d0 *= d5;
                d2 *= d5;
                d3 *= d5;
                final double d6 = d0 * 100.0;
                final double d7 = d2 * 100.0;
                final double d8 = d3 * 100.0;
                final double d9 = Math.atan2(d0, d3);
                final double d10 = Math.sin(d9);
                final double d11 = Math.cos(d9);
                final double d12 = Math.atan2(Math.sqrt(d0 * d0 + d3 * d3), d2);
                final double d13 = Math.sin(d12);
                final double d14 = Math.cos(d12);
                final double d15 = random.nextDouble() * 3.141592653589793 * 2.0;
                final double d16 = Math.sin(d15);
                final double d17 = Math.cos(d15);
                for (int j = 0; j < 4; ++j) {
                    final double d18 = 0.0;
                    final double d19 = ((j & 0x2) - 1) * d4;
                    final double d20 = ((j + 1 & 0x2) - 1) * d4;
                    final double d21 = 0.0;
                    final double d22 = d19 * d17 - d20 * d16;
                    final double d23 = d20 * d17 + d19 * d16;
                    final double d24 = d22 * d13 + 0.0 * d14;
                    final double d25 = 0.0 * d13 - d22 * d14;
                    final double d26 = d25 * d10 - d23 * d11;
                    final double d27 = d23 * d10 + d25 * d11;
                    worldRendererIn.pos(d6 + d26, d7 + d24, d8 + d27).endVertex();
                }
            }
        }
    }
    
    public void setWorldAndLoadRenderers(final WorldClient worldClientIn) {
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
        if (Config.isDynamicLights()) {
            DynamicLights.clear();
        }
        ChunkVisibility.reset();
        this.worldChunkProvider = null;
        this.worldChunkProviderMap = null;
        this.renderEnv.reset(null, null);
        Shaders.checkWorldChanged(this.theWorld);
        if (worldClientIn != null) {
            worldClientIn.addWorldAccess(this);
            this.loadRenderers();
        }
        else {
            this.chunksToUpdate.clear();
            this.clearRenderInfos();
            if (this.viewFrustum != null) {
                this.viewFrustum.deleteGlResources();
            }
            this.viewFrustum = null;
        }
    }
    
    public void loadRenderers() {
        if (this.theWorld != null) {
            this.displayListEntitiesDirty = true;
            Blocks.leaves.setGraphicsLevel(Config.isTreesFancy());
            Blocks.leaves2.setGraphicsLevel(Config.isTreesFancy());
            BlockModelRenderer.updateAoLightValue();
            if (Config.isDynamicLights()) {
                DynamicLights.clear();
            }
            SmartAnimations.update();
            this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
            this.renderDistance = this.renderDistanceChunks * 16;
            this.renderDistanceSq = this.renderDistance * this.renderDistance;
            final boolean flag = this.vboEnabled;
            this.vboEnabled = OpenGlHelper.useVbo();
            if (flag && !this.vboEnabled) {
                this.renderContainer = new RenderList();
                this.renderChunkFactory = new ListChunkFactory();
            }
            else if (!flag && this.vboEnabled) {
                this.renderContainer = new VboRenderList();
                this.renderChunkFactory = new VboChunkFactory();
            }
            this.generateStars();
            this.generateSky();
            this.generateSky2();
            if (this.viewFrustum != null) {
                this.viewFrustum.deleteGlResources();
            }
            this.stopChunkUpdates();
            synchronized (this.setTileEntities) {
                this.setTileEntities.clear();
            }
            this.viewFrustum = new ViewFrustum(this.theWorld, this.mc.gameSettings.renderDistanceChunks, this, this.renderChunkFactory);
            if (this.theWorld != null) {
                final Entity entity = this.mc.getRenderViewEntity();
                if (entity != null) {
                    this.viewFrustum.updateChunkPositions(entity.posX, entity.posZ);
                }
            }
            this.renderEntitiesStartupCounter = 2;
        }
        if (this.mc.thePlayer == null) {
            this.firstWorldLoad = true;
        }
    }
    
    protected void stopChunkUpdates() {
        this.chunksToUpdate.clear();
        this.renderDispatcher.stopChunkUpdates();
    }
    
    public void createBindEntityOutlineFbs(final int width, final int height) {
        if (OpenGlHelper.shadersSupported && this.entityOutlineShader != null) {
            this.entityOutlineShader.createBindFramebuffers(width, height);
        }
    }
    
    public void renderEntities(final Entity renderViewEntity, final ICamera camera, final float partialTicks) {
        final int i = 0;
        if (this.renderEntitiesStartupCounter > 0) {
            if (i > 0) {
                return;
            }
            --this.renderEntitiesStartupCounter;
        }
        else {
            final double d0 = renderViewEntity.prevPosX + (renderViewEntity.posX - renderViewEntity.prevPosX) * partialTicks;
            final double d2 = renderViewEntity.prevPosY + (renderViewEntity.posY - renderViewEntity.prevPosY) * partialTicks;
            final double d3 = renderViewEntity.prevPosZ + (renderViewEntity.posZ - renderViewEntity.prevPosZ) * partialTicks;
            this.theWorld.theProfiler.startSection("prepare");
            TileEntityRendererDispatcher.instance.cacheActiveRenderInfo(this.theWorld, this.mc.getTextureManager(), this.mc.fontRendererObj, this.mc.getRenderViewEntity(), partialTicks);
            this.renderManager.cacheActiveRenderInfo(this.theWorld, this.mc.fontRendererObj, this.mc.getRenderViewEntity(), this.mc.pointedEntity, this.mc.gameSettings, partialTicks);
            ++RenderGlobal.renderEntitiesCounter;
            if (i == 0) {
                this.countEntitiesTotal = 0;
                this.countEntitiesRendered = 0;
                this.countEntitiesHidden = 0;
                this.countTileEntitiesRendered = 0;
            }
            final Entity entity = this.mc.getRenderViewEntity();
            final double d4 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
            final double d5 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
            final double d6 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
            TileEntityRendererDispatcher.staticPlayerX = d4;
            TileEntityRendererDispatcher.staticPlayerY = d5;
            TileEntityRendererDispatcher.staticPlayerZ = d6;
            this.renderManager.setRenderPosition(d4, d5, d6);
            this.mc.entityRenderer.enableLightmap();
            this.theWorld.theProfiler.endStartSection("global");
            final List<Entity> list = this.theWorld.getLoadedEntityList();
            if (i == 0) {
                this.countEntitiesTotal = list.size();
            }
            if (Config.isFogOff() && this.mc.entityRenderer.fogStandard) {
                GlStateManager.disableFog();
            }
            for (int j = 0; j < this.theWorld.weatherEffects.size(); ++j) {
                final Entity entity2 = this.theWorld.weatherEffects.get(j);
                ++this.countEntitiesRendered;
                if (entity2.isInRangeToRender3d(d0, d2, d3)) {
                    this.renderManager.renderEntitySimple(entity2, partialTicks);
                }
            }
            if (this.isRenderEntityOutlines()) {
                GlStateManager.depthFunc(519);
                GlStateManager.disableFog();
                this.entityOutlineFramebuffer.framebufferClear();
                this.entityOutlineFramebuffer.bindFramebuffer(false);
                this.theWorld.theProfiler.endStartSection("entityOutlines");
                RenderHelper.disableStandardItemLighting();
                this.renderManager.setRenderOutlines(true);
                for (int k = 0; k < list.size(); ++k) {
                    final Entity entity3 = list.get(k);
                    final boolean flag2 = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
                    final boolean flag3 = entity3.isInRangeToRender3d(d0, d2, d3) && (entity3.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(entity3.getEntityBoundingBox()) || entity3.riddenByEntity == this.mc.thePlayer) && entity3 instanceof EntityPlayer;
                    if ((entity3 != this.mc.getRenderViewEntity() || this.mc.gameSettings.thirdPersonView != 0 || flag2) && flag3) {
                        this.renderManager.renderEntitySimple(entity3, partialTicks);
                    }
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
            final boolean flag4 = Config.isShaders();
            if (flag4) {
                Shaders.beginEntities();
            }
            RenderItemFrame.updateItemRenderDistance();
            final boolean flag5 = this.mc.gameSettings.fancyGraphics;
            this.mc.gameSettings.fancyGraphics = Config.isDroppedItemsFancy();
            final boolean flag6 = Shaders.isShadowPass && !this.mc.thePlayer.isSpectator();
            for (final Object renderglobal$containerlocalrenderinformation0 : this.renderInfosEntities) {
                final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation2 = (ContainerLocalRenderInformation)renderglobal$containerlocalrenderinformation0;
                final Chunk chunk = renderglobal$containerlocalrenderinformation2.renderChunk.getChunk();
                final ClassInheritanceMultiMap<Entity> classinheritancemultimap = chunk.getEntityLists()[renderglobal$containerlocalrenderinformation2.renderChunk.getPosition().getY() / 16];
                if (!classinheritancemultimap.isEmpty()) {
                    for (final Entity entity4 : classinheritancemultimap) {
                        final boolean flag7 = this.renderManager.shouldRender(entity4, camera, d0, d2, d3) || entity4.riddenByEntity == this.mc.thePlayer;
                        if (flag7) {
                            final boolean flag8 = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
                            if ((entity4 == this.mc.getRenderViewEntity() && !flag6 && this.mc.gameSettings.thirdPersonView == 0 && !flag8) || (entity4.posY >= 0.0 && entity4.posY < 256.0 && !this.theWorld.isBlockLoaded(new BlockPos(entity4)))) {
                                continue;
                            }
                            ++this.countEntitiesRendered;
                            this.renderedEntity = entity4;
                            if (flag4) {
                                Shaders.nextEntity(entity4);
                            }
                            this.renderManager.renderEntitySimple(entity4, partialTicks);
                            this.renderedEntity = null;
                        }
                        if (!flag7 && entity4 instanceof EntityWitherSkull) {
                            this.renderedEntity = entity4;
                            if (flag4) {
                                Shaders.nextEntity(entity4);
                            }
                            this.mc.getRenderManager().renderWitherSkull(entity4, partialTicks);
                            this.renderedEntity = null;
                        }
                    }
                }
            }
            this.mc.gameSettings.fancyGraphics = flag5;
            if (flag4) {
                Shaders.endEntities();
                Shaders.beginBlockEntities();
            }
            this.theWorld.theProfiler.endStartSection("blockentities");
            RenderHelper.enableStandardItemLighting();
            TileEntitySignRenderer.updateTextRenderDistance();
            for (final Object renderglobal$containerlocalrenderinformation3 : this.renderInfosTileEntities) {
                final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation4 = (ContainerLocalRenderInformation)renderglobal$containerlocalrenderinformation3;
                final List<TileEntity> list2 = renderglobal$containerlocalrenderinformation4.renderChunk.getCompiledChunk().getTileEntities();
                if (!list2.isEmpty()) {
                    for (final TileEntity tileentity1 : list2) {
                        if (flag4) {
                            Shaders.nextBlockEntity(tileentity1);
                        }
                        TileEntityRendererDispatcher.instance.renderTileEntity(tileentity1, partialTicks, -1);
                        ++this.countTileEntitiesRendered;
                    }
                }
            }
            synchronized (this.setTileEntities) {
                for (final TileEntity tileentity2 : this.setTileEntities) {
                    if (flag4) {
                        Shaders.nextBlockEntity(tileentity2);
                    }
                    TileEntityRendererDispatcher.instance.renderTileEntity(tileentity2, partialTicks, -1);
                }
            }
            this.renderOverlayDamaged = true;
            this.preRenderDamagedBlocks();
            for (final DestroyBlockProgress destroyblockprogress : this.damagedBlocks.values()) {
                BlockPos blockpos = destroyblockprogress.getPosition();
                TileEntity tileentity3 = this.theWorld.getTileEntity(blockpos);
                if (tileentity3 instanceof TileEntityChest) {
                    final TileEntityChest tileentitychest = (TileEntityChest)tileentity3;
                    if (tileentitychest.adjacentChestXNeg != null) {
                        blockpos = blockpos.offset(EnumFacing.WEST);
                        tileentity3 = this.theWorld.getTileEntity(blockpos);
                    }
                    else if (tileentitychest.adjacentChestZNeg != null) {
                        blockpos = blockpos.offset(EnumFacing.NORTH);
                        tileentity3 = this.theWorld.getTileEntity(blockpos);
                    }
                }
                final Block block = this.theWorld.getBlockState(blockpos).getBlock();
                final boolean flag9 = tileentity3 != null && (block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSign || block instanceof BlockSkull);
                if (flag9) {
                    if (flag4) {
                        Shaders.nextBlockEntity(tileentity3);
                    }
                    TileEntityRendererDispatcher.instance.renderTileEntity(tileentity3, partialTicks, destroyblockprogress.getPartialBlockDamage());
                }
            }
            this.postRenderDamagedBlocks();
            this.renderOverlayDamaged = false;
            if (flag4) {
                Shaders.endBlockEntities();
            }
            --RenderGlobal.renderEntitiesCounter;
            this.mc.entityRenderer.disableLightmap();
            this.mc.mcProfiler.endSection();
        }
    }
    
    public String getDebugInfoRenders() {
        final int i = this.viewFrustum.renderChunks.length;
        int j = 0;
        for (final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos) {
            final CompiledChunk compiledchunk = renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk;
            if (compiledchunk != CompiledChunk.DUMMY && !compiledchunk.isEmpty()) {
                ++j;
            }
        }
        return String.format("C: %d/%d %sD: %d, %s", j, i, this.mc.renderChunksMany ? "(s) " : "", this.renderDistanceChunks, this.renderDispatcher.getDebugInfo());
    }
    
    public String getDebugInfoEntities() {
        return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered) + ", " + Config.getVersionDebug();
    }
    
    public void setupTerrain(final Entity viewEntity, final double partialTicks, ICamera camera, final int frameCount, final boolean playerSpectator) {
        if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks) {
            this.loadRenderers();
        }
        this.theWorld.theProfiler.startSection("camera");
        final double d0 = viewEntity.posX - this.frustumUpdatePosX;
        final double d2 = viewEntity.posY - this.frustumUpdatePosY;
        final double d3 = viewEntity.posZ - this.frustumUpdatePosZ;
        if (this.frustumUpdatePosChunkX != viewEntity.chunkCoordX || this.frustumUpdatePosChunkY != viewEntity.chunkCoordY || this.frustumUpdatePosChunkZ != viewEntity.chunkCoordZ || d0 * d0 + d2 * d2 + d3 * d3 > 16.0) {
            this.frustumUpdatePosX = viewEntity.posX;
            this.frustumUpdatePosY = viewEntity.posY;
            this.frustumUpdatePosZ = viewEntity.posZ;
            this.frustumUpdatePosChunkX = viewEntity.chunkCoordX;
            this.frustumUpdatePosChunkY = viewEntity.chunkCoordY;
            this.frustumUpdatePosChunkZ = viewEntity.chunkCoordZ;
            this.viewFrustum.updateChunkPositions(viewEntity.posX, viewEntity.posZ);
        }
        if (Config.isDynamicLights()) {
            DynamicLights.update(this);
        }
        this.theWorld.theProfiler.endStartSection("renderlistcamera");
        final double d4 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
        final double d5 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
        final double d6 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
        this.renderContainer.initialize(d4, d5, d6);
        this.theWorld.theProfiler.endStartSection("cull");
        if (this.debugFixedClippingHelper != null) {
            final Frustum frustum = new Frustum(this.debugFixedClippingHelper);
            frustum.setPosition(this.debugTerrainFrustumPosition.x, this.debugTerrainFrustumPosition.y, this.debugTerrainFrustumPosition.z);
            camera = frustum;
        }
        this.mc.mcProfiler.endStartSection("culling");
        final BlockPos blockpos = new BlockPos(d4, d5 + viewEntity.getEyeHeight(), d6);
        final RenderChunk renderchunk = this.viewFrustum.getRenderChunk(blockpos);
        new BlockPos(MathHelper.floor_double(d4 / 16.0) * 16, MathHelper.floor_double(d5 / 16.0) * 16, MathHelper.floor_double(d6 / 16.0) * 16);
        this.displayListEntitiesDirty = (this.displayListEntitiesDirty || !this.chunksToUpdate.isEmpty() || viewEntity.posX != this.lastViewEntityX || viewEntity.posY != this.lastViewEntityY || viewEntity.posZ != this.lastViewEntityZ || viewEntity.rotationPitch != this.lastViewEntityPitch || viewEntity.rotationYaw != this.lastViewEntityYaw);
        this.lastViewEntityX = viewEntity.posX;
        this.lastViewEntityY = viewEntity.posY;
        this.lastViewEntityZ = viewEntity.posZ;
        this.lastViewEntityPitch = viewEntity.rotationPitch;
        this.lastViewEntityYaw = viewEntity.rotationYaw;
        final boolean flag = this.debugFixedClippingHelper != null;
        this.mc.mcProfiler.endStartSection("update");
        Lagometer.timerVisibility.start();
        final int i = this.getCountLoadedChunks();
        if (i != this.countLoadedChunksPrev) {
            this.countLoadedChunksPrev = i;
            this.displayListEntitiesDirty = true;
        }
        int j = 256;
        if (!ChunkVisibility.isFinished()) {
            this.displayListEntitiesDirty = true;
        }
        if (!flag && this.displayListEntitiesDirty && Config.isIntegratedServerRunning()) {
            j = ChunkVisibility.getMaxChunkY(this.theWorld, viewEntity, this.renderDistanceChunks);
        }
        final RenderChunk renderchunk2 = this.viewFrustum.getRenderChunk(new BlockPos(viewEntity.posX, viewEntity.posY, viewEntity.posZ));
        if (Shaders.isShadowPass) {
            this.renderInfos = (List<ContainerLocalRenderInformation>)this.renderInfosShadow;
            this.renderInfosEntities = this.renderInfosEntitiesShadow;
            this.renderInfosTileEntities = this.renderInfosTileEntitiesShadow;
            if (!flag && this.displayListEntitiesDirty) {
                this.clearRenderInfos();
                if (renderchunk2 != null && renderchunk2.getPosition().getY() > j) {
                    this.renderInfosEntities.add(renderchunk2.getRenderInfo());
                }
                final Iterator<RenderChunk> iterator = ShadowUtils.makeShadowChunkIterator(this.theWorld, partialTicks, viewEntity, this.renderDistanceChunks, this.viewFrustum);
                while (iterator.hasNext()) {
                    final RenderChunk renderchunk3 = iterator.next();
                    if (renderchunk3 != null && renderchunk3.getPosition().getY() <= j) {
                        final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = renderchunk3.getRenderInfo();
                        if (!renderchunk3.compiledChunk.isEmpty() || renderchunk3.isNeedsUpdate()) {
                            this.renderInfos.add(renderglobal$containerlocalrenderinformation);
                        }
                        if (ChunkUtils.hasEntities(renderchunk3.getChunk())) {
                            this.renderInfosEntities.add(renderglobal$containerlocalrenderinformation);
                        }
                        if (renderchunk3.getCompiledChunk().getTileEntities().size() <= 0) {
                            continue;
                        }
                        this.renderInfosTileEntities.add(renderglobal$containerlocalrenderinformation);
                    }
                }
            }
        }
        else {
            this.renderInfos = (List<ContainerLocalRenderInformation>)this.renderInfosNormal;
            this.renderInfosEntities = this.renderInfosEntitiesNormal;
            this.renderInfosTileEntities = this.renderInfosTileEntitiesNormal;
        }
        if (!flag && this.displayListEntitiesDirty && !Shaders.isShadowPass) {
            this.displayListEntitiesDirty = false;
            this.clearRenderInfos();
            this.visibilityDeque.clear();
            final Deque deque = this.visibilityDeque;
            boolean flag2 = this.mc.renderChunksMany;
            if (renderchunk != null && renderchunk.getPosition().getY() <= j) {
                boolean flag3 = false;
                final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation2 = new ContainerLocalRenderInformation(renderchunk, null, 0);
                final Set set1 = RenderGlobal.SET_ALL_FACINGS;
                if (set1.size() == 1) {
                    final Vector3f vector3f = this.getViewVector(viewEntity, partialTicks);
                    final EnumFacing enumfacing2 = EnumFacing.getFacingFromVector(vector3f.x, vector3f.y, vector3f.z).getOpposite();
                    set1.remove(enumfacing2);
                }
                if (set1.isEmpty()) {
                    flag3 = true;
                }
                if (flag3 && !playerSpectator) {
                    this.renderInfos.add(renderglobal$containerlocalrenderinformation2);
                }
                else {
                    if (playerSpectator && this.theWorld.getBlockState(blockpos).getBlock().isOpaqueCube()) {
                        flag2 = false;
                    }
                    renderchunk.setFrameIndex(frameCount);
                    deque.add(renderglobal$containerlocalrenderinformation2);
                }
            }
            else {
                final int j2 = (blockpos.getY() > 0) ? Math.min(j, 248) : 8;
                if (renderchunk2 != null) {
                    this.renderInfosEntities.add(renderchunk2.getRenderInfo());
                }
                for (int k = -this.renderDistanceChunks; k <= this.renderDistanceChunks; ++k) {
                    for (int l = -this.renderDistanceChunks; l <= this.renderDistanceChunks; ++l) {
                        final RenderChunk renderchunk4 = this.viewFrustum.getRenderChunk(new BlockPos((k << 4) + 8, j2, (l << 4) + 8));
                        if (renderchunk4 != null && renderchunk4.isBoundingBoxInFrustum(camera, frameCount)) {
                            renderchunk4.setFrameIndex(frameCount);
                            final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation3 = renderchunk4.getRenderInfo();
                            renderglobal$containerlocalrenderinformation3.initialize(null, 0);
                            deque.add(renderglobal$containerlocalrenderinformation3);
                        }
                    }
                }
            }
            this.mc.mcProfiler.startSection("iteration");
            final boolean flag4 = Config.isFogOn();
            while (!deque.isEmpty()) {
                final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation4 = deque.poll();
                final RenderChunk renderchunk5 = renderglobal$containerlocalrenderinformation4.renderChunk;
                final EnumFacing enumfacing3 = renderglobal$containerlocalrenderinformation4.facing;
                final CompiledChunk compiledchunk = renderchunk5.compiledChunk;
                if (!compiledchunk.isEmpty() || renderchunk5.isNeedsUpdate()) {
                    this.renderInfos.add(renderglobal$containerlocalrenderinformation4);
                }
                if (ChunkUtils.hasEntities(renderchunk5.getChunk())) {
                    this.renderInfosEntities.add(renderglobal$containerlocalrenderinformation4);
                }
                if (compiledchunk.getTileEntities().size() > 0) {
                    this.renderInfosTileEntities.add(renderglobal$containerlocalrenderinformation4);
                }
                for (final EnumFacing enumfacing4 : flag2 ? ChunkVisibility.getFacingsNotOpposite(renderglobal$containerlocalrenderinformation4.setFacing) : EnumFacing.VALUES) {
                    if (!flag2 || enumfacing3 == null || compiledchunk.isVisible(enumfacing3.getOpposite(), enumfacing4)) {
                        final RenderChunk renderchunk6 = this.getRenderChunkOffset(blockpos, renderchunk5, enumfacing4, flag4, j);
                        if (renderchunk6 != null && renderchunk6.setFrameIndex(frameCount) && renderchunk6.isBoundingBoxInFrustum(camera, frameCount)) {
                            final int i2 = renderglobal$containerlocalrenderinformation4.setFacing | 1 << enumfacing4.ordinal();
                            final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation5 = renderchunk6.getRenderInfo();
                            renderglobal$containerlocalrenderinformation5.initialize(enumfacing4, i2);
                            deque.add(renderglobal$containerlocalrenderinformation5);
                        }
                    }
                }
            }
            this.mc.mcProfiler.endSection();
        }
        this.mc.mcProfiler.endStartSection("captureFrustum");
        if (this.debugFixTerrainFrustum) {
            this.fixTerrainFrustum(d4, d5, d6);
            this.debugFixTerrainFrustum = false;
        }
        Lagometer.timerVisibility.end();
        if (Shaders.isShadowPass) {
            Shaders.mcProfilerEndSection();
        }
        else {
            this.mc.mcProfiler.endStartSection("rebuildNear");
            this.renderDispatcher.clearChunkUpdates();
            final Set<RenderChunk> set2 = this.chunksToUpdate;
            this.chunksToUpdate = (Set<RenderChunk>)Sets.newLinkedHashSet();
            Lagometer.timerChunkUpdate.start();
            for (final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation6 : this.renderInfos) {
                final RenderChunk renderchunk7 = renderglobal$containerlocalrenderinformation6.renderChunk;
                if (renderchunk7.isNeedsUpdate() || set2.contains(renderchunk7)) {
                    this.displayListEntitiesDirty = true;
                    final BlockPos blockpos2 = renderchunk7.getPosition();
                    final boolean flag5 = blockpos.distanceSq(blockpos2.getX() + 8, blockpos2.getY() + 8, blockpos2.getZ() + 8) < 768.0;
                    if (!flag5) {
                        this.chunksToUpdate.add(renderchunk7);
                    }
                    else if (!renderchunk7.isPlayerUpdate()) {
                        this.chunksToUpdateForced.add(renderchunk7);
                    }
                    else {
                        this.mc.mcProfiler.startSection("build near");
                        this.renderDispatcher.updateChunkNow(renderchunk7);
                        renderchunk7.setNeedsUpdate(false);
                        this.mc.mcProfiler.endSection();
                    }
                }
            }
            Lagometer.timerChunkUpdate.end();
            this.chunksToUpdate.addAll(set2);
            this.mc.mcProfiler.endSection();
        }
    }
    
    private boolean isPositionInRenderChunk(final BlockPos pos, final RenderChunk renderChunkIn) {
        final BlockPos blockpos = renderChunkIn.getPosition();
        return MathHelper.abs_int(pos.getX() - blockpos.getX()) <= 16 && MathHelper.abs_int(pos.getY() - blockpos.getY()) <= 16 && MathHelper.abs_int(pos.getZ() - blockpos.getZ()) <= 16;
    }
    
    private Set<EnumFacing> getVisibleFacings(final BlockPos pos) {
        final VisGraph visgraph = new VisGraph();
        final BlockPos blockpos = new BlockPos(pos.getX() >> 4 << 4, pos.getY() >> 4 << 4, pos.getZ() >> 4 << 4);
        final Chunk chunk = this.theWorld.getChunkFromBlockCoords(blockpos);
        for (final BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos, blockpos.add(15, 15, 15))) {
            if (chunk.getBlock(blockpos$mutableblockpos).isOpaqueCube()) {
                visgraph.func_178606_a(blockpos$mutableblockpos);
            }
        }
        return visgraph.func_178609_b(pos);
    }
    
    private RenderChunk getRenderChunkOffset(final BlockPos p_getRenderChunkOffset_1_, final RenderChunk p_getRenderChunkOffset_2_, final EnumFacing p_getRenderChunkOffset_3_, final boolean p_getRenderChunkOffset_4_, final int p_getRenderChunkOffset_5_) {
        final RenderChunk renderchunk = p_getRenderChunkOffset_2_.getRenderChunkNeighbour(p_getRenderChunkOffset_3_);
        if (renderchunk == null) {
            return null;
        }
        if (renderchunk.getPosition().getY() > p_getRenderChunkOffset_5_) {
            return null;
        }
        if (p_getRenderChunkOffset_4_) {
            final BlockPos blockpos = renderchunk.getPosition();
            final int i = p_getRenderChunkOffset_1_.getX() - blockpos.getX();
            final int j = p_getRenderChunkOffset_1_.getZ() - blockpos.getZ();
            final int k = i * i + j * j;
            if (k > this.renderDistanceSq) {
                return null;
            }
        }
        return renderchunk;
    }
    
    private void fixTerrainFrustum(final double x, final double y, final double z) {
        this.debugFixedClippingHelper = new ClippingHelperImpl();
        ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
        final Matrix4f matrix4f = new Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
        matrix4f.transpose();
        final Matrix4f matrix4f2 = new Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
        matrix4f2.transpose();
        final Matrix4f matrix4f3 = new Matrix4f();
        Matrix4f.mul((org.lwjgl.util.vector.Matrix4f)matrix4f2, (org.lwjgl.util.vector.Matrix4f)matrix4f, (org.lwjgl.util.vector.Matrix4f)matrix4f3);
        matrix4f3.invert();
        this.debugTerrainFrustumPosition.x = x;
        this.debugTerrainFrustumPosition.y = y;
        this.debugTerrainFrustumPosition.z = z;
        this.debugTerrainMatrix[0] = new Vector4f(-1.0f, -1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[1] = new Vector4f(1.0f, -1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[2] = new Vector4f(1.0f, 1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[3] = new Vector4f(-1.0f, 1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[4] = new Vector4f(-1.0f, -1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[5] = new Vector4f(1.0f, -1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[6] = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[7] = new Vector4f(-1.0f, 1.0f, 1.0f, 1.0f);
        for (int i = 0; i < 8; ++i) {
            Matrix4f.transform((org.lwjgl.util.vector.Matrix4f)matrix4f3, this.debugTerrainMatrix[i], this.debugTerrainMatrix[i]);
            final Vector4f vector4f = this.debugTerrainMatrix[i];
            vector4f.x /= this.debugTerrainMatrix[i].w;
            final Vector4f vector4f2 = this.debugTerrainMatrix[i];
            vector4f2.y /= this.debugTerrainMatrix[i].w;
            final Vector4f vector4f3 = this.debugTerrainMatrix[i];
            vector4f3.z /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].w = 1.0f;
        }
    }
    
    protected Vector3f getViewVector(final Entity entityIn, final double partialTicks) {
        float f = (float)(entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks);
        final float f2 = (float)(entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks);
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
            f += 180.0f;
        }
        final float f3 = MathHelper.cos(-f2 * 0.017453292f - 3.1415927f);
        final float f4 = MathHelper.sin(-f2 * 0.017453292f - 3.1415927f);
        final float f5 = -MathHelper.cos(-f * 0.017453292f);
        final float f6 = MathHelper.sin(-f * 0.017453292f);
        return new Vector3f(f4 * f5, f6, f3 * f5);
    }
    
    public int renderBlockLayer(final EnumWorldBlockLayer blockLayerIn, final double partialTicks, final int pass, final Entity entityIn) {
        RenderHelper.disableStandardItemLighting();
        if (blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT && !Shaders.isShadowPass) {
            this.mc.mcProfiler.startSection("translucent_sort");
            final double d0 = entityIn.posX - this.prevRenderSortX;
            final double d2 = entityIn.posY - this.prevRenderSortY;
            final double d3 = entityIn.posZ - this.prevRenderSortZ;
            if (d0 * d0 + d2 * d2 + d3 * d3 > 1.0) {
                this.prevRenderSortX = entityIn.posX;
                this.prevRenderSortY = entityIn.posY;
                this.prevRenderSortZ = entityIn.posZ;
                int k = 0;
                this.chunksToResortTransparency.clear();
                for (final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos) {
                    if (renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk.isLayerStarted(blockLayerIn) && k++ < 15) {
                        this.chunksToResortTransparency.add(renderglobal$containerlocalrenderinformation.renderChunk);
                    }
                }
            }
            this.mc.mcProfiler.endSection();
        }
        this.mc.mcProfiler.startSection("filterempty");
        int l = 0;
        final boolean flag = blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT;
        final int i1 = flag ? (this.renderInfos.size() - 1) : 0;
        for (int j = flag ? -1 : this.renderInfos.size(), j2 = flag ? -1 : 1, m = i1; m != j; m += j2) {
            final RenderChunk renderchunk = this.renderInfos.get(m).renderChunk;
            if (!renderchunk.getCompiledChunk().isLayerEmpty(blockLayerIn)) {
                ++l;
                this.renderContainer.addRenderChunk(renderchunk, blockLayerIn);
            }
        }
        if (l == 0) {
            this.mc.mcProfiler.endSection();
            return l;
        }
        if (Config.isFogOff() && this.mc.entityRenderer.fogStandard) {
            GlStateManager.disableFog();
        }
        this.mc.mcProfiler.endStartSection("render_" + blockLayerIn);
        this.renderBlockLayer(blockLayerIn);
        this.mc.mcProfiler.endSection();
        return l;
    }
    
    private void renderBlockLayer(final EnumWorldBlockLayer blockLayerIn) {
        this.mc.entityRenderer.enableLightmap();
        if (OpenGlHelper.useVbo()) {
            GL11.glEnableClientState(32884);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glEnableClientState(32888);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glEnableClientState(32888);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glEnableClientState(32886);
        }
        if (Config.isShaders()) {
            ShadersRender.preRenderChunkLayer(blockLayerIn);
        }
        this.renderContainer.renderChunkLayer(blockLayerIn);
        if (Config.isShaders()) {
            ShadersRender.postRenderChunkLayer(blockLayerIn);
        }
        if (OpenGlHelper.useVbo()) {
            for (final VertexFormatElement vertexformatelement : DefaultVertexFormats.BLOCK.getElements()) {
                final VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                final int i = vertexformatelement.getIndex();
                switch (vertexformatelement$enumusage) {
                    case POSITION: {
                        GL11.glDisableClientState(32884);
                        continue;
                    }
                    case UV: {
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + i);
                        GL11.glDisableClientState(32888);
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                        continue;
                    }
                    case COLOR: {
                        GL11.glDisableClientState(32886);
                        GlStateManager.resetColor();
                        continue;
                    }
                }
            }
        }
        this.mc.entityRenderer.disableLightmap();
    }
    
    private void cleanupDamagedBlocks(final Iterator<DestroyBlockProgress> iteratorIn) {
        while (iteratorIn.hasNext()) {
            final DestroyBlockProgress destroyblockprogress = iteratorIn.next();
            final int i = destroyblockprogress.getCreationCloudUpdateTick();
            if (this.cloudTickCounter - i > 400) {
                iteratorIn.remove();
            }
        }
    }
    
    public void updateClouds() {
        if (Config.isShaders()) {
            if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(24)) {
                final GuiShaderOptions guishaderoptions = new GuiShaderOptions(null, Config.getGameSettings());
                Config.getMinecraft().displayGuiScreen(guishaderoptions);
            }
            if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(19)) {
                Shaders.uninit();
                Shaders.loadShaderPack();
            }
        }
        ++this.cloudTickCounter;
        if (this.cloudTickCounter % 20 == 0) {
            this.cleanupDamagedBlocks(this.damagedBlocks.values().iterator());
        }
    }
    
    private void renderSkyEnd() {
        if (Config.isSkyEnabled()) {
            GlStateManager.disableFog();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.depthMask(false);
            this.renderEngine.bindTexture(RenderGlobal.locationEndSkyPng);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            for (int i = 0; i < 6; ++i) {
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
                int j = 40;
                int k = 40;
                int l = 40;
                if (Config.isCustomColors()) {
                    Vec3 vec3 = new Vec3(j / 255.0, k / 255.0, l / 255.0);
                    vec3 = CustomColors.getWorldSkyColor(vec3, this.theWorld, this.mc.getRenderViewEntity(), 0.0f);
                    j = (int)(vec3.xCoord * 255.0);
                    k = (int)(vec3.yCoord * 255.0);
                    l = (int)(vec3.zCoord * 255.0);
                }
                worldrenderer.pos(-100.0, -100.0, -100.0).tex(0.0, 0.0).color(j, k, l, 255).endVertex();
                worldrenderer.pos(-100.0, -100.0, 100.0).tex(0.0, 16.0).color(j, k, l, 255).endVertex();
                worldrenderer.pos(100.0, -100.0, 100.0).tex(16.0, 16.0).color(j, k, l, 255).endVertex();
                worldrenderer.pos(100.0, -100.0, -100.0).tex(16.0, 0.0).color(j, k, l, 255).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }
    
    public void renderSky(final float partialTicks, final int pass) {
        if (this.mc.theWorld.provider.getDimensionId() == 1) {
            this.renderSkyEnd();
        }
        else if (this.mc.theWorld.provider.isSurfaceWorld()) {
            GlStateManager.disableTexture2D();
            final boolean flag = Config.isShaders();
            if (flag) {
                Shaders.disableTexture2D();
            }
            Vec3 vec3 = this.theWorld.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
            vec3 = CustomColors.getSkyColor(vec3, this.mc.theWorld, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ);
            if (flag) {
                Shaders.setSkyColor(vec3);
            }
            float f = (float)vec3.xCoord;
            float f2 = (float)vec3.yCoord;
            float f3 = (float)vec3.zCoord;
            if (pass != 2) {
                final float f4 = (f * 30.0f + f2 * 59.0f + f3 * 11.0f) / 100.0f;
                final float f5 = (f * 30.0f + f2 * 70.0f) / 100.0f;
                final float f6 = (f * 30.0f + f3 * 70.0f) / 100.0f;
                f = f4;
                f2 = f5;
                f3 = f6;
            }
            GlStateManager.color(f, f2, f3);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.depthMask(false);
            GlStateManager.enableFog();
            if (flag) {
                Shaders.enableFog();
            }
            GlStateManager.color(f, f2, f3);
            if (flag) {
                Shaders.preSkyList();
            }
            if (Config.isSkyEnabled()) {
                if (this.vboEnabled) {
                    this.skyVBO.bindBuffer();
                    GL11.glEnableClientState(32884);
                    GL11.glVertexPointer(3, 5126, 12, 0L);
                    this.skyVBO.drawArrays(7);
                    this.skyVBO.unbindBuffer();
                    GL11.glDisableClientState(32884);
                }
                else {
                    GlStateManager.callList(this.glSkyList);
                }
            }
            GlStateManager.disableFog();
            if (flag) {
                Shaders.disableFog();
            }
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.disableStandardItemLighting();
            final float[] afloat = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(partialTicks), partialTicks);
            if (afloat != null && Config.isSunMoonEnabled()) {
                GlStateManager.disableTexture2D();
                if (flag) {
                    Shaders.disableTexture2D();
                }
                GlStateManager.shadeModel(7425);
                GlStateManager.pushMatrix();
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate((MathHelper.sin(this.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0f) ? 180.0f : 0.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                float f7 = afloat[0];
                float f8 = afloat[1];
                float f9 = afloat[2];
                if (pass != 2) {
                    final float f10 = (f7 * 30.0f + f8 * 59.0f + f9 * 11.0f) / 100.0f;
                    final float f11 = (f7 * 30.0f + f8 * 70.0f) / 100.0f;
                    final float f12 = (f7 * 30.0f + f9 * 70.0f) / 100.0f;
                    f7 = f10;
                    f8 = f11;
                    f9 = f12;
                }
                worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos(0.0, 100.0, 0.0).color(f7, f8, f9, afloat[3]).endVertex();
                final int j = 16;
                for (int l = 0; l <= 16; ++l) {
                    final float f13 = l * 3.1415927f * 2.0f / 16.0f;
                    final float f14 = MathHelper.sin(f13);
                    final float f15 = MathHelper.cos(f13);
                    worldrenderer.pos(f14 * 120.0f, f15 * 120.0f, -f15 * 40.0f * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0f).endVertex();
                }
                tessellator.draw();
                GlStateManager.popMatrix();
                GlStateManager.shadeModel(7424);
            }
            GlStateManager.enableTexture2D();
            if (flag) {
                Shaders.enableTexture2D();
            }
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            GlStateManager.pushMatrix();
            final float f16 = 1.0f - this.theWorld.getRainStrength(partialTicks);
            GlStateManager.color(1.0f, 1.0f, 1.0f, f16);
            GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
            CustomSky.renderSky(this.theWorld, this.renderEngine, partialTicks);
            if (flag) {
                Shaders.preCelestialRotate();
            }
            GlStateManager.rotate(this.theWorld.getCelestialAngle(partialTicks) * 360.0f, 1.0f, 0.0f, 0.0f);
            if (flag) {
                Shaders.postCelestialRotate();
            }
            float f17 = 30.0f;
            if (Config.isSunTexture()) {
                this.renderEngine.bindTexture(RenderGlobal.locationSunPng);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
                worldrenderer.pos(-f17, 100.0, -f17).tex(0.0, 0.0).endVertex();
                worldrenderer.pos(f17, 100.0, -f17).tex(1.0, 0.0).endVertex();
                worldrenderer.pos(f17, 100.0, f17).tex(1.0, 1.0).endVertex();
                worldrenderer.pos(-f17, 100.0, f17).tex(0.0, 1.0).endVertex();
                tessellator.draw();
            }
            f17 = 20.0f;
            if (Config.isMoonTexture()) {
                this.renderEngine.bindTexture(RenderGlobal.locationMoonPhasesPng);
                final int i = this.theWorld.getMoonPhase();
                final int k = i % 4;
                final int i2 = i / 4 % 2;
                final float f18 = (k + 0) / 4.0f;
                final float f19 = (i2 + 0) / 2.0f;
                final float f20 = (k + 1) / 4.0f;
                final float f21 = (i2 + 1) / 2.0f;
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
                worldrenderer.pos(-f17, -100.0, f17).tex(f20, f21).endVertex();
                worldrenderer.pos(f17, -100.0, f17).tex(f18, f21).endVertex();
                worldrenderer.pos(f17, -100.0, -f17).tex(f18, f19).endVertex();
                worldrenderer.pos(-f17, -100.0, -f17).tex(f20, f19).endVertex();
                tessellator.draw();
            }
            GlStateManager.disableTexture2D();
            if (flag) {
                Shaders.disableTexture2D();
            }
            final float f22 = this.theWorld.getStarBrightness(partialTicks) * f16;
            if (f22 > 0.0f && Config.isStarsEnabled() && !CustomSky.hasSkyLayers(this.theWorld)) {
                GlStateManager.color(f22, f22, f22, f22);
                if (this.vboEnabled) {
                    this.starVBO.bindBuffer();
                    GL11.glEnableClientState(32884);
                    GL11.glVertexPointer(3, 5126, 12, 0L);
                    this.starVBO.drawArrays(7);
                    this.starVBO.unbindBuffer();
                    GL11.glDisableClientState(32884);
                }
                else {
                    GlStateManager.callList(this.starGLCallList);
                }
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableFog();
            if (flag) {
                Shaders.enableFog();
            }
            GlStateManager.popMatrix();
            GlStateManager.disableTexture2D();
            if (flag) {
                Shaders.disableTexture2D();
            }
            GlStateManager.color(0.0f, 0.0f, 0.0f);
            final double d0 = this.mc.thePlayer.getPositionEyes(partialTicks).yCoord - this.theWorld.getHorizon();
            if (d0 < 0.0) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0f, 12.0f, 0.0f);
                if (this.vboEnabled) {
                    this.sky2VBO.bindBuffer();
                    GL11.glEnableClientState(32884);
                    GL11.glVertexPointer(3, 5126, 12, 0L);
                    this.sky2VBO.drawArrays(7);
                    this.sky2VBO.unbindBuffer();
                    GL11.glDisableClientState(32884);
                }
                else {
                    GlStateManager.callList(this.glSkyList2);
                }
                GlStateManager.popMatrix();
                final float f23 = 1.0f;
                final float f24 = -(float)(d0 + 65.0);
                final float f25 = -1.0f;
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos(-1.0, f24, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, f24, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, f24, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, f24, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, f24, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, f24, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, f24, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, f24, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
            }
            if (this.theWorld.provider.isSkyColored()) {
                GlStateManager.color(f * 0.2f + 0.04f, f2 * 0.2f + 0.04f, f3 * 0.6f + 0.1f);
            }
            else {
                GlStateManager.color(f, f2, f3);
            }
            if (this.mc.gameSettings.renderDistanceChunks <= 4) {
                GlStateManager.color(this.mc.entityRenderer.fogColorRed, this.mc.entityRenderer.fogColorGreen, this.mc.entityRenderer.fogColorBlue);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, -(float)(d0 - 16.0), 0.0f);
            if (Config.isSkyEnabled()) {
                if (this.vboEnabled) {
                    this.sky2VBO.bindBuffer();
                    GlStateManager.glEnableClientState(32884);
                    GlStateManager.glVertexPointer(3, 5126, 12, 0);
                    this.sky2VBO.drawArrays(7);
                    this.sky2VBO.unbindBuffer();
                    GlStateManager.glDisableClientState(32884);
                }
                else {
                    GlStateManager.callList(this.glSkyList2);
                }
            }
            GlStateManager.popMatrix();
            GlStateManager.enableTexture2D();
            if (flag) {
                Shaders.enableTexture2D();
            }
            GlStateManager.depthMask(true);
        }
    }
    
    public void renderClouds(float partialTicks, final int pass) {
        if (!Config.isCloudsOff() && this.mc.theWorld.provider.isSurfaceWorld()) {
            if (Config.isShaders()) {
                Shaders.beginClouds();
            }
            if (Config.isCloudsFancy()) {
                this.renderCloudsFancy(partialTicks, pass);
            }
            else {
                final float f9 = partialTicks;
                partialTicks = 0.0f;
                GlStateManager.disableCull();
                final float f10 = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * partialTicks);
                final int i = 32;
                final int j = 8;
                final Tessellator tessellator = Tessellator.getInstance();
                final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                this.renderEngine.bindTexture(RenderGlobal.locationCloudsPng);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                final Vec3 vec3 = this.theWorld.getCloudColour(partialTicks);
                float f11 = (float)vec3.xCoord;
                float f12 = (float)vec3.yCoord;
                float f13 = (float)vec3.zCoord;
                this.cloudRenderer.prepareToRender(false, this.cloudTickCounter, f9, vec3);
                if (this.cloudRenderer.shouldUpdateGlList()) {
                    this.cloudRenderer.startUpdateGlList();
                    if (pass != 2) {
                        final float f14 = (f11 * 30.0f + f12 * 59.0f + f13 * 11.0f) / 100.0f;
                        final float f15 = (f11 * 30.0f + f12 * 70.0f) / 100.0f;
                        final float f16 = (f11 * 30.0f + f13 * 70.0f) / 100.0f;
                        f11 = f14;
                        f12 = f15;
                        f13 = f16;
                    }
                    final float f17 = 4.8828125E-4f;
                    final double d2 = this.cloudTickCounter + partialTicks;
                    double d3 = this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * partialTicks + d2 * 0.029999999329447746;
                    double d4 = this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * partialTicks;
                    final int k = MathHelper.floor_double(d3 / 2048.0);
                    final int l = MathHelper.floor_double(d4 / 2048.0);
                    d3 -= k * 2048;
                    d4 -= l * 2048;
                    float f18 = this.theWorld.provider.getCloudHeight() - f10 + 0.33f;
                    f18 += this.mc.gameSettings.ofCloudsHeight * 128.0f;
                    final float f19 = (float)(d3 * 4.8828125E-4);
                    final float f20 = (float)(d4 * 4.8828125E-4);
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                    for (int i2 = -256; i2 < 256; i2 += 32) {
                        for (int j2 = -256; j2 < 256; j2 += 32) {
                            worldrenderer.pos(i2 + 0, f18, j2 + 32).tex((i2 + 0) * 4.8828125E-4f + f19, (j2 + 32) * 4.8828125E-4f + f20).color(f11, f12, f13, 0.8f).endVertex();
                            worldrenderer.pos(i2 + 32, f18, j2 + 32).tex((i2 + 32) * 4.8828125E-4f + f19, (j2 + 32) * 4.8828125E-4f + f20).color(f11, f12, f13, 0.8f).endVertex();
                            worldrenderer.pos(i2 + 32, f18, j2 + 0).tex((i2 + 32) * 4.8828125E-4f + f19, (j2 + 0) * 4.8828125E-4f + f20).color(f11, f12, f13, 0.8f).endVertex();
                            worldrenderer.pos(i2 + 0, f18, j2 + 0).tex((i2 + 0) * 4.8828125E-4f + f19, (j2 + 0) * 4.8828125E-4f + f20).color(f11, f12, f13, 0.8f).endVertex();
                        }
                    }
                    tessellator.draw();
                    this.cloudRenderer.endUpdateGlList();
                }
                this.cloudRenderer.renderGlList();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableBlend();
                GlStateManager.enableCull();
            }
            if (Config.isShaders()) {
                Shaders.endClouds();
            }
        }
    }
    
    public boolean hasCloudFog(final double x, final double y, final double z, final float partialTicks) {
        return false;
    }
    
    private void renderCloudsFancy(float partialTicks, final int pass) {
        partialTicks = 0.0f;
        GlStateManager.disableCull();
        final float f = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * partialTicks);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        final float f2 = 12.0f;
        final float f3 = 4.0f;
        final double d0 = this.cloudTickCounter + partialTicks;
        double d2 = (this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * partialTicks + d0 * 0.029999999329447746) / 12.0;
        double d3 = (this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * partialTicks) / 12.0 + 0.33000001311302185;
        float f4 = this.theWorld.provider.getCloudHeight() - f + 0.33f;
        f4 += this.mc.gameSettings.ofCloudsHeight * 128.0f;
        final int i = MathHelper.floor_double(d2 / 2048.0);
        final int j = MathHelper.floor_double(d3 / 2048.0);
        d2 -= i * 2048;
        d3 -= j * 2048;
        this.renderEngine.bindTexture(RenderGlobal.locationCloudsPng);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        final Vec3 vec3 = this.theWorld.getCloudColour(partialTicks);
        float f5 = (float)vec3.xCoord;
        float f6 = (float)vec3.yCoord;
        float f7 = (float)vec3.zCoord;
        this.cloudRenderer.prepareToRender(true, this.cloudTickCounter, partialTicks, vec3);
        if (pass != 2) {
            final float f8 = (f5 * 30.0f + f6 * 59.0f + f7 * 11.0f) / 100.0f;
            final float f9 = (f5 * 30.0f + f6 * 70.0f) / 100.0f;
            final float f10 = (f5 * 30.0f + f7 * 70.0f) / 100.0f;
            f5 = f8;
            f6 = f9;
            f7 = f10;
        }
        final float f11 = f5 * 0.9f;
        final float f12 = f6 * 0.9f;
        final float f13 = f7 * 0.9f;
        final float f14 = f5 * 0.7f;
        final float f15 = f6 * 0.7f;
        final float f16 = f7 * 0.7f;
        final float f17 = f5 * 0.8f;
        final float f18 = f6 * 0.8f;
        final float f19 = f7 * 0.8f;
        final float f20 = 0.00390625f;
        final float f21 = MathHelper.floor_double(d2) * 0.00390625f;
        final float f22 = MathHelper.floor_double(d3) * 0.00390625f;
        final float f23 = (float)(d2 - MathHelper.floor_double(d2));
        final float f24 = (float)(d3 - MathHelper.floor_double(d3));
        final int k = 8;
        final int l = 4;
        final float f25 = 9.765625E-4f;
        GlStateManager.scale(12.0f, 1.0f, 12.0f);
        for (int i2 = 0; i2 < 2; ++i2) {
            if (i2 == 0) {
                GlStateManager.colorMask(false, false, false, false);
            }
            else {
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
            this.cloudRenderer.renderGlList();
        }
        if (this.cloudRenderer.shouldUpdateGlList()) {
            this.cloudRenderer.startUpdateGlList();
            for (int l2 = -3; l2 <= 4; ++l2) {
                for (int j2 = -3; j2 <= 4; ++j2) {
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                    final float f26 = (float)(l2 * 8);
                    final float f27 = (float)(j2 * 8);
                    final float f28 = f26 - f23;
                    final float f29 = f27 - f24;
                    if (f4 > -5.0f) {
                        worldrenderer.pos(f28 + 0.0f, f4 + 0.0f, f29 + 8.0f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f14, f15, f16, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f28 + 8.0f, f4 + 0.0f, f29 + 8.0f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f14, f15, f16, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f28 + 8.0f, f4 + 0.0f, f29 + 0.0f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f14, f15, f16, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f28 + 0.0f, f4 + 0.0f, f29 + 0.0f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f14, f15, f16, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                    }
                    if (f4 <= 5.0f) {
                        worldrenderer.pos(f28 + 0.0f, f4 + 4.0f - 9.765625E-4f, f29 + 8.0f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f5, f6, f7, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f28 + 8.0f, f4 + 4.0f - 9.765625E-4f, f29 + 8.0f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f5, f6, f7, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f28 + 8.0f, f4 + 4.0f - 9.765625E-4f, f29 + 0.0f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f5, f6, f7, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f28 + 0.0f, f4 + 4.0f - 9.765625E-4f, f29 + 0.0f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f5, f6, f7, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                    }
                    if (l2 > -1) {
                        for (int k2 = 0; k2 < 8; ++k2) {
                            worldrenderer.pos(f28 + k2 + 0.0f, f4 + 0.0f, f29 + 8.0f).tex((f26 + k2 + 0.5f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f28 + k2 + 0.0f, f4 + 4.0f, f29 + 8.0f).tex((f26 + k2 + 0.5f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f28 + k2 + 0.0f, f4 + 4.0f, f29 + 0.0f).tex((f26 + k2 + 0.5f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f28 + k2 + 0.0f, f4 + 0.0f, f29 + 0.0f).tex((f26 + k2 + 0.5f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                        }
                    }
                    if (l2 <= 1) {
                        for (int i3 = 0; i3 < 8; ++i3) {
                            worldrenderer.pos(f28 + i3 + 1.0f - 9.765625E-4f, f4 + 0.0f, f29 + 8.0f).tex((f26 + i3 + 0.5f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f28 + i3 + 1.0f - 9.765625E-4f, f4 + 4.0f, f29 + 8.0f).tex((f26 + i3 + 0.5f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f28 + i3 + 1.0f - 9.765625E-4f, f4 + 4.0f, f29 + 0.0f).tex((f26 + i3 + 0.5f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f28 + i3 + 1.0f - 9.765625E-4f, f4 + 0.0f, f29 + 0.0f).tex((f26 + i3 + 0.5f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                        }
                    }
                    if (j2 > -1) {
                        for (int j3 = 0; j3 < 8; ++j3) {
                            worldrenderer.pos(f28 + 0.0f, f4 + 4.0f, f29 + j3 + 0.0f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + j3 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            worldrenderer.pos(f28 + 8.0f, f4 + 4.0f, f29 + j3 + 0.0f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + j3 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            worldrenderer.pos(f28 + 8.0f, f4 + 0.0f, f29 + j3 + 0.0f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + j3 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            worldrenderer.pos(f28 + 0.0f, f4 + 0.0f, f29 + j3 + 0.0f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + j3 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                        }
                    }
                    if (j2 <= 1) {
                        for (int k3 = 0; k3 < 8; ++k3) {
                            worldrenderer.pos(f28 + 0.0f, f4 + 4.0f, f29 + k3 + 1.0f - 9.765625E-4f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + k3 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            worldrenderer.pos(f28 + 8.0f, f4 + 4.0f, f29 + k3 + 1.0f - 9.765625E-4f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + k3 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            worldrenderer.pos(f28 + 8.0f, f4 + 0.0f, f29 + k3 + 1.0f - 9.765625E-4f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + k3 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            worldrenderer.pos(f28 + 0.0f, f4 + 0.0f, f29 + k3 + 1.0f - 9.765625E-4f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + k3 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                        }
                    }
                    tessellator.draw();
                }
            }
            this.cloudRenderer.endUpdateGlList();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }
    
    public void updateChunks(long finishTimeNano) {
        finishTimeNano += (long)1.0E8;
        this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(finishTimeNano);
        if (this.chunksToUpdateForced.size() > 0) {
            final Iterator iterator = this.chunksToUpdateForced.iterator();
            while (iterator.hasNext()) {
                final RenderChunk renderchunk = iterator.next();
                if (!this.renderDispatcher.updateChunkLater(renderchunk)) {
                    break;
                }
                renderchunk.setNeedsUpdate(false);
                iterator.remove();
                this.chunksToUpdate.remove(renderchunk);
                this.chunksToResortTransparency.remove(renderchunk);
            }
        }
        if (this.chunksToResortTransparency.size() > 0) {
            final Iterator iterator2 = this.chunksToResortTransparency.iterator();
            if (iterator2.hasNext()) {
                final RenderChunk renderchunk2 = iterator2.next();
                if (this.renderDispatcher.updateTransparencyLater(renderchunk2)) {
                    iterator2.remove();
                }
            }
        }
        double d1 = 0.0;
        final int i = Config.getUpdatesPerFrame();
        if (!this.chunksToUpdate.isEmpty()) {
            final Iterator<RenderChunk> iterator3 = this.chunksToUpdate.iterator();
            while (iterator3.hasNext()) {
                final RenderChunk renderchunk3 = iterator3.next();
                final boolean flag = renderchunk3.isChunkRegionEmpty();
                boolean flag2;
                if (flag) {
                    flag2 = this.renderDispatcher.updateChunkNow(renderchunk3);
                }
                else {
                    flag2 = this.renderDispatcher.updateChunkLater(renderchunk3);
                }
                if (!flag2) {
                    break;
                }
                renderchunk3.setNeedsUpdate(false);
                iterator3.remove();
                if (flag) {
                    continue;
                }
                final double d2 = 2.0 * RenderChunkUtils.getRelativeBufferSize(renderchunk3);
                d1 += d2;
                if (d1 > i) {
                    break;
                }
            }
        }
    }
    
    public void renderWorldBorder(final Entity entityIn, final float partialTicks) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        final WorldBorder worldborder = this.theWorld.getWorldBorder();
        final double d0 = this.mc.gameSettings.renderDistanceChunks * 16;
        if (entityIn.posX >= worldborder.maxX() - d0 || entityIn.posX <= worldborder.minX() + d0 || entityIn.posZ >= worldborder.maxZ() - d0 || entityIn.posZ <= worldborder.minZ() + d0) {
            if (Config.isShaders()) {
                Shaders.pushProgram();
                Shaders.useProgram(Shaders.ProgramTexturedLit);
            }
            double d2 = 1.0 - worldborder.getClosestDistance(entityIn) / d0;
            d2 = Math.pow(d2, 4.0);
            final double d3 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
            final double d4 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
            final double d5 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            this.renderEngine.bindTexture(RenderGlobal.locationForcefieldPng);
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            final int i = worldborder.getStatus().getID();
            final float f = (i >> 16 & 0xFF) / 255.0f;
            final float f2 = (i >> 8 & 0xFF) / 255.0f;
            final float f3 = (i & 0xFF) / 255.0f;
            GlStateManager.color(f, f2, f3, (float)d2);
            GlStateManager.doPolygonOffset(-3.0f, -3.0f);
            GlStateManager.enablePolygonOffset();
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            final float f4 = Minecraft.getSystemTime() % 3000L / 3000.0f;
            final float f5 = 0.0f;
            final float f6 = 0.0f;
            final float f7 = 128.0f;
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.setTranslation(-d3, -d4, -d5);
            double d6 = Math.max(MathHelper.floor_double(d5 - d0), worldborder.minZ());
            double d7 = Math.min(MathHelper.ceiling_double_int(d5 + d0), worldborder.maxZ());
            if (d3 > worldborder.maxX() - d0) {
                float f8 = 0.0f;
                for (double d8 = d6; d8 < d7; ++d8, f8 += 0.5f) {
                    final double d9 = Math.min(1.0, d7 - d8);
                    final float f9 = (float)d9 * 0.5f;
                    worldrenderer.pos(worldborder.maxX(), 256.0, d8).tex(f4 + f8, f4 + 0.0f).endVertex();
                    worldrenderer.pos(worldborder.maxX(), 256.0, d8 + d9).tex(f4 + f9 + f8, f4 + 0.0f).endVertex();
                    worldrenderer.pos(worldborder.maxX(), 0.0, d8 + d9).tex(f4 + f9 + f8, f4 + 128.0f).endVertex();
                    worldrenderer.pos(worldborder.maxX(), 0.0, d8).tex(f4 + f8, f4 + 128.0f).endVertex();
                }
            }
            if (d3 < worldborder.minX() + d0) {
                float f10 = 0.0f;
                for (double d10 = d6; d10 < d7; ++d10, f10 += 0.5f) {
                    final double d11 = Math.min(1.0, d7 - d10);
                    final float f11 = (float)d11 * 0.5f;
                    worldrenderer.pos(worldborder.minX(), 256.0, d10).tex(f4 + f10, f4 + 0.0f).endVertex();
                    worldrenderer.pos(worldborder.minX(), 256.0, d10 + d11).tex(f4 + f11 + f10, f4 + 0.0f).endVertex();
                    worldrenderer.pos(worldborder.minX(), 0.0, d10 + d11).tex(f4 + f11 + f10, f4 + 128.0f).endVertex();
                    worldrenderer.pos(worldborder.minX(), 0.0, d10).tex(f4 + f10, f4 + 128.0f).endVertex();
                }
            }
            d6 = Math.max(MathHelper.floor_double(d3 - d0), worldborder.minX());
            d7 = Math.min(MathHelper.ceiling_double_int(d3 + d0), worldborder.maxX());
            if (d5 > worldborder.maxZ() - d0) {
                float f12 = 0.0f;
                for (double d12 = d6; d12 < d7; ++d12, f12 += 0.5f) {
                    final double d13 = Math.min(1.0, d7 - d12);
                    final float f13 = (float)d13 * 0.5f;
                    worldrenderer.pos(d12, 256.0, worldborder.maxZ()).tex(f4 + f12, f4 + 0.0f).endVertex();
                    worldrenderer.pos(d12 + d13, 256.0, worldborder.maxZ()).tex(f4 + f13 + f12, f4 + 0.0f).endVertex();
                    worldrenderer.pos(d12 + d13, 0.0, worldborder.maxZ()).tex(f4 + f13 + f12, f4 + 128.0f).endVertex();
                    worldrenderer.pos(d12, 0.0, worldborder.maxZ()).tex(f4 + f12, f4 + 128.0f).endVertex();
                }
            }
            if (d5 < worldborder.minZ() + d0) {
                float f14 = 0.0f;
                for (double d14 = d6; d14 < d7; ++d14, f14 += 0.5f) {
                    final double d15 = Math.min(1.0, d7 - d14);
                    final float f15 = (float)d15 * 0.5f;
                    worldrenderer.pos(d14, 256.0, worldborder.minZ()).tex(f4 + f14, f4 + 0.0f).endVertex();
                    worldrenderer.pos(d14 + d15, 256.0, worldborder.minZ()).tex(f4 + f15 + f14, f4 + 0.0f).endVertex();
                    worldrenderer.pos(d14 + d15, 0.0, worldborder.minZ()).tex(f4 + f15 + f14, f4 + 128.0f).endVertex();
                    worldrenderer.pos(d14, 0.0, worldborder.minZ()).tex(f4 + f14, f4 + 128.0f).endVertex();
                }
            }
            tessellator.draw();
            worldrenderer.setTranslation(0.0, 0.0, 0.0);
            GlStateManager.enableCull();
            GlStateManager.disableAlpha();
            GlStateManager.doPolygonOffset(0.0f, 0.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.enableAlpha();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
            if (Config.isShaders()) {
                Shaders.popProgram();
            }
        }
    }
    
    private void preRenderDamagedBlocks() {
        GlStateManager.tryBlendFuncSeparate(774, 768, 1, 0);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
        GlStateManager.doPolygonOffset(-1.0f, -10.0f);
        GlStateManager.enablePolygonOffset();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
        if (Config.isShaders()) {
            ShadersRender.beginBlockDamage();
        }
    }
    
    private void postRenderDamagedBlocks() {
        GlStateManager.disableAlpha();
        GlStateManager.doPolygonOffset(0.0f, 0.0f);
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
        if (Config.isShaders()) {
            ShadersRender.endBlockDamage();
        }
    }
    
    public void drawBlockDamageTexture(final Tessellator tessellatorIn, final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks) {
        final double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
        final double d2 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
        final double d3 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
        if (!this.damagedBlocks.isEmpty()) {
            this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            this.preRenderDamagedBlocks();
            worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
            worldRendererIn.setTranslation(-d0, -d2, -d3);
            worldRendererIn.noColor();
            final Iterator<DestroyBlockProgress> iterator = this.damagedBlocks.values().iterator();
            while (iterator.hasNext()) {
                final DestroyBlockProgress destroyblockprogress = iterator.next();
                final BlockPos blockpos = destroyblockprogress.getPosition();
                final double d4 = blockpos.getX() - d0;
                final double d5 = blockpos.getY() - d2;
                final double d6 = blockpos.getZ() - d3;
                final Block block = this.theWorld.getBlockState(blockpos).getBlock();
                final boolean flag = !(block instanceof BlockChest) && !(block instanceof BlockEnderChest) && !(block instanceof BlockSign) && !(block instanceof BlockSkull);
                if (flag) {
                    if (d4 * d4 + d5 * d5 + d6 * d6 > 1024.0) {
                        iterator.remove();
                    }
                    else {
                        final IBlockState iblockstate = this.theWorld.getBlockState(blockpos);
                        if (iblockstate.getBlock().getMaterial() == Material.air) {
                            continue;
                        }
                        final int i = destroyblockprogress.getPartialBlockDamage();
                        final TextureAtlasSprite textureatlassprite = this.destroyBlockIcons[i];
                        final BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
                        blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, this.theWorld);
                    }
                }
            }
            tessellatorIn.draw();
            worldRendererIn.setTranslation(0.0, 0.0, 0.0);
            this.postRenderDamagedBlocks();
        }
    }
    
    public void drawSelectionBox(final EntityPlayer player, final MovingObjectPosition movingObjectPositionIn, final int execute, final float partialTicks) {
        if (execute == 0 && movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.4f);
            GL11.glLineWidth(2.0f);
            GlStateManager.disableTexture2D();
            if (Config.isShaders()) {
                Shaders.disableTexture2D();
            }
            GlStateManager.depthMask(false);
            final float f = 0.002f;
            final BlockPos blockpos = movingObjectPositionIn.getBlockPos();
            if (!Flight.hiddenBlocks.contains(blockpos)) {
                final Block block = this.theWorld.getBlockState(blockpos).getBlock();
                if (block.getMaterial() != Material.air && this.theWorld.getWorldBorder().contains(blockpos)) {
                    block.setBlockBoundsBasedOnState(this.theWorld, blockpos);
                    final double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
                    final double d2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
                    final double d3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
                    AxisAlignedBB axisalignedbb = block.getSelectedBoundingBox(this.theWorld, blockpos);
                    final Block.EnumOffsetType block$enumoffsettype = block.getOffsetType();
                    if (block$enumoffsettype != Block.EnumOffsetType.NONE) {
                        axisalignedbb = BlockModelUtils.getOffsetBoundingBox(axisalignedbb, block$enumoffsettype, blockpos);
                    }
                    drawSelectionBoundingBox(axisalignedbb.expand(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026).offset(-d0, -d2, -d3));
                }
            }
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            if (Config.isShaders()) {
                Shaders.enableTexture2D();
            }
            GlStateManager.disableBlend();
        }
    }
    
    public static void renderCustomBoundingBox(final AxisAlignedBB bb, final boolean outline, final boolean filled) {
        if (outline) {
            GL11.glBegin(3);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glEnd();
            GL11.glBegin(3);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glEnd();
            GL11.glBegin(1);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glEnd();
        }
        if (filled) {
            GL11.glBegin(7);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glEnd();
            GL11.glBegin(7);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glEnd();
            GL11.glBegin(7);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glEnd();
            GL11.glBegin(7);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glEnd();
            GL11.glBegin(7);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glEnd();
            GL11.glBegin(7);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glEnd();
        }
    }
    
    public static void drawSelectionBoundingBox(final AxisAlignedBB boundingBox) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(1, DefaultVertexFormats.POSITION);
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }
    
    public static void drawOutlinedBoundingBox(final AxisAlignedBB boundingBox, final int red, final int green, final int blue, final int alpha) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        worldrenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
    }
    
    private void markBlocksForUpdate(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        this.viewFrustum.markBlocksForUpdate(x1, y1, z1, x2, y2, z2);
    }
    
    @Override
    public void markBlockForUpdate(final BlockPos pos) {
        final int i = pos.getX();
        final int j = pos.getY();
        final int k = pos.getZ();
        this.markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
    }
    
    @Override
    public void notifyLightSet(final BlockPos pos) {
        final int i = pos.getX();
        final int j = pos.getY();
        final int k = pos.getZ();
        this.markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
    }
    
    @Override
    public void markBlockRangeForRenderUpdate(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        this.markBlocksForUpdate(x1 - 1, y1 - 1, z1 - 1, x2 + 1, y2 + 1, z2 + 1);
    }
    
    @Override
    public void playRecord(final String recordName, final BlockPos blockPosIn) {
        final ISound isound = this.mapSoundPositions.get(blockPosIn);
        if (isound != null) {
            this.mc.getSoundHandler().stopSound(isound);
            this.mapSoundPositions.remove(blockPosIn);
        }
        if (recordName != null) {
            final ItemRecord itemrecord = ItemRecord.getRecord(recordName);
            if (itemrecord != null) {
                this.mc.ingameGUI.setRecordPlayingMessage(itemrecord.getRecordNameLocal());
            }
            final PositionedSoundRecord positionedsoundrecord = PositionedSoundRecord.create(new ResourceLocation(recordName), (float)blockPosIn.getX(), (float)blockPosIn.getY(), (float)blockPosIn.getZ());
            this.mapSoundPositions.put(blockPosIn, positionedsoundrecord);
            this.mc.getSoundHandler().playSound(positionedsoundrecord);
        }
    }
    
    @Override
    public void playSound(final String soundName, final double x, final double y, final double z, final float volume, final float pitch) {
    }
    
    @Override
    public void playSoundToNearExcept(final EntityPlayer except, final String soundName, final double x, final double y, final double z, final float volume, final float pitch) {
    }
    
    @Override
    public void spawnParticle(final int particleID, final boolean ignoreRange, final double xCoord, final double yCoord, final double zCoord, final double xOffset, final double yOffset, final double zOffset, final int... parameters) {
        try {
            this.spawnEntityFX(particleID, ignoreRange, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, parameters);
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while adding particle");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being added");
            crashreportcategory.addCrashSection("ID", particleID);
            if (parameters != null) {
                crashreportcategory.addCrashSection("Parameters", parameters);
            }
            crashreportcategory.addCrashSectionCallable("Position", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(xCoord, yCoord, zCoord);
                }
            });
            throw new ReportedException(crashreport);
        }
    }
    
    private void spawnParticle(final EnumParticleTypes particleIn, final double xCoord, final double yCoord, final double zCoord, final double xOffset, final double yOffset, final double zOffset, final int... parameters) {
        this.spawnParticle(particleIn.getParticleID(), particleIn.getShouldIgnoreRange(), xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, parameters);
    }
    
    private EntityFX spawnEntityFX(final int particleID, final boolean ignoreRange, final double xCoord, final double yCoord, final double zCoord, final double xOffset, final double yOffset, final double zOffset, final int... parameters) {
        if (this.mc == null || this.mc.getRenderViewEntity() == null || this.mc.effectRenderer == null) {
            return null;
        }
        int i = this.mc.gameSettings.particleSetting;
        if (i == 1 && this.theWorld.rand.nextInt(3) == 0) {
            i = 2;
        }
        final double d0 = this.mc.getRenderViewEntity().posX - xCoord;
        final double d2 = this.mc.getRenderViewEntity().posY - yCoord;
        final double d3 = this.mc.getRenderViewEntity().posZ - zCoord;
        if (particleID == EnumParticleTypes.EXPLOSION_HUGE.getParticleID() && !Config.isAnimatedExplosion()) {
            return null;
        }
        if (particleID == EnumParticleTypes.EXPLOSION_LARGE.getParticleID() && !Config.isAnimatedExplosion()) {
            return null;
        }
        if (particleID == EnumParticleTypes.EXPLOSION_NORMAL.getParticleID() && !Config.isAnimatedExplosion()) {
            return null;
        }
        if (particleID == EnumParticleTypes.SUSPENDED.getParticleID() && !Config.isWaterParticles()) {
            return null;
        }
        if (particleID == EnumParticleTypes.SUSPENDED_DEPTH.getParticleID() && !Config.isVoidParticles()) {
            return null;
        }
        if (particleID == EnumParticleTypes.SMOKE_NORMAL.getParticleID() && !Config.isAnimatedSmoke()) {
            return null;
        }
        if (particleID == EnumParticleTypes.SMOKE_LARGE.getParticleID() && !Config.isAnimatedSmoke()) {
            return null;
        }
        if (particleID == EnumParticleTypes.SPELL_MOB.getParticleID() && !Config.isPotionParticles()) {
            return null;
        }
        if (particleID == EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID() && !Config.isPotionParticles()) {
            return null;
        }
        if (particleID == EnumParticleTypes.SPELL.getParticleID() && !Config.isPotionParticles()) {
            return null;
        }
        if (particleID == EnumParticleTypes.SPELL_INSTANT.getParticleID() && !Config.isPotionParticles()) {
            return null;
        }
        if (particleID == EnumParticleTypes.SPELL_WITCH.getParticleID() && !Config.isPotionParticles()) {
            return null;
        }
        if (particleID == EnumParticleTypes.PORTAL.getParticleID() && !Config.isPortalParticles()) {
            return null;
        }
        if (particleID == EnumParticleTypes.FLAME.getParticleID() && !Config.isAnimatedFlame()) {
            return null;
        }
        if (particleID == EnumParticleTypes.REDSTONE.getParticleID() && !Config.isAnimatedRedstone()) {
            return null;
        }
        if (particleID == EnumParticleTypes.DRIP_WATER.getParticleID() && !Config.isDrippingWaterLava()) {
            return null;
        }
        if (particleID == EnumParticleTypes.DRIP_LAVA.getParticleID() && !Config.isDrippingWaterLava()) {
            return null;
        }
        if (particleID == EnumParticleTypes.FIREWORKS_SPARK.getParticleID() && !Config.isFireworkParticles()) {
            return null;
        }
        if (!ignoreRange) {
            double d4 = 256.0;
            if (particleID == EnumParticleTypes.CRIT.getParticleID()) {
                d4 = 38416.0;
            }
            if (d0 * d0 + d2 * d2 + d3 * d3 > d4) {
                return null;
            }
            if (i > 1) {
                return null;
            }
        }
        final EntityFX entityfx = this.mc.effectRenderer.spawnEffectParticle(particleID, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, parameters);
        if (particleID == EnumParticleTypes.WATER_BUBBLE.getParticleID()) {
            CustomColors.updateWaterFX(entityfx, this.theWorld, xCoord, yCoord, zCoord, this.renderEnv);
        }
        if (particleID == EnumParticleTypes.WATER_SPLASH.getParticleID()) {
            CustomColors.updateWaterFX(entityfx, this.theWorld, xCoord, yCoord, zCoord, this.renderEnv);
        }
        if (particleID == EnumParticleTypes.WATER_DROP.getParticleID()) {
            CustomColors.updateWaterFX(entityfx, this.theWorld, xCoord, yCoord, zCoord, this.renderEnv);
        }
        if (particleID == EnumParticleTypes.TOWN_AURA.getParticleID()) {
            CustomColors.updateMyceliumFX(entityfx);
        }
        if (particleID == EnumParticleTypes.PORTAL.getParticleID()) {
            CustomColors.updatePortalFX(entityfx);
        }
        if (particleID == EnumParticleTypes.REDSTONE.getParticleID()) {
            CustomColors.updateReddustFX(entityfx, this.theWorld, xCoord, yCoord, zCoord);
        }
        return entityfx;
    }
    
    @Override
    public void onEntityAdded(final Entity entityIn) {
        RandomEntities.entityLoaded(entityIn, this.theWorld);
        if (Config.isDynamicLights()) {
            DynamicLights.entityAdded(entityIn, this);
        }
    }
    
    @Override
    public void onEntityRemoved(final Entity entityIn) {
        RandomEntities.entityUnloaded(entityIn, this.theWorld);
        if (Config.isDynamicLights()) {
            DynamicLights.entityRemoved(entityIn, this);
        }
    }
    
    public void deleteAllDisplayLists() {
    }
    
    @Override
    public void broadcastSound(final int soundID, final BlockPos pos, final int data) {
        switch (soundID) {
            case 1013:
            case 1018: {
                if (this.mc.getRenderViewEntity() == null) {
                    break;
                }
                final double d0 = pos.getX() - this.mc.getRenderViewEntity().posX;
                final double d2 = pos.getY() - this.mc.getRenderViewEntity().posY;
                final double d3 = pos.getZ() - this.mc.getRenderViewEntity().posZ;
                final double d4 = Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
                double d5 = this.mc.getRenderViewEntity().posX;
                double d6 = this.mc.getRenderViewEntity().posY;
                double d7 = this.mc.getRenderViewEntity().posZ;
                if (d4 > 0.0) {
                    d5 += d0 / d4 * 2.0;
                    d6 += d2 / d4 * 2.0;
                    d7 += d3 / d4 * 2.0;
                }
                if (soundID == 1013) {
                    this.theWorld.playSound(d5, d6, d7, "mob.wither.spawn", 1.0f, 1.0f, false);
                    break;
                }
                this.theWorld.playSound(d5, d6, d7, "mob.enderdragon.end", 5.0f, 1.0f, false);
                break;
            }
        }
    }
    
    @Override
    public void playAuxSFX(final EntityPlayer player, final int sfxType, final BlockPos blockPosIn, final int data) {
        final Random random = this.theWorld.rand;
        switch (sfxType) {
            case 1000: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0f, 1.0f, false);
                break;
            }
            case 1001: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0f, 1.2f, false);
                break;
            }
            case 1002: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.bow", 1.0f, 1.2f, false);
                break;
            }
            case 1003: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.door_open", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1004: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.fizz", 0.5f, 2.6f + (random.nextFloat() - random.nextFloat()) * 0.8f, false);
                break;
            }
            case 1005: {
                if (Item.getItemById(data) instanceof ItemRecord) {
                    this.theWorld.playRecord(blockPosIn, "records." + ((ItemRecord)Item.getItemById(data)).recordName);
                    break;
                }
                this.theWorld.playRecord(blockPosIn, null);
                break;
            }
            case 1006: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.door_close", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1007: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.charge", 10.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1008: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 10.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1009: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1010: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.wood", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1011: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.metal", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1012: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.woodbreak", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1014: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.wither.shoot", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1015: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.bat.takeoff", 0.05f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1016: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.infect", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1017: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.unfect", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1020: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_break", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1021: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_use", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1022: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_land", 0.3f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 2000: {
                final int i = data % 3 - 1;
                final int j = data / 3 % 3 - 1;
                final double d0 = blockPosIn.getX() + i * 0.6 + 0.5;
                final double d2 = blockPosIn.getY() + 0.5;
                final double d3 = blockPosIn.getZ() + j * 0.6 + 0.5;
                for (int i2 = 0; i2 < 10; ++i2) {
                    final double d4 = random.nextDouble() * 0.2 + 0.01;
                    final double d5 = d0 + i * 0.01 + (random.nextDouble() - 0.5) * j * 0.5;
                    final double d6 = d2 + (random.nextDouble() - 0.5) * 0.5;
                    final double d7 = d3 + j * 0.01 + (random.nextDouble() - 0.5) * i * 0.5;
                    final double d8 = i * d4 + random.nextGaussian() * 0.01;
                    final double d9 = -0.03 + random.nextGaussian() * 0.01;
                    final double d10 = j * d4 + random.nextGaussian() * 0.01;
                    this.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d5, d6, d7, d8, d9, d10, new int[0]);
                }
            }
            case 2001: {
                final Block block = Block.getBlockById(data & 0xFFF);
                if (block.getMaterial() != Material.air) {
                    this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(block.stepSound.getBreakSound()), (block.stepSound.getVolume() + 1.0f) / 2.0f, block.stepSound.getFrequency() * 0.8f, blockPosIn.getX() + 0.5f, blockPosIn.getY() + 0.5f, blockPosIn.getZ() + 0.5f));
                }
                this.mc.effectRenderer.addBlockDestroyEffects(blockPosIn, block.getStateFromMeta(data >> 12 & 0xFF));
                break;
            }
            case 2002: {
                final double d11 = blockPosIn.getX();
                final double d12 = blockPosIn.getY();
                final double d13 = blockPosIn.getZ();
                for (int k = 0; k < 8; ++k) {
                    this.spawnParticle(EnumParticleTypes.ITEM_CRACK, d11, d12, d13, random.nextGaussian() * 0.15, random.nextDouble() * 0.2, random.nextGaussian() * 0.15, Item.getIdFromItem(Items.potionitem), data);
                }
                final int j2 = Items.potionitem.getColorFromDamage(data);
                final float f = (j2 >> 16 & 0xFF) / 255.0f;
                final float f2 = (j2 >> 8 & 0xFF) / 255.0f;
                final float f3 = (j2 >> 0 & 0xFF) / 255.0f;
                EnumParticleTypes enumparticletypes = EnumParticleTypes.SPELL;
                if (Items.potionitem.isEffectInstant(data)) {
                    enumparticletypes = EnumParticleTypes.SPELL_INSTANT;
                }
                for (int k2 = 0; k2 < 100; ++k2) {
                    final double d14 = random.nextDouble() * 4.0;
                    final double d15 = random.nextDouble() * 3.141592653589793 * 2.0;
                    final double d16 = Math.cos(d15) * d14;
                    final double d17 = 0.01 + random.nextDouble() * 0.5;
                    final double d18 = Math.sin(d15) * d14;
                    final EntityFX entityfx = this.spawnEntityFX(enumparticletypes.getParticleID(), enumparticletypes.getShouldIgnoreRange(), d11 + d16 * 0.1, d12 + 0.3, d13 + d18 * 0.1, d16, d17, d18, new int[0]);
                    if (entityfx != null) {
                        final float f4 = 0.75f + random.nextFloat() * 0.25f;
                        entityfx.setRBGColorF(f * f4, f2 * f4, f3 * f4);
                        entityfx.multiplyVelocity((float)d14);
                    }
                }
                this.theWorld.playSoundAtPos(blockPosIn, "game.potion.smash", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 2003: {
                final double d19 = blockPosIn.getX() + 0.5;
                final double d20 = blockPosIn.getY();
                final double d21 = blockPosIn.getZ() + 0.5;
                for (int l1 = 0; l1 < 8; ++l1) {
                    this.spawnParticle(EnumParticleTypes.ITEM_CRACK, d19, d20, d21, random.nextGaussian() * 0.15, random.nextDouble() * 0.2, random.nextGaussian() * 0.15, Item.getIdFromItem(Items.ender_eye));
                }
                for (double d22 = 0.0; d22 < 6.283185307179586; d22 += 0.15707963267948966) {
                    this.spawnParticle(EnumParticleTypes.PORTAL, d19 + Math.cos(d22) * 5.0, d20 - 0.4, d21 + Math.sin(d22) * 5.0, Math.cos(d22) * -5.0, 0.0, Math.sin(d22) * -5.0, new int[0]);
                    this.spawnParticle(EnumParticleTypes.PORTAL, d19 + Math.cos(d22) * 5.0, d20 - 0.4, d21 + Math.sin(d22) * 5.0, Math.cos(d22) * -7.0, 0.0, Math.sin(d22) * -7.0, new int[0]);
                }
            }
            case 2004: {
                for (int m = 0; m < 20; ++m) {
                    final double d23 = blockPosIn.getX() + 0.5 + (this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    final double d24 = blockPosIn.getY() + 0.5 + (this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    final double d25 = blockPosIn.getZ() + 0.5 + (this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    this.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d23, d24, d25, 0.0, 0.0, 0.0, new int[0]);
                    this.theWorld.spawnParticle(EnumParticleTypes.FLAME, d23, d24, d25, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            case 2005: {
                ItemDye.spawnBonemealParticles(this.theWorld, blockPosIn, data);
                break;
            }
        }
    }
    
    @Override
    public void sendBlockBreakProgress(final int breakerId, final BlockPos pos, final int progress) {
        if (progress >= 0 && progress < 10) {
            DestroyBlockProgress destroyblockprogress = this.damagedBlocks.get(breakerId);
            if (destroyblockprogress == null || destroyblockprogress.getPosition().getX() != pos.getX() || destroyblockprogress.getPosition().getY() != pos.getY() || destroyblockprogress.getPosition().getZ() != pos.getZ()) {
                destroyblockprogress = new DestroyBlockProgress(breakerId, pos);
                this.damagedBlocks.put(breakerId, destroyblockprogress);
            }
            destroyblockprogress.setPartialBlockDamage(progress);
            destroyblockprogress.setCloudUpdateTick(this.cloudTickCounter);
        }
        else {
            this.damagedBlocks.remove(breakerId);
        }
    }
    
    public void setDisplayListEntitiesDirty() {
        this.displayListEntitiesDirty = true;
    }
    
    public boolean hasNoChunkUpdates() {
        return this.chunksToUpdate.isEmpty() && this.renderDispatcher.hasChunkUpdates();
    }
    
    public void resetClouds() {
        this.cloudRenderer.reset();
    }
    
    public int getCountRenderers() {
        return this.viewFrustum.renderChunks.length;
    }
    
    public int getCountActiveRenderers() {
        return this.renderInfos.size();
    }
    
    public int getCountEntitiesRendered() {
        return this.countEntitiesRendered;
    }
    
    public int getCountTileEntitiesRendered() {
        return this.countTileEntitiesRendered;
    }
    
    public int getCountLoadedChunks() {
        if (this.theWorld == null) {
            return 0;
        }
        final IChunkProvider ichunkprovider = this.theWorld.getChunkProvider();
        if (ichunkprovider == null) {
            return 0;
        }
        if (ichunkprovider != this.worldChunkProvider) {
            this.worldChunkProvider = ichunkprovider;
            this.worldChunkProviderMap = (LongHashMap)Reflector.getFieldValue(ichunkprovider, Reflector.ChunkProviderClient_chunkMapping);
        }
        return (this.worldChunkProviderMap == null) ? 0 : this.worldChunkProviderMap.getNumHashElements();
    }
    
    public int getCountChunksToUpdate() {
        return this.chunksToUpdate.size();
    }
    
    public RenderChunk getRenderChunk(final BlockPos p_getRenderChunk_1_) {
        return this.viewFrustum.getRenderChunk(p_getRenderChunk_1_);
    }
    
    public WorldClient getWorld() {
        return this.theWorld;
    }
    
    private void clearRenderInfos() {
        if (RenderGlobal.renderEntitiesCounter > 0) {
            this.renderInfos = new ArrayList<ContainerLocalRenderInformation>(this.renderInfos.size() + 16);
            this.renderInfosEntities = new ArrayList(this.renderInfosEntities.size() + 16);
            this.renderInfosTileEntities = new ArrayList(this.renderInfosTileEntities.size() + 16);
        }
        else {
            this.renderInfos.clear();
            this.renderInfosEntities.clear();
            this.renderInfosTileEntities.clear();
        }
    }
    
    public void onPlayerPositionSet() {
        if (this.firstWorldLoad) {
            this.loadRenderers();
            this.firstWorldLoad = false;
        }
    }
    
    public void pauseChunkUpdates() {
        if (this.renderDispatcher != null) {
            this.renderDispatcher.pauseChunkUpdates();
        }
    }
    
    public void resumeChunkUpdates() {
        if (this.renderDispatcher != null) {
            this.renderDispatcher.resumeChunkUpdates();
        }
    }
    
    public void updateTileEntities(final Collection<TileEntity> tileEntitiesToRemove, final Collection<TileEntity> tileEntitiesToAdd) {
        synchronized (this.setTileEntities) {
            this.setTileEntities.removeAll(tileEntitiesToRemove);
            this.setTileEntities.addAll(tileEntitiesToAdd);
        }
    }
    
    static {
        logger = LogManager.getLogger();
        locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
        locationSunPng = new ResourceLocation("textures/environment/sun.png");
        locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
        locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
        locationForcefieldPng = new ResourceLocation("textures/misc/forcefield.png");
        SET_ALL_FACINGS = Collections.unmodifiableSet((Set<?>)new HashSet<Object>(Arrays.asList(EnumFacing.VALUES)));
        RenderGlobal.renderEntitiesCounter = 0;
    }
    
    public static class ContainerLocalRenderInformation
    {
        final RenderChunk renderChunk;
        EnumFacing facing;
        int setFacing;
        
        public ContainerLocalRenderInformation(final RenderChunk p_i2_1_, final EnumFacing p_i2_2_, final int p_i2_3_) {
            this.renderChunk = p_i2_1_;
            this.facing = p_i2_2_;
            this.setFacing = p_i2_3_;
        }
        
        public void setFacingBit(final byte p_setFacingBit_1_, final EnumFacing p_setFacingBit_2_) {
            this.setFacing = (this.setFacing | p_setFacingBit_1_ | 1 << p_setFacingBit_2_.ordinal());
        }
        
        public boolean isFacingBit(final EnumFacing p_isFacingBit_1_) {
            return (this.setFacing & 1 << p_isFacingBit_1_.ordinal()) > 0;
        }
        
        private void initialize(final EnumFacing p_initialize_1_, final int p_initialize_2_) {
            this.facing = p_initialize_1_;
            this.setFacing = p_initialize_2_;
        }
    }
}
