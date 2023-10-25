package com.ibm.icu.impl.data;

import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.UTF16;
import java.io.IOException;

public class TokenIterator {
  private ResourceReader reader;
  
  private String line;
  
  private StringBuffer buf;
  
  private boolean done;
  
  private int pos;
  
  private int lastpos;
  
  public TokenIterator(ResourceReader r) {
    this.reader = r;
    this.line = null;
    this.done = false;
    this.buf = new StringBuffer();
    this.pos = this.lastpos = -1;
  }
  
  public String next() throws IOException {
    if (this.done)
      return null; 
    while (true) {
      if (this.line == null) {
        this.line = this.reader.readLineSkippingComments();
        if (this.line == null) {
          this.done = true;
          return null;
        } 
        this.pos = 0;
      } 
      this.buf.setLength(0);
      this.lastpos = this.pos;
      this.pos = nextToken(this.pos);
      if (this.pos < 0) {
        this.line = null;
        continue;
      } 
      break;
    } 
    return this.buf.toString();
  }
  
  public int getLineNumber() {
    return this.reader.getLineNumber();
  }
  
  public String describePosition() {
    return this.reader.describePosition() + ':' + (this.lastpos + 1);
  }
  
  private int nextToken(int position) {
    position = PatternProps.skipWhiteSpace(this.line, position);
    if (position == this.line.length())
      return -1; 
    int startpos = position;
    char c = this.line.charAt(position++);
    char quote = Character.MIN_VALUE;
    switch (c) {
      case '"':
      case '\'':
        quote = c;
        break;
      case '#':
        return -1;
      default:
        this.buf.append(c);
        break;
    } 
    int[] posref = null;
    while (position < this.line.length()) {
      c = this.line.charAt(position);
      if (c == '\\') {
        if (posref == null)
          posref = new int[1]; 
        posref[0] = position + 1;
        int c32 = Utility.unescapeAt(this.line, posref);
        if (c32 < 0)
          throw new RuntimeException("Invalid escape at " + this.reader.describePosition() + ':' + position); 
        UTF16.append(this.buf, c32);
        position = posref[0];
        continue;
      } 
      if ((quote != '\000' && c == quote) || (quote == '\000' && PatternProps.isWhiteSpace(c)))
        return ++position; 
      if (quote == '\000' && c == '#')
        return position; 
      this.buf.append(c);
      position++;
    } 
    if (quote != '\000')
      throw new RuntimeException("Unterminated quote at " + this.reader.describePosition() + ':' + startpos); 
    return position;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\TokenIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */