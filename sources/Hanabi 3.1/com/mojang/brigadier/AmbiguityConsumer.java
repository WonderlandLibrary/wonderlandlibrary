package com.mojang.brigadier;

import com.mojang.brigadier.tree.CommandNode;
import java.util.Collection;

@FunctionalInterface
public interface AmbiguityConsumer {
   void ambiguous(CommandNode var1, CommandNode var2, CommandNode var3, Collection var4);
}
