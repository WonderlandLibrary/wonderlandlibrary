package cn.hanabi.gui.font;

import org.lwjgl.opengl.GL11;

public class VertexCache {
   private int list;
   private long lastUsed;
   private char char_;
   private int width;

   public VertexCache(char char__, int list_, int width_) {
      this.char_ = char__;
      this.list = list_;
      this.width = width_;
   }

   public char getChar() {
      return this.char_;
   }

   public int getWidth() {
      return this.width;
   }

   public void render() {
      GL11.glCallList(this.list);
      GL11.glCallList(this.list);
      this.lastUsed = System.currentTimeMillis();
   }

   public boolean checkTimeNotUsed(long time) {
      return System.currentTimeMillis() - this.lastUsed > time;
   }

   public void destroy() {
      GL11.glDeleteLists(this.list, 1);
   }
}
