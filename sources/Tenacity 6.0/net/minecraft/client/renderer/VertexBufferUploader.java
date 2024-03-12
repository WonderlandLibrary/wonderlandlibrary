// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.src.Config;
import net.minecraft.client.renderer.vertex.VertexBuffer;

public class VertexBufferUploader extends WorldVertexBufferUploader
{
    private VertexBuffer vertexBuffer;
    
    public VertexBufferUploader() {
        this.vertexBuffer = null;
    }
    
    @Override
    public void draw(final WorldRenderer p_181679_1_) {
        if (p_181679_1_.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
            p_181679_1_.quadsToTriangles();
            this.vertexBuffer.setDrawMode(p_181679_1_.getDrawMode());
        }
        this.vertexBuffer.bufferData(p_181679_1_.getByteBuffer());
        p_181679_1_.reset();
    }
    
    public void setVertexBuffer(final VertexBuffer vertexBufferIn) {
        this.vertexBuffer = vertexBufferIn;
    }
}
