// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.util;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.Key;
import javax.crypto.Cipher;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;

public final class CipherUtil
{
    private static final KeyFactory RSA_FACTORY;
    
    public static byte[] encryptNonce(final byte[] publicKeyBytes, final byte[] nonceBytes) throws Exception {
        final EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        final PublicKey key = CipherUtil.RSA_FACTORY.generatePublic(keySpec);
        final Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        cipher.init(1, key);
        return cipher.doFinal(nonceBytes);
    }
    
    static {
        try {
            RSA_FACTORY = KeyFactory.getInstance("RSA");
        }
        catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
