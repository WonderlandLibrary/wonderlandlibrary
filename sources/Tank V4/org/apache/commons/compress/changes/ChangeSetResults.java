package org.apache.commons.compress.changes;

import java.util.ArrayList;
import java.util.List;

public class ChangeSetResults {
   private final List addedFromChangeSet = new ArrayList();
   private final List addedFromStream = new ArrayList();
   private final List deleted = new ArrayList();

   void deleted(String var1) {
      this.deleted.add(var1);
   }

   void addedFromStream(String var1) {
      this.addedFromStream.add(var1);
   }

   void addedFromChangeSet(String var1) {
      this.addedFromChangeSet.add(var1);
   }

   public List getAddedFromChangeSet() {
      return this.addedFromChangeSet;
   }

   public List getAddedFromStream() {
      return this.addedFromStream;
   }

   public List getDeleted() {
      return this.deleted;
   }

   boolean hasBeenAdded(String var1) {
      return this.addedFromChangeSet.contains(var1) || this.addedFromStream.contains(var1);
   }
}
