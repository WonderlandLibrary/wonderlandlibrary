package com.mojang.brigadier.suggestion;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.StringRange;
import java.util.Objects;

public class Suggestion implements Comparable {
   private final StringRange range;
   private final String text;
   private final Message tooltip;

   public Suggestion(StringRange range, String text) {
      this(range, text, (Message)null);
   }

   public Suggestion(StringRange range, String text, Message tooltip) {
      this.range = range;
      this.text = text;
      this.tooltip = tooltip;
   }

   public StringRange getRange() {
      return this.range;
   }

   public String getText() {
      return this.text;
   }

   public Message getTooltip() {
      return this.tooltip;
   }

   public String apply(String input) {
      if (this.range.getStart() == 0 && this.range.getEnd() == input.length()) {
         return this.text;
      } else {
         StringBuilder result = new StringBuilder();
         if (this.range.getStart() > 0) {
            result.append(input, 0, this.range.getStart());
         }

         result.append(this.text);
         if (this.range.getEnd() < input.length()) {
            result.append(input.substring(this.range.getEnd()));
         }

         return result.toString();
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof Suggestion)) {
         return false;
      } else {
         Suggestion that = (Suggestion)o;
         return Objects.equals(this.range, that.range) && Objects.equals(this.text, that.text) && Objects.equals(this.tooltip, that.tooltip);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.range, this.text, this.tooltip});
   }

   public String toString() {
      return "Suggestion{range=" + this.range + ", text='" + this.text + '\'' + ", tooltip='" + this.tooltip + '\'' + '}';
   }

   public int compareTo(Suggestion o) {
      return this.text.compareTo(o.text);
   }

   public int compareToIgnoreCase(Suggestion b) {
      return this.text.compareToIgnoreCase(b.text);
   }

   public Suggestion expand(String command, StringRange range) {
      if (range.equals(this.range)) {
         return this;
      } else {
         StringBuilder result = new StringBuilder();
         if (range.getStart() < this.range.getStart()) {
            result.append(command, range.getStart(), this.range.getStart());
         }

         result.append(this.text);
         if (range.getEnd() > this.range.getEnd()) {
            result.append(command, this.range.getEnd(), range.getEnd());
         }

         return new Suggestion(range, result.toString(), this.tooltip);
      }
   }
}
