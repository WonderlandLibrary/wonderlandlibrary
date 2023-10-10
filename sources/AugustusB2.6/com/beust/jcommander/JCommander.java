// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander;

import java.util.concurrent.CopyOnWriteArrayList;
import java.nio.charset.Charset;
import java.lang.reflect.Constructor;
import com.beust.jcommander.converters.NoConverter;
import com.beust.jcommander.converters.StringConverter;
import com.beust.jcommander.converters.EnumConverter;
import com.beust.jcommander.converters.DefaultListConverter;
import com.beust.jcommander.converters.IParameterSplitter;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;
import java.util.Comparator;
import java.util.Collections;
import java.util.Locale;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import com.beust.jcommander.internal.DefaultConsole;
import com.beust.jcommander.internal.JDK6Console;
import com.beust.jcommander.internal.Nullable;
import java.util.ResourceBundle;
import com.beust.jcommander.internal.DefaultConverterFactory;
import com.beust.jcommander.internal.Maps;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Console;
import java.util.List;
import java.util.Map;

public class JCommander
{
    public static final String DEBUG_PROPERTY = "jcommander.debug";
    private Map<FuzzyMap.IKey, ParameterDescription> descriptions;
    private List<Object> objects;
    private boolean firstTimeMainParameter;
    private Parameterized mainParameter;
    private Object mainParameterObject;
    private Parameter mainParameterAnnotation;
    private ParameterDescription mainParameterDescription;
    private Map<Parameterized, ParameterDescription> requiredFields;
    private Map<Parameterized, ParameterDescription> fields;
    private Map<ProgramName, JCommander> commands;
    private Map<FuzzyMap.IKey, ProgramName> aliasMap;
    private String parsedCommand;
    private String parsedAlias;
    private ProgramName programName;
    private boolean helpWasSpecified;
    private List<String> unknownArgs;
    private static Console console;
    private final Options options;
    private final IVariableArity DEFAULT_VARIABLE_ARITY;
    
    private JCommander(final Options options) {
        this.objects = Lists.newArrayList();
        this.firstTimeMainParameter = true;
        this.mainParameter = null;
        this.requiredFields = Maps.newHashMap();
        this.fields = Maps.newHashMap();
        this.commands = Maps.newLinkedHashMap();
        this.aliasMap = Maps.newLinkedHashMap();
        this.unknownArgs = Lists.newArrayList();
        this.DEFAULT_VARIABLE_ARITY = new DefaultVariableArity();
        if (options == null) {
            throw new NullPointerException("options");
        }
        this.options = options;
        this.addConverterFactory(new DefaultConverterFactory());
    }
    
    public JCommander() {
        this(new Options());
    }
    
    public JCommander(final Object o) {
        this(o, (ResourceBundle)null);
    }
    
    public JCommander(final Object o, @Nullable final ResourceBundle resourceBundle) {
        this(o, resourceBundle, (String[])null);
    }
    
    public JCommander(final Object o, @Nullable final ResourceBundle descriptionsBundle, final String... array) {
        this();
        this.addObject(o);
        if (descriptionsBundle != null) {
            this.setDescriptionsBundle(descriptionsBundle);
        }
        this.createDescriptions();
        if (array != null) {
            this.parse(array);
        }
    }
    
    @Deprecated
    public JCommander(final Object o, final String... array) {
        this(o);
        this.parse(array);
    }
    
    public void setExpandAtSign(final boolean b) {
        this.options.expandAtSign = b;
    }
    
    public static Console getConsole() {
        if (JCommander.console == null) {
            try {
                JCommander.console = new JDK6Console(System.class.getDeclaredMethod("console", (Class<?>[])new Class[0]).invoke(null, new Object[0]));
            }
            catch (Throwable t) {
                JCommander.console = new DefaultConsole();
            }
        }
        return JCommander.console;
    }
    
    public final void addObject(final Object o) {
        if (o instanceof Iterable) {
            final Iterator<Object> iterator = (Iterator<Object>)((Iterable)o).iterator();
            while (iterator.hasNext()) {
                this.objects.add(iterator.next());
            }
            return;
        }
        if (o.getClass().isArray()) {
            Object[] array;
            for (int length = (array = (Object[])o).length, i = 0; i < length; ++i) {
                this.objects.add(array[i]);
            }
            return;
        }
        this.objects.add(o);
    }
    
    public final void setDescriptionsBundle(final ResourceBundle resourceBundle) {
        this.options.bundle = resourceBundle;
    }
    
    public void parse(final String... array) {
        try {
            this.parse(true, array);
        }
        catch (ParameterException ex2) {
            final ParameterException ex = ex2;
            ex2.setJCommander(this);
            throw ex;
        }
    }
    
    public void parseWithoutValidation(final String... array) {
        this.parse(false, array);
    }
    
    private void parse(final boolean b, final String... array) {
        final StringBuilder sb;
        (sb = new StringBuilder("Parsing \"")).append((CharSequence)this.join(array).append("\"\n  with:").append((CharSequence)this.join(this.objects.toArray())));
        this.p(sb.toString());
        if (this.descriptions == null) {
            this.createDescriptions();
        }
        this.initializeDefaultValues();
        this.parseValues(this.expandArgs(array), b);
        if (b) {
            this.validateOptions();
        }
    }
    
    private StringBuilder join(final Object[] array) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(array[i]);
        }
        return sb;
    }
    
    private void initializeDefaultValues() {
        if (this.options.defaultProvider != null) {
            final Iterator<ParameterDescription> iterator = this.descriptions.values().iterator();
            while (iterator.hasNext()) {
                this.initializeDefaultValue(iterator.next());
            }
            final Iterator<Map.Entry<ProgramName, JCommander>> iterator2 = this.commands.entrySet().iterator();
            while (iterator2.hasNext()) {
                iterator2.next().getValue().initializeDefaultValues();
            }
        }
    }
    
    private void validateOptions() {
        if (this.helpWasSpecified) {
            return;
        }
        if (!this.requiredFields.isEmpty()) {
            final ArrayList<String> elements = new ArrayList<String>();
            final Iterator<ParameterDescription> iterator = this.requiredFields.values().iterator();
            while (iterator.hasNext()) {
                elements.add("[" + String.join(" | ", (CharSequence[])iterator.next().getParameter().names()) + "]");
            }
            throw new ParameterException("The following " + pluralize(this.requiredFields.size(), "option is required: ", "options are required: ") + String.join(", ", elements));
        }
        if (this.mainParameterDescription != null && this.mainParameterDescription.getParameter().required() && !this.mainParameterDescription.isAssigned()) {
            throw new ParameterException("Main parameters are required (\"" + this.mainParameterDescription.getDescription() + "\")");
        }
    }
    
    private static String pluralize(final int n, final String s, final String s2) {
        if (n == 1) {
            return s;
        }
        return s2;
    }
    
    private String[] expandArgs(String[] array) {
        final List<String> arrayList = Lists.newArrayList();
        for (int length = (array = array).length, i = 0; i < length; ++i) {
            final String s;
            if ((s = array[i]).startsWith("@") && this.options.expandAtSign) {
                arrayList.addAll(this.readFile(s.substring(1)));
            }
            else {
                arrayList.addAll(this.expandDynamicArg(s));
            }
        }
        final List<String> arrayList2 = Lists.newArrayList();
        for (final String s2 : arrayList) {
            if (this.isOption(s2)) {
                final String separator = this.getSeparatorFor(s2);
                if (!" ".equals(separator)) {
                    String[] split;
                    for (int length2 = (split = s2.split("[" + separator + "]", 2)).length, j = 0; j < length2; ++j) {
                        arrayList2.add(split[j]);
                    }
                }
                else {
                    arrayList2.add(s2);
                }
            }
            else {
                arrayList2.add(s2);
            }
        }
        final List<String> list = arrayList2;
        return list.toArray(new String[list.size()]);
    }
    
    private List<String> expandDynamicArg(final String s) {
        final Iterator<ParameterDescription> iterator = this.descriptions.values().iterator();
        while (iterator.hasNext()) {
            final ParameterDescription parameterDescription;
            if ((parameterDescription = iterator.next()).isDynamicParameter()) {
                String[] names;
                for (int length = (names = parameterDescription.getParameter().names()).length, i = 0; i < length; ++i) {
                    final String s2 = names[i];
                    if (s.startsWith(s2) && !s.equals(s2)) {
                        return Arrays.asList(s2, s.substring(s2.length()));
                    }
                }
            }
        }
        return Arrays.asList(s);
    }
    
    private boolean matchArg(final String anObject, final FuzzyMap.IKey key) {
        final String prefix = this.options.caseSensitiveOptions ? key.getName() : key.getName().toLowerCase();
        if (this.options.allowAbbreviatedOptions) {
            if (prefix.startsWith(anObject)) {
                return true;
            }
        }
        else if (this.descriptions.get(key) != null) {
            if (!" ".equals(this.getSeparatorFor(anObject))) {
                if (anObject.startsWith(prefix)) {
                    return true;
                }
            }
            else if (prefix.equals(anObject)) {
                return true;
            }
        }
        else if (prefix.equals(anObject)) {
            return true;
        }
        return false;
    }
    
    private boolean isOption(String s) {
        if (this.options.acceptUnknownOptions) {
            return true;
        }
        s = (this.options.caseSensitiveOptions ? s : s.toLowerCase());
        final Iterator<FuzzyMap.IKey> iterator = this.descriptions.keySet().iterator();
        while (iterator.hasNext()) {
            if (this.matchArg(s, iterator.next())) {
                return true;
            }
        }
        final Iterator<ProgramName> iterator2 = this.commands.keySet().iterator();
        while (iterator2.hasNext()) {
            if (this.matchArg(s, iterator2.next())) {
                return true;
            }
        }
        return false;
    }
    
    private ParameterDescription getPrefixDescriptionFor(final String s) {
        for (final Map.Entry<FuzzyMap.IKey, ParameterDescription> entry : this.descriptions.entrySet()) {
            if (s.startsWith(entry.getKey().getName())) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    private ParameterDescription getDescriptionFor(final String s) {
        return this.getPrefixDescriptionFor(s);
    }
    
    private String getSeparatorFor(final String s) {
        final ParameterDescription description;
        final Parameters parameters;
        if ((description = this.getDescriptionFor(s)) != null && (parameters = description.getObject().getClass().getAnnotation(Parameters.class)) != null) {
            return parameters.separators();
        }
        return " ";
    }
    
    private List<String> readFile(final String s) {
        final List<String> arrayList = Lists.newArrayList();
        try {
            final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(s, new String[0]), this.options.atFileCharset);
            Throwable t = null;
            try {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.length() > 0 && !line.trim().startsWith("#")) {
                        arrayList.add(line);
                    }
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }
            catch (Throwable t3) {
                final Throwable t2 = t3;
                t = t3;
                throw t2;
            }
            finally {
                if (bufferedReader != null) {
                    if (t != null) {
                        try {
                            bufferedReader.close();
                        }
                        catch (Throwable exception) {
                            t.addSuppressed(exception);
                        }
                    }
                    else {
                        bufferedReader.close();
                    }
                }
            }
        }
        catch (IOException obj) {
            throw new ParameterException("Could not read file " + s + ": " + obj);
        }
        return;
    }
    
    private static String trim(String s) {
        if ((s = s.trim()).startsWith("\"") && s.endsWith("\"") && s.length() > 1) {
            s = s.substring(1, s.length() - 1);
        }
        return s;
    }
    
    private void createDescriptions() {
        this.descriptions = Maps.newHashMap();
        final Iterator<Object> iterator = this.objects.iterator();
        while (iterator.hasNext()) {
            this.addDescription(iterator.next());
        }
    }
    
    private void addDescription(final Object mainParameterObject) {
        mainParameterObject.getClass();
        final Iterator<Parameterized> iterator = Parameterized.parseArg(mainParameterObject).iterator();
        while (iterator.hasNext()) {
            final Parameterized mainParameter;
            final WrappedParameter wrappedParameter;
            if ((wrappedParameter = (mainParameter = iterator.next()).getWrappedParameter()) != null && wrappedParameter.getParameter() != null) {
                final Parameter parameter;
                if ((parameter = wrappedParameter.getParameter()).names().length == 0) {
                    this.p("Found main parameter:" + mainParameter);
                    if (this.mainParameter != null) {
                        throw new ParameterException("Only one @Parameter with no names attribute is allowed, found:" + this.mainParameter + " and " + mainParameter);
                    }
                    this.mainParameter = mainParameter;
                    this.mainParameterObject = mainParameterObject;
                    this.mainParameterAnnotation = parameter;
                    this.mainParameterDescription = new ParameterDescription(mainParameterObject, parameter, mainParameter, this.options.bundle, this);
                }
                else {
                    final ParameterDescription parameterDescription = new ParameterDescription(mainParameterObject, parameter, mainParameter, this.options.bundle, this);
                    String[] names;
                    for (int length = (names = parameter.names()).length, i = 0; i < length; ++i) {
                        final String s = names[i];
                        if (this.descriptions.containsKey(new StringKey(s))) {
                            throw new ParameterException("Found the option " + s + " multiple times");
                        }
                        this.p("Adding description for " + s);
                        this.fields.put(mainParameter, parameterDescription);
                        this.descriptions.put(new StringKey(s), parameterDescription);
                        if (parameter.required()) {
                            this.requiredFields.put(mainParameter, parameterDescription);
                        }
                    }
                }
            }
            else if (mainParameter.getDelegateAnnotation() != null) {
                final Object value;
                if ((value = mainParameter.get(mainParameterObject)) == null) {
                    throw new ParameterException("Delegate field '" + mainParameter.getName() + "' cannot be null.");
                }
                this.addDescription(value);
            }
            else {
                if (wrappedParameter == null || wrappedParameter.getDynamicParameter() == null) {
                    continue;
                }
                DynamicParameter dynamicParameter;
                String[] names2;
                for (int length2 = (names2 = (dynamicParameter = wrappedParameter.getDynamicParameter()).names()).length, j = 0; j < length2; ++j) {
                    final String s2 = names2[j];
                    if (this.descriptions.containsKey(s2)) {
                        throw new ParameterException("Found the option " + s2 + " multiple times");
                    }
                    this.p("Adding description for " + s2);
                    final ParameterDescription parameterDescription2 = new ParameterDescription(mainParameterObject, dynamicParameter, mainParameter, this.options.bundle, this);
                    this.fields.put(mainParameter, parameterDescription2);
                    this.descriptions.put(new StringKey(s2), parameterDescription2);
                    if (dynamicParameter.required()) {
                        this.requiredFields.put(mainParameter, parameterDescription2);
                    }
                }
            }
        }
    }
    
    private void initializeDefaultValue(final ParameterDescription parameterDescription) {
        String[] names;
        for (int length = (names = parameterDescription.getParameter().names()).length, i = 0; i < length; ++i) {
            final String str = names[i];
            final String defaultValue;
            if ((defaultValue = this.options.defaultProvider.getDefaultValueFor(str)) != null) {
                this.p("Initializing " + str + " with default value:" + defaultValue);
                parameterDescription.addValue(defaultValue, true);
                this.requiredFields.remove(parameterDescription.getParameterized());
                return;
            }
        }
    }
    
    private void parseValues(final String[] array, final boolean b) {
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        while (n2 < array.length && n == 0) {
            final String s;
            final String trim = trim(s = array[n2]);
            array[n2] = trim;
            this.p("Parsing arg: " + trim);
            final JCommander commandByAlias = this.findCommandByAlias(s);
            int n4 = 1;
            if (n3 == 0 && !"--".equals(trim) && this.isOption(trim) && commandByAlias == null) {
                final ParameterDescription parameterDescription;
                if ((parameterDescription = this.findParameterDescription(trim)) != null) {
                    if (parameterDescription.getParameter().password()) {
                        n4 = this.processPassword(array, n2, parameterDescription, b);
                    }
                    else if (parameterDescription.getParameter().variableArity()) {
                        n4 = this.processVariableArity(array, n2, parameterDescription, b);
                    }
                    else {
                        final Class<?> type;
                        if (((type = parameterDescription.getParameterized().getType()) == Boolean.TYPE || type == Boolean.class) && parameterDescription.getParameter().arity() == -1) {
                            parameterDescription.addValue("true");
                            this.requiredFields.remove(parameterDescription.getParameterized());
                        }
                        else {
                            n4 = this.processFixedArity(array, n2, parameterDescription, b, type);
                        }
                        if (parameterDescription.isHelp()) {
                            this.helpWasSpecified = true;
                        }
                    }
                }
                else {
                    if (!this.options.acceptUnknownOptions) {
                        throw new ParameterException("Unknown option: " + s);
                    }
                    this.unknownArgs.add(s);
                    ++n2;
                    while (n2 < array.length && !this.isOption(array[n2])) {
                        this.unknownArgs.add(array[n2++]);
                    }
                    n4 = 0;
                }
            }
            else if ("--".equals(s) && n3 == 0) {
                n3 = 1;
            }
            else if (this.commands.isEmpty()) {
                final List<?> mainParameter = this.getMainParameter(s);
                Object convertValue;
                final String s2 = (String)(convertValue = trim);
                final Type type2;
                if (this.mainParameter.getGenericType() instanceof ParameterizedType && (type2 = ((ParameterizedType)this.mainParameter.getGenericType()).getActualTypeArguments()[0]) instanceof Class) {
                    convertValue = this.convertValue(this.mainParameter, (Class)type2, null, s2);
                }
                Class<? extends IParameterValidator>[] validateWith;
                for (int length = (validateWith = this.mainParameterAnnotation.validateWith()).length, i = 0; i < length; ++i) {
                    ParameterDescription.validateParameter(this.mainParameterDescription, validateWith[i], "Default", s2);
                }
                this.mainParameterDescription.setAssigned(true);
                mainParameter.add(convertValue);
            }
            else {
                if (commandByAlias == null && b) {
                    throw new MissingCommandException("Expected a command, got " + s, s);
                }
                if (commandByAlias != null) {
                    this.parsedCommand = commandByAlias.programName.name;
                    this.parsedAlias = s;
                    commandByAlias.parse(b, this.subArray(array, n2 + 1));
                    n = 1;
                }
            }
            n2 += n4;
        }
        final Iterator<ParameterDescription> iterator = this.descriptions.values().iterator();
        while (iterator.hasNext()) {
            final ParameterDescription parameterDescription2;
            if ((parameterDescription2 = iterator.next()).isAssigned()) {
                this.fields.get(parameterDescription2.getParameterized()).setAssigned(true);
            }
        }
    }
    
    private final int determineArity(final String[] array, int i, final ParameterDescription parameterDescription, final IVariableArity variableArity) {
        final List<String> arrayList = Lists.newArrayList();
        for (++i; i < array.length; ++i) {
            arrayList.add(array[i]);
        }
        return variableArity.processVariableArity(parameterDescription.getParameter().names()[0], arrayList.toArray(new String[0]));
    }
    
    private int processPassword(final String[] array, final int n, final ParameterDescription parameterDescription, final boolean b) {
        final int determineArity;
        if ((determineArity = this.determineArity(array, n, parameterDescription, this.DEFAULT_VARIABLE_ARITY)) == 0) {
            parameterDescription.addValue(new String(this.readPassword(parameterDescription.getDescription(), parameterDescription.getParameter().echoInput())));
            this.requiredFields.remove(parameterDescription.getParameterized());
            return 1;
        }
        if (determineArity == 1) {
            return this.processFixedArity(array, n, parameterDescription, b, List.class, 1);
        }
        throw new ParameterException("Password parameter must have at most 1 argument.");
    }
    
    private int processVariableArity(final String[] array, final int n, final ParameterDescription parameterDescription, final boolean b) {
        final Object object;
        IVariableArity default_VARIABLE_ARITY;
        if (!((object = parameterDescription.getObject()) instanceof IVariableArity)) {
            default_VARIABLE_ARITY = this.DEFAULT_VARIABLE_ARITY;
        }
        else {
            default_VARIABLE_ARITY = (IVariableArity)object;
        }
        return this.processFixedArity(array, n, parameterDescription, b, List.class, this.determineArity(array, n, parameterDescription, default_VARIABLE_ARITY));
    }
    
    private int processFixedArity(final String[] array, final int n, final ParameterDescription parameterDescription, final boolean b, final Class<?> clazz) {
        final int arity;
        return this.processFixedArity(array, n, parameterDescription, b, clazz, ((arity = parameterDescription.getParameter().arity()) != -1) ? arity : true);
    }
    
    private int processFixedArity(final String[] array, final int n, final ParameterDescription parameterDescription, final boolean b, final Class<?> clazz, final int i) {
        final String str = array[n];
        if (i == 0 && (Boolean.class.isAssignableFrom(clazz) || Boolean.TYPE.isAssignableFrom(clazz))) {
            parameterDescription.addValue("true");
            this.requiredFields.remove(parameterDescription.getParameterized());
        }
        else {
            if (i == 0) {
                throw new ParameterException("Expected a value after parameter " + str);
            }
            if (n >= array.length - 1) {
                throw new ParameterException("Expected a value after parameter " + str);
            }
            final int equals = "--".equals(array[n + 1]) ? 1 : 0;
            Object addValue = null;
            if (n + i >= array.length) {
                throw new ParameterException("Expected " + i + " values after " + str);
            }
            for (int j = 1; j <= i; ++j) {
                addValue = parameterDescription.addValue(str, trim(array[n + j + equals]), false, b, j - 1);
                this.requiredFields.remove(parameterDescription.getParameterized());
            }
            if (addValue != null && b) {
                parameterDescription.validateValueParameter(str, addValue);
            }
        }
        return i + 1;
    }
    
    private char[] readPassword(final String str, final boolean b) {
        getConsole().print(str + ": ");
        return getConsole().readPassword(b);
    }
    
    private String[] subArray(final String[] array, final int n) {
        final int n2;
        final String[] array2 = new String[n2 = array.length - n];
        System.arraycopy(array, n, array2, 0, n2);
        return array2;
    }
    
    private List<?> getMainParameter(final String str) {
        if (this.mainParameter == null) {
            throw new ParameterException("Was passed main parameter '" + str + "' but no main parameter was defined in your arg class");
        }
        List<?> arrayList;
        if ((arrayList = (List<?>)this.mainParameter.get(this.mainParameterObject)) == null) {
            arrayList = Lists.newArrayList();
            if (!List.class.isAssignableFrom(this.mainParameter.getType())) {
                throw new ParameterException("Main parameter field " + this.mainParameter + " needs to be of type List, not " + this.mainParameter.getType());
            }
            this.mainParameter.set(this.mainParameterObject, arrayList);
        }
        if (this.firstTimeMainParameter) {
            arrayList.clear();
            this.firstTimeMainParameter = false;
        }
        return arrayList;
    }
    
    public String getMainParameterDescription() {
        if (this.descriptions == null) {
            this.createDescriptions();
        }
        if (this.mainParameterAnnotation != null) {
            return this.mainParameterAnnotation.description();
        }
        return null;
    }
    
    public void setProgramName(final String s) {
        this.setProgramName(s, new String[0]);
    }
    
    public String getProgramName() {
        if (this.programName == null) {
            return null;
        }
        return this.programName.getName();
    }
    
    public void setProgramName(final String s, final String... a) {
        this.programName = new ProgramName(s, Arrays.asList(a));
    }
    
    public void usage(final String s) {
        final StringBuilder sb = new StringBuilder();
        this.usage(s, sb);
        getConsole().println(sb.toString());
    }
    
    public void usage(final String s, final StringBuilder sb) {
        this.usage(s, sb, "");
    }
    
    public void usage(final String s, final StringBuilder sb, final String str) {
        final String commandDescription = this.getCommandDescription(s);
        final JCommander commandByAlias = this.findCommandByAlias(s);
        if (commandDescription != null) {
            sb.append(str).append(commandDescription);
            sb.append("\n");
        }
        commandByAlias.usage(sb, str);
    }
    
    public String getCommandDescription(final String str) {
        final JCommander commandByAlias;
        if ((commandByAlias = this.findCommandByAlias(str)) == null) {
            throw new ParameterException("Asking description for unknown command: " + str);
        }
        final Parameters parameters = commandByAlias.getObjects().get(0).getClass().getAnnotation(Parameters.class);
        String s = null;
        if (parameters != null) {
            s = parameters.commandDescription();
            final String resourceBundle = parameters.resourceBundle();
            ResourceBundle resourceBundle2;
            if (!"".equals(resourceBundle)) {
                resourceBundle2 = ResourceBundle.getBundle(resourceBundle, Locale.getDefault());
            }
            else {
                resourceBundle2 = this.options.bundle;
            }
            if (resourceBundle2 != null) {
                final String commandDescriptionKey = parameters.commandDescriptionKey();
                if (!"".equals(commandDescriptionKey)) {
                    s = this.getI18nString(resourceBundle2, commandDescriptionKey, parameters.commandDescription());
                }
            }
        }
        return s;
    }
    
    private String getI18nString(final ResourceBundle resourceBundle, final String key, final String s) {
        final String s2;
        if ((s2 = ((resourceBundle != null) ? resourceBundle.getString(key) : null)) != null) {
            return s2;
        }
        return s;
    }
    
    public void usage() {
        final StringBuilder sb = new StringBuilder();
        this.usage(sb);
        getConsole().println(sb.toString());
    }
    
    public static Builder newBuilder() {
        return new Builder();
    }
    
    public void usage(final StringBuilder sb) {
        this.usage(sb, "");
    }
    
    public void usage(final StringBuilder sb, final String str) {
        if (this.descriptions == null) {
            this.createDescriptions();
        }
        final boolean b = !this.commands.isEmpty();
        final boolean b2 = !this.descriptions.isEmpty();
        final int n = str.length() + 6;
        final StringBuilder sb2;
        (sb2 = new StringBuilder()).append(str).append("Usage: ").append((this.programName != null) ? this.programName.getDisplayName() : "<main class>");
        if (b2) {
            sb2.append(" [options]");
        }
        if (b) {
            sb2.append(str).append(" [command] [command options]");
        }
        if (this.mainParameterDescription != null) {
            sb2.append(" ").append(this.mainParameterDescription.getDescription());
        }
        this.wrapDescription(sb, n, sb2.toString());
        sb.append("\n");
        final List<Object> arrayList = Lists.newArrayList();
        final Iterator<ParameterDescription> iterator = this.fields.values().iterator();
        while (iterator.hasNext()) {
            final ParameterDescription parameterDescription;
            if (!(parameterDescription = iterator.next()).getParameter().hidden()) {
                arrayList.add(parameterDescription);
                parameterDescription.getNames().length();
            }
        }
        Collections.sort(arrayList, (Comparator<? super Object>)this.getParameterDescriptionComparator());
        if (arrayList.size() > 0) {
            sb.append(str).append("  Options:\n");
        }
        final Iterator<ParameterDescription> iterator2 = arrayList.iterator();
        while (iterator2.hasNext()) {
            final ParameterDescription parameterDescription2;
            final WrappedParameter parameter = (parameterDescription2 = iterator2.next()).getParameter();
            sb.append(str).append("  " + (parameter.required() ? "* " : "  ") + parameterDescription2.getNames() + "\n");
            this.wrapDescription(sb, n, this.s(n) + parameterDescription2.getDescription());
            final Object default1 = parameterDescription2.getDefault();
            if (parameterDescription2.isDynamicParameter()) {
                sb.append("\n" + this.s(n)).append("Syntax: " + parameter.names()[0] + "key" + parameter.getAssignment() + "value");
            }
            if (default1 != null && !parameterDescription2.isHelp()) {
                final String s = Strings.isStringEmpty(default1.toString()) ? "<empty string>" : default1.toString();
                sb.append("\n" + this.s(n)).append("Default: " + (parameter.password() ? "********" : s));
            }
            final Class<?> type;
            if ((type = parameterDescription2.getParameterized().getType()).isEnum()) {
                sb.append("\n" + this.s(n)).append("Possible Values: " + EnumSet.allOf(type));
            }
            sb.append("\n");
        }
        if (b) {
            sb.append(str + "  Commands:\n");
            final Iterator<Map.Entry<ProgramName, JCommander>> iterator3 = this.commands.entrySet().iterator();
            while (iterator3.hasNext()) {
                final Map.Entry<ProgramName, JCommander> entry;
                final Parameters parameters;
                if ((parameters = (entry = iterator3.next()).getValue().getObjects().get(0).getClass().getAnnotation(Parameters.class)) == null || !parameters.hidden()) {
                    final ProgramName programName;
                    this.wrapDescription(sb, n + 6, str + "    " + (programName = entry.getKey()).getDisplayName() + "      " + this.getCommandDescription(programName.getName()));
                    sb.append("\n");
                    this.findCommandByAlias(programName.getName()).usage(sb, str + "      ");
                    sb.append("\n");
                }
            }
        }
    }
    
    private Comparator<? super ParameterDescription> getParameterDescriptionComparator() {
        return this.options.parameterDescriptionComparator;
    }
    
    public void setParameterDescriptionComparator(final Comparator<? super ParameterDescription> comparator) {
        this.options.parameterDescriptionComparator = comparator;
    }
    
    public void setColumnSize(final int n) {
        this.options.columnSize = n;
    }
    
    public int getColumnSize() {
        return this.options.columnSize;
    }
    
    private void wrapDescription(final StringBuilder sb, final int n, final String s) {
        final int columnSize = this.getColumnSize();
        final String[] split = s.split(" ");
        int n2 = 0;
        for (int i = 0; i < split.length; ++i) {
            final String s2;
            if ((s2 = split[i]).length() > columnSize || n2 + 1 + s2.length() <= columnSize) {
                sb.append(s2);
                n2 += s2.length();
                if (i != split.length - 1) {
                    sb.append(" ");
                    ++n2;
                }
            }
            else {
                sb.append("\n").append(this.s(n)).append(s2).append(" ");
                n2 = n + 1 + s2.length();
            }
        }
    }
    
    public List<ParameterDescription> getParameters() {
        return new ArrayList<ParameterDescription>(this.fields.values());
    }
    
    public ParameterDescription getMainParameter() {
        return this.mainParameterDescription;
    }
    
    private void p(final String str) {
        if (this.options.verbose > 0 || System.getProperty("jcommander.debug") != null) {
            getConsole().println("[JCommander] " + str);
        }
    }
    
    public void setDefaultProvider(final IDefaultProvider defaultProvider) {
        this.options.defaultProvider = defaultProvider;
    }
    
    public void addConverterFactory(final IStringConverterFactory stringConverterFactory) {
        this.addConverterInstanceFactory(new IStringConverterInstanceFactory() {
            @Override
            public IStringConverter<?> getConverterInstance(final Parameter parameter, final Class<?> clazz, String s) {
                final Class<? extends IStringConverter<Object>> converter = stringConverterFactory.getConverter(clazz);
                try {
                    if (s == null) {
                        s = ((parameter.names().length > 0) ? parameter.names()[0] : "[Main class]");
                    }
                    if (converter != null) {
                        return (IStringConverter<?>)instantiateConverter(s, (Class<?>)converter);
                    }
                    return null;
                }
                catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    final Object o;
                    throw new ParameterException((Throwable)o);
                }
            }
        });
    }
    
    public void addConverterInstanceFactory(final IStringConverterInstanceFactory stringConverterInstanceFactory) {
        this.options.converterInstanceFactories.add(0, stringConverterInstanceFactory);
    }
    
    private IStringConverter<?> findConverterInstance(final Parameter parameter, final Class<?> clazz, final String s) {
        final Iterator<IStringConverterInstanceFactory> iterator = this.options.converterInstanceFactories.iterator();
        while (iterator.hasNext()) {
            final IStringConverter<?> converterInstance;
            if ((converterInstance = iterator.next().getConverterInstance(parameter, clazz, s)) != null) {
                return converterInstance;
            }
        }
        return null;
    }
    
    public Object convertValue(final Parameterized parameterized, final Class clazz, String s, final String s2) {
        final Parameter parameter;
        if ((parameter = parameterized.getParameter()) == null) {
            return s2;
        }
        if (s == null) {
            s = ((parameter.names().length > 0) ? parameter.names()[0] : "[Main class]");
        }
        IStringConverter<?> converterInstance = null;
        if (clazz.isAssignableFrom(List.class)) {
            converterInstance = tryInstantiateConverter(s, parameter.listConverter());
        }
        if (clazz.isAssignableFrom(List.class) && converterInstance == null) {
            converterInstance = new DefaultListConverter<Object>(tryInstantiateConverter(null, parameter.splitter()), new IStringConverter() {
                @Override
                public Object convert(final String s) {
                    final Type fieldGenericType = parameterized.findFieldGenericType();
                    return JCommander.this.convertValue(parameterized, (Class)((fieldGenericType instanceof Class) ? fieldGenericType : String.class), null, s);
                }
            });
        }
        if (converterInstance == null) {
            converterInstance = tryInstantiateConverter(s, parameter.converter());
        }
        if (converterInstance == null) {
            converterInstance = this.findConverterInstance(parameter, clazz, s);
        }
        if (converterInstance == null && clazz.isEnum()) {
            converterInstance = new EnumConverter<Object>(s, clazz);
        }
        if (converterInstance == null) {
            converterInstance = new StringConverter();
        }
        return converterInstance.convert(s2);
    }
    
    private static <T> T tryInstantiateConverter(final String s, final Class<T> clazz) {
        if (clazz == NoConverter.class || clazz == null) {
            return null;
        }
        try {
            return (T)instantiateConverter(s, (Class<?>)clazz);
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            return null;
        }
    }
    
    private static <T> T instantiateConverter(final String s, final Class<? extends T> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<T> constructor = null;
        Constructor<?> constructor2 = null;
        Constructor[] array;
        for (int length = (array = clazz.getDeclaredConstructors()).length, i = 0; i < length; ++i) {
            final Constructor constructor3;
            (constructor3 = array[i]).setAccessible(true);
            final Class[] parameterTypes;
            if ((parameterTypes = constructor3.getParameterTypes()).length == 1 && parameterTypes[0].equals(String.class)) {
                constructor2 = (Constructor<?>)constructor3;
            }
            else if (parameterTypes.length == 0) {
                constructor = (Constructor<T>)constructor3;
            }
        }
        if (constructor2 != null) {
            return (T)constructor2.newInstance(s);
        }
        if (constructor != null) {
            return constructor.newInstance(new Object[0]);
        }
        return null;
    }
    
    public void addCommand(final String s, final Object o) {
        this.addCommand(s, o, new String[0]);
    }
    
    public void addCommand(final Object o) {
        final Parameters parameters;
        if ((parameters = o.getClass().getAnnotation(Parameters.class)) != null && parameters.commandNames().length > 0) {
            String[] commandNames;
            for (int length = (commandNames = parameters.commandNames()).length, i = 0; i < length; ++i) {
                this.addCommand(commandNames[i], o);
            }
            return;
        }
        throw new ParameterException("Trying to add command " + o.getClass().getName() + " without specifying its names in @Parameters");
    }
    
    public void addCommand(final String s, final Object o, String... array) {
        final JCommander commander;
        (commander = new JCommander(this.options)).addObject(o);
        commander.createDescriptions();
        commander.setProgramName(s, array);
        final ProgramName programName = commander.programName;
        this.commands.put(programName, commander);
        this.aliasMap.put(new StringKey(s), programName);
        for (int length = (array = array).length, i = 0; i < length; ++i) {
            final StringKey obj;
            if (!(obj = new StringKey(array[i])).equals(s)) {
                final ProgramName programName2;
                if ((programName2 = this.aliasMap.get(obj)) != null && !programName2.equals(programName)) {
                    throw new ParameterException("Cannot set alias " + obj + " for " + s + " command because it has already been defined for " + programName2.name + " command");
                }
                this.aliasMap.put(obj, programName);
            }
        }
    }
    
    public Map<String, JCommander> getCommands() {
        final Map<String, JCommander> linkedHashMap = Maps.newLinkedHashMap();
        for (final Map.Entry<ProgramName, JCommander> entry : this.commands.entrySet()) {
            linkedHashMap.put(entry.getKey().name, entry.getValue());
        }
        return linkedHashMap;
    }
    
    public String getParsedCommand() {
        return this.parsedCommand;
    }
    
    public String getParsedAlias() {
        return this.parsedAlias;
    }
    
    private String s(final int n) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            sb.append(" ");
        }
        return sb.toString();
    }
    
    public List<Object> getObjects() {
        return this.objects;
    }
    
    private ParameterDescription findParameterDescription(final String s) {
        return FuzzyMap.findInMap(this.descriptions, new StringKey(s), this.options.caseSensitiveOptions, this.options.allowAbbreviatedOptions);
    }
    
    private JCommander findCommand(final ProgramName programName) {
        return FuzzyMap.findInMap(this.commands, programName, this.options.caseSensitiveOptions, this.options.allowAbbreviatedOptions);
    }
    
    private ProgramName findProgramName(final String s) {
        return FuzzyMap.findInMap(this.aliasMap, new StringKey(s), this.options.caseSensitiveOptions, this.options.allowAbbreviatedOptions);
    }
    
    private JCommander findCommandByAlias(final String s) {
        final ProgramName programName;
        if ((programName = this.findProgramName(s)) == null) {
            return null;
        }
        final JCommander command;
        if ((command = this.findCommand(programName)) == null) {
            throw new IllegalStateException("There appears to be inconsistency in the internal command database.  This is likely a bug. Please report.");
        }
        return command;
    }
    
    public void setVerbose(final int n) {
        this.options.verbose = n;
    }
    
    public void setCaseSensitiveOptions(final boolean b) {
        this.options.caseSensitiveOptions = b;
    }
    
    public void setAllowAbbreviatedOptions(final boolean b) {
        this.options.allowAbbreviatedOptions = b;
    }
    
    public void setAcceptUnknownOptions(final boolean b) {
        this.options.acceptUnknownOptions = b;
    }
    
    public List<String> getUnknownOptions() {
        return this.unknownArgs;
    }
    
    public void setAllowParameterOverwriting(final boolean b) {
        this.options.allowParameterOverwriting = b;
    }
    
    public boolean isParameterOverwritingAllowed() {
        return this.options.allowParameterOverwriting;
    }
    
    public void setAtFileCharset(final Charset charset) {
        this.options.atFileCharset = charset;
    }
    
    private static class Options
    {
        private ResourceBundle bundle;
        private IDefaultProvider defaultProvider;
        private Comparator<? super ParameterDescription> parameterDescriptionComparator;
        private int columnSize;
        private boolean acceptUnknownOptions;
        private boolean allowParameterOverwriting;
        private boolean expandAtSign;
        private int verbose;
        private boolean caseSensitiveOptions;
        private boolean allowAbbreviatedOptions;
        private final List<IStringConverterInstanceFactory> converterInstanceFactories;
        private Charset atFileCharset;
        
        private Options() {
            this.parameterDescriptionComparator = new Comparator<ParameterDescription>() {
                @Override
                public int compare(final ParameterDescription parameterDescription, final ParameterDescription parameterDescription2) {
                    final Parameter parameterAnnotation = parameterDescription.getParameterAnnotation();
                    final Parameter parameterAnnotation2 = parameterDescription2.getParameterAnnotation();
                    if (parameterAnnotation != null && parameterAnnotation.order() != -1 && parameterAnnotation2 != null && parameterAnnotation2.order() != -1) {
                        return Integer.compare(parameterAnnotation.order(), parameterAnnotation2.order());
                    }
                    if (parameterAnnotation != null && parameterAnnotation.order() != -1) {
                        return -1;
                    }
                    if (parameterAnnotation2 != null && parameterAnnotation2.order() != -1) {
                        return 1;
                    }
                    return parameterDescription.getLongestName().compareTo(parameterDescription2.getLongestName());
                }
            };
            this.columnSize = 79;
            this.acceptUnknownOptions = false;
            this.allowParameterOverwriting = false;
            this.expandAtSign = true;
            this.verbose = 0;
            this.caseSensitiveOptions = true;
            this.allowAbbreviatedOptions = false;
            this.converterInstanceFactories = new CopyOnWriteArrayList<IStringConverterInstanceFactory>();
            this.atFileCharset = Charset.defaultCharset();
        }
    }
    
    private class DefaultVariableArity implements IVariableArity
    {
        @Override
        public int processVariableArity(final String s, final String[] array) {
            int n;
            for (n = 0; n < array.length && !JCommander.this.isOption(array[n]); ++n) {}
            return n;
        }
    }
    
    public static class Builder
    {
        private JCommander jCommander;
        private String[] args;
        
        public Builder() {
            this.jCommander = new JCommander();
            this.args = null;
        }
        
        public Builder addObject(final Object o) {
            this.jCommander.addObject(o);
            return this;
        }
        
        public Builder resourceBundle(final ResourceBundle descriptionsBundle) {
            this.jCommander.setDescriptionsBundle(descriptionsBundle);
            return this;
        }
        
        public Builder args(final String[] args) {
            this.args = args;
            return this;
        }
        
        public Builder expandAtSign(final Boolean b) {
            this.jCommander.setExpandAtSign(b);
            return this;
        }
        
        public Builder programName(final String programName) {
            this.jCommander.setProgramName(programName);
            return this;
        }
        
        public Builder columnSize(final int columnSize) {
            this.jCommander.setColumnSize(columnSize);
            return this;
        }
        
        public Builder defaultProvider(final IDefaultProvider defaultProvider) {
            this.jCommander.setDefaultProvider(defaultProvider);
            return this;
        }
        
        public Builder addConverterFactory(final IStringConverterFactory stringConverterFactory) {
            this.jCommander.addConverterFactory(stringConverterFactory);
            return this;
        }
        
        public Builder verbose(final int verbose) {
            this.jCommander.setVerbose(verbose);
            return this;
        }
        
        public Builder allowAbbreviatedOptions(final boolean allowAbbreviatedOptions) {
            this.jCommander.setAllowAbbreviatedOptions(allowAbbreviatedOptions);
            return this;
        }
        
        public Builder acceptUnknownOptions(final boolean acceptUnknownOptions) {
            this.jCommander.setAcceptUnknownOptions(acceptUnknownOptions);
            return this;
        }
        
        public Builder allowParameterOverwriting(final boolean allowParameterOverwriting) {
            this.jCommander.setAllowParameterOverwriting(allowParameterOverwriting);
            return this;
        }
        
        public Builder atFileCharset(final Charset atFileCharset) {
            this.jCommander.setAtFileCharset(atFileCharset);
            return this;
        }
        
        public Builder addConverterInstanceFactory(final IStringConverterInstanceFactory stringConverterInstanceFactory) {
            this.jCommander.addConverterInstanceFactory(stringConverterInstanceFactory);
            return this;
        }
        
        public Builder addCommand(final Object o) {
            this.jCommander.addCommand(o);
            return this;
        }
        
        public Builder addCommand(final String s, final Object o, final String... array) {
            this.jCommander.addCommand(s, o, array);
            return this;
        }
        
        public JCommander build() {
            if (this.args != null) {
                this.jCommander.parse(this.args);
            }
            return this.jCommander;
        }
    }
    
    private static final class ProgramName implements FuzzyMap.IKey
    {
        private final String name;
        private final List<String> aliases;
        
        ProgramName(final String name, final List<String> aliases) {
            this.name = name;
            this.aliases = aliases;
        }
        
        @Override
        public final String getName() {
            return this.name;
        }
        
        private String getDisplayName() {
            final StringBuilder sb;
            (sb = new StringBuilder()).append(this.name);
            if (!this.aliases.isEmpty()) {
                sb.append("(");
                final Iterator<String> iterator = this.aliases.iterator();
                while (iterator.hasNext()) {
                    sb.append(iterator.next());
                    if (iterator.hasNext()) {
                        sb.append(",");
                    }
                }
                sb.append(")");
            }
            return sb.toString();
        }
        
        @Override
        public final int hashCode() {
            return 31 + ((this.name == null) ? 0 : this.name.hashCode());
        }
        
        @Override
        public final boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final ProgramName programName = (ProgramName)o;
            if (this.name == null) {
                if (programName.name != null) {
                    return false;
                }
            }
            else if (!this.name.equals(programName.name)) {
                return false;
            }
            return true;
        }
        
        @Override
        public final String toString() {
            return this.getDisplayName();
        }
    }
}
