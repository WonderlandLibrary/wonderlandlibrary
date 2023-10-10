// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.tools.picocli;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.util.Locale;
import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.SortedSet;
import java.lang.reflect.Array;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.UUID;
import java.util.regex.Pattern;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.net.URL;
import java.net.URI;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Collections;
import java.util.Stack;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.lang.reflect.WildcardType;
import java.lang.reflect.ParameterizedType;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class CommandLine
{
    public static final String VERSION = "2.0.3";
    private final Tracer tracer;
    private final Interpreter interpreter;
    private String commandName;
    private boolean overwrittenOptionsAllowed;
    private boolean unmatchedArgumentsAllowed;
    private final List<String> unmatchedArguments;
    private CommandLine parent;
    private boolean usageHelpRequested;
    private boolean versionHelpRequested;
    private final List<String> versionLines;
    
    public CommandLine(final Object command) {
        this.tracer = new Tracer();
        this.commandName = "<main class>";
        this.overwrittenOptionsAllowed = false;
        this.unmatchedArgumentsAllowed = false;
        this.unmatchedArguments = new ArrayList<String>();
        this.versionLines = new ArrayList<String>();
        this.interpreter = new Interpreter(command);
    }
    
    public CommandLine addSubcommand(final String name, final Object command) {
        final CommandLine commandLine = toCommandLine(command);
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
    
    public CommandLine setOverwrittenOptionsAllowed(final boolean newValue) {
        this.overwrittenOptionsAllowed = newValue;
        for (final CommandLine command : this.interpreter.commands.values()) {
            command.setOverwrittenOptionsAllowed(newValue);
        }
        return this;
    }
    
    public boolean isUnmatchedArgumentsAllowed() {
        return this.unmatchedArgumentsAllowed;
    }
    
    public CommandLine setUnmatchedArgumentsAllowed(final boolean newValue) {
        this.unmatchedArgumentsAllowed = newValue;
        for (final CommandLine command : this.interpreter.commands.values()) {
            command.setUnmatchedArgumentsAllowed(newValue);
        }
        return this;
    }
    
    public List<String> getUnmatchedArguments() {
        return this.unmatchedArguments;
    }
    
    public static <T> T populateCommand(final T command, final String... args) {
        final CommandLine cli = toCommandLine(command);
        cli.parse(args);
        return command;
    }
    
    public List<CommandLine> parse(final String... args) {
        return this.interpreter.parse(args);
    }
    
    public static boolean printHelpIfRequested(final List<CommandLine> parsedCommands, final PrintStream out, final Help.Ansi ansi) {
        for (final CommandLine parsed : parsedCommands) {
            if (parsed.isUsageHelpRequested()) {
                parsed.usage(out, ansi);
                return true;
            }
            if (parsed.isVersionHelpRequested()) {
                parsed.printVersionHelp(out, ansi);
                return true;
            }
        }
        return false;
    }
    
    private static Object execute(final CommandLine parsed) {
        final Object command = parsed.getCommand();
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
    
    public List<Object> parseWithHandler(final IParseResultHandler handler, final PrintStream out, final String... args) {
        return this.parseWithHandlers(handler, out, Help.Ansi.AUTO, new DefaultExceptionHandler(), args);
    }
    
    public List<Object> parseWithHandlers(final IParseResultHandler handler, final PrintStream out, final Help.Ansi ansi, final IExceptionHandler exceptionHandler, final String... args) {
        try {
            final List<CommandLine> result = this.parse(args);
            return handler.handleParseResult(result, out, ansi);
        }
        catch (ParameterException ex) {
            return exceptionHandler.handleException(ex, out, ansi, args);
        }
    }
    
    public static void usage(final Object command, final PrintStream out) {
        toCommandLine(command).usage(out);
    }
    
    public static void usage(final Object command, final PrintStream out, final Help.Ansi ansi) {
        toCommandLine(command).usage(out, ansi);
    }
    
    public static void usage(final Object command, final PrintStream out, final Help.ColorScheme colorScheme) {
        toCommandLine(command).usage(out, colorScheme);
    }
    
    public void usage(final PrintStream out) {
        this.usage(out, Help.Ansi.AUTO);
    }
    
    public void usage(final PrintStream out, final Help.Ansi ansi) {
        this.usage(out, Help.defaultColorScheme(ansi));
    }
    
    public void usage(final PrintStream out, final Help.ColorScheme colorScheme) {
        final Help help = new Help(this.interpreter.command, colorScheme).addAllSubcommands(this.getSubcommands());
        if (!"=".equals(this.getSeparator())) {
            help.separator = this.getSeparator();
            help.parameterLabelRenderer = help.createDefaultParamLabelRenderer();
        }
        if (!"<main class>".equals(this.getCommandName())) {
            help.commandName = this.getCommandName();
        }
        final StringBuilder sb = new StringBuilder().append(help.headerHeading(new Object[0])).append(help.header(new Object[0])).append(help.synopsisHeading(new Object[0])).append(help.synopsis(help.synopsisHeadingLength())).append(help.descriptionHeading(new Object[0])).append(help.description(new Object[0])).append(help.parameterListHeading(new Object[0])).append(help.parameterList()).append(help.optionListHeading(new Object[0])).append(help.optionList()).append(help.commandListHeading(new Object[0])).append(help.commandList()).append(help.footerHeading(new Object[0])).append(help.footer(new Object[0]));
        out.print(sb);
    }
    
    public void printVersionHelp(final PrintStream out) {
        this.printVersionHelp(out, Help.Ansi.AUTO);
    }
    
    public void printVersionHelp(final PrintStream out, final Help.Ansi ansi) {
        for (final String versionInfo : this.versionLines) {
            out.println(ansi.new Text(versionInfo));
        }
    }
    
    public void printVersionHelp(final PrintStream out, final Help.Ansi ansi, final Object... params) {
        for (final String versionInfo : this.versionLines) {
            out.println(ansi.new Text(String.format(versionInfo, params)));
        }
    }
    
    public static <C extends Callable<T>, T> T call(final C callable, final PrintStream out, final String... args) {
        return call(callable, out, Help.Ansi.AUTO, args);
    }
    
    public static <C extends Callable<T>, T> T call(final C callable, final PrintStream out, final Help.Ansi ansi, final String... args) {
        final CommandLine cmd = new CommandLine(callable);
        final List<Object> results = cmd.parseWithHandlers(new RunLast(), out, ansi, new DefaultExceptionHandler(), args);
        return (T)((results == null || results.isEmpty()) ? null : results.get(0));
    }
    
    public static <R extends Runnable> void run(final R runnable, final PrintStream out, final String... args) {
        run(runnable, out, Help.Ansi.AUTO, args);
    }
    
    public static <R extends Runnable> void run(final R runnable, final PrintStream out, final Help.Ansi ansi, final String... args) {
        final CommandLine cmd = new CommandLine(runnable);
        cmd.parseWithHandlers(new RunLast(), out, ansi, new DefaultExceptionHandler(), args);
    }
    
    public <K> CommandLine registerConverter(final Class<K> cls, final ITypeConverter<K> converter) {
        this.interpreter.converterRegistry.put(Assert.notNull(cls, "class"), Assert.notNull(converter, "converter"));
        for (final CommandLine command : this.interpreter.commands.values()) {
            command.registerConverter((Class<Object>)cls, (ITypeConverter<Object>)converter);
        }
        return this;
    }
    
    public String getSeparator() {
        return this.interpreter.separator;
    }
    
    public CommandLine setSeparator(final String separator) {
        this.interpreter.separator = Assert.notNull(separator, "separator");
        return this;
    }
    
    public String getCommandName() {
        return this.commandName;
    }
    
    public CommandLine setCommandName(final String commandName) {
        this.commandName = Assert.notNull(commandName, "commandName");
        return this;
    }
    
    private static boolean empty(final String str) {
        return str == null || str.trim().length() == 0;
    }
    
    private static boolean empty(final Object[] array) {
        return array == null || array.length == 0;
    }
    
    private static boolean empty(final Help.Ansi.Text txt) {
        return txt == null || txt.plain.toString().trim().length() == 0;
    }
    
    private static String str(final String[] arr, final int i) {
        return (arr == null || arr.length == 0) ? "" : arr[i];
    }
    
    private static boolean isBoolean(final Class<?> type) {
        return type == Boolean.class || type == Boolean.TYPE;
    }
    
    private static CommandLine toCommandLine(final Object obj) {
        return (CommandLine)((obj instanceof CommandLine) ? obj : new CommandLine(obj));
    }
    
    private static boolean isMultiValue(final Field field) {
        return isMultiValue(field.getType());
    }
    
    private static boolean isMultiValue(final Class<?> cls) {
        return cls.isArray() || Collection.class.isAssignableFrom(cls) || Map.class.isAssignableFrom(cls);
    }
    
    private static Class<?>[] getTypeAttribute(final Field field) {
        final Class<?>[] explicit = field.isAnnotationPresent(Parameters.class) ? field.getAnnotation(Parameters.class).type() : field.getAnnotation(Option.class).type();
        if (explicit.length > 0) {
            return explicit;
        }
        if (field.getType().isArray()) {
            return (Class<?>[])new Class[] { field.getType().getComponentType() };
        }
        if (!isMultiValue(field)) {
            return (Class<?>[])new Class[] { field.getType() };
        }
        final Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)type;
            final Type[] paramTypes = parameterizedType.getActualTypeArguments();
            final Class<?>[] result = (Class<?>[])new Class[paramTypes.length];
            for (int i = 0; i < paramTypes.length; ++i) {
                if (!(paramTypes[i] instanceof Class)) {
                    if (paramTypes[i] instanceof WildcardType) {
                        final WildcardType wildcardType = (WildcardType)paramTypes[i];
                        final Type[] lower = wildcardType.getLowerBounds();
                        if (lower.length > 0 && lower[0] instanceof Class) {
                            result[i] = (Class<?>)lower[0];
                            continue;
                        }
                        final Type[] upper = wildcardType.getUpperBounds();
                        if (upper.length > 0 && upper[0] instanceof Class) {
                            result[i] = (Class<?>)upper[0];
                            continue;
                        }
                    }
                    Arrays.fill(result, String.class);
                    return result;
                }
                result[i] = (Class<?>)paramTypes[i];
            }
            return result;
        }
        return (Class<?>[])new Class[] { String.class, String.class };
    }
    
    static void init(final Class<?> cls, final List<Field> requiredFields, final Map<String, Field> optionName2Field, final Map<Character, Field> singleCharOption2Field, final List<Field> positionalParametersFields) {
        final Field[] declaredFields2;
        final Field[] declaredFields = declaredFields2 = cls.getDeclaredFields();
        for (final Field field : declaredFields2) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Option.class)) {
                final Option option = field.getAnnotation(Option.class);
                if (option.required()) {
                    requiredFields.add(field);
                }
                for (final String name : option.names()) {
                    final Field existing = optionName2Field.put(name, field);
                    if (existing != null && existing != field) {
                        throw create(name, field, existing);
                    }
                    if (name.length() == 2 && name.startsWith("-")) {
                        final char flag = name.charAt(1);
                        final Field existing2 = singleCharOption2Field.put(flag, field);
                        if (existing2 != null && existing2 != field) {
                            throw create(name, field, existing2);
                        }
                    }
                }
            }
            if (field.isAnnotationPresent(Parameters.class)) {
                if (field.isAnnotationPresent(Option.class)) {
                    throw new DuplicateOptionAnnotationsException("A field can be either @Option or @Parameters, but '" + field.getName() + "' is both.");
                }
                positionalParametersFields.add(field);
                final Range arity = Range.parameterArity(field);
                if (arity.min > 0) {
                    requiredFields.add(field);
                }
            }
        }
    }
    
    static void validatePositionalParameters(final List<Field> positionalParametersFields) {
        int min = 0;
        for (final Field field : positionalParametersFields) {
            final Range index = Range.parameterIndex(field);
            if (index.min > min) {
                throw new ParameterIndexGapException("Missing field annotated with @Parameter(index=" + min + "). Nearest field '" + field.getName() + "' has index=" + index.min);
            }
            min = Math.max(min, index.max);
            min = ((min == Integer.MAX_VALUE) ? min : (min + 1));
        }
    }
    
    private static <T> Stack<T> reverse(final Stack<T> stack) {
        Collections.reverse(stack);
        return stack;
    }
    
    public static class DefaultExceptionHandler implements IExceptionHandler
    {
        @Override
        public List<Object> handleException(final ParameterException ex, final PrintStream out, final Help.Ansi ansi, final String... args) {
            out.println(ex.getMessage());
            ex.getCommandLine().usage(out, ansi);
            return Collections.emptyList();
        }
    }
    
    public static class RunFirst implements IParseResultHandler
    {
        @Override
        public List<Object> handleParseResult(final List<CommandLine> parsedCommands, final PrintStream out, final Help.Ansi ansi) {
            if (CommandLine.printHelpIfRequested(parsedCommands, out, ansi)) {
                return Collections.emptyList();
            }
            return Arrays.asList(execute(parsedCommands.get(0)));
        }
    }
    
    public static class RunLast implements IParseResultHandler
    {
        @Override
        public List<Object> handleParseResult(final List<CommandLine> parsedCommands, final PrintStream out, final Help.Ansi ansi) {
            if (CommandLine.printHelpIfRequested(parsedCommands, out, ansi)) {
                return Collections.emptyList();
            }
            final CommandLine last = parsedCommands.get(parsedCommands.size() - 1);
            return Arrays.asList(execute(last));
        }
    }
    
    public static class RunAll implements IParseResultHandler
    {
        @Override
        public List<Object> handleParseResult(final List<CommandLine> parsedCommands, final PrintStream out, final Help.Ansi ansi) {
            if (CommandLine.printHelpIfRequested(parsedCommands, out, ansi)) {
                return null;
            }
            final List<Object> result = new ArrayList<Object>();
            for (final CommandLine parsed : parsedCommands) {
                result.add(execute(parsed));
            }
            return result;
        }
    }
    
    public static class Range implements Comparable<Range>
    {
        public final int min;
        public final int max;
        public final boolean isVariable;
        private final boolean isUnspecified;
        private final String originalValue;
        
        public Range(final int min, final int max, final boolean variable, final boolean unspecified, final String originalValue) {
            this.min = min;
            this.max = max;
            this.isVariable = variable;
            this.isUnspecified = unspecified;
            this.originalValue = originalValue;
        }
        
        public static Range optionArity(final Field field) {
            return field.isAnnotationPresent(Option.class) ? adjustForType(valueOf(field.getAnnotation(Option.class).arity()), field) : new Range(0, 0, false, true, "0");
        }
        
        public static Range parameterArity(final Field field) {
            return field.isAnnotationPresent(Parameters.class) ? adjustForType(valueOf(field.getAnnotation(Parameters.class).arity()), field) : new Range(0, 0, false, true, "0");
        }
        
        public static Range parameterIndex(final Field field) {
            return field.isAnnotationPresent(Parameters.class) ? valueOf(field.getAnnotation(Parameters.class).index()) : new Range(0, 0, false, true, "0");
        }
        
        static Range adjustForType(final Range result, final Field field) {
            return result.isUnspecified ? defaultArity(field) : result;
        }
        
        public static Range defaultArity(final Field field) {
            final Class<?> type = field.getType();
            if (field.isAnnotationPresent(Option.class)) {
                return defaultArity(type);
            }
            if (isMultiValue(type)) {
                return valueOf("0..1");
            }
            return valueOf("1");
        }
        
        public static Range defaultArity(final Class<?> type) {
            return isBoolean(type) ? valueOf("0") : valueOf("1");
        }
        
        private int size() {
            return 1 + this.max - this.min;
        }
        
        static Range parameterCapacity(final Field field) {
            final Range arity = parameterArity(field);
            if (!isMultiValue(field)) {
                return arity;
            }
            final Range index = parameterIndex(field);
            if (arity.max == 0) {
                return arity;
            }
            if (index.size() == 1) {
                return arity;
            }
            if (index.isVariable) {
                return valueOf(arity.min + "..*");
            }
            if (arity.size() == 1) {
                return valueOf(arity.min * index.size() + "");
            }
            if (arity.isVariable) {
                return valueOf(arity.min * index.size() + "..*");
            }
            return valueOf(arity.min * index.size() + ".." + arity.max * index.size());
        }
        
        public static Range valueOf(String range) {
            range = range.trim();
            final boolean unspecified = range.length() == 0 || range.startsWith("..");
            int min = -1;
            int max = -1;
            boolean variable = false;
            int dots = -1;
            if ((dots = range.indexOf("..")) >= 0) {
                min = parseInt(range.substring(0, dots), 0);
                max = parseInt(range.substring(dots + 2), Integer.MAX_VALUE);
                variable = (max == Integer.MAX_VALUE);
            }
            else {
                max = parseInt(range, Integer.MAX_VALUE);
                variable = (max == Integer.MAX_VALUE);
                min = (variable ? 0 : max);
            }
            final Range result = new Range(min, max, variable, unspecified, range);
            return result;
        }
        
        private static int parseInt(final String str, final int defaultValue) {
            try {
                return Integer.parseInt(str);
            }
            catch (Exception ex) {
                return defaultValue;
            }
        }
        
        public Range min(final int newMin) {
            return new Range(newMin, Math.max(newMin, this.max), this.isVariable, this.isUnspecified, this.originalValue);
        }
        
        public Range max(final int newMax) {
            return new Range(Math.min(this.min, newMax), newMax, this.isVariable, this.isUnspecified, this.originalValue);
        }
        
        public boolean contains(final int value) {
            return this.min <= value && this.max >= value;
        }
        
        @Override
        public boolean equals(final Object object) {
            if (!(object instanceof Range)) {
                return false;
            }
            final Range other = (Range)object;
            return other.max == this.max && other.min == this.min && other.isVariable == this.isVariable;
        }
        
        @Override
        public int hashCode() {
            return ((629 + this.max) * 37 + this.min) * 37 + (this.isVariable ? 1 : 0);
        }
        
        @Override
        public String toString() {
            return (this.min == this.max) ? String.valueOf(this.min) : (this.min + ".." + (this.isVariable ? "*" : Integer.valueOf(this.max)));
        }
        
        @Override
        public int compareTo(final Range other) {
            final int result = this.min - other.min;
            return (result == 0) ? (this.max - other.max) : result;
        }
    }
    
    private class Interpreter
    {
        private final Map<String, CommandLine> commands;
        private final Map<Class<?>, ITypeConverter<?>> converterRegistry;
        private final Map<String, Field> optionName2Field;
        private final Map<Character, Field> singleCharOption2Field;
        private final List<Field> requiredFields;
        private final List<Field> positionalParametersFields;
        private final Object command;
        private boolean isHelpRequested;
        private String separator;
        private int position;
        
        Interpreter(final Object command) {
            this.commands = new LinkedHashMap<String, CommandLine>();
            this.converterRegistry = new HashMap<Class<?>, ITypeConverter<?>>();
            this.optionName2Field = new HashMap<String, Field>();
            this.singleCharOption2Field = new HashMap<Character, Field>();
            this.requiredFields = new ArrayList<Field>();
            this.positionalParametersFields = new ArrayList<Field>();
            this.separator = "=";
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
            Class<?> cls = command.getClass();
            String declaredName = null;
            String declaredSeparator = null;
            boolean hasCommandAnnotation = false;
            while (cls != null) {
                CommandLine.init(cls, this.requiredFields, this.optionName2Field, this.singleCharOption2Field, this.positionalParametersFields);
                if (cls.isAnnotationPresent(Command.class)) {
                    hasCommandAnnotation = true;
                    final Command cmd = cls.getAnnotation(Command.class);
                    declaredSeparator = ((declaredSeparator == null) ? cmd.separator() : declaredSeparator);
                    declaredName = ((declaredName == null) ? cmd.name() : declaredName);
                    CommandLine.this.versionLines.addAll(Arrays.asList(cmd.version()));
                    for (final Class<?> sub : cmd.subcommands()) {
                        final Command subCommand = sub.getAnnotation(Command.class);
                        if (subCommand == null || "<main class>".equals(subCommand.name())) {
                            throw new InitializationException("Subcommand " + sub.getName() + " is missing the mandatory @Command annotation with a 'name' attribute");
                        }
                        try {
                            final Constructor<?> constructor = sub.getDeclaredConstructor((Class<?>[])new Class[0]);
                            constructor.setAccessible(true);
                            final CommandLine commandLine = toCommandLine(constructor.newInstance(new Object[0]));
                            commandLine.parent = CommandLine.this;
                            this.commands.put(subCommand.name(), commandLine);
                        }
                        catch (InitializationException ex) {
                            throw ex;
                        }
                        catch (NoSuchMethodException ex2) {
                            throw new InitializationException("Cannot instantiate subcommand " + sub.getName() + ": the class has no constructor", ex2);
                        }
                        catch (Exception ex3) {
                            throw new InitializationException("Could not instantiate and add subcommand " + sub.getName() + ": " + ex3, ex3);
                        }
                    }
                }
                cls = cls.getSuperclass();
            }
            this.separator = ((declaredSeparator != null) ? declaredSeparator : this.separator);
            CommandLine.this.commandName = ((declaredName != null) ? declaredName : CommandLine.this.commandName);
            Collections.sort(this.positionalParametersFields, new PositionalParametersSorter());
            CommandLine.validatePositionalParameters(this.positionalParametersFields);
            if (this.positionalParametersFields.isEmpty() && this.optionName2Field.isEmpty() && !hasCommandAnnotation) {
                throw new InitializationException(command + " (" + command.getClass() + ") is not a command: it has no @Command, @Option or @Parameters annotations");
            }
        }
        
        List<CommandLine> parse(final String... args) {
            Assert.notNull(args, "argument array");
            if (CommandLine.this.tracer.isInfo()) {
                CommandLine.this.tracer.info("Parsing %d command line args %s%n", args.length, Arrays.toString(args));
            }
            final Stack<String> arguments = new Stack<String>();
            for (int i = args.length - 1; i >= 0; --i) {
                arguments.push(args[i]);
            }
            final List<CommandLine> result = new ArrayList<CommandLine>();
            this.parse(result, arguments, args);
            return result;
        }
        
        private void parse(final List<CommandLine> parsedCommands, final Stack<String> argumentStack, final String[] originalArgs) {
            this.isHelpRequested = false;
            CommandLine.this.versionHelpRequested = false;
            CommandLine.this.usageHelpRequested = false;
            final Class<?> cmdClass = this.command.getClass();
            if (CommandLine.this.tracer.isDebug()) {
                CommandLine.this.tracer.debug("Initializing %s: %d options, %d positional parameters, %d required, %d subcommands.%n", cmdClass.getName(), new HashSet(this.optionName2Field.values()).size(), this.positionalParametersFields.size(), this.requiredFields.size(), this.commands.size());
            }
            parsedCommands.add(CommandLine.this);
            final List<Field> required = new ArrayList<Field>(this.requiredFields);
            final Set<Field> initialized = new HashSet<Field>();
            Collections.sort(required, new PositionalParametersSorter());
            try {
                this.processArguments(parsedCommands, argumentStack, required, initialized, originalArgs);
            }
            catch (ParameterException ex) {
                throw ex;
            }
            catch (Exception ex2) {
                final int offendingArgIndex = originalArgs.length - argumentStack.size() - 1;
                final String arg = (offendingArgIndex >= 0 && offendingArgIndex < originalArgs.length) ? originalArgs[offendingArgIndex] : "?";
                throw create(CommandLine.this, ex2, arg, offendingArgIndex, originalArgs);
            }
            if (!this.isAnyHelpRequested() && !required.isEmpty()) {
                for (final Field missing : required) {
                    if (missing.isAnnotationPresent(Option.class)) {
                        throw create(CommandLine.this, required, this.separator);
                    }
                    this.assertNoMissingParameters(missing, Range.parameterArity(missing).min, argumentStack);
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
        
        private void processArguments(final List<CommandLine> parsedCommands, final Stack<String> args, final Collection<Field> required, final Set<Field> initialized, final String[] originalArgs) throws Exception {
            while (!args.isEmpty()) {
                String arg = args.pop();
                if (CommandLine.this.tracer.isDebug()) {
                    CommandLine.this.tracer.debug("Processing argument '%s'. Remainder=%s%n", arg, reverse((Stack<Object>)args.clone()));
                }
                if ("--".equals(arg)) {
                    CommandLine.this.tracer.info("Found end-of-options delimiter '--'. Treating remainder as positional parameters.%n", new Object[0]);
                    this.processRemainderAsPositionalParameters(required, initialized, args);
                    return;
                }
                if (this.commands.containsKey(arg)) {
                    if (!this.isHelpRequested && !required.isEmpty()) {
                        throw create(CommandLine.this, required, this.separator);
                    }
                    if (CommandLine.this.tracer.isDebug()) {
                        CommandLine.this.tracer.debug("Found subcommand '%s' (%s)%n", arg, this.commands.get(arg).interpreter.command.getClass().getName());
                    }
                    this.commands.get(arg).interpreter.parse(parsedCommands, args, originalArgs);
                }
                else {
                    boolean paramAttachedToOption = false;
                    final int separatorIndex = arg.indexOf(this.separator);
                    if (separatorIndex > 0) {
                        final String key = arg.substring(0, separatorIndex);
                        if (this.optionName2Field.containsKey(key) && !this.optionName2Field.containsKey(arg)) {
                            paramAttachedToOption = true;
                            final String optionParam = arg.substring(separatorIndex + this.separator.length());
                            args.push(optionParam);
                            arg = key;
                            if (CommandLine.this.tracer.isDebug()) {
                                CommandLine.this.tracer.debug("Separated '%s' option from '%s' option parameter%n", key, optionParam);
                            }
                        }
                        else if (CommandLine.this.tracer.isDebug()) {
                            CommandLine.this.tracer.debug("'%s' contains separator '%s' but '%s' is not a known option%n", arg, this.separator, key);
                        }
                    }
                    else if (CommandLine.this.tracer.isDebug()) {
                        CommandLine.this.tracer.debug("'%s' cannot be separated into <option>%s<option-parameter>%n", arg, this.separator);
                    }
                    if (this.optionName2Field.containsKey(arg)) {
                        this.processStandaloneOption(required, initialized, arg, args, paramAttachedToOption);
                    }
                    else if (arg.length() > 2 && arg.startsWith("-")) {
                        if (CommandLine.this.tracer.isDebug()) {
                            CommandLine.this.tracer.debug("Trying to process '%s' as clustered short options%n", arg, args);
                        }
                        this.processClusteredShortOptions(required, initialized, arg, args);
                    }
                    else {
                        args.push(arg);
                        if (CommandLine.this.tracer.isDebug()) {
                            CommandLine.this.tracer.debug("Could not find option '%s', deciding whether to treat as unmatched option or positional parameter...%n", arg);
                        }
                        if (this.resemblesOption(arg)) {
                            this.handleUnmatchedArguments(args.pop());
                        }
                        else {
                            if (CommandLine.this.tracer.isDebug()) {
                                CommandLine.this.tracer.debug("No option named '%s' found. Processing remainder as positional parameters%n", arg);
                            }
                            this.processPositionalParameter(required, initialized, args);
                        }
                    }
                }
            }
        }
        
        private boolean resemblesOption(final String arg) {
            int count = 0;
            for (final String optionName : this.optionName2Field.keySet()) {
                for (int i = 0; i < arg.length() && optionName.length() > i && arg.charAt(i) == optionName.charAt(i); ++i) {
                    ++count;
                }
            }
            final boolean result = count > 0 && count * 10 >= this.optionName2Field.size() * 9;
            if (CommandLine.this.tracer.isDebug()) {
                CommandLine.this.tracer.debug("%s %s an option: %d matching prefix chars out of %d option names%n", arg, result ? "resembles" : "doesn't resemble", count, this.optionName2Field.size());
            }
            return result;
        }
        
        private void handleUnmatchedArguments(final String arg) {
            final Stack<String> args = new Stack<String>();
            args.add(arg);
            this.handleUnmatchedArguments(args);
        }
        
        private void handleUnmatchedArguments(final Stack<String> args) {
            while (!args.isEmpty()) {
                CommandLine.this.unmatchedArguments.add(args.pop());
            }
        }
        
        private void processRemainderAsPositionalParameters(final Collection<Field> required, final Set<Field> initialized, final Stack<String> args) throws Exception {
            while (!args.empty()) {
                this.processPositionalParameter(required, initialized, args);
            }
        }
        
        private void processPositionalParameter(final Collection<Field> required, final Set<Field> initialized, final Stack<String> args) throws Exception {
            if (CommandLine.this.tracer.isDebug()) {
                CommandLine.this.tracer.debug("Processing next arg as a positional parameter at index=%d. Remainder=%s%n", this.position, reverse((Stack<Object>)args.clone()));
            }
            int consumed = 0;
            for (final Field positionalParam : this.positionalParametersFields) {
                final Range indexRange = Range.parameterIndex(positionalParam);
                if (!indexRange.contains(this.position)) {
                    continue;
                }
                final Stack<String> argsCopy = (Stack<String>)args.clone();
                final Range arity = Range.parameterArity(positionalParam);
                if (CommandLine.this.tracer.isDebug()) {
                    CommandLine.this.tracer.debug("Position %d is in index range %s. Trying to assign args to %s, arity=%s%n", this.position, indexRange, positionalParam, arity);
                }
                this.assertNoMissingParameters(positionalParam, arity.min, argsCopy);
                final int originalSize = argsCopy.size();
                this.applyOption(positionalParam, Parameters.class, arity, false, argsCopy, initialized, "args[" + indexRange + "] at position " + this.position);
                final int count = originalSize - argsCopy.size();
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
        
        private void processStandaloneOption(final Collection<Field> required, final Set<Field> initialized, final String arg, final Stack<String> args, final boolean paramAttachedToKey) throws Exception {
            final Field field = this.optionName2Field.get(arg);
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
        
        private void processClusteredShortOptions(final Collection<Field> required, final Set<Field> initialized, final String arg, final Stack<String> args) throws Exception {
            final String prefix = arg.substring(0, 1);
            String cluster = arg.substring(1);
            boolean paramAttachedToOption = true;
            while (cluster.length() > 0 && this.singleCharOption2Field.containsKey(cluster.charAt(0))) {
                final Field field = this.singleCharOption2Field.get(cluster.charAt(0));
                Range arity = Range.optionArity(field);
                final String argDescription = "option " + prefix + cluster.charAt(0);
                if (CommandLine.this.tracer.isDebug()) {
                    CommandLine.this.tracer.debug("Found option '%s%s' in %s: field %s, arity=%s%n", prefix, cluster.charAt(0), arg, field, arity);
                }
                required.remove(field);
                cluster = ((cluster.length() > 0) ? cluster.substring(1) : "");
                paramAttachedToOption = (cluster.length() > 0);
                if (cluster.startsWith(this.separator)) {
                    cluster = cluster.substring(this.separator.length());
                    arity = arity.min(Math.max(1, arity.min));
                }
                if (arity.min > 0 && !empty(cluster) && CommandLine.this.tracer.isDebug()) {
                    CommandLine.this.tracer.debug("Trying to process '%s' as option parameter%n", cluster);
                }
                if (!empty(cluster)) {
                    args.push(cluster);
                }
                final int consumed = this.applyOption(field, Option.class, arity, paramAttachedToOption, args, initialized, argDescription);
                if (empty(cluster) || consumed > 0 || args.isEmpty()) {
                    return;
                }
                cluster = args.pop();
            }
            if (cluster.length() == 0) {
                return;
            }
            if (arg.endsWith(cluster)) {
                args.push(paramAttachedToOption ? (prefix + cluster) : cluster);
                if (args.peek().equals(arg)) {
                    if (CommandLine.this.tracer.isDebug()) {
                        CommandLine.this.tracer.debug("Could not match any short options in %s, deciding whether to treat as unmatched option or positional parameter...%n", arg);
                    }
                    if (this.resemblesOption(arg)) {
                        this.handleUnmatchedArguments(args.pop());
                        return;
                    }
                    this.processPositionalParameter(required, initialized, args);
                }
                else {
                    if (CommandLine.this.tracer.isDebug()) {
                        CommandLine.this.tracer.debug("No option found for %s in %s%n", cluster, arg);
                    }
                    this.handleUnmatchedArguments(args.pop());
                }
            }
            else {
                args.push(cluster);
                if (CommandLine.this.tracer.isDebug()) {
                    CommandLine.this.tracer.debug("%s is not an option parameter for %s%n", cluster, arg);
                }
                this.processPositionalParameter(required, initialized, args);
            }
        }
        
        private int applyOption(final Field field, final Class<?> annotation, final Range arity, final boolean valueAttachedToOption, final Stack<String> args, final Set<Field> initialized, final String argDescription) throws Exception {
            this.updateHelpRequested(field);
            final int length = args.size();
            this.assertNoMissingParameters(field, arity.min, args);
            Class<?> cls = field.getType();
            if (cls.isArray()) {
                return this.applyValuesToArrayField(field, annotation, arity, args, cls, argDescription);
            }
            if (Collection.class.isAssignableFrom(cls)) {
                return this.applyValuesToCollectionField(field, annotation, arity, args, cls, argDescription);
            }
            if (Map.class.isAssignableFrom(cls)) {
                return this.applyValuesToMapField(field, annotation, arity, args, cls, argDescription);
            }
            cls = getTypeAttribute(field)[0];
            return this.applyValueToSingleValuedField(field, arity, args, cls, initialized, argDescription);
        }
        
        private int applyValueToSingleValuedField(final Field field, final Range arity, final Stack<String> args, final Class<?> cls, final Set<Field> initialized, final String argDescription) throws Exception {
            final boolean noMoreValues = args.isEmpty();
            String value = args.isEmpty() ? null : this.trim(args.pop());
            int result = arity.min;
            if ((cls == Boolean.class || cls == Boolean.TYPE) && arity.min <= 0) {
                if (arity.max > 0 && ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value))) {
                    result = 1;
                }
                else {
                    if (value != null) {
                        args.push(value);
                    }
                    final Boolean currentValue = (Boolean)field.get(this.command);
                    value = String.valueOf(currentValue == null || !currentValue);
                }
            }
            if (noMoreValues && value == null) {
                return 0;
            }
            final ITypeConverter<?> converter = this.getTypeConverter(cls, field);
            final Object newValue = this.tryConvert(field, -1, converter, value, cls);
            final Object oldValue = field.get(this.command);
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
            if (CommandLine.this.tracer.level.isEnabled(level)) {
                level.print(CommandLine.this.tracer, traceMessage, field.getType().getSimpleName(), field.getDeclaringClass().getSimpleName(), field.getName(), String.valueOf(oldValue), String.valueOf(newValue), argDescription);
            }
            field.set(this.command, newValue);
            return result;
        }
        
        private int applyValuesToMapField(final Field field, final Class<?> annotation, final Range arity, final Stack<String> args, final Class<?> cls, final String argDescription) throws Exception {
            final Class<?>[] classes = getTypeAttribute(field);
            if (classes.length < 2) {
                throw new ParameterException(CommandLine.this, "Field " + field + " needs two types (one for the map key, one for the value) but only has " + classes.length + " types configured.");
            }
            final ITypeConverter<?> keyConverter = this.getTypeConverter(classes[0], field);
            final ITypeConverter<?> valueConverter = this.getTypeConverter(classes[1], field);
            Map<Object, Object> result = (Map<Object, Object>)field.get(this.command);
            if (result == null) {
                result = this.createMap(cls);
                field.set(this.command, result);
            }
            final int originalSize = result.size();
            this.consumeMapArguments(field, arity, args, classes, keyConverter, valueConverter, result, argDescription);
            return result.size() - originalSize;
        }
        
        private void consumeMapArguments(final Field field, final Range arity, final Stack<String> args, final Class<?>[] classes, final ITypeConverter<?> keyConverter, final ITypeConverter<?> valueConverter, final Map<Object, Object> result, final String argDescription) throws Exception {
            for (int i = 0; i < arity.min; ++i) {
                this.consumeOneMapArgument(field, arity, args, classes, keyConverter, valueConverter, result, i, argDescription);
            }
            for (int i = arity.min; i < arity.max && !args.isEmpty(); ++i) {
                if (!field.isAnnotationPresent(Parameters.class) && (this.commands.containsKey(args.peek()) || this.isOption(args.peek()))) {
                    return;
                }
                this.consumeOneMapArgument(field, arity, args, classes, keyConverter, valueConverter, result, i, argDescription);
            }
        }
        
        private void consumeOneMapArgument(final Field field, final Range arity, final Stack<String> args, final Class<?>[] classes, final ITypeConverter<?> keyConverter, final ITypeConverter<?> valueConverter, final Map<Object, Object> result, final int index, final String argDescription) throws Exception {
            final String[] split;
            final String[] values = split = this.split(this.trim(args.pop()), field);
            final int length = split.length;
            int i = 0;
            while (i < length) {
                final String value = split[i];
                final String[] keyValue = value.split("=");
                if (keyValue.length < 2) {
                    final String splitRegex = this.splitRegex(field);
                    if (splitRegex.length() == 0) {
                        throw new ParameterException(CommandLine.this, "Value for option " + this.optionDescription("", field, 0) + " should be in KEY=VALUE format but was " + value);
                    }
                    throw new ParameterException(CommandLine.this, "Value for option " + this.optionDescription("", field, 0) + " should be in KEY=VALUE[" + splitRegex + "KEY=VALUE]... format but was " + value);
                }
                else {
                    final Object mapKey = this.tryConvert(field, index, keyConverter, keyValue[0], classes[0]);
                    final Object mapValue = this.tryConvert(field, index, valueConverter, keyValue[1], classes[1]);
                    result.put(mapKey, mapValue);
                    if (CommandLine.this.tracer.isInfo()) {
                        CommandLine.this.tracer.info("Putting [%s : %s] in %s<%s, %s> field '%s.%s' for %s%n", String.valueOf(mapKey), String.valueOf(mapValue), result.getClass().getSimpleName(), classes[0].getSimpleName(), classes[1].getSimpleName(), field.getDeclaringClass().getSimpleName(), field.getName(), argDescription);
                    }
                    ++i;
                }
            }
        }
        
        private void checkMaxArityExceeded(final Range arity, final int remainder, final Field field, final String[] values) {
            if (values.length <= remainder) {
                return;
            }
            final String desc = (arity.max == remainder) ? ("" + remainder) : (arity + ", remainder=" + remainder);
            throw new MaxValuesforFieldExceededException(CommandLine.this, this.optionDescription("", field, -1) + " max number of values (" + arity.max + ") exceeded: remainder is " + remainder + " but " + values.length + " values were specified: " + Arrays.toString(values));
        }
        
        private int applyValuesToArrayField(final Field field, final Class<?> annotation, final Range arity, final Stack<String> args, final Class<?> cls, final String argDescription) throws Exception {
            final Object existing = field.get(this.command);
            final int length = (existing == null) ? 0 : Array.getLength(existing);
            final Class<?> type = getTypeAttribute(field)[0];
            final List<Object> converted = this.consumeArguments(field, annotation, arity, args, type, length, argDescription);
            final List<Object> newValues = new ArrayList<Object>();
            for (int i = 0; i < length; ++i) {
                newValues.add(Array.get(existing, i));
            }
            for (final Object obj : converted) {
                if (obj instanceof Collection) {
                    newValues.addAll((Collection<?>)obj);
                }
                else {
                    newValues.add(obj);
                }
            }
            final Object array = Array.newInstance(type, newValues.size());
            field.set(this.command, array);
            for (int j = 0; j < newValues.size(); ++j) {
                Array.set(array, j, newValues.get(j));
            }
            return converted.size();
        }
        
        private int applyValuesToCollectionField(final Field field, final Class<?> annotation, final Range arity, final Stack<String> args, final Class<?> cls, final String argDescription) throws Exception {
            Collection<Object> collection = (Collection<Object>)field.get(this.command);
            final Class<?> type = getTypeAttribute(field)[0];
            final int length = (collection == null) ? 0 : collection.size();
            final List<Object> converted = this.consumeArguments(field, annotation, arity, args, type, length, argDescription);
            if (collection == null) {
                collection = this.createCollection(cls);
                field.set(this.command, collection);
            }
            for (final Object element : converted) {
                if (element instanceof Collection) {
                    collection.addAll((Collection<?>)element);
                }
                else {
                    collection.add(element);
                }
            }
            return converted.size();
        }
        
        private List<Object> consumeArguments(final Field field, final Class<?> annotation, final Range arity, final Stack<String> args, final Class<?> type, final int originalSize, final String argDescription) throws Exception {
            final List<Object> result = new ArrayList<Object>();
            for (int i = 0; i < arity.min; ++i) {
                this.consumeOneArgument(field, arity, args, type, result, i, originalSize, argDescription);
            }
            for (int i = arity.min; i < arity.max && !args.isEmpty(); ++i) {
                if (annotation != Parameters.class && (this.commands.containsKey(args.peek()) || this.isOption(args.peek()))) {
                    return result;
                }
                this.consumeOneArgument(field, arity, args, type, result, i, originalSize, argDescription);
            }
            return result;
        }
        
        private int consumeOneArgument(final Field field, final Range arity, final Stack<String> args, final Class<?> type, final List<Object> result, int index, final int originalSize, final String argDescription) throws Exception {
            final String[] values = this.split(this.trim(args.pop()), field);
            final ITypeConverter<?> converter = this.getTypeConverter(type, field);
            for (int j = 0; j < values.length; ++j) {
                result.add(this.tryConvert(field, index, converter, values[j], type));
                if (CommandLine.this.tracer.isInfo()) {
                    if (field.getType().isArray()) {
                        CommandLine.this.tracer.info("Adding [%s] to %s[] field '%s.%s' for %s%n", String.valueOf(result.get(result.size() - 1)), type.getSimpleName(), field.getDeclaringClass().getSimpleName(), field.getName(), argDescription);
                    }
                    else {
                        CommandLine.this.tracer.info("Adding [%s] to %s<%s> field '%s.%s' for %s%n", String.valueOf(result.get(result.size() - 1)), field.getType().getSimpleName(), type.getSimpleName(), field.getDeclaringClass().getSimpleName(), field.getName(), argDescription);
                    }
                }
            }
            return ++index;
        }
        
        private String splitRegex(final Field field) {
            if (field.isAnnotationPresent(Option.class)) {
                return field.getAnnotation(Option.class).split();
            }
            if (field.isAnnotationPresent(Parameters.class)) {
                return field.getAnnotation(Parameters.class).split();
            }
            return "";
        }
        
        private String[] split(final String value, final Field field) {
            final String regex = this.splitRegex(field);
            return (regex.length() == 0) ? new String[] { value } : value.split(regex);
        }
        
        private boolean isOption(final String arg) {
            if ("--".equals(arg)) {
                return true;
            }
            if (this.optionName2Field.containsKey(arg)) {
                return true;
            }
            final int separatorIndex = arg.indexOf(this.separator);
            return (separatorIndex > 0 && this.optionName2Field.containsKey(arg.substring(0, separatorIndex))) || (arg.length() > 2 && arg.startsWith("-") && this.singleCharOption2Field.containsKey(arg.charAt(1)));
        }
        
        private Object tryConvert(final Field field, final int index, final ITypeConverter<?> converter, final String value, final Class<?> type) throws Exception {
            try {
                return converter.convert(value);
            }
            catch (TypeConversionException ex) {
                throw new ParameterException(CommandLine.this, ex.getMessage() + this.optionDescription(" for ", field, index));
            }
            catch (Exception other) {
                final String desc = this.optionDescription(" for ", field, index) + ": " + other;
                throw new ParameterException(CommandLine.this, "Could not convert '" + value + "' to " + type.getSimpleName() + desc, other);
            }
        }
        
        private String optionDescription(final String prefix, final Field field, final int index) {
            final Help.IParamLabelRenderer labelRenderer = Help.createMinimalParamLabelRenderer();
            String desc = "";
            if (field.isAnnotationPresent(Option.class)) {
                desc = prefix + "option '" + field.getAnnotation(Option.class).names()[0] + "'";
                if (index >= 0) {
                    final Range arity = Range.optionArity(field);
                    if (arity.max > 1) {
                        desc = desc + " at index " + index;
                    }
                    desc = desc + " (" + labelRenderer.renderParameterLabel(field, Help.Ansi.OFF, Collections.emptyList()) + ")";
                }
            }
            else if (field.isAnnotationPresent(Parameters.class)) {
                final Range indexRange = Range.parameterIndex(field);
                final Help.Ansi.Text label = labelRenderer.renderParameterLabel(field, Help.Ansi.OFF, Collections.emptyList());
                desc = prefix + "positional parameter at index " + indexRange + " (" + label + ")";
            }
            return desc;
        }
        
        private boolean isAnyHelpRequested() {
            return this.isHelpRequested || CommandLine.this.versionHelpRequested || CommandLine.this.usageHelpRequested;
        }
        
        private void updateHelpRequested(final Field field) {
            if (field.isAnnotationPresent(Option.class)) {
                this.isHelpRequested |= this.is(field, "help", field.getAnnotation(Option.class).help());
                final CommandLine this$0 = CommandLine.this;
                this$0.versionHelpRequested |= this.is(field, "versionHelp", field.getAnnotation(Option.class).versionHelp());
                final CommandLine this$2 = CommandLine.this;
                this$2.usageHelpRequested |= this.is(field, "usageHelp", field.getAnnotation(Option.class).usageHelp());
            }
        }
        
        private boolean is(final Field f, final String description, final boolean value) {
            if (value && CommandLine.this.tracer.isInfo()) {
                CommandLine.this.tracer.info("Field '%s.%s' has '%s' annotation: not validating required fields%n", f.getDeclaringClass().getSimpleName(), f.getName(), description);
            }
            return value;
        }
        
        private Collection<Object> createCollection(final Class<?> collectionClass) throws Exception {
            if (!collectionClass.isInterface()) {
                return (Collection<Object>)collectionClass.newInstance();
            }
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
        
        private Map<Object, Object> createMap(final Class<?> mapClass) throws Exception {
            try {
                return (Map<Object, Object>)mapClass.newInstance();
            }
            catch (Exception ex) {
                return new LinkedHashMap<Object, Object>();
            }
        }
        
        private ITypeConverter<?> getTypeConverter(final Class<?> type, final Field field) {
            final ITypeConverter<?> result = this.converterRegistry.get(type);
            if (result != null) {
                return result;
            }
            if (type.isEnum()) {
                return new ITypeConverter<Object>() {
                    @Override
                    public Object convert(final String value) throws Exception {
                        return Enum.valueOf(type, value);
                    }
                };
            }
            throw new MissingTypeConverterException(CommandLine.this, "No TypeConverter registered for " + type.getName() + " of field " + field);
        }
        
        private void assertNoMissingParameters(final Field field, final int arity, final Stack<String> args) {
            if (arity <= args.size()) {
                return;
            }
            if (arity == 1) {
                if (field.isAnnotationPresent(Option.class)) {
                    throw new MissingParameterException(CommandLine.this, "Missing required parameter for " + this.optionDescription("", field, 0));
                }
                final Range indexRange = Range.parameterIndex(field);
                final Help.IParamLabelRenderer labelRenderer = Help.createMinimalParamLabelRenderer();
                String sep = "";
                String names = "";
                int count = 0;
                for (int i = indexRange.min; i < this.positionalParametersFields.size(); ++i) {
                    if (Range.parameterArity(this.positionalParametersFields.get(i)).min > 0) {
                        names = names + sep + labelRenderer.renderParameterLabel(this.positionalParametersFields.get(i), Help.Ansi.OFF, Collections.emptyList());
                        sep = ", ";
                        ++count;
                    }
                }
                String msg = "Missing required parameter";
                final Range paramArity = Range.parameterArity(field);
                if (paramArity.isVariable) {
                    msg = msg + "s at positions " + indexRange + ": ";
                }
                else {
                    msg += ((count > 1) ? "s: " : ": ");
                }
                throw new MissingParameterException(CommandLine.this, msg + names);
            }
            else {
                if (args.isEmpty()) {
                    throw new MissingParameterException(CommandLine.this, this.optionDescription("", field, 0) + " requires at least " + arity + " values, but none were specified.");
                }
                throw new MissingParameterException(CommandLine.this, this.optionDescription("", field, 0) + " requires at least " + arity + " values, but only " + args.size() + " were specified: " + reverse((Stack<Object>)args));
            }
        }
        
        private String trim(final String value) {
            return this.unquote(value);
        }
        
        private String unquote(final String value) {
            return (value == null) ? null : ((value.length() > 1 && value.startsWith("\"") && value.endsWith("\"")) ? value.substring(1, value.length() - 1) : value);
        }
    }
    
    private static class PositionalParametersSorter implements Comparator<Field>
    {
        @Override
        public int compare(final Field o1, final Field o2) {
            final int result = Range.parameterIndex(o1).compareTo(Range.parameterIndex(o2));
            return (result == 0) ? Range.parameterArity(o1).compareTo(Range.parameterArity(o2)) : result;
        }
    }
    
    private static class BuiltIn
    {
        static class PathConverter implements ITypeConverter<Path>
        {
            @Override
            public Path convert(final String value) {
                return Paths.get(value, new String[0]);
            }
        }
        
        static class StringConverter implements ITypeConverter<String>
        {
            @Override
            public String convert(final String value) {
                return value;
            }
        }
        
        static class StringBuilderConverter implements ITypeConverter<StringBuilder>
        {
            @Override
            public StringBuilder convert(final String value) {
                return new StringBuilder(value);
            }
        }
        
        static class CharSequenceConverter implements ITypeConverter<CharSequence>
        {
            @Override
            public String convert(final String value) {
                return value;
            }
        }
        
        static class ByteConverter implements ITypeConverter<Byte>
        {
            @Override
            public Byte convert(final String value) {
                return Byte.valueOf(value);
            }
        }
        
        static class BooleanConverter implements ITypeConverter<Boolean>
        {
            @Override
            public Boolean convert(final String value) {
                if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                    return Boolean.parseBoolean(value);
                }
                throw new TypeConversionException("'" + value + "' is not a boolean");
            }
        }
        
        static class CharacterConverter implements ITypeConverter<Character>
        {
            @Override
            public Character convert(final String value) {
                if (value.length() > 1) {
                    throw new TypeConversionException("'" + value + "' is not a single character");
                }
                return value.charAt(0);
            }
        }
        
        static class ShortConverter implements ITypeConverter<Short>
        {
            @Override
            public Short convert(final String value) {
                return Short.valueOf(value);
            }
        }
        
        static class IntegerConverter implements ITypeConverter<Integer>
        {
            @Override
            public Integer convert(final String value) {
                return Integer.valueOf(value);
            }
        }
        
        static class LongConverter implements ITypeConverter<Long>
        {
            @Override
            public Long convert(final String value) {
                return Long.valueOf(value);
            }
        }
        
        static class FloatConverter implements ITypeConverter<Float>
        {
            @Override
            public Float convert(final String value) {
                return Float.valueOf(value);
            }
        }
        
        static class DoubleConverter implements ITypeConverter<Double>
        {
            @Override
            public Double convert(final String value) {
                return Double.valueOf(value);
            }
        }
        
        static class FileConverter implements ITypeConverter<File>
        {
            @Override
            public File convert(final String value) {
                return new File(value);
            }
        }
        
        static class URLConverter implements ITypeConverter<URL>
        {
            @Override
            public URL convert(final String value) throws MalformedURLException {
                return new URL(value);
            }
        }
        
        static class URIConverter implements ITypeConverter<URI>
        {
            @Override
            public URI convert(final String value) throws URISyntaxException {
                return new URI(value);
            }
        }
        
        static class ISO8601DateConverter implements ITypeConverter<Date>
        {
            @Override
            public Date convert(final String value) {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd").parse(value);
                }
                catch (ParseException e) {
                    throw new TypeConversionException("'" + value + "' is not a yyyy-MM-dd date");
                }
            }
        }
        
        static class ISO8601TimeConverter implements ITypeConverter<Time>
        {
            @Override
            public Time convert(final String value) {
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
                catch (ParseException ex) {}
                throw new TypeConversionException("'" + value + "' is not a HH:mm[:ss[.SSS]] time");
            }
        }
        
        static class BigDecimalConverter implements ITypeConverter<BigDecimal>
        {
            @Override
            public BigDecimal convert(final String value) {
                return new BigDecimal(value);
            }
        }
        
        static class BigIntegerConverter implements ITypeConverter<BigInteger>
        {
            @Override
            public BigInteger convert(final String value) {
                return new BigInteger(value);
            }
        }
        
        static class CharsetConverter implements ITypeConverter<Charset>
        {
            @Override
            public Charset convert(final String s) {
                return Charset.forName(s);
            }
        }
        
        static class InetAddressConverter implements ITypeConverter<InetAddress>
        {
            @Override
            public InetAddress convert(final String s) throws Exception {
                return InetAddress.getByName(s);
            }
        }
        
        static class PatternConverter implements ITypeConverter<Pattern>
        {
            @Override
            public Pattern convert(final String s) {
                return Pattern.compile(s);
            }
        }
        
        static class UUIDConverter implements ITypeConverter<UUID>
        {
            @Override
            public UUID convert(final String s) throws Exception {
                return UUID.fromString(s);
            }
        }
    }
    
    public static class Help
    {
        protected static final String DEFAULT_COMMAND_NAME = "<main class>";
        protected static final String DEFAULT_SEPARATOR = "=";
        private static final int usageHelpWidth = 80;
        private static final int optionsColumnWidth = 29;
        private final Object command;
        private final Map<String, Help> commands;
        final ColorScheme colorScheme;
        public final List<Field> optionFields;
        public final List<Field> positionalParametersFields;
        public String separator;
        public String commandName;
        public String[] description;
        public String[] customSynopsis;
        public String[] header;
        public String[] footer;
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
        
        public Help(final Object command) {
            this(command, Ansi.AUTO);
        }
        
        public Help(final Object command, final Ansi ansi) {
            this(command, defaultColorScheme(ansi));
        }
        
        public Help(final Object command, final ColorScheme colorScheme) {
            this.commands = new LinkedHashMap<String, Help>();
            this.commandName = "<main class>";
            this.description = new String[0];
            this.customSynopsis = new String[0];
            this.header = new String[0];
            this.footer = new String[0];
            this.command = Assert.notNull(command, "command");
            this.colorScheme = Assert.notNull(colorScheme, "colorScheme").applySystemProperties();
            final List<Field> options = new ArrayList<Field>();
            final List<Field> operands = new ArrayList<Field>();
            for (Class<?> cls = command.getClass(); cls != null; cls = cls.getSuperclass()) {
                for (final Field field : cls.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Option.class)) {
                        final Option option = field.getAnnotation(Option.class);
                        if (!option.hidden()) {
                            options.add(field);
                        }
                    }
                    if (field.isAnnotationPresent(Parameters.class)) {
                        operands.add(field);
                    }
                }
                if (cls.isAnnotationPresent(Command.class)) {
                    final Command cmd = cls.getAnnotation(Command.class);
                    if ("<main class>".equals(this.commandName)) {
                        this.commandName = cmd.name();
                    }
                    this.separator = ((this.separator == null) ? cmd.separator() : this.separator);
                    this.abbreviateSynopsis = ((this.abbreviateSynopsis == null) ? cmd.abbreviateSynopsis() : this.abbreviateSynopsis);
                    this.sortOptions = ((this.sortOptions == null) ? cmd.sortOptions() : this.sortOptions);
                    this.requiredOptionMarker = ((this.requiredOptionMarker == null) ? cmd.requiredOptionMarker() : this.requiredOptionMarker);
                    this.showDefaultValues = ((this.showDefaultValues == null) ? cmd.showDefaultValues() : this.showDefaultValues);
                    this.customSynopsis = (empty(this.customSynopsis) ? cmd.customSynopsis() : this.customSynopsis);
                    this.description = (empty(this.description) ? cmd.description() : this.description);
                    this.header = (empty(this.header) ? cmd.header() : this.header);
                    this.footer = (empty(this.footer) ? cmd.footer() : this.footer);
                    this.headerHeading = (empty(this.headerHeading) ? cmd.headerHeading() : this.headerHeading);
                    this.synopsisHeading = ((empty(this.synopsisHeading) || "Usage: ".equals(this.synopsisHeading)) ? cmd.synopsisHeading() : this.synopsisHeading);
                    this.descriptionHeading = (empty(this.descriptionHeading) ? cmd.descriptionHeading() : this.descriptionHeading);
                    this.parameterListHeading = (empty(this.parameterListHeading) ? cmd.parameterListHeading() : this.parameterListHeading);
                    this.optionListHeading = (empty(this.optionListHeading) ? cmd.optionListHeading() : this.optionListHeading);
                    this.commandListHeading = ((empty(this.commandListHeading) || "Commands:%n".equals(this.commandListHeading)) ? cmd.commandListHeading() : this.commandListHeading);
                    this.footerHeading = (empty(this.footerHeading) ? cmd.footerHeading() : this.footerHeading);
                }
            }
            this.sortOptions = (this.sortOptions == null || this.sortOptions);
            this.abbreviateSynopsis = (this.abbreviateSynopsis != null && this.abbreviateSynopsis);
            this.requiredOptionMarker = ((this.requiredOptionMarker == null) ? ' ' : this.requiredOptionMarker);
            this.showDefaultValues = (this.showDefaultValues != null && this.showDefaultValues);
            this.synopsisHeading = ((this.synopsisHeading == null) ? "Usage: " : this.synopsisHeading);
            this.commandListHeading = ((this.commandListHeading == null) ? "Commands:%n" : this.commandListHeading);
            this.separator = ((this.separator == null) ? "=" : this.separator);
            this.parameterLabelRenderer = this.createDefaultParamLabelRenderer();
            Collections.sort(operands, new PositionalParametersSorter());
            this.positionalParametersFields = Collections.unmodifiableList((List<? extends Field>)operands);
            this.optionFields = Collections.unmodifiableList((List<? extends Field>)options);
        }
        
        public Help addAllSubcommands(final Map<String, CommandLine> commands) {
            if (commands != null) {
                for (final Map.Entry<String, CommandLine> entry : commands.entrySet()) {
                    this.addSubcommand(entry.getKey(), entry.getValue().getCommand());
                }
            }
            return this;
        }
        
        public Help addSubcommand(final String commandName, final Object command) {
            this.commands.put(commandName, new Help(command));
            return this;
        }
        
        @Deprecated
        public String synopsis() {
            return this.synopsis(0);
        }
        
        public String synopsis(final int synopsisHeadingLength) {
            if (!empty(this.customSynopsis)) {
                return this.customSynopsis(new Object[0]);
            }
            return this.abbreviateSynopsis ? this.abbreviatedSynopsis() : this.detailedSynopsis(synopsisHeadingLength, createShortOptionArityAndNameComparator(), true);
        }
        
        public String abbreviatedSynopsis() {
            final StringBuilder sb = new StringBuilder();
            if (!this.optionFields.isEmpty()) {
                sb.append(" [OPTIONS]");
            }
            for (final Field positionalParam : this.positionalParametersFields) {
                if (!positionalParam.getAnnotation(Parameters.class).hidden()) {
                    sb.append(' ').append(this.parameterLabelRenderer.renderParameterLabel(positionalParam, this.ansi(), this.colorScheme.parameterStyles));
                }
            }
            return this.colorScheme.commandText(this.commandName).toString() + sb.toString() + System.getProperty("line.separator");
        }
        
        @Deprecated
        public String detailedSynopsis(final Comparator<Field> optionSort, final boolean clusterBooleanOptions) {
            return this.detailedSynopsis(0, optionSort, clusterBooleanOptions);
        }
        
        public String detailedSynopsis(final int synopsisHeadingLength, final Comparator<Field> optionSort, final boolean clusterBooleanOptions) {
            Ansi.Text optionText = this.ansi().new Text(0);
            final List<Field> fields = new ArrayList<Field>(this.optionFields);
            if (optionSort != null) {
                Collections.sort(fields, optionSort);
            }
            if (clusterBooleanOptions) {
                final List<Field> booleanOptions = new ArrayList<Field>();
                final StringBuilder clusteredRequired = new StringBuilder("-");
                final StringBuilder clusteredOptional = new StringBuilder("-");
                for (final Field field : fields) {
                    if (field.getType() == Boolean.TYPE || field.getType() == Boolean.class) {
                        final Option option = field.getAnnotation(Option.class);
                        final String shortestName = ShortestFirst.sort(option.names())[0];
                        if (shortestName.length() != 2 || !shortestName.startsWith("-")) {
                            continue;
                        }
                        booleanOptions.add(field);
                        if (option.required()) {
                            clusteredRequired.append(shortestName.substring(1));
                        }
                        else {
                            clusteredOptional.append(shortestName.substring(1));
                        }
                    }
                }
                fields.removeAll(booleanOptions);
                if (clusteredRequired.length() > 1) {
                    optionText = optionText.append(" ").append(this.colorScheme.optionText(clusteredRequired.toString()));
                }
                if (clusteredOptional.length() > 1) {
                    optionText = optionText.append(" [").append(this.colorScheme.optionText(clusteredOptional.toString())).append("]");
                }
            }
            for (final Field field2 : fields) {
                final Option option2 = field2.getAnnotation(Option.class);
                if (!option2.hidden()) {
                    if (option2.required()) {
                        optionText = this.appendOptionSynopsis(optionText, field2, ShortestFirst.sort(option2.names())[0], " ", "");
                        if (!isMultiValue(field2)) {
                            continue;
                        }
                        optionText = this.appendOptionSynopsis(optionText, field2, ShortestFirst.sort(option2.names())[0], " [", "]...");
                    }
                    else {
                        optionText = this.appendOptionSynopsis(optionText, field2, ShortestFirst.sort(option2.names())[0], " [", "]");
                        if (!isMultiValue(field2)) {
                            continue;
                        }
                        optionText = optionText.append("...");
                    }
                }
            }
            for (final Field positionalParam : this.positionalParametersFields) {
                if (!positionalParam.getAnnotation(Parameters.class).hidden()) {
                    optionText = optionText.append(" ");
                    final Ansi.Text label = this.parameterLabelRenderer.renderParameterLabel(positionalParam, this.colorScheme.ansi(), this.colorScheme.parameterStyles);
                    optionText = optionText.append(label);
                }
            }
            final int firstColumnLength = this.commandName.length() + synopsisHeadingLength;
            final TextTable textTable = new TextTable(this.ansi(), new int[] { firstColumnLength, 80 - firstColumnLength });
            textTable.indentWrappedLines = 1;
            final Ansi.Text PADDING = Ansi.OFF.new Text(stringOf('X', synopsisHeadingLength));
            textTable.addRowValues(PADDING.append(this.colorScheme.commandText(this.commandName)), optionText);
            return textTable.toString().substring(synopsisHeadingLength);
        }
        
        private Ansi.Text appendOptionSynopsis(final Ansi.Text optionText, final Field field, final String optionName, final String prefix, final String suffix) {
            final Ansi.Text optionParamText = this.parameterLabelRenderer.renderParameterLabel(field, this.colorScheme.ansi(), this.colorScheme.optionParamStyles);
            return optionText.append(prefix).append(this.colorScheme.optionText(optionName)).append(optionParamText).append(suffix);
        }
        
        public int synopsisHeadingLength() {
            final String[] lines = Ansi.OFF.new Text(this.synopsisHeading).toString().split("\\r?\\n|\\r|%n", -1);
            return lines[lines.length - 1].length();
        }
        
        public String optionList() {
            final Comparator<Field> sortOrder = (this.sortOptions == null || this.sortOptions) ? createShortOptionNameComparator() : null;
            return this.optionList(this.createDefaultLayout(), sortOrder, this.parameterLabelRenderer);
        }
        
        public String optionList(final Layout layout, final Comparator<Field> optionSort, final IParamLabelRenderer valueLabelRenderer) {
            final List<Field> fields = new ArrayList<Field>(this.optionFields);
            if (optionSort != null) {
                Collections.sort(fields, optionSort);
            }
            layout.addOptions(fields, valueLabelRenderer);
            return layout.toString();
        }
        
        public String parameterList() {
            return this.parameterList(this.createDefaultLayout(), this.parameterLabelRenderer);
        }
        
        public String parameterList(final Layout layout, final IParamLabelRenderer paramLabelRenderer) {
            layout.addPositionalParameters(this.positionalParametersFields, paramLabelRenderer);
            return layout.toString();
        }
        
        private static String heading(final Ansi ansi, final String values, final Object... params) {
            final StringBuilder sb = join(ansi, new String[] { values }, new StringBuilder(), params);
            String result = sb.toString();
            result = (result.endsWith(System.getProperty("line.separator")) ? result.substring(0, result.length() - System.getProperty("line.separator").length()) : result);
            return result + new String(spaces(countTrailingSpaces(values)));
        }
        
        private static char[] spaces(final int length) {
            final char[] result = new char[length];
            Arrays.fill(result, ' ');
            return result;
        }
        
        private static int countTrailingSpaces(final String str) {
            if (str == null) {
                return 0;
            }
            int trailingSpaces = 0;
            for (int i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i) {
                ++trailingSpaces;
            }
            return trailingSpaces;
        }
        
        public static StringBuilder join(final Ansi ansi, final String[] values, final StringBuilder sb, final Object... params) {
            if (values != null) {
                final TextTable table = new TextTable(ansi, new int[] { 80 });
                table.indentWrappedLines = 0;
                for (final String summaryLine : values) {
                    final Ansi.Text[] splitLines;
                    final Ansi.Text[] lines = splitLines = ansi.new Text(format(summaryLine, params)).splitLines();
                    for (final Ansi.Text line : splitLines) {
                        table.addRowValues(line);
                    }
                }
                table.toString(sb);
            }
            return sb;
        }
        
        private static String format(final String formatString, final Object... params) {
            return (formatString == null) ? "" : String.format(formatString, params);
        }
        
        public String customSynopsis(final Object... params) {
            return join(this.ansi(), this.customSynopsis, new StringBuilder(), params).toString();
        }
        
        public String description(final Object... params) {
            return join(this.ansi(), this.description, new StringBuilder(), params).toString();
        }
        
        public String header(final Object... params) {
            return join(this.ansi(), this.header, new StringBuilder(), params).toString();
        }
        
        public String footer(final Object... params) {
            return join(this.ansi(), this.footer, new StringBuilder(), params).toString();
        }
        
        public String headerHeading(final Object... params) {
            return heading(this.ansi(), this.headerHeading, params);
        }
        
        public String synopsisHeading(final Object... params) {
            return heading(this.ansi(), this.synopsisHeading, params);
        }
        
        public String descriptionHeading(final Object... params) {
            return empty(this.descriptionHeading) ? "" : heading(this.ansi(), this.descriptionHeading, params);
        }
        
        public String parameterListHeading(final Object... params) {
            return this.positionalParametersFields.isEmpty() ? "" : heading(this.ansi(), this.parameterListHeading, params);
        }
        
        public String optionListHeading(final Object... params) {
            return this.optionFields.isEmpty() ? "" : heading(this.ansi(), this.optionListHeading, params);
        }
        
        public String commandListHeading(final Object... params) {
            return this.commands.isEmpty() ? "" : heading(this.ansi(), this.commandListHeading, params);
        }
        
        public String footerHeading(final Object... params) {
            return heading(this.ansi(), this.footerHeading, params);
        }
        
        public String commandList() {
            if (this.commands.isEmpty()) {
                return "";
            }
            final int commandLength = maxLength(this.commands.keySet());
            final TextTable textTable = new TextTable(this.ansi(), new Column[] { new Column(commandLength + 2, 2, Column.Overflow.SPAN), new Column(80 - (commandLength + 2), 2, Column.Overflow.WRAP) });
            for (final Map.Entry<String, Help> entry : this.commands.entrySet()) {
                final Help command = entry.getValue();
                final String header = (command.header != null && command.header.length > 0) ? command.header[0] : ((command.description != null && command.description.length > 0) ? command.description[0] : "");
                textTable.addRowValues(this.colorScheme.commandText(entry.getKey()), this.ansi().new Text(header));
            }
            return textTable.toString();
        }
        
        private static int maxLength(final Collection<String> any) {
            final List<String> strings = new ArrayList<String>(any);
            Collections.sort(strings, (Comparator<? super String>)Collections.reverseOrder((Comparator<? super T>)shortestFirst()));
            return strings.get(0).length();
        }
        
        private static String join(final String[] names, final int offset, final int length, final String separator) {
            if (names == null) {
                return "";
            }
            final StringBuilder result = new StringBuilder();
            for (int i = offset; i < offset + length; ++i) {
                result.append((i > offset) ? separator : "").append(names[i]);
            }
            return result.toString();
        }
        
        private static String stringOf(final char chr, final int length) {
            final char[] buff = new char[length];
            Arrays.fill(buff, chr);
            return new String(buff);
        }
        
        public Layout createDefaultLayout() {
            return new Layout(this.colorScheme, new TextTable(this.colorScheme.ansi()), this.createDefaultOptionRenderer(), this.createDefaultParameterRenderer());
        }
        
        public IOptionRenderer createDefaultOptionRenderer() {
            final DefaultOptionRenderer result = new DefaultOptionRenderer();
            result.requiredMarker = String.valueOf(this.requiredOptionMarker);
            if (this.showDefaultValues != null && this.showDefaultValues) {
                result.command = this.command;
            }
            return result;
        }
        
        public static IOptionRenderer createMinimalOptionRenderer() {
            return new MinimalOptionRenderer();
        }
        
        public IParameterRenderer createDefaultParameterRenderer() {
            final DefaultParameterRenderer result = new DefaultParameterRenderer();
            result.requiredMarker = String.valueOf(this.requiredOptionMarker);
            return result;
        }
        
        public static IParameterRenderer createMinimalParameterRenderer() {
            return new MinimalParameterRenderer();
        }
        
        public static IParamLabelRenderer createMinimalParamLabelRenderer() {
            return new IParamLabelRenderer() {
                @Override
                public Ansi.Text renderParameterLabel(final Field field, final Ansi ansi, final List<Ansi.IStyle> styles) {
                    final String text = renderParameterName(field);
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
        
        public static ColorScheme defaultColorScheme(final Ansi ansi) {
            return new ColorScheme(ansi).commands(Ansi.Style.bold).options(Ansi.Style.fg_yellow).parameters(Ansi.Style.fg_yellow).optionParams(Ansi.Style.italic);
        }
        
        static class DefaultOptionRenderer implements IOptionRenderer
        {
            public String requiredMarker;
            public Object command;
            private String sep;
            private boolean showDefault;
            
            DefaultOptionRenderer() {
                this.requiredMarker = " ";
            }
            
            @Override
            public Ansi.Text[][] render(final Option option, final Field field, final IParamLabelRenderer paramLabelRenderer, final ColorScheme scheme) {
                final String[] names = ShortestFirst.sort(option.names());
                final int shortOptionCount = (names[0].length() == 2) ? 1 : 0;
                final String shortOption = (shortOptionCount > 0) ? names[0] : "";
                this.sep = ((shortOptionCount > 0 && names.length > 1) ? "," : "");
                final String longOption = join(names, shortOptionCount, names.length - shortOptionCount, ", ");
                final Ansi.Text longOptionText = this.createLongOptionText(field, paramLabelRenderer, scheme, longOption);
                this.showDefault = (this.command != null && !option.help() && !isBoolean(field.getType()));
                final Object defaultValue = this.createDefaultValue(field);
                final String requiredOption = option.required() ? this.requiredMarker : "";
                return this.renderDescriptionLines(option, scheme, requiredOption, shortOption, longOptionText, defaultValue);
            }
            
            private Object createDefaultValue(final Field field) {
                Object defaultValue = null;
                try {
                    defaultValue = field.get(this.command);
                    if (defaultValue == null) {
                        this.showDefault = false;
                    }
                    else if (field.getType().isArray()) {
                        final StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < Array.getLength(defaultValue); ++i) {
                            sb.append((i > 0) ? ", " : "").append(Array.get(defaultValue, i));
                        }
                        defaultValue = sb.insert(0, "[").append("]").toString();
                    }
                }
                catch (Exception ex) {
                    this.showDefault = false;
                }
                return defaultValue;
            }
            
            private Ansi.Text createLongOptionText(final Field field, final IParamLabelRenderer renderer, final ColorScheme scheme, final String longOption) {
                Ansi.Text paramLabelText = renderer.renderParameterLabel(field, scheme.ansi(), scheme.optionParamStyles);
                if (paramLabelText.length > 0 && longOption.length() == 0) {
                    this.sep = renderer.separator();
                    final int sepStart = paramLabelText.plainString().indexOf(this.sep);
                    final Ansi.Text prefix = paramLabelText.substring(0, sepStart);
                    paramLabelText = prefix.append(paramLabelText.substring(sepStart + this.sep.length()));
                }
                Ansi.Text longOptionText = scheme.optionText(longOption);
                longOptionText = longOptionText.append(paramLabelText);
                return longOptionText;
            }
            
            private Ansi.Text[][] renderDescriptionLines(final Option option, final ColorScheme scheme, final String requiredOption, final String shortOption, final Ansi.Text longOptionText, final Object defaultValue) {
                final Ansi.Text EMPTY = Ansi.EMPTY_TEXT;
                final List<Ansi.Text[]> result = new ArrayList<Ansi.Text[]>();
                Ansi.Text[] descriptionFirstLines = scheme.ansi().new Text(str(option.description(), 0)).splitLines();
                if (descriptionFirstLines.length == 0) {
                    if (this.showDefault) {
                        final Ansi.Text[] array = { null };
                        final int n = 0;
                        final Ansi ansi = scheme.ansi();
                        ansi.getClass();
                        array[n] = ansi.new Text("  Default: " + defaultValue);
                        descriptionFirstLines = array;
                        this.showDefault = false;
                    }
                    else {
                        descriptionFirstLines = new Ansi.Text[] { EMPTY };
                    }
                }
                result.add(new Ansi.Text[] { scheme.optionText(requiredOption), scheme.optionText(shortOption), scheme.ansi().new Text(this.sep), longOptionText, descriptionFirstLines[0] });
                for (int i = 1; i < descriptionFirstLines.length; ++i) {
                    result.add(new Ansi.Text[] { EMPTY, EMPTY, EMPTY, EMPTY, descriptionFirstLines[i] });
                }
                for (int i = 1; i < option.description().length; ++i) {
                    final Ansi.Text[] splitLines;
                    final Ansi.Text[] descriptionNextLines = splitLines = scheme.ansi().new Text(option.description()[i]).splitLines();
                    for (final Ansi.Text line : splitLines) {
                        result.add(new Ansi.Text[] { EMPTY, EMPTY, EMPTY, EMPTY, line });
                    }
                }
                if (this.showDefault) {
                    final List<Ansi.Text[]> list = result;
                    final Ansi.Text[] array2 = { EMPTY, EMPTY, EMPTY, EMPTY, null };
                    final int n2 = 4;
                    final Ansi ansi2 = scheme.ansi();
                    ansi2.getClass();
                    array2[n2] = ansi2.new Text("  Default: " + defaultValue);
                    list.add(array2);
                }
                return result.toArray(new Ansi.Text[result.size()][]);
            }
        }
        
        static class MinimalOptionRenderer implements IOptionRenderer
        {
            @Override
            public Ansi.Text[][] render(final Option option, final Field field, final IParamLabelRenderer parameterLabelRenderer, final ColorScheme scheme) {
                Ansi.Text optionText = scheme.optionText(option.names()[0]);
                final Ansi.Text paramLabelText = parameterLabelRenderer.renderParameterLabel(field, scheme.ansi(), scheme.optionParamStyles);
                optionText = optionText.append(paramLabelText);
                final Ansi.Text[][] array = { null };
                final int n = 0;
                final Ansi.Text[] array2 = { optionText, null };
                final int n2 = 1;
                final Ansi ansi = scheme.ansi();
                ansi.getClass();
                array2[n2] = ansi.new Text((option.description().length == 0) ? "" : option.description()[0]);
                array[n] = array2;
                return array;
            }
        }
        
        static class MinimalParameterRenderer implements IParameterRenderer
        {
            @Override
            public Ansi.Text[][] render(final Parameters param, final Field field, final IParamLabelRenderer parameterLabelRenderer, final ColorScheme scheme) {
                final Ansi.Text[][] array = { null };
                final int n = 0;
                final Ansi.Text[] array2 = { parameterLabelRenderer.renderParameterLabel(field, scheme.ansi(), scheme.parameterStyles), null };
                final int n2 = 1;
                final Ansi ansi = scheme.ansi();
                ansi.getClass();
                array2[n2] = ansi.new Text((param.description().length == 0) ? "" : param.description()[0]);
                array[n] = array2;
                return array;
            }
        }
        
        static class DefaultParameterRenderer implements IParameterRenderer
        {
            public String requiredMarker;
            
            DefaultParameterRenderer() {
                this.requiredMarker = " ";
            }
            
            @Override
            public Ansi.Text[][] render(final Parameters params, final Field field, final IParamLabelRenderer paramLabelRenderer, final ColorScheme scheme) {
                final Ansi.Text label = paramLabelRenderer.renderParameterLabel(field, scheme.ansi(), scheme.parameterStyles);
                final Ansi.Text requiredParameter = scheme.parameterText((Range.parameterArity(field).min > 0) ? this.requiredMarker : "");
                final Ansi.Text EMPTY = Ansi.EMPTY_TEXT;
                final List<Ansi.Text[]> result = new ArrayList<Ansi.Text[]>();
                Ansi.Text[] descriptionFirstLines = scheme.ansi().new Text(str(params.description(), 0)).splitLines();
                if (descriptionFirstLines.length == 0) {
                    descriptionFirstLines = new Ansi.Text[] { EMPTY };
                }
                result.add(new Ansi.Text[] { requiredParameter, EMPTY, EMPTY, label, descriptionFirstLines[0] });
                for (int i = 1; i < descriptionFirstLines.length; ++i) {
                    result.add(new Ansi.Text[] { EMPTY, EMPTY, EMPTY, EMPTY, descriptionFirstLines[i] });
                }
                for (int i = 1; i < params.description().length; ++i) {
                    final Ansi.Text[] splitLines;
                    final Ansi.Text[] descriptionNextLines = splitLines = scheme.ansi().new Text(params.description()[i]).splitLines();
                    for (final Ansi.Text line : splitLines) {
                        result.add(new Ansi.Text[] { EMPTY, EMPTY, EMPTY, EMPTY, line });
                    }
                }
                return result.toArray(new Ansi.Text[result.size()][]);
            }
        }
        
        static class DefaultParamLabelRenderer implements IParamLabelRenderer
        {
            public final String separator;
            
            public DefaultParamLabelRenderer(final String separator) {
                this.separator = Assert.notNull(separator, "separator");
            }
            
            @Override
            public String separator() {
                return this.separator;
            }
            
            @Override
            public Ansi.Text renderParameterLabel(final Field field, final Ansi ansi, final List<Ansi.IStyle> styles) {
                final boolean isOptionParameter = field.isAnnotationPresent(Option.class);
                final Range arity = isOptionParameter ? Range.optionArity(field) : Range.parameterCapacity(field);
                final String split = isOptionParameter ? field.getAnnotation(Option.class).split() : field.getAnnotation(Parameters.class).split();
                Ansi.Text result = ansi.new Text("");
                String sep = isOptionParameter ? this.separator : "";
                Ansi.Text paramName = ansi.apply(renderParameterName(field), styles);
                if (!empty(split)) {
                    paramName = paramName.append("[" + split).append(paramName).append("]...");
                }
                for (int i = 0; i < arity.min; ++i) {
                    result = result.append(sep).append(paramName);
                    sep = " ";
                }
                if (arity.isVariable) {
                    if (result.length == 0) {
                        result = result.append(sep + "[").append(paramName).append("]...");
                    }
                    else if (!result.plainString().endsWith("...")) {
                        result = result.append("...");
                    }
                }
                else {
                    sep = ((result.length == 0) ? (isOptionParameter ? this.separator : "") : " ");
                    for (int i = arity.min; i < arity.max; ++i) {
                        if (sep.trim().length() == 0) {
                            result = result.append(sep + "[").append(paramName);
                        }
                        else {
                            result = result.append("[" + sep).append(paramName);
                        }
                        sep = " ";
                    }
                    for (int i = arity.min; i < arity.max; ++i) {
                        result = result.append("]");
                    }
                }
                return result;
            }
            
            private static String renderParameterName(final Field field) {
                String result = null;
                if (field.isAnnotationPresent(Option.class)) {
                    result = field.getAnnotation(Option.class).paramLabel();
                }
                else if (field.isAnnotationPresent(Parameters.class)) {
                    result = field.getAnnotation(Parameters.class).paramLabel();
                }
                if (result != null && result.trim().length() > 0) {
                    return result.trim();
                }
                String name = field.getName();
                if (Map.class.isAssignableFrom(field.getType())) {
                    final Class<?>[] paramTypes = getTypeAttribute(field);
                    if (paramTypes.length < 2 || paramTypes[0] == null || paramTypes[1] == null) {
                        name = "String=String";
                    }
                    else {
                        name = paramTypes[0].getSimpleName() + "=" + paramTypes[1].getSimpleName();
                    }
                }
                return "<" + name + ">";
            }
        }
        
        public static class Layout
        {
            protected final ColorScheme colorScheme;
            protected final TextTable table;
            protected IOptionRenderer optionRenderer;
            protected IParameterRenderer parameterRenderer;
            
            public Layout(final ColorScheme colorScheme) {
                this(colorScheme, new TextTable(colorScheme.ansi()));
            }
            
            public Layout(final ColorScheme colorScheme, final TextTable textTable) {
                this(colorScheme, textTable, new DefaultOptionRenderer(), new DefaultParameterRenderer());
            }
            
            public Layout(final ColorScheme colorScheme, final TextTable textTable, final IOptionRenderer optionRenderer, final IParameterRenderer parameterRenderer) {
                this.colorScheme = Assert.notNull(colorScheme, "colorScheme");
                this.table = Assert.notNull(textTable, "textTable");
                this.optionRenderer = Assert.notNull(optionRenderer, "optionRenderer");
                this.parameterRenderer = Assert.notNull(parameterRenderer, "parameterRenderer");
            }
            
            public void layout(final Field field, final Ansi.Text[][] cellValues) {
                for (final Ansi.Text[] oneRow : cellValues) {
                    this.table.addRowValues(oneRow);
                }
            }
            
            public void addOptions(final List<Field> fields, final IParamLabelRenderer paramLabelRenderer) {
                for (final Field field : fields) {
                    final Option option = field.getAnnotation(Option.class);
                    if (!option.hidden()) {
                        this.addOption(field, paramLabelRenderer);
                    }
                }
            }
            
            public void addOption(final Field field, final IParamLabelRenderer paramLabelRenderer) {
                final Option option = field.getAnnotation(Option.class);
                final Ansi.Text[][] values = this.optionRenderer.render(option, field, paramLabelRenderer, this.colorScheme);
                this.layout(field, values);
            }
            
            public void addPositionalParameters(final List<Field> fields, final IParamLabelRenderer paramLabelRenderer) {
                for (final Field field : fields) {
                    final Parameters parameters = field.getAnnotation(Parameters.class);
                    if (!parameters.hidden()) {
                        this.addPositionalParameter(field, paramLabelRenderer);
                    }
                }
            }
            
            public void addPositionalParameter(final Field field, final IParamLabelRenderer paramLabelRenderer) {
                final Parameters option = field.getAnnotation(Parameters.class);
                final Ansi.Text[][] values = this.parameterRenderer.render(option, field, paramLabelRenderer, this.colorScheme);
                this.layout(field, values);
            }
            
            @Override
            public String toString() {
                return this.table.toString();
            }
        }
        
        static class ShortestFirst implements Comparator<String>
        {
            @Override
            public int compare(final String o1, final String o2) {
                return o1.length() - o2.length();
            }
            
            public static String[] sort(final String[] names) {
                Arrays.sort(names, new ShortestFirst());
                return names;
            }
        }
        
        static class SortByShortestOptionNameAlphabetically implements Comparator<Field>
        {
            @Override
            public int compare(final Field f1, final Field f2) {
                final Option o1 = f1.getAnnotation(Option.class);
                final Option o2 = f2.getAnnotation(Option.class);
                if (o1 == null) {
                    return 1;
                }
                if (o2 == null) {
                    return -1;
                }
                final String[] names1 = ShortestFirst.sort(o1.names());
                final String[] names2 = ShortestFirst.sort(o2.names());
                int result = names1[0].toUpperCase().compareTo(names2[0].toUpperCase());
                result = ((result == 0) ? (-names1[0].compareTo(names2[0])) : result);
                return (o1.help() == o2.help()) ? result : (o2.help() ? -1 : 1);
            }
        }
        
        static class SortByOptionArityAndNameAlphabetically extends SortByShortestOptionNameAlphabetically
        {
            @Override
            public int compare(final Field f1, final Field f2) {
                final Option o1 = f1.getAnnotation(Option.class);
                final Option o2 = f2.getAnnotation(Option.class);
                final Range arity1 = Range.optionArity(f1);
                final Range arity2 = Range.optionArity(f2);
                int result = arity1.max - arity2.max;
                if (result == 0) {
                    result = arity1.min - arity2.min;
                }
                if (result == 0) {
                    if (isMultiValue(f1) && !isMultiValue(f2)) {
                        result = 1;
                    }
                    if (!isMultiValue(f1) && isMultiValue(f2)) {
                        result = -1;
                    }
                }
                return (result == 0) ? super.compare(f1, f2) : result;
            }
        }
        
        public static class TextTable
        {
            public final Column[] columns;
            protected final List<Ansi.Text> columnValues;
            public int indentWrappedLines;
            private final Ansi ansi;
            
            public TextTable(final Ansi ansi) {
                this(ansi, new Column[] { new Column(2, 0, Column.Overflow.TRUNCATE), new Column(2, 0, Column.Overflow.TRUNCATE), new Column(1, 0, Column.Overflow.TRUNCATE), new Column(24, 1, Column.Overflow.SPAN), new Column(51, 1, Column.Overflow.WRAP) });
            }
            
            public TextTable(final Ansi ansi, final int... columnWidths) {
                this.columnValues = new ArrayList<Ansi.Text>();
                this.indentWrappedLines = 2;
                this.ansi = Assert.notNull(ansi, "ansi");
                this.columns = new Column[columnWidths.length];
                for (int i = 0; i < columnWidths.length; ++i) {
                    this.columns[i] = new Column(columnWidths[i], 0, (i == columnWidths.length - 1) ? Column.Overflow.SPAN : Column.Overflow.WRAP);
                }
            }
            
            public TextTable(final Ansi ansi, final Column... columns) {
                this.columnValues = new ArrayList<Ansi.Text>();
                this.indentWrappedLines = 2;
                this.ansi = Assert.notNull(ansi, "ansi");
                this.columns = Assert.notNull(columns, "columns");
                if (columns.length == 0) {
                    throw new IllegalArgumentException("At least one column is required");
                }
            }
            
            public Ansi.Text textAt(final int row, final int col) {
                return this.columnValues.get(col + row * this.columns.length);
            }
            
            @Deprecated
            public Ansi.Text cellAt(final int row, final int col) {
                return this.textAt(row, col);
            }
            
            public int rowCount() {
                return this.columnValues.size() / this.columns.length;
            }
            
            public void addEmptyRow() {
                for (int i = 0; i < this.columns.length; ++i) {
                    this.columnValues.add(this.ansi.new Text(this.columns[i].width));
                }
            }
            
            public void addRowValues(final String... values) {
                final Ansi.Text[] array = new Ansi.Text[values.length];
                for (int i = 0; i < array.length; ++i) {
                    array[i] = ((values[i] == null) ? Ansi.EMPTY_TEXT : this.ansi.new Text(values[i]));
                }
                this.addRowValues(array);
            }
            
            public void addRowValues(final Ansi.Text... values) {
                if (values.length > this.columns.length) {
                    throw new IllegalArgumentException(values.length + " values don't fit in " + this.columns.length + " columns");
                }
                this.addEmptyRow();
                for (int col = 0; col < values.length; ++col) {
                    final int row = this.rowCount() - 1;
                    final Cell cell = this.putValue(row, col, values[col]);
                    if ((cell.row != row || cell.column != col) && col != values.length - 1) {
                        this.addEmptyRow();
                    }
                }
            }
            
            public Cell putValue(int row, int col, Ansi.Text value) {
                if (row > this.rowCount() - 1) {
                    throw new IllegalArgumentException("Cannot write to row " + row + ": rowCount=" + this.rowCount());
                }
                if (value == null || value.plain.length() == 0) {
                    return new Cell(col, row);
                }
                final Column column = this.columns[col];
                int indent = column.indent;
                switch (column.overflow) {
                    case TRUNCATE: {
                        copy(value, this.textAt(row, col), indent);
                        return new Cell(col, row);
                    }
                    case SPAN: {
                        final int startColumn = col;
                        do {
                            final boolean lastColumn = col == this.columns.length - 1;
                            final int charsWritten = lastColumn ? this.copy(BreakIterator.getLineInstance(), value, this.textAt(row, col), indent) : copy(value, this.textAt(row, col), indent);
                            value = value.substring(charsWritten);
                            indent = 0;
                            if (value.length > 0) {
                                ++col;
                            }
                            if (value.length > 0 && col >= this.columns.length) {
                                this.addEmptyRow();
                                ++row;
                                col = startColumn;
                                indent = column.indent + this.indentWrappedLines;
                            }
                        } while (value.length > 0);
                        return new Cell(col, row);
                    }
                    case WRAP: {
                        final BreakIterator lineBreakIterator = BreakIterator.getLineInstance();
                        do {
                            final int charsWritten = this.copy(lineBreakIterator, value, this.textAt(row, col), indent);
                            value = value.substring(charsWritten);
                            indent = column.indent + this.indentWrappedLines;
                            if (value.length > 0) {
                                ++row;
                                this.addEmptyRow();
                            }
                        } while (value.length > 0);
                        return new Cell(col, row);
                    }
                    default: {
                        throw new IllegalStateException(column.overflow.toString());
                    }
                }
            }
            
            private static int length(final Ansi.Text str) {
                return str.length;
            }
            
            private int copy(final BreakIterator line, final Ansi.Text text, final Ansi.Text columnValue, final int offset) {
                line.setText(text.plainString().replace("-", "\u00ff"));
                int done = 0;
                int start = line.first();
                for (int end = line.next(); end != -1; end = line.next()) {
                    final Ansi.Text word = text.substring(start, end);
                    if (columnValue.maxLength < offset + done + length(word)) {
                        break;
                    }
                    done += copy(word, columnValue, offset + done);
                    start = end;
                }
                if (done == 0 && length(text) > columnValue.maxLength) {
                    done = copy(text, columnValue, offset);
                }
                return done;
            }
            
            private static int copy(final Ansi.Text value, final Ansi.Text destination, final int offset) {
                final int length = Math.min(value.length, destination.maxLength - offset);
                value.getStyledChars(value.from, length, destination, offset);
                return length;
            }
            
            public StringBuilder toString(final StringBuilder text) {
                final int columnCount = this.columns.length;
                final StringBuilder row = new StringBuilder(80);
                for (int i = 0; i < this.columnValues.size(); ++i) {
                    final Ansi.Text column = this.columnValues.get(i);
                    row.append(column.toString());
                    row.append(new String(spaces(this.columns[i % columnCount].width - column.length)));
                    if (i % columnCount == columnCount - 1) {
                        int lastChar;
                        for (lastChar = row.length() - 1; lastChar >= 0 && row.charAt(lastChar) == ' '; --lastChar) {}
                        row.setLength(lastChar + 1);
                        text.append(row.toString()).append(System.getProperty("line.separator"));
                        row.setLength(0);
                    }
                }
                return text;
            }
            
            @Override
            public String toString() {
                return this.toString(new StringBuilder()).toString();
            }
            
            public static class Cell
            {
                public final int column;
                public final int row;
                
                public Cell(final int column, final int row) {
                    this.column = column;
                    this.row = row;
                }
            }
        }
        
        public static class Column
        {
            public final int width;
            public final int indent;
            public final Overflow overflow;
            
            public Column(final int width, final int indent, final Overflow overflow) {
                this.width = width;
                this.indent = indent;
                this.overflow = Assert.notNull(overflow, "overflow");
            }
            
            public enum Overflow
            {
                TRUNCATE, 
                SPAN, 
                WRAP;
            }
        }
        
        public static class ColorScheme
        {
            public final List<Ansi.IStyle> commandStyles;
            public final List<Ansi.IStyle> optionStyles;
            public final List<Ansi.IStyle> parameterStyles;
            public final List<Ansi.IStyle> optionParamStyles;
            private final Ansi ansi;
            
            public ColorScheme() {
                this(Ansi.AUTO);
            }
            
            public ColorScheme(final Ansi ansi) {
                this.commandStyles = new ArrayList<Ansi.IStyle>();
                this.optionStyles = new ArrayList<Ansi.IStyle>();
                this.parameterStyles = new ArrayList<Ansi.IStyle>();
                this.optionParamStyles = new ArrayList<Ansi.IStyle>();
                this.ansi = Assert.notNull(ansi, "ansi");
            }
            
            public ColorScheme commands(final Ansi.IStyle... styles) {
                return this.addAll(this.commandStyles, styles);
            }
            
            public ColorScheme options(final Ansi.IStyle... styles) {
                return this.addAll(this.optionStyles, styles);
            }
            
            public ColorScheme parameters(final Ansi.IStyle... styles) {
                return this.addAll(this.parameterStyles, styles);
            }
            
            public ColorScheme optionParams(final Ansi.IStyle... styles) {
                return this.addAll(this.optionParamStyles, styles);
            }
            
            public Ansi.Text commandText(final String command) {
                return this.ansi().apply(command, this.commandStyles);
            }
            
            public Ansi.Text optionText(final String option) {
                return this.ansi().apply(option, this.optionStyles);
            }
            
            public Ansi.Text parameterText(final String parameter) {
                return this.ansi().apply(parameter, this.parameterStyles);
            }
            
            public Ansi.Text optionParamText(final String optionParam) {
                return this.ansi().apply(optionParam, this.optionParamStyles);
            }
            
            public ColorScheme applySystemProperties() {
                this.replace(this.commandStyles, System.getProperty("picocli.color.commands"));
                this.replace(this.optionStyles, System.getProperty("picocli.color.options"));
                this.replace(this.parameterStyles, System.getProperty("picocli.color.parameters"));
                this.replace(this.optionParamStyles, System.getProperty("picocli.color.optionParams"));
                return this;
            }
            
            private void replace(final List<Ansi.IStyle> styles, final String property) {
                if (property != null) {
                    styles.clear();
                    this.addAll(styles, Ansi.Style.parse(property));
                }
            }
            
            private ColorScheme addAll(final List<Ansi.IStyle> styles, final Ansi.IStyle... add) {
                styles.addAll(Arrays.asList(add));
                return this;
            }
            
            public Ansi ansi() {
                return this.ansi;
            }
        }
        
        public enum Ansi
        {
            AUTO, 
            ON, 
            OFF;
            
            static Text EMPTY_TEXT;
            static final boolean isWindows;
            static final boolean isXterm;
            static final boolean ISATTY;
            
            static final boolean calcTTY() {
                if (Ansi.isWindows && Ansi.isXterm) {
                    return true;
                }
                try {
                    return System.class.getDeclaredMethod("console", (Class<?>[])new Class[0]).invoke(null, new Object[0]) != null;
                }
                catch (Throwable reflectionFailed) {
                    return true;
                }
            }
            
            private static boolean ansiPossible() {
                return Ansi.ISATTY && (!Ansi.isWindows || Ansi.isXterm);
            }
            
            public boolean enabled() {
                return this == Ansi.ON || (this != Ansi.OFF && ((System.getProperty("picocli.ansi") == null) ? ansiPossible() : Boolean.getBoolean("picocli.ansi")));
            }
            
            public Text apply(final String plainText, final List<IStyle> styles) {
                if (plainText.length() == 0) {
                    return new Text(0);
                }
                final Text result = new Text(plainText.length());
                final IStyle[] all = styles.toArray(new IStyle[styles.size()]);
                result.sections.add(new StyledSection(0, plainText.length(), Style.on(all), Style.off((IStyle[])reverse(all)) + Style.reset.off()));
                result.plain.append(plainText);
                result.length = result.plain.length();
                return result;
            }
            
            private static <T> T[] reverse(final T[] all) {
                for (int i = 0; i < all.length / 2; ++i) {
                    final T temp = all[i];
                    all[i] = all[all.length - i - 1];
                    all[all.length - i - 1] = temp;
                }
                return all;
            }
            
            static {
                Ansi.EMPTY_TEXT = Ansi.OFF.new Text(0);
                isWindows = System.getProperty("os.name").startsWith("Windows");
                isXterm = (System.getenv("TERM") != null && System.getenv("TERM").startsWith("xterm"));
                ISATTY = calcTTY();
            }
            
            public enum Style implements IStyle
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
                
                private Style(final int startCode, final int endCode) {
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
                
                public static String on(final IStyle... styles) {
                    final StringBuilder result = new StringBuilder();
                    for (final IStyle style : styles) {
                        result.append(style.on());
                    }
                    return result.toString();
                }
                
                public static String off(final IStyle... styles) {
                    final StringBuilder result = new StringBuilder();
                    for (final IStyle style : styles) {
                        result.append(style.off());
                    }
                    return result.toString();
                }
                
                public static IStyle fg(final String str) {
                    try {
                        return valueOf(str.toLowerCase(Locale.ENGLISH));
                    }
                    catch (Exception ex) {
                        try {
                            return valueOf("fg_" + str.toLowerCase(Locale.ENGLISH));
                        }
                        catch (Exception ex2) {
                            return new Palette256Color(true, str);
                        }
                    }
                }
                
                public static IStyle bg(final String str) {
                    try {
                        return valueOf(str.toLowerCase(Locale.ENGLISH));
                    }
                    catch (Exception ex) {
                        try {
                            return valueOf("bg_" + str.toLowerCase(Locale.ENGLISH));
                        }
                        catch (Exception ex2) {
                            return new Palette256Color(false, str);
                        }
                    }
                }
                
                public static IStyle[] parse(final String commaSeparatedCodes) {
                    final String[] codes = commaSeparatedCodes.split(",");
                    final IStyle[] styles = new IStyle[codes.length];
                    for (int i = 0; i < codes.length; ++i) {
                        if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("fg(")) {
                            final int end = codes[i].indexOf(41);
                            styles[i] = fg(codes[i].substring(3, (end < 0) ? codes[i].length() : end));
                        }
                        else if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("bg(")) {
                            final int end = codes[i].indexOf(41);
                            styles[i] = bg(codes[i].substring(3, (end < 0) ? codes[i].length() : end));
                        }
                        else {
                            styles[i] = fg(codes[i]);
                        }
                    }
                    return styles;
                }
            }
            
            static class Palette256Color implements IStyle
            {
                private final int fgbg;
                private final int color;
                
                Palette256Color(final boolean foreground, final String color) {
                    this.fgbg = (foreground ? 38 : 48);
                    final String[] rgb = color.split(";");
                    if (rgb.length == 3) {
                        this.color = 16 + 36 * Integer.decode(rgb[0]) + 6 * Integer.decode(rgb[1]) + Integer.decode(rgb[2]);
                    }
                    else {
                        this.color = Integer.decode(color);
                    }
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
            
            private static class StyledSection
            {
                int startIndex;
                int length;
                String startStyles;
                String endStyles;
                
                StyledSection(final int start, final int len, final String style1, final String style2) {
                    this.startIndex = start;
                    this.length = len;
                    this.startStyles = style1;
                    this.endStyles = style2;
                }
                
                StyledSection withStartIndex(final int newStart) {
                    return new StyledSection(newStart, this.length, this.startStyles, this.endStyles);
                }
            }
            
            public class Text implements Cloneable
            {
                private final int maxLength;
                private int from;
                private int length;
                private StringBuilder plain;
                private List<StyledSection> sections;
                
                public Text(final int maxLength) {
                    this.plain = new StringBuilder();
                    this.sections = new ArrayList<StyledSection>();
                    this.maxLength = maxLength;
                }
                
                public Text(final String input) {
                    this.plain = new StringBuilder();
                    this.sections = new ArrayList<StyledSection>();
                    this.maxLength = -1;
                    this.plain.setLength(0);
                    int i = 0;
                    while (true) {
                        int j = input.indexOf("@|", i);
                        if (j == -1) {
                            if (i == 0) {
                                this.plain.append(input);
                                this.length = this.plain.length();
                                return;
                            }
                            this.plain.append(input.substring(i, input.length()));
                            this.length = this.plain.length();
                        }
                        else {
                            this.plain.append(input.substring(i, j));
                            final int k = input.indexOf("|@", j);
                            if (k == -1) {
                                this.plain.append(input);
                                this.length = this.plain.length();
                                return;
                            }
                            j += 2;
                            final String spec = input.substring(j, k);
                            final String[] items = spec.split(" ", 2);
                            if (items.length == 1) {
                                this.plain.append(input);
                                this.length = this.plain.length();
                                return;
                            }
                            final IStyle[] styles = Style.parse(items[0]);
                            this.addStyledSection(this.plain.length(), items[1].length(), Style.on(styles), Style.off((IStyle[])reverse(styles)) + Style.reset.off());
                            this.plain.append(items[1]);
                            i = k + 2;
                        }
                    }
                }
                
                private void addStyledSection(final int start, final int length, final String startStyle, final String endStyle) {
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
                    final List<Text> result = new ArrayList<Text>();
                    boolean trailingEmptyString = false;
                    int start = 0;
                    int end = 0;
                    for (int i = 0; i < this.plain.length(); end = ++i) {
                        final char c = this.plain.charAt(i);
                        boolean eol = c == '\n';
                        eol |= (c == '\r' && i + 1 < this.plain.length() && this.plain.charAt(i + 1) == '\n' && ++i > 0);
                        eol |= (c == '\r');
                        if (eol) {
                            result.add(this.substring(start, end));
                            trailingEmptyString = (i == this.plain.length() - 1);
                            start = i + 1;
                        }
                    }
                    if (start < this.plain.length() || trailingEmptyString) {
                        result.add(this.substring(start, this.plain.length()));
                    }
                    return result.toArray(new Text[result.size()]);
                }
                
                public Text substring(final int start) {
                    return this.substring(start, this.length);
                }
                
                public Text substring(final int start, final int end) {
                    final Text result = (Text)this.clone();
                    result.from = this.from + start;
                    result.length = end - start;
                    return result;
                }
                
                public Text append(final String string) {
                    return this.append(new Text(string));
                }
                
                public Text append(final Text other) {
                    final Text result = (Text)this.clone();
                    result.plain = new StringBuilder(this.plain.toString().substring(this.from, this.from + this.length));
                    result.from = 0;
                    result.sections = new ArrayList<StyledSection>();
                    for (final StyledSection section : this.sections) {
                        result.sections.add(section.withStartIndex(section.startIndex - this.from));
                    }
                    result.plain.append(other.plain.toString().substring(other.from, other.from + other.length));
                    for (final StyledSection section : other.sections) {
                        final int index = result.length + section.startIndex - other.from;
                        result.sections.add(section.withStartIndex(index));
                    }
                    result.length = result.plain.length();
                    return result;
                }
                
                public void getStyledChars(final int from, final int length, final Text destination, final int offset) {
                    if (destination.length < offset) {
                        for (int i = destination.length; i < offset; ++i) {
                            destination.plain.append(' ');
                        }
                        destination.length = offset;
                    }
                    for (final StyledSection section : this.sections) {
                        destination.sections.add(section.withStartIndex(section.startIndex - from + destination.length));
                    }
                    destination.plain.append(this.plain.toString().substring(from, from + length));
                    destination.length = destination.plain.length();
                }
                
                public String plainString() {
                    return this.plain.toString().substring(this.from, this.from + this.length);
                }
                
                @Override
                public boolean equals(final Object obj) {
                    return this.toString().equals(String.valueOf(obj));
                }
                
                @Override
                public int hashCode() {
                    return this.toString().hashCode();
                }
                
                @Override
                public String toString() {
                    if (!Ansi.this.enabled()) {
                        return this.plain.toString().substring(this.from, this.from + this.length);
                    }
                    if (this.length == 0) {
                        return "";
                    }
                    final StringBuilder sb = new StringBuilder(this.plain.length() + 20 * this.sections.size());
                    StyledSection current = null;
                    for (int end = Math.min(this.from + this.length, this.plain.length()), i = this.from; i < end; ++i) {
                        final StyledSection section = this.findSectionContaining(i);
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
                
                private StyledSection findSectionContaining(final int index) {
                    for (final StyledSection section : this.sections) {
                        if (index >= section.startIndex && index < section.startIndex + section.length) {
                            return section;
                        }
                    }
                    return null;
                }
            }
            
            public interface IStyle
            {
                public static final String CSI = "\u001b[";
                
                String on();
                
                String off();
            }
        }
        
        public interface IOptionRenderer
        {
            Ansi.Text[][] render(final Option option, final Field field, final IParamLabelRenderer parameterLabelRenderer, final ColorScheme scheme);
        }
        
        public interface IParamLabelRenderer
        {
            Ansi.Text renderParameterLabel(final Field field, final Ansi ansi, final List<Ansi.IStyle> styles);
            
            String separator();
        }
        
        public interface IParameterRenderer
        {
            Ansi.Text[][] render(final Parameters parameters, final Field field, final IParamLabelRenderer parameterLabelRenderer, final ColorScheme scheme);
        }
    }
    
    private static final class Assert
    {
        static <T> T notNull(final T object, final String description) {
            if (object == null) {
                throw new NullPointerException(description);
            }
            return object;
        }
    }
    
    private enum TraceLevel
    {
        OFF, 
        WARN, 
        INFO, 
        DEBUG;
        
        public boolean isEnabled(final TraceLevel other) {
            return this.ordinal() >= other.ordinal();
        }
        
        private void print(final Tracer tracer, final String msg, final Object... params) {
            if (tracer.level.isEnabled(this)) {
                tracer.stream.printf(this.prefix(msg), params);
            }
        }
        
        private String prefix(final String msg) {
            return "[picocli " + this + "] " + msg;
        }
        
        static TraceLevel lookup(final String key) {
            return (key == null) ? TraceLevel.WARN : ((empty(key) || "true".equalsIgnoreCase(key)) ? TraceLevel.INFO : valueOf(key));
        }
    }
    
    private static class Tracer
    {
        TraceLevel level;
        PrintStream stream;
        
        private Tracer() {
            this.level = TraceLevel.lookup(System.getProperty("picocli.trace"));
            this.stream = System.err;
        }
        
        void warn(final String msg, final Object... params) {
            TraceLevel.WARN.print(this, msg, params);
        }
        
        void info(final String msg, final Object... params) {
            TraceLevel.INFO.print(this, msg, params);
        }
        
        void debug(final String msg, final Object... params) {
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
    
    public static class PicocliException extends RuntimeException
    {
        private static final long serialVersionUID = -2574128880125050818L;
        
        public PicocliException(final String msg) {
            super(msg);
        }
        
        public PicocliException(final String msg, final Exception ex) {
            super(msg, ex);
        }
    }
    
    public static class InitializationException extends PicocliException
    {
        private static final long serialVersionUID = 8423014001666638895L;
        
        public InitializationException(final String msg) {
            super(msg);
        }
        
        public InitializationException(final String msg, final Exception ex) {
            super(msg, ex);
        }
    }
    
    public static class ExecutionException extends PicocliException
    {
        private static final long serialVersionUID = 7764539594267007998L;
        private final CommandLine commandLine;
        
        public ExecutionException(final CommandLine commandLine, final String msg) {
            super(msg);
            this.commandLine = Assert.notNull(commandLine, "commandLine");
        }
        
        public ExecutionException(final CommandLine commandLine, final String msg, final Exception ex) {
            super(msg, ex);
            this.commandLine = Assert.notNull(commandLine, "commandLine");
        }
        
        public CommandLine getCommandLine() {
            return this.commandLine;
        }
    }
    
    public static class TypeConversionException extends PicocliException
    {
        private static final long serialVersionUID = 4251973913816346114L;
        
        public TypeConversionException(final String msg) {
            super(msg);
        }
    }
    
    public static class ParameterException extends PicocliException
    {
        private static final long serialVersionUID = 1477112829129763139L;
        private final CommandLine commandLine;
        
        public ParameterException(final CommandLine commandLine, final String msg) {
            super(msg);
            this.commandLine = Assert.notNull(commandLine, "commandLine");
        }
        
        public ParameterException(final CommandLine commandLine, final String msg, final Exception ex) {
            super(msg, ex);
            this.commandLine = Assert.notNull(commandLine, "commandLine");
        }
        
        public CommandLine getCommandLine() {
            return this.commandLine;
        }
        
        private static ParameterException create(final CommandLine cmd, final Exception ex, final String arg, final int i, final String[] args) {
            final String msg = ex.getClass().getSimpleName() + ": " + ex.getLocalizedMessage() + " while processing argument at or before arg[" + i + "] '" + arg + "' in " + Arrays.toString(args) + ": " + ex.toString();
            return new ParameterException(cmd, msg, ex);
        }
    }
    
    public static class MissingParameterException extends ParameterException
    {
        private static final long serialVersionUID = 5075678535706338753L;
        
        public MissingParameterException(final CommandLine commandLine, final String msg) {
            super(commandLine, msg);
        }
        
        private static MissingParameterException create(final CommandLine cmd, final Collection<Field> missing, final String separator) {
            if (missing.size() == 1) {
                return new MissingParameterException(cmd, "Missing required option '" + describe(missing.iterator().next(), separator) + "'");
            }
            final List<String> names = new ArrayList<String>(missing.size());
            for (final Field field : missing) {
                names.add(describe(field, separator));
            }
            return new MissingParameterException(cmd, "Missing required options " + names.toString());
        }
        
        private static String describe(final Field field, final String separator) {
            final String prefix = field.isAnnotationPresent(Option.class) ? (field.getAnnotation(Option.class).names()[0] + separator) : ("params[" + field.getAnnotation(Parameters.class).index() + "]" + separator);
            return prefix + renderParameterName(field);
        }
    }
    
    public static class DuplicateOptionAnnotationsException extends InitializationException
    {
        private static final long serialVersionUID = -3355128012575075641L;
        
        public DuplicateOptionAnnotationsException(final String msg) {
            super(msg);
        }
        
        private static DuplicateOptionAnnotationsException create(final String name, final Field field1, final Field field2) {
            return new DuplicateOptionAnnotationsException("Option name '" + name + "' is used by both " + field1.getDeclaringClass().getName() + "." + field1.getName() + " and " + field2.getDeclaringClass().getName() + "." + field2.getName());
        }
    }
    
    public static class ParameterIndexGapException extends InitializationException
    {
        private static final long serialVersionUID = -1520981133257618319L;
        
        public ParameterIndexGapException(final String msg) {
            super(msg);
        }
    }
    
    public static class UnmatchedArgumentException extends ParameterException
    {
        private static final long serialVersionUID = -8700426380701452440L;
        
        public UnmatchedArgumentException(final CommandLine commandLine, final String msg) {
            super(commandLine, msg);
        }
        
        public UnmatchedArgumentException(final CommandLine commandLine, final Stack<String> args) {
            this(commandLine, new ArrayList<String>(reverse((Stack<Object>)args)));
        }
        
        public UnmatchedArgumentException(final CommandLine commandLine, final List<String> args) {
            this(commandLine, "Unmatched argument" + ((args.size() == 1) ? " " : "s ") + args);
        }
    }
    
    public static class MaxValuesforFieldExceededException extends ParameterException
    {
        private static final long serialVersionUID = 6536145439570100641L;
        
        public MaxValuesforFieldExceededException(final CommandLine commandLine, final String msg) {
            super(commandLine, msg);
        }
    }
    
    public static class OverwrittenOptionException extends ParameterException
    {
        private static final long serialVersionUID = 1338029208271055776L;
        
        public OverwrittenOptionException(final CommandLine commandLine, final String msg) {
            super(commandLine, msg);
        }
    }
    
    public static class MissingTypeConverterException extends ParameterException
    {
        private static final long serialVersionUID = -6050931703233083760L;
        
        public MissingTypeConverterException(final CommandLine commandLine, final String msg) {
            super(commandLine, msg);
        }
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Option {
        String[] names();
        
        boolean required() default false;
        
        @Deprecated
        boolean help() default false;
        
        boolean usageHelp() default false;
        
        boolean versionHelp() default false;
        
        String[] description() default {};
        
        String arity() default "";
        
        String paramLabel() default "";
        
        Class<?>[] type() default {};
        
        String split() default "";
        
        boolean hidden() default false;
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Parameters {
        String index() default "*";
        
        String[] description() default {};
        
        String arity() default "";
        
        String paramLabel() default "";
        
        Class<?>[] type() default {};
        
        String split() default "";
        
        boolean hidden() default false;
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.LOCAL_VARIABLE, ElementType.PACKAGE })
    public @interface Command {
        String name() default "<main class>";
        
        Class<?>[] subcommands() default {};
        
        String separator() default "=";
        
        String[] version() default {};
        
        String headerHeading() default "";
        
        String[] header() default {};
        
        String synopsisHeading() default "Usage: ";
        
        boolean abbreviateSynopsis() default false;
        
        String[] customSynopsis() default {};
        
        String descriptionHeading() default "";
        
        String[] description() default {};
        
        String parameterListHeading() default "";
        
        String optionListHeading() default "";
        
        boolean sortOptions() default true;
        
        char requiredOptionMarker() default ' ';
        
        boolean showDefaultValues() default false;
        
        String commandListHeading() default "Commands:%n";
        
        String footerHeading() default "";
        
        String[] footer() default {};
    }
    
    public interface ITypeConverter<K>
    {
        K convert(final String value) throws Exception;
    }
    
    public interface IParseResultHandler
    {
        List<Object> handleParseResult(final List<CommandLine> parsedCommands, final PrintStream out, final Help.Ansi ansi) throws ExecutionException;
    }
    
    public interface IExceptionHandler
    {
        List<Object> handleException(final ParameterException ex, final PrintStream out, final Help.Ansi ansi, final String... args);
    }
}
