package com.ibm.icu.text;

import com.ibm.icu.impl.ICUConfig;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.util.Freezable;
import java.util.ArrayList;
import java.util.Locale;

public final class MessagePattern implements Cloneable, Freezable<MessagePattern> {
  public static final int ARG_NAME_NOT_NUMBER = -1;
  
  public static final int ARG_NAME_NOT_VALID = -2;
  
  public static final double NO_NUMERIC_VALUE = -1.23456789E8D;
  
  private static final int MAX_PREFIX_LENGTH = 24;
  
  private ApostropheMode aposMode;
  
  private String msg;
  
  public enum ApostropheMode {
    DOUBLE_OPTIONAL, DOUBLE_REQUIRED;
  }
  
  public MessagePattern() {
    this.aposMode = defaultAposMode;
  }
  
  public MessagePattern(ApostropheMode mode) {
    this.aposMode = mode;
  }
  
  public MessagePattern(String pattern) {
    this.aposMode = defaultAposMode;
    parse(pattern);
  }
  
  public MessagePattern parse(String pattern) {
    preParse(pattern);
    parseMessage(0, 0, 0, ArgType.NONE);
    postParse();
    return this;
  }
  
  public MessagePattern parseChoiceStyle(String pattern) {
    preParse(pattern);
    parseChoiceStyle(0, 0);
    postParse();
    return this;
  }
  
  public MessagePattern parsePluralStyle(String pattern) {
    preParse(pattern);
    parsePluralOrSelectStyle(ArgType.PLURAL, 0, 0);
    postParse();
    return this;
  }
  
  public MessagePattern parseSelectStyle(String pattern) {
    preParse(pattern);
    parsePluralOrSelectStyle(ArgType.SELECT, 0, 0);
    postParse();
    return this;
  }
  
  public void clear() {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to clear() a frozen MessagePattern instance."); 
    this.msg = null;
    this.hasArgNames = this.hasArgNumbers = false;
    this.needsAutoQuoting = false;
    this.parts.clear();
    if (this.numericValues != null)
      this.numericValues.clear(); 
  }
  
  public void clearPatternAndSetApostropheMode(ApostropheMode mode) {
    clear();
    this.aposMode = mode;
  }
  
  public boolean equals(Object other) {
    if (this == other)
      return true; 
    if (other == null || getClass() != other.getClass())
      return false; 
    MessagePattern o = (MessagePattern)other;
    return (this.aposMode.equals(o.aposMode) && ((this.msg == null) ? (o.msg == null) : this.msg.equals(o.msg)) && this.parts.equals(o.parts));
  }
  
  public int hashCode() {
    return (this.aposMode.hashCode() * 37 + ((this.msg != null) ? this.msg.hashCode() : 0)) * 37 + this.parts.hashCode();
  }
  
  public ApostropheMode getApostropheMode() {
    return this.aposMode;
  }
  
  boolean jdkAposMode() {
    return (this.aposMode == ApostropheMode.DOUBLE_REQUIRED);
  }
  
  public String getPatternString() {
    return this.msg;
  }
  
  public boolean hasNamedArguments() {
    return this.hasArgNames;
  }
  
  public boolean hasNumberedArguments() {
    return this.hasArgNumbers;
  }
  
  public String toString() {
    return this.msg;
  }
  
  public static int validateArgumentName(String name) {
    if (!PatternProps.isIdentifier(name))
      return -2; 
    return parseArgNumber(name, 0, name.length());
  }
  
  public String autoQuoteApostropheDeep() {
    if (!this.needsAutoQuoting)
      return this.msg; 
    StringBuilder modified = null;
    int count = countParts();
    for (int i = count; i > 0;) {
      if ((part = getPart(--i)).getType() == Part.Type.INSERT_CHAR) {
        if (modified == null)
          modified = (new StringBuilder(this.msg.length() + 10)).append(this.msg); 
        modified.insert(part.index, (char)part.value);
      } 
    } 
    if (modified == null)
      return this.msg; 
    return modified.toString();
  }
  
  public int countParts() {
    return this.parts.size();
  }
  
  public Part getPart(int i) {
    return this.parts.get(i);
  }
  
  public Part.Type getPartType(int i) {
    return (this.parts.get(i)).type;
  }
  
  public int getPatternIndex(int partIndex) {
    return (this.parts.get(partIndex)).index;
  }
  
  public String getSubstring(Part part) {
    int index = part.index;
    return this.msg.substring(index, index + part.length);
  }
  
  public boolean partSubstringMatches(Part part, String s) {
    return this.msg.regionMatches(part.index, s, 0, part.length);
  }
  
  public double getNumericValue(Part part) {
    Part.Type type = part.type;
    if (type == Part.Type.ARG_INT)
      return part.value; 
    if (type == Part.Type.ARG_DOUBLE)
      return ((Double)this.numericValues.get(part.value)).doubleValue(); 
    return -1.23456789E8D;
  }
  
  public double getPluralOffset(int pluralStart) {
    Part part = this.parts.get(pluralStart);
    if (part.type.hasNumericValue())
      return getNumericValue(part); 
    return 0.0D;
  }
  
  public int getLimitPartIndex(int start) {
    int limit = (this.parts.get(start)).limitPartIndex;
    if (limit < start)
      return start; 
    return limit;
  }
  
  public static final class Part {
    private static final int MAX_LENGTH = 65535;
    
    private static final int MAX_VALUE = 32767;
    
    private final Type type;
    
    private final int index;
    
    private final char length;
    
    private short value;
    
    private int limitPartIndex;
    
    private Part(Type t, int i, int l, int v) {
      this.type = t;
      this.index = i;
      this.length = (char)l;
      this.value = (short)v;
    }
    
    public Type getType() {
      return this.type;
    }
    
    public int getIndex() {
      return this.index;
    }
    
    public int getLength() {
      return this.length;
    }
    
    public int getLimit() {
      return this.index + this.length;
    }
    
    public int getValue() {
      return this.value;
    }
    
    public MessagePattern.ArgType getArgType() {
      Type type = getType();
      if (type == Type.ARG_START || type == Type.ARG_LIMIT)
        return MessagePattern.argTypes[this.value]; 
      return MessagePattern.ArgType.NONE;
    }
    
    public enum Type {
      MSG_START, MSG_LIMIT, SKIP_SYNTAX, INSERT_CHAR, REPLACE_NUMBER, ARG_START, ARG_LIMIT, ARG_NUMBER, ARG_NAME, ARG_TYPE, ARG_STYLE, ARG_SELECTOR, ARG_INT, ARG_DOUBLE;
      
      public boolean hasNumericValue() {
        return (this == ARG_INT || this == ARG_DOUBLE);
      }
    }
    
    public String toString() {
      String valueString = (this.type == Type.ARG_START || this.type == Type.ARG_LIMIT) ? getArgType().name() : Integer.toString(this.value);
      return this.type.name() + "(" + valueString + ")@" + this.index;
    }
    
    public boolean equals(Object other) {
      if (this == other)
        return true; 
      if (other == null || getClass() != other.getClass())
        return false; 
      Part o = (Part)other;
      return (this.type.equals(o.type) && this.index == o.index && this.length == o.length && this.value == o.value && this.limitPartIndex == o.limitPartIndex);
    }
    
    public int hashCode() {
      return ((this.type.hashCode() * 37 + this.index) * 37 + this.length) * 37 + this.value;
    }
  }
  
  public enum Type {
    MSG_START, MSG_LIMIT, SKIP_SYNTAX, INSERT_CHAR, REPLACE_NUMBER, ARG_START, ARG_LIMIT, ARG_NUMBER, ARG_NAME, ARG_TYPE, ARG_STYLE, ARG_SELECTOR, ARG_INT, ARG_DOUBLE;
    
    public boolean hasNumericValue() {
      return (this == ARG_INT || this == ARG_DOUBLE);
    }
  }
  
  public enum ArgType {
    NONE, SIMPLE, CHOICE, PLURAL, SELECT, SELECTORDINAL;
    
    public boolean hasPluralStyle() {
      return (this == PLURAL || this == SELECTORDINAL);
    }
  }
  
  public Object clone() {
    if (isFrozen())
      return this; 
    return cloneAsThawed();
  }
  
  public MessagePattern cloneAsThawed() {
    MessagePattern newMsg;
    try {
      newMsg = (MessagePattern)super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    } 
    newMsg.parts = (ArrayList<Part>)this.parts.clone();
    if (this.numericValues != null)
      newMsg.numericValues = (ArrayList<Double>)this.numericValues.clone(); 
    newMsg.frozen = false;
    return newMsg;
  }
  
  public MessagePattern freeze() {
    this.frozen = true;
    return this;
  }
  
  public boolean isFrozen() {
    return this.frozen;
  }
  
  private void preParse(String pattern) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to parse(" + prefix(pattern) + ") on frozen MessagePattern instance."); 
    this.msg = pattern;
    this.hasArgNames = this.hasArgNumbers = false;
    this.needsAutoQuoting = false;
    this.parts.clear();
    if (this.numericValues != null)
      this.numericValues.clear(); 
  }
  
  private void postParse() {}
  
  private int parseMessage(int index, int msgStartLength, int nestingLevel, ArgType parentType) {
    if (nestingLevel > 32767)
      throw new IndexOutOfBoundsException(); 
    int msgStart = this.parts.size();
    addPart(Part.Type.MSG_START, index, msgStartLength, nestingLevel);
    index += msgStartLength;
    while (index < this.msg.length()) {
      char c = this.msg.charAt(index++);
      if (c == '\'') {
        if (index == this.msg.length()) {
          addPart(Part.Type.INSERT_CHAR, index, 0, 39);
          this.needsAutoQuoting = true;
          continue;
        } 
        c = this.msg.charAt(index);
        if (c == '\'') {
          addPart(Part.Type.SKIP_SYNTAX, index++, 1, 0);
          continue;
        } 
        if (this.aposMode == ApostropheMode.DOUBLE_REQUIRED || c == '{' || c == '}' || (parentType == ArgType.CHOICE && c == '|') || (parentType.hasPluralStyle() && c == '#')) {
          addPart(Part.Type.SKIP_SYNTAX, index - 1, 1, 0);
          while (true) {
            index = this.msg.indexOf('\'', index + 1);
            if (index >= 0) {
              if (index + 1 < this.msg.length()) {
                if (this.msg.charAt(index + 1) == '\'') {
                  addPart(Part.Type.SKIP_SYNTAX, ++index, 1, 0);
                  continue;
                } 
                continue;
              } 
              addPart(Part.Type.SKIP_SYNTAX, index++, 1, 0);
              continue;
            } 
            break;
          } 
          index = this.msg.length();
          addPart(Part.Type.INSERT_CHAR, index, 0, 39);
          this.needsAutoQuoting = true;
          continue;
        } 
        addPart(Part.Type.INSERT_CHAR, index, 0, 39);
        this.needsAutoQuoting = true;
        continue;
      } 
      if (parentType.hasPluralStyle() && c == '#') {
        addPart(Part.Type.REPLACE_NUMBER, index - 1, 1, 0);
        continue;
      } 
      if (c == '{') {
        index = parseArg(index - 1, 1, nestingLevel);
        continue;
      } 
      if ((nestingLevel > 0 && c == '}') || (parentType == ArgType.CHOICE && c == '|')) {
        int limitLength = (parentType == ArgType.CHOICE && c == '}') ? 0 : 1;
        addLimitPart(msgStart, Part.Type.MSG_LIMIT, index - 1, limitLength, nestingLevel);
        if (parentType == ArgType.CHOICE)
          return index - 1; 
        return index;
      } 
    } 
    if (nestingLevel > 0 && !inTopLevelChoiceMessage(nestingLevel, parentType))
      throw new IllegalArgumentException("Unmatched '{' braces in message " + prefix()); 
    addLimitPart(msgStart, Part.Type.MSG_LIMIT, index, 0, nestingLevel);
    return index;
  }
  
  private int parseArg(int index, int argStartLength, int nestingLevel) {
    int argStart = this.parts.size();
    ArgType argType = ArgType.NONE;
    addPart(Part.Type.ARG_START, index, argStartLength, argType.ordinal());
    int nameIndex = index = skipWhiteSpace(index + argStartLength);
    if (index == this.msg.length())
      throw new IllegalArgumentException("Unmatched '{' braces in message " + prefix()); 
    index = skipIdentifier(index);
    int number = parseArgNumber(nameIndex, index);
    if (number >= 0) {
      int length = index - nameIndex;
      if (length > 65535 || number > 32767)
        throw new IndexOutOfBoundsException("Argument number too large: " + prefix(nameIndex)); 
      this.hasArgNumbers = true;
      addPart(Part.Type.ARG_NUMBER, nameIndex, length, number);
    } else if (number == -1) {
      int length = index - nameIndex;
      if (length > 65535)
        throw new IndexOutOfBoundsException("Argument name too long: " + prefix(nameIndex)); 
      this.hasArgNames = true;
      addPart(Part.Type.ARG_NAME, nameIndex, length, 0);
    } else {
      throw new IllegalArgumentException("Bad argument syntax: " + prefix(nameIndex));
    } 
    index = skipWhiteSpace(index);
    if (index == this.msg.length())
      throw new IllegalArgumentException("Unmatched '{' braces in message " + prefix()); 
    char c = this.msg.charAt(index);
    if (c != '}') {
      if (c != ',')
        throw new IllegalArgumentException("Bad argument syntax: " + prefix(nameIndex)); 
      int typeIndex = index = skipWhiteSpace(index + 1);
      while (index < this.msg.length() && isArgTypeChar(this.msg.charAt(index)))
        index++; 
      int length = index - typeIndex;
      index = skipWhiteSpace(index);
      if (index == this.msg.length())
        throw new IllegalArgumentException("Unmatched '{' braces in message " + prefix()); 
      if (length == 0 || ((c = this.msg.charAt(index)) != ',' && c != '}'))
        throw new IllegalArgumentException("Bad argument syntax: " + prefix(nameIndex)); 
      if (length > 65535)
        throw new IndexOutOfBoundsException("Argument type name too long: " + prefix(nameIndex)); 
      argType = ArgType.SIMPLE;
      if (length == 6) {
        if (isChoice(typeIndex)) {
          argType = ArgType.CHOICE;
        } else if (isPlural(typeIndex)) {
          argType = ArgType.PLURAL;
        } else if (isSelect(typeIndex)) {
          argType = ArgType.SELECT;
        } 
      } else if (length == 13 && 
        isSelect(typeIndex) && isOrdinal(typeIndex + 6)) {
        argType = ArgType.SELECTORDINAL;
      } 
      (this.parts.get(argStart)).value = (short)argType.ordinal();
      if (argType == ArgType.SIMPLE)
        addPart(Part.Type.ARG_TYPE, typeIndex, length, 0); 
      if (c == '}') {
        if (argType != ArgType.SIMPLE)
          throw new IllegalArgumentException("No style field for complex argument: " + prefix(nameIndex)); 
      } else {
        index++;
        if (argType == ArgType.SIMPLE) {
          index = parseSimpleStyle(index);
        } else if (argType == ArgType.CHOICE) {
          index = parseChoiceStyle(index, nestingLevel);
        } else {
          index = parsePluralOrSelectStyle(argType, index, nestingLevel);
        } 
      } 
    } 
    addLimitPart(argStart, Part.Type.ARG_LIMIT, index, 1, argType.ordinal());
    return index + 1;
  }
  
  private int parseSimpleStyle(int index) {
    int start = index;
    int nestedBraces = 0;
    while (index < this.msg.length()) {
      char c = this.msg.charAt(index++);
      if (c == '\'') {
        index = this.msg.indexOf('\'', index);
        if (index < 0)
          throw new IllegalArgumentException("Quoted literal argument style text reaches to the end of the message: " + prefix(start)); 
        index++;
        continue;
      } 
      if (c == '{') {
        nestedBraces++;
        continue;
      } 
      if (c == '}') {
        if (nestedBraces > 0) {
          nestedBraces--;
          continue;
        } 
        int length = --index - start;
        if (length > 65535)
          throw new IndexOutOfBoundsException("Argument style text too long: " + prefix(start)); 
        addPart(Part.Type.ARG_STYLE, start, length, 0);
        return index;
      } 
    } 
    throw new IllegalArgumentException("Unmatched '{' braces in message " + prefix());
  }
  
  private int parseChoiceStyle(int index, int nestingLevel) {
    int start = index;
    index = skipWhiteSpace(index);
    if (index == this.msg.length() || this.msg.charAt(index) == '}')
      throw new IllegalArgumentException("Missing choice argument pattern in " + prefix()); 
    while (true) {
      int numberIndex = index;
      index = skipDouble(index);
      int length = index - numberIndex;
      if (length == 0)
        throw new IllegalArgumentException("Bad choice pattern syntax: " + prefix(start)); 
      if (length > 65535)
        throw new IndexOutOfBoundsException("Choice number too long: " + prefix(numberIndex)); 
      parseDouble(numberIndex, index, true);
      index = skipWhiteSpace(index);
      if (index == this.msg.length())
        throw new IllegalArgumentException("Bad choice pattern syntax: " + prefix(start)); 
      char c = this.msg.charAt(index);
      if (c != '#' && c != '<' && c != '≤')
        throw new IllegalArgumentException("Expected choice separator (#<≤) instead of '" + c + "' in choice pattern " + prefix(start)); 
      addPart(Part.Type.ARG_SELECTOR, index, 1, 0);
      index = parseMessage(++index, 0, nestingLevel + 1, ArgType.CHOICE);
      if (index == this.msg.length())
        return index; 
      if (this.msg.charAt(index) == '}') {
        if (!inMessageFormatPattern(nestingLevel))
          throw new IllegalArgumentException("Bad choice pattern syntax: " + prefix(start)); 
        return index;
      } 
      index = skipWhiteSpace(index + 1);
    } 
  }
  
  private int parsePluralOrSelectStyle(ArgType argType, int index, int nestingLevel) {
    int start = index;
    boolean isEmpty = true;
    boolean hasOther = false;
    while (true) {
      index = skipWhiteSpace(index);
      boolean eos = (index == this.msg.length());
      if (eos || this.msg.charAt(index) == '}') {
        if (eos == inMessageFormatPattern(nestingLevel))
          throw new IllegalArgumentException("Bad " + argType.toString().toLowerCase(Locale.ENGLISH) + " pattern syntax: " + prefix(start)); 
        if (!hasOther)
          throw new IllegalArgumentException("Missing 'other' keyword in " + argType.toString().toLowerCase(Locale.ENGLISH) + " pattern in " + prefix()); 
        return index;
      } 
      int selectorIndex = index;
      if (argType.hasPluralStyle() && this.msg.charAt(selectorIndex) == '=') {
        index = skipDouble(index + 1);
        int length = index - selectorIndex;
        if (length == 1)
          throw new IllegalArgumentException("Bad " + argType.toString().toLowerCase(Locale.ENGLISH) + " pattern syntax: " + prefix(start)); 
        if (length > 65535)
          throw new IndexOutOfBoundsException("Argument selector too long: " + prefix(selectorIndex)); 
        addPart(Part.Type.ARG_SELECTOR, selectorIndex, length, 0);
        parseDouble(selectorIndex + 1, index, false);
      } else {
        index = skipIdentifier(index);
        int length = index - selectorIndex;
        if (length == 0)
          throw new IllegalArgumentException("Bad " + argType.toString().toLowerCase(Locale.ENGLISH) + " pattern syntax: " + prefix(start)); 
        if (argType.hasPluralStyle() && length == 6 && index < this.msg.length() && this.msg.regionMatches(selectorIndex, "offset:", 0, 7)) {
          if (!isEmpty)
            throw new IllegalArgumentException("Plural argument 'offset:' (if present) must precede key-message pairs: " + prefix(start)); 
          int valueIndex = skipWhiteSpace(index + 1);
          index = skipDouble(valueIndex);
          if (index == valueIndex)
            throw new IllegalArgumentException("Missing value for plural 'offset:' " + prefix(start)); 
          if (index - valueIndex > 65535)
            throw new IndexOutOfBoundsException("Plural offset value too long: " + prefix(valueIndex)); 
          parseDouble(valueIndex, index, false);
          isEmpty = false;
          continue;
        } 
        if (length > 65535)
          throw new IndexOutOfBoundsException("Argument selector too long: " + prefix(selectorIndex)); 
        addPart(Part.Type.ARG_SELECTOR, selectorIndex, length, 0);
        if (this.msg.regionMatches(selectorIndex, "other", 0, length))
          hasOther = true; 
      } 
      index = skipWhiteSpace(index);
      if (index == this.msg.length() || this.msg.charAt(index) != '{')
        throw new IllegalArgumentException("No message fragment after " + argType.toString().toLowerCase(Locale.ENGLISH) + " selector: " + prefix(selectorIndex)); 
      index = parseMessage(index, 1, nestingLevel + 1, argType);
      isEmpty = false;
    } 
  }
  
  private static int parseArgNumber(CharSequence s, int start, int limit) {
    int number;
    boolean badNumber;
    if (start >= limit)
      return -2; 
    char c = s.charAt(start++);
    if (c == '0') {
      if (start == limit)
        return 0; 
      number = 0;
      badNumber = true;
    } else if ('1' <= c && c <= '9') {
      number = c - 48;
      badNumber = false;
    } else {
      return -1;
    } 
    while (start < limit) {
      c = s.charAt(start++);
      if ('0' <= c && c <= '9') {
        if (number >= 214748364)
          badNumber = true; 
        number = number * 10 + c - 48;
        continue;
      } 
      return -1;
    } 
    if (badNumber)
      return -2; 
    return number;
  }
  
  private int parseArgNumber(int start, int limit) {
    return parseArgNumber(this.msg, start, limit);
  }
  
  private void parseDouble(int start, int limit, boolean allowInfinity) {
    assert start < limit;
    int value = 0;
    int isNegative = 0;
    int index = start;
    char c = this.msg.charAt(index++);
    if (c == '-') {
      isNegative = 1;
      if (index == limit)
        throw new NumberFormatException("Bad syntax for numeric value: " + this.msg.substring(start, limit)); 
      c = this.msg.charAt(index++);
    } else if (c == '+') {
      if (index == limit)
        throw new NumberFormatException("Bad syntax for numeric value: " + this.msg.substring(start, limit)); 
      c = this.msg.charAt(index++);
    } 
    if (c == '∞') {
      if (allowInfinity && index == limit) {
        addArgDoublePart((isNegative != 0) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY, start, limit - start);
        return;
      } 
    } else {
      while ('0' <= c && c <= '9') {
        value = value * 10 + c - 48;
        if (value > 32767 + isNegative)
          break; 
        if (index == limit) {
          addPart(Part.Type.ARG_INT, start, limit - start, (isNegative != 0) ? -value : value);
          return;
        } 
        c = this.msg.charAt(index++);
      } 
      double numericValue = Double.parseDouble(this.msg.substring(start, limit));
      addArgDoublePart(numericValue, start, limit - start);
      return;
    } 
    throw new NumberFormatException("Bad syntax for numeric value: " + this.msg.substring(start, limit));
  }
  
  static void appendReducedApostrophes(String s, int start, int limit, StringBuilder sb) {
    int doubleApos = -1;
    while (true) {
      int i = s.indexOf('\'', start);
      if (i < 0 || i >= limit) {
        sb.append(s, start, limit);
        break;
      } 
      if (i == doubleApos) {
        sb.append('\'');
        start++;
        doubleApos = -1;
        continue;
      } 
      sb.append(s, start, i);
      doubleApos = start = i + 1;
    } 
  }
  
  private int skipWhiteSpace(int index) {
    return PatternProps.skipWhiteSpace(this.msg, index);
  }
  
  private int skipIdentifier(int index) {
    return PatternProps.skipIdentifier(this.msg, index);
  }
  
  private int skipDouble(int index) {
    while (index < this.msg.length()) {
      char c = this.msg.charAt(index);
      if ((c < '0' && "+-.".indexOf(c) < 0) || (c > '9' && c != 'e' && c != 'E' && c != '∞'))
        break; 
      index++;
    } 
    return index;
  }
  
  private static boolean isArgTypeChar(int c) {
    return ((97 <= c && c <= 122) || (65 <= c && c <= 90));
  }
  
  private boolean isChoice(int index) {
    char c;
    return (((c = this.msg.charAt(index++)) == 'c' || c == 'C') && ((c = this.msg.charAt(index++)) == 'h' || c == 'H') && ((c = this.msg.charAt(index++)) == 'o' || c == 'O') && ((c = this.msg.charAt(index++)) == 'i' || c == 'I') && ((c = this.msg.charAt(index++)) == 'c' || c == 'C') && ((c = this.msg.charAt(index)) == 'e' || c == 'E'));
  }
  
  private boolean isPlural(int index) {
    char c;
    return (((c = this.msg.charAt(index++)) == 'p' || c == 'P') && ((c = this.msg.charAt(index++)) == 'l' || c == 'L') && ((c = this.msg.charAt(index++)) == 'u' || c == 'U') && ((c = this.msg.charAt(index++)) == 'r' || c == 'R') && ((c = this.msg.charAt(index++)) == 'a' || c == 'A') && ((c = this.msg.charAt(index)) == 'l' || c == 'L'));
  }
  
  private boolean isSelect(int index) {
    char c;
    return (((c = this.msg.charAt(index++)) == 's' || c == 'S') && ((c = this.msg.charAt(index++)) == 'e' || c == 'E') && ((c = this.msg.charAt(index++)) == 'l' || c == 'L') && ((c = this.msg.charAt(index++)) == 'e' || c == 'E') && ((c = this.msg.charAt(index++)) == 'c' || c == 'C') && ((c = this.msg.charAt(index)) == 't' || c == 'T'));
  }
  
  private boolean isOrdinal(int index) {
    char c;
    return (((c = this.msg.charAt(index++)) == 'o' || c == 'O') && ((c = this.msg.charAt(index++)) == 'r' || c == 'R') && ((c = this.msg.charAt(index++)) == 'd' || c == 'D') && ((c = this.msg.charAt(index++)) == 'i' || c == 'I') && ((c = this.msg.charAt(index++)) == 'n' || c == 'N') && ((c = this.msg.charAt(index++)) == 'a' || c == 'A') && ((c = this.msg.charAt(index)) == 'l' || c == 'L'));
  }
  
  private boolean inMessageFormatPattern(int nestingLevel) {
    return (nestingLevel > 0 || (this.parts.get(0)).type == Part.Type.MSG_START);
  }
  
  private boolean inTopLevelChoiceMessage(int nestingLevel, ArgType parentType) {
    return (nestingLevel == 1 && parentType == ArgType.CHOICE && (this.parts.get(0)).type != Part.Type.MSG_START);
  }
  
  private void addPart(Part.Type type, int index, int length, int value) {
    this.parts.add(new Part(type, index, length, value));
  }
  
  private void addLimitPart(int start, Part.Type type, int index, int length, int value) {
    (this.parts.get(start)).limitPartIndex = this.parts.size();
    addPart(type, index, length, value);
  }
  
  private void addArgDoublePart(double numericValue, int start, int length) {
    int numericIndex;
    if (this.numericValues == null) {
      this.numericValues = new ArrayList<Double>();
      numericIndex = 0;
    } else {
      numericIndex = this.numericValues.size();
      if (numericIndex > 32767)
        throw new IndexOutOfBoundsException("Too many numeric values"); 
    } 
    this.numericValues.add(Double.valueOf(numericValue));
    addPart(Part.Type.ARG_DOUBLE, start, length, numericIndex);
  }
  
  private static String prefix(String s, int start) {
    StringBuilder prefix = new StringBuilder(44);
    if (start == 0) {
      prefix.append("\"");
    } else {
      prefix.append("[at pattern index ").append(start).append("] \"");
    } 
    int substringLength = s.length() - start;
    if (substringLength <= 24) {
      prefix.append((start == 0) ? s : s.substring(start));
    } else {
      int limit = start + 24 - 4;
      if (Character.isHighSurrogate(s.charAt(limit - 1)))
        limit--; 
      prefix.append(s, start, limit).append(" ...");
    } 
    return prefix.append("\"").toString();
  }
  
  private static String prefix(String s) {
    return prefix(s, 0);
  }
  
  private String prefix(int start) {
    return prefix(this.msg, start);
  }
  
  private String prefix() {
    return prefix(this.msg, 0);
  }
  
  private ArrayList<Part> parts = new ArrayList<Part>();
  
  private ArrayList<Double> numericValues;
  
  private boolean hasArgNames;
  
  private boolean hasArgNumbers;
  
  private boolean needsAutoQuoting;
  
  private boolean frozen;
  
  private static final ApostropheMode defaultAposMode = ApostropheMode.valueOf(ICUConfig.get("com.ibm.icu.text.MessagePattern.ApostropheMode", "DOUBLE_OPTIONAL"));
  
  private static final ArgType[] argTypes = ArgType.values();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\MessagePattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */