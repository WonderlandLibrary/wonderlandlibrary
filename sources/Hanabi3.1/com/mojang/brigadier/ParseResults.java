package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContextBuilder;
import java.util.Collections;
import java.util.Map;

public class ParseResults {
   private final CommandContextBuilder context;
   private final Map exceptions;
   private final ImmutableStringReader reader;

   public ParseResults(CommandContextBuilder context, ImmutableStringReader reader, Map exceptions) {
      this.context = context;
      this.reader = reader;
      this.exceptions = exceptions;
   }

   public ParseResults(CommandContextBuilder context) {
      this(context, new StringReader(""), Collections.emptyMap());
   }

   public CommandContextBuilder getContext() {
      return this.context;
   }

   public ImmutableStringReader getReader() {
      return this.reader;
   }

   public Map getExceptions() {
      return this.exceptions;
   }
}
