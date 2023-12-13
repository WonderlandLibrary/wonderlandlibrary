package javafx.stage;

import java.io.File;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class DirectoryChooserBuilder implements Builder {
   private int __set;
   private File initialDirectory;
   private String title;

   protected DirectoryChooserBuilder() {
   }

   public static DirectoryChooserBuilder create() {
      return new DirectoryChooserBuilder();
   }

   public void applyTo(DirectoryChooser var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setInitialDirectory(this.initialDirectory);
      }

      if ((var2 & 2) != 0) {
         var1.setTitle(this.title);
      }

   }

   public DirectoryChooserBuilder initialDirectory(File var1) {
      this.initialDirectory = var1;
      this.__set |= 1;
      return this;
   }

   public DirectoryChooserBuilder title(String var1) {
      this.title = var1;
      this.__set |= 2;
      return this;
   }

   public DirectoryChooser build() {
      DirectoryChooser var1 = new DirectoryChooser();
      this.applyTo(var1);
      return var1;
   }
}
