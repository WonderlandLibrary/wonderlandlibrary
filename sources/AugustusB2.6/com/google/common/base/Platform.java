// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.ServiceConfigurationError;
import javax.annotation.CheckForNull;
import java.util.Locale;
import java.lang.ref.WeakReference;
import java.util.logging.Logger;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
final class Platform
{
    private static final Logger logger;
    private static final PatternCompiler patternCompiler;
    
    private Platform() {
    }
    
    static long systemNanoTime() {
        return System.nanoTime();
    }
    
    static CharMatcher precomputeCharMatcher(final CharMatcher matcher) {
        return matcher.precomputedInternal();
    }
    
    static <T extends Enum<T>> Optional<T> getEnumIfPresent(final Class<T> enumClass, final String value) {
        final WeakReference<? extends Enum<?>> ref = Enums.getEnumConstants(enumClass).get(value);
        return (ref == null) ? Optional.absent() : Optional.of(enumClass.cast(ref.get()));
    }
    
    static String formatCompact4Digits(final double value) {
        return String.format(Locale.ROOT, "%.4g", value);
    }
    
    static boolean stringIsNullOrEmpty(@CheckForNull final String string) {
        return string == null || string.isEmpty();
    }
    
    static String nullToEmpty(@CheckForNull final String string) {
        return (string == null) ? "" : string;
    }
    
    @CheckForNull
    static String emptyToNull(@CheckForNull final String string) {
        return stringIsNullOrEmpty(string) ? null : string;
    }
    
    static CommonPattern compilePattern(final String pattern) {
        Preconditions.checkNotNull(pattern);
        return Platform.patternCompiler.compile(pattern);
    }
    
    static boolean patternCompilerIsPcreLike() {
        return Platform.patternCompiler.isPcreLike();
    }
    
    private static PatternCompiler loadPatternCompiler() {
        return new JdkPatternCompiler();
    }
    
    private static void logPatternCompilerError(final ServiceConfigurationError e) {
        Platform.logger.log(Level.WARNING, "Error loading regex compiler, falling back to next option", e);
    }
    
    static void checkGwtRpcEnabled() {
        final String propertyName = "guava.gwt.emergency_reenable_rpc";
        if (!Boolean.parseBoolean(System.getProperty(propertyName, "false"))) {
            throw new UnsupportedOperationException(Strings.lenientFormat("We are removing GWT-RPC support for Guava types. You can temporarily reenable support by setting the system property %s to true. For more about system properties, see %s. For more about Guava's GWT-RPC support, see %s.", propertyName, "https://stackoverflow.com/q/5189914/28465", "https://groups.google.com/d/msg/guava-announce/zHZTFg7YF3o/rQNnwdHeEwAJ"));
        }
        Platform.logger.log(Level.WARNING, "Later in 2020, we will remove GWT-RPC support for Guava types. You are seeing this warning because you are sending a Guava type over GWT-RPC, which will break. You can identify which type by looking at the class name in the attached stack trace.", new Throwable());
    }
    
    static {
        logger = Logger.getLogger(Platform.class.getName());
        patternCompiler = loadPatternCompiler();
    }
    
    private static final class JdkPatternCompiler implements PatternCompiler
    {
        @Override
        public CommonPattern compile(final String pattern) {
            return new JdkPattern(Pattern.compile(pattern));
        }
        
        @Override
        public boolean isPcreLike() {
            return true;
        }
    }
}
