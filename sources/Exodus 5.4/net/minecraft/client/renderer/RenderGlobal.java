/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  com.google.gson.JsonSyntaxException
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
import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
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
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
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
    private double frustumUpdatePosX;
    private List<ContainerLocalRenderInformation> renderInfos;
    private int countEntitiesRendered;
    private VertexFormat vertexBufferFormat;
    private double lastViewEntityYaw;
    private boolean debugFixTerrainFrustum = false;
    IRenderChunkFactory renderChunkFactory;
    private ClippingHelper debugFixedClippingHelper;
    private static final ResourceLocation locationSunPng;
    private int renderDistanceChunks = -1;
    private int frustumUpdatePosChunkZ;
    private final Map<BlockPos, ISound> mapSoundPositions;
    private int countEntitiesTotal;
    private int cloudTickCounter;
    private boolean vboEnabled = false;
    private int renderEntitiesStartupCounter = 2;
    private int glSkyList = -1;
    private final Map<Integer, DestroyBlockProgress> damagedBlocks;
    private double prevRenderSortY;
    private ChunkRenderContainer renderContainer;
    private final ChunkRenderDispatcher renderDispatcher;
    private int countEntitiesHidden;
    private final TextureManager renderEngine;
    private final Vector4f[] debugTerrainMatrix;
    private boolean displayListEntitiesDirty = true;
    private Framebuffer entityOutlineFramebuffer;
    private final Minecraft mc;
    private static final ResourceLocation locationEndSkyPng;
    private final Set<TileEntity> field_181024_n;
    private double prevRenderSortX;
    private VertexBuffer skyVBO;
    private double lastViewEntityX;
    private int starGLCallList = -1;
    private int frustumUpdatePosChunkY;
    private Set<RenderChunk> chunksToUpdate = Sets.newLinkedHashSet();
    private static final ResourceLocation locationForcefieldPng;
    private final RenderManager renderManager;
    private WorldClient theWorld;
    private double frustumUpdatePosY;
    private ShaderGroup entityOutlineShader;
    private VertexBuffer starVBO;
    private double lastViewEntityY;
    private static final ResourceLocation locationCloudsPng;
    private int glSkyList2 = -1;
    private double frustumUpdatePosZ;
    private final TextureAtlasSprite[] destroyBlockIcons;
    private double lastViewEntityPitch;
    private double lastViewEntityZ;
    private int frustumUpdatePosChunkX;
    private static final ResourceLocation locationMoonPhasesPng;
    private final Vector3d debugTerrainFrustumPosition;
    private double prevRenderSortZ;
    private ViewFrustum viewFrustum;
    private VertexBuffer sky2VBO;

    private void markBlocksForUpdate(int n, int n2, int n3, int n4, int n5, int n6) {
        this.viewFrustum.markBlocksForUpdate(n, n2, n3, n4, n5, n6);
    }

    public void makeEntityOutlineShader() {
        if (OpenGlHelper.shadersSupported) {
            if (ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
                ShaderLinkHelper.setNewStaticShaderLinkHelper();
            }
            ResourceLocation resourceLocation = new ResourceLocation("shaders/post/entity_outline.json");
            try {
                this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), resourceLocation);
                this.entityOutlineShader.createBindFramebuffers(this.mc.displayWidth, Minecraft.displayHeight);
                this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
            }
            catch (IOException iOException) {
                logger.warn("Failed to load shader: " + resourceLocation, (Throwable)iOException);
                this.entityOutlineShader = null;
                this.entityOutlineFramebuffer = null;
            }
            catch (JsonSyntaxException jsonSyntaxException) {
                logger.warn("Failed to load shader: " + resourceLocation, (Throwable)jsonSyntaxException);
                this.entityOutlineShader = null;
                this.entityOutlineFramebuffer = null;
            }
        } else {
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
        }
    }

    public static void func_181563_a(AxisAlignedBB axisAlignedBB, int n, int n2, int n3, int n4) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
        tessellator.draw();
    }

    public void createBindEntityOutlineFbs(int n, int n2) {
        if (OpenGlHelper.shadersSupported && this.entityOutlineShader != null) {
            this.entityOutlineShader.createBindFramebuffers(n, n2);
        }
    }

    public int renderBlockLayer(EnumWorldBlockLayer enumWorldBlockLayer, double d, int n, Entity entity) {
        RenderHelper.disableStandardItemLighting();
        if (enumWorldBlockLayer == EnumWorldBlockLayer.TRANSLUCENT) {
            this.mc.mcProfiler.startSection("translucent_sort");
            double d2 = entity.posX - this.prevRenderSortX;
            double d3 = entity.posY - this.prevRenderSortY;
            double d4 = entity.posZ - this.prevRenderSortZ;
            if (d2 * d2 + d3 * d3 + d4 * d4 > 1.0) {
                this.prevRenderSortX = entity.posX;
                this.prevRenderSortY = entity.posY;
                this.prevRenderSortZ = entity.posZ;
                int n2 = 0;
                for (ContainerLocalRenderInformation containerLocalRenderInformation : this.renderInfos) {
                    if (!containerLocalRenderInformation.renderChunk.compiledChunk.isLayerStarted(enumWorldBlockLayer) || n2++ >= 15) continue;
                    this.renderDispatcher.updateTransparencyLater(containerLocalRenderInformation.renderChunk);
                }
            }
            this.mc.mcProfiler.endSection();
        }
        this.mc.mcProfiler.startSection("filterempty");
        int n3 = 0;
        boolean bl = enumWorldBlockLayer == EnumWorldBlockLayer.TRANSLUCENT;
        int n4 = bl ? this.renderInfos.size() - 1 : 0;
        int n5 = bl ? -1 : this.renderInfos.size();
        int n6 = bl ? -1 : 1;
        int n7 = n4;
        while (n7 != n5) {
            RenderChunk renderChunk = this.renderInfos.get((int)n7).renderChunk;
            if (!renderChunk.getCompiledChunk().isLayerEmpty(enumWorldBlockLayer)) {
                ++n3;
                this.renderContainer.addRenderChunk(renderChunk, enumWorldBlockLayer);
            }
            n7 += n6;
        }
        this.mc.mcProfiler.endStartSection("render_" + (Object)((Object)enumWorldBlockLayer));
        this.renderBlockLayer(enumWorldBlockLayer);
        this.mc.mcProfiler.endSection();
        return n3;
    }

    public void renderClouds(float f, int n) {
        if (Minecraft.theWorld.provider.isSurfaceWorld()) {
            if (Minecraft.gameSettings.func_181147_e() == 2) {
                this.renderCloudsFancy(f, n);
            } else {
                float f2;
                GlStateManager.disableCull();
                float f3 = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * (double)f);
                int n2 = 32;
                int n3 = 8;
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                this.renderEngine.bindTexture(locationCloudsPng);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                Vec3 vec3 = this.theWorld.getCloudColour(f);
                float f4 = (float)vec3.xCoord;
                float f5 = (float)vec3.yCoord;
                float f6 = (float)vec3.zCoord;
                if (n != 2) {
                    f2 = (f4 * 30.0f + f5 * 59.0f + f6 * 11.0f) / 100.0f;
                    float f7 = (f4 * 30.0f + f5 * 70.0f) / 100.0f;
                    float f8 = (f4 * 30.0f + f6 * 70.0f) / 100.0f;
                    f4 = f2;
                    f5 = f7;
                    f6 = f8;
                }
                f2 = 4.8828125E-4f;
                double d = (float)this.cloudTickCounter + f;
                double d2 = this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * (double)f + d * (double)0.03f;
                double d3 = this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * (double)f;
                int n4 = MathHelper.floor_double(d2 / 2048.0);
                int n5 = MathHelper.floor_double(d3 / 2048.0);
                float f9 = this.theWorld.provider.getCloudHeight() - f3 + 0.33f;
                float f10 = (float)((d2 -= (double)(n4 * 2048)) * 4.8828125E-4);
                float f11 = (float)((d3 -= (double)(n5 * 2048)) * 4.8828125E-4);
                worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int n6 = -256;
                while (n6 < 256) {
                    int n7 = -256;
                    while (n7 < 256) {
                        worldRenderer.pos(n6 + 0, f9, n7 + 32).tex((float)(n6 + 0) * 4.8828125E-4f + f10, (float)(n7 + 32) * 4.8828125E-4f + f11).color(f4, f5, f6, 0.8f).endVertex();
                        worldRenderer.pos(n6 + 32, f9, n7 + 32).tex((float)(n6 + 32) * 4.8828125E-4f + f10, (float)(n7 + 32) * 4.8828125E-4f + f11).color(f4, f5, f6, 0.8f).endVertex();
                        worldRenderer.pos(n6 + 32, f9, n7 + 0).tex((float)(n6 + 32) * 4.8828125E-4f + f10, (float)(n7 + 0) * 4.8828125E-4f + f11).color(f4, f5, f6, 0.8f).endVertex();
                        worldRenderer.pos(n6 + 0, f9, n7 + 0).tex((float)(n6 + 0) * 4.8828125E-4f + f10, (float)(n7 + 0) * 4.8828125E-4f + f11).color(f4, f5, f6, 0.8f).endVertex();
                        n7 += 32;
                    }
                    n6 += 32;
                }
                tessellator.draw();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableBlend();
                GlStateManager.enableCull();
            }
        }
    }

    @Override
    public void playAuxSFX(EntityPlayer entityPlayer, int n, BlockPos blockPos, int n2) {
        Random random = this.theWorld.rand;
        switch (n) {
            case 1000: {
                this.theWorld.playSoundAtPos(blockPos, "random.click", 1.0f, 1.0f, false);
                break;
            }
            case 1001: {
                this.theWorld.playSoundAtPos(blockPos, "random.click", 1.0f, 1.2f, false);
                break;
            }
            case 1002: {
                this.theWorld.playSoundAtPos(blockPos, "random.bow", 1.0f, 1.2f, false);
                break;
            }
            case 1003: {
                this.theWorld.playSoundAtPos(blockPos, "random.door_open", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1004: {
                this.theWorld.playSoundAtPos(blockPos, "random.fizz", 0.5f, 2.6f + (random.nextFloat() - random.nextFloat()) * 0.8f, false);
                break;
            }
            case 1005: {
                if (Item.getItemById(n2) instanceof ItemRecord) {
                    this.theWorld.playRecord(blockPos, "records." + ((ItemRecord)Item.getItemById((int)n2)).recordName);
                    break;
                }
                this.theWorld.playRecord(blockPos, null);
                break;
            }
            case 1006: {
                this.theWorld.playSoundAtPos(blockPos, "random.door_close", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1007: {
                this.theWorld.playSoundAtPos(blockPos, "mob.ghast.charge", 10.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1008: {
                this.theWorld.playSoundAtPos(blockPos, "mob.ghast.fireball", 10.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1009: {
                this.theWorld.playSoundAtPos(blockPos, "mob.ghast.fireball", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1010: {
                this.theWorld.playSoundAtPos(blockPos, "mob.zombie.wood", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1011: {
                this.theWorld.playSoundAtPos(blockPos, "mob.zombie.metal", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1012: {
                this.theWorld.playSoundAtPos(blockPos, "mob.zombie.woodbreak", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1014: {
                this.theWorld.playSoundAtPos(blockPos, "mob.wither.shoot", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1015: {
                this.theWorld.playSoundAtPos(blockPos, "mob.bat.takeoff", 0.05f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1016: {
                this.theWorld.playSoundAtPos(blockPos, "mob.zombie.infect", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1017: {
                this.theWorld.playSoundAtPos(blockPos, "mob.zombie.unfect", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1020: {
                this.theWorld.playSoundAtPos(blockPos, "random.anvil_break", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1021: {
                this.theWorld.playSoundAtPos(blockPos, "random.anvil_use", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1022: {
                this.theWorld.playSoundAtPos(blockPos, "random.anvil_land", 0.3f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 2000: {
                int n3 = n2 % 3 - 1;
                int n4 = n2 / 3 % 3 - 1;
                double d = (double)blockPos.getX() + (double)n3 * 0.6 + 0.5;
                double d2 = (double)blockPos.getY() + 0.5;
                double d3 = (double)blockPos.getZ() + (double)n4 * 0.6 + 0.5;
                int n5 = 0;
                while (n5 < 10) {
                    double d4 = random.nextDouble() * 0.2 + 0.01;
                    double d5 = d + (double)n3 * 0.01 + (random.nextDouble() - 0.5) * (double)n4 * 0.5;
                    double d6 = d2 + (random.nextDouble() - 0.5) * 0.5;
                    double d7 = d3 + (double)n4 * 0.01 + (random.nextDouble() - 0.5) * (double)n3 * 0.5;
                    double d8 = (double)n3 * d4 + random.nextGaussian() * 0.01;
                    double d9 = -0.03 + random.nextGaussian() * 0.01;
                    double d10 = (double)n4 * d4 + random.nextGaussian() * 0.01;
                    this.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d5, d6, d7, d8, d9, d10, new int[0]);
                    ++n5;
                }
                return;
            }
            case 2001: {
                Block block = Block.getBlockById(n2 & 0xFFF);
                if (block.getMaterial() != Material.air) {
                    this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(block.stepSound.getBreakSound()), (block.stepSound.getVolume() + 1.0f) / 2.0f, block.stepSound.getFrequency() * 0.8f, (float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f));
                }
                this.mc.effectRenderer.addBlockDestroyEffects(blockPos, block.getStateFromMeta(n2 >> 12 & 0xFF));
                break;
            }
            case 2002: {
                double d = blockPos.getX();
                double d11 = blockPos.getY();
                double d12 = blockPos.getZ();
                int n6 = 0;
                while (n6 < 8) {
                    this.spawnParticle(EnumParticleTypes.ITEM_CRACK, d, d11, d12, random.nextGaussian() * 0.15, random.nextDouble() * 0.2, random.nextGaussian() * 0.15, Item.getIdFromItem(Items.potionitem), n2);
                    ++n6;
                }
                n6 = Items.potionitem.getColorFromDamage(n2);
                float f = (float)(n6 >> 16 & 0xFF) / 255.0f;
                float f2 = (float)(n6 >> 8 & 0xFF) / 255.0f;
                float f3 = (float)(n6 >> 0 & 0xFF) / 255.0f;
                EnumParticleTypes enumParticleTypes = EnumParticleTypes.SPELL;
                if (Items.potionitem.isEffectInstant(n2)) {
                    enumParticleTypes = EnumParticleTypes.SPELL_INSTANT;
                }
                int n7 = 0;
                while (n7 < 100) {
                    double d13 = random.nextDouble() * 4.0;
                    double d14 = random.nextDouble() * Math.PI * 2.0;
                    double d15 = Math.cos(d14) * d13;
                    double d16 = 0.01 + random.nextDouble() * 0.5;
                    double d17 = Math.sin(d14) * d13;
                    EntityFX entityFX = this.spawnEntityFX(enumParticleTypes.getParticleID(), enumParticleTypes.getShouldIgnoreRange(), d + d15 * 0.1, d11 + 0.3, d12 + d17 * 0.1, d15, d16, d17, new int[0]);
                    if (entityFX != null) {
                        float f4 = 0.75f + random.nextFloat() * 0.25f;
                        entityFX.setRBGColorF(f * f4, f2 * f4, f3 * f4);
                        entityFX.multiplyVelocity((float)d13);
                    }
                    ++n7;
                }
                this.theWorld.playSoundAtPos(blockPos, "game.potion.smash", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 2003: {
                double d = (double)blockPos.getX() + 0.5;
                double d18 = blockPos.getY();
                double d19 = (double)blockPos.getZ() + 0.5;
                int n8 = 0;
                while (n8 < 8) {
                    this.spawnParticle(EnumParticleTypes.ITEM_CRACK, d, d18, d19, random.nextGaussian() * 0.15, random.nextDouble() * 0.2, random.nextGaussian() * 0.15, Item.getIdFromItem(Items.ender_eye));
                    ++n8;
                }
                double d20 = 0.0;
                while (d20 < Math.PI * 2) {
                    this.spawnParticle(EnumParticleTypes.PORTAL, d + Math.cos(d20) * 5.0, d18 - 0.4, d19 + Math.sin(d20) * 5.0, Math.cos(d20) * -5.0, 0.0, Math.sin(d20) * -5.0, new int[0]);
                    this.spawnParticle(EnumParticleTypes.PORTAL, d + Math.cos(d20) * 5.0, d18 - 0.4, d19 + Math.sin(d20) * 5.0, Math.cos(d20) * -7.0, 0.0, Math.sin(d20) * -7.0, new int[0]);
                    d20 += 0.15707963267948966;
                }
                return;
            }
            case 2004: {
                int n9 = 0;
                while (n9 < 20) {
                    double d = (double)blockPos.getX() + 0.5 + ((double)this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    double d21 = (double)blockPos.getY() + 0.5 + ((double)this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    double d22 = (double)blockPos.getZ() + 0.5 + ((double)this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    this.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d, d21, d22, 0.0, 0.0, 0.0, new int[0]);
                    this.theWorld.spawnParticle(EnumParticleTypes.FLAME, d, d21, d22, 0.0, 0.0, 0.0, new int[0]);
                    ++n9;
                }
                return;
            }
            case 2005: {
                ItemDye.spawnBonemealParticles(this.theWorld, blockPos, n2);
            }
        }
    }

    public void setWorldAndLoadRenderers(WorldClient worldClient) {
        if (this.theWorld != null) {
            this.theWorld.removeWorldAccess(this);
        }
        this.frustumUpdatePosX = Double.MIN_VALUE;
        this.frustumUpdatePosY = Double.MIN_VALUE;
        this.frustumUpdatePosZ = Double.MIN_VALUE;
        this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
        this.renderManager.set(worldClient);
        this.theWorld = worldClient;
        if (worldClient != null) {
            worldClient.addWorldAccess(this);
            this.loadRenderers();
        }
    }

    private RenderChunk func_181562_a(BlockPos blockPos, RenderChunk renderChunk, EnumFacing enumFacing) {
        BlockPos blockPos2 = renderChunk.func_181701_a(enumFacing);
        return MathHelper.abs_int(blockPos.getX() - blockPos2.getX()) > this.renderDistanceChunks * 16 ? null : (blockPos2.getY() >= 0 && blockPos2.getY() < 256 ? (MathHelper.abs_int(blockPos.getZ() - blockPos2.getZ()) > this.renderDistanceChunks * 16 ? null : this.viewFrustum.getRenderChunk(blockPos2)) : null);
    }

    private void renderBlockLayer(EnumWorldBlockLayer enumWorldBlockLayer) {
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
        this.renderContainer.renderChunkLayer(enumWorldBlockLayer);
        if (OpenGlHelper.useVbo()) {
            for (VertexFormatElement vertexFormatElement : DefaultVertexFormats.BLOCK.getElements()) {
                VertexFormatElement.EnumUsage enumUsage = vertexFormatElement.getUsage();
                int n = vertexFormatElement.getIndex();
                switch (enumUsage) {
                    case POSITION: {
                        GL11.glDisableClientState((int)32884);
                        break;
                    }
                    case UV: {
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + n);
                        GL11.glDisableClientState((int)32888);
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                        break;
                    }
                    case COLOR: {
                        GL11.glDisableClientState((int)32886);
                        GlStateManager.resetColor();
                    }
                }
            }
        }
        this.mc.entityRenderer.disableLightmap();
    }

    public String getDebugInfoRenders() {
        int n = this.viewFrustum.renderChunks.length;
        int n2 = 0;
        for (ContainerLocalRenderInformation containerLocalRenderInformation : this.renderInfos) {
            CompiledChunk compiledChunk = containerLocalRenderInformation.renderChunk.compiledChunk;
            if (compiledChunk == CompiledChunk.DUMMY || compiledChunk.isEmpty()) continue;
            ++n2;
        }
        return String.format("C: %d/%d %sD: %d, %s", n2, n, this.mc.renderChunksMany ? "(s) " : "", this.renderDistanceChunks, this.renderDispatcher.getDebugInfo());
    }

    private void renderSky(WorldRenderer worldRenderer, float f, boolean bl) {
        int n = 64;
        int n2 = 6;
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        int n3 = -384;
        while (n3 <= 384) {
            int n4 = -384;
            while (n4 <= 384) {
                float f2 = n3;
                float f3 = n3 + 64;
                if (bl) {
                    f3 = n3;
                    f2 = n3 + 64;
                }
                worldRenderer.pos(f2, f, n4).endVertex();
                worldRenderer.pos(f3, f, n4).endVertex();
                worldRenderer.pos(f3, f, n4 + 64).endVertex();
                worldRenderer.pos(f2, f, n4 + 64).endVertex();
                n4 += 64;
            }
            n3 += 64;
        }
    }

    public void renderWorldBorder(Entity entity, float f) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        WorldBorder worldBorder = this.theWorld.getWorldBorder();
        double d = Minecraft.gameSettings.renderDistanceChunks * 16;
        if (entity.posX >= worldBorder.maxX() - d || entity.posX <= worldBorder.minX() + d || entity.posZ >= worldBorder.maxZ() - d || entity.posZ <= worldBorder.minZ() + d) {
            float f2;
            double d2;
            double d3;
            float f3;
            double d4 = 1.0 - worldBorder.getClosestDistance(entity) / d;
            d4 = Math.pow(d4, 4.0);
            double d5 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f;
            double d6 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f;
            double d7 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            this.renderEngine.bindTexture(locationForcefieldPng);
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            int n = worldBorder.getStatus().getID();
            float f4 = (float)(n >> 16 & 0xFF) / 255.0f;
            float f5 = (float)(n >> 8 & 0xFF) / 255.0f;
            float f6 = (float)(n & 0xFF) / 255.0f;
            GlStateManager.color(f4, f5, f6, (float)d4);
            GlStateManager.doPolygonOffset(-3.0f, -3.0f);
            GlStateManager.enablePolygonOffset();
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            float f7 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0f;
            float f8 = 0.0f;
            float f9 = 0.0f;
            float f10 = 128.0f;
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.setTranslation(-d5, -d6, -d7);
            double d8 = Math.max((double)MathHelper.floor_double(d7 - d), worldBorder.minZ());
            double d9 = Math.min((double)MathHelper.ceiling_double_int(d7 + d), worldBorder.maxZ());
            if (d5 > worldBorder.maxX() - d) {
                f3 = 0.0f;
                d3 = d8;
                while (d3 < d9) {
                    d2 = Math.min(1.0, d9 - d3);
                    f2 = (float)d2 * 0.5f;
                    worldRenderer.pos(worldBorder.maxX(), 256.0, d3).tex(f7 + f3, f7 + 0.0f).endVertex();
                    worldRenderer.pos(worldBorder.maxX(), 256.0, d3 + d2).tex(f7 + f2 + f3, f7 + 0.0f).endVertex();
                    worldRenderer.pos(worldBorder.maxX(), 0.0, d3 + d2).tex(f7 + f2 + f3, f7 + 128.0f).endVertex();
                    worldRenderer.pos(worldBorder.maxX(), 0.0, d3).tex(f7 + f3, f7 + 128.0f).endVertex();
                    d3 += 1.0;
                    f3 += 0.5f;
                }
            }
            if (d5 < worldBorder.minX() + d) {
                f3 = 0.0f;
                d3 = d8;
                while (d3 < d9) {
                    d2 = Math.min(1.0, d9 - d3);
                    f2 = (float)d2 * 0.5f;
                    worldRenderer.pos(worldBorder.minX(), 256.0, d3).tex(f7 + f3, f7 + 0.0f).endVertex();
                    worldRenderer.pos(worldBorder.minX(), 256.0, d3 + d2).tex(f7 + f2 + f3, f7 + 0.0f).endVertex();
                    worldRenderer.pos(worldBorder.minX(), 0.0, d3 + d2).tex(f7 + f2 + f3, f7 + 128.0f).endVertex();
                    worldRenderer.pos(worldBorder.minX(), 0.0, d3).tex(f7 + f3, f7 + 128.0f).endVertex();
                    d3 += 1.0;
                    f3 += 0.5f;
                }
            }
            d8 = Math.max((double)MathHelper.floor_double(d5 - d), worldBorder.minX());
            d9 = Math.min((double)MathHelper.ceiling_double_int(d5 + d), worldBorder.maxX());
            if (d7 > worldBorder.maxZ() - d) {
                f3 = 0.0f;
                d3 = d8;
                while (d3 < d9) {
                    d2 = Math.min(1.0, d9 - d3);
                    f2 = (float)d2 * 0.5f;
                    worldRenderer.pos(d3, 256.0, worldBorder.maxZ()).tex(f7 + f3, f7 + 0.0f).endVertex();
                    worldRenderer.pos(d3 + d2, 256.0, worldBorder.maxZ()).tex(f7 + f2 + f3, f7 + 0.0f).endVertex();
                    worldRenderer.pos(d3 + d2, 0.0, worldBorder.maxZ()).tex(f7 + f2 + f3, f7 + 128.0f).endVertex();
                    worldRenderer.pos(d3, 0.0, worldBorder.maxZ()).tex(f7 + f3, f7 + 128.0f).endVertex();
                    d3 += 1.0;
                    f3 += 0.5f;
                }
            }
            if (d7 < worldBorder.minZ() + d) {
                f3 = 0.0f;
                d3 = d8;
                while (d3 < d9) {
                    d2 = Math.min(1.0, d9 - d3);
                    f2 = (float)d2 * 0.5f;
                    worldRenderer.pos(d3, 256.0, worldBorder.minZ()).tex(f7 + f3, f7 + 0.0f).endVertex();
                    worldRenderer.pos(d3 + d2, 256.0, worldBorder.minZ()).tex(f7 + f2 + f3, f7 + 0.0f).endVertex();
                    worldRenderer.pos(d3 + d2, 0.0, worldBorder.minZ()).tex(f7 + f2 + f3, f7 + 128.0f).endVertex();
                    worldRenderer.pos(d3, 0.0, worldBorder.minZ()).tex(f7 + f3, f7 + 128.0f).endVertex();
                    d3 += 1.0;
                    f3 += 0.5f;
                }
            }
            tessellator.draw();
            worldRenderer.setTranslation(0.0, 0.0, 0.0);
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

    static {
        locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
        locationSunPng = new ResourceLocation("textures/environment/sun.png");
        locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
        locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
        locationForcefieldPng = new ResourceLocation("textures/misc/forcefield.png");
    }

    @Override
    public void spawnParticle(int n, boolean bl, final double d, final double d2, final double d3, double d4, double d5, double d6, int ... nArray) {
        try {
            this.spawnEntityFX(n, bl, d, d2, d3, d4, d5, d6, nArray);
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Exception while adding particle");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("Particle being added");
            crashReportCategory.addCrashSection("ID", n);
            if (nArray != null) {
                crashReportCategory.addCrashSection("Parameters", nArray);
            }
            crashReportCategory.addCrashSectionCallable("Position", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(d, d2, d3);
                }
            });
            throw new ReportedException(crashReport);
        }
    }

    public String getDebugInfoEntities() {
        return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
    }

    public void setDisplayListEntitiesDirty() {
        this.displayListEntitiesDirty = true;
    }

    @Override
    public void onEntityRemoved(Entity entity) {
    }

    public void setupTerrain(Entity entity, double d, ICamera object, int n, boolean bl) {
        Object object2;
        Collection<RenderChunk> collection;
        boolean bl2;
        Object object3;
        if (Minecraft.gameSettings.renderDistanceChunks != this.renderDistanceChunks) {
            this.loadRenderers();
        }
        this.theWorld.theProfiler.startSection("camera");
        double d2 = entity.posX - this.frustumUpdatePosX;
        double d3 = entity.posY - this.frustumUpdatePosY;
        double d4 = entity.posZ - this.frustumUpdatePosZ;
        if (this.frustumUpdatePosChunkX != entity.chunkCoordX || this.frustumUpdatePosChunkY != entity.chunkCoordY || this.frustumUpdatePosChunkZ != entity.chunkCoordZ || d2 * d2 + d3 * d3 + d4 * d4 > 16.0) {
            this.frustumUpdatePosX = entity.posX;
            this.frustumUpdatePosY = entity.posY;
            this.frustumUpdatePosZ = entity.posZ;
            this.frustumUpdatePosChunkX = entity.chunkCoordX;
            this.frustumUpdatePosChunkY = entity.chunkCoordY;
            this.frustumUpdatePosChunkZ = entity.chunkCoordZ;
            this.viewFrustum.updateChunkPositions(entity.posX, entity.posZ);
        }
        this.theWorld.theProfiler.endStartSection("renderlistcamera");
        double d5 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * d;
        double d6 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * d;
        double d7 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * d;
        this.renderContainer.initialize(d5, d6, d7);
        this.theWorld.theProfiler.endStartSection("cull");
        if (this.debugFixedClippingHelper != null) {
            object3 = new Frustum(this.debugFixedClippingHelper);
            ((Frustum)object3).setPosition(this.debugTerrainFrustumPosition.field_181059_a, this.debugTerrainFrustumPosition.field_181060_b, this.debugTerrainFrustumPosition.field_181061_c);
            object = object3;
        }
        this.mc.mcProfiler.endStartSection("culling");
        object3 = new BlockPos(d5, d6 + (double)entity.getEyeHeight(), d7);
        RenderChunk renderChunk = this.viewFrustum.getRenderChunk((BlockPos)object3);
        BlockPos blockPos = new BlockPos(MathHelper.floor_double(d5 / 16.0) * 16, MathHelper.floor_double(d6 / 16.0) * 16, MathHelper.floor_double(d7 / 16.0) * 16);
        this.displayListEntitiesDirty = this.displayListEntitiesDirty || !this.chunksToUpdate.isEmpty() || entity.posX != this.lastViewEntityX || entity.posY != this.lastViewEntityY || entity.posZ != this.lastViewEntityZ || (double)entity.rotationPitch != this.lastViewEntityPitch || (double)entity.rotationYaw != this.lastViewEntityYaw;
        this.lastViewEntityX = entity.posX;
        this.lastViewEntityY = entity.posY;
        this.lastViewEntityZ = entity.posZ;
        this.lastViewEntityPitch = entity.rotationPitch;
        this.lastViewEntityYaw = entity.rotationYaw;
        boolean bl3 = bl2 = this.debugFixedClippingHelper != null;
        if (!bl2 && this.displayListEntitiesDirty) {
            EnumFacing enumFacing;
            Object object4;
            Set<EnumFacing> set;
            this.displayListEntitiesDirty = false;
            this.renderInfos = Lists.newArrayList();
            collection = Lists.newLinkedList();
            boolean bl4 = this.mc.renderChunksMany;
            if (renderChunk != null) {
                boolean bl5 = false;
                object2 = new ContainerLocalRenderInformation(renderChunk, null, 0);
                set = this.getVisibleFacings((BlockPos)object3);
                if (set.size() == 1) {
                    object4 = this.getViewVector(entity, d);
                    enumFacing = EnumFacing.getFacingFromVector(((Vector3f)object4).x, ((Vector3f)object4).y, ((Vector3f)object4).z).getOpposite();
                    set.remove(enumFacing);
                }
                if (set.isEmpty()) {
                    bl5 = true;
                }
                if (bl5 && !bl) {
                    this.renderInfos.add((ContainerLocalRenderInformation)object2);
                } else {
                    if (bl && this.theWorld.getBlockState((BlockPos)object3).getBlock().isOpaqueCube()) {
                        bl4 = false;
                    }
                    renderChunk.setFrameIndex(n);
                    collection.add((RenderChunk)object2);
                }
            } else {
                int n2 = ((Vec3i)object3).getY() > 0 ? 248 : 8;
                int n3 = -this.renderDistanceChunks;
                while (n3 <= this.renderDistanceChunks) {
                    int n4 = -this.renderDistanceChunks;
                    while (n4 <= this.renderDistanceChunks) {
                        object4 = this.viewFrustum.getRenderChunk(new BlockPos((n3 << 4) + 8, n2, (n4 << 4) + 8));
                        if (object4 != null && object.isBoundingBoxInFrustum(((RenderChunk)object4).boundingBox)) {
                            ((RenderChunk)object4).setFrameIndex(n);
                            collection.add((RenderChunk)((Object)new ContainerLocalRenderInformation((RenderChunk)object4, null, 0)));
                        }
                        ++n4;
                    }
                    ++n3;
                }
            }
            while (!collection.isEmpty()) {
                ContainerLocalRenderInformation containerLocalRenderInformation = (ContainerLocalRenderInformation)collection.poll();
                object2 = containerLocalRenderInformation.renderChunk;
                set = containerLocalRenderInformation.facing;
                object4 = ((RenderChunk)object2).getPosition();
                this.renderInfos.add(containerLocalRenderInformation);
                EnumFacing[] enumFacingArray = EnumFacing.values();
                int n5 = enumFacingArray.length;
                int n6 = 0;
                while (n6 < n5) {
                    enumFacing = enumFacingArray[n6];
                    RenderChunk renderChunk2 = this.func_181562_a(blockPos, (RenderChunk)object2, enumFacing);
                    if (!(bl4 && containerLocalRenderInformation.setFacing.contains(enumFacing.getOpposite()) || bl4 && set != null && !((RenderChunk)object2).getCompiledChunk().isVisible(((EnumFacing)((Object)set)).getOpposite(), enumFacing) || renderChunk2 == null || !renderChunk2.setFrameIndex(n) || !object.isBoundingBoxInFrustum(renderChunk2.boundingBox))) {
                        ContainerLocalRenderInformation containerLocalRenderInformation2 = new ContainerLocalRenderInformation(renderChunk2, enumFacing, containerLocalRenderInformation.counter + 1);
                        containerLocalRenderInformation2.setFacing.addAll(containerLocalRenderInformation.setFacing);
                        containerLocalRenderInformation2.setFacing.add(enumFacing);
                        collection.add((RenderChunk)((Object)containerLocalRenderInformation2));
                    }
                    ++n6;
                }
            }
        }
        if (this.debugFixTerrainFrustum) {
            this.fixTerrainFrustum(d5, d6, d7);
            this.debugFixTerrainFrustum = false;
        }
        this.renderDispatcher.clearChunkUpdates();
        collection = this.chunksToUpdate;
        this.chunksToUpdate = Sets.newLinkedHashSet();
        for (ContainerLocalRenderInformation containerLocalRenderInformation : this.renderInfos) {
            object2 = containerLocalRenderInformation.renderChunk;
            if (!((RenderChunk)object2).isNeedsUpdate() && !collection.contains(object2)) continue;
            this.displayListEntitiesDirty = true;
            if (this.isPositionInRenderChunk(blockPos, containerLocalRenderInformation.renderChunk)) {
                this.mc.mcProfiler.startSection("build near");
                this.renderDispatcher.updateChunkNow((RenderChunk)object2);
                ((RenderChunk)object2).setNeedsUpdate(false);
                this.mc.mcProfiler.endSection();
                continue;
            }
            this.chunksToUpdate.add((RenderChunk)object2);
        }
        this.chunksToUpdate.addAll(collection);
        this.mc.mcProfiler.endSection();
    }

    @Override
    public void markBlockRangeForRenderUpdate(int n, int n2, int n3, int n4, int n5, int n6) {
        this.markBlocksForUpdate(n - 1, n2 - 1, n3 - 1, n4 + 1, n5 + 1, n6 + 1);
    }

    private EntityFX spawnEntityFX(int n, boolean bl, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
        if (this.mc != null && this.mc.getRenderViewEntity() != null && this.mc.effectRenderer != null) {
            int n2 = Minecraft.gameSettings.particleSetting;
            if (n2 == 1 && this.theWorld.rand.nextInt(3) == 0) {
                n2 = 2;
            }
            double d7 = this.mc.getRenderViewEntity().posX - d;
            double d8 = this.mc.getRenderViewEntity().posY - d2;
            double d9 = this.mc.getRenderViewEntity().posZ - d3;
            if (bl) {
                return this.mc.effectRenderer.spawnEffectParticle(n, d, d2, d3, d4, d5, d6, nArray);
            }
            double d10 = 16.0;
            return d7 * d7 + d8 * d8 + d9 * d9 > 256.0 ? null : (n2 > 1 ? null : this.mc.effectRenderer.spawnEffectParticle(n, d, d2, d3, d4, d5, d6, nArray));
        }
        return null;
    }

    public void updateChunks(long l) {
        this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(l);
        if (!this.chunksToUpdate.isEmpty()) {
            Iterator<RenderChunk> iterator = this.chunksToUpdate.iterator();
            while (iterator.hasNext()) {
                RenderChunk renderChunk = iterator.next();
                if (!this.renderDispatcher.updateChunkLater(renderChunk)) break;
                renderChunk.setNeedsUpdate(false);
                iterator.remove();
                long l2 = l - System.nanoTime();
                if (l2 < 0L) break;
            }
        }
    }

    private void generateSky() {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        if (this.skyVBO != null) {
            this.skyVBO.deleteGlBuffers();
        }
        if (this.glSkyList >= 0) {
            GLAllocation.deleteDisplayLists(this.glSkyList);
            this.glSkyList = -1;
        }
        if (this.vboEnabled) {
            this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(worldRenderer, 16.0f, false);
            worldRenderer.finishDrawing();
            worldRenderer.reset();
            this.skyVBO.func_181722_a(worldRenderer.getByteBuffer());
        } else {
            this.glSkyList = GLAllocation.generateDisplayLists(1);
            GL11.glNewList((int)this.glSkyList, (int)4864);
            this.renderSky(worldRenderer, 16.0f, false);
            tessellator.draw();
            GL11.glEndList();
        }
    }

    private Set<EnumFacing> getVisibleFacings(BlockPos blockPos) {
        VisGraph visGraph = new VisGraph();
        BlockPos blockPos2 = new BlockPos(blockPos.getX() >> 4 << 4, blockPos.getY() >> 4 << 4, blockPos.getZ() >> 4 << 4);
        Chunk chunk = this.theWorld.getChunkFromBlockCoords(blockPos2);
        for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.getAllInBoxMutable(blockPos2, blockPos2.add(15, 15, 15))) {
            if (!chunk.getBlock(mutableBlockPos).isOpaqueCube()) continue;
            visGraph.func_178606_a(mutableBlockPos);
        }
        return visGraph.func_178609_b(blockPos);
    }

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager) {
        this.updateDestroyBlockIcons();
    }

    private void fixTerrainFrustum(double d, double d2, double d3) {
        this.debugFixedClippingHelper = new ClippingHelperImpl();
        ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
        net.minecraft.util.Matrix4f matrix4f = new net.minecraft.util.Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
        matrix4f.transpose();
        net.minecraft.util.Matrix4f matrix4f2 = new net.minecraft.util.Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
        matrix4f2.transpose();
        net.minecraft.util.Matrix4f matrix4f3 = new net.minecraft.util.Matrix4f();
        net.minecraft.util.Matrix4f.mul((Matrix4f)matrix4f2, (Matrix4f)matrix4f, (Matrix4f)matrix4f3);
        matrix4f3.invert();
        this.debugTerrainFrustumPosition.field_181059_a = d;
        this.debugTerrainFrustumPosition.field_181060_b = d2;
        this.debugTerrainFrustumPosition.field_181061_c = d3;
        this.debugTerrainMatrix[0] = new Vector4f(-1.0f, -1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[1] = new Vector4f(1.0f, -1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[2] = new Vector4f(1.0f, 1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[3] = new Vector4f(-1.0f, 1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[4] = new Vector4f(-1.0f, -1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[5] = new Vector4f(1.0f, -1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[6] = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[7] = new Vector4f(-1.0f, 1.0f, 1.0f, 1.0f);
        int n = 0;
        while (n < 8) {
            net.minecraft.util.Matrix4f.transform((Matrix4f)matrix4f3, (Vector4f)this.debugTerrainMatrix[n], (Vector4f)this.debugTerrainMatrix[n]);
            this.debugTerrainMatrix[n].x /= this.debugTerrainMatrix[n].w;
            this.debugTerrainMatrix[n].y /= this.debugTerrainMatrix[n].w;
            this.debugTerrainMatrix[n].z /= this.debugTerrainMatrix[n].w;
            this.debugTerrainMatrix[n].w = 1.0f;
            ++n;
        }
    }

    @Override
    public void playSound(String string, double d, double d2, double d3, float f, float f2) {
    }

    protected void stopChunkUpdates() {
        this.chunksToUpdate.clear();
        this.renderDispatcher.stopChunkUpdates();
    }

    public void drawSelectionBox(EntityPlayer entityPlayer, MovingObjectPosition movingObjectPosition, int n, float f) {
        if (n == 0 && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.4f);
            GL11.glLineWidth((float)2.0f);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            float f2 = 0.002f;
            BlockPos blockPos = movingObjectPosition.getBlockPos();
            Block block = this.theWorld.getBlockState(blockPos).getBlock();
            if (block.getMaterial() != Material.air && this.theWorld.getWorldBorder().contains(blockPos)) {
                block.setBlockBoundsBasedOnState(this.theWorld, blockPos);
                double d = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * (double)f;
                double d2 = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * (double)f;
                double d3 = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * (double)f;
                RenderGlobal.func_181561_a(block.getSelectedBoundingBox(this.theWorld, blockPos).expand(0.002f, 0.002f, 0.002f).offset(-d, -d2, -d3));
            }
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    @Override
    public void sendBlockBreakProgress(int n, BlockPos blockPos, int n2) {
        if (n2 >= 0 && n2 < 10) {
            DestroyBlockProgress destroyBlockProgress = this.damagedBlocks.get(n);
            if (destroyBlockProgress == null || destroyBlockProgress.getPosition().getX() != blockPos.getX() || destroyBlockProgress.getPosition().getY() != blockPos.getY() || destroyBlockProgress.getPosition().getZ() != blockPos.getZ()) {
                destroyBlockProgress = new DestroyBlockProgress(n, blockPos);
                this.damagedBlocks.put(n, destroyBlockProgress);
            }
            destroyBlockProgress.setPartialBlockDamage(n2);
            destroyBlockProgress.setCloudUpdateTick(this.cloudTickCounter);
        } else {
            this.damagedBlocks.remove(n);
        }
    }

    public RenderGlobal(Minecraft minecraft) {
        this.renderInfos = Lists.newArrayListWithCapacity((int)69696);
        this.field_181024_n = Sets.newHashSet();
        this.damagedBlocks = Maps.newHashMap();
        this.mapSoundPositions = Maps.newHashMap();
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
        this.debugTerrainMatrix = new Vector4f[8];
        this.debugTerrainFrustumPosition = new Vector3d();
        this.mc = minecraft;
        this.renderManager = minecraft.getRenderManager();
        this.renderEngine = minecraft.getTextureManager();
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

    public void loadRenderers() {
        if (this.theWorld != null) {
            this.displayListEntitiesDirty = true;
            Blocks.leaves.setGraphicsLevel(Minecraft.gameSettings.fancyGraphics);
            Blocks.leaves2.setGraphicsLevel(Minecraft.gameSettings.fancyGraphics);
            this.renderDistanceChunks = Minecraft.gameSettings.renderDistanceChunks;
            boolean bl = this.vboEnabled;
            this.vboEnabled = OpenGlHelper.useVbo();
            if (bl && !this.vboEnabled) {
                this.renderContainer = new RenderList();
                this.renderChunkFactory = new ListChunkFactory();
            } else if (!bl && this.vboEnabled) {
                this.renderContainer = new VboRenderList();
                this.renderChunkFactory = new VboChunkFactory();
            }
            if (bl != this.vboEnabled) {
                this.generateStars();
                this.generateSky();
                this.generateSky2();
            }
            if (this.viewFrustum != null) {
                this.viewFrustum.deleteGlResources();
            }
            this.stopChunkUpdates();
            Object object = this.field_181024_n;
            synchronized (object) {
                this.field_181024_n.clear();
            }
            this.viewFrustum = new ViewFrustum(this.theWorld, Minecraft.gameSettings.renderDistanceChunks, this, this.renderChunkFactory);
            if (this.theWorld != null && (object = this.mc.getRenderViewEntity()) != null) {
                this.viewFrustum.updateChunkPositions(((Entity)object).posX, ((Entity)object).posZ);
            }
            this.renderEntitiesStartupCounter = 2;
        }
    }

    public void drawBlockDamageTexture(Tessellator tessellator, WorldRenderer worldRenderer, Entity entity, float f) {
        double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f;
        double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f;
        double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f;
        if (!this.damagedBlocks.isEmpty()) {
            this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            this.preRenderDamagedBlocks();
            worldRenderer.begin(7, DefaultVertexFormats.BLOCK);
            worldRenderer.setTranslation(-d, -d2, -d3);
            worldRenderer.markDirty();
            Iterator<DestroyBlockProgress> iterator = this.damagedBlocks.values().iterator();
            while (iterator.hasNext()) {
                DestroyBlockProgress destroyBlockProgress = iterator.next();
                BlockPos blockPos = destroyBlockProgress.getPosition();
                double d4 = (double)blockPos.getX() - d;
                double d5 = (double)blockPos.getY() - d2;
                double d6 = (double)blockPos.getZ() - d3;
                Block block = this.theWorld.getBlockState(blockPos).getBlock();
                if (block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSign || block instanceof BlockSkull) continue;
                if (d4 * d4 + d5 * d5 + d6 * d6 > 1024.0) {
                    iterator.remove();
                    continue;
                }
                IBlockState iBlockState = this.theWorld.getBlockState(blockPos);
                if (iBlockState.getBlock().getMaterial() == Material.air) continue;
                int n = destroyBlockProgress.getPartialBlockDamage();
                TextureAtlasSprite textureAtlasSprite = this.destroyBlockIcons[n];
                BlockRendererDispatcher blockRendererDispatcher = this.mc.getBlockRendererDispatcher();
                blockRendererDispatcher.renderBlockDamage(iBlockState, blockPos, textureAtlasSprite, this.theWorld);
            }
            tessellator.draw();
            worldRenderer.setTranslation(0.0, 0.0, 0.0);
            this.postRenderDamagedBlocks();
        }
    }

    private void generateStars() {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        if (this.starVBO != null) {
            this.starVBO.deleteGlBuffers();
        }
        if (this.starGLCallList >= 0) {
            GLAllocation.deleteDisplayLists(this.starGLCallList);
            this.starGLCallList = -1;
        }
        if (this.vboEnabled) {
            this.starVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderStars(worldRenderer);
            worldRenderer.finishDrawing();
            worldRenderer.reset();
            this.starVBO.func_181722_a(worldRenderer.getByteBuffer());
        } else {
            this.starGLCallList = GLAllocation.generateDisplayLists(1);
            GlStateManager.pushMatrix();
            GL11.glNewList((int)this.starGLCallList, (int)4864);
            this.renderStars(worldRenderer);
            tessellator.draw();
            GL11.glEndList();
            GlStateManager.popMatrix();
        }
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

    public void renderSky(float f, int n) {
        if (Minecraft.theWorld.provider.getDimensionId() == 1) {
            this.renderSkyEnd();
        } else if (Minecraft.theWorld.provider.isSurfaceWorld()) {
            float f2;
            float f3;
            int n2;
            int n3;
            float f4;
            float f5;
            float f6;
            GlStateManager.disableTexture2D();
            Vec3 vec3 = this.theWorld.getSkyColor(this.mc.getRenderViewEntity(), f);
            float f7 = (float)vec3.xCoord;
            float f8 = (float)vec3.yCoord;
            float f9 = (float)vec3.zCoord;
            if (n != 2) {
                float f10 = (f7 * 30.0f + f8 * 59.0f + f9 * 11.0f) / 100.0f;
                float f11 = (f7 * 30.0f + f8 * 70.0f) / 100.0f;
                float f12 = (f7 * 30.0f + f9 * 70.0f) / 100.0f;
                f7 = f10;
                f8 = f11;
                f9 = f12;
            }
            GlStateManager.color(f7, f8, f9);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            GlStateManager.depthMask(false);
            GlStateManager.enableFog();
            GlStateManager.color(f7, f8, f9);
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
            float[] fArray = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(f), f);
            if (fArray != null) {
                GlStateManager.disableTexture2D();
                GlStateManager.shadeModel(7425);
                GlStateManager.pushMatrix();
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(MathHelper.sin(this.theWorld.getCelestialAngleRadians(f)) < 0.0f ? 180.0f : 0.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                f6 = fArray[0];
                f5 = fArray[1];
                float f13 = fArray[2];
                if (n != 2) {
                    float f14 = (f6 * 30.0f + f5 * 59.0f + f13 * 11.0f) / 100.0f;
                    float f15 = (f6 * 30.0f + f5 * 70.0f) / 100.0f;
                    f4 = (f6 * 30.0f + f13 * 70.0f) / 100.0f;
                    f6 = f14;
                    f5 = f15;
                    f13 = f4;
                }
                worldRenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos(0.0, 100.0, 0.0).color(f6, f5, f13, fArray[3]).endVertex();
                n3 = 16;
                n2 = 0;
                while (n2 <= 16) {
                    f4 = (float)n2 * (float)Math.PI * 2.0f / 16.0f;
                    f3 = MathHelper.sin(f4);
                    f2 = MathHelper.cos(f4);
                    worldRenderer.pos(f3 * 120.0f, f2 * 120.0f, -f2 * 40.0f * fArray[3]).color(fArray[0], fArray[1], fArray[2], 0.0f).endVertex();
                    ++n2;
                }
                tessellator.draw();
                GlStateManager.popMatrix();
                GlStateManager.shadeModel(7424);
            }
            GlStateManager.enableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            GlStateManager.pushMatrix();
            f6 = 1.0f - this.theWorld.getRainStrength(f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, f6);
            GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(this.theWorld.getCelestialAngle(f) * 360.0f, 1.0f, 0.0f, 0.0f);
            f5 = 30.0f;
            this.renderEngine.bindTexture(locationSunPng);
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(-f5, 100.0, -f5).tex(0.0, 0.0).endVertex();
            worldRenderer.pos(f5, 100.0, -f5).tex(1.0, 0.0).endVertex();
            worldRenderer.pos(f5, 100.0, f5).tex(1.0, 1.0).endVertex();
            worldRenderer.pos(-f5, 100.0, f5).tex(0.0, 1.0).endVertex();
            tessellator.draw();
            f5 = 20.0f;
            this.renderEngine.bindTexture(locationMoonPhasesPng);
            int n4 = this.theWorld.getMoonPhase();
            n3 = n4 % 4;
            n2 = n4 / 4 % 2;
            f4 = (float)(n3 + 0) / 4.0f;
            f3 = (float)(n2 + 0) / 2.0f;
            f2 = (float)(n3 + 1) / 4.0f;
            float f16 = (float)(n2 + 1) / 2.0f;
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(-f5, -100.0, f5).tex(f2, f16).endVertex();
            worldRenderer.pos(f5, -100.0, f5).tex(f4, f16).endVertex();
            worldRenderer.pos(f5, -100.0, -f5).tex(f4, f3).endVertex();
            worldRenderer.pos(-f5, -100.0, -f5).tex(f2, f3).endVertex();
            tessellator.draw();
            GlStateManager.disableTexture2D();
            float f17 = this.theWorld.getStarBrightness(f) * f6;
            if (f17 > 0.0f) {
                GlStateManager.color(f17, f17, f17, f17);
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
            double d = Minecraft.thePlayer.getPositionEyes((float)f).yCoord - this.theWorld.getHorizon();
            if (d < 0.0) {
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
                float f19 = -((float)(d + 65.0));
                float f20 = -1.0f;
                worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos(-1.0, f19, 1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(1.0, f19, 1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(1.0, f19, -1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(-1.0, f19, -1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(1.0, f19, 1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(1.0, f19, -1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(-1.0, f19, -1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(-1.0, f19, 1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
            }
            if (this.theWorld.provider.isSkyColored()) {
                GlStateManager.color(f7 * 0.2f + 0.04f, f8 * 0.2f + 0.04f, f9 * 0.6f + 0.1f);
            } else {
                GlStateManager.color(f7, f8, f9);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, -((float)(d - 16.0)), 0.0f);
            GlStateManager.callList(this.glSkyList2);
            GlStateManager.popMatrix();
            GlStateManager.enableTexture2D();
            GlStateManager.depthMask(true);
        }
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
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        int n = 0;
        while (n < 6) {
            GlStateManager.pushMatrix();
            if (n == 1) {
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            }
            if (n == 2) {
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            }
            if (n == 3) {
                GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
            }
            if (n == 4) {
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            }
            if (n == 5) {
                GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
            }
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldRenderer.pos(-100.0, -100.0, -100.0).tex(0.0, 0.0).color(40, 40, 40, 255).endVertex();
            worldRenderer.pos(-100.0, -100.0, 100.0).tex(0.0, 16.0).color(40, 40, 40, 255).endVertex();
            worldRenderer.pos(100.0, -100.0, 100.0).tex(16.0, 16.0).color(40, 40, 40, 255).endVertex();
            worldRenderer.pos(100.0, -100.0, -100.0).tex(16.0, 0.0).color(40, 40, 40, 255).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            ++n;
        }
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
    }

    @Override
    public void notifyLightSet(BlockPos blockPos) {
        int n = blockPos.getX();
        int n2 = blockPos.getY();
        int n3 = blockPos.getZ();
        this.markBlocksForUpdate(n - 1, n2 - 1, n3 - 1, n + 1, n2 + 1, n3 + 1);
    }

    public void func_181023_a(Collection<TileEntity> collection, Collection<TileEntity> collection2) {
        Set<TileEntity> set = this.field_181024_n;
        synchronized (set) {
            this.field_181024_n.removeAll(collection);
            this.field_181024_n.addAll(collection2);
        }
    }

    public void renderEntityOutlineFramebuffer() {
        if (this.isRenderEntityOutlines()) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, Minecraft.displayHeight, false);
            GlStateManager.disableBlend();
        }
    }

    private void updateDestroyBlockIcons() {
        TextureMap textureMap = this.mc.getTextureMapBlocks();
        int n = 0;
        while (n < this.destroyBlockIcons.length) {
            this.destroyBlockIcons[n] = textureMap.getAtlasSprite("minecraft:blocks/destroy_stage_" + n);
            ++n;
        }
    }

    private void spawnParticle(EnumParticleTypes enumParticleTypes, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
        this.spawnParticle(enumParticleTypes.getParticleID(), enumParticleTypes.getShouldIgnoreRange(), d, d2, d3, d4, d5, d6, nArray);
    }

    @Override
    public void markBlockForUpdate(BlockPos blockPos) {
        int n = blockPos.getX();
        int n2 = blockPos.getY();
        int n3 = blockPos.getZ();
        this.markBlocksForUpdate(n - 1, n2 - 1, n3 - 1, n + 1, n2 + 1, n3 + 1);
    }

    public void updateClouds() {
        ++this.cloudTickCounter;
        if (this.cloudTickCounter % 20 == 0) {
            this.cleanupDamagedBlocks(this.damagedBlocks.values().iterator());
        }
    }

    public boolean hasCloudFog(double d, double d2, double d3, float f) {
        return false;
    }

    /*
     * WARNING - void declaration
     */
    public void renderEntities(Entity entity, ICamera iCamera, float f) {
        if (this.renderEntitiesStartupCounter > 0) {
            --this.renderEntitiesStartupCounter;
        } else {
            void containerLocalRenderInformation;
            double d = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)f;
            double d2 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)f;
            double d3 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)f;
            this.theWorld.theProfiler.startSection("prepare");
            TileEntityRendererDispatcher.instance.cacheActiveRenderInfo(this.theWorld, this.mc.getTextureManager(), Minecraft.fontRendererObj, this.mc.getRenderViewEntity(), f);
            this.renderManager.cacheActiveRenderInfo(this.theWorld, Minecraft.fontRendererObj, this.mc.getRenderViewEntity(), this.mc.pointedEntity, Minecraft.gameSettings, f);
            this.countEntitiesTotal = 0;
            this.countEntitiesRendered = 0;
            this.countEntitiesHidden = 0;
            Entity entity2 = this.mc.getRenderViewEntity();
            double d4 = entity2.lastTickPosX + (entity2.posX - entity2.lastTickPosX) * (double)f;
            double d5 = entity2.lastTickPosY + (entity2.posY - entity2.lastTickPosY) * (double)f;
            double d6 = entity2.lastTickPosZ + (entity2.posZ - entity2.lastTickPosZ) * (double)f;
            TileEntityRendererDispatcher.staticPlayerX = d4;
            TileEntityRendererDispatcher.staticPlayerY = d5;
            TileEntityRendererDispatcher.staticPlayerZ = d6;
            this.renderManager.setRenderPosition(d4, d5, d6);
            this.mc.entityRenderer.enableLightmap();
            this.theWorld.theProfiler.endStartSection("global");
            List<Entity> list = this.theWorld.getLoadedEntityList();
            this.countEntitiesTotal = list.size();
            boolean n = false;
            while (containerLocalRenderInformation < this.theWorld.weatherEffects.size()) {
                Entity entity3 = (Entity)this.theWorld.weatherEffects.get((int)containerLocalRenderInformation);
                ++this.countEntitiesRendered;
                if (entity3.isInRangeToRender3d(d, d2, d3)) {
                    this.renderManager.renderEntitySimple(entity3, f);
                }
                ++containerLocalRenderInformation;
            }
            if (this.isRenderEntityOutlines()) {
                void object2;
                GlStateManager.depthFunc(519);
                GlStateManager.disableFog();
                this.entityOutlineFramebuffer.framebufferClear();
                this.entityOutlineFramebuffer.bindFramebuffer(false);
                this.theWorld.theProfiler.endStartSection("entityOutlines");
                RenderHelper.disableStandardItemLighting();
                this.renderManager.setRenderOutlines(true);
                boolean containerLocalRenderInformation2 = false;
                while (object2 < list.size()) {
                    boolean bl;
                    Entity entity4 = list.get((int)object2);
                    boolean bl2 = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
                    boolean bl3 = bl = entity4.isInRangeToRender3d(d, d2, d3) && (entity4.ignoreFrustumCheck || iCamera.isBoundingBoxInFrustum(entity4.getEntityBoundingBox()) || entity4.riddenByEntity == Minecraft.thePlayer) && entity4 instanceof EntityPlayer;
                    if ((entity4 != this.mc.getRenderViewEntity() || Minecraft.gameSettings.thirdPersonView != 0 || bl2) && bl) {
                        this.renderManager.renderEntitySimple(entity4, f);
                    }
                    ++object2;
                }
                this.renderManager.setRenderOutlines(false);
                RenderHelper.enableStandardItemLighting();
                GlStateManager.depthMask(false);
                this.entityOutlineShader.loadShaderGroup(f);
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
            for (ContainerLocalRenderInformation containerLocalRenderInformation3 : this.renderInfos) {
                Chunk chunk = this.theWorld.getChunkFromBlockCoords(containerLocalRenderInformation3.renderChunk.getPosition());
                ClassInheritanceMultiMap<Entity> classInheritanceMultiMap = chunk.getEntityLists()[containerLocalRenderInformation3.renderChunk.getPosition().getY() / 16];
                if (classInheritanceMultiMap.isEmpty()) continue;
                for (Entity entity5 : classInheritanceMultiMap) {
                    boolean bl;
                    boolean bl4 = bl = this.renderManager.shouldRender(entity5, iCamera, d, d2, d3) || entity5.riddenByEntity == Minecraft.thePlayer;
                    if (bl) {
                        boolean bl5;
                        boolean bl6 = bl5 = this.mc.getRenderViewEntity() instanceof EntityLivingBase ? ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping() : false;
                        if (entity5 == this.mc.getRenderViewEntity() && Minecraft.gameSettings.thirdPersonView == 0 && !bl5 || !(entity5.posY < 0.0) && !(entity5.posY >= 256.0) && !this.theWorld.isBlockLoaded(new BlockPos(entity5))) continue;
                        ++this.countEntitiesRendered;
                        this.renderManager.renderEntitySimple(entity5, f);
                    }
                    if (bl || !(entity5 instanceof EntityWitherSkull)) continue;
                    this.mc.getRenderManager().renderWitherSkull(entity5, f);
                }
            }
            this.theWorld.theProfiler.endStartSection("blockentities");
            RenderHelper.enableStandardItemLighting();
            for (ContainerLocalRenderInformation containerLocalRenderInformation4 : this.renderInfos) {
                List<TileEntity> list2 = containerLocalRenderInformation4.renderChunk.getCompiledChunk().getTileEntities();
                if (list2.isEmpty()) continue;
                for (TileEntity tileEntity : list2) {
                    TileEntityRendererDispatcher.instance.renderTileEntity(tileEntity, f, -1);
                }
            }
            Set<TileEntity> set = this.field_181024_n;
            synchronized (set) {
                for (TileEntity tileEntity : this.field_181024_n) {
                    TileEntityRendererDispatcher.instance.renderTileEntity(tileEntity, f, -1);
                }
            }
            this.preRenderDamagedBlocks();
            for (DestroyBlockProgress destroyBlockProgress : this.damagedBlocks.values()) {
                Object object;
                Object object2 = destroyBlockProgress.getPosition();
                TileEntity tileEntity = this.theWorld.getTileEntity((BlockPos)object2);
                if (tileEntity instanceof TileEntityChest) {
                    object = (TileEntityChest)tileEntity;
                    if (((TileEntityChest)object).adjacentChestXNeg != null) {
                        object2 = ((BlockPos)object2).offset(EnumFacing.WEST);
                        tileEntity = this.theWorld.getTileEntity((BlockPos)object2);
                    } else if (((TileEntityChest)object).adjacentChestZNeg != null) {
                        object2 = ((BlockPos)object2).offset(EnumFacing.NORTH);
                        tileEntity = this.theWorld.getTileEntity((BlockPos)object2);
                    }
                }
                object = this.theWorld.getBlockState((BlockPos)object2).getBlock();
                if (tileEntity == null || !(object instanceof BlockChest) && !(object instanceof BlockEnderChest) && !(object instanceof BlockSign) && !(object instanceof BlockSkull)) continue;
                TileEntityRendererDispatcher.instance.renderTileEntity(tileEntity, f, destroyBlockProgress.getPartialBlockDamage());
            }
            this.postRenderDamagedBlocks();
            this.mc.entityRenderer.disableLightmap();
            this.mc.mcProfiler.endSection();
        }
    }

    public static void func_181561_a(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }

    private void generateSky2() {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        if (this.sky2VBO != null) {
            this.sky2VBO.deleteGlBuffers();
        }
        if (this.glSkyList2 >= 0) {
            GLAllocation.deleteDisplayLists(this.glSkyList2);
            this.glSkyList2 = -1;
        }
        if (this.vboEnabled) {
            this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(worldRenderer, -16.0f, true);
            worldRenderer.finishDrawing();
            worldRenderer.reset();
            this.sky2VBO.func_181722_a(worldRenderer.getByteBuffer());
        } else {
            this.glSkyList2 = GLAllocation.generateDisplayLists(1);
            GL11.glNewList((int)this.glSkyList2, (int)4864);
            this.renderSky(worldRenderer, -16.0f, true);
            tessellator.draw();
            GL11.glEndList();
        }
    }

    @Override
    public void onEntityAdded(Entity entity) {
    }

    @Override
    public void playSoundToNearExcept(EntityPlayer entityPlayer, String string, double d, double d2, double d3, float f, float f2) {
    }

    public void deleteAllDisplayLists() {
    }

    private void cleanupDamagedBlocks(Iterator<DestroyBlockProgress> iterator) {
        while (iterator.hasNext()) {
            DestroyBlockProgress destroyBlockProgress = iterator.next();
            int n = destroyBlockProgress.getCreationCloudUpdateTick();
            if (this.cloudTickCounter - n <= 400) continue;
            iterator.remove();
        }
    }

    private void renderCloudsFancy(float f, int n) {
        float f2;
        float f3;
        float f4;
        GlStateManager.disableCull();
        float f5 = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * (double)f);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        float f6 = 12.0f;
        float f7 = 4.0f;
        double d = (float)this.cloudTickCounter + f;
        double d2 = (this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * (double)f + d * (double)0.03f) / 12.0;
        double d3 = (this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * (double)f) / 12.0 + (double)0.33f;
        float f8 = this.theWorld.provider.getCloudHeight() - f5 + 0.33f;
        int n2 = MathHelper.floor_double(d2 / 2048.0);
        int n3 = MathHelper.floor_double(d3 / 2048.0);
        d2 -= (double)(n2 * 2048);
        d3 -= (double)(n3 * 2048);
        this.renderEngine.bindTexture(locationCloudsPng);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Vec3 vec3 = this.theWorld.getCloudColour(f);
        float f9 = (float)vec3.xCoord;
        float f10 = (float)vec3.yCoord;
        float f11 = (float)vec3.zCoord;
        if (n != 2) {
            f4 = (f9 * 30.0f + f10 * 59.0f + f11 * 11.0f) / 100.0f;
            f3 = (f9 * 30.0f + f10 * 70.0f) / 100.0f;
            f2 = (f9 * 30.0f + f11 * 70.0f) / 100.0f;
            f9 = f4;
            f10 = f3;
            f11 = f2;
        }
        f4 = f9 * 0.9f;
        f3 = f10 * 0.9f;
        f2 = f11 * 0.9f;
        float f12 = f9 * 0.7f;
        float f13 = f10 * 0.7f;
        float f14 = f11 * 0.7f;
        float f15 = f9 * 0.8f;
        float f16 = f10 * 0.8f;
        float f17 = f11 * 0.8f;
        float f18 = 0.00390625f;
        float f19 = (float)MathHelper.floor_double(d2) * 0.00390625f;
        float f20 = (float)MathHelper.floor_double(d3) * 0.00390625f;
        float f21 = (float)(d2 - (double)MathHelper.floor_double(d2));
        float f22 = (float)(d3 - (double)MathHelper.floor_double(d3));
        int n4 = 8;
        int n5 = 4;
        float f23 = 9.765625E-4f;
        GlStateManager.scale(12.0f, 1.0f, 12.0f);
        int n6 = 0;
        while (n6 < 2) {
            if (n6 == 0) {
                GlStateManager.colorMask(false, false, false, false);
            } else {
                switch (n) {
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
                    }
                }
            }
            int n7 = -3;
            while (n7 <= 4) {
                int n8 = -3;
                while (n8 <= 4) {
                    int n9;
                    worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                    float f24 = n7 * 8;
                    float f25 = n8 * 8;
                    float f26 = f24 - f21;
                    float f27 = f25 - f22;
                    if (f8 > -5.0f) {
                        worldRenderer.pos(f26 + 0.0f, f8 + 0.0f, f27 + 8.0f).tex((f24 + 0.0f) * 0.00390625f + f19, (f25 + 8.0f) * 0.00390625f + f20).color(f12, f13, f14, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        worldRenderer.pos(f26 + 8.0f, f8 + 0.0f, f27 + 8.0f).tex((f24 + 8.0f) * 0.00390625f + f19, (f25 + 8.0f) * 0.00390625f + f20).color(f12, f13, f14, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        worldRenderer.pos(f26 + 8.0f, f8 + 0.0f, f27 + 0.0f).tex((f24 + 8.0f) * 0.00390625f + f19, (f25 + 0.0f) * 0.00390625f + f20).color(f12, f13, f14, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        worldRenderer.pos(f26 + 0.0f, f8 + 0.0f, f27 + 0.0f).tex((f24 + 0.0f) * 0.00390625f + f19, (f25 + 0.0f) * 0.00390625f + f20).color(f12, f13, f14, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                    }
                    if (f8 <= 5.0f) {
                        worldRenderer.pos(f26 + 0.0f, f8 + 4.0f - 9.765625E-4f, f27 + 8.0f).tex((f24 + 0.0f) * 0.00390625f + f19, (f25 + 8.0f) * 0.00390625f + f20).color(f9, f10, f11, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldRenderer.pos(f26 + 8.0f, f8 + 4.0f - 9.765625E-4f, f27 + 8.0f).tex((f24 + 8.0f) * 0.00390625f + f19, (f25 + 8.0f) * 0.00390625f + f20).color(f9, f10, f11, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldRenderer.pos(f26 + 8.0f, f8 + 4.0f - 9.765625E-4f, f27 + 0.0f).tex((f24 + 8.0f) * 0.00390625f + f19, (f25 + 0.0f) * 0.00390625f + f20).color(f9, f10, f11, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldRenderer.pos(f26 + 0.0f, f8 + 4.0f - 9.765625E-4f, f27 + 0.0f).tex((f24 + 0.0f) * 0.00390625f + f19, (f25 + 0.0f) * 0.00390625f + f20).color(f9, f10, f11, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                    }
                    if (n7 > -1) {
                        n9 = 0;
                        while (n9 < 8) {
                            worldRenderer.pos(f26 + (float)n9 + 0.0f, f8 + 0.0f, f27 + 8.0f).tex((f24 + (float)n9 + 0.5f) * 0.00390625f + f19, (f25 + 8.0f) * 0.00390625f + f20).color(f4, f3, f2, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            worldRenderer.pos(f26 + (float)n9 + 0.0f, f8 + 4.0f, f27 + 8.0f).tex((f24 + (float)n9 + 0.5f) * 0.00390625f + f19, (f25 + 8.0f) * 0.00390625f + f20).color(f4, f3, f2, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            worldRenderer.pos(f26 + (float)n9 + 0.0f, f8 + 4.0f, f27 + 0.0f).tex((f24 + (float)n9 + 0.5f) * 0.00390625f + f19, (f25 + 0.0f) * 0.00390625f + f20).color(f4, f3, f2, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            worldRenderer.pos(f26 + (float)n9 + 0.0f, f8 + 0.0f, f27 + 0.0f).tex((f24 + (float)n9 + 0.5f) * 0.00390625f + f19, (f25 + 0.0f) * 0.00390625f + f20).color(f4, f3, f2, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            ++n9;
                        }
                    }
                    if (n7 <= 1) {
                        n9 = 0;
                        while (n9 < 8) {
                            worldRenderer.pos(f26 + (float)n9 + 1.0f - 9.765625E-4f, f8 + 0.0f, f27 + 8.0f).tex((f24 + (float)n9 + 0.5f) * 0.00390625f + f19, (f25 + 8.0f) * 0.00390625f + f20).color(f4, f3, f2, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            worldRenderer.pos(f26 + (float)n9 + 1.0f - 9.765625E-4f, f8 + 4.0f, f27 + 8.0f).tex((f24 + (float)n9 + 0.5f) * 0.00390625f + f19, (f25 + 8.0f) * 0.00390625f + f20).color(f4, f3, f2, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            worldRenderer.pos(f26 + (float)n9 + 1.0f - 9.765625E-4f, f8 + 4.0f, f27 + 0.0f).tex((f24 + (float)n9 + 0.5f) * 0.00390625f + f19, (f25 + 0.0f) * 0.00390625f + f20).color(f4, f3, f2, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            worldRenderer.pos(f26 + (float)n9 + 1.0f - 9.765625E-4f, f8 + 0.0f, f27 + 0.0f).tex((f24 + (float)n9 + 0.5f) * 0.00390625f + f19, (f25 + 0.0f) * 0.00390625f + f20).color(f4, f3, f2, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            ++n9;
                        }
                    }
                    if (n8 > -1) {
                        n9 = 0;
                        while (n9 < 8) {
                            worldRenderer.pos(f26 + 0.0f, f8 + 4.0f, f27 + (float)n9 + 0.0f).tex((f24 + 0.0f) * 0.00390625f + f19, (f25 + (float)n9 + 0.5f) * 0.00390625f + f20).color(f15, f16, f17, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            worldRenderer.pos(f26 + 8.0f, f8 + 4.0f, f27 + (float)n9 + 0.0f).tex((f24 + 8.0f) * 0.00390625f + f19, (f25 + (float)n9 + 0.5f) * 0.00390625f + f20).color(f15, f16, f17, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            worldRenderer.pos(f26 + 8.0f, f8 + 0.0f, f27 + (float)n9 + 0.0f).tex((f24 + 8.0f) * 0.00390625f + f19, (f25 + (float)n9 + 0.5f) * 0.00390625f + f20).color(f15, f16, f17, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            worldRenderer.pos(f26 + 0.0f, f8 + 0.0f, f27 + (float)n9 + 0.0f).tex((f24 + 0.0f) * 0.00390625f + f19, (f25 + (float)n9 + 0.5f) * 0.00390625f + f20).color(f15, f16, f17, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            ++n9;
                        }
                    }
                    if (n8 <= 1) {
                        n9 = 0;
                        while (n9 < 8) {
                            worldRenderer.pos(f26 + 0.0f, f8 + 4.0f, f27 + (float)n9 + 1.0f - 9.765625E-4f).tex((f24 + 0.0f) * 0.00390625f + f19, (f25 + (float)n9 + 0.5f) * 0.00390625f + f20).color(f15, f16, f17, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            worldRenderer.pos(f26 + 8.0f, f8 + 4.0f, f27 + (float)n9 + 1.0f - 9.765625E-4f).tex((f24 + 8.0f) * 0.00390625f + f19, (f25 + (float)n9 + 0.5f) * 0.00390625f + f20).color(f15, f16, f17, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            worldRenderer.pos(f26 + 8.0f, f8 + 0.0f, f27 + (float)n9 + 1.0f - 9.765625E-4f).tex((f24 + 8.0f) * 0.00390625f + f19, (f25 + (float)n9 + 0.5f) * 0.00390625f + f20).color(f15, f16, f17, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            worldRenderer.pos(f26 + 0.0f, f8 + 0.0f, f27 + (float)n9 + 1.0f - 9.765625E-4f).tex((f24 + 0.0f) * 0.00390625f + f19, (f25 + (float)n9 + 0.5f) * 0.00390625f + f20).color(f15, f16, f17, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            ++n9;
                        }
                    }
                    tessellator.draw();
                    ++n8;
                }
                ++n7;
            }
            ++n6;
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }

    protected Vector3f getViewVector(Entity entity, double d) {
        float f = (float)((double)entity.prevRotationPitch + (double)(entity.rotationPitch - entity.prevRotationPitch) * d);
        float f2 = (float)((double)entity.prevRotationYaw + (double)(entity.rotationYaw - entity.prevRotationYaw) * d);
        Minecraft.getMinecraft();
        if (Minecraft.gameSettings.thirdPersonView == 2) {
            f += 180.0f;
        }
        float f3 = MathHelper.cos(-f2 * ((float)Math.PI / 180) - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * ((float)Math.PI / 180) - (float)Math.PI);
        float f5 = -MathHelper.cos(-f * ((float)Math.PI / 180));
        float f6 = MathHelper.sin(-f * ((float)Math.PI / 180));
        return new Vector3f(f4 * f5, f6, f3 * f5);
    }

    @Override
    public void broadcastSound(int n, BlockPos blockPos, int n2) {
        switch (n) {
            case 1013: 
            case 1018: {
                if (this.mc.getRenderViewEntity() == null) break;
                double d = (double)blockPos.getX() - this.mc.getRenderViewEntity().posX;
                double d2 = (double)blockPos.getY() - this.mc.getRenderViewEntity().posY;
                double d3 = (double)blockPos.getZ() - this.mc.getRenderViewEntity().posZ;
                double d4 = Math.sqrt(d * d + d2 * d2 + d3 * d3);
                double d5 = this.mc.getRenderViewEntity().posX;
                double d6 = this.mc.getRenderViewEntity().posY;
                double d7 = this.mc.getRenderViewEntity().posZ;
                if (d4 > 0.0) {
                    d5 += d / d4 * 2.0;
                    d6 += d2 / d4 * 2.0;
                    d7 += d3 / d4 * 2.0;
                }
                if (n == 1013) {
                    this.theWorld.playSound(d5, d6, d7, "mob.wither.spawn", 1.0f, 1.0f, false);
                    break;
                }
                this.theWorld.playSound(d5, d6, d7, "mob.enderdragon.end", 5.0f, 1.0f, false);
            }
        }
    }

    private boolean isPositionInRenderChunk(BlockPos blockPos, RenderChunk renderChunk) {
        BlockPos blockPos2 = renderChunk.getPosition();
        return MathHelper.abs_int(blockPos.getX() - blockPos2.getX()) > 16 ? false : (MathHelper.abs_int(blockPos.getY() - blockPos2.getY()) > 16 ? false : MathHelper.abs_int(blockPos.getZ() - blockPos2.getZ()) <= 16);
    }

    private void postRenderDamagedBlocks() {
        GlStateManager.disableAlpha();
        GlStateManager.doPolygonOffset(0.0f, 0.0f);
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    @Override
    public void playRecord(String string, BlockPos blockPos) {
        ISound iSound = this.mapSoundPositions.get(blockPos);
        if (iSound != null) {
            this.mc.getSoundHandler().stopSound(iSound);
            this.mapSoundPositions.remove(blockPos);
        }
        if (string != null) {
            ItemRecord itemRecord = ItemRecord.getRecord(string);
            if (itemRecord != null) {
                this.mc.ingameGUI.setRecordPlayingMessage(itemRecord.getRecordNameLocal());
            }
            PositionedSoundRecord positionedSoundRecord = PositionedSoundRecord.create(new ResourceLocation(string), blockPos.getX(), blockPos.getY(), blockPos.getZ());
            this.mapSoundPositions.put(blockPos, positionedSoundRecord);
            this.mc.getSoundHandler().playSound(positionedSoundRecord);
        }
    }

    private void renderStars(WorldRenderer worldRenderer) {
        Random random = new Random(10842L);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        int n = 0;
        while (n < 1500) {
            double d = random.nextFloat() * 2.0f - 1.0f;
            double d2 = random.nextFloat() * 2.0f - 1.0f;
            double d3 = random.nextFloat() * 2.0f - 1.0f;
            double d4 = 0.15f + random.nextFloat() * 0.1f;
            double d5 = d * d + d2 * d2 + d3 * d3;
            if (d5 < 1.0 && d5 > 0.01) {
                d5 = 1.0 / Math.sqrt(d5);
                double d6 = (d *= d5) * 100.0;
                double d7 = (d2 *= d5) * 100.0;
                double d8 = (d3 *= d5) * 100.0;
                double d9 = Math.atan2(d, d3);
                double d10 = Math.sin(d9);
                double d11 = Math.cos(d9);
                double d12 = Math.atan2(Math.sqrt(d * d + d3 * d3), d2);
                double d13 = Math.sin(d12);
                double d14 = Math.cos(d12);
                double d15 = random.nextDouble() * Math.PI * 2.0;
                double d16 = Math.sin(d15);
                double d17 = Math.cos(d15);
                int n2 = 0;
                while (n2 < 4) {
                    double d18 = 0.0;
                    double d19 = (double)((n2 & 2) - 1) * d4;
                    double d20 = (double)((n2 + 1 & 2) - 1) * d4;
                    double d21 = 0.0;
                    double d22 = d19 * d17 - d20 * d16;
                    double d23 = d20 * d17 + d19 * d16;
                    double d24 = d22 * d13 + 0.0 * d14;
                    double d25 = 0.0 * d13 - d22 * d14;
                    double d26 = d25 * d10 - d23 * d11;
                    double d27 = d23 * d10 + d25 * d11;
                    worldRenderer.pos(d6 + d26, d7 + d24, d8 + d27).endVertex();
                    ++n2;
                }
            }
            ++n;
        }
    }

    protected boolean isRenderEntityOutlines() {
        return this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && Minecraft.thePlayer != null && Minecraft.thePlayer.isSpectator() && Minecraft.gameSettings.keyBindSpectatorOutlines.isKeyDown();
    }

    class ContainerLocalRenderInformation {
        final int counter;
        final Set<EnumFacing> setFacing = EnumSet.noneOf(EnumFacing.class);
        final RenderChunk renderChunk;
        final EnumFacing facing;

        private ContainerLocalRenderInformation(RenderChunk renderChunk, EnumFacing enumFacing, int n) {
            this.renderChunk = renderChunk;
            this.facing = enumFacing;
            this.counter = n;
        }
    }
}

