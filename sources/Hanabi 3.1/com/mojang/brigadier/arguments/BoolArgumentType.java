package com.mojang.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class BoolArgumentType implements ArgumentType {
   private static final Collection EXAMPLES = Arrays.asList("true", "false");

   private BoolArgumentType() {
   }

   public static BoolArgumentType bool() {
      return new BoolArgumentType();
   }

   public static boolean getBool(CommandContext context, String name) {
      return (Boolean)context.getArgument(name, Boolean.class);
   }

   public Boolean parse(StringReader reader) throws CommandSyntaxException {
      return reader.readBoolean();
   }

   public CompletableFuture listSuggestions(CommandContext context, SuggestionsBuilder builder) {
      if ("true".startsWith(builder.getRemaining().toLowerCase())) {
         builder.suggest("true");
      }

      if ("false".startsWith(builder.getRemaining().toLowerCase())) {
         builder.suggest("false");
      }

      return builder.buildFuture();
   }

   public Collection getExamples() {
      return EXAMPLES;
   }
}
