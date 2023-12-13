package org.newdawn.slick;

public interface ControllerListener extends ControlledInputReciever {
   void controllerLeftPressed(int var1);

   void controllerLeftReleased(int var1);

   void controllerRightPressed(int var1);

   void controllerRightReleased(int var1);

   void controllerUpPressed(int var1);

   void controllerUpReleased(int var1);

   void controllerDownPressed(int var1);

   void controllerDownReleased(int var1);

   void controllerButtonPressed(int var1, int var2);

   void controllerButtonReleased(int var1, int var2);
}
