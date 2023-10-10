package net.minecraft.src;

import net.minecraft.src.Config;
import net.minecraft.src.ReflectorField;
import net.minecraft.src.ReflectorMethod;

public class ReflectorClass {
   private String targetClassName;
   private boolean checked;
   private Class targetClass;

   public ReflectorClass(Class p_i76_1_) {
      this.targetClassName = null;
      this.checked = false;
      this.targetClass = null;
      this.targetClass = p_i76_1_;
      this.targetClassName = p_i76_1_.getName();
      this.checked = true;
   }

   public ReflectorClass(String name, boolean p_i75_2_) {
      this.targetClassName = null;
      this.checked = false;
      this.targetClass = null;
      this.targetClassName = name;
      if(!p_i75_2_) {
         Class var3 = this.getTargetClass();
      }

   }

   public ReflectorClass(String name) {
      this(name, false);
   }

   public boolean isInstance(Object p_isInstance_1_) {
      return this.getTargetClass() == null?false:this.getTargetClass().isInstance(p_isInstance_1_);
   }

   public boolean exists() {
      return this.getTargetClass() != null;
   }

   public String getTargetClassName() {
      return this.targetClassName;
   }

   public ReflectorField makeField(String p_makeField_1_) {
      return new ReflectorField(this, p_makeField_1_);
   }

   public ReflectorMethod makeMethod(String p_makeMethod_1_, Class[] p_makeMethod_2_) {
      return new ReflectorMethod(this, p_makeMethod_1_, p_makeMethod_2_);
   }

   public ReflectorMethod makeMethod(String p_makeMethod_1_, Class[] p_makeMethod_2_, boolean p_makeMethod_3_) {
      return new ReflectorMethod(this, p_makeMethod_1_, p_makeMethod_2_, p_makeMethod_3_);
   }

   public ReflectorMethod makeMethod(String p_makeMethod_1_) {
      return new ReflectorMethod(this, p_makeMethod_1_);
   }

   public Class getTargetClass() {
      if(this.checked) {
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
}
