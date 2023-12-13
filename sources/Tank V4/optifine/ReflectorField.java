package optifine;

import java.lang.reflect.Field;

public class ReflectorField {
   private IFieldLocator fieldLocator;
   private boolean checked;
   private Field targetField;

   public ReflectorField(ReflectorClass var1, String var2) {
      this((IFieldLocator)(new FieldLocatorName(var1, var2)));
   }

   public ReflectorField(ReflectorClass var1, Class var2) {
      this(var1, var2, 0);
   }

   public ReflectorField(ReflectorClass var1, Class var2, int var3) {
      this((IFieldLocator)(new FieldLocatorType(var1, var2, var3)));
   }

   public ReflectorField(Field var1) {
      this((IFieldLocator)(new FieldLocatorFixed(var1)));
   }

   public ReflectorField(IFieldLocator var1) {
      this.fieldLocator = null;
      this.checked = false;
      this.targetField = null;
      this.fieldLocator = var1;
      this.getTargetField();
   }

   public Field getTargetField() {
      if (this.checked) {
         return this.targetField;
      } else {
         this.checked = true;
         this.targetField = this.fieldLocator.getField();
         if (this.targetField != null) {
            this.targetField.setAccessible(true);
         }

         return this.targetField;
      }
   }

   public Object getValue() {
      return Reflector.getFieldValue((Object)null, this);
   }

   public void setValue(Object var1) {
      Reflector.setFieldValue((Object)null, this, var1);
   }

   public void setValue(Object var1, Object var2) {
      Reflector.setFieldValue(var1, this, var2);
   }

   public boolean exists() {
      return this.getTargetField() != null;
   }
}
