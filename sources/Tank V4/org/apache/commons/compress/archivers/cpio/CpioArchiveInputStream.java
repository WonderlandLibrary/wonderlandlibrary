package org.apache.commons.compress.archivers.cpio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.IOUtils;

public class CpioArchiveInputStream extends ArchiveInputStream implements CpioConstants {
   private boolean closed;
   private CpioArchiveEntry entry;
   private long entryBytesRead;
   private boolean entryEOF;
   private final byte[] tmpbuf;
   private long crc;
   private final InputStream in;
   private final byte[] TWO_BYTES_BUF;
   private final byte[] FOUR_BYTES_BUF;
   private final byte[] SIX_BYTES_BUF;
   private final int blockSize;
   private final ZipEncoding encoding;

   public CpioArchiveInputStream(InputStream var1) {
      this(var1, 512, "US-ASCII");
   }

   public CpioArchiveInputStream(InputStream var1, String var2) {
      this(var1, 512, var2);
   }

   public CpioArchiveInputStream(InputStream var1, int var2) {
      this(var1, var2, "US-ASCII");
   }

   public CpioArchiveInputStream(InputStream var1, int var2, String var3) {
      this.closed = false;
      this.entryBytesRead = 0L;
      this.entryEOF = false;
      this.tmpbuf = new byte[4096];
      this.crc = 0L;
      this.TWO_BYTES_BUF = new byte[2];
      this.FOUR_BYTES_BUF = new byte[4];
      this.SIX_BYTES_BUF = new byte[6];
      this.in = var1;
      this.blockSize = var2;
      this.encoding = ZipEncodingHelper.getZipEncoding(var3);
   }

   public int available() throws IOException {
      this.ensureOpen();
      return this.entryEOF ? 0 : 1;
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.in.close();
         this.closed = true;
      }

   }

   private void closeEntry() throws IOException {
      while(this.skip(2147483647L) == 2147483647L) {
      }

   }

   private void ensureOpen() throws IOException {
      if (this.closed) {
         throw new IOException("Stream closed");
      }
   }

   public CpioArchiveEntry getNextCPIOEntry() throws IOException {
      this.ensureOpen();
      if (this.entry != null) {
         this.closeEntry();
      }

      this.readFully(this.TWO_BYTES_BUF, 0, this.TWO_BYTES_BUF.length);
      if (CpioUtil.byteArray2long(this.TWO_BYTES_BUF, false) == 29127L) {
         this.entry = this.readOldBinaryEntry(false);
      } else if (CpioUtil.byteArray2long(this.TWO_BYTES_BUF, true) == 29127L) {
         this.entry = this.readOldBinaryEntry(true);
      } else {
         System.arraycopy(this.TWO_BYTES_BUF, 0, this.SIX_BYTES_BUF, 0, this.TWO_BYTES_BUF.length);
         this.readFully(this.SIX_BYTES_BUF, this.TWO_BYTES_BUF.length, this.FOUR_BYTES_BUF.length);
         String var1 = ArchiveUtils.toAsciiString(this.SIX_BYTES_BUF);
         if (var1.equals("070701")) {
            this.entry = this.readNewEntry(false);
         } else if (var1.equals("070702")) {
            this.entry = this.readNewEntry(true);
         } else {
            if (!var1.equals("070707")) {
               throw new IOException("Unknown magic [" + var1 + "]. Occured at byte: " + this.getBytesRead());
            }

            this.entry = this.readOldAsciiEntry();
         }
      }

      this.entryBytesRead = 0L;
      this.entryEOF = false;
      this.crc = 0L;
      if (this.entry.getName().equals("TRAILER!!!")) {
         this.entryEOF = true;
         this.skipRemainderOfLastBlock();
         return null;
      } else {
         return this.entry;
      }
   }

   private void skip(int var1) throws IOException {
      if (var1 > 0) {
         this.readFully(this.FOUR_BYTES_BUF, 0, var1);
      }

   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      this.ensureOpen();
      if (var2 >= 0 && var3 >= 0 && var2 <= var1.length - var3) {
         if (var3 == 0) {
            return 0;
         } else if (this.entry != null && !this.entryEOF) {
            if (this.entryBytesRead == this.entry.getSize()) {
               this.skip(this.entry.getDataPadCount());
               this.entryEOF = true;
               if (this.entry.getFormat() == 2 && this.crc != this.entry.getChksum()) {
                  throw new IOException("CRC Error. Occured at byte: " + this.getBytesRead());
               } else {
                  return -1;
               }
            } else {
               int var4 = (int)Math.min((long)var3, this.entry.getSize() - this.entryBytesRead);
               if (var4 < 0) {
                  return -1;
               } else {
                  int var5 = this.readFully(var1, var2, var4);
                  if (this.entry.getFormat() == 2) {
                     for(int var6 = 0; var6 < var5; ++var6) {
                        this.crc += (long)(var1[var6] & 255);
                     }
                  }

                  this.entryBytesRead += (long)var5;
                  return var5;
               }
            }
         } else {
            return -1;
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   private final int readFully(byte[] var1, int var2, int var3) throws IOException {
      int var4 = IOUtils.readFully(this.in, var1, var2, var3);
      this.count(var4);
      if (var4 < var3) {
         throw new EOFException();
      } else {
         return var4;
      }
   }

   private long readBinaryLong(int var1, boolean var2) throws IOException {
      byte[] var3 = new byte[var1];
      this.readFully(var3, 0, var3.length);
      return CpioUtil.byteArray2long(var3, var2);
   }

   private long readAsciiLong(int var1, int var2) throws IOException {
      byte[] var3 = new byte[var1];
      this.readFully(var3, 0, var3.length);
      return Long.parseLong(ArchiveUtils.toAsciiString(var3), var2);
   }

   private CpioArchiveEntry readNewEntry(boolean var1) throws IOException {
      CpioArchiveEntry var2;
      if (var1) {
         var2 = new CpioArchiveEntry((short)2);
      } else {
         var2 = new CpioArchiveEntry((short)1);
      }

      var2.setInode(this.readAsciiLong(8, 16));
      long var3 = this.readAsciiLong(8, 16);
      if (CpioUtil.fileType(var3) != 0L) {
         var2.setMode(var3);
      }

      var2.setUID(this.readAsciiLong(8, 16));
      var2.setGID(this.readAsciiLong(8, 16));
      var2.setNumberOfLinks(this.readAsciiLong(8, 16));
      var2.setTime(this.readAsciiLong(8, 16));
      var2.setSize(this.readAsciiLong(8, 16));
      var2.setDeviceMaj(this.readAsciiLong(8, 16));
      var2.setDeviceMin(this.readAsciiLong(8, 16));
      var2.setRemoteDeviceMaj(this.readAsciiLong(8, 16));
      var2.setRemoteDeviceMin(this.readAsciiLong(8, 16));
      long var5 = this.readAsciiLong(8, 16);
      var2.setChksum(this.readAsciiLong(8, 16));
      String var7 = this.readCString((int)var5);
      var2.setName(var7);
      if (CpioUtil.fileType(var3) == 0L && !var7.equals("TRAILER!!!")) {
         throw new IOException("Mode 0 only allowed in the trailer. Found entry name: " + var7 + " Occured at byte: " + this.getBytesRead());
      } else {
         this.skip(var2.getHeaderPadCount());
         return var2;
      }
   }

   private CpioArchiveEntry readOldAsciiEntry() throws IOException {
      CpioArchiveEntry var1 = new CpioArchiveEntry((short)4);
      var1.setDevice(this.readAsciiLong(6, 8));
      var1.setInode(this.readAsciiLong(6, 8));
      long var2 = this.readAsciiLong(6, 8);
      if (CpioUtil.fileType(var2) != 0L) {
         var1.setMode(var2);
      }

      var1.setUID(this.readAsciiLong(6, 8));
      var1.setGID(this.readAsciiLong(6, 8));
      var1.setNumberOfLinks(this.readAsciiLong(6, 8));
      var1.setRemoteDevice(this.readAsciiLong(6, 8));
      var1.setTime(this.readAsciiLong(11, 8));
      long var4 = this.readAsciiLong(6, 8);
      var1.setSize(this.readAsciiLong(11, 8));
      String var6 = this.readCString((int)var4);
      var1.setName(var6);
      if (CpioUtil.fileType(var2) == 0L && !var6.equals("TRAILER!!!")) {
         throw new IOException("Mode 0 only allowed in the trailer. Found entry: " + var6 + " Occured at byte: " + this.getBytesRead());
      } else {
         return var1;
      }
   }

   private CpioArchiveEntry readOldBinaryEntry(boolean var1) throws IOException {
      CpioArchiveEntry var2 = new CpioArchiveEntry((short)8);
      var2.setDevice(this.readBinaryLong(2, var1));
      var2.setInode(this.readBinaryLong(2, var1));
      long var3 = this.readBinaryLong(2, var1);
      if (CpioUtil.fileType(var3) != 0L) {
         var2.setMode(var3);
      }

      var2.setUID(this.readBinaryLong(2, var1));
      var2.setGID(this.readBinaryLong(2, var1));
      var2.setNumberOfLinks(this.readBinaryLong(2, var1));
      var2.setRemoteDevice(this.readBinaryLong(2, var1));
      var2.setTime(this.readBinaryLong(4, var1));
      long var5 = this.readBinaryLong(2, var1);
      var2.setSize(this.readBinaryLong(4, var1));
      String var7 = this.readCString((int)var5);
      var2.setName(var7);
      if (CpioUtil.fileType(var3) == 0L && !var7.equals("TRAILER!!!")) {
         throw new IOException("Mode 0 only allowed in the trailer. Found entry: " + var7 + "Occured at byte: " + this.getBytesRead());
      } else {
         this.skip(var2.getHeaderPadCount());
         return var2;
      }
   }

   private String readCString(int var1) throws IOException {
      byte[] var2 = new byte[var1 - 1];
      this.readFully(var2, 0, var2.length);
      this.in.read();
      return this.encoding.decode(var2);
   }

   public long skip(long var1) throws IOException {
      if (var1 < 0L) {
         throw new IllegalArgumentException("negative skip length");
      } else {
         this.ensureOpen();
         int var3 = (int)Math.min(var1, 2147483647L);

         int var4;
         int var5;
         for(var4 = 0; var4 < var3; var4 += var5) {
            var5 = var3 - var4;
            if (var5 > this.tmpbuf.length) {
               var5 = this.tmpbuf.length;
            }

            var5 = this.read(this.tmpbuf, 0, var5);
            if (var5 == -1) {
               this.entryEOF = true;
               break;
            }
         }

         return (long)var4;
      }
   }

   public ArchiveEntry getNextEntry() throws IOException {
      return this.getNextCPIOEntry();
   }

   private void skipRemainderOfLastBlock() throws IOException {
      long var1 = this.getBytesRead() % (long)this.blockSize;

      long var5;
      for(long var3 = var1 == 0L ? 0L : (long)this.blockSize - var1; var3 > 0L; var3 -= var5) {
         var5 = this.skip((long)this.blockSize - var1);
         if (var5 <= 0L) {
            break;
         }
      }

   }

   public static boolean matches(byte[] var0, int var1) {
      if (var1 < 6) {
         return false;
      } else if (var0[0] == 113 && (var0[1] & 255) == 199) {
         return true;
      } else if (var0[1] == 113 && (var0[0] & 255) == 199) {
         return true;
      } else if (var0[0] != 48) {
         return false;
      } else if (var0[1] != 55) {
         return false;
      } else if (var0[2] != 48) {
         return false;
      } else if (var0[3] != 55) {
         return false;
      } else if (var0[4] != 48) {
         return false;
      } else if (var0[5] == 49) {
         return true;
      } else if (var0[5] == 50) {
         return true;
      } else {
         return var0[5] == 55;
      }
   }
}
