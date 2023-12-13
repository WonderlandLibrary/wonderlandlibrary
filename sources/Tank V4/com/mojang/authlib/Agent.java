package com.mojang.authlib;

public class Agent {
   public static final Agent MINECRAFT = new Agent("Minecraft", 1);
   public static final Agent SCROLLS = new Agent("Scrolls", 1);
   private final String name;
   private final int version;

   public Agent(String var1, int var2) {
      this.name = var1;
      this.version = var2;
   }

   public String getName() {
      return this.name;
   }

   public int getVersion() {
      return this.version;
   }

   public String toString() {
      return "Agent{name='" + this.name + '\'' + ", version=" + this.version + '}';
   }
}
