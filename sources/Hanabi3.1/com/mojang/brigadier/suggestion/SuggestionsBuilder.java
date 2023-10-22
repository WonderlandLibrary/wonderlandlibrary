package com.mojang.brigadier.suggestion;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.StringRange;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SuggestionsBuilder {
   private final String input;
   private final int start;
   private final String remaining;
   private final List result = new ArrayList();

   public SuggestionsBuilder(String input, int start) {
      this.input = input;
      this.start = start;
      this.remaining = input.substring(start);
   }

   public String getInput() {
      return this.input;
   }

   public int getStart() {
      return this.start;
   }

   public String getRemaining() {
      return this.remaining;
   }

   public Suggestions build() {
      return Suggestions.create(this.input, this.result);
   }

   public CompletableFuture buildFuture() {
      return CompletableFuture.completedFuture(this.build());
   }

   public SuggestionsBuilder suggest(String text) {
      if (text.equals(this.remaining)) {
         return this;
      } else {
         this.result.add(new Suggestion(StringRange.between(this.start, this.input.length()), text));
         return this;
      }
   }

   public SuggestionsBuilder suggest(String text, Message tooltip) {
      if (text.equals(this.remaining)) {
         return this;
      } else {
         this.result.add(new Suggestion(StringRange.between(this.start, this.input.length()), text, tooltip));
         return this;
      }
   }

   public SuggestionsBuilder suggest(int value) {
      this.result.add(new IntegerSuggestion(StringRange.between(this.start, this.input.length()), value));
      return this;
   }

   public SuggestionsBuilder suggest(int value, Message tooltip) {
      this.result.add(new IntegerSuggestion(StringRange.between(this.start, this.input.length()), value, tooltip));
      return this;
   }

   public SuggestionsBuilder add(SuggestionsBuilder other) {
      this.result.addAll(other.result);
      return this;
   }

   public SuggestionsBuilder createOffset(int start) {
      return new SuggestionsBuilder(this.input, start);
   }

   public SuggestionsBuilder restart() {
      return new SuggestionsBuilder(this.input, this.start);
   }
}
