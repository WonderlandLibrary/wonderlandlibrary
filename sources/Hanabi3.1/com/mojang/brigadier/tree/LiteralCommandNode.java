package com.mojang.brigadier.tree;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class LiteralCommandNode extends CommandNode {
   private final String literal;

   public LiteralCommandNode(String literal, Command command, Predicate requirement, CommandNode redirect, RedirectModifier modifier, boolean forks) {
      super(command, requirement, redirect, modifier, forks);
      this.literal = literal;
   }

   public String getLiteral() {
      return this.literal;
   }

   public String getName() {
      return this.literal;
   }

   public void parse(StringReader reader, CommandContextBuilder contextBuilder) throws CommandSyntaxException {
      int start = reader.getCursor();
      int end = this.parse(reader);
      if (end > -1) {
         contextBuilder.withNode(this, StringRange.between(start, end));
      } else {
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, this.literal);
      }
   }

   private int parse(StringReader reader) {
      int start = reader.getCursor();
      if (reader.canRead(this.literal.length())) {
         int end = start + this.literal.length();
         if (reader.getString().substring(start, end).equals(this.literal)) {
            reader.setCursor(end);
            if (!reader.canRead() || reader.peek() == ' ') {
               return end;
            }

            reader.setCursor(start);
         }
      }

      return -1;
   }

   public CompletableFuture listSuggestions(CommandContext context, SuggestionsBuilder builder) {
      return this.literal.toLowerCase().startsWith(builder.getRemaining().toLowerCase()) ? builder.suggest(this.literal).buildFuture() : Suggestions.empty();
   }

   public boolean isValidInput(String input) {
      return this.parse(new StringReader(input)) > -1;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof LiteralCommandNode)) {
         return false;
      } else {
         LiteralCommandNode that = (LiteralCommandNode)o;
         return !this.literal.equals(that.literal) ? false : super.equals(o);
      }
   }

   public String getUsageText() {
      return this.literal;
   }

   public int hashCode() {
      int result = this.literal.hashCode();
      result = 31 * result + super.hashCode();
      return result;
   }

   public LiteralArgumentBuilder createBuilder() {
      LiteralArgumentBuilder builder = LiteralArgumentBuilder.literal(this.literal);
      builder.requires(this.getRequirement());
      builder.forward(this.getRedirect(), this.getRedirectModifier(), this.isFork());
      if (this.getCommand() != null) {
         builder.executes(this.getCommand());
      }

      return builder;
   }

   protected String getSortedKey() {
      return this.literal;
   }

   public Collection getExamples() {
      return Collections.singleton(this.literal);
   }

   public String toString() {
      return "<literal " + this.literal + ">";
   }
}
