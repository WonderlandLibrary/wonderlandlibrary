package javafx.stage;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class FileChooserBuilder implements Builder {
   private int __set;
   private Collection extensionFilters;
   private File initialDirectory;
   private String title;

   protected FileChooserBuilder() {
   }

   public static FileChooserBuilder create() {
      return new FileChooserBuilder();
   }

   public void applyTo(FileChooser var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.getExtensionFilters().addAll(this.extensionFilters);
      }

      if ((var2 & 2) != 0) {
         var1.setInitialDirectory(this.initialDirectory);
      }

      if ((var2 & 4) != 0) {
         var1.setTitle(this.title);
      }

   }

   public FileChooserBuilder extensionFilters(Collection var1) {
      this.extensionFilters = var1;
      this.__set |= 1;
      return this;
   }

   public FileChooserBuilder extensionFilters(FileChooser.ExtensionFilter... var1) {
      return this.extensionFilters((Collection)Arrays.asList(var1));
   }

   public FileChooserBuilder initialDirectory(File var1) {
      this.initialDirectory = var1;
      this.__set |= 2;
      return this;
   }

   public FileChooserBuilder title(String var1) {
      this.title = var1;
      this.__set |= 4;
      return this;
   }

   public FileChooser build() {
      FileChooser var1 = new FileChooser();
      this.applyTo(var1);
      return var1;
   }
}
