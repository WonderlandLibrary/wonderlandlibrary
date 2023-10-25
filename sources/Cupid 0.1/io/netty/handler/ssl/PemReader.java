package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class PemReader {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(PemReader.class);
  
  private static final Pattern CERT_PATTERN = Pattern.compile("-+BEGIN\\s+.*CERTIFICATE[^-]*-+(?:\\s|\\r|\\n)+([a-z0-9+/=\\r\\n]+)-+END\\s+.*CERTIFICATE[^-]*-+", 2);
  
  private static final Pattern KEY_PATTERN = Pattern.compile("-+BEGIN\\s+.*PRIVATE\\s+KEY[^-]*-+(?:\\s|\\r|\\n)+([a-z0-9+/=\\r\\n]+)-+END\\s+.*PRIVATE\\s+KEY[^-]*-+", 2);
  
  static ByteBuf[] readCertificates(File file) throws CertificateException {
    String content;
    try {
      content = readContent(file);
    } catch (IOException e) {
      throw new CertificateException("failed to read a file: " + file, e);
    } 
    List<ByteBuf> certs = new ArrayList<ByteBuf>();
    Matcher m = CERT_PATTERN.matcher(content);
    int start = 0;
    while (m.find(start)) {
      ByteBuf base64 = Unpooled.copiedBuffer(m.group(1), CharsetUtil.US_ASCII);
      ByteBuf der = Base64.decode(base64);
      base64.release();
      certs.add(der);
      start = m.end();
    } 
    if (certs.isEmpty())
      throw new CertificateException("found no certificates: " + file); 
    return certs.<ByteBuf>toArray(new ByteBuf[certs.size()]);
  }
  
  static ByteBuf readPrivateKey(File file) throws KeyException {
    String content;
    try {
      content = readContent(file);
    } catch (IOException e) {
      throw new KeyException("failed to read a file: " + file, e);
    } 
    Matcher m = KEY_PATTERN.matcher(content);
    if (!m.find())
      throw new KeyException("found no private key: " + file); 
    ByteBuf base64 = Unpooled.copiedBuffer(m.group(1), CharsetUtil.US_ASCII);
    ByteBuf der = Base64.decode(base64);
    base64.release();
    return der;
  }
  
  private static String readContent(File file) throws IOException {
    InputStream in = new FileInputStream(file);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      byte[] buf = new byte[8192];
      while (true) {
        int ret = in.read(buf);
        if (ret < 0)
          break; 
        out.write(buf, 0, ret);
      } 
      return out.toString(CharsetUtil.US_ASCII.name());
    } finally {
      safeClose(in);
      safeClose(out);
    } 
  }
  
  private static void safeClose(InputStream in) {
    try {
      in.close();
    } catch (IOException e) {
      logger.warn("Failed to close a stream.", e);
    } 
  }
  
  private static void safeClose(OutputStream out) {
    try {
      out.close();
    } catch (IOException e) {
      logger.warn("Failed to close a stream.", e);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\ssl\PemReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */