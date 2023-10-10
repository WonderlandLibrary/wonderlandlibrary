package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.util.Date;

public class IPBanEntry extends BanEntry {
   public IPBanEntry(String var1) {
      this(var1, (Date)null, (String)null, (Date)null, (String)null);
   }

   public IPBanEntry(JsonObject var1) {
      super(getIPFromJson(var1), var1);
   }

   public IPBanEntry(String var1, Date var2, String var3, Date var4, String var5) {
      super(var1, var2, var3, var4, var5);
   }

   protected void onSerialization(JsonObject var1) {
      if (this.getValue() != null) {
         var1.addProperty("ip", (String)this.getValue());
         super.onSerialization(var1);
      }

   }

   private static String getIPFromJson(JsonObject var0) {
      return var0.has("ip") ? var0.get("ip").getAsString() : null;
   }
}
