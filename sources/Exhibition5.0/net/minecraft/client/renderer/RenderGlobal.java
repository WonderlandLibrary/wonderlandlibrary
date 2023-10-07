// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer;

import java.util.EnumSet;
import org.apache.logging.log4j.LogManager;
import net.minecraft.item.ItemDye;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.optifine.RandomMobs;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReportCategory;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.item.ItemRecord;
import net.minecraft.client.audio.ISound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.optifine.CustomSky;
import net.minecraft.world.IBlockAccess;
import net.minecraft.optifine.CustomColorizer;
import net.minecraft.optifine.Reflector;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumWorldBlockLayer;
import javax.vecmath.Tuple4f;
import javax.vecmath.Matrix4f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.chunk.VisGraph;
import javax.vecmath.Vector3f;
import java.util.LinkedList;
import net.minecraft.client.renderer.chunk.RenderChunk;
import java.util.Collection;
import net.minecraft.optifine.Lagometer;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ClassInheratanceMultiMap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import com.google.common.collect.Iterators;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.optifine.Config;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import java.util.Random;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import org.lwjgl.opengl.GL11;
import java.util.LinkedHashSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4f;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.util.Map;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import java.util.List;
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
    private static final ResourceLocation field_175006_g;
    public final Minecraft mc;
    private final TextureManager renderEngine;
    private final RenderManager field_175010_j;
    private WorldClient theWorld;
    private Set field_175009_l;
    private List glRenderLists;
    private ViewFrustum field_175008_n;
    private int starGLCallList;
    private int glSkyList;
    private int glSkyList2;
    private VertexFormat field_175014_r;
    private VertexBuffer field_175013_s;
    private VertexBuffer field_175012_t;
    private VertexBuffer field_175011_u;
    private int cloudTickCounter;
    public final Map damagedBlocks;
    private final Map mapSoundPositions;
    private final TextureAtlasSprite[] destroyBlockIcons;
    public Framebuffer buffer;
    public ShaderGroup shaders;
    private double field_174992_B;
    private double field_174993_C;
    private double field_174987_D;
    private int field_174988_E;
    private int field_174989_F;
    private int field_174990_G;
    private double field_174997_H;
    private double field_174998_I;
    private double field_174999_J;
    private double field_175000_K;
    private double field_174994_L;
    private final ChunkRenderDispatcher field_174995_M;
    private ChunkRenderContainer field_174996_N;
    private int renderDistanceChunks;
    private int renderEntitiesStartupCounter;
    private int countEntitiesTotal;
    private int countEntitiesRendered;
    private int countEntitiesHidden;
    private boolean field_175002_T;
    private ClippingHelper field_175001_U;
    private final Vector4f[] field_175004_V;
    private final Vector3d field_175003_W;
    private boolean field_175005_X;
    IRenderChunkFactory field_175007_a;
    private double prevRenderSortX;
    private double prevRenderSortY;
    private double prevRenderSortZ;
    public boolean displayListEntitiesDirty;
    private static final String __OBFID = "CL_00000954";
    private int glListClouds;
    private boolean isFancyGlListClouds;
    private int cloudTickCounterGlList;
    private double cloudPlayerX;
    private double cloudPlayerY;
    private double cloudPlayerZ;
    public Entity renderedEntity;
    public Set chunksToResortTransparency;
    public Set chunksToUpdateForced;
    
    public RenderGlobal(final Minecraft mcIn) {
        this.field_175009_l = Sets.newLinkedHashSet();
        this.glRenderLists = Lists.newArrayListWithCapacity(69696);
        this.starGLCallList = -1;
        this.glSkyList = -1;
        this.glSkyList2 = -1;
        this.damagedBlocks = Maps.newHashMap();
        this.mapSoundPositions = Maps.newHashMap();
        this.destroyBlockIcons = new TextureAtlasSprite[10];
        this.field_174992_B = Double.MIN_VALUE;
        this.field_174993_C = Double.MIN_VALUE;
        this.field_174987_D = Double.MIN_VALUE;
        this.field_174988_E = Integer.MIN_VALUE;
        this.field_174989_F = Integer.MIN_VALUE;
        this.field_174990_G = Integer.MIN_VALUE;
        this.field_174997_H = Double.MIN_VALUE;
        this.field_174998_I = Double.MIN_VALUE;
        this.field_174999_J = Double.MIN_VALUE;
        this.field_175000_K = Double.MIN_VALUE;
        this.field_174994_L = Double.MIN_VALUE;
        this.field_174995_M = new ChunkRenderDispatcher();
        this.renderDistanceChunks = -1;
        this.renderEntitiesStartupCounter = 2;
        this.field_175002_T = false;
        this.field_175004_V = new Vector4f[8];
        this.field_175003_W = new Vector3d();
        this.field_175005_X = false;
        this.displayListEntitiesDirty = true;
        this.glListClouds = -1;
        this.isFancyGlListClouds = false;
        this.cloudTickCounterGlList = -99999;
        this.cloudPlayerX = 0.0;
        this.cloudPlayerY = 0.0;
        this.cloudPlayerZ = 0.0;
        this.chunksToResortTransparency = new LinkedHashSet();
        this.chunksToUpdateForced = new LinkedHashSet();
        this.glListClouds = GLAllocation.generateDisplayLists(1);
        this.mc = mcIn;
        this.field_175010_j = mcIn.getRenderManager();
        (this.renderEngine = mcIn.getTextureManager()).bindTexture(RenderGlobal.field_175006_g);
        GL11.glTexParameteri(3553, 10242, 10497);
        GL11.glTexParameteri(3553, 10243, 10497);
        GlStateManager.func_179144_i(0);
        this.func_174971_n();
        this.field_175005_X = OpenGlHelper.func_176075_f();
        if (this.field_175005_X) {
            this.field_174996_N = new VboRenderList();
            this.field_175007_a = new VboChunkFactory();
        }
        else {
            this.field_174996_N = new RenderList();
            this.field_175007_a = new ListChunkFactory();
        }
        (this.field_175014_r = new VertexFormat()).func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.POSITION, 3));
        this.func_174963_q();
        this.func_174980_p();
        this.func_174964_o();
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        this.func_174971_n();
    }
    
    private void func_174971_n() {
        final TextureMap var1 = this.mc.getTextureMapBlocks();
        for (int var2 = 0; var2 < this.destroyBlockIcons.length; ++var2) {
            this.destroyBlockIcons[var2] = var1.getAtlasSprite("minecraft:blocks/destroy_stage_" + var2);
        }
    }
    
    public void func_174966_b() {
        if (OpenGlHelper.shadersSupported) {
            if (ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
                ShaderLinkHelper.setNewStaticShaderLinkHelper();
            }
            final ResourceLocation var1 = new ResourceLocation("shaders/post/entity_outline.json");
            try {
                (this.shaders = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), var1)).createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
                this.buffer = this.shaders.func_177066_a("final");
            }
            catch (IOException var2) {
                RenderGlobal.logger.warn("Failed to load shader: " + var1, (Throwable)var2);
                this.shaders = null;
                this.buffer = null;
            }
            catch (JsonSyntaxException var3) {
                RenderGlobal.logger.warn("Failed to load shader: " + var1, (Throwable)var3);
                this.shaders = null;
                this.buffer = null;
            }
        }
        else {
            this.shaders = null;
            this.buffer = null;
        }
    }
    
    public void func_174975_c() {
        if (this.func_174985_d()) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            this.buffer.func_178038_a(this.mc.displayWidth, this.mc.displayHeight, false);
            GlStateManager.disableBlend();
        }
    }
    
    protected boolean func_174985_d() {
        return this.buffer != null && this.shaders != null && this.mc.thePlayer != null && this.mc.thePlayer.func_175149_v() && this.mc.gameSettings.keybindSpectateView.getIsKeyPressed();
    }
    
    private void func_174964_o() {
        final Tessellator var1 = Tessellator.getInstance();
        final WorldRenderer var2 = var1.getWorldRenderer();
        if (this.field_175011_u != null) {
            this.field_175011_u.func_177362_c();
        }
        if (this.glSkyList2 >= 0) {
            GLAllocation.deleteDisplayLists(this.glSkyList2);
            this.glSkyList2 = -1;
        }
        if (this.field_175005_X) {
            this.field_175011_u = new VertexBuffer(this.field_175014_r);
            this.func_174968_a(var2, -16.0f, true);
            var2.draw();
            var2.reset();
            this.field_175011_u.func_177360_a(var2.func_178966_f(), var2.func_178976_e());
        }
        else {
            GL11.glNewList(this.glSkyList2 = GLAllocation.generateDisplayLists(1), 4864);
            this.func_174968_a(var2, -16.0f, true);
            var1.draw();
            GL11.glEndList();
        }
    }
    
    private void func_174980_p() {
        final Tessellator var1 = Tessellator.getInstance();
        final WorldRenderer var2 = var1.getWorldRenderer();
        if (this.field_175012_t != null) {
            this.field_175012_t.func_177362_c();
        }
        if (this.glSkyList >= 0) {
            GLAllocation.deleteDisplayLists(this.glSkyList);
            this.glSkyList = -1;
        }
        if (this.field_175005_X) {
            this.field_175012_t = new VertexBuffer(this.field_175014_r);
            this.func_174968_a(var2, 16.0f, false);
            var2.draw();
            var2.reset();
            this.field_175012_t.func_177360_a(var2.func_178966_f(), var2.func_178976_e());
        }
        else {
            GL11.glNewList(this.glSkyList = GLAllocation.generateDisplayLists(1), 4864);
            this.func_174968_a(var2, 16.0f, false);
            var1.draw();
            GL11.glEndList();
        }
    }
    
    private void func_174968_a(final WorldRenderer worldRendererIn, final float p_174968_2_, final boolean p_174968_3_) {
        final boolean var4 = true;
        final boolean var5 = true;
        worldRendererIn.startDrawingQuads();
        for (int var6 = -384; var6 <= 384; var6 += 64) {
            for (int var7 = -384; var7 <= 384; var7 += 64) {
                float var8 = var6;
                float var9 = var6 + 64;
                if (p_174968_3_) {
                    var9 = var6;
                    var8 = var6 + 64;
                }
                worldRendererIn.addVertex(var8, p_174968_2_, var7);
                worldRendererIn.addVertex(var9, p_174968_2_, var7);
                worldRendererIn.addVertex(var9, p_174968_2_, var7 + 64);
                worldRendererIn.addVertex(var8, p_174968_2_, var7 + 64);
            }
        }
    }
    
    private void func_174963_q() {
        final Tessellator var1 = Tessellator.getInstance();
        final WorldRenderer var2 = var1.getWorldRenderer();
        if (this.field_175013_s != null) {
            this.field_175013_s.func_177362_c();
        }
        if (this.starGLCallList >= 0) {
            GLAllocation.deleteDisplayLists(this.starGLCallList);
            this.starGLCallList = -1;
        }
        if (this.field_175005_X) {
            this.field_175013_s = new VertexBuffer(this.field_175014_r);
            this.func_180444_a(var2);
            var2.draw();
            var2.reset();
            this.field_175013_s.func_177360_a(var2.func_178966_f(), var2.func_178976_e());
        }
        else {
            this.starGLCallList = GLAllocation.generateDisplayLists(1);
            GlStateManager.pushMatrix();
            GL11.glNewList(this.starGLCallList, 4864);
            this.func_180444_a(var2);
            var1.draw();
            GL11.glEndList();
            GlStateManager.popMatrix();
        }
    }
    
    private void func_180444_a(final WorldRenderer worldRendererIn) {
        final Random var2 = new Random(10842L);
        worldRendererIn.startDrawingQuads();
        for (int var3 = 0; var3 < 1500; ++var3) {
            double var4 = var2.nextFloat() * 2.0f - 1.0f;
            double var5 = var2.nextFloat() * 2.0f - 1.0f;
            double var6 = var2.nextFloat() * 2.0f - 1.0f;
            final double var7 = 0.15f + var2.nextFloat() * 0.1f;
            double var8 = var4 * var4 + var5 * var5 + var6 * var6;
            if (var8 < 1.0 && var8 > 0.01) {
                var8 = 1.0 / Math.sqrt(var8);
                var4 *= var8;
                var5 *= var8;
                var6 *= var8;
                final double var9 = var4 * 100.0;
                final double var10 = var5 * 100.0;
                final double var11 = var6 * 100.0;
                final double var12 = Math.atan2(var4, var6);
                final double var13 = Math.sin(var12);
                final double var14 = Math.cos(var12);
                final double var15 = Math.atan2(Math.sqrt(var4 * var4 + var6 * var6), var5);
                final double var16 = Math.sin(var15);
                final double var17 = Math.cos(var15);
                final double var18 = var2.nextDouble() * 3.141592653589793 * 2.0;
                final double var19 = Math.sin(var18);
                final double var20 = Math.cos(var18);
                for (int var21 = 0; var21 < 4; ++var21) {
                    final double var22 = 0.0;
                    final double var23 = ((var21 & 0x2) - 1) * var7;
                    final double var24 = ((var21 + 1 & 0x2) - 1) * var7;
                    final double var25 = 0.0;
                    final double var26 = var23 * var20 - var24 * var19;
                    final double var27 = var24 * var20 + var23 * var19;
                    final double var28 = var26 * var16 + 0.0 * var17;
                    final double var29 = 0.0 * var16 - var26 * var17;
                    final double var30 = var29 * var13 - var27 * var14;
                    final double var31 = var27 * var13 + var29 * var14;
                    worldRendererIn.addVertex(var9 + var30, var10 + var28, var11 + var31);
                }
            }
        }
    }
    
    public void setWorldAndLoadRenderers(final WorldClient worldClientIn) {
        if (this.theWorld != null) {
            this.theWorld.removeWorldAccess(this);
        }
        this.field_174992_B = Double.MIN_VALUE;
        this.field_174993_C = Double.MIN_VALUE;
        this.field_174987_D = Double.MIN_VALUE;
        this.field_174988_E = Integer.MIN_VALUE;
        this.field_174989_F = Integer.MIN_VALUE;
        this.field_174990_G = Integer.MIN_VALUE;
        this.field_175010_j.set(worldClientIn);
        if ((this.theWorld = worldClientIn) != null) {
            worldClientIn.addWorldAccess(this);
            this.loadRenderers();
        }
    }
    
    public void loadRenderers() {
        if (this.theWorld != null) {
            this.displayListEntitiesDirty = true;
            Blocks.leaves.setGraphicsLevel(Config.isTreesFancy());
            Blocks.leaves2.setGraphicsLevel(Config.isTreesFancy());
            BlockModelRenderer.updateAoLightValue();
            this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
            final boolean var1 = this.field_175005_X;
            this.field_175005_X = OpenGlHelper.func_176075_f();
            if (var1 && !this.field_175005_X) {
                this.field_174996_N = new RenderList();
                this.field_175007_a = new ListChunkFactory();
            }
            else if (!var1 && this.field_175005_X) {
                this.field_174996_N = new VboRenderList();
                this.field_175007_a = new VboChunkFactory();
            }
            if (var1 != this.field_175005_X) {
                this.func_174963_q();
                this.func_174980_p();
                this.func_174964_o();
            }
            if (this.field_175008_n != null) {
                this.field_175008_n.func_178160_a();
            }
            this.func_174986_e();
            this.field_175008_n = new ViewFrustum(this.theWorld, this.mc.gameSettings.renderDistanceChunks, this, this.field_175007_a);
            if (this.theWorld != null) {
                final Entity var2 = this.mc.func_175606_aa();
                if (var2 != null) {
                    this.field_175008_n.func_178163_a(var2.posX, var2.posZ);
                }
            }
            this.renderEntitiesStartupCounter = 2;
        }
    }
    
    protected void func_174986_e() {
        this.field_175009_l.clear();
        this.field_174995_M.func_178514_b();
    }
    
    public void checkOcclusionQueryResult(final int p_72720_1_, final int p_72720_2_) {
        if (OpenGlHelper.shadersSupported && this.shaders != null) {
            this.shaders.createBindFramebuffers(p_72720_1_, p_72720_2_);
        }
    }
    
    public void func_180446_a(final Entity p_180446_1_, final ICamera p_180446_2_, final float partialTicks) {
        if (this.renderEntitiesStartupCounter > 0) {
            --this.renderEntitiesStartupCounter;
        }
        else {
            final double var4 = p_180446_1_.prevPosX + (p_180446_1_.posX - p_180446_1_.prevPosX) * partialTicks;
            final double var5 = p_180446_1_.prevPosY + (p_180446_1_.posY - p_180446_1_.prevPosY) * partialTicks;
            final double var6 = p_180446_1_.prevPosZ + (p_180446_1_.posZ - p_180446_1_.prevPosZ) * partialTicks;
            this.theWorld.theProfiler.startSection("prepare");
            TileEntityRendererDispatcher.instance.func_178470_a(this.theWorld, this.mc.getTextureManager(), this.mc.fontRendererObj, this.mc.func_175606_aa(), partialTicks);
            this.field_175010_j.func_180597_a(this.theWorld, this.mc.fontRendererObj, this.mc.func_175606_aa(), this.mc.pointedEntity, this.mc.gameSettings, partialTicks);
            this.countEntitiesTotal = 0;
            this.countEntitiesRendered = 0;
            this.countEntitiesHidden = 0;
            final Entity var7 = this.mc.func_175606_aa();
            final double var8 = var7.lastTickPosX + (var7.posX - var7.lastTickPosX) * partialTicks;
            final double var9 = var7.lastTickPosY + (var7.posY - var7.lastTickPosY) * partialTicks;
            final double var10 = var7.lastTickPosZ + (var7.posZ - var7.lastTickPosZ) * partialTicks;
            TileEntityRendererDispatcher.staticPlayerX = var8;
            TileEntityRendererDispatcher.staticPlayerY = var9;
            TileEntityRendererDispatcher.staticPlayerZ = var10;
            this.field_175010_j.func_178628_a(var8, var9, var10);
            this.mc.entityRenderer.func_180436_i();
            this.theWorld.theProfiler.endStartSection("global");
            final List var11 = this.theWorld.getLoadedEntityList();
            this.countEntitiesTotal = var11.size();
            if (Config.isFogOff() && this.mc.entityRenderer.fogStandard) {
                GlStateManager.disableFog();
            }
            for (int var12 = 0; var12 < this.theWorld.weatherEffects.size(); ++var12) {
                final Entity entity = this.theWorld.weatherEffects.get(var12);
                ++this.countEntitiesRendered;
                if (entity.isInRangeToRender3d(var4, var5, var6)) {
                    this.field_175010_j.renderEntitySimple(entity, partialTicks);
                }
            }
            if (this.func_174985_d()) {
                GlStateManager.depthFunc(519);
                GlStateManager.disableFog();
                this.buffer.framebufferClear();
                this.buffer.bindFramebuffer(false);
                this.theWorld.theProfiler.endStartSection("entityOutlines");
                RenderHelper.disableStandardItemLighting();
                this.field_175010_j.func_178632_c(true);
                for (int var12 = 0; var12 < var11.size(); ++var12) {
                    final Entity entity = var11.get(var12);
                    final boolean var13 = this.mc.func_175606_aa() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.func_175606_aa()).isPlayerSleeping();
                    final boolean var14 = entity.isInRangeToRender3d(var4, var5, var6) && (entity.ignoreFrustumCheck || p_180446_2_.isBoundingBoxInFrustum(entity.getEntityBoundingBox()) || entity.riddenByEntity == this.mc.thePlayer);
                    if ((entity != this.mc.func_175606_aa() || this.mc.gameSettings.thirdPersonView != 0 || var13) && var14) {
                        this.field_175010_j.renderEntitySimple(entity, partialTicks);
                    }
                }
                this.field_175010_j.func_178632_c(false);
                RenderHelper.enableStandardItemLighting();
                GlStateManager.depthMask(false);
                this.shaders.loadShaderGroup(partialTicks);
                GlStateManager.depthMask(true);
                this.mc.getFramebuffer().bindFramebuffer(false);
                GlStateManager.enableFog();
                GlStateManager.depthFunc(515);
                GlStateManager.enableDepth();
                GlStateManager.enableAlpha();
            }
            this.theWorld.theProfiler.endStartSection("entities");
            Iterator var15 = this.glRenderLists.iterator();
            final boolean oldFancyGraphics = this.mc.gameSettings.fancyGraphics;
            this.mc.gameSettings.fancyGraphics = Config.isDroppedItemsFancy();
            while (var15.hasNext()) {
                final ContainerLocalRenderInformation var16 = var15.next();
                final Chunk fontRenderer = this.theWorld.getChunkFromBlockCoords(var16.field_178036_a.func_178568_j());
                final ClassInheratanceMultiMap var17 = fontRenderer.getEntityLists()[var16.field_178036_a.func_178568_j().getY() / 16];
                Object var18 = Iterators.emptyIterator();
                if (var17.size() > 0) {
                    var18 = var17.iterator();
                }
                while (((Iterator)var18).hasNext()) {
                    final Entity var19 = ((Iterator)var18).next();
                    final boolean var20 = this.field_175010_j.func_178635_a(var19, p_180446_2_, var4, var5, var6) || var19.riddenByEntity == this.mc.thePlayer;
                    if (var20) {
                        final boolean teClass = this.mc.func_175606_aa() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.func_175606_aa()).isPlayerSleeping();
                        if (var19 == this.mc.func_175606_aa() && this.mc.gameSettings.thirdPersonView == 0 && !teClass) {
                            continue;
                        }
                        if (var19.posY >= 0.0 && var19.posY < 256.0 && !this.theWorld.isBlockLoaded(new BlockPos(var19))) {
                            continue;
                        }
                        ++this.countEntitiesRendered;
                        if (var19.getClass() == EntityItemFrame.class) {
                            var19.renderDistanceWeight = 0.06;
                        }
                        this.renderedEntity = var19;
                        this.field_175010_j.renderEntitySimple(var19, partialTicks);
                        this.renderedEntity = null;
                    }
                    if (!var20 && var19 instanceof EntityWitherSkull) {
                        this.mc.getRenderManager().func_178630_b(var19, partialTicks);
                    }
                }
            }
            this.mc.gameSettings.fancyGraphics = oldFancyGraphics;
            final FontRenderer var21 = TileEntityRendererDispatcher.instance.getFontRenderer();
            this.theWorld.theProfiler.endStartSection("blockentities");
            RenderHelper.enableStandardItemLighting();
            var15 = this.glRenderLists.iterator();
            while (var15.hasNext()) {
                final ContainerLocalRenderInformation var16 = var15.next();
                final List var22 = var16.field_178036_a.func_178571_g().func_178485_b();
                Object var23 = Iterators.emptyIterator();
                if (var22.size() > 0) {
                    var23 = var22.iterator();
                }
                while (((Iterator)var23).hasNext()) {
                    final TileEntity var24 = ((Iterator)var23).next();
                    final Class var25 = var24.getClass();
                    if (var25 == TileEntitySign.class && !Config.zoomMode) {
                        final EntityPlayerSP pl = this.mc.thePlayer;
                        final double distSq = var24.getDistanceSq(pl.posX, pl.posY, pl.posZ);
                        if (distSq > 256.0) {
                            var21.enabled = false;
                        }
                    }
                    TileEntityRendererDispatcher.instance.func_180546_a(var24, partialTicks, -1);
                    var21.enabled = true;
                }
            }
            this.func_180443_s();
            var15 = this.damagedBlocks.values().iterator();
            while (var15.hasNext()) {
                final DestroyBlockProgress var26 = var15.next();
                BlockPos var27 = var26.func_180246_b();
                TileEntity var24 = this.theWorld.getTileEntity(var27);
                if (var24 instanceof TileEntityChest) {
                    final TileEntityChest var28 = (TileEntityChest)var24;
                    if (var28.adjacentChestXNeg != null) {
                        var27 = var27.offset(EnumFacing.WEST);
                        var24 = this.theWorld.getTileEntity(var27);
                    }
                    else if (var28.adjacentChestZNeg != null) {
                        var27 = var27.offset(EnumFacing.NORTH);
                        var24 = this.theWorld.getTileEntity(var27);
                    }
                }
                final Block var29 = this.theWorld.getBlockState(var27).getBlock();
                if (var24 != null && (var29 instanceof BlockChest || var29 instanceof BlockEnderChest || var29 instanceof BlockSign || var29 instanceof BlockSkull)) {
                    TileEntityRendererDispatcher.instance.func_180546_a(var24, partialTicks, var26.getPartialBlockDamage());
                }
            }
            this.func_174969_t();
            this.mc.entityRenderer.func_175072_h();
            this.mc.mcProfiler.endSection();
        }
    }
    
    public String getDebugInfoRenders() {
        final int var1 = this.field_175008_n.field_178164_f.length;
        int var2 = 0;
        for (final ContainerLocalRenderInformation var4 : this.glRenderLists) {
            final CompiledChunk var5 = var4.field_178036_a.field_178590_b;
            if (var5 != CompiledChunk.field_178502_a && !var5.func_178489_a()) {
                ++var2;
            }
        }
        return String.format("C: %d/%d %sD: %d, %s", var2, var1, this.mc.field_175612_E ? "(s) " : "", this.renderDistanceChunks, this.field_174995_M.func_178504_a());
    }
    
    public String getDebugInfoEntities() {
        return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered) + ", " + Config.getVersion();
    }
    
    public void func_174970_a(final Entity viewEntity, final double partialTicks, ICamera camera, final int frameCount, final boolean playerSpectator) {
        if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks) {
            this.loadRenderers();
        }
        this.theWorld.theProfiler.startSection("camera");
        final double var7 = viewEntity.posX - this.field_174992_B;
        final double var8 = viewEntity.posY - this.field_174993_C;
        final double var9 = viewEntity.posZ - this.field_174987_D;
        if (this.field_174988_E != viewEntity.chunkCoordX || this.field_174989_F != viewEntity.chunkCoordY || this.field_174990_G != viewEntity.chunkCoordZ || var7 * var7 + var8 * var8 + var9 * var9 > 16.0) {
            this.field_174992_B = viewEntity.posX;
            this.field_174993_C = viewEntity.posY;
            this.field_174987_D = viewEntity.posZ;
            this.field_174988_E = viewEntity.chunkCoordX;
            this.field_174989_F = viewEntity.chunkCoordY;
            this.field_174990_G = viewEntity.chunkCoordZ;
            this.field_175008_n.func_178163_a(viewEntity.posX, viewEntity.posZ);
        }
        this.theWorld.theProfiler.endStartSection("renderlistcamera");
        final double var10 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
        final double var11 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
        final double var12 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
        this.field_174996_N.func_178004_a(var10, var11, var12);
        this.theWorld.theProfiler.endStartSection("cull");
        if (this.field_175001_U != null) {
            final Frustrum var13 = new Frustrum(this.field_175001_U);
            var13.setPosition(this.field_175003_W.x, this.field_175003_W.y, this.field_175003_W.z);
            camera = var13;
        }
        this.mc.mcProfiler.endStartSection("culling");
        final BlockPos var14 = new BlockPos(var10, var11 + viewEntity.getEyeHeight(), var12);
        final RenderChunk var15 = this.field_175008_n.func_178161_a(var14);
        final BlockPos var16 = new BlockPos(MathHelper.floor_double(var10) / 16 * 16, MathHelper.floor_double(var11) / 16 * 16, MathHelper.floor_double(var12) / 16 * 16);
        this.displayListEntitiesDirty = (this.displayListEntitiesDirty || !this.field_175009_l.isEmpty() || viewEntity.posX != this.field_174997_H || viewEntity.posY != this.field_174998_I || viewEntity.posZ != this.field_174999_J || viewEntity.rotationPitch != this.field_175000_K || viewEntity.rotationYaw != this.field_174994_L);
        this.field_174997_H = viewEntity.posX;
        this.field_174998_I = viewEntity.posY;
        this.field_174999_J = viewEntity.posZ;
        this.field_175000_K = viewEntity.rotationPitch;
        this.field_174994_L = viewEntity.rotationYaw;
        final boolean var17 = this.field_175001_U != null;
        Lagometer.timerVisibility.start();
        if (!var17 && this.displayListEntitiesDirty) {
            this.displayListEntitiesDirty = false;
            this.glRenderLists = Lists.newArrayList();
            final LinkedList var18 = Lists.newLinkedList();
            boolean var19 = this.mc.field_175612_E;
            if (var15 == null) {
                final int var20 = (var14.getY() > 0) ? 248 : 8;
                for (int var21 = -this.renderDistanceChunks; var21 <= this.renderDistanceChunks; ++var21) {
                    for (int var22 = -this.renderDistanceChunks; var22 <= this.renderDistanceChunks; ++var22) {
                        final RenderChunk var23 = this.field_175008_n.func_178161_a(new BlockPos((var21 << 4) + 8, var20, (var22 << 4) + 8));
                        if (var23 != null && camera.isBoundingBoxInFrustum(var23.field_178591_c)) {
                            var23.func_178577_a(frameCount);
                            var18.add(new ContainerLocalRenderInformation(var23, null, 0, null));
                        }
                    }
                }
            }
            else {
                boolean var24 = false;
                final ContainerLocalRenderInformation var25 = new ContainerLocalRenderInformation(var15, null, 0, null);
                final Set var26 = this.func_174978_c(var14);
                if (!var26.isEmpty() && var26.size() == 1) {
                    final Vector3f var27 = this.func_174962_a(viewEntity, partialTicks);
                    final EnumFacing var28 = EnumFacing.func_176737_a(var27.x, var27.y, var27.z).getOpposite();
                    var26.remove(var28);
                }
                if (var26.isEmpty()) {
                    var24 = true;
                }
                if (var24 && !playerSpectator) {
                    this.glRenderLists.add(var25);
                }
                else {
                    if (playerSpectator && this.theWorld.getBlockState(var14).getBlock().isOpaqueCube()) {
                        var19 = false;
                    }
                    var15.func_178577_a(frameCount);
                    var18.add(var25);
                }
            }
            while (!var18.isEmpty()) {
                final ContainerLocalRenderInformation var29 = var18.poll();
                final RenderChunk var30 = var29.field_178036_a;
                final EnumFacing var31 = var29.field_178034_b;
                final BlockPos var32 = var30.func_178568_j();
                this.glRenderLists.add(var29);
                for (final EnumFacing var36 : EnumFacing.VALUES) {
                    final RenderChunk var37 = this.getRenderChunkOffset(var14, var30, var36);
                    if ((!var19 || !var29.field_178035_c.contains(var36.getOpposite())) && (!var19 || var31 == null || var30.func_178571_g().func_178495_a(var31.getOpposite(), var36)) && var37 != null && var37.func_178577_a(frameCount) && camera.isBoundingBoxInFrustum(var37.field_178591_c)) {
                        final ContainerLocalRenderInformation var38 = new ContainerLocalRenderInformation(var37, var36, var29.field_178032_d + 1, null);
                        var38.field_178035_c.addAll(var29.field_178035_c);
                        var38.field_178035_c.add(var36);
                        var18.add(var38);
                    }
                }
            }
        }
        if (this.field_175002_T) {
            this.func_174984_a(var10, var11, var12);
            this.field_175002_T = false;
        }
        Lagometer.timerVisibility.end();
        this.field_174995_M.func_178513_e();
        final Set var39 = this.field_175009_l;
        this.field_175009_l = Sets.newLinkedHashSet();
        final Iterator var40 = this.glRenderLists.iterator();
        Lagometer.timerChunkUpdate.start();
        while (var40.hasNext()) {
            final ContainerLocalRenderInformation var29 = var40.next();
            final RenderChunk var30 = var29.field_178036_a;
            if (var30.func_178569_m() || var39.contains(var30)) {
                this.displayListEntitiesDirty = true;
                if (this.func_174983_a(var16, var29.field_178036_a)) {
                    if (!Config.isActing()) {
                        this.chunksToUpdateForced.add(var30);
                    }
                    else {
                        this.mc.mcProfiler.startSection("build near");
                        this.field_174995_M.func_178505_b(var30);
                        var30.func_178575_a(false);
                        this.mc.mcProfiler.endSection();
                    }
                }
                else {
                    this.field_175009_l.add(var30);
                }
            }
        }
        Lagometer.timerChunkUpdate.end();
        this.field_175009_l.addAll(var39);
        this.mc.mcProfiler.endSection();
    }
    
    private boolean func_174983_a(final BlockPos p_174983_1_, final RenderChunk p_174983_2_) {
        final BlockPos var3 = p_174983_2_.func_178568_j();
        return MathHelper.abs_int(p_174983_1_.getX() - var3.getX()) <= 16 && MathHelper.abs_int(p_174983_1_.getY() - var3.getY()) <= 16 && MathHelper.abs_int(p_174983_1_.getZ() - var3.getZ()) <= 16;
    }
    
    private Set func_174978_c(final BlockPos p_174978_1_) {
        final VisGraph var2 = new VisGraph();
        final BlockPos var3 = new BlockPos(p_174978_1_.getX() >> 4 << 4, p_174978_1_.getY() >> 4 << 4, p_174978_1_.getZ() >> 4 << 4);
        final Chunk var4 = this.theWorld.getChunkFromBlockCoords(var3);
        for (final BlockPos.MutableBlockPos var6 : BlockPos.getAllInBoxMutable(var3, var3.add(15, 15, 15))) {
            if (var4.getBlock(var6).isOpaqueCube()) {
                var2.func_178606_a(var6);
            }
        }
        return var2.func_178609_b(p_174978_1_);
    }
    
    private RenderChunk getRenderChunkOffset(final BlockPos p_174973_1_, final RenderChunk renderChunk, final EnumFacing p_174973_3_) {
        final BlockPos var4 = renderChunk.getPositionOffset16(p_174973_3_);
        return (MathHelper.abs_int(p_174973_1_.getX() - var4.getX()) > this.renderDistanceChunks * 16) ? null : ((var4.getY() >= 0 && var4.getY() < 256) ? ((MathHelper.abs_int(p_174973_1_.getZ() - var4.getZ()) > this.renderDistanceChunks * 16) ? null : this.field_175008_n.func_178161_a(var4)) : null);
    }
    
    private void func_174984_a(final double p_174984_1_, final double p_174984_3_, final double p_174984_5_) {
        this.field_175001_U = new ClippingHelperImpl();
        ((ClippingHelperImpl)this.field_175001_U).init();
        final Matrix4f var7 = new Matrix4f(this.field_175001_U.field_178626_c);
        var7.transpose();
        final Matrix4f var8 = new Matrix4f(this.field_175001_U.field_178625_b);
        var8.transpose();
        final Matrix4f var9 = new Matrix4f();
        var9.mul(var8, var7);
        var9.invert();
        this.field_175003_W.x = p_174984_1_;
        this.field_175003_W.y = p_174984_3_;
        this.field_175003_W.z = p_174984_5_;
        this.field_175004_V[0] = new Vector4f(-1.0f, -1.0f, -1.0f, 1.0f);
        this.field_175004_V[1] = new Vector4f(1.0f, -1.0f, -1.0f, 1.0f);
        this.field_175004_V[2] = new Vector4f(1.0f, 1.0f, -1.0f, 1.0f);
        this.field_175004_V[3] = new Vector4f(-1.0f, 1.0f, -1.0f, 1.0f);
        this.field_175004_V[4] = new Vector4f(-1.0f, -1.0f, 1.0f, 1.0f);
        this.field_175004_V[5] = new Vector4f(1.0f, -1.0f, 1.0f, 1.0f);
        this.field_175004_V[6] = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.field_175004_V[7] = new Vector4f(-1.0f, 1.0f, 1.0f, 1.0f);
        for (int var10 = 0; var10 < 8; ++var10) {
            var9.transform((Tuple4f)this.field_175004_V[var10]);
            final Vector4f vector4f = this.field_175004_V[var10];
            vector4f.x /= this.field_175004_V[var10].w;
            final Vector4f vector4f2 = this.field_175004_V[var10];
            vector4f2.y /= this.field_175004_V[var10].w;
            final Vector4f vector4f3 = this.field_175004_V[var10];
            vector4f3.z /= this.field_175004_V[var10].w;
            this.field_175004_V[var10].w = 1.0f;
        }
    }
    
    protected Vector3f func_174962_a(final Entity entityIn, final double partialTicks) {
        float var4 = (float)(entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks);
        final float var5 = (float)(entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks);
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
            var4 += 180.0f;
        }
        final float var6 = MathHelper.cos(-var5 * 0.017453292f - 3.1415927f);
        final float var7 = MathHelper.sin(-var5 * 0.017453292f - 3.1415927f);
        final float var8 = -MathHelper.cos(-var4 * 0.017453292f);
        final float var9 = MathHelper.sin(-var4 * 0.017453292f);
        return new Vector3f(var7 * var8, var9, var6 * var8);
    }
    
    public int func_174977_a(final EnumWorldBlockLayer blockLayerIn, final double partialTicks, final int pass, final Entity entityIn) {
        RenderHelper.disableStandardItemLighting();
        if (blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT) {
            this.mc.mcProfiler.startSection("translucent_sort");
            final double var15 = entityIn.posX - this.prevRenderSortX;
            final double var16 = entityIn.posY - this.prevRenderSortY;
            final double var17 = entityIn.posZ - this.prevRenderSortZ;
            if (var15 * var15 + var16 * var16 + var17 * var17 > 1.0) {
                this.prevRenderSortX = entityIn.posX;
                this.prevRenderSortY = entityIn.posY;
                this.prevRenderSortZ = entityIn.posZ;
                int var18 = 0;
                final Iterator var19 = this.glRenderLists.iterator();
                this.chunksToResortTransparency.clear();
                while (var19.hasNext()) {
                    final ContainerLocalRenderInformation var20 = var19.next();
                    if (var20.field_178036_a.field_178590_b.func_178492_d(blockLayerIn) && var18++ < 15) {
                        this.chunksToResortTransparency.add(var20.field_178036_a);
                    }
                }
            }
            this.mc.mcProfiler.endSection();
        }
        this.mc.mcProfiler.startSection("filterempty");
        int var21 = 0;
        final boolean var22 = blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT;
        final int var23 = var22 ? (this.glRenderLists.size() - 1) : 0;
        for (int var24 = var22 ? -1 : this.glRenderLists.size(), var25 = var22 ? -1 : 1, var26 = var23; var26 != var24; var26 += var25) {
            final RenderChunk var27 = this.glRenderLists.get(var26).field_178036_a;
            if (!var27.func_178571_g().func_178491_b(blockLayerIn)) {
                ++var21;
                this.field_174996_N.func_178002_a(var27, blockLayerIn);
            }
        }
        if (Config.isFogOff() && this.mc.entityRenderer.fogStandard) {
            GlStateManager.disableFog();
        }
        this.mc.mcProfiler.endStartSection("render_" + blockLayerIn);
        this.func_174982_a(blockLayerIn);
        this.mc.mcProfiler.endSection();
        return var21;
    }
    
    private void func_174982_a(final EnumWorldBlockLayer blockLayerIn) {
        this.mc.entityRenderer.func_180436_i();
        if (OpenGlHelper.func_176075_f()) {
            GL11.glEnableClientState(32884);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glEnableClientState(32888);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glEnableClientState(32888);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glEnableClientState(32886);
        }
        this.field_174996_N.func_178001_a(blockLayerIn);
        if (OpenGlHelper.func_176075_f()) {
            final List var2 = DefaultVertexFormats.field_176600_a.func_177343_g();
            for (final VertexFormatElement var4 : var2) {
                final VertexFormatElement.EnumUseage var5 = var4.func_177375_c();
                final int var6 = var4.func_177369_e();
                switch (SwitchEnumUseage.field_178037_a[var5.ordinal()]) {
                    case 1: {
                        GL11.glDisableClientState(32884);
                        continue;
                    }
                    case 2: {
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var6);
                        GL11.glDisableClientState(32888);
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                        continue;
                    }
                    case 3: {
                        GL11.glDisableClientState(32886);
                        GlStateManager.func_179117_G();
                        continue;
                    }
                }
            }
        }
        this.mc.entityRenderer.func_175072_h();
    }
    
    private void func_174965_a(final Iterator p_174965_1_) {
        while (p_174965_1_.hasNext()) {
            final DestroyBlockProgress var2 = p_174965_1_.next();
            final int var3 = var2.getCreationCloudUpdateTick();
            if (this.cloudTickCounter - var3 > 400) {
                p_174965_1_.remove();
            }
        }
    }
    
    public void updateClouds() {
        ++this.cloudTickCounter;
        if (this.cloudTickCounter % 20 == 0) {
            this.func_174965_a(this.damagedBlocks.values().iterator());
        }
    }
    
    private void func_180448_r() {
        if (Config.isSkyEnabled()) {
            GlStateManager.disableFog();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.depthMask(false);
            this.renderEngine.bindTexture(RenderGlobal.locationEndSkyPng);
            final Tessellator var1 = Tessellator.getInstance();
            final WorldRenderer var2 = var1.getWorldRenderer();
            for (int var3 = 0; var3 < 6; ++var3) {
                GlStateManager.pushMatrix();
                if (var3 == 1) {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (var3 == 2) {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (var3 == 3) {
                    GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
                }
                if (var3 == 4) {
                    GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                }
                if (var3 == 5) {
                    GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
                }
                var2.startDrawingQuads();
                var2.func_178991_c(2631720);
                var2.addVertexWithUV(-100.0, -100.0, -100.0, 0.0, 0.0);
                var2.addVertexWithUV(-100.0, -100.0, 100.0, 0.0, 16.0);
                var2.addVertexWithUV(100.0, -100.0, 100.0, 16.0, 16.0);
                var2.addVertexWithUV(100.0, -100.0, -100.0, 16.0, 0.0);
                var1.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.depthMask(true);
            GlStateManager.enableTextures();
            GlStateManager.enableAlpha();
        }
    }
    
    public void func_174976_a(final float partialTicks, final int pass) {
        if (Reflector.ForgeWorldProvider_getSkyRenderer.exists()) {
            final WorldProvider var3 = this.mc.theWorld.provider;
            final Object var4 = Reflector.call(var3, Reflector.ForgeWorldProvider_getSkyRenderer, new Object[0]);
            if (var4 != null) {
                Reflector.callVoid(var4, Reflector.IRenderHandler_render, partialTicks, this.theWorld, this.mc);
                return;
            }
        }
        if (this.mc.theWorld.provider.getDimensionId() == 1) {
            this.func_180448_r();
        }
        else if (this.mc.theWorld.provider.isSurfaceWorld()) {
            GlStateManager.disableTextures();
            Vec3 var5 = this.theWorld.getSkyColor(this.mc.func_175606_aa(), partialTicks);
            var5 = CustomColorizer.getSkyColor(var5, this.mc.theWorld, this.mc.func_175606_aa().posX, this.mc.func_175606_aa().posY + 1.0, this.mc.func_175606_aa().posZ);
            float var6 = (float)var5.xCoord;
            float var7 = (float)var5.yCoord;
            float var8 = (float)var5.zCoord;
            if (pass != 2) {
                final float var9 = (var6 * 30.0f + var7 * 59.0f + var8 * 11.0f) / 100.0f;
                final float var10 = (var6 * 30.0f + var7 * 70.0f) / 100.0f;
                final float var11 = (var6 * 30.0f + var8 * 70.0f) / 100.0f;
                var6 = var9;
                var7 = var10;
                var8 = var11;
            }
            GlStateManager.color(var6, var7, var8);
            final Tessellator var12 = Tessellator.getInstance();
            final WorldRenderer var13 = var12.getWorldRenderer();
            GlStateManager.depthMask(false);
            GlStateManager.enableFog();
            GlStateManager.color(var6, var7, var8);
            if (Config.isSkyEnabled()) {
                if (this.field_175005_X) {
                    this.field_175012_t.func_177359_a();
                    GL11.glEnableClientState(32884);
                    GL11.glVertexPointer(3, 5126, 12, 0L);
                    this.field_175012_t.func_177358_a(7);
                    this.field_175012_t.func_177361_b();
                    GL11.glDisableClientState(32884);
                }
                else {
                    GlStateManager.callList(this.glSkyList);
                }
            }
            GlStateManager.disableFog();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.disableStandardItemLighting();
            final float[] var14 = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(partialTicks), partialTicks);
            if (var14 != null && Config.isSunMoonEnabled()) {
                GlStateManager.disableTextures();
                GlStateManager.shadeModel(7425);
                GlStateManager.pushMatrix();
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate((MathHelper.sin(this.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0f) ? 180.0f : 0.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                float var15 = var14[0];
                float var16 = var14[1];
                float var17 = var14[2];
                if (pass != 2) {
                    final float var18 = (var15 * 30.0f + var16 * 59.0f + var17 * 11.0f) / 100.0f;
                    final float var19 = (var15 * 30.0f + var16 * 70.0f) / 100.0f;
                    final float var20 = (var15 * 30.0f + var17 * 70.0f) / 100.0f;
                    var15 = var18;
                    var16 = var19;
                    var17 = var20;
                }
                var13.startDrawing(6);
                var13.setColorRGBA(var15, var16, var17, var14[3]);
                var13.addVertex(0.0, 100.0, 0.0);
                final boolean var21 = true;
                var13.setColorRGBA(var14[0], var14[1], var14[2], 0.0f);
                for (int var22 = 0; var22 <= 16; ++var22) {
                    final float var20 = var22 * 3.1415927f * 2.0f / 16.0f;
                    final float var23 = MathHelper.sin(var20);
                    final float var24 = MathHelper.cos(var20);
                    var13.addVertex(var23 * 120.0f, var24 * 120.0f, -var24 * 40.0f * var14[3]);
                }
                var12.draw();
                GlStateManager.popMatrix();
                GlStateManager.shadeModel(7424);
            }
            GlStateManager.enableTextures();
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            GlStateManager.pushMatrix();
            float var15 = 1.0f - this.theWorld.getRainStrength(partialTicks);
            float var16 = 0.0f;
            float var17 = 0.0f;
            float var18 = 0.0f;
            GlStateManager.color(1.0f, 1.0f, 1.0f, var15);
            GlStateManager.translate(0.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
            CustomSky.renderSky(this.theWorld, this.renderEngine, this.theWorld.getCelestialAngle(partialTicks), var15);
            GlStateManager.rotate(this.theWorld.getCelestialAngle(partialTicks) * 360.0f, 1.0f, 0.0f, 0.0f);
            if (Config.isSunMoonEnabled()) {
                float var19 = 30.0f;
                this.renderEngine.bindTexture(RenderGlobal.locationSunPng);
                var13.startDrawingQuads();
                var13.addVertexWithUV(-var19, 100.0, -var19, 0.0, 0.0);
                var13.addVertexWithUV(var19, 100.0, -var19, 1.0, 0.0);
                var13.addVertexWithUV(var19, 100.0, var19, 1.0, 1.0);
                var13.addVertexWithUV(-var19, 100.0, var19, 0.0, 1.0);
                var12.draw();
                var19 = 20.0f;
                this.renderEngine.bindTexture(RenderGlobal.locationMoonPhasesPng);
                final int var25 = this.theWorld.getMoonPhase();
                final int var26 = var25 % 4;
                final int var22 = var25 / 4 % 2;
                final float var23 = (var26 + 0) / 4.0f;
                final float var24 = (var22 + 0) / 2.0f;
                final float var27 = (var26 + 1) / 4.0f;
                final float var28 = (var22 + 1) / 2.0f;
                var13.startDrawingQuads();
                var13.addVertexWithUV(-var19, -100.0, var19, var27, var28);
                var13.addVertexWithUV(var19, -100.0, var19, var23, var28);
                var13.addVertexWithUV(var19, -100.0, -var19, var23, var24);
                var13.addVertexWithUV(-var19, -100.0, -var19, var27, var24);
                var12.draw();
            }
            GlStateManager.disableTextures();
            final float var20 = this.theWorld.getStarBrightness(partialTicks) * var15;
            if (var20 > 0.0f && Config.isStarsEnabled() && !CustomSky.hasSkyLayers(this.theWorld)) {
                GlStateManager.color(var20, var20, var20, var20);
                if (this.field_175005_X) {
                    this.field_175013_s.func_177359_a();
                    GL11.glEnableClientState(32884);
                    GL11.glVertexPointer(3, 5126, 12, 0L);
                    this.field_175013_s.func_177358_a(7);
                    this.field_175013_s.func_177361_b();
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
            GlStateManager.popMatrix();
            GlStateManager.disableTextures();
            GlStateManager.color(0.0f, 0.0f, 0.0f);
            final double var29 = this.mc.thePlayer.func_174824_e(partialTicks).yCoord - this.theWorld.getHorizon();
            if (var29 < 0.0) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0f, 12.0f, 0.0f);
                if (this.field_175005_X) {
                    this.field_175011_u.func_177359_a();
                    GL11.glEnableClientState(32884);
                    GL11.glVertexPointer(3, 5126, 12, 0L);
                    this.field_175011_u.func_177358_a(7);
                    this.field_175011_u.func_177361_b();
                    GL11.glDisableClientState(32884);
                }
                else {
                    GlStateManager.callList(this.glSkyList2);
                }
                GlStateManager.popMatrix();
                var17 = 1.0f;
                var18 = -(float)(var29 + 65.0);
                final float var19 = -1.0f;
                var13.startDrawingQuads();
                var13.drawColor(0, 255);
                var13.addVertex(-1.0, var18, 1.0);
                var13.addVertex(1.0, var18, 1.0);
                var13.addVertex(1.0, -1.0, 1.0);
                var13.addVertex(-1.0, -1.0, 1.0);
                var13.addVertex(-1.0, -1.0, -1.0);
                var13.addVertex(1.0, -1.0, -1.0);
                var13.addVertex(1.0, var18, -1.0);
                var13.addVertex(-1.0, var18, -1.0);
                var13.addVertex(1.0, -1.0, -1.0);
                var13.addVertex(1.0, -1.0, 1.0);
                var13.addVertex(1.0, var18, 1.0);
                var13.addVertex(1.0, var18, -1.0);
                var13.addVertex(-1.0, var18, -1.0);
                var13.addVertex(-1.0, var18, 1.0);
                var13.addVertex(-1.0, -1.0, 1.0);
                var13.addVertex(-1.0, -1.0, -1.0);
                var13.addVertex(-1.0, -1.0, -1.0);
                var13.addVertex(-1.0, -1.0, 1.0);
                var13.addVertex(1.0, -1.0, 1.0);
                var13.addVertex(1.0, -1.0, -1.0);
                var12.draw();
            }
            if (this.theWorld.provider.isSkyColored()) {
                GlStateManager.color(var6 * 0.2f + 0.04f, var7 * 0.2f + 0.04f, var8 * 0.6f + 0.1f);
            }
            else {
                GlStateManager.color(var6, var7, var8);
            }
            if (this.mc.gameSettings.renderDistanceChunks <= 4) {
                GlStateManager.color(this.mc.entityRenderer.field_175080_Q, this.mc.entityRenderer.field_175082_R, this.mc.entityRenderer.field_175081_S);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, -(float)(var29 - 16.0), 0.0f);
            if (Config.isSkyEnabled()) {
                GlStateManager.callList(this.glSkyList2);
            }
            GlStateManager.popMatrix();
            GlStateManager.enableTextures();
            GlStateManager.depthMask(true);
        }
    }
    
    public void func_180447_b(float p_180447_1_, final int p_180447_2_) {
        if (!Config.isCloudsOff()) {
            if (Reflector.ForgeWorldProvider_getCloudRenderer.exists()) {
                final WorldProvider partialTicks = this.mc.theWorld.provider;
                final Object var3 = Reflector.call(partialTicks, Reflector.ForgeWorldProvider_getCloudRenderer, new Object[0]);
                if (var3 != null) {
                    Reflector.callVoid(var3, Reflector.IRenderHandler_render, p_180447_1_, this.theWorld, this.mc);
                    return;
                }
            }
            if (this.mc.theWorld.provider.isSurfaceWorld()) {
                if (Config.isCloudsFancy()) {
                    this.func_180445_c(p_180447_1_, p_180447_2_);
                }
                else {
                    final float partialTicks2 = p_180447_1_;
                    p_180447_1_ = 0.0f;
                    GlStateManager.disableCull();
                    final float var4 = (float)(this.mc.func_175606_aa().lastTickPosY + (this.mc.func_175606_aa().posY - this.mc.func_175606_aa().lastTickPosY) * p_180447_1_);
                    final boolean var5 = true;
                    final boolean var6 = true;
                    final Tessellator var7 = Tessellator.getInstance();
                    final WorldRenderer var8 = var7.getWorldRenderer();
                    this.renderEngine.bindTexture(RenderGlobal.locationCloudsPng);
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    if (this.isFancyGlListClouds || this.cloudTickCounter >= this.cloudTickCounterGlList + 20) {
                        GL11.glNewList(this.glListClouds, 4864);
                        final Vec3 entityliving = this.theWorld.getCloudColour(p_180447_1_);
                        float exactPlayerX = (float)entityliving.xCoord;
                        float var9 = (float)entityliving.yCoord;
                        float exactPlayerY = (float)entityliving.zCoord;
                        if (p_180447_2_ != 2) {
                            final float var10 = (exactPlayerX * 30.0f + var9 * 59.0f + exactPlayerY * 11.0f) / 100.0f;
                            final float exactPlayerZ = (exactPlayerX * 30.0f + var9 * 70.0f) / 100.0f;
                            final float var11 = (exactPlayerX * 30.0f + exactPlayerY * 70.0f) / 100.0f;
                            exactPlayerX = var10;
                            var9 = exactPlayerZ;
                            exactPlayerY = var11;
                        }
                        final float var10 = 4.8828125E-4f;
                        final double exactPlayerZ2 = this.cloudTickCounter + p_180447_1_;
                        double dc = this.mc.func_175606_aa().prevPosX + (this.mc.func_175606_aa().posX - this.mc.func_175606_aa().prevPosX) * p_180447_1_ + exactPlayerZ2 * 0.029999999329447746;
                        double cdx = this.mc.func_175606_aa().prevPosZ + (this.mc.func_175606_aa().posZ - this.mc.func_175606_aa().prevPosZ) * p_180447_1_;
                        final int cdz = MathHelper.floor_double(dc / 2048.0);
                        final int var12 = MathHelper.floor_double(cdx / 2048.0);
                        dc -= cdz * 2048;
                        cdx -= var12 * 2048;
                        float var13 = this.theWorld.provider.getCloudHeight() - var4 + 0.33f;
                        var13 += this.mc.gameSettings.ofCloudsHeight * 128.0f;
                        final float var14 = (float)(dc * 4.8828125E-4);
                        final float var15 = (float)(cdx * 4.8828125E-4);
                        var8.startDrawingQuads();
                        var8.setColorRGBA(exactPlayerX, var9, exactPlayerY, 0.8f);
                        for (int var16 = -256; var16 < 256; var16 += 32) {
                            for (int var17 = -256; var17 < 256; var17 += 32) {
                                var8.addVertexWithUV(var16 + 0, var13, var17 + 32, (var16 + 0) * 4.8828125E-4f + var14, (var17 + 32) * 4.8828125E-4f + var15);
                                var8.addVertexWithUV(var16 + 32, var13, var17 + 32, (var16 + 32) * 4.8828125E-4f + var14, (var17 + 32) * 4.8828125E-4f + var15);
                                var8.addVertexWithUV(var16 + 32, var13, var17 + 0, (var16 + 32) * 4.8828125E-4f + var14, (var17 + 0) * 4.8828125E-4f + var15);
                                var8.addVertexWithUV(var16 + 0, var13, var17 + 0, (var16 + 0) * 4.8828125E-4f + var14, (var17 + 0) * 4.8828125E-4f + var15);
                            }
                        }
                        var7.draw();
                        GL11.glEndList();
                        this.isFancyGlListClouds = false;
                        this.cloudTickCounterGlList = this.cloudTickCounter;
                        this.cloudPlayerX = this.mc.func_175606_aa().prevPosX;
                        this.cloudPlayerY = this.mc.func_175606_aa().prevPosY;
                        this.cloudPlayerZ = this.mc.func_175606_aa().prevPosZ;
                    }
                    final Entity entityliving2 = this.mc.func_175606_aa();
                    final double exactPlayerX2 = entityliving2.prevPosX + (entityliving2.posX - entityliving2.prevPosX) * partialTicks2;
                    final double exactPlayerY2 = entityliving2.prevPosY + (entityliving2.posY - entityliving2.prevPosY) * partialTicks2;
                    final double exactPlayerZ2 = entityliving2.prevPosZ + (entityliving2.posZ - entityliving2.prevPosZ) * partialTicks2;
                    double dc = this.cloudTickCounter - this.cloudTickCounterGlList + partialTicks2;
                    final float cdx2 = (float)(exactPlayerX2 - this.cloudPlayerX + dc * 0.03);
                    final float cdy = (float)(exactPlayerY2 - this.cloudPlayerY);
                    final float cdz2 = (float)(exactPlayerZ2 - this.cloudPlayerZ);
                    GlStateManager.translate(-cdx2, -cdy, -cdz2);
                    GlStateManager.callList(this.glListClouds);
                    GlStateManager.translate(cdx2, cdy, cdz2);
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.disableBlend();
                    GlStateManager.enableCull();
                }
            }
        }
    }
    
    public boolean hasCloudFog(final double p_72721_1_, final double p_72721_3_, final double p_72721_5_, final float p_72721_7_) {
        return false;
    }
    
    private void func_180445_c(float p_180445_1_, final int p_180445_2_) {
        final float partialTicks = p_180445_1_;
        p_180445_1_ = 0.0f;
        GlStateManager.disableCull();
        final float var3 = (float)(this.mc.func_175606_aa().lastTickPosY + (this.mc.func_175606_aa().posY - this.mc.func_175606_aa().lastTickPosY) * p_180445_1_);
        final Tessellator var4 = Tessellator.getInstance();
        final WorldRenderer var5 = var4.getWorldRenderer();
        final float var6 = 12.0f;
        final float var7 = 4.0f;
        final double var8 = this.cloudTickCounter + p_180445_1_;
        double var9 = (this.mc.func_175606_aa().prevPosX + (this.mc.func_175606_aa().posX - this.mc.func_175606_aa().prevPosX) * p_180445_1_ + var8 * 0.029999999329447746) / 12.0;
        double var10 = (this.mc.func_175606_aa().prevPosZ + (this.mc.func_175606_aa().posZ - this.mc.func_175606_aa().prevPosZ) * p_180445_1_) / 12.0 + 0.33000001311302185;
        float var11 = this.theWorld.provider.getCloudHeight() - var3 + 0.33f;
        var11 += this.mc.gameSettings.ofCloudsHeight * 128.0f;
        final int var12 = MathHelper.floor_double(var9 / 2048.0);
        final int var13 = MathHelper.floor_double(var10 / 2048.0);
        var9 -= var12 * 2048;
        var10 -= var13 * 2048;
        this.renderEngine.bindTexture(RenderGlobal.locationCloudsPng);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        final Vec3 var14 = this.theWorld.getCloudColour(p_180445_1_);
        float var15 = (float)var14.xCoord;
        float var16 = (float)var14.yCoord;
        float var17 = (float)var14.zCoord;
        if (p_180445_2_ != 2) {
            final float var18 = (var15 * 30.0f + var16 * 59.0f + var17 * 11.0f) / 100.0f;
            final float var19 = (var15 * 30.0f + var16 * 70.0f) / 100.0f;
            final float var20 = (var15 * 30.0f + var17 * 70.0f) / 100.0f;
            var15 = var18;
            var16 = var19;
            var17 = var20;
        }
        final float var18 = 0.00390625f;
        final float var19 = MathHelper.floor_double(var9) * 0.00390625f;
        final float var20 = MathHelper.floor_double(var10) * 0.00390625f;
        final float var21 = (float)(var9 - MathHelper.floor_double(var9));
        final float var22 = (float)(var10 - MathHelper.floor_double(var10));
        final boolean var23 = true;
        final boolean var24 = true;
        final float var25 = 9.765625E-4f;
        GlStateManager.scale(12.0f, 1.0f, 12.0f);
        for (int var26 = 0; var26 < 2; ++var26) {
            if (var26 == 0) {
                GL11.glColorMask(false, false, false, false);
            }
            else {
                switch (p_180445_2_) {
                    case 0: {
                        GL11.glColorMask(false, true, true, true);
                        break;
                    }
                    case 1: {
                        GL11.glColorMask(true, false, false, true);
                        break;
                    }
                    case 2: {
                        GL11.glColorMask(true, true, true, true);
                        break;
                    }
                }
            }
            if (!this.isFancyGlListClouds || this.cloudTickCounter >= this.cloudTickCounterGlList + 20) {
                GL11.glNewList(this.glListClouds, 4864);
                for (int entityliving = -3; entityliving <= 4; ++entityliving) {
                    for (int exactPlayerX = -3; exactPlayerX <= 4; ++exactPlayerX) {
                        var5.startDrawingQuads();
                        final float var27 = entityliving * 8;
                        final float exactPlayerY = exactPlayerX * 8;
                        final float var28 = var27 - var21;
                        final float exactPlayerZ = exactPlayerY - var22;
                        if (var11 > -5.0f) {
                            var5.setColorRGBA(var15 * 0.7f, var16 * 0.7f, var17 * 0.7f, 0.8f);
                            var5.func_178980_d(0.0f, -1.0f, 0.0f);
                            var5.addVertexWithUV(var28 + 0.0f, var11 + 0.0f, exactPlayerZ + 8.0f, (var27 + 0.0f) * 0.00390625f + var19, (exactPlayerY + 8.0f) * 0.00390625f + var20);
                            var5.addVertexWithUV(var28 + 8.0f, var11 + 0.0f, exactPlayerZ + 8.0f, (var27 + 8.0f) * 0.00390625f + var19, (exactPlayerY + 8.0f) * 0.00390625f + var20);
                            var5.addVertexWithUV(var28 + 8.0f, var11 + 0.0f, exactPlayerZ + 0.0f, (var27 + 8.0f) * 0.00390625f + var19, (exactPlayerY + 0.0f) * 0.00390625f + var20);
                            var5.addVertexWithUV(var28 + 0.0f, var11 + 0.0f, exactPlayerZ + 0.0f, (var27 + 0.0f) * 0.00390625f + var19, (exactPlayerY + 0.0f) * 0.00390625f + var20);
                        }
                        if (var11 <= 5.0f) {
                            var5.setColorRGBA(var15, var16, var17, 0.8f);
                            var5.func_178980_d(0.0f, 1.0f, 0.0f);
                            var5.addVertexWithUV(var28 + 0.0f, var11 + 4.0f - 9.765625E-4f, exactPlayerZ + 8.0f, (var27 + 0.0f) * 0.00390625f + var19, (exactPlayerY + 8.0f) * 0.00390625f + var20);
                            var5.addVertexWithUV(var28 + 8.0f, var11 + 4.0f - 9.765625E-4f, exactPlayerZ + 8.0f, (var27 + 8.0f) * 0.00390625f + var19, (exactPlayerY + 8.0f) * 0.00390625f + var20);
                            var5.addVertexWithUV(var28 + 8.0f, var11 + 4.0f - 9.765625E-4f, exactPlayerZ + 0.0f, (var27 + 8.0f) * 0.00390625f + var19, (exactPlayerY + 0.0f) * 0.00390625f + var20);
                            var5.addVertexWithUV(var28 + 0.0f, var11 + 4.0f - 9.765625E-4f, exactPlayerZ + 0.0f, (var27 + 0.0f) * 0.00390625f + var19, (exactPlayerY + 0.0f) * 0.00390625f + var20);
                        }
                        var5.setColorRGBA(var15 * 0.9f, var16 * 0.9f, var17 * 0.9f, 0.8f);
                        if (entityliving > -1) {
                            var5.func_178980_d(-1.0f, 0.0f, 0.0f);
                            for (int var29 = 0; var29 < 8; ++var29) {
                                var5.addVertexWithUV(var28 + var29 + 0.0f, var11 + 0.0f, exactPlayerZ + 8.0f, (var27 + var29 + 0.5f) * 0.00390625f + var19, (exactPlayerY + 8.0f) * 0.00390625f + var20);
                                var5.addVertexWithUV(var28 + var29 + 0.0f, var11 + 4.0f, exactPlayerZ + 8.0f, (var27 + var29 + 0.5f) * 0.00390625f + var19, (exactPlayerY + 8.0f) * 0.00390625f + var20);
                                var5.addVertexWithUV(var28 + var29 + 0.0f, var11 + 4.0f, exactPlayerZ + 0.0f, (var27 + var29 + 0.5f) * 0.00390625f + var19, (exactPlayerY + 0.0f) * 0.00390625f + var20);
                                var5.addVertexWithUV(var28 + var29 + 0.0f, var11 + 0.0f, exactPlayerZ + 0.0f, (var27 + var29 + 0.5f) * 0.00390625f + var19, (exactPlayerY + 0.0f) * 0.00390625f + var20);
                            }
                        }
                        if (entityliving <= 1) {
                            var5.func_178980_d(1.0f, 0.0f, 0.0f);
                            for (int var29 = 0; var29 < 8; ++var29) {
                                var5.addVertexWithUV(var28 + var29 + 1.0f - 9.765625E-4f, var11 + 0.0f, exactPlayerZ + 8.0f, (var27 + var29 + 0.5f) * 0.00390625f + var19, (exactPlayerY + 8.0f) * 0.00390625f + var20);
                                var5.addVertexWithUV(var28 + var29 + 1.0f - 9.765625E-4f, var11 + 4.0f, exactPlayerZ + 8.0f, (var27 + var29 + 0.5f) * 0.00390625f + var19, (exactPlayerY + 8.0f) * 0.00390625f + var20);
                                var5.addVertexWithUV(var28 + var29 + 1.0f - 9.765625E-4f, var11 + 4.0f, exactPlayerZ + 0.0f, (var27 + var29 + 0.5f) * 0.00390625f + var19, (exactPlayerY + 0.0f) * 0.00390625f + var20);
                                var5.addVertexWithUV(var28 + var29 + 1.0f - 9.765625E-4f, var11 + 0.0f, exactPlayerZ + 0.0f, (var27 + var29 + 0.5f) * 0.00390625f + var19, (exactPlayerY + 0.0f) * 0.00390625f + var20);
                            }
                        }
                        var5.setColorRGBA(var15 * 0.8f, var16 * 0.8f, var17 * 0.8f, 0.8f);
                        if (exactPlayerX > -1) {
                            var5.func_178980_d(0.0f, 0.0f, -1.0f);
                            for (int var29 = 0; var29 < 8; ++var29) {
                                var5.addVertexWithUV(var28 + 0.0f, var11 + 4.0f, exactPlayerZ + var29 + 0.0f, (var27 + 0.0f) * 0.00390625f + var19, (exactPlayerY + var29 + 0.5f) * 0.00390625f + var20);
                                var5.addVertexWithUV(var28 + 8.0f, var11 + 4.0f, exactPlayerZ + var29 + 0.0f, (var27 + 8.0f) * 0.00390625f + var19, (exactPlayerY + var29 + 0.5f) * 0.00390625f + var20);
                                var5.addVertexWithUV(var28 + 8.0f, var11 + 0.0f, exactPlayerZ + var29 + 0.0f, (var27 + 8.0f) * 0.00390625f + var19, (exactPlayerY + var29 + 0.5f) * 0.00390625f + var20);
                                var5.addVertexWithUV(var28 + 0.0f, var11 + 0.0f, exactPlayerZ + var29 + 0.0f, (var27 + 0.0f) * 0.00390625f + var19, (exactPlayerY + var29 + 0.5f) * 0.00390625f + var20);
                            }
                        }
                        if (exactPlayerX <= 1) {
                            var5.func_178980_d(0.0f, 0.0f, 1.0f);
                            for (int var29 = 0; var29 < 8; ++var29) {
                                var5.addVertexWithUV(var28 + 0.0f, var11 + 4.0f, exactPlayerZ + var29 + 1.0f - 9.765625E-4f, (var27 + 0.0f) * 0.00390625f + var19, (exactPlayerY + var29 + 0.5f) * 0.00390625f + var20);
                                var5.addVertexWithUV(var28 + 8.0f, var11 + 4.0f, exactPlayerZ + var29 + 1.0f - 9.765625E-4f, (var27 + 8.0f) * 0.00390625f + var19, (exactPlayerY + var29 + 0.5f) * 0.00390625f + var20);
                                var5.addVertexWithUV(var28 + 8.0f, var11 + 0.0f, exactPlayerZ + var29 + 1.0f - 9.765625E-4f, (var27 + 8.0f) * 0.00390625f + var19, (exactPlayerY + var29 + 0.5f) * 0.00390625f + var20);
                                var5.addVertexWithUV(var28 + 0.0f, var11 + 0.0f, exactPlayerZ + var29 + 1.0f - 9.765625E-4f, (var27 + 0.0f) * 0.00390625f + var19, (exactPlayerY + var29 + 0.5f) * 0.00390625f + var20);
                            }
                        }
                        var4.draw();
                    }
                }
                GL11.glEndList();
                this.isFancyGlListClouds = true;
                this.cloudTickCounterGlList = this.cloudTickCounter;
                this.cloudPlayerX = this.mc.func_175606_aa().prevPosX;
                this.cloudPlayerY = this.mc.func_175606_aa().prevPosY;
                this.cloudPlayerZ = this.mc.func_175606_aa().prevPosZ;
            }
            final Entity var30 = this.mc.func_175606_aa();
            final double var31 = var30.prevPosX + (var30.posX - var30.prevPosX) * partialTicks;
            final double var32 = var30.prevPosY + (var30.posY - var30.prevPosY) * partialTicks;
            final double var33 = var30.prevPosZ + (var30.posZ - var30.prevPosZ) * partialTicks;
            final double dc = this.cloudTickCounter - this.cloudTickCounterGlList + partialTicks;
            final float cdx = (float)(var31 - this.cloudPlayerX + dc * 0.03);
            final float cdy = (float)(var32 - this.cloudPlayerY);
            final float cdz = (float)(var33 - this.cloudPlayerZ);
            GlStateManager.translate(-cdx / 12.0f, -cdy, -cdz / 12.0f);
            GlStateManager.callList(this.glListClouds);
            GlStateManager.translate(cdx / 12.0f, cdy, cdz / 12.0f);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }
    
    public void func_174967_a(final long p_174967_1_) {
        this.displayListEntitiesDirty |= this.field_174995_M.func_178516_a(p_174967_1_);
        if (this.chunksToUpdateForced.size() > 0) {
            final Iterator countUpdated = this.chunksToUpdateForced.iterator();
            while (countUpdated.hasNext()) {
                final RenderChunk updatesPerFrame = countUpdated.next();
                if (!this.field_174995_M.func_178507_a(updatesPerFrame)) {
                    break;
                }
                updatesPerFrame.func_178575_a(false);
                countUpdated.remove();
                this.field_175009_l.remove(updatesPerFrame);
                this.chunksToResortTransparency.remove(updatesPerFrame);
            }
        }
        if (this.chunksToResortTransparency.size() > 0) {
            final Iterator countUpdated = this.chunksToResortTransparency.iterator();
            if (countUpdated.hasNext()) {
                final RenderChunk updatesPerFrame = countUpdated.next();
                if (this.field_174995_M.func_178509_c(updatesPerFrame)) {
                    countUpdated.remove();
                }
            }
        }
        int var8 = 0;
        int var9 = Config.getUpdatesPerFrame();
        final int maxUpdatesPerFrame = var9 * 2;
        final Iterator var10 = this.field_175009_l.iterator();
        while (var10.hasNext()) {
            final RenderChunk var11 = var10.next();
            if (!this.field_174995_M.func_178507_a(var11)) {
                break;
            }
            var11.func_178575_a(false);
            var10.remove();
            if (var11.func_178571_g().func_178489_a() && var9 < maxUpdatesPerFrame) {
                ++var9;
            }
            if (++var8 >= var9) {
                break;
            }
        }
    }
    
    public void func_180449_a(final Entity p_180449_1_, final float p_180449_2_) {
        final Tessellator var3 = Tessellator.getInstance();
        final WorldRenderer var4 = var3.getWorldRenderer();
        final WorldBorder var5 = this.theWorld.getWorldBorder();
        final double var6 = this.mc.gameSettings.renderDistanceChunks * 16;
        if (p_180449_1_.posX >= var5.maxX() - var6 || p_180449_1_.posX <= var5.minX() + var6 || p_180449_1_.posZ >= var5.maxZ() - var6 || p_180449_1_.posZ <= var5.minZ() + var6) {
            double var7 = 1.0 - var5.getClosestDistance(p_180449_1_) / var6;
            var7 = Math.pow(var7, 4.0);
            final double var8 = p_180449_1_.lastTickPosX + (p_180449_1_.posX - p_180449_1_.lastTickPosX) * p_180449_2_;
            final double var9 = p_180449_1_.lastTickPosY + (p_180449_1_.posY - p_180449_1_.lastTickPosY) * p_180449_2_;
            final double var10 = p_180449_1_.lastTickPosZ + (p_180449_1_.posZ - p_180449_1_.lastTickPosZ) * p_180449_2_;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            this.renderEngine.bindTexture(RenderGlobal.field_175006_g);
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            final int var11 = var5.getStatus().func_177766_a();
            final float var12 = (var11 >> 16 & 0xFF) / 255.0f;
            final float var13 = (var11 >> 8 & 0xFF) / 255.0f;
            final float var14 = (var11 & 0xFF) / 255.0f;
            GlStateManager.color(var12, var13, var14, (float)var7);
            GlStateManager.doPolygonOffset(-3.0f, -3.0f);
            GlStateManager.enablePolygonOffset();
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            final float var15 = Minecraft.getSystemTime() % 3000L / 3000.0f;
            final float var16 = 0.0f;
            final float var17 = 0.0f;
            final float var18 = 128.0f;
            var4.startDrawingQuads();
            var4.setTranslation(-var8, -var9, -var10);
            var4.markDirty();
            double var19 = Math.max(MathHelper.floor_double(var10 - var6), var5.minZ());
            double var20 = Math.min(MathHelper.ceiling_double_int(var10 + var6), var5.maxZ());
            if (var8 > var5.maxX() - var6) {
                float var21 = 0.0f;
                for (double var22 = var19; var22 < var20; ++var22, var21 += 0.5f) {
                    final double var23 = Math.min(1.0, var20 - var22);
                    final float var24 = (float)var23 * 0.5f;
                    var4.addVertexWithUV(var5.maxX(), 256.0, var22, var15 + var21, var15 + 0.0f);
                    var4.addVertexWithUV(var5.maxX(), 256.0, var22 + var23, var15 + var24 + var21, var15 + 0.0f);
                    var4.addVertexWithUV(var5.maxX(), 0.0, var22 + var23, var15 + var24 + var21, var15 + 128.0f);
                    var4.addVertexWithUV(var5.maxX(), 0.0, var22, var15 + var21, var15 + 128.0f);
                }
            }
            if (var8 < var5.minX() + var6) {
                float var21 = 0.0f;
                for (double var22 = var19; var22 < var20; ++var22, var21 += 0.5f) {
                    final double var23 = Math.min(1.0, var20 - var22);
                    final float var24 = (float)var23 * 0.5f;
                    var4.addVertexWithUV(var5.minX(), 256.0, var22, var15 + var21, var15 + 0.0f);
                    var4.addVertexWithUV(var5.minX(), 256.0, var22 + var23, var15 + var24 + var21, var15 + 0.0f);
                    var4.addVertexWithUV(var5.minX(), 0.0, var22 + var23, var15 + var24 + var21, var15 + 128.0f);
                    var4.addVertexWithUV(var5.minX(), 0.0, var22, var15 + var21, var15 + 128.0f);
                }
            }
            var19 = Math.max(MathHelper.floor_double(var8 - var6), var5.minX());
            var20 = Math.min(MathHelper.ceiling_double_int(var8 + var6), var5.maxX());
            if (var10 > var5.maxZ() - var6) {
                float var21 = 0.0f;
                for (double var22 = var19; var22 < var20; ++var22, var21 += 0.5f) {
                    final double var23 = Math.min(1.0, var20 - var22);
                    final float var24 = (float)var23 * 0.5f;
                    var4.addVertexWithUV(var22, 256.0, var5.maxZ(), var15 + var21, var15 + 0.0f);
                    var4.addVertexWithUV(var22 + var23, 256.0, var5.maxZ(), var15 + var24 + var21, var15 + 0.0f);
                    var4.addVertexWithUV(var22 + var23, 0.0, var5.maxZ(), var15 + var24 + var21, var15 + 128.0f);
                    var4.addVertexWithUV(var22, 0.0, var5.maxZ(), var15 + var21, var15 + 128.0f);
                }
            }
            if (var10 < var5.minZ() + var6) {
                float var21 = 0.0f;
                for (double var22 = var19; var22 < var20; ++var22, var21 += 0.5f) {
                    final double var23 = Math.min(1.0, var20 - var22);
                    final float var24 = (float)var23 * 0.5f;
                    var4.addVertexWithUV(var22, 256.0, var5.minZ(), var15 + var21, var15 + 0.0f);
                    var4.addVertexWithUV(var22 + var23, 256.0, var5.minZ(), var15 + var24 + var21, var15 + 0.0f);
                    var4.addVertexWithUV(var22 + var23, 0.0, var5.minZ(), var15 + var24 + var21, var15 + 128.0f);
                    var4.addVertexWithUV(var22, 0.0, var5.minZ(), var15 + var21, var15 + 128.0f);
                }
            }
            var3.draw();
            var4.setTranslation(0.0, 0.0, 0.0);
            GlStateManager.enableCull();
            GlStateManager.disableAlpha();
            GlStateManager.doPolygonOffset(0.0f, 0.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
        }
    }
    
    private void func_180443_s() {
        GlStateManager.tryBlendFuncSeparate(774, 768, 1, 0);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
        GlStateManager.doPolygonOffset(-3.0f, -3.0f);
        GlStateManager.enablePolygonOffset();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
    }
    
    private void func_174969_t() {
        GlStateManager.disableAlpha();
        GlStateManager.doPolygonOffset(0.0f, 0.0f);
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }
    
    public void func_174981_a(final Tessellator p_174981_1_, final WorldRenderer p_174981_2_, final Entity p_174981_3_, final float p_174981_4_) {
        final double var5 = p_174981_3_.lastTickPosX + (p_174981_3_.posX - p_174981_3_.lastTickPosX) * p_174981_4_;
        final double var6 = p_174981_3_.lastTickPosY + (p_174981_3_.posY - p_174981_3_.lastTickPosY) * p_174981_4_;
        final double var7 = p_174981_3_.lastTickPosZ + (p_174981_3_.posZ - p_174981_3_.lastTickPosZ) * p_174981_4_;
        if (!this.damagedBlocks.isEmpty()) {
            this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            this.func_180443_s();
            p_174981_2_.startDrawingQuads();
            p_174981_2_.setVertexFormat(DefaultVertexFormats.field_176600_a);
            p_174981_2_.setTranslation(-var5, -var6, -var7);
            p_174981_2_.markDirty();
            final Iterator var8 = this.damagedBlocks.values().iterator();
            while (var8.hasNext()) {
                final DestroyBlockProgress var9 = var8.next();
                final BlockPos var10 = var9.func_180246_b();
                final double var11 = var10.getX() - var5;
                final double var12 = var10.getY() - var6;
                final double var13 = var10.getZ() - var7;
                final Block var14 = this.theWorld.getBlockState(var10).getBlock();
                if (!(var14 instanceof BlockChest) && !(var14 instanceof BlockEnderChest) && !(var14 instanceof BlockSign) && !(var14 instanceof BlockSkull)) {
                    if (var11 * var11 + var12 * var12 + var13 * var13 > 1024.0) {
                        var8.remove();
                    }
                    else {
                        final IBlockState var15 = this.theWorld.getBlockState(var10);
                        if (var15.getBlock().getMaterial() == Material.air) {
                            continue;
                        }
                        final int var16 = var9.getPartialBlockDamage();
                        final TextureAtlasSprite var17 = this.destroyBlockIcons[var16];
                        final BlockRendererDispatcher var18 = this.mc.getBlockRendererDispatcher();
                        var18.func_175020_a(var15, var10, var17, this.theWorld);
                    }
                }
            }
            p_174981_1_.draw();
            p_174981_2_.setTranslation(0.0, 0.0, 0.0);
            this.func_174969_t();
        }
    }
    
    public void drawSelectionBox(final EntityPlayer player, final MovingObjectPosition mouseOver, final int i, final float partialTick) {
        if (i == 0 && mouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.4f);
            GL11.glLineWidth(2.0f);
            GlStateManager.disableTextures();
            GlStateManager.depthMask(false);
            final float var5 = 0.002f;
            final BlockPos var6 = mouseOver.getBlockPos();
            final Block var7 = this.theWorld.getBlockState(var6).getBlock();
            if (var7.getMaterial() != Material.air && this.theWorld.getWorldBorder().contains(var6)) {
                var7.setBlockBoundsBasedOnState(this.theWorld, var6);
                final double var8 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTick;
                final double var9 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTick;
                final double var10 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTick;
                drawOutlinedBoundingBox(var7.getSelectedBoundingBox(this.theWorld, var6).expand(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026).offset(-var8, -var9, -var10), -1);
            }
            GlStateManager.depthMask(true);
            GlStateManager.enableTextures();
            GlStateManager.disableBlend();
        }
    }
    
    public static void drawOutlinedBoundingBox(final AxisAlignedBB box, final int color) {
        final Tessellator var2 = Tessellator.getInstance();
        final WorldRenderer var3 = var2.getWorldRenderer();
        var3.startDrawing(3);
        if (color != -1) {
            var3.func_178991_c(color);
        }
        var3.addVertex(box.minX, box.minY, box.minZ);
        var3.addVertex(box.maxX, box.minY, box.minZ);
        var3.addVertex(box.maxX, box.minY, box.maxZ);
        var3.addVertex(box.minX, box.minY, box.maxZ);
        var3.addVertex(box.minX, box.minY, box.minZ);
        var2.draw();
        var3.startDrawing(3);
        if (color != -1) {
            var3.func_178991_c(color);
        }
        var3.addVertex(box.minX, box.maxY, box.minZ);
        var3.addVertex(box.maxX, box.maxY, box.minZ);
        var3.addVertex(box.maxX, box.maxY, box.maxZ);
        var3.addVertex(box.minX, box.maxY, box.maxZ);
        var3.addVertex(box.minX, box.maxY, box.minZ);
        var2.draw();
        var3.startDrawing(1);
        if (color != -1) {
            var3.func_178991_c(color);
        }
        var3.addVertex(box.minX, box.minY, box.minZ);
        var3.addVertex(box.minX, box.maxY, box.minZ);
        var3.addVertex(box.maxX, box.minY, box.minZ);
        var3.addVertex(box.maxX, box.maxY, box.minZ);
        var3.addVertex(box.maxX, box.minY, box.maxZ);
        var3.addVertex(box.maxX, box.maxY, box.maxZ);
        var3.addVertex(box.minX, box.minY, box.maxZ);
        var3.addVertex(box.minX, box.maxY, box.maxZ);
        var2.draw();
    }
    
    private void markBlocksForUpdate(final int p_72725_1_, final int p_72725_2_, final int p_72725_3_, final int p_72725_4_, final int p_72725_5_, final int p_72725_6_) {
        this.field_175008_n.func_178162_a(p_72725_1_, p_72725_2_, p_72725_3_, p_72725_4_, p_72725_5_, p_72725_6_);
    }
    
    @Override
    public void markBlockForUpdate(final BlockPos pos) {
        final int var2 = pos.getX();
        final int var3 = pos.getY();
        final int var4 = pos.getZ();
        this.markBlocksForUpdate(var2 - 1, var3 - 1, var4 - 1, var2 + 1, var3 + 1, var4 + 1);
    }
    
    @Override
    public void notifyLightSet(final BlockPos pos) {
        final int var2 = pos.getX();
        final int var3 = pos.getY();
        final int var4 = pos.getZ();
        this.markBlocksForUpdate(var2 - 1, var3 - 1, var4 - 1, var2 + 1, var3 + 1, var4 + 1);
    }
    
    @Override
    public void markBlockRangeForRenderUpdate(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        this.markBlocksForUpdate(x1 - 1, y1 - 1, z1 - 1, x2 + 1, y2 + 1, z2 + 1);
    }
    
    @Override
    public void func_174961_a(final String p_174961_1_, final BlockPos p_174961_2_) {
        final ISound var3 = this.mapSoundPositions.get(p_174961_2_);
        if (var3 != null) {
            this.mc.getSoundHandler().stopSound(var3);
            this.mapSoundPositions.remove(p_174961_2_);
        }
        if (p_174961_1_ != null) {
            final ItemRecord var4 = ItemRecord.getRecord(p_174961_1_);
            if (var4 != null) {
                this.mc.ingameGUI.setRecordPlayingMessage(var4.getRecordNameLocal());
            }
            ResourceLocation resource = null;
            if (Reflector.ForgeItemRecord_getRecordResource.exists() && var4 != null) {
                resource = (ResourceLocation)Reflector.call(var4, Reflector.ForgeItemRecord_getRecordResource, p_174961_1_);
            }
            if (resource == null) {
                resource = new ResourceLocation(p_174961_1_);
            }
            final PositionedSoundRecord var5 = PositionedSoundRecord.createRecordSoundAtPosition(resource, p_174961_2_.getX(), p_174961_2_.getY(), p_174961_2_.getZ());
            this.mapSoundPositions.put(p_174961_2_, var5);
            this.mc.getSoundHandler().playSound(var5);
        }
    }
    
    @Override
    public void playSound(final String soundName, final double x, final double y, final double z, final float volume, final float pitch) {
    }
    
    @Override
    public void playSoundToNearExcept(final EntityPlayer except, final String soundName, final double x, final double y, final double z, final float volume, final float pitch) {
    }
    
    @Override
    public void func_180442_a(final int p_180442_1_, final boolean p_180442_2_, final double p_180442_3_, final double p_180442_5_, final double p_180442_7_, final double p_180442_9_, final double p_180442_11_, final double p_180442_13_, final int... p_180442_15_) {
        try {
            this.func_174974_b(p_180442_1_, p_180442_2_, p_180442_3_, p_180442_5_, p_180442_7_, p_180442_9_, p_180442_11_, p_180442_13_, p_180442_15_);
        }
        catch (Throwable var18) {
            final CrashReport var17 = CrashReport.makeCrashReport(var18, "Exception while adding particle");
            final CrashReportCategory var19 = var17.makeCategory("Particle being added");
            var19.addCrashSection("ID", p_180442_1_);
            if (p_180442_15_ != null) {
                var19.addCrashSection("Parameters", p_180442_15_);
            }
            var19.addCrashSectionCallable("Position", new Callable() {
                private static final String __OBFID = "CL_00000955";
                
                @Override
                public String call() {
                    return CrashReportCategory.getCoordinateInfo(p_180442_3_, p_180442_5_, p_180442_7_);
                }
            });
            throw new ReportedException(var17);
        }
    }
    
    private void func_174972_a(final EnumParticleTypes p_174972_1_, final double p_174972_2_, final double p_174972_4_, final double p_174972_6_, final double p_174972_8_, final double p_174972_10_, final double p_174972_12_, final int... p_174972_14_) {
        this.func_180442_a(p_174972_1_.func_179348_c(), p_174972_1_.func_179344_e(), p_174972_2_, p_174972_4_, p_174972_6_, p_174972_8_, p_174972_10_, p_174972_12_, p_174972_14_);
    }
    
    private EntityFX func_174974_b(final int p_174974_1_, final boolean p_174974_2_, final double p_174974_3_, final double p_174974_5_, final double p_174974_7_, final double p_174974_9_, final double p_174974_11_, final double p_174974_13_, final int... p_174974_15_) {
        if (this.mc == null || this.mc.func_175606_aa() == null || this.mc.effectRenderer == null) {
            return null;
        }
        int var16 = this.mc.gameSettings.particleSetting;
        if (var16 == 1 && this.theWorld.rand.nextInt(3) == 0) {
            var16 = 2;
        }
        final double var17 = this.mc.func_175606_aa().posX - p_174974_3_;
        final double var18 = this.mc.func_175606_aa().posY - p_174974_5_;
        final double var19 = this.mc.func_175606_aa().posZ - p_174974_7_;
        if (p_174974_1_ == EnumParticleTypes.EXPLOSION_HUGE.func_179348_c() && !Config.isAnimatedExplosion()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.EXPLOSION_LARGE.func_179348_c() && !Config.isAnimatedExplosion()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.EXPLOSION_NORMAL.func_179348_c() && !Config.isAnimatedExplosion()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SUSPENDED.func_179348_c() && !Config.isWaterParticles()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SUSPENDED_DEPTH.func_179348_c() && !Config.isVoidParticles()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SMOKE_NORMAL.func_179348_c() && !Config.isAnimatedSmoke()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SMOKE_LARGE.func_179348_c() && !Config.isAnimatedSmoke()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SPELL_MOB.func_179348_c() && !Config.isPotionParticles()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SPELL_MOB_AMBIENT.func_179348_c() && !Config.isPotionParticles()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SPELL.func_179348_c() && !Config.isPotionParticles()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SPELL_INSTANT.func_179348_c() && !Config.isPotionParticles()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SPELL_WITCH.func_179348_c() && !Config.isPotionParticles()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.PORTAL.func_179348_c() && !Config.isAnimatedPortal()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.FLAME.func_179348_c() && !Config.isAnimatedFlame()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.REDSTONE.func_179348_c() && !Config.isAnimatedRedstone()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.DRIP_WATER.func_179348_c() && !Config.isDrippingWaterLava()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.DRIP_LAVA.func_179348_c() && !Config.isDrippingWaterLava()) {
            return null;
        }
        if (p_174974_2_) {
            return this.mc.effectRenderer.func_178927_a(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
        }
        final double var20 = 16.0;
        double maxDistSq = 256.0;
        if (p_174974_1_ == EnumParticleTypes.CRIT.func_179348_c()) {
            maxDistSq = 38416.0;
        }
        if (var17 * var17 + var18 * var18 + var19 * var19 > maxDistSq) {
            return null;
        }
        if (var16 > 1) {
            return null;
        }
        final EntityFX entityFx = this.mc.effectRenderer.func_178927_a(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
        if (p_174974_1_ == EnumParticleTypes.WATER_BUBBLE.func_179348_c()) {
            CustomColorizer.updateWaterFX(entityFx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
        }
        if (p_174974_1_ == EnumParticleTypes.WATER_SPLASH.func_179348_c()) {
            CustomColorizer.updateWaterFX(entityFx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
        }
        if (p_174974_1_ == EnumParticleTypes.WATER_DROP.func_179348_c()) {
            CustomColorizer.updateWaterFX(entityFx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
        }
        if (p_174974_1_ == EnumParticleTypes.TOWN_AURA.func_179348_c()) {
            CustomColorizer.updateMyceliumFX(entityFx);
        }
        if (p_174974_1_ == EnumParticleTypes.PORTAL.func_179348_c()) {
            CustomColorizer.updatePortalFX(entityFx);
        }
        if (p_174974_1_ == EnumParticleTypes.REDSTONE.func_179348_c()) {
            CustomColorizer.updateReddustFX(entityFx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
        }
        return entityFx;
    }
    
    @Override
    public void onEntityAdded(final Entity entityIn) {
        RandomMobs.entityLoaded(entityIn);
    }
    
    @Override
    public void onEntityRemoved(final Entity entityIn) {
    }
    
    public void deleteAllDisplayLists() {
    }
    
    @Override
    public void func_180440_a(final int p_180440_1_, final BlockPos p_180440_2_, final int p_180440_3_) {
        switch (p_180440_1_) {
            case 1013:
            case 1018: {
                if (this.mc.func_175606_aa() == null) {
                    break;
                }
                final double var4 = p_180440_2_.getX() - this.mc.func_175606_aa().posX;
                final double var5 = p_180440_2_.getY() - this.mc.func_175606_aa().posY;
                final double var6 = p_180440_2_.getZ() - this.mc.func_175606_aa().posZ;
                final double var7 = Math.sqrt(var4 * var4 + var5 * var5 + var6 * var6);
                double var8 = this.mc.func_175606_aa().posX;
                double var9 = this.mc.func_175606_aa().posY;
                double var10 = this.mc.func_175606_aa().posZ;
                if (var7 > 0.0) {
                    var8 += var4 / var7 * 2.0;
                    var9 += var5 / var7 * 2.0;
                    var10 += var6 / var7 * 2.0;
                }
                if (p_180440_1_ == 1013) {
                    this.theWorld.playSound(var8, var9, var10, "mob.wither.spawn", 1.0f, 1.0f, false);
                    break;
                }
                this.theWorld.playSound(var8, var9, var10, "mob.enderdragon.end", 5.0f, 1.0f, false);
                break;
            }
        }
    }
    
    @Override
    public void func_180439_a(final EntityPlayer p_180439_1_, final int p_180439_2_, final BlockPos p_180439_3_, final int p_180439_4_) {
        final Random var5 = this.theWorld.rand;
        switch (p_180439_2_) {
            case 1000: {
                this.theWorld.func_175731_a(p_180439_3_, "random.click", 1.0f, 1.0f, false);
                break;
            }
            case 1001: {
                this.theWorld.func_175731_a(p_180439_3_, "random.click", 1.0f, 1.2f, false);
                break;
            }
            case 1002: {
                this.theWorld.func_175731_a(p_180439_3_, "random.bow", 1.0f, 1.2f, false);
                break;
            }
            case 1003: {
                this.theWorld.func_175731_a(p_180439_3_, "random.door_open", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1004: {
                this.theWorld.func_175731_a(p_180439_3_, "random.fizz", 0.5f, 2.6f + (var5.nextFloat() - var5.nextFloat()) * 0.8f, false);
                break;
            }
            case 1005: {
                if (Item.getItemById(p_180439_4_) instanceof ItemRecord) {
                    this.theWorld.func_175717_a(p_180439_3_, "records." + ((ItemRecord)Item.getItemById(p_180439_4_)).recordName);
                    break;
                }
                this.theWorld.func_175717_a(p_180439_3_, null);
                break;
            }
            case 1006: {
                this.theWorld.func_175731_a(p_180439_3_, "random.door_close", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1007: {
                this.theWorld.func_175731_a(p_180439_3_, "mob.ghast.charge", 10.0f, (var5.nextFloat() - var5.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1008: {
                this.theWorld.func_175731_a(p_180439_3_, "mob.ghast.fireball", 10.0f, (var5.nextFloat() - var5.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1009: {
                this.theWorld.func_175731_a(p_180439_3_, "mob.ghast.fireball", 2.0f, (var5.nextFloat() - var5.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1010: {
                this.theWorld.func_175731_a(p_180439_3_, "mob.zombie.wood", 2.0f, (var5.nextFloat() - var5.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1011: {
                this.theWorld.func_175731_a(p_180439_3_, "mob.zombie.metal", 2.0f, (var5.nextFloat() - var5.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1012: {
                this.theWorld.func_175731_a(p_180439_3_, "mob.zombie.woodbreak", 2.0f, (var5.nextFloat() - var5.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1014: {
                this.theWorld.func_175731_a(p_180439_3_, "mob.wither.shoot", 2.0f, (var5.nextFloat() - var5.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1015: {
                this.theWorld.func_175731_a(p_180439_3_, "mob.bat.takeoff", 0.05f, (var5.nextFloat() - var5.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1016: {
                this.theWorld.func_175731_a(p_180439_3_, "mob.zombie.infect", 2.0f, (var5.nextFloat() - var5.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1017: {
                this.theWorld.func_175731_a(p_180439_3_, "mob.zombie.unfect", 2.0f, (var5.nextFloat() - var5.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1020: {
                this.theWorld.func_175731_a(p_180439_3_, "random.anvil_break", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1021: {
                this.theWorld.func_175731_a(p_180439_3_, "random.anvil_use", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1022: {
                this.theWorld.func_175731_a(p_180439_3_, "random.anvil_land", 0.3f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 2000: {
                final int var6 = p_180439_4_ % 3 - 1;
                final int var7 = p_180439_4_ / 3 % 3 - 1;
                final double var8 = p_180439_3_.getX() + var6 * 0.6 + 0.5;
                final double var9 = p_180439_3_.getY() + 0.5;
                final double var10 = p_180439_3_.getZ() + var7 * 0.6 + 0.5;
                for (int var11 = 0; var11 < 10; ++var11) {
                    final double var12 = var5.nextDouble() * 0.2 + 0.01;
                    final double var13 = var8 + var6 * 0.01 + (var5.nextDouble() - 0.5) * var7 * 0.5;
                    final double var14 = var9 + (var5.nextDouble() - 0.5) * 0.5;
                    final double var15 = var10 + var7 * 0.01 + (var5.nextDouble() - 0.5) * var6 * 0.5;
                    final double var16 = var6 * var12 + var5.nextGaussian() * 0.01;
                    final double var17 = -0.03 + var5.nextGaussian() * 0.01;
                    final double var18 = var7 * var12 + var5.nextGaussian() * 0.01;
                    this.func_174972_a(EnumParticleTypes.SMOKE_NORMAL, var13, var14, var15, var16, var17, var18, new int[0]);
                }
            }
            case 2001: {
                final Block var19 = Block.getBlockById(p_180439_4_ & 0xFFF);
                if (var19.getMaterial() != Material.air) {
                    this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(var19.stepSound.getBreakSound()), (var19.stepSound.getVolume() + 1.0f) / 2.0f, var19.stepSound.getFrequency() * 0.8f, p_180439_3_.getX() + 0.5f, p_180439_3_.getY() + 0.5f, p_180439_3_.getZ() + 0.5f));
                }
                this.mc.effectRenderer.func_180533_a(p_180439_3_, var19.getStateFromMeta(p_180439_4_ >> 12 & 0xFF));
                break;
            }
            case 2002: {
                final double var20 = p_180439_3_.getX();
                final double var8 = p_180439_3_.getY();
                final double var9 = p_180439_3_.getZ();
                for (int var21 = 0; var21 < 8; ++var21) {
                    this.func_174972_a(EnumParticleTypes.ITEM_CRACK, var20, var8, var9, var5.nextGaussian() * 0.15, var5.nextDouble() * 0.2, var5.nextGaussian() * 0.15, Item.getIdFromItem(Items.potionitem), p_180439_4_);
                }
                int var21 = Items.potionitem.getColorFromDamage(p_180439_4_);
                final float var22 = (var21 >> 16 & 0xFF) / 255.0f;
                final float var23 = (var21 >> 8 & 0xFF) / 255.0f;
                final float var24 = (var21 >> 0 & 0xFF) / 255.0f;
                EnumParticleTypes var25 = EnumParticleTypes.SPELL;
                if (Items.potionitem.isEffectInstant(p_180439_4_)) {
                    var25 = EnumParticleTypes.SPELL_INSTANT;
                }
                for (int var26 = 0; var26 < 100; ++var26) {
                    final double var27 = var5.nextDouble() * 4.0;
                    final double var28 = var5.nextDouble() * 3.141592653589793 * 2.0;
                    final double var29 = Math.cos(var28) * var27;
                    final double var14 = 0.01 + var5.nextDouble() * 0.5;
                    final double var15 = Math.sin(var28) * var27;
                    final EntityFX var30 = this.func_174974_b(var25.func_179348_c(), var25.func_179344_e(), var20 + var29 * 0.1, var8 + 0.3, var9 + var15 * 0.1, var29, var14, var15, new int[0]);
                    if (var30 != null) {
                        final float var31 = 0.75f + var5.nextFloat() * 0.25f;
                        var30.setRBGColorF(var22 * var31, var23 * var31, var24 * var31);
                        var30.multiplyVelocity((float)var27);
                    }
                }
                this.theWorld.func_175731_a(p_180439_3_, "game.potion.smash", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 2003: {
                final double var20 = p_180439_3_.getX() + 0.5;
                final double var8 = p_180439_3_.getY();
                final double var9 = p_180439_3_.getZ() + 0.5;
                for (int var21 = 0; var21 < 8; ++var21) {
                    this.func_174972_a(EnumParticleTypes.ITEM_CRACK, var20, var8, var9, var5.nextGaussian() * 0.15, var5.nextDouble() * 0.2, var5.nextGaussian() * 0.15, Item.getIdFromItem(Items.ender_eye));
                }
                for (double var10 = 0.0; var10 < 6.283185307179586; var10 += 0.15707963267948966) {
                    this.func_174972_a(EnumParticleTypes.PORTAL, var20 + Math.cos(var10) * 5.0, var8 - 0.4, var9 + Math.sin(var10) * 5.0, Math.cos(var10) * -5.0, 0.0, Math.sin(var10) * -5.0, new int[0]);
                    this.func_174972_a(EnumParticleTypes.PORTAL, var20 + Math.cos(var10) * 5.0, var8 - 0.4, var9 + Math.sin(var10) * 5.0, Math.cos(var10) * -7.0, 0.0, Math.sin(var10) * -7.0, new int[0]);
                }
            }
            case 2004: {
                for (int var26 = 0; var26 < 20; ++var26) {
                    final double var27 = p_180439_3_.getX() + 0.5 + (this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    final double var28 = p_180439_3_.getY() + 0.5 + (this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    final double var29 = p_180439_3_.getZ() + 0.5 + (this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    this.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var27, var28, var29, 0.0, 0.0, 0.0, new int[0]);
                    this.theWorld.spawnParticle(EnumParticleTypes.FLAME, var27, var28, var29, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            case 2005: {
                ItemDye.func_180617_a(this.theWorld, p_180439_3_, p_180439_4_);
                break;
            }
        }
    }
    
    @Override
    public void sendBlockBreakProgress(final int breakerId, final BlockPos pos, final int progress) {
        if (progress >= 0 && progress < 10) {
            DestroyBlockProgress var4 = this.damagedBlocks.get(breakerId);
            if (var4 == null || var4.func_180246_b().getX() != pos.getX() || var4.func_180246_b().getY() != pos.getY() || var4.func_180246_b().getZ() != pos.getZ()) {
                var4 = new DestroyBlockProgress(breakerId, pos);
                this.damagedBlocks.put(breakerId, var4);
            }
            var4.setPartialBlockDamage(progress);
            var4.setCloudUpdateTick(this.cloudTickCounter);
        }
        else {
            this.damagedBlocks.remove(breakerId);
        }
    }
    
    public void func_174979_m() {
        this.displayListEntitiesDirty = true;
    }
    
    public void resetClouds() {
        this.cloudTickCounterGlList = -9999;
    }
    
    static {
        logger = LogManager.getLogger();
        locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
        locationSunPng = new ResourceLocation("textures/environment/sun.png");
        locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
        locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
        field_175006_g = new ResourceLocation("textures/misc/forcefield.png");
    }
    
    class ContainerLocalRenderInformation
    {
        final RenderChunk field_178036_a;
        final EnumFacing field_178034_b;
        final Set field_178035_c;
        final int field_178032_d;
        private static final String __OBFID = "CL_00002534";
        
        private ContainerLocalRenderInformation(final RenderChunk p_i46248_2_, final EnumFacing p_i46248_3_, final int p_i46248_4_) {
            this.field_178035_c = EnumSet.noneOf(EnumFacing.class);
            this.field_178036_a = p_i46248_2_;
            this.field_178034_b = p_i46248_3_;
            this.field_178032_d = p_i46248_4_;
        }
        
        ContainerLocalRenderInformation(final RenderGlobal this$0, final RenderChunk p_i46249_2_, final EnumFacing p_i46249_3_, final int p_i46249_4_, final Object p_i46249_5_) {
            this(this$0, p_i46249_2_, p_i46249_3_, p_i46249_4_);
        }
    }
    
    static final class SwitchEnumUseage
    {
        static final int[] field_178037_a;
        private static final String __OBFID = "CL_00002535";
        
        static {
            field_178037_a = new int[VertexFormatElement.EnumUseage.values().length];
            try {
                SwitchEnumUseage.field_178037_a[VertexFormatElement.EnumUseage.POSITION.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumUseage.field_178037_a[VertexFormatElement.EnumUseage.UV.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumUseage.field_178037_a[VertexFormatElement.EnumUseage.COLOR.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
        }
    }
}
