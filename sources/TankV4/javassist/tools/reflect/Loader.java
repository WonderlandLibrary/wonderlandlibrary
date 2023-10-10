package javassist.tools.reflect;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;

public class Loader extends javassist.Loader {
   protected Reflection reflection;

   public static void main(String[] var0) throws Throwable {
      Loader var1 = new Loader();
      var1.run(var0);
   }

   public Loader() throws CannotCompileException, NotFoundException {
      this.delegateLoadingOf("javassist.tools.reflect.Loader");
      this.reflection = new Reflection();
      ClassPool var1 = ClassPool.getDefault();
      this.addTranslator(var1, this.reflection);
   }

   public boolean makeReflective(String var1, String var2, String var3) throws CannotCompileException, NotFoundException {
      return this.reflection.makeReflective(var1, var2, var3);
   }
}
