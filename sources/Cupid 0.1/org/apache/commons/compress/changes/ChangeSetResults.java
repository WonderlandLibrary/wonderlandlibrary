package org.apache.commons.compress.changes;

import java.util.ArrayList;
import java.util.List;

public class ChangeSetResults {
  private final List<String> addedFromChangeSet = new ArrayList<String>();
  
  private final List<String> addedFromStream = new ArrayList<String>();
  
  private final List<String> deleted = new ArrayList<String>();
  
  void deleted(String fileName) {
    this.deleted.add(fileName);
  }
  
  void addedFromStream(String fileName) {
    this.addedFromStream.add(fileName);
  }
  
  void addedFromChangeSet(String fileName) {
    this.addedFromChangeSet.add(fileName);
  }
  
  public List<String> getAddedFromChangeSet() {
    return this.addedFromChangeSet;
  }
  
  public List<String> getAddedFromStream() {
    return this.addedFromStream;
  }
  
  public List<String> getDeleted() {
    return this.deleted;
  }
  
  boolean hasBeenAdded(String filename) {
    if (this.addedFromChangeSet.contains(filename) || this.addedFromStream.contains(filename))
      return true; 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\changes\ChangeSetResults.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */