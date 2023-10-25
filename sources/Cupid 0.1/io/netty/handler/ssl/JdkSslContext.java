package io.netty.handler.ssl;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSessionContext;

public abstract class JdkSslContext extends SslContext {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(JdkSslContext.class);
  
  static final String PROTOCOL = "TLS";
  
  static final String[] PROTOCOLS;
  
  static final List<String> DEFAULT_CIPHERS;
  
  private final String[] cipherSuites;
  
  private final List<String> unmodifiableCipherSuites;
  
  static {
    SSLContext context;
  }
  
  static {
    try {
      context = SSLContext.getInstance("TLS");
      context.init(null, null, null);
    } catch (Exception e) {
      throw new Error("failed to initialize the default SSL context", e);
    } 
    SSLEngine engine = context.createSSLEngine();
    String[] supportedProtocols = engine.getSupportedProtocols();
    List<String> protocols = new ArrayList<String>();
    addIfSupported(supportedProtocols, protocols, new String[] { "TLSv1.2", "TLSv1.1", "TLSv1", "SSLv3" });
    if (!protocols.isEmpty()) {
      PROTOCOLS = protocols.<String>toArray(new String[protocols.size()]);
    } else {
      PROTOCOLS = engine.getEnabledProtocols();
    } 
    String[] supportedCiphers = engine.getSupportedCipherSuites();
    List<String> ciphers = new ArrayList<String>();
    addIfSupported(supportedCiphers, ciphers, new String[] { "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_RSA_WITH_RC4_128_SHA", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", "TLS_RSA_WITH_AES_128_GCM_SHA256", "SSL_RSA_WITH_RC4_128_SHA", "SSL_RSA_WITH_RC4_128_MD5", "TLS_RSA_WITH_AES_128_CBC_SHA", "TLS_RSA_WITH_AES_256_CBC_SHA", "SSL_RSA_WITH_DES_CBC_SHA" });
    if (!ciphers.isEmpty()) {
      DEFAULT_CIPHERS = Collections.unmodifiableList(ciphers);
    } else {
      DEFAULT_CIPHERS = Collections.unmodifiableList(Arrays.asList(engine.getEnabledCipherSuites()));
    } 
    if (logger.isDebugEnabled()) {
      logger.debug("Default protocols (JDK): {} ", Arrays.asList(PROTOCOLS));
      logger.debug("Default cipher suites (JDK): {}", DEFAULT_CIPHERS);
    } 
  }
  
  private static void addIfSupported(String[] supported, List<String> enabled, String... names) {
    for (String n : names) {
      for (String s : supported) {
        if (n.equals(s)) {
          enabled.add(s);
          break;
        } 
      } 
    } 
  }
  
  JdkSslContext(Iterable<String> ciphers) {
    this.cipherSuites = toCipherSuiteArray(ciphers);
    this.unmodifiableCipherSuites = Collections.unmodifiableList(Arrays.asList(this.cipherSuites));
  }
  
  public final SSLSessionContext sessionContext() {
    if (isServer())
      return context().getServerSessionContext(); 
    return context().getClientSessionContext();
  }
  
  public final List<String> cipherSuites() {
    return this.unmodifiableCipherSuites;
  }
  
  public final long sessionCacheSize() {
    return sessionContext().getSessionCacheSize();
  }
  
  public final long sessionTimeout() {
    return sessionContext().getSessionTimeout();
  }
  
  public final SSLEngine newEngine(ByteBufAllocator alloc) {
    SSLEngine engine = context().createSSLEngine();
    engine.setEnabledCipherSuites(this.cipherSuites);
    engine.setEnabledProtocols(PROTOCOLS);
    engine.setUseClientMode(isClient());
    return wrapEngine(engine);
  }
  
  public final SSLEngine newEngine(ByteBufAllocator alloc, String peerHost, int peerPort) {
    SSLEngine engine = context().createSSLEngine(peerHost, peerPort);
    engine.setEnabledCipherSuites(this.cipherSuites);
    engine.setEnabledProtocols(PROTOCOLS);
    engine.setUseClientMode(isClient());
    return wrapEngine(engine);
  }
  
  private SSLEngine wrapEngine(SSLEngine engine) {
    if (nextProtocols().isEmpty())
      return engine; 
    return new JettyNpnSslEngine(engine, nextProtocols(), isServer());
  }
  
  private static String[] toCipherSuiteArray(Iterable<String> ciphers) {
    if (ciphers == null)
      return DEFAULT_CIPHERS.<String>toArray(new String[DEFAULT_CIPHERS.size()]); 
    List<String> newCiphers = new ArrayList<String>();
    for (String c : ciphers) {
      if (c == null)
        break; 
      newCiphers.add(c);
    } 
    return newCiphers.<String>toArray(new String[newCiphers.size()]);
  }
  
  public abstract SSLContext context();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\ssl\JdkSslContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */