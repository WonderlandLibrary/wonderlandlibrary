package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.CharacterIteration;
import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Stack;

class CjkBreakEngine implements LanguageBreakEngine {
  private static final UnicodeSet fHangulWordSet = new UnicodeSet();
  
  private static final UnicodeSet fHanWordSet = new UnicodeSet();
  
  private static final UnicodeSet fKatakanaWordSet = new UnicodeSet();
  
  private static final UnicodeSet fHiraganaWordSet = new UnicodeSet();
  
  private final UnicodeSet fWordSet;
  
  static {
    fHangulWordSet.applyPattern("[\\uac00-\\ud7a3]");
    fHanWordSet.applyPattern("[:Han:]");
    fKatakanaWordSet.applyPattern("[[:Katakana:]\\uff9e\\uff9f]");
    fHiraganaWordSet.applyPattern("[:Hiragana:]");
    fHangulWordSet.freeze();
    fHanWordSet.freeze();
    fKatakanaWordSet.freeze();
    fHiraganaWordSet.freeze();
  }
  
  private DictionaryMatcher fDictionary = null;
  
  private static final int kMaxKatakanaLength = 8;
  
  private static final int kMaxKatakanaGroupLength = 20;
  
  private static final int maxSnlp = 255;
  
  private static final int kint32max = 2147483647;
  
  public CjkBreakEngine(boolean korean) throws IOException {
    this.fDictionary = DictionaryData.loadDictionaryFor("Hira");
    if (korean) {
      this.fWordSet = fHangulWordSet;
    } else {
      this.fWordSet = new UnicodeSet();
      this.fWordSet.addAll(fHanWordSet);
      this.fWordSet.addAll(fKatakanaWordSet);
      this.fWordSet.addAll(fHiraganaWordSet);
      this.fWordSet.add("\\uff70\\u30fc");
    } 
  }
  
  public boolean handles(int c, int breakType) {
    return (breakType == 1 && this.fWordSet.contains(c));
  }
  
  private static int getKatakanaCost(int wordlength) {
    int[] katakanaCost = { 8192, 984, 408, 240, 204, 252, 300, 372, 480 };
    return (wordlength > 8) ? 8192 : katakanaCost[wordlength];
  }
  
  private static boolean isKatakana(int value) {
    return ((value >= 12449 && value <= 12542 && value != 12539) || (value >= 65382 && value <= 65439));
  }
  
  public int findBreaks(CharacterIterator inText, int startPos, int endPos, boolean reverse, int breakType, Stack<Integer> foundBreaks) {
    if (startPos >= endPos)
      return 0; 
    inText.setIndex(startPos);
    int inputLength = endPos - startPos;
    int[] charPositions = new int[inputLength + 1];
    StringBuffer s = new StringBuffer("");
    inText.setIndex(startPos);
    while (inText.getIndex() < endPos) {
      s.append(inText.current());
      inText.next();
    } 
    String prenormstr = s.toString();
    boolean isNormalized = (Normalizer.quickCheck(prenormstr, Normalizer.NFKC) == Normalizer.YES || Normalizer.isNormalized(prenormstr, Normalizer.NFKC, 0));
    CharacterIterator text = inText;
    int numChars = 0;
    if (isNormalized) {
      int index = 0;
      charPositions[0] = 0;
      while (index < prenormstr.length()) {
        int codepoint = prenormstr.codePointAt(index);
        index += Character.charCount(codepoint);
        numChars++;
        charPositions[numChars] = index;
      } 
    } else {
      String normStr = Normalizer.normalize(prenormstr, Normalizer.NFKC);
      text = new StringCharacterIterator(normStr);
      charPositions = new int[normStr.length() + 1];
      Normalizer normalizer = new Normalizer(prenormstr, Normalizer.NFKC, 0);
      int index = 0;
      charPositions[0] = 0;
      while (index < normalizer.endIndex()) {
        normalizer.next();
        numChars++;
        index = normalizer.getIndex();
        charPositions[numChars] = index;
      } 
    } 
    int[] bestSnlp = new int[numChars + 1];
    bestSnlp[0] = 0;
    for (int i = 1; i <= numChars; i++)
      bestSnlp[i] = Integer.MAX_VALUE; 
    int[] prev = new int[numChars + 1];
    for (int j = 0; j <= numChars; j++)
      prev[j] = -1; 
    int maxWordSize = 20;
    int[] values = new int[numChars];
    int[] lengths = new int[numChars];
    boolean is_prev_katakana = false;
    for (int k = 0; k < numChars; k++) {
      text.setIndex(k);
      if (bestSnlp[k] != Integer.MAX_VALUE) {
        int maxSearchLength = (k + 20 < numChars) ? 20 : (numChars - k);
        int[] count_ = new int[1];
        this.fDictionary.matches(text, maxSearchLength, lengths, count_, maxSearchLength, values);
        int count = count_[0];
        if ((count == 0 || lengths[0] != 1) && CharacterIteration.current32(text) != Integer.MAX_VALUE && !fHangulWordSet.contains(CharacterIteration.current32(text))) {
          values[count] = 255;
          lengths[count] = 1;
          count++;
        } 
        for (int n = 0; n < count; n++) {
          int newSnlp = bestSnlp[k] + values[n];
          if (newSnlp < bestSnlp[lengths[n] + k]) {
            bestSnlp[lengths[n] + k] = newSnlp;
            prev[lengths[n] + k] = k;
          } 
        } 
        text.setIndex(k);
        boolean is_katakana = isKatakana(CharacterIteration.current32(text));
        if (!is_prev_katakana && is_katakana) {
          int i1 = k + 1;
          CharacterIteration.next32(text);
          while (i1 < numChars && i1 - k < 20 && isKatakana(CharacterIteration.current32(text))) {
            CharacterIteration.next32(text);
            i1++;
          } 
          if (i1 - k < 20) {
            int newSnlp = bestSnlp[k] + getKatakanaCost(i1 - k);
            if (newSnlp < bestSnlp[i1]) {
              bestSnlp[i1] = newSnlp;
              prev[i1] = k;
            } 
          } 
        } 
        is_prev_katakana = is_katakana;
      } 
    } 
    int[] t_boundary = new int[numChars + 1];
    int numBreaks = 0;
    if (bestSnlp[numChars] == Integer.MAX_VALUE) {
      t_boundary[numBreaks] = numChars;
      numBreaks++;
    } else {
      int n;
      for (n = numChars; n > 0; n = prev[n]) {
        t_boundary[numBreaks] = n;
        numBreaks++;
      } 
      Assert.assrt((prev[t_boundary[numBreaks - 1]] == 0));
    } 
    if (foundBreaks.size() == 0 || ((Integer)foundBreaks.peek()).intValue() < startPos)
      t_boundary[numBreaks++] = 0; 
    for (int m = numBreaks - 1; m >= 0; m--) {
      int pos = charPositions[t_boundary[m]] + startPos;
      if (!foundBreaks.contains(Integer.valueOf(pos)) && pos != startPos)
        foundBreaks.push(Integer.valueOf(charPositions[t_boundary[m]] + startPos)); 
    } 
    if (!foundBreaks.empty() && ((Integer)foundBreaks.peek()).intValue() == endPos)
      foundBreaks.pop(); 
    if (!foundBreaks.empty())
      inText.setIndex(((Integer)foundBreaks.peek()).intValue()); 
    return 0;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\CjkBreakEngine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */