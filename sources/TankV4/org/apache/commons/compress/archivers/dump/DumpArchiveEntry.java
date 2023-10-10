package org.apache.commons.compress.archivers.dump;

import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class DumpArchiveEntry implements ArchiveEntry {
   private String name;
   private DumpArchiveEntry.TYPE type;
   private int mode;
   private Set permissions;
   private long size;
   private long atime;
   private long mtime;
   private int uid;
   private int gid;
   private final DumpArchiveSummary summary;
   private final DumpArchiveEntry.TapeSegmentHeader header;
   private String simpleName;
   private String originalName;
   private int volume;
   private long offset;
   private int ino;
   private int nlink;
   private long ctime;
   private int generation;
   private boolean isDeleted;

   public DumpArchiveEntry() {
      this.type = DumpArchiveEntry.TYPE.UNKNOWN;
      this.permissions = Collections.emptySet();
      this.summary = null;
      this.header = new DumpArchiveEntry.TapeSegmentHeader();
   }

   public DumpArchiveEntry(String var1, String var2) {
      this.type = DumpArchiveEntry.TYPE.UNKNOWN;
      this.permissions = Collections.emptySet();
      this.summary = null;
      this.header = new DumpArchiveEntry.TapeSegmentHeader();
      this.setName(var1);
      this.simpleName = var2;
   }

   protected DumpArchiveEntry(String var1, String var2, int var3, DumpArchiveEntry.TYPE var4) {
      this.type = DumpArchiveEntry.TYPE.UNKNOWN;
      this.permissions = Collections.emptySet();
      this.summary = null;
      this.header = new DumpArchiveEntry.TapeSegmentHeader();
      this.setType(var4);
      this.setName(var1);
      this.simpleName = var2;
      this.ino = var3;
      this.offset = 0L;
   }

   public String getSimpleName() {
      return this.simpleName;
   }

   protected void setSimpleName(String var1) {
      this.simpleName = var1;
   }

   public int getIno() {
      return this.header.getIno();
   }

   public int getNlink() {
      return this.nlink;
   }

   public void setNlink(int var1) {
      this.nlink = var1;
   }

   public Date getCreationTime() {
      return new Date(this.ctime);
   }

   public void setCreationTime(Date var1) {
      this.ctime = var1.getTime();
   }

   public int getGeneration() {
      return this.generation;
   }

   public void setGeneration(int var1) {
      this.generation = var1;
   }

   public boolean isDeleted() {
      return this.isDeleted;
   }

   public void setDeleted(boolean var1) {
      this.isDeleted = var1;
   }

   public long getOffset() {
      return this.offset;
   }

   public void setOffset(long var1) {
      this.offset = var1;
   }

   public int getVolume() {
      return this.volume;
   }

   public void setVolume(int var1) {
      this.volume = var1;
   }

   public DumpArchiveConstants.SEGMENT_TYPE getHeaderType() {
      return this.header.getType();
   }

   public int getHeaderCount() {
      return this.header.getCount();
   }

   public int getHeaderHoles() {
      return this.header.getHoles();
   }

   public boolean isSparseRecord(int var1) {
      return (this.header.getCdata(var1) & 1) == 0;
   }

   public int hashCode() {
      return this.ino;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 != null && var1.getClass().equals(this.getClass())) {
         DumpArchiveEntry var2 = (DumpArchiveEntry)var1;
         if (this.header != null && var2.header != null) {
            if (this.ino != var2.ino) {
               return false;
            } else {
               return (this.summary != null || var2.summary == null) && (this.summary == null || this.summary.equals(var2.summary));
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public String toString() {
      return this.getName();
   }

   static DumpArchiveEntry parse(byte[] var0) {
      DumpArchiveEntry var1 = new DumpArchiveEntry();
      DumpArchiveEntry.TapeSegmentHeader var2 = var1.header;
      DumpArchiveEntry.TapeSegmentHeader.access$002(var2, DumpArchiveConstants.SEGMENT_TYPE.find(DumpArchiveUtil.convert32(var0, 0)));
      DumpArchiveEntry.TapeSegmentHeader.access$102(var2, DumpArchiveUtil.convert32(var0, 12));
      var1.ino = DumpArchiveEntry.TapeSegmentHeader.access$202(var2, DumpArchiveUtil.convert32(var0, 20));
      int var3 = DumpArchiveUtil.convert16(var0, 32);
      var1.setType(DumpArchiveEntry.TYPE.find(var3 >> 12 & 15));
      var1.setMode(var3);
      var1.nlink = DumpArchiveUtil.convert16(var0, 34);
      var1.setSize(DumpArchiveUtil.convert64(var0, 40));
      long var4 = 1000L * (long)DumpArchiveUtil.convert32(var0, 48) + (long)(DumpArchiveUtil.convert32(var0, 52) / 1000);
      var1.setAccessTime(new Date(var4));
      var4 = 1000L * (long)DumpArchiveUtil.convert32(var0, 56) + (long)(DumpArchiveUtil.convert32(var0, 60) / 1000);
      var1.setLastModifiedDate(new Date(var4));
      var4 = 1000L * (long)DumpArchiveUtil.convert32(var0, 64) + (long)(DumpArchiveUtil.convert32(var0, 68) / 1000);
      var1.ctime = var4;
      var1.generation = DumpArchiveUtil.convert32(var0, 140);
      var1.setUserId(DumpArchiveUtil.convert32(var0, 144));
      var1.setGroupId(DumpArchiveUtil.convert32(var0, 148));
      DumpArchiveEntry.TapeSegmentHeader.access$302(var2, DumpArchiveUtil.convert32(var0, 160));
      DumpArchiveEntry.TapeSegmentHeader.access$402(var2, 0);

      for(int var6 = 0; var6 < 512 && var6 < DumpArchiveEntry.TapeSegmentHeader.access$300(var2); ++var6) {
         if (var0[164 + var6] == 0) {
            DumpArchiveEntry.TapeSegmentHeader.access$408(var2);
         }
      }

      System.arraycopy(var0, 164, DumpArchiveEntry.TapeSegmentHeader.access$500(var2), 0, 512);
      var1.volume = var2.getVolume();
      return var1;
   }

   void update(byte[] var1) {
      DumpArchiveEntry.TapeSegmentHeader.access$102(this.header, DumpArchiveUtil.convert32(var1, 16));
      DumpArchiveEntry.TapeSegmentHeader.access$302(this.header, DumpArchiveUtil.convert32(var1, 160));
      DumpArchiveEntry.TapeSegmentHeader.access$402(this.header, 0);

      for(int var2 = 0; var2 < 512 && var2 < DumpArchiveEntry.TapeSegmentHeader.access$300(this.header); ++var2) {
         if (var1[164 + var2] == 0) {
            DumpArchiveEntry.TapeSegmentHeader.access$408(this.header);
         }
      }

      System.arraycopy(var1, 164, DumpArchiveEntry.TapeSegmentHeader.access$500(this.header), 0, 512);
   }

   public String getName() {
      return this.name;
   }

   String getOriginalName() {
      return this.originalName;
   }

   public final void setName(String param1) {
      // $FF: Couldn't be decompiled
   }

   public Date getLastModifiedDate() {
      return new Date(this.mtime);
   }

   public boolean isFile() {
      return this.type == DumpArchiveEntry.TYPE.FILE;
   }

   public boolean isSocket() {
      return this.type == DumpArchiveEntry.TYPE.SOCKET;
   }

   public boolean isChrDev() {
      return this.type == DumpArchiveEntry.TYPE.CHRDEV;
   }

   public boolean isBlkDev() {
      return this.type == DumpArchiveEntry.TYPE.BLKDEV;
   }

   public boolean isFifo() {
      return this.type == DumpArchiveEntry.TYPE.FIFO;
   }

   public DumpArchiveEntry.TYPE getType() {
      return this.type;
   }

   public void setType(DumpArchiveEntry.TYPE var1) {
      this.type = var1;
   }

   public int getMode() {
      return this.mode;
   }

   public void setMode(int var1) {
      this.mode = var1 & 4095;
      this.permissions = DumpArchiveEntry.PERMISSION.find(var1);
   }

   public Set getPermissions() {
      return this.permissions;
   }

   public long getSize() {
      // $FF: Couldn't be decompiled
   }

   long getEntrySize() {
      return this.size;
   }

   public void setSize(long var1) {
      this.size = var1;
   }

   public void setLastModifiedDate(Date var1) {
      this.mtime = var1.getTime();
   }

   public Date getAccessTime() {
      return new Date(this.atime);
   }

   public void setAccessTime(Date var1) {
      this.atime = var1.getTime();
   }

   public int getUserId() {
      return this.uid;
   }

   public void setUserId(int var1) {
      this.uid = var1;
   }

   public int getGroupId() {
      return this.gid;
   }

   public void setGroupId(int var1) {
      this.gid = var1;
   }

   public static enum PERMISSION {
      SETUID(2048),
      SETGUI(1024),
      STICKY(512),
      USER_READ(256),
      USER_WRITE(128),
      USER_EXEC(64),
      GROUP_READ(32),
      GROUP_WRITE(16),
      GROUP_EXEC(8),
      WORLD_READ(4),
      WORLD_WRITE(2),
      WORLD_EXEC(1);

      private int code;
      private static final DumpArchiveEntry.PERMISSION[] $VALUES = new DumpArchiveEntry.PERMISSION[]{SETUID, SETGUI, STICKY, USER_READ, USER_WRITE, USER_EXEC, GROUP_READ, GROUP_WRITE, GROUP_EXEC, WORLD_READ, WORLD_WRITE, WORLD_EXEC};

      private PERMISSION(int var3) {
         this.code = var3;
      }

      public static Set find(int var0) {
         HashSet var1 = new HashSet();
         DumpArchiveEntry.PERMISSION[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            DumpArchiveEntry.PERMISSION var5 = var2[var4];
            if ((var0 & var5.code) == var5.code) {
               var1.add(var5);
            }
         }

         if (var1.isEmpty()) {
            return Collections.emptySet();
         } else {
            return EnumSet.copyOf(var1);
         }
      }
   }

   public static enum TYPE {
      WHITEOUT(14),
      SOCKET(12),
      LINK(10),
      FILE(8),
      BLKDEV(6),
      DIRECTORY(4),
      CHRDEV(2),
      FIFO(1),
      UNKNOWN(15);

      private int code;
      private static final DumpArchiveEntry.TYPE[] $VALUES = new DumpArchiveEntry.TYPE[]{WHITEOUT, SOCKET, LINK, FILE, BLKDEV, DIRECTORY, CHRDEV, FIFO, UNKNOWN};

      private TYPE(int var3) {
         this.code = var3;
      }

      public static DumpArchiveEntry.TYPE find(int var0) {
         DumpArchiveEntry.TYPE var1 = UNKNOWN;
         DumpArchiveEntry.TYPE[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            DumpArchiveEntry.TYPE var5 = var2[var4];
            if (var0 == var5.code) {
               var1 = var5;
            }
         }

         return var1;
      }
   }

   static class TapeSegmentHeader {
      private DumpArchiveConstants.SEGMENT_TYPE type;
      private int volume;
      private int ino;
      private int count;
      private int holes;
      private final byte[] cdata = new byte[512];

      public DumpArchiveConstants.SEGMENT_TYPE getType() {
         return this.type;
      }

      public int getVolume() {
         return this.volume;
      }

      public int getIno() {
         return this.ino;
      }

      void setIno(int var1) {
         this.ino = var1;
      }

      public int getCount() {
         return this.count;
      }

      public int getHoles() {
         return this.holes;
      }

      public int getCdata(int var1) {
         return this.cdata[var1];
      }

      static DumpArchiveConstants.SEGMENT_TYPE access$002(DumpArchiveEntry.TapeSegmentHeader var0, DumpArchiveConstants.SEGMENT_TYPE var1) {
         return var0.type = var1;
      }

      static int access$102(DumpArchiveEntry.TapeSegmentHeader var0, int var1) {
         return var0.volume = var1;
      }

      static int access$202(DumpArchiveEntry.TapeSegmentHeader var0, int var1) {
         return var0.ino = var1;
      }

      static int access$302(DumpArchiveEntry.TapeSegmentHeader var0, int var1) {
         return var0.count = var1;
      }

      static int access$402(DumpArchiveEntry.TapeSegmentHeader var0, int var1) {
         return var0.holes = var1;
      }

      static int access$300(DumpArchiveEntry.TapeSegmentHeader var0) {
         return var0.count;
      }

      static int access$408(DumpArchiveEntry.TapeSegmentHeader var0) {
         return var0.holes++;
      }

      static byte[] access$500(DumpArchiveEntry.TapeSegmentHeader var0) {
         return var0.cdata;
      }
   }
}
