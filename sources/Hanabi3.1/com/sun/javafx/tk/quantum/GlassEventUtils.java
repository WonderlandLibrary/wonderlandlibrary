package com.sun.javafx.tk.quantum;

class GlassEventUtils {
   private GlassEventUtils() {
   }

   public static String getMouseEventString(int var0) {
      switch (var0) {
         case 211:
            return "BUTTON_NONE";
         case 212:
            return "BUTTON_LEFT";
         case 213:
            return "BUTTON_RIGHT";
         case 214:
            return "BUTTON_OTHER";
         case 215:
         case 216:
         case 217:
         case 218:
         case 219:
         case 220:
         default:
            return "UNKNOWN";
         case 221:
            return "DOWN";
         case 222:
            return "UP";
         case 223:
            return "DRAG";
         case 224:
            return "MOVE";
         case 225:
            return "ENTER";
         case 226:
            return "EXIT";
         case 227:
            return "CLICK";
         case 228:
            return "WHEEL";
      }
   }

   public static String getViewEventString(int var0) {
      switch (var0) {
         case 411:
            return "ADD";
         case 412:
            return "REMOVE";
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
            return "UNKNOWN";
         case 421:
            return "REPAINT";
         case 422:
            return "RESIZE";
         case 423:
            return "MOVE";
         case 431:
            return "FULLSCREEN_ENTER";
         case 432:
            return "FULLSCREEN_EXIT";
      }
   }

   public static String getWindowEventString(int var0) {
      switch (var0) {
         case 511:
            return "RESIZE";
         case 512:
            return "MOVE";
         case 513:
         case 514:
         case 515:
         case 516:
         case 517:
         case 518:
         case 519:
         case 520:
         case 523:
         case 524:
         case 525:
         case 526:
         case 527:
         case 528:
         case 529:
         case 530:
         case 534:
         case 535:
         case 536:
         case 537:
         case 538:
         case 539:
         case 540:
         default:
            return "UNKNOWN";
         case 521:
            return "CLOSE";
         case 522:
            return "DESTROY";
         case 531:
            return "MINIMIZE";
         case 532:
            return "MAXIMIZE";
         case 533:
            return "RESTORE";
         case 541:
            return "FOCUS_LOST";
         case 542:
            return "FOCUS_GAINED";
         case 543:
            return "FOCUS_GAINED_FORWARD";
         case 544:
            return "FOCUS_GAINED_BACKWARD";
         case 545:
            return "FOCUS_DISABLED";
         case 546:
            return "FOCUS_UNGRAB";
      }
   }
}
