package org.newdawn.slick.state.transition;

import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class CombinedTransition implements Transition {
   private ArrayList transitions = new ArrayList();

   public void addTransition(Transition var1) {
      this.transitions.add(var1);
   }

   public boolean isComplete() {
      for(int var1 = 0; var1 < this.transitions.size(); ++var1) {
         if (!((Transition)this.transitions.get(var1)).isComplete()) {
            return false;
         }
      }

      return true;
   }

   public void postRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException {
      for(int var4 = this.transitions.size() - 1; var4 >= 0; --var4) {
         ((Transition)this.transitions.get(var4)).postRender(var1, var2, var3);
      }

   }

   public void preRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException {
      for(int var4 = 0; var4 < this.transitions.size(); ++var4) {
         ((Transition)this.transitions.get(var4)).postRender(var1, var2, var3);
      }

   }

   public void update(StateBasedGame var1, GameContainer var2, int var3) throws SlickException {
      for(int var4 = 0; var4 < this.transitions.size(); ++var4) {
         Transition var5 = (Transition)this.transitions.get(var4);
         if (!var5.isComplete()) {
            var5.update(var1, var2, var3);
         }
      }

   }

   public void init(GameState var1, GameState var2) {
      for(int var3 = this.transitions.size() - 1; var3 >= 0; --var3) {
         ((Transition)this.transitions.get(var3)).init(var1, var2);
      }

   }
}
