package org.apache.logging.log4j.core.lookup;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationAware;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Strings;

public class StrSubstitutor implements ConfigurationAware {
  public static final char DEFAULT_ESCAPE = '$';
  
  public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
  
  public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
  
  public static final String DEFAULT_VALUE_DELIMITER_STRING = ":-";
  
  public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");
  
  public static final String ESCAPE_DELIMITER_STRING = ":\\-";
  
  public static final StrMatcher DEFAULT_VALUE_ESCAPE_DELIMITER = StrMatcher.stringMatcher(":\\-");
  
  private static final int BUF_SIZE = 256;
  
  private char escapeChar;
  
  private StrMatcher prefixMatcher;
  
  private StrMatcher suffixMatcher;
  
  private String valueDelimiterString;
  
  private StrMatcher valueDelimiterMatcher;
  
  private StrMatcher valueEscapeDelimiterMatcher;
  
  private StrLookup variableResolver;
  
  private boolean enableSubstitutionInVariables = true;
  
  private Configuration configuration;
  
  private boolean recursiveEvaluationAllowed;
  
  public StrSubstitutor() {
    this((StrLookup)null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
  }
  
  public StrSubstitutor(Map<String, String> valueMap) {
    this(new MapLookup(valueMap), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
  }
  
  public StrSubstitutor(Map<String, String> valueMap, String prefix, String suffix) {
    this(new MapLookup(valueMap), prefix, suffix, '$');
  }
  
  public StrSubstitutor(Map<String, String> valueMap, String prefix, String suffix, char escape) {
    this(new MapLookup(valueMap), prefix, suffix, escape);
  }
  
  public StrSubstitutor(Map<String, String> valueMap, String prefix, String suffix, char escape, String valueDelimiter) {
    this(new MapLookup(valueMap), prefix, suffix, escape, valueDelimiter);
  }
  
  public StrSubstitutor(Properties properties) {
    this(toTypeSafeMap(properties));
  }
  
  public StrSubstitutor(StrLookup variableResolver) {
    this(variableResolver, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
  }
  
  public StrSubstitutor(StrLookup variableResolver, String prefix, String suffix, char escape) {
    setVariableResolver(variableResolver);
    setVariablePrefix(prefix);
    setVariableSuffix(suffix);
    setEscapeChar(escape);
  }
  
  public StrSubstitutor(StrLookup variableResolver, String prefix, String suffix, char escape, String valueDelimiter) {
    setVariableResolver(variableResolver);
    setVariablePrefix(prefix);
    setVariableSuffix(suffix);
    setEscapeChar(escape);
    setValueDelimiter(valueDelimiter);
  }
  
  public StrSubstitutor(StrLookup variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape) {
    this(variableResolver, prefixMatcher, suffixMatcher, escape, DEFAULT_VALUE_DELIMITER, DEFAULT_VALUE_ESCAPE_DELIMITER);
    this.valueDelimiterString = ":-";
  }
  
  public StrSubstitutor(StrLookup variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape, StrMatcher valueDelimiterMatcher) {
    setVariableResolver(variableResolver);
    setVariablePrefixMatcher(prefixMatcher);
    setVariableSuffixMatcher(suffixMatcher);
    setEscapeChar(escape);
    setValueDelimiterMatcher(valueDelimiterMatcher);
  }
  
  public StrSubstitutor(StrLookup variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape, StrMatcher valueDelimiterMatcher, StrMatcher valueEscapeMatcher) {
    setVariableResolver(variableResolver);
    setVariablePrefixMatcher(prefixMatcher);
    setVariableSuffixMatcher(suffixMatcher);
    setEscapeChar(escape);
    setValueDelimiterMatcher(valueDelimiterMatcher);
    this.valueEscapeDelimiterMatcher = valueEscapeMatcher;
  }
  
  StrSubstitutor(StrSubstitutor other) {
    Objects.requireNonNull(other, "other");
    setVariableResolver(other.getVariableResolver());
    setVariablePrefixMatcher(other.getVariablePrefixMatcher());
    setVariableSuffixMatcher(other.getVariableSuffixMatcher());
    setEscapeChar(other.getEscapeChar());
    setValueDelimiterMatcher(other.valueDelimiterMatcher);
    this.valueEscapeDelimiterMatcher = other.valueEscapeDelimiterMatcher;
    this.configuration = other.configuration;
    this.recursiveEvaluationAllowed = other.isRecursiveEvaluationAllowed();
    this.enableSubstitutionInVariables = other.isEnableSubstitutionInVariables();
    this.valueDelimiterString = other.valueDelimiterString;
  }
  
  public static String replace(Object source, Map<String, String> valueMap) {
    return (new StrSubstitutor(valueMap)).replace(source);
  }
  
  public static String replace(Object source, Map<String, String> valueMap, String prefix, String suffix) {
    return (new StrSubstitutor(valueMap, prefix, suffix)).replace(source);
  }
  
  public static String replace(Object source, Properties valueProperties) {
    if (valueProperties == null)
      return source.toString(); 
    Map<String, String> valueMap = new HashMap<>();
    Enumeration<?> propNames = valueProperties.propertyNames();
    while (propNames.hasMoreElements()) {
      String propName = (String)propNames.nextElement();
      String propValue = valueProperties.getProperty(propName);
      valueMap.put(propName, propValue);
    } 
    return replace(source, valueMap);
  }
  
  private static Map<String, String> toTypeSafeMap(Properties properties) {
    Map<String, String> map = new HashMap<>(properties.size());
    for (String name : properties.stringPropertyNames())
      map.put(name, properties.getProperty(name)); 
    return map;
  }
  
  private static String handleFailedReplacement(String input, Throwable throwable) {
    StatusLogger.getLogger().error("Replacement failed on {}", input, throwable);
    return input;
  }
  
  public String replace(String source) {
    return replace((LogEvent)null, source);
  }
  
  public String replace(LogEvent event, String source) {
    if (source == null)
      return null; 
    StringBuilder buf = new StringBuilder(source);
    try {
      if (!substitute(event, buf, 0, source.length()))
        return source; 
    } catch (Throwable t) {
      return handleFailedReplacement(source, t);
    } 
    return buf.toString();
  }
  
  public String replace(String source, int offset, int length) {
    return replace((LogEvent)null, source, offset, length);
  }
  
  public String replace(LogEvent event, String source, int offset, int length) {
    if (source == null)
      return null; 
    StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
    try {
      if (!substitute(event, buf, 0, length))
        return source.substring(offset, offset + length); 
    } catch (Throwable t) {
      return handleFailedReplacement(source, t);
    } 
    return buf.toString();
  }
  
  public String replace(char[] source) {
    return replace((LogEvent)null, source);
  }
  
  public String replace(LogEvent event, char[] source) {
    if (source == null)
      return null; 
    StringBuilder buf = (new StringBuilder(source.length)).append(source);
    try {
      substitute(event, buf, 0, source.length);
    } catch (Throwable t) {
      return handleFailedReplacement(new String(source), t);
    } 
    return buf.toString();
  }
  
  public String replace(char[] source, int offset, int length) {
    return replace((LogEvent)null, source, offset, length);
  }
  
  public String replace(LogEvent event, char[] source, int offset, int length) {
    if (source == null)
      return null; 
    StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
    try {
      substitute(event, buf, 0, length);
    } catch (Throwable t) {
      return handleFailedReplacement(new String(source, offset, length), t);
    } 
    return buf.toString();
  }
  
  public String replace(StringBuffer source) {
    return replace((LogEvent)null, source);
  }
  
  public String replace(LogEvent event, StringBuffer source) {
    if (source == null)
      return null; 
    StringBuilder buf = (new StringBuilder(source.length())).append(source);
    try {
      substitute(event, buf, 0, buf.length());
    } catch (Throwable t) {
      return handleFailedReplacement(source.toString(), t);
    } 
    return buf.toString();
  }
  
  public String replace(StringBuffer source, int offset, int length) {
    return replace((LogEvent)null, source, offset, length);
  }
  
  public String replace(LogEvent event, StringBuffer source, int offset, int length) {
    if (source == null)
      return null; 
    StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
    try {
      substitute(event, buf, 0, length);
    } catch (Throwable t) {
      return handleFailedReplacement(source.substring(offset, offset + length), t);
    } 
    return buf.toString();
  }
  
  public String replace(StringBuilder source) {
    return replace((LogEvent)null, source);
  }
  
  public String replace(LogEvent event, StringBuilder source) {
    if (source == null)
      return null; 
    StringBuilder buf = (new StringBuilder(source.length())).append(source);
    try {
      substitute(event, buf, 0, buf.length());
    } catch (Throwable t) {
      return handleFailedReplacement(source.toString(), t);
    } 
    return buf.toString();
  }
  
  public String replace(StringBuilder source, int offset, int length) {
    return replace((LogEvent)null, source, offset, length);
  }
  
  public String replace(LogEvent event, StringBuilder source, int offset, int length) {
    if (source == null)
      return null; 
    StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
    try {
      substitute(event, buf, 0, length);
    } catch (Throwable t) {
      return handleFailedReplacement(source.substring(offset, offset + length), t);
    } 
    return buf.toString();
  }
  
  public String replace(Object source) {
    return replace((LogEvent)null, source);
  }
  
  public String replace(LogEvent event, Object source) {
    if (source == null)
      return null; 
    String stringValue = String.valueOf(source);
    StringBuilder buf = (new StringBuilder(stringValue.length())).append(stringValue);
    try {
      substitute(event, buf, 0, buf.length());
    } catch (Throwable t) {
      return handleFailedReplacement(stringValue, t);
    } 
    return buf.toString();
  }
  
  public boolean replaceIn(StringBuffer source) {
    if (source == null)
      return false; 
    return replaceIn(source, 0, source.length());
  }
  
  public boolean replaceIn(StringBuffer source, int offset, int length) {
    return replaceIn((LogEvent)null, source, offset, length);
  }
  
  public boolean replaceIn(LogEvent event, StringBuffer source, int offset, int length) {
    if (source == null)
      return false; 
    StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
    try {
      if (!substitute(event, buf, 0, length))
        return false; 
    } catch (Throwable t) {
      StatusLogger.getLogger().error("Replacement failed on {}", source, t);
      return false;
    } 
    source.replace(offset, offset + length, buf.toString());
    return true;
  }
  
  public boolean replaceIn(StringBuilder source) {
    return replaceIn(null, source);
  }
  
  public boolean replaceIn(LogEvent event, StringBuilder source) {
    if (source == null)
      return false; 
    return substitute(event, source, 0, source.length());
  }
  
  public boolean replaceIn(StringBuilder source, int offset, int length) {
    return replaceIn((LogEvent)null, source, offset, length);
  }
  
  public boolean replaceIn(LogEvent event, StringBuilder source, int offset, int length) {
    if (source == null)
      return false; 
    return substitute(event, source, offset, length);
  }
  
  protected boolean substitute(LogEvent event, StringBuilder buf, int offset, int length) {
    return (substitute(event, buf, offset, length, null) > 0);
  }
  
  private int substitute(LogEvent event, StringBuilder buf, int offset, int length, List<String> priorVariables) {
    StrMatcher prefixMatcher = getVariablePrefixMatcher();
    StrMatcher suffixMatcher = getVariableSuffixMatcher();
    char escape = getEscapeChar();
    StrMatcher valueDelimiterMatcher = getValueDelimiterMatcher();
    boolean substitutionInVariablesEnabled = isEnableSubstitutionInVariables();
    boolean top = (priorVariables == null);
    boolean altered = false;
    int lengthChange = 0;
    char[] chars = getChars(buf);
    int bufEnd = offset + length;
    int pos = offset;
    while (pos < bufEnd) {
      int startMatchLen = prefixMatcher.isMatch(chars, pos, offset, bufEnd);
      if (startMatchLen == 0) {
        pos++;
        continue;
      } 
      if (pos > offset && chars[pos - 1] == escape) {
        buf.deleteCharAt(pos - 1);
        chars = getChars(buf);
        lengthChange--;
        altered = true;
        bufEnd--;
        continue;
      } 
      int startPos = pos;
      pos += startMatchLen;
      int endMatchLen = 0;
      int nestedVarCount = 0;
      while (pos < bufEnd) {
        if (substitutionInVariablesEnabled && (
          endMatchLen = prefixMatcher.isMatch(chars, pos, offset, bufEnd)) != 0) {
          nestedVarCount++;
          pos += endMatchLen;
          continue;
        } 
        endMatchLen = suffixMatcher.isMatch(chars, pos, offset, bufEnd);
        if (endMatchLen == 0) {
          pos++;
          continue;
        } 
        if (nestedVarCount == 0) {
          String varNameExpr = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);
          if (substitutionInVariablesEnabled) {
            if (priorVariables == null)
              priorVariables = new ArrayList<>(); 
            StringBuilder bufName = new StringBuilder(varNameExpr);
            substitute(event, bufName, 0, bufName.length(), priorVariables);
            varNameExpr = bufName.toString();
          } 
          pos += endMatchLen;
          int endPos = pos;
          String varName = varNameExpr;
          String varDefaultValue = null;
          if (valueDelimiterMatcher != null) {
            char[] varNameExprChars = varNameExpr.toCharArray();
            int valueDelimiterMatchLen = 0;
            for (int i = 0; i < varNameExprChars.length; i++) {
              if (!substitutionInVariablesEnabled && prefixMatcher
                .isMatch(varNameExprChars, i, i, varNameExprChars.length) != 0)
                break; 
              if (this.valueEscapeDelimiterMatcher != null) {
                int matchLen = this.valueEscapeDelimiterMatcher.isMatch(varNameExprChars, i);
                if (matchLen != 0) {
                  String varNamePrefix = varNameExpr.substring(0, i) + ':';
                  varName = varNamePrefix + varNameExpr.substring(i + matchLen - 1);
                  for (int j = i + matchLen; j < varNameExprChars.length; j++) {
                    if ((valueDelimiterMatchLen = valueDelimiterMatcher.isMatch(varNameExprChars, j)) != 0) {
                      varName = varNamePrefix + varNameExpr.substring(i + matchLen, j);
                      varDefaultValue = varNameExpr.substring(j + valueDelimiterMatchLen);
                      break;
                    } 
                  } 
                  break;
                } 
                if ((valueDelimiterMatchLen = valueDelimiterMatcher.isMatch(varNameExprChars, i)) != 0) {
                  varName = varNameExpr.substring(0, i);
                  varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
                  break;
                } 
              } else if ((valueDelimiterMatchLen = valueDelimiterMatcher.isMatch(varNameExprChars, i)) != 0) {
                varName = varNameExpr.substring(0, i);
                varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
                break;
              } 
            } 
          } 
          if (priorVariables == null) {
            priorVariables = new ArrayList<>();
            priorVariables.add(new String(chars, offset, length + lengthChange));
          } 
          boolean isCyclic = isCyclicSubstitution(varName, priorVariables);
          String varValue = isCyclic ? null : resolveVariable(event, varName, buf, startPos, endPos);
          if (varValue == null)
            varValue = varDefaultValue; 
          if (varValue != null) {
            int varLen = varValue.length();
            buf.replace(startPos, endPos, varValue);
            altered = true;
            int change = isRecursiveEvaluationAllowed() ? substitute(event, buf, startPos, varLen, priorVariables) : 0;
            change += varLen - endPos - startPos;
            pos += change;
            bufEnd += change;
            lengthChange += change;
            chars = getChars(buf);
          } 
          if (!isCyclic)
            priorVariables.remove(priorVariables.size() - 1); 
          break;
        } 
        nestedVarCount--;
        pos += endMatchLen;
      } 
    } 
    if (top)
      return altered ? 1 : 0; 
    return lengthChange;
  }
  
  private boolean isCyclicSubstitution(String varName, List<String> priorVariables) {
    if (!priorVariables.contains(varName)) {
      priorVariables.add(varName);
      return false;
    } 
    StringBuilder buf = new StringBuilder(256);
    buf.append("Infinite loop in property interpolation of ");
    appendWithSeparators(buf, priorVariables, "->");
    StatusLogger.getLogger().warn(buf);
    return true;
  }
  
  protected String resolveVariable(LogEvent event, String variableName, StringBuilder buf, int startPos, int endPos) {
    StrLookup resolver = getVariableResolver();
    if (resolver == null)
      return null; 
    try {
      return resolver.lookup(event, variableName);
    } catch (Throwable t) {
      StatusLogger.getLogger().error("Resolver failed to lookup {}", variableName, t);
      return null;
    } 
  }
  
  public char getEscapeChar() {
    return this.escapeChar;
  }
  
  public void setEscapeChar(char escapeCharacter) {
    this.escapeChar = escapeCharacter;
  }
  
  public StrMatcher getVariablePrefixMatcher() {
    return this.prefixMatcher;
  }
  
  public StrSubstitutor setVariablePrefixMatcher(StrMatcher prefixMatcher) {
    if (prefixMatcher == null)
      throw new IllegalArgumentException("Variable prefix matcher must not be null!"); 
    this.prefixMatcher = prefixMatcher;
    return this;
  }
  
  public StrSubstitutor setVariablePrefix(char prefix) {
    return setVariablePrefixMatcher(StrMatcher.charMatcher(prefix));
  }
  
  public StrSubstitutor setVariablePrefix(String prefix) {
    if (prefix == null)
      throw new IllegalArgumentException("Variable prefix must not be null!"); 
    return setVariablePrefixMatcher(StrMatcher.stringMatcher(prefix));
  }
  
  public StrMatcher getVariableSuffixMatcher() {
    return this.suffixMatcher;
  }
  
  public StrSubstitutor setVariableSuffixMatcher(StrMatcher suffixMatcher) {
    if (suffixMatcher == null)
      throw new IllegalArgumentException("Variable suffix matcher must not be null!"); 
    this.suffixMatcher = suffixMatcher;
    return this;
  }
  
  public StrSubstitutor setVariableSuffix(char suffix) {
    return setVariableSuffixMatcher(StrMatcher.charMatcher(suffix));
  }
  
  public StrSubstitutor setVariableSuffix(String suffix) {
    if (suffix == null)
      throw new IllegalArgumentException("Variable suffix must not be null!"); 
    return setVariableSuffixMatcher(StrMatcher.stringMatcher(suffix));
  }
  
  public StrMatcher getValueDelimiterMatcher() {
    return this.valueDelimiterMatcher;
  }
  
  public StrSubstitutor setValueDelimiterMatcher(StrMatcher valueDelimiterMatcher) {
    this.valueDelimiterMatcher = valueDelimiterMatcher;
    return this;
  }
  
  public StrSubstitutor setValueDelimiter(char valueDelimiter) {
    return setValueDelimiterMatcher(StrMatcher.charMatcher(valueDelimiter));
  }
  
  public StrSubstitutor setValueDelimiter(String valueDelimiter) {
    if (Strings.isEmpty(valueDelimiter)) {
      setValueDelimiterMatcher(null);
      return this;
    } 
    String escapeValue = valueDelimiter.substring(0, valueDelimiter.length() - 1) + "\\" + valueDelimiter.substring(valueDelimiter.length() - 1);
    this.valueEscapeDelimiterMatcher = StrMatcher.stringMatcher(escapeValue);
    return setValueDelimiterMatcher(StrMatcher.stringMatcher(valueDelimiter));
  }
  
  public StrLookup getVariableResolver() {
    return this.variableResolver;
  }
  
  public void setVariableResolver(StrLookup variableResolver) {
    if (variableResolver instanceof ConfigurationAware && this.configuration != null)
      ((ConfigurationAware)variableResolver).setConfiguration(this.configuration); 
    this.variableResolver = variableResolver;
  }
  
  public boolean isEnableSubstitutionInVariables() {
    return this.enableSubstitutionInVariables;
  }
  
  public void setEnableSubstitutionInVariables(boolean enableSubstitutionInVariables) {
    this.enableSubstitutionInVariables = enableSubstitutionInVariables;
  }
  
  boolean isRecursiveEvaluationAllowed() {
    return this.recursiveEvaluationAllowed;
  }
  
  void setRecursiveEvaluationAllowed(boolean recursiveEvaluationAllowed) {
    this.recursiveEvaluationAllowed = recursiveEvaluationAllowed;
  }
  
  private char[] getChars(StringBuilder sb) {
    char[] chars = new char[sb.length()];
    sb.getChars(0, sb.length(), chars, 0);
    return chars;
  }
  
  public void appendWithSeparators(StringBuilder sb, Iterable<?> iterable, String separator) {
    if (iterable != null) {
      separator = (separator == null) ? "" : separator;
      Iterator<?> it = iterable.iterator();
      while (it.hasNext()) {
        sb.append(it.next());
        if (it.hasNext())
          sb.append(separator); 
      } 
    } 
  }
  
  public String toString() {
    return "StrSubstitutor(" + this.variableResolver.toString() + ')';
  }
  
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
    if (this.variableResolver instanceof ConfigurationAware)
      ((ConfigurationAware)this.variableResolver).setConfiguration(this.configuration); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\StrSubstitutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */