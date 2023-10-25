package org.lwjgl.util.glu.tessellation;

class TessMono {
  static boolean __gl_meshTessellateMonoRegion(GLUface face) {
    GLUhalfEdge up = face.anEdge;
    assert up.Lnext != up && up.Lnext.Lnext != up;
    for (; Geom.VertLeq(up.Sym.Org, up.Org); up = up.Onext.Sym);
    for (; Geom.VertLeq(up.Org, up.Sym.Org); up = up.Lnext);
    GLUhalfEdge lo = up.Onext.Sym;
    while (up.Lnext != lo) {
      if (Geom.VertLeq(up.Sym.Org, lo.Org)) {
        while (lo.Lnext != up && (Geom.EdgeGoesLeft(lo.Lnext) || Geom.EdgeSign(lo.Org, lo.Sym.Org, lo.Lnext.Sym.Org) <= 0.0D)) {
          GLUhalfEdge tempHalfEdge = Mesh.__gl_meshConnect(lo.Lnext, lo);
          if (tempHalfEdge == null)
            return false; 
          lo = tempHalfEdge.Sym;
        } 
        lo = lo.Onext.Sym;
        continue;
      } 
      while (lo.Lnext != up && (Geom.EdgeGoesRight(up.Onext.Sym) || Geom.EdgeSign(up.Sym.Org, up.Org, up.Onext.Sym.Org) >= 0.0D)) {
        GLUhalfEdge tempHalfEdge = Mesh.__gl_meshConnect(up, up.Onext.Sym);
        if (tempHalfEdge == null)
          return false; 
        up = tempHalfEdge.Sym;
      } 
      up = up.Lnext;
    } 
    assert lo.Lnext != up;
    while (lo.Lnext.Lnext != up) {
      GLUhalfEdge tempHalfEdge = Mesh.__gl_meshConnect(lo.Lnext, lo);
      if (tempHalfEdge == null)
        return false; 
      lo = tempHalfEdge.Sym;
    } 
    return true;
  }
  
  public static boolean __gl_meshTessellateInterior(GLUmesh mesh) {
    for (GLUface f = mesh.fHead.next; f != mesh.fHead; f = next) {
      GLUface next = f.next;
      if (f.inside && 
        !__gl_meshTessellateMonoRegion(f))
        return false; 
    } 
    return true;
  }
  
  public static void __gl_meshDiscardExterior(GLUmesh mesh) {
    for (GLUface f = mesh.fHead.next; f != mesh.fHead; f = next) {
      GLUface next = f.next;
      if (!f.inside)
        Mesh.__gl_meshZapFace(f); 
    } 
  }
  
  public static boolean __gl_meshSetWindingNumber(GLUmesh mesh, int value, boolean keepOnlyBoundary) {
    for (GLUhalfEdge e = mesh.eHead.next; e != mesh.eHead; e = eNext) {
      GLUhalfEdge eNext = e.next;
      if (e.Sym.Lface.inside != e.Lface.inside) {
        e.winding = e.Lface.inside ? value : -value;
      } else if (!keepOnlyBoundary) {
        e.winding = 0;
      } else if (!Mesh.__gl_meshDelete(e)) {
        return false;
      } 
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\glu\tessellation\TessMono.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */