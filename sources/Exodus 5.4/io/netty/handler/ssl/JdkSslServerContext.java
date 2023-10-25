/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.ssl.JdkSslContext;
import io.netty.handler.ssl.JettyNpnSslEngine;
import io.netty.handler.ssl.PemReader;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSessionContext;

public final class JdkSslServerContext
extends JdkSslContext {
    private final SSLContext ctx;
    private final List<String> nextProtocols;

    public JdkSslServerContext(File file, File file2) throws SSLException {
        this(file, file2, null);
    }

    @Override
    public boolean isClient() {
        return false;
    }

    public JdkSslServerContext(File file, File file2, String string, Iterable<String> iterable, Iterable<String> iterable2, long l, long l2) throws SSLException {
        super(iterable);
        Object object;
        Object object2;
        Object object3;
        if (file == null) {
            throw new NullPointerException("certChainFile");
        }
        if (file2 == null) {
            throw new NullPointerException("keyFile");
        }
        if (string == null) {
            string = "";
        }
        if (iterable2 != null && iterable2.iterator().hasNext()) {
            if (!JettyNpnSslEngine.isAvailable()) {
                throw new SSLException("NPN/ALPN unsupported: " + iterable2);
            }
            object3 = new ArrayList();
            object2 = iterable2.iterator();
            while (object2.hasNext() && (object = (String)object2.next()) != null) {
                object3.add(object);
            }
            this.nextProtocols = Collections.unmodifiableList(object3);
        } else {
            this.nextProtocols = Collections.emptyList();
        }
        object3 = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (object3 == null) {
            object3 = "SunX509";
        }
        try {
            PrivateKey privateKey;
            object2 = KeyStore.getInstance("JKS");
            ((KeyStore)object2).load(null, null);
            object = CertificateFactory.getInstance("X.509");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeyFactory keyFactory2 = KeyFactory.getInstance("DSA");
            ByteBuf byteBuf = PemReader.readPrivateKey(file2);
            byte[] byArray = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(byArray).release();
            char[] cArray = string.toCharArray();
            PKCS8EncodedKeySpec pKCS8EncodedKeySpec = JdkSslServerContext.generateKeySpec(cArray, byArray);
            try {
                privateKey = keyFactory.generatePrivate(pKCS8EncodedKeySpec);
            }
            catch (InvalidKeySpecException invalidKeySpecException) {
                privateKey = keyFactory2.generatePrivate(pKCS8EncodedKeySpec);
            }
            ArrayList<Certificate> arrayList = new ArrayList<Certificate>();
            ByteBuf[] byteBufArray = PemReader.readCertificates(file);
            for (ByteBuf byteBuf2 : byteBufArray) {
                arrayList.add(((CertificateFactory)object).generateCertificate(new ByteBufInputStream(byteBuf2)));
            }
            for (ByteBuf byteBuf2 : byteBufArray) {
                byteBuf2.release();
            }
            ((KeyStore)object2).setKeyEntry("key", privateKey, cArray, arrayList.toArray(new Certificate[arrayList.size()]));
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance((String)object3);
            keyManagerFactory.init((KeyStore)object2, cArray);
            this.ctx = SSLContext.getInstance("TLS");
            this.ctx.init(keyManagerFactory.getKeyManagers(), null, null);
            SSLSessionContext sSLSessionContext = this.ctx.getServerSessionContext();
            if (l > 0L) {
                sSLSessionContext.setSessionCacheSize((int)Math.min(l, Integer.MAX_VALUE));
            }
            if (l2 > 0L) {
                sSLSessionContext.setSessionTimeout((int)Math.min(l2, Integer.MAX_VALUE));
            }
        }
        catch (Exception exception) {
            throw new SSLException("failed to initialize the server-side SSL context", exception);
        }
    }

    @Override
    public SSLContext context() {
        return this.ctx;
    }

    private static PKCS8EncodedKeySpec generateKeySpec(char[] cArray, byte[] byArray) throws InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, InvalidKeySpecException, IOException, NoSuchAlgorithmException {
        if (cArray == null || cArray.length == 0) {
            return new PKCS8EncodedKeySpec(byArray);
        }
        EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(byArray);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(encryptedPrivateKeyInfo.getAlgName());
        PBEKeySpec pBEKeySpec = new PBEKeySpec(cArray);
        SecretKey secretKey = secretKeyFactory.generateSecret(pBEKeySpec);
        Cipher cipher = Cipher.getInstance(encryptedPrivateKeyInfo.getAlgName());
        cipher.init(2, (Key)secretKey, encryptedPrivateKeyInfo.getAlgParameters());
        return encryptedPrivateKeyInfo.getKeySpec(cipher);
    }

    @Override
    public List<String> nextProtocols() {
        return this.nextProtocols;
    }

    public JdkSslServerContext(File file, File file2, String string) throws SSLException {
        this(file, file2, string, null, null, 0L, 0L);
    }
}

