// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import org.lwjgl.opengl.GL11;
import net.optifine.shaders.ShadersRender;
import net.optifine.render.VboRegion;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import java.util.Iterator;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.src.Config;
import net.minecraft.util.EnumWorldBlockLayer;

public class VboRenderList extends ChunkRenderContainer
{
    private double viewEntityX;
    private double viewEntityY;
    private double viewEntityZ;
    
    @Override
    public void renderChunkLayer(final EnumWorldBlockLayer layer) {
        if (this.initialized) {
            if (!Config.isRenderRegions()) {
                for (final RenderChunk renderchunk1 : this.renderChunks) {
                    final VertexBuffer vertexbuffer1 = renderchunk1.getVertexBufferByLayer(layer.ordinal());
                    GlStateManager.pushMatrix();
                    this.preRenderChunk(renderchunk1);
                    renderchunk1.multModelviewMatrix();
                    vertexbuffer1.bindBuffer();
                    this.setupArrayPointers();
                    vertexbuffer1.drawArrays(7);
                    GlStateManager.popMatrix();
                }
            }
            else {
                int i = Integer.MIN_VALUE;
                int j = Integer.MIN_VALUE;
                VboRegion vboregion = null;
                for (final RenderChunk renderchunk2 : this.renderChunks) {
                    final VertexBuffer vertexbuffer2 = renderchunk2.getVertexBufferByLayer(layer.ordinal());
                    final VboRegion vboregion2 = vertexbuffer2.getVboRegion();
                    if (vboregion2 != vboregion || i != renderchunk2.regionX || j != renderchunk2.regionZ) {
                        if (vboregion != null) {
                            this.drawRegion(i, j, vboregion);
                        }
                        i = renderchunk2.regionX;
                        j = renderchunk2.regionZ;
                        vboregion = vboregion2;
                    }
                    vertexbuffer2.drawArrays(7);
                }
                if (vboregion != null) {
                    this.drawRegion(i, j, vboregion);
                }
            }
            OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }
    
    public void setupArrayPointers() {
        if (Config.isShaders()) {
            ShadersRender.setupArrayPointersVbo();
        }
        else {
            GL11.glVertexPointer(3, 5126, 28, 0L);
            GL11.glColorPointer(4, 5121, 28, 12L);
            GL11.glTexCoordPointer(2, 5126, 28, 16L);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glTexCoordPointer(2, 5122, 28, 24L);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
        }
    }
    
    @Override
    public void initialize(final double viewEntityXIn, final double viewEntityYIn, final double viewEntityZIn) {
        super.initialize(this.viewEntityX = viewEntityXIn, this.viewEntityY = viewEntityYIn, this.viewEntityZ = viewEntityZIn);
    }
    
    private void drawRegion(final int p_drawRegion_1_, final int p_drawRegion_2_, final VboRegion p_drawRegion_3_) {
        GlStateManager.pushMatrix();
        this.preRenderRegion(p_drawRegion_1_, 0, p_drawRegion_2_);
        p_drawRegion_3_.finishDraw(this);
        GlStateManager.popMatrix();
    }
    
    public void preRenderRegion(final int p_preRenderRegion_1_, final int p_preRenderRegion_2_, final int p_preRenderRegion_3_) {
        GlStateManager.translate((float)(p_preRenderRegion_1_ - this.viewEntityX), (float)(p_preRenderRegion_2_ - this.viewEntityY), (float)(p_preRenderRegion_3_ - this.viewEntityZ));
    }
}
