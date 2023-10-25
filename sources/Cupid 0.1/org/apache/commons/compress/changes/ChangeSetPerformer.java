package org.apache.commons.compress.changes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
  private final Set<Change> changes;
  
  public ChangeSetPerformer(ChangeSet changeSet) {
    this.changes = changeSet.getChanges();
  }
  
  public ChangeSetResults perform(ArchiveInputStream in, ArchiveOutputStream out) throws IOException {
    return perform(new ArchiveInputStreamIterator(in), out);
  }
  
  public ChangeSetResults perform(ZipFile in, ArchiveOutputStream out) throws IOException {
    return perform(new ZipFileIterator(in), out);
  }
  
  private ChangeSetResults perform(ArchiveEntryIterator entryIterator, ArchiveOutputStream out) throws IOException {
    ChangeSetResults results = new ChangeSetResults();
    Set<Change> workingSet = new LinkedHashSet<Change>(this.changes);
    Iterator<Change> it;
    for (it = workingSet.iterator(); it.hasNext(); ) {
      Change change = it.next();
      if (change.type() == 2 && change.isReplaceMode()) {
        copyStream(change.getInput(), out, change.getEntry());
        it.remove();
        results.addedFromChangeSet(change.getEntry().getName());
      } 
    } 
    while (entryIterator.hasNext()) {
      ArchiveEntry entry = entryIterator.next();
      boolean copy = true;
      for (Iterator<Change> iterator = workingSet.iterator(); iterator.hasNext(); ) {
        Change change = iterator.next();
        int type = change.type();
        String name = entry.getName();
        if (type == 1 && name != null) {
          if (name.equals(change.targetFile())) {
            copy = false;
            iterator.remove();
            results.deleted(name);
            break;
          } 
          continue;
        } 
        if (type == 4 && name != null)
          if (name.startsWith(change.targetFile() + "/")) {
            copy = false;
            results.deleted(name);
            break;
          }  
      } 
      if (copy && !isDeletedLater(workingSet, entry) && !results.hasBeenAdded(entry.getName())) {
        copyStream(entryIterator.getInputStream(), out, entry);
        results.addedFromStream(entry.getName());
      } 
    } 
    for (it = workingSet.iterator(); it.hasNext(); ) {
      Change change = it.next();
      if (change.type() == 2 && !change.isReplaceMode() && !results.hasBeenAdded(change.getEntry().getName())) {
        copyStream(change.getInput(), out, change.getEntry());
        it.remove();
        results.addedFromChangeSet(change.getEntry().getName());
      } 
    } 
    out.finish();
    return results;
  }
  
  private boolean isDeletedLater(Set<Change> workingSet, ArchiveEntry entry) {
    String source = entry.getName();
    if (!workingSet.isEmpty())
      for (Change change : workingSet) {
        int type = change.type();
        String target = change.targetFile();
        if (type == 1 && source.equals(target))
          return true; 
        if (type == 4 && source.startsWith(target + "/"))
          return true; 
      }  
    return false;
  }
  
  private void copyStream(InputStream in, ArchiveOutputStream out, ArchiveEntry entry) throws IOException {
    out.putArchiveEntry(entry);
    IOUtils.copy(in, (OutputStream)out);
    out.closeArchiveEntry();
  }
  
  static interface ArchiveEntryIterator {
    boolean hasNext() throws IOException;
    
    ArchiveEntry next();
    
    InputStream getInputStream() throws IOException;
  }
  
  private static class ArchiveInputStreamIterator implements ArchiveEntryIterator {
    private final ArchiveInputStream in;
    
    private ArchiveEntry next;
    
    ArchiveInputStreamIterator(ArchiveInputStream in) {
      this.in = in;
    }
    
    public boolean hasNext() throws IOException {
      return ((this.next = this.in.getNextEntry()) != null);
    }
    
    public ArchiveEntry next() {
      return this.next;
    }
    
    public InputStream getInputStream() {
      return (InputStream)this.in;
    }
  }
  
  private static class ZipFileIterator implements ArchiveEntryIterator {
    private final ZipFile in;
    
    private final Enumeration<ZipArchiveEntry> nestedEnum;
    
    private ZipArchiveEntry current;
    
    ZipFileIterator(ZipFile in) {
      this.in = in;
      this.nestedEnum = in.getEntriesInPhysicalOrder();
    }
    
    public boolean hasNext() {
      return this.nestedEnum.hasMoreElements();
    }
    
    public ArchiveEntry next() {
      return (ArchiveEntry)(this.current = this.nestedEnum.nextElement());
    }
    
    public InputStream getInputStream() throws IOException {
      return this.in.getInputStream(this.current);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\changes\ChangeSetPerformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */