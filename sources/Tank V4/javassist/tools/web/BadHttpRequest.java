package javassist.tools.web;

public class BadHttpRequest extends Exception {
   private Exception e;

   public BadHttpRequest() {
      this.e = null;
   }

   public BadHttpRequest(Exception var1) {
      this.e = var1;
   }

   public String toString() {
      return this.e == null ? super.toString() : this.e.toString();
   }
}
