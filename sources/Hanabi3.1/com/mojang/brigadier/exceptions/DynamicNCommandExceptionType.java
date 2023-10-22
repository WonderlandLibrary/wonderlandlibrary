package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;

public class DynamicNCommandExceptionType implements CommandExceptionType {
   private final Function function;

   public DynamicNCommandExceptionType(Function function) {
      this.function = function;
   }

   public CommandSyntaxException create(Object a, Object... args) {
      return new CommandSyntaxException(this, this.function.apply(args));
   }

   public CommandSyntaxException createWithContext(ImmutableStringReader reader, Object... args) {
      return new CommandSyntaxException(this, this.function.apply(args), reader.getString(), reader.getCursor());
   }

   public interface Function {
      Message apply(Object[] var1);
   }
}
