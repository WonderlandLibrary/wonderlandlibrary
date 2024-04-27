package me.aquavit.liquidsense.utils.render;

import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class SafeVertexBuffer extends VertexBuffer {

    public SafeVertexBuffer(VertexFormat vertexFormatIn) {
        super(vertexFormatIn);
    }

    @Override
    protected void finalize() throws Throwable {
        this.deleteGlBuffers();
    }
}
