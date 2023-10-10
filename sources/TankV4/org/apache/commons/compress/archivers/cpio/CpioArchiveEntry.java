package org.apache.commons.compress.archivers.cpio;

import java.io.File;
import java.util.Date;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class CpioArchiveEntry implements CpioConstants, ArchiveEntry {
   private final short fileFormat;
   private final int headerSize;
   private final int alignmentBoundary;
   private long chksum;
   private long filesize;
   private long gid;
   private long inode;
   private long maj;
   private long min;
   private long mode;
   private long mtime;
   private String name;
   private long nlink;
   private long rmaj;
   private long rmin;
   private long uid;

   public CpioArchiveEntry(short var1) {
      this.chksum = 0L;
      this.filesize = 0L;
      this.gid = 0L;
      this.inode = 0L;
      this.maj = 0L;
      this.min = 0L;
      this.mode = 0L;
      this.mtime = 0L;
      this.nlink = 0L;
      this.rmaj = 0L;
      this.rmin = 0L;
      this.uid = 0L;
      switch(var1) {
      case 1:
         this.headerSize = 110;
         this.alignmentBoundary = 4;
         break;
      case 2:
         this.headerSize = 110;
         this.alignmentBoundary = 4;
         break;
      case 3:
      case 5:
      case 6:
      case 7:
      default:
         throw new IllegalArgumentException("Unknown header type");
      case 4:
         this.headerSize = 76;
         this.alignmentBoundary = 0;
         break;
      case 8:
         this.headerSize = 26;
         this.alignmentBoundary = 2;
      }

      this.fileFormat = var1;
   }

   public CpioArchiveEntry(String var1) {
      this((short)1, var1);
   }

   public CpioArchiveEntry(short var1, String var2) {
      this(var1);
      this.name = var2;
   }

   public CpioArchiveEntry(String var1, long var2) {
      this(var1);
      this.setSize(var2);
   }

   public CpioArchiveEntry(short var1, String var2, long var3) {
      this(var1, var2);
      this.setSize(var3);
   }

   public CpioArchiveEntry(File var1, String var2) {
      this((short)1, var1, var2);
   }

   public CpioArchiveEntry(short var1, File var2, String var3) {
      this(var1, var3, var2.isFile() ? var2.length() : 0L);
      if (var2.isDirectory()) {
         this.setMode(16384L);
      } else {
         if (!var2.isFile()) {
            throw new IllegalArgumentException("Cannot determine type of file " + var2.getName());
         }

         this.setMode(32768L);
      }

      this.setTime(var2.lastModified() / 1000L);
   }

   private void checkNewFormat() {
      if ((this.fileFormat & 3) == 0) {
         throw new UnsupportedOperationException();
      }
   }

   private void checkOldFormat() {
      if ((this.fileFormat & 12) == 0) {
         throw new UnsupportedOperationException();
      }
   }

   public long getChksum() {
      this.checkNewFormat();
      return this.chksum;
   }

   public long getDevice() {
      this.checkOldFormat();
      return this.min;
   }

   public long getDeviceMaj() {
      this.checkNewFormat();
      return this.maj;
   }

   public long getDeviceMin() {
      this.checkNewFormat();
      return this.min;
   }

   public long getSize() {
      return this.filesize;
   }

   public short getFormat() {
      return this.fileFormat;
   }

   public long getGID() {
      return this.gid;
   }

   public int getHeaderSize() {
      return this.headerSize;
   }

   public int getAlignmentBoundary() {
      return this.alignmentBoundary;
   }

   public int getHeaderPadCount() {
      if (this.alignmentBoundary == 0) {
         return 0;
      } else {
         int var1 = this.headerSize + 1;
         if (this.name != null) {
            var1 += this.name.length();
         }

         int var2 = var1 % this.alignmentBoundary;
         return var2 > 0 ? this.alignmentBoundary - var2 : 0;
      }
   }

   public int getDataPadCount() {
      if (this.alignmentBoundary == 0) {
         return 0;
      } else {
         long var1 = this.filesize;
         int var3 = (int)(var1 % (long)this.alignmentBoundary);
         return var3 > 0 ? this.alignmentBoundary - var3 : 0;
      }
   }

   public long getInode() {
      return this.inode;
   }

   public long getMode() {
      return this.mode == 0L && !"TRAILER!!!".equals(this.name) ? 32768L : this.mode;
   }

   public String getName() {
      return this.name;
   }

   public long getNumberOfLinks() {
      return this.nlink == 0L ? (this == false ? 2L : 1L) : this.nlink;
   }

   public long getRemoteDevice() {
      this.checkOldFormat();
      return this.rmin;
   }

   public long getRemoteDeviceMaj() {
      this.checkNewFormat();
      return this.rmaj;
   }

   public long getRemoteDeviceMin() {
      this.checkNewFormat();
      return this.rmin;
   }

   public long getTime() {
      return this.mtime;
   }

   public Date getLastModifiedDate() {
      return new Date(1000L * this.getTime());
   }

   public long getUID() {
      return this.uid;
   }

   public boolean isBlockDevice() {
      return CpioUtil.fileType(this.mode) == 24576L;
   }

   public boolean isCharacterDevice() {
      return CpioUtil.fileType(this.mode) == 8192L;
   }

   public boolean isNetwork() {
      return CpioUtil.fileType(this.mode) == 36864L;
   }

   public boolean isPipe() {
      return CpioUtil.fileType(this.mode) == 4096L;
   }

   public boolean isRegularFile() {
      return CpioUtil.fileType(this.mode) == 32768L;
   }

   public boolean isSocket() {
      return CpioUtil.fileType(this.mode) == 49152L;
   }

   public boolean isSymbolicLink() {
      return CpioUtil.fileType(this.mode) == 40960L;
   }

   public void setChksum(long var1) {
      this.checkNewFormat();
      this.chksum = var1;
   }

   public void setDevice(long var1) {
      this.checkOldFormat();
      this.min = var1;
   }

   public void setDeviceMaj(long var1) {
      this.checkNewFormat();
      this.maj = var1;
   }

   public void setDeviceMin(long var1) {
      this.checkNewFormat();
      this.min = var1;
   }

   public void setSize(long var1) {
      if (var1 >= 0L && var1 <= 4294967295L) {
         this.filesize = var1;
      } else {
         throw new IllegalArgumentException("invalid entry size <" + var1 + ">");
      }
   }

   public void setGID(long var1) {
      this.gid = var1;
   }

   public void setInode(long var1) {
      this.inode = var1;
   }

   public void setMode(long var1) {
      long var3 = var1 & 61440L;
      switch((int)var3) {
      case 4096:
      case 8192:
      case 16384:
      case 24576:
      case 32768:
      case 36864:
      case 40960:
      case 49152:
         this.mode = var1;
         return;
      default:
         throw new IllegalArgumentException("Unknown mode. Full: " + Long.toHexString(var1) + " Masked: " + Long.toHexString(var3));
      }
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void setNumberOfLinks(long var1) {
      this.nlink = var1;
   }

   public void setRemoteDevice(long var1) {
      this.checkOldFormat();
      this.rmin = var1;
   }

   public void setRemoteDeviceMaj(long var1) {
      this.checkNewFormat();
      this.rmaj = var1;
   }

   public void setRemoteDeviceMin(long var1) {
      this.checkNewFormat();
      this.rmin = var1;
   }

   public void setTime(long var1) {
      this.mtime = var1;
   }

   public void setUID(long var1) {
      this.uid = var1;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = 31 * var2 + (this.name == null ? 0 : this.name.hashCode());
      return var3;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         CpioArchiveEntry var2 = (CpioArchiveEntry)var1;
         if (this.name == null) {
            if (var2.name != null) {
               return false;
            }
         } else if (!this.name.equals(var2.name)) {
            return false;
         }

         return true;
      } else {
         return false;
      }
   }
}
