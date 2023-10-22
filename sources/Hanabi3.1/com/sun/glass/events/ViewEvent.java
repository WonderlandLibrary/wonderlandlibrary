package com.sun.glass.events;

public class ViewEvent {
   public static final int ADD = 411;
   public static final int REMOVE = 412;
   public static final int REPAINT = 421;
   public static final int RESIZE = 422;
   public static final int MOVE = 423;
   public static final int FULLSCREEN_ENTER = 431;
   public static final int FULLSCREEN_EXIT = 432;

   public static String getTypeString(int var0) {
      String var1 = "UNKNOWN";
      switch (var0) {
         case 411:
            var1 = "ADD";
            break;
         case 412:
            var1 = "REMOVE";
            break;
         case 413:
         case 414:
         case 415:
         case 416:
         case 417:
         case 418:
         case 419:
         case 420:
         case 424:
         case 425:
         case 426:
         case 427:
         case 428:
         case 429:
         case 430:
         default:
            System.err.println("Unknown view event type: " + var0);
            break;
         case 421:
            var1 = "REPAINT";
            break;
         case 422:
            var1 = "RESIZE";
            break;
         case 423:
            var1 = "MOVE";
            break;
         case 431:
            var1 = "FULLSCREEN_ENTER";
            break;
         case 432:
            var1 = "FULLSCREEN_EXIT";
      }

      return var1;
   }
}
