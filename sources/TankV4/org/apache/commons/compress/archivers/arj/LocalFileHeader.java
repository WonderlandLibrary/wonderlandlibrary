package org.apache.commons.compress.archivers.arj;

import java.util.Arrays;

class LocalFileHeader {
   int archiverVersionNumber;
   int minVersionToExtract;
   int hostOS;
   int arjFlags;
   int method;
   int fileType;
   int reserved;
   int dateTimeModified;
   long compressedSize;
   long originalSize;
   long originalCrc32;
   int fileSpecPosition;
   int fileAccessMode;
   int firstChapter;
   int lastChapter;
   int extendedFilePosition;
   int dateTimeAccessed;
   int dateTimeCreated;
   int originalSizeEvenForVolumes;
   String name;
   String comment;
   byte[][] extendedHeaders = (byte[][])null;

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("LocalFileHeader [archiverVersionNumber=");
      var1.append(this.archiverVersionNumber);
      var1.append(", minVersionToExtract=");
      var1.append(this.minVersionToExtract);
      var1.append(", hostOS=");
      var1.append(this.hostOS);
      var1.append(", arjFlags=");
      var1.append(this.arjFlags);
      var1.append(", method=");
      var1.append(this.method);
      var1.append(", fileType=");
      var1.append(this.fileType);
      var1.append(", reserved=");
      var1.append(this.reserved);
      var1.append(", dateTimeModified=");
      var1.append(this.dateTimeModified);
      var1.append(", compressedSize=");
      var1.append(this.compressedSize);
      var1.append(", originalSize=");
      var1.append(this.originalSize);
      var1.append(", originalCrc32=");
      var1.append(this.originalCrc32);
      var1.append(", fileSpecPosition=");
      var1.append(this.fileSpecPosition);
      var1.append(", fileAccessMode=");
      var1.append(this.fileAccessMode);
      var1.append(", firstChapter=");
      var1.append(this.firstChapter);
      var1.append(", lastChapter=");
      var1.append(this.lastChapter);
      var1.append(", extendedFilePosition=");
      var1.append(this.extendedFilePosition);
      var1.append(", dateTimeAccessed=");
      var1.append(this.dateTimeAccessed);
      var1.append(", dateTimeCreated=");
      var1.append(this.dateTimeCreated);
      var1.append(", originalSizeEvenForVolumes=");
      var1.append(this.originalSizeEvenForVolumes);
      var1.append(", name=");
      var1.append(this.name);
      var1.append(", comment=");
      var1.append(this.comment);
      var1.append(", extendedHeaders=");
      var1.append(Arrays.toString(this.extendedHeaders));
      var1.append("]");
      return var1.toString();
   }

   static class Methods {
      static final int STORED = 0;
      static final int COMPRESSED_MOST = 1;
      static final int COMPRESSED_FASTEST = 4;
      static final int NO_DATA_NO_CRC = 8;
      static final int NO_DATA = 9;
   }

   static class FileTypes {
      static final int BINARY = 0;
      static final int SEVEN_BIT_TEXT = 1;
      static final int DIRECTORY = 3;
      static final int VOLUME_LABEL = 4;
      static final int CHAPTER_LABEL = 5;
   }

   static class Flags {
      static final int GARBLED = 1;
      static final int VOLUME = 4;
      static final int EXTFILE = 8;
      static final int PATHSYM = 16;
      static final int BACKUP = 32;
   }
}
