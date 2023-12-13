package javassist.tools;

import java.util.HashMap;
import java.util.UUID;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public abstract class Callback {
   public static HashMap callbacks = new HashMap();
   private final String sourceCode;

   public Callback(String var1) {
      String var2 = UUID.randomUUID().toString();
      callbacks.put(var2, this);
      this.sourceCode = "((javassist.tools.Callback) javassist.tools.Callback.callbacks.get(\"" + var2 + "\")).result(new Object[]{" + var1 + "});";
   }

   public abstract void result(Object... var1);

   public String toString() {
      return this.sourceCode();
   }

   public String sourceCode() {
      return this.sourceCode;
   }

   public static void insertBefore(CtBehavior var0, Callback var1) throws CannotCompileException {
      var0.insertBefore(var1.toString());
   }

   public static void insertAfter(CtBehavior var0, Callback var1) throws CannotCompileException {
      var0.insertAfter(var1.toString(), false);
   }

   public static void insertAfter(CtBehavior var0, Callback var1, boolean var2) throws CannotCompileException {
      var0.insertAfter(var1.toString(), var2);
   }

   public static int insertAt(CtBehavior var0, Callback var1, int var2) throws CannotCompileException {
      return var0.insertAt(var2, var1.toString());
   }
}
