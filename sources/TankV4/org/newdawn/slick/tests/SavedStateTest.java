package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SavedState;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;

public class SavedStateTest extends BasicGame implements ComponentListener {
   private TextField name;
   private TextField age;
   private String nameValue = "none";
   private int ageValue = 0;
   private SavedState state;
   private String message = "Enter a name and age to store";
   private static AppGameContainer container;

   public SavedStateTest() {
      super("Saved State Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.state = new SavedState("testdata");
      this.nameValue = this.state.getString("name", "DefaultName");
      this.ageValue = (int)this.state.getNumber("age", 64.0D);
      this.name = new TextField(var1, var1.getDefaultFont(), 100, 100, 300, 20, this);
      this.age = new TextField(var1, var1.getDefaultFont(), 100, 150, 201, 20, this);
   }

   public void render(GameContainer var1, Graphics var2) {
      this.name.render(var1, var2);
      this.age.render(var1, var2);
      var1.getDefaultFont().drawString(100.0F, 300.0F, "Stored Name: " + this.nameValue);
      var1.getDefaultFont().drawString(100.0F, 350.0F, "Stored Age: " + this.ageValue);
      var1.getDefaultFont().drawString(200.0F, 500.0F, this.message);
   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 1) {
         System.exit(0);
      }

   }

   public static void main(String[] var0) {
      try {
         container = new AppGameContainer(new SavedStateTest());
         container.setDisplayMode(800, 600, false);
         container.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }

   public void componentActivated(AbstractComponent var1) {
      if (var1 == this.name) {
         this.nameValue = this.name.getText();
         this.state.setString("name", this.nameValue);
      }

      if (var1 == this.age) {
         try {
            this.ageValue = Integer.parseInt(this.age.getText());
            this.state.setNumber("age", (double)this.ageValue);
         } catch (NumberFormatException var4) {
         }
      }

      try {
         this.state.save();
      } catch (Exception var3) {
         this.message = System.currentTimeMillis() + " : Failed to save state";
      }

   }
}
