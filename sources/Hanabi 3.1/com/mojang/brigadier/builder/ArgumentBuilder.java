package com.mojang.brigadier.builder;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.SingleRedirectModifier;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

public abstract class ArgumentBuilder {
   private final RootCommandNode arguments = new RootCommandNode();
   private Command command;
   private Predicate requirement = (s) -> {
      return true;
   };
   private CommandNode target;
   private RedirectModifier modifier = null;
   private boolean forks;

   protected abstract ArgumentBuilder getThis();

   public ArgumentBuilder then(ArgumentBuilder argument) {
      if (this.target != null) {
         throw new IllegalStateException("Cannot add children to a redirected node");
      } else {
         this.arguments.addChild(argument.build());
         return this.getThis();
      }
   }

   public ArgumentBuilder then(CommandNode argument) {
      if (this.target != null) {
         throw new IllegalStateException("Cannot add children to a redirected node");
      } else {
         this.arguments.addChild(argument);
         return this.getThis();
      }
   }

   public Collection getArguments() {
      return this.arguments.getChildren();
   }

   public ArgumentBuilder executes(Command command) {
      this.command = command;
      return this.getThis();
   }

   public Command getCommand() {
      return this.command;
   }

   public ArgumentBuilder requires(Predicate requirement) {
      this.requirement = requirement;
      return this.getThis();
   }

   public Predicate getRequirement() {
      return this.requirement;
   }

   public ArgumentBuilder redirect(CommandNode target) {
      return this.forward(target, (RedirectModifier)null, false);
   }

   public ArgumentBuilder redirect(CommandNode target, SingleRedirectModifier modifier) {
      return this.forward(target, modifier == null ? null : (o) -> {
         return Collections.singleton(modifier.apply(o));
      }, false);
   }

   public ArgumentBuilder fork(CommandNode target, RedirectModifier modifier) {
      return this.forward(target, modifier, true);
   }

   public ArgumentBuilder forward(CommandNode target, RedirectModifier modifier, boolean fork) {
      if (!this.arguments.getChildren().isEmpty()) {
         throw new IllegalStateException("Cannot forward a node with children");
      } else {
         this.target = target;
         this.modifier = modifier;
         this.forks = fork;
         return this.getThis();
      }
   }

   public CommandNode getRedirect() {
      return this.target;
   }

   public RedirectModifier getRedirectModifier() {
      return this.modifier;
   }

   public boolean isFork() {
      return this.forks;
   }

   public abstract CommandNode build();
}
