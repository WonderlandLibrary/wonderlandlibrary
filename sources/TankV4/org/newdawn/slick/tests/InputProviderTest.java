package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.ControllerButtonControl;
import org.newdawn.slick.command.ControllerDirectionControl;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.InputProviderListener;
import org.newdawn.slick.command.KeyControl;
import org.newdawn.slick.command.MouseButtonControl;

public class InputProviderTest extends BasicGame implements InputProviderListener {
   private Command attack = new BasicCommand("attack");
   private Command jump = new BasicCommand("jump");
   private Command run = new BasicCommand("run");
   private InputProvider provider;
   private String message = "";

   public InputProviderTest() {
      super("InputProvider Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.provider = new InputProvider(var1.getInput());
      this.provider.addListener(this);
      this.provider.bindCommand(new KeyControl(203), this.run);
      this.provider.bindCommand(new KeyControl(30), this.run);
      this.provider.bindCommand(new ControllerDirectionControl(0, ControllerDirectionControl.LEFT), this.run);
      this.provider.bindCommand(new KeyControl(200), this.jump);
      this.provider.bindCommand(new KeyControl(17), this.jump);
      this.provider.bindCommand(new ControllerDirectionControl(0, ControllerDirectionControl.UP), this.jump);
      this.provider.bindCommand(new KeyControl(57), this.attack);
      this.provider.bindCommand(new MouseButtonControl(0), this.attack);
      this.provider.bindCommand(new ControllerButtonControl(0, 1), this.attack);
   }

   public void render(GameContainer var1, Graphics var2) {
      var2.drawString("Press A, W, Left, Up, space, mouse button 1,and gamepad controls", 10.0F, 50.0F);
      var2.drawString(this.message, 100.0F, 150.0F);
   }

   public void update(GameContainer var1, int var2) {
   }

   public void controlPressed(Command var1) {
      this.message = "Pressed: " + var1;
   }

   public void controlReleased(Command var1) {
      this.message = "Released: " + var1;
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new InputProviderTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
