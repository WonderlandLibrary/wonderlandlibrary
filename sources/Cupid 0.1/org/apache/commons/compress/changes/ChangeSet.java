package org.apache.commons.compress.changes;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.compress.archivers.ArchiveEntry;

public final class ChangeSet {
  private final Set<Change> changes = new LinkedHashSet<Change>();
  
  public void delete(String filename) {
    addDeletion(new Change(filename, 1));
  }
  
  public void deleteDir(String dirName) {
    addDeletion(new Change(dirName, 4));
  }
  
  public void add(ArchiveEntry pEntry, InputStream pInput) {
    add(pEntry, pInput, true);
  }
  
  public void add(ArchiveEntry pEntry, InputStream pInput, boolean replace) {
    addAddition(new Change(pEntry, pInput, replace));
  }
  
  private void addAddition(Change pChange) {
    if (2 != pChange.type() || pChange.getInput() == null)
      return; 
    if (!this.changes.isEmpty())
      for (Iterator<Change> it = this.changes.iterator(); it.hasNext(); ) {
        Change change = it.next();
        if (change.type() == 2 && change.getEntry() != null) {
          ArchiveEntry entry = change.getEntry();
          if (entry.equals(pChange.getEntry())) {
            if (pChange.isReplaceMode()) {
              it.remove();
              this.changes.add(pChange);
              return;
            } 
            return;
          } 
        } 
      }  
    this.changes.add(pChange);
  }
  
  private void addDeletion(Change pChange) {
    if ((1 != pChange.type() && 4 != pChange.type()) || pChange.targetFile() == null)
      return; 
    String source = pChange.targetFile();
    if (source != null && !this.changes.isEmpty())
      for (Iterator<Change> it = this.changes.iterator(); it.hasNext(); ) {
        Change change = it.next();
        if (change.type() == 2 && change.getEntry() != null) {
          String target = change.getEntry().getName();
          if (target == null)
            continue; 
          if (1 == pChange.type() && source.equals(target)) {
            it.remove();
            continue;
          } 
          if (4 == pChange.type() && target.matches(source + "/.*"))
            it.remove(); 
        } 
      }  
    this.changes.add(pChange);
  }
  
  Set<Change> getChanges() {
    return new LinkedHashSet<Change>(this.changes);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\changes\ChangeSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */