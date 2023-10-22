package com.mojang.brigadier.builder;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.Iterator;

public class LiteralArgumentBuilder extends ArgumentBuilder {
   private final String literal;

   protected LiteralArgumentBuilder(String literal) {
      this.literal = literal;
   }

   public static LiteralArgumentBuilder literal(String name) {
      return new LiteralArgumentBuilder(name);
   }

   protected LiteralArgumentBuilder getThis() {
      return this;
   }

   public String getLiteral() {
      return this.literal;
   }

   public LiteralCommandNode build() {
      LiteralCommandNode result = new LiteralCommandNode(this.getLiteral(), this.getCommand(), this.getRequirement(), this.getRedirect(), this.getRedirectModifier(), this.isFork());
      Iterator var2 = this.getArguments().iterator();

      while(var2.hasNext()) {
         CommandNode argument = (CommandNode)var2.next();
         result.addChild(argument);
      }

      return result;
   }
}
