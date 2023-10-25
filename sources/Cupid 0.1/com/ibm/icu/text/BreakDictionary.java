package com.ibm.icu.text;

import com.ibm.icu.util.CompactByteArray;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

class BreakDictionary {
  static void writeToFile(String inFile, String outFile) throws FileNotFoundException, UnsupportedEncodingException, IOException {
    BreakDictionary dictionary = new BreakDictionary(new FileInputStream(inFile));
    PrintWriter out = null;
    if (outFile != null)
      out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UnicodeLittle")); 
    dictionary.printWordList("", 0, out);
    if (out != null)
      out.close(); 
  }
  
  void printWordList(String partialWord, int state, PrintWriter out) throws IOException {
    if (state == 65535) {
      System.out.println(partialWord);
      if (out != null)
        out.println(partialWord); 
    } else {
      for (int i = 0; i < this.numCols; i++) {
        int newState = at(state, i) & 0xFFFF;
        if (newState != 0) {
          char newChar = this.reverseColumnMap[i];
          String newPartialWord = partialWord;
          if (newChar != '\000')
            newPartialWord = newPartialWord + newChar; 
          printWordList(newPartialWord, newState, out);
        } 
      } 
    } 
  }
  
  private char[] reverseColumnMap = null;
  
  private CompactByteArray columnMap = null;
  
  private int numCols;
  
  private short[] table = null;
  
  private short[] rowIndex = null;
  
  private int[] rowIndexFlags = null;
  
  private short[] rowIndexFlagsIndex = null;
  
  private byte[] rowIndexShifts = null;
  
  BreakDictionary(InputStream dictionaryStream) throws IOException {
    readDictionaryFile(new DataInputStream(dictionaryStream));
  }
  
  void readDictionaryFile(DataInputStream in) throws IOException {
    in.readInt();
    int l = in.readInt();
    char[] temp = new char[l];
    for (int i = 0; i < temp.length; i++)
      temp[i] = (char)in.readShort(); 
    l = in.readInt();
    byte[] temp2 = new byte[l];
    int j;
    for (j = 0; j < temp2.length; j++)
      temp2[j] = in.readByte(); 
    this.columnMap = new CompactByteArray(temp, temp2);
    this.numCols = in.readInt();
    in.readInt();
    l = in.readInt();
    this.rowIndex = new short[l];
    for (j = 0; j < this.rowIndex.length; j++)
      this.rowIndex[j] = in.readShort(); 
    l = in.readInt();
    this.rowIndexFlagsIndex = new short[l];
    for (j = 0; j < this.rowIndexFlagsIndex.length; j++)
      this.rowIndexFlagsIndex[j] = in.readShort(); 
    l = in.readInt();
    this.rowIndexFlags = new int[l];
    for (j = 0; j < this.rowIndexFlags.length; j++)
      this.rowIndexFlags[j] = in.readInt(); 
    l = in.readInt();
    this.rowIndexShifts = new byte[l];
    for (j = 0; j < this.rowIndexShifts.length; j++)
      this.rowIndexShifts[j] = in.readByte(); 
    l = in.readInt();
    this.table = new short[l];
    for (j = 0; j < this.table.length; j++)
      this.table[j] = in.readShort(); 
    this.reverseColumnMap = new char[this.numCols];
    char c;
    for (c = Character.MIN_VALUE; c < Character.MAX_VALUE; c = (char)(c + 1)) {
      int col = this.columnMap.elementAt(c);
      if (col != 0)
        this.reverseColumnMap[col] = c; 
    } 
    in.close();
  }
  
  final short at(int row, char ch) {
    int col = this.columnMap.elementAt(ch);
    return at(row, col);
  }
  
  final short at(int row, int col) {
    if (cellIsPopulated(row, col))
      return internalAt(this.rowIndex[row], col + this.rowIndexShifts[row]); 
    return 0;
  }
  
  private final boolean cellIsPopulated(int row, int col) {
    if (this.rowIndexFlagsIndex[row] < 0)
      return (col == -this.rowIndexFlagsIndex[row]); 
    int flags = this.rowIndexFlags[this.rowIndexFlagsIndex[row] + (col >> 5)];
    return ((flags & 1 << (col & 0x1F)) != 0);
  }
  
  private final short internalAt(int row, int col) {
    return this.table[row * this.numCols + col];
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\BreakDictionary.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */