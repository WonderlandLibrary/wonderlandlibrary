package com.mojang.brigadier.tree;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class ArgumentCommandNode extends CommandNode {
   private static final String USAGE_ARGUMENT_OPEN = "<";
   private static final String USAGE_ARGUMENT_CLOSE = ">";
   private final String name;
   private final ArgumentType type;
   private final SuggestionProvider customSuggestions;

   public ArgumentCommandNode(String name, ArgumentType type, Command command, Predicate requirement, CommandNode redirect, RedirectModifier modifier, boolean forks, SuggestionProvider customSuggestions) {
      super(command, requirement, redirect, modifier, forks);
      this.name = name;
      this.type = type;
      this.customSuggestions = customSuggestions;
   }

   public ArgumentType getType() {
      return this.type;
   }

   public String getName() {
      return this.name;
   }

   public String getUsageText() {
      return "<" + this.name + ">";
   }

   public SuggestionProvider getCustomSuggestions() {
      return this.customSuggestions;
   }

   public void parse(StringReader reader, CommandContextBuilder contextBuilder) throws CommandSyntaxException {
      int start = reader.getCursor();
      Object result = this.type.parse(reader);
      ParsedArgument parsed = new ParsedArgument(start, reader.getCursor(), result);
      contextBuilder.withArgument(this.name, parsed);
      contextBuilder.withNode(this, parsed.getRange());
   }

   public CompletableFuture listSuggestions(CommandContext context, SuggestionsBuilder builder) throws CommandSyntaxException {
      return this.customSuggestions == null ? this.type.listSuggestions(context, builder) : this.customSuggestions.getSuggestions(context, builder);
   }

   public RequiredArgumentBuilder createBuilder() {
      RequiredArgumentBuilder builder = RequiredArgumentBuilder.argument(this.name, this.type);
      builder.requires(this.getRequirement());
      builder.forward(this.getRedirect(), this.getRedirectModifier(), this.isFork());
      builder.suggests(this.customSuggestions);
      if (this.getCommand() != null) {
         builder.executes(this.getCommand());
      }

      return builder;
   }

   public boolean isValidInput(String input) {
      try {
         StringReader reader = new StringReader(input);
         this.type.parse(reader);
         return !reader.canRead() || reader.peek() == ' ';
      } catch (CommandSyntaxException var3) {
         return false;
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof ArgumentCommandNode)) {
         return false;
      } else {
         ArgumentCommandNode that = (ArgumentCommandNode)o;
         if (!this.name.equals(that.name)) {
            return false;
         } else {
            return !this.type.equals(that.type) ? false : super.equals(o);
         }
      }
   }

   public int hashCode() {
      int result = this.name.hashCode();
      result = 31 * result + this.type.hashCode();
      return result;
   }

   protected String getSortedKey() {
      return this.name;
   }

   public Collection getExamples() {
      return this.type.getExamples();
   }

   public String toString() {
      return "<argument " + this.name + ":" + this.type + ">";
   }
}
