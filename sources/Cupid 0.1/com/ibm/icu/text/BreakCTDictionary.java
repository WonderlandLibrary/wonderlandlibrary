package com.ibm.icu.text;

import com.ibm.icu.impl.ICUBinary;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.CharacterIterator;

class BreakCTDictionary {
  private CompactTrieHeader fData;
  
  private CompactTrieNodes[] nodes;
  
  static class CompactTrieHeader {
    int size = 0;
    
    int magic = 0;
    
    int nodeCount = 0;
    
    int root = 0;
    
    int[] offset = null;
  }
  
  static final class CompactTrieNodeFlags {
    static final int kVerticalNode = 4096;
    
    static final int kParentEndsWord = 8192;
    
    static final int kReservedFlag1 = 16384;
    
    static final int kReservedFlag2 = 32768;
    
    static final int kCountMask = 4095;
    
    static final int kFlagMask = 61440;
  }
  
  static class CompactTrieHorizontalNode {
    char ch;
    
    int equal;
    
    CompactTrieHorizontalNode(char newCh, int newEqual) {
      this.ch = newCh;
      this.equal = newEqual;
    }
  }
  
  static class CompactTrieVerticalNode {
    int equal = 0;
    
    char[] chars = null;
  }
  
  private CompactTrieNodes getCompactTrieNode(int node) {
    return this.nodes[node];
  }
  
  static class CompactTrieNodes {
    short flagscount = 0;
    
    BreakCTDictionary.CompactTrieHorizontalNode[] hnode = null;
    
    BreakCTDictionary.CompactTrieVerticalNode vnode = null;
  }
  
  public BreakCTDictionary(InputStream is) throws IOException {
    ICUBinary.readHeader(is, DATA_FORMAT_ID, null);
    DataInputStream in = new DataInputStream(is);
    this.fData = new CompactTrieHeader();
    this.fData.size = in.readInt();
    this.fData.magic = in.readInt();
    this.fData.nodeCount = in.readShort();
    this.fData.root = in.readShort();
    loadBreakCTDictionary(in);
  }
  
  private void loadBreakCTDictionary(DataInputStream in) throws IOException {
    for (int i = 0; i < this.fData.nodeCount; i++)
      in.readInt(); 
    this.nodes = new CompactTrieNodes[this.fData.nodeCount];
    this.nodes[0] = new CompactTrieNodes();
    for (int j = 1; j < this.fData.nodeCount; j++) {
      this.nodes[j] = new CompactTrieNodes();
      (this.nodes[j]).flagscount = in.readShort();
      int count = (this.nodes[j]).flagscount & 0xFFF;
      if (count != 0) {
        boolean isVerticalNode = (((this.nodes[j]).flagscount & 0x1000) != 0);
        if (isVerticalNode) {
          (this.nodes[j]).vnode = new CompactTrieVerticalNode();
          (this.nodes[j]).vnode.equal = in.readShort();
          (this.nodes[j]).vnode.chars = new char[count];
          for (int l = 0; l < count; l++)
            (this.nodes[j]).vnode.chars[l] = in.readChar(); 
        } else {
          (this.nodes[j]).hnode = new CompactTrieHorizontalNode[count];
          for (int n = 0; n < count; n++)
            (this.nodes[j]).hnode[n] = new CompactTrieHorizontalNode(in.readChar(), in.readShort()); 
        } 
      } 
    } 
  }
  
  public int matches(CharacterIterator text, int maxLength, int[] lengths, int[] count, int limit) {
    CompactTrieNodes node = getCompactTrieNode(this.fData.root);
    int mycount = 0;
    char uc = text.current();
    int i = 0;
    boolean exitFlag = false;
    while (node != null) {
      if (limit > 0 && (node.flagscount & 0x2000) != 0) {
        lengths[mycount++] = i;
        limit--;
      } 
      if (i >= maxLength)
        break; 
      int nodeCount = node.flagscount & 0xFFF;
      if (nodeCount == 0)
        break; 
      if ((node.flagscount & 0x1000) != 0) {
        CompactTrieVerticalNode vnode = node.vnode;
        for (int j = 0; j < nodeCount && i < maxLength; j++) {
          if (uc != vnode.chars[j]) {
            exitFlag = true;
            break;
          } 
          text.next();
          uc = text.current();
          i++;
        } 
        if (exitFlag)
          break; 
        node = getCompactTrieNode(vnode.equal);
        continue;
      } 
      CompactTrieHorizontalNode[] hnode = node.hnode;
      int low = 0;
      int high = nodeCount - 1;
      node = null;
      while (high >= low) {
        int middle = high + low >>> 1;
        if (uc == (hnode[middle]).ch) {
          node = getCompactTrieNode((hnode[middle]).equal);
          text.next();
          uc = text.current();
          i++;
          break;
        } 
        if (uc < (hnode[middle]).ch) {
          high = middle - 1;
          continue;
        } 
        low = middle + 1;
      } 
    } 
    count[0] = mycount;
    return i;
  }
  
  private static final byte[] DATA_FORMAT_ID = new byte[] { 84, 114, 68, 99 };
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\BreakCTDictionary.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */