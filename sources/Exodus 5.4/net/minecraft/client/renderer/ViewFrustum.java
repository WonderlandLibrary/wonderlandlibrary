/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ViewFrustum {
    protected int countChunksY;
    public RenderChunk[] renderChunks;
    protected int countChunksZ;
    protected int countChunksX;
    protected final World world;
    protected final RenderGlobal renderGlobal;

    protected void setCountChunksXYZ(int n) {
        int n2;
        this.countChunksX = n2 = n * 2 + 1;
        this.countChunksY = 16;
        this.countChunksZ = n2;
    }

    public ViewFrustum(World world, int n, RenderGlobal renderGlobal, IRenderChunkFactory iRenderChunkFactory) {
        this.renderGlobal = renderGlobal;
        this.world = world;
        this.setCountChunksXYZ(n);
        this.createRenderChunks(iRenderChunkFactory);
    }

    public void deleteGlResources() {
        RenderChunk[] renderChunkArray = this.renderChunks;
        int n = this.renderChunks.length;
        int n2 = 0;
        while (n2 < n) {
            RenderChunk renderChunk = renderChunkArray[n2];
            renderChunk.deleteGlResources();
            ++n2;
        }
    }

    private int func_178157_a(int n, int n2, int n3) {
        int n4 = n3 * 16;
        int n5 = n4 - n + n2 / 2;
        if (n5 < 0) {
            n5 -= n2 - 1;
        }
        return n4 - n5 / n2 * n2;
    }

    public void markBlocksForUpdate(int n, int n2, int n3, int n4, int n5, int n6) {
        int n7 = MathHelper.bucketInt(n, 16);
        int n8 = MathHelper.bucketInt(n2, 16);
        int n9 = MathHelper.bucketInt(n3, 16);
        int n10 = MathHelper.bucketInt(n4, 16);
        int n11 = MathHelper.bucketInt(n5, 16);
        int n12 = MathHelper.bucketInt(n6, 16);
        int n13 = n7;
        while (n13 <= n10) {
            int n14 = n13 % this.countChunksX;
            if (n14 < 0) {
                n14 += this.countChunksX;
            }
            int n15 = n8;
            while (n15 <= n11) {
                int n16 = n15 % this.countChunksY;
                if (n16 < 0) {
                    n16 += this.countChunksY;
                }
                int n17 = n9;
                while (n17 <= n12) {
                    int n18 = n17 % this.countChunksZ;
                    if (n18 < 0) {
                        n18 += this.countChunksZ;
                    }
                    int n19 = (n18 * this.countChunksY + n16) * this.countChunksX + n14;
                    RenderChunk renderChunk = this.renderChunks[n19];
                    renderChunk.setNeedsUpdate(true);
                    ++n17;
                }
                ++n15;
            }
            ++n13;
        }
    }

    protected void createRenderChunks(IRenderChunkFactory iRenderChunkFactory) {
        int n = this.countChunksX * this.countChunksY * this.countChunksZ;
        this.renderChunks = new RenderChunk[n];
        int n2 = 0;
        int n3 = 0;
        while (n3 < this.countChunksX) {
            int n4 = 0;
            while (n4 < this.countChunksY) {
                int n5 = 0;
                while (n5 < this.countChunksZ) {
                    int n6 = (n5 * this.countChunksY + n4) * this.countChunksX + n3;
                    BlockPos blockPos = new BlockPos(n3 * 16, n4 * 16, n5 * 16);
                    this.renderChunks[n6] = iRenderChunkFactory.makeRenderChunk(this.world, this.renderGlobal, blockPos, n2++);
                    ++n5;
                }
                ++n4;
            }
            ++n3;
        }
    }

    public void updateChunkPositions(double d, double d2) {
        int n = MathHelper.floor_double(d) - 8;
        int n2 = MathHelper.floor_double(d2) - 8;
        int n3 = this.countChunksX * 16;
        int n4 = 0;
        while (n4 < this.countChunksX) {
            int n5 = this.func_178157_a(n, n3, n4);
            int n6 = 0;
            while (n6 < this.countChunksZ) {
                int n7 = this.func_178157_a(n2, n3, n6);
                int n8 = 0;
                while (n8 < this.countChunksY) {
                    int n9 = n8 * 16;
                    BlockPos blockPos = new BlockPos(n5, n9, n7);
                    RenderChunk renderChunk = this.renderChunks[(n6 * this.countChunksY + n8) * this.countChunksX + n4];
                    if (!blockPos.equals(renderChunk.getPosition())) {
                        renderChunk.setPosition(blockPos);
                    }
                    ++n8;
                }
                ++n6;
            }
            ++n4;
        }
    }

    protected RenderChunk getRenderChunk(BlockPos blockPos) {
        int n = MathHelper.bucketInt(blockPos.getX(), 16);
        int n2 = MathHelper.bucketInt(blockPos.getY(), 16);
        int n3 = MathHelper.bucketInt(blockPos.getZ(), 16);
        if (n2 >= 0 && n2 < this.countChunksY) {
            if ((n %= this.countChunksX) < 0) {
                n += this.countChunksX;
            }
            if ((n3 %= this.countChunksZ) < 0) {
                n3 += this.countChunksZ;
            }
            int n4 = (n3 * this.countChunksY + n2) * this.countChunksX + n;
            return this.renderChunks[n4];
        }
        return null;
    }
}

