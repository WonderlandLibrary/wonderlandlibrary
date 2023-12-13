package cn.hanabi.utils;

public class RectUtil {
   public float x;
   public float y;
   public float x2;
   public float y2;
   public float xd;
   public float yd;
   public float x2d;
   public float y2d;
   public TranslateUtil tl = new TranslateUtil(0.0F, 0.0F);
   public TranslateUtil br = new TranslateUtil(0.0F, 0.0F);

   public RectUtil(float x, float y, float x2, float y2) {
      this.x = x;
      this.y = y;
      this.x2 = x2;
      this.y2 = y2;
      this.xd = x;
      this.yd = y;
      this.x2d = x2;
      this.y2d = y2;
      this.tl.setXY(x, y);
      this.br.setXY(x2, y2);
   }

   public void interpolate(float x, float y, float x2, float y2, float s1, float s2) {
      this.xd = x;
      this.yd = y;
      this.x2d = x2;
      this.y2d = y2;
      if (this.tl.getX() < this.xd) {
         this.tl.interpolate(this.xd, this.tl.getY(), s1);
         this.br.interpolate(this.x2d, this.br.getY(), s2);
      } else {
         this.tl.interpolate(this.xd, this.tl.getY(), s2);
         this.br.interpolate(this.x2d, this.br.getY(), s1);
      }

      if (this.tl.getY() < this.yd) {
         this.tl.interpolate(this.tl.getX(), this.yd, s1);
         this.br.interpolate(this.br.getX(), this.y2d, s2);
      } else {
         this.tl.interpolate(this.tl.getX(), this.yd, s2);
         this.br.interpolate(this.br.getX(), this.y2d, s1);
      }

      this.x = this.tl.getX();
      this.y = this.tl.getY();
      this.x2 = this.br.getX();
      this.y2 = this.br.getY();
   }

   public void setX(float x) {
      this.tl.setX(x);
      this.x = this.tl.getX();
   }

   public void setY(float y) {
      this.tl.setY(y);
      this.y = this.tl.getY();
   }

   public void setX2(float x2) {
      this.br.setX(x2);
      this.x2 = this.br.getX();
   }

   public void setY2(float y2) {
      this.br.setY(y2);
      this.y2 = this.br.getY();
   }

   public float getX() {
      return this.x;
   }

   public float getX2() {
      return this.x2;
   }

   public float getY() {
      return this.y;
   }

   public float getY2() {
      return this.y2;
   }
}
