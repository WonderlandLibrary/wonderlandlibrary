package org.newdawn.slick.state.transition;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public interface Transition {
   void update(StateBasedGame var1, GameContainer var2, int var3) throws SlickException;

   void preRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException;

   void postRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException;

   boolean isComplete();

   void init(GameState var1, GameState var2);
}
