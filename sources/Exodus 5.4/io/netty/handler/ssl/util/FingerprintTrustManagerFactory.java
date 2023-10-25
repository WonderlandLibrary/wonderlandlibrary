/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.ssl.util;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.handler.ssl.util.SimpleTrustManagerFactory;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.EmptyArrays;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public final class FingerprintTrustManagerFactory
extends SimpleTrustManagerFactory {
    private static final int SHA1_HEX_LEN = 40;
    private final TrustManager tm = new X509TrustManager(){

        private byte[] fingerprint(X509Certificate x509Certificate) throws CertificateEncodingException {
            MessageDigest messageDigest = (MessageDigest)tlmd.get();
            messageDigest.reset();
            return messageDigest.digest(x509Certificate.getEncoded());
        }

        private void checkTrusted(String string, X509Certificate[] x509CertificateArray) throws CertificateException {
            X509Certificate x509Certificate = x509CertificateArray[0];
            byte[] byArray = this.fingerprint(x509Certificate);
            boolean bl = false;
            for (byte[] byArray2 : FingerprintTrustManagerFactory.this.fingerprints) {
                if (!Arrays.equals(byArray, byArray2)) continue;
                bl = true;
                break;
            }
            if (!bl) {
                throw new CertificateException(string + " certificate with unknown fingerprint: " + x509Certificate.getSubjectDN());
            }
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509CertificateArray, String string) throws CertificateException {
            this.checkTrusted("client", x509CertificateArray);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509CertificateArray, String string) throws CertificateException {
            this.checkTrusted("server", x509CertificateArray);
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return EmptyArrays.EMPTY_X509_CERTIFICATES;
        }
    };
    private static final Pattern FINGERPRINT_STRIP_PATTERN;
    private static final FastThreadLocal<MessageDigest> tlmd;
    private final byte[][] fingerprints;
    private static final int SHA1_BYTE_LEN = 20;
    private static final Pattern FINGERPRINT_PATTERN;

    static {
        FINGERPRINT_PATTERN = Pattern.compile("^[0-9a-fA-F:]+$");
        FINGERPRINT_STRIP_PATTERN = Pattern.compile(":");
        tlmd = new FastThreadLocal<MessageDigest>(){

            @Override
            protected MessageDigest initialValue() {
                try {
                    return MessageDigest.getInstance("SHA1");
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    throw new Error(noSuchAlgorithmException);
                }
            }
        };
    }

    public FingerprintTrustManagerFactory(Iterable<String> iterable) {
        this(FingerprintTrustManagerFactory.toFingerprintArray(iterable));
    }

    public FingerprintTrustManagerFactory(byte[] ... byArray) {
        if (byArray == null) {
            throw new NullPointerException("fingerprints");
        }
        ArrayList<Object> arrayList = new ArrayList<Object>();
        for (byte[] byArray2 : byArray) {
            if (byArray2 == null) break;
            if (byArray2.length != 20) {
                throw new IllegalArgumentException("malformed fingerprint: " + ByteBufUtil.hexDump(Unpooled.wrappedBuffer(byArray2)) + " (expected: SHA1)");
            }
            arrayList.add(byArray2.clone());
        }
        this.fingerprints = (byte[][])arrayList.toArray((T[])new byte[arrayList.size()][]);
    }

    @Override
    protected void engineInit(KeyStore keyStore) throws Exception {
    }

    private static byte[][] toFingerprintArray(Iterable<String> iterable) {
        if (iterable == null) {
            throw new NullPointerException("fingerprints");
        }
        ArrayList arrayList = new ArrayList();
        for (String string : iterable) {
            if (string == null) break;
            if (!FINGERPRINT_PATTERN.matcher(string).matches()) {
                throw new IllegalArgumentException("malformed fingerprint: " + string);
            }
            if ((string = FINGERPRINT_STRIP_PATTERN.matcher(string).replaceAll("")).length() != 40) {
                throw new IllegalArgumentException("malformed fingerprint: " + string + " (expected: SHA1)");
            }
            byte[] byArray = new byte[20];
            for (int i = 0; i < byArray.length; ++i) {
                int n = i << 1;
                byArray[i] = (byte)Integer.parseInt(string.substring(n, n + 2), 16);
            }
        }
        return (byte[][])arrayList.toArray((T[])new byte[arrayList.size()][]);
    }

    @Override
    protected TrustManager[] engineGetTrustManagers() {
        return new TrustManager[]{this.tm};
    }

    public FingerprintTrustManagerFactory(String ... stringArray) {
        this(FingerprintTrustManagerFactory.toFingerprintArray(Arrays.asList(stringArray)));
    }

    @Override
    protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws Exception {
    }
}

