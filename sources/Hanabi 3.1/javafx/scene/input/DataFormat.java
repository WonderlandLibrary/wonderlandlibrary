package javafx.scene.input;

import com.sun.javafx.util.WeakReferenceQueue;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.NamedArg;

public class DataFormat {
   private static final WeakReferenceQueue DATA_FORMAT_LIST = new WeakReferenceQueue();
   public static final DataFormat PLAIN_TEXT = new DataFormat(new String[]{"text/plain"});
   public static final DataFormat HTML = new DataFormat(new String[]{"text/html"});
   public static final DataFormat RTF = new DataFormat(new String[]{"text/rtf"});
   public static final DataFormat URL = new DataFormat(new String[]{"text/uri-list"});
   public static final DataFormat IMAGE = new DataFormat(new String[]{"application/x-java-rawimage"});
   public static final DataFormat FILES = new DataFormat(new String[]{"application/x-java-file-list", "java.file-list"});
   private static final DataFormat DRAG_IMAGE = new DataFormat(new String[]{"application/x-java-drag-image"});
   private static final DataFormat DRAG_IMAGE_OFFSET = new DataFormat(new String[]{"application/x-java-drag-image-offset"});
   private final Set identifier;

   public DataFormat(@NamedArg("ids") String... var1) {
      DATA_FORMAT_LIST.cleanup();
      if (var1 != null) {
         String[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (lookupMimeType(var5) != null) {
               throw new IllegalArgumentException("DataFormat '" + var5 + "' already exists.");
            }
         }

         this.identifier = Collections.unmodifiableSet(new HashSet(Arrays.asList(var1)));
      } else {
         this.identifier = Collections.emptySet();
      }

      DATA_FORMAT_LIST.add(this);
   }

   public final Set getIdentifiers() {
      return this.identifier;
   }

   public String toString() {
      if (this.identifier.isEmpty()) {
         return "[]";
      } else {
         StringBuilder var1;
         if (this.identifier.size() == 1) {
            var1 = new StringBuilder("[");
            var1.append((String)this.identifier.iterator().next());
            return var1.append("]").toString();
         } else {
            var1 = new StringBuilder("[");
            Iterator var2 = this.identifier.iterator();

            while(var2.hasNext()) {
               var1 = var1.append((String)var2.next());
               if (var2.hasNext()) {
                  var1 = var1.append(", ");
               }
            }

            var1 = var1.append("]");
            return var1.toString();
         }
      }
   }

   public int hashCode() {
      int var1 = 7;

      String var3;
      for(Iterator var2 = this.identifier.iterator(); var2.hasNext(); var1 = 31 * var1 + var3.hashCode()) {
         var3 = (String)var2.next();
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof DataFormat) {
         DataFormat var2 = (DataFormat)var1;
         return this.identifier.equals(var2.identifier);
      } else {
         return false;
      }
   }

   public static DataFormat lookupMimeType(String var0) {
      if (var0 != null && var0.length() != 0) {
         Iterator var1 = DATA_FORMAT_LIST.iterator();

         DataFormat var2;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            var2 = (DataFormat)var1.next();
         } while(!var2.getIdentifiers().contains(var0));

         return var2;
      } else {
         return null;
      }
   }
}
