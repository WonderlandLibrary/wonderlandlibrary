package javafx.scene.input;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javafx.scene.image.Image;

public class ClipboardContent extends HashMap {
   public final boolean hasString() {
      return this.containsKey(DataFormat.PLAIN_TEXT);
   }

   public final boolean putString(String var1) {
      if (var1 == null) {
         this.remove(DataFormat.PLAIN_TEXT);
      } else {
         this.put(DataFormat.PLAIN_TEXT, var1);
      }

      return true;
   }

   public final String getString() {
      return (String)this.get(DataFormat.PLAIN_TEXT);
   }

   public final boolean hasUrl() {
      return this.containsKey(DataFormat.URL);
   }

   public final boolean putUrl(String var1) {
      if (var1 == null) {
         this.remove(DataFormat.URL);
      } else {
         this.put(DataFormat.URL, var1);
      }

      return true;
   }

   public final String getUrl() {
      return (String)this.get(DataFormat.URL);
   }

   public final boolean hasHtml() {
      return this.containsKey(DataFormat.HTML);
   }

   public final boolean putHtml(String var1) {
      if (var1 == null) {
         this.remove(DataFormat.HTML);
      } else {
         this.put(DataFormat.HTML, var1);
      }

      return true;
   }

   public final String getHtml() {
      return (String)this.get(DataFormat.HTML);
   }

   public final boolean hasRtf() {
      return this.containsKey(DataFormat.RTF);
   }

   public final boolean putRtf(String var1) {
      if (var1 == null) {
         this.remove(DataFormat.RTF);
      } else {
         this.put(DataFormat.RTF, var1);
      }

      return true;
   }

   public final String getRtf() {
      return (String)this.get(DataFormat.RTF);
   }

   public final boolean hasImage() {
      return this.containsKey(DataFormat.IMAGE);
   }

   public final boolean putImage(Image var1) {
      if (var1 == null) {
         this.remove(DataFormat.IMAGE);
      } else {
         this.put(DataFormat.IMAGE, var1);
      }

      return true;
   }

   public final Image getImage() {
      return (Image)this.get(DataFormat.IMAGE);
   }

   public final boolean hasFiles() {
      return this.containsKey(DataFormat.FILES);
   }

   public final boolean putFiles(List var1) {
      if (var1 == null) {
         this.remove(DataFormat.FILES);
      } else {
         this.put(DataFormat.FILES, var1);
      }

      return true;
   }

   public final boolean putFilesByPath(List var1) {
      ArrayList var2 = new ArrayList(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var2.add(new File(var4));
      }

      return this.putFiles(var2);
   }

   public final List getFiles() {
      return (List)this.get(DataFormat.FILES);
   }
}
