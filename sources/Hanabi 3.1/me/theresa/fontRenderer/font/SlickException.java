package me.theresa.fontRenderer.font;

public class SlickException extends Exception {
   private static final long serialVersionUID = 1L;

   public SlickException(String message) {
      super(message);
   }

   public SlickException(String message, Throwable e) {
      super(message, e);
   }
}
