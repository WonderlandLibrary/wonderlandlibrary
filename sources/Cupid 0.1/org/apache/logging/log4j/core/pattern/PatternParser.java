package org.apache.logging.log4j.core.pattern;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.core.util.SystemNanoClock;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Strings;

public final class PatternParser {
  static final String DISABLE_ANSI = "disableAnsi";
  
  static final String NO_CONSOLE_NO_ANSI = "noConsoleNoAnsi";
  
  private static final char ESCAPE_CHAR = '%';
  
  private enum ParserState {
    LITERAL_STATE, CONVERTER_STATE, DOT_STATE, MIN_STATE, MAX_STATE;
  }
  
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final int BUF_SIZE = 32;
  
  private static final int DECIMAL = 10;
  
  private final Configuration config;
  
  private final Map<String, Class<PatternConverter>> converterRules;
  
  public PatternParser(String converterKey) {
    this(null, converterKey, null, null);
  }
  
  public PatternParser(Configuration config, String converterKey, Class<?> expected) {
    this(config, converterKey, expected, null);
  }
  
  public PatternParser(Configuration config, String converterKey, Class<?> expectedClass, Class<?> filterClass) {
    this.config = config;
    PluginManager manager = new PluginManager(converterKey);
    manager.collectPlugins((config == null) ? null : config.getPluginPackages());
    Map<String, PluginType<?>> plugins = manager.getPlugins();
    Map<String, Class<PatternConverter>> converters = new LinkedHashMap<>();
    for (PluginType<?> type : plugins.values()) {
      try {
        Class<PatternConverter> clazz = type.getPluginClass();
        if (filterClass != null && !filterClass.isAssignableFrom(clazz))
          continue; 
        ConverterKeys keys = clazz.<ConverterKeys>getAnnotation(ConverterKeys.class);
        if (keys != null)
          for (String key : keys.value()) {
            if (converters.containsKey(key)) {
              LOGGER.warn("Converter key '{}' is already mapped to '{}'. Sorry, Dave, I can't let you do that! Ignoring plugin [{}].", key, converters
                  
                  .get(key), clazz);
            } else {
              converters.put(key, clazz);
            } 
          }  
      } catch (Exception ex) {
        LOGGER.error("Error processing plugin " + type.getElementName(), ex);
      } 
    } 
    this.converterRules = converters;
  }
  
  public List<PatternFormatter> parse(String pattern) {
    return parse(pattern, false, false, false);
  }
  
  public List<PatternFormatter> parse(String pattern, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi) {
    return parse(pattern, alwaysWriteExceptions, false, noConsoleNoAnsi);
  }
  
  public List<PatternFormatter> parse(String pattern, boolean alwaysWriteExceptions, boolean disableAnsi, boolean noConsoleNoAnsi) {
    List<PatternFormatter> list = new ArrayList<>();
    List<PatternConverter> converters = new ArrayList<>();
    List<FormattingInfo> fields = new ArrayList<>();
    parse(pattern, converters, fields, disableAnsi, noConsoleNoAnsi, true);
    Iterator<FormattingInfo> fieldIter = fields.iterator();
    boolean handlesThrowable = false;
    for (PatternConverter converter : converters) {
      LogEventPatternConverter pc;
      FormattingInfo field;
      if (converter instanceof NanoTimePatternConverter)
        if (this.config != null)
          this.config.setNanoClock((NanoClock)new SystemNanoClock());  
      if (converter instanceof LogEventPatternConverter) {
        pc = (LogEventPatternConverter)converter;
        handlesThrowable |= pc.handlesThrowable();
      } else {
        pc = SimpleLiteralPatternConverter.of("");
      } 
      if (fieldIter.hasNext()) {
        field = fieldIter.next();
      } else {
        field = FormattingInfo.getDefault();
      } 
      list.add(new PatternFormatter(pc, field));
    } 
    if (alwaysWriteExceptions && !handlesThrowable) {
      LogEventPatternConverter pc = ExtendedThrowablePatternConverter.newInstance(this.config, null);
      list.add(new PatternFormatter(pc, FormattingInfo.getDefault()));
    } 
    return list;
  }
  
  private static int extractConverter(char lastChar, String pattern, int start, StringBuilder convBuf, StringBuilder currentLiteral) {
    int i = start;
    convBuf.setLength(0);
    if (!Character.isUnicodeIdentifierStart(lastChar))
      return i; 
    convBuf.append(lastChar);
    while (i < pattern.length() && Character.isUnicodeIdentifierPart(pattern.charAt(i))) {
      convBuf.append(pattern.charAt(i));
      currentLiteral.append(pattern.charAt(i));
      i++;
    } 
    return i;
  }
  
  private static int extractOptions(String pattern, int start, List<String> options) {
    int i = start;
    while (i < pattern.length() && pattern.charAt(i) == '{') {
      int begin = ++i;
      int depth = 1;
      while (depth > 0 && i < pattern.length()) {
        char c = pattern.charAt(i);
        if (c == '{') {
          depth++;
        } else if (c == '}') {
          depth--;
        } 
        i++;
      } 
      if (depth > 0) {
        i = pattern.lastIndexOf('}');
        if (i == -1 || i < start)
          return begin; 
        return i + 1;
      } 
      options.add(pattern.substring(begin, i - 1));
    } 
    return i;
  }
  
  public void parse(String pattern, List<PatternConverter> patternConverters, List<FormattingInfo> formattingInfos, boolean noConsoleNoAnsi, boolean convertBackslashes) {
    parse(pattern, patternConverters, formattingInfos, false, noConsoleNoAnsi, convertBackslashes);
  }
  
  public void parse(String pattern, List<PatternConverter> patternConverters, List<FormattingInfo> formattingInfos, boolean disableAnsi, boolean noConsoleNoAnsi, boolean convertBackslashes) {
    Objects.requireNonNull(pattern, "pattern");
    StringBuilder currentLiteral = new StringBuilder(32);
    int patternLength = pattern.length();
    ParserState state = ParserState.LITERAL_STATE;
    int i = 0;
    FormattingInfo formattingInfo = FormattingInfo.getDefault();
    while (i < patternLength) {
      char c = pattern.charAt(i++);
      switch (state) {
        case LITERAL_STATE:
          if (i == patternLength) {
            currentLiteral.append(c);
            continue;
          } 
          if (c == '%') {
            switch (pattern.charAt(i)) {
              case '%':
                currentLiteral.append(c);
                i++;
                continue;
            } 
            if (currentLiteral.length() != 0) {
              patternConverters.add(literalPattern(currentLiteral.toString(), convertBackslashes));
              formattingInfos.add(FormattingInfo.getDefault());
            } 
            currentLiteral.setLength(0);
            currentLiteral.append(c);
            state = ParserState.CONVERTER_STATE;
            formattingInfo = FormattingInfo.getDefault();
            continue;
          } 
          currentLiteral.append(c);
        case CONVERTER_STATE:
          currentLiteral.append(c);
          switch (c) {
            case '0':
              formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), formattingInfo.getMaxLength(), formattingInfo.isLeftTruncate(), true);
              continue;
            case '-':
              formattingInfo = new FormattingInfo(true, formattingInfo.getMinLength(), formattingInfo.getMaxLength(), formattingInfo.isLeftTruncate(), formattingInfo.isZeroPad());
              continue;
            case '.':
              state = ParserState.DOT_STATE;
              continue;
          } 
          if (c >= '0' && c <= '9') {
            formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), c - 48, formattingInfo.getMaxLength(), formattingInfo.isLeftTruncate(), formattingInfo.isZeroPad());
            state = ParserState.MIN_STATE;
            continue;
          } 
          i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, this.converterRules, patternConverters, formattingInfos, disableAnsi, noConsoleNoAnsi, convertBackslashes);
          state = ParserState.LITERAL_STATE;
          formattingInfo = FormattingInfo.getDefault();
          currentLiteral.setLength(0);
        case MIN_STATE:
          currentLiteral.append(c);
          if (c >= '0' && c <= '9') {
            formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength() * 10 + c - 48, formattingInfo.getMaxLength(), formattingInfo.isLeftTruncate(), formattingInfo.isZeroPad());
            continue;
          } 
          if (c == '.') {
            state = ParserState.DOT_STATE;
            continue;
          } 
          i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, this.converterRules, patternConverters, formattingInfos, disableAnsi, noConsoleNoAnsi, convertBackslashes);
          state = ParserState.LITERAL_STATE;
          formattingInfo = FormattingInfo.getDefault();
          currentLiteral.setLength(0);
        case DOT_STATE:
          currentLiteral.append(c);
          switch (c) {
            case '-':
              formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), formattingInfo.getMaxLength(), false, formattingInfo.isZeroPad());
              continue;
          } 
          if (c >= '0' && c <= '9') {
            formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), c - 48, formattingInfo.isLeftTruncate(), formattingInfo.isZeroPad());
            state = ParserState.MAX_STATE;
            continue;
          } 
          LOGGER.error("Error occurred in position " + i + ".\n Was expecting digit, instead got char \"" + c + "\".");
          state = ParserState.LITERAL_STATE;
        case MAX_STATE:
          currentLiteral.append(c);
          if (c >= '0' && c <= '9') {
            formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), formattingInfo.getMaxLength() * 10 + c - 48, formattingInfo.isLeftTruncate(), formattingInfo.isZeroPad());
            continue;
          } 
          i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, this.converterRules, patternConverters, formattingInfos, disableAnsi, noConsoleNoAnsi, convertBackslashes);
          state = ParserState.LITERAL_STATE;
          formattingInfo = FormattingInfo.getDefault();
          currentLiteral.setLength(0);
      } 
    } 
    if (currentLiteral.length() != 0) {
      patternConverters.add(literalPattern(currentLiteral.toString(), convertBackslashes));
      formattingInfos.add(FormattingInfo.getDefault());
    } 
  }
  
  private PatternConverter createConverter(String converterId, StringBuilder currentLiteral, Map<String, Class<PatternConverter>> rules, List<String> options, boolean disableAnsi, boolean noConsoleNoAnsi) {
    String converterName = converterId;
    Class<PatternConverter> converterClass = null;
    if (rules == null) {
      LOGGER.error("Null rules for [" + converterId + ']');
      return null;
    } 
    for (int i = converterId.length(); i > 0 && converterClass == null; i--) {
      converterName = converterName.substring(0, i);
      converterClass = rules.get(converterName);
    } 
    if (converterClass == null) {
      LOGGER.error("Unrecognized format specifier [" + converterId + ']');
      return null;
    } 
    if (AnsiConverter.class.isAssignableFrom(converterClass)) {
      options.add("disableAnsi=" + disableAnsi);
      options.add("noConsoleNoAnsi=" + noConsoleNoAnsi);
    } 
    Method[] methods = converterClass.getDeclaredMethods();
    Method newInstanceMethod = null;
    for (Method method : methods) {
      if (Modifier.isStatic(method.getModifiers()) && method
        .getDeclaringClass().equals(converterClass) && method
        .getName().equals("newInstance") && 
        areValidNewInstanceParameters(method.getParameterTypes()))
        if (newInstanceMethod == null) {
          newInstanceMethod = method;
        } else if (method.getReturnType().equals(newInstanceMethod.getReturnType())) {
          LOGGER.error("Class " + converterClass + " cannot contain multiple static newInstance methods");
          return null;
        }  
    } 
    if (newInstanceMethod == null) {
      LOGGER.error("Class " + converterClass + " does not contain a static newInstance method");
      return null;
    } 
    Class<?>[] parmTypes = newInstanceMethod.getParameterTypes();
    Object[] parms = (parmTypes.length > 0) ? new Object[parmTypes.length] : null;
    if (parms != null) {
      int j = 0;
      boolean errors = false;
      for (Class<?> clazz : parmTypes) {
        if (clazz.isArray() && clazz.getName().equals("[Ljava.lang.String;")) {
          String[] optionsArray = options.<String>toArray(new String[options.size()]);
          parms[j] = optionsArray;
        } else if (clazz.isAssignableFrom(Configuration.class)) {
          parms[j] = this.config;
        } else {
          LOGGER.error("Unknown parameter type " + clazz.getName() + " for static newInstance method of " + converterClass
              .getName());
          errors = true;
        } 
        j++;
      } 
      if (errors)
        return null; 
    } 
    try {
      Object newObj = newInstanceMethod.invoke(null, parms);
      if (newObj instanceof PatternConverter) {
        currentLiteral.delete(0, currentLiteral.length() - converterId.length() - converterName.length());
        return (PatternConverter)newObj;
      } 
      LOGGER.warn("Class {} does not extend PatternConverter.", converterClass.getName());
    } catch (Exception ex) {
      LOGGER.error("Error creating converter for " + converterId, ex);
    } 
    return null;
  }
  
  private static boolean areValidNewInstanceParameters(Class<?>[] parameterTypes) {
    for (Class<?> clazz : parameterTypes) {
      if (!clazz.isAssignableFrom(Configuration.class) && (
        !clazz.isArray() || !"[Ljava.lang.String;".equals(clazz.getName())))
        return false; 
    } 
    return true;
  }
  
  private int finalizeConverter(char c, String pattern, int start, StringBuilder currentLiteral, FormattingInfo formattingInfo, Map<String, Class<PatternConverter>> rules, List<PatternConverter> patternConverters, List<FormattingInfo> formattingInfos, boolean disableAnsi, boolean noConsoleNoAnsi, boolean convertBackslashes) {
    int i = start;
    StringBuilder convBuf = new StringBuilder();
    i = extractConverter(c, pattern, i, convBuf, currentLiteral);
    String converterId = convBuf.toString();
    List<String> options = new ArrayList<>();
    i = extractOptions(pattern, i, options);
    PatternConverter pc = createConverter(converterId, currentLiteral, rules, options, disableAnsi, noConsoleNoAnsi);
    if (pc == null) {
      StringBuilder msg;
      if (Strings.isEmpty(converterId)) {
        msg = new StringBuilder("Empty conversion specifier starting at position ");
      } else {
        msg = new StringBuilder("Unrecognized conversion specifier [");
        msg.append(converterId);
        msg.append("] starting at position ");
      } 
      msg.append(i);
      msg.append(" in conversion pattern.");
      LOGGER.error(msg.toString());
      patternConverters.add(literalPattern(currentLiteral.toString(), convertBackslashes));
      formattingInfos.add(FormattingInfo.getDefault());
    } else {
      patternConverters.add(pc);
      formattingInfos.add(formattingInfo);
      if (currentLiteral.length() > 0) {
        patternConverters
          .add(literalPattern(currentLiteral.toString(), convertBackslashes));
        formattingInfos.add(FormattingInfo.getDefault());
      } 
    } 
    currentLiteral.setLength(0);
    return i;
  }
  
  private LogEventPatternConverter literalPattern(String literal, boolean convertBackslashes) {
    if (this.config != null && LiteralPatternConverter.containsSubstitutionSequence(literal))
      return new LiteralPatternConverter(this.config, literal, convertBackslashes); 
    return SimpleLiteralPatternConverter.of(literal, convertBackslashes);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\PatternParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */