package net.minecraft.client.stream;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import java.util.Map;

public class Metadata {
   private final String name;
   private Map payload;
   private static final Gson field_152811_a = new Gson();
   private String description;

   public String toString() {
      return Objects.toStringHelper((Object)this).add("name", this.name).add("description", this.description).add("data", this.func_152806_b()).toString();
   }

   public Metadata(String var1) {
      this(var1, (String)null);
   }

   public void func_152807_a(String var1) {
      this.description = var1;
   }

   public String func_152810_c() {
      return this.name;
   }

   public void func_152808_a(String var1, String var2) {
      if (this.payload == null) {
         this.payload = Maps.newHashMap();
      }

      if (this.payload.size() > 50) {
         throw new IllegalArgumentException("Metadata payload is full, cannot add more to it!");
      } else if (var1 == null) {
         throw new IllegalArgumentException("Metadata payload key cannot be null!");
      } else if (var1.length() > 255) {
         throw new IllegalArgumentException("Metadata payload key is too long!");
      } else if (var2 == null) {
         throw new IllegalArgumentException("Metadata payload value cannot be null!");
      } else if (var2.length() > 255) {
         throw new IllegalArgumentException("Metadata payload value is too long!");
      } else {
         this.payload.put(var1, var2);
      }
   }

   public String func_152809_a() {
      return this.description == null ? this.name : this.description;
   }

   public Metadata(String var1, String var2) {
      this.name = var1;
      this.description = var2;
   }

   public String func_152806_b() {
      return this.payload != null && !this.payload.isEmpty() ? field_152811_a.toJson((Object)this.payload) : null;
   }
}
