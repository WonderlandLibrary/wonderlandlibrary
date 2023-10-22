package me.theresa.fontRenderer.font;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import me.theresa.fontRenderer.font.impl.Font;
import me.theresa.fontRenderer.font.log.Log;
import me.theresa.fontRenderer.font.opengl.renderer.Renderer;
import me.theresa.fontRenderer.font.opengl.renderer.SGL;
import me.theresa.fontRenderer.font.util.ResourceLoader;

public class AngelCodeFont implements Font {
   private static final SGL GL = Renderer.get();
   private static final int DISPLAY_LIST_CACHE_SIZE = 200;
   private static final int MAX_CHAR = 255;
   private boolean displayListCaching = true;
   private final Image fontImage;
   private CharDef[] chars;
   private int lineHeight;
   private int baseDisplayListID = -1;
   private int eldestDisplayListID;
   private DisplayList eldestDisplayList;
   private final LinkedHashMap displayLists = new LinkedHashMap(200, 1.0F, true) {
      protected boolean removeEldestEntry(Map.Entry eldest) {
         AngelCodeFont.this.eldestDisplayList = (DisplayList)eldest.getValue();
         AngelCodeFont.this.eldestDisplayListID = AngelCodeFont.this.eldestDisplayList.id;
         return false;
      }
   };

   public AngelCodeFont(String fntFile, Image image) throws SlickException {
      this.fontImage = image;
      this.parseFnt(ResourceLoader.getResourceAsStream(fntFile));
   }

   public AngelCodeFont(String fntFile, String imgFile) throws SlickException {
      this.fontImage = new Image(imgFile);
      this.parseFnt(ResourceLoader.getResourceAsStream(fntFile));
   }

   public AngelCodeFont(String fntFile, Image image, boolean caching) throws SlickException {
      this.fontImage = image;
      this.displayListCaching = caching;
      this.parseFnt(ResourceLoader.getResourceAsStream(fntFile));
   }

   public AngelCodeFont(String fntFile, String imgFile, boolean caching) throws SlickException {
      this.fontImage = new Image(imgFile);
      this.displayListCaching = caching;
      this.parseFnt(ResourceLoader.getResourceAsStream(fntFile));
   }

   public AngelCodeFont(String name, InputStream fntFile, InputStream imgFile) throws SlickException {
      this.fontImage = new Image(imgFile, name, false);
      this.parseFnt(fntFile);
   }

   public AngelCodeFont(String name, InputStream fntFile, InputStream imgFile, boolean caching) throws SlickException {
      this.fontImage = new Image(imgFile, name, false);
      this.displayListCaching = caching;
      this.parseFnt(fntFile);
   }

   private void parseFnt(InputStream fntFile) throws SlickException {
      if (this.displayListCaching) {
         this.baseDisplayListID = GL.glGenLists(200);
         if (this.baseDisplayListID == 0) {
            this.displayListCaching = false;
         }
      }

      try {
         BufferedReader in = new BufferedReader(new InputStreamReader(fntFile));
         String info = in.readLine();
         String common = in.readLine();
         String page = in.readLine();
         Map kerning = new HashMap(64);
         List charDefs = new ArrayList(255);
         int maxChar = 0;
         boolean done = false;

         while(!done) {
            String line = in.readLine();
            if (line == null) {
               done = true;
            } else {
               if (!line.startsWith("chars c") && line.startsWith("char")) {
                  CharDef def = this.parseChar(line);
                  if (def != null) {
                     maxChar = Math.max(maxChar, def.id);
                     charDefs.add(def);
                  }
               }

               if (!line.startsWith("kernings c") && line.startsWith("kerning")) {
                  StringTokenizer tokens = new StringTokenizer(line, " =");
                  tokens.nextToken();
                  tokens.nextToken();
                  short first = Short.parseShort(tokens.nextToken());
                  tokens.nextToken();
                  int second = Integer.parseInt(tokens.nextToken());
                  tokens.nextToken();
                  int offset = Integer.parseInt(tokens.nextToken());
                  List values = (List)kerning.get(first);
                  if (values == null) {
                     values = new ArrayList();
                     kerning.put(first, values);
                  }

                  ((List)values).add((short)(offset << 8 | second));
               }
            }
         }

         this.chars = new CharDef[maxChar + 1];

         Iterator var19;
         Object o;
         CharDef def;
         for(var19 = charDefs.iterator(); var19.hasNext(); this.chars[def.id] = def) {
            o = var19.next();
            def = (CharDef)o;
         }

         short first;
         short[] valueArray;
         for(var19 = kerning.entrySet().iterator(); var19.hasNext(); this.chars[first].kerning = valueArray) {
            o = var19.next();
            Map.Entry entry = (Map.Entry)o;
            first = (Short)entry.getKey();
            List valueList = (List)entry.getValue();
            valueArray = new short[valueList.size()];
            int i = 0;

            for(Iterator valueIter = valueList.iterator(); valueIter.hasNext(); ++i) {
               valueArray[i] = (Short)valueIter.next();
            }
         }

      } catch (IOException var18) {
         Log.error((Throwable)var18);
         throw new SlickException("Failed to parse font file: " + fntFile);
      }
   }

   private CharDef parseChar(String line) throws SlickException {
      CharDef def = new CharDef();
      StringTokenizer tokens = new StringTokenizer(line, " =");
      tokens.nextToken();
      tokens.nextToken();
      def.id = Short.parseShort(tokens.nextToken());
      if (def.id < 0) {
         return null;
      } else if (def.id > 255) {
         throw new SlickException("Invalid character '" + def.id + "': AngelCodeFont does not support characters above " + 255);
      } else {
         tokens.nextToken();
         def.x = Short.parseShort(tokens.nextToken());
         tokens.nextToken();
         def.y = Short.parseShort(tokens.nextToken());
         tokens.nextToken();
         def.width = Short.parseShort(tokens.nextToken());
         tokens.nextToken();
         def.height = Short.parseShort(tokens.nextToken());
         tokens.nextToken();
         def.xoffset = Short.parseShort(tokens.nextToken());
         tokens.nextToken();
         def.yoffset = Short.parseShort(tokens.nextToken());
         tokens.nextToken();
         def.xadvance = Short.parseShort(tokens.nextToken());
         def.init();
         if (def.id != 32) {
            this.lineHeight = Math.max(def.height + def.yoffset, this.lineHeight);
         }

         return def;
      }
   }

   public void drawString(float x, float y, String text) {
      this.drawString(x, y, text, Color.white);
   }

   public void drawString(float x, float y, String text, Color col) {
      this.drawString(x, y, text, col, 0, text.length() - 1);
   }

   public void drawString(float x, float y, String text, Color col, int startIndex, int endIndex) {
      this.fontImage.bind();
      col.bind();
      GL.glTranslatef(x, y, 0.0F);
      if (this.displayListCaching && startIndex == 0 && endIndex == text.length() - 1) {
         DisplayList displayList = (DisplayList)this.displayLists.get(text);
         if (displayList != null) {
            GL.glCallList(displayList.id);
         } else {
            displayList = new DisplayList();
            displayList.text = text;
            int displayListCount = this.displayLists.size();
            if (displayListCount < 200) {
               displayList.id = this.baseDisplayListID + displayListCount;
            } else {
               displayList.id = this.eldestDisplayListID;
               this.displayLists.remove(this.eldestDisplayList.text);
            }

            this.displayLists.put(text, displayList);
            GL.glNewList(displayList.id, 4865);
            this.render(text, startIndex, endIndex);
            GL.glEndList();
         }
      } else {
         this.render(text, startIndex, endIndex);
      }

      GL.glTranslatef(-x, -y, 0.0F);
   }

   private void render(String text, int start, int end) {
      GL.glBegin(7);
      int x = 0;
      int y = 0;
      CharDef lastCharDef = null;
      char[] data = text.toCharArray();

      for(int i = 0; i < data.length; ++i) {
         int id = data[i];
         if (id == '\n') {
            x = 0;
            y += this.getLineHeight();
         } else if (id < this.chars.length) {
            CharDef charDef = this.chars[id];
            if (charDef != null) {
               if (lastCharDef != null) {
                  x += lastCharDef.getKerning(id);
               }

               lastCharDef = charDef;
               if (i >= start && i <= end) {
                  charDef.draw((float)x, (float)y);
               }

               x += charDef.xadvance;
            }
         }
      }

      GL.glEnd();
   }

   public int getYOffset(String text) {
      DisplayList displayList = null;
      if (this.displayListCaching) {
         displayList = (DisplayList)this.displayLists.get(text);
         if (displayList != null && displayList.yOffset != null) {
            return displayList.yOffset.intValue();
         }
      }

      int stopIndex = text.indexOf(10);
      if (stopIndex == -1) {
         stopIndex = text.length();
      }

      int minYOffset = 10000;

      for(int i = 0; i < stopIndex; ++i) {
         int id = text.charAt(i);
         CharDef charDef = this.chars[id];
         if (charDef != null) {
            minYOffset = Math.min(charDef.yoffset, minYOffset);
         }
      }

      if (displayList != null) {
         displayList.yOffset = (short)minYOffset;
      }

      return minYOffset;
   }

   public int getHeight(String text) {
      DisplayList displayList = null;
      if (this.displayListCaching) {
         displayList = (DisplayList)this.displayLists.get(text);
         if (displayList != null && displayList.height != null) {
            return displayList.height.intValue();
         }
      }

      int lines = 0;
      int maxHeight = 0;

      for(int i = 0; i < text.length(); ++i) {
         int id = text.charAt(i);
         if (id == '\n') {
            ++lines;
            maxHeight = 0;
         } else if (id != ' ') {
            CharDef charDef = this.chars[id];
            if (charDef != null) {
               maxHeight = Math.max(charDef.height + charDef.yoffset, maxHeight);
            }
         }
      }

      maxHeight += lines * this.getLineHeight();
      if (displayList != null) {
         displayList.height = (short)maxHeight;
      }

      return maxHeight;
   }

   public int getWidth(String text) {
      DisplayList displayList = null;
      if (this.displayListCaching) {
         displayList = (DisplayList)this.displayLists.get(text);
         if (displayList != null && displayList.width != null) {
            return displayList.width.intValue();
         }
      }

      int maxWidth = 0;
      int width = 0;
      CharDef lastCharDef = null;
      int i = 0;

      for(int n = text.length(); i < n; ++i) {
         int id = text.charAt(i);
         if (id == '\n') {
            width = 0;
         } else if (id < this.chars.length) {
            CharDef charDef = this.chars[id];
            if (charDef != null) {
               if (lastCharDef != null) {
                  width += lastCharDef.getKerning(id);
               }

               lastCharDef = charDef;
               if (i < n - 1) {
                  width += charDef.xadvance;
               } else {
                  width += charDef.width;
               }

               maxWidth = Math.max(maxWidth, width);
            }
         }
      }

      if (displayList != null) {
         displayList.width = (short)maxWidth;
      }

      return maxWidth;
   }

   public int getLineHeight() {
      return this.lineHeight;
   }

   private static class DisplayList {
      int id;
      Short yOffset;
      Short width;
      Short height;
      String text;

      private DisplayList() {
      }

      // $FF: synthetic method
      DisplayList(Object x0) {
         this();
      }
   }

   private class CharDef {
      public short id;
      public short x;
      public short y;
      public short width;
      public short height;
      public short xoffset;
      public short yoffset;
      public short xadvance;
      public Image image;
      public short dlIndex;
      public short[] kerning;

      private CharDef() {
      }

      public void init() {
         this.image = AngelCodeFont.this.fontImage.getSubImage(this.x, this.y, this.width, this.height);
      }

      public String toString() {
         return "[CharDef id=" + this.id + " x=" + this.x + " y=" + this.y + "]";
      }

      public void draw(float x, float y) {
         this.image.drawEmbedded(x + (float)this.xoffset, y + (float)this.yoffset, (float)this.width, (float)this.height);
      }

      public int getKerning(int otherCodePoint) {
         if (this.kerning == null) {
            return 0;
         } else {
            int low = 0;
            int high = this.kerning.length - 1;

            while(low <= high) {
               int midIndex = low + high >>> 1;
               int value = this.kerning[midIndex];
               int foundCodePoint = value & 255;
               if (foundCodePoint < otherCodePoint) {
                  low = midIndex + 1;
               } else {
                  if (foundCodePoint <= otherCodePoint) {
                     return value >> 8;
                  }

                  high = midIndex - 1;
               }
            }

            return 0;
         }
      }

      // $FF: synthetic method
      CharDef(Object x1) {
         this();
      }
   }
}
