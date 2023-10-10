package org.apache.commons.compress.archivers.arj;

import java.util.Arrays;

class MainHeader {
   int archiverVersionNumber;
   int minVersionToExtract;
   int hostOS;
   int arjFlags;
   int securityVersion;
   int fileType;
   int reserved;
   int dateTimeCreated;
   int dateTimeModified;
   long archiveSize;
   int securityEnvelopeFilePosition;
   int fileSpecPosition;
   int securityEnvelopeLength;
   int encryptionVersion;
   int lastChapter;
   int arjProtectionFactor;
   int arjFlags2;
   String name;
   String comment;
   byte[] extendedHeaderBytes = null;

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("MainHeader [archiverVersionNumber=");
      var1.append(this.archiverVersionNumber);
      var1.append(", minVersionToExtract=");
      var1.append(this.minVersionToExtract);
      var1.append(", hostOS=");
      var1.append(this.hostOS);
      var1.append(", arjFlags=");
      var1.append(this.arjFlags);
      var1.append(", securityVersion=");
      var1.append(this.securityVersion);
      var1.append(", fileType=");
      var1.append(this.fileType);
      var1.append(", reserved=");
      var1.append(this.reserved);
      var1.append(", dateTimeCreated=");
      var1.append(this.dateTimeCreated);
      var1.append(", dateTimeModified=");
      var1.append(this.dateTimeModified);
      var1.append(", archiveSize=");
      var1.append(this.archiveSize);
      var1.append(", securityEnvelopeFilePosition=");
      var1.append(this.securityEnvelopeFilePosition);
      var1.append(", fileSpecPosition=");
      var1.append(this.fileSpecPosition);
      var1.append(", securityEnvelopeLength=");
      var1.append(this.securityEnvelopeLength);
      var1.append(", encryptionVersion=");
      var1.append(this.encryptionVersion);
      var1.append(", lastChapter=");
      var1.append(this.lastChapter);
      var1.append(", arjProtectionFactor=");
      var1.append(this.arjProtectionFactor);
      var1.append(", arjFlags2=");
      var1.append(this.arjFlags2);
      var1.append(", name=");
      var1.append(this.name);
      var1.append(", comment=");
      var1.append(this.comment);
      var1.append(", extendedHeaderBytes=");
      var1.append(Arrays.toString(this.extendedHeaderBytes));
      var1.append("]");
      return var1.toString();
   }

   static class Flags {
      static final int GARBLED = 1;
      static final int OLD_SECURED_NEW_ANSI_PAGE = 2;
      static final int VOLUME = 4;
      static final int ARJPROT = 8;
      static final int PATHSYM = 16;
      static final int BACKUP = 32;
      static final int SECURED = 64;
      static final int ALTNAME = 128;
   }
}
