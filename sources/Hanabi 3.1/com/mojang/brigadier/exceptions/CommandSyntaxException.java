package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.Message;

public class CommandSyntaxException extends Exception {
   public static final int CONTEXT_AMOUNT = 10;
   public static boolean ENABLE_COMMAND_STACK_TRACES = true;
   public static BuiltInExceptionProvider BUILT_IN_EXCEPTIONS = new BuiltInExceptions();
   private final CommandExceptionType type;
   private final Message message;
   private final String input;
   private final int cursor;

   public CommandSyntaxException(CommandExceptionType type, Message message) {
      super(message.getString(), (Throwable)null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES);
      this.type = type;
      this.message = message;
      this.input = null;
      this.cursor = -1;
   }

   public CommandSyntaxException(CommandExceptionType type, Message message, String input, int cursor) {
      super(message.getString(), (Throwable)null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES);
      this.type = type;
      this.message = message;
      this.input = input;
      this.cursor = cursor;
   }

   public String getMessage() {
      String message = this.message.getString();
      String context = this.getContext();
      if (context != null) {
         message = message + " at position " + this.cursor + ": " + context;
      }

      return message;
   }

   public Message getRawMessage() {
      return this.message;
   }

   public String getContext() {
      if (this.input != null && this.cursor >= 0) {
         StringBuilder builder = new StringBuilder();
         int cursor = Math.min(this.input.length(), this.cursor);
         if (cursor > 10) {
            builder.append("...");
         }

         builder.append(this.input, Math.max(0, cursor - 10), cursor);
         builder.append("<--[HERE]");
         return builder.toString();
      } else {
         return null;
      }
   }

   public CommandExceptionType getType() {
      return this.type;
   }

   public String getInput() {
      return this.input;
   }

   public int getCursor() {
      return this.cursor;
   }
}
