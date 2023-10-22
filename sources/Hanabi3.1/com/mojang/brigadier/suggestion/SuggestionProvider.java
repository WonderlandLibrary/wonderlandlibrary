package com.mojang.brigadier.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface SuggestionProvider {
   CompletableFuture getSuggestions(CommandContext var1, SuggestionsBuilder var2) throws CommandSyntaxException;
}
