package javassist.bytecode.analysis;

public class Frame {
   private Type[] locals;
   private Type[] stack;
   private int top;
   private boolean jsrMerged;
   private boolean retMerged;

   public Frame(int var1, int var2) {
      this.locals = new Type[var1];
      this.stack = new Type[var2];
   }

   public Type getLocal(int var1) {
      return this.locals[var1];
   }

   public void setLocal(int var1, Type var2) {
      this.locals[var1] = var2;
   }

   public Type getStack(int var1) {
      return this.stack[var1];
   }

   public void setStack(int var1, Type var2) {
      this.stack[var1] = var2;
   }

   public void clearStack() {
      this.top = 0;
   }

   public int getTopIndex() {
      return this.top - 1;
   }

   public int localsLength() {
      return this.locals.length;
   }

   public Type peek() {
      if (this.top < 1) {
         throw new IndexOutOfBoundsException("Stack is empty");
      } else {
         return this.stack[this.top - 1];
      }
   }

   public Type pop() {
      if (this.top < 1) {
         throw new IndexOutOfBoundsException("Stack is empty");
      } else {
         return this.stack[--this.top];
      }
   }

   public void push(Type var1) {
      this.stack[this.top++] = var1;
   }

   public Frame copy() {
      Frame var1 = new Frame(this.locals.length, this.stack.length);
      System.arraycopy(this.locals, 0, var1.locals, 0, this.locals.length);
      System.arraycopy(this.stack, 0, var1.stack, 0, this.stack.length);
      var1.top = this.top;
      return var1;
   }

   public Frame copyStack() {
      Frame var1 = new Frame(this.locals.length, this.stack.length);
      System.arraycopy(this.stack, 0, var1.stack, 0, this.stack.length);
      var1.top = this.top;
      return var1;
   }

   public boolean mergeStack(Frame var1) {
      boolean var2 = false;
      if (this.top != var1.top) {
         throw new RuntimeException("Operand stacks could not be merged, they are different sizes!");
      } else {
         for(int var3 = 0; var3 < this.top; ++var3) {
            if (this.stack[var3] != null) {
               Type var4 = this.stack[var3];
               Type var5 = var4.merge(var1.stack[var3]);
               if (var5 == Type.BOGUS) {
                  throw new RuntimeException("Operand stacks could not be merged due to differing primitive types: pos = " + var3);
               }

               this.stack[var3] = var5;
               if (!var5.equals(var4) || var5.popChanged()) {
                  var2 = true;
               }
            }
         }

         return var2;
      }
   }

   public boolean merge(Frame var1) {
      boolean var2 = false;

      for(int var3 = 0; var3 < this.locals.length; ++var3) {
         if (this.locals[var3] != null) {
            Type var4 = this.locals[var3];
            Type var5 = var4.merge(var1.locals[var3]);
            this.locals[var3] = var5;
            if (!var5.equals(var4) || var5.popChanged()) {
               var2 = true;
            }
         } else if (var1.locals[var3] != null) {
            this.locals[var3] = var1.locals[var3];
            var2 = true;
         }
      }

      var2 |= this.mergeStack(var1);
      return var2;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("locals = [");

      int var2;
      for(var2 = 0; var2 < this.locals.length; ++var2) {
         var1.append(this.locals[var2] == null ? "empty" : this.locals[var2].toString());
         if (var2 < this.locals.length - 1) {
            var1.append(", ");
         }
      }

      var1.append("] stack = [");

      for(var2 = 0; var2 < this.top; ++var2) {
         var1.append(this.stack[var2]);
         if (var2 < this.top - 1) {
            var1.append(", ");
         }
      }

      var1.append("]");
      return var1.toString();
   }

   boolean isJsrMerged() {
      return this.jsrMerged;
   }

   void setJsrMerged(boolean var1) {
      this.jsrMerged = var1;
   }

   boolean isRetMerged() {
      return this.retMerged;
   }

   void setRetMerged(boolean var1) {
      this.retMerged = var1;
   }
}
