package org.apache.commons.compress.archivers.cpio;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.ArchiveUtils;

public class CpioArchiveOutputStream extends ArchiveOutputStream implements CpioConstants {
   private CpioArchiveEntry entry;
   private boolean closed;
   private boolean finished;
   private final short entryFormat;
   private final HashMap names;
   private long crc;
   private long written;
   private final OutputStream out;
   private final int blockSize;
   private long nextArtificalDeviceAndInode;
   private final ZipEncoding encoding;

   public CpioArchiveOutputStream(OutputStream var1, short var2) {
      this(var1, var2, 512, "US-ASCII");
   }

   public CpioArchiveOutputStream(OutputStream var1, short var2, int var3) {
      this(var1, var2, var3, "US-ASCII");
   }

   public CpioArchiveOutputStream(OutputStream var1, short var2, int var3, String var4) {
      this.closed = false;
      this.names = new HashMap();
      this.crc = 0L;
      this.nextArtificalDeviceAndInode = 1L;
      this.out = var1;
      switch(var2) {
      case 1:
      case 2:
      case 4:
      case 8:
         this.entryFormat = var2;
         this.blockSize = var3;
         this.encoding = ZipEncodingHelper.getZipEncoding(var4);
         return;
      case 3:
      case 5:
      case 6:
      case 7:
      default:
         throw new IllegalArgumentException("Unknown format: " + var2);
      }
   }

   public CpioArchiveOutputStream(OutputStream var1) {
      this(var1, (short)1);
   }

   public CpioArchiveOutputStream(OutputStream var1, String var2) {
      this(var1, (short)1, 512, var2);
   }

   private void ensureOpen() throws IOException {
      if (this.closed) {
         throw new IOException("Stream closed");
      }
   }

   public void putArchiveEntry(ArchiveEntry var1) throws IOException {
      if (this.finished) {
         throw new IOException("Stream has already been finished");
      } else {
         CpioArchiveEntry var2 = (CpioArchiveEntry)var1;
         this.ensureOpen();
         if (this.entry != null) {
            this.closeArchiveEntry();
         }

         if (var2.getTime() == -1L) {
            var2.setTime(System.currentTimeMillis() / 1000L);
         }

         short var3 = var2.getFormat();
         if (var3 != this.entryFormat) {
            throw new IOException("Header format: " + var3 + " does not match existing format: " + this.entryFormat);
         } else if (this.names.put(var2.getName(), var2) != null) {
            throw new IOException("duplicate entry: " + var2.getName());
         } else {
            this.writeHeader(var2);
            this.entry = var2;
            this.written = 0L;
         }
      }
   }

   private void writeHeader(CpioArchiveEntry var1) throws IOException {
      switch(var1.getFormat()) {
      case 1:
         this.out.write(ArchiveUtils.toAsciiBytes("070701"));
         this.count(6);
         this.writeNewEntry(var1);
         break;
      case 2:
         this.out.write(ArchiveUtils.toAsciiBytes("070702"));
         this.count(6);
         this.writeNewEntry(var1);
         break;
      case 3:
      case 5:
      case 6:
      case 7:
      default:
         throw new IOException("unknown format " + var1.getFormat());
      case 4:
         this.out.write(ArchiveUtils.toAsciiBytes("070707"));
         this.count(6);
         this.writeOldAsciiEntry(var1);
         break;
      case 8:
         boolean var2 = true;
         this.writeBinaryLong(29127L, 2, var2);
         this.writeOldBinaryEntry(var1, var2);
      }

   }

   private void writeNewEntry(CpioArchiveEntry var1) throws IOException {
      long var2 = var1.getInode();
      long var4 = var1.getDeviceMin();
      if ("TRAILER!!!".equals(var1.getName())) {
         var4 = 0L;
         var2 = 0L;
      } else if (var2 == 0L && var4 == 0L) {
         var2 = this.nextArtificalDeviceAndInode & -1L;
         var4 = this.nextArtificalDeviceAndInode++ >> 32 & -1L;
      } else {
         this.nextArtificalDeviceAndInode = Math.max(this.nextArtificalDeviceAndInode, var2 + 4294967296L * var4) + 1L;
      }

      this.writeAsciiLong(var2, 8, 16);
      this.writeAsciiLong(var1.getMode(), 8, 16);
      this.writeAsciiLong(var1.getUID(), 8, 16);
      this.writeAsciiLong(var1.getGID(), 8, 16);
      this.writeAsciiLong(var1.getNumberOfLinks(), 8, 16);
      this.writeAsciiLong(var1.getTime(), 8, 16);
      this.writeAsciiLong(var1.getSize(), 8, 16);
      this.writeAsciiLong(var1.getDeviceMaj(), 8, 16);
      this.writeAsciiLong(var4, 8, 16);
      this.writeAsciiLong(var1.getRemoteDeviceMaj(), 8, 16);
      this.writeAsciiLong(var1.getRemoteDeviceMin(), 8, 16);
      this.writeAsciiLong((long)(var1.getName().length() + 1), 8, 16);
      this.writeAsciiLong(var1.getChksum(), 8, 16);
      this.writeCString(var1.getName());
      this.pad(var1.getHeaderPadCount());
   }

   private void writeOldAsciiEntry(CpioArchiveEntry var1) throws IOException {
      long var2 = var1.getInode();
      long var4 = var1.getDevice();
      if ("TRAILER!!!".equals(var1.getName())) {
         var4 = 0L;
         var2 = 0L;
      } else if (var2 == 0L && var4 == 0L) {
         var2 = this.nextArtificalDeviceAndInode & 262143L;
         var4 = this.nextArtificalDeviceAndInode++ >> 18 & 262143L;
      } else {
         this.nextArtificalDeviceAndInode = Math.max(this.nextArtificalDeviceAndInode, var2 + 262144L * var4) + 1L;
      }

      this.writeAsciiLong(var4, 6, 8);
      this.writeAsciiLong(var2, 6, 8);
      this.writeAsciiLong(var1.getMode(), 6, 8);
      this.writeAsciiLong(var1.getUID(), 6, 8);
      this.writeAsciiLong(var1.getGID(), 6, 8);
      this.writeAsciiLong(var1.getNumberOfLinks(), 6, 8);
      this.writeAsciiLong(var1.getRemoteDevice(), 6, 8);
      this.writeAsciiLong(var1.getTime(), 11, 8);
      this.writeAsciiLong((long)(var1.getName().length() + 1), 6, 8);
      this.writeAsciiLong(var1.getSize(), 11, 8);
      this.writeCString(var1.getName());
   }

   private void writeOldBinaryEntry(CpioArchiveEntry var1, boolean var2) throws IOException {
      long var3 = var1.getInode();
      long var5 = var1.getDevice();
      if ("TRAILER!!!".equals(var1.getName())) {
         var5 = 0L;
         var3 = 0L;
      } else if (var3 == 0L && var5 == 0L) {
         var3 = this.nextArtificalDeviceAndInode & 65535L;
         var5 = this.nextArtificalDeviceAndInode++ >> 16 & 65535L;
      } else {
         this.nextArtificalDeviceAndInode = Math.max(this.nextArtificalDeviceAndInode, var3 + 65536L * var5) + 1L;
      }

      this.writeBinaryLong(var5, 2, var2);
      this.writeBinaryLong(var3, 2, var2);
      this.writeBinaryLong(var1.getMode(), 2, var2);
      this.writeBinaryLong(var1.getUID(), 2, var2);
      this.writeBinaryLong(var1.getGID(), 2, var2);
      this.writeBinaryLong(var1.getNumberOfLinks(), 2, var2);
      this.writeBinaryLong(var1.getRemoteDevice(), 2, var2);
      this.writeBinaryLong(var1.getTime(), 4, var2);
      this.writeBinaryLong((long)(var1.getName().length() + 1), 2, var2);
      this.writeBinaryLong(var1.getSize(), 4, var2);
      this.writeCString(var1.getName());
      this.pad(var1.getHeaderPadCount());
   }

   public void closeArchiveEntry() throws IOException {
      if (this.finished) {
         throw new IOException("Stream has already been finished");
      } else {
         this.ensureOpen();
         if (this.entry == null) {
            throw new IOException("Trying to close non-existent entry");
         } else if (this.entry.getSize() != this.written) {
            throw new IOException("invalid entry size (expected " + this.entry.getSize() + " but got " + this.written + " bytes)");
         } else {
            this.pad(this.entry.getDataPadCount());
            if (this.entry.getFormat() == 2 && this.crc != this.entry.getChksum()) {
               throw new IOException("CRC Error");
            } else {
               this.entry = null;
               this.crc = 0L;
               this.written = 0L;
            }
         }
      }
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.ensureOpen();
      if (var2 >= 0 && var3 >= 0 && var2 <= var1.length - var3) {
         if (var3 != 0) {
            if (this.entry == null) {
               throw new IOException("no current CPIO entry");
            } else if (this.written + (long)var3 > this.entry.getSize()) {
               throw new IOException("attempt to write past end of STORED entry");
            } else {
               this.out.write(var1, var2, var3);
               this.written += (long)var3;
               if (this.entry.getFormat() == 2) {
                  for(int var4 = 0; var4 < var3; ++var4) {
                     this.crc += (long)(var1[var4] & 255);
                  }
               }

               this.count(var3);
            }
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public void finish() throws IOException {
      this.ensureOpen();
      if (this.finished) {
         throw new IOException("This archive has already been finished");
      } else if (this.entry != null) {
         throw new IOException("This archive contains unclosed entries.");
      } else {
         this.entry = new CpioArchiveEntry(this.entryFormat);
         this.entry.setName("TRAILER!!!");
         this.entry.setNumberOfLinks(1L);
         this.writeHeader(this.entry);
         this.closeArchiveEntry();
         int var1 = (int)(this.getBytesWritten() % (long)this.blockSize);
         if (var1 != 0) {
            this.pad(this.blockSize - var1);
         }

         this.finished = true;
      }
   }

   public void close() throws IOException {
      if (!this.finished) {
         this.finish();
      }

      if (!this.closed) {
         this.out.close();
         this.closed = true;
      }

   }

   private void pad(int var1) throws IOException {
      if (var1 > 0) {
         byte[] var2 = new byte[var1];
         this.out.write(var2);
         this.count(var1);
      }

   }

   private void writeBinaryLong(long var1, int var3, boolean var4) throws IOException {
      byte[] var5 = CpioUtil.long2byteArray(var1, var3, var4);
      this.out.write(var5);
      this.count(var5.length);
   }

   private void writeAsciiLong(long var1, int var3, int var4) throws IOException {
      StringBuilder var5 = new StringBuilder();
      if (var4 == 16) {
         var5.append(Long.toHexString(var1));
      } else if (var4 == 8) {
         var5.append(Long.toOctalString(var1));
      } else {
         var5.append(Long.toString(var1));
      }

      String var6;
      if (var5.length() <= var3) {
         long var7 = (long)(var3 - var5.length());

         for(int var9 = 0; (long)var9 < var7; ++var9) {
            var5.insert(0, "0");
         }

         var6 = var5.toString();
      } else {
         var6 = var5.substring(var5.length() - var3);
      }

      byte[] var10 = ArchiveUtils.toAsciiBytes(var6);
      this.out.write(var10);
      this.count(var10.length);
   }

   private void writeCString(String var1) throws IOException {
      ByteBuffer var2 = this.encoding.encode(var1);
      int var3 = var2.limit() - var2.position();
      this.out.write(var2.array(), var2.arrayOffset(), var3);
      this.out.write(0);
      this.count(var3 + 1);
   }

   public ArchiveEntry createArchiveEntry(File var1, String var2) throws IOException {
      if (this.finished) {
         throw new IOException("Stream has already been finished");
      } else {
         return new CpioArchiveEntry(var1, var2);
      }
   }
}
