package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class RefinedSoundex implements StringEncoder {
  public static final String US_ENGLISH_MAPPING_STRING = "01360240043788015936020505";
  
  private static final char[] US_ENGLISH_MAPPING = "01360240043788015936020505".toCharArray();
  
  private final char[] soundexMapping;
  
  public static final RefinedSoundex US_ENGLISH = new RefinedSoundex();
  
  public RefinedSoundex() {
    this.soundexMapping = US_ENGLISH_MAPPING;
  }
  
  public RefinedSoundex(char[] mapping) {
    this.soundexMapping = new char[mapping.length];
    System.arraycopy(mapping, 0, this.soundexMapping, 0, mapping.length);
  }
  
  public RefinedSoundex(String mapping) {
    this.soundexMapping = mapping.toCharArray();
  }
  
  public int difference(String s1, String s2) throws EncoderException {
    return SoundexUtils.difference(this, s1, s2);
  }
  
  public Object encode(Object obj) throws EncoderException {
    if (!(obj instanceof String))
      throw new EncoderException("Parameter supplied to RefinedSoundex encode is not of type java.lang.String"); 
    return soundex((String)obj);
  }
  
  public String encode(String str) {
    return soundex(str);
  }
  
  char getMappingCode(char c) {
    if (!Character.isLetter(c))
      return Character.MIN_VALUE; 
    return this.soundexMapping[Character.toUpperCase(c) - 65];
  }
  
  public String soundex(String str) {
    if (str == null)
      return null; 
    str = SoundexUtils.clean(str);
    if (str.length() == 0)
      return str; 
    StringBuilder sBuf = new StringBuilder();
    sBuf.append(str.charAt(0));
    char last = '*';
    for (int i = 0; i < str.length(); i++) {
      char current = getMappingCode(str.charAt(i));
      if (current != last) {
        if (current != '\000')
          sBuf.append(current); 
        last = current;
      } 
    } 
    return sBuf.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\codec\language\RefinedSoundex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */