package org.newdawn.slick.tests.xml;

public class Item {
   protected String name;
   protected int condition;

   public void dump(String var1) {
      System.out.println(var1 + "Item " + this.name + "," + this.condition);
   }
}
