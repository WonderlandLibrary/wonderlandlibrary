// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.src.Config;
import net.minecraft.client.renderer.vertex.VertexBuffer;

public class VertexBufferUploader extends WorldVertexBufferUploader
{
    private VertexBuffer vertexBuffer;
    
    @Override
    public void draw(final BufferBuilder bufferBuilderIn) {
        if (bufferBuilderIn.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
            bufferBuilderIn.quadsToTriangles();
            this.vertexBuffer.setDrawMode(bufferBuilderIn.getDrawMode());
        }
        this.vertexBuffer.bufferData(bufferBuilderIn.getByteBuffer());
        bufferBuilderIn.reset();
    }
    
    public void setVertexBuffer(final VertexBuffer vertexBufferIn) {
        this.vertexBuffer = vertexBufferIn;
    }
}
