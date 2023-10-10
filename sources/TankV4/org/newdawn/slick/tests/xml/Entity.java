package org.newdawn.slick.tests.xml;

public class Entity {
   private float x;
   private float y;
   private Inventory invent;
   private Stats stats;

   private void add(Inventory var1) {
      this.invent = var1;
   }

   private void add(Stats var1) {
      this.stats = var1;
   }

   public void dump(String var1) {
      System.out.println(var1 + "Entity " + this.x + "," + this.y);
      this.invent.dump(var1 + "\t");
      this.stats.dump(var1 + "\t");
   }
}
