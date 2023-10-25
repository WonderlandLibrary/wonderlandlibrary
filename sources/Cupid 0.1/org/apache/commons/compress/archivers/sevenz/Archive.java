package org.apache.commons.compress.archivers.sevenz;

import java.util.BitSet;

class Archive {
  long packPos;
  
  long[] packSizes;
  
  BitSet packCrcsDefined;
  
  long[] packCrcs;
  
  Folder[] folders;
  
  SubStreamsInfo subStreamsInfo;
  
  SevenZArchiveEntry[] files;
  
  StreamMap streamMap;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\sevenz\Archive.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */