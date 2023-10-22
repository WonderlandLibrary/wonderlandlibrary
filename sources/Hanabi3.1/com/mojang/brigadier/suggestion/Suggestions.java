package com.mojang.brigadier.suggestion;

import com.mojang.brigadier.context.StringRange;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class Suggestions {
   private static final Suggestions EMPTY = new Suggestions(StringRange.at(0), new ArrayList());
   private final StringRange range;
   private final List suggestions;

   public Suggestions(StringRange range, List suggestions) {
      this.range = range;
      this.suggestions = suggestions;
   }

   public StringRange getRange() {
      return this.range;
   }

   public List getList() {
      return this.suggestions;
   }

   public boolean isEmpty() {
      return this.suggestions.isEmpty();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof Suggestions)) {
         return false;
      } else {
         Suggestions that = (Suggestions)o;
         return Objects.equals(this.range, that.range) && Objects.equals(this.suggestions, that.suggestions);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.range, this.suggestions});
   }

   public String toString() {
      return "Suggestions{range=" + this.range + ", suggestions=" + this.suggestions + '}';
   }

   public static CompletableFuture empty() {
      return CompletableFuture.completedFuture(EMPTY);
   }

   public static Suggestions merge(String command, Collection input) {
      if (input.isEmpty()) {
         return EMPTY;
      } else if (input.size() == 1) {
         return (Suggestions)input.iterator().next();
      } else {
         Set texts = new HashSet();
         Iterator var3 = input.iterator();

         while(var3.hasNext()) {
            Suggestions suggestions = (Suggestions)var3.next();
            texts.addAll(suggestions.getList());
         }

         return create(command, texts);
      }
   }

   public static Suggestions create(String command, Collection suggestions) {
      if (suggestions.isEmpty()) {
         return EMPTY;
      } else {
         int start = Integer.MAX_VALUE;
         int end = Integer.MIN_VALUE;

         Suggestion suggestion;
         for(Iterator var4 = suggestions.iterator(); var4.hasNext(); end = Math.max(suggestion.getRange().getEnd(), end)) {
            suggestion = (Suggestion)var4.next();
            start = Math.min(suggestion.getRange().getStart(), start);
         }

         StringRange range = new StringRange(start, end);
         Set texts = new HashSet();
         Iterator var6 = suggestions.iterator();

         while(var6.hasNext()) {
            Suggestion suggestion = (Suggestion)var6.next();
            texts.add(suggestion.expand(command, range));
         }

         List sorted = new ArrayList(texts);
         sorted.sort(Suggestion::compareToIgnoreCase);
         return new Suggestions(range, sorted);
      }
   }
}
