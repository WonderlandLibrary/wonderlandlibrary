package javassist.runtime;

public class DotClass {
   public static NoClassDefFoundError fail(ClassNotFoundException var0) {
      return new NoClassDefFoundError(var0.getMessage());
   }
}
