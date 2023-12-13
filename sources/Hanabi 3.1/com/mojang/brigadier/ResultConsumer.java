package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContext;

@FunctionalInterface
public interface ResultConsumer {
   void onCommandComplete(CommandContext var1, boolean var2, int var3);
}
