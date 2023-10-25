package net.minecraft.client.renderer;

import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.src.Config;

public class VertexBufferUploader extends WorldVertexBufferUploader
{
    private VertexBuffer vertexBuffer;

    public void draw(BufferBuilder bufferBuilderIn)
    {
        if (bufferBuilderIn.getDrawMode() == 7 && Config.isQuadsToTriangles())
        {
            bufferBuilderIn.quadsToTriangles();
            this.vertexBuffer.setDrawMode(bufferBuilderIn.getDrawMode());
        }

        this.vertexBuffer.bufferData(bufferBuilderIn.getByteBuffer());
        bufferBuilderIn.reset();
    }

    public void setVertexBuffer(VertexBuffer vertexBufferIn)
    {
        this.vertexBuffer = vertexBufferIn;
    }
}
