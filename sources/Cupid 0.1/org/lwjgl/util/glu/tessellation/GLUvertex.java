package org.lwjgl.util.glu.tessellation;

class GLUvertex {
  public GLUvertex next;
  
  public GLUvertex prev;
  
  public GLUhalfEdge anEdge;
  
  public Object data;
  
  public double[] coords = new double[3];
  
  public double s;
  
  public double t;
  
  public int pqHandle;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\glu\tessellation\GLUvertex.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */