package my.NewSnake.Tank.gui.click.component;

public abstract class Component {
   private double height;
   private double x;
   private double width;
   private double y;
   private Object parent;

   public double getX() {
      return this.x;
   }

   public boolean hovering(int var1, int var2) {
      return (double)var1 > this.getX() && (double)var1 < this.getX() + this.getWidth() && (double)var2 > this.getY() && (double)var2 < this.getY() + this.getHeight();
   }

   public void setY(double var1) {
      this.y = var1;
   }

   public Component(Object var1, double var2, double var4, double var6, double var8) {
      this.parent = var1;
      this.x = var2;
      this.y = var4;
      this.width = var6;
      this.height = var8;
   }

   public void setWidth(double var1) {
      this.width = var1;
   }

   public abstract void drag(int var1, int var2, int var3);

   public double getHeight() {
      return this.height;
   }

   public Object getParent() {
      return this.parent;
   }

   public void setHeight(double var1) {
      this.height = var1;
   }

   public abstract void click(int var1, int var2, int var3);

   public abstract void keyPress(int var1, char var2);

   public double getY() {
      return this.y;
   }

   public abstract void draw(int var1, int var2);

   public double getWidth() {
      return this.width;
   }

   public void setX(double var1) {
      this.x = var1;
   }

   public void setParent(Object var1) {
      this.parent = var1;
   }

   public abstract void release(int var1, int var2, int var3);
}
