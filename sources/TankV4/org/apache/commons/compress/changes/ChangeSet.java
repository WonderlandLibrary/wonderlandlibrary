package org.apache.commons.compress.changes;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.compress.archivers.ArchiveEntry;

public final class ChangeSet {
   private final Set changes = new LinkedHashSet();

   public void delete(String var1) {
      this.addDeletion(new Change(var1, 1));
   }

   public void deleteDir(String var1) {
      this.addDeletion(new Change(var1, 4));
   }

   public void add(ArchiveEntry var1, InputStream var2) {
      this.add(var1, var2, true);
   }

   public void add(ArchiveEntry var1, InputStream var2, boolean var3) {
      this.addAddition(new Change(var1, var2, var3));
   }

   private void addAddition(Change var1) {
      if (2 == var1.type() && var1.getInput() != null) {
         if (!this.changes.isEmpty()) {
            Iterator var2 = this.changes.iterator();

            while(var2.hasNext()) {
               Change var3 = (Change)var2.next();
               if (var3.type() == 2 && var3.getEntry() != null) {
                  ArchiveEntry var4 = var3.getEntry();
                  if (var4.equals(var1.getEntry())) {
                     if (var1.isReplaceMode()) {
                        var2.remove();
                        this.changes.add(var1);
                        return;
                     }

                     return;
                  }
               }
            }
         }

         this.changes.add(var1);
      }
   }

   private void addDeletion(Change var1) {
      if ((1 == var1.type() || 4 == var1.type()) && var1.targetFile() != null) {
         String var2 = var1.targetFile();
         if (var2 != null && !this.changes.isEmpty()) {
            Iterator var3 = this.changes.iterator();

            label47:
            while(true) {
               while(true) {
                  String var5;
                  do {
                     Change var4;
                     do {
                        do {
                           if (!var3.hasNext()) {
                              break label47;
                           }

                           var4 = (Change)var3.next();
                        } while(var4.type() != 2);
                     } while(var4.getEntry() == null);

                     var5 = var4.getEntry().getName();
                  } while(var5 == null);

                  if (1 == var1.type() && var2.equals(var5)) {
                     var3.remove();
                  } else if (4 == var1.type() && var5.matches(var2 + "/.*")) {
                     var3.remove();
                  }
               }
            }
         }

         this.changes.add(var1);
      }
   }

   Set getChanges() {
      return new LinkedHashSet(this.changes);
   }
}
