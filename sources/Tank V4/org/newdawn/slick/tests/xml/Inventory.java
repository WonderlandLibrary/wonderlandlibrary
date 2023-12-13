package org.newdawn.slick.tests.xml;

import java.util.ArrayList;

public class Inventory {
   private ArrayList items = new ArrayList();

   private void add(Item var1) {
      this.items.add(var1);
   }

   public void dump(String var1) {
      System.out.println(var1 + "Inventory");

      for(int var2 = 0; var2 < this.items.size(); ++var2) {
         ((Item)this.items.get(var2)).dump(var1 + "\t");
      }

   }
}
