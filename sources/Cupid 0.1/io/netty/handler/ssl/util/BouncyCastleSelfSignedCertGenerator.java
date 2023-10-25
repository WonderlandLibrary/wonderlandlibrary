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
  private static final Provider PROVIDER = (Provider)new BouncyCastleProvider();
  
  static String[] generate(String fqdn, KeyPair keypair, SecureRandom random) throws Exception {
    PrivateKey key = keypair.getPrivate();
    X500Name owner = new X500Name("CN=" + fqdn);
    JcaX509v3CertificateBuilder jcaX509v3CertificateBuilder = new JcaX509v3CertificateBuilder(owner, new BigInteger(64, random), SelfSignedCertificate.NOT_BEFORE, SelfSignedCertificate.NOT_AFTER, owner, keypair.getPublic());
    ContentSigner signer = (new JcaContentSignerBuilder("SHA256WithRSAEncryption")).build(key);
    X509CertificateHolder certHolder = jcaX509v3CertificateBuilder.build(signer);
    X509Certificate cert = (new JcaX509CertificateConverter()).setProvider(PROVIDER).getCertificate(certHolder);
    cert.verify(keypair.getPublic());
    return SelfSignedCertificate.newSelfSignedCertificate(fqdn, key, cert);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\ss\\util\BouncyCastleSelfSignedCertGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */