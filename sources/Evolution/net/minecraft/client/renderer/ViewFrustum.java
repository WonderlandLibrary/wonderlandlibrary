package net.minecraft.client.renderer;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.src.Config;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.optifine.render.VboRegion;

public class ViewFrustum
{
    protected final RenderGlobal renderGlobal;
    protected final World world;
    protected int countChunksY;
    protected int countChunksX;
    protected int countChunksZ;
    public RenderChunk[] renderChunks;
    private Map<ChunkPos, VboRegion[]> mapVboRegions = new HashMap<ChunkPos, VboRegion[]>();

    public ViewFrustum(World worldIn, int renderDistanceChunks, RenderGlobal renderGlobalIn, IRenderChunkFactory renderChunkFactory)
    {
        this.renderGlobal = renderGlobalIn;
        this.world = worldIn;
        this.setCountChunksXYZ(renderDistanceChunks);
        this.createRenderChunks(renderChunkFactory);
    }

    protected void createRenderChunks(IRenderChunkFactory renderChunkFactory)
    {
        int i = this.countChunksX * this.countChunksY * this.countChunksZ;
        this.renderChunks = new RenderChunk[i];
        int j = 0;

        for (int k = 0; k < this.countChunksX; ++k)
        {
            for (int l = 0; l < this.countChunksY; ++l)
            {
                for (int i1 = 0; i1 < this.countChunksZ; ++i1)
                {
                    int j1 = (i1 * this.countChunksY + l) * this.countChunksX + k;
                    this.renderChunks[j1] = renderChunkFactory.create(this.world, this.renderGlobal, j++);
                    this.renderChunks[j1].setPosition(k * 16, l * 16, i1 * 16);

                    if (Config.isVbo() && Config.isRenderRegions())
                    {
                        this.updateVboRegion(this.renderChunks[j1]);
                    }
                }
            }
        }

        for (int k1 = 0; k1 < this.renderChunks.length; ++k1)
        {
            RenderChunk renderchunk1 = this.renderChunks[k1];

            for (int l1 = 0; l1 < EnumFacing.VALUES.length; ++l1)
            {
                EnumFacing enumfacing = EnumFacing.VALUES[l1];
                BlockPos blockpos = renderchunk1.getBlockPosOffset16(enumfacing);
                RenderChunk renderchunk = this.getRenderChunk(blockpos);
                renderchunk1.setRenderChunkNeighbour(enumfacing, renderchunk);
            }
        }
    }

    public void deleteGlResources()
    {
        for (RenderChunk renderchunk : this.renderChunks)
        {
            renderchunk.deleteGlResources();
        }

        this.deleteVboRegions();
    }

    protected void setCountChunksXYZ(int renderDistanceChunks)
    {
        int i = renderDistanceChunks * 2 + 1;
        this.countChunksX = i;
        this.countChunksY = 16;
        this.countChunksZ = i;
    }

    public void updateChunkPositions(double viewEntityX, double viewEntityZ)
    {
        int i = MathHelper.floor(viewEntityX) - 8;
        int j = MathHelper.floor(viewEntityZ) - 8;
        int k = this.countChunksX * 16;

        for (int l = 0; l < this.countChunksX; ++l)
        {
            int i1 = this.getBaseCoordinate(i, k, l);

            for (int j1 = 0; j1 < this.countChunksZ; ++j1)
            {
                int k1 = this.getBaseCoordinate(j, k, j1);

                for (int l1 = 0; l1 < this.countChunksY; ++l1)
                {
                    int i2 = l1 * 16;
                    RenderChunk renderchunk = this.renderChunks[(j1 * this.countChunksY + l1) * this.countChunksX + l];
                    renderchunk.setPosition(i1, i2, k1);
                }
            }
        }
    }

    private int getBaseCoordinate(int p_178157_1_, int p_178157_2_, int p_178157_3_)
    {
        int i = p_178157_3_ * 16;
        int j = i - p_178157_1_ + p_178157_2_ / 2;

        if (j < 0)
        {
            j -= p_178157_2_ - 1;
        }

        return i - j / p_178157_2_ * p_178157_2_;
    }

    public void markBlocksForUpdate(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean updateImmediately)
    {
        int i = MathHelper.intFloorDiv(minX, 16);
        int j = MathHelper.intFloorDiv(minY, 16);
        int k = MathHelper.intFloorDiv(minZ, 16);
        int l = MathHelper.intFloorDiv(maxX, 16);
        int i1 = MathHelper.intFloorDiv(maxY, 16);
        int j1 = MathHelper.intFloorDiv(maxZ, 16);

        for (int k1 = i; k1 <= l; ++k1)
        {
            int l1 = k1 % this.countChunksX;

            if (l1 < 0)
            {
                l1 += this.countChunksX;
            }

            for (int i2 = j; i2 <= i1; ++i2)
            {
                int j2 = i2 % this.countChunksY;

                if (j2 < 0)
                {
                    j2 += this.countChunksY;
                }

                for (int k2 = k; k2 <= j1; ++k2)
                {
                    int l2 = k2 % this.countChunksZ;

                    if (l2 < 0)
                    {
                        l2 += this.countChunksZ;
                    }

                    int i3 = (l2 * this.countChunksY + j2) * this.countChunksX + l1;
                    RenderChunk renderchunk = this.renderChunks[i3];
                    renderchunk.setNeedsUpdate(updateImmediately);
                }
            }
        }
    }

    @Nullable
    public RenderChunk getRenderChunk(BlockPos pos)
    {
        int i = pos.getX() >> 4;
        int j = pos.getY() >> 4;
        int k = pos.getZ() >> 4;

        if (j >= 0 && j < this.countChunksY)
        {
            i = i % this.countChunksX;

            if (i < 0)
            {
                i += this.countChunksX;
            }

            k = k % this.countChunksZ;

            if (k < 0)
            {
                k += this.countChunksZ;
            }

            int l = (k * this.countChunksY + j) * this.countChunksX + i;
            return this.renderChunks[l];
        }
        else
        {
            return null;
        }
    }

    private void updateVboRegion(RenderChunk p_updateVboRegion_1_)
    {
        BlockPos blockpos = p_updateVboRegion_1_.getPosition();
        int i = blockpos.getX() >> 8 << 8;
        int j = blockpos.getZ() >> 8 << 8;
        ChunkPos chunkpos = new ChunkPos(i, j);
        BlockRenderLayer[] ablockrenderlayer = BlockRenderLayer.values();
        VboRegion[] avboregion = this.mapVboRegions.get(chunkpos);

        if (avboregion == null)
        {
            avboregion = new VboRegion[ablockrenderlayer.length];

            for (int k = 0; k < ablockrenderlayer.length; ++k)
            {
                avboregion[k] = new VboRegion(ablockrenderlayer[k]);
            }

            this.mapVboRegions.put(chunkpos, avboregion);
        }

        for (int l = 0; l < ablockrenderlayer.length; ++l)
        {
            VboRegion vboregion = avboregion[l];

            if (vboregion != null)
            {
                p_updateVboRegion_1_.getVertexBufferByLayer(l).setVboRegion(vboregion);
            }
        }
    }

    public void deleteVboRegions()
    {
        for (ChunkPos chunkpos : this.mapVboRegions.keySet())
        {
            VboRegion[] avboregion = this.mapVboRegions.get(chunkpos);

            for (int i = 0; i < avboregion.length; ++i)
            {
                VboRegion vboregion = avboregion[i];

                if (vboregion != null)
                {
                    vboregion.deleteGlBuffers();
                }

                avboregion[i] = null;
            }
        }

        this.mapVboRegions.clear();
    }
}
