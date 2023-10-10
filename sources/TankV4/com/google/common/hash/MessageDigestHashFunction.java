package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

final class MessageDigestHashFunction extends AbstractStreamingHashFunction implements Serializable {
   private final MessageDigest prototype;
   private final int bytes;
   private final boolean supportsClone;
   private final String toString;

   MessageDigestHashFunction(String var1, String var2) {
      this.prototype = getMessageDigest(var1);
      this.bytes = this.prototype.getDigestLength();
      this.toString = (String)Preconditions.checkNotNull(var2);
      this.supportsClone = this.supportsClone();
   }

   MessageDigestHashFunction(String var1, int var2, String var3) {
      this.toString = (String)Preconditions.checkNotNull(var3);
      this.prototype = getMessageDigest(var1);
      int var4 = this.prototype.getDigestLength();
      Preconditions.checkArgument(var2 >= 4 && var2 <= var4, "bytes (%s) must be >= 4 and < %s", var2, var4);
      this.bytes = var2;
      this.supportsClone = this.supportsClone();
   }

   private boolean supportsClone() {
      try {
         this.prototype.clone();
         return true;
      } catch (CloneNotSupportedException var2) {
         return false;
      }
   }

   public int bits() {
      return this.bytes * 8;
   }

   public String toString() {
      return this.toString;
   }

   private static MessageDigest getMessageDigest(String var0) {
      try {
         return MessageDigest.getInstance(var0);
      } catch (NoSuchAlgorithmException var2) {
         throw new AssertionError(var2);
      }
   }

   public Hasher newHasher() {
      if (this.supportsClone) {
         try {
            return new MessageDigestHashFunction.MessageDigestHasher((MessageDigest)this.prototype.clone(), this.bytes);
         } catch (CloneNotSupportedException var2) {
         }
      }

      return new MessageDigestHashFunction.MessageDigestHasher(getMessageDigest(this.prototype.getAlgorithm()), this.bytes);
   }

   Object writeReplace() {
      return new MessageDigestHashFunction.SerializedForm(this.prototype.getAlgorithm(), this.bytes, this.toString);
   }

   private static final class MessageDigestHasher extends AbstractByteHasher {
      private final MessageDigest digest;
      private final int bytes;
      private boolean done;

      private MessageDigestHasher(MessageDigest var1, int var2) {
         this.digest = var1;
         this.bytes = var2;
      }

      protected void update(byte var1) {
         this.checkNotDone();
         this.digest.update(var1);
      }

      protected void update(byte[] var1) {
         this.checkNotDone();
         this.digest.update(var1);
      }

      protected void update(byte[] var1, int var2, int var3) {
         this.checkNotDone();
         this.digest.update(var1, var2, var3);
      }

      private void checkNotDone() {
         Preconditions.checkState(!this.done, "Cannot re-use a Hasher after calling hash() on it");
      }

      public HashCode hash() {
         this.checkNotDone();
         this.done = true;
         return this.bytes == this.digest.getDigestLength() ? HashCode.fromBytesNoCopy(this.digest.digest()) : HashCode.fromBytesNoCopy(Arrays.copyOf(this.digest.digest(), this.bytes));
      }

      MessageDigestHasher(MessageDigest var1, int var2, Object var3) {
         this(var1, var2);
      }
   }

   private static final class SerializedForm implements Serializable {
      private final String algorithmName;
      private final int bytes;
      private final String toString;
      private static final long serialVersionUID = 0L;

      private SerializedForm(String var1, int var2, String var3) {
         this.algorithmName = var1;
         this.bytes = var2;
         this.toString = var3;
      }

      private Object readResolve() {
         return new MessageDigestHashFunction(this.algorithmName, this.bytes, this.toString);
      }

      SerializedForm(String var1, int var2, String var3, Object var4) {
         this(var1, var2, var3);
      }
   }
}
