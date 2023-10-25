package org.apache.commons.lang3.text;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

public class ExtendedMessageFormat extends MessageFormat {
  private static final long serialVersionUID = -2362048321261811743L;
  
  private static final int HASH_SEED = 31;
  
  private static final String DUMMY_PATTERN = "";
  
  private static final String ESCAPED_QUOTE = "''";
  
  private static final char START_FMT = ',';
  
  private static final char END_FE = '}';
  
  private static final char START_FE = '{';
  
  private static final char QUOTE = '\'';
  
  private String toPattern;
  
  private final Map<String, ? extends FormatFactory> registry;
  
  public ExtendedMessageFormat(String pattern) {
    this(pattern, Locale.getDefault());
  }
  
  public ExtendedMessageFormat(String pattern, Locale locale) {
    this(pattern, locale, (Map<String, ? extends FormatFactory>)null);
  }
  
  public ExtendedMessageFormat(String pattern, Map<String, ? extends FormatFactory> registry) {
    this(pattern, Locale.getDefault(), registry);
  }
  
  public ExtendedMessageFormat(String pattern, Locale locale, Map<String, ? extends FormatFactory> registry) {
    super("");
    setLocale(locale);
    this.registry = registry;
    applyPattern(pattern);
  }
  
  public String toPattern() {
    return this.toPattern;
  }
  
  public final void applyPattern(String pattern) {
    if (this.registry == null) {
      super.applyPattern(pattern);
      this.toPattern = super.toPattern();
      return;
    } 
    ArrayList<Format> foundFormats = new ArrayList<Format>();
    ArrayList<String> foundDescriptions = new ArrayList<String>();
    StringBuilder stripCustom = new StringBuilder(pattern.length());
    ParsePosition pos = new ParsePosition(0);
    char[] c = pattern.toCharArray();
    int fmtCount = 0;
    while (pos.getIndex() < pattern.length()) {
      int start, index;
      Format format;
      String formatDescription;
      switch (c[pos.getIndex()]) {
        case '\'':
          appendQuotedString(pattern, pos, stripCustom, true);
          continue;
        case '{':
          fmtCount++;
          seekNonWs(pattern, pos);
          start = pos.getIndex();
          index = readArgumentIndex(pattern, next(pos));
          stripCustom.append('{').append(index);
          seekNonWs(pattern, pos);
          format = null;
          formatDescription = null;
          if (c[pos.getIndex()] == ',') {
            formatDescription = parseFormatDescription(pattern, next(pos));
            format = getFormat(formatDescription);
            if (format == null)
              stripCustom.append(',').append(formatDescription); 
          } 
          foundFormats.add(format);
          foundDescriptions.add((format == null) ? null : formatDescription);
          Validate.isTrue((foundFormats.size() == fmtCount));
          Validate.isTrue((foundDescriptions.size() == fmtCount));
          if (c[pos.getIndex()] != '}')
            throw new IllegalArgumentException("Unreadable format element at position " + start); 
          break;
      } 
      stripCustom.append(c[pos.getIndex()]);
      next(pos);
    } 
    super.applyPattern(stripCustom.toString());
    this.toPattern = insertFormats(super.toPattern(), foundDescriptions);
    if (containsElements(foundFormats)) {
      Format[] origFormats = getFormats();
      int i = 0;
      for (Iterator<Format> it = foundFormats.iterator(); it.hasNext(); i++) {
        Format f = it.next();
        if (f != null)
          origFormats[i] = f; 
      } 
      super.setFormats(origFormats);
    } 
  }
  
  public void setFormat(int formatElementIndex, Format newFormat) {
    throw new UnsupportedOperationException();
  }
  
  public void setFormatByArgumentIndex(int argumentIndex, Format newFormat) {
    throw new UnsupportedOperationException();
  }
  
  public void setFormats(Format[] newFormats) {
    throw new UnsupportedOperationException();
  }
  
  public void setFormatsByArgumentIndex(Format[] newFormats) {
    throw new UnsupportedOperationException();
  }
  
  public boolean equals(Object obj) {
    if (obj == this)
      return true; 
    if (obj == null)
      return false; 
    if (!super.equals(obj))
      return false; 
    if (ObjectUtils.notEqual(getClass(), obj.getClass()))
      return false; 
    ExtendedMessageFormat rhs = (ExtendedMessageFormat)obj;
    if (ObjectUtils.notEqual(this.toPattern, rhs.toPattern))
      return false; 
    if (ObjectUtils.notEqual(this.registry, rhs.registry))
      return false; 
    return true;
  }
  
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + ObjectUtils.hashCode(this.registry);
    result = 31 * result + ObjectUtils.hashCode(this.toPattern);
    return result;
  }
  
  private Format getFormat(String desc) {
    if (this.registry != null) {
      String name = desc;
      String args = null;
      int i = desc.indexOf(',');
      if (i > 0) {
        name = desc.substring(0, i).trim();
        args = desc.substring(i + 1).trim();
      } 
      FormatFactory factory = this.registry.get(name);
      if (factory != null)
        return factory.getFormat(name, args, getLocale()); 
    } 
    return null;
  }
  
  private int readArgumentIndex(String pattern, ParsePosition pos) {
    int start = pos.getIndex();
    seekNonWs(pattern, pos);
    StringBuilder result = new StringBuilder();
    boolean error = false;
    for (; !error && pos.getIndex() < pattern.length(); next(pos)) {
      char c = pattern.charAt(pos.getIndex());
      if (Character.isWhitespace(c)) {
        seekNonWs(pattern, pos);
        c = pattern.charAt(pos.getIndex());
        if (c != ',' && c != '}') {
          error = true;
          continue;
        } 
      } 
      if ((c == ',' || c == '}') && result.length() > 0)
        try {
          return Integer.parseInt(result.toString());
        } catch (NumberFormatException e) {} 
      error = !Character.isDigit(c);
      result.append(c);
      continue;
    } 
    if (error)
      throw new IllegalArgumentException("Invalid format argument index at position " + start + ": " + pattern.substring(start, pos.getIndex())); 
    throw new IllegalArgumentException("Unterminated format element at position " + start);
  }
  
  private String parseFormatDescription(String pattern, ParsePosition pos) {
    int start = pos.getIndex();
    seekNonWs(pattern, pos);
    int text = pos.getIndex();
    int depth = 1;
    for (; pos.getIndex() < pattern.length(); next(pos)) {
      switch (pattern.charAt(pos.getIndex())) {
        case '{':
          depth++;
          break;
        case '}':
          depth--;
          if (depth == 0)
            return pattern.substring(text, pos.getIndex()); 
          break;
        case '\'':
          getQuotedString(pattern, pos, false);
          break;
      } 
    } 
    throw new IllegalArgumentException("Unterminated format element at position " + start);
  }
  
  private String insertFormats(String pattern, ArrayList<String> customPatterns) {
    if (!containsElements(customPatterns))
      return pattern; 
    StringBuilder sb = new StringBuilder(pattern.length() * 2);
    ParsePosition pos = new ParsePosition(0);
    int fe = -1;
    int depth = 0;
    while (pos.getIndex() < pattern.length()) {
      char c = pattern.charAt(pos.getIndex());
      switch (c) {
        case '\'':
          appendQuotedString(pattern, pos, sb, false);
          continue;
        case '{':
          depth++;
          sb.append('{').append(readArgumentIndex(pattern, next(pos)));
          if (depth == 1) {
            fe++;
            String customPattern = customPatterns.get(fe);
            if (customPattern != null)
              sb.append(',').append(customPattern); 
          } 
          continue;
        case '}':
          depth--;
          break;
      } 
      sb.append(c);
      next(pos);
    } 
    return sb.toString();
  }
  
  private void seekNonWs(String pattern, ParsePosition pos) {
    int len = 0;
    char[] buffer = pattern.toCharArray();
    do {
      len = StrMatcher.splitMatcher().isMatch(buffer, pos.getIndex());
      pos.setIndex(pos.getIndex() + len);
    } while (len > 0 && pos.getIndex() < pattern.length());
  }
  
  private ParsePosition next(ParsePosition pos) {
    pos.setIndex(pos.getIndex() + 1);
    return pos;
  }
  
  private StringBuilder appendQuotedString(String pattern, ParsePosition pos, StringBuilder appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == '\'') {
      next(pos);
      return (appendTo == null) ? null : appendTo.append('\'');
    } 
    int lastHold = start;
    for (int i = pos.getIndex(); i < pattern.length(); i++) {
      if (escapingOn && pattern.substring(i).startsWith("''")) {
        appendTo.append(c, lastHold, pos.getIndex() - lastHold).append('\'');
        pos.setIndex(i + "''".length());
        lastHold = pos.getIndex();
      } else {
        switch (c[pos.getIndex()]) {
          case '\'':
            next(pos);
            return (appendTo == null) ? null : appendTo.append(c, lastHold, pos.getIndex() - lastHold);
        } 
        next(pos);
      } 
    } 
    throw new IllegalArgumentException("Unterminated quoted string at position " + start);
  }
  
  private void getQuotedString(String pattern, ParsePosition pos, boolean escapingOn) {
    appendQuotedString(pattern, pos, (StringBuilder)null, escapingOn);
  }
  
  private boolean containsElements(Collection<?> coll) {
    if (coll == null || coll.isEmpty())
      return false; 
    for (Object name : coll) {
      if (name != null)
        return true; 
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\text\ExtendedMessageFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */