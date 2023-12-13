package javassist;

public abstract class CtMember {
   CtMember next;
   protected CtClass declaringClass;

   protected CtMember(CtClass var1) {
      this.declaringClass = var1;
      this.next = null;
   }

   final CtMember next() {
      return this.next;
   }

   void nameReplaced() {
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(this.getClass().getName());
      var1.append("@");
      var1.append(Integer.toHexString(this.hashCode()));
      var1.append("[");
      var1.append(Modifier.toString(this.getModifiers()));
      this.extendToString(var1);
      var1.append("]");
      return var1.toString();
   }

   protected abstract void extendToString(StringBuffer var1);

   public CtClass getDeclaringClass() {
      return this.declaringClass;
   }

   public boolean visibleFrom(CtClass var1) {
      int var2 = this.getModifiers();
      if (Modifier.isPublic(var2)) {
         return true;
      } else if (Modifier.isPrivate(var2)) {
         return var1 == this.declaringClass;
      } else {
         String var3 = this.declaringClass.getPackageName();
         String var4 = var1.getPackageName();
         boolean var5;
         if (var3 == null) {
            var5 = var4 == null;
         } else {
            var5 = var3.equals(var4);
         }

         return !var5 && Modifier.isProtected(var2) ? var1.subclassOf(this.declaringClass) : var5;
      }
   }

   public abstract int getModifiers();

   public abstract void setModifiers(int var1);

   public boolean hasAnnotation(Class var1) {
      return this.hasAnnotation(var1.getName());
   }

   public abstract boolean hasAnnotation(String var1);

   public abstract Object getAnnotation(Class var1) throws ClassNotFoundException;

   public abstract Object[] getAnnotations() throws ClassNotFoundException;

   public abstract Object[] getAvailableAnnotations();

   public abstract String getName();

   public abstract String getSignature();

   public abstract String getGenericSignature();

   public abstract void setGenericSignature(String var1);

   public abstract byte[] getAttribute(String var1);

   public abstract void setAttribute(String var1, byte[] var2);

   static class Cache extends CtMember {
      private CtMember methodTail = this;
      private CtMember consTail = this;
      private CtMember fieldTail = this;

      protected void extendToString(StringBuffer var1) {
      }

      public boolean hasAnnotation(String var1) {
         return false;
      }

      public Object getAnnotation(Class var1) throws ClassNotFoundException {
         return null;
      }

      public Object[] getAnnotations() throws ClassNotFoundException {
         return null;
      }

      public byte[] getAttribute(String var1) {
         return null;
      }

      public Object[] getAvailableAnnotations() {
         return null;
      }

      public int getModifiers() {
         return 0;
      }

      public String getName() {
         return null;
      }

      public String getSignature() {
         return null;
      }

      public void setAttribute(String var1, byte[] var2) {
      }

      public void setModifiers(int var1) {
      }

      public String getGenericSignature() {
         return null;
      }

      public void setGenericSignature(String var1) {
      }

      Cache(CtClassType var1) {
         super(var1);
         this.fieldTail.next = this;
      }

      CtMember methodHead() {
         return this;
      }

      CtMember lastMethod() {
         return this.methodTail;
      }

      CtMember consHead() {
         return this.methodTail;
      }

      CtMember lastCons() {
         return this.consTail;
      }

      CtMember fieldHead() {
         return this.consTail;
      }

      CtMember lastField() {
         return this.fieldTail;
      }

      void addMethod(CtMember var1) {
         var1.next = this.methodTail.next;
         this.methodTail.next = var1;
         if (this.methodTail == this.consTail) {
            this.consTail = var1;
            if (this.methodTail == this.fieldTail) {
               this.fieldTail = var1;
            }
         }

         this.methodTail = var1;
      }

      void addConstructor(CtMember var1) {
         var1.next = this.consTail.next;
         this.consTail.next = var1;
         if (this.consTail == this.fieldTail) {
            this.fieldTail = var1;
         }

         this.consTail = var1;
      }

      void addField(CtMember var1) {
         var1.next = this;
         this.fieldTail.next = var1;
         this.fieldTail = var1;
      }

      static int count(CtMember var0, CtMember var1) {
         int var2;
         for(var2 = 0; var0 != var1; var0 = var0.next) {
            ++var2;
         }

         return var2;
      }

      void remove(CtMember var1) {
         CtMember var3;
         for(Object var2 = this; (var3 = ((CtMember)var2).next) != this; var2 = ((CtMember)var2).next) {
            if (var3 == var1) {
               ((CtMember)var2).next = var3.next;
               if (var3 == this.methodTail) {
                  this.methodTail = (CtMember)var2;
               }

               if (var3 == this.consTail) {
                  this.consTail = (CtMember)var2;
               }

               if (var3 == this.fieldTail) {
                  this.fieldTail = (CtMember)var2;
               }
               break;
            }
         }

      }
   }
}
