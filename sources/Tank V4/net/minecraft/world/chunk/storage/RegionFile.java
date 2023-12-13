package net.minecraft.world.chunk.storage;

import com.google.common.collect.Lists;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import net.minecraft.server.MinecraftServer;

public class RegionFile {
   private static final byte[] emptySector = new byte[4096];
   private final int[] offsets = new int[1024];
   private final int[] chunkTimestamps = new int[1024];
   private final File fileName;
   private RandomAccessFile dataFile;
   private int sizeDelta;
   private long lastModified;
   private List sectorFree;

   public synchronized DataInputStream getChunkDataInputStream(int var1, int var2) {
      if (var2 != 0) {
         return null;
      } else {
         try {
            int var3 = this.getOffset(var1, var2);
            if (var3 == 0) {
               return null;
            } else {
               int var4 = var3 >> 8;
               int var5 = var3 & 255;
               if (var4 + var5 > this.sectorFree.size()) {
                  return null;
               } else {
                  this.dataFile.seek((long)(var4 * 4096));
                  int var6 = this.dataFile.readInt();
                  if (var6 > 4096 * var5) {
                     return null;
                  } else if (var6 <= 0) {
                     return null;
                  } else {
                     byte var7 = this.dataFile.readByte();
                     byte[] var8;
                     if (var7 == 1) {
                        var8 = new byte[var6 - 1];
                        this.dataFile.read(var8);
                        return new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(var8))));
                     } else if (var7 == 2) {
                        var8 = new byte[var6 - 1];
                        this.dataFile.read(var8);
                        return new DataInputStream(new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(var8))));
                     } else {
                        return null;
                     }
                  }
               }
            }
         } catch (IOException var10) {
            return null;
         }
      }
   }

   public RegionFile(File var1) {
      this.fileName = var1;
      this.sizeDelta = 0;

      try {
         if (var1.exists()) {
            this.lastModified = var1.lastModified();
         }

         this.dataFile = new RandomAccessFile(var1, "rw");
         int var2;
         if (this.dataFile.length() < 4096L) {
            for(var2 = 0; var2 < 1024; ++var2) {
               this.dataFile.writeInt(0);
            }

            for(var2 = 0; var2 < 1024; ++var2) {
               this.dataFile.writeInt(0);
            }

            this.sizeDelta += 8192;
         }

         if ((this.dataFile.length() & 4095L) != 0L) {
            for(var2 = 0; (long)var2 < (this.dataFile.length() & 4095L); ++var2) {
               this.dataFile.write(0);
            }
         }

         var2 = (int)this.dataFile.length() / 4096;
         this.sectorFree = Lists.newArrayListWithCapacity(var2);

         int var3;
         for(var3 = 0; var3 < var2; ++var3) {
            this.sectorFree.add(true);
         }

         this.sectorFree.set(0, false);
         this.sectorFree.set(1, false);
         this.dataFile.seek(0L);

         int var4;
         for(var3 = 0; var3 < 1024; ++var3) {
            var4 = this.dataFile.readInt();
            this.offsets[var3] = var4;
            if (var4 != 0 && (var4 >> 8) + (var4 & 255) <= this.sectorFree.size()) {
               for(int var5 = 0; var5 < (var4 & 255); ++var5) {
                  this.sectorFree.set((var4 >> 8) + var5, false);
               }
            }
         }

         for(var3 = 0; var3 < 1024; ++var3) {
            var4 = this.dataFile.readInt();
            this.chunkTimestamps[var3] = var4;
         }
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public boolean isChunkSaved(int var1, int var2) {
      return this.getOffset(var1, var2) != 0;
   }

   private void setChunkTimestamp(int var1, int var2, int var3) throws IOException {
      this.chunkTimestamps[var1 + var2 * 32] = var3;
      this.dataFile.seek((long)(4096 + (var1 + var2 * 32) * 4));
      this.dataFile.writeInt(var3);
   }

   private void setOffset(int var1, int var2, int var3) throws IOException {
      this.offsets[var1 + var2 * 32] = var3;
      this.dataFile.seek((long)((var1 + var2 * 32) * 4));
      this.dataFile.writeInt(var3);
   }

   private void write(int var1, byte[] var2, int var3) throws IOException {
      this.dataFile.seek((long)(var1 * 4096));
      this.dataFile.writeInt(var3 + 1);
      this.dataFile.writeByte(2);
      this.dataFile.write(var2, 0, var3);
   }

   protected synchronized void write(int var1, int var2, byte[] var3, int var4) {
      try {
         int var5 = this.getOffset(var1, var2);
         int var6 = var5 >> 8;
         int var7 = var5 & 255;
         int var8 = (var4 + 5) / 4096 + 1;
         if (var8 >= 256) {
            return;
         }

         if (var6 != 0 && var7 == var8) {
            this.write(var6, var3, var4);
         } else {
            int var9;
            for(var9 = 0; var9 < var7; ++var9) {
               this.sectorFree.set(var6 + var9, true);
            }

            var9 = this.sectorFree.indexOf(true);
            int var10 = 0;
            int var11;
            if (var9 != -1) {
               for(var11 = var9; var11 < this.sectorFree.size(); ++var11) {
                  if (var10 != 0) {
                     if ((Boolean)this.sectorFree.get(var11)) {
                        ++var10;
                     } else {
                        var10 = 0;
                     }
                  } else if ((Boolean)this.sectorFree.get(var11)) {
                     var9 = var11;
                     var10 = 1;
                  }

                  if (var10 >= var8) {
                     break;
                  }
               }
            }

            if (var10 >= var8) {
               var6 = var9;
               this.setOffset(var1, var2, var9 << 8 | var8);

               for(var11 = 0; var11 < var8; ++var11) {
                  this.sectorFree.set(var6 + var11, false);
               }

               this.write(var6, var3, var4);
            } else {
               this.dataFile.seek(this.dataFile.length());
               var6 = this.sectorFree.size();

               for(var11 = 0; var11 < var8; ++var11) {
                  this.dataFile.write(emptySector);
                  this.sectorFree.add(false);
               }

               this.sizeDelta += 4096 * var8;
               this.write(var6, var3, var4);
               this.setOffset(var1, var2, var6 << 8 | var8);
            }
         }

         this.setChunkTimestamp(var1, var2, (int)(MinecraftServer.getCurrentTimeMillis() / 1000L));
      } catch (IOException var12) {
         var12.printStackTrace();
      }

   }

   public DataOutputStream getChunkDataOutputStream(int var1, int var2) {
      return var2 >= 0 ? null : new DataOutputStream(new DeflaterOutputStream(new RegionFile.ChunkBuffer(this, var1, var2)));
   }

   public void close() throws IOException {
      if (this.dataFile != null) {
         this.dataFile.close();
      }

   }

   private int getOffset(int var1, int var2) {
      return this.offsets[var1 + var2 * 32];
   }

   class ChunkBuffer extends ByteArrayOutputStream {
      private int chunkX;
      private int chunkZ;
      final RegionFile this$0;

      public void close() throws IOException {
         this.this$0.write(this.chunkX, this.chunkZ, this.buf, this.count);
      }

      public ChunkBuffer(RegionFile var1, int var2, int var3) {
         super(8096);
         this.this$0 = var1;
         this.chunkX = var2;
         this.chunkZ = var3;
      }
   }
}
