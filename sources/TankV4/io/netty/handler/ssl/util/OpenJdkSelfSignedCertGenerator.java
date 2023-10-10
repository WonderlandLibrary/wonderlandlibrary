package io.netty.handler.ssl.util;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateSubjectName;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

final class OpenJdkSelfSignedCertGenerator {
   static String[] generate(String var0, KeyPair var1, SecureRandom var2) throws Exception {
      PrivateKey var3 = var1.getPrivate();
      X509CertInfo var4 = new X509CertInfo();
      X500Name var5 = new X500Name("CN=" + var0);
      var4.set("version", new CertificateVersion(2));
      var4.set("serialNumber", new CertificateSerialNumber(new BigInteger(64, var2)));

      try {
         var4.set("subject", new CertificateSubjectName(var5));
      } catch (CertificateException var8) {
         var4.set("subject", var5);
      }

      try {
         var4.set("issuer", new CertificateIssuerName(var5));
      } catch (CertificateException var7) {
         var4.set("issuer", var5);
      }

      var4.set("validity", new CertificateValidity(SelfSignedCertificate.NOT_BEFORE, SelfSignedCertificate.NOT_AFTER));
      var4.set("key", new CertificateX509Key(var1.getPublic()));
      var4.set("algorithmID", new CertificateAlgorithmId(new AlgorithmId(AlgorithmId.sha1WithRSAEncryption_oid)));
      X509CertImpl var6 = new X509CertImpl(var4);
      var6.sign(var3, "SHA1withRSA");
      var4.set("algorithmID.algorithm", var6.get("x509.algorithm"));
      var6 = new X509CertImpl(var4);
      var6.sign(var3, "SHA1withRSA");
      var6.verify(var1.getPublic());
      return SelfSignedCertificate.newSelfSignedCertificate(var0, var3, var6);
   }

   private OpenJdkSelfSignedCertGenerator() {
   }
}
