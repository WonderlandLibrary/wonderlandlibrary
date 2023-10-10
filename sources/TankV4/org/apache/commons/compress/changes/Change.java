package org.apache.commons.compress.changes;

import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;

class Change {
   private final String targetFile;
   private final ArchiveEntry entry;
   private final InputStream input;
   private final boolean replaceMode;
   private final int type;
   static final int TYPE_DELETE = 1;
   static final int TYPE_ADD = 2;
   static final int TYPE_MOVE = 3;
   static final int TYPE_DELETE_DIR = 4;

   Change(String var1, int var2) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.targetFile = var1;
         this.type = var2;
         this.input = null;
         this.entry = null;
         this.replaceMode = true;
      }
   }

   Change(ArchiveEntry var1, InputStream var2, boolean var3) {
      if (var1 != null && var2 != null) {
         this.entry = var1;
         this.input = var2;
         this.type = 2;
         this.targetFile = null;
         this.replaceMode = var3;
      } else {
         throw new NullPointerException();
      }
   }

   ArchiveEntry getEntry() {
      return this.entry;
   }

   InputStream getInput() {
      return this.input;
   }

   String targetFile() {
      return this.targetFile;
   }

   int type() {
      return this.type;
   }

   boolean isReplaceMode() {
      return this.replaceMode;
   }
}
