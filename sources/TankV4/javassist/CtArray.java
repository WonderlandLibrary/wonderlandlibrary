package javassist;

final class CtArray extends CtClass {
   protected ClassPool pool;
   private CtClass[] interfaces = null;

   CtArray(String var1, ClassPool var2) {
      super(var1);
      this.pool = var2;
   }

   public ClassPool getClassPool() {
      return this.pool;
   }

   public boolean isArray() {
      return true;
   }

   public int getModifiers() {
      int var1 = 16;

      try {
         var1 |= this.getComponentType().getModifiers() & 7;
      } catch (NotFoundException var3) {
      }

      return var1;
   }

   public CtClass[] getInterfaces() throws NotFoundException {
      if (this.interfaces == null) {
         Class[] var1 = Object[].class.getInterfaces();
         this.interfaces = new CtClass[var1.length];

         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.interfaces[var2] = this.pool.get(var1[var2].getName());
         }
      }

      return this.interfaces;
   }

   public boolean subtypeOf(CtClass var1) throws NotFoundException {
      if (super.subtypeOf(var1)) {
         return true;
      } else {
         String var2 = var1.getName();
         if (var2.equals("java.lang.Object")) {
            return true;
         } else {
            CtClass[] var3 = this.getInterfaces();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (var3[var4].subtypeOf(var1)) {
                  return true;
               }
            }

            return var1.isArray() && this.getComponentType().subtypeOf(var1.getComponentType());
         }
      }
   }

   public CtClass getComponentType() throws NotFoundException {
      String var1 = this.getName();
      return this.pool.get(var1.substring(0, var1.length() - 2));
   }

   public CtClass getSuperclass() throws NotFoundException {
      return this.pool.get("java.lang.Object");
   }

   public CtMethod[] getMethods() {
      try {
         return this.getSuperclass().getMethods();
      } catch (NotFoundException var2) {
         return super.getMethods();
      }
   }

   public CtMethod getMethod(String var1, String var2) throws NotFoundException {
      return this.getSuperclass().getMethod(var1, var2);
   }

   public CtConstructor[] getConstructors() {
      try {
         return this.getSuperclass().getConstructors();
      } catch (NotFoundException var2) {
         return super.getConstructors();
      }
   }
}
