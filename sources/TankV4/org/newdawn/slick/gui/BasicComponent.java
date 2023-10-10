package org.newdawn.slick.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/** @deprecated */
public abstract class BasicComponent extends AbstractComponent {
   protected int x;
   protected int y;
   protected int width;
   protected int height;

   public BasicComponent(GUIContext var1) {
      super(var1);
   }

   public int getHeight() {
      return this.height;
   }

   public int getWidth() {
      return this.width;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public abstract void renderImpl(GUIContext var1, Graphics var2);

   public void render(GUIContext var1, Graphics var2) throws SlickException {
      this.renderImpl(var1, var2);
   }

   public void setLocation(int var1, int var2) {
      this.x = var1;
      this.y = var2;
   }
}
