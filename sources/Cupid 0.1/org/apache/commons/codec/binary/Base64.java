package org.apache.commons.codec.binary;

import java.math.BigInteger;

public class Base64 extends BaseNCodec {
  private static final int BITS_PER_ENCODED_BYTE = 6;
  
  private static final int BYTES_PER_UNENCODED_BLOCK = 3;
  
  private static final int BYTES_PER_ENCODED_BLOCK = 4;
  
  static final byte[] CHUNK_SEPARATOR = new byte[] { 13, 10 };
  
  private static final byte[] STANDARD_ENCODE_TABLE = new byte[] { 
      65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
      85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
      101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
      111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
      121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
      56, 57, 43, 47 };
  
  private static final byte[] URL_SAFE_ENCODE_TABLE = new byte[] { 
      65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
      85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
      101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
      111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
      121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
      56, 57, 45, 95 };
  
  private static final byte[] DECODE_TABLE = new byte[] { 
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
      -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 
      54, 55, 56, 57, 58, 59, 60, 61, -1, -1, 
      -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 
      5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 
      15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 
      25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 
      29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 
      39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 
      49, 50, 51 };
  
  private static final int MASK_6BITS = 63;
  
  private final byte[] encodeTable;
  
  private final byte[] decodeTable = DECODE_TABLE;
  
  private final byte[] lineSeparator;
  
  private final int decodeSize;
  
  private final int encodeSize;
  
  public Base64() {
    this(0);
  }
  
  public Base64(boolean urlSafe) {
    this(76, CHUNK_SEPARATOR, urlSafe);
  }
  
  public Base64(int lineLength) {
    this(lineLength, CHUNK_SEPARATOR);
  }
  
  public Base64(int lineLength, byte[] lineSeparator) {
    this(lineLength, lineSeparator, false);
  }
  
  public Base64(int lineLength, byte[] lineSeparator, boolean urlSafe) {
    super(3, 4, lineLength, (lineSeparator == null) ? 0 : lineSeparator.length);
    if (lineSeparator != null) {
      if (containsAlphabetOrPad(lineSeparator)) {
        String sep = StringUtils.newStringUtf8(lineSeparator);
        throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + sep + "]");
      } 
      if (lineLength > 0) {
        this.encodeSize = 4 + lineSeparator.length;
        this.lineSeparator = new byte[lineSeparator.length];
        System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
      } else {
        this.encodeSize = 4;
        this.lineSeparator = null;
      } 
    } else {
      this.encodeSize = 4;
      this.lineSeparator = null;
    } 
    this.decodeSize = this.encodeSize - 1;
    this.encodeTable = urlSafe ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
  }
  
  public boolean isUrlSafe() {
    return (this.encodeTable == URL_SAFE_ENCODE_TABLE);
  }
  
  void encode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context) {
    if (context.eof)
      return; 
    if (inAvail < 0) {
      context.eof = true;
      if (0 == context.modulus && this.lineLength == 0)
        return; 
      byte[] buffer = ensureBufferSize(this.encodeSize, context);
      int savedPos = context.pos;
      switch (context.modulus) {
        case 0:
          break;
        case 1:
          buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 2 & 0x3F];
          buffer[context.pos++] = this.encodeTable[context.ibitWorkArea << 4 & 0x3F];
          if (this.encodeTable == STANDARD_ENCODE_TABLE) {
            buffer[context.pos++] = 61;
            buffer[context.pos++] = 61;
          } 
          break;
        case 2:
          buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 10 & 0x3F];
          buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 4 & 0x3F];
          buffer[context.pos++] = this.encodeTable[context.ibitWorkArea << 2 & 0x3F];
          if (this.encodeTable == STANDARD_ENCODE_TABLE)
            buffer[context.pos++] = 61; 
          break;
        default:
          throw new IllegalStateException("Impossible modulus " + context.modulus);
      } 
      context.currentLinePos += context.pos - savedPos;
      if (this.lineLength > 0 && context.currentLinePos > 0) {
        System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
        context.pos += this.lineSeparator.length;
      } 
    } else {
      for (int i = 0; i < inAvail; i++) {
        byte[] buffer = ensureBufferSize(this.encodeSize, context);
        context.modulus = (context.modulus + 1) % 3;
        int b = in[inPos++];
        if (b < 0)
          b += 256; 
        context.ibitWorkArea = (context.ibitWorkArea << 8) + b;
        if (0 == context.modulus) {
          buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 18 & 0x3F];
          buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 12 & 0x3F];
          buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 6 & 0x3F];
          buffer[context.pos++] = this.encodeTable[context.ibitWorkArea & 0x3F];
          context.currentLinePos += 4;
          if (this.lineLength > 0 && this.lineLength <= context.currentLinePos) {
            System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
            context.pos += this.lineSeparator.length;
            context.currentLinePos = 0;
          } 
        } 
      } 
    } 
  }
  
  void decode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context) {
    if (context.eof)
      return; 
    if (inAvail < 0)
      context.eof = true; 
    for (int i = 0; i < inAvail; i++) {
      byte[] buffer = ensureBufferSize(this.decodeSize, context);
      byte b = in[inPos++];
      if (b == 61) {
        context.eof = true;
        break;
      } 
      if (b >= 0 && b < DECODE_TABLE.length) {
        int result = DECODE_TABLE[b];
        if (result >= 0) {
          context.modulus = (context.modulus + 1) % 4;
          context.ibitWorkArea = (context.ibitWorkArea << 6) + result;
          if (context.modulus == 0) {
            buffer[context.pos++] = (byte)(context.ibitWorkArea >> 16 & 0xFF);
            buffer[context.pos++] = (byte)(context.ibitWorkArea >> 8 & 0xFF);
            buffer[context.pos++] = (byte)(context.ibitWorkArea & 0xFF);
          } 
        } 
      } 
    } 
    if (context.eof && context.modulus != 0) {
      byte[] buffer = ensureBufferSize(this.decodeSize, context);
      switch (context.modulus) {
        case 1:
          return;
        case 2:
          context.ibitWorkArea >>= 4;
          buffer[context.pos++] = (byte)(context.ibitWorkArea & 0xFF);
        case 3:
          context.ibitWorkArea >>= 2;
          buffer[context.pos++] = (byte)(context.ibitWorkArea >> 8 & 0xFF);
          buffer[context.pos++] = (byte)(context.ibitWorkArea & 0xFF);
      } 
      throw new IllegalStateException("Impossible modulus " + context.modulus);
    } 
  }
  
  @Deprecated
  public static boolean isArrayByteBase64(byte[] arrayOctet) {
    return isBase64(arrayOctet);
  }
  
  public static boolean isBase64(byte octet) {
    return (octet == 61 || (octet >= 0 && octet < DECODE_TABLE.length && DECODE_TABLE[octet] != -1));
  }
  
  public static boolean isBase64(String base64) {
    return isBase64(StringUtils.getBytesUtf8(base64));
  }
  
  public static boolean isBase64(byte[] arrayOctet) {
    for (int i = 0; i < arrayOctet.length; i++) {
      if (!isBase64(arrayOctet[i]) && !isWhiteSpace(arrayOctet[i]))
        return false; 
    } 
    return true;
  }
  
  public static byte[] encodeBase64(byte[] binaryData) {
    return encodeBase64(binaryData, false);
  }
  
  public static String encodeBase64String(byte[] binaryData) {
    return StringUtils.newStringUtf8(encodeBase64(binaryData, false));
  }
  
  public static byte[] encodeBase64URLSafe(byte[] binaryData) {
    return encodeBase64(binaryData, false, true);
  }
  
  public static String encodeBase64URLSafeString(byte[] binaryData) {
    return StringUtils.newStringUtf8(encodeBase64(binaryData, false, true));
  }
  
  public static byte[] encodeBase64Chunked(byte[] binaryData) {
    return encodeBase64(binaryData, true);
  }
  
  public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
    return encodeBase64(binaryData, isChunked, false);
  }
  
  public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe) {
    return encodeBase64(binaryData, isChunked, urlSafe, 2147483647);
  }
  
  public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe, int maxResultSize) {
    if (binaryData == null || binaryData.length == 0)
      return binaryData; 
    Base64 b64 = isChunked ? new Base64(urlSafe) : new Base64(0, CHUNK_SEPARATOR, urlSafe);
    long len = b64.getEncodedLength(binaryData);
    if (len > maxResultSize)
      throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + len + ") than the specified maximum size of " + maxResultSize); 
    return b64.encode(binaryData);
  }
  
  public static byte[] decodeBase64(String base64String) {
    return (new Base64()).decode(base64String);
  }
  
  public static byte[] decodeBase64(byte[] base64Data) {
    return (new Base64()).decode(base64Data);
  }
  
  public static BigInteger decodeInteger(byte[] pArray) {
    return new BigInteger(1, decodeBase64(pArray));
  }
  
  public static byte[] encodeInteger(BigInteger bigInt) {
    if (bigInt == null)
      throw new NullPointerException("encodeInteger called with null parameter"); 
    return encodeBase64(toIntegerBytes(bigInt), false);
  }
  
  static byte[] toIntegerBytes(BigInteger bigInt) {
    int bitlen = bigInt.bitLength();
    bitlen = bitlen + 7 >> 3 << 3;
    byte[] bigBytes = bigInt.toByteArray();
    if (bigInt.bitLength() % 8 != 0 && bigInt.bitLength() / 8 + 1 == bitlen / 8)
      return bigBytes; 
    int startSrc = 0;
    int len = bigBytes.length;
    if (bigInt.bitLength() % 8 == 0) {
      startSrc = 1;
      len--;
    } 
    int startDst = bitlen / 8 - len;
    byte[] resizedBytes = new byte[bitlen / 8];
    System.arraycopy(bigBytes, startSrc, resizedBytes, startDst, len);
    return resizedBytes;
  }
  
  protected boolean isInAlphabet(byte octet) {
    return (octet >= 0 && octet < this.decodeTable.length && this.decodeTable[octet] != -1);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\codec\binary\Base64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */