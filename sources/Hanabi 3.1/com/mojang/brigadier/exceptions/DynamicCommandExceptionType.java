package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;
import java.util.function.Function;

public class DynamicCommandExceptionType implements CommandExceptionType {
   private final Function function;

   public DynamicCommandExceptionType(Function function) {
      this.function = function;
   }

   public CommandSyntaxException create(Object arg) {
      return new CommandSyntaxException(this, (Message)this.function.apply(arg));
   }

   public CommandSyntaxException createWithContext(ImmutableStringReader reader, Object arg) {
      return new CommandSyntaxException(this, (Message)this.function.apply(arg), reader.getString(), reader.getCursor());
   }
}
