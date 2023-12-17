// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import java.util.Iterator;
import net.minecraft.util.BlockRenderLayer;
import javax.annotation.Nullable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.src.Config;
import java.util.HashMap;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.optifine.render.VboRegion;
import net.minecraft.util.math.ChunkPos;
import java.util.Map;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.world.World;

public class ViewFrustum
{
    protected final RenderGlobal renderGlobal;
    protected final World world;
    protected int countChunksY;
    protected int countChunksX;
    protected int countChunksZ;
    public RenderChunk[] renderChunks;
    private Map<ChunkPos, VboRegion[]> mapVboRegions;
    
    public ViewFrustum(final World worldIn, final int renderDistanceChunks, final RenderGlobal renderGlobalIn, final IRenderChunkFactory renderChunkFactory) {
        this.mapVboRegions = new HashMap<ChunkPos, VboRegion[]>();
        this.renderGlobal = renderGlobalIn;
        this.world = worldIn;
        this.setCountChunksXYZ(renderDistanceChunks);
        this.createRenderChunks(renderChunkFactory);
    }
    
    protected void createRenderChunks(final IRenderChunkFactory renderChunkFactory) {
        final int i = this.countChunksX * this.countChunksY * this.countChunksZ;
        this.renderChunks = new RenderChunk[i];
        int j = 0;
        for (int k = 0; k < this.countChunksX; ++k) {
            for (int l = 0; l < this.countChunksY; ++l) {
                for (int i2 = 0; i2 < this.countChunksZ; ++i2) {
                    final int j2 = (i2 * this.countChunksY + l) * this.countChunksX + k;
                    (this.renderChunks[j2] = renderChunkFactory.create(this.world, this.renderGlobal, j++)).setPosition(k * 16, l * 16, i2 * 16);
                    if (Config.isVbo() && Config.isRenderRegions()) {
                        this.updateVboRegion(this.renderChunks[j2]);
                    }
                }
            }
        }
        for (int k2 = 0; k2 < this.renderChunks.length; ++k2) {
            final RenderChunk renderchunk1 = this.renderChunks[k2];
            for (int l2 = 0; l2 < EnumFacing.VALUES.length; ++l2) {
                final EnumFacing enumfacing = EnumFacing.VALUES[l2];
                final BlockPos blockpos = renderchunk1.getBlockPosOffset16(enumfacing);
                final RenderChunk renderchunk2 = this.getRenderChunk(blockpos);
                renderchunk1.setRenderChunkNeighbour(enumfacing, renderchunk2);
            }
        }
    }
    
    public void deleteGlResources() {
        for (final RenderChunk renderchunk : this.renderChunks) {
            renderchunk.deleteGlResources();
        }
        this.deleteVboRegions();
    }
    
    protected void setCountChunksXYZ(final int renderDistanceChunks) {
        final int i = renderDistanceChunks * 2 + 1;
        this.countChunksX = i;
        this.countChunksY = 16;
        this.countChunksZ = i;
    }
    
    public void updateChunkPositions(final double viewEntityX, final double viewEntityZ) {
        final int i = MathHelper.floor(viewEntityX) - 8;
        final int j = MathHelper.floor(viewEntityZ) - 8;
        final int k = this.countChunksX * 16;
        for (int l = 0; l < this.countChunksX; ++l) {
            final int i2 = this.getBaseCoordinate(i, k, l);
            for (int j2 = 0; j2 < this.countChunksZ; ++j2) {
                final int k2 = this.getBaseCoordinate(j, k, j2);
                for (int l2 = 0; l2 < this.countChunksY; ++l2) {
                    final int i3 = l2 * 16;
                    final RenderChunk renderchunk = this.renderChunks[(j2 * this.countChunksY + l2) * this.countChunksX + l];
                    renderchunk.setPosition(i2, i3, k2);
                }
            }
        }
    }
    
    private int getBaseCoordinate(final int p_178157_1_, final int p_178157_2_, final int p_178157_3_) {
        final int i = p_178157_3_ * 16;
        int j = i - p_178157_1_ + p_178157_2_ / 2;
        if (j < 0) {
            j -= p_178157_2_ - 1;
        }
        return i - j / p_178157_2_ * p_178157_2_;
    }
    
    public void markBlocksForUpdate(final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ, final boolean updateImmediately) {
        final int i = MathHelper.intFloorDiv(minX, 16);
        final int j = MathHelper.intFloorDiv(minY, 16);
        final int k = MathHelper.intFloorDiv(minZ, 16);
        final int l = MathHelper.intFloorDiv(maxX, 16);
        final int i2 = MathHelper.intFloorDiv(maxY, 16);
        final int j2 = MathHelper.intFloorDiv(maxZ, 16);
        for (int k2 = i; k2 <= l; ++k2) {
            int l2 = k2 % this.countChunksX;
            if (l2 < 0) {
                l2 += this.countChunksX;
            }
            for (int i3 = j; i3 <= i2; ++i3) {
                int j3 = i3 % this.countChunksY;
                if (j3 < 0) {
                    j3 += this.countChunksY;
                }
                for (int k3 = k; k3 <= j2; ++k3) {
                    int l3 = k3 % this.countChunksZ;
                    if (l3 < 0) {
                        l3 += this.countChunksZ;
                    }
                    final int i4 = (l3 * this.countChunksY + j3) * this.countChunksX + l2;
                    final RenderChunk renderchunk = this.renderChunks[i4];
                    renderchunk.setNeedsUpdate(updateImmediately);
                }
            }
        }
    }
    
    @Nullable
    public RenderChunk getRenderChunk(final BlockPos pos) {
        int i = pos.getX() >> 4;
        final int j = pos.getY() >> 4;
        int k = pos.getZ() >> 4;
        if (j >= 0 && j < this.countChunksY) {
            i %= this.countChunksX;
            if (i < 0) {
                i += this.countChunksX;
            }
            k %= this.countChunksZ;
            if (k < 0) {
                k += this.countChunksZ;
            }
            final int l = (k * this.countChunksY + j) * this.countChunksX + i;
            return this.renderChunks[l];
        }
        return null;
    }
    
    private void updateVboRegion(final RenderChunk p_updateVboRegion_1_) {
        final BlockPos blockpos = p_updateVboRegion_1_.getPosition();
        final int i = blockpos.getX() >> 8 << 8;
        final int j = blockpos.getZ() >> 8 << 8;
        final ChunkPos chunkpos = new ChunkPos(i, j);
        final BlockRenderLayer[] ablockrenderlayer = BlockRenderLayer.values();
        VboRegion[] avboregion = this.mapVboRegions.get(chunkpos);
        if (avboregion == null) {
            avboregion = new VboRegion[ablockrenderlayer.length];
            for (int k = 0; k < ablockrenderlayer.length; ++k) {
                avboregion[k] = new VboRegion(ablockrenderlayer[k]);
            }
            this.mapVboRegions.put(chunkpos, avboregion);
        }
        for (int l = 0; l < ablockrenderlayer.length; ++l) {
            final VboRegion vboregion = avboregion[l];
            if (vboregion != null) {
                p_updateVboRegion_1_.getVertexBufferByLayer(l).setVboRegion(vboregion);
            }
        }
    }
    
    public void deleteVboRegions() {
        for (final ChunkPos chunkpos : this.mapVboRegions.keySet()) {
            final VboRegion[] avboregion = this.mapVboRegions.get(chunkpos);
            for (int i = 0; i < avboregion.length; ++i) {
                final VboRegion vboregion = avboregion[i];
                if (vboregion != null) {
                    vboregion.deleteGlBuffers();
                }
                avboregion[i] = null;
            }
        }
        this.mapVboRegions.clear();
    }
}
