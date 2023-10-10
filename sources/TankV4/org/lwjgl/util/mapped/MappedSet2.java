package org.lwjgl.util.mapped;

public class MappedSet2 {
   private final MappedObject a;
   private final MappedObject b;
   public int view;

   MappedSet2(MappedObject var1, MappedObject var2) {
      this.a = var1;
      this.b = var2;
   }

   void view(int var1) {
      this.a.setViewAddress(this.a.getViewAddress(var1));
      this.b.setViewAddress(this.b.getViewAddress(var1));
   }

   public void next() {
      this.a.next();
      this.b.next();
   }
}
