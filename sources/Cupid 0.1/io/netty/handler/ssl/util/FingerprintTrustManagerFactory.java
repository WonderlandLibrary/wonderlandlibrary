package io.netty.handler.ssl.util;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
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
import java.util.List;
import java.util.regex.Pattern;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public final class FingerprintTrustManagerFactory extends SimpleTrustManagerFactory {
  private static final Pattern FINGERPRINT_PATTERN = Pattern.compile("^[0-9a-fA-F:]+$");
  
  private static final Pattern FINGERPRINT_STRIP_PATTERN = Pattern.compile(":");
  
  private static final int SHA1_BYTE_LEN = 20;
  
  private static final int SHA1_HEX_LEN = 40;
  
  private static final FastThreadLocal<MessageDigest> tlmd = new FastThreadLocal<MessageDigest>() {
      protected MessageDigest initialValue() {
        try {
          return MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
          throw new Error(e);
        } 
      }
    };
  
  private final TrustManager tm = new X509TrustManager() {
      public void checkClientTrusted(X509Certificate[] chain, String s) throws CertificateException {
        checkTrusted("client", chain);
      }
      
      public void checkServerTrusted(X509Certificate[] chain, String s) throws CertificateException {
        checkTrusted("server", chain);
      }
      
      private void checkTrusted(String type, X509Certificate[] chain) throws CertificateException {
        X509Certificate cert = chain[0];
        byte[] fingerprint = fingerprint(cert);
        boolean found = false;
        for (byte[] allowedFingerprint : FingerprintTrustManagerFactory.this.fingerprints) {
          if (Arrays.equals(fingerprint, allowedFingerprint)) {
            found = true;
            break;
          } 
        } 
        if (!found)
          throw new CertificateException(type + " certificate with unknown fingerprint: " + cert.getSubjectDN()); 
      }
      
      private byte[] fingerprint(X509Certificate cert) throws CertificateEncodingException {
        MessageDigest md = (MessageDigest)FingerprintTrustManagerFactory.tlmd.get();
        md.reset();
        return md.digest(cert.getEncoded());
      }
      
      public X509Certificate[] getAcceptedIssuers() {
        return EmptyArrays.EMPTY_X509_CERTIFICATES;
      }
    };
  
  private final byte[][] fingerprints;
  
  public FingerprintTrustManagerFactory(Iterable<String> fingerprints) {
    this(toFingerprintArray(fingerprints));
  }
  
  public FingerprintTrustManagerFactory(String... fingerprints) {
    this(toFingerprintArray(Arrays.asList(fingerprints)));
  }
  
  public FingerprintTrustManagerFactory(byte[]... fingerprints) {
    if (fingerprints == null)
      throw new NullPointerException("fingerprints"); 
    List<byte[]> list = (List)new ArrayList<byte>();
    for (byte[] f : fingerprints) {
      if (f == null)
        break; 
      if (f.length != 20)
        throw new IllegalArgumentException("malformed fingerprint: " + ByteBufUtil.hexDump(Unpooled.wrappedBuffer(f)) + " (expected: SHA1)"); 
      list.add(f.clone());
    } 
    this.fingerprints = list.<byte[]>toArray(new byte[list.size()][]);
  }
  
  private static byte[][] toFingerprintArray(Iterable<String> fingerprints) {
    if (fingerprints == null)
      throw new NullPointerException("fingerprints"); 
    List<byte[]> list = (List)new ArrayList<byte>();
    for (String f : fingerprints) {
      if (f == null)
        break; 
      if (!FINGERPRINT_PATTERN.matcher(f).matches())
        throw new IllegalArgumentException("malformed fingerprint: " + f); 
      f = FINGERPRINT_STRIP_PATTERN.matcher(f).replaceAll("");
      if (f.length() != 40)
        throw new IllegalArgumentException("malformed fingerprint: " + f + " (expected: SHA1)"); 
      byte[] farr = new byte[20];
      for (int i = 0; i < farr.length; i++) {
        int strIdx = i << 1;
        farr[i] = (byte)Integer.parseInt(f.substring(strIdx, strIdx + 2), 16);
      } 
    } 
    return list.<byte[]>toArray(new byte[list.size()][]);
  }
  
  protected void engineInit(KeyStore keyStore) throws Exception {}
  
  protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws Exception {}
  
  protected TrustManager[] engineGetTrustManagers() {
    return new TrustManager[] { this.tm };
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\ss\\util\FingerprintTrustManagerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */