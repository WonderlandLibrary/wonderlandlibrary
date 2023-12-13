package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;

public class Dynamic4CommandExceptionType implements CommandExceptionType {
   private final Function function;

   public Dynamic4CommandExceptionType(Function function) {
      this.function = function;
   }

   public CommandSyntaxException create(Object a, Object b, Object c, Object d) {
      return new CommandSyntaxException(this, this.function.apply(a, b, c, d));
   }

   public CommandSyntaxException createWithContext(ImmutableStringReader reader, Object a, Object b, Object c, Object d) {
      return new CommandSyntaxException(this, this.function.apply(a, b, c, d), reader.getString(), reader.getCursor());
   }

   public interface Function {
      Message apply(Object var1, Object var2, Object var3, Object var4);
   }
}
