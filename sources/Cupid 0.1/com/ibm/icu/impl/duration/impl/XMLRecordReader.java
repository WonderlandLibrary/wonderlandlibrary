package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.lang.UCharacter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class XMLRecordReader implements RecordReader {
  private Reader r;
  
  private List<String> nameStack;
  
  private boolean atTag;
  
  private String tag;
  
  public XMLRecordReader(Reader r) {
    this.r = r;
    this.nameStack = new ArrayList<String>();
    if (getTag().startsWith("?xml"))
      advance(); 
    if (getTag().startsWith("!--"))
      advance(); 
  }
  
  public boolean open(String title) {
    if (getTag().equals(title)) {
      this.nameStack.add(title);
      advance();
      return true;
    } 
    return false;
  }
  
  public boolean close() {
    int ix = this.nameStack.size() - 1;
    String name = this.nameStack.get(ix);
    if (getTag().equals("/" + name)) {
      this.nameStack.remove(ix);
      advance();
      return true;
    } 
    return false;
  }
  
  public boolean bool(String name) {
    String s = string(name);
    if (s != null)
      return "true".equals(s); 
    return false;
  }
  
  public boolean[] boolArray(String name) {
    String[] sa = stringArray(name);
    if (sa != null) {
      boolean[] result = new boolean[sa.length];
      for (int i = 0; i < sa.length; i++)
        result[i] = "true".equals(sa[i]); 
      return result;
    } 
    return null;
  }
  
  public char character(String name) {
    String s = string(name);
    if (s != null)
      return s.charAt(0); 
    return Character.MAX_VALUE;
  }
  
  public char[] characterArray(String name) {
    String[] sa = stringArray(name);
    if (sa != null) {
      char[] result = new char[sa.length];
      for (int i = 0; i < sa.length; i++)
        result[i] = sa[i].charAt(0); 
      return result;
    } 
    return null;
  }
  
  public byte namedIndex(String name, String[] names) {
    String sa = string(name);
    if (sa != null)
      for (int i = 0; i < names.length; i++) {
        if (sa.equals(names[i]))
          return (byte)i; 
      }  
    return -1;
  }
  
  public byte[] namedIndexArray(String name, String[] names) {
    String[] sa = stringArray(name);
    if (sa != null) {
      byte[] result = new byte[sa.length];
      for (int i = 0; i < sa.length; i++) {
        String s = sa[i];
        int j = 0;
        while (true) {
          if (j < names.length) {
            if (names[j].equals(s)) {
              result[i] = (byte)j;
              break;
            } 
            j++;
            continue;
          } 
          result[i] = -1;
          break;
        } 
      } 
      return result;
    } 
    return null;
  }
  
  public String string(String name) {
    String result = readData();
    if (match(name) && match("/" + name))
      return result; 
    return null;
  }
  
  public String[] stringArray(String name) {
    if (match(name + "List")) {
      List<String> list = new ArrayList<String>();
      String s;
      while (null != (s = string(name))) {
        if ("Null".equals(s))
          s = null; 
        list.add(s);
      } 
      if (match("/" + name + "List"))
        return list.<String>toArray(new String[list.size()]); 
    } 
    return null;
  }
  
  public String[][] stringTable(String name) {
    if (match(name + "Table")) {
      List<String[]> list = (List)new ArrayList<String>();
      String[] sa;
      while (null != (sa = stringArray(name)))
        list.add(sa); 
      if (match("/" + name + "Table"))
        return list.<String[]>toArray(new String[list.size()][]); 
    } 
    return (String[][])null;
  }
  
  private boolean match(String target) {
    if (getTag().equals(target)) {
      advance();
      return true;
    } 
    return false;
  }
  
  private String getTag() {
    if (this.tag == null)
      this.tag = readNextTag(); 
    return this.tag;
  }
  
  private void advance() {
    this.tag = null;
  }
  
  private String readData() {
    StringBuilder sb = new StringBuilder();
    boolean inWhitespace = false;
    while (true) {
      int c = readChar();
      if (c == -1 || c == 60) {
        this.atTag = (c == 60);
        break;
      } 
      if (c == 38) {
        c = readChar();
        if (c == 35) {
          StringBuilder numBuf = new StringBuilder();
          int radix = 10;
          c = readChar();
          if (c == 120) {
            radix = 16;
            c = readChar();
          } 
          while (c != 59 && c != -1) {
            numBuf.append((char)c);
            c = readChar();
          } 
          try {
            int num = Integer.parseInt(numBuf.toString(), radix);
            c = (char)num;
          } catch (NumberFormatException ex) {
            System.err.println("numbuf: " + numBuf.toString() + " radix: " + radix);
            throw ex;
          } 
        } else {
          StringBuilder charBuf = new StringBuilder();
          while (c != 59 && c != -1) {
            charBuf.append((char)c);
            c = readChar();
          } 
          String charName = charBuf.toString();
          if (charName.equals("lt")) {
            c = 60;
          } else if (charName.equals("gt")) {
            c = 62;
          } else if (charName.equals("quot")) {
            c = 34;
          } else if (charName.equals("apos")) {
            c = 39;
          } else if (charName.equals("amp")) {
            c = 38;
          } else {
            System.err.println("unrecognized character entity: '" + charName + "'");
            continue;
          } 
        } 
      } 
      if (UCharacter.isWhitespace(c)) {
        if (inWhitespace)
          continue; 
        c = 32;
        inWhitespace = true;
      } else {
        inWhitespace = false;
      } 
      sb.append((char)c);
    } 
    return sb.toString();
  }
  
  private String readNextTag() {
    int c = 0;
    while (!this.atTag) {
      c = readChar();
      if (c == 60 || c == -1) {
        if (c == 60)
          this.atTag = true; 
        break;
      } 
      if (!UCharacter.isWhitespace(c)) {
        System.err.println("Unexpected non-whitespace character " + Integer.toHexString(c));
        break;
      } 
    } 
    if (this.atTag) {
      this.atTag = false;
      StringBuilder sb = new StringBuilder();
      while (true) {
        c = readChar();
        if (c == 62 || c == -1)
          break; 
        sb.append((char)c);
      } 
      return sb.toString();
    } 
    return null;
  }
  
  int readChar() {
    try {
      return this.r.read();
    } catch (IOException e) {
      return -1;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\impl\XMLRecordReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */