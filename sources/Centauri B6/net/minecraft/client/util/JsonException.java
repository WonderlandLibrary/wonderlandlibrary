package net.minecraft.client.util;

import com.google.common.collect.Lists;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.util.JsonException.1;
import net.minecraft.client.util.JsonException.Entry;

public class JsonException extends IOException {
   private final List field_151383_a = Lists.newArrayList();
   private final String field_151382_b;

   public JsonException(String p_i45279_1_) {
      this.field_151383_a.add(new Entry((1)null));
      this.field_151382_b = p_i45279_1_;
   }

   public JsonException(String p_i45280_1_, Throwable p_i45280_2_) {
      super(p_i45280_2_);
      this.field_151383_a.add(new Entry((1)null));
      this.field_151382_b = p_i45280_1_;
   }

   public String getMessage() {
      return "Invalid " + ((Entry)this.field_151383_a.get(this.field_151383_a.size() - 1)).toString() + ": " + this.field_151382_b;
   }

   public void func_151381_b(String p_151381_1_) {
      Entry.access$202((Entry)this.field_151383_a.get(0), p_151381_1_);
      this.field_151383_a.add(0, new Entry((1)null));
   }

   public static JsonException func_151379_a(Exception p_151379_0_) {
      if(p_151379_0_ instanceof JsonException) {
         return (JsonException)p_151379_0_;
      } else {
         String s = p_151379_0_.getMessage();
         if(p_151379_0_ instanceof FileNotFoundException) {
            s = "File not found";
         }

         return new JsonException(s, p_151379_0_);
      }
   }

   public void func_151380_a(String p_151380_1_) {
      Entry.access$100((Entry)this.field_151383_a.get(0), p_151380_1_);
   }
}
