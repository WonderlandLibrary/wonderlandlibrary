package org.apache.commons.compress.archivers.arj;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.CRC32;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.BoundedInputStream;
import org.apache.commons.compress.utils.CRC32VerifyingInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class ArjArchiveInputStream extends ArchiveInputStream {
   private static final int ARJ_MAGIC_1 = 96;
   private static final int ARJ_MAGIC_2 = 234;
   private final DataInputStream in;
   private final String charsetName;
   private final MainHeader mainHeader;
   private LocalFileHeader currentLocalFileHeader;
   private InputStream currentInputStream;

   public ArjArchiveInputStream(InputStream var1, String var2) throws ArchiveException {
      this.currentLocalFileHeader = null;
      this.currentInputStream = null;
      this.in = new DataInputStream(var1);
      this.charsetName = var2;

      try {
         this.mainHeader = this.readMainHeader();
         if ((this.mainHeader.arjFlags & 1) != 0) {
            throw new ArchiveException("Encrypted ARJ files are unsupported");
         } else if ((this.mainHeader.arjFlags & 4) != 0) {
            throw new ArchiveException("Multi-volume ARJ files are unsupported");
         }
      } catch (IOException var4) {
         throw new ArchiveException(var4.getMessage(), var4);
      }
   }

   public ArjArchiveInputStream(InputStream var1) throws ArchiveException {
      this(var1, "CP437");
   }

   public void close() throws IOException {
      this.in.close();
   }

   private int read8(DataInputStream var1) throws IOException {
      int var2 = var1.readUnsignedByte();
      this.count(1);
      return var2;
   }

   private int read16(DataInputStream var1) throws IOException {
      int var2 = var1.readUnsignedShort();
      this.count(2);
      return Integer.reverseBytes(var2) >>> 16;
   }

   private int read32(DataInputStream var1) throws IOException {
      int var2 = var1.readInt();
      this.count(4);
      return Integer.reverseBytes(var2);
   }

   private String readString(DataInputStream var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();

      int var3;
      while((var3 = var1.readUnsignedByte()) != 0) {
         var2.write(var3);
      }

      return this.charsetName != null ? new String(var2.toByteArray(), this.charsetName) : new String(var2.toByteArray());
   }

   private void readFully(DataInputStream var1, byte[] var2) throws IOException {
      var1.readFully(var2);
      this.count(var2.length);
   }

   private byte[] readHeader() throws IOException {
      boolean var1 = false;
      byte[] var2 = null;

      do {
         boolean var3 = false;
         int var4 = this.read8(this.in);

         int var9;
         do {
            var9 = var4;
            var4 = this.read8(this.in);
         } while(var9 != 96 && var4 != 234);

         int var5 = this.read16(this.in);
         if (var5 == 0) {
            return null;
         }

         if (var5 <= 2600) {
            var2 = new byte[var5];
            this.readFully(this.in, var2);
            long var6 = (long)this.read32(this.in) & 4294967295L;
            CRC32 var8 = new CRC32();
            var8.update(var2);
            if (var6 == var8.getValue()) {
               var1 = true;
            }
         }
      } while(!var1);

      return var2;
   }

   private MainHeader readMainHeader() throws IOException {
      byte[] var1 = this.readHeader();
      if (var1 == null) {
         throw new IOException("Archive ends without any headers");
      } else {
         DataInputStream var2 = new DataInputStream(new ByteArrayInputStream(var1));
         int var3 = var2.readUnsignedByte();
         byte[] var4 = new byte[var3 - 1];
         var2.readFully(var4);
         DataInputStream var5 = new DataInputStream(new ByteArrayInputStream(var4));
         MainHeader var6 = new MainHeader();
         var6.archiverVersionNumber = var5.readUnsignedByte();
         var6.minVersionToExtract = var5.readUnsignedByte();
         var6.hostOS = var5.readUnsignedByte();
         var6.arjFlags = var5.readUnsignedByte();
         var6.securityVersion = var5.readUnsignedByte();
         var6.fileType = var5.readUnsignedByte();
         var6.reserved = var5.readUnsignedByte();
         var6.dateTimeCreated = this.read32(var5);
         var6.dateTimeModified = this.read32(var5);
         var6.archiveSize = 4294967295L & (long)this.read32(var5);
         var6.securityEnvelopeFilePosition = this.read32(var5);
         var6.fileSpecPosition = this.read16(var5);
         var6.securityEnvelopeLength = this.read16(var5);
         this.pushedBackBytes(20L);
         var6.encryptionVersion = var5.readUnsignedByte();
         var6.lastChapter = var5.readUnsignedByte();
         if (var3 >= 33) {
            var6.arjProtectionFactor = var5.readUnsignedByte();
            var6.arjFlags2 = var5.readUnsignedByte();
            var5.readUnsignedByte();
            var5.readUnsignedByte();
         }

         var6.name = this.readString(var2);
         var6.comment = this.readString(var2);
         int var7 = this.read16(this.in);
         if (var7 > 0) {
            var6.extendedHeaderBytes = new byte[var7];
            this.readFully(this.in, var6.extendedHeaderBytes);
            long var8 = 4294967295L & (long)this.read32(this.in);
            CRC32 var10 = new CRC32();
            var10.update(var6.extendedHeaderBytes);
            if (var8 != var10.getValue()) {
               throw new IOException("Extended header CRC32 verification failure");
            }
         }

         return var6;
      }
   }

   private LocalFileHeader readLocalFileHeader() throws IOException {
      byte[] var1 = this.readHeader();
      if (var1 == null) {
         return null;
      } else {
         DataInputStream var2 = new DataInputStream(new ByteArrayInputStream(var1));
         int var3 = var2.readUnsignedByte();
         byte[] var4 = new byte[var3 - 1];
         var2.readFully(var4);
         DataInputStream var5 = new DataInputStream(new ByteArrayInputStream(var4));
         LocalFileHeader var6 = new LocalFileHeader();
         var6.archiverVersionNumber = var5.readUnsignedByte();
         var6.minVersionToExtract = var5.readUnsignedByte();
         var6.hostOS = var5.readUnsignedByte();
         var6.arjFlags = var5.readUnsignedByte();
         var6.method = var5.readUnsignedByte();
         var6.fileType = var5.readUnsignedByte();
         var6.reserved = var5.readUnsignedByte();
         var6.dateTimeModified = this.read32(var5);
         var6.compressedSize = 4294967295L & (long)this.read32(var5);
         var6.originalSize = 4294967295L & (long)this.read32(var5);
         var6.originalCrc32 = 4294967295L & (long)this.read32(var5);
         var6.fileSpecPosition = this.read16(var5);
         var6.fileAccessMode = this.read16(var5);
         this.pushedBackBytes(20L);
         var6.firstChapter = var5.readUnsignedByte();
         var6.lastChapter = var5.readUnsignedByte();
         this.readExtraData(var3, var5, var6);
         var6.name = this.readString(var2);
         var6.comment = this.readString(var2);
         ArrayList var7 = new ArrayList();

         int var8;
         while((var8 = this.read16(this.in)) > 0) {
            byte[] var9 = new byte[var8];
            this.readFully(this.in, var9);
            long var10 = 4294967295L & (long)this.read32(this.in);
            CRC32 var12 = new CRC32();
            var12.update(var9);
            if (var10 != var12.getValue()) {
               throw new IOException("Extended header CRC32 verification failure");
            }

            var7.add(var9);
         }

         var6.extendedHeaders = (byte[][])var7.toArray(new byte[var7.size()][]);
         return var6;
      }
   }

   private void readExtraData(int var1, DataInputStream var2, LocalFileHeader var3) throws IOException {
      if (var1 >= 33) {
         var3.extendedFilePosition = this.read32(var2);
         if (var1 >= 45) {
            var3.dateTimeAccessed = this.read32(var2);
            var3.dateTimeCreated = this.read32(var2);
            var3.originalSizeEvenForVolumes = this.read32(var2);
            this.pushedBackBytes(12L);
         }

         this.pushedBackBytes(4L);
      }

   }

   public static boolean matches(byte[] var0, int var1) {
      return var1 >= 2 && (255 & var0[0]) == 96 && (255 & var0[1]) == 234;
   }

   public String getArchiveName() {
      return this.mainHeader.name;
   }

   public String getArchiveComment() {
      return this.mainHeader.comment;
   }

   public ArjArchiveEntry getNextEntry() throws IOException {
      if (this.currentInputStream != null) {
         IOUtils.skip(this.currentInputStream, Long.MAX_VALUE);
         this.currentInputStream.close();
         this.currentLocalFileHeader = null;
         this.currentInputStream = null;
      }

      this.currentLocalFileHeader = this.readLocalFileHeader();
      if (this.currentLocalFileHeader != null) {
         this.currentInputStream = new BoundedInputStream(this.in, this.currentLocalFileHeader.compressedSize);
         if (this.currentLocalFileHeader.method == 0) {
            this.currentInputStream = new CRC32VerifyingInputStream(this.currentInputStream, this.currentLocalFileHeader.originalSize, this.currentLocalFileHeader.originalCrc32);
         }

         return new ArjArchiveEntry(this.currentLocalFileHeader);
      } else {
         this.currentInputStream = null;
         return null;
      }
   }

   public boolean canReadEntryData(ArchiveEntry var1) {
      return var1 instanceof ArjArchiveEntry && ((ArjArchiveEntry)var1).getMethod() == 0;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.currentLocalFileHeader == null) {
         throw new IllegalStateException("No current arj entry");
      } else if (this.currentLocalFileHeader.method != 0) {
         throw new IOException("Unsupported compression method " + this.currentLocalFileHeader.method);
      } else {
         return this.currentInputStream.read(var1, var2, var3);
      }
   }

   public ArchiveEntry getNextEntry() throws IOException {
      return this.getNextEntry();
   }
}
