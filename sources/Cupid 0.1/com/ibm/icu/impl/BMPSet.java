package com.ibm.icu.impl;

import com.ibm.icu.text.UnicodeSet;

public final class BMPSet {
  public static int U16_SURROGATE_OFFSET = 56613888;
  
  private boolean[] latin1Contains;
  
  private int[] table7FF;
  
  private int[] bmpBlockBits;
  
  private int[] list4kStarts;
  
  private final int[] list;
  
  private final int listLength;
  
  public BMPSet(int[] parentList, int parentListLength) {
    this.list = parentList;
    this.listLength = parentListLength;
    this.latin1Contains = new boolean[256];
    this.table7FF = new int[64];
    this.bmpBlockBits = new int[64];
    this.list4kStarts = new int[18];
    this.list4kStarts[0] = findCodePoint(2048, 0, this.listLength - 1);
    for (int i = 1; i <= 16; i++)
      this.list4kStarts[i] = findCodePoint(i << 12, this.list4kStarts[i - 1], this.listLength - 1); 
    this.list4kStarts[17] = this.listLength - 1;
    initBits();
  }
  
  public BMPSet(BMPSet otherBMPSet, int[] newParentList, int newParentListLength) {
    this.list = newParentList;
    this.listLength = newParentListLength;
    this.latin1Contains = (boolean[])otherBMPSet.latin1Contains.clone();
    this.table7FF = (int[])otherBMPSet.table7FF.clone();
    this.bmpBlockBits = (int[])otherBMPSet.bmpBlockBits.clone();
    this.list4kStarts = (int[])otherBMPSet.list4kStarts.clone();
  }
  
  public boolean contains(int c) {
    if (c <= 255)
      return this.latin1Contains[c]; 
    if (c <= 2047)
      return ((this.table7FF[c & 0x3F] & 1 << c >> 6) != 0); 
    if (c < 55296 || (c >= 57344 && c <= 65535)) {
      int lead = c >> 12;
      int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
      if (twoBits <= 1)
        return (0 != twoBits); 
      return containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1]);
    } 
    if (c <= 1114111)
      return containsSlow(c, this.list4kStarts[13], this.list4kStarts[17]); 
    return false;
  }
  
  public final int span(CharSequence s, int start, int end, UnicodeSet.SpanCondition spanCondition) {
    int i = start;
    int limit = Math.min(s.length(), end);
    if (UnicodeSet.SpanCondition.NOT_CONTAINED != spanCondition) {
      while (i < limit) {
        char c = s.charAt(i);
        if (c <= 'ÿ') {
          if (!this.latin1Contains[c])
            break; 
        } else if (c <= '߿') {
          if ((this.table7FF[c & 0x3F] & 1 << c >> 6) == 0)
            break; 
        } else {
          char c2;
          if (c < '?' || c >= '?' || i + 1 == limit || (c2 = s.charAt(i + 1)) < '?' || c2 >= '') {
            int lead = c >> 12;
            int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
            if (twoBits <= 1) {
              if (twoBits == 0)
                break; 
            } else if (!containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1])) {
              break;
            } 
          } else {
            int supplementary = UCharacterProperty.getRawSupplementary(c, c2);
            if (!containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17]))
              break; 
            i++;
          } 
        } 
        i++;
      } 
    } else {
      while (i < limit) {
        char c = s.charAt(i);
        if (c <= 'ÿ') {
          if (this.latin1Contains[c])
            break; 
        } else if (c <= '߿') {
          if ((this.table7FF[c & 0x3F] & 1 << c >> 6) != 0)
            break; 
        } else {
          char c2;
          if (c < '?' || c >= '?' || i + 1 == limit || (c2 = s.charAt(i + 1)) < '?' || c2 >= '') {
            int lead = c >> 12;
            int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
            if (twoBits <= 1) {
              if (twoBits != 0)
                break; 
            } else if (containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1])) {
              break;
            } 
          } else {
            int supplementary = UCharacterProperty.getRawSupplementary(c, c2);
            if (containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17]))
              break; 
            i++;
          } 
        } 
        i++;
      } 
    } 
    return i - start;
  }
  
  public final int spanBack(CharSequence s, int limit, UnicodeSet.SpanCondition spanCondition) {
    limit = Math.min(s.length(), limit);
    if (UnicodeSet.SpanCondition.NOT_CONTAINED != spanCondition) {
      while (true) {
        char c = s.charAt(--limit);
        if (c <= 'ÿ') {
          if (!this.latin1Contains[c])
            break; 
        } else if (c <= '߿') {
          if ((this.table7FF[c & 0x3F] & 1 << c >> 6) == 0)
            break; 
        } else {
          char c2;
          if (c < '?' || c < '?' || 0 == limit || (c2 = s.charAt(limit - 1)) < '?' || c2 >= '?') {
            int lead = c >> 12;
            int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
            if (twoBits <= 1) {
              if (twoBits == 0)
                break; 
            } else if (!containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1])) {
              break;
            } 
          } else {
            int supplementary = UCharacterProperty.getRawSupplementary(c2, c);
            if (!containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17]))
              break; 
            limit--;
          } 
        } 
        if (0 == limit)
          return 0; 
      } 
    } else {
      while (true) {
        char c = s.charAt(--limit);
        if (c <= 'ÿ') {
          if (this.latin1Contains[c])
            break; 
        } else if (c <= '߿') {
          if ((this.table7FF[c & 0x3F] & 1 << c >> 6) != 0)
            break; 
        } else {
          char c2;
          if (c < '?' || c < '?' || 0 == limit || (c2 = s.charAt(limit - 1)) < '?' || c2 >= '?') {
            int lead = c >> 12;
            int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
            if (twoBits <= 1) {
              if (twoBits != 0)
                break; 
            } else if (containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1])) {
              break;
            } 
          } else {
            int supplementary = UCharacterProperty.getRawSupplementary(c2, c);
            if (containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17]))
              break; 
            limit--;
          } 
        } 
        if (0 == limit)
          return 0; 
      } 
    } 
    return limit + 1;
  }
  
  private static void set32x64Bits(int[] table, int start, int limit) {
    assert 64 == table.length;
    int lead = start >> 6;
    int trail = start & 0x3F;
    int bits = 1 << lead;
    if (start + 1 == limit) {
      table[trail] = table[trail] | bits;
      return;
    } 
    int limitLead = limit >> 6;
    int limitTrail = limit & 0x3F;
    if (lead == limitLead) {
      while (trail < limitTrail)
        table[trail++] = table[trail++] | bits; 
    } else {
      if (trail > 0)
        while (true) {
          table[trail++] = table[trail++] | bits;
          if (trail >= 64) {
            lead++;
            break;
          } 
        }  
      if (lead < limitLead) {
        bits = (1 << lead) - 1 ^ 0xFFFFFFFF;
        if (limitLead < 32)
          bits &= (1 << limitLead) - 1; 
        for (trail = 0; trail < 64; trail++)
          table[trail] = table[trail] | bits; 
      } 
      bits = 1 << limitLead;
      for (trail = 0; trail < limitTrail; trail++)
        table[trail] = table[trail] | bits; 
    } 
  }
  
  private void initBits() {
    int start, limit, listIndex = 0;
    do {
      start = this.list[listIndex++];
      if (listIndex < this.listLength) {
        limit = this.list[listIndex++];
      } else {
        limit = 1114112;
      } 
      if (start >= 256)
        break; 
      do {
        this.latin1Contains[start++] = true;
      } while (start < limit && start < 256);
    } while (limit <= 256);
    while (start < 2048) {
      set32x64Bits(this.table7FF, start, (limit <= 2048) ? limit : 2048);
      if (limit > 2048) {
        start = 2048;
        break;
      } 
      start = this.list[listIndex++];
      if (listIndex < this.listLength) {
        limit = this.list[listIndex++];
        continue;
      } 
      limit = 1114112;
    } 
    int minStart = 2048;
    while (start < 65536) {
      if (limit > 65536)
        limit = 65536; 
      if (start < minStart)
        start = minStart; 
      if (start < limit) {
        if (0 != (start & 0x3F)) {
          start >>= 6;
          this.bmpBlockBits[start & 0x3F] = this.bmpBlockBits[start & 0x3F] | 65537 << start >> 6;
          start = start + 1 << 6;
          minStart = start;
        } 
        if (start < limit) {
          if (start < (limit & 0xFFFFFFC0))
            set32x64Bits(this.bmpBlockBits, start >> 6, limit >> 6); 
          if (0 != (limit & 0x3F)) {
            limit >>= 6;
            this.bmpBlockBits[limit & 0x3F] = this.bmpBlockBits[limit & 0x3F] | 65537 << limit >> 6;
            limit = limit + 1 << 6;
            minStart = limit;
          } 
        } 
      } 
      if (limit == 65536)
        break; 
      start = this.list[listIndex++];
      if (listIndex < this.listLength) {
        limit = this.list[listIndex++];
        continue;
      } 
      limit = 1114112;
    } 
  }
  
  private int findCodePoint(int c, int lo, int hi) {
    if (c < this.list[lo])
      return lo; 
    if (lo >= hi || c >= this.list[hi - 1])
      return hi; 
    while (true) {
      int i = lo + hi >>> 1;
      if (i == lo)
        break; 
      if (c < this.list[i]) {
        hi = i;
        continue;
      } 
      lo = i;
    } 
    return hi;
  }
  
  private final boolean containsSlow(int c, int lo, int hi) {
    return (0 != (findCodePoint(c, lo, hi) & 0x1));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\BMPSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */