package org.newdawn.slick.tests.xml;

public class Stats {
   private int hp;
   private int mp;
   private float age;
   private int exp;

   public void dump(String var1) {
      System.out.println(var1 + "Stats " + this.hp + "," + this.mp + "," + this.age + "," + this.exp);
   }
}
