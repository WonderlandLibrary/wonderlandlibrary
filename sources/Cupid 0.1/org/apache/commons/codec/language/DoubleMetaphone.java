package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class DoubleMetaphone implements StringEncoder {
  private static final String VOWELS = "AEIOUY";
  
  private static final String[] SILENT_START = new String[] { "GN", "KN", "PN", "WR", "PS" };
  
  private static final String[] L_R_N_M_B_H_F_V_W_SPACE = new String[] { "L", "R", "N", "M", "B", "H", "F", "V", "W", " " };
  
  private static final String[] ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER = new String[] { 
      "ES", "EP", "EB", "EL", "EY", "IB", "IL", "IN", "IE", "EI", 
      "ER" };
  
  private static final String[] L_T_K_S_N_M_B_Z = new String[] { "L", "T", "K", "S", "N", "M", "B", "Z" };
  
  private int maxCodeLen = 4;
  
  public String doubleMetaphone(String value) {
    return doubleMetaphone(value, false);
  }
  
  public String doubleMetaphone(String value, boolean alternate) {
    value = cleanInput(value);
    if (value == null)
      return null; 
    boolean slavoGermanic = isSlavoGermanic(value);
    int index = isSilentStart(value) ? 1 : 0;
    DoubleMetaphoneResult result = new DoubleMetaphoneResult(getMaxCodeLen());
    while (!result.isComplete() && index <= value.length() - 1) {
      switch (value.charAt(index)) {
        case 'A':
        case 'E':
        case 'I':
        case 'O':
        case 'U':
        case 'Y':
          index = handleAEIOUY(result, index);
          continue;
        case 'B':
          result.append('P');
          index = (charAt(value, index + 1) == 'B') ? (index + 2) : (index + 1);
          continue;
        case 'Ç':
          result.append('S');
          index++;
          continue;
        case 'C':
          index = handleC(value, result, index);
          continue;
        case 'D':
          index = handleD(value, result, index);
          continue;
        case 'F':
          result.append('F');
          index = (charAt(value, index + 1) == 'F') ? (index + 2) : (index + 1);
          continue;
        case 'G':
          index = handleG(value, result, index, slavoGermanic);
          continue;
        case 'H':
          index = handleH(value, result, index);
          continue;
        case 'J':
          index = handleJ(value, result, index, slavoGermanic);
          continue;
        case 'K':
          result.append('K');
          index = (charAt(value, index + 1) == 'K') ? (index + 2) : (index + 1);
          continue;
        case 'L':
          index = handleL(value, result, index);
          continue;
        case 'M':
          result.append('M');
          index = conditionM0(value, index) ? (index + 2) : (index + 1);
          continue;
        case 'N':
          result.append('N');
          index = (charAt(value, index + 1) == 'N') ? (index + 2) : (index + 1);
          continue;
        case 'Ñ':
          result.append('N');
          index++;
          continue;
        case 'P':
          index = handleP(value, result, index);
          continue;
        case 'Q':
          result.append('K');
          index = (charAt(value, index + 1) == 'Q') ? (index + 2) : (index + 1);
          continue;
        case 'R':
          index = handleR(value, result, index, slavoGermanic);
          continue;
        case 'S':
          index = handleS(value, result, index, slavoGermanic);
          continue;
        case 'T':
          index = handleT(value, result, index);
          continue;
        case 'V':
          result.append('F');
          index = (charAt(value, index + 1) == 'V') ? (index + 2) : (index + 1);
          continue;
        case 'W':
          index = handleW(value, result, index);
          continue;
        case 'X':
          index = handleX(value, result, index);
          continue;
        case 'Z':
          index = handleZ(value, result, index, slavoGermanic);
          continue;
      } 
      index++;
    } 
    return alternate ? result.getAlternate() : result.getPrimary();
  }
  
  public Object encode(Object obj) throws EncoderException {
    if (!(obj instanceof String))
      throw new EncoderException("DoubleMetaphone encode parameter is not of type String"); 
    return doubleMetaphone((String)obj);
  }
  
  public String encode(String value) {
    return doubleMetaphone(value);
  }
  
  public boolean isDoubleMetaphoneEqual(String value1, String value2) {
    return isDoubleMetaphoneEqual(value1, value2, false);
  }
  
  public boolean isDoubleMetaphoneEqual(String value1, String value2, boolean alternate) {
    return doubleMetaphone(value1, alternate).equals(doubleMetaphone(value2, alternate));
  }
  
  public int getMaxCodeLen() {
    return this.maxCodeLen;
  }
  
  public void setMaxCodeLen(int maxCodeLen) {
    this.maxCodeLen = maxCodeLen;
  }
  
  private int handleAEIOUY(DoubleMetaphoneResult result, int index) {
    if (index == 0)
      result.append('A'); 
    return index + 1;
  }
  
  private int handleC(String value, DoubleMetaphoneResult result, int index) {
    if (conditionC0(value, index)) {
      result.append('K');
      index += 2;
    } else if (index == 0 && contains(value, index, 6, new String[] { "CAESAR" })) {
      result.append('S');
      index += 2;
    } else if (contains(value, index, 2, new String[] { "CH" })) {
      index = handleCH(value, result, index);
    } else if (contains(value, index, 2, new String[] { "CZ" }) && !contains(value, index - 2, 4, new String[] { "WICZ" })) {
      result.append('S', 'X');
      index += 2;
    } else if (contains(value, index + 1, 3, new String[] { "CIA" })) {
      result.append('X');
      index += 3;
    } else {
      if (contains(value, index, 2, new String[] { "CC" }) && (index != 1 || charAt(value, 0) != 'M'))
        return handleCC(value, result, index); 
      if (contains(value, index, 2, new String[] { "CK", "CG", "CQ" })) {
        result.append('K');
        index += 2;
      } else if (contains(value, index, 2, new String[] { "CI", "CE", "CY" })) {
        if (contains(value, index, 3, new String[] { "CIO", "CIE", "CIA" })) {
          result.append('S', 'X');
        } else {
          result.append('S');
        } 
        index += 2;
      } else {
        result.append('K');
        if (contains(value, index + 1, 2, new String[] { " C", " Q", " G" })) {
          index += 3;
        } else if (contains(value, index + 1, 1, new String[] { "C", "K", "Q" }) && !contains(value, index + 1, 2, new String[] { "CE", "CI" })) {
          index += 2;
        } else {
          index++;
        } 
      } 
    } 
    return index;
  }
  
  private int handleCC(String value, DoubleMetaphoneResult result, int index) {
    if (contains(value, index + 2, 1, new String[] { "I", "E", "H" }) && !contains(value, index + 2, 2, new String[] { "HU" })) {
      if ((index == 1 && charAt(value, index - 1) == 'A') || contains(value, index - 1, 5, new String[] { "UCCEE", "UCCES" })) {
        result.append("KS");
      } else {
        result.append('X');
      } 
      index += 3;
    } else {
      result.append('K');
      index += 2;
    } 
    return index;
  }
  
  private int handleCH(String value, DoubleMetaphoneResult result, int index) {
    if (index > 0 && contains(value, index, 4, new String[] { "CHAE" })) {
      result.append('K', 'X');
      return index + 2;
    } 
    if (conditionCH0(value, index)) {
      result.append('K');
      return index + 2;
    } 
    if (conditionCH1(value, index)) {
      result.append('K');
      return index + 2;
    } 
    if (index > 0) {
      if (contains(value, 0, 2, new String[] { "MC" })) {
        result.append('K');
      } else {
        result.append('X', 'K');
      } 
    } else {
      result.append('X');
    } 
    return index + 2;
  }
  
  private int handleD(String value, DoubleMetaphoneResult result, int index) {
    if (contains(value, index, 2, new String[] { "DG" })) {
      if (contains(value, index + 2, 1, new String[] { "I", "E", "Y" })) {
        result.append('J');
        index += 3;
      } else {
        result.append("TK");
        index += 2;
      } 
    } else if (contains(value, index, 2, new String[] { "DT", "DD" })) {
      result.append('T');
      index += 2;
    } else {
      result.append('T');
      index++;
    } 
    return index;
  }
  
  private int handleG(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
    if (charAt(value, index + 1) == 'H') {
      index = handleGH(value, result, index);
    } else if (charAt(value, index + 1) == 'N') {
      if (index == 1 && isVowel(charAt(value, 0)) && !slavoGermanic) {
        result.append("KN", "N");
      } else if (!contains(value, index + 2, 2, new String[] { "EY" }) && charAt(value, index + 1) != 'Y' && !slavoGermanic) {
        result.append("N", "KN");
      } else {
        result.append("KN");
      } 
      index += 2;
    } else if (contains(value, index + 1, 2, new String[] { "LI" }) && !slavoGermanic) {
      result.append("KL", "L");
      index += 2;
    } else if (index == 0 && (charAt(value, index + 1) == 'Y' || contains(value, index + 1, 2, ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER))) {
      result.append('K', 'J');
      index += 2;
    } else if ((contains(value, index + 1, 2, new String[] { "ER" }) || charAt(value, index + 1) == 'Y') && !contains(value, 0, 6, new String[] { "DANGER", "RANGER", "MANGER" }) && !contains(value, index - 1, 1, new String[] { "E", "I" }) && !contains(value, index - 1, 3, new String[] { "RGY", "OGY" })) {
      result.append('K', 'J');
      index += 2;
    } else if (contains(value, index + 1, 1, new String[] { "E", "I", "Y" }) || contains(value, index - 1, 4, new String[] { "AGGI", "OGGI" })) {
      if (contains(value, 0, 4, new String[] { "VAN ", "VON " }) || contains(value, 0, 3, new String[] { "SCH" }) || contains(value, index + 1, 2, new String[] { "ET" })) {
        result.append('K');
      } else if (contains(value, index + 1, 3, new String[] { "IER" })) {
        result.append('J');
      } else {
        result.append('J', 'K');
      } 
      index += 2;
    } else if (charAt(value, index + 1) == 'G') {
      index += 2;
      result.append('K');
    } else {
      index++;
      result.append('K');
    } 
    return index;
  }
  
  private int handleGH(String value, DoubleMetaphoneResult result, int index) {
    if (index > 0 && !isVowel(charAt(value, index - 1))) {
      result.append('K');
      index += 2;
    } else if (index == 0) {
      if (charAt(value, index + 2) == 'I') {
        result.append('J');
      } else {
        result.append('K');
      } 
      index += 2;
    } else if ((index > 1 && contains(value, index - 2, 1, new String[] { "B", "H", "D" })) || (index > 2 && contains(value, index - 3, 1, new String[] { "B", "H", "D" })) || (index > 3 && contains(value, index - 4, 1, new String[] { "B", "H" }))) {
      index += 2;
    } else {
      if (index > 2 && charAt(value, index - 1) == 'U' && contains(value, index - 3, 1, new String[] { "C", "G", "L", "R", "T" })) {
        result.append('F');
      } else if (index > 0 && charAt(value, index - 1) != 'I') {
        result.append('K');
      } 
      index += 2;
    } 
    return index;
  }
  
  private int handleH(String value, DoubleMetaphoneResult result, int index) {
    if ((index == 0 || isVowel(charAt(value, index - 1))) && isVowel(charAt(value, index + 1))) {
      result.append('H');
      index += 2;
    } else {
      index++;
    } 
    return index;
  }
  
  private int handleJ(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
    if (contains(value, index, 4, new String[] { "JOSE" }) || contains(value, 0, 4, new String[] { "SAN " })) {
      if ((index == 0 && charAt(value, index + 4) == ' ') || value.length() == 4 || contains(value, 0, 4, new String[] { "SAN " })) {
        result.append('H');
      } else {
        result.append('J', 'H');
      } 
      index++;
    } else {
      if (index == 0 && !contains(value, index, 4, new String[] { "JOSE" })) {
        result.append('J', 'A');
      } else if (isVowel(charAt(value, index - 1)) && !slavoGermanic && (charAt(value, index + 1) == 'A' || charAt(value, index + 1) == 'O')) {
        result.append('J', 'H');
      } else if (index == value.length() - 1) {
        result.append('J', ' ');
      } else if (!contains(value, index + 1, 1, L_T_K_S_N_M_B_Z) && !contains(value, index - 1, 1, new String[] { "S", "K", "L" })) {
        result.append('J');
      } 
      if (charAt(value, index + 1) == 'J') {
        index += 2;
      } else {
        index++;
      } 
    } 
    return index;
  }
  
  private int handleL(String value, DoubleMetaphoneResult result, int index) {
    if (charAt(value, index + 1) == 'L') {
      if (conditionL0(value, index)) {
        result.appendPrimary('L');
      } else {
        result.append('L');
      } 
      index += 2;
    } else {
      index++;
      result.append('L');
    } 
    return index;
  }
  
  private int handleP(String value, DoubleMetaphoneResult result, int index) {
    if (charAt(value, index + 1) == 'H') {
      result.append('F');
      index += 2;
    } else {
      result.append('P');
      index = contains(value, index + 1, 1, new String[] { "P", "B" }) ? (index + 2) : (index + 1);
    } 
    return index;
  }
  
  private int handleR(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
    if (index == value.length() - 1 && !slavoGermanic && contains(value, index - 2, 2, new String[] { "IE" }) && !contains(value, index - 4, 2, new String[] { "ME", "MA" })) {
      result.appendAlternate('R');
    } else {
      result.append('R');
    } 
    return (charAt(value, index + 1) == 'R') ? (index + 2) : (index + 1);
  }
  
  private int handleS(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
    if (contains(value, index - 1, 3, new String[] { "ISL", "YSL" })) {
      index++;
    } else if (index == 0 && contains(value, index, 5, new String[] { "SUGAR" })) {
      result.append('X', 'S');
      index++;
    } else if (contains(value, index, 2, new String[] { "SH" })) {
      if (contains(value, index + 1, 4, new String[] { "HEIM", "HOEK", "HOLM", "HOLZ" })) {
        result.append('S');
      } else {
        result.append('X');
      } 
      index += 2;
    } else if (contains(value, index, 3, new String[] { "SIO", "SIA" }) || contains(value, index, 4, new String[] { "SIAN" })) {
      if (slavoGermanic) {
        result.append('S');
      } else {
        result.append('S', 'X');
      } 
      index += 3;
    } else if ((index == 0 && contains(value, index + 1, 1, new String[] { "M", "N", "L", "W" })) || contains(value, index + 1, 1, new String[] { "Z" })) {
      result.append('S', 'X');
      index = contains(value, index + 1, 1, new String[] { "Z" }) ? (index + 2) : (index + 1);
    } else if (contains(value, index, 2, new String[] { "SC" })) {
      index = handleSC(value, result, index);
    } else {
      if (index == value.length() - 1 && contains(value, index - 2, 2, new String[] { "AI", "OI" })) {
        result.appendAlternate('S');
      } else {
        result.append('S');
      } 
      index = contains(value, index + 1, 1, new String[] { "S", "Z" }) ? (index + 2) : (index + 1);
    } 
    return index;
  }
  
  private int handleSC(String value, DoubleMetaphoneResult result, int index) {
    if (charAt(value, index + 2) == 'H') {
      if (contains(value, index + 3, 2, new String[] { "OO", "ER", "EN", "UY", "ED", "EM" })) {
        if (contains(value, index + 3, 2, new String[] { "ER", "EN" })) {
          result.append("X", "SK");
        } else {
          result.append("SK");
        } 
      } else if (index == 0 && !isVowel(charAt(value, 3)) && charAt(value, 3) != 'W') {
        result.append('X', 'S');
      } else {
        result.append('X');
      } 
    } else if (contains(value, index + 2, 1, new String[] { "I", "E", "Y" })) {
      result.append('S');
    } else {
      result.append("SK");
    } 
    return index + 3;
  }
  
  private int handleT(String value, DoubleMetaphoneResult result, int index) {
    if (contains(value, index, 4, new String[] { "TION" })) {
      result.append('X');
      index += 3;
    } else if (contains(value, index, 3, new String[] { "TIA", "TCH" })) {
      result.append('X');
      index += 3;
    } else if (contains(value, index, 2, new String[] { "TH" }) || contains(value, index, 3, new String[] { "TTH" })) {
      if (contains(value, index + 2, 2, new String[] { "OM", "AM" }) || contains(value, 0, 4, new String[] { "VAN ", "VON " }) || contains(value, 0, 3, new String[] { "SCH" })) {
        result.append('T');
      } else {
        result.append('0', 'T');
      } 
      index += 2;
    } else {
      result.append('T');
      index = contains(value, index + 1, 1, new String[] { "T", "D" }) ? (index + 2) : (index + 1);
    } 
    return index;
  }
  
  private int handleW(String value, DoubleMetaphoneResult result, int index) {
    if (contains(value, index, 2, new String[] { "WR" })) {
      result.append('R');
      index += 2;
    } else if (index == 0 && (isVowel(charAt(value, index + 1)) || contains(value, index, 2, new String[] { "WH" }))) {
      if (isVowel(charAt(value, index + 1))) {
        result.append('A', 'F');
      } else {
        result.append('A');
      } 
      index++;
    } else if ((index == value.length() - 1 && isVowel(charAt(value, index - 1))) || contains(value, index - 1, 5, new String[] { "EWSKI", "EWSKY", "OWSKI", "OWSKY" }) || contains(value, 0, 3, new String[] { "SCH" })) {
      result.appendAlternate('F');
      index++;
    } else if (contains(value, index, 4, new String[] { "WICZ", "WITZ" })) {
      result.append("TS", "FX");
      index += 4;
    } else {
      index++;
    } 
    return index;
  }
  
  private int handleX(String value, DoubleMetaphoneResult result, int index) {
    if (index == 0) {
      result.append('S');
      index++;
    } else {
      if (index != value.length() - 1 || (!contains(value, index - 3, 3, new String[] { "IAU", "EAU" }) && !contains(value, index - 2, 2, new String[] { "AU", "OU" })))
        result.append("KS"); 
      index = contains(value, index + 1, 1, new String[] { "C", "X" }) ? (index + 2) : (index + 1);
    } 
    return index;
  }
  
  private int handleZ(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
    if (charAt(value, index + 1) == 'H') {
      result.append('J');
      index += 2;
    } else {
      if (contains(value, index + 1, 2, new String[] { "ZO", "ZI", "ZA" }) || (slavoGermanic && index > 0 && charAt(value, index - 1) != 'T')) {
        result.append("S", "TS");
      } else {
        result.append('S');
      } 
      index = (charAt(value, index + 1) == 'Z') ? (index + 2) : (index + 1);
    } 
    return index;
  }
  
  private boolean conditionC0(String value, int index) {
    if (contains(value, index, 4, new String[] { "CHIA" }))
      return true; 
    if (index <= 1)
      return false; 
    if (isVowel(charAt(value, index - 2)))
      return false; 
    if (!contains(value, index - 1, 3, new String[] { "ACH" }))
      return false; 
    char c = charAt(value, index + 2);
    return ((c != 'I' && c != 'E') || contains(value, index - 2, 6, new String[] { "BACHER", "MACHER" }));
  }
  
  private boolean conditionCH0(String value, int index) {
    if (index != 0)
      return false; 
    if (!contains(value, index + 1, 5, new String[] { "HARAC", "HARIS" }) && !contains(value, index + 1, 3, new String[] { "HOR", "HYM", "HIA", "HEM" }))
      return false; 
    if (contains(value, 0, 5, new String[] { "CHORE" }))
      return false; 
    return true;
  }
  
  private boolean conditionCH1(String value, int index) {
    return (contains(value, 0, 4, new String[] { "VAN ", "VON " }) || contains(value, 0, 3, new String[] { "SCH" }) || contains(value, index - 2, 6, new String[] { "ORCHES", "ARCHIT", "ORCHID" }) || contains(value, index + 2, 1, new String[] { "T", "S" }) || ((contains(value, index - 1, 1, new String[] { "A", "O", "U", "E" }) || index == 0) && (contains(value, index + 2, 1, L_R_N_M_B_H_F_V_W_SPACE) || index + 1 == value.length() - 1)));
  }
  
  private boolean conditionL0(String value, int index) {
    if (index == value.length() - 3 && contains(value, index - 1, 4, new String[] { "ILLO", "ILLA", "ALLE" }))
      return true; 
    if ((contains(value, value.length() - 2, 2, new String[] { "AS", "OS" }) || contains(value, value.length() - 1, 1, new String[] { "A", "O" })) && contains(value, index - 1, 4, new String[] { "ALLE" }))
      return true; 
    return false;
  }
  
  private boolean conditionM0(String value, int index) {
    if (charAt(value, index + 1) == 'M')
      return true; 
    return (contains(value, index - 1, 3, new String[] { "UMB" }) && (index + 1 == value.length() - 1 || contains(value, index + 2, 2, new String[] { "ER" })));
  }
  
  private boolean isSlavoGermanic(String value) {
    return (value.indexOf('W') > -1 || value.indexOf('K') > -1 || value.indexOf("CZ") > -1 || value.indexOf("WITZ") > -1);
  }
  
  private boolean isVowel(char ch) {
    return ("AEIOUY".indexOf(ch) != -1);
  }
  
  private boolean isSilentStart(String value) {
    boolean result = false;
    for (String element : SILENT_START) {
      if (value.startsWith(element)) {
        result = true;
        break;
      } 
    } 
    return result;
  }
  
  private String cleanInput(String input) {
    if (input == null)
      return null; 
    input = input.trim();
    if (input.length() == 0)
      return null; 
    return input.toUpperCase(Locale.ENGLISH);
  }
  
  protected char charAt(String value, int index) {
    if (index < 0 || index >= value.length())
      return Character.MIN_VALUE; 
    return value.charAt(index);
  }
  
  protected static boolean contains(String value, int start, int length, String... criteria) {
    boolean result = false;
    if (start >= 0 && start + length <= value.length()) {
      String target = value.substring(start, start + length);
      for (String element : criteria) {
        if (target.equals(element)) {
          result = true;
          break;
        } 
      } 
    } 
    return result;
  }
  
  public class DoubleMetaphoneResult {
    private final StringBuilder primary = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
    
    private final StringBuilder alternate = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
    
    private final int maxLength;
    
    public DoubleMetaphoneResult(int maxLength) {
      this.maxLength = maxLength;
    }
    
    public void append(char value) {
      appendPrimary(value);
      appendAlternate(value);
    }
    
    public void append(char primary, char alternate) {
      appendPrimary(primary);
      appendAlternate(alternate);
    }
    
    public void appendPrimary(char value) {
      if (this.primary.length() < this.maxLength)
        this.primary.append(value); 
    }
    
    public void appendAlternate(char value) {
      if (this.alternate.length() < this.maxLength)
        this.alternate.append(value); 
    }
    
    public void append(String value) {
      appendPrimary(value);
      appendAlternate(value);
    }
    
    public void append(String primary, String alternate) {
      appendPrimary(primary);
      appendAlternate(alternate);
    }
    
    public void appendPrimary(String value) {
      int addChars = this.maxLength - this.primary.length();
      if (value.length() <= addChars) {
        this.primary.append(value);
      } else {
        this.primary.append(value.substring(0, addChars));
      } 
    }
    
    public void appendAlternate(String value) {
      int addChars = this.maxLength - this.alternate.length();
      if (value.length() <= addChars) {
        this.alternate.append(value);
      } else {
        this.alternate.append(value.substring(0, addChars));
      } 
    }
    
    public String getPrimary() {
      return this.primary.toString();
    }
    
    public String getAlternate() {
      return this.alternate.toString();
    }
    
    public boolean isComplete() {
      return (this.primary.length() >= this.maxLength && this.alternate.length() >= this.maxLength);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\codec\language\DoubleMetaphone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */