package org.apache.commons.io.monitor;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.comparator.NameFileComparator;

public class FileAlterationObserver implements Serializable {
   private final List listeners;
   private final FileEntry rootEntry;
   private final FileFilter fileFilter;
   private final Comparator comparator;

   public FileAlterationObserver(String var1) {
      this(new File(var1));
   }

   public FileAlterationObserver(String var1, FileFilter var2) {
      this(new File(var1), var2);
   }

   public FileAlterationObserver(String var1, FileFilter var2, IOCase var3) {
      this(new File(var1), var2, var3);
   }

   public FileAlterationObserver(File var1) {
      this(var1, (FileFilter)null);
   }

   public FileAlterationObserver(File var1, FileFilter var2) {
      this(var1, var2, (IOCase)null);
   }

   public FileAlterationObserver(File var1, FileFilter var2, IOCase var3) {
      this(new FileEntry(var1), var2, var3);
   }

   protected FileAlterationObserver(FileEntry var1, FileFilter var2, IOCase var3) {
      this.listeners = new CopyOnWriteArrayList();
      if (var1 == null) {
         throw new IllegalArgumentException("Root entry is missing");
      } else if (var1.getFile() == null) {
         throw new IllegalArgumentException("Root directory is missing");
      } else {
         this.rootEntry = var1;
         this.fileFilter = var2;
         if (var3 != null && !var3.equals(IOCase.SYSTEM)) {
            if (var3.equals(IOCase.INSENSITIVE)) {
               this.comparator = NameFileComparator.NAME_INSENSITIVE_COMPARATOR;
            } else {
               this.comparator = NameFileComparator.NAME_COMPARATOR;
            }
         } else {
            this.comparator = NameFileComparator.NAME_SYSTEM_COMPARATOR;
         }

      }
   }

   public File getDirectory() {
      return this.rootEntry.getFile();
   }

   public FileFilter getFileFilter() {
      return this.fileFilter;
   }

   public void addListener(FileAlterationListener var1) {
      if (var1 != null) {
         this.listeners.add(var1);
      }

   }

   public void removeListener(FileAlterationListener var1) {
      if (var1 != null) {
         while(true) {
            if (this.listeners.remove(var1)) {
               continue;
            }
         }
      }

   }

   public Iterable getListeners() {
      return this.listeners;
   }

   public void initialize() throws Exception {
      this.rootEntry.refresh(this.rootEntry.getFile());
      File[] var1 = this.listFiles(this.rootEntry.getFile());
      FileEntry[] var2 = var1.length > 0 ? new FileEntry[var1.length] : FileEntry.EMPTY_ENTRIES;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = this.createFileEntry(this.rootEntry, var1[var3]);
      }

      this.rootEntry.setChildren(var2);
   }

   public void destroy() throws Exception {
   }

   public void checkAndNotify() {
      Iterator var1 = this.listeners.iterator();

      while(var1.hasNext()) {
         FileAlterationListener var2 = (FileAlterationListener)var1.next();
         var2.onStart(this);
      }

      File var4 = this.rootEntry.getFile();
      if (var4.exists()) {
         this.checkAndNotify(this.rootEntry, this.rootEntry.getChildren(), this.listFiles(var4));
      } else if (this.rootEntry.isExists()) {
         this.checkAndNotify(this.rootEntry, this.rootEntry.getChildren(), FileUtils.EMPTY_FILE_ARRAY);
      }

      Iterator var5 = this.listeners.iterator();

      while(var5.hasNext()) {
         FileAlterationListener var3 = (FileAlterationListener)var5.next();
         var3.onStop(this);
      }

   }

   private void checkAndNotify(FileEntry var1, FileEntry[] var2, File[] var3) {
      int var4 = 0;
      FileEntry[] var5 = var3.length > 0 ? new FileEntry[var3.length] : FileEntry.EMPTY_ENTRIES;
      FileEntry[] var6 = var2;
      int var7 = var2.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         FileEntry var9;
         for(var9 = var6[var8]; var4 < var3.length && this.comparator.compare(var9.getFile(), var3[var4]) > 0; ++var4) {
            var5[var4] = this.createFileEntry(var1, var3[var4]);
            this.doCreate(var5[var4]);
         }

         if (var4 < var3.length && this.comparator.compare(var9.getFile(), var3[var4]) == 0) {
            this.doMatch(var9, var3[var4]);
            this.checkAndNotify(var9, var9.getChildren(), this.listFiles(var3[var4]));
            var5[var4] = var9;
            ++var4;
         } else {
            this.checkAndNotify(var9, var9.getChildren(), FileUtils.EMPTY_FILE_ARRAY);
            this.doDelete(var9);
         }
      }

      while(var4 < var3.length) {
         var5[var4] = this.createFileEntry(var1, var3[var4]);
         this.doCreate(var5[var4]);
         ++var4;
      }

      var1.setChildren(var5);
   }

   private FileEntry createFileEntry(FileEntry var1, File var2) {
      FileEntry var3 = var1.newChildInstance(var2);
      var3.refresh(var2);
      File[] var4 = this.listFiles(var2);
      FileEntry[] var5 = var4.length > 0 ? new FileEntry[var4.length] : FileEntry.EMPTY_ENTRIES;

      for(int var6 = 0; var6 < var4.length; ++var6) {
         var5[var6] = this.createFileEntry(var3, var4[var6]);
      }

      var3.setChildren(var5);
      return var3;
   }

   private void doCreate(FileEntry var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         FileAlterationListener var3 = (FileAlterationListener)var2.next();
         if (var1.isDirectory()) {
            var3.onDirectoryCreate(var1.getFile());
         } else {
            var3.onFileCreate(var1.getFile());
         }
      }

      FileEntry[] var7 = var1.getChildren();
      FileEntry[] var8 = var7;
      int var4 = var7.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         FileEntry var6 = var8[var5];
         this.doCreate(var6);
      }

   }

   private void doMatch(FileEntry var1, File var2) {
      if (var1.refresh(var2)) {
         Iterator var3 = this.listeners.iterator();

         while(var3.hasNext()) {
            FileAlterationListener var4 = (FileAlterationListener)var3.next();
            if (var1.isDirectory()) {
               var4.onDirectoryChange(var2);
            } else {
               var4.onFileChange(var2);
            }
         }
      }

   }

   private void doDelete(FileEntry var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         FileAlterationListener var3 = (FileAlterationListener)var2.next();
         if (var1.isDirectory()) {
            var3.onDirectoryDelete(var1.getFile());
         } else {
            var3.onFileDelete(var1.getFile());
         }
      }

   }

   private File[] listFiles(File var1) {
      File[] var2 = null;
      if (var1.isDirectory()) {
         var2 = this.fileFilter == null ? var1.listFiles() : var1.listFiles(this.fileFilter);
      }

      if (var2 == null) {
         var2 = FileUtils.EMPTY_FILE_ARRAY;
      }

      if (this.comparator != null && var2.length > 1) {
         Arrays.sort(var2, this.comparator);
      }

      return var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getSimpleName());
      var1.append("[file='");
      var1.append(this.getDirectory().getPath());
      var1.append('\'');
      if (this.fileFilter != null) {
         var1.append(", ");
         var1.append(this.fileFilter.toString());
      }

      var1.append(", listeners=");
      var1.append(this.listeners.size());
      var1.append("]");
      return var1.toString();
   }
}
