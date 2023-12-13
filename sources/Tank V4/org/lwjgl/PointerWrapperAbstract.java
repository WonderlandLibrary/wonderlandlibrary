package org.lwjgl;

public abstract class PointerWrapperAbstract implements PointerWrapper {
   protected final long pointer;

   protected PointerWrapperAbstract(long var1) {
      this.pointer = var1;
   }

   public final void checkValid() {
      if (LWJGLUtil.DEBUG && this != false) {
         throw new IllegalStateException("This " + this.getClass().getSimpleName() + " pointer is not valid.");
      }
   }

   public final long getPointer() {
      this.checkValid();
      return this.pointer;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof PointerWrapperAbstract)) {
         return false;
      } else {
         PointerWrapperAbstract var2 = (PointerWrapperAbstract)var1;
         return this.pointer == var2.pointer;
      }
   }

   public int hashCode() {
      return (int)(this.pointer ^ this.pointer >>> 32);
   }

   public String toString() {
      return this.getClass().getSimpleName() + " pointer (0x" + Long.toHexString(this.pointer).toUpperCase() + ")";
   }
}
