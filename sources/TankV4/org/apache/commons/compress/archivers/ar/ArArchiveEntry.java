package org.apache.commons.compress.archivers.ar;

import java.io.File;
import java.util.Date;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArArchiveEntry implements ArchiveEntry {
   public static final String HEADER = "!<arch>\n";
   public static final String TRAILER = "`\n";
   private final String name;
   private final int userId;
   private final int groupId;
   private final int mode;
   private static final int DEFAULT_MODE = 33188;
   private final long lastModified;
   private final long length;

   public ArArchiveEntry(String var1, long var2) {
      this(var1, var2, 0, 0, 33188, System.currentTimeMillis() / 1000L);
   }

   public ArArchiveEntry(String var1, long var2, int var4, int var5, int var6, long var7) {
      this.name = var1;
      this.length = var2;
      this.userId = var4;
      this.groupId = var5;
      this.mode = var6;
      this.lastModified = var7;
   }

   public ArArchiveEntry(File var1, String var2) {
      this(var2, var1.isFile() ? var1.length() : 0L, 0, 0, 33188, var1.lastModified() / 1000L);
   }

   public long getSize() {
      return this.getLength();
   }

   public String getName() {
      return this.name;
   }

   public int getUserId() {
      return this.userId;
   }

   public int getGroupId() {
      return this.groupId;
   }

   public int getMode() {
      return this.mode;
   }

   public long getLastModified() {
      return this.lastModified;
   }

   public Date getLastModifiedDate() {
      return new Date(1000L * this.getLastModified());
   }

   public long getLength() {
      return this.length;
   }

   public boolean isDirectory() {
      return false;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = 31 * var2 + (this.name == null ? 0 : this.name.hashCode());
      return var3;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         ArArchiveEntry var2 = (ArArchiveEntry)var1;
         if (this.name == null) {
            if (var2.name != null) {
               return false;
            }
         } else if (!this.name.equals(var2.name)) {
            return false;
         }

         return true;
      } else {
         return false;
      }
   }
}
