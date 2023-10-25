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
  
  private final List<String> unmatchedArguments = new ArrayList<>();
  
  private CommandLine parent;
  
  private boolean usageHelpRequested;
  
  private boolean versionHelpRequested;
  
  private final List<String> versionLines = new ArrayList<>();
  
  public CommandLine(Object command) {
    this.interpreter = new Interpreter(command);
  }
  
  public CommandLine addSubcommand(String name, Object command) {
    CommandLine commandLine = toCommandLine(command);
    commandLine.parent = this;
    this.interpreter.commands.put(name, commandLine);
    return this;
  }
  
  public Map<String, CommandLine> getSubcommands() {
    return new LinkedHashMap<>(this.interpreter.commands);
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
    for (CommandLine command : this.interpreter.commands.values())
      command.setOverwrittenOptionsAllowed(newValue); 
    return this;
  }
  
  public boolean isUnmatchedArgumentsAllowed() {
    return this.unmatchedArgumentsAllowed;
  }
  
  public CommandLine setUnmatchedArgumentsAllowed(boolean newValue) {
    this.unmatchedArgumentsAllowed = newValue;
    for (CommandLine command : this.interpreter.commands.values())
      command.setUnmatchedArgumentsAllowed(newValue); 
    return this;
  }
  
  public List<String> getUnmatchedArguments() {
    return this.unmatchedArguments;
  }
  
  public static <T> T populateCommand(T command, String... args) {
    CommandLine cli = toCommandLine(command);
    cli.parse(args);
    return command;
  }
  
  public List<CommandLine> parse(String... args) {
    return this.interpreter.parse(args);
  }
  
  public static class DefaultExceptionHandler implements IExceptionHandler {
    public List<Object> handleException(CommandLine.ParameterException ex, PrintStream out, CommandLine.Help.Ansi ansi, String... args) {
      out.println(ex.getMessage());
      ex.getCommandLine().usage(out, ansi);
      return Collections.emptyList();
    }
  }
  
  public static boolean printHelpIfRequested(List<CommandLine> parsedCommands, PrintStream out, Help.Ansi ansi) {
    for (CommandLine parsed : parsedCommands) {
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
  
  private static Object execute(CommandLine parsed) {
    Object command = parsed.getCommand();
    if (command instanceof Runnable)
      try {
        ((Runnable)command).run();
        return null;
      } catch (Exception ex) {
        throw new ExecutionException(parsed, "Error while running command (" + command + ")", ex);
      }  
    if (command instanceof Callable)
      try {
        return ((Callable)command).call();
      } catch (Exception ex) {
        throw new ExecutionException(parsed, "Error while calling command (" + command + ")", ex);
      }  
    throw new ExecutionException(parsed, "Parsed command (" + command + ") is not Runnable or Callable");
  }
  
  public static class RunFirst implements IParseResultHandler {
    public List<Object> handleParseResult(List<CommandLine> parsedCommands, PrintStream out, CommandLine.Help.Ansi ansi) {
      if (CommandLine.printHelpIfRequested(parsedCommands, out, ansi))
        return Collections.emptyList(); 
      return Arrays.asList(new Object[] { CommandLine.access$300(parsedCommands.get(0)) });
    }
  }
  
  public static class RunLast implements IParseResultHandler {
    public List<Object> handleParseResult(List<CommandLine> parsedCommands, PrintStream out, CommandLine.Help.Ansi ansi) {
      if (CommandLine.printHelpIfRequested(parsedCommands, out, ansi))
        return Collections.emptyList(); 
      CommandLine last = parsedCommands.get(parsedCommands.size() - 1);
      return Arrays.asList(new Object[] { CommandLine.access$300(last) });
    }
  }
  
  public static class RunAll implements IParseResultHandler {
    public List<Object> handleParseResult(List<CommandLine> parsedCommands, PrintStream out, CommandLine.Help.Ansi ansi) {
      if (CommandLine.printHelpIfRequested(parsedCommands, out, ansi))
        return null; 
      List<Object> result = new ArrayList();
      for (CommandLine parsed : parsedCommands)
        result.add(CommandLine.execute(parsed)); 
      return result;
    }
  }
  
  public List<Object> parseWithHandler(IParseResultHandler handler, PrintStream out, String... args) {
    return parseWithHandlers(handler, out, Help.Ansi.AUTO, new DefaultExceptionHandler(), args);
  }
  
  public List<Object> parseWithHandlers(IParseResultHandler handler, PrintStream out, Help.Ansi ansi, IExceptionHandler exceptionHandler, String... args) {
    try {
      List<CommandLine> result = parse(args);
      return handler.handleParseResult(result, out, ansi);
    } catch (ParameterException ex) {
      return exceptionHandler.handleException(ex, out, ansi, args);
    } 
  }
  
  public static void usage(Object command, PrintStream out) {
    toCommandLine(command).usage(out);
  }
  
  public static void usage(Object command, PrintStream out, Help.Ansi ansi) {
    toCommandLine(command).usage(out, ansi);
  }
  
  public static void usage(Object command, PrintStream out, Help.ColorScheme colorScheme) {
    toCommandLine(command).usage(out, colorScheme);
  }
  
  public void usage(PrintStream out) {
    usage(out, Help.Ansi.AUTO);
  }
  
  public void usage(PrintStream out, Help.Ansi ansi) {
    usage(out, Help.defaultColorScheme(ansi));
  }
  
  public void usage(PrintStream out, Help.ColorScheme colorScheme) {
    Help help = (new Help(this.interpreter.command, colorScheme)).addAllSubcommands(getSubcommands());
    if (!"=".equals(getSeparator())) {
      help.separator = getSeparator();
      help.parameterLabelRenderer = help.createDefaultParamLabelRenderer();
    } 
    if (!"<main class>".equals(getCommandName()))
      help.commandName = getCommandName(); 
    StringBuilder sb = (new StringBuilder()).append(help.headerHeading(new Object[0])).append(help.header(new Object[0])).append(help.synopsisHeading(new Object[0])).append(help.synopsis(help.synopsisHeadingLength())).append(help.descriptionHeading(new Object[0])).append(help.description(new Object[0])).append(help.parameterListHeading(new Object[0])).append(help.parameterList()).append(help.optionListHeading(new Object[0])).append(help.optionList()).append(help.commandListHeading(new Object[0])).append(help.commandList()).append(help.footerHeading(new Object[0])).append(help.footer(new Object[0]));
    out.print(sb);
  }
  
  public void printVersionHelp(PrintStream out) {
    printVersionHelp(out, Help.Ansi.AUTO);
  }
  
  public void printVersionHelp(PrintStream out, Help.Ansi ansi) {
    for (String versionInfo : this.versionLines) {
      ansi.getClass();
      out.println(new Help.Ansi.Text(versionInfo));
    } 
  }
  
  public void printVersionHelp(PrintStream out, Help.Ansi ansi, Object... params) {
    for (String versionInfo : this.versionLines) {
      ansi.getClass();
      out.println(new Help.Ansi.Text(String.format(versionInfo, params)));
    } 
  }
  
  public static <C extends Callable<T>, T> T call(C callable, PrintStream out, String... args) {
    return call(callable, out, Help.Ansi.AUTO, args);
  }
  
  public static <C extends Callable<T>, T> T call(C callable, PrintStream out, Help.Ansi ansi, String... args) {
    CommandLine cmd = new CommandLine(callable);
    List<Object> results = cmd.parseWithHandlers(new RunLast(), out, ansi, new DefaultExceptionHandler(), args);
    return (results == null || results.isEmpty()) ? null : (T)results.get(0);
  }
  
  public static <R extends Runnable> void run(R runnable, PrintStream out, String... args) {
    run(runnable, out, Help.Ansi.AUTO, args);
  }
  
  public static <R extends Runnable> void run(R runnable, PrintStream out, Help.Ansi ansi, String... args) {
    CommandLine cmd = new CommandLine(runnable);
    cmd.parseWithHandlers(new RunLast(), out, ansi, new DefaultExceptionHandler(), args);
  }
  
  public <K> CommandLine registerConverter(Class<K> cls, ITypeConverter<K> converter) {
    this.interpreter.converterRegistry.put(Assert.notNull(cls, "class"), Assert.notNull(converter, "converter"));
    for (CommandLine command : this.interpreter.commands.values())
      command.registerConverter(cls, converter); 
    return this;
  }
  
  public String getSeparator() {
    return this.interpreter.separator;
  }
  
  public CommandLine setSeparator(String separator) {
    this.interpreter.separator = Assert.<String>notNull(separator, "separator");
    return this;
  }
  
  public String getCommandName() {
    return this.commandName;
  }
  
  public CommandLine setCommandName(String commandName) {
    this.commandName = Assert.<String>notNull(commandName, "commandName");
    return this;
  }
  
  private static boolean empty(String str) {
    return (str == null || str.trim().length() == 0);
  }
  
  private static boolean empty(Object[] array) {
    return (array == null || array.length == 0);
  }
  
  private static boolean empty(Help.Ansi.Text txt) {
    return (txt == null || txt.plain.toString().trim().length() == 0);
  }
  
  private static String str(String[] arr, int i) {
    return (arr == null || arr.length == 0) ? "" : arr[i];
  }
  
  private static boolean isBoolean(Class<?> type) {
    return (type == Boolean.class || type == boolean.class);
  }
  
  private static CommandLine toCommandLine(Object obj) {
    return (obj instanceof CommandLine) ? (CommandLine)obj : new CommandLine(obj);
  }
  
  private static boolean isMultiValue(Field field) {
    return isMultiValue(field.getType());
  }
  
  private static boolean isMultiValue(Class<?> cls) {
    return (cls.isArray() || Collection.class.isAssignableFrom(cls) || Map.class.isAssignableFrom(cls));
  }
  
  private static Class<?>[] getTypeAttribute(Field field) {
    Class<?>[] explicit = field.isAnnotationPresent((Class)Parameters.class) ? ((Parameters)field.<Parameters>getAnnotation(Parameters.class)).type() : ((Option)field.<Option>getAnnotation(Option.class)).type();
    if (explicit.length > 0)
      return explicit; 
    if (field.getType().isArray())
      return new Class[] { field.getType().getComponentType() }; 
    if (isMultiValue(field)) {
      Type type = field.getGenericType();
      if (type instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType)type;
        Type[] paramTypes = parameterizedType.getActualTypeArguments();
        Class<?>[] result = new Class[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
          if (paramTypes[i] instanceof Class) {
            result[i] = (Class)paramTypes[i];
          } else if (paramTypes[i] instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType)paramTypes[i];
            Type[] lower = wildcardType.getLowerBounds();
            if (lower.length > 0 && lower[0] instanceof Class) {
              result[i] = (Class)lower[0];
            } else {
              Type[] upper = wildcardType.getUpperBounds();
              if (upper.length > 0 && upper[0] instanceof Class) {
                result[i] = (Class)upper[0];
              } else {
                Arrays.fill((Object[])result, String.class);
                return result;
              } 
            } 
          } else {
            Arrays.fill((Object[])result, String.class);
            return result;
          } 
        } 
        return result;
      } 
      return new Class[] { String.class, String.class };
    } 
    return new Class[] { field.getType() };
  }
  
  public static class Range implements Comparable<Range> {
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
      return field.isAnnotationPresent((Class)CommandLine.Option.class) ? 
        adjustForType(valueOf(((CommandLine.Option)field.<CommandLine.Option>getAnnotation(CommandLine.Option.class)).arity()), field) : new Range(0, 0, false, true, "0");
    }
    
    public static Range parameterArity(Field field) {
      return field.isAnnotationPresent((Class)CommandLine.Parameters.class) ? 
        adjustForType(valueOf(((CommandLine.Parameters)field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).arity()), field) : new Range(0, 0, false, true, "0");
    }
    
    public static Range parameterIndex(Field field) {
      return field.isAnnotationPresent((Class)CommandLine.Parameters.class) ? 
        valueOf(((CommandLine.Parameters)field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).index()) : new Range(0, 0, false, true, "0");
    }
    
    static Range adjustForType(Range result, Field field) {
      return result.isUnspecified ? defaultArity(field) : result;
    }
    
    public static Range defaultArity(Field field) {
      Class<?> type = field.getType();
      if (field.isAnnotationPresent((Class)CommandLine.Option.class))
        return defaultArity(type); 
      if (CommandLine.isMultiValue(type))
        return valueOf("0..1"); 
      return valueOf("1");
    }
    
    public static Range defaultArity(Class<?> type) {
      return CommandLine.isBoolean(type) ? valueOf("0") : valueOf("1");
    }
    
    private int size() {
      return 1 + this.max - this.min;
    }
    
    static Range parameterCapacity(Field field) {
      Range arity = parameterArity(field);
      if (!CommandLine.isMultiValue(field))
        return arity; 
      Range index = parameterIndex(field);
      if (arity.max == 0)
        return arity; 
      if (index.size() == 1)
        return arity; 
      if (index.isVariable)
        return valueOf(arity.min + "..*"); 
      if (arity.size() == 1)
        return valueOf((arity.min * index.size()) + ""); 
      if (arity.isVariable)
        return valueOf((arity.min * index.size()) + "..*"); 
      return valueOf((arity.min * index.size()) + ".." + (arity.max * index.size()));
    }
    
    public static Range valueOf(String range) {
      range = range.trim();
      boolean unspecified = (range.length() == 0 || range.startsWith(".."));
      int min = -1, max = -1;
      boolean variable = false;
      int dots = -1;
      if ((dots = range.indexOf("..")) >= 0) {
        min = parseInt(range.substring(0, dots), 0);
        max = parseInt(range.substring(dots + 2), 2147483647);
        variable = (max == Integer.MAX_VALUE);
      } else {
        max = parseInt(range, 2147483647);
        variable = (max == Integer.MAX_VALUE);
        min = variable ? 0 : max;
      } 
      Range result = new Range(min, max, variable, unspecified, range);
      return result;
    }
    
    private static int parseInt(String str, int defaultValue) {
      try {
        return Integer.parseInt(str);
      } catch (Exception ex) {
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
      return (this.min <= value && this.max >= value);
    }
    
    public boolean equals(Object object) {
      if (!(object instanceof Range))
        return false; 
      Range other = (Range)object;
      return (other.max == this.max && other.min == this.min && other.isVariable == this.isVariable);
    }
    
    public int hashCode() {
      return ((629 + this.max) * 37 + this.min) * 37 + (this.isVariable ? 1 : 0);
    }
    
    public String toString() {
      return (this.min == this.max) ? String.valueOf(this.min) : (this.min + ".." + (this.isVariable ? "*" : (String)Integer.valueOf(this.max)));
    }
    
    public int compareTo(Range other) {
      int result = this.min - other.min;
      return (result == 0) ? (this.max - other.max) : result;
    }
  }
  
  static void init(Class<?> cls, List<Field> requiredFields, Map<String, Field> optionName2Field, Map<Character, Field> singleCharOption2Field, List<Field> positionalParametersFields) {
    Field[] declaredFields = cls.getDeclaredFields();
    for (Field field : declaredFields) {
      field.setAccessible(true);
      if (field.isAnnotationPresent((Class)Option.class)) {
        Option option = field.<Option>getAnnotation(Option.class);
        if (option.required())
          requiredFields.add(field); 
        for (String name : option.names()) {
          Field existing = optionName2Field.put(name, field);
          if (existing != null && existing != field)
            throw DuplicateOptionAnnotationsException.create(name, field, existing); 
          if (name.length() == 2 && name.startsWith("-")) {
            char flag = name.charAt(1);
            Field existing2 = singleCharOption2Field.put(Character.valueOf(flag), field);
            if (existing2 != null && existing2 != field)
              throw DuplicateOptionAnnotationsException.create(name, field, existing2); 
          } 
        } 
      } 
      if (field.isAnnotationPresent((Class)Parameters.class)) {
        if (field.isAnnotationPresent((Class)Option.class))
          throw new DuplicateOptionAnnotationsException("A field can be either @Option or @Parameters, but '" + field
              .getName() + "' is both."); 
        positionalParametersFields.add(field);
        Range arity = Range.parameterArity(field);
        if (arity.min > 0)
          requiredFields.add(field); 
      } 
    } 
  }
  
  static void validatePositionalParameters(List<Field> positionalParametersFields) {
    int min = 0;
    for (Field field : positionalParametersFields) {
      Range index = Range.parameterIndex(field);
      if (index.min > min)
        throw new ParameterIndexGapException("Missing field annotated with @Parameter(index=" + min + "). Nearest field '" + field
            .getName() + "' has index=" + index.min); 
      min = Math.max(min, index.max);
      min = (min == Integer.MAX_VALUE) ? min : (min + 1);
    } 
  }
  
  private static <T> Stack<T> reverse(Stack<T> stack) {
    Collections.reverse(stack);
    return stack;
  }
  
  private class Interpreter {
    private final Map<String, CommandLine> commands = new LinkedHashMap<>();
    
    private final Map<Class<?>, CommandLine.ITypeConverter<?>> converterRegistry = new HashMap<>();
    
    private final Map<String, Field> optionName2Field = new HashMap<>();
    
    private final Map<Character, Field> singleCharOption2Field = new HashMap<>();
    
    private final List<Field> requiredFields = new ArrayList<>();
    
    private final List<Field> positionalParametersFields = new ArrayList<>();
    
    private final Object command;
    
    private boolean isHelpRequested;
    
    private String separator = "=";
    
    private int position;
    
    Interpreter(Object command) {
      this.converterRegistry.put(Path.class, new CommandLine.BuiltIn.PathConverter());
      this.converterRegistry.put(Object.class, new CommandLine.BuiltIn.StringConverter());
      this.converterRegistry.put(String.class, new CommandLine.BuiltIn.StringConverter());
      this.converterRegistry.put(StringBuilder.class, new CommandLine.BuiltIn.StringBuilderConverter());
      this.converterRegistry.put(CharSequence.class, new CommandLine.BuiltIn.CharSequenceConverter());
      this.converterRegistry.put(Byte.class, new CommandLine.BuiltIn.ByteConverter());
      this.converterRegistry.put(byte.class, new CommandLine.BuiltIn.ByteConverter());
      this.converterRegistry.put(Boolean.class, new CommandLine.BuiltIn.BooleanConverter());
      this.converterRegistry.put(boolean.class, new CommandLine.BuiltIn.BooleanConverter());
      this.converterRegistry.put(Character.class, new CommandLine.BuiltIn.CharacterConverter());
      this.converterRegistry.put(char.class, new CommandLine.BuiltIn.CharacterConverter());
      this.converterRegistry.put(Short.class, new CommandLine.BuiltIn.ShortConverter());
      this.converterRegistry.put(short.class, new CommandLine.BuiltIn.ShortConverter());
      this.converterRegistry.put(Integer.class, new CommandLine.BuiltIn.IntegerConverter());
      this.converterRegistry.put(int.class, new CommandLine.BuiltIn.IntegerConverter());
      this.converterRegistry.put(Long.class, new CommandLine.BuiltIn.LongConverter());
      this.converterRegistry.put(long.class, new CommandLine.BuiltIn.LongConverter());
      this.converterRegistry.put(Float.class, new CommandLine.BuiltIn.FloatConverter());
      this.converterRegistry.put(float.class, new CommandLine.BuiltIn.FloatConverter());
      this.converterRegistry.put(Double.class, new CommandLine.BuiltIn.DoubleConverter());
      this.converterRegistry.put(double.class, new CommandLine.BuiltIn.DoubleConverter());
      this.converterRegistry.put(File.class, new CommandLine.BuiltIn.FileConverter());
      this.converterRegistry.put(URI.class, new CommandLine.BuiltIn.URIConverter());
      this.converterRegistry.put(URL.class, new CommandLine.BuiltIn.URLConverter());
      this.converterRegistry.put(Date.class, new CommandLine.BuiltIn.ISO8601DateConverter());
      this.converterRegistry.put(Time.class, new CommandLine.BuiltIn.ISO8601TimeConverter());
      this.converterRegistry.put(BigDecimal.class, new CommandLine.BuiltIn.BigDecimalConverter());
      this.converterRegistry.put(BigInteger.class, new CommandLine.BuiltIn.BigIntegerConverter());
      this.converterRegistry.put(Charset.class, new CommandLine.BuiltIn.CharsetConverter());
      this.converterRegistry.put(InetAddress.class, new CommandLine.BuiltIn.InetAddressConverter());
      this.converterRegistry.put(Pattern.class, new CommandLine.BuiltIn.PatternConverter());
      this.converterRegistry.put(UUID.class, new CommandLine.BuiltIn.UUIDConverter());
      this.command = CommandLine.Assert.notNull(command, "command");
      Class<?> cls = command.getClass();
      String declaredName = null;
      String declaredSeparator = null;
      boolean hasCommandAnnotation = false;
      while (cls != null) {
        CommandLine.init(cls, this.requiredFields, this.optionName2Field, this.singleCharOption2Field, this.positionalParametersFields);
        if (cls.isAnnotationPresent((Class)CommandLine.Command.class)) {
          hasCommandAnnotation = true;
          CommandLine.Command cmd = cls.<CommandLine.Command>getAnnotation(CommandLine.Command.class);
          declaredSeparator = (declaredSeparator == null) ? cmd.separator() : declaredSeparator;
          declaredName = (declaredName == null) ? cmd.name() : declaredName;
          CommandLine.this.versionLines.addAll(Arrays.asList(cmd.version()));
          for (Class<?> sub : cmd.subcommands()) {
            CommandLine.Command subCommand = sub.<CommandLine.Command>getAnnotation(CommandLine.Command.class);
            if (subCommand == null || "<main class>".equals(subCommand.name()))
              throw new CommandLine.InitializationException("Subcommand " + sub.getName() + " is missing the mandatory @Command annotation with a 'name' attribute"); 
            try {
              Constructor<?> constructor = sub.getDeclaredConstructor(new Class[0]);
              constructor.setAccessible(true);
              CommandLine commandLine = CommandLine.toCommandLine(constructor.newInstance(new Object[0]));
              commandLine.parent = CommandLine.this;
              this.commands.put(subCommand.name(), commandLine);
            } catch (InitializationException ex) {
              throw ex;
            } catch (NoSuchMethodException ex) {
              throw new CommandLine.InitializationException("Cannot instantiate subcommand " + sub
                  .getName() + ": the class has no constructor", ex);
            } catch (Exception ex) {
              throw new CommandLine.InitializationException("Could not instantiate and add subcommand " + sub
                  .getName() + ": " + ex, ex);
            } 
          } 
        } 
        cls = cls.getSuperclass();
      } 
      this.separator = (declaredSeparator != null) ? declaredSeparator : this.separator;
      CommandLine.this.commandName = (declaredName != null) ? declaredName : CommandLine.this.commandName;
      Collections.sort(this.positionalParametersFields, new CommandLine.PositionalParametersSorter());
      CommandLine.validatePositionalParameters(this.positionalParametersFields);
      if (this.positionalParametersFields.isEmpty() && this.optionName2Field.isEmpty() && !hasCommandAnnotation)
        throw new CommandLine.InitializationException(command + " (" + command.getClass() + ") is not a command: it has no @Command, @Option or @Parameters annotations"); 
    }
    
    List<CommandLine> parse(String... args) {
      CommandLine.Assert.notNull(args, "argument array");
      if (CommandLine.this.tracer.isInfo())
        CommandLine.this.tracer.info("Parsing %d command line args %s%n", new Object[] { Integer.valueOf(args.length), Arrays.toString((Object[])args) }); 
      Stack<String> arguments = new Stack<>();
      for (int i = args.length - 1; i >= 0; i--)
        arguments.push(args[i]); 
      List<CommandLine> result = new ArrayList<>();
      parse(result, arguments, args);
      return result;
    }
    
    private void parse(List<CommandLine> parsedCommands, Stack<String> argumentStack, String[] originalArgs) {
      this.isHelpRequested = false;
      CommandLine.this.versionHelpRequested = false;
      CommandLine.this.usageHelpRequested = false;
      Class<?> cmdClass = this.command.getClass();
      if (CommandLine.this.tracer.isDebug())
        CommandLine.this.tracer.debug("Initializing %s: %d options, %d positional parameters, %d required, %d subcommands.%n", new Object[] { cmdClass.getName(), Integer.valueOf((new HashSet(this.optionName2Field.values())).size()), Integer.valueOf(this.positionalParametersFields.size()), Integer.valueOf(this.requiredFields.size()), Integer.valueOf(this.commands.size()) }); 
      parsedCommands.add(CommandLine.this);
      List<Field> required = new ArrayList<>(this.requiredFields);
      Set<Field> initialized = new HashSet<>();
      Collections.sort(required, new CommandLine.PositionalParametersSorter());
      try {
        processArguments(parsedCommands, argumentStack, required, initialized, originalArgs);
      } catch (ParameterException ex) {
        throw ex;
      } catch (Exception ex) {
        int offendingArgIndex = originalArgs.length - argumentStack.size() - 1;
        String arg = (offendingArgIndex >= 0 && offendingArgIndex < originalArgs.length) ? originalArgs[offendingArgIndex] : "?";
        throw CommandLine.ParameterException.create(CommandLine.this, ex, arg, offendingArgIndex, originalArgs);
      } 
      if (!isAnyHelpRequested() && !required.isEmpty())
        for (Field missing : required) {
          if (missing.isAnnotationPresent((Class)CommandLine.Option.class))
            throw CommandLine.MissingParameterException.create(CommandLine.this, required, this.separator); 
          assertNoMissingParameters(missing, (CommandLine.Range.parameterArity(missing)).min, argumentStack);
        }  
      if (!CommandLine.this.unmatchedArguments.isEmpty()) {
        if (!CommandLine.this.isUnmatchedArgumentsAllowed())
          throw new CommandLine.UnmatchedArgumentException(CommandLine.this, CommandLine.this.unmatchedArguments); 
        if (CommandLine.this.tracer.isWarn())
          CommandLine.this.tracer.warn("Unmatched arguments: %s%n", new Object[] { CommandLine.access$2100(this.this$0) }); 
      } 
    }
    
    private void processArguments(List<CommandLine> parsedCommands, Stack<String> args, Collection<Field> required, Set<Field> initialized, String[] originalArgs) throws Exception {
      while (!args.isEmpty()) {
        String arg = args.pop();
        if (CommandLine.this.tracer.isDebug())
          CommandLine.this.tracer.debug("Processing argument '%s'. Remainder=%s%n", new Object[] { arg, CommandLine.access$2200((Stack)args.clone()) }); 
        if ("--".equals(arg)) {
          CommandLine.this.tracer.info("Found end-of-options delimiter '--'. Treating remainder as positional parameters.%n", new Object[0]);
          processRemainderAsPositionalParameters(required, initialized, args);
          return;
        } 
        if (this.commands.containsKey(arg)) {
          if (!this.isHelpRequested && !required.isEmpty())
            throw CommandLine.MissingParameterException.create(CommandLine.this, required, this.separator); 
          if (CommandLine.this.tracer.isDebug())
            CommandLine.this.tracer.debug("Found subcommand '%s' (%s)%n", new Object[] { arg, (CommandLine.access$2300((CommandLine)this.commands.get(arg))).command.getClass().getName() }); 
          (this.commands.get(arg)).interpreter.parse(parsedCommands, args, originalArgs);
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
            if (CommandLine.this.tracer.isDebug())
              CommandLine.this.tracer.debug("Separated '%s' option from '%s' option parameter%n", new Object[] { key, optionParam }); 
          } else if (CommandLine.this.tracer.isDebug()) {
            CommandLine.this.tracer.debug("'%s' contains separator '%s' but '%s' is not a known option%n", new Object[] { arg, this.separator, key });
          } 
        } else if (CommandLine.this.tracer.isDebug()) {
          CommandLine.this.tracer.debug("'%s' cannot be separated into <option>%s<option-parameter>%n", new Object[] { arg, this.separator });
        } 
        if (this.optionName2Field.containsKey(arg)) {
          processStandaloneOption(required, initialized, arg, args, paramAttachedToOption);
          continue;
        } 
        if (arg.length() > 2 && arg.startsWith("-")) {
          if (CommandLine.this.tracer.isDebug())
            CommandLine.this.tracer.debug("Trying to process '%s' as clustered short options%n", new Object[] { arg, args }); 
          processClusteredShortOptions(required, initialized, arg, args);
          continue;
        } 
        args.push(arg);
        if (CommandLine.this.tracer.isDebug())
          CommandLine.this.tracer.debug("Could not find option '%s', deciding whether to treat as unmatched option or positional parameter...%n", new Object[] { arg }); 
        if (resemblesOption(arg)) {
          handleUnmatchedArguments(args.pop());
          continue;
        } 
        if (CommandLine.this.tracer.isDebug())
          CommandLine.this.tracer.debug("No option named '%s' found. Processing remainder as positional parameters%n", new Object[] { arg }); 
        processPositionalParameter(required, initialized, args);
      } 
    }
    
    private boolean resemblesOption(String arg) {
      int count = 0;
      for (String optionName : this.optionName2Field.keySet()) {
        for (int i = 0; i < arg.length() && 
          optionName.length() > i && arg.charAt(i) == optionName.charAt(i); i++)
          count++; 
      } 
      boolean result = (count > 0 && count * 10 >= this.optionName2Field.size() * 9);
      if (CommandLine.this.tracer.isDebug())
        CommandLine.this.tracer.debug("%s %s an option: %d matching prefix chars out of %d option names%n", new Object[] { arg, result ? "resembles" : "doesn't resemble", Integer.valueOf(count), Integer.valueOf(this.optionName2Field.size()) }); 
      return result;
    }
    
    private void handleUnmatchedArguments(String arg) {
      Stack<String> args = new Stack<>();
      args.add(arg);
      handleUnmatchedArguments(args);
    }
    
    private void handleUnmatchedArguments(Stack<String> args) {
      for (; !args.isEmpty(); CommandLine.this.unmatchedArguments.add(args.pop()));
    }
    
    private void processRemainderAsPositionalParameters(Collection<Field> required, Set<Field> initialized, Stack<String> args) throws Exception {
      while (!args.empty())
        processPositionalParameter(required, initialized, args); 
    }
    
    private void processPositionalParameter(Collection<Field> required, Set<Field> initialized, Stack<String> args) throws Exception {
      if (CommandLine.this.tracer.isDebug())
        CommandLine.this.tracer.debug("Processing next arg as a positional parameter at index=%d. Remainder=%s%n", new Object[] { Integer.valueOf(this.position), CommandLine.access$2200((Stack)args.clone()) }); 
      int consumed = 0;
      for (Field positionalParam : this.positionalParametersFields) {
        CommandLine.Range indexRange = CommandLine.Range.parameterIndex(positionalParam);
        if (!indexRange.contains(this.position))
          continue; 
        Stack<String> argsCopy = (Stack<String>)args.clone();
        CommandLine.Range arity = CommandLine.Range.parameterArity(positionalParam);
        if (CommandLine.this.tracer.isDebug())
          CommandLine.this.tracer.debug("Position %d is in index range %s. Trying to assign args to %s, arity=%s%n", new Object[] { Integer.valueOf(this.position), indexRange, positionalParam, arity }); 
        assertNoMissingParameters(positionalParam, arity.min, argsCopy);
        int originalSize = argsCopy.size();
        applyOption(positionalParam, CommandLine.Parameters.class, arity, false, argsCopy, initialized, "args[" + indexRange + "] at position " + this.position);
        int count = originalSize - argsCopy.size();
        if (count > 0)
          required.remove(positionalParam); 
        consumed = Math.max(consumed, count);
      } 
      for (int i = 0; i < consumed; ) {
        args.pop();
        i++;
      } 
      this.position += consumed;
      if (CommandLine.this.tracer.isDebug())
        CommandLine.this.tracer.debug("Consumed %d arguments, moving position to index %d.%n", new Object[] { Integer.valueOf(consumed), Integer.valueOf(this.position) }); 
      if (consumed == 0 && !args.isEmpty())
        handleUnmatchedArguments(args.pop()); 
    }
    
    private void processStandaloneOption(Collection<Field> required, Set<Field> initialized, String arg, Stack<String> args, boolean paramAttachedToKey) throws Exception {
      Field field = this.optionName2Field.get(arg);
      required.remove(field);
      CommandLine.Range arity = CommandLine.Range.optionArity(field);
      if (paramAttachedToKey)
        arity = arity.min(Math.max(1, arity.min)); 
      if (CommandLine.this.tracer.isDebug())
        CommandLine.this.tracer.debug("Found option named '%s': field %s, arity=%s%n", new Object[] { arg, field, arity }); 
      applyOption(field, CommandLine.Option.class, arity, paramAttachedToKey, args, initialized, "option " + arg);
    }
    
    private void processClusteredShortOptions(Collection<Field> required, Set<Field> initialized, String arg, Stack<String> args) throws Exception {
      String prefix = arg.substring(0, 1);
      String cluster = arg.substring(1);
      boolean paramAttachedToOption = true;
      while (cluster.length() > 0 && this.singleCharOption2Field.containsKey(Character.valueOf(cluster.charAt(0)))) {
        Field field = this.singleCharOption2Field.get(Character.valueOf(cluster.charAt(0)));
        CommandLine.Range arity = CommandLine.Range.optionArity(field);
        String argDescription = "option " + prefix + cluster.charAt(0);
        if (CommandLine.this.tracer.isDebug())
          CommandLine.this.tracer.debug("Found option '%s%s' in %s: field %s, arity=%s%n", new Object[] { prefix, Character.valueOf(cluster.charAt(0)), arg, field, arity }); 
        required.remove(field);
        cluster = (cluster.length() > 0) ? cluster.substring(1) : "";
        paramAttachedToOption = (cluster.length() > 0);
        if (cluster.startsWith(this.separator)) {
          cluster = cluster.substring(this.separator.length());
          arity = arity.min(Math.max(1, arity.min));
        } 
        if (arity.min > 0 && !CommandLine.empty(cluster) && 
          CommandLine.this.tracer.isDebug())
          CommandLine.this.tracer.debug("Trying to process '%s' as option parameter%n", new Object[] { cluster }); 
        if (!CommandLine.empty(cluster))
          args.push(cluster); 
        int consumed = applyOption(field, CommandLine.Option.class, arity, paramAttachedToOption, args, initialized, argDescription);
        if (CommandLine.empty(cluster) || consumed > 0 || args.isEmpty())
          return; 
        cluster = args.pop();
      } 
      if (cluster.length() == 0)
        return; 
      if (arg.endsWith(cluster)) {
        args.push(paramAttachedToOption ? (prefix + cluster) : cluster);
        if (((String)args.peek()).equals(arg)) {
          if (CommandLine.this.tracer.isDebug())
            CommandLine.this.tracer.debug("Could not match any short options in %s, deciding whether to treat as unmatched option or positional parameter...%n", new Object[] { arg }); 
          if (resemblesOption(arg)) {
            handleUnmatchedArguments(args.pop());
            return;
          } 
          processPositionalParameter(required, initialized, args);
          return;
        } 
        if (CommandLine.this.tracer.isDebug())
          CommandLine.this.tracer.debug("No option found for %s in %s%n", new Object[] { cluster, arg }); 
        handleUnmatchedArguments(args.pop());
      } else {
        args.push(cluster);
        if (CommandLine.this.tracer.isDebug())
          CommandLine.this.tracer.debug("%s is not an option parameter for %s%n", new Object[] { cluster, arg }); 
        processPositionalParameter(required, initialized, args);
      } 
    }
    
    private int applyOption(Field field, Class<?> annotation, CommandLine.Range arity, boolean valueAttachedToOption, Stack<String> args, Set<Field> initialized, String argDescription) throws Exception {
      updateHelpRequested(field);
      int length = args.size();
      assertNoMissingParameters(field, arity.min, args);
      Class<?> cls = field.getType();
      if (cls.isArray())
        return applyValuesToArrayField(field, annotation, arity, args, cls, argDescription); 
      if (Collection.class.isAssignableFrom(cls))
        return applyValuesToCollectionField(field, annotation, arity, args, cls, argDescription); 
      if (Map.class.isAssignableFrom(cls))
        return applyValuesToMapField(field, annotation, arity, args, cls, argDescription); 
      cls = CommandLine.getTypeAttribute(field)[0];
      return applyValueToSingleValuedField(field, arity, args, cls, initialized, argDescription);
    }
    
    private int applyValueToSingleValuedField(Field field, CommandLine.Range arity, Stack<String> args, Class<?> cls, Set<Field> initialized, String argDescription) throws Exception {
      boolean noMoreValues = args.isEmpty();
      String value = args.isEmpty() ? null : trim(args.pop());
      int result = arity.min;
      if ((cls == Boolean.class || cls == boolean.class) && arity.min <= 0)
        if (arity.max > 0 && ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value))) {
          result = 1;
        } else {
          if (value != null)
            args.push(value); 
          Boolean currentValue = (Boolean)field.get(this.command);
          value = String.valueOf((currentValue == null) ? true : (!currentValue.booleanValue()));
        }  
      if (noMoreValues && value == null)
        return 0; 
      CommandLine.ITypeConverter<?> converter = getTypeConverter(cls, field);
      Object newValue = tryConvert(field, -1, converter, value, cls);
      Object oldValue = field.get(this.command);
      CommandLine.TraceLevel level = CommandLine.TraceLevel.INFO;
      String traceMessage = "Setting %s field '%s.%s' to '%5$s' (was '%4$s') for %6$s%n";
      if (initialized != null) {
        if (initialized.contains(field)) {
          if (!CommandLine.this.isOverwrittenOptionsAllowed())
            throw new CommandLine.OverwrittenOptionException(CommandLine.this, optionDescription("", field, 0) + " should be specified only once"); 
          level = CommandLine.TraceLevel.WARN;
          traceMessage = "Overwriting %s field '%s.%s' value '%s' with '%s' for %s%n";
        } 
        initialized.add(field);
      } 
      if (CommandLine.this.tracer.level.isEnabled(level))
        level.print(CommandLine.this.tracer, traceMessage, new Object[] { field.getType().getSimpleName(), field
              .getDeclaringClass().getSimpleName(), field.getName(), String.valueOf(oldValue), String.valueOf(newValue), argDescription }); 
      field.set(this.command, newValue);
      return result;
    }
    
    private int applyValuesToMapField(Field field, Class<?> annotation, CommandLine.Range arity, Stack<String> args, Class<?> cls, String argDescription) throws Exception {
      Class<?>[] classes = CommandLine.getTypeAttribute(field);
      if (classes.length < 2)
        throw new CommandLine.ParameterException(CommandLine.this, "Field " + field + " needs two types (one for the map key, one for the value) but only has " + classes.length + " types configured."); 
      CommandLine.ITypeConverter<?> keyConverter = getTypeConverter(classes[0], field);
      CommandLine.ITypeConverter<?> valueConverter = getTypeConverter(classes[1], field);
      Map<Object, Object> result = (Map<Object, Object>)field.get(this.command);
      if (result == null) {
        result = createMap(cls);
        field.set(this.command, result);
      } 
      int originalSize = result.size();
      consumeMapArguments(field, arity, args, classes, keyConverter, valueConverter, result, argDescription);
      return result.size() - originalSize;
    }
    
    private void consumeMapArguments(Field field, CommandLine.Range arity, Stack<String> args, Class<?>[] classes, CommandLine.ITypeConverter<?> keyConverter, CommandLine.ITypeConverter<?> valueConverter, Map<Object, Object> result, String argDescription) throws Exception {
      int i;
      for (i = 0; i < arity.min; i++)
        consumeOneMapArgument(field, arity, args, classes, keyConverter, valueConverter, result, i, argDescription); 
      for (i = arity.min; i < arity.max && !args.isEmpty(); i++) {
        if (!field.isAnnotationPresent((Class)CommandLine.Parameters.class) && (
          this.commands.containsKey(args.peek()) || isOption(args.peek())))
          return; 
        consumeOneMapArgument(field, arity, args, classes, keyConverter, valueConverter, result, i, argDescription);
      } 
    }
    
    private void consumeOneMapArgument(Field field, CommandLine.Range arity, Stack<String> args, Class<?>[] classes, CommandLine.ITypeConverter<?> keyConverter, CommandLine.ITypeConverter<?> valueConverter, Map<Object, Object> result, int index, String argDescription) throws Exception {
      String[] values = split(trim(args.pop()), field);
      for (String value : values) {
        String[] keyValue = value.split("=");
        if (keyValue.length < 2) {
          String splitRegex = splitRegex(field);
          if (splitRegex.length() == 0)
            throw new CommandLine.ParameterException(CommandLine.this, "Value for option " + optionDescription("", field, 0) + " should be in KEY=VALUE format but was " + value); 
          throw new CommandLine.ParameterException(CommandLine.this, "Value for option " + optionDescription("", field, 0) + " should be in KEY=VALUE[" + splitRegex + "KEY=VALUE]... format but was " + value);
        } 
        Object mapKey = tryConvert(field, index, keyConverter, keyValue[0], classes[0]);
        Object mapValue = tryConvert(field, index, valueConverter, keyValue[1], classes[1]);
        result.put(mapKey, mapValue);
        if (CommandLine.this.tracer.isInfo())
          CommandLine.this.tracer.info("Putting [%s : %s] in %s<%s, %s> field '%s.%s' for %s%n", new Object[] { String.valueOf(mapKey), String.valueOf(mapValue), result
                .getClass().getSimpleName(), classes[0].getSimpleName(), classes[1].getSimpleName(), field.getDeclaringClass().getSimpleName(), field.getName(), argDescription }); 
      } 
    }
    
    private void checkMaxArityExceeded(CommandLine.Range arity, int remainder, Field field, String[] values) {
      if (values.length <= remainder)
        return; 
      String desc = (arity.max == remainder) ? ("" + remainder) : (arity + ", remainder=" + remainder);
      throw new CommandLine.MaxValuesforFieldExceededException(CommandLine.this, optionDescription("", field, -1) + " max number of values (" + arity.max + ") exceeded: remainder is " + remainder + " but " + values.length + " values were specified: " + 
          
          Arrays.toString(values));
    }
    
    private int applyValuesToArrayField(Field field, Class<?> annotation, CommandLine.Range arity, Stack<String> args, Class<?> cls, String argDescription) throws Exception {
      Object existing = field.get(this.command);
      int length = (existing == null) ? 0 : Array.getLength(existing);
      Class<?> type = CommandLine.getTypeAttribute(field)[0];
      List<Object> converted = consumeArguments(field, annotation, arity, args, type, length, argDescription);
      List<Object> newValues = new ArrayList();
      for (int i = 0; i < length; i++)
        newValues.add(Array.get(existing, i)); 
      for (Object obj : converted) {
        if (obj instanceof Collection) {
          newValues.addAll((Collection)obj);
          continue;
        } 
        newValues.add(obj);
      } 
      Object array = Array.newInstance(type, newValues.size());
      field.set(this.command, array);
      for (int j = 0; j < newValues.size(); j++)
        Array.set(array, j, newValues.get(j)); 
      return converted.size();
    }
    
    private int applyValuesToCollectionField(Field field, Class<?> annotation, CommandLine.Range arity, Stack<String> args, Class<?> cls, String argDescription) throws Exception {
      Collection<Object> collection = (Collection<Object>)field.get(this.command);
      Class<?> type = CommandLine.getTypeAttribute(field)[0];
      int length = (collection == null) ? 0 : collection.size();
      List<Object> converted = consumeArguments(field, annotation, arity, args, type, length, argDescription);
      if (collection == null) {
        collection = createCollection(cls);
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
    
    private List<Object> consumeArguments(Field field, Class<?> annotation, CommandLine.Range arity, Stack<String> args, Class<?> type, int originalSize, String argDescription) throws Exception {
      List<Object> result = new ArrayList();
      int i;
      for (i = 0; i < arity.min; i++)
        consumeOneArgument(field, arity, args, type, result, i, originalSize, argDescription); 
      for (i = arity.min; i < arity.max && !args.isEmpty(); i++) {
        if (annotation != CommandLine.Parameters.class && (
          this.commands.containsKey(args.peek()) || isOption(args.peek())))
          return result; 
        consumeOneArgument(field, arity, args, type, result, i, originalSize, argDescription);
      } 
      return result;
    }
    
    private int consumeOneArgument(Field field, CommandLine.Range arity, Stack<String> args, Class<?> type, List<Object> result, int index, int originalSize, String argDescription) throws Exception {
      String[] values = split(trim(args.pop()), field);
      CommandLine.ITypeConverter<?> converter = getTypeConverter(type, field);
      for (int j = 0; j < values.length; j++) {
        result.add(tryConvert(field, index, converter, values[j], type));
        if (CommandLine.this.tracer.isInfo())
          if (field.getType().isArray()) {
            CommandLine.this.tracer.info("Adding [%s] to %s[] field '%s.%s' for %s%n", new Object[] { String.valueOf(result.get(result.size() - 1)), type.getSimpleName(), field.getDeclaringClass().getSimpleName(), field.getName(), argDescription });
          } else {
            CommandLine.this.tracer.info("Adding [%s] to %s<%s> field '%s.%s' for %s%n", new Object[] { String.valueOf(result.get(result.size() - 1)), field.getType().getSimpleName(), type.getSimpleName(), field.getDeclaringClass().getSimpleName(), field.getName(), argDescription });
          }  
      } 
      return ++index;
    }
    
    private String splitRegex(Field field) {
      if (field.isAnnotationPresent((Class)CommandLine.Option.class))
        return ((CommandLine.Option)field.<CommandLine.Option>getAnnotation(CommandLine.Option.class)).split(); 
      if (field.isAnnotationPresent((Class)CommandLine.Parameters.class))
        return ((CommandLine.Parameters)field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).split(); 
      return "";
    }
    
    private String[] split(String value, Field field) {
      String regex = splitRegex(field);
      (new String[1])[0] = value;
      return (regex.length() == 0) ? new String[1] : value.split(regex);
    }
    
    private boolean isOption(String arg) {
      if ("--".equals(arg))
        return true; 
      if (this.optionName2Field.containsKey(arg))
        return true; 
      int separatorIndex = arg.indexOf(this.separator);
      if (separatorIndex > 0 && 
        this.optionName2Field.containsKey(arg.substring(0, separatorIndex)))
        return true; 
      return (arg.length() > 2 && arg.startsWith("-") && this.singleCharOption2Field.containsKey(Character.valueOf(arg.charAt(1))));
    }
    
    private Object tryConvert(Field field, int index, CommandLine.ITypeConverter<?> converter, String value, Class<?> type) throws Exception {
      try {
        return converter.convert(value);
      } catch (TypeConversionException ex) {
        throw new CommandLine.ParameterException(CommandLine.this, ex.getMessage() + optionDescription(" for ", field, index));
      } catch (Exception other) {
        String desc = optionDescription(" for ", field, index) + ": " + other;
        throw new CommandLine.ParameterException(CommandLine.this, "Could not convert '" + value + "' to " + type.getSimpleName() + desc, other);
      } 
    }
    
    private String optionDescription(String prefix, Field field, int index) {
      CommandLine.Help.IParamLabelRenderer labelRenderer = CommandLine.Help.createMinimalParamLabelRenderer();
      String desc = "";
      if (field.isAnnotationPresent((Class)CommandLine.Option.class)) {
        desc = prefix + "option '" + ((CommandLine.Option)field.getAnnotation((Class)CommandLine.Option.class)).names()[0] + "'";
        if (index >= 0) {
          CommandLine.Range arity = CommandLine.Range.optionArity(field);
          if (arity.max > 1)
            desc = desc + " at index " + index; 
          desc = desc + " (" + labelRenderer.renderParameterLabel(field, CommandLine.Help.Ansi.OFF, Collections.emptyList()) + ")";
        } 
      } else if (field.isAnnotationPresent((Class)CommandLine.Parameters.class)) {
        CommandLine.Range indexRange = CommandLine.Range.parameterIndex(field);
        CommandLine.Help.Ansi.Text label = labelRenderer.renderParameterLabel(field, CommandLine.Help.Ansi.OFF, Collections.emptyList());
        desc = prefix + "positional parameter at index " + indexRange + " (" + label + ")";
      } 
      return desc;
    }
    
    private boolean isAnyHelpRequested() {
      return (this.isHelpRequested || CommandLine.this.versionHelpRequested || CommandLine.this.usageHelpRequested);
    }
    
    private void updateHelpRequested(Field field) {
      if (field.isAnnotationPresent((Class)CommandLine.Option.class)) {
        this.isHelpRequested |= is(field, "help", ((CommandLine.Option)field.<CommandLine.Option>getAnnotation(CommandLine.Option.class)).help());
        CommandLine commandLine = CommandLine.this;
        commandLine.versionHelpRequested = commandLine.versionHelpRequested | is(field, "versionHelp", ((CommandLine.Option)field.<CommandLine.Option>getAnnotation(CommandLine.Option.class)).versionHelp());
        commandLine = CommandLine.this;
        commandLine.usageHelpRequested = commandLine.usageHelpRequested | is(field, "usageHelp", ((CommandLine.Option)field.<CommandLine.Option>getAnnotation(CommandLine.Option.class)).usageHelp());
      } 
    }
    
    private boolean is(Field f, String description, boolean value) {
      if (value && CommandLine.this.tracer.isInfo())
        CommandLine.this.tracer.info("Field '%s.%s' has '%s' annotation: not validating required fields%n", new Object[] { f.getDeclaringClass().getSimpleName(), f.getName(), description }); 
      return value;
    }
    
    private Collection<Object> createCollection(Class<?> collectionClass) throws Exception {
      if (collectionClass.isInterface()) {
        if (SortedSet.class.isAssignableFrom(collectionClass))
          return new TreeSet(); 
        if (Set.class.isAssignableFrom(collectionClass))
          return new LinkedHashSet(); 
        if (Queue.class.isAssignableFrom(collectionClass))
          return new LinkedList(); 
        return new ArrayList();
      } 
      return (Collection<Object>)collectionClass.newInstance();
    }
    
    private Map<Object, Object> createMap(Class<?> mapClass) throws Exception {
      try {
        return (Map<Object, Object>)mapClass.newInstance();
      } catch (Exception exception) {
        return new LinkedHashMap<>();
      } 
    }
    
    private CommandLine.ITypeConverter<?> getTypeConverter(final Class<?> type, Field field) {
      CommandLine.ITypeConverter<?> result = this.converterRegistry.get(type);
      if (result != null)
        return result; 
      if (type.isEnum())
        return new CommandLine.ITypeConverter() {
            public Object convert(String value) throws Exception {
              return Enum.valueOf(type, value);
            }
          }; 
      throw new CommandLine.MissingTypeConverterException(CommandLine.this, "No TypeConverter registered for " + type.getName() + " of field " + field);
    }
    
    private void assertNoMissingParameters(Field field, int arity, Stack<String> args) {
      if (arity > args.size()) {
        if (arity == 1) {
          if (field.isAnnotationPresent((Class)CommandLine.Option.class))
            throw new CommandLine.MissingParameterException(CommandLine.this, "Missing required parameter for " + 
                optionDescription("", field, 0)); 
          CommandLine.Range indexRange = CommandLine.Range.parameterIndex(field);
          CommandLine.Help.IParamLabelRenderer labelRenderer = CommandLine.Help.createMinimalParamLabelRenderer();
          String sep = "";
          String names = "";
          int count = 0;
          for (int i = indexRange.min; i < this.positionalParametersFields.size(); i++) {
            if ((CommandLine.Range.parameterArity((Field)this.positionalParametersFields.get(i))).min > 0) {
              names = names + sep + labelRenderer.renderParameterLabel(this.positionalParametersFields.get(i), CommandLine.Help.Ansi.OFF, 
                  Collections.emptyList());
              sep = ", ";
              count++;
            } 
          } 
          String msg = "Missing required parameter";
          CommandLine.Range paramArity = CommandLine.Range.parameterArity(field);
          if (paramArity.isVariable) {
            msg = msg + "s at positions " + indexRange + ": ";
          } else {
            msg = msg + ((count > 1) ? "s: " : ": ");
          } 
          throw new CommandLine.MissingParameterException(CommandLine.this, msg + names);
        } 
        if (args.isEmpty())
          throw new CommandLine.MissingParameterException(CommandLine.this, optionDescription("", field, 0) + " requires at least " + arity + " values, but none were specified."); 
        throw new CommandLine.MissingParameterException(CommandLine.this, optionDescription("", field, 0) + " requires at least " + arity + " values, but only " + args
            .size() + " were specified: " + CommandLine.reverse(args));
      } 
    }
    
    private String trim(String value) {
      return unquote(value);
    }
    
    private String unquote(String value) {
      return (value == null) ? null : ((value
        
        .length() > 1 && value.startsWith("\"") && value.endsWith("\"")) ? value
        .substring(1, value.length() - 1) : value);
    }
  }
  
  private static class PositionalParametersSorter implements Comparator<Field> {
    private PositionalParametersSorter() {}
    
    public int compare(Field o1, Field o2) {
      int result = CommandLine.Range.parameterIndex(o1).compareTo(CommandLine.Range.parameterIndex(o2));
      return (result == 0) ? CommandLine.Range.parameterArity(o1).compareTo(CommandLine.Range.parameterArity(o2)) : result;
    }
  }
  
  private static class BuiltIn {
    static class PathConverter implements CommandLine.ITypeConverter<Path> {
      public Path convert(String value) {
        return Paths.get(value, new String[0]);
      }
    }
    
    static class StringConverter implements CommandLine.ITypeConverter<String> {
      public String convert(String value) {
        return value;
      }
    }
    
    static class StringBuilderConverter implements CommandLine.ITypeConverter<StringBuilder> {
      public StringBuilder convert(String value) {
        return new StringBuilder(value);
      }
    }
    
    static class CharSequenceConverter implements CommandLine.ITypeConverter<CharSequence> {
      public String convert(String value) {
        return value;
      }
    }
    
    static class ByteConverter implements CommandLine.ITypeConverter<Byte> {
      public Byte convert(String value) {
        return Byte.valueOf(value);
      }
    }
    
    static class BooleanConverter implements CommandLine.ITypeConverter<Boolean> {
      public Boolean convert(String value) {
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value))
          return Boolean.valueOf(Boolean.parseBoolean(value)); 
        throw new CommandLine.TypeConversionException("'" + value + "' is not a boolean");
      }
    }
    
    static class CharacterConverter implements CommandLine.ITypeConverter<Character> {
      public Character convert(String value) {
        if (value.length() > 1)
          throw new CommandLine.TypeConversionException("'" + value + "' is not a single character"); 
        return Character.valueOf(value.charAt(0));
      }
    }
    
    static class ShortConverter implements CommandLine.ITypeConverter<Short> {
      public Short convert(String value) {
        return Short.valueOf(value);
      }
    }
    
    static class IntegerConverter implements CommandLine.ITypeConverter<Integer> {
      public Integer convert(String value) {
        return Integer.valueOf(value);
      }
    }
    
    static class LongConverter implements CommandLine.ITypeConverter<Long> {
      public Long convert(String value) {
        return Long.valueOf(value);
      }
    }
    
    static class FloatConverter implements CommandLine.ITypeConverter<Float> {
      public Float convert(String value) {
        return Float.valueOf(value);
      }
    }
    
    static class DoubleConverter implements CommandLine.ITypeConverter<Double> {
      public Double convert(String value) {
        return Double.valueOf(value);
      }
    }
    
    static class FileConverter implements CommandLine.ITypeConverter<File> {
      public File convert(String value) {
        return new File(value);
      }
    }
    
    static class URLConverter implements CommandLine.ITypeConverter<URL> {
      public URL convert(String value) throws MalformedURLException {
        return new URL(value);
      }
    }
    
    static class URIConverter implements CommandLine.ITypeConverter<URI> {
      public URI convert(String value) throws URISyntaxException {
        return new URI(value);
      }
    }
    
    static class ISO8601DateConverter implements CommandLine.ITypeConverter<Date> {
      public Date convert(String value) {
        try {
          return (new SimpleDateFormat("yyyy-MM-dd")).parse(value);
        } catch (ParseException e) {
          throw new CommandLine.TypeConversionException("'" + value + "' is not a yyyy-MM-dd date");
        } 
      }
    }
    
    static class ISO8601TimeConverter implements CommandLine.ITypeConverter<Time> {
      public Time convert(String value) {
        try {
          if (value.length() <= 5)
            return new Time((new SimpleDateFormat("HH:mm")).parse(value).getTime()); 
          if (value.length() <= 8)
            return new Time((new SimpleDateFormat("HH:mm:ss")).parse(value).getTime()); 
          if (value.length() <= 12)
            try {
              return new Time((new SimpleDateFormat("HH:mm:ss.SSS")).parse(value).getTime());
            } catch (ParseException e2) {
              return new Time((new SimpleDateFormat("HH:mm:ss,SSS")).parse(value).getTime());
            }  
        } catch (ParseException parseException) {}
        throw new CommandLine.TypeConversionException("'" + value + "' is not a HH:mm[:ss[.SSS]] time");
      }
    }
    
    static class BigDecimalConverter implements CommandLine.ITypeConverter<BigDecimal> {
      public BigDecimal convert(String value) {
        return new BigDecimal(value);
      }
    }
    
    static class BigIntegerConverter implements CommandLine.ITypeConverter<BigInteger> {
      public BigInteger convert(String value) {
        return new BigInteger(value);
      }
    }
    
    static class CharsetConverter implements CommandLine.ITypeConverter<Charset> {
      public Charset convert(String s) {
        return Charset.forName(s);
      }
    }
    
    static class InetAddressConverter implements CommandLine.ITypeConverter<InetAddress> {
      public InetAddress convert(String s) throws Exception {
        return InetAddress.getByName(s);
      }
    }
    
    static class PatternConverter implements CommandLine.ITypeConverter<Pattern> {
      public Pattern convert(String s) {
        return Pattern.compile(s);
      }
    }
    
    static class UUIDConverter implements CommandLine.ITypeConverter<UUID> {
      public UUID convert(String s) throws Exception {
        return UUID.fromString(s);
      }
    }
  }
  
  public static class Help {
    protected static final String DEFAULT_COMMAND_NAME = "<main class>";
    
    protected static final String DEFAULT_SEPARATOR = "=";
    
    private static final int usageHelpWidth = 80;
    
    private static final int optionsColumnWidth = 29;
    
    private final Object command;
    
    private final Map<String, Help> commands = new LinkedHashMap<>();
    
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
      this(command, defaultColorScheme(ansi));
    }
    
    public Help(Object command, ColorScheme colorScheme) {
      this.command = CommandLine.Assert.notNull(command, "command");
      this.colorScheme = ((ColorScheme)CommandLine.Assert.<ColorScheme>notNull(colorScheme, "colorScheme")).applySystemProperties();
      List<Field> options = new ArrayList<>();
      List<Field> operands = new ArrayList<>();
      Class<?> cls = command.getClass();
      while (cls != null) {
        for (Field field : cls.getDeclaredFields()) {
          field.setAccessible(true);
          if (field.isAnnotationPresent((Class)CommandLine.Option.class)) {
            CommandLine.Option option = field.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
            if (!option.hidden())
              options.add(field); 
          } 
          if (field.isAnnotationPresent((Class)CommandLine.Parameters.class))
            operands.add(field); 
        } 
        if (cls.isAnnotationPresent((Class)CommandLine.Command.class)) {
          CommandLine.Command cmd = cls.<CommandLine.Command>getAnnotation(CommandLine.Command.class);
          if ("<main class>".equals(this.commandName))
            this.commandName = cmd.name(); 
          this.separator = (this.separator == null) ? cmd.separator() : this.separator;
          this.abbreviateSynopsis = Boolean.valueOf((this.abbreviateSynopsis == null) ? cmd.abbreviateSynopsis() : this.abbreviateSynopsis.booleanValue());
          this.sortOptions = Boolean.valueOf((this.sortOptions == null) ? cmd.sortOptions() : this.sortOptions.booleanValue());
          this.requiredOptionMarker = Character.valueOf((this.requiredOptionMarker == null) ? cmd.requiredOptionMarker() : this.requiredOptionMarker.charValue());
          this.showDefaultValues = Boolean.valueOf((this.showDefaultValues == null) ? cmd.showDefaultValues() : this.showDefaultValues.booleanValue());
          this.customSynopsis = CommandLine.empty((Object[])this.customSynopsis) ? cmd.customSynopsis() : this.customSynopsis;
          this.description = CommandLine.empty((Object[])this.description) ? cmd.description() : this.description;
          this.header = CommandLine.empty((Object[])this.header) ? cmd.header() : this.header;
          this.footer = CommandLine.empty((Object[])this.footer) ? cmd.footer() : this.footer;
          this.headerHeading = CommandLine.empty(this.headerHeading) ? cmd.headerHeading() : this.headerHeading;
          this.synopsisHeading = (CommandLine.empty(this.synopsisHeading) || "Usage: ".equals(this.synopsisHeading)) ? cmd.synopsisHeading() : this.synopsisHeading;
          this.descriptionHeading = CommandLine.empty(this.descriptionHeading) ? cmd.descriptionHeading() : this.descriptionHeading;
          this.parameterListHeading = CommandLine.empty(this.parameterListHeading) ? cmd.parameterListHeading() : this.parameterListHeading;
          this.optionListHeading = CommandLine.empty(this.optionListHeading) ? cmd.optionListHeading() : this.optionListHeading;
          this.commandListHeading = (CommandLine.empty(this.commandListHeading) || "Commands:%n".equals(this.commandListHeading)) ? cmd.commandListHeading() : this.commandListHeading;
          this.footerHeading = CommandLine.empty(this.footerHeading) ? cmd.footerHeading() : this.footerHeading;
        } 
        cls = cls.getSuperclass();
      } 
      this.sortOptions = Boolean.valueOf((this.sortOptions == null) ? true : this.sortOptions.booleanValue());
      this.abbreviateSynopsis = Boolean.valueOf((this.abbreviateSynopsis == null) ? false : this.abbreviateSynopsis.booleanValue());
      this.requiredOptionMarker = Character.valueOf((this.requiredOptionMarker == null) ? 32 : this.requiredOptionMarker.charValue());
      this.showDefaultValues = Boolean.valueOf((this.showDefaultValues == null) ? false : this.showDefaultValues.booleanValue());
      this.synopsisHeading = (this.synopsisHeading == null) ? "Usage: " : this.synopsisHeading;
      this.commandListHeading = (this.commandListHeading == null) ? "Commands:%n" : this.commandListHeading;
      this.separator = (this.separator == null) ? "=" : this.separator;
      this.parameterLabelRenderer = createDefaultParamLabelRenderer();
      Collections.sort(operands, new CommandLine.PositionalParametersSorter());
      this.positionalParametersFields = Collections.unmodifiableList(operands);
      this.optionFields = Collections.unmodifiableList(options);
    }
    
    public Help addAllSubcommands(Map<String, CommandLine> commands) {
      if (commands != null)
        for (Map.Entry<String, CommandLine> entry : commands.entrySet())
          addSubcommand(entry.getKey(), ((CommandLine)entry.getValue()).getCommand());  
      return this;
    }
    
    public Help addSubcommand(String commandName, Object command) {
      this.commands.put(commandName, new Help(command));
      return this;
    }
    
    @Deprecated
    public String synopsis() {
      return synopsis(0);
    }
    
    public String synopsis(int synopsisHeadingLength) {
      if (!CommandLine.empty((Object[])this.customSynopsis))
        return customSynopsis(new Object[0]); 
      return this.abbreviateSynopsis.booleanValue() ? abbreviatedSynopsis() : 
        detailedSynopsis(synopsisHeadingLength, createShortOptionArityAndNameComparator(), true);
    }
    
    public String abbreviatedSynopsis() {
      StringBuilder sb = new StringBuilder();
      if (!this.optionFields.isEmpty())
        sb.append(" [OPTIONS]"); 
      for (Field positionalParam : this.positionalParametersFields) {
        if (!((CommandLine.Parameters)positionalParam.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).hidden())
          sb.append(' ').append(this.parameterLabelRenderer.renderParameterLabel(positionalParam, ansi(), this.colorScheme.parameterStyles)); 
      } 
      return this.colorScheme.commandText(this.commandName).toString() + sb
        .toString() + System.getProperty("line.separator");
    }
    
    @Deprecated
    public String detailedSynopsis(Comparator<Field> optionSort, boolean clusterBooleanOptions) {
      return detailedSynopsis(0, optionSort, clusterBooleanOptions);
    }
    
    public String detailedSynopsis(int synopsisHeadingLength, Comparator<Field> optionSort, boolean clusterBooleanOptions) {
      ansi().getClass();
      Ansi.Text optionText = new Ansi.Text(0);
      List<Field> fields = new ArrayList<>(this.optionFields);
      if (optionSort != null)
        Collections.sort(fields, optionSort); 
      if (clusterBooleanOptions) {
        List<Field> booleanOptions = new ArrayList<>();
        StringBuilder clusteredRequired = new StringBuilder("-");
        StringBuilder clusteredOptional = new StringBuilder("-");
        for (Field field : fields) {
          if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            CommandLine.Option option = field.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
            String shortestName = ShortestFirst.sort(option.names())[0];
            if (shortestName.length() == 2 && shortestName.startsWith("-")) {
              booleanOptions.add(field);
              if (option.required()) {
                clusteredRequired.append(shortestName.substring(1));
                continue;
              } 
              clusteredOptional.append(shortestName.substring(1));
            } 
          } 
        } 
        fields.removeAll(booleanOptions);
        if (clusteredRequired.length() > 1)
          optionText = optionText.append(" ").append(this.colorScheme.optionText(clusteredRequired.toString())); 
        if (clusteredOptional.length() > 1)
          optionText = optionText.append(" [").append(this.colorScheme.optionText(clusteredOptional.toString())).append("]"); 
      } 
      for (Field field : fields) {
        CommandLine.Option option = field.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
        if (!option.hidden()) {
          if (option.required()) {
            optionText = appendOptionSynopsis(optionText, field, ShortestFirst.sort(option.names())[0], " ", "");
            if (CommandLine.isMultiValue(field))
              optionText = appendOptionSynopsis(optionText, field, ShortestFirst.sort(option.names())[0], " [", "]..."); 
            continue;
          } 
          optionText = appendOptionSynopsis(optionText, field, ShortestFirst.sort(option.names())[0], " [", "]");
          if (CommandLine.isMultiValue(field))
            optionText = optionText.append("..."); 
        } 
      } 
      for (Field positionalParam : this.positionalParametersFields) {
        if (!((CommandLine.Parameters)positionalParam.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).hidden()) {
          optionText = optionText.append(" ");
          Ansi.Text label = this.parameterLabelRenderer.renderParameterLabel(positionalParam, this.colorScheme.ansi(), this.colorScheme.parameterStyles);
          optionText = optionText.append(label);
        } 
      } 
      int firstColumnLength = this.commandName.length() + synopsisHeadingLength;
      TextTable textTable = new TextTable(ansi(), new int[] { firstColumnLength, 80 - firstColumnLength });
      textTable.indentWrappedLines = 1;
      Ansi.OFF.getClass();
      Ansi.Text PADDING = new Ansi.Text(stringOf('X', synopsisHeadingLength));
      textTable.addRowValues(new Ansi.Text[] { PADDING.append(this.colorScheme.commandText(this.commandName)), optionText });
      return textTable.toString().substring(synopsisHeadingLength);
    }
    
    private Ansi.Text appendOptionSynopsis(Ansi.Text optionText, Field field, String optionName, String prefix, String suffix) {
      Ansi.Text optionParamText = this.parameterLabelRenderer.renderParameterLabel(field, this.colorScheme.ansi(), this.colorScheme.optionParamStyles);
      return optionText.append(prefix)
        .append(this.colorScheme.optionText(optionName))
        .append(optionParamText)
        .append(suffix);
    }
    
    public int synopsisHeadingLength() {
      Ansi.OFF.getClass();
      String[] lines = (new Ansi.Text(this.synopsisHeading)).toString().split("\\r?\\n|\\r|%n", -1);
      return lines[lines.length - 1].length();
    }
    
    public String optionList() {
      Comparator<Field> sortOrder = (this.sortOptions == null || this.sortOptions.booleanValue()) ? createShortOptionNameComparator() : null;
      return optionList(createDefaultLayout(), sortOrder, this.parameterLabelRenderer);
    }
    
    public String optionList(Layout layout, Comparator<Field> optionSort, IParamLabelRenderer valueLabelRenderer) {
      List<Field> fields = new ArrayList<>(this.optionFields);
      if (optionSort != null)
        Collections.sort(fields, optionSort); 
      layout.addOptions(fields, valueLabelRenderer);
      return layout.toString();
    }
    
    public String parameterList() {
      return parameterList(createDefaultLayout(), this.parameterLabelRenderer);
    }
    
    public String parameterList(Layout layout, IParamLabelRenderer paramLabelRenderer) {
      layout.addPositionalParameters(this.positionalParametersFields, paramLabelRenderer);
      return layout.toString();
    }
    
    private static String heading(Ansi ansi, String values, Object... params) {
      StringBuilder sb = join(ansi, new String[] { values }, new StringBuilder(), params);
      String result = sb.toString();
      result = result.endsWith(System.getProperty("line.separator")) ? result.substring(0, result.length() - System.getProperty("line.separator").length()) : result;
      return result + new String(spaces(countTrailingSpaces(values)));
    }
    
    private static char[] spaces(int length) {
      char[] result = new char[length];
      Arrays.fill(result, ' ');
      return result;
    }
    
    private static int countTrailingSpaces(String str) {
      if (str == null)
        return 0; 
      int trailingSpaces = 0;
      for (int i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; ) {
        trailingSpaces++;
        i--;
      } 
      return trailingSpaces;
    }
    
    public static StringBuilder join(Ansi ansi, String[] values, StringBuilder sb, Object... params) {
      if (values != null) {
        TextTable table = new TextTable(ansi, new int[] { 80 });
        table.indentWrappedLines = 0;
        for (String summaryLine : values) {
          ansi.getClass();
          Ansi.Text[] lines = (new Ansi.Text(format(summaryLine, params))).splitLines();
          for (Ansi.Text line : lines) {
            table.addRowValues(new Ansi.Text[] { line });
          } 
        } 
        table.toString(sb);
      } 
      return sb;
    }
    
    private static String format(String formatString, Object... params) {
      return (formatString == null) ? "" : String.format(formatString, params);
    }
    
    public String customSynopsis(Object... params) {
      return join(ansi(), this.customSynopsis, new StringBuilder(), params).toString();
    }
    
    public String description(Object... params) {
      return join(ansi(), this.description, new StringBuilder(), params).toString();
    }
    
    public String header(Object... params) {
      return join(ansi(), this.header, new StringBuilder(), params).toString();
    }
    
    public String footer(Object... params) {
      return join(ansi(), this.footer, new StringBuilder(), params).toString();
    }
    
    public String headerHeading(Object... params) {
      return heading(ansi(), this.headerHeading, params);
    }
    
    public String synopsisHeading(Object... params) {
      return heading(ansi(), this.synopsisHeading, params);
    }
    
    public String descriptionHeading(Object... params) {
      return CommandLine.empty(this.descriptionHeading) ? "" : heading(ansi(), this.descriptionHeading, params);
    }
    
    public String parameterListHeading(Object... params) {
      return this.positionalParametersFields.isEmpty() ? "" : heading(ansi(), this.parameterListHeading, params);
    }
    
    public String optionListHeading(Object... params) {
      return this.optionFields.isEmpty() ? "" : heading(ansi(), this.optionListHeading, params);
    }
    
    public String commandListHeading(Object... params) {
      return this.commands.isEmpty() ? "" : heading(ansi(), this.commandListHeading, params);
    }
    
    public String footerHeading(Object... params) {
      return heading(ansi(), this.footerHeading, params);
    }
    
    public String commandList() {
      if (this.commands.isEmpty())
        return ""; 
      int commandLength = maxLength(this.commands.keySet());
      TextTable textTable = new TextTable(ansi(), new Column[] { new Column(commandLength + 2, 2, Column.Overflow.SPAN), new Column(80 - commandLength + 2, 2, Column.Overflow.WRAP) });
      for (Map.Entry<String, Help> entry : this.commands.entrySet()) {
        Help command = entry.getValue();
        String header = (command.header != null && command.header.length > 0) ? command.header[0] : ((command.description != null && command.description.length > 0) ? command.description[0] : "");
        (new Ansi.Text[2])[0] = this.colorScheme.commandText(entry.getKey());
        ansi().getClass();
        textTable.addRowValues(new Ansi.Text[] { null, new Ansi.Text(header) });
      } 
      return textTable.toString();
    }
    
    private static int maxLength(Collection<String> any) {
      List<String> strings = new ArrayList<>(any);
      Collections.sort(strings, Collections.reverseOrder(shortestFirst()));
      return ((String)strings.get(0)).length();
    }
    
    private static String join(String[] names, int offset, int length, String separator) {
      if (names == null)
        return ""; 
      StringBuilder result = new StringBuilder();
      for (int i = offset; i < offset + length; i++)
        result.append((i > offset) ? separator : "").append(names[i]); 
      return result.toString();
    }
    
    private static String stringOf(char chr, int length) {
      char[] buff = new char[length];
      Arrays.fill(buff, chr);
      return new String(buff);
    }
    
    public Layout createDefaultLayout() {
      return new Layout(this.colorScheme, new TextTable(this.colorScheme.ansi()), createDefaultOptionRenderer(), createDefaultParameterRenderer());
    }
    
    public IOptionRenderer createDefaultOptionRenderer() {
      DefaultOptionRenderer result = new DefaultOptionRenderer();
      result.requiredMarker = String.valueOf(this.requiredOptionMarker);
      if (this.showDefaultValues != null && this.showDefaultValues.booleanValue())
        result.command = this.command; 
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
      return new IParamLabelRenderer() {
          public CommandLine.Help.Ansi.Text renderParameterLabel(Field field, CommandLine.Help.Ansi ansi, List<CommandLine.Help.Ansi.IStyle> styles) {
            String text = CommandLine.Help.DefaultParamLabelRenderer.renderParameterName(field);
            return ansi.apply(text, styles);
          }
          
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
    
    static class DefaultOptionRenderer implements IOptionRenderer {
      public String requiredMarker = " ";
      
      public Object command;
      
      private String sep;
      
      private boolean showDefault;
      
      public CommandLine.Help.Ansi.Text[][] render(CommandLine.Option option, Field field, CommandLine.Help.IParamLabelRenderer paramLabelRenderer, CommandLine.Help.ColorScheme scheme) {
        String[] names = CommandLine.Help.ShortestFirst.sort(option.names());
        int shortOptionCount = (names[0].length() == 2) ? 1 : 0;
        String shortOption = (shortOptionCount > 0) ? names[0] : "";
        this.sep = (shortOptionCount > 0 && names.length > 1) ? "," : "";
        String longOption = CommandLine.Help.join(names, shortOptionCount, names.length - shortOptionCount, ", ");
        CommandLine.Help.Ansi.Text longOptionText = createLongOptionText(field, paramLabelRenderer, scheme, longOption);
        this.showDefault = (this.command != null && !option.help() && !CommandLine.isBoolean(field.getType()));
        Object defaultValue = createDefaultValue(field);
        String requiredOption = option.required() ? this.requiredMarker : "";
        return renderDescriptionLines(option, scheme, requiredOption, shortOption, longOptionText, defaultValue);
      }
      
      private Object createDefaultValue(Field field) {
        Object defaultValue = null;
        try {
          defaultValue = field.get(this.command);
          if (defaultValue == null) {
            this.showDefault = false;
          } else if (field.getType().isArray()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Array.getLength(defaultValue); i++)
              sb.append((i > 0) ? ", " : "").append(Array.get(defaultValue, i)); 
            defaultValue = sb.insert(0, "[").append("]").toString();
          } 
        } catch (Exception ex) {
          this.showDefault = false;
        } 
        return defaultValue;
      }
      
      private CommandLine.Help.Ansi.Text createLongOptionText(Field field, CommandLine.Help.IParamLabelRenderer renderer, CommandLine.Help.ColorScheme scheme, String longOption) {
        CommandLine.Help.Ansi.Text paramLabelText = renderer.renderParameterLabel(field, scheme.ansi(), scheme.optionParamStyles);
        if (paramLabelText.length > 0 && longOption.length() == 0) {
          this.sep = renderer.separator();
          int sepStart = paramLabelText.plainString().indexOf(this.sep);
          CommandLine.Help.Ansi.Text prefix = paramLabelText.substring(0, sepStart);
          paramLabelText = prefix.append(paramLabelText.substring(sepStart + this.sep.length()));
        } 
        CommandLine.Help.Ansi.Text longOptionText = scheme.optionText(longOption);
        longOptionText = longOptionText.append(paramLabelText);
        return longOptionText;
      }
      
      private CommandLine.Help.Ansi.Text[][] renderDescriptionLines(CommandLine.Option option, CommandLine.Help.ColorScheme scheme, String requiredOption, String shortOption, CommandLine.Help.Ansi.Text longOptionText, Object defaultValue) {
        CommandLine.Help.Ansi.Text EMPTY = CommandLine.Help.Ansi.EMPTY_TEXT;
        List<CommandLine.Help.Ansi.Text[]> result = (List)new ArrayList<>();
        scheme.ansi().getClass();
        CommandLine.Help.Ansi.Text[] descriptionFirstLines = (new CommandLine.Help.Ansi.Text(CommandLine.str(option.description(), 0))).splitLines();
        if (descriptionFirstLines.length == 0)
          if (this.showDefault) {
            scheme.ansi().getClass();
            descriptionFirstLines = new CommandLine.Help.Ansi.Text[] { new CommandLine.Help.Ansi.Text("  Default: " + defaultValue) };
            this.showDefault = false;
          } else {
            descriptionFirstLines = new CommandLine.Help.Ansi.Text[] { EMPTY };
          }  
        (new CommandLine.Help.Ansi.Text[5])[0] = scheme.optionText(requiredOption);
        (new CommandLine.Help.Ansi.Text[5])[1] = scheme.optionText(shortOption);
        scheme
          .ansi().getClass();
        result.add(new CommandLine.Help.Ansi.Text[] { null, null, new CommandLine.Help.Ansi.Text(this.sep), longOptionText, descriptionFirstLines[0] });
        int i;
        for (i = 1; i < descriptionFirstLines.length; i++) {
          result.add(new CommandLine.Help.Ansi.Text[] { EMPTY, EMPTY, EMPTY, EMPTY, descriptionFirstLines[i] });
        } 
        for (i = 1; i < (option.description()).length; i++) {
          scheme.ansi().getClass();
          CommandLine.Help.Ansi.Text[] descriptionNextLines = (new CommandLine.Help.Ansi.Text(option.description()[i])).splitLines();
          for (CommandLine.Help.Ansi.Text line : descriptionNextLines) {
            result.add(new CommandLine.Help.Ansi.Text[] { EMPTY, EMPTY, EMPTY, EMPTY, line });
          } 
        } 
        if (this.showDefault) {
          (new CommandLine.Help.Ansi.Text[5])[0] = EMPTY;
          (new CommandLine.Help.Ansi.Text[5])[1] = EMPTY;
          (new CommandLine.Help.Ansi.Text[5])[2] = EMPTY;
          (new CommandLine.Help.Ansi.Text[5])[3] = EMPTY;
          scheme.ansi().getClass();
          result.add(new CommandLine.Help.Ansi.Text[] { null, null, null, null, new CommandLine.Help.Ansi.Text("  Default: " + defaultValue) });
        } 
        return result.<CommandLine.Help.Ansi.Text[]>toArray(new CommandLine.Help.Ansi.Text[result.size()][]);
      }
    }
    
    static class MinimalOptionRenderer implements IOptionRenderer {
      public CommandLine.Help.Ansi.Text[][] render(CommandLine.Option option, Field field, CommandLine.Help.IParamLabelRenderer parameterLabelRenderer, CommandLine.Help.ColorScheme scheme) {
        CommandLine.Help.Ansi.Text optionText = scheme.optionText(option.names()[0]);
        CommandLine.Help.Ansi.Text paramLabelText = parameterLabelRenderer.renderParameterLabel(field, scheme.ansi(), scheme.optionParamStyles);
        optionText = optionText.append(paramLabelText);
        (new CommandLine.Help.Ansi.Text[2])[0] = optionText;
        scheme
          .ansi().getClass();
        return new CommandLine.Help.Ansi.Text[][] { { null, new CommandLine.Help.Ansi.Text(((option.description()).length == 0) ? "" : option.description()[0]) } };
      }
    }
    
    static class MinimalParameterRenderer implements IParameterRenderer {
      public CommandLine.Help.Ansi.Text[][] render(CommandLine.Parameters param, Field field, CommandLine.Help.IParamLabelRenderer parameterLabelRenderer, CommandLine.Help.ColorScheme scheme) {
        (new CommandLine.Help.Ansi.Text[2])[0] = parameterLabelRenderer.renderParameterLabel(field, scheme.ansi(), scheme.parameterStyles);
        scheme
          .ansi().getClass();
        return new CommandLine.Help.Ansi.Text[][] { { null, new CommandLine.Help.Ansi.Text(((param.description()).length == 0) ? "" : param.description()[0]) } };
      }
    }
    
    static class DefaultParameterRenderer implements IParameterRenderer {
      public String requiredMarker = " ";
      
      public CommandLine.Help.Ansi.Text[][] render(CommandLine.Parameters params, Field field, CommandLine.Help.IParamLabelRenderer paramLabelRenderer, CommandLine.Help.ColorScheme scheme) {
        CommandLine.Help.Ansi.Text label = paramLabelRenderer.renderParameterLabel(field, scheme.ansi(), scheme.parameterStyles);
        CommandLine.Help.Ansi.Text requiredParameter = scheme.parameterText(((CommandLine.Range.parameterArity(field)).min > 0) ? this.requiredMarker : "");
        CommandLine.Help.Ansi.Text EMPTY = CommandLine.Help.Ansi.EMPTY_TEXT;
        List<CommandLine.Help.Ansi.Text[]> result = (List)new ArrayList<>();
        scheme.ansi().getClass();
        CommandLine.Help.Ansi.Text[] descriptionFirstLines = (new CommandLine.Help.Ansi.Text(CommandLine.str(params.description(), 0))).splitLines();
        if (descriptionFirstLines.length == 0)
          descriptionFirstLines = new CommandLine.Help.Ansi.Text[] { EMPTY }; 
        result.add(new CommandLine.Help.Ansi.Text[] { requiredParameter, EMPTY, EMPTY, label, descriptionFirstLines[0] });
        int i;
        for (i = 1; i < descriptionFirstLines.length; i++) {
          result.add(new CommandLine.Help.Ansi.Text[] { EMPTY, EMPTY, EMPTY, EMPTY, descriptionFirstLines[i] });
        } 
        for (i = 1; i < (params.description()).length; i++) {
          scheme.ansi().getClass();
          CommandLine.Help.Ansi.Text[] descriptionNextLines = (new CommandLine.Help.Ansi.Text(params.description()[i])).splitLines();
          for (CommandLine.Help.Ansi.Text line : descriptionNextLines) {
            result.add(new CommandLine.Help.Ansi.Text[] { EMPTY, EMPTY, EMPTY, EMPTY, line });
          } 
        } 
        return result.<CommandLine.Help.Ansi.Text[]>toArray(new CommandLine.Help.Ansi.Text[result.size()][]);
      }
    }
    
    static class DefaultParamLabelRenderer implements IParamLabelRenderer {
      public final String separator;
      
      public DefaultParamLabelRenderer(String separator) {
        this.separator = CommandLine.Assert.<String>notNull(separator, "separator");
      }
      
      public String separator() {
        return this.separator;
      }
      
      public CommandLine.Help.Ansi.Text renderParameterLabel(Field field, CommandLine.Help.Ansi ansi, List<CommandLine.Help.Ansi.IStyle> styles) {
        boolean isOptionParameter = field.isAnnotationPresent((Class)CommandLine.Option.class);
        CommandLine.Range arity = isOptionParameter ? CommandLine.Range.optionArity(field) : CommandLine.Range.parameterCapacity(field);
        String split = isOptionParameter ? ((CommandLine.Option)field.<CommandLine.Option>getAnnotation(CommandLine.Option.class)).split() : ((CommandLine.Parameters)field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).split();
        ansi.getClass();
        CommandLine.Help.Ansi.Text result = new CommandLine.Help.Ansi.Text("");
        String sep = isOptionParameter ? this.separator : "";
        CommandLine.Help.Ansi.Text paramName = ansi.apply(renderParameterName(field), styles);
        if (!CommandLine.empty(split))
          paramName = paramName.append("[" + split).append(paramName).append("]..."); 
        int i;
        for (i = 0; i < arity.min; i++) {
          result = result.append(sep).append(paramName);
          sep = " ";
        } 
        if (arity.isVariable) {
          if (result.length == 0) {
            result = result.append(sep + "[").append(paramName).append("]...");
          } else if (!result.plainString().endsWith("...")) {
            result = result.append("...");
          } 
        } else {
          sep = (result.length == 0) ? (isOptionParameter ? this.separator : "") : " ";
          for (i = arity.min; i < arity.max; i++) {
            if (sep.trim().length() == 0) {
              result = result.append(sep + "[").append(paramName);
            } else {
              result = result.append("[" + sep).append(paramName);
            } 
            sep = " ";
          } 
          for (i = arity.min; i < arity.max; ) {
            result = result.append("]");
            i++;
          } 
        } 
        return result;
      }
      
      private static String renderParameterName(Field field) {
        String result = null;
        if (field.isAnnotationPresent((Class)CommandLine.Option.class)) {
          result = ((CommandLine.Option)field.<CommandLine.Option>getAnnotation(CommandLine.Option.class)).paramLabel();
        } else if (field.isAnnotationPresent((Class)CommandLine.Parameters.class)) {
          result = ((CommandLine.Parameters)field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).paramLabel();
        } 
        if (result != null && result.trim().length() > 0)
          return result.trim(); 
        String name = field.getName();
        if (Map.class.isAssignableFrom(field.getType())) {
          Class<?>[] paramTypes = CommandLine.getTypeAttribute(field);
          if (paramTypes.length < 2 || paramTypes[0] == null || paramTypes[1] == null) {
            name = "String=String";
          } else {
            name = paramTypes[0].getSimpleName() + "=" + paramTypes[1].getSimpleName();
          } 
        } 
        return "<" + name + ">";
      }
    }
    
    public static class Layout {
      protected final CommandLine.Help.ColorScheme colorScheme;
      
      protected final CommandLine.Help.TextTable table;
      
      protected CommandLine.Help.IOptionRenderer optionRenderer;
      
      protected CommandLine.Help.IParameterRenderer parameterRenderer;
      
      public Layout(CommandLine.Help.ColorScheme colorScheme) {
        this(colorScheme, new CommandLine.Help.TextTable(colorScheme.ansi()));
      }
      
      public Layout(CommandLine.Help.ColorScheme colorScheme, CommandLine.Help.TextTable textTable) {
        this(colorScheme, textTable, new CommandLine.Help.DefaultOptionRenderer(), new CommandLine.Help.DefaultParameterRenderer());
      }
      
      public Layout(CommandLine.Help.ColorScheme colorScheme, CommandLine.Help.TextTable textTable, CommandLine.Help.IOptionRenderer optionRenderer, CommandLine.Help.IParameterRenderer parameterRenderer) {
        this.colorScheme = CommandLine.Assert.<CommandLine.Help.ColorScheme>notNull(colorScheme, "colorScheme");
        this.table = CommandLine.Assert.<CommandLine.Help.TextTable>notNull(textTable, "textTable");
        this.optionRenderer = CommandLine.Assert.<CommandLine.Help.IOptionRenderer>notNull(optionRenderer, "optionRenderer");
        this.parameterRenderer = CommandLine.Assert.<CommandLine.Help.IParameterRenderer>notNull(parameterRenderer, "parameterRenderer");
      }
      
      public void layout(Field field, CommandLine.Help.Ansi.Text[][] cellValues) {
        for (CommandLine.Help.Ansi.Text[] oneRow : cellValues)
          this.table.addRowValues(oneRow); 
      }
      
      public void addOptions(List<Field> fields, CommandLine.Help.IParamLabelRenderer paramLabelRenderer) {
        for (Field field : fields) {
          CommandLine.Option option = field.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
          if (!option.hidden())
            addOption(field, paramLabelRenderer); 
        } 
      }
      
      public void addOption(Field field, CommandLine.Help.IParamLabelRenderer paramLabelRenderer) {
        CommandLine.Option option = field.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
        CommandLine.Help.Ansi.Text[][] values = this.optionRenderer.render(option, field, paramLabelRenderer, this.colorScheme);
        layout(field, values);
      }
      
      public void addPositionalParameters(List<Field> fields, CommandLine.Help.IParamLabelRenderer paramLabelRenderer) {
        for (Field field : fields) {
          CommandLine.Parameters parameters = field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class);
          if (!parameters.hidden())
            addPositionalParameter(field, paramLabelRenderer); 
        } 
      }
      
      public void addPositionalParameter(Field field, CommandLine.Help.IParamLabelRenderer paramLabelRenderer) {
        CommandLine.Parameters option = field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class);
        CommandLine.Help.Ansi.Text[][] values = this.parameterRenderer.render(option, field, paramLabelRenderer, this.colorScheme);
        layout(field, values);
      }
      
      public String toString() {
        return this.table.toString();
      }
    }
    
    static class ShortestFirst implements Comparator<String> {
      public int compare(String o1, String o2) {
        return o1.length() - o2.length();
      }
      
      public static String[] sort(String[] names) {
        Arrays.sort(names, new ShortestFirst());
        return names;
      }
    }
    
    static class SortByShortestOptionNameAlphabetically implements Comparator<Field> {
      public int compare(Field f1, Field f2) {
        CommandLine.Option o1 = f1.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
        CommandLine.Option o2 = f2.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
        if (o1 == null)
          return 1; 
        if (o2 == null)
          return -1; 
        String[] names1 = CommandLine.Help.ShortestFirst.sort(o1.names());
        String[] names2 = CommandLine.Help.ShortestFirst.sort(o2.names());
        int result = names1[0].toUpperCase().compareTo(names2[0].toUpperCase());
        result = (result == 0) ? -names1[0].compareTo(names2[0]) : result;
        return (o1.help() == o2.help()) ? result : (o2.help() ? -1 : 1);
      }
    }
    
    static class SortByOptionArityAndNameAlphabetically extends SortByShortestOptionNameAlphabetically {
      public int compare(Field f1, Field f2) {
        CommandLine.Option o1 = f1.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
        CommandLine.Option o2 = f2.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
        CommandLine.Range arity1 = CommandLine.Range.optionArity(f1);
        CommandLine.Range arity2 = CommandLine.Range.optionArity(f2);
        int result = arity1.max - arity2.max;
        if (result == 0)
          result = arity1.min - arity2.min; 
        if (result == 0) {
          if (CommandLine.isMultiValue(f1) && !CommandLine.isMultiValue(f2))
            result = 1; 
          if (!CommandLine.isMultiValue(f1) && CommandLine.isMultiValue(f2))
            result = -1; 
        } 
        return (result == 0) ? super.compare(f1, f2) : result;
      }
    }
    
    public static class TextTable {
      public final CommandLine.Help.Column[] columns;
      
      public static class Cell {
        public final int column;
        
        public final int row;
        
        public Cell(int column, int row) {
          this.column = column;
          this.row = row;
        }
      }
      
      protected final List<CommandLine.Help.Ansi.Text> columnValues = new ArrayList<>();
      
      public int indentWrappedLines = 2;
      
      private final CommandLine.Help.Ansi ansi;
      
      public TextTable(CommandLine.Help.Ansi ansi) {
        this(ansi, new CommandLine.Help.Column[] { new CommandLine.Help.Column(2, 0, CommandLine.Help.Column.Overflow.TRUNCATE), new CommandLine.Help.Column(2, 0, CommandLine.Help.Column.Overflow.TRUNCATE), new CommandLine.Help.Column(1, 0, CommandLine.Help.Column.Overflow.TRUNCATE), new CommandLine.Help.Column(24, 1, CommandLine.Help.Column.Overflow.SPAN), new CommandLine.Help.Column(51, 1, CommandLine.Help.Column.Overflow.WRAP) });
      }
      
      public TextTable(CommandLine.Help.Ansi ansi, int... columnWidths) {
        this.ansi = CommandLine.Assert.<CommandLine.Help.Ansi>notNull(ansi, "ansi");
        this.columns = new CommandLine.Help.Column[columnWidths.length];
        for (int i = 0; i < columnWidths.length; i++)
          this.columns[i] = new CommandLine.Help.Column(columnWidths[i], 0, (i == columnWidths.length - 1) ? CommandLine.Help.Column.Overflow.SPAN : CommandLine.Help.Column.Overflow.WRAP); 
      }
      
      public TextTable(CommandLine.Help.Ansi ansi, CommandLine.Help.Column... columns) {
        this.ansi = CommandLine.Assert.<CommandLine.Help.Ansi>notNull(ansi, "ansi");
        this.columns = CommandLine.Assert.<CommandLine.Help.Column[]>notNull(columns, "columns");
        if (columns.length == 0)
          throw new IllegalArgumentException("At least one column is required"); 
      }
      
      public CommandLine.Help.Ansi.Text textAt(int row, int col) {
        return this.columnValues.get(col + row * this.columns.length);
      }
      
      @Deprecated
      public CommandLine.Help.Ansi.Text cellAt(int row, int col) {
        return textAt(row, col);
      }
      
      public int rowCount() {
        return this.columnValues.size() / this.columns.length;
      }
      
      public void addEmptyRow() {
        for (int i = 0; i < this.columns.length; i++) {
          this.ansi.getClass();
          this.columnValues.add(new CommandLine.Help.Ansi.Text((this.columns[i]).width));
        } 
      }
      
      public void addRowValues(String... values) {
        CommandLine.Help.Ansi.Text[] array = new CommandLine.Help.Ansi.Text[values.length];
        for (int i = 0; i < array.length; i++) {
          this.ansi.getClass();
          array[i] = (values[i] == null) ? CommandLine.Help.Ansi.EMPTY_TEXT : new CommandLine.Help.Ansi.Text(values[i]);
        } 
        addRowValues(array);
      }
      
      public void addRowValues(CommandLine.Help.Ansi.Text... values) {
        if (values.length > this.columns.length)
          throw new IllegalArgumentException(values.length + " values don't fit in " + this.columns.length + " columns"); 
        addEmptyRow();
        for (int col = 0; col < values.length; col++) {
          int row = rowCount() - 1;
          Cell cell = putValue(row, col, values[col]);
          if ((cell.row != row || cell.column != col) && col != values.length - 1)
            addEmptyRow(); 
        } 
      }
      
      public Cell putValue(int row, int col, CommandLine.Help.Ansi.Text value) {
        int startColumn;
        BreakIterator lineBreakIterator;
        if (row > rowCount() - 1)
          throw new IllegalArgumentException("Cannot write to row " + row + ": rowCount=" + rowCount()); 
        if (value == null || value.plain.length() == 0)
          return new Cell(col, row); 
        CommandLine.Help.Column column = this.columns[col];
        int indent = column.indent;
        switch (column.overflow) {
          case TRUNCATE:
            copy(value, textAt(row, col), indent);
            return new Cell(col, row);
          case SPAN:
            startColumn = col;
            while (true) {
              boolean lastColumn = (col == this.columns.length - 1);
              int charsWritten = lastColumn ? copy(BreakIterator.getLineInstance(), value, textAt(row, col), indent) : copy(value, textAt(row, col), indent);
              value = value.substring(charsWritten);
              indent = 0;
              if (value.length > 0)
                col++; 
              if (value.length > 0 && col >= this.columns.length) {
                addEmptyRow();
                row++;
                col = startColumn;
                indent = column.indent + this.indentWrappedLines;
              } 
              if (value.length <= 0)
                return new Cell(col, row); 
            } 
          case WRAP:
            lineBreakIterator = BreakIterator.getLineInstance();
            while (true) {
              int charsWritten = copy(lineBreakIterator, value, textAt(row, col), indent);
              value = value.substring(charsWritten);
              indent = column.indent + this.indentWrappedLines;
              if (value.length > 0) {
                row++;
                addEmptyRow();
              } 
              if (value.length <= 0)
                return new Cell(col, row); 
            } 
        } 
        throw new IllegalStateException(column.overflow.toString());
      }
      
      private static int length(CommandLine.Help.Ansi.Text str) {
        return str.length;
      }
      
      private int copy(BreakIterator line, CommandLine.Help.Ansi.Text text, CommandLine.Help.Ansi.Text columnValue, int offset) {
        line.setText(text.plainString().replace("-", "ÿ"));
        int done = 0;
        int end;
        for (int start = line.first(); end != -1; ) {
          CommandLine.Help.Ansi.Text word = text.substring(start, end);
          if (columnValue.maxLength >= offset + done + length(word)) {
            done += copy(word, columnValue, offset + done);
            start = end;
            end = line.next();
          } 
        } 
        if (done == 0 && length(text) > columnValue.maxLength)
          done = copy(text, columnValue, offset); 
        return done;
      }
      
      private static int copy(CommandLine.Help.Ansi.Text value, CommandLine.Help.Ansi.Text destination, int offset) {
        int length = Math.min(value.length, destination.maxLength - offset);
        value.getStyledChars(value.from, length, destination, offset);
        return length;
      }
      
      public StringBuilder toString(StringBuilder text) {
        int columnCount = this.columns.length;
        StringBuilder row = new StringBuilder(80);
        for (int i = 0; i < this.columnValues.size(); i++) {
          CommandLine.Help.Ansi.Text column = this.columnValues.get(i);
          row.append(column.toString());
          row.append(new String(CommandLine.Help.spaces((this.columns[i % columnCount]).width - column.length)));
          if (i % columnCount == columnCount - 1) {
            int lastChar = row.length() - 1;
            for (; lastChar >= 0 && row.charAt(lastChar) == ' '; lastChar--);
            row.setLength(lastChar + 1);
            text.append(row.toString()).append(System.getProperty("line.separator"));
            row.setLength(0);
          } 
        } 
        return text;
      }
      
      public String toString() {
        return toString(new StringBuilder()).toString();
      }
    }
    
    public static class Column {
      public final int width;
      
      public final int indent;
      
      public final Overflow overflow;
      
      public enum Overflow {
        TRUNCATE, SPAN, WRAP;
      }
      
      public Column(int width, int indent, Overflow overflow) {
        this.width = width;
        this.indent = indent;
        this.overflow = CommandLine.Assert.<Overflow>notNull(overflow, "overflow");
      }
    }
    
    public enum Overflow {
      TRUNCATE, SPAN, WRAP;
    }
    
    public static class ColorScheme {
      public final List<CommandLine.Help.Ansi.IStyle> commandStyles = new ArrayList<>();
      
      public final List<CommandLine.Help.Ansi.IStyle> optionStyles = new ArrayList<>();
      
      public final List<CommandLine.Help.Ansi.IStyle> parameterStyles = new ArrayList<>();
      
      public final List<CommandLine.Help.Ansi.IStyle> optionParamStyles = new ArrayList<>();
      
      private final CommandLine.Help.Ansi ansi;
      
      public ColorScheme() {
        this(CommandLine.Help.Ansi.AUTO);
      }
      
      public ColorScheme(CommandLine.Help.Ansi ansi) {
        this.ansi = CommandLine.Assert.<CommandLine.Help.Ansi>notNull(ansi, "ansi");
      }
      
      public ColorScheme commands(CommandLine.Help.Ansi.IStyle... styles) {
        return addAll(this.commandStyles, styles);
      }
      
      public ColorScheme options(CommandLine.Help.Ansi.IStyle... styles) {
        return addAll(this.optionStyles, styles);
      }
      
      public ColorScheme parameters(CommandLine.Help.Ansi.IStyle... styles) {
        return addAll(this.parameterStyles, styles);
      }
      
      public ColorScheme optionParams(CommandLine.Help.Ansi.IStyle... styles) {
        return addAll(this.optionParamStyles, styles);
      }
      
      public CommandLine.Help.Ansi.Text commandText(String command) {
        return ansi().apply(command, this.commandStyles);
      }
      
      public CommandLine.Help.Ansi.Text optionText(String option) {
        return ansi().apply(option, this.optionStyles);
      }
      
      public CommandLine.Help.Ansi.Text parameterText(String parameter) {
        return ansi().apply(parameter, this.parameterStyles);
      }
      
      public CommandLine.Help.Ansi.Text optionParamText(String optionParam) {
        return ansi().apply(optionParam, this.optionParamStyles);
      }
      
      public ColorScheme applySystemProperties() {
        replace(this.commandStyles, System.getProperty("picocli.color.commands"));
        replace(this.optionStyles, System.getProperty("picocli.color.options"));
        replace(this.parameterStyles, System.getProperty("picocli.color.parameters"));
        replace(this.optionParamStyles, System.getProperty("picocli.color.optionParams"));
        return this;
      }
      
      private void replace(List<CommandLine.Help.Ansi.IStyle> styles, String property) {
        if (property != null) {
          styles.clear();
          addAll(styles, CommandLine.Help.Ansi.Style.parse(property));
        } 
      }
      
      private ColorScheme addAll(List<CommandLine.Help.Ansi.IStyle> styles, CommandLine.Help.Ansi.IStyle... add) {
        styles.addAll(Arrays.asList(add));
        return this;
      }
      
      public CommandLine.Help.Ansi ansi() {
        return this.ansi;
      }
    }
    
    public static ColorScheme defaultColorScheme(Ansi ansi) {
      return (new ColorScheme(ansi))
        .commands(new Ansi.IStyle[] { Ansi.Style.bold }).options(new Ansi.IStyle[] { Ansi.Style.fg_yellow }).parameters(new Ansi.IStyle[] { Ansi.Style.fg_yellow }).optionParams(new Ansi.IStyle[] { Ansi.Style.italic });
    }
    
    public enum Ansi {
      AUTO, ON, OFF;
      
      static Text EMPTY_TEXT = new Text(0);
      
      static final boolean isWindows = System.getProperty("os.name").startsWith("Windows");
      
      static final boolean isXterm = (System.getenv("TERM") != null && System.getenv("TERM").startsWith("xterm"));
      
      static final boolean ISATTY = calcTTY();
      
      static {
      
      }
      
      static final boolean calcTTY() {
        if (isWindows && isXterm)
          return true; 
        try {
          return (System.class.getDeclaredMethod("console", new Class[0]).invoke(null, new Object[0]) != null);
        } catch (Throwable reflectionFailed) {
          return true;
        } 
      }
      
      private static boolean ansiPossible() {
        return (ISATTY && (!isWindows || isXterm));
      }
      
      public boolean enabled() {
        if (this == ON)
          return true; 
        if (this == OFF)
          return false; 
        return (System.getProperty("picocli.ansi") == null) ? ansiPossible() : Boolean.getBoolean("picocli.ansi");
      }
      
      public static interface IStyle {
        public static final String CSI = "\033[";
        
        String on();
        
        String off();
      }
      
      public enum Style implements IStyle {
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
        
        Style(int startCode, int endCode) {
          this.startCode = startCode;
          this.endCode = endCode;
        }
        
        public String on() {
          return "\033[" + this.startCode + "m";
        }
        
        public String off() {
          return "\033[" + this.endCode + "m";
        }
        
        public static String on(CommandLine.Help.Ansi.IStyle... styles) {
          StringBuilder result = new StringBuilder();
          for (CommandLine.Help.Ansi.IStyle style : styles)
            result.append(style.on()); 
          return result.toString();
        }
        
        public static String off(CommandLine.Help.Ansi.IStyle... styles) {
          StringBuilder result = new StringBuilder();
          for (CommandLine.Help.Ansi.IStyle style : styles)
            result.append(style.off()); 
          return result.toString();
        }
        
        public static CommandLine.Help.Ansi.IStyle fg(String str) {
          try {
            return valueOf(str.toLowerCase(Locale.ENGLISH));
          } catch (Exception exception) {
            try {
              return valueOf("fg_" + str.toLowerCase(Locale.ENGLISH));
            } catch (Exception exception1) {
              return new CommandLine.Help.Ansi.Palette256Color(true, str);
            } 
          } 
        }
        
        public static CommandLine.Help.Ansi.IStyle bg(String str) {
          try {
            return valueOf(str.toLowerCase(Locale.ENGLISH));
          } catch (Exception exception) {
            try {
              return valueOf("bg_" + str.toLowerCase(Locale.ENGLISH));
            } catch (Exception exception1) {
              return new CommandLine.Help.Ansi.Palette256Color(false, str);
            } 
          } 
        }
        
        public static CommandLine.Help.Ansi.IStyle[] parse(String commaSeparatedCodes) {
          String[] codes = commaSeparatedCodes.split(",");
          CommandLine.Help.Ansi.IStyle[] styles = new CommandLine.Help.Ansi.IStyle[codes.length];
          for (int i = 0; i < codes.length; i++) {
            if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("fg(")) {
              int end = codes[i].indexOf(')');
              styles[i] = fg(codes[i].substring(3, (end < 0) ? codes[i].length() : end));
            } else if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("bg(")) {
              int end = codes[i].indexOf(')');
              styles[i] = bg(codes[i].substring(3, (end < 0) ? codes[i].length() : end));
            } else {
              styles[i] = fg(codes[i]);
            } 
          } 
          return styles;
        }
      }
      
      static class Palette256Color implements IStyle {
        private final int fgbg;
        
        private final int color;
        
        Palette256Color(boolean foreground, String color) {
          this.fgbg = foreground ? 38 : 48;
          String[] rgb = color.split(";");
          if (rgb.length == 3) {
            this.color = 16 + 36 * Integer.decode(rgb[0]).intValue() + 6 * Integer.decode(rgb[1]).intValue() + Integer.decode(rgb[2]).intValue();
          } else {
            this.color = Integer.decode(color).intValue();
          } 
        }
        
        public String on() {
          return String.format("\033[%d;5;%dm", new Object[] { Integer.valueOf(this.fgbg), Integer.valueOf(this.color) });
        }
        
        public String off() {
          return "\033[" + (this.fgbg + 1) + "m";
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
      
      public Text apply(String plainText, List<IStyle> styles) {
        if (plainText.length() == 0)
          return new Text(0); 
        Text result = new Text(plainText.length());
        IStyle[] all = styles.<IStyle>toArray(new IStyle[styles.size()]);
        result.sections.add(new StyledSection(0, plainText
              .length(), Style.on(all), Style.off(reverse(all)) + Style.reset.off()));
        result.plain.append(plainText);
        result.length = result.plain.length();
        return result;
      }
      
      private static <T> T[] reverse(T[] all) {
        for (int i = 0; i < all.length / 2; i++) {
          T temp = all[i];
          all[i] = all[all.length - i - 1];
          all[all.length - i - 1] = temp;
        } 
        return all;
      }
      
      public class Text implements Cloneable {
        private final int maxLength;
        
        private int from;
        
        private int length;
        
        private StringBuilder plain = new StringBuilder();
        
        private List<CommandLine.Help.Ansi.StyledSection> sections = new ArrayList<>();
        
        public Text(int maxLength) {
          this.maxLength = maxLength;
        }
        
        public Text(String input) {
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
              return;
            } 
            this.plain.append(input.substring(i, j));
            int k = input.indexOf("|@", j);
            if (k == -1) {
              this.plain.append(input);
              this.length = this.plain.length();
              return;
            } 
            j += 2;
            String spec = input.substring(j, k);
            String[] items = spec.split(" ", 2);
            if (items.length == 1) {
              this.plain.append(input);
              this.length = this.plain.length();
              return;
            } 
            CommandLine.Help.Ansi.IStyle[] styles = CommandLine.Help.Ansi.Style.parse(items[0]);
            addStyledSection(this.plain.length(), items[1].length(), 
                CommandLine.Help.Ansi.Style.on(styles), CommandLine.Help.Ansi.Style.off((CommandLine.Help.Ansi.IStyle[])CommandLine.Help.Ansi.reverse((T[])styles)) + CommandLine.Help.Ansi.Style.reset.off());
            this.plain.append(items[1]);
            i = k + 2;
          } 
        }
        
        private void addStyledSection(int start, int length, String startStyle, String endStyle) {
          this.sections.add(new CommandLine.Help.Ansi.StyledSection(start, length, startStyle, endStyle));
        }
        
        public Object clone() {
          try {
            return super.clone();
          } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
          } 
        }
        
        public Text[] splitLines() {
          List<Text> result = new ArrayList<>();
          boolean trailingEmptyString = false;
          int start = 0, end = 0;
          for (int i = 0; i < this.plain.length(); end = ++i) {
            char c = this.plain.charAt(i);
            boolean eol = (c == '\n');
            int j = eol | ((c == '\r' && i + 1 < this.plain.length() && this.plain.charAt(i + 1) == '\n' && ++i > 0) ? 1 : 0);
            j |= (c == '\r') ? 1 : 0;
            if (j != 0) {
              result.add(substring(start, end));
              trailingEmptyString = (i == this.plain.length() - 1);
              start = i + 1;
            } 
          } 
          if (start < this.plain.length() || trailingEmptyString)
            result.add(substring(start, this.plain.length())); 
          return result.<Text>toArray(new Text[result.size()]);
        }
        
        public Text substring(int start) {
          return substring(start, this.length);
        }
        
        public Text substring(int start, int end) {
          Text result = (Text)clone();
          this.from += start;
          result.length = end - start;
          return result;
        }
        
        public Text append(String string) {
          return append(new Text(string));
        }
        
        public Text append(Text other) {
          Text result = (Text)clone();
          result.plain = new StringBuilder(this.plain.toString().substring(this.from, this.from + this.length));
          result.from = 0;
          result.sections = new ArrayList<>();
          for (CommandLine.Help.Ansi.StyledSection section : this.sections)
            result.sections.add(section.withStartIndex(section.startIndex - this.from)); 
          result.plain.append(other.plain.toString().substring(other.from, other.from + other.length));
          for (CommandLine.Help.Ansi.StyledSection section : other.sections) {
            int index = result.length + section.startIndex - other.from;
            result.sections.add(section.withStartIndex(index));
          } 
          result.length = result.plain.length();
          return result;
        }
        
        public void getStyledChars(int from, int length, Text destination, int offset) {
          if (destination.length < offset) {
            for (int i = destination.length; i < offset; i++)
              destination.plain.append(' '); 
            destination.length = offset;
          } 
          for (CommandLine.Help.Ansi.StyledSection section : this.sections)
            destination.sections.add(section.withStartIndex(section.startIndex - from + destination.length)); 
          destination.plain.append(this.plain.toString().substring(from, from + length));
          destination.length = destination.plain.length();
        }
        
        public String plainString() {
          return this.plain.toString().substring(this.from, this.from + this.length);
        }
        
        public boolean equals(Object obj) {
          return toString().equals(String.valueOf(obj));
        }
        
        public int hashCode() {
          return toString().hashCode();
        }
        
        public String toString() {
          if (!CommandLine.Help.Ansi.this.enabled())
            return this.plain.toString().substring(this.from, this.from + this.length); 
          if (this.length == 0)
            return ""; 
          StringBuilder sb = new StringBuilder(this.plain.length() + 20 * this.sections.size());
          CommandLine.Help.Ansi.StyledSection current = null;
          int end = Math.min(this.from + this.length, this.plain.length());
          for (int i = this.from; i < end; i++) {
            CommandLine.Help.Ansi.StyledSection section = findSectionContaining(i);
            if (section != current) {
              if (current != null)
                sb.append(current.endStyles); 
              if (section != null)
                sb.append(section.startStyles); 
              current = section;
            } 
            sb.append(this.plain.charAt(i));
          } 
          if (current != null)
            sb.append(current.endStyles); 
          return sb.toString();
        }
        
        private CommandLine.Help.Ansi.StyledSection findSectionContaining(int index) {
          for (CommandLine.Help.Ansi.StyledSection section : this.sections) {
            if (index >= section.startIndex && index < section.startIndex + section.length)
              return section; 
          } 
          return null;
        }
      }
    }
    
    public enum Style implements Ansi.IStyle {
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
      
      Style(int startCode, int endCode) {
        this.startCode = startCode;
        this.endCode = endCode;
      }
      
      public String on() {
        return "\033[" + this.startCode + "m";
      }
      
      public String off() {
        return "\033[" + this.endCode + "m";
      }
      
      public static String on(CommandLine.Help.Ansi.IStyle... styles) {
        StringBuilder result = new StringBuilder();
        for (CommandLine.Help.Ansi.IStyle style : styles)
          result.append(style.on()); 
        return result.toString();
      }
      
      public static String off(CommandLine.Help.Ansi.IStyle... styles) {
        StringBuilder result = new StringBuilder();
        for (CommandLine.Help.Ansi.IStyle style : styles)
          result.append(style.off()); 
        return result.toString();
      }
      
      public static CommandLine.Help.Ansi.IStyle fg(String str) {
        try {
          return valueOf(str.toLowerCase(Locale.ENGLISH));
        } catch (Exception exception) {
          try {
            return valueOf("fg_" + str.toLowerCase(Locale.ENGLISH));
          } catch (Exception exception1) {
            return new CommandLine.Help.Ansi.Palette256Color(true, str);
          } 
        } 
      }
      
      public static CommandLine.Help.Ansi.IStyle bg(String str) {
        try {
          return valueOf(str.toLowerCase(Locale.ENGLISH));
        } catch (Exception exception) {
          try {
            return valueOf("bg_" + str.toLowerCase(Locale.ENGLISH));
          } catch (Exception exception1) {
            return new CommandLine.Help.Ansi.Palette256Color(false, str);
          } 
        } 
      }
      
      public static CommandLine.Help.Ansi.IStyle[] parse(String commaSeparatedCodes) {
        String[] codes = commaSeparatedCodes.split(",");
        CommandLine.Help.Ansi.IStyle[] styles = new CommandLine.Help.Ansi.IStyle[codes.length];
        for (int i = 0; i < codes.length; i++) {
          if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("fg(")) {
            int end = codes[i].indexOf(')');
            styles[i] = fg(codes[i].substring(3, (end < 0) ? codes[i].length() : end));
          } else if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("bg(")) {
            int end = codes[i].indexOf(')');
            styles[i] = bg(codes[i].substring(3, (end < 0) ? codes[i].length() : end));
          } else {
            styles[i] = fg(codes[i]);
          } 
        } 
        return styles;
      }
    }
    
    public class Text implements Cloneable {
      private final int maxLength;
      
      private int from;
      
      private int length;
      
      private StringBuilder plain = new StringBuilder();
      
      private List<CommandLine.Help.Ansi.StyledSection> sections = new ArrayList<>();
      
      public Text(int maxLength) {
        this.maxLength = maxLength;
      }
      
      public Text(String input) {
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
            return;
          } 
          this.plain.append(input.substring(i, j));
          int k = input.indexOf("|@", j);
          if (k == -1) {
            this.plain.append(input);
            this.length = this.plain.length();
            return;
          } 
          j += 2;
          String spec = input.substring(j, k);
          String[] items = spec.split(" ", 2);
          if (items.length == 1) {
            this.plain.append(input);
            this.length = this.plain.length();
            return;
          } 
          CommandLine.Help.Ansi.IStyle[] styles = CommandLine.Help.Ansi.Style.parse(items[0]);
          addStyledSection(this.plain.length(), items[1].length(), CommandLine.Help.Ansi.Style.on(styles), CommandLine.Help.Ansi.Style.off((CommandLine.Help.Ansi.IStyle[])CommandLine.Help.Ansi.reverse((T[])styles)) + CommandLine.Help.Ansi.Style.reset.off());
          this.plain.append(items[1]);
          i = k + 2;
        } 
      }
      
      private void addStyledSection(int start, int length, String startStyle, String endStyle) {
        this.sections.add(new CommandLine.Help.Ansi.StyledSection(start, length, startStyle, endStyle));
      }
      
      public Object clone() {
        try {
          return super.clone();
        } catch (CloneNotSupportedException e) {
          throw new IllegalStateException(e);
        } 
      }
      
      public Text[] splitLines() {
        List<Text> result = new ArrayList<>();
        boolean trailingEmptyString = false;
        int start = 0, end = 0;
        for (int i = 0; i < this.plain.length(); end = ++i) {
          char c = this.plain.charAt(i);
          boolean eol = (c == '\n');
          int j = eol | ((c == '\r' && i + 1 < this.plain.length() && this.plain.charAt(i + 1) == '\n' && ++i > 0) ? 1 : 0);
          j |= (c == '\r') ? 1 : 0;
          if (j != 0) {
            result.add(substring(start, end));
            trailingEmptyString = (i == this.plain.length() - 1);
            start = i + 1;
          } 
        } 
        if (start < this.plain.length() || trailingEmptyString)
          result.add(substring(start, this.plain.length())); 
        return result.<Text>toArray(new Text[result.size()]);
      }
      
      public Text substring(int start) {
        return substring(start, this.length);
      }
      
      public Text substring(int start, int end) {
        Text result = (Text)clone();
        this.from += start;
        result.length = end - start;
        return result;
      }
      
      public Text append(String string) {
        return append(new Text(string));
      }
      
      public Text append(Text other) {
        Text result = (Text)clone();
        result.plain = new StringBuilder(this.plain.toString().substring(this.from, this.from + this.length));
        result.from = 0;
        result.sections = new ArrayList<>();
        for (CommandLine.Help.Ansi.StyledSection section : this.sections)
          result.sections.add(section.withStartIndex(section.startIndex - this.from)); 
        result.plain.append(other.plain.toString().substring(other.from, other.from + other.length));
        for (CommandLine.Help.Ansi.StyledSection section : other.sections) {
          int index = result.length + section.startIndex - other.from;
          result.sections.add(section.withStartIndex(index));
        } 
        result.length = result.plain.length();
        return result;
      }
      
      public void getStyledChars(int from, int length, Text destination, int offset) {
        if (destination.length < offset) {
          for (int i = destination.length; i < offset; i++)
            destination.plain.append(' '); 
          destination.length = offset;
        } 
        for (CommandLine.Help.Ansi.StyledSection section : this.sections)
          destination.sections.add(section.withStartIndex(section.startIndex - from + destination.length)); 
        destination.plain.append(this.plain.toString().substring(from, from + length));
        destination.length = destination.plain.length();
      }
      
      public String plainString() {
        return this.plain.toString().substring(this.from, this.from + this.length);
      }
      
      public boolean equals(Object obj) {
        return toString().equals(String.valueOf(obj));
      }
      
      public int hashCode() {
        return toString().hashCode();
      }
      
      public String toString() {
        if (!CommandLine.Help.Ansi.this.enabled())
          return this.plain.toString().substring(this.from, this.from + this.length); 
        if (this.length == 0)
          return ""; 
        StringBuilder sb = new StringBuilder(this.plain.length() + 20 * this.sections.size());
        CommandLine.Help.Ansi.StyledSection current = null;
        int end = Math.min(this.from + this.length, this.plain.length());
        for (int i = this.from; i < end; i++) {
          CommandLine.Help.Ansi.StyledSection section = findSectionContaining(i);
          if (section != current) {
            if (current != null)
              sb.append(current.endStyles); 
            if (section != null)
              sb.append(section.startStyles); 
            current = section;
          } 
          sb.append(this.plain.charAt(i));
        } 
        if (current != null)
          sb.append(current.endStyles); 
        return sb.toString();
      }
      
      private CommandLine.Help.Ansi.StyledSection findSectionContaining(int index) {
        for (CommandLine.Help.Ansi.StyledSection section : this.sections) {
          if (index >= section.startIndex && index < section.startIndex + section.length)
            return section; 
        } 
        return null;
      }
    }
    
    public static interface IParamLabelRenderer {
      CommandLine.Help.Ansi.Text renderParameterLabel(Field param2Field, CommandLine.Help.Ansi param2Ansi, List<CommandLine.Help.Ansi.IStyle> param2List);
      
      String separator();
    }
    
    public static interface IParameterRenderer {
      CommandLine.Help.Ansi.Text[][] render(CommandLine.Parameters param2Parameters, Field param2Field, CommandLine.Help.IParamLabelRenderer param2IParamLabelRenderer, CommandLine.Help.ColorScheme param2ColorScheme);
    }
    
    public static interface IOptionRenderer {
      CommandLine.Help.Ansi.Text[][] render(CommandLine.Option param2Option, Field param2Field, CommandLine.Help.IParamLabelRenderer param2IParamLabelRenderer, CommandLine.Help.ColorScheme param2ColorScheme);
    }
    
    public static interface IStyle {
      public static final String CSI = "\033[";
      
      String on();
      
      String off();
    }
  }
  
  public static class ColorScheme {
    public final List<CommandLine.Help.Ansi.IStyle> commandStyles = new ArrayList<>();
    
    public final List<CommandLine.Help.Ansi.IStyle> optionStyles = new ArrayList<>();
    
    public final List<CommandLine.Help.Ansi.IStyle> parameterStyles = new ArrayList<>();
    
    public final List<CommandLine.Help.Ansi.IStyle> optionParamStyles = new ArrayList<>();
    
    private final CommandLine.Help.Ansi ansi;
    
    public ColorScheme() {
      this(CommandLine.Help.Ansi.AUTO);
    }
    
    public ColorScheme(CommandLine.Help.Ansi ansi) {
      this.ansi = CommandLine.Assert.<CommandLine.Help.Ansi>notNull(ansi, "ansi");
    }
    
    public ColorScheme commands(CommandLine.Help.Ansi.IStyle... styles) {
      return addAll(this.commandStyles, styles);
    }
    
    public ColorScheme options(CommandLine.Help.Ansi.IStyle... styles) {
      return addAll(this.optionStyles, styles);
    }
    
    public ColorScheme parameters(CommandLine.Help.Ansi.IStyle... styles) {
      return addAll(this.parameterStyles, styles);
    }
    
    public ColorScheme optionParams(CommandLine.Help.Ansi.IStyle... styles) {
      return addAll(this.optionParamStyles, styles);
    }
    
    public CommandLine.Help.Ansi.Text commandText(String command) {
      return ansi().apply(command, this.commandStyles);
    }
    
    public CommandLine.Help.Ansi.Text optionText(String option) {
      return ansi().apply(option, this.optionStyles);
    }
    
    public CommandLine.Help.Ansi.Text parameterText(String parameter) {
      return ansi().apply(parameter, this.parameterStyles);
    }
    
    public CommandLine.Help.Ansi.Text optionParamText(String optionParam) {
      return ansi().apply(optionParam, this.optionParamStyles);
    }
    
    public ColorScheme applySystemProperties() {
      replace(this.commandStyles, System.getProperty("picocli.color.commands"));
      replace(this.optionStyles, System.getProperty("picocli.color.options"));
      replace(this.parameterStyles, System.getProperty("picocli.color.parameters"));
      replace(this.optionParamStyles, System.getProperty("picocli.color.optionParams"));
      return this;
    }
    
    private void replace(List<CommandLine.Help.Ansi.IStyle> styles, String property) {
      if (property != null) {
        styles.clear();
        addAll(styles, CommandLine.Help.Ansi.Style.parse(property));
      } 
    }
    
    private ColorScheme addAll(List<CommandLine.Help.Ansi.IStyle> styles, CommandLine.Help.Ansi.IStyle... add) {
      styles.addAll(Arrays.asList(add));
      return this;
    }
    
    public CommandLine.Help.Ansi ansi() {
      return this.ansi;
    }
  }
  
  public enum Ansi {
    AUTO, ON, OFF;
    
    static Text EMPTY_TEXT = new Text(0);
    
    static final boolean isWindows = System.getProperty("os.name").startsWith("Windows");
    
    static final boolean isXterm = (System.getenv("TERM") != null && System.getenv("TERM").startsWith("xterm"));
    
    static final boolean ISATTY = calcTTY();
    
    static {
    
    }
    
    static final boolean calcTTY() {
      if (isWindows && isXterm)
        return true; 
      try {
        return (System.class.getDeclaredMethod("console", new Class[0]).invoke(null, new Object[0]) != null);
      } catch (Throwable reflectionFailed) {
        return true;
      } 
    }
    
    private static boolean ansiPossible() {
      return (ISATTY && (!isWindows || isXterm));
    }
    
    public boolean enabled() {
      if (this == ON)
        return true; 
      if (this == OFF)
        return false; 
      return (System.getProperty("picocli.ansi") == null) ? ansiPossible() : Boolean.getBoolean("picocli.ansi");
    }
    
    public static interface IStyle {
      public static final String CSI = "\033[";
      
      String on();
      
      String off();
    }
    
    public enum Style implements IStyle {
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
      
      Style(int startCode, int endCode) {
        this.startCode = startCode;
        this.endCode = endCode;
      }
      
      public String on() {
        return "\033[" + this.startCode + "m";
      }
      
      public String off() {
        return "\033[" + this.endCode + "m";
      }
      
      public static String on(CommandLine.Help.Ansi.IStyle... styles) {
        StringBuilder result = new StringBuilder();
        for (CommandLine.Help.Ansi.IStyle style : styles)
          result.append(style.on()); 
        return result.toString();
      }
      
      public static String off(CommandLine.Help.Ansi.IStyle... styles) {
        StringBuilder result = new StringBuilder();
        for (CommandLine.Help.Ansi.IStyle style : styles)
          result.append(style.off()); 
        return result.toString();
      }
      
      public static CommandLine.Help.Ansi.IStyle fg(String str) {
        try {
          return valueOf(str.toLowerCase(Locale.ENGLISH));
        } catch (Exception exception) {
          try {
            return valueOf("fg_" + str.toLowerCase(Locale.ENGLISH));
          } catch (Exception exception1) {
            return new CommandLine.Help.Ansi.Palette256Color(true, str);
          } 
        } 
      }
      
      public static CommandLine.Help.Ansi.IStyle bg(String str) {
        try {
          return valueOf(str.toLowerCase(Locale.ENGLISH));
        } catch (Exception exception) {
          try {
            return valueOf("bg_" + str.toLowerCase(Locale.ENGLISH));
          } catch (Exception exception1) {
            return new CommandLine.Help.Ansi.Palette256Color(false, str);
          } 
        } 
      }
      
      public static CommandLine.Help.Ansi.IStyle[] parse(String commaSeparatedCodes) {
        String[] codes = commaSeparatedCodes.split(",");
        CommandLine.Help.Ansi.IStyle[] styles = new CommandLine.Help.Ansi.IStyle[codes.length];
        for (int i = 0; i < codes.length; i++) {
          if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("fg(")) {
            int end = codes[i].indexOf(')');
            styles[i] = fg(codes[i].substring(3, (end < 0) ? codes[i].length() : end));
          } else if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("bg(")) {
            int end = codes[i].indexOf(')');
            styles[i] = bg(codes[i].substring(3, (end < 0) ? codes[i].length() : end));
          } else {
            styles[i] = fg(codes[i]);
          } 
        } 
        return styles;
      }
    }
    
    static class Palette256Color implements IStyle {
      private final int fgbg;
      
      private final int color;
      
      Palette256Color(boolean foreground, String color) {
        this.fgbg = foreground ? 38 : 48;
        String[] rgb = color.split(";");
        if (rgb.length == 3) {
          this.color = 16 + 36 * Integer.decode(rgb[0]).intValue() + 6 * Integer.decode(rgb[1]).intValue() + Integer.decode(rgb[2]).intValue();
        } else {
          this.color = Integer.decode(color).intValue();
        } 
      }
      
      public String on() {
        return String.format("\033[%d;5;%dm", new Object[] { Integer.valueOf(this.fgbg), Integer.valueOf(this.color) });
      }
      
      public String off() {
        return "\033[" + (this.fgbg + 1) + "m";
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
    
    public Text apply(String plainText, List<IStyle> styles) {
      if (plainText.length() == 0)
        return new Text(0); 
      Text result = new Text(plainText.length());
      IStyle[] all = styles.<IStyle>toArray(new IStyle[styles.size()]);
      result.sections.add(new StyledSection(0, plainText.length(), Style.on(all), Style.off(reverse(all)) + Style.reset.off()));
      result.plain.append(plainText);
      result.length = result.plain.length();
      return result;
    }
    
    private static <T> T[] reverse(T[] all) {
      for (int i = 0; i < all.length / 2; i++) {
        T temp = all[i];
        all[i] = all[all.length - i - 1];
        all[all.length - i - 1] = temp;
      } 
      return all;
    }
    
    public class Text implements Cloneable {
      private final int maxLength;
      
      private int from;
      
      private int length;
      
      private StringBuilder plain = new StringBuilder();
      
      private List<CommandLine.Help.Ansi.StyledSection> sections = new ArrayList<>();
      
      public Text(int maxLength) {
        this.maxLength = maxLength;
      }
      
      public Text(String input) {
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
            return;
          } 
          this.plain.append(input.substring(i, j));
          int k = input.indexOf("|@", j);
          if (k == -1) {
            this.plain.append(input);
            this.length = this.plain.length();
            return;
          } 
          j += 2;
          String spec = input.substring(j, k);
          String[] items = spec.split(" ", 2);
          if (items.length == 1) {
            this.plain.append(input);
            this.length = this.plain.length();
            return;
          } 
          CommandLine.Help.Ansi.IStyle[] styles = CommandLine.Help.Ansi.Style.parse(items[0]);
          addStyledSection(this.plain.length(), items[1].length(), CommandLine.Help.Ansi.Style.on(styles), CommandLine.Help.Ansi.Style.off((CommandLine.Help.Ansi.IStyle[])CommandLine.Help.Ansi.reverse((T[])styles)) + CommandLine.Help.Ansi.Style.reset.off());
          this.plain.append(items[1]);
          i = k + 2;
        } 
      }
      
      private void addStyledSection(int start, int length, String startStyle, String endStyle) {
        this.sections.add(new CommandLine.Help.Ansi.StyledSection(start, length, startStyle, endStyle));
      }
      
      public Object clone() {
        try {
          return super.clone();
        } catch (CloneNotSupportedException e) {
          throw new IllegalStateException(e);
        } 
      }
      
      public Text[] splitLines() {
        List<Text> result = new ArrayList<>();
        boolean trailingEmptyString = false;
        int start = 0, end = 0;
        for (int i = 0; i < this.plain.length(); end = ++i) {
          char c = this.plain.charAt(i);
          boolean eol = (c == '\n');
          int j = eol | ((c == '\r' && i + 1 < this.plain.length() && this.plain.charAt(i + 1) == '\n' && ++i > 0) ? 1 : 0);
          j |= (c == '\r') ? 1 : 0;
          if (j != 0) {
            result.add(substring(start, end));
            trailingEmptyString = (i == this.plain.length() - 1);
            start = i + 1;
          } 
        } 
        if (start < this.plain.length() || trailingEmptyString)
          result.add(substring(start, this.plain.length())); 
        return result.<Text>toArray(new Text[result.size()]);
      }
      
      public Text substring(int start) {
        return substring(start, this.length);
      }
      
      public Text substring(int start, int end) {
        Text result = (Text)clone();
        this.from += start;
        result.length = end - start;
        return result;
      }
      
      public Text append(String string) {
        return append(new Text(string));
      }
      
      public Text append(Text other) {
        Text result = (Text)clone();
        result.plain = new StringBuilder(this.plain.toString().substring(this.from, this.from + this.length));
        result.from = 0;
        result.sections = new ArrayList<>();
        for (CommandLine.Help.Ansi.StyledSection section : this.sections)
          result.sections.add(section.withStartIndex(section.startIndex - this.from)); 
        result.plain.append(other.plain.toString().substring(other.from, other.from + other.length));
        for (CommandLine.Help.Ansi.StyledSection section : other.sections) {
          int index = result.length + section.startIndex - other.from;
          result.sections.add(section.withStartIndex(index));
        } 
        result.length = result.plain.length();
        return result;
      }
      
      public void getStyledChars(int from, int length, Text destination, int offset) {
        if (destination.length < offset) {
          for (int i = destination.length; i < offset; i++)
            destination.plain.append(' '); 
          destination.length = offset;
        } 
        for (CommandLine.Help.Ansi.StyledSection section : this.sections)
          destination.sections.add(section.withStartIndex(section.startIndex - from + destination.length)); 
        destination.plain.append(this.plain.toString().substring(from, from + length));
        destination.length = destination.plain.length();
      }
      
      public String plainString() {
        return this.plain.toString().substring(this.from, this.from + this.length);
      }
      
      public boolean equals(Object obj) {
        return toString().equals(String.valueOf(obj));
      }
      
      public int hashCode() {
        return toString().hashCode();
      }
      
      public String toString() {
        if (!CommandLine.Help.Ansi.this.enabled())
          return this.plain.toString().substring(this.from, this.from + this.length); 
        if (this.length == 0)
          return ""; 
        StringBuilder sb = new StringBuilder(this.plain.length() + 20 * this.sections.size());
        CommandLine.Help.Ansi.StyledSection current = null;
        int end = Math.min(this.from + this.length, this.plain.length());
        for (int i = this.from; i < end; i++) {
          CommandLine.Help.Ansi.StyledSection section = findSectionContaining(i);
          if (section != current) {
            if (current != null)
              sb.append(current.endStyles); 
            if (section != null)
              sb.append(section.startStyles); 
            current = section;
          } 
          sb.append(this.plain.charAt(i));
        } 
        if (current != null)
          sb.append(current.endStyles); 
        return sb.toString();
      }
      
      private CommandLine.Help.Ansi.StyledSection findSectionContaining(int index) {
        for (CommandLine.Help.Ansi.StyledSection section : this.sections) {
          if (index >= section.startIndex && index < section.startIndex + section.length)
            return section; 
        } 
        return null;
      }
    }
  }
  
  public class Text implements Cloneable {
    private final int maxLength;
    
    private int from;
    
    private int length;
    
    private StringBuilder plain = new StringBuilder();
    
    private List<CommandLine.Help.Ansi.StyledSection> sections = new ArrayList<>();
    
    public Text(int maxLength) {
      this.maxLength = maxLength;
    }
    
    public Text(String input) {
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
          return;
        } 
        this.plain.append(input.substring(i, j));
        int k = input.indexOf("|@", j);
        if (k == -1) {
          this.plain.append(input);
          this.length = this.plain.length();
          return;
        } 
        j += 2;
        String spec = input.substring(j, k);
        String[] items = spec.split(" ", 2);
        if (items.length == 1) {
          this.plain.append(input);
          this.length = this.plain.length();
          return;
        } 
        CommandLine.Help.Ansi.IStyle[] styles = CommandLine.Help.Ansi.Style.parse(items[0]);
        addStyledSection(this.plain.length(), items[1].length(), CommandLine.Help.Ansi.Style.on(styles), CommandLine.Help.Ansi.Style.off((CommandLine.Help.Ansi.IStyle[])CommandLine.Help.Ansi.reverse((T[])styles)) + CommandLine.Help.Ansi.Style.reset.off());
        this.plain.append(items[1]);
        i = k + 2;
      } 
    }
    
    private void addStyledSection(int start, int length, String startStyle, String endStyle) {
      this.sections.add(new CommandLine.Help.Ansi.StyledSection(start, length, startStyle, endStyle));
    }
    
    public Object clone() {
      try {
        return super.clone();
      } catch (CloneNotSupportedException e) {
        throw new IllegalStateException(e);
      } 
    }
    
    public Text[] splitLines() {
      List<Text> result = new ArrayList<>();
      boolean trailingEmptyString = false;
      int start = 0, end = 0;
      for (int i = 0; i < this.plain.length(); end = ++i) {
        char c = this.plain.charAt(i);
        boolean eol = (c == '\n');
        int j = eol | ((c == '\r' && i + 1 < this.plain.length() && this.plain.charAt(i + 1) == '\n' && ++i > 0) ? 1 : 0);
        j |= (c == '\r') ? 1 : 0;
        if (j != 0) {
          result.add(substring(start, end));
          trailingEmptyString = (i == this.plain.length() - 1);
          start = i + 1;
        } 
      } 
      if (start < this.plain.length() || trailingEmptyString)
        result.add(substring(start, this.plain.length())); 
      return result.<Text>toArray(new Text[result.size()]);
    }
    
    public Text substring(int start) {
      return substring(start, this.length);
    }
    
    public Text substring(int start, int end) {
      Text result = (Text)clone();
      this.from += start;
      result.length = end - start;
      return result;
    }
    
    public Text append(String string) {
      return append(new Text(string));
    }
    
    public Text append(Text other) {
      Text result = (Text)clone();
      result.plain = new StringBuilder(this.plain.toString().substring(this.from, this.from + this.length));
      result.from = 0;
      result.sections = new ArrayList<>();
      for (CommandLine.Help.Ansi.StyledSection section : this.sections)
        result.sections.add(section.withStartIndex(section.startIndex - this.from)); 
      result.plain.append(other.plain.toString().substring(other.from, other.from + other.length));
      for (CommandLine.Help.Ansi.StyledSection section : other.sections) {
        int index = result.length + section.startIndex - other.from;
        result.sections.add(section.withStartIndex(index));
      } 
      result.length = result.plain.length();
      return result;
    }
    
    public void getStyledChars(int from, int length, Text destination, int offset) {
      if (destination.length < offset) {
        for (int i = destination.length; i < offset; i++)
          destination.plain.append(' '); 
        destination.length = offset;
      } 
      for (CommandLine.Help.Ansi.StyledSection section : this.sections)
        destination.sections.add(section.withStartIndex(section.startIndex - from + destination.length)); 
      destination.plain.append(this.plain.toString().substring(from, from + length));
      destination.length = destination.plain.length();
    }
    
    public String plainString() {
      return this.plain.toString().substring(this.from, this.from + this.length);
    }
    
    public boolean equals(Object obj) {
      return toString().equals(String.valueOf(obj));
    }
    
    public int hashCode() {
      return toString().hashCode();
    }
    
    public String toString() {
      if (!CommandLine.Help.Ansi.this.enabled())
        return this.plain.toString().substring(this.from, this.from + this.length); 
      if (this.length == 0)
        return ""; 
      StringBuilder sb = new StringBuilder(this.plain.length() + 20 * this.sections.size());
      CommandLine.Help.Ansi.StyledSection current = null;
      int end = Math.min(this.from + this.length, this.plain.length());
      for (int i = this.from; i < end; i++) {
        CommandLine.Help.Ansi.StyledSection section = findSectionContaining(i);
        if (section != current) {
          if (current != null)
            sb.append(current.endStyles); 
          if (section != null)
            sb.append(section.startStyles); 
          current = section;
        } 
        sb.append(this.plain.charAt(i));
      } 
      if (current != null)
        sb.append(current.endStyles); 
      return sb.toString();
    }
    
    private CommandLine.Help.Ansi.StyledSection findSectionContaining(int index) {
      for (CommandLine.Help.Ansi.StyledSection section : this.sections) {
        if (index >= section.startIndex && index < section.startIndex + section.length)
          return section; 
      } 
      return null;
    }
  }
  
  private static final class Assert {
    static <T> T notNull(T object, String description) {
      if (object == null)
        throw new NullPointerException(description); 
      return object;
    }
  }
  
  private enum TraceLevel {
    OFF, WARN, INFO, DEBUG;
    
    public boolean isEnabled(TraceLevel other) {
      return (ordinal() >= other.ordinal());
    }
    
    private void print(CommandLine.Tracer tracer, String msg, Object... params) {
      if (tracer.level.isEnabled(this))
        tracer.stream.printf(prefix(msg), params); 
    }
    
    private String prefix(String msg) {
      return "[picocli " + this + "] " + msg;
    }
    
    static TraceLevel lookup(String key) {
      return (key == null) ? WARN : ((CommandLine.empty(key) || "true".equalsIgnoreCase(key)) ? INFO : valueOf(key));
    }
  }
  
  private static class Tracer {
    CommandLine.TraceLevel level = CommandLine.TraceLevel.lookup(System.getProperty("picocli.trace"));
    
    PrintStream stream = System.err;
    
    void warn(String msg, Object... params) {
      CommandLine.TraceLevel.WARN.print(this, msg, params);
    }
    
    void info(String msg, Object... params) {
      CommandLine.TraceLevel.INFO.print(this, msg, params);
    }
    
    void debug(String msg, Object... params) {
      CommandLine.TraceLevel.DEBUG.print(this, msg, params);
    }
    
    boolean isWarn() {
      return this.level.isEnabled(CommandLine.TraceLevel.WARN);
    }
    
    boolean isInfo() {
      return this.level.isEnabled(CommandLine.TraceLevel.INFO);
    }
    
    boolean isDebug() {
      return this.level.isEnabled(CommandLine.TraceLevel.DEBUG);
    }
    
    private Tracer() {}
  }
  
  public static class PicocliException extends RuntimeException {
    private static final long serialVersionUID = -2574128880125050818L;
    
    public PicocliException(String msg) {
      super(msg);
    }
    
    public PicocliException(String msg, Exception ex) {
      super(msg, ex);
    }
  }
  
  public static class InitializationException extends PicocliException {
    private static final long serialVersionUID = 8423014001666638895L;
    
    public InitializationException(String msg) {
      super(msg);
    }
    
    public InitializationException(String msg, Exception ex) {
      super(msg, ex);
    }
  }
  
  public static class ExecutionException extends PicocliException {
    private static final long serialVersionUID = 7764539594267007998L;
    
    private final CommandLine commandLine;
    
    public ExecutionException(CommandLine commandLine, String msg) {
      super(msg);
      this.commandLine = CommandLine.Assert.<CommandLine>notNull(commandLine, "commandLine");
    }
    
    public ExecutionException(CommandLine commandLine, String msg, Exception ex) {
      super(msg, ex);
      this.commandLine = CommandLine.Assert.<CommandLine>notNull(commandLine, "commandLine");
    }
    
    public CommandLine getCommandLine() {
      return this.commandLine;
    }
  }
  
  public static class TypeConversionException extends PicocliException {
    private static final long serialVersionUID = 4251973913816346114L;
    
    public TypeConversionException(String msg) {
      super(msg);
    }
  }
  
  public static class ParameterException extends PicocliException {
    private static final long serialVersionUID = 1477112829129763139L;
    
    private final CommandLine commandLine;
    
    public ParameterException(CommandLine commandLine, String msg) {
      super(msg);
      this.commandLine = CommandLine.Assert.<CommandLine>notNull(commandLine, "commandLine");
    }
    
    public ParameterException(CommandLine commandLine, String msg, Exception ex) {
      super(msg, ex);
      this.commandLine = CommandLine.Assert.<CommandLine>notNull(commandLine, "commandLine");
    }
    
    public CommandLine getCommandLine() {
      return this.commandLine;
    }
    
    private static ParameterException create(CommandLine cmd, Exception ex, String arg, int i, String[] args) {
      String msg = ex.getClass().getSimpleName() + ": " + ex.getLocalizedMessage() + " while processing argument at or before arg[" + i + "] '" + arg + "' in " + Arrays.toString((Object[])args) + ": " + ex.toString();
      return new ParameterException(cmd, msg, ex);
    }
  }
  
  public static class MissingParameterException extends ParameterException {
    private static final long serialVersionUID = 5075678535706338753L;
    
    public MissingParameterException(CommandLine commandLine, String msg) {
      super(commandLine, msg);
    }
    
    private static MissingParameterException create(CommandLine cmd, Collection<Field> missing, String separator) {
      if (missing.size() == 1)
        return new MissingParameterException(cmd, "Missing required option '" + 
            describe(missing.iterator().next(), separator) + "'"); 
      List<String> names = new ArrayList<>(missing.size());
      for (Field field : missing)
        names.add(describe(field, separator)); 
      return new MissingParameterException(cmd, "Missing required options " + names.toString());
    }
    
    private static String describe(Field field, String separator) {
      String prefix = field.isAnnotationPresent((Class)CommandLine.Option.class) ? (((CommandLine.Option)field.getAnnotation((Class)CommandLine.Option.class)).names()[0] + separator) : ("params[" + ((CommandLine.Parameters)field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).index() + "]" + separator);
      return prefix + CommandLine.Help.DefaultParamLabelRenderer.renderParameterName(field);
    }
  }
  
  public static class DuplicateOptionAnnotationsException extends InitializationException {
    private static final long serialVersionUID = -3355128012575075641L;
    
    public DuplicateOptionAnnotationsException(String msg) {
      super(msg);
    }
    
    private static DuplicateOptionAnnotationsException create(String name, Field field1, Field field2) {
      return new DuplicateOptionAnnotationsException("Option name '" + name + "' is used by both " + field1
          .getDeclaringClass().getName() + "." + field1.getName() + " and " + field2
          .getDeclaringClass().getName() + "." + field2.getName());
    }
  }
  
  public static class ParameterIndexGapException extends InitializationException {
    private static final long serialVersionUID = -1520981133257618319L;
    
    public ParameterIndexGapException(String msg) {
      super(msg);
    }
  }
  
  public static class UnmatchedArgumentException extends ParameterException {
    private static final long serialVersionUID = -8700426380701452440L;
    
    public UnmatchedArgumentException(CommandLine commandLine, String msg) {
      super(commandLine, msg);
    }
    
    public UnmatchedArgumentException(CommandLine commandLine, Stack<String> args) {
      this(commandLine, new ArrayList<>((Collection)CommandLine.reverse((Stack)args)));
    }
    
    public UnmatchedArgumentException(CommandLine commandLine, List<String> args) {
      this(commandLine, "Unmatched argument" + ((args.size() == 1) ? " " : "s ") + args);
    }
  }
  
  public static class MaxValuesforFieldExceededException extends ParameterException {
    private static final long serialVersionUID = 6536145439570100641L;
    
    public MaxValuesforFieldExceededException(CommandLine commandLine, String msg) {
      super(commandLine, msg);
    }
  }
  
  public static class OverwrittenOptionException extends ParameterException {
    private static final long serialVersionUID = 1338029208271055776L;
    
    public OverwrittenOptionException(CommandLine commandLine, String msg) {
      super(commandLine, msg);
    }
  }
  
  public static class MissingTypeConverterException extends ParameterException {
    private static final long serialVersionUID = -6050931703233083760L;
    
    public MissingTypeConverterException(CommandLine commandLine, String msg) {
      super(commandLine, msg);
    }
  }
  
  public static interface ITypeConverter<K> {
    K convert(String param1String) throws Exception;
  }
  
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE, ElementType.LOCAL_VARIABLE, ElementType.PACKAGE})
  public static @interface Command {
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
  
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD})
  public static @interface Parameters {
    String index() default "*";
    
    String[] description() default {};
    
    String arity() default "";
    
    String paramLabel() default "";
    
    Class<?>[] type() default {};
    
    String split() default "";
    
    boolean hidden() default false;
  }
  
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD})
  public static @interface Option {
    String[] names();
    
    boolean required() default false;
    
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
  
  public static interface IExceptionHandler {
    List<Object> handleException(CommandLine.ParameterException param1ParameterException, PrintStream param1PrintStream, CommandLine.Help.Ansi param1Ansi, String... param1VarArgs);
  }
  
  public static interface IParseResultHandler {
    List<Object> handleParseResult(List<CommandLine> param1List, PrintStream param1PrintStream, CommandLine.Help.Ansi param1Ansi) throws CommandLine.ExecutionException;
  }
  
  public static interface IParamLabelRenderer {
    CommandLine.Help.Ansi.Text renderParameterLabel(Field param1Field, CommandLine.Help.Ansi param1Ansi, List<CommandLine.Help.Ansi.IStyle> param1List);
    
    String separator();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\tools\picocli\CommandLine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */