package paulscode.sound;

public class Vector3D {
   public float x;
   public float y;
   public float z;

   public Vector3D() {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
   }

   public Vector3D(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public Vector3D clone() {
      return new Vector3D(this.x, this.y, this.z);
   }

   public Vector3D cross(Vector3D var1, Vector3D var2) {
      return new Vector3D(var1.y * var2.z - var2.y * var1.z, var1.z * var2.x - var2.z * var1.x, var1.x * var2.y - var2.x * var1.y);
   }

   public Vector3D cross(Vector3D var1) {
      return new Vector3D(this.y * var1.z - var1.y * this.z, this.z * var1.x - var1.z * this.x, this.x * var1.y - var1.x * this.y);
   }

   public float dot(Vector3D var1, Vector3D var2) {
      return var1.x * var2.x + var1.y * var2.y + var1.z * var2.z;
   }

   public float dot(Vector3D var1) {
      return this.x * var1.x + this.y * var1.y + this.z * var1.z;
   }

   public Vector3D add(Vector3D var1, Vector3D var2) {
      return new Vector3D(var1.x + var2.x, var1.y + var2.y, var1.z + var2.z);
   }

   public Vector3D add(Vector3D var1) {
      return new Vector3D(this.x + var1.x, this.y + var1.y, this.z + var1.z);
   }

   public Vector3D subtract(Vector3D var1, Vector3D var2) {
      return new Vector3D(var1.x - var2.x, var1.y - var2.y, var1.z - var2.z);
   }

   public Vector3D subtract(Vector3D var1) {
      return new Vector3D(this.x - var1.x, this.y - var1.y, this.z - var1.z);
   }

   public float length() {
      return (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
   }

   public void normalize() {
      double var1 = Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
      this.x = (float)((double)this.x / var1);
      this.y = (float)((double)this.y / var1);
      this.z = (float)((double)this.z / var1);
   }

   public String toString() {
      return "Vector3D (" + this.x + ", " + this.y + ", " + this.z + ")";
   }

   public Object clone() throws CloneNotSupportedException {
      return this.clone();
   }
}
