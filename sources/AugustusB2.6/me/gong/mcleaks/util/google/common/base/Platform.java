// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.base;

import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import javax.annotation.Nullable;
import java.util.Locale;
import java.lang.ref.WeakReference;
import java.util.logging.Logger;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

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
    
    static boolean stringIsNullOrEmpty(@Nullable final String string) {
        return string == null || string.isEmpty();
    }
    
    static CommonPattern compilePattern(final String pattern) {
        Preconditions.checkNotNull(pattern);
        return Platform.patternCompiler.compile(pattern);
    }
    
    static boolean usingJdkPatternCompiler() {
        return Platform.patternCompiler instanceof JdkPatternCompiler;
    }
    
    private static PatternCompiler loadPatternCompiler() {
        final ServiceLoader<PatternCompiler> loader = ServiceLoader.load(PatternCompiler.class);
        try {
            final Iterator<PatternCompiler> it = loader.iterator();
            while (it.hasNext()) {
                try {
                    return it.next();
                }
                catch (ServiceConfigurationError e) {
                    logPatternCompilerError(e);
                    continue;
                }
                break;
            }
        }
        catch (ServiceConfigurationError e2) {
            logPatternCompilerError(e2);
        }
        return new JdkPatternCompiler();
    }
    
    private static void logPatternCompilerError(final ServiceConfigurationError e) {
        Platform.logger.log(Level.WARNING, "Error loading regex compiler, falling back to next option", e);
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
    }
}
