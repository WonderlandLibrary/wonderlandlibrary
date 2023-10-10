package org.apache.commons.compress.archivers.dump;

public final class DumpArchiveConstants {
   public static final int TP_SIZE = 1024;
   public static final int NTREC = 10;
   public static final int HIGH_DENSITY_NTREC = 32;
   public static final int OFS_MAGIC = 60011;
   public static final int NFS_MAGIC = 60012;
   public static final int FS_UFS2_MAGIC = 424935705;
   public static final int CHECKSUM = 84446;
   public static final int LBLSIZE = 16;
   public static final int NAMELEN = 64;

   private DumpArchiveConstants() {
   }

   public static enum COMPRESSION_TYPE {
      ZLIB(0),
      BZLIB(1),
      LZO(2);

      int code;
      private static final DumpArchiveConstants.COMPRESSION_TYPE[] $VALUES = new DumpArchiveConstants.COMPRESSION_TYPE[]{ZLIB, BZLIB, LZO};

      private COMPRESSION_TYPE(int var3) {
         this.code = var3;
      }

      public static DumpArchiveConstants.COMPRESSION_TYPE find(int var0) {
         DumpArchiveConstants.COMPRESSION_TYPE[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            DumpArchiveConstants.COMPRESSION_TYPE var4 = var1[var3];
            if (var4.code == var0) {
               return var4;
            }
         }

         return null;
      }
   }

   public static enum SEGMENT_TYPE {
      TAPE(1),
      INODE(2),
      BITS(3),
      ADDR(4),
      END(5),
      CLRI(6);

      int code;
      private static final DumpArchiveConstants.SEGMENT_TYPE[] $VALUES = new DumpArchiveConstants.SEGMENT_TYPE[]{TAPE, INODE, BITS, ADDR, END, CLRI};

      private SEGMENT_TYPE(int var3) {
         this.code = var3;
      }

      public static DumpArchiveConstants.SEGMENT_TYPE find(int var0) {
         DumpArchiveConstants.SEGMENT_TYPE[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            DumpArchiveConstants.SEGMENT_TYPE var4 = var1[var3];
            if (var4.code == var0) {
               return var4;
            }
         }

         return null;
      }
   }
}
