package org.newdawn.slick.command;

public class ControllerDirectionControl extends ControllerControl {
   public static final ControllerDirectionControl.Direction LEFT = new ControllerDirectionControl.Direction(1);
   public static final ControllerDirectionControl.Direction UP = new ControllerDirectionControl.Direction(3);
   public static final ControllerDirectionControl.Direction DOWN = new ControllerDirectionControl.Direction(4);
   public static final ControllerDirectionControl.Direction RIGHT = new ControllerDirectionControl.Direction(2);

   public ControllerDirectionControl(int var1, ControllerDirectionControl.Direction var2) {
      super(var1, ControllerDirectionControl.Direction.access$000(var2), 0);
   }

   private static class Direction {
      private int event;

      public Direction(int var1) {
         this.event = var1;
      }

      static int access$000(ControllerDirectionControl.Direction var0) {
         return var0.event;
      }
   }
}
