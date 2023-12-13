package javafx.stage;

import com.sun.glass.ui.CommonDialogs;
import com.sun.javafx.tk.FileChooserType;
import com.sun.javafx.tk.Toolkit;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class FileChooser {
   private StringProperty title;
   private ObjectProperty initialDirectory;
   private ObjectProperty initialFileName;
   private ObservableList extensionFilters = FXCollections.observableArrayList();
   private ObjectProperty selectedExtensionFilter;

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

   public final void setInitialFileName(String var1) {
      this.initialFileNameProperty().set(var1);
   }

   public final String getInitialFileName() {
      return this.initialFileName != null ? (String)this.initialFileName.get() : null;
   }

   public final ObjectProperty initialFileNameProperty() {
      if (this.initialFileName == null) {
         this.initialFileName = new SimpleObjectProperty(this, "initialFileName");
      }

      return this.initialFileName;
   }

   public ObservableList getExtensionFilters() {
      return this.extensionFilters;
   }

   public final ObjectProperty selectedExtensionFilterProperty() {
      if (this.selectedExtensionFilter == null) {
         this.selectedExtensionFilter = new SimpleObjectProperty(this, "selectedExtensionFilter");
      }

      return this.selectedExtensionFilter;
   }

   public final void setSelectedExtensionFilter(ExtensionFilter var1) {
      this.selectedExtensionFilterProperty().setValue(var1);
   }

   public final ExtensionFilter getSelectedExtensionFilter() {
      return this.selectedExtensionFilter != null ? (ExtensionFilter)this.selectedExtensionFilter.get() : null;
   }

   public File showOpenDialog(Window var1) {
      List var2 = this.showDialog(var1, FileChooserType.OPEN);
      return var2 != null && var2.size() > 0 ? (File)var2.get(0) : null;
   }

   public List showOpenMultipleDialog(Window var1) {
      List var2 = this.showDialog(var1, FileChooserType.OPEN_MULTIPLE);
      return var2 != null && var2.size() > 0 ? Collections.unmodifiableList(var2) : null;
   }

   public File showSaveDialog(Window var1) {
      List var2 = this.showDialog(var1, FileChooserType.SAVE);
      return var2 != null && var2.size() > 0 ? (File)var2.get(0) : null;
   }

   private ExtensionFilter findSelectedFilter(CommonDialogs.ExtensionFilter var1) {
      if (var1 != null) {
         String var2 = var1.getDescription();
         List var3 = var1.getExtensions();
         Iterator var4 = this.extensionFilters.iterator();

         while(var4.hasNext()) {
            ExtensionFilter var5 = (ExtensionFilter)var4.next();
            if (var2.equals(var5.getDescription()) && var3.equals(var5.getExtensions())) {
               return var5;
            }
         }
      }

      return null;
   }

   private List showDialog(Window var1, FileChooserType var2) {
      CommonDialogs.FileChooserResult var3 = Toolkit.getToolkit().showFileChooser(var1 != null ? var1.impl_getPeer() : null, this.getTitle(), this.getInitialDirectory(), this.getInitialFileName(), var2, this.extensionFilters, this.getSelectedExtensionFilter());
      if (var3 == null) {
         return null;
      } else {
         List var4 = var3.getFiles();
         if (var4 != null && var4.size() > 0) {
            this.selectedExtensionFilterProperty().set(this.findSelectedFilter(var3.getExtensionFilter()));
         }

         return var4;
      }
   }

   public static final class ExtensionFilter {
      private final String description;
      private final List extensions;

      public ExtensionFilter(String var1, String... var2) {
         validateArgs(var1, var2);
         this.description = var1;
         this.extensions = Collections.unmodifiableList(Arrays.asList((Object[])var2.clone()));
      }

      public ExtensionFilter(String var1, List var2) {
         String[] var3 = var2 != null ? (String[])var2.toArray(new String[var2.size()]) : null;
         validateArgs(var1, var3);
         this.description = var1;
         this.extensions = Collections.unmodifiableList(Arrays.asList(var3));
      }

      public String getDescription() {
         return this.description;
      }

      public List getExtensions() {
         return this.extensions;
      }

      private static void validateArgs(String var0, String[] var1) {
         if (var0 == null) {
            throw new NullPointerException("Description must not be null");
         } else if (var0.isEmpty()) {
            throw new IllegalArgumentException("Description must not be empty");
         } else if (var1 == null) {
            throw new NullPointerException("Extensions must not be null");
         } else if (var1.length == 0) {
            throw new IllegalArgumentException("At least one extension must be defined");
         } else {
            String[] var2 = var1;
            int var3 = var1.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               String var5 = var2[var4];
               if (var5 == null) {
                  throw new NullPointerException("Extension must not be null");
               }

               if (var5.isEmpty()) {
                  throw new IllegalArgumentException("Extension must not be empty");
               }
            }

         }
      }
   }
}
