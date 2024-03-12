// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.src.Config;
import net.minecraft.util.EnumWorldBlockLayer;
import java.nio.IntBuffer;

public class RenderList extends ChunkRenderContainer
{
    private double viewEntityX;
    private double viewEntityY;
    private double viewEntityZ;
    IntBuffer bufferLists;
    
    public RenderList() {
        this.bufferLists = GLAllocation.createDirectIntBuffer(16);
    }
    
    @Override
    public void renderChunkLayer(final EnumWorldBlockLayer layer) {
        if (this.initialized) {
            if (!Config.isRenderRegions()) {
                for (final RenderChunk renderchunk1 : this.renderChunks) {
                    final ListedRenderChunk listedrenderchunk1 = (ListedRenderChunk)renderchunk1;
                    GlStateManager.pushMatrix();
                    this.preRenderChunk(renderchunk1);
                    GL11.glCallList(listedrenderchunk1.getDisplayList(layer, listedrenderchunk1.getCompiledChunk()));
                    GlStateManager.popMatrix();
                }
            }
            else {
                int i = Integer.MIN_VALUE;
                int j = Integer.MIN_VALUE;
                for (final RenderChunk renderchunk2 : this.renderChunks) {
                    final ListedRenderChunk listedrenderchunk2 = (ListedRenderChunk)renderchunk2;
                    if (i != renderchunk2.regionX || j != renderchunk2.regionZ) {
                        if (this.bufferLists.position() > 0) {
                            this.drawRegion(i, j, this.bufferLists);
                        }
                        i = renderchunk2.regionX;
                        j = renderchunk2.regionZ;
                    }
                    if (this.bufferLists.position() >= this.bufferLists.capacity()) {
                        final IntBuffer intbuffer = GLAllocation.createDirectIntBuffer(this.bufferLists.capacity() * 2);
                        this.bufferLists.flip();
                        intbuffer.put(this.bufferLists);
                        this.bufferLists = intbuffer;
                    }
                    this.bufferLists.put(listedrenderchunk2.getDisplayList(layer, listedrenderchunk2.getCompiledChunk()));
                }
                if (this.bufferLists.position() > 0) {
                    this.drawRegion(i, j, this.bufferLists);
                }
            }
            if (Config.isMultiTexture()) {
                GlStateManager.bindCurrentTexture();
            }
            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }
    
    @Override
    public void initialize(final double viewEntityXIn, final double viewEntityYIn, final double viewEntityZIn) {
        super.initialize(this.viewEntityX = viewEntityXIn, this.viewEntityY = viewEntityYIn, this.viewEntityZ = viewEntityZIn);
    }
    
    private void drawRegion(final int p_drawRegion_1_, final int p_drawRegion_2_, final IntBuffer p_drawRegion_3_) {
        GlStateManager.pushMatrix();
        this.preRenderRegion(p_drawRegion_1_, 0, p_drawRegion_2_);
        p_drawRegion_3_.flip();
        GlStateManager.callLists(p_drawRegion_3_);
        p_drawRegion_3_.clear();
        GlStateManager.popMatrix();
    }
    
    public void preRenderRegion(final int p_preRenderRegion_1_, final int p_preRenderRegion_2_, final int p_preRenderRegion_3_) {
        GlStateManager.translate((float)(p_preRenderRegion_1_ - this.viewEntityX), (float)(p_preRenderRegion_2_ - this.viewEntityY), (float)(p_preRenderRegion_3_ - this.viewEntityZ));
    }
}
