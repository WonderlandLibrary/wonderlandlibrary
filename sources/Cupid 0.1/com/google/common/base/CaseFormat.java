package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible
public enum CaseFormat {
  LOWER_HYPHEN(CharMatcher.is('-'), "-") {
    String normalizeWord(String word) {
      return Ascii.toLowerCase(word);
    }
    
    String convert(CaseFormat format, String s) {
      if (format == LOWER_UNDERSCORE)
        return s.replace('-', '_'); 
      if (format == UPPER_UNDERSCORE)
        return Ascii.toUpperCase(s.replace('-', '_')); 
      return super.convert(format, s);
    }
  },
  LOWER_UNDERSCORE(CharMatcher.is('_'), "_") {
    String normalizeWord(String word) {
      return Ascii.toLowerCase(word);
    }
    
    String convert(CaseFormat format, String s) {
      if (format == LOWER_HYPHEN)
        return s.replace('_', '-'); 
      if (format == UPPER_UNDERSCORE)
        return Ascii.toUpperCase(s); 
      return super.convert(format, s);
    }
  },
  LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
    String normalizeWord(String word) {
      return firstCharOnlyToUpper(word);
    }
  },
  UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
    String normalizeWord(String word) {
      return firstCharOnlyToUpper(word);
    }
  },
  UPPER_UNDERSCORE(CharMatcher.is('_'), "_") {
    String normalizeWord(String word) {
      return Ascii.toUpperCase(word);
    }
    
    String convert(CaseFormat format, String s) {
      if (format == LOWER_HYPHEN)
        return Ascii.toLowerCase(s.replace('_', '-')); 
      if (format == LOWER_UNDERSCORE)
        return Ascii.toLowerCase(s); 
      return super.convert(format, s);
    }
  };
  
  private final CharMatcher wordBoundary;
  
  private final String wordSeparator;
  
  CaseFormat(CharMatcher wordBoundary, String wordSeparator) {
    this.wordBoundary = wordBoundary;
    this.wordSeparator = wordSeparator;
  }
  
  public final String to(CaseFormat format, String str) {
    Preconditions.checkNotNull(format);
    Preconditions.checkNotNull(str);
    return (format == this) ? str : convert(format, str);
  }
  
  String convert(CaseFormat format, String s) {
    StringBuilder out = null;
    int i = 0;
    int j = -1;
    while ((j = this.wordBoundary.indexIn(s, ++j)) != -1) {
      if (i == 0) {
        out = new StringBuilder(s.length() + 4 * this.wordSeparator.length());
        out.append(format.normalizeFirstWord(s.substring(i, j)));
      } else {
        out.append(format.normalizeWord(s.substring(i, j)));
      } 
      out.append(format.wordSeparator);
      i = j + this.wordSeparator.length();
    } 
    return (i == 0) ? format.normalizeFirstWord(s) : out.append(format.normalizeWord(s.substring(i))).toString();
  }
  
  @Beta
  public Converter<String, String> converterTo(CaseFormat targetFormat) {
    return new StringConverter(this, targetFormat);
  }
  
  private static final class StringConverter extends Converter<String, String> implements Serializable {
    private final CaseFormat sourceFormat;
    
    private final CaseFormat targetFormat;
    
    private static final long serialVersionUID = 0L;
    
    StringConverter(CaseFormat sourceFormat, CaseFormat targetFormat) {
      this.sourceFormat = Preconditions.<CaseFormat>checkNotNull(sourceFormat);
      this.targetFormat = Preconditions.<CaseFormat>checkNotNull(targetFormat);
    }
    
    protected String doForward(String s) {
      return (s == null) ? null : this.sourceFormat.to(this.targetFormat, s);
    }
    
    protected String doBackward(String s) {
      return (s == null) ? null : this.targetFormat.to(this.sourceFormat, s);
    }
    
    public boolean equals(@Nullable Object object) {
      if (object instanceof StringConverter) {
        StringConverter that = (StringConverter)object;
        return (this.sourceFormat.equals(that.sourceFormat) && this.targetFormat.equals(that.targetFormat));
      } 
      return false;
    }
    
    public int hashCode() {
      return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode();
    }
    
    public String toString() {
      return this.sourceFormat + ".converterTo(" + this.targetFormat + ")";
    }
  }
  
  private String normalizeFirstWord(String word) {
    return (this == LOWER_CAMEL) ? Ascii.toLowerCase(word) : normalizeWord(word);
  }
  
  private static String firstCharOnlyToUpper(String word) {
    return word.isEmpty() ? word : (new StringBuilder(word.length())).append(Ascii.toUpperCase(word.charAt(0))).append(Ascii.toLowerCase(word.substring(1))).toString();
  }
  
  abstract String normalizeWord(String paramString);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\CaseFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */