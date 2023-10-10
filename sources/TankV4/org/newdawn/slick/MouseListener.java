package org.newdawn.slick;

public interface MouseListener extends ControlledInputReciever {
   void mouseWheelMoved(int var1);

   void mouseClicked(int var1, int var2, int var3, int var4);

   void mousePressed(int var1, int var2, int var3);

   void mouseReleased(int var1, int var2, int var3);

   void mouseMoved(int var1, int var2, int var3, int var4);

   void mouseDragged(int var1, int var2, int var3, int var4);
}
