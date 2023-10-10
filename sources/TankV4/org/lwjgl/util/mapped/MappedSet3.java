package org.lwjgl.util.mapped;

public class MappedSet3 {
   private final MappedObject a;
   private final MappedObject b;
   private final MappedObject c;
   public int view;

   MappedSet3(MappedObject var1, MappedObject var2, MappedObject var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   void view(int var1) {
      this.a.setViewAddress(this.a.getViewAddress(var1));
      this.b.setViewAddress(this.b.getViewAddress(var1));
      this.c.setViewAddress(this.c.getViewAddress(var1));
   }

   public void next() {
      this.a.next();
      this.b.next();
      this.c.next();
   }
}
