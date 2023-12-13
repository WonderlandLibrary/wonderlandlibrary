package com.mojang.brigadier.context;

import com.mojang.brigadier.tree.CommandNode;

public class SuggestionContext {
   public final CommandNode parent;
   public final int startPos;

   public SuggestionContext(CommandNode parent, int startPos) {
      this.parent = parent;
      this.startPos = startPos;
   }
}
