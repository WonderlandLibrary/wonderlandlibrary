package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;

public class ServerActivity {
   public String profileUuid;
   public long joinTime;
   public long leaveTime;

   public static ServerActivity parse(JsonObject var0) {
      ServerActivity var1 = new ServerActivity();

      try {
         var1.profileUuid = JsonUtils.getStringOr("profileUuid", var0, (String)null);
         var1.joinTime = JsonUtils.getLongOr("joinTime", var0, Long.MIN_VALUE);
         var1.leaveTime = JsonUtils.getLongOr("leaveTime", var0, Long.MIN_VALUE);
      } catch (Exception var3) {
      }

      return var1;
   }
}
