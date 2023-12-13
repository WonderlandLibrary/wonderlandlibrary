package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BanEntry extends UserListEntry {
   protected final Date banEndDate;
   protected final Date banStartDate;
   public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
   protected final String bannedBy;
   protected final String reason;

   public BanEntry(Object var1, Date var2, String var3, Date var4, String var5) {
      super(var1);
      this.banStartDate = var2 == null ? new Date() : var2;
      this.bannedBy = var3 == null ? "(Unknown)" : var3;
      this.banEndDate = var4;
      this.reason = var5 == null ? "Banned by an operator." : var5;
   }

   protected void onSerialization(JsonObject var1) {
      var1.addProperty("created", dateFormat.format(this.banStartDate));
      var1.addProperty("source", this.bannedBy);
      var1.addProperty("expires", this.banEndDate == null ? "forever" : dateFormat.format(this.banEndDate));
      var1.addProperty("reason", this.reason);
   }

   public Date getBanEndDate() {
      return this.banEndDate;
   }

   protected BanEntry(Object var1, JsonObject var2) {
      super(var1, var2);

      Date var3;
      try {
         var3 = var2.has("created") ? dateFormat.parse(var2.get("created").getAsString()) : new Date();
      } catch (ParseException var8) {
         var3 = new Date();
      }

      this.banStartDate = var3;
      this.bannedBy = var2.has("source") ? var2.get("source").getAsString() : "(Unknown)";

      Date var4;
      try {
         var4 = var2.has("expires") ? dateFormat.parse(var2.get("expires").getAsString()) : null;
      } catch (ParseException var7) {
         var4 = null;
      }

      this.banEndDate = var4;
      this.reason = var2.has("reason") ? var2.get("reason").getAsString() : "Banned by an operator.";
   }

   boolean hasBanExpired() {
      return this.banEndDate == null ? false : this.banEndDate.before(new Date());
   }

   public String getBanReason() {
      return this.reason;
   }
}
