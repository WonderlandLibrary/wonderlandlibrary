package org.apache.commons.compress.archivers.sevenz;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class AES256SHA256Decoder extends CoderBase {
   AES256SHA256Decoder() {
      super();
   }

   InputStream decode(InputStream var1, Coder var2, byte[] var3) throws IOException {
      return new InputStream(this, var2, var3, var1) {
         private boolean isInitialized;
         private CipherInputStream cipherInputStream;
         final Coder val$coder;
         final byte[] val$passwordBytes;
         final InputStream val$in;
         final AES256SHA256Decoder this$0;

         {
            this.this$0 = var1;
            this.val$coder = var2;
            this.val$passwordBytes = var3;
            this.val$in = var4;
            this.isInitialized = false;
            this.cipherInputStream = null;
         }

         private CipherInputStream init() throws IOException {
            if (this.isInitialized) {
               return this.cipherInputStream;
            } else {
               int var1 = 255 & this.val$coder.properties[0];
               int var2 = var1 & 63;
               int var3 = 255 & this.val$coder.properties[1];
               int var4 = (var1 >> 6 & 1) + (var3 & 15);
               int var5 = (var1 >> 7 & 1) + (var3 >> 4);
               if (2 + var5 + var4 > this.val$coder.properties.length) {
                  throw new IOException("Salt size + IV size too long");
               } else {
                  byte[] var6 = new byte[var5];
                  System.arraycopy(this.val$coder.properties, 2, var6, 0, var5);
                  byte[] var7 = new byte[16];
                  System.arraycopy(this.val$coder.properties, 2 + var5, var7, 0, var4);
                  if (this.val$passwordBytes == null) {
                     throw new IOException("Cannot read encrypted files without a password");
                  } else {
                     byte[] var8;
                     IOException var11;
                     if (var2 == 63) {
                        var8 = new byte[32];
                        System.arraycopy(var6, 0, var8, 0, var5);
                        System.arraycopy(this.val$passwordBytes, 0, var8, var5, Math.min(this.val$passwordBytes.length, var8.length - var5));
                     } else {
                        MessageDigest var9;
                        try {
                           var9 = MessageDigest.getInstance("SHA-256");
                        } catch (NoSuchAlgorithmException var15) {
                           var11 = new IOException("SHA-256 is unsupported by your Java implementation");
                           var11.initCause(var15);
                           throw var11;
                        }

                        byte[] var10 = new byte[8];

                        for(long var18 = 0L; var18 < 1L << var2; ++var18) {
                           var9.update(var6);
                           var9.update(this.val$passwordBytes);
                           var9.update(var10);

                           for(int var13 = 0; var13 < var10.length; ++var13) {
                              ++var10[var13];
                              if (var10[var13] != 0) {
                                 break;
                              }
                           }
                        }

                        var8 = var9.digest();
                     }

                     SecretKeySpec var16 = new SecretKeySpec(var8, "AES");

                     try {
                        Cipher var17 = Cipher.getInstance("AES/CBC/NoPadding");
                        var17.init(2, var16, new IvParameterSpec(var7));
                        this.cipherInputStream = new CipherInputStream(this.val$in, var17);
                        this.isInitialized = true;
                        return this.cipherInputStream;
                     } catch (GeneralSecurityException var14) {
                        var11 = new IOException("Decryption error (do you have the JCE Unlimited Strength Jurisdiction Policy Files installed?)");
                        var11.initCause(var14);
                        throw var11;
                     }
                  }
               }
            }
         }

         public int read() throws IOException {
            return this.init().read();
         }

         public int read(byte[] var1, int var2, int var3) throws IOException {
            return this.init().read(var1, var2, var3);
         }

         public void close() {
         }
      };
   }
}
