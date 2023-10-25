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
  
  public Property(String value, String name) {
    this(value, name, null);
  }
  
  public Property(String name, String value, String signature) {
    this.name = name;
    this.value = value;
    this.signature = signature;
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
    return (this.signature != null);
  }
  
  public boolean isSignatureValid(PublicKey publicKey) {
    try {
      Signature signature = Signature.getInstance("SHA1withRSA");
      signature.initVerify(publicKey);
      signature.update(this.value.getBytes());
      return signature.verify(Base64.decodeBase64(this.signature));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (SignatureException e) {
      e.printStackTrace();
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\properties\Property.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */