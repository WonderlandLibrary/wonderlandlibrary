package org.newdawn.slick.tests;

import java.util.ArrayList;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

public class InputTest extends BasicGame {
   private String message = "Press any key, mouse button, or drag the mouse";
   private ArrayList lines = new ArrayList();
   private boolean buttonDown;
   private float x;
   private float y;
   private Color[] cols;
   private int index;
   private Input input;
   private int ypos;
   private AppGameContainer app;
   private boolean space;
   private boolean lshift;
   private boolean rshift;

   public InputTest() {
      super("Input Test");
      this.cols = new Color[]{Color.red, Color.green, Color.blue, Color.white, Color.magenta, Color.cyan};
   }

   public void init(GameContainer var1) throws SlickException {
      if (var1 instanceof AppGameContainer) {
         this.app = (AppGameContainer)var1;
      }

      this.input = var1.getInput();
      this.x = 300.0F;
      this.y = 300.0F;
   }

   public void render(GameContainer var1, Graphics var2) {
      var2.drawString("left shift down: " + this.lshift, 100.0F, 240.0F);
      var2.drawString("right shift down: " + this.rshift, 100.0F, 260.0F);
      var2.drawString("space down: " + this.space, 100.0F, 280.0F);
      var2.setColor(Color.white);
      var2.drawString(this.message, 10.0F, 50.0F);
      var2.drawString("" + var1.getInput().getMouseY(), 10.0F, 400.0F);
      var2.drawString("Use the primary gamepad to control the blob, and hit a gamepad button to change the color", 10.0F, 90.0F);

      for(int var3 = 0; var3 < this.lines.size(); ++var3) {
         InputTest.Line var4 = (InputTest.Line)this.lines.get(var3);
         var4.draw(var2);
      }

      var2.setColor(this.cols[this.index]);
      var2.fillOval((float)((int)this.x), (float)((int)this.y), 50.0F, 50.0F);
      var2.setColor(Color.yellow);
      var2.fillRect(50.0F, (float)(200 + this.ypos), 40.0F, 40.0F);
   }

   public void update(GameContainer var1, int var2) {
      this.lshift = var1.getInput().isKeyDown(42);
      this.rshift = var1.getInput().isKeyDown(54);
      this.space = var1.getInput().isKeyDown(57);
      if (this.controllerLeft[0]) {
         this.x -= (float)var2 * 0.1F;
      }

      if (this.controllerRight[0]) {
         this.x += (float)var2 * 0.1F;
      }

      if (this.controllerUp[0]) {
         this.y -= (float)var2 * 0.1F;
      }

      if (this.controllerDown[0]) {
         this.y += (float)var2 * 0.1F;
      }

   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 1) {
         System.exit(0);
      }

      if (var1 == 59 && this.app != null) {
         try {
            this.app.setDisplayMode(600, 600, false);
            this.app.reinit();
         } catch (Exception var4) {
            Log.error((Throwable)var4);
         }
      }

   }

   public void keyReleased(int var1, char var2) {
      this.message = "You pressed key code " + var1 + " (character = " + var2 + ")";
   }

   public void mousePressed(int var1, int var2, int var3) {
      if (var1 == 0) {
         this.buttonDown = true;
      }

      this.message = "Mouse pressed " + var1 + " " + var2 + "," + var3;
   }

   public void mouseReleased(int var1, int var2, int var3) {
      if (var1 == 0) {
         this.buttonDown = false;
      }

      this.message = "Mouse released " + var1 + " " + var2 + "," + var3;
   }

   public void mouseClicked(int var1, int var2, int var3, int var4) {
      System.out.println("CLICKED:" + var2 + "," + var3 + " " + var4);
   }

   public void mouseWheelMoved(int var1) {
      this.message = "Mouse wheel moved: " + var1;
      if (var1 < 0) {
         this.ypos -= 10;
      }

      if (var1 > 0) {
         this.ypos += 10;
      }

   }

   public void mouseMoved(int var1, int var2, int var3, int var4) {
      if (this.buttonDown) {
         this.lines.add(new InputTest.Line(this, var1, var2, var3, var4));
      }

   }

   public void controllerButtonPressed(int var1, int var2) {
      super.controllerButtonPressed(var1, var2);
      ++this.index;
      this.index %= this.cols.length;
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new InputTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }

   private class Line {
      private int oldx;
      private int oldy;
      private int newx;
      private int newy;
      private final InputTest this$0;

      public Line(InputTest var1, int var2, int var3, int var4, int var5) {
         this.this$0 = var1;
         this.oldx = var2;
         this.oldy = var3;
         this.newx = var4;
         this.newy = var5;
      }

      public void draw(Graphics var1) {
         var1.drawLine((float)this.oldx, (float)this.oldy, (float)this.newx, (float)this.newy);
      }
   }
}
