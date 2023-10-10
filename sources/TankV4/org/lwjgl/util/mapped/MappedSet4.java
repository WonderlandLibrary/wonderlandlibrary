package org.lwjgl.util.mapped;

public class MappedSet4 {
   private final MappedObject a;
   private final MappedObject b;
   private final MappedObject c;
   private final MappedObject d;
   public int view;

   MappedSet4(MappedObject var1, MappedObject var2, MappedObject var3, MappedObject var4) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
   }

   void view(int var1) {
      this.a.setViewAddress(this.a.getViewAddress(var1));
      this.b.setViewAddress(this.b.getViewAddress(var1));
      this.c.setViewAddress(this.c.getViewAddress(var1));
      this.d.setViewAddress(this.d.getViewAddress(var1));
   }

   public void next() {
      this.a.next();
      this.b.next();
      this.c.next();
      this.d.next();
   }
}
