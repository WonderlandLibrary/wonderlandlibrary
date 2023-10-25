package org.apache.logging.log4j.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.core.pattern.JAnsiTextRenderer;
import org.apache.logging.log4j.core.pattern.PlainTextRenderer;
import org.apache.logging.log4j.core.pattern.TextRenderer;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.util.Patterns;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Strings;

public final class ThrowableFormatOptions {
  private static final int DEFAULT_LINES = 2147483647;
  
  protected static final ThrowableFormatOptions DEFAULT = new ThrowableFormatOptions();
  
  private static final String FULL = "full";
  
  private static final String NONE = "none";
  
  private static final String SHORT = "short";
  
  private final TextRenderer textRenderer;
  
  private final int lines;
  
  private final String separator;
  
  private final String suffix;
  
  private final List<String> ignorePackages;
  
  public static final String CLASS_NAME = "short.className";
  
  public static final String METHOD_NAME = "short.methodName";
  
  public static final String LINE_NUMBER = "short.lineNumber";
  
  public static final String FILE_NAME = "short.fileName";
  
  public static final String MESSAGE = "short.message";
  
  public static final String LOCALIZED_MESSAGE = "short.localizedMessage";
  
  protected ThrowableFormatOptions(int lines, String separator, List<String> ignorePackages, TextRenderer textRenderer, String suffix) {
    this.lines = lines;
    this.separator = (separator == null) ? Strings.LINE_SEPARATOR : separator;
    this.ignorePackages = ignorePackages;
    this.textRenderer = (textRenderer == null) ? (TextRenderer)PlainTextRenderer.getInstance() : textRenderer;
    this.suffix = suffix;
  }
  
  protected ThrowableFormatOptions(List<String> packages) {
    this(2147483647, null, packages, null, null);
  }
  
  protected ThrowableFormatOptions() {
    this(2147483647, null, null, null, null);
  }
  
  public int getLines() {
    return this.lines;
  }
  
  public String getSeparator() {
    return this.separator;
  }
  
  public TextRenderer getTextRenderer() {
    return this.textRenderer;
  }
  
  public List<String> getIgnorePackages() {
    return this.ignorePackages;
  }
  
  public boolean allLines() {
    return (this.lines == Integer.MAX_VALUE);
  }
  
  public boolean anyLines() {
    return (this.lines > 0);
  }
  
  public int minLines(int maxLines) {
    return (this.lines > maxLines) ? maxLines : this.lines;
  }
  
  public boolean hasPackages() {
    return (this.ignorePackages != null && !this.ignorePackages.isEmpty());
  }
  
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append('{')
      .append(allLines() ? "full" : ((this.lines == 2) ? "short" : (anyLines() ? String.valueOf(this.lines) : "none")))
      .append('}');
    s.append("{separator(").append(this.separator).append(")}");
    if (hasPackages()) {
      s.append("{filters(");
      for (String p : this.ignorePackages)
        s.append(p).append(','); 
      s.deleteCharAt(s.length() - 1);
      s.append(")}");
    } 
    return s.toString();
  }
  
  public static ThrowableFormatOptions newInstance(String[] options) {
    JAnsiTextRenderer jAnsiTextRenderer;
    if (options == null || options.length == 0)
      return DEFAULT; 
    if (options.length == 1 && Strings.isNotEmpty(options[0])) {
      String[] opts = options[0].split(Patterns.COMMA_SEPARATOR, 2);
      String first = opts[0].trim();
      try (Scanner scanner = new Scanner(first)) {
        if (opts.length > 1 && (first.equalsIgnoreCase("full") || first.equalsIgnoreCase("short") || first
          .equalsIgnoreCase("none") || scanner.hasNextInt()))
          options = new String[] { first, opts[1].trim() }; 
      } 
    } 
    int lines = DEFAULT.lines;
    String separator = DEFAULT.separator;
    List<String> packages = DEFAULT.ignorePackages;
    TextRenderer ansiRenderer = DEFAULT.textRenderer;
    String suffix = DEFAULT.getSuffix();
    for (String rawOption : options) {
      if (rawOption != null) {
        String option = rawOption.trim();
        if (!option.isEmpty())
          if (option.startsWith("separator(") && option.endsWith(")")) {
            separator = option.substring("separator(".length(), option.length() - 1);
          } else if (option.startsWith("filters(") && option.endsWith(")")) {
            String filterStr = option.substring("filters(".length(), option.length() - 1);
            if (filterStr.length() > 0) {
              String[] array = filterStr.split(Patterns.COMMA_SEPARATOR);
              if (array.length > 0) {
                packages = new ArrayList<>(array.length);
                for (String token : array) {
                  token = token.trim();
                  if (token.length() > 0)
                    packages.add(token); 
                } 
              } 
            } 
          } else if (option.equalsIgnoreCase("none")) {
            lines = 0;
          } else if (option.equalsIgnoreCase("short") || option.equalsIgnoreCase("short.className") || option
            .equalsIgnoreCase("short.methodName") || option.equalsIgnoreCase("short.lineNumber") || option
            .equalsIgnoreCase("short.fileName") || option.equalsIgnoreCase("short.message") || option
            .equalsIgnoreCase("short.localizedMessage")) {
            lines = 2;
          } else if ((option.startsWith("ansi(") && option.endsWith(")")) || option.equals("ansi")) {
            if (Loader.isJansiAvailable()) {
              String styleMapStr = option.equals("ansi") ? "" : option.substring("ansi(".length(), option.length() - 1);
              jAnsiTextRenderer = new JAnsiTextRenderer(new String[] { null, styleMapStr }, JAnsiTextRenderer.DefaultExceptionStyleMap);
            } else {
              StatusLogger.getLogger().warn("You requested ANSI exception rendering but JANSI is not on the classpath. Please see https://logging.apache.org/log4j/2.x/runtime-dependencies.html");
            } 
          } else if (option.startsWith("S(") && option.endsWith(")")) {
            suffix = option.substring("S(".length(), option.length() - 1);
          } else if (option.startsWith("suffix(") && option.endsWith(")")) {
            suffix = option.substring("suffix(".length(), option.length() - 1);
          } else if (!option.equalsIgnoreCase("full")) {
            lines = Integer.parseInt(option);
          }  
      } 
    } 
    return new ThrowableFormatOptions(lines, separator, packages, (TextRenderer)jAnsiTextRenderer, suffix);
  }
  
  public String getSuffix() {
    return this.suffix;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\impl\ThrowableFormatOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */