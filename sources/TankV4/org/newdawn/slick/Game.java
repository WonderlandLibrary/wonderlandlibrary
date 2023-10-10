package org.newdawn.slick;

public interface Game {
   void init(GameContainer var1) throws SlickException;

   void update(GameContainer var1, int var2) throws SlickException;

   void render(GameContainer var1, Graphics var2) throws SlickException;

   boolean closeRequested();

   String getTitle();
}
