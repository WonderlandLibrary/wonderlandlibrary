package de.gerrygames.viarewind.utils.math;

public class Ray3d {
  Vector3d start;
  
  Vector3d dir;
  
  public Vector3d getStart() {
    return this.start;
  }
  
  public Vector3d getDir() {
    return this.dir;
  }
  
  public Ray3d(Vector3d start, Vector3d dir) {
    this.start = start;
    this.dir = dir;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewin\\utils\math\Ray3d.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */