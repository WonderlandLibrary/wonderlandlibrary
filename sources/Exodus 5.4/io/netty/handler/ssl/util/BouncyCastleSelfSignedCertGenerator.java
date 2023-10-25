/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bouncycastle.asn1.x500.X500Name
 *  org.bouncycastle.cert.X509CertificateHolder
 *  org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
 *  org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
 *  org.bouncycastle.jce.provider.BouncyCastleProvider
 *  org.bouncycastle.operator.ContentSigner
 *  org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
 */
package io.netty.handler.ssl.util;

import io.netty.handler.ssl.util.SelfSignedCertificate;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

final class BouncyCastleSelfSignedCertGenerator {
    private static final Provider PROVIDER = new BouncyCastleProvider();

    static String[] generate(String string, KeyPair keyPair, SecureRandom secureRandom) throws Exception {
        PrivateKey privateKey = keyPair.getPrivate();
        X500Name x500Name = new X500Name("CN=" + string);
        JcaX509v3CertificateBuilder jcaX509v3CertificateBuilder = new JcaX509v3CertificateBuilder(x500Name, new BigInteger(64, secureRandom), SelfSignedCertificate.NOT_BEFORE, SelfSignedCertificate.NOT_AFTER, x500Name, keyPair.getPublic());
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSAEncryption").build(privateKey);
        X509CertificateHolder x509CertificateHolder = jcaX509v3CertificateBuilder.build(contentSigner);
        X509Certificate x509Certificate = new JcaX509CertificateConverter().setProvider(PROVIDER).getCertificate(x509CertificateHolder);
        x509Certificate.verify(keyPair.getPublic());
        return SelfSignedCertificate.newSelfSignedCertificate(string, privateKey, x509Certificate);
    }

    private BouncyCastleSelfSignedCertGenerator() {
    }
}

