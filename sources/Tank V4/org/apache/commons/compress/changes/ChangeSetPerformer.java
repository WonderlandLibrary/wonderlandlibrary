package org.apache.commons.compress.changes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

public class ChangeSetPerformer {
   private final Set changes;

   public ChangeSetPerformer(ChangeSet var1) {
      this.changes = var1.getChanges();
   }

   public ChangeSetResults perform(ArchiveInputStream var1, ArchiveOutputStream var2) throws IOException {
      return this.perform((ChangeSetPerformer.ArchiveEntryIterator)(new ChangeSetPerformer.ArchiveInputStreamIterator(var1)), var2);
   }

   public ChangeSetResults perform(ZipFile var1, ArchiveOutputStream var2) throws IOException {
      return this.perform((ChangeSetPerformer.ArchiveEntryIterator)(new ChangeSetPerformer.ZipFileIterator(var1)), var2);
   }

   private ChangeSetResults perform(ChangeSetPerformer.ArchiveEntryIterator var1, ArchiveOutputStream var2) throws IOException {
      ChangeSetResults var3 = new ChangeSetResults();
      LinkedHashSet var4 = new LinkedHashSet(this.changes);
      Iterator var5 = var4.iterator();

      Change var6;
      while(var5.hasNext()) {
         var6 = (Change)var5.next();
         if (var6.type() == 2 && var6.isReplaceMode()) {
            this.copyStream(var6.getInput(), var2, var6.getEntry());
            var5.remove();
            var3.addedFromChangeSet(var6.getEntry().getName());
         }
      }

      while(var1.hasNext()) {
         ArchiveEntry var11 = var1.next();
         boolean var12 = true;
         Iterator var7 = var4.iterator();

         label62:
         while(true) {
            while(true) {
               if (!var7.hasNext()) {
                  break label62;
               }

               Change var8 = (Change)var7.next();
               int var9 = var8.type();
               String var10 = var11.getName();
               if (var9 == 1 && var10 != null) {
                  if (var10.equals(var8.targetFile())) {
                     var12 = false;
                     var7.remove();
                     var3.deleted(var10);
                     break label62;
                  }
               } else if (var9 == 4 && var10 != null && var10.startsWith(var8.targetFile() + "/")) {
                  var12 = false;
                  var3.deleted(var10);
                  break label62;
               }
            }
         }

         if (var12 && var11 == false && !var3.hasBeenAdded(var11.getName())) {
            this.copyStream(var1.getInputStream(), var2, var11);
            var3.addedFromStream(var11.getName());
         }
      }

      var5 = var4.iterator();

      while(var5.hasNext()) {
         var6 = (Change)var5.next();
         if (var6.type() == 2 && !var6.isReplaceMode() && !var3.hasBeenAdded(var6.getEntry().getName())) {
            this.copyStream(var6.getInput(), var2, var6.getEntry());
            var5.remove();
            var3.addedFromChangeSet(var6.getEntry().getName());
         }
      }

      var2.finish();
      return var3;
   }

   private void copyStream(InputStream var1, ArchiveOutputStream var2, ArchiveEntry var3) throws IOException {
      var2.putArchiveEntry(var3);
      IOUtils.copy(var1, var2);
      var2.closeArchiveEntry();
   }

   private static class ZipFileIterator implements ChangeSetPerformer.ArchiveEntryIterator {
      private final ZipFile in;
      private final Enumeration nestedEnum;
      private ZipArchiveEntry current;

      ZipFileIterator(ZipFile var1) {
         this.in = var1;
         this.nestedEnum = var1.getEntriesInPhysicalOrder();
      }

      public boolean hasNext() {
         return this.nestedEnum.hasMoreElements();
      }

      public ArchiveEntry next() {
         return this.current = (ZipArchiveEntry)this.nestedEnum.nextElement();
      }

      public InputStream getInputStream() throws IOException {
         return this.in.getInputStream(this.current);
      }
   }

   private static class ArchiveInputStreamIterator implements ChangeSetPerformer.ArchiveEntryIterator {
      private final ArchiveInputStream in;
      private ArchiveEntry next;

      ArchiveInputStreamIterator(ArchiveInputStream var1) {
         this.in = var1;
      }

      public boolean hasNext() throws IOException {
         return (this.next = this.in.getNextEntry()) != null;
      }

      public ArchiveEntry next() {
         return this.next;
      }

      public InputStream getInputStream() {
         return this.in;
      }
   }

   interface ArchiveEntryIterator {
      boolean hasNext() throws IOException;

      ArchiveEntry next();

      InputStream getInputStream() throws IOException;
   }
}
