package com.mojang.brigadier.context;

import java.util.Objects;

public class ParsedArgument {
   private final StringRange range;
   private final Object result;

   public ParsedArgument(int start, int end, Object result) {
      this.range = StringRange.between(start, end);
      this.result = result;
   }

   public StringRange getRange() {
      return this.range;
   }

   public Object getResult() {
      return this.result;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof ParsedArgument)) {
         return false;
      } else {
         ParsedArgument that = (ParsedArgument)o;
         return Objects.equals(this.range, that.range) && Objects.equals(this.result, that.result);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.range, this.result});
   }
}
