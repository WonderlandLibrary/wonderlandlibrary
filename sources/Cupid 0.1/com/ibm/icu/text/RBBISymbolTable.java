package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import java.text.ParsePosition;
import java.util.HashMap;

class RBBISymbolTable implements SymbolTable {
  String fRules;
  
  HashMap<String, RBBISymbolTableEntry> fHashTable;
  
  RBBIRuleScanner fRuleScanner;
  
  String ffffString;
  
  UnicodeSet fCachedSetLookup;
  
  static class RBBISymbolTableEntry {
    String key;
    
    RBBINode val;
  }
  
  RBBISymbolTable(RBBIRuleScanner rs, String rules) {
    this.fRules = rules;
    this.fRuleScanner = rs;
    this.fHashTable = new HashMap<String, RBBISymbolTableEntry>();
    this.ffffString = "￿";
  }
  
  public char[] lookup(String s) {
    String retString;
    RBBISymbolTableEntry el = this.fHashTable.get(s);
    if (el == null)
      return null; 
    RBBINode varRefNode = el.val;
    while (varRefNode.fLeftChild.fType == 2)
      varRefNode = varRefNode.fLeftChild; 
    RBBINode exprNode = varRefNode.fLeftChild;
    if (exprNode.fType == 0) {
      RBBINode usetNode = exprNode.fLeftChild;
      this.fCachedSetLookup = usetNode.fInputSet;
      retString = this.ffffString;
    } else {
      this.fRuleScanner.error(66063);
      retString = exprNode.fText;
      this.fCachedSetLookup = null;
    } 
    return retString.toCharArray();
  }
  
  public UnicodeMatcher lookupMatcher(int ch) {
    UnicodeSet retVal = null;
    if (ch == 65535) {
      retVal = this.fCachedSetLookup;
      this.fCachedSetLookup = null;
    } 
    return retVal;
  }
  
  public String parseReference(String text, ParsePosition pos, int limit) {
    int start = pos.getIndex();
    int i = start;
    String result = "";
    while (i < limit) {
      int c = UTF16.charAt(text, i);
      if ((i == start && !UCharacter.isUnicodeIdentifierStart(c)) || !UCharacter.isUnicodeIdentifierPart(c))
        break; 
      i += UTF16.getCharCount(c);
    } 
    if (i == start)
      return result; 
    pos.setIndex(i);
    result = text.substring(start, i);
    return result;
  }
  
  RBBINode lookupNode(String key) {
    RBBINode retNode = null;
    RBBISymbolTableEntry el = this.fHashTable.get(key);
    if (el != null)
      retNode = el.val; 
    return retNode;
  }
  
  void addEntry(String key, RBBINode val) {
    RBBISymbolTableEntry e = this.fHashTable.get(key);
    if (e != null) {
      this.fRuleScanner.error(66055);
      return;
    } 
    e = new RBBISymbolTableEntry();
    e.key = key;
    e.val = val;
    this.fHashTable.put(e.key, e);
  }
  
  void rbbiSymtablePrint() {
    System.out.print("Variable Definitions\nName               Node Val     String Val\n----------------------------------------------------------------------\n");
    RBBISymbolTableEntry[] syms = (RBBISymbolTableEntry[])this.fHashTable.values().toArray((Object[])new RBBISymbolTableEntry[0]);
    int i;
    for (i = 0; i < syms.length; i++) {
      RBBISymbolTableEntry s = syms[i];
      System.out.print("  " + s.key + "  ");
      System.out.print("  " + s.val + "  ");
      System.out.print(s.val.fLeftChild.fText);
      System.out.print("\n");
    } 
    System.out.println("\nParsed Variable Definitions\n");
    for (i = 0; i < syms.length; i++) {
      RBBISymbolTableEntry s = syms[i];
      System.out.print(s.key);
      s.val.fLeftChild.printTree(true);
      System.out.print("\n");
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\RBBISymbolTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */