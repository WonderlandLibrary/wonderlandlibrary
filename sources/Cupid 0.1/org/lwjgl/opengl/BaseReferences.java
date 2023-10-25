package org.lwjgl.opengl;

import java.nio.Buffer;
import java.util.Arrays;

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
    int max_vertex_attribs, max_texture_units;
    if (caps.OpenGL20 || caps.GL_ARB_vertex_shader) {
      max_vertex_attribs = GL11.glGetInteger(34921);
    } else {
      max_vertex_attribs = 0;
    } 
    this.glVertexAttribPointer_buffer = new Buffer[max_vertex_attribs];
    if (caps.OpenGL20) {
      max_texture_units = GL11.glGetInteger(34930);
    } else if (caps.OpenGL13 || caps.GL_ARB_multitexture) {
      max_texture_units = GL11.glGetInteger(34018);
    } else {
      max_texture_units = 1;
    } 
    this.glTexCoordPointer_buffer = new Buffer[max_texture_units];
  }
  
  void clear() {
    this.elementArrayBuffer = 0;
    this.arrayBuffer = 0;
    this.glClientActiveTexture = 0;
    Arrays.fill((Object[])this.glVertexAttribPointer_buffer, (Object)null);
    Arrays.fill((Object[])this.glTexCoordPointer_buffer, (Object)null);
    this.vertexArrayObject = 0;
    this.pixelPackBuffer = 0;
    this.pixelUnpackBuffer = 0;
    this.indirectBuffer = 0;
  }
  
  void copy(BaseReferences references, int mask) {
    if ((mask & 0x2) != 0) {
      this.elementArrayBuffer = references.elementArrayBuffer;
      this.arrayBuffer = references.arrayBuffer;
      this.glClientActiveTexture = references.glClientActiveTexture;
      System.arraycopy(references.glVertexAttribPointer_buffer, 0, this.glVertexAttribPointer_buffer, 0, this.glVertexAttribPointer_buffer.length);
      System.arraycopy(references.glTexCoordPointer_buffer, 0, this.glTexCoordPointer_buffer, 0, this.glTexCoordPointer_buffer.length);
      this.vertexArrayObject = references.vertexArrayObject;
      this.indirectBuffer = references.indirectBuffer;
    } 
    if ((mask & 0x1) != 0) {
      this.pixelPackBuffer = references.pixelPackBuffer;
      this.pixelUnpackBuffer = references.pixelUnpackBuffer;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\BaseReferences.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */