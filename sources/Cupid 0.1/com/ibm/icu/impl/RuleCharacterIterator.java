package com.ibm.icu.impl;

import com.ibm.icu.text.SymbolTable;
import com.ibm.icu.text.UTF16;
import java.text.ParsePosition;

public class RuleCharacterIterator {
  private String text;
  
  private ParsePosition pos;
  
  private SymbolTable sym;
  
  private char[] buf;
  
  private int bufPos;
  
  private boolean isEscaped;
  
  public static final int DONE = -1;
  
  public static final int PARSE_VARIABLES = 1;
  
  public static final int PARSE_ESCAPES = 2;
  
  public static final int SKIP_WHITESPACE = 4;
  
  public RuleCharacterIterator(String text, SymbolTable sym, ParsePosition pos) {
    if (text == null || pos.getIndex() > text.length())
      throw new IllegalArgumentException(); 
    this.text = text;
    this.sym = sym;
    this.pos = pos;
    this.buf = null;
  }
  
  public boolean atEnd() {
    return (this.buf == null && this.pos.getIndex() == this.text.length());
  }
  
  public int next(int options) {
    int c = -1;
    this.isEscaped = false;
    while (true) {
      c = _current();
      _advance(UTF16.getCharCount(c));
      if (c == 36 && this.buf == null && (options & 0x1) != 0 && this.sym != null) {
        String name = this.sym.parseReference(this.text, this.pos, this.text.length());
        if (name == null)
          break; 
        this.bufPos = 0;
        this.buf = this.sym.lookup(name);
        if (this.buf == null)
          throw new IllegalArgumentException("Undefined variable: " + name); 
        if (this.buf.length == 0)
          this.buf = null; 
        continue;
      } 
      if ((options & 0x4) != 0 && PatternProps.isWhiteSpace(c))
        continue; 
      if (c == 92 && (options & 0x2) != 0) {
        int[] offset = { 0 };
        c = Utility.unescapeAt(lookahead(), offset);
        jumpahead(offset[0]);
        this.isEscaped = true;
        if (c < 0)
          throw new IllegalArgumentException("Invalid escape"); 
      } 
      break;
    } 
    return c;
  }
  
  public boolean isEscaped() {
    return this.isEscaped;
  }
  
  public boolean inVariable() {
    return (this.buf != null);
  }
  
  public Object getPos(Object p) {
    if (p == null)
      return new Object[] { this.buf, { this.pos.getIndex(), this.bufPos } }; 
    Object[] a = (Object[])p;
    a[0] = this.buf;
    int[] v = (int[])a[1];
    v[0] = this.pos.getIndex();
    v[1] = this.bufPos;
    return p;
  }
  
  public void setPos(Object p) {
    Object[] a = (Object[])p;
    this.buf = (char[])a[0];
    int[] v = (int[])a[1];
    this.pos.setIndex(v[0]);
    this.bufPos = v[1];
  }
  
  public void skipIgnored(int options) {
    if ((options & 0x4) != 0)
      while (true) {
        int a = _current();
        if (!PatternProps.isWhiteSpace(a))
          break; 
        _advance(UTF16.getCharCount(a));
      }  
  }
  
  public String lookahead() {
    if (this.buf != null)
      return new String(this.buf, this.bufPos, this.buf.length - this.bufPos); 
    return this.text.substring(this.pos.getIndex());
  }
  
  public void jumpahead(int count) {
    if (count < 0)
      throw new IllegalArgumentException(); 
    if (this.buf != null) {
      this.bufPos += count;
      if (this.bufPos > this.buf.length)
        throw new IllegalArgumentException(); 
      if (this.bufPos == this.buf.length)
        this.buf = null; 
    } else {
      int i = this.pos.getIndex() + count;
      this.pos.setIndex(i);
      if (i > this.text.length())
        throw new IllegalArgumentException(); 
    } 
  }
  
  public String toString() {
    int b = this.pos.getIndex();
    return this.text.substring(0, b) + '|' + this.text.substring(b);
  }
  
  private int _current() {
    if (this.buf != null)
      return UTF16.charAt(this.buf, 0, this.buf.length, this.bufPos); 
    int i = this.pos.getIndex();
    return (i < this.text.length()) ? UTF16.charAt(this.text, i) : -1;
  }
  
  private void _advance(int count) {
    if (this.buf != null) {
      this.bufPos += count;
      if (this.bufPos == this.buf.length)
        this.buf = null; 
    } else {
      this.pos.setIndex(this.pos.getIndex() + count);
      if (this.pos.getIndex() > this.text.length())
        this.pos.setIndex(this.text.length()); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\RuleCharacterIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */