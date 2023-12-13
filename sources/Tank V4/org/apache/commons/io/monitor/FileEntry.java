package org.apache.commons.io.monitor;

import java.io.File;
import java.io.Serializable;

public class FileEntry implements Serializable {
   static final FileEntry[] EMPTY_ENTRIES = new FileEntry[0];
   private final FileEntry parent;
   private FileEntry[] children;
   private final File file;
   private String name;
   private boolean exists;
   private boolean directory;
   private long lastModified;
   private long length;

   public FileEntry(File var1) {
      this((FileEntry)null, var1);
   }

   public FileEntry(FileEntry var1, File var2) {
      if (var2 == null) {
         throw new IllegalArgumentException("File is missing");
      } else {
         this.file = var2;
         this.parent = var1;
         this.name = var2.getName();
      }
   }

   public boolean refresh(File var1) {
      boolean var2 = this.exists;
      long var3 = this.lastModified;
      boolean var5 = this.directory;
      long var6 = this.length;
      this.name = var1.getName();
      this.exists = var1.exists();
      this.directory = this.exists ? var1.isDirectory() : false;
      this.lastModified = this.exists ? var1.lastModified() : 0L;
      this.length = this.exists && !this.directory ? var1.length() : 0L;
      return this.exists != var2 || this.lastModified != var3 || this.directory != var5 || this.length != var6;
   }

   public FileEntry newChildInstance(File var1) {
      return new FileEntry(this, var1);
   }

   public FileEntry getParent() {
      return this.parent;
   }

   public int getLevel() {
      return this.parent == null ? 0 : this.parent.getLevel() + 1;
   }

   public FileEntry[] getChildren() {
      return this.children != null ? this.children : EMPTY_ENTRIES;
   }

   public void setChildren(FileEntry[] var1) {
      this.children = var1;
   }

   public File getFile() {
      return this.file;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public long getLastModified() {
      return this.lastModified;
   }

   public void setLastModified(long var1) {
      this.lastModified = var1;
   }

   public long getLength() {
      return this.length;
   }

   public void setLength(long var1) {
      this.length = var1;
   }

   public boolean isExists() {
      return this.exists;
   }

   public void setExists(boolean var1) {
      this.exists = var1;
   }

   public boolean isDirectory() {
      return this.directory;
   }

   public void setDirectory(boolean var1) {
      this.directory = var1;
   }
}
