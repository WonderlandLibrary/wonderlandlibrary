package org.apache.commons.codec;

import java.util.Comparator;

public class StringEncoderComparator implements Comparator {
  private final StringEncoder stringEncoder;
  
  @Deprecated
  public StringEncoderComparator() {
    this.stringEncoder = null;
  }
  
  public StringEncoderComparator(StringEncoder stringEncoder) {
    this.stringEncoder = stringEncoder;
  }
  
  public int compare(Object o1, Object o2) {
    int compareCode = 0;
    try {
      Comparable<Comparable<?>> s1 = (Comparable<Comparable<?>>)this.stringEncoder.encode(o1);
      Comparable<?> s2 = (Comparable)this.stringEncoder.encode(o2);
      compareCode = s1.compareTo(s2);
    } catch (EncoderException ee) {
      compareCode = 0;
    } 
    return compareCode;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\codec\StringEncoderComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */