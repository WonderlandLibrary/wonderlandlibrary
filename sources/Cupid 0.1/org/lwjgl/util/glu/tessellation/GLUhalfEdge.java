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
  
  GLUhalfEdge(boolean first) {
    this.first = first;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\glu\tessellation\GLUhalfEdge.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */