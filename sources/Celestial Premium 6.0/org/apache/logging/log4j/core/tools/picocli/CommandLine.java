/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.tools.picocli;

import java.io.File;
import java.io.PrintStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class CommandLine {
    public static final String VERSION = "2.0.3";
    private final Tracer tracer = new Tracer();
    private final Interpreter interpreter;
    private String commandName = "<main class>";
    private boolean overwrittenOptionsAllowed = false;
    private boolean unmatchedArgumentsAllowed = false;
    private final List<String> unmatchedArguments = new ArrayList<String>();
    private CommandLine parent;
    private boolean usageHelpRequested;
    private boolean versionHelpRequested;
    private final List<String> versionLines = new ArrayList<String>();

    public CommandLine(Object command) {
        this.interpreter = new Interpreter(command);
    }

    public CommandLine addSubcommand(String name, Object command) {
        CommandLine commandLine = CommandLine.toCommandLine(command);
        commandLine.parent = this;
        this.interpreter.commands.put(name, commandLine);
        return this;
    }

    public Map<String, CommandLine> getSubcommands() {
        return new LinkedHashMap<String, CommandLine>(this.interpreter.commands);
    }

    public CommandLine getParent() {
        return this.parent;
    }

    public <T> T getCommand() {
        return (T)this.interpreter.command;
    }

    public boolean isUsageHelpRequested() {
        return this.usageHelpRequested;
    }

    public boolean isVersionHelpRequested() {
        return this.versionHelpRequested;
    }

    public boolean isOverwrittenOptionsAllowed() {
        return this.overwrittenOptionsAllowed;
    }

    public CommandLine setOverwrittenOptionsAllowed(boolean newValue) {
        this.overwrittenOptionsAllowed = newValue;
        for (CommandLine command : this.interpreter.commands.values()) {
            command.setOverwrittenOptionsAllowed(newValue);
        }
        return this;
    }

    public boolean isUnmatchedArgumentsAllowed() {
        return this.unmatchedArgumentsAllowed;
    }

    public CommandLine setUnmatchedArgumentsAllowed(boolean newValue) {
        this.unmatchedArgumentsAllowed = newValue;
        for (CommandLine command : this.interpreter.commands.values()) {
            command.setUnmatchedArgumentsAllowed(newValue);
        }
        return this;
    }

    public List<String> getUnmatchedArguments() {
        return this.unmatchedArguments;
    }

    public static <T> T populateCommand(T command, String ... args) {
        CommandLine cli = CommandLine.toCommandLine(command);
        cli.parse(args);
        return command;
    }

    public List<CommandLine> parse(String ... args) {
        return this.interpreter.parse(args);
    }

    public static boolean printHelpIfRequested(List<CommandLine> parsedCommands, PrintStream out, Help.Ansi ansi) {
        for (CommandLine parsed : parsedCommands) {
            if (parsed.isUsageHelpRequested()) {
                parsed.usage(out, ansi);
                return true;
            }
            if (!parsed.isVersionHelpRequested()) continue;
            parsed.printVersionHelp(out, ansi);
            return true;
        }
        return false;
    }

    private static Object execute(CommandLine parsed) {
        Object command = parsed.getCommand();
        if (command instanceof Runnable) {
            try {
                ((Runnable)command).run();
                return null;
            }
            catch (Exception ex) {
                throw new ExecutionException(parsed, "Error while running command (" + command + ")", ex);
            }
        }
        if (command instanceof Callable) {
            try {
                return ((Callable)command).call();
            }
            catch (Exception ex) {
                throw new ExecutionException(parsed, "Error while calling command (" + command + ")", ex);
            }
        }
        throw new ExecutionException(parsed, "Parsed command (" + command + ") is not Runnable or Callable");
    }

    public List<Object> parseWithHandler(IParseResultHandler handler, PrintStream out, String ... args) {
        return this.parseWithHandlers(handler, out, Help.Ansi.AUTO, new DefaultExceptionHandler(), args);
    }

    public List<Object> parseWithHandlers(IParseResultHandler handler, PrintStream out, Help.Ansi ansi, IExceptionHandler exceptionHandler, String ... args) {
        try {
            List<CommandLine> result = this.parse(args);
            return handler.handleParseResult(result, out, ansi);
        }
        catch (ParameterException ex) {
            return exceptionHandler.handleException(ex, out, ansi, args);
        }
    }

    public static void usage(Object command, PrintStream out) {
        CommandLine.toCommandLine(command).usage(out);
    }

    public static void usage(Object command, PrintStream out, Help.Ansi ansi) {
        CommandLine.toCommandLine(command).usage(out, ansi);
    }

    public static void usage(Object command, PrintStream out, Help.ColorScheme colorScheme) {
        CommandLine.toCommandLine(command).usage(out, colorScheme);
    }

    public void usage(PrintStream out) {
        this.usage(out, Help.Ansi.AUTO);
    }

    public void usage(PrintStream out, Help.Ansi ansi) {
        this.usage(out, Help.defaultColorScheme(ansi));
    }

    public void usage(PrintStream out, Help.ColorScheme colorScheme) {
        Help help = new Help(this.interpreter.command, colorScheme).addAllSubcommands(this.getSubcommands());
        if (!"=".equals(this.getSeparator())) {
            help.separator = this.getSeparator();
            help.parameterLabelRenderer = help.createDefaultParamLabelRenderer();
        }
        if (!"<main class>".equals(this.getCommandName())) {
            help.commandName = this.getCommandName();
        }
        StringBuilder sb = new StringBuilder().append(help.headerHeading(new Object[0])).append(help.header(new Object[0])).append(help.synopsisHeading(new Object[0])).append(help.synopsis(help.synopsisHeadingLength())).append(help.descriptionHeading(new Object[0])).append(help.description(new Object[0])).append(help.parameterListHeading(new Object[0])).append(help.parameterList()).append(help.optionListHeading(new Object[0])).append(help.optionList()).append(help.commandListHeading(new Object[0])).append(help.commandList()).append(help.footerHeading(new Object[0])).append(help.footer(new Object[0]));
        out.print(sb);
    }

    public void printVersionHelp(PrintStream out) {
        this.printVersionHelp(out, Help.Ansi.AUTO);
    }

    public void printVersionHelp(PrintStream out, Help.Ansi ansi) {
        for (String versionInfo : this.versionLines) {
            Help.Ansi ansi2 = ansi;
            ((Object)((Object)ansi2)).getClass();
            out.println(ansi2.new Help.Ansi.Text(versionInfo));
        }
    }

    public void printVersionHelp(PrintStream out, Help.Ansi ansi, Object ... params) {
        for (String versionInfo : this.versionLines) {
            Help.Ansi ansi2 = ansi;
            ((Object)((Object)ansi2)).getClass();
            out.println(ansi2.new Help.Ansi.Text(String.format(versionInfo, params)));
        }
    }

    public static <C extends Callable<T>, T> T call(C callable, PrintStream out, String ... args) {
        return CommandLine.call(callable, out, Help.Ansi.AUTO, args);
    }

    public static <C extends Callable<T>, T> T call(C callable, PrintStream out, Help.Ansi ansi, String ... args) {
        CommandLine cmd = new CommandLine(callable);
        List<Object> results = cmd.parseWithHandlers(new RunLast(), out, ansi, new DefaultExceptionHandler(), args);
        return (T)(results == null || results.isEmpty() ? null : results.get(0));
    }

    public static <R extends Runnable> void run(R runnable, PrintStream out, String ... args) {
        CommandLine.run(runnable, out, Help.Ansi.AUTO, args);
    }

    public static <R extends Runnable> void run(R runnable, PrintStream out, Help.Ansi ansi, String ... args) {
        CommandLine cmd = new CommandLine(runnable);
        cmd.parseWithHandlers(new RunLast(), out, ansi, new DefaultExceptionHandler(), args);
    }

    public <K> CommandLine registerConverter(Class<K> cls, ITypeConverter<K> converter) {
        this.interpreter.converterRegistry.put(Assert.notNull(cls, "class"), Assert.notNull(converter, "converter"));
        for (CommandLine command : this.interpreter.commands.values()) {
            command.registerConverter(cls, converter);
        }
        return this;
    }

    public String getSeparator() {
        return this.interpreter.separator;
    }

    public CommandLine setSeparator(String separator) {
        this.interpreter.separator = Assert.notNull(separator, "separator");
        return this;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public CommandLine setCommandName(String commandName) {
        this.commandName = Assert.notNull(commandName, "commandName");
        return this;
    }

    private static boolean empty(String str) {
        return str == null || str.trim().length() == 0;
    }

    private static boolean empty(Object[] array) {
        return array == null || array.length == 0;
    }

    private static boolean empty(Help.Ansi.Text txt) {
        return txt == null || txt.plain.toString().trim().length() == 0;
    }

    private static String str(String[] arr, int i) {
        return arr == null || arr.length == 0 ? "" : arr[i];
    }

    private static boolean isBoolean(Class<?> type) {
        return type == Boolean.class || type == Boolean.TYPE;
    }

    private static CommandLine toCommandLine(Object obj) {
        return obj instanceof CommandLine ? (CommandLine)obj : new CommandLine(obj);
    }

    private static boolean isMultiValue(Field field) {
        return CommandLine.isMultiValue(field.getType());
    }

    private static boolean isMultiValue(Class<?> cls) {
        return cls.isArray() || Collection.class.isAssignableFrom(cls) || Map.class.isAssignableFrom(cls);
    }

    private static Class<?>[] getTypeAttribute(Field field) {
        Class<?>[] explicit;
        Class<?>[] arrclass = explicit = field.isAnnotationPresent(Parameters.class) ? field.getAnnotation(Parameters.class).type() : field.getAnnotation(Option.class).type();
        if (explicit.length > 0) {
            return explicit;
        }
        if (field.getType().isArray()) {
            return new Class[]{field.getType().getComponentType()};
        }
        if (CommandLine.isMultiValue(field)) {
            Type type = field.getGenericType();
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType)type;
                Type[] paramTypes = parameterizedType.getActualTypeArguments();
                Object[] result = new Class[paramTypes.length];
                for (int i = 0; i < paramTypes.length; ++i) {
                    if (paramTypes[i] instanceof Class) {
                        result[i] = (Class)paramTypes[i];
                        continue;
                    }
                    if (paramTypes[i] instanceof WildcardType) {
                        WildcardType wildcardType = (WildcardType)paramTypes[i];
                        Type[] lower = wildcardType.getLowerBounds();
                        if (lower.length > 0 && lower[0] instanceof Class) {
                            result[i] = (Class)lower[0];
                            continue;
                        }
                        Type[] upper = wildcardType.getUpperBounds();
                        if (upper.length > 0 && upper[0] instanceof Class) {
                            result[i] = (Class)upper[0];
                            continue;
                        }
                    }
                    Arrays.fill(result, String.class);
                    return result;
                }
                return result;
            }
            return new Class[]{String.class, String.class};
        }
        return new Class[]{field.getType()};
    }

    static void init(Class<?> cls, List<Field> requiredFields, Map<String, Field> optionName2Field, Map<Character, Field> singleCharOption2Field, List<Field> positionalParametersFields) {
        Field[] declaredFields;
        for (Field field : declaredFields = cls.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Option.class)) {
                Option option = field.getAnnotation(Option.class);
                if (option.required()) {
                    requiredFields.add(field);
                }
                for (String name : option.names()) {
                    char flag;
                    Field existing2;
                    Field existing = optionName2Field.put(name, field);
                    if (existing != null && existing != field) {
                        throw DuplicateOptionAnnotationsException.create(name, field, existing);
                    }
                    if (name.length() != 2 || !name.startsWith("-") || (existing2 = singleCharOption2Field.put(Character.valueOf(flag = name.charAt(1)), field)) == null || existing2 == field) continue;
                    throw DuplicateOptionAnnotationsException.create(name, field, existing2);
                }
            }
            if (!field.isAnnotationPresent(Parameters.class)) continue;
            if (field.isAnnotationPresent(Option.class)) {
                throw new DuplicateOptionAnnotationsException("A field can be either @Option or @Parameters, but '" + field.getName() + "' is both.");
            }
            positionalParametersFields.add(field);
            Range arity = Range.parameterArity(field);
            if (arity.min <= 0) continue;
            requiredFields.add(field);
        }
    }

    static void validatePositionalParameters(List<Field> positionalParametersFields) {
        int min = 0;
        for (Field field : positionalParametersFields) {
            Range index = Range.parameterIndex(field);
            if (index.min > min) {
                throw new ParameterIndexGapException("Missing field annotated with @Parameter(index=" + min + "). Nearest field '" + field.getName() + "' has index=" + index.min);
            }
            min = (min = Math.max(min, index.max)) == Integer.MAX_VALUE ? min : min + 1;
        }
    }

    private static <T> Stack<T> reverse(Stack<T> stack) {
        Collections.reverse(stack);
        return stack;
    }

    public static class MissingTypeConverterException
    extends ParameterException {
        private static final long serialVersionUID = -6050931703233083760L;

        public MissingTypeConverterException(CommandLine commandLine, String msg) {
            super(commandLine, msg);
        }
    }

    public static class OverwrittenOptionException
    extends ParameterException {
        private static final long serialVersionUID = 1338029208271055776L;

        public OverwrittenOptionException(CommandLine commandLine, String msg) {
            super(commandLine, msg);
        }
    }

    public static class MaxValuesforFieldExceededException
    extends ParameterException {
        private static final long serialVersionUID = 6536145439570100641L;

        public MaxValuesforFieldExceededException(CommandLine commandLine, String msg) {
            super(commandLine, msg);
        }
    }

    public static class UnmatchedArgumentException
    extends ParameterException {
        private static final long serialVersionUID = -8700426380701452440L;

        public UnmatchedArgumentException(CommandLine commandLine, String msg) {
            super(commandLine, msg);
        }

        public UnmatchedArgumentException(CommandLine commandLine, Stack<String> args) {
            this(commandLine, new ArrayList<String>(CommandLine.reverse(args)));
        }

        public UnmatchedArgumentException(CommandLine commandLine, List<String> args) {
            this(commandLine, "Unmatched argument" + (args.size() == 1 ? " " : "s ") + args);
        }
    }

    public static class ParameterIndexGapException
    extends InitializationException {
        private static final long serialVersionUID = -1520981133257618319L;

        public ParameterIndexGapException(String msg) {
            super(msg);
        }
    }

    public static class DuplicateOptionAnnotationsException
    extends InitializationException {
        private static final long serialVersionUID = -3355128012575075641L;

        public DuplicateOptionAnnotationsException(String msg) {
            super(msg);
        }

        private static DuplicateOptionAnnotationsException create(String name, Field field1, Field field2) {
            return new DuplicateOptionAnnotationsException("Option name '" + name + "' is used by both " + field1.getDeclaringClass().getName() + "." + field1.getName() + " and " + field2.getDeclaringClass().getName() + "." + field2.getName());
        }
    }

    public static class MissingParameterException
    extends ParameterException {
        private static final long serialVersionUID = 5075678535706338753L;

        public MissingParameterException(CommandLine commandLine, String msg) {
            super(commandLine, msg);
        }

        private static MissingParameterException create(CommandLine cmd, Collection<Field> missing, String separator) {
            if (missing.size() == 1) {
                return new MissingParameterException(cmd, "Missing required option '" + MissingParameterException.describe(missing.iterator().next(), separator) + "'");
            }
            ArrayList<String> names = new ArrayList<String>(missing.size());
            for (Field field : missing) {
                names.add(MissingParameterException.describe(field, separator));
            }
            return new MissingParameterException(cmd, "Missing required options " + ((Object)names).toString());
        }

        private static String describe(Field field, String separator) {
            String prefix = field.isAnnotationPresent(Option.class) ? field.getAnnotation(Option.class).names()[0] + separator : "params[" + field.getAnnotation(Parameters.class).index() + "]" + separator;
            return prefix + Help.DefaultParamLabelRenderer.renderParameterName(field);
        }
    }

    public static class ParameterException
    extends PicocliException {
        private static final long serialVersionUID = 1477112829129763139L;
        private final CommandLine commandLine;

        public ParameterException(CommandLine commandLine, String msg) {
            super(msg);
            this.commandLine = Assert.notNull(commandLine, "commandLine");
        }

        public ParameterException(CommandLine commandLine, String msg, Exception ex) {
            super(msg, ex);
            this.commandLine = Assert.notNull(commandLine, "commandLine");
        }

        public CommandLine getCommandLine() {
            return this.commandLine;
        }

        private static ParameterException create(CommandLine cmd, Exception ex, String arg, int i, String[] args) {
            String msg = ex.getClass().getSimpleName() + ": " + ex.getLocalizedMessage() + " while processing argument at or before arg[" + i + "] '" + arg + "' in " + Arrays.toString(args) + ": " + ex.toString();
            return new ParameterException(cmd, msg, ex);
        }
    }

    public static class TypeConversionException
    extends PicocliException {
        private static final long serialVersionUID = 4251973913816346114L;

        public TypeConversionException(String msg) {
            super(msg);
        }
    }

    public static class ExecutionException
    extends PicocliException {
        private static final long serialVersionUID = 7764539594267007998L;
        private final CommandLine commandLine;

        public ExecutionException(CommandLine commandLine, String msg) {
            super(msg);
            this.commandLine = Assert.notNull(commandLine, "commandLine");
        }

        public ExecutionException(CommandLine commandLine, String msg, Exception ex) {
            super(msg, ex);
            this.commandLine = Assert.notNull(commandLine, "commandLine");
        }

        public CommandLine getCommandLine() {
            return this.commandLine;
        }
    }

    public static class InitializationException
    extends PicocliException {
        private static final long serialVersionUID = 8423014001666638895L;

        public InitializationException(String msg) {
            super(msg);
        }

        public InitializationException(String msg, Exception ex) {
            super(msg, ex);
        }
    }

    public static class PicocliException
    extends RuntimeException {
        private static final long serialVersionUID = -2574128880125050818L;

        public PicocliException(String msg) {
            super(msg);
        }

        public PicocliException(String msg, Exception ex) {
            super(msg, ex);
        }
    }

    private static class Tracer {
        TraceLevel level = TraceLevel.lookup(System.getProperty("picocli.trace"));
        PrintStream stream = System.err;

        private Tracer() {
        }

        void warn(String msg, Object ... params) {
            TraceLevel.WARN.print(this, msg, params);
        }

        void info(String msg, Object ... params) {
            TraceLevel.INFO.print(this, msg, params);
        }

        void debug(String msg, Object ... params) {
            TraceLevel.DEBUG.print(this, msg, params);
        }

        boolean isWarn() {
            return this.level.isEnabled(TraceLevel.WARN);
        }

        boolean isInfo() {
            return this.level.isEnabled(TraceLevel.INFO);
        }

        boolean isDebug() {
            return this.level.isEnabled(TraceLevel.DEBUG);
        }
    }

    private static enum TraceLevel {
        OFF,
        WARN,
        INFO,
        DEBUG;


        public boolean isEnabled(TraceLevel other) {
            return this.ordinal() >= other.ordinal();
        }

        private void print(Tracer tracer, String msg, Object ... params) {
            if (tracer.level.isEnabled(this)) {
                tracer.stream.printf(this.prefix(msg), params);
            }
        }

        private String prefix(String msg) {
            return "[picocli " + (Object)((Object)this) + "] " + msg;
        }

        static TraceLevel lookup(String key) {
            return key == null ? WARN : (CommandLine.empty(key) || "true".equalsIgnoreCase(key) ? INFO : TraceLevel.valueOf(key));
        }
    }

    private static final class Assert {
        static <T> T notNull(T object, String description) {
            if (object == null) {
                throw new NullPointerException(description);
            }
            return object;
        }

        private Assert() {
        }
    }

    public static class Help {
        protected static final String DEFAULT_COMMAND_NAME = "<main class>";
        protected static final String DEFAULT_SEPARATOR = "=";
        private static final int usageHelpWidth = 80;
        private static final int optionsColumnWidth = 29;
        private final Object command;
        private final Map<String, Help> commands = new LinkedHashMap<String, Help>();
        final ColorScheme colorScheme;
        public final List<Field> optionFields;
        public final List<Field> positionalParametersFields;
        public String separator;
        public String commandName = "<main class>";
        public String[] description = new String[0];
        public String[] customSynopsis = new String[0];
        public String[] header = new String[0];
        public String[] footer = new String[0];
        public IParamLabelRenderer parameterLabelRenderer;
        public Boolean abbreviateSynopsis;
        public Boolean sortOptions;
        public Boolean showDefaultValues;
        public Character requiredOptionMarker;
        public String headerHeading;
        public String synopsisHeading;
        public String descriptionHeading;
        public String parameterListHeading;
        public String optionListHeading;
        public String commandListHeading;
        public String footerHeading;

        public Help(Object command) {
            this(command, Ansi.AUTO);
        }

        public Help(Object command, Ansi ansi) {
            this(command, Help.defaultColorScheme(ansi));
        }

        public Help(Object command, ColorScheme colorScheme) {
            this.command = Assert.notNull(command, "command");
            this.colorScheme = Assert.notNull(colorScheme, "colorScheme").applySystemProperties();
            ArrayList<Field> options = new ArrayList<Field>();
            ArrayList<Field> operands = new ArrayList<Field>();
            for (Class<?> cls = command.getClass(); cls != null; cls = cls.getSuperclass()) {
                for (Field field : cls.getDeclaredFields()) {
                    Option option;
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Option.class) && !(option = field.getAnnotation(Option.class)).hidden()) {
                        options.add(field);
                    }
                    if (!field.isAnnotationPresent(Parameters.class)) continue;
                    operands.add(field);
                }
                if (!cls.isAnnotationPresent(Command.class)) continue;
                Command cmd = cls.getAnnotation(Command.class);
                if (DEFAULT_COMMAND_NAME.equals(this.commandName)) {
                    this.commandName = cmd.name();
                }
                this.separator = this.separator == null ? cmd.separator() : this.separator;
                this.abbreviateSynopsis = this.abbreviateSynopsis == null ? cmd.abbreviateSynopsis() : this.abbreviateSynopsis.booleanValue();
                this.sortOptions = this.sortOptions == null ? cmd.sortOptions() : this.sortOptions.booleanValue();
                this.requiredOptionMarker = Character.valueOf(this.requiredOptionMarker == null ? cmd.requiredOptionMarker() : this.requiredOptionMarker.charValue());
                this.showDefaultValues = this.showDefaultValues == null ? cmd.showDefaultValues() : this.showDefaultValues.booleanValue();
                this.customSynopsis = CommandLine.empty(this.customSynopsis) ? cmd.customSynopsis() : this.customSynopsis;
                this.description = CommandLine.empty(this.description) ? cmd.description() : this.description;
                this.header = CommandLine.empty(this.header) ? cmd.header() : this.header;
                this.footer = CommandLine.empty(this.footer) ? cmd.footer() : this.footer;
                this.headerHeading = CommandLine.empty(this.headerHeading) ? cmd.headerHeading() : this.headerHeading;
                this.synopsisHeading = CommandLine.empty(this.synopsisHeading) || "Usage: ".equals(this.synopsisHeading) ? cmd.synopsisHeading() : this.synopsisHeading;
                this.descriptionHeading = CommandLine.empty(this.descriptionHeading) ? cmd.descriptionHeading() : this.descriptionHeading;
                this.parameterListHeading = CommandLine.empty(this.parameterListHeading) ? cmd.parameterListHeading() : this.parameterListHeading;
                this.optionListHeading = CommandLine.empty(this.optionListHeading) ? cmd.optionListHeading() : this.optionListHeading;
                this.commandListHeading = CommandLine.empty(this.commandListHeading) || "Commands:%n".equals(this.commandListHeading) ? cmd.commandListHeading() : this.commandListHeading;
                this.footerHeading = CommandLine.empty(this.footerHeading) ? cmd.footerHeading() : this.footerHeading;
            }
            this.sortOptions = this.sortOptions == null ? true : this.sortOptions;
            this.abbreviateSynopsis = this.abbreviateSynopsis == null ? false : this.abbreviateSynopsis;
            this.requiredOptionMarker = Character.valueOf(this.requiredOptionMarker == null ? (char)' ' : this.requiredOptionMarker.charValue());
            this.showDefaultValues = this.showDefaultValues == null ? false : this.showDefaultValues;
            this.synopsisHeading = this.synopsisHeading == null ? "Usage: " : this.synopsisHeading;
            this.commandListHeading = this.commandListHeading == null ? "Commands:%n" : this.commandListHeading;
            this.separator = this.separator == null ? DEFAULT_SEPARATOR : this.separator;
            this.parameterLabelRenderer = this.createDefaultParamLabelRenderer();
            Collections.sort(operands, new PositionalParametersSorter());
            this.positionalParametersFields = Collections.unmodifiableList(operands);
            this.optionFields = Collections.unmodifiableList(options);
        }

        public Help addAllSubcommands(Map<String, CommandLine> commands) {
            if (commands != null) {
                for (Map.Entry<String, CommandLine> entry : commands.entrySet()) {
                    this.addSubcommand(entry.getKey(), entry.getValue().getCommand());
                }
            }
            return this;
        }

        public Help addSubcommand(String commandName, Object command) {
            this.commands.put(commandName, new Help(command));
            return this;
        }

        @Deprecated
        public String synopsis() {
            return this.synopsis(0);
        }

        public String synopsis(int synopsisHeadingLength) {
            if (!CommandLine.empty(this.customSynopsis)) {
                return this.customSynopsis(new Object[0]);
            }
            return this.abbreviateSynopsis != false ? this.abbreviatedSynopsis() : this.detailedSynopsis(synopsisHeadingLength, Help.createShortOptionArityAndNameComparator(), true);
        }

        public String abbreviatedSynopsis() {
            StringBuilder sb = new StringBuilder();
            if (!this.optionFields.isEmpty()) {
                sb.append(" [OPTIONS]");
            }
            for (Field positionalParam : this.positionalParametersFields) {
                if (positionalParam.getAnnotation(Parameters.class).hidden()) continue;
                sb.append(' ').append(this.parameterLabelRenderer.renderParameterLabel(positionalParam, this.ansi(), this.colorScheme.parameterStyles));
            }
            return this.colorScheme.commandText(this.commandName).toString() + sb.toString() + System.getProperty("line.separator");
        }

        @Deprecated
        public String detailedSynopsis(Comparator<Field> optionSort, boolean clusterBooleanOptions) {
            return this.detailedSynopsis(0, optionSort, clusterBooleanOptions);
        }

        public String detailedSynopsis(int synopsisHeadingLength, Comparator<Field> optionSort, boolean clusterBooleanOptions) {
            Ansi ansi = this.ansi();
            ((Object)((Object)ansi)).getClass();
            Ansi.Text optionText = ansi.new Ansi.Text(0);
            ArrayList<Field> fields = new ArrayList<Field>(this.optionFields);
            if (optionSort != null) {
                Collections.sort(fields, optionSort);
            }
            if (clusterBooleanOptions) {
                Iterator<Field> booleanOptions = new ArrayList();
                StringBuilder clusteredRequired = new StringBuilder("-");
                StringBuilder clusteredOptional = new StringBuilder("-");
                for (Field field : fields) {
                    Option option;
                    String shortestName;
                    if (field.getType() != Boolean.TYPE && field.getType() != Boolean.class || (shortestName = ShortestFirst.sort((option = field.getAnnotation(Option.class)).names())[0]).length() != 2 || !shortestName.startsWith("-")) continue;
                    booleanOptions.add(field);
                    if (option.required()) {
                        clusteredRequired.append(shortestName.substring(1));
                        continue;
                    }
                    clusteredOptional.append(shortestName.substring(1));
                }
                fields.removeAll((Collection<?>)((Object)booleanOptions));
                if (clusteredRequired.length() > 1) {
                    optionText = optionText.append(" ").append(this.colorScheme.optionText(clusteredRequired.toString()));
                }
                if (clusteredOptional.length() > 1) {
                    optionText = optionText.append(" [").append(this.colorScheme.optionText(clusteredOptional.toString())).append("]");
                }
            }
            for (Field field : fields) {
                Option option = field.getAnnotation(Option.class);
                if (option.hidden()) continue;
                if (option.required()) {
                    optionText = this.appendOptionSynopsis(optionText, field, ShortestFirst.sort(option.names())[0], " ", "");
                    if (!CommandLine.isMultiValue(field)) continue;
                    optionText = this.appendOptionSynopsis(optionText, field, ShortestFirst.sort(option.names())[0], " [", "]...");
                    continue;
                }
                optionText = this.appendOptionSynopsis(optionText, field, ShortestFirst.sort(option.names())[0], " [", "]");
                if (!CommandLine.isMultiValue(field)) continue;
                optionText = optionText.append("...");
            }
            for (Field positionalParam : this.positionalParametersFields) {
                if (positionalParam.getAnnotation(Parameters.class).hidden()) continue;
                optionText = optionText.append(" ");
                Ansi.Text label = this.parameterLabelRenderer.renderParameterLabel(positionalParam, this.colorScheme.ansi(), this.colorScheme.parameterStyles);
                optionText = optionText.append(label);
            }
            int firstColumnLength = this.commandName.length() + synopsisHeadingLength;
            TextTable textTable = new TextTable(this.ansi(), firstColumnLength, 80 - firstColumnLength);
            textTable.indentWrappedLines = 1;
            Ansi ansi2 = Ansi.OFF;
            ((Object)((Object)ansi2)).getClass();
            Ansi.Text PADDING = ansi2.new Ansi.Text(Help.stringOf('X', synopsisHeadingLength));
            textTable.addRowValues(PADDING.append(this.colorScheme.commandText(this.commandName)), optionText);
            return textTable.toString().substring(synopsisHeadingLength);
        }

        private Ansi.Text appendOptionSynopsis(Ansi.Text optionText, Field field, String optionName, String prefix, String suffix) {
            Ansi.Text optionParamText = this.parameterLabelRenderer.renderParameterLabel(field, this.colorScheme.ansi(), this.colorScheme.optionParamStyles);
            return optionText.append(prefix).append(this.colorScheme.optionText(optionName)).append(optionParamText).append(suffix);
        }

        public int synopsisHeadingLength() {
            Ansi ansi = Ansi.OFF;
            ((Object)((Object)ansi)).getClass();
            String[] lines = ansi.new Ansi.Text(this.synopsisHeading).toString().split("\\r?\\n|\\r|%n", -1);
            return lines[lines.length - 1].length();
        }

        public String optionList() {
            Comparator<Field> sortOrder = this.sortOptions == null || this.sortOptions != false ? Help.createShortOptionNameComparator() : null;
            return this.optionList(this.createDefaultLayout(), sortOrder, this.parameterLabelRenderer);
        }

        public String optionList(Layout layout, Comparator<Field> optionSort, IParamLabelRenderer valueLabelRenderer) {
            ArrayList<Field> fields = new ArrayList<Field>(this.optionFields);
            if (optionSort != null) {
                Collections.sort(fields, optionSort);
            }
            layout.addOptions(fields, valueLabelRenderer);
            return layout.toString();
        }

        public String parameterList() {
            return this.parameterList(this.createDefaultLayout(), this.parameterLabelRenderer);
        }

        public String parameterList(Layout layout, IParamLabelRenderer paramLabelRenderer) {
            layout.addPositionalParameters(this.positionalParametersFields, paramLabelRenderer);
            return layout.toString();
        }

        private static String heading(Ansi ansi, String values, Object ... params) {
            StringBuilder sb = Help.join(ansi, new String[]{values}, new StringBuilder(), params);
            String result = sb.toString();
            result = result.endsWith(System.getProperty("line.separator")) ? result.substring(0, result.length() - System.getProperty("line.separator").length()) : result;
            return result + new String(Help.spaces(Help.countTrailingSpaces(values)));
        }

        private static char[] spaces(int length) {
            char[] result = new char[length];
            Arrays.fill(result, ' ');
            return result;
        }

        private static int countTrailingSpaces(String str) {
            if (str == null) {
                return 0;
            }
            int trailingSpaces = 0;
            for (int i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i) {
                ++trailingSpaces;
            }
            return trailingSpaces;
        }

        public static StringBuilder join(Ansi ansi, String[] values, StringBuilder sb, Object ... params) {
            if (values != null) {
                TextTable table = new TextTable(ansi, 80);
                table.indentWrappedLines = 0;
                for (String summaryLine : values) {
                    Ansi.Text[] lines;
                    Ansi ansi2 = ansi;
                    ((Object)((Object)ansi2)).getClass();
                    for (Ansi.Text line : lines = ansi2.new Ansi.Text(Help.format(summaryLine, params)).splitLines()) {
                        table.addRowValues(line);
                    }
                }
                table.toString(sb);
            }
            return sb;
        }

        private static String format(String formatString, Object ... params) {
            return formatString == null ? "" : String.format(formatString, params);
        }

        public String customSynopsis(Object ... params) {
            return Help.join(this.ansi(), this.customSynopsis, new StringBuilder(), params).toString();
        }

        public String description(Object ... params) {
            return Help.join(this.ansi(), this.description, new StringBuilder(), params).toString();
        }

        public String header(Object ... params) {
            return Help.join(this.ansi(), this.header, new StringBuilder(), params).toString();
        }

        public String footer(Object ... params) {
            return Help.join(this.ansi(), this.footer, new StringBuilder(), params).toString();
        }

        public String headerHeading(Object ... params) {
            return Help.heading(this.ansi(), this.headerHeading, params);
        }

        public String synopsisHeading(Object ... params) {
            return Help.heading(this.ansi(), this.synopsisHeading, params);
        }

        public String descriptionHeading(Object ... params) {
            return CommandLine.empty(this.descriptionHeading) ? "" : Help.heading(this.ansi(), this.descriptionHeading, params);
        }

        public String parameterListHeading(Object ... params) {
            return this.positionalParametersFields.isEmpty() ? "" : Help.heading(this.ansi(), this.parameterListHeading, params);
        }

        public String optionListHeading(Object ... params) {
            return this.optionFields.isEmpty() ? "" : Help.heading(this.ansi(), this.optionListHeading, params);
        }

        public String commandListHeading(Object ... params) {
            return this.commands.isEmpty() ? "" : Help.heading(this.ansi(), this.commandListHeading, params);
        }

        public String footerHeading(Object ... params) {
            return Help.heading(this.ansi(), this.footerHeading, params);
        }

        public String commandList() {
            if (this.commands.isEmpty()) {
                return "";
            }
            int commandLength = Help.maxLength(this.commands.keySet());
            TextTable textTable = new TextTable(this.ansi(), new Column(commandLength + 2, 2, Column.Overflow.SPAN), new Column(80 - (commandLength + 2), 2, Column.Overflow.WRAP));
            for (Map.Entry<String, Help> entry : this.commands.entrySet()) {
                Help command = entry.getValue();
                String header = command.header != null && command.header.length > 0 ? command.header[0] : (command.description != null && command.description.length > 0 ? command.description[0] : "");
                Ansi.Text[] arrtext = new Ansi.Text[2];
                arrtext[0] = this.colorScheme.commandText(entry.getKey());
                Ansi ansi = this.ansi();
                ((Object)((Object)ansi)).getClass();
                arrtext[1] = ansi.new Ansi.Text(header);
                textTable.addRowValues(arrtext);
            }
            return textTable.toString();
        }

        private static int maxLength(Collection<String> any) {
            ArrayList<String> strings = new ArrayList<String>(any);
            Collections.sort(strings, Collections.reverseOrder(Help.shortestFirst()));
            return ((String)strings.get(0)).length();
        }

        private static String join(String[] names, int offset, int length, String separator) {
            if (names == null) {
                return "";
            }
            StringBuilder result = new StringBuilder();
            for (int i = offset; i < offset + length; ++i) {
                result.append(i > offset ? separator : "").append(names[i]);
            }
            return result.toString();
        }

        private static String stringOf(char chr, int length) {
            char[] buff = new char[length];
            Arrays.fill(buff, chr);
            return new String(buff);
        }

        public Layout createDefaultLayout() {
            return new Layout(this.colorScheme, new TextTable(this.colorScheme.ansi()), this.createDefaultOptionRenderer(), this.createDefaultParameterRenderer());
        }

        public IOptionRenderer createDefaultOptionRenderer() {
            DefaultOptionRenderer result = new DefaultOptionRenderer();
            result.requiredMarker = String.valueOf(this.requiredOptionMarker);
            if (this.showDefaultValues != null && this.showDefaultValues.booleanValue()) {
                result.command = this.command;
            }
            return result;
        }

        public static IOptionRenderer createMinimalOptionRenderer() {
            return new MinimalOptionRenderer();
        }

        public IParameterRenderer createDefaultParameterRenderer() {
            DefaultParameterRenderer result = new DefaultParameterRenderer();
            result.requiredMarker = String.valueOf(this.requiredOptionMarker);
            return result;
        }

        public static IParameterRenderer createMinimalParameterRenderer() {
            return new MinimalParameterRenderer();
        }

        public static IParamLabelRenderer createMinimalParamLabelRenderer() {
            return new IParamLabelRenderer(){

                @Override
                public Ansi.Text renderParameterLabel(Field field, Ansi ansi, List<Ansi.IStyle> styles) {
                    String text = DefaultParamLabelRenderer.renderParameterName(field);
                    return ansi.apply(text, styles);
                }

                @Override
                public String separator() {
                    return "";
                }
            };
        }

        public IParamLabelRenderer createDefaultParamLabelRenderer() {
            return new DefaultParamLabelRenderer(this.separator);
        }

        public static Comparator<Field> createShortOptionNameComparator() {
            return new SortByShortestOptionNameAlphabetically();
        }

        public static Comparator<Field> createShortOptionArityAndNameComparator() {
            return new SortByOptionArityAndNameAlphabetically();
        }

        public static Comparator<String> shortestFirst() {
            return new ShortestFirst();
        }

        public Ansi ansi() {
            return this.colorScheme.ansi;
        }

        public static ColorScheme defaultColorScheme(Ansi ansi) {
            return new ColorScheme(ansi).commands(Ansi.Style.bold).options(Ansi.Style.fg_yellow).parameters(Ansi.Style.fg_yellow).optionParams(Ansi.Style.italic);
        }

        public static enum Ansi {
            AUTO,
            ON,
            OFF;

            static Text EMPTY_TEXT;
            static final boolean isWindows;
            static final boolean isXterm;
            static final boolean ISATTY;

            static final boolean calcTTY() {
                if (isWindows && isXterm) {
                    return true;
                }
                try {
                    return System.class.getDeclaredMethod("console", new Class[0]).invoke(null, new Object[0]) != null;
                }
                catch (Throwable reflectionFailed) {
                    return true;
                }
            }

            private static boolean ansiPossible() {
                return ISATTY && (!isWindows || isXterm);
            }

            public boolean enabled() {
                if (this == ON) {
                    return true;
                }
                if (this == OFF) {
                    return false;
                }
                return System.getProperty("picocli.ansi") == null ? Ansi.ansiPossible() : Boolean.getBoolean("picocli.ansi");
            }

            public Text apply(String plainText, List<IStyle> styles) {
                if (plainText.length() == 0) {
                    return new Text(0);
                }
                Text result = new Text(plainText.length());
                IStyle[] all = styles.toArray(new IStyle[styles.size()]);
                result.sections.add(new StyledSection(0, plainText.length(), Style.on(all), Style.off(Ansi.reverse(all)) + Style.reset.off()));
                result.plain.append(plainText);
                result.length = result.plain.length();
                return result;
            }

            private static <T> T[] reverse(T[] all) {
                for (int i = 0; i < all.length / 2; ++i) {
                    T temp = all[i];
                    all[i] = all[all.length - i - 1];
                    all[all.length - i - 1] = temp;
                }
                return all;
            }

            static {
                Ansi ansi = OFF;
                ((Object)((Object)ansi)).getClass();
                EMPTY_TEXT = ansi.new Text(0);
                isWindows = System.getProperty("os.name").startsWith("Windows");
                isXterm = System.getenv("TERM") != null && System.getenv("TERM").startsWith("xterm");
                ISATTY = Ansi.calcTTY();
            }

            public class Text
            implements Cloneable {
                private final int maxLength;
                private int from;
                private int length;
                private StringBuilder plain = new StringBuilder();
                private List<StyledSection> sections = new ArrayList<StyledSection>();

                public Text(int maxLength) {
                    this.maxLength = maxLength;
                }

                public Text(String input) {
                    this.maxLength = -1;
                    this.plain.setLength(0);
                    int i = 0;
                    while (true) {
                        int j;
                        if ((j = input.indexOf("@|", i)) == -1) {
                            if (i == 0) {
                                this.plain.append(input);
                                this.length = this.plain.length();
                                return;
                            }
                            this.plain.append(input.substring(i, input.length()));
                            this.length = this.plain.length();
                            return;
                        }
                        this.plain.append(input.substring(i, j));
                        int k = input.indexOf("|@", j);
                        if (k == -1) {
                            this.plain.append(input);
                            this.length = this.plain.length();
                            return;
                        }
                        String spec = input.substring(j += 2, k);
                        String[] items = spec.split(" ", 2);
                        if (items.length == 1) {
                            this.plain.append(input);
                            this.length = this.plain.length();
                            return;
                        }
                        Object[] styles = Style.parse(items[0]);
                        this.addStyledSection(this.plain.length(), items[1].length(), Style.on((IStyle[])styles), Style.off((IStyle[])Ansi.reverse(styles)) + Style.reset.off());
                        this.plain.append(items[1]);
                        i = k + 2;
                    }
                }

                private void addStyledSection(int start, int length, String startStyle, String endStyle) {
                    this.sections.add(new StyledSection(start, length, startStyle, endStyle));
                }

                public Object clone() {
                    try {
                        return super.clone();
                    }
                    catch (CloneNotSupportedException e) {
                        throw new IllegalStateException(e);
                    }
                }

                public Text[] splitLines() {
                    ArrayList<Text> result = new ArrayList<Text>();
                    boolean trailingEmptyString = false;
                    int start = 0;
                    int end = 0;
                    int i = 0;
                    while (i < this.plain.length()) {
                        char c = this.plain.charAt(i);
                        boolean eol = c == '\n';
                        eol |= c == '\r' && i + 1 < this.plain.length() && this.plain.charAt(i + 1) == '\n' && ++i > 0;
                        if (eol |= c == '\r') {
                            result.add(this.substring(start, end));
                            trailingEmptyString = i == this.plain.length() - 1;
                            start = i + 1;
                        }
                        end = ++i;
                    }
                    if (start < this.plain.length() || trailingEmptyString) {
                        result.add(this.substring(start, this.plain.length()));
                    }
                    return result.toArray(new Text[result.size()]);
                }

                public Text substring(int start) {
                    return this.substring(start, this.length);
                }

                public Text substring(int start, int end) {
                    Text result = (Text)this.clone();
                    result.from = this.from + start;
                    result.length = end - start;
                    return result;
                }

                public Text append(String string) {
                    return this.append(new Text(string));
                }

                public Text append(Text other) {
                    Text result = (Text)this.clone();
                    result.plain = new StringBuilder(this.plain.toString().substring(this.from, this.from + this.length));
                    result.from = 0;
                    result.sections = new ArrayList<StyledSection>();
                    for (StyledSection section : this.sections) {
                        result.sections.add(section.withStartIndex(section.startIndex - this.from));
                    }
                    result.plain.append(other.plain.toString().substring(other.from, other.from + other.length));
                    for (StyledSection section : other.sections) {
                        int index = result.length + section.startIndex - other.from;
                        result.sections.add(section.withStartIndex(index));
                    }
                    result.length = result.plain.length();
                    return result;
                }

                public void getStyledChars(int from, int length, Text destination, int offset) {
                    if (destination.length < offset) {
                        for (int i = destination.length; i < offset; ++i) {
                            destination.plain.append(' ');
                        }
                        destination.length = offset;
                    }
                    for (StyledSection section : this.sections) {
                        destination.sections.add(section.withStartIndex(section.startIndex - from + destination.length));
                    }
                    destination.plain.append(this.plain.toString().substring(from, from + length));
                    destination.length = destination.plain.length();
                }

                public String plainString() {
                    return this.plain.toString().substring(this.from, this.from + this.length);
                }

                public boolean equals(Object obj) {
                    return this.toString().equals(String.valueOf(obj));
                }

                public int hashCode() {
                    return this.toString().hashCode();
                }

                public String toString() {
                    if (!Ansi.this.enabled()) {
                        return this.plain.toString().substring(this.from, this.from + this.length);
                    }
                    if (this.length == 0) {
                        return "";
                    }
                    StringBuilder sb = new StringBuilder(this.plain.length() + 20 * this.sections.size());
                    StyledSection current = null;
                    int end = Math.min(this.from + this.length, this.plain.length());
                    for (int i = this.from; i < end; ++i) {
                        StyledSection section = this.findSectionContaining(i);
                        if (section != current) {
                            if (current != null) {
                                sb.append(current.endStyles);
                            }
                            if (section != null) {
                                sb.append(section.startStyles);
                            }
                            current = section;
                        }
                        sb.append(this.plain.charAt(i));
                    }
                    if (current != null) {
                        sb.append(current.endStyles);
                    }
                    return sb.toString();
                }

                private StyledSection findSectionContaining(int index) {
                    for (StyledSection section : this.sections) {
                        if (index < section.startIndex || index >= section.startIndex + section.length) continue;
                        return section;
                    }
                    return null;
                }
            }

            private static class StyledSection {
                int startIndex;
                int length;
                String startStyles;
                String endStyles;

                StyledSection(int start, int len, String style1, String style2) {
                    this.startIndex = start;
                    this.length = len;
                    this.startStyles = style1;
                    this.endStyles = style2;
                }

                StyledSection withStartIndex(int newStart) {
                    return new StyledSection(newStart, this.length, this.startStyles, this.endStyles);
                }
            }

            static class Palette256Color
            implements IStyle {
                private final int fgbg;
                private final int color;

                Palette256Color(boolean foreground, String color) {
                    this.fgbg = foreground ? 38 : 48;
                    String[] rgb = color.split(";");
                    this.color = rgb.length == 3 ? 16 + 36 * Integer.decode(rgb[0]) + 6 * Integer.decode(rgb[1]) + Integer.decode(rgb[2]) : Integer.decode(color);
                }

                @Override
                public String on() {
                    return String.format("\u001b[%d;5;%dm", this.fgbg, this.color);
                }

                @Override
                public String off() {
                    return "\u001b[" + (this.fgbg + 1) + "m";
                }
            }

            public static enum Style implements IStyle
            {
                reset(0, 0),
                bold(1, 21),
                faint(2, 22),
                italic(3, 23),
                underline(4, 24),
                blink(5, 25),
                reverse(7, 27),
                fg_black(30, 39),
                fg_red(31, 39),
                fg_green(32, 39),
                fg_yellow(33, 39),
                fg_blue(34, 39),
                fg_magenta(35, 39),
                fg_cyan(36, 39),
                fg_white(37, 39),
                bg_black(40, 49),
                bg_red(41, 49),
                bg_green(42, 49),
                bg_yellow(43, 49),
                bg_blue(44, 49),
                bg_magenta(45, 49),
                bg_cyan(46, 49),
                bg_white(47, 49);

                private final int startCode;
                private final int endCode;

                private Style(int startCode, int endCode) {
                    this.startCode = startCode;
                    this.endCode = endCode;
                }

                @Override
                public String on() {
                    return "\u001b[" + this.startCode + "m";
                }

                @Override
                public String off() {
                    return "\u001b[" + this.endCode + "m";
                }

                public static String on(IStyle ... styles) {
                    StringBuilder result = new StringBuilder();
                    for (IStyle style : styles) {
                        result.append(style.on());
                    }
                    return result.toString();
                }

                public static String off(IStyle ... styles) {
                    StringBuilder result = new StringBuilder();
                    for (IStyle style : styles) {
                        result.append(style.off());
                    }
                    return result.toString();
                }

                public static IStyle fg(String str) {
                    try {
                        return Style.valueOf(str.toLowerCase(Locale.ENGLISH));
                    }
                    catch (Exception exception) {
                        try {
                            return Style.valueOf("fg_" + str.toLowerCase(Locale.ENGLISH));
                        }
                        catch (Exception exception2) {
                            return new Palette256Color(true, str);
                        }
                    }
                }

                public static IStyle bg(String str) {
                    try {
                        return Style.valueOf(str.toLowerCase(Locale.ENGLISH));
                    }
                    catch (Exception exception) {
                        try {
                            return Style.valueOf("bg_" + str.toLowerCase(Locale.ENGLISH));
                        }
                        catch (Exception exception2) {
                            return new Palette256Color(false, str);
                        }
                    }
                }

                public static IStyle[] parse(String commaSeparatedCodes) {
                    String[] codes = commaSeparatedCodes.split(",");
                    IStyle[] styles = new IStyle[codes.length];
                    for (int i = 0; i < codes.length; ++i) {
                        int end;
                        if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("fg(")) {
                            end = codes[i].indexOf(41);
                            styles[i] = Style.fg(codes[i].substring(3, end < 0 ? codes[i].length() : end));
                            continue;
                        }
                        if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("bg(")) {
                            end = codes[i].indexOf(41);
                            styles[i] = Style.bg(codes[i].substring(3, end < 0 ? codes[i].length() : end));
                            continue;
                        }
                        styles[i] = Style.fg(codes[i]);
                    }
                    return styles;
                }
            }

            public static interface IStyle {
                public static final String CSI = "\u001b[";

                public String on();

                public String off();
            }
        }

        public static class ColorScheme {
            public final List<Ansi.IStyle> commandStyles = new ArrayList<Ansi.IStyle>();
            public final List<Ansi.IStyle> optionStyles = new ArrayList<Ansi.IStyle>();
            public final List<Ansi.IStyle> parameterStyles = new ArrayList<Ansi.IStyle>();
            public final List<Ansi.IStyle> optionParamStyles = new ArrayList<Ansi.IStyle>();
            private final Ansi ansi;

            public ColorScheme() {
                this(Ansi.AUTO);
            }

            public ColorScheme(Ansi ansi) {
                this.ansi = Assert.notNull(ansi, "ansi");
            }

            public ColorScheme commands(Ansi.IStyle ... styles) {
                return this.addAll(this.commandStyles, styles);
            }

            public ColorScheme options(Ansi.IStyle ... styles) {
                return this.addAll(this.optionStyles, styles);
            }

            public ColorScheme parameters(Ansi.IStyle ... styles) {
                return this.addAll(this.parameterStyles, styles);
            }

            public ColorScheme optionParams(Ansi.IStyle ... styles) {
                return this.addAll(this.optionParamStyles, styles);
            }

            public Ansi.Text commandText(String command) {
                return this.ansi().apply(command, this.commandStyles);
            }

            public Ansi.Text optionText(String option) {
                return this.ansi().apply(option, this.optionStyles);
            }

            public Ansi.Text parameterText(String parameter) {
                return this.ansi().apply(parameter, this.parameterStyles);
            }

            public Ansi.Text optionParamText(String optionParam) {
                return this.ansi().apply(optionParam, this.optionParamStyles);
            }

            public ColorScheme applySystemProperties() {
                this.replace(this.commandStyles, System.getProperty("picocli.color.commands"));
                this.replace(this.optionStyles, System.getProperty("picocli.color.options"));
                this.replace(this.parameterStyles, System.getProperty("picocli.color.parameters"));
                this.replace(this.optionParamStyles, System.getProperty("picocli.color.optionParams"));
                return this;
            }

            private void replace(List<Ansi.IStyle> styles, String property) {
                if (property != null) {
                    styles.clear();
                    this.addAll(styles, Ansi.Style.parse(property));
                }
            }

            private ColorScheme addAll(List<Ansi.IStyle> styles, Ansi.IStyle ... add) {
                styles.addAll(Arrays.asList(add));
                return this;
            }

            public Ansi ansi() {
                return this.ansi;
            }
        }

        public static class Column {
            public final int width;
            public final int indent;
            public final Overflow overflow;

            public Column(int width, int indent, Overflow overflow) {
                this.width = width;
                this.indent = indent;
                this.overflow = Assert.notNull(overflow, "overflow");
            }

            public static enum Overflow {
                TRUNCATE,
                SPAN,
                WRAP;

            }
        }

        public static class TextTable {
            public final Column[] columns;
            protected final List<Ansi.Text> columnValues = new ArrayList<Ansi.Text>();
            public int indentWrappedLines = 2;
            private final Ansi ansi;

            public TextTable(Ansi ansi) {
                this(ansi, new Column(2, 0, Column.Overflow.TRUNCATE), new Column(2, 0, Column.Overflow.TRUNCATE), new Column(1, 0, Column.Overflow.TRUNCATE), new Column(24, 1, Column.Overflow.SPAN), new Column(51, 1, Column.Overflow.WRAP));
            }

            public TextTable(Ansi ansi, int ... columnWidths) {
                this.ansi = Assert.notNull(ansi, "ansi");
                this.columns = new Column[columnWidths.length];
                for (int i = 0; i < columnWidths.length; ++i) {
                    this.columns[i] = new Column(columnWidths[i], 0, i == columnWidths.length - 1 ? Column.Overflow.SPAN : Column.Overflow.WRAP);
                }
            }

            public TextTable(Ansi ansi, Column ... columns) {
                this.ansi = Assert.notNull(ansi, "ansi");
                this.columns = Assert.notNull(columns, "columns");
                if (columns.length == 0) {
                    throw new IllegalArgumentException("At least one column is required");
                }
            }

            public Ansi.Text textAt(int row, int col) {
                return this.columnValues.get(col + row * this.columns.length);
            }

            @Deprecated
            public Ansi.Text cellAt(int row, int col) {
                return this.textAt(row, col);
            }

            public int rowCount() {
                return this.columnValues.size() / this.columns.length;
            }

            public void addEmptyRow() {
                for (int i = 0; i < this.columns.length; ++i) {
                    Ansi ansi = this.ansi;
                    ((Object)((Object)ansi)).getClass();
                    this.columnValues.add(ansi.new Ansi.Text(this.columns[i].width));
                }
            }

            public void addRowValues(String ... values) {
                Ansi.Text[] array = new Ansi.Text[values.length];
                for (int i = 0; i < array.length; ++i) {
                    Ansi.Text text;
                    if (values[i] == null) {
                        text = Ansi.EMPTY_TEXT;
                    } else {
                        Ansi ansi = this.ansi;
                        ((Object)((Object)ansi)).getClass();
                        text = ansi.new Ansi.Text(values[i]);
                    }
                    array[i] = text;
                }
                this.addRowValues(array);
            }

            public void addRowValues(Ansi.Text ... values) {
                if (values.length > this.columns.length) {
                    throw new IllegalArgumentException(values.length + " values don't fit in " + this.columns.length + " columns");
                }
                this.addEmptyRow();
                for (int col = 0; col < values.length; ++col) {
                    int row = this.rowCount() - 1;
                    Cell cell = this.putValue(row, col, values[col]);
                    if (cell.row == row && cell.column == col || col == values.length - 1) continue;
                    this.addEmptyRow();
                }
            }

            public Cell putValue(int row, int col, Ansi.Text value) {
                if (row > this.rowCount() - 1) {
                    throw new IllegalArgumentException("Cannot write to row " + row + ": rowCount=" + this.rowCount());
                }
                if (value == null || value.plain.length() == 0) {
                    return new Cell(col, row);
                }
                Column column = this.columns[col];
                int indent = column.indent;
                switch (column.overflow) {
                    case TRUNCATE: {
                        TextTable.copy(value, this.textAt(row, col), indent);
                        return new Cell(col, row);
                    }
                    case SPAN: {
                        int startColumn = col;
                        do {
                            boolean lastColumn = col == this.columns.length - 1;
                            int charsWritten = lastColumn ? this.copy(BreakIterator.getLineInstance(), value, this.textAt(row, col), indent) : TextTable.copy(value, this.textAt(row, col), indent);
                            value = value.substring(charsWritten);
                            indent = 0;
                            if (value.length > 0) {
                                ++col;
                            }
                            if (value.length <= 0 || col < this.columns.length) continue;
                            this.addEmptyRow();
                            ++row;
                            col = startColumn;
                            indent = column.indent + this.indentWrappedLines;
                        } while (value.length > 0);
                        return new Cell(col, row);
                    }
                    case WRAP: {
                        BreakIterator lineBreakIterator = BreakIterator.getLineInstance();
                        do {
                            int charsWritten = this.copy(lineBreakIterator, value, this.textAt(row, col), indent);
                            value = value.substring(charsWritten);
                            indent = column.indent + this.indentWrappedLines;
                            if (value.length <= 0) continue;
                            ++row;
                            this.addEmptyRow();
                        } while (value.length > 0);
                        return new Cell(col, row);
                    }
                }
                throw new IllegalStateException(column.overflow.toString());
            }

            private static int length(Ansi.Text str) {
                return str.length;
            }

            private int copy(BreakIterator line, Ansi.Text text, Ansi.Text columnValue, int offset) {
                line.setText(text.plainString().replace("-", "\u00ff"));
                int done = 0;
                int start = line.first();
                int end = line.next();
                while (end != -1) {
                    Ansi.Text word = text.substring(start, end);
                    if (columnValue.maxLength < offset + done + TextTable.length(word)) break;
                    done += TextTable.copy(word, columnValue, offset + done);
                    start = end;
                    end = line.next();
                }
                if (done == 0 && TextTable.length(text) > columnValue.maxLength) {
                    done = TextTable.copy(text, columnValue, offset);
                }
                return done;
            }

            private static int copy(Ansi.Text value, Ansi.Text destination, int offset) {
                int length = Math.min(value.length, destination.maxLength - offset);
                value.getStyledChars(value.from, length, destination, offset);
                return length;
            }

            public StringBuilder toString(StringBuilder text) {
                int columnCount = this.columns.length;
                StringBuilder row = new StringBuilder(80);
                for (int i = 0; i < this.columnValues.size(); ++i) {
                    int lastChar;
                    Ansi.Text column = this.columnValues.get(i);
                    row.append(column.toString());
                    row.append(new String(Help.spaces(this.columns[i % columnCount].width - column.length)));
                    if (i % columnCount != columnCount - 1) continue;
                    for (lastChar = row.length() - 1; lastChar >= 0 && row.charAt(lastChar) == ' '; --lastChar) {
                    }
                    row.setLength(lastChar + 1);
                    text.append(row.toString()).append(System.getProperty("line.separator"));
                    row.setLength(0);
                }
                return text;
            }

            public String toString() {
                return this.toString(new StringBuilder()).toString();
            }

            public static class Cell {
                public final int column;
                public final int row;

                public Cell(int column, int row) {
                    this.column = column;
                    this.row = row;
                }
            }
        }

        static class SortByOptionArityAndNameAlphabetically
        extends SortByShortestOptionNameAlphabetically {
            SortByOptionArityAndNameAlphabetically() {
            }

            @Override
            public int compare(Field f1, Field f2) {
                Option o1 = f1.getAnnotation(Option.class);
                Option o2 = f2.getAnnotation(Option.class);
                Range arity1 = Range.optionArity(f1);
                Range arity2 = Range.optionArity(f2);
                int result = arity1.max - arity2.max;
                if (result == 0) {
                    result = arity1.min - arity2.min;
                }
                if (result == 0) {
                    if (CommandLine.isMultiValue(f1) && !CommandLine.isMultiValue(f2)) {
                        result = 1;
                    }
                    if (!CommandLine.isMultiValue(f1) && CommandLine.isMultiValue(f2)) {
                        result = -1;
                    }
                }
                return result == 0 ? super.compare(f1, f2) : result;
            }
        }

        static class SortByShortestOptionNameAlphabetically
        implements Comparator<Field> {
            SortByShortestOptionNameAlphabetically() {
            }

            @Override
            public int compare(Field f1, Field f2) {
                Option o1 = f1.getAnnotation(Option.class);
                Option o2 = f2.getAnnotation(Option.class);
                if (o1 == null) {
                    return 1;
                }
                if (o2 == null) {
                    return -1;
                }
                String[] names1 = ShortestFirst.sort(o1.names());
                String[] names2 = ShortestFirst.sort(o2.names());
                int result = names1[0].toUpperCase().compareTo(names2[0].toUpperCase());
                int n = result = result == 0 ? -names1[0].compareTo(names2[0]) : result;
                return o1.help() == o2.help() ? result : (o2.help() ? -1 : 1);
            }
        }

        static class ShortestFirst
        implements Comparator<String> {
            ShortestFirst() {
            }

            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }

            public static String[] sort(String[] names) {
                Arrays.sort(names, new ShortestFirst());
                return names;
            }
        }

        public static class Layout {
            protected final ColorScheme colorScheme;
            protected final TextTable table;
            protected IOptionRenderer optionRenderer;
            protected IParameterRenderer parameterRenderer;

            public Layout(ColorScheme colorScheme) {
                this(colorScheme, new TextTable(colorScheme.ansi()));
            }

            public Layout(ColorScheme colorScheme, TextTable textTable) {
                this(colorScheme, textTable, new DefaultOptionRenderer(), new DefaultParameterRenderer());
            }

            public Layout(ColorScheme colorScheme, TextTable textTable, IOptionRenderer optionRenderer, IParameterRenderer parameterRenderer) {
                this.colorScheme = Assert.notNull(colorScheme, "colorScheme");
                this.table = Assert.notNull(textTable, "textTable");
                this.optionRenderer = Assert.notNull(optionRenderer, "optionRenderer");
                this.parameterRenderer = Assert.notNull(parameterRenderer, "parameterRenderer");
            }

            public void layout(Field field, Ansi.Text[][] cellValues) {
                for (Ansi.Text[] oneRow : cellValues) {
                    this.table.addRowValues(oneRow);
                }
            }

            public void addOptions(List<Field> fields, IParamLabelRenderer paramLabelRenderer) {
                for (Field field : fields) {
                    Option option = field.getAnnotation(Option.class);
                    if (option.hidden()) continue;
                    this.addOption(field, paramLabelRenderer);
                }
            }

            public void addOption(Field field, IParamLabelRenderer paramLabelRenderer) {
                Option option = field.getAnnotation(Option.class);
                Ansi.Text[][] values = this.optionRenderer.render(option, field, paramLabelRenderer, this.colorScheme);
                this.layout(field, values);
            }

            public void addPositionalParameters(List<Field> fields, IParamLabelRenderer paramLabelRenderer) {
                for (Field field : fields) {
                    Parameters parameters = field.getAnnotation(Parameters.class);
                    if (parameters.hidden()) continue;
                    this.addPositionalParameter(field, paramLabelRenderer);
                }
            }

            public void addPositionalParameter(Field field, IParamLabelRenderer paramLabelRenderer) {
                Parameters option = field.getAnnotation(Parameters.class);
                Ansi.Text[][] values = this.parameterRenderer.render(option, field, paramLabelRenderer, this.colorScheme);
                this.layout(field, values);
            }

            public String toString() {
                return this.table.toString();
            }
        }

        static class DefaultParamLabelRenderer
        implements IParamLabelRenderer {
            public final String separator;

            public DefaultParamLabelRenderer(String separator) {
                this.separator = Assert.notNull(separator, "separator");
            }

            @Override
            public String separator() {
                return this.separator;
            }

            @Override
            public Ansi.Text renderParameterLabel(Field field, Ansi ansi, List<Ansi.IStyle> styles) {
                Ansi.Text result;
                block6: {
                    int i;
                    Ansi.Text paramName;
                    String sep;
                    Range arity;
                    boolean isOptionParameter;
                    block4: {
                        block5: {
                            isOptionParameter = field.isAnnotationPresent(Option.class);
                            arity = isOptionParameter ? Range.optionArity(field) : Range.parameterCapacity(field);
                            String split = isOptionParameter ? field.getAnnotation(Option.class).split() : field.getAnnotation(Parameters.class).split();
                            Ansi ansi2 = ansi;
                            ((Object)((Object)ansi2)).getClass();
                            result = ansi2.new Ansi.Text("");
                            sep = isOptionParameter ? this.separator : "";
                            paramName = ansi.apply(DefaultParamLabelRenderer.renderParameterName(field), styles);
                            if (!CommandLine.empty(split)) {
                                paramName = paramName.append("[" + split).append(paramName).append("]...");
                            }
                            for (i = 0; i < arity.min; ++i) {
                                result = result.append(sep).append(paramName);
                                sep = " ";
                            }
                            if (!arity.isVariable) break block4;
                            if (result.length != 0) break block5;
                            result = result.append(sep + "[").append(paramName).append("]...");
                            break block6;
                        }
                        if (result.plainString().endsWith("...")) break block6;
                        result = result.append("...");
                        break block6;
                    }
                    sep = result.length == 0 ? (isOptionParameter ? this.separator : "") : " ";
                    for (i = arity.min; i < arity.max; ++i) {
                        result = sep.trim().length() == 0 ? result.append(sep + "[").append(paramName) : result.append("[" + sep).append(paramName);
                        sep = " ";
                    }
                    for (i = arity.min; i < arity.max; ++i) {
                        result = result.append("]");
                    }
                }
                return result;
            }

            private static String renderParameterName(Field field) {
                String result = null;
                if (field.isAnnotationPresent(Option.class)) {
                    result = field.getAnnotation(Option.class).paramLabel();
                } else if (field.isAnnotationPresent(Parameters.class)) {
                    result = field.getAnnotation(Parameters.class).paramLabel();
                }
                if (result != null && result.trim().length() > 0) {
                    return result.trim();
                }
                String name = field.getName();
                if (Map.class.isAssignableFrom(field.getType())) {
                    Class[] paramTypes = CommandLine.getTypeAttribute(field);
                    name = paramTypes.length < 2 || paramTypes[0] == null || paramTypes[1] == null ? "String=String" : paramTypes[0].getSimpleName() + Help.DEFAULT_SEPARATOR + paramTypes[1].getSimpleName();
                }
                return "<" + name + ">";
            }
        }

        public static interface IParamLabelRenderer {
            public Ansi.Text renderParameterLabel(Field var1, Ansi var2, List<Ansi.IStyle> var3);

            public String separator();
        }

        static class DefaultParameterRenderer
        implements IParameterRenderer {
            public String requiredMarker = " ";

            DefaultParameterRenderer() {
            }

            @Override
            public Ansi.Text[][] render(Parameters params, Field field, IParamLabelRenderer paramLabelRenderer, ColorScheme scheme) {
                int i;
                Ansi.Text label = paramLabelRenderer.renderParameterLabel(field, scheme.ansi(), scheme.parameterStyles);
                Ansi.Text requiredParameter = scheme.parameterText(Range.parameterArity((Field)field).min > 0 ? this.requiredMarker : "");
                Ansi.Text EMPTY = Ansi.EMPTY_TEXT;
                ArrayList<Ansi.Text[]> result = new ArrayList<Ansi.Text[]>();
                Ansi ansi = scheme.ansi();
                ((Object)((Object)ansi)).getClass();
                Ansi.Text[] descriptionFirstLines = ansi.new Ansi.Text(CommandLine.str(params.description(), 0)).splitLines();
                if (descriptionFirstLines.length == 0) {
                    descriptionFirstLines = new Ansi.Text[]{EMPTY};
                }
                result.add(new Ansi.Text[]{requiredParameter, EMPTY, EMPTY, label, descriptionFirstLines[0]});
                for (i = 1; i < descriptionFirstLines.length; ++i) {
                    result.add(new Ansi.Text[]{EMPTY, EMPTY, EMPTY, EMPTY, descriptionFirstLines[i]});
                }
                for (i = 1; i < params.description().length; ++i) {
                    Ansi.Text[] descriptionNextLines;
                    Ansi ansi2 = scheme.ansi();
                    ((Object)((Object)ansi2)).getClass();
                    for (Ansi.Text line : descriptionNextLines = ansi2.new Ansi.Text(params.description()[i]).splitLines()) {
                        result.add(new Ansi.Text[]{EMPTY, EMPTY, EMPTY, EMPTY, line});
                    }
                }
                return (Ansi.Text[][])result.toArray((T[])new Ansi.Text[result.size()][]);
            }
        }

        public static interface IParameterRenderer {
            public Ansi.Text[][] render(Parameters var1, Field var2, IParamLabelRenderer var3, ColorScheme var4);
        }

        static class MinimalParameterRenderer
        implements IParameterRenderer {
            MinimalParameterRenderer() {
            }

            @Override
            public Ansi.Text[][] render(Parameters param, Field field, IParamLabelRenderer parameterLabelRenderer, ColorScheme scheme) {
                Ansi.Text[][] arrtext = new Ansi.Text[1][];
                Ansi.Text[] arrtext2 = new Ansi.Text[2];
                arrtext2[0] = parameterLabelRenderer.renderParameterLabel(field, scheme.ansi(), scheme.parameterStyles);
                Ansi ansi = scheme.ansi();
                ((Object)((Object)ansi)).getClass();
                arrtext2[1] = ansi.new Ansi.Text(param.description().length == 0 ? "" : param.description()[0]);
                arrtext[0] = arrtext2;
                return arrtext;
            }
        }

        static class MinimalOptionRenderer
        implements IOptionRenderer {
            MinimalOptionRenderer() {
            }

            @Override
            public Ansi.Text[][] render(Option option, Field field, IParamLabelRenderer parameterLabelRenderer, ColorScheme scheme) {
                Ansi.Text optionText = scheme.optionText(option.names()[0]);
                Ansi.Text paramLabelText = parameterLabelRenderer.renderParameterLabel(field, scheme.ansi(), scheme.optionParamStyles);
                optionText = optionText.append(paramLabelText);
                Ansi.Text[][] arrtext = new Ansi.Text[1][];
                Ansi.Text[] arrtext2 = new Ansi.Text[2];
                arrtext2[0] = optionText;
                Ansi ansi = scheme.ansi();
                ((Object)((Object)ansi)).getClass();
                arrtext2[1] = ansi.new Ansi.Text(option.description().length == 0 ? "" : option.description()[0]);
                arrtext[0] = arrtext2;
                return arrtext;
            }
        }

        static class DefaultOptionRenderer
        implements IOptionRenderer {
            public String requiredMarker = " ";
            public Object command;
            private String sep;
            private boolean showDefault;

            DefaultOptionRenderer() {
            }

            @Override
            public Ansi.Text[][] render(Option option, Field field, IParamLabelRenderer paramLabelRenderer, ColorScheme scheme) {
                String[] names = ShortestFirst.sort(option.names());
                int shortOptionCount = names[0].length() == 2 ? 1 : 0;
                String shortOption = shortOptionCount > 0 ? names[0] : "";
                this.sep = shortOptionCount > 0 && names.length > 1 ? "," : "";
                String longOption = Help.join(names, shortOptionCount, names.length - shortOptionCount, ", ");
                Ansi.Text longOptionText = this.createLongOptionText(field, paramLabelRenderer, scheme, longOption);
                this.showDefault = this.command != null && !option.help() && !CommandLine.isBoolean(field.getType());
                Object defaultValue = this.createDefaultValue(field);
                String requiredOption = option.required() ? this.requiredMarker : "";
                return this.renderDescriptionLines(option, scheme, requiredOption, shortOption, longOptionText, defaultValue);
            }

            private Object createDefaultValue(Field field) {
                Object defaultValue = null;
                try {
                    defaultValue = field.get(this.command);
                    if (defaultValue == null) {
                        this.showDefault = false;
                    } else if (field.getType().isArray()) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < Array.getLength(defaultValue); ++i) {
                            sb.append(i > 0 ? ", " : "").append(Array.get(defaultValue, i));
                        }
                        defaultValue = sb.insert(0, "[").append("]").toString();
                    }
                }
                catch (Exception ex) {
                    this.showDefault = false;
                }
                return defaultValue;
            }

            private Ansi.Text createLongOptionText(Field field, IParamLabelRenderer renderer, ColorScheme scheme, String longOption) {
                Ansi.Text paramLabelText = renderer.renderParameterLabel(field, scheme.ansi(), scheme.optionParamStyles);
                if (paramLabelText.length > 0 && longOption.length() == 0) {
                    this.sep = renderer.separator();
                    int sepStart = paramLabelText.plainString().indexOf(this.sep);
                    Ansi.Text prefix = paramLabelText.substring(0, sepStart);
                    paramLabelText = prefix.append(paramLabelText.substring(sepStart + this.sep.length()));
                }
                Ansi.Text longOptionText = scheme.optionText(longOption);
                longOptionText = longOptionText.append(paramLabelText);
                return longOptionText;
            }

            private Ansi.Text[][] renderDescriptionLines(Option option, ColorScheme scheme, String requiredOption, String shortOption, Ansi.Text longOptionText, Object defaultValue) {
                int i;
                Ansi.Text EMPTY = Ansi.EMPTY_TEXT;
                ArrayList<Ansi.Text[]> result = new ArrayList<Ansi.Text[]>();
                Ansi ansi = scheme.ansi();
                ((Object)((Object)ansi)).getClass();
                Ansi.Text[] descriptionFirstLines = ansi.new Ansi.Text(CommandLine.str(option.description(), 0)).splitLines();
                if (descriptionFirstLines.length == 0) {
                    if (this.showDefault) {
                        Ansi.Text[] arrtext = new Ansi.Text[1];
                        Ansi ansi2 = scheme.ansi();
                        ((Object)((Object)ansi2)).getClass();
                        arrtext[0] = ansi2.new Ansi.Text("  Default: " + defaultValue);
                        descriptionFirstLines = arrtext;
                        this.showDefault = false;
                    } else {
                        descriptionFirstLines = new Ansi.Text[]{EMPTY};
                    }
                }
                Ansi.Text[] arrtext = new Ansi.Text[5];
                arrtext[0] = scheme.optionText(requiredOption);
                arrtext[1] = scheme.optionText(shortOption);
                Ansi ansi3 = scheme.ansi();
                ((Object)((Object)ansi3)).getClass();
                arrtext[2] = ansi3.new Ansi.Text(this.sep);
                arrtext[3] = longOptionText;
                arrtext[4] = descriptionFirstLines[0];
                result.add(arrtext);
                for (i = 1; i < descriptionFirstLines.length; ++i) {
                    result.add(new Ansi.Text[]{EMPTY, EMPTY, EMPTY, EMPTY, descriptionFirstLines[i]});
                }
                for (i = 1; i < option.description().length; ++i) {
                    Ansi.Text[] descriptionNextLines;
                    Ansi ansi4 = scheme.ansi();
                    ((Object)((Object)ansi4)).getClass();
                    for (Ansi.Text line : descriptionNextLines = ansi4.new Ansi.Text(option.description()[i]).splitLines()) {
                        result.add(new Ansi.Text[]{EMPTY, EMPTY, EMPTY, EMPTY, line});
                    }
                }
                if (this.showDefault) {
                    Ansi.Text[] arrtext2 = new Ansi.Text[5];
                    arrtext2[0] = EMPTY;
                    arrtext2[1] = EMPTY;
                    arrtext2[2] = EMPTY;
                    arrtext2[3] = EMPTY;
                    Ansi ansi5 = scheme.ansi();
                    ((Object)((Object)ansi5)).getClass();
                    arrtext2[4] = ansi5.new Ansi.Text("  Default: " + defaultValue);
                    result.add(arrtext2);
                }
                return (Ansi.Text[][])result.toArray((T[])new Ansi.Text[result.size()][]);
            }
        }

        public static interface IOptionRenderer {
            public Ansi.Text[][] render(Option var1, Field var2, IParamLabelRenderer var3, ColorScheme var4);
        }
    }

    private static class BuiltIn {
        private BuiltIn() {
        }

        static class UUIDConverter
        implements ITypeConverter<UUID> {
            UUIDConverter() {
            }

            @Override
            public UUID convert(String s) throws Exception {
                return UUID.fromString(s);
            }
        }

        static class PatternConverter
        implements ITypeConverter<Pattern> {
            PatternConverter() {
            }

            @Override
            public Pattern convert(String s) {
                return Pattern.compile(s);
            }
        }

        static class InetAddressConverter
        implements ITypeConverter<InetAddress> {
            InetAddressConverter() {
            }

            @Override
            public InetAddress convert(String s) throws Exception {
                return InetAddress.getByName(s);
            }
        }

        static class CharsetConverter
        implements ITypeConverter<Charset> {
            CharsetConverter() {
            }

            @Override
            public Charset convert(String s) {
                return Charset.forName(s);
            }
        }

        static class BigIntegerConverter
        implements ITypeConverter<BigInteger> {
            BigIntegerConverter() {
            }

            @Override
            public BigInteger convert(String value) {
                return new BigInteger(value);
            }
        }

        static class BigDecimalConverter
        implements ITypeConverter<BigDecimal> {
            BigDecimalConverter() {
            }

            @Override
            public BigDecimal convert(String value) {
                return new BigDecimal(value);
            }
        }

        static class ISO8601TimeConverter
        implements ITypeConverter<Time> {
            ISO8601TimeConverter() {
            }

            @Override
            public Time convert(String value) {
                try {
                    if (value.length() <= 5) {
                        return new Time(new SimpleDateFormat("HH:mm").parse(value).getTime());
                    }
                    if (value.length() <= 8) {
                        return new Time(new SimpleDateFormat("HH:mm:ss").parse(value).getTime());
                    }
                    if (value.length() <= 12) {
                        try {
                            return new Time(new SimpleDateFormat("HH:mm:ss.SSS").parse(value).getTime());
                        }
                        catch (ParseException e2) {
                            return new Time(new SimpleDateFormat("HH:mm:ss,SSS").parse(value).getTime());
                        }
                    }
                }
                catch (ParseException parseException) {
                    // empty catch block
                }
                throw new TypeConversionException("'" + value + "' is not a HH:mm[:ss[.SSS]] time");
            }
        }

        static class ISO8601DateConverter
        implements ITypeConverter<Date> {
            ISO8601DateConverter() {
            }

            @Override
            public Date convert(String value) {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd").parse(value);
                }
                catch (ParseException e) {
                    throw new TypeConversionException("'" + value + "' is not a yyyy-MM-dd date");
                }
            }
        }

        static class URIConverter
        implements ITypeConverter<URI> {
            URIConverter() {
            }

            @Override
            public URI convert(String value) throws URISyntaxException {
                return new URI(value);
            }
        }

        static class URLConverter
        implements ITypeConverter<URL> {
            URLConverter() {
            }

            @Override
            public URL convert(String value) throws MalformedURLException {
                return new URL(value);
            }
        }

        static class FileConverter
        implements ITypeConverter<File> {
            FileConverter() {
            }

            @Override
            public File convert(String value) {
                return new File(value);
            }
        }

        static class DoubleConverter
        implements ITypeConverter<Double> {
            DoubleConverter() {
            }

            @Override
            public Double convert(String value) {
                return Double.valueOf(value);
            }
        }

        static class FloatConverter
        implements ITypeConverter<Float> {
            FloatConverter() {
            }

            @Override
            public Float convert(String value) {
                return Float.valueOf(value);
            }
        }

        static class LongConverter
        implements ITypeConverter<Long> {
            LongConverter() {
            }

            @Override
            public Long convert(String value) {
                return Long.valueOf(value);
            }
        }

        static class IntegerConverter
        implements ITypeConverter<Integer> {
            IntegerConverter() {
            }

            @Override
            public Integer convert(String value) {
                return Integer.valueOf(value);
            }
        }

        static class ShortConverter
        implements ITypeConverter<Short> {
            ShortConverter() {
            }

            @Override
            public Short convert(String value) {
                return Short.valueOf(value);
            }
        }

        static class CharacterConverter
        implements ITypeConverter<Character> {
            CharacterConverter() {
            }

            @Override
            public Character convert(String value) {
                if (value.length() > 1) {
                    throw new TypeConversionException("'" + value + "' is not a single character");
                }
                return Character.valueOf(value.charAt(0));
            }
        }

        static class BooleanConverter
        implements ITypeConverter<Boolean> {
            BooleanConverter() {
            }

            @Override
            public Boolean convert(String value) {
                if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                    return Boolean.parseBoolean(value);
                }
                throw new TypeConversionException("'" + value + "' is not a boolean");
            }
        }

        static class ByteConverter
        implements ITypeConverter<Byte> {
            ByteConverter() {
            }

            @Override
            public Byte convert(String value) {
                return Byte.valueOf(value);
            }
        }

        static class CharSequenceConverter
        implements ITypeConverter<CharSequence> {
            CharSequenceConverter() {
            }

            @Override
            public String convert(String value) {
                return value;
            }
        }

        static class StringBuilderConverter
        implements ITypeConverter<StringBuilder> {
            StringBuilderConverter() {
            }

            @Override
            public StringBuilder convert(String value) {
                return new StringBuilder(value);
            }
        }

        static class StringConverter
        implements ITypeConverter<String> {
            StringConverter() {
            }

            @Override
            public String convert(String value) {
                return value;
            }
        }

        static class PathConverter
        implements ITypeConverter<Path> {
            PathConverter() {
            }

            @Override
            public Path convert(String value) {
                return Paths.get(value, new String[0]);
            }
        }
    }

    private static class PositionalParametersSorter
    implements Comparator<Field> {
        private PositionalParametersSorter() {
        }

        @Override
        public int compare(Field o1, Field o2) {
            int result = Range.parameterIndex(o1).compareTo(Range.parameterIndex(o2));
            return result == 0 ? Range.parameterArity(o1).compareTo(Range.parameterArity(o2)) : result;
        }
    }

    private class Interpreter {
        private final Map<String, CommandLine> commands = new LinkedHashMap<String, CommandLine>();
        private final Map<Class<?>, ITypeConverter<?>> converterRegistry = new HashMap();
        private final Map<String, Field> optionName2Field = new HashMap<String, Field>();
        private final Map<Character, Field> singleCharOption2Field = new HashMap<Character, Field>();
        private final List<Field> requiredFields = new ArrayList<Field>();
        private final List<Field> positionalParametersFields = new ArrayList<Field>();
        private final Object command;
        private boolean isHelpRequested;
        private String separator = "=";
        private int position;

        Interpreter(Object command) {
            this.converterRegistry.put(Path.class, new BuiltIn.PathConverter());
            this.converterRegistry.put(Object.class, new BuiltIn.StringConverter());
            this.converterRegistry.put(String.class, new BuiltIn.StringConverter());
            this.converterRegistry.put(StringBuilder.class, new BuiltIn.StringBuilderConverter());
            this.converterRegistry.put(CharSequence.class, new BuiltIn.CharSequenceConverter());
            this.converterRegistry.put(Byte.class, new BuiltIn.ByteConverter());
            this.converterRegistry.put(Byte.TYPE, new BuiltIn.ByteConverter());
            this.converterRegistry.put(Boolean.class, new BuiltIn.BooleanConverter());
            this.converterRegistry.put(Boolean.TYPE, new BuiltIn.BooleanConverter());
            this.converterRegistry.put(Character.class, new BuiltIn.CharacterConverter());
            this.converterRegistry.put(Character.TYPE, new BuiltIn.CharacterConverter());
            this.converterRegistry.put(Short.class, new BuiltIn.ShortConverter());
            this.converterRegistry.put(Short.TYPE, new BuiltIn.ShortConverter());
            this.converterRegistry.put(Integer.class, new BuiltIn.IntegerConverter());
            this.converterRegistry.put(Integer.TYPE, new BuiltIn.IntegerConverter());
            this.converterRegistry.put(Long.class, new BuiltIn.LongConverter());
            this.converterRegistry.put(Long.TYPE, new BuiltIn.LongConverter());
            this.converterRegistry.put(Float.class, new BuiltIn.FloatConverter());
            this.converterRegistry.put(Float.TYPE, new BuiltIn.FloatConverter());
            this.converterRegistry.put(Double.class, new BuiltIn.DoubleConverter());
            this.converterRegistry.put(Double.TYPE, new BuiltIn.DoubleConverter());
            this.converterRegistry.put(File.class, new BuiltIn.FileConverter());
            this.converterRegistry.put(URI.class, new BuiltIn.URIConverter());
            this.converterRegistry.put(URL.class, new BuiltIn.URLConverter());
            this.converterRegistry.put(Date.class, new BuiltIn.ISO8601DateConverter());
            this.converterRegistry.put(Time.class, new BuiltIn.ISO8601TimeConverter());
            this.converterRegistry.put(BigDecimal.class, new BuiltIn.BigDecimalConverter());
            this.converterRegistry.put(BigInteger.class, new BuiltIn.BigIntegerConverter());
            this.converterRegistry.put(Charset.class, new BuiltIn.CharsetConverter());
            this.converterRegistry.put(InetAddress.class, new BuiltIn.InetAddressConverter());
            this.converterRegistry.put(Pattern.class, new BuiltIn.PatternConverter());
            this.converterRegistry.put(UUID.class, new BuiltIn.UUIDConverter());
            this.command = Assert.notNull(command, "command");
            String declaredName = null;
            String declaredSeparator = null;
            boolean hasCommandAnnotation = false;
            for (Class<?> cls = command.getClass(); cls != null; cls = cls.getSuperclass()) {
                CommandLine.init(cls, this.requiredFields, this.optionName2Field, this.singleCharOption2Field, this.positionalParametersFields);
                if (!cls.isAnnotationPresent(Command.class)) continue;
                hasCommandAnnotation = true;
                Command cmd = cls.getAnnotation(Command.class);
                declaredSeparator = declaredSeparator == null ? cmd.separator() : declaredSeparator;
                declaredName = declaredName == null ? cmd.name() : declaredName;
                CommandLine.this.versionLines.addAll(Arrays.asList(cmd.version()));
                for (Class<?> sub : cmd.subcommands()) {
                    Command subCommand = sub.getAnnotation(Command.class);
                    if (subCommand == null || "<main class>".equals(subCommand.name())) {
                        throw new InitializationException("Subcommand " + sub.getName() + " is missing the mandatory @Command annotation with a 'name' attribute");
                    }
                    try {
                        Constructor<?> constructor = sub.getDeclaredConstructor(new Class[0]);
                        constructor.setAccessible(true);
                        CommandLine commandLine2 = CommandLine.toCommandLine(constructor.newInstance(new Object[0]));
                        commandLine2.parent = CommandLine.this;
                        this.commands.put(subCommand.name(), commandLine2);
                    }
                    catch (InitializationException ex) {
                        throw ex;
                    }
                    catch (NoSuchMethodException ex) {
                        throw new InitializationException("Cannot instantiate subcommand " + sub.getName() + ": the class has no constructor", ex);
                    }
                    catch (Exception ex) {
                        throw new InitializationException("Could not instantiate and add subcommand " + sub.getName() + ": " + ex, ex);
                    }
                }
            }
            this.separator = declaredSeparator != null ? declaredSeparator : this.separator;
            CommandLine.this.commandName = declaredName != null ? declaredName : CommandLine.this.commandName;
            Collections.sort(this.positionalParametersFields, new PositionalParametersSorter());
            CommandLine.validatePositionalParameters(this.positionalParametersFields);
            if (this.positionalParametersFields.isEmpty() && this.optionName2Field.isEmpty() && !hasCommandAnnotation) {
                throw new InitializationException(command + " (" + command.getClass() + ") is not a command: it has no @Command, @Option or @Parameters annotations");
            }
        }

        List<CommandLine> parse(String ... args) {
            Assert.notNull(args, "argument array");
            if (CommandLine.this.tracer.isInfo()) {
                CommandLine.this.tracer.info("Parsing %d command line args %s%n", args.length, Arrays.toString(args));
            }
            Stack<String> arguments = new Stack<String>();
            for (int i = args.length - 1; i >= 0; --i) {
                arguments.push(args[i]);
            }
            ArrayList<CommandLine> result = new ArrayList<CommandLine>();
            this.parse(result, arguments, args);
            return result;
        }

        private void parse(List<CommandLine> parsedCommands, Stack<String> argumentStack, String[] originalArgs) {
            this.isHelpRequested = false;
            CommandLine.this.versionHelpRequested = false;
            CommandLine.this.usageHelpRequested = false;
            Class<?> cmdClass = this.command.getClass();
            if (CommandLine.this.tracer.isDebug()) {
                CommandLine.this.tracer.debug("Initializing %s: %d options, %d positional parameters, %d required, %d subcommands.%n", cmdClass.getName(), new HashSet<Field>(this.optionName2Field.values()).size(), this.positionalParametersFields.size(), this.requiredFields.size(), this.commands.size());
            }
            parsedCommands.add(CommandLine.this);
            ArrayList<Field> required = new ArrayList<Field>(this.requiredFields);
            HashSet<Field> initialized = new HashSet<Field>();
            Collections.sort(required, new PositionalParametersSorter());
            try {
                this.processArguments(parsedCommands, argumentStack, required, initialized, originalArgs);
            }
            catch (ParameterException ex) {
                throw ex;
            }
            catch (Exception ex) {
                int offendingArgIndex = originalArgs.length - argumentStack.size() - 1;
                String arg = offendingArgIndex >= 0 && offendingArgIndex < originalArgs.length ? originalArgs[offendingArgIndex] : "?";
                throw ParameterException.create(CommandLine.this, ex, arg, offendingArgIndex, originalArgs);
            }
            if (!this.isAnyHelpRequested() && !required.isEmpty()) {
                for (Field missing : required) {
                    if (missing.isAnnotationPresent(Option.class)) {
                        throw MissingParameterException.create(CommandLine.this, required, this.separator);
                    }
                    this.assertNoMissingParameters(missing, Range.parameterArity((Field)missing).min, argumentStack);
                }
            }
            if (!CommandLine.this.unmatchedArguments.isEmpty()) {
                if (!CommandLine.this.isUnmatchedArgumentsAllowed()) {
                    throw new UnmatchedArgumentException(CommandLine.this, CommandLine.this.unmatchedArguments);
                }
                if (CommandLine.this.tracer.isWarn()) {
                    CommandLine.this.tracer.warn("Unmatched arguments: %s%n", CommandLine.this.unmatchedArguments);
                }
            }
        }

        private void processArguments(List<CommandLine> parsedCommands, Stack<String> args, Collection<Field> required, Set<Field> initialized, String[] originalArgs) throws Exception {
            while (!args.isEmpty()) {
                String arg = args.pop();
                if (CommandLine.this.tracer.isDebug()) {
                    CommandLine.this.tracer.debug("Processing argument '%s'. Remainder=%s%n", arg, CommandLine.reverse((Stack)args.clone()));
                }
                if ("--".equals(arg)) {
                    CommandLine.this.tracer.info("Found end-of-options delimiter '--'. Treating remainder as positional parameters.%n", new Object[0]);
                    this.processRemainderAsPositionalParameters(required, initialized, args);
                    return;
                }
                if (this.commands.containsKey(arg)) {
                    if (!this.isHelpRequested && !required.isEmpty()) {
                        throw MissingParameterException.create(CommandLine.this, required, this.separator);
                    }
                    if (CommandLine.this.tracer.isDebug()) {
                        CommandLine.this.tracer.debug("Found subcommand '%s' (%s)%n", arg, ((CommandLine)this.commands.get((Object)arg)).interpreter.command.getClass().getName());
                    }
                    this.commands.get(arg).interpreter.parse(parsedCommands, args, originalArgs);
                    return;
                }
                boolean paramAttachedToOption = false;
                int separatorIndex = arg.indexOf(this.separator);
                if (separatorIndex > 0) {
                    String key = arg.substring(0, separatorIndex);
                    if (this.optionName2Field.containsKey(key) && !this.optionName2Field.containsKey(arg)) {
                        paramAttachedToOption = true;
                        String optionParam = arg.substring(separatorIndex + this.separator.length());
                        args.push(optionParam);
                        arg = key;
                        if (CommandLine.this.tracer.isDebug()) {
                            CommandLine.this.tracer.debug("Separated '%s' option from '%s' option parameter%n", key, optionParam);
                        }
                    } else if (CommandLine.this.tracer.isDebug()) {
                        CommandLine.this.tracer.debug("'%s' contains separator '%s' but '%s' is not a known option%n", arg, this.separator, key);
                    }
                } else if (CommandLine.this.tracer.isDebug()) {
                    CommandLine.this.tracer.debug("'%s' cannot be separated into <option>%s<option-parameter>%n", arg, this.separator);
                }
                if (this.optionName2Field.containsKey(arg)) {
                    this.processStandaloneOption(required, initialized, arg, args, paramAttachedToOption);
                    continue;
                }
                if (arg.length() > 2 && arg.startsWith("-")) {
                    if (CommandLine.this.tracer.isDebug()) {
                        CommandLine.this.tracer.debug("Trying to process '%s' as clustered short options%n", arg, args);
                    }
                    this.processClusteredShortOptions(required, initialized, arg, args);
                    continue;
                }
                args.push(arg);
                if (CommandLine.this.tracer.isDebug()) {
                    CommandLine.this.tracer.debug("Could not find option '%s', deciding whether to treat as unmatched option or positional parameter...%n", arg);
                }
                if (this.resemblesOption(arg)) {
                    this.handleUnmatchedArguments(args.pop());
                    continue;
                }
                if (CommandLine.this.tracer.isDebug()) {
                    CommandLine.this.tracer.debug("No option named '%s' found. Processing remainder as positional parameters%n", arg);
                }
                this.processPositionalParameter(required, initialized, args);
            }
        }

        private boolean resemblesOption(String arg) {
            boolean result;
            int count = 0;
            for (String optionName : this.optionName2Field.keySet()) {
                for (int i = 0; i < arg.length() && optionName.length() > i && arg.charAt(i) == optionName.charAt(i); ++i) {
                    ++count;
                }
            }
            boolean bl = result = count > 0 && count * 10 >= this.optionName2Field.size() * 9;
            if (CommandLine.this.tracer.isDebug()) {
                CommandLine.this.tracer.debug("%s %s an option: %d matching prefix chars out of %d option names%n", arg, result ? "resembles" : "doesn't resemble", count, this.optionName2Field.size());
            }
            return result;
        }

        private void handleUnmatchedArguments(String arg) {
            Stack<String> args = new Stack<String>();
            args.add(arg);
            this.handleUnmatchedArguments(args);
        }

        private void handleUnmatchedArguments(Stack<String> args) {
            while (!args.isEmpty()) {
                CommandLine.this.unmatchedArguments.add(args.pop());
            }
        }

        private void processRemainderAsPositionalParameters(Collection<Field> required, Set<Field> initialized, Stack<String> args) throws Exception {
            while (!args.empty()) {
                this.processPositionalParameter(required, initialized, args);
            }
        }

        private void processPositionalParameter(Collection<Field> required, Set<Field> initialized, Stack<String> args) throws Exception {
            if (CommandLine.this.tracer.isDebug()) {
                CommandLine.this.tracer.debug("Processing next arg as a positional parameter at index=%d. Remainder=%s%n", this.position, CommandLine.reverse((Stack)args.clone()));
            }
            int consumed = 0;
            for (Field positionalParam : this.positionalParametersFields) {
                Range indexRange = Range.parameterIndex(positionalParam);
                if (!indexRange.contains(this.position)) continue;
                Stack argsCopy = (Stack)args.clone();
                Range arity = Range.parameterArity(positionalParam);
                if (CommandLine.this.tracer.isDebug()) {
                    CommandLine.this.tracer.debug("Position %d is in index range %s. Trying to assign args to %s, arity=%s%n", this.position, indexRange, positionalParam, arity);
                }
                this.assertNoMissingParameters(positionalParam, arity.min, argsCopy);
                int originalSize = argsCopy.size();
                this.applyOption(positionalParam, Parameters.class, arity, false, argsCopy, initialized, "args[" + indexRange + "] at position " + this.position);
                int count = originalSize - argsCopy.size();
                if (count > 0) {
                    required.remove(positionalParam);
                }
                consumed = Math.max(consumed, count);
            }
            for (int i = 0; i < consumed; ++i) {
                args.pop();
            }
            this.position += consumed;
            if (CommandLine.this.tracer.isDebug()) {
                CommandLine.this.tracer.debug("Consumed %d arguments, moving position to index %d.%n", consumed, this.position);
            }
            if (consumed == 0 && !args.isEmpty()) {
                this.handleUnmatchedArguments(args.pop());
            }
        }

        private void processStandaloneOption(Collection<Field> required, Set<Field> initialized, String arg, Stack<String> args, boolean paramAttachedToKey) throws Exception {
            Field field = this.optionName2Field.get(arg);
            required.remove(field);
            Range arity = Range.optionArity(field);
            if (paramAttachedToKey) {
                arity = arity.min(Math.max(1, arity.min));
            }
            if (CommandLine.this.tracer.isDebug()) {
                CommandLine.this.tracer.debug("Found option named '%s': field %s, arity=%s%n", arg, field, arity);
            }
            this.applyOption(field, Option.class, arity, paramAttachedToKey, args, initialized, "option " + arg);
        }

        private void processClusteredShortOptions(Collection<Field> required, Set<Field> initialized, String arg, Stack<String> args) throws Exception {
            String prefix = arg.substring(0, 1);
            String cluster = arg.substring(1);
            boolean paramAttachedToOption = true;
            while (cluster.length() > 0 && this.singleCharOption2Field.containsKey(Character.valueOf(cluster.charAt(0)))) {
                Field field = this.singleCharOption2Field.get(Character.valueOf(cluster.charAt(0)));
                Range arity = Range.optionArity(field);
                String argDescription = "option " + prefix + cluster.charAt(0);
                if (CommandLine.this.tracer.isDebug()) {
                    CommandLine.this.tracer.debug("Found option '%s%s' in %s: field %s, arity=%s%n", prefix, Character.valueOf(cluster.charAt(0)), arg, field, arity);
                }
                required.remove(field);
                cluster = cluster.length() > 0 ? cluster.substring(1) : "";
                boolean bl = paramAttachedToOption = cluster.length() > 0;
                if (cluster.startsWith(this.separator)) {
                    cluster = cluster.substring(this.separator.length());
                    arity = arity.min(Math.max(1, arity.min));
                }
                if (arity.min > 0 && !CommandLine.empty(cluster) && CommandLine.this.tracer.isDebug()) {
                    CommandLine.this.tracer.debug("Trying to process '%s' as option parameter%n", cluster);
                }
                if (!CommandLine.empty(cluster)) {
                    args.push(cluster);
                }
                int consumed = this.applyOption(field, Option.class, arity, paramAttachedToOption, args, initialized, argDescription);
                if (CommandLine.empty(cluster) || consumed > 0 || args.isEmpty()) {
                    return;
                }
                cluster = args.pop();
            }
            if (cluster.length() == 0) {
                return;
            }
            if (arg.endsWith(cluster)) {
                args.push(paramAttachedToOption ? prefix + cluster : cluster);
                if (args.peek().equals(arg)) {
                    if (CommandLine.this.tracer.isDebug()) {
                        CommandLine.this.tracer.debug("Could not match any short options in %s, deciding whether to treat as unmatched option or positional parameter...%n", arg);
                    }
                    if (this.resemblesOption(arg)) {
                        this.handleUnmatchedArguments(args.pop());
                        return;
                    }
                    this.processPositionalParameter(required, initialized, args);
                    return;
                }
                if (CommandLine.this.tracer.isDebug()) {
                    CommandLine.this.tracer.debug("No option found for %s in %s%n", cluster, arg);
                }
                this.handleUnmatchedArguments(args.pop());
            } else {
                args.push(cluster);
                if (CommandLine.this.tracer.isDebug()) {
                    CommandLine.this.tracer.debug("%s is not an option parameter for %s%n", cluster, arg);
                }
                this.processPositionalParameter(required, initialized, args);
            }
        }

        private int applyOption(Field field, Class<?> annotation, Range arity, boolean valueAttachedToOption, Stack<String> args, Set<Field> initialized, String argDescription) throws Exception {
            this.updateHelpRequested(field);
            int length = args.size();
            this.assertNoMissingParameters(field, arity.min, args);
            Class cls = field.getType();
            if (cls.isArray()) {
                return this.applyValuesToArrayField(field, annotation, arity, args, cls, argDescription);
            }
            if (Collection.class.isAssignableFrom(cls)) {
                return this.applyValuesToCollectionField(field, annotation, arity, args, cls, argDescription);
            }
            if (Map.class.isAssignableFrom(cls)) {
                return this.applyValuesToMapField(field, annotation, arity, args, cls, argDescription);
            }
            cls = CommandLine.getTypeAttribute(field)[0];
            return this.applyValueToSingleValuedField(field, arity, args, cls, initialized, argDescription);
        }

        private int applyValueToSingleValuedField(Field field, Range arity, Stack<String> args, Class<?> cls, Set<Field> initialized, String argDescription) throws Exception {
            boolean noMoreValues = args.isEmpty();
            String value = args.isEmpty() ? null : this.trim(args.pop());
            int result = arity.min;
            if ((cls == Boolean.class || cls == Boolean.TYPE) && arity.min <= 0) {
                if (arity.max > 0 && ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value))) {
                    result = 1;
                } else {
                    Boolean currentValue;
                    if (value != null) {
                        args.push(value);
                    }
                    value = String.valueOf((currentValue = (Boolean)field.get(this.command)) == null ? true : currentValue == false);
                }
            }
            if (noMoreValues && value == null) {
                return 0;
            }
            ITypeConverter<?> converter = this.getTypeConverter(cls, field);
            Object newValue = this.tryConvert(field, -1, converter, value, cls);
            Object oldValue = field.get(this.command);
            TraceLevel level = TraceLevel.INFO;
            String traceMessage = "Setting %s field '%s.%s' to '%5$s' (was '%4$s') for %6$s%n";
            if (initialized != null) {
                if (initialized.contains(field)) {
                    if (!CommandLine.this.isOverwrittenOptionsAllowed()) {
                        throw new OverwrittenOptionException(CommandLine.this, this.optionDescription("", field, 0) + " should be specified only once");
                    }
                    level = TraceLevel.WARN;
                    traceMessage = "Overwriting %s field '%s.%s' value '%s' with '%s' for %s%n";
                }
                initialized.add(field);
            }
            if (((CommandLine)CommandLine.this).tracer.level.isEnabled(level)) {
                level.print(CommandLine.this.tracer, traceMessage, new Object[]{field.getType().getSimpleName(), field.getDeclaringClass().getSimpleName(), field.getName(), String.valueOf(oldValue), String.valueOf(newValue), argDescription});
            }
            field.set(this.command, newValue);
            return result;
        }

        private int applyValuesToMapField(Field field, Class<?> annotation, Range arity, Stack<String> args, Class<?> cls, String argDescription) throws Exception {
            Class[] classes = CommandLine.getTypeAttribute(field);
            if (classes.length < 2) {
                throw new ParameterException(CommandLine.this, "Field " + field + " needs two types (one for the map key, one for the value) but only has " + classes.length + " types configured.");
            }
            ITypeConverter<?> keyConverter = this.getTypeConverter(classes[0], field);
            ITypeConverter<?> valueConverter = this.getTypeConverter(classes[1], field);
            Map<Object, Object> result = (Map<Object, Object>)field.get(this.command);
            if (result == null) {
                result = this.createMap(cls);
                field.set(this.command, result);
            }
            int originalSize = result.size();
            this.consumeMapArguments(field, arity, args, classes, keyConverter, valueConverter, result, argDescription);
            return result.size() - originalSize;
        }

        private void consumeMapArguments(Field field, Range arity, Stack<String> args, Class<?>[] classes, ITypeConverter<?> keyConverter, ITypeConverter<?> valueConverter, Map<Object, Object> result, String argDescription) throws Exception {
            int i;
            for (i = 0; i < arity.min; ++i) {
                this.consumeOneMapArgument(field, arity, args, classes, keyConverter, valueConverter, result, i, argDescription);
            }
            for (i = arity.min; i < arity.max && !args.isEmpty(); ++i) {
                if (!field.isAnnotationPresent(Parameters.class) && (this.commands.containsKey(args.peek()) || this.isOption(args.peek()))) {
                    return;
                }
                this.consumeOneMapArgument(field, arity, args, classes, keyConverter, valueConverter, result, i, argDescription);
            }
        }

        private void consumeOneMapArgument(Field field, Range arity, Stack<String> args, Class<?>[] classes, ITypeConverter<?> keyConverter, ITypeConverter<?> valueConverter, Map<Object, Object> result, int index, String argDescription) throws Exception {
            String[] values;
            for (String value : values = this.split(this.trim(args.pop()), field)) {
                String[] keyValue = value.split("=");
                if (keyValue.length < 2) {
                    String splitRegex = this.splitRegex(field);
                    if (splitRegex.length() == 0) {
                        throw new ParameterException(CommandLine.this, "Value for option " + this.optionDescription("", field, 0) + " should be in KEY=VALUE format but was " + value);
                    }
                    throw new ParameterException(CommandLine.this, "Value for option " + this.optionDescription("", field, 0) + " should be in KEY=VALUE[" + splitRegex + "KEY=VALUE]... format but was " + value);
                }
                Object mapKey = this.tryConvert(field, index, keyConverter, keyValue[0], classes[0]);
                Object mapValue = this.tryConvert(field, index, valueConverter, keyValue[1], classes[1]);
                result.put(mapKey, mapValue);
                if (!CommandLine.this.tracer.isInfo()) continue;
                CommandLine.this.tracer.info("Putting [%s : %s] in %s<%s, %s> field '%s.%s' for %s%n", String.valueOf(mapKey), String.valueOf(mapValue), result.getClass().getSimpleName(), classes[0].getSimpleName(), classes[1].getSimpleName(), field.getDeclaringClass().getSimpleName(), field.getName(), argDescription);
            }
        }

        private void checkMaxArityExceeded(Range arity, int remainder, Field field, String[] values) {
            if (values.length <= remainder) {
                return;
            }
            String desc = arity.max == remainder ? "" + remainder : arity + ", remainder=" + remainder;
            throw new MaxValuesforFieldExceededException(CommandLine.this, this.optionDescription("", field, -1) + " max number of values (" + arity.max + ") exceeded: remainder is " + remainder + " but " + values.length + " values were specified: " + Arrays.toString(values));
        }

        private int applyValuesToArrayField(Field field, Class<?> annotation, Range arity, Stack<String> args, Class<?> cls, String argDescription) throws Exception {
            Object existing = field.get(this.command);
            int length = existing == null ? 0 : Array.getLength(existing);
            Class type = CommandLine.getTypeAttribute(field)[0];
            List<Object> converted = this.consumeArguments(field, annotation, arity, args, type, length, argDescription);
            ArrayList<Object> newValues = new ArrayList<Object>();
            for (int i = 0; i < length; ++i) {
                newValues.add(Array.get(existing, i));
            }
            for (Object obj : converted) {
                if (obj instanceof Collection) {
                    newValues.addAll((Collection)obj);
                    continue;
                }
                newValues.add(obj);
            }
            Object array = Array.newInstance(type, newValues.size());
            field.set(this.command, array);
            for (int i = 0; i < newValues.size(); ++i) {
                Array.set(array, i, newValues.get(i));
            }
            return converted.size();
        }

        private int applyValuesToCollectionField(Field field, Class<?> annotation, Range arity, Stack<String> args, Class<?> cls, String argDescription) throws Exception {
            Collection<Object> collection = (Collection<Object>)field.get(this.command);
            Class type = CommandLine.getTypeAttribute(field)[0];
            int length = collection == null ? 0 : collection.size();
            List<Object> converted = this.consumeArguments(field, annotation, arity, args, type, length, argDescription);
            if (collection == null) {
                collection = this.createCollection(cls);
                field.set(this.command, collection);
            }
            for (Object element : converted) {
                if (element instanceof Collection) {
                    collection.addAll((Collection)element);
                    continue;
                }
                collection.add(element);
            }
            return converted.size();
        }

        private List<Object> consumeArguments(Field field, Class<?> annotation, Range arity, Stack<String> args, Class<?> type, int originalSize, String argDescription) throws Exception {
            int i;
            ArrayList<Object> result = new ArrayList<Object>();
            for (i = 0; i < arity.min; ++i) {
                this.consumeOneArgument(field, arity, args, type, result, i, originalSize, argDescription);
            }
            for (i = arity.min; i < arity.max && !args.isEmpty(); ++i) {
                if (annotation != Parameters.class && (this.commands.containsKey(args.peek()) || this.isOption(args.peek()))) {
                    return result;
                }
                this.consumeOneArgument(field, arity, args, type, result, i, originalSize, argDescription);
            }
            return result;
        }

        private int consumeOneArgument(Field field, Range arity, Stack<String> args, Class<?> type, List<Object> result, int index, int originalSize, String argDescription) throws Exception {
            String[] values = this.split(this.trim(args.pop()), field);
            ITypeConverter<?> converter = this.getTypeConverter(type, field);
            for (int j = 0; j < values.length; ++j) {
                result.add(this.tryConvert(field, index, converter, values[j], type));
                if (!CommandLine.this.tracer.isInfo()) continue;
                if (field.getType().isArray()) {
                    CommandLine.this.tracer.info("Adding [%s] to %s[] field '%s.%s' for %s%n", String.valueOf(result.get(result.size() - 1)), type.getSimpleName(), field.getDeclaringClass().getSimpleName(), field.getName(), argDescription);
                    continue;
                }
                CommandLine.this.tracer.info("Adding [%s] to %s<%s> field '%s.%s' for %s%n", String.valueOf(result.get(result.size() - 1)), field.getType().getSimpleName(), type.getSimpleName(), field.getDeclaringClass().getSimpleName(), field.getName(), argDescription);
            }
            return ++index;
        }

        private String splitRegex(Field field) {
            if (field.isAnnotationPresent(Option.class)) {
                return field.getAnnotation(Option.class).split();
            }
            if (field.isAnnotationPresent(Parameters.class)) {
                return field.getAnnotation(Parameters.class).split();
            }
            return "";
        }

        private String[] split(String value, Field field) {
            String[] arrstring;
            String regex = this.splitRegex(field);
            if (regex.length() == 0) {
                String[] arrstring2 = new String[1];
                arrstring = arrstring2;
                arrstring2[0] = value;
            } else {
                arrstring = value.split(regex);
            }
            return arrstring;
        }

        private boolean isOption(String arg) {
            if ("--".equals(arg)) {
                return true;
            }
            if (this.optionName2Field.containsKey(arg)) {
                return true;
            }
            int separatorIndex = arg.indexOf(this.separator);
            if (separatorIndex > 0 && this.optionName2Field.containsKey(arg.substring(0, separatorIndex))) {
                return true;
            }
            return arg.length() > 2 && arg.startsWith("-") && this.singleCharOption2Field.containsKey(Character.valueOf(arg.charAt(1)));
        }

        private Object tryConvert(Field field, int index, ITypeConverter<?> converter, String value, Class<?> type) throws Exception {
            try {
                return converter.convert(value);
            }
            catch (TypeConversionException ex) {
                throw new ParameterException(CommandLine.this, ex.getMessage() + this.optionDescription(" for ", field, index));
            }
            catch (Exception other) {
                String desc = this.optionDescription(" for ", field, index) + ": " + other;
                throw new ParameterException(CommandLine.this, "Could not convert '" + value + "' to " + type.getSimpleName() + desc, other);
            }
        }

        private String optionDescription(String prefix, Field field, int index) {
            Help.IParamLabelRenderer labelRenderer = Help.createMinimalParamLabelRenderer();
            String desc = "";
            if (field.isAnnotationPresent(Option.class)) {
                desc = prefix + "option '" + field.getAnnotation(Option.class).names()[0] + "'";
                if (index >= 0) {
                    Range arity = Range.optionArity(field);
                    if (arity.max > 1) {
                        desc = desc + " at index " + index;
                    }
                    desc = desc + " (" + labelRenderer.renderParameterLabel(field, Help.Ansi.OFF, Collections.emptyList()) + ")";
                }
            } else if (field.isAnnotationPresent(Parameters.class)) {
                Range indexRange = Range.parameterIndex(field);
                Help.Ansi.Text label = labelRenderer.renderParameterLabel(field, Help.Ansi.OFF, Collections.emptyList());
                desc = prefix + "positional parameter at index " + indexRange + " (" + label + ")";
            }
            return desc;
        }

        private boolean isAnyHelpRequested() {
            return this.isHelpRequested || CommandLine.this.versionHelpRequested || CommandLine.this.usageHelpRequested;
        }

        private void updateHelpRequested(Field field) {
            if (field.isAnnotationPresent(Option.class)) {
                this.isHelpRequested |= this.is(field, "help", field.getAnnotation(Option.class).help());
                CommandLine commandLine = CommandLine.this;
                commandLine.versionHelpRequested = commandLine.versionHelpRequested | this.is(field, "versionHelp", field.getAnnotation(Option.class).versionHelp());
                commandLine = CommandLine.this;
                commandLine.usageHelpRequested = commandLine.usageHelpRequested | this.is(field, "usageHelp", field.getAnnotation(Option.class).usageHelp());
            }
        }

        private boolean is(Field f, String description, boolean value) {
            if (value && CommandLine.this.tracer.isInfo()) {
                CommandLine.this.tracer.info("Field '%s.%s' has '%s' annotation: not validating required fields%n", f.getDeclaringClass().getSimpleName(), f.getName(), description);
            }
            return value;
        }

        private Collection<Object> createCollection(Class<?> collectionClass) throws Exception {
            if (collectionClass.isInterface()) {
                if (SortedSet.class.isAssignableFrom(collectionClass)) {
                    return new TreeSet<Object>();
                }
                if (Set.class.isAssignableFrom(collectionClass)) {
                    return new LinkedHashSet<Object>();
                }
                if (Queue.class.isAssignableFrom(collectionClass)) {
                    return new LinkedList<Object>();
                }
                return new ArrayList<Object>();
            }
            return (Collection)collectionClass.newInstance();
        }

        private Map<Object, Object> createMap(Class<?> mapClass) throws Exception {
            try {
                return (Map)mapClass.newInstance();
            }
            catch (Exception exception) {
                return new LinkedHashMap<Object, Object>();
            }
        }

        private ITypeConverter<?> getTypeConverter(final Class<?> type, Field field) {
            ITypeConverter<?> result = this.converterRegistry.get(type);
            if (result != null) {
                return result;
            }
            if (type.isEnum()) {
                return new ITypeConverter<Object>(){

                    @Override
                    public Object convert(String value) throws Exception {
                        return Enum.valueOf(type, value);
                    }
                };
            }
            throw new MissingTypeConverterException(CommandLine.this, "No TypeConverter registered for " + type.getName() + " of field " + field);
        }

        private void assertNoMissingParameters(Field field, int arity, Stack<String> args) {
            if (arity > args.size()) {
                if (arity == 1) {
                    if (field.isAnnotationPresent(Option.class)) {
                        throw new MissingParameterException(CommandLine.this, "Missing required parameter for " + this.optionDescription("", field, 0));
                    }
                    Range indexRange = Range.parameterIndex(field);
                    Help.IParamLabelRenderer labelRenderer = Help.createMinimalParamLabelRenderer();
                    String sep = "";
                    String names = "";
                    int count = 0;
                    for (int i = indexRange.min; i < this.positionalParametersFields.size(); ++i) {
                        if (Range.parameterArity((Field)this.positionalParametersFields.get((int)i)).min <= 0) continue;
                        names = names + sep + labelRenderer.renderParameterLabel(this.positionalParametersFields.get(i), Help.Ansi.OFF, Collections.emptyList());
                        sep = ", ";
                        ++count;
                    }
                    String msg = "Missing required parameter";
                    Range paramArity = Range.parameterArity(field);
                    msg = paramArity.isVariable ? msg + "s at positions " + indexRange + ": " : msg + (count > 1 ? "s: " : ": ");
                    throw new MissingParameterException(CommandLine.this, msg + names);
                }
                if (args.isEmpty()) {
                    throw new MissingParameterException(CommandLine.this, this.optionDescription("", field, 0) + " requires at least " + arity + " values, but none were specified.");
                }
                throw new MissingParameterException(CommandLine.this, this.optionDescription("", field, 0) + " requires at least " + arity + " values, but only " + args.size() + " were specified: " + CommandLine.reverse(args));
            }
        }

        private String trim(String value) {
            return this.unquote(value);
        }

        private String unquote(String value) {
            return value == null ? null : (value.length() > 1 && value.startsWith("\"") && value.endsWith("\"") ? value.substring(1, value.length() - 1) : value);
        }
    }

    public static class Range
    implements Comparable<Range> {
        public final int min;
        public final int max;
        public final boolean isVariable;
        private final boolean isUnspecified;
        private final String originalValue;

        public Range(int min, int max, boolean variable, boolean unspecified, String originalValue) {
            this.min = min;
            this.max = max;
            this.isVariable = variable;
            this.isUnspecified = unspecified;
            this.originalValue = originalValue;
        }

        public static Range optionArity(Field field) {
            return field.isAnnotationPresent(Option.class) ? Range.adjustForType(Range.valueOf(field.getAnnotation(Option.class).arity()), field) : new Range(0, 0, false, true, "0");
        }

        public static Range parameterArity(Field field) {
            return field.isAnnotationPresent(Parameters.class) ? Range.adjustForType(Range.valueOf(field.getAnnotation(Parameters.class).arity()), field) : new Range(0, 0, false, true, "0");
        }

        public static Range parameterIndex(Field field) {
            return field.isAnnotationPresent(Parameters.class) ? Range.valueOf(field.getAnnotation(Parameters.class).index()) : new Range(0, 0, false, true, "0");
        }

        static Range adjustForType(Range result, Field field) {
            return result.isUnspecified ? Range.defaultArity(field) : result;
        }

        public static Range defaultArity(Field field) {
            Class<?> type = field.getType();
            if (field.isAnnotationPresent(Option.class)) {
                return Range.defaultArity(type);
            }
            if (CommandLine.isMultiValue(type)) {
                return Range.valueOf("0..1");
            }
            return Range.valueOf("1");
        }

        public static Range defaultArity(Class<?> type) {
            return CommandLine.isBoolean(type) ? Range.valueOf("0") : Range.valueOf("1");
        }

        private int size() {
            return 1 + this.max - this.min;
        }

        static Range parameterCapacity(Field field) {
            Range arity = Range.parameterArity(field);
            if (!CommandLine.isMultiValue(field)) {
                return arity;
            }
            Range index = Range.parameterIndex(field);
            if (arity.max == 0) {
                return arity;
            }
            if (index.size() == 1) {
                return arity;
            }
            if (index.isVariable) {
                return Range.valueOf(arity.min + "..*");
            }
            if (arity.size() == 1) {
                return Range.valueOf(arity.min * index.size() + "");
            }
            if (arity.isVariable) {
                return Range.valueOf(arity.min * index.size() + "..*");
            }
            return Range.valueOf(arity.min * index.size() + ".." + arity.max * index.size());
        }

        public static Range valueOf(String range) {
            boolean unspecified = (range = range.trim()).length() == 0 || range.startsWith("..");
            int min = -1;
            int max = -1;
            boolean variable = false;
            int dots = -1;
            dots = range.indexOf("..");
            if (dots >= 0) {
                min = Range.parseInt(range.substring(0, dots), 0);
                max = Range.parseInt(range.substring(dots + 2), Integer.MAX_VALUE);
                variable = max == Integer.MAX_VALUE;
            } else {
                max = Range.parseInt(range, Integer.MAX_VALUE);
                variable = max == Integer.MAX_VALUE;
                min = variable ? 0 : max;
            }
            Range result = new Range(min, max, variable, unspecified, range);
            return result;
        }

        private static int parseInt(String str, int defaultValue) {
            try {
                return Integer.parseInt(str);
            }
            catch (Exception ex) {
                return defaultValue;
            }
        }

        public Range min(int newMin) {
            return new Range(newMin, Math.max(newMin, this.max), this.isVariable, this.isUnspecified, this.originalValue);
        }

        public Range max(int newMax) {
            return new Range(Math.min(this.min, newMax), newMax, this.isVariable, this.isUnspecified, this.originalValue);
        }

        public boolean contains(int value) {
            return this.min <= value && this.max >= value;
        }

        public boolean equals(Object object) {
            if (!(object instanceof Range)) {
                return false;
            }
            Range other = (Range)object;
            return other.max == this.max && other.min == this.min && other.isVariable == this.isVariable;
        }

        public int hashCode() {
            return ((629 + this.max) * 37 + this.min) * 37 + (this.isVariable ? 1 : 0);
        }

        public String toString() {
            return this.min == this.max ? String.valueOf(this.min) : this.min + ".." + (this.isVariable ? "*" : Integer.valueOf(this.max));
        }

        @Override
        public int compareTo(Range other) {
            int result = this.min - other.min;
            return result == 0 ? this.max - other.max : result;
        }
    }

    public static interface ITypeConverter<K> {
        public K convert(String var1) throws Exception;
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.TYPE, ElementType.LOCAL_VARIABLE, ElementType.PACKAGE})
    public static @interface Command {
        public String name() default "<main class>";

        public Class<?>[] subcommands() default {};

        public String separator() default "=";

        public String[] version() default {};

        public String headerHeading() default "";

        public String[] header() default {};

        public String synopsisHeading() default "Usage: ";

        public boolean abbreviateSynopsis() default false;

        public String[] customSynopsis() default {};

        public String descriptionHeading() default "";

        public String[] description() default {};

        public String parameterListHeading() default "";

        public String optionListHeading() default "";

        public boolean sortOptions() default true;

        public char requiredOptionMarker() default 32;

        public boolean showDefaultValues() default false;

        public String commandListHeading() default "Commands:%n";

        public String footerHeading() default "";

        public String[] footer() default {};
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.FIELD})
    public static @interface Parameters {
        public String index() default "*";

        public String[] description() default {};

        public String arity() default "";

        public String paramLabel() default "";

        public Class<?>[] type() default {};

        public String split() default "";

        public boolean hidden() default false;
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.FIELD})
    public static @interface Option {
        public String[] names();

        public boolean required() default false;

        public boolean help() default false;

        public boolean usageHelp() default false;

        public boolean versionHelp() default false;

        public String[] description() default {};

        public String arity() default "";

        public String paramLabel() default "";

        public Class<?>[] type() default {};

        public String split() default "";

        public boolean hidden() default false;
    }

    public static class RunAll
    implements IParseResultHandler {
        @Override
        public List<Object> handleParseResult(List<CommandLine> parsedCommands, PrintStream out, Help.Ansi ansi) {
            if (CommandLine.printHelpIfRequested(parsedCommands, out, ansi)) {
                return null;
            }
            ArrayList<Object> result = new ArrayList<Object>();
            for (CommandLine parsed : parsedCommands) {
                result.add(CommandLine.execute(parsed));
            }
            return result;
        }
    }

    public static class RunLast
    implements IParseResultHandler {
        @Override
        public List<Object> handleParseResult(List<CommandLine> parsedCommands, PrintStream out, Help.Ansi ansi) {
            if (CommandLine.printHelpIfRequested(parsedCommands, out, ansi)) {
                return Collections.emptyList();
            }
            CommandLine last = parsedCommands.get(parsedCommands.size() - 1);
            return Arrays.asList(CommandLine.execute(last));
        }
    }

    public static class RunFirst
    implements IParseResultHandler {
        @Override
        public List<Object> handleParseResult(List<CommandLine> parsedCommands, PrintStream out, Help.Ansi ansi) {
            if (CommandLine.printHelpIfRequested(parsedCommands, out, ansi)) {
                return Collections.emptyList();
            }
            return Arrays.asList(CommandLine.execute(parsedCommands.get(0)));
        }
    }

    public static class DefaultExceptionHandler
    implements IExceptionHandler {
        @Override
        public List<Object> handleException(ParameterException ex, PrintStream out, Help.Ansi ansi, String ... args) {
            out.println(ex.getMessage());
            ex.getCommandLine().usage(out, ansi);
            return Collections.emptyList();
        }
    }

    public static interface IExceptionHandler {
        public List<Object> handleException(ParameterException var1, PrintStream var2, Help.Ansi var3, String ... var4);
    }

    public static interface IParseResultHandler {
        public List<Object> handleParseResult(List<CommandLine> var1, PrintStream var2, Help.Ansi var3) throws ExecutionException;
    }
}

