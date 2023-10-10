package org.lwjgl.opengl;

import java.nio.Buffer;

class References extends BaseReferences {
   Buffer ARB_matrix_palette_glMatrixIndexPointerARB_pPointer;
   Buffer ARB_vertex_blend_glWeightPointerARB_pPointer;
   Buffer EXT_fog_coord_glFogCoordPointerEXT_data;
   Buffer EXT_secondary_color_glSecondaryColorPointerEXT_pPointer;
   Buffer EXT_vertex_shader_glVariantPointerEXT_pAddr;
   Buffer EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer;
   Buffer GL11_glColorPointer_pointer;
   Buffer GL11_glEdgeFlagPointer_pointer;
   Buffer GL11_glNormalPointer_pointer;
   Buffer GL11_glVertexPointer_pointer;
   Buffer GL14_glFogCoordPointer_data;

   References(ContextCapabilities var1) {
      super(var1);
   }

   void copy(References var1, int var2) {
      super.copy(var1, var2);
      if ((var2 & 2) != 0) {
         this.ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = var1.ARB_matrix_palette_glMatrixIndexPointerARB_pPointer;
         this.ARB_vertex_blend_glWeightPointerARB_pPointer = var1.ARB_vertex_blend_glWeightPointerARB_pPointer;
         this.EXT_fog_coord_glFogCoordPointerEXT_data = var1.EXT_fog_coord_glFogCoordPointerEXT_data;
         this.EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = var1.EXT_secondary_color_glSecondaryColorPointerEXT_pPointer;
         this.EXT_vertex_shader_glVariantPointerEXT_pAddr = var1.EXT_vertex_shader_glVariantPointerEXT_pAddr;
         this.EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer = var1.EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer;
         this.GL11_glColorPointer_pointer = var1.GL11_glColorPointer_pointer;
         this.GL11_glEdgeFlagPointer_pointer = var1.GL11_glEdgeFlagPointer_pointer;
         this.GL11_glNormalPointer_pointer = var1.GL11_glNormalPointer_pointer;
         this.GL11_glVertexPointer_pointer = var1.GL11_glVertexPointer_pointer;
         this.GL14_glFogCoordPointer_data = var1.GL14_glFogCoordPointer_data;
      }

   }

   void clear() {
      super.clear();
      this.ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = null;
      this.ARB_vertex_blend_glWeightPointerARB_pPointer = null;
      this.EXT_fog_coord_glFogCoordPointerEXT_data = null;
      this.EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = null;
      this.EXT_vertex_shader_glVariantPointerEXT_pAddr = null;
      this.EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer = null;
      this.GL11_glColorPointer_pointer = null;
      this.GL11_glEdgeFlagPointer_pointer = null;
      this.GL11_glNormalPointer_pointer = null;
      this.GL11_glVertexPointer_pointer = null;
      this.GL14_glFogCoordPointer_data = null;
   }
}
