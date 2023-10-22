package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface Command {
   int SINGLE_SUCCESS = 1;

   int run(CommandContext var1) throws CommandSyntaxException;
}
