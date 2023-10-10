package org.lwjgl.util.glu.tessellation;

class GLUmesh {
   GLUvertex vHead = new GLUvertex();
   GLUface fHead = new GLUface();
   GLUhalfEdge eHead = new GLUhalfEdge(true);
   GLUhalfEdge eHeadSym = new GLUhalfEdge(false);
}
