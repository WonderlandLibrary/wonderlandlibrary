package org.newdawn.slick.tests.states;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class TestState2 extends BasicGameState {
   public static final int ID = 2;
   private Font font;
   private Image image;
   private float ang;
   private StateBasedGame game;

   public int getID() {
      return 2;
   }

   public void init(GameContainer var1, StateBasedGame var2) throws SlickException {
      this.game = var2;
      this.font = new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
      this.image = new Image("testdata/logo.tga");
   }

   public void render(GameContainer var1, StateBasedGame var2, Graphics var3) {
      var3.setFont(this.font);
      var3.setColor(Color.green);
      var3.drawString("This is State 2", 200.0F, 50.0F);
      var3.rotate(400.0F, 300.0F, this.ang);
      var3.drawImage(this.image, (float)(400 - this.image.getWidth() / 2), (float)(300 - this.image.getHeight() / 2));
   }

   public void update(GameContainer var1, StateBasedGame var2, int var3) {
      this.ang += (float)var3 * 0.1F;
   }

   public void keyReleased(int var1, char var2) {
      if (var1 == 2) {
         this.game.enterState(1, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
      }

      if (var1 == 4) {
         this.game.enterState(3, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
      }

   }
}
