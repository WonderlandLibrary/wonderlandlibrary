package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

abstract class AbstractStreamingHashFunction implements HashFunction {
   public HashCode hashObject(Object var1, Funnel var2) {
      return this.newHasher().putObject(var1, var2).hash();
   }

   public HashCode hashUnencodedChars(CharSequence var1) {
      return this.newHasher().putUnencodedChars(var1).hash();
   }

   public HashCode hashString(CharSequence var1, Charset var2) {
      return this.newHasher().putString(var1, var2).hash();
   }

   public HashCode hashInt(int var1) {
      return this.newHasher().putInt(var1).hash();
   }

   public HashCode hashLong(long var1) {
      return this.newHasher().putLong(var1).hash();
   }

   public HashCode hashBytes(byte[] var1) {
      return this.newHasher().putBytes(var1).hash();
   }

   public HashCode hashBytes(byte[] var1, int var2, int var3) {
      return this.newHasher().putBytes(var1, var2, var3).hash();
   }

   public Hasher newHasher(int var1) {
      Preconditions.checkArgument(var1 >= 0);
      return this.newHasher();
   }

   protected abstract static class AbstractStreamingHasher extends AbstractHasher {
      private final ByteBuffer buffer;
      private final int bufferSize;
      private final int chunkSize;

      protected AbstractStreamingHasher(int var1) {
         this(var1, var1);
      }

      protected AbstractStreamingHasher(int var1, int var2) {
         Preconditions.checkArgument(var2 % var1 == 0);
         this.buffer = ByteBuffer.allocate(var2 + 7).order(ByteOrder.LITTLE_ENDIAN);
         this.bufferSize = var2;
         this.chunkSize = var1;
      }

      protected abstract void process(ByteBuffer var1);

      protected void processRemaining(ByteBuffer var1) {
         var1.position(var1.limit());
         var1.limit(this.chunkSize + 7);

         while(var1.position() < this.chunkSize) {
            var1.putLong(0L);
         }

         var1.limit(this.chunkSize);
         var1.flip();
         this.process(var1);
      }

      public final Hasher putBytes(byte[] var1) {
         return this.putBytes(var1, 0, var1.length);
      }

      public final Hasher putBytes(byte[] var1, int var2, int var3) {
         return this.putBytes(ByteBuffer.wrap(var1, var2, var3).order(ByteOrder.LITTLE_ENDIAN));
      }

      private Hasher putBytes(ByteBuffer var1) {
         if (var1.remaining() <= this.buffer.remaining()) {
            this.buffer.put(var1);
            this.munchIfFull();
            return this;
         } else {
            int var2 = this.bufferSize - this.buffer.position();

            for(int var3 = 0; var3 < var2; ++var3) {
               this.buffer.put(var1.get());
            }

            this.munch();

            while(var1.remaining() >= this.chunkSize) {
               this.process(var1);
            }

            this.buffer.put(var1);
            return this;
         }
      }

      public final Hasher putUnencodedChars(CharSequence var1) {
         for(int var2 = 0; var2 < var1.length(); ++var2) {
            this.putChar(var1.charAt(var2));
         }

         return this;
      }

      public final Hasher putByte(byte var1) {
         this.buffer.put(var1);
         this.munchIfFull();
         return this;
      }

      public final Hasher putShort(short var1) {
         this.buffer.putShort(var1);
         this.munchIfFull();
         return this;
      }

      public final Hasher putChar(char var1) {
         this.buffer.putChar(var1);
         this.munchIfFull();
         return this;
      }

      public final Hasher putInt(int var1) {
         this.buffer.putInt(var1);
         this.munchIfFull();
         return this;
      }

      public final Hasher putLong(long var1) {
         this.buffer.putLong(var1);
         this.munchIfFull();
         return this;
      }

      public final Hasher putObject(Object var1, Funnel var2) {
         var2.funnel(var1, this);
         return this;
      }

      public final HashCode hash() {
         this.munch();
         this.buffer.flip();
         if (this.buffer.remaining() > 0) {
            this.processRemaining(this.buffer);
         }

         return this.makeHash();
      }

      abstract HashCode makeHash();

      private void munchIfFull() {
         if (this.buffer.remaining() < 8) {
            this.munch();
         }

      }

      private void munch() {
         this.buffer.flip();

         while(this.buffer.remaining() >= this.chunkSize) {
            this.process(this.buffer);
         }

         this.buffer.compact();
      }

      public PrimitiveSink putUnencodedChars(CharSequence var1) {
         return this.putUnencodedChars(var1);
      }

      public PrimitiveSink putChar(char var1) {
         return this.putChar(var1);
      }

      public PrimitiveSink putLong(long var1) {
         return this.putLong(var1);
      }

      public PrimitiveSink putInt(int var1) {
         return this.putInt(var1);
      }

      public PrimitiveSink putShort(short var1) {
         return this.putShort(var1);
      }

      public PrimitiveSink putBytes(byte[] var1, int var2, int var3) {
         return this.putBytes(var1, var2, var3);
      }

      public PrimitiveSink putBytes(byte[] var1) {
         return this.putBytes(var1);
      }

      public PrimitiveSink putByte(byte var1) {
         return this.putByte(var1);
      }
   }
}
