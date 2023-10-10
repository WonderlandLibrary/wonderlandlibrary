// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.pattern.JAnsiTextRenderer;
import org.apache.logging.log4j.core.util.Loader;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.logging.log4j.core.util.Patterns;
import java.util.Iterator;
import org.apache.logging.log4j.core.pattern.PlainTextRenderer;
import org.apache.logging.log4j.util.Strings;
import java.util.List;
import org.apache.logging.log4j.core.pattern.TextRenderer;

public final class ThrowableFormatOptions
{
    private static final int DEFAULT_LINES = Integer.MAX_VALUE;
    protected static final ThrowableFormatOptions DEFAULT;
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
    
    protected ThrowableFormatOptions(final int lines, final String separator, final List<String> ignorePackages, final TextRenderer textRenderer, final String suffix) {
        this.lines = lines;
        this.separator = ((separator == null) ? Strings.LINE_SEPARATOR : separator);
        this.ignorePackages = ignorePackages;
        this.textRenderer = ((textRenderer == null) ? PlainTextRenderer.getInstance() : textRenderer);
        this.suffix = suffix;
    }
    
    protected ThrowableFormatOptions(final List<String> packages) {
        this(Integer.MAX_VALUE, null, packages, null, null);
    }
    
    protected ThrowableFormatOptions() {
        this(Integer.MAX_VALUE, null, null, null, null);
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
        return this.lines == Integer.MAX_VALUE;
    }
    
    public boolean anyLines() {
        return this.lines > 0;
    }
    
    public int minLines(final int maxLines) {
        return (this.lines > maxLines) ? maxLines : this.lines;
    }
    
    public boolean hasPackages() {
        return this.ignorePackages != null && !this.ignorePackages.isEmpty();
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        s.append('{').append(this.allLines() ? "full" : ((this.lines == 2) ? "short" : (this.anyLines() ? String.valueOf(this.lines) : "none"))).append('}');
        s.append("{separator(").append(this.separator).append(")}");
        if (this.hasPackages()) {
            s.append("{filters(");
            for (final String p : this.ignorePackages) {
                s.append(p).append(',');
            }
            s.deleteCharAt(s.length() - 1);
            s.append(")}");
        }
        return s.toString();
    }
    
    public static ThrowableFormatOptions newInstance(String[] options) {
        if (options == null || options.length == 0) {
            return ThrowableFormatOptions.DEFAULT;
        }
        if (options.length == 1 && Strings.isNotEmpty(options[0])) {
            final String[] opts = options[0].split(Patterns.COMMA_SEPARATOR, 2);
            final String first = opts[0].trim();
            try (final Scanner scanner = new Scanner(first)) {
                if (opts.length > 1 && (first.equalsIgnoreCase("full") || first.equalsIgnoreCase("short") || first.equalsIgnoreCase("none") || scanner.hasNextInt())) {
                    options = new String[] { first, opts[1].trim() };
                }
            }
        }
        int lines = ThrowableFormatOptions.DEFAULT.lines;
        String separator = ThrowableFormatOptions.DEFAULT.separator;
        List<String> packages = ThrowableFormatOptions.DEFAULT.ignorePackages;
        TextRenderer ansiRenderer = ThrowableFormatOptions.DEFAULT.textRenderer;
        String suffix = ThrowableFormatOptions.DEFAULT.getSuffix();
        for (final String rawOption : options) {
            if (rawOption != null) {
                final String option = rawOption.trim();
                if (!option.isEmpty()) {
                    if (option.startsWith("separator(") && option.endsWith(")")) {
                        separator = option.substring("separator(".length(), option.length() - 1);
                    }
                    else if (option.startsWith("filters(") && option.endsWith(")")) {
                        final String filterStr = option.substring("filters(".length(), option.length() - 1);
                        if (filterStr.length() > 0) {
                            final String[] array = filterStr.split(Patterns.COMMA_SEPARATOR);
                            if (array.length > 0) {
                                packages = new ArrayList<String>(array.length);
                                for (String token : array) {
                                    token = token.trim();
                                    if (token.length() > 0) {
                                        packages.add(token);
                                    }
                                }
                            }
                        }
                    }
                    else if (option.equalsIgnoreCase("none")) {
                        lines = 0;
                    }
                    else if (option.equalsIgnoreCase("short") || option.equalsIgnoreCase("short.className") || option.equalsIgnoreCase("short.methodName") || option.equalsIgnoreCase("short.lineNumber") || option.equalsIgnoreCase("short.fileName") || option.equalsIgnoreCase("short.message") || option.equalsIgnoreCase("short.localizedMessage")) {
                        lines = 2;
                    }
                    else if ((option.startsWith("ansi(") && option.endsWith(")")) || option.equals("ansi")) {
                        if (Loader.isJansiAvailable()) {
                            final String styleMapStr = option.equals("ansi") ? "" : option.substring("ansi(".length(), option.length() - 1);
                            ansiRenderer = new JAnsiTextRenderer(new String[] { null, styleMapStr }, JAnsiTextRenderer.DefaultExceptionStyleMap);
                        }
                        else {
                            StatusLogger.getLogger().warn("You requested ANSI exception rendering but JANSI is not on the classpath. Please see https://logging.apache.org/log4j/2.x/runtime-dependencies.html");
                        }
                    }
                    else if (option.startsWith("S(") && option.endsWith(")")) {
                        suffix = option.substring("S(".length(), option.length() - 1);
                    }
                    else if (option.startsWith("suffix(") && option.endsWith(")")) {
                        suffix = option.substring("suffix(".length(), option.length() - 1);
                    }
                    else if (!option.equalsIgnoreCase("full")) {
                        lines = Integer.parseInt(option);
                    }
                }
            }
        }
        return new ThrowableFormatOptions(lines, separator, packages, ansiRenderer, suffix);
    }
    
    public String getSuffix() {
        return this.suffix;
    }
    
    static {
        DEFAULT = new ThrowableFormatOptions();
    }
}
