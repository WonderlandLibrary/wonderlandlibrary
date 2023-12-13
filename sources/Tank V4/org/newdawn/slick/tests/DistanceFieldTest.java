package org.newdawn.slick.tests;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class DistanceFieldTest extends BasicGame {
   private AngelCodeFont font;

   public DistanceFieldTest() {
      super("DistanceMapTest Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.font = new AngelCodeFont("testdata/distance.fnt", "testdata/distance-dis.png");
      var1.getGraphics().setBackground(Color.black);
   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      String var3 = "abc";
      this.font.drawString(610.0F, 100.0F, var3);
      GL11.glDisable(3042);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(518, 0.5F);
      this.font.drawString(610.0F, 150.0F, var3);
      GL11.glDisable(3008);
      GL11.glEnable(3042);
      var2.translate(-50.0F, -130.0F);
      var2.scale(10.0F, 10.0F);
      this.font.drawString(0.0F, 0.0F, var3);
      GL11.glDisable(3042);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(518, 0.5F);
      this.font.drawString(0.0F, 26.0F, var3);
      GL11.glDisable(3008);
      GL11.glEnable(3042);
      var2.resetTransform();
      var2.setColor(Color.lightGray);
      var2.drawString("Original Size on Sheet", 620.0F, 210.0F);
      var2.drawString("10x Scale Up", 40.0F, 575.0F);
      var2.setColor(Color.darkGray);
      var2.drawRect(40.0F, 40.0F, 560.0F, 530.0F);
      var2.drawRect(610.0F, 105.0F, 150.0F, 100.0F);
      var2.setColor(Color.white);
      var2.drawString("512x512 Font Sheet", 620.0F, 300.0F);
      var2.drawString("NEHE Charset", 620.0F, 320.0F);
      var2.drawString("4096x4096 (8x) Source Image", 620.0F, 340.0F);
      var2.drawString("ScanSize = 20", 620.0F, 360.0F);
   }

   public void keyPressed(int var1, char var2) {
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new DistanceFieldTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
