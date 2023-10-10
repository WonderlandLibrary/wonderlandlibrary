package org.newdawn.slick.tests;

import java.util.ArrayList;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.util.Log;

public class TestBox extends BasicGame {
   private ArrayList games = new ArrayList();
   private BasicGame currentGame;
   private int index;
   private AppGameContainer container;
   static Class class$org$newdawn$slick$tests$AnimationTest;
   static Class class$org$newdawn$slick$tests$AntiAliasTest;
   static Class class$org$newdawn$slick$tests$BigImageTest;
   static Class class$org$newdawn$slick$tests$ClipTest;
   static Class class$org$newdawn$slick$tests$DuplicateEmitterTest;
   static Class class$org$newdawn$slick$tests$FlashTest;
   static Class class$org$newdawn$slick$tests$FontPerformanceTest;
   static Class class$org$newdawn$slick$tests$FontTest;
   static Class class$org$newdawn$slick$tests$GeomTest;
   static Class class$org$newdawn$slick$tests$GradientTest;
   static Class class$org$newdawn$slick$tests$GraphicsTest;
   static Class class$org$newdawn$slick$tests$ImageBufferTest;
   static Class class$org$newdawn$slick$tests$ImageReadTest;
   static Class class$org$newdawn$slick$tests$ImageTest;
   static Class class$org$newdawn$slick$tests$KeyRepeatTest;
   static Class class$org$newdawn$slick$tests$MusicListenerTest;
   static Class class$org$newdawn$slick$tests$PackedSheetTest;
   static Class class$org$newdawn$slick$tests$PedigreeTest;
   static Class class$org$newdawn$slick$tests$PureFontTest;
   static Class class$org$newdawn$slick$tests$ShapeTest;
   static Class class$org$newdawn$slick$tests$SoundTest;
   static Class class$org$newdawn$slick$tests$SpriteSheetFontTest;
   static Class class$org$newdawn$slick$tests$TransparentColorTest;

   public TestBox() {
      super("Test Box");
   }

   public void addGame(Class var1) {
      this.games.add(var1);
   }

   private void nextGame() {
      if (this.index != -1) {
         ++this.index;
         if (this.index >= this.games.size()) {
            this.index = 0;
         }

         this.startGame();
      }
   }

   private void startGame() {
      try {
         this.currentGame = (BasicGame)((Class)this.games.get(this.index)).newInstance();
         this.container.getGraphics().setBackground(Color.black);
         this.currentGame.init(this.container);
         this.currentGame.render(this.container, this.container.getGraphics());
      } catch (Exception var2) {
         Log.error((Throwable)var2);
      }

      this.container.setTitle(this.currentGame.getTitle());
   }

   public void init(GameContainer var1) throws SlickException {
      if (this.games.size() == 0) {
         this.currentGame = new BasicGame(this, "NULL") {
            private final TestBox this$0;

            {
               this.this$0 = var1;
            }

            public void init(GameContainer var1) throws SlickException {
            }

            public void update(GameContainer var1, int var2) throws SlickException {
            }

            public void render(GameContainer var1, Graphics var2) throws SlickException {
            }
         };
         this.currentGame.init(var1);
         this.index = -1;
      } else {
         this.index = 0;
         this.container = (AppGameContainer)var1;
         this.startGame();
      }

   }

   public void update(GameContainer var1, int var2) throws SlickException {
      this.currentGame.update(var1, var2);
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      SlickCallable.enterSafeBlock();
      this.currentGame.render(var1, var2);
      SlickCallable.leaveSafeBlock();
   }

   public void controllerButtonPressed(int var1, int var2) {
      this.currentGame.controllerButtonPressed(var1, var2);
   }

   public void controllerButtonReleased(int var1, int var2) {
      this.currentGame.controllerButtonReleased(var1, var2);
   }

   public void controllerDownPressed(int var1) {
      this.currentGame.controllerDownPressed(var1);
   }

   public void controllerDownReleased(int var1) {
      this.currentGame.controllerDownReleased(var1);
   }

   public void controllerLeftPressed(int var1) {
      this.currentGame.controllerLeftPressed(var1);
   }

   public void controllerLeftReleased(int var1) {
      this.currentGame.controllerLeftReleased(var1);
   }

   public void controllerRightPressed(int var1) {
      this.currentGame.controllerRightPressed(var1);
   }

   public void controllerRightReleased(int var1) {
      this.currentGame.controllerRightReleased(var1);
   }

   public void controllerUpPressed(int var1) {
      this.currentGame.controllerUpPressed(var1);
   }

   public void controllerUpReleased(int var1) {
      this.currentGame.controllerUpReleased(var1);
   }

   public void keyPressed(int var1, char var2) {
      this.currentGame.keyPressed(var1, var2);
      if (var1 == 28) {
         this.nextGame();
      }

   }

   public void keyReleased(int var1, char var2) {
      this.currentGame.keyReleased(var1, var2);
   }

   public void mouseMoved(int var1, int var2, int var3, int var4) {
      this.currentGame.mouseMoved(var1, var2, var3, var4);
   }

   public void mousePressed(int var1, int var2, int var3) {
      this.currentGame.mousePressed(var1, var2, var3);
   }

   public void mouseReleased(int var1, int var2, int var3) {
      this.currentGame.mouseReleased(var1, var2, var3);
   }

   public void mouseWheelMoved(int var1) {
      this.currentGame.mouseWheelMoved(var1);
   }

   public static void main(String[] var0) {
      try {
         TestBox var1 = new TestBox();
         var1.addGame(class$org$newdawn$slick$tests$AnimationTest == null ? (class$org$newdawn$slick$tests$AnimationTest = class$("org.newdawn.slick.tests.AnimationTest")) : class$org$newdawn$slick$tests$AnimationTest);
         var1.addGame(class$org$newdawn$slick$tests$AntiAliasTest == null ? (class$org$newdawn$slick$tests$AntiAliasTest = class$("org.newdawn.slick.tests.AntiAliasTest")) : class$org$newdawn$slick$tests$AntiAliasTest);
         var1.addGame(class$org$newdawn$slick$tests$BigImageTest == null ? (class$org$newdawn$slick$tests$BigImageTest = class$("org.newdawn.slick.tests.BigImageTest")) : class$org$newdawn$slick$tests$BigImageTest);
         var1.addGame(class$org$newdawn$slick$tests$ClipTest == null ? (class$org$newdawn$slick$tests$ClipTest = class$("org.newdawn.slick.tests.ClipTest")) : class$org$newdawn$slick$tests$ClipTest);
         var1.addGame(class$org$newdawn$slick$tests$DuplicateEmitterTest == null ? (class$org$newdawn$slick$tests$DuplicateEmitterTest = class$("org.newdawn.slick.tests.DuplicateEmitterTest")) : class$org$newdawn$slick$tests$DuplicateEmitterTest);
         var1.addGame(class$org$newdawn$slick$tests$FlashTest == null ? (class$org$newdawn$slick$tests$FlashTest = class$("org.newdawn.slick.tests.FlashTest")) : class$org$newdawn$slick$tests$FlashTest);
         var1.addGame(class$org$newdawn$slick$tests$FontPerformanceTest == null ? (class$org$newdawn$slick$tests$FontPerformanceTest = class$("org.newdawn.slick.tests.FontPerformanceTest")) : class$org$newdawn$slick$tests$FontPerformanceTest);
         var1.addGame(class$org$newdawn$slick$tests$FontTest == null ? (class$org$newdawn$slick$tests$FontTest = class$("org.newdawn.slick.tests.FontTest")) : class$org$newdawn$slick$tests$FontTest);
         var1.addGame(class$org$newdawn$slick$tests$GeomTest == null ? (class$org$newdawn$slick$tests$GeomTest = class$("org.newdawn.slick.tests.GeomTest")) : class$org$newdawn$slick$tests$GeomTest);
         var1.addGame(class$org$newdawn$slick$tests$GradientTest == null ? (class$org$newdawn$slick$tests$GradientTest = class$("org.newdawn.slick.tests.GradientTest")) : class$org$newdawn$slick$tests$GradientTest);
         var1.addGame(class$org$newdawn$slick$tests$GraphicsTest == null ? (class$org$newdawn$slick$tests$GraphicsTest = class$("org.newdawn.slick.tests.GraphicsTest")) : class$org$newdawn$slick$tests$GraphicsTest);
         var1.addGame(class$org$newdawn$slick$tests$ImageBufferTest == null ? (class$org$newdawn$slick$tests$ImageBufferTest = class$("org.newdawn.slick.tests.ImageBufferTest")) : class$org$newdawn$slick$tests$ImageBufferTest);
         var1.addGame(class$org$newdawn$slick$tests$ImageReadTest == null ? (class$org$newdawn$slick$tests$ImageReadTest = class$("org.newdawn.slick.tests.ImageReadTest")) : class$org$newdawn$slick$tests$ImageReadTest);
         var1.addGame(class$org$newdawn$slick$tests$ImageTest == null ? (class$org$newdawn$slick$tests$ImageTest = class$("org.newdawn.slick.tests.ImageTest")) : class$org$newdawn$slick$tests$ImageTest);
         var1.addGame(class$org$newdawn$slick$tests$KeyRepeatTest == null ? (class$org$newdawn$slick$tests$KeyRepeatTest = class$("org.newdawn.slick.tests.KeyRepeatTest")) : class$org$newdawn$slick$tests$KeyRepeatTest);
         var1.addGame(class$org$newdawn$slick$tests$MusicListenerTest == null ? (class$org$newdawn$slick$tests$MusicListenerTest = class$("org.newdawn.slick.tests.MusicListenerTest")) : class$org$newdawn$slick$tests$MusicListenerTest);
         var1.addGame(class$org$newdawn$slick$tests$PackedSheetTest == null ? (class$org$newdawn$slick$tests$PackedSheetTest = class$("org.newdawn.slick.tests.PackedSheetTest")) : class$org$newdawn$slick$tests$PackedSheetTest);
         var1.addGame(class$org$newdawn$slick$tests$PedigreeTest == null ? (class$org$newdawn$slick$tests$PedigreeTest = class$("org.newdawn.slick.tests.PedigreeTest")) : class$org$newdawn$slick$tests$PedigreeTest);
         var1.addGame(class$org$newdawn$slick$tests$PureFontTest == null ? (class$org$newdawn$slick$tests$PureFontTest = class$("org.newdawn.slick.tests.PureFontTest")) : class$org$newdawn$slick$tests$PureFontTest);
         var1.addGame(class$org$newdawn$slick$tests$ShapeTest == null ? (class$org$newdawn$slick$tests$ShapeTest = class$("org.newdawn.slick.tests.ShapeTest")) : class$org$newdawn$slick$tests$ShapeTest);
         var1.addGame(class$org$newdawn$slick$tests$SoundTest == null ? (class$org$newdawn$slick$tests$SoundTest = class$("org.newdawn.slick.tests.SoundTest")) : class$org$newdawn$slick$tests$SoundTest);
         var1.addGame(class$org$newdawn$slick$tests$SpriteSheetFontTest == null ? (class$org$newdawn$slick$tests$SpriteSheetFontTest = class$("org.newdawn.slick.tests.SpriteSheetFontTest")) : class$org$newdawn$slick$tests$SpriteSheetFontTest);
         var1.addGame(class$org$newdawn$slick$tests$TransparentColorTest == null ? (class$org$newdawn$slick$tests$TransparentColorTest = class$("org.newdawn.slick.tests.TransparentColorTest")) : class$org$newdawn$slick$tests$TransparentColorTest);
         AppGameContainer var2 = new AppGameContainer(var1);
         var2.setDisplayMode(800, 600, false);
         var2.start();
      } catch (SlickException var3) {
         var3.printStackTrace();
      }

   }

   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw (new NoClassDefFoundError()).initCause(var2);
      }
   }
}
