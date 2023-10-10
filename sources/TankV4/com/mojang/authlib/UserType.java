package com.mojang.authlib;

import java.util.HashMap;
import java.util.Map;

public enum UserType {
   LEGACY("legacy"),
   MOJANG("mojang");

   private static final Map BY_NAME = new HashMap();
   private final String name;
   private static final UserType[] $VALUES = new UserType[]{LEGACY, MOJANG};

   private UserType(String var3) {
      this.name = var3;
   }

   public static UserType byName(String var0) {
      return (UserType)BY_NAME.get(var0.toLowerCase());
   }

   public String getName() {
      return this.name;
   }

   static {
      UserType[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         UserType var3 = var0[var2];
         BY_NAME.put(var3.name, var3);
      }

   }
}
