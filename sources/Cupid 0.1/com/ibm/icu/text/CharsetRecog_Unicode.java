package com.ibm.icu.text;

abstract class CharsetRecog_Unicode extends CharsetRecognizer {
  abstract String getName();
  
  abstract CharsetMatch match(CharsetDetector paramCharsetDetector);
  
  static class CharsetRecog_UTF_16_BE extends CharsetRecog_Unicode {
    String getName() {
      return "UTF-16BE";
    }
    
    CharsetMatch match(CharsetDetector det) {
      byte[] input = det.fRawInput;
      if (input.length >= 2 && (input[0] & 0xFF) == 254 && (input[1] & 0xFF) == 255) {
        int confidence = 100;
        return new CharsetMatch(det, this, confidence);
      } 
      return null;
    }
  }
  
  static class CharsetRecog_UTF_16_LE extends CharsetRecog_Unicode {
    String getName() {
      return "UTF-16LE";
    }
    
    CharsetMatch match(CharsetDetector det) {
      byte[] input = det.fRawInput;
      if (input.length >= 2 && (input[0] & 0xFF) == 255 && (input[1] & 0xFF) == 254) {
        if (input.length >= 4 && input[2] == 0 && input[3] == 0)
          return null; 
        int confidence = 100;
        return new CharsetMatch(det, this, confidence);
      } 
      return null;
    }
  }
  
  static abstract class CharsetRecog_UTF_32 extends CharsetRecog_Unicode {
    abstract int getChar(byte[] param1ArrayOfbyte, int param1Int);
    
    abstract String getName();
    
    CharsetMatch match(CharsetDetector det) {
      byte[] input = det.fRawInput;
      int limit = det.fRawLength / 4 * 4;
      int numValid = 0;
      int numInvalid = 0;
      boolean hasBOM = false;
      int confidence = 0;
      if (limit == 0)
        return null; 
      if (getChar(input, 0) == 65279)
        hasBOM = true; 
      for (int i = 0; i < limit; i += 4) {
        int ch = getChar(input, i);
        if (ch < 0 || ch >= 1114111 || (ch >= 55296 && ch <= 57343)) {
          numInvalid++;
        } else {
          numValid++;
        } 
      } 
      if (hasBOM && numInvalid == 0) {
        confidence = 100;
      } else if (hasBOM && numValid > numInvalid * 10) {
        confidence = 80;
      } else if (numValid > 3 && numInvalid == 0) {
        confidence = 100;
      } else if (numValid > 0 && numInvalid == 0) {
        confidence = 80;
      } else if (numValid > numInvalid * 10) {
        confidence = 25;
      } 
      return (confidence == 0) ? null : new CharsetMatch(det, this, confidence);
    }
  }
  
  static class CharsetRecog_UTF_32_BE extends CharsetRecog_UTF_32 {
    int getChar(byte[] input, int index) {
      return (input[index + 0] & 0xFF) << 24 | (input[index + 1] & 0xFF) << 16 | (input[index + 2] & 0xFF) << 8 | input[index + 3] & 0xFF;
    }
    
    String getName() {
      return "UTF-32BE";
    }
  }
  
  static class CharsetRecog_UTF_32_LE extends CharsetRecog_UTF_32 {
    int getChar(byte[] input, int index) {
      return (input[index + 3] & 0xFF) << 24 | (input[index + 2] & 0xFF) << 16 | (input[index + 1] & 0xFF) << 8 | input[index + 0] & 0xFF;
    }
    
    String getName() {
      return "UTF-32LE";
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\CharsetRecog_Unicode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */