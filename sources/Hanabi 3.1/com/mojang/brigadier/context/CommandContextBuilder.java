package com.mojang.brigadier.context;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.tree.CommandNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommandContextBuilder {
   private final Map arguments = new LinkedHashMap();
   private final CommandNode rootNode;
   private final List nodes = new ArrayList();
   private final CommandDispatcher dispatcher;
   private Object source;
   private Command command;
   private CommandContextBuilder child;
   private StringRange range;
   private RedirectModifier modifier = null;
   private boolean forks;

   public CommandContextBuilder(CommandDispatcher dispatcher, Object source, CommandNode rootNode, int start) {
      this.rootNode = rootNode;
      this.dispatcher = dispatcher;
      this.source = source;
      this.range = StringRange.at(start);
   }

   public CommandContextBuilder withSource(Object source) {
      this.source = source;
      return this;
   }

   public Object getSource() {
      return this.source;
   }

   public CommandNode getRootNode() {
      return this.rootNode;
   }

   public CommandContextBuilder withArgument(String name, ParsedArgument argument) {
      this.arguments.put(name, argument);
      return this;
   }

   public Map getArguments() {
      return this.arguments;
   }

   public CommandContextBuilder withCommand(Command command) {
      this.command = command;
      return this;
   }

   public CommandContextBuilder withNode(CommandNode node, StringRange range) {
      this.nodes.add(new ParsedCommandNode(node, range));
      this.range = StringRange.encompassing(this.range, range);
      this.modifier = node.getRedirectModifier();
      this.forks = node.isFork();
      return this;
   }

   public CommandContextBuilder copy() {
      CommandContextBuilder copy = new CommandContextBuilder(this.dispatcher, this.source, this.rootNode, this.range.getStart());
      copy.command = this.command;
      copy.arguments.putAll(this.arguments);
      copy.nodes.addAll(this.nodes);
      copy.child = this.child;
      copy.range = this.range;
      copy.forks = this.forks;
      return copy;
   }

   public CommandContextBuilder withChild(CommandContextBuilder child) {
      this.child = child;
      return this;
   }

   public CommandContextBuilder getChild() {
      return this.child;
   }

   public CommandContextBuilder getLastChild() {
      CommandContextBuilder result;
      for(result = this; result.getChild() != null; result = result.getChild()) {
      }

      return result;
   }

   public Command getCommand() {
      return this.command;
   }

   public List getNodes() {
      return this.nodes;
   }

   public CommandContext build(String input) {
      return new CommandContext(this.source, input, this.arguments, this.command, this.rootNode, this.nodes, this.range, this.child == null ? null : this.child.build(input), this.modifier, this.forks);
   }

   public CommandDispatcher getDispatcher() {
      return this.dispatcher;
   }

   public StringRange getRange() {
      return this.range;
   }

   public SuggestionContext findSuggestionContext(int cursor) {
      if (this.range.getStart() <= cursor) {
         if (this.range.getEnd() < cursor) {
            if (this.child != null) {
               return this.child.findSuggestionContext(cursor);
            } else if (!this.nodes.isEmpty()) {
               ParsedCommandNode last = (ParsedCommandNode)this.nodes.get(this.nodes.size() - 1);
               return new SuggestionContext(last.getNode(), last.getRange().getEnd() + 1);
            } else {
               return new SuggestionContext(this.rootNode, this.range.getStart());
            }
         } else {
            CommandNode prev = this.rootNode;

            ParsedCommandNode node;
            for(Iterator var3 = this.nodes.iterator(); var3.hasNext(); prev = node.getNode()) {
               node = (ParsedCommandNode)var3.next();
               StringRange nodeRange = node.getRange();
               if (nodeRange.getStart() <= cursor && cursor <= nodeRange.getEnd()) {
                  return new SuggestionContext(prev, nodeRange.getStart());
               }
            }

            if (prev == null) {
               throw new IllegalStateException("Can't find node before cursor");
            } else {
               return new SuggestionContext(prev, this.range.getStart());
            }
         }
      } else {
         throw new IllegalStateException("Can't find node before cursor");
      }
   }
}
