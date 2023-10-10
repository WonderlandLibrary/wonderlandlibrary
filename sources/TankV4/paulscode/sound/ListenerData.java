package paulscode.sound;

public class ListenerData {
   public Vector3D position;
   public Vector3D lookAt;
   public Vector3D up;
   public Vector3D velocity;
   public float angle = 0.0F;

   public ListenerData() {
      this.position = new Vector3D(0.0F, 0.0F, 0.0F);
      this.lookAt = new Vector3D(0.0F, 0.0F, -1.0F);
      this.up = new Vector3D(0.0F, 1.0F, 0.0F);
      this.velocity = new Vector3D(0.0F, 0.0F, 0.0F);
      this.angle = 0.0F;
   }

   public ListenerData(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      this.position = new Vector3D(var1, var2, var3);
      this.lookAt = new Vector3D(var4, var5, var6);
      this.up = new Vector3D(var7, var8, var9);
      this.velocity = new Vector3D(0.0F, 0.0F, 0.0F);
      this.angle = var10;
   }

   public ListenerData(Vector3D var1, Vector3D var2, Vector3D var3, float var4) {
      this.position = var1.clone();
      this.lookAt = var2.clone();
      this.up = var3.clone();
      this.velocity = new Vector3D(0.0F, 0.0F, 0.0F);
      this.angle = var4;
   }

   public void setData(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      this.position.x = var1;
      this.position.y = var2;
      this.position.z = var3;
      this.lookAt.x = var4;
      this.lookAt.y = var5;
      this.lookAt.z = var6;
      this.up.x = var7;
      this.up.y = var8;
      this.up.z = var9;
      this.angle = var10;
   }

   public void setData(Vector3D var1, Vector3D var2, Vector3D var3, float var4) {
      this.position.x = var1.x;
      this.position.y = var1.y;
      this.position.z = var1.z;
      this.lookAt.x = var2.x;
      this.lookAt.y = var2.y;
      this.lookAt.z = var2.z;
      this.up.x = var3.x;
      this.up.y = var3.y;
      this.up.z = var3.z;
      this.angle = var4;
   }

   public void setData(ListenerData var1) {
      this.position.x = var1.position.x;
      this.position.y = var1.position.y;
      this.position.z = var1.position.z;
      this.lookAt.x = var1.lookAt.x;
      this.lookAt.y = var1.lookAt.y;
      this.lookAt.z = var1.lookAt.z;
      this.up.x = var1.up.x;
      this.up.y = var1.up.y;
      this.up.z = var1.up.z;
      this.angle = var1.angle;
   }

   public void setPosition(float var1, float var2, float var3) {
      this.position.x = var1;
      this.position.y = var2;
      this.position.z = var3;
   }

   public void setPosition(Vector3D var1) {
      this.position.x = var1.x;
      this.position.y = var1.y;
      this.position.z = var1.z;
   }

   public void setOrientation(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.lookAt.x = var1;
      this.lookAt.y = var2;
      this.lookAt.z = var3;
      this.up.x = var4;
      this.up.y = var5;
      this.up.z = var6;
   }

   public void setOrientation(Vector3D var1, Vector3D var2) {
      this.lookAt.x = var1.x;
      this.lookAt.y = var1.y;
      this.lookAt.z = var1.z;
      this.up.x = var2.x;
      this.up.y = var2.y;
      this.up.z = var2.z;
   }

   public void setVelocity(Vector3D var1) {
      this.velocity.x = var1.x;
      this.velocity.y = var1.y;
      this.velocity.z = var1.z;
   }

   public void setVelocity(float var1, float var2, float var3) {
      this.velocity.x = var1;
      this.velocity.y = var2;
      this.velocity.z = var3;
   }

   public void setAngle(float var1) {
      this.angle = var1;
      this.lookAt.x = -1.0F * (float)Math.sin((double)this.angle);
      this.lookAt.z = -1.0F * (float)Math.cos((double)this.angle);
   }
}
