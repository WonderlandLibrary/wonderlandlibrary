package javassist.bytecode.annotation;

public class NoSuchClassError extends Error {
   private String className;

   public NoSuchClassError(String var1, Error var2) {
      super(var2.toString(), var2);
      this.className = var1;
   }

   public String getClassName() {
      return this.className;
   }
}
