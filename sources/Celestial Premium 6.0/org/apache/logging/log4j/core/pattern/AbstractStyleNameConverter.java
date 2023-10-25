/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.AnsiEscape;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;

public abstract class AbstractStyleNameConverter
extends LogEventPatternConverter {
    private final List<PatternFormatter> formatters;
    private final String style;

    protected AbstractStyleNameConverter(String name, List<PatternFormatter> formatters, String styling) {
        super(name, "style");
        this.formatters = formatters;
        this.style = styling;
    }

    protected static <T extends AbstractStyleNameConverter> T newInstance(Class<T> asnConverterClass, String name, Configuration config, String[] options) {
        List<PatternFormatter> formatters = AbstractStyleNameConverter.toPatternFormatterList(config, options);
        if (formatters == null) {
            return null;
        }
        try {
            Constructor<T> constructor = asnConverterClass.getConstructor(List.class, String.class);
            return (T)((AbstractStyleNameConverter)constructor.newInstance(formatters, AnsiEscape.createSequence(name)));
        }
        catch (SecurityException e) {
            LOGGER.error(e.toString(), (Throwable)e);
        }
        catch (NoSuchMethodException e) {
            LOGGER.error(e.toString(), (Throwable)e);
        }
        catch (IllegalArgumentException e) {
            LOGGER.error(e.toString(), (Throwable)e);
        }
        catch (InstantiationException e) {
            LOGGER.error(e.toString(), (Throwable)e);
        }
        catch (IllegalAccessException e) {
            LOGGER.error(e.toString(), (Throwable)e);
        }
        catch (InvocationTargetException e) {
            LOGGER.error(e.toString(), (Throwable)e);
        }
        return null;
    }

    private static List<PatternFormatter> toPatternFormatterList(Configuration config, String[] options) {
        if (options.length == 0 || options[0] == null) {
            LOGGER.error("No pattern supplied on style for config=" + config);
            return null;
        }
        PatternParser parser = PatternLayout.createPatternParser(config);
        if (parser == null) {
            LOGGER.error("No PatternParser created for config=" + config + ", options=" + Arrays.toString(options));
            return null;
        }
        return parser.parse(options[0]);
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        StringBuilder buf = new StringBuilder();
        for (PatternFormatter formatter : this.formatters) {
            formatter.format(event, buf);
        }
        if (buf.length() > 0) {
            toAppendTo.append(this.style).append(buf.toString()).append(AnsiEscape.getDefaultStyle());
        }
    }

    @Plugin(name="yellow", category="Converter")
    @ConverterKeys(value={"yellow"})
    public static final class Yellow
    extends AbstractStyleNameConverter {
        protected static final String NAME = "yellow";

        public Yellow(List<PatternFormatter> formatters, String styling) {
            super(NAME, formatters, styling);
        }

        public static Yellow newInstance(Configuration config, String[] options) {
            return Yellow.newInstance(Yellow.class, NAME, config, options);
        }
    }

    @Plugin(name="white", category="Converter")
    @ConverterKeys(value={"white"})
    public static final class White
    extends AbstractStyleNameConverter {
        protected static final String NAME = "white";

        public White(List<PatternFormatter> formatters, String styling) {
            super(NAME, formatters, styling);
        }

        public static White newInstance(Configuration config, String[] options) {
            return White.newInstance(White.class, NAME, config, options);
        }
    }

    @Plugin(name="red", category="Converter")
    @ConverterKeys(value={"red"})
    public static final class Red
    extends AbstractStyleNameConverter {
        protected static final String NAME = "red";

        public Red(List<PatternFormatter> formatters, String styling) {
            super(NAME, formatters, styling);
        }

        public static Red newInstance(Configuration config, String[] options) {
            return Red.newInstance(Red.class, NAME, config, options);
        }
    }

    @Plugin(name="magenta", category="Converter")
    @ConverterKeys(value={"magenta"})
    public static final class Magenta
    extends AbstractStyleNameConverter {
        protected static final String NAME = "magenta";

        public Magenta(List<PatternFormatter> formatters, String styling) {
            super(NAME, formatters, styling);
        }

        public static Magenta newInstance(Configuration config, String[] options) {
            return Magenta.newInstance(Magenta.class, NAME, config, options);
        }
    }

    @Plugin(name="green", category="Converter")
    @ConverterKeys(value={"green"})
    public static final class Green
    extends AbstractStyleNameConverter {
        protected static final String NAME = "green";

        public Green(List<PatternFormatter> formatters, String styling) {
            super(NAME, formatters, styling);
        }

        public static Green newInstance(Configuration config, String[] options) {
            return Green.newInstance(Green.class, NAME, config, options);
        }
    }

    @Plugin(name="cyan", category="Converter")
    @ConverterKeys(value={"cyan"})
    public static final class Cyan
    extends AbstractStyleNameConverter {
        protected static final String NAME = "cyan";

        public Cyan(List<PatternFormatter> formatters, String styling) {
            super(NAME, formatters, styling);
        }

        public static Cyan newInstance(Configuration config, String[] options) {
            return Cyan.newInstance(Cyan.class, NAME, config, options);
        }
    }

    @Plugin(name="blue", category="Converter")
    @ConverterKeys(value={"blue"})
    public static final class Blue
    extends AbstractStyleNameConverter {
        protected static final String NAME = "blue";

        public Blue(List<PatternFormatter> formatters, String styling) {
            super(NAME, formatters, styling);
        }

        public static Blue newInstance(Configuration config, String[] options) {
            return Blue.newInstance(Blue.class, NAME, config, options);
        }
    }

    @Plugin(name="black", category="Converter")
    @ConverterKeys(value={"black"})
    public static final class Black
    extends AbstractStyleNameConverter {
        protected static final String NAME = "black";

        public Black(List<PatternFormatter> formatters, String styling) {
            super(NAME, formatters, styling);
        }

        public static Black newInstance(Configuration config, String[] options) {
            return Black.newInstance(Black.class, NAME, config, options);
        }
    }
}

