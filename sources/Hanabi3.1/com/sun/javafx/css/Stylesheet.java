package com.sun.javafx.css;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.css.parser.CSSParser;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.StyleOrigin;

public class Stylesheet {
   static final int BINARY_CSS_VERSION = 5;
   private final String url;
   private StyleOrigin origin;
   private final ObservableList rules;
   private final List fontFaces;
   private String[] stringStore;

   public String getUrl() {
      return this.url;
   }

   public StyleOrigin getOrigin() {
      return this.origin;
   }

   public void setOrigin(StyleOrigin var1) {
      this.origin = var1;
   }

   public Stylesheet() {
      this((String)null);
   }

   public Stylesheet(String var1) {
      this.origin = StyleOrigin.AUTHOR;
      this.rules = new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            var1.reset();

            while(true) {
               while(var1.next()) {
                  Iterator var2;
                  Rule var3;
                  if (var1.wasAdded()) {
                     var2 = var1.getAddedSubList().iterator();

                     while(var2.hasNext()) {
                        var3 = (Rule)var2.next();
                        var3.setStylesheet(Stylesheet.this);
                     }
                  } else if (var1.wasRemoved()) {
                     var2 = var1.getRemoved().iterator();

                     while(var2.hasNext()) {
                        var3 = (Rule)var2.next();
                        if (var3.getStylesheet() == Stylesheet.this) {
                           var3.setStylesheet((Stylesheet)null);
                        }
                     }
                  }
               }

               return;
            }
         }
      };
      this.fontFaces = new ArrayList();
      this.url = var1;
   }

   public List getRules() {
      return this.rules;
   }

   public List getFontFaces() {
      return this.fontFaces;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof Stylesheet) {
         Stylesheet var2 = (Stylesheet)var1;
         if (this.url == null && var2.url == null) {
            return true;
         } else {
            return this.url != null && var2.url != null ? this.url.equals(var2.url) : false;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = 7;
      var1 = 13 * var1 + (this.url != null ? this.url.hashCode() : 0);
      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("/* ");
      if (this.url != null) {
         var1.append(this.url);
      }

      if (this.rules.isEmpty()) {
         var1.append(" */");
      } else {
         var1.append(" */\n");

         for(int var2 = 0; var2 < this.rules.size(); ++var2) {
            var1.append(this.rules.get(var2));
            var1.append('\n');
         }
      }

      return var1.toString();
   }

   final void writeBinary(DataOutputStream var1, StringStore var2) throws IOException {
      int var3 = var2.addString(this.origin.name());
      var1.writeShort(var3);
      var1.writeShort(this.rules.size());
      Iterator var4 = this.rules.iterator();

      while(var4.hasNext()) {
         Rule var5 = (Rule)var4.next();
         var5.writeBinary(var1, var2);
      }

      List var8 = this.getFontFaces();
      int var9 = var8 != null ? var8.size() : 0;
      var1.writeShort(var9);

      for(int var6 = 0; var6 < var9; ++var6) {
         FontFace var7 = (FontFace)var8.get(var6);
         var7.writeBinary(var1, var2);
      }

   }

   final void readBinary(int var1, DataInputStream var2, String[] var3) throws IOException {
      this.stringStore = var3;
      short var4 = var2.readShort();
      this.setOrigin(StyleOrigin.valueOf(var3[var4]));
      short var5 = var2.readShort();
      ArrayList var6 = new ArrayList(var5);

      for(int var7 = 0; var7 < var5; ++var7) {
         var6.add(Rule.readBinary(var1, var2, var3));
      }

      this.rules.addAll(var6);
      if (var1 >= 5) {
         List var11 = this.getFontFaces();
         short var8 = var2.readShort();

         for(int var9 = 0; var9 < var8; ++var9) {
            FontFace var10 = FontFace.readBinary(var1, var2, var3);
            var11.add(var10);
         }
      }

   }

   final String[] getStringStore() {
      return this.stringStore;
   }

   public static Stylesheet loadBinary(URL var0) throws IOException {
      if (var0 == null) {
         return null;
      } else {
         Stylesheet var1 = null;

         try {
            DataInputStream var2 = new DataInputStream(new BufferedInputStream(var0.openStream(), 40960));
            Throwable var3 = null;

            try {
               short var4 = var2.readShort();
               if (var4 > 5) {
                  throw new IOException(var0.toString() + " wrong binary CSS version: " + var4 + ". Expected version less than or equal to" + 5);
               }

               String[] var5 = StringStore.readBinary(var2);
               var1 = new Stylesheet(var0.toExternalForm());

               try {
                  var2.mark(Integer.MAX_VALUE);
                  var1.readBinary(var4, var2, var5);
               } catch (Exception var16) {
                  var1 = new Stylesheet(var0.toExternalForm());
                  var2.reset();
                  if (var4 == 2) {
                     var1.readBinary(3, var2, var5);
                  } else {
                     var1.readBinary(5, var2, var5);
                  }
               }
            } catch (Throwable var17) {
               var3 = var17;
               throw var17;
            } finally {
               if (var2 != null) {
                  if (var3 != null) {
                     try {
                        var2.close();
                     } catch (Throwable var15) {
                        var3.addSuppressed(var15);
                     }
                  } else {
                     var2.close();
                  }
               }

            }
         } catch (FileNotFoundException var19) {
         }

         return var1;
      }
   }

   public static void convertToBinary(File var0, File var1) throws IOException {
      if (var0 != null && var1 != null) {
         if (var0.getAbsolutePath().equals(var1.getAbsolutePath())) {
            throw new IllegalArgumentException("source and destination may not be the same");
         } else if (!var0.canRead()) {
            throw new IllegalArgumentException("cannot read source file");
         } else {
            if (var1.exists()) {
               if (!var1.canWrite()) {
                  throw new IllegalArgumentException("cannot write destination file");
               }
            } else if (!var1.createNewFile()) {
               throw new IllegalArgumentException("cannot write destination file");
            }

            URI var2 = var0.toURI();
            Stylesheet var3 = (new CSSParser()).parse(var2.toURL());
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            DataOutputStream var5 = new DataOutputStream(var4);
            StringStore var6 = new StringStore();
            var3.writeBinary(var5, var6);
            var5.flush();
            var5.close();
            FileOutputStream var7 = new FileOutputStream(var1);
            DataOutputStream var8 = new DataOutputStream(var7);
            var8.writeShort(5);
            var6.writeBinary(var8);
            var8.write(var4.toByteArray());
            var8.flush();
            var8.close();
         }
      } else {
         throw new IllegalArgumentException("parameters may not be null");
      }
   }

   public void importStylesheet(Stylesheet var1) {
      if (var1 != null) {
         List var2 = var1.getRules();
         if (var2 != null && !var2.isEmpty()) {
            ArrayList var3 = new ArrayList(var2.size());
            Iterator var4 = var2.iterator();

            while(var4.hasNext()) {
               Rule var5 = (Rule)var4.next();
               ObservableList var6 = var5.getSelectors();
               List var7 = var5.getUnobservedDeclarationList();
               var3.add(new Rule(var6, var7));
            }

            this.rules.addAll(var3);
         }
      }
   }
}
