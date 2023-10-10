package javassist.bytecode;

public class BadBytecode extends Exception {
   public BadBytecode(int var1) {
      super("bytecode " + var1);
   }

   public BadBytecode(String var1) {
      super(var1);
   }

   public BadBytecode(String var1, Throwable var2) {
      super(var1, var2);
   }

   public BadBytecode(MethodInfo var1, Throwable var2) {
      super(var1.toString() + " in " + var1.getConstPool().getClassName() + ": " + var2.getMessage(), var2);
   }
}
