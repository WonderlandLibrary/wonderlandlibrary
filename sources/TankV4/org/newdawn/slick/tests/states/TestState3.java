package org.newdawn.slick.tests.states;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class TestState3 extends BasicGameState {
   public static final int ID = 3;
   private Font font;
   private String[] options = new String[]{"Start Game", "Credits", "Highscores", "Instructions", "Exit"};
   private int selected;
   private StateBasedGame game;

   public int getID() {
      return 3;
   }

   public void init(GameContainer var1, StateBasedGame var2) throws SlickException {
      this.font = new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
      this.game = var2;
   }

   public void render(GameContainer var1, StateBasedGame var2, Graphics var3) {
      var3.setFont(this.font);
      var3.setColor(Color.blue);
      var3.drawString("This is State 3", 200.0F, 50.0F);
      var3.setColor(Color.white);

      for(int var4 = 0; var4 < this.options.length; ++var4) {
         var3.drawString(this.options[var4], (float)(400 - this.font.getWidth(this.options[var4]) / 2), (float)(200 + var4 * 50));
         if (this.selected == var4) {
            var3.drawRect(200.0F, (float)(190 + var4 * 50), 400.0F, 50.0F);
         }
      }

   }

   public void update(GameContainer var1, StateBasedGame var2, int var3) {
   }

   public void keyReleased(int var1, char var2) {
      if (var1 == 208) {
         ++this.selected;
         if (this.selected >= this.options.length) {
            this.selected = 0;
         }
      }

      if (var1 == 200) {
         --this.selected;
         if (this.selected < 0) {
            this.selected = this.options.length - 1;
         }
      }

      if (var1 == 2) {
         this.game.enterState(1, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
      }

      if (var1 == 3) {
         this.game.enterState(2, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
      }

   }
}
