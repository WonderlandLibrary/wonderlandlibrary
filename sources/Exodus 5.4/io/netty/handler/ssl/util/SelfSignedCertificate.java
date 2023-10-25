/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.ssl.util;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.handler.ssl.util.BouncyCastleSelfSignedCertGenerator;
import io.netty.handler.ssl.util.OpenJdkSelfSignedCertGenerator;
import io.netty.handler.ssl.util.ThreadLocalInsecureRandom;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public final class SelfSignedCertificate {
    static final Date NOT_BEFORE;
    private final File privateKey;
    private static final InternalLogger logger;
    private final File certificate;
    static final Date NOT_AFTER;

    private static void safeClose(File file, OutputStream outputStream) {
        try {
            outputStream.close();
        }
        catch (IOException iOException) {
            logger.warn("Failed to close a file: " + file, iOException);
        }
    }

    public SelfSignedCertificate() throws CertificateException {
        this("example.com");
    }

    public File privateKey() {
        return this.privateKey;
    }

    public SelfSignedCertificate(String string) throws CertificateException {
        this(string, ThreadLocalInsecureRandom.current(), 1024);
    }

    static {
        logger = InternalLoggerFactory.getInstance(SelfSignedCertificate.class);
        NOT_BEFORE = new Date(System.currentTimeMillis() - 31536000000L);
        NOT_AFTER = new Date(253402300799000L);
    }

    public SelfSignedCertificate(String string, SecureRandom secureRandom, int n) throws CertificateException {
        KeyPair keyPair;
        String[] stringArray;
        try {
            stringArray = KeyPairGenerator.getInstance("RSA");
            stringArray.initialize(n, secureRandom);
            keyPair = stringArray.generateKeyPair();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new Error(noSuchAlgorithmException);
        }
        try {
            stringArray = OpenJdkSelfSignedCertGenerator.generate(string, keyPair, secureRandom);
        }
        catch (Throwable throwable) {
            logger.debug("Failed to generate a self-signed X.509 certificate using sun.security.x509:", throwable);
            try {
                stringArray = BouncyCastleSelfSignedCertGenerator.generate(string, keyPair, secureRandom);
            }
            catch (Throwable throwable2) {
                logger.debug("Failed to generate a self-signed X.509 certificate using Bouncy Castle:", throwable2);
                throw new CertificateException("No provider succeeded to generate a self-signed certificate. See debug log for the root cause.");
            }
        }
        this.certificate = new File(stringArray[0]);
        this.privateKey = new File(stringArray[1]);
    }

    static String[] newSelfSignedCertificate(String string, PrivateKey privateKey, X509Certificate x509Certificate) throws IOException, CertificateEncodingException {
        String string2 = "-----BEGIN PRIVATE KEY-----\n" + Base64.encode(Unpooled.wrappedBuffer(privateKey.getEncoded()), true).toString(CharsetUtil.US_ASCII) + "\n-----END PRIVATE KEY-----\n";
        File file = File.createTempFile("keyutil_" + string + '_', ".key");
        file.deleteOnExit();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ((OutputStream)fileOutputStream).write(string2.getBytes(CharsetUtil.US_ASCII));
        ((OutputStream)fileOutputStream).close();
        fileOutputStream = null;
        if (fileOutputStream != null) {
            SelfSignedCertificate.safeClose(file, fileOutputStream);
            SelfSignedCertificate.safeDelete(file);
        }
        String string3 = "-----BEGIN CERTIFICATE-----\n" + Base64.encode(Unpooled.wrappedBuffer(x509Certificate.getEncoded()), true).toString(CharsetUtil.US_ASCII) + "\n-----END CERTIFICATE-----\n";
        File file2 = File.createTempFile("keyutil_" + string + '_', ".crt");
        file2.deleteOnExit();
        FileOutputStream fileOutputStream2 = new FileOutputStream(file2);
        ((OutputStream)fileOutputStream2).write(string3.getBytes(CharsetUtil.US_ASCII));
        ((OutputStream)fileOutputStream2).close();
        fileOutputStream2 = null;
        if (fileOutputStream2 != null) {
            SelfSignedCertificate.safeClose(file2, fileOutputStream2);
            SelfSignedCertificate.safeDelete(file2);
            SelfSignedCertificate.safeDelete(file);
        }
        return new String[]{file2.getPath(), file.getPath()};
    }

    public File certificate() {
        return this.certificate;
    }

    private static void safeDelete(File file) {
        if (!file.delete()) {
            logger.warn("Failed to delete a file: " + file);
        }
    }

    public void delete() {
        SelfSignedCertificate.safeDelete(this.certificate);
        SelfSignedCertificate.safeDelete(this.privateKey);
    }
}

