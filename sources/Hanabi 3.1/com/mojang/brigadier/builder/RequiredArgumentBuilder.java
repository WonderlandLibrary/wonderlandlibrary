package com.mojang.brigadier.builder;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import java.util.Iterator;

public class RequiredArgumentBuilder extends ArgumentBuilder {
   private final String name;
   private final ArgumentType type;
   private SuggestionProvider suggestionsProvider = null;

   private RequiredArgumentBuilder(String name, ArgumentType type) {
      this.name = name;
      this.type = type;
   }

   public static RequiredArgumentBuilder argument(String name, ArgumentType type) {
      return new RequiredArgumentBuilder(name, type);
   }

   public RequiredArgumentBuilder suggests(SuggestionProvider provider) {
      this.suggestionsProvider = provider;
      return this.getThis();
   }

   public SuggestionProvider getSuggestionsProvider() {
      return this.suggestionsProvider;
   }

   protected RequiredArgumentBuilder getThis() {
      return this;
   }

   public ArgumentType getType() {
      return this.type;
   }

   public String getName() {
      return this.name;
   }

   public ArgumentCommandNode build() {
      ArgumentCommandNode result = new ArgumentCommandNode(this.getName(), this.getType(), this.getCommand(), this.getRequirement(), this.getRedirect(), this.getRedirectModifier(), this.isFork(), this.getSuggestionsProvider());
      Iterator var2 = this.getArguments().iterator();

      while(var2.hasNext()) {
         CommandNode argument = (CommandNode)var2.next();
         result.addChild(argument);
      }

      return result;
   }
}
