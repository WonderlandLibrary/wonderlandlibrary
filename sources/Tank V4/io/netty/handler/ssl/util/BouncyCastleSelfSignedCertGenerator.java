package io.netty.handler.ssl.util;

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

   static String[] generate(String var0, KeyPair var1, SecureRandom var2) throws Exception {
      PrivateKey var3 = var1.getPrivate();
      X500Name var4 = new X500Name("CN=" + var0);
      JcaX509v3CertificateBuilder var5 = new JcaX509v3CertificateBuilder(var4, new BigInteger(64, var2), SelfSignedCertificate.NOT_BEFORE, SelfSignedCertificate.NOT_AFTER, var4, var1.getPublic());
      ContentSigner var6 = (new JcaContentSignerBuilder("SHA256WithRSAEncryption")).build(var3);
      X509CertificateHolder var7 = var5.build(var6);
      X509Certificate var8 = (new JcaX509CertificateConverter()).setProvider(PROVIDER).getCertificate(var7);
      var8.verify(var1.getPublic());
      return SelfSignedCertificate.newSelfSignedCertificate(var0, var3, var8);
   }

   private BouncyCastleSelfSignedCertGenerator() {
   }
}
