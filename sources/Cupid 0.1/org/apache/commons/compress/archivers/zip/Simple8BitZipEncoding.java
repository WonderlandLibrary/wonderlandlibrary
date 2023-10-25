package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Simple8BitZipEncoding implements ZipEncoding {
  private final char[] highChars;
  
  private final List<Simple8BitChar> reverseMapping;
  
  private static final class Simple8BitChar implements Comparable<Simple8BitChar> {
    public final char unicode;
    
    public final byte code;
    
    Simple8BitChar(byte code, char unicode) {
      this.code = code;
      this.unicode = unicode;
    }
    
    public int compareTo(Simple8BitChar a) {
      return this.unicode - a.unicode;
    }
    
    public String toString() {
      return "0x" + Integer.toHexString(Character.MAX_VALUE & this.unicode) + "->0x" + Integer.toHexString(0xFF & this.code);
    }
    
    public boolean equals(Object o) {
      if (o instanceof Simple8BitChar) {
        Simple8BitChar other = (Simple8BitChar)o;
        return (this.unicode == other.unicode && this.code == other.code);
      } 
      return false;
    }
    
    public int hashCode() {
      return this.unicode;
    }
  }
  
  public Simple8BitZipEncoding(char[] highChars) {
    this.highChars = (char[])highChars.clone();
    List<Simple8BitChar> temp = new ArrayList<Simple8BitChar>(this.highChars.length);
    byte code = Byte.MAX_VALUE;
    for (char highChar : this.highChars) {
      code = (byte)(code + 1);
      temp.add(new Simple8BitChar(code, highChar));
    } 
    Collections.sort(temp);
    this.reverseMapping = Collections.unmodifiableList(temp);
  }
  
  public char decodeByte(byte b) {
    if (b >= 0)
      return (char)b; 
    return this.highChars[128 + b];
  }
  
  public boolean canEncodeChar(char c) {
    if (c >= '\000' && c < '')
      return true; 
    Simple8BitChar r = encodeHighChar(c);
    return (r != null);
  }
  
  public boolean pushEncodedChar(ByteBuffer bb, char c) {
    if (c >= '\000' && c < '') {
      bb.put((byte)c);
      return true;
    } 
    Simple8BitChar r = encodeHighChar(c);
    if (r == null)
      return false; 
    bb.put(r.code);
    return true;
  }
  
  private Simple8BitChar encodeHighChar(char c) {
    int i0 = 0;
    int i1 = this.reverseMapping.size();
    while (i1 > i0) {
      int i = i0 + (i1 - i0) / 2;
      Simple8BitChar m = this.reverseMapping.get(i);
      if (m.unicode == c)
        return m; 
      if (m.unicode < c) {
        i0 = i + 1;
        continue;
      } 
      i1 = i;
    } 
    if (i0 >= this.reverseMapping.size())
      return null; 
    Simple8BitChar r = this.reverseMapping.get(i0);
    if (r.unicode != c)
      return null; 
    return r;
  }
  
  public boolean canEncode(String name) {
    for (int i = 0; i < name.length(); i++) {
      char c = name.charAt(i);
      if (!canEncodeChar(c))
        return false; 
    } 
    return true;
  }
  
  public ByteBuffer encode(String name) {
    ByteBuffer out = ByteBuffer.allocate(name.length() + 6 + (name.length() + 1) / 2);
    for (int i = 0; i < name.length(); i++) {
      char c = name.charAt(i);
      if (out.remaining() < 6)
        out = ZipEncodingHelper.growBuffer(out, out.position() + 6); 
      if (!pushEncodedChar(out, c))
        ZipEncodingHelper.appendSurrogate(out, c); 
    } 
    out.limit(out.position());
    out.rewind();
    return out;
  }
  
  public String decode(byte[] data) throws IOException {
    char[] ret = new char[data.length];
    for (int i = 0; i < data.length; i++)
      ret[i] = decodeByte(data[i]); 
    return new String(ret);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\zip\Simple8BitZipEncoding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */