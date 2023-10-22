package javafx.stage;

import com.sun.javafx.tk.Toolkit;
import java.io.File;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class DirectoryChooser {
   private StringProperty title;
   private ObjectProperty initialDirectory;

   public final void setTitle(String var1) {
      this.titleProperty().set(var1);
   }

   public final String getTitle() {
      return this.title != null ? (String)this.title.get() : null;
   }

   public final StringProperty titleProperty() {
      if (this.title == null) {
         this.title = new SimpleStringProperty(this, "title");
      }

      return this.title;
   }

   public final void setInitialDirectory(File var1) {
      this.initialDirectoryProperty().set(var1);
   }

   public final File getInitialDirectory() {
      return this.initialDirectory != null ? (File)this.initialDirectory.get() : null;
   }

   public final ObjectProperty initialDirectoryProperty() {
      if (this.initialDirectory == null) {
         this.initialDirectory = new SimpleObjectProperty(this, "initialDirectory");
      }

      return this.initialDirectory;
   }

   public File showDialog(Window var1) {
      return Toolkit.getToolkit().showDirectoryChooser(var1 != null ? var1.impl_getPeer() : null, this.getTitle(), this.getInitialDirectory());
   }
}
