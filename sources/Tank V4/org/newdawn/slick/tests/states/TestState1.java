package org.newdawn.slick.tests.states;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.CrossStateTransition;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class TestState1 extends BasicGameState {
   public static final int ID = 1;
   private Font font;
   private StateBasedGame game;

   public int getID() {
      return 1;
   }

   public void init(GameContainer var1, StateBasedGame var2) throws SlickException {
      this.game = var2;
      this.font = new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
   }

   public void render(GameContainer var1, StateBasedGame var2, Graphics var3) {
      var3.setFont(this.font);
      var3.setColor(Color.white);
      var3.drawString("State Based Game Test", 100.0F, 100.0F);
      var3.drawString("Numbers 1-3 will switch between states.", 150.0F, 300.0F);
      var3.setColor(Color.red);
      var3.drawString("This is State 1", 200.0F, 50.0F);
   }

   public void update(GameContainer var1, StateBasedGame var2, int var3) {
   }

   public void keyReleased(int var1, char var2) {
      if (var1 == 3) {
         GameState var3 = this.game.getState(2);
         long var4 = System.currentTimeMillis();
         CrossStateTransition var6 = new CrossStateTransition(this, var3, var4) {
            private final long val$start;
            private final TestState1 this$0;

            {
               this.this$0 = var1;
               this.val$start = var3;
            }

            public boolean isComplete() {
               return System.currentTimeMillis() - this.val$start > 2000L;
            }

            public void init(GameState var1, GameState var2) {
            }
         };
         this.game.enterState(2, var6, new EmptyTransition());
      }

      if (var1 == 4) {
         this.game.enterState(3, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
      }

   }
}
