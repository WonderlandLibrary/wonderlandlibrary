package org.lwjgl.util.glu.tessellation;

class GLUhalfEdge {
   public GLUhalfEdge next;
   public GLUhalfEdge Sym;
   public GLUhalfEdge Onext;
   public GLUhalfEdge Lnext;
   public GLUvertex Org;
   public GLUface Lface;
   public ActiveRegion activeRegion;
   public int winding;
   public boolean first;

   GLUhalfEdge(boolean var1) {
      this.first = var1;
   }
}
