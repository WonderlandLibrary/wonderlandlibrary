package com.ibm.icu.text;

public enum DisplayContext {
   STANDARD_NAMES(DisplayContext.Type.DIALECT_HANDLING, 0),
   DIALECT_NAMES(DisplayContext.Type.DIALECT_HANDLING, 1),
   CAPITALIZATION_NONE(DisplayContext.Type.CAPITALIZATION, 0),
   CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE(DisplayContext.Type.CAPITALIZATION, 1),
   CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE(DisplayContext.Type.CAPITALIZATION, 2),
   CAPITALIZATION_FOR_UI_LIST_OR_MENU(DisplayContext.Type.CAPITALIZATION, 3),
   CAPITALIZATION_FOR_STANDALONE(DisplayContext.Type.CAPITALIZATION, 4);

   private final DisplayContext.Type type;
   private final int value;
   private static final DisplayContext[] $VALUES = new DisplayContext[]{STANDARD_NAMES, DIALECT_NAMES, CAPITALIZATION_NONE, CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE, CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE, CAPITALIZATION_FOR_UI_LIST_OR_MENU, CAPITALIZATION_FOR_STANDALONE};

   private DisplayContext(DisplayContext.Type var3, int var4) {
      this.type = var3;
      this.value = var4;
   }

   public DisplayContext.Type type() {
      return this.type;
   }

   public int value() {
      return this.value;
   }

   public static enum Type {
      DIALECT_HANDLING,
      CAPITALIZATION;

      private static final DisplayContext.Type[] $VALUES = new DisplayContext.Type[]{DIALECT_HANDLING, CAPITALIZATION};
   }
}
