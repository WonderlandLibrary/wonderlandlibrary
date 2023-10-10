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
