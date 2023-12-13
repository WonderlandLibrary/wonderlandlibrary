package org.apache.commons.compress.archivers.tar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.IOUtils;

public class TarArchiveInputStream extends ArchiveInputStream {
   private static final int SMALL_BUFFER_SIZE = 256;
   private final byte[] SMALL_BUF;
   private final int recordSize;
   private final int blockSize;
   private boolean hasHitEOF;
   private long entrySize;
   private long entryOffset;
   private final InputStream is;
   private TarArchiveEntry currEntry;
   private final ZipEncoding encoding;

   public TarArchiveInputStream(InputStream var1) {
      this(var1, 10240, 512);
   }

   public TarArchiveInputStream(InputStream var1, String var2) {
      this(var1, 10240, 512, var2);
   }

   public TarArchiveInputStream(InputStream var1, int var2) {
      this(var1, var2, 512);
   }

   public TarArchiveInputStream(InputStream var1, int var2, String var3) {
      this(var1, var2, 512, var3);
   }

   public TarArchiveInputStream(InputStream var1, int var2, int var3) {
      this(var1, var2, var3, (String)null);
   }

   public TarArchiveInputStream(InputStream var1, int var2, int var3, String var4) {
      this.SMALL_BUF = new byte[256];
      this.is = var1;
      this.hasHitEOF = false;
      this.encoding = ZipEncodingHelper.getZipEncoding(var4);
      this.recordSize = var3;
      this.blockSize = var2;
   }

   public void close() throws IOException {
      this.is.close();
   }

   public int getRecordSize() {
      return this.recordSize;
   }

   public int available() throws IOException {
      return this.entrySize - this.entryOffset > 2147483647L ? Integer.MAX_VALUE : (int)(this.entrySize - this.entryOffset);
   }

   public long skip(long var1) throws IOException {
      if (var1 <= 0L) {
         return 0L;
      } else {
         long var3 = this.entrySize - this.entryOffset;
         long var5 = this.is.skip(Math.min(var1, var3));
         this.count(var5);
         this.entryOffset += var5;
         return var5;
      }
   }

   public synchronized void reset() {
   }

   public TarArchiveEntry getNextTarEntry() throws IOException {
      if (this.hasHitEOF) {
         return null;
      } else {
         if (this.currEntry != null) {
            IOUtils.skip(this, Long.MAX_VALUE);
            this.skipRecordPadding();
         }

         byte[] var1 = this.getRecord();
         if (var1 == null) {
            this.currEntry = null;
            return null;
         } else {
            try {
               this.currEntry = new TarArchiveEntry(var1, this.encoding);
            } catch (IllegalArgumentException var4) {
               IOException var3 = new IOException("Error detected parsing the header");
               var3.initCause(var4);
               throw var3;
            }

            this.entryOffset = 0L;
            this.entrySize = this.currEntry.getSize();
            byte[] var2;
            if (this.currEntry.isGNULongLinkEntry()) {
               var2 = this.getLongNameData();
               if (var2 == null) {
                  return null;
               }

               this.currEntry.setLinkName(this.encoding.decode(var2));
            }

            if (this.currEntry.isGNULongNameEntry()) {
               var2 = this.getLongNameData();
               if (var2 == null) {
                  return null;
               }

               this.currEntry.setName(this.encoding.decode(var2));
            }

            if (this.currEntry.isPaxHeader()) {
               this.paxHeaders();
            }

            if (this.currEntry.isGNUSparse()) {
               this.readGNUSparse();
            }

            this.entrySize = this.currEntry.getSize();
            return this.currEntry;
         }
      }
   }

   private void skipRecordPadding() throws IOException {
      if (this.entrySize > 0L && this.entrySize % (long)this.recordSize != 0L) {
         long var1 = this.entrySize / (long)this.recordSize + 1L;
         long var3 = var1 * (long)this.recordSize - this.entrySize;
         long var5 = IOUtils.skip(this.is, var3);
         this.count(var5);
      }

   }

   protected byte[] getLongNameData() throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      boolean var2 = false;

      int var5;
      while((var5 = this.read(this.SMALL_BUF)) >= 0) {
         var1.write(this.SMALL_BUF, 0, var5);
      }

      this.getNextEntry();
      if (this.currEntry == null) {
         return null;
      } else {
         byte[] var3 = var1.toByteArray();

         for(var5 = var3.length; var5 > 0 && var3[var5 - 1] == 0; --var5) {
         }

         if (var5 != var3.length) {
            byte[] var4 = new byte[var5];
            System.arraycopy(var3, 0, var4, 0, var5);
            var3 = var4;
         }

         return var3;
      }
   }

   private byte[] getRecord() throws IOException {
      byte[] var1 = this.readRecord();
      this.hasHitEOF = this.isEOFRecord(var1);
      if (this.hasHitEOF && var1 != null) {
         this.tryToConsumeSecondEOFRecord();
         this.consumeRemainderOfLastBlock();
         var1 = null;
      }

      return var1;
   }

   protected byte[] readRecord() throws IOException {
      byte[] var1 = new byte[this.recordSize];
      int var2 = IOUtils.readFully(this.is, var1);
      this.count(var2);
      return var2 != this.recordSize ? null : var1;
   }

   private void paxHeaders() throws IOException {
      Map var1 = this.parsePaxHeaders(this);
      this.getNextEntry();
      this.applyPaxHeadersToCurrentEntry(var1);
   }

   Map parsePaxHeaders(InputStream var1) throws IOException {
      HashMap var2 = new HashMap();

      int var3;
      label37:
      do {
         int var4 = 0;

         for(int var5 = 0; (var3 = var1.read()) != -1; var4 += var3 - 48) {
            ++var5;
            if (var3 == 32) {
               ByteArrayOutputStream var6 = new ByteArrayOutputStream();

               while(true) {
                  if ((var3 = var1.read()) == -1) {
                     continue label37;
                  }

                  ++var5;
                  if (var3 == 61) {
                     String var7 = var6.toString("UTF-8");
                     byte[] var8 = new byte[var4 - var5];
                     int var9 = IOUtils.readFully(var1, var8);
                     if (var9 != var4 - var5) {
                        throw new IOException("Failed to read Paxheader. Expected " + (var4 - var5) + " bytes, read " + var9);
                     }

                     String var10 = new String(var8, 0, var4 - var5 - 1, "UTF-8");
                     var2.put(var7, var10);
                     continue label37;
                  }

                  var6.write((byte)var3);
               }
            }

            var4 *= 10;
         }
      } while(var3 != -1);

      return var2;
   }

   private void applyPaxHeadersToCurrentEntry(Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         String var4 = (String)var3.getKey();
         String var5 = (String)var3.getValue();
         if ("path".equals(var4)) {
            this.currEntry.setName(var5);
         } else if ("linkpath".equals(var4)) {
            this.currEntry.setLinkName(var5);
         } else if ("gid".equals(var4)) {
            this.currEntry.setGroupId(Integer.parseInt(var5));
         } else if ("gname".equals(var4)) {
            this.currEntry.setGroupName(var5);
         } else if ("uid".equals(var4)) {
            this.currEntry.setUserId(Integer.parseInt(var5));
         } else if ("uname".equals(var4)) {
            this.currEntry.setUserName(var5);
         } else if ("size".equals(var4)) {
            this.currEntry.setSize(Long.parseLong(var5));
         } else if ("mtime".equals(var4)) {
            this.currEntry.setModTime((long)(Double.parseDouble(var5) * 1000.0D));
         } else if ("SCHILY.devminor".equals(var4)) {
            this.currEntry.setDevMinor(Integer.parseInt(var5));
         } else if ("SCHILY.devmajor".equals(var4)) {
            this.currEntry.setDevMajor(Integer.parseInt(var5));
         }
      }

   }

   private void readGNUSparse() throws IOException {
      TarArchiveSparseEntry var1;
      if (this.currEntry.isExtended()) {
         do {
            byte[] var2 = this.getRecord();
            if (var2 == null) {
               this.currEntry = null;
               break;
            }

            var1 = new TarArchiveSparseEntry(var2);
         } while(var1.isExtended());
      }

   }

   public ArchiveEntry getNextEntry() throws IOException {
      return this.getNextTarEntry();
   }

   private void tryToConsumeSecondEOFRecord() throws IOException {
      boolean var1 = true;
      boolean var2 = this.is.markSupported();
      if (var2) {
         this.is.mark(this.recordSize);
      }

      var1 = this.readRecord() != null;
      if (var1 && var2) {
         this.pushedBackBytes((long)this.recordSize);
         this.is.reset();
      }

   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      boolean var4 = false;
      if (!this.hasHitEOF && this.entryOffset < this.entrySize) {
         if (this.currEntry == null) {
            throw new IllegalStateException("No current tar entry");
         } else {
            var3 = Math.min(var3, this.available());
            int var5 = this.is.read(var1, var2, var3);
            if (var5 == -1) {
               if (var3 > 0) {
                  throw new IOException("Truncated TAR archive");
               }

               this.hasHitEOF = true;
            } else {
               this.count(var5);
               this.entryOffset += (long)var5;
            }

            return var5;
         }
      } else {
         return -1;
      }
   }

   public boolean canReadEntryData(ArchiveEntry var1) {
      if (var1 instanceof TarArchiveEntry) {
         TarArchiveEntry var2 = (TarArchiveEntry)var1;
         return !var2.isGNUSparse();
      } else {
         return false;
      }
   }

   public TarArchiveEntry getCurrentEntry() {
      return this.currEntry;
   }

   protected final void setCurrentEntry(TarArchiveEntry var1) {
      this.currEntry = var1;
   }

   protected final boolean isAtEOF() {
      return this.hasHitEOF;
   }

   protected final void setAtEOF(boolean var1) {
      this.hasHitEOF = var1;
   }

   private void consumeRemainderOfLastBlock() throws IOException {
      long var1 = this.getBytesRead() % (long)this.blockSize;
      if (var1 > 0L) {
         long var3 = IOUtils.skip(this.is, (long)this.blockSize - var1);
         this.count(var3);
      }

   }

   public static boolean matches(byte[] var0, int var1) {
      if (var1 < 265) {
         return false;
      } else if (ArchiveUtils.matchAsciiBuffer("ustar\u0000", var0, 257, 6) && ArchiveUtils.matchAsciiBuffer("00", var0, 263, 2)) {
         return true;
      } else if (ArchiveUtils.matchAsciiBuffer("ustar ", var0, 257, 6) && (ArchiveUtils.matchAsciiBuffer(" \u0000", var0, 263, 2) || ArchiveUtils.matchAsciiBuffer("0\u0000", var0, 263, 2))) {
         return true;
      } else {
         return ArchiveUtils.matchAsciiBuffer("ustar\u0000", var0, 257, 6) && ArchiveUtils.matchAsciiBuffer("\u0000\u0000", var0, 263, 2);
      }
   }
}
