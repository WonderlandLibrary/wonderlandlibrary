package com.mojang.authlib.properties;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import org.apache.commons.codec.binary.Base64;

public class Property {
   private final String name;
   private final String value;
   private final String signature;

   public Property(String var1, String var2) {
      this(var1, var2, (String)null);
   }

   public Property(String var1, String var2, String var3) {
      this.name = var1;
      this.value = var2;
      this.signature = var3;
   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.value;
   }

   public String getSignature() {
      return this.signature;
   }

   public boolean hasSignature() {
      return this.signature != null;
   }

   public boolean isSignatureValid(PublicKey var1) {
      try {
         Signature var2 = Signature.getInstance("SHA1withRSA");
         var2.initVerify(var1);
         var2.update(this.value.getBytes());
         return var2.verify(Base64.decodeBase64(this.signature));
      } catch (NoSuchAlgorithmException var3) {
         var3.printStackTrace();
      } catch (InvalidKeyException var4) {
         var4.printStackTrace();
      } catch (SignatureException var5) {
         var5.printStackTrace();
      }

      return false;
   }
}
