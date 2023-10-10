package javassist;

class FieldInitLink {
   FieldInitLink next = null;
   CtField field;
   CtField.Initializer init;

   FieldInitLink(CtField var1, CtField.Initializer var2) {
      this.field = var1;
      this.init = var2;
   }
}
