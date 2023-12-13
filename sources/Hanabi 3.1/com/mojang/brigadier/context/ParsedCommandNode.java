package com.mojang.brigadier.context;

import com.mojang.brigadier.tree.CommandNode;
import java.util.Objects;

public class ParsedCommandNode {
   private final CommandNode node;
   private final StringRange range;

   public ParsedCommandNode(CommandNode node, StringRange range) {
      this.node = node;
      this.range = range;
   }

   public CommandNode getNode() {
      return this.node;
   }

   public StringRange getRange() {
      return this.range;
   }

   public String toString() {
      return this.node + "@" + this.range;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ParsedCommandNode that = (ParsedCommandNode)o;
         return Objects.equals(this.node, that.node) && Objects.equals(this.range, that.range);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.node, this.range});
   }
}
