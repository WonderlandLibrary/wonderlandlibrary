package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.ULocale;
import java.text.CharacterIterator;

final class BreakTransliterator extends Transliterator {
  private BreakIterator bi;
  
  private String insertion;
  
  private int[] boundaries = new int[50];
  
  private int boundaryCount = 0;
  
  static final int LETTER_OR_MARK_MASK = 510;
  
  public BreakTransliterator(String ID, UnicodeFilter filter, BreakIterator bi, String insertion) {
    super(ID, filter);
    this.bi = bi;
    this.insertion = insertion;
  }
  
  public BreakTransliterator(String ID, UnicodeFilter filter) {
    this(ID, filter, null, " ");
  }
  
  public String getInsertion() {
    return this.insertion;
  }
  
  public void setInsertion(String insertion) {
    this.insertion = insertion;
  }
  
  public BreakIterator getBreakIterator() {
    if (this.bi == null)
      this.bi = BreakIterator.getWordInstance(new ULocale("th_TH")); 
    return this.bi;
  }
  
  public void setBreakIterator(BreakIterator bi) {
    this.bi = bi;
  }
  
  protected synchronized void handleTransliterate(Replaceable text, Transliterator.Position pos, boolean incremental) {
    this.boundaryCount = 0;
    int boundary = 0;
    getBreakIterator();
    this.bi.setText(new ReplaceableCharacterIterator(text, pos.start, pos.limit, pos.start));
    for (boundary = this.bi.first(); boundary != -1 && boundary < pos.limit; boundary = this.bi.next()) {
      if (boundary != 0) {
        int cp = UTF16.charAt(text, boundary - 1);
        int type = UCharacter.getType(cp);
        if ((1 << type & 0x1FE) != 0) {
          cp = UTF16.charAt(text, boundary);
          type = UCharacter.getType(cp);
          if ((1 << type & 0x1FE) != 0) {
            if (this.boundaryCount >= this.boundaries.length) {
              int[] temp = new int[this.boundaries.length * 2];
              System.arraycopy(this.boundaries, 0, temp, 0, this.boundaries.length);
              this.boundaries = temp;
            } 
            this.boundaries[this.boundaryCount++] = boundary;
          } 
        } 
      } 
    } 
    int delta = 0;
    int lastBoundary = 0;
    if (this.boundaryCount != 0) {
      delta = this.boundaryCount * this.insertion.length();
      lastBoundary = this.boundaries[this.boundaryCount - 1];
      while (this.boundaryCount > 0) {
        boundary = this.boundaries[--this.boundaryCount];
        text.replace(boundary, boundary, this.insertion);
      } 
    } 
    pos.contextLimit += delta;
    pos.limit += delta;
    pos.start = incremental ? (lastBoundary + delta) : pos.limit;
  }
  
  static void register() {
    Transliterator trans = new BreakTransliterator("Any-BreakInternal", null);
    Transliterator.registerInstance(trans, false);
  }
  
  static final class ReplaceableCharacterIterator implements CharacterIterator {
    private Replaceable text;
    
    private int begin;
    
    private int end;
    
    private int pos;
    
    public ReplaceableCharacterIterator(Replaceable text, int begin, int end, int pos) {
      if (text == null)
        throw new NullPointerException(); 
      this.text = text;
      if (begin < 0 || begin > end || end > text.length())
        throw new IllegalArgumentException("Invalid substring range"); 
      if (pos < begin || pos > end)
        throw new IllegalArgumentException("Invalid position"); 
      this.begin = begin;
      this.end = end;
      this.pos = pos;
    }
    
    public void setText(Replaceable text) {
      if (text == null)
        throw new NullPointerException(); 
      this.text = text;
      this.begin = 0;
      this.end = text.length();
      this.pos = 0;
    }
    
    public char first() {
      this.pos = this.begin;
      return current();
    }
    
    public char last() {
      if (this.end != this.begin) {
        this.pos = this.end - 1;
      } else {
        this.pos = this.end;
      } 
      return current();
    }
    
    public char setIndex(int p) {
      if (p < this.begin || p > this.end)
        throw new IllegalArgumentException("Invalid index"); 
      this.pos = p;
      return current();
    }
    
    public char current() {
      if (this.pos >= this.begin && this.pos < this.end)
        return this.text.charAt(this.pos); 
      return Character.MAX_VALUE;
    }
    
    public char next() {
      if (this.pos < this.end - 1) {
        this.pos++;
        return this.text.charAt(this.pos);
      } 
      this.pos = this.end;
      return Character.MAX_VALUE;
    }
    
    public char previous() {
      if (this.pos > this.begin) {
        this.pos--;
        return this.text.charAt(this.pos);
      } 
      return Character.MAX_VALUE;
    }
    
    public int getBeginIndex() {
      return this.begin;
    }
    
    public int getEndIndex() {
      return this.end;
    }
    
    public int getIndex() {
      return this.pos;
    }
    
    public boolean equals(Object obj) {
      if (this == obj)
        return true; 
      if (!(obj instanceof ReplaceableCharacterIterator))
        return false; 
      ReplaceableCharacterIterator that = (ReplaceableCharacterIterator)obj;
      if (hashCode() != that.hashCode())
        return false; 
      if (!this.text.equals(that.text))
        return false; 
      if (this.pos != that.pos || this.begin != that.begin || this.end != that.end)
        return false; 
      return true;
    }
    
    public int hashCode() {
      return this.text.hashCode() ^ this.pos ^ this.begin ^ this.end;
    }
    
    public Object clone() {
      try {
        ReplaceableCharacterIterator other = (ReplaceableCharacterIterator)super.clone();
        return other;
      } catch (CloneNotSupportedException e) {
        throw new IllegalStateException();
      } 
    }
  }
  
  public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
    UnicodeSet myFilter = getFilterAsUnicodeSet(inputFilter);
    if (myFilter.size() != 0)
      targetSet.addAll(this.insertion); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\BreakTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */