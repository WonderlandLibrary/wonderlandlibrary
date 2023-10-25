/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.nio.Buffer;
import java.util.Arrays;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;

class BaseReferences {
    int elementArrayBuffer;
    int arrayBuffer;
    final Buffer[] glVertexAttribPointer_buffer;
    final Buffer[] glTexCoordPointer_buffer;
    int glClientActiveTexture;
    int vertexArrayObject;
    int pixelPackBuffer;
    int pixelUnpackBuffer;
    int indirectBuffer;

    BaseReferences(ContextCapabilities caps) {
        int max_vertex_attribs = caps.OpenGL20 || caps.GL_ARB_vertex_shader ? GL11.glGetInteger(34921) : 0;
        this.glVertexAttribPointer_buffer = new Buffer[max_vertex_attribs];
        int max_texture_units = caps.OpenGL20 ? GL11.glGetInteger(34930) : (caps.OpenGL13 || caps.GL_ARB_multitexture ? GL11.glGetInteger(34018) : 1);
        this.glTexCoordPointer_buffer = new Buffer[max_texture_units];
    }

    void clear() {
        this.elementArrayBuffer = 0;
        this.arrayBuffer = 0;
        this.glClientActiveTexture = 0;
        Arrays.fill(this.glVertexAttribPointer_buffer, null);
        Arrays.fill(this.glTexCoordPointer_buffer, null);
        this.vertexArrayObject = 0;
        this.pixelPackBuffer = 0;
        this.pixelUnpackBuffer = 0;
        this.indirectBuffer = 0;
    }

    void copy(BaseReferences references, int mask) {
        if ((mask & 2) != 0) {
            this.elementArrayBuffer = references.elementArrayBuffer;
            this.arrayBuffer = references.arrayBuffer;
            this.glClientActiveTexture = references.glClientActiveTexture;
            System.arraycopy(references.glVertexAttribPointer_buffer, 0, this.glVertexAttribPointer_buffer, 0, this.glVertexAttribPointer_buffer.length);
            System.arraycopy(references.glTexCoordPointer_buffer, 0, this.glTexCoordPointer_buffer, 0, this.glTexCoordPointer_buffer.length);
            this.vertexArrayObject = references.vertexArrayObject;
            this.indirectBuffer = references.indirectBuffer;
        }
        if ((mask & 1) != 0) {
            this.pixelPackBuffer = references.pixelPackBuffer;
            this.pixelUnpackBuffer = references.pixelUnpackBuffer;
        }
    }
}

