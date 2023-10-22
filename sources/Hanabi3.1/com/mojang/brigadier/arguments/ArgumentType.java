package com.mojang.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public interface ArgumentType {
   Object parse(StringReader var1) throws CommandSyntaxException;

   default CompletableFuture listSuggestions(CommandContext context, SuggestionsBuilder builder) {
      return Suggestions.empty();
   }

   default Collection getExamples() {
      return Collections.emptyList();
   }
}
