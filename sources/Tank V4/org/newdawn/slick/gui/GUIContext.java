package org.newdawn.slick.gui;

import org.lwjgl.input.Cursor;
import org.newdawn.slick.Font;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.ImageData;

public interface GUIContext {
   Input getInput();

   long getTime();

   int getScreenWidth();

   int getScreenHeight();

   int getWidth();

   int getHeight();

   Font getDefaultFont();

   void setMouseCursor(String var1, int var2, int var3) throws SlickException;

   void setMouseCursor(ImageData var1, int var2, int var3) throws SlickException;

   void setMouseCursor(Cursor var1, int var2, int var3) throws SlickException;

   void setDefaultMouseCursor();
}
