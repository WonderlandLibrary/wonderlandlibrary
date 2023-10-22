package me.theresa.fontRenderer.font.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.theresa.fontRenderer.font.SlickException;
import me.theresa.fontRenderer.font.effect.ConfigurableEffect;

public class HieroSettings {
   private int fontSize;
   private boolean bold;
   private boolean italic;
   private int paddingTop;
   private int paddingLeft;
   private int paddingBottom;
   private int paddingRight;
   private int paddingAdvanceX;
   private int paddingAdvanceY;
   private int glyphPageWidth;
   private int glyphPageHeight;
   private final List effects;

   public HieroSettings() {
      this.fontSize = 12;
      this.bold = false;
      this.italic = false;
      this.glyphPageWidth = 512;
      this.glyphPageHeight = 512;
      this.effects = new ArrayList();
   }

   public HieroSettings(String hieroFileRef) throws SlickException {
      this(ResourceLoader.getResourceAsStream(hieroFileRef));
   }

   public HieroSettings(InputStream in) throws SlickException {
      this.fontSize = 12;
      this.bold = false;
      this.italic = false;
      this.glyphPageWidth = 512;
      this.glyphPageHeight = 512;
      this.effects = new ArrayList();

      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(in));

         while(true) {
            while(true) {
               String line;
               do {
                  line = reader.readLine();
                  if (line == null) {
                     reader.close();
                     return;
                  }

                  line = line.trim();
               } while(line.length() == 0);

               String[] pieces = line.split("=", 2);
               String name = pieces[0].trim();
               String value = pieces[1];
               if (name.equals("font.size")) {
                  this.fontSize = Integer.parseInt(value);
               } else if (name.equals("font.bold")) {
                  this.bold = Boolean.parseBoolean(value);
               } else if (name.equals("font.italic")) {
                  this.italic = Boolean.parseBoolean(value);
               } else if (name.equals("pad.top")) {
                  this.paddingTop = Integer.parseInt(value);
               } else if (name.equals("pad.right")) {
                  this.paddingRight = Integer.parseInt(value);
               } else if (name.equals("pad.bottom")) {
                  this.paddingBottom = Integer.parseInt(value);
               } else if (name.equals("pad.left")) {
                  this.paddingLeft = Integer.parseInt(value);
               } else if (name.equals("pad.advance.x")) {
                  this.paddingAdvanceX = Integer.parseInt(value);
               } else if (name.equals("pad.advance.y")) {
                  this.paddingAdvanceY = Integer.parseInt(value);
               } else if (name.equals("glyph.page.width")) {
                  this.glyphPageWidth = Integer.parseInt(value);
               } else if (name.equals("glyph.page.height")) {
                  this.glyphPageHeight = Integer.parseInt(value);
               } else if (name.equals("effect.class")) {
                  try {
                     this.effects.add(Class.forName(value).newInstance());
                  } catch (Exception var12) {
                     throw new SlickException("Unable to create effect instance: " + value, var12);
                  }
               } else if (name.startsWith("effect.")) {
                  name = name.substring(7);
                  ConfigurableEffect effect = (ConfigurableEffect)this.effects.get(this.effects.size() - 1);
                  List values = effect.getValues();
                  Iterator var9 = values.iterator();

                  while(var9.hasNext()) {
                     Object o = var9.next();
                     ConfigurableEffect.Value effectValue = (ConfigurableEffect.Value)o;
                     if (effectValue.getName().equals(name)) {
                        effectValue.setString(value);
                        break;
                     }
                  }

                  effect.setValues(values);
               }
            }
         }
      } catch (Exception var13) {
         throw new SlickException("Unable to load Hiero font file", var13);
      }
   }

   public int getPaddingTop() {
      return this.paddingTop;
   }

   public void setPaddingTop(int paddingTop) {
      this.paddingTop = paddingTop;
   }

   public int getPaddingLeft() {
      return this.paddingLeft;
   }

   public void setPaddingLeft(int paddingLeft) {
      this.paddingLeft = paddingLeft;
   }

   public int getPaddingBottom() {
      return this.paddingBottom;
   }

   public void setPaddingBottom(int paddingBottom) {
      this.paddingBottom = paddingBottom;
   }

   public int getPaddingRight() {
      return this.paddingRight;
   }

   public void setPaddingRight(int paddingRight) {
      this.paddingRight = paddingRight;
   }

   public int getPaddingAdvanceX() {
      return this.paddingAdvanceX;
   }

   public void setPaddingAdvanceX(int paddingAdvanceX) {
      this.paddingAdvanceX = paddingAdvanceX;
   }

   public int getPaddingAdvanceY() {
      return this.paddingAdvanceY;
   }

   public void setPaddingAdvanceY(int paddingAdvanceY) {
      this.paddingAdvanceY = paddingAdvanceY;
   }

   public int getGlyphPageWidth() {
      return this.glyphPageWidth;
   }

   public void setGlyphPageWidth(int glyphPageWidth) {
      this.glyphPageWidth = glyphPageWidth;
   }

   public int getGlyphPageHeight() {
      return this.glyphPageHeight;
   }

   public void setGlyphPageHeight(int glyphPageHeight) {
      this.glyphPageHeight = glyphPageHeight;
   }

   public int getFontSize() {
      return this.fontSize;
   }

   public void setFontSize(int fontSize) {
      this.fontSize = fontSize;
   }

   public boolean isBold() {
      return this.bold;
   }

   public void setBold(boolean bold) {
      this.bold = bold;
   }

   public boolean isItalic() {
      return this.italic;
   }

   public void setItalic(boolean italic) {
      this.italic = italic;
   }

   public List getEffects() {
      return this.effects;
   }

   public void save(File file) throws IOException {
      PrintStream out = new PrintStream(new FileOutputStream(file));
      out.println("font.size=" + this.fontSize);
      out.println("font.bold=" + this.bold);
      out.println("font.italic=" + this.italic);
      out.println();
      out.println("pad.top=" + this.paddingTop);
      out.println("pad.right=" + this.paddingRight);
      out.println("pad.bottom=" + this.paddingBottom);
      out.println("pad.left=" + this.paddingLeft);
      out.println("pad.advance.x=" + this.paddingAdvanceX);
      out.println("pad.advance.y=" + this.paddingAdvanceY);
      out.println();
      out.println("glyph.page.width=" + this.glyphPageWidth);
      out.println("glyph.page.height=" + this.glyphPageHeight);
      out.println();
      Iterator var3 = this.effects.iterator();

      while(var3.hasNext()) {
         Object item = var3.next();
         ConfigurableEffect effect = (ConfigurableEffect)item;
         out.println("effect.class=" + effect.getClass().getName());
         Iterator var6 = effect.getValues().iterator();

         while(var6.hasNext()) {
            Object o = var6.next();
            ConfigurableEffect.Value value = (ConfigurableEffect.Value)o;
            out.println("effect." + value.getName() + "=" + value.getString());
         }

         out.println();
      }

      out.close();
   }
}
