package org.apache.commons.io.input;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.ByteOrderMark;

public class XmlStreamReader extends Reader {
  private static final int BUFFER_SIZE = 4096;
  
  private static final String UTF_8 = "UTF-8";
  
  private static final String US_ASCII = "US-ASCII";
  
  private static final String UTF_16BE = "UTF-16BE";
  
  private static final String UTF_16LE = "UTF-16LE";
  
  private static final String UTF_32BE = "UTF-32BE";
  
  private static final String UTF_32LE = "UTF-32LE";
  
  private static final String UTF_16 = "UTF-16";
  
  private static final String UTF_32 = "UTF-32";
  
  private static final String EBCDIC = "CP1047";
  
  private static final ByteOrderMark[] BOMS = new ByteOrderMark[] { ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE };
  
  private static final ByteOrderMark[] XML_GUESS_BYTES = new ByteOrderMark[] { new ByteOrderMark("UTF-8", new int[] { 60, 63, 120, 109 }), new ByteOrderMark("UTF-16BE", new int[] { 0, 60, 0, 63 }), new ByteOrderMark("UTF-16LE", new int[] { 60, 0, 63, 0 }), new ByteOrderMark("UTF-32BE", new int[] { 
          0, 0, 0, 60, 0, 0, 0, 63, 0, 0, 
          0, 120, 0, 0, 0, 109 }), new ByteOrderMark("UTF-32LE", new int[] { 
          60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 
          0, 0, 109, 0, 0, 0 }), new ByteOrderMark("CP1047", new int[] { 76, 111, 167, 148 }) };
  
  private final Reader reader;
  
  private final String encoding;
  
  private final String defaultEncoding;
  
  public String getDefaultEncoding() {
    return this.defaultEncoding;
  }
  
  public XmlStreamReader(File file) throws IOException {
    this(new FileInputStream(file));
  }
  
  public XmlStreamReader(InputStream is) throws IOException {
    this(is, true);
  }
  
  public XmlStreamReader(InputStream is, boolean lenient) throws IOException {
    this(is, lenient, (String)null);
  }
  
  public XmlStreamReader(InputStream is, boolean lenient, String defaultEncoding) throws IOException {
    this.defaultEncoding = defaultEncoding;
    BOMInputStream bom = new BOMInputStream(new BufferedInputStream(is, 4096), false, BOMS);
    BOMInputStream pis = new BOMInputStream(bom, true, XML_GUESS_BYTES);
    this.encoding = doRawStream(bom, pis, lenient);
    this.reader = new InputStreamReader(pis, this.encoding);
  }
  
  public XmlStreamReader(URL url) throws IOException {
    this(url.openConnection(), (String)null);
  }
  
  public XmlStreamReader(URLConnection conn, String defaultEncoding) throws IOException {
    this.defaultEncoding = defaultEncoding;
    boolean lenient = true;
    String contentType = conn.getContentType();
    InputStream is = conn.getInputStream();
    BOMInputStream bom = new BOMInputStream(new BufferedInputStream(is, 4096), false, BOMS);
    BOMInputStream pis = new BOMInputStream(bom, true, XML_GUESS_BYTES);
    if (conn instanceof java.net.HttpURLConnection || contentType != null) {
      this.encoding = doHttpStream(bom, pis, contentType, lenient);
    } else {
      this.encoding = doRawStream(bom, pis, lenient);
    } 
    this.reader = new InputStreamReader(pis, this.encoding);
  }
  
  public XmlStreamReader(InputStream is, String httpContentType) throws IOException {
    this(is, httpContentType, true);
  }
  
  public XmlStreamReader(InputStream is, String httpContentType, boolean lenient, String defaultEncoding) throws IOException {
    this.defaultEncoding = defaultEncoding;
    BOMInputStream bom = new BOMInputStream(new BufferedInputStream(is, 4096), false, BOMS);
    BOMInputStream pis = new BOMInputStream(bom, true, XML_GUESS_BYTES);
    this.encoding = doHttpStream(bom, pis, httpContentType, lenient);
    this.reader = new InputStreamReader(pis, this.encoding);
  }
  
  public XmlStreamReader(InputStream is, String httpContentType, boolean lenient) throws IOException {
    this(is, httpContentType, lenient, null);
  }
  
  public String getEncoding() {
    return this.encoding;
  }
  
  public int read(char[] buf, int offset, int len) throws IOException {
    return this.reader.read(buf, offset, len);
  }
  
  public void close() throws IOException {
    this.reader.close();
  }
  
  private String doRawStream(BOMInputStream bom, BOMInputStream pis, boolean lenient) throws IOException {
    String bomEnc = bom.getBOMCharsetName();
    String xmlGuessEnc = pis.getBOMCharsetName();
    String xmlEnc = getXmlProlog(pis, xmlGuessEnc);
    try {
      return calculateRawEncoding(bomEnc, xmlGuessEnc, xmlEnc);
    } catch (XmlStreamReaderException ex) {
      if (lenient)
        return doLenientDetection(null, ex); 
      throw ex;
    } 
  }
  
  private String doHttpStream(BOMInputStream bom, BOMInputStream pis, String httpContentType, boolean lenient) throws IOException {
    String bomEnc = bom.getBOMCharsetName();
    String xmlGuessEnc = pis.getBOMCharsetName();
    String xmlEnc = getXmlProlog(pis, xmlGuessEnc);
    try {
      return calculateHttpEncoding(httpContentType, bomEnc, xmlGuessEnc, xmlEnc, lenient);
    } catch (XmlStreamReaderException ex) {
      if (lenient)
        return doLenientDetection(httpContentType, ex); 
      throw ex;
    } 
  }
  
  private String doLenientDetection(String httpContentType, XmlStreamReaderException ex) throws IOException {
    if (httpContentType != null && httpContentType.startsWith("text/html")) {
      httpContentType = httpContentType.substring("text/html".length());
      httpContentType = "text/xml" + httpContentType;
      try {
        return calculateHttpEncoding(httpContentType, ex.getBomEncoding(), ex.getXmlGuessEncoding(), ex.getXmlEncoding(), true);
      } catch (XmlStreamReaderException ex2) {
        ex = ex2;
      } 
    } 
    String encoding = ex.getXmlEncoding();
    if (encoding == null)
      encoding = ex.getContentTypeEncoding(); 
    if (encoding == null)
      encoding = (this.defaultEncoding == null) ? "UTF-8" : this.defaultEncoding; 
    return encoding;
  }
  
  String calculateRawEncoding(String bomEnc, String xmlGuessEnc, String xmlEnc) throws IOException {
    if (bomEnc == null) {
      if (xmlGuessEnc == null || xmlEnc == null)
        return (this.defaultEncoding == null) ? "UTF-8" : this.defaultEncoding; 
      if (xmlEnc.equals("UTF-16") && (xmlGuessEnc.equals("UTF-16BE") || xmlGuessEnc.equals("UTF-16LE")))
        return xmlGuessEnc; 
      return xmlEnc;
    } 
    if (bomEnc.equals("UTF-8")) {
      if (xmlGuessEnc != null && !xmlGuessEnc.equals("UTF-8")) {
        String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
        throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
      } 
      if (xmlEnc != null && !xmlEnc.equals("UTF-8")) {
        String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
        throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
      } 
      return bomEnc;
    } 
    if (bomEnc.equals("UTF-16BE") || bomEnc.equals("UTF-16LE")) {
      if (xmlGuessEnc != null && !xmlGuessEnc.equals(bomEnc)) {
        String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
        throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
      } 
      if (xmlEnc != null && !xmlEnc.equals("UTF-16") && !xmlEnc.equals(bomEnc)) {
        String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
        throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
      } 
      return bomEnc;
    } 
    if (bomEnc.equals("UTF-32BE") || bomEnc.equals("UTF-32LE")) {
      if (xmlGuessEnc != null && !xmlGuessEnc.equals(bomEnc)) {
        String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
        throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
      } 
      if (xmlEnc != null && !xmlEnc.equals("UTF-32") && !xmlEnc.equals(bomEnc)) {
        String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
        throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
      } 
      return bomEnc;
    } 
    String msg = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
    throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
  }
  
  String calculateHttpEncoding(String httpContentType, String bomEnc, String xmlGuessEnc, String xmlEnc, boolean lenient) throws IOException {
    if (lenient && xmlEnc != null)
      return xmlEnc; 
    String cTMime = getContentTypeMime(httpContentType);
    String cTEnc = getContentTypeEncoding(httpContentType);
    boolean appXml = isAppXml(cTMime);
    boolean textXml = isTextXml(cTMime);
    if (!appXml && !textXml) {
      String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
      throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
    } 
    if (cTEnc == null) {
      if (appXml)
        return calculateRawEncoding(bomEnc, xmlGuessEnc, xmlEnc); 
      return (this.defaultEncoding == null) ? "US-ASCII" : this.defaultEncoding;
    } 
    if (cTEnc.equals("UTF-16BE") || cTEnc.equals("UTF-16LE")) {
      if (bomEnc != null) {
        String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
        throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
      } 
      return cTEnc;
    } 
    if (cTEnc.equals("UTF-16")) {
      if (bomEnc != null && bomEnc.startsWith("UTF-16"))
        return bomEnc; 
      String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
      throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
    } 
    if (cTEnc.equals("UTF-32BE") || cTEnc.equals("UTF-32LE")) {
      if (bomEnc != null) {
        String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
        throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
      } 
      return cTEnc;
    } 
    if (cTEnc.equals("UTF-32")) {
      if (bomEnc != null && bomEnc.startsWith("UTF-32"))
        return bomEnc; 
      String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
      throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
    } 
    return cTEnc;
  }
  
  static String getContentTypeMime(String httpContentType) {
    String mime = null;
    if (httpContentType != null) {
      int i = httpContentType.indexOf(";");
      if (i >= 0) {
        mime = httpContentType.substring(0, i);
      } else {
        mime = httpContentType;
      } 
      mime = mime.trim();
    } 
    return mime;
  }
  
  private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=[\"']?([.[^; \"']]*)[\"']?");
  
  static String getContentTypeEncoding(String httpContentType) {
    String encoding = null;
    if (httpContentType != null) {
      int i = httpContentType.indexOf(";");
      if (i > -1) {
        String postMime = httpContentType.substring(i + 1);
        Matcher m = CHARSET_PATTERN.matcher(postMime);
        encoding = m.find() ? m.group(1) : null;
        encoding = (encoding != null) ? encoding.toUpperCase(Locale.US) : null;
      } 
    } 
    return encoding;
  }
  
  public static final Pattern ENCODING_PATTERN = Pattern.compile("<\\?xml.*encoding[\\s]*=[\\s]*((?:\".[^\"]*\")|(?:'.[^']*'))", 8);
  
  private static final String RAW_EX_1 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch";
  
  private static final String RAW_EX_2 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM";
  
  private static final String HTTP_EX_1 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL";
  
  private static final String HTTP_EX_2 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch";
  
  private static final String HTTP_EX_3 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME";
  
  private static String getXmlProlog(InputStream is, String guessedEnc) throws IOException {
    String encoding = null;
    if (guessedEnc != null) {
      byte[] bytes = new byte[4096];
      is.mark(4096);
      int offset = 0;
      int max = 4096;
      int c = is.read(bytes, offset, max);
      int firstGT = -1;
      String xmlProlog = null;
      while (c != -1 && firstGT == -1 && offset < 4096) {
        offset += c;
        max -= c;
        c = is.read(bytes, offset, max);
        xmlProlog = new String(bytes, 0, offset, guessedEnc);
        firstGT = xmlProlog.indexOf('>');
      } 
      if (firstGT == -1) {
        if (c == -1)
          throw new IOException("Unexpected end of XML stream"); 
        throw new IOException("XML prolog or ROOT element not found on first " + offset + " bytes");
      } 
      int bytesRead = offset;
      if (bytesRead > 0) {
        is.reset();
        BufferedReader bReader = new BufferedReader(new StringReader(xmlProlog.substring(0, firstGT + 1)));
        StringBuffer prolog = new StringBuffer();
        String line = bReader.readLine();
        while (line != null) {
          prolog.append(line);
          line = bReader.readLine();
        } 
        Matcher m = ENCODING_PATTERN.matcher(prolog);
        if (m.find()) {
          encoding = m.group(1).toUpperCase();
          encoding = encoding.substring(1, encoding.length() - 1);
        } 
      } 
    } 
    return encoding;
  }
  
  static boolean isAppXml(String mime) {
    return (mime != null && (mime.equals("application/xml") || mime.equals("application/xml-dtd") || mime.equals("application/xml-external-parsed-entity") || (mime.startsWith("application/") && mime.endsWith("+xml"))));
  }
  
  static boolean isTextXml(String mime) {
    return (mime != null && (mime.equals("text/xml") || mime.equals("text/xml-external-parsed-entity") || (mime.startsWith("text/") && mime.endsWith("+xml"))));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\input\XmlStreamReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */