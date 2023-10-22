package javafx.scene.input;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ClipboardContentBuilder implements Builder {
   private boolean __set;
   private Collection files;

   protected ClipboardContentBuilder() {
   }

   public static ClipboardContentBuilder create() {
      return new ClipboardContentBuilder();
   }

   public void applyTo(ClipboardContent var1) {
      if (this.__set) {
         var1.getFiles().clear();
         var1.getFiles().addAll(this.files);
      }

   }

   public ClipboardContentBuilder files(Collection var1) {
      this.files = var1;
      this.__set = true;
      return this;
   }

   public ClipboardContentBuilder files(File... var1) {
      return this.files((Collection)Arrays.asList(var1));
   }

   public ClipboardContent build() {
      ClipboardContent var1 = new ClipboardContent();
      this.applyTo(var1);
      return var1;
   }
}
