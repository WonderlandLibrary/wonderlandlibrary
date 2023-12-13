package com.sun.webkit.dom;

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CharacterDataImpl extends NodeImpl implements CharacterData {
   public static final byte DIRECTIONALITY_LEFT_TO_RIGHT = 0;
   public static final byte DIRECTIONALITY_RIGHT_TO_LEFT = 1;
   public static final byte DIRECTIONALITY_EUROPEAN_NUMBER = 3;
   public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR = 4;
   public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR = 5;
   public static final byte DIRECTIONALITY_ARABIC_NUMBER = 6;
   public static final byte DIRECTIONALITY_COMMON_NUMBER_SEPARATOR = 7;
   public static final byte DIRECTIONALITY_PARAGRAPH_SEPARATOR = 10;
   public static final byte DIRECTIONALITY_SEGMENT_SEPARATOR = 11;
   public static final byte DIRECTIONALITY_WHITESPACE = 12;
   public static final byte DIRECTIONALITY_OTHER_NEUTRALS = 13;
   public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING = 14;
   public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE = 15;
   public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC = 2;
   public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING = 16;
   public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE = 17;
   public static final byte DIRECTIONALITY_POP_DIRECTIONAL_FORMAT = 18;
   public static final byte DIRECTIONALITY_NONSPACING_MARK = 8;
   public static final byte DIRECTIONALITY_BOUNDARY_NEUTRAL = 9;
   public static final byte UNASSIGNED = 0;
   public static final byte UPPERCASE_LETTER = 1;
   public static final byte LOWERCASE_LETTER = 2;
   public static final byte TITLECASE_LETTER = 3;
   public static final byte MODIFIER_LETTER = 4;
   public static final byte OTHER_LETTER = 5;
   public static final byte NON_SPACING_MARK = 6;
   public static final byte ENCLOSING_MARK = 7;
   public static final byte COMBINING_SPACING_MARK = 8;
   public static final byte DECIMAL_DIGIT_NUMBER = 9;
   public static final byte LETTER_NUMBER = 10;
   public static final byte OTHER_NUMBER = 11;
   public static final byte SPACE_SEPARATOR = 12;
   public static final byte LINE_SEPARATOR = 13;
   public static final byte PARAGRAPH_SEPARATOR = 14;
   public static final byte CONTROL = 15;
   public static final byte FORMAT = 16;
   public static final byte PRIVATE_USE = 18;
   public static final byte SURROGATE = 19;
   public static final byte DASH_PUNCTUATION = 20;
   public static final byte START_PUNCTUATION = 21;
   public static final byte END_PUNCTUATION = 22;
   public static final byte CONNECTOR_PUNCTUATION = 23;
   public static final byte OTHER_PUNCTUATION = 24;
   public static final byte MATH_SYMBOL = 25;
   public static final byte CURRENCY_SYMBOL = 26;
   public static final byte MODIFIER_SYMBOL = 27;
   public static final byte OTHER_SYMBOL = 28;
   public static final byte INITIAL_QUOTE_PUNCTUATION = 29;
   public static final byte FINAL_QUOTE_PUNCTUATION = 30;

   CharacterDataImpl(long var1) {
      super(var1);
   }

   static Node getImpl(long var0) {
      return create(var0);
   }

   public String getData() {
      return getDataImpl(this.getPeer());
   }

   static native String getDataImpl(long var0);

   public void setData(String var1) {
      setDataImpl(this.getPeer(), var1);
   }

   static native void setDataImpl(long var0, String var2);

   public int getLength() {
      return getLengthImpl(this.getPeer());
   }

   static native int getLengthImpl(long var0);

   public Element getPreviousElementSibling() {
      return ElementImpl.getImpl(getPreviousElementSiblingImpl(this.getPeer()));
   }

   static native long getPreviousElementSiblingImpl(long var0);

   public Element getNextElementSibling() {
      return ElementImpl.getImpl(getNextElementSiblingImpl(this.getPeer()));
   }

   static native long getNextElementSiblingImpl(long var0);

   public String substringData(int var1, int var2) throws DOMException {
      return substringDataImpl(this.getPeer(), var1, var2);
   }

   static native String substringDataImpl(long var0, int var2, int var3);

   public void appendData(String var1) {
      appendDataImpl(this.getPeer(), var1);
   }

   static native void appendDataImpl(long var0, String var2);

   public void insertData(int var1, String var2) throws DOMException {
      insertDataImpl(this.getPeer(), var1, var2);
   }

   static native void insertDataImpl(long var0, int var2, String var3);

   public void deleteData(int var1, int var2) throws DOMException {
      deleteDataImpl(this.getPeer(), var1, var2);
   }

   static native void deleteDataImpl(long var0, int var2, int var3);

   public void replaceData(int var1, int var2, String var3) throws DOMException {
      replaceDataImpl(this.getPeer(), var1, var2, var3);
   }

   static native void replaceDataImpl(long var0, int var2, int var3, String var4);

   public void remove() throws DOMException {
      removeImpl(this.getPeer());
   }

   static native void removeImpl(long var0);
}
