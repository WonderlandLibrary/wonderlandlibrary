/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer.vertex;

import java.nio.ByteBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;

public class VertexBuffer {
    private int glBufferId;
    private int count;
    private final VertexFormat vertexFormat;

    public void func_181722_a(ByteBuffer byteBuffer) {
        this.bindBuffer();
        OpenGlHelper.glBufferData(OpenGlHelper.GL_ARRAY_BUFFER, byteBuffer, 35044);
        this.unbindBuffer();
        this.count = byteBuffer.limit() / this.vertexFormat.getNextOffset();
    }

    public void unbindBuffer() {
        OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
    }

    public void drawArrays(int n) {
        GL11.glDrawArrays((int)n, (int)0, (int)this.count);
    }

    public void deleteGlBuffers() {
        if (this.glBufferId >= 0) {
            OpenGlHelper.glDeleteBuffers(this.glBufferId);
            this.glBufferId = -1;
        }
    }

    public void bindBuffer() {
        OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, this.glBufferId);
    }

    public VertexBuffer(VertexFormat vertexFormat) {
        this.vertexFormat = vertexFormat;
        this.glBufferId = OpenGlHelper.glGenBuffers();
    }
}

