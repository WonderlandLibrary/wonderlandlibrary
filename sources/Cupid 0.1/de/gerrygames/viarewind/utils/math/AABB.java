package de.gerrygames.viarewind.utils.math;

public class AABB {
  Vector3d min;
  
  Vector3d max;
  
  public Vector3d getMin() {
    return this.min;
  }
  
  public Vector3d getMax() {
    return this.max;
  }
  
  public AABB(Vector3d min, Vector3d max) {
    this.min = min;
    this.max = max;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewin\\utils\math\AABB.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */