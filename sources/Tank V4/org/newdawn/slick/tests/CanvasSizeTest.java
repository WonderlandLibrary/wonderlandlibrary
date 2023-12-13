package org.newdawn.slick.tests;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

public class CanvasSizeTest extends BasicGame {
   public CanvasSizeTest() {
      super("Test");
   }

   public void init(GameContainer var1) throws SlickException {
      System.out.println(var1.getWidth() + ", " + var1.getHeight());
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   public static void main(String[] var0) {
      try {
         CanvasGameContainer var1 = new CanvasGameContainer(new CanvasSizeTest());
         var1.setSize(640, 480);
         Frame var2 = new Frame("Test");
         var2.setLayout(new GridLayout(1, 2));
         var2.add(var1);
         var2.pack();
         var2.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent var1) {
               System.exit(0);
            }
         });
         var2.setVisible(true);
         var1.start();
      } catch (Exception var3) {
         Log.error((Throwable)var3);
      }

   }
}
