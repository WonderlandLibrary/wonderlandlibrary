package optifine;

public class ReflectorClass {
   private String targetClassName;
   private boolean checked;
   private Class targetClass;

   public ReflectorClass(String var1) {
      this(var1, false);
   }

   public ReflectorClass(String var1, boolean var2) {
      this.targetClassName = null;
      this.checked = false;
      this.targetClass = null;
      this.targetClassName = var1;
      if (!var2) {
         Class var3 = this.getTargetClass();
      }

   }

   public ReflectorClass(Class var1) {
      this.targetClassName = null;
      this.checked = false;
      this.targetClass = null;
      this.targetClass = var1;
      this.targetClassName = var1.getName();
      this.checked = true;
   }

   public Class getTargetClass() {
      if (this.checked) {
         return this.targetClass;
      } else {
         this.checked = true;

         try {
            this.targetClass = Class.forName(this.targetClassName);
         } catch (ClassNotFoundException var2) {
            Config.log("(Reflector) Class not present: " + this.targetClassName);
         } catch (Throwable var3) {
            var3.printStackTrace();
         }

         return this.targetClass;
      }
   }

   public boolean exists() {
      return this.getTargetClass() != null;
   }

   public String getTargetClassName() {
      return this.targetClassName;
   }

   public boolean isInstance(Object var1) {
      return this.getTargetClass() == null ? false : this.getTargetClass().isInstance(var1);
   }

   public ReflectorField makeField(String var1) {
      return new ReflectorField(this, var1);
   }

   public ReflectorMethod makeMethod(String var1) {
      return new ReflectorMethod(this, var1);
   }

   public ReflectorMethod makeMethod(String var1, Class[] var2) {
      return new ReflectorMethod(this, var1, var2);
   }

   public ReflectorMethod makeMethod(String var1, Class[] var2, boolean var3) {
      return new ReflectorMethod(this, var1, var2, var3);
   }
}
