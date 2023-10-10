package javassist.bytecode.analysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javassist.CtClass;

public class MultiType extends Type {
   private Map interfaces;
   private Type resolved;
   private Type potentialClass;
   private MultiType mergeSource;
   private boolean changed;

   public MultiType(Map var1) {
      this(var1, (Type)null);
   }

   public MultiType(Map var1, Type var2) {
      super((CtClass)null);
      this.changed = false;
      this.interfaces = var1;
      this.potentialClass = var2;
   }

   public CtClass getCtClass() {
      return this.resolved != null ? this.resolved.getCtClass() : Type.OBJECT.getCtClass();
   }

   public Type getComponent() {
      return null;
   }

   public int getSize() {
      return 1;
   }

   public boolean isArray() {
      return false;
   }

   boolean popChanged() {
      boolean var1 = this.changed;
      this.changed = false;
      return var1;
   }

   public boolean isAssignableFrom(Type var1) {
      throw new UnsupportedOperationException("Not implemented");
   }

   public boolean isAssignableTo(Type var1) {
      if (this.resolved != null) {
         return var1.isAssignableFrom(this.resolved);
      } else if (Type.OBJECT.equals(var1)) {
         return true;
      } else {
         if (this.potentialClass != null && !var1.isAssignableFrom(this.potentialClass)) {
            this.potentialClass = null;
         }

         Map var2 = this.mergeMultiAndSingle(this, var1);
         if (var2.size() == 1 && this.potentialClass == null) {
            this.resolved = Type.get((CtClass)var2.values().iterator().next());
            this.propogateResolved();
            return true;
         } else if (var2.size() >= 1) {
            this.interfaces = var2;
            this.propogateState();
            return true;
         } else if (this.potentialClass != null) {
            this.resolved = this.potentialClass;
            this.propogateResolved();
            return true;
         } else {
            return false;
         }
      }
   }

   private void propogateState() {
      for(MultiType var1 = this.mergeSource; var1 != null; var1 = var1.mergeSource) {
         var1.interfaces = this.interfaces;
         var1.potentialClass = this.potentialClass;
      }

   }

   private void propogateResolved() {
      for(MultiType var1 = this.mergeSource; var1 != null; var1 = var1.mergeSource) {
         var1.resolved = this.resolved;
      }

   }

   public boolean isReference() {
      return true;
   }

   private Map getAllMultiInterfaces(MultiType var1) {
      HashMap var2 = new HashMap();
      Iterator var3 = var1.interfaces.values().iterator();

      while(var3.hasNext()) {
         CtClass var4 = (CtClass)var3.next();
         var2.put(var4.getName(), var4);
         this.getAllInterfaces(var4, var2);
      }

      return var2;
   }

   private Map mergeMultiInterfaces(MultiType var1, MultiType var2) {
      Map var3 = this.getAllMultiInterfaces(var1);
      Map var4 = this.getAllMultiInterfaces(var2);
      return this.findCommonInterfaces(var3, var4);
   }

   private Map mergeMultiAndSingle(MultiType var1, Type var2) {
      Map var3 = this.getAllMultiInterfaces(var1);
      Map var4 = this.getAllInterfaces(var2.getCtClass(), (Map)null);
      return this.findCommonInterfaces(var3, var4);
   }

   public Type merge(Type var1) {
      if (this == var1) {
         return this;
      } else if (var1 == UNINIT) {
         return this;
      } else if (var1 == BOGUS) {
         return BOGUS;
      } else if (var1 == null) {
         return this;
      } else if (this.resolved != null) {
         return this.resolved.merge(var1);
      } else {
         if (this.potentialClass != null) {
            Type var2 = this.potentialClass.merge(var1);
            if (!var2.equals(this.potentialClass) || var2.popChanged()) {
               this.potentialClass = Type.OBJECT.equals(var2) ? null : var2;
               this.changed = true;
            }
         }

         Map var4;
         if (var1 instanceof MultiType) {
            MultiType var3 = (MultiType)var1;
            if (var3.resolved != null) {
               var4 = this.mergeMultiAndSingle(this, var3.resolved);
            } else {
               var4 = this.mergeMultiInterfaces(var3, this);
               if (var3 != null) {
                  this.mergeSource = var3;
               }
            }
         } else {
            var4 = this.mergeMultiAndSingle(this, var1);
         }

         if (var4.size() <= 1 && (var4.size() != 1 || this.potentialClass == null)) {
            if (var4.size() == 1) {
               this.resolved = Type.get((CtClass)var4.values().iterator().next());
            } else if (this.potentialClass != null) {
               this.resolved = this.potentialClass;
            } else {
               this.resolved = OBJECT;
            }

            this.propogateResolved();
            return this.resolved;
         } else {
            if (var4.size() != this.interfaces.size()) {
               this.changed = true;
            } else if (!this.changed) {
               Iterator var5 = var4.keySet().iterator();

               while(var5.hasNext()) {
                  if (!this.interfaces.containsKey(var5.next())) {
                     this.changed = true;
                  }
               }
            }

            this.interfaces = var4;
            this.propogateState();
            return this;
         }
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof MultiType)) {
         return false;
      } else {
         MultiType var2 = (MultiType)var1;
         if (this.resolved != null) {
            return this.resolved.equals(var2.resolved);
         } else {
            return var2.resolved != null ? false : this.interfaces.keySet().equals(var2.interfaces.keySet());
         }
      }
   }

   public String toString() {
      if (this.resolved != null) {
         return this.resolved.toString();
      } else {
         StringBuffer var1 = new StringBuffer("{");
         Iterator var2 = this.interfaces.keySet().iterator();

         while(var2.hasNext()) {
            var1.append(var2.next());
            var1.append(", ");
         }

         var1.setLength(var1.length() - 2);
         if (this.potentialClass != null) {
            var1.append(", *").append(this.potentialClass.toString());
         }

         var1.append("}");
         return var1.toString();
      }
   }
}
