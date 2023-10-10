// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.SortedSet;
import com.beust.jcommander.validators.NoValidator;
import com.beust.jcommander.validators.NoValueValidator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.util.List;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ParameterDescription
{
    private Object object;
    private WrappedParameter wrappedParameter;
    private Parameter parameterAnnotation;
    private DynamicParameter dynamicParameterAnnotation;
    private Parameterized parameterized;
    private boolean assigned;
    private ResourceBundle bundle;
    private String description;
    private JCommander jCommander;
    private Object defaultObject;
    private String longestName;
    
    public ParameterDescription(final Object o, final DynamicParameter dynamicParameterAnnotation, final Parameterized parameterized, final ResourceBundle resourceBundle, final JCommander commander) {
        this.assigned = false;
        this.longestName = "";
        if (!Map.class.isAssignableFrom(parameterized.getType())) {
            throw new ParameterException("@DynamicParameter " + parameterized.getName() + " should be of type Map but is " + parameterized.getType().getName());
        }
        this.dynamicParameterAnnotation = dynamicParameterAnnotation;
        this.wrappedParameter = new WrappedParameter(this.dynamicParameterAnnotation);
        this.init(o, parameterized, resourceBundle, commander);
    }
    
    public ParameterDescription(final Object o, final Parameter parameterAnnotation, final Parameterized parameterized, final ResourceBundle resourceBundle, final JCommander commander) {
        this.assigned = false;
        this.longestName = "";
        this.parameterAnnotation = parameterAnnotation;
        this.wrappedParameter = new WrappedParameter(this.parameterAnnotation);
        this.init(o, parameterized, resourceBundle, commander);
    }
    
    private ResourceBundle findResourceBundle(final Object o) {
        ResourceBundle resourceBundle = null;
        final Parameters parameters;
        if ((parameters = o.getClass().getAnnotation(Parameters.class)) != null && !this.isEmpty(parameters.resourceBundle())) {
            resourceBundle = ResourceBundle.getBundle(parameters.resourceBundle(), Locale.getDefault());
        }
        else {
            final com.beust.jcommander.ResourceBundle resourceBundle2;
            if ((resourceBundle2 = o.getClass().getAnnotation(com.beust.jcommander.ResourceBundle.class)) != null && !this.isEmpty(resourceBundle2.value())) {
                resourceBundle = ResourceBundle.getBundle(resourceBundle2.value(), Locale.getDefault());
            }
        }
        return resourceBundle;
    }
    
    private boolean isEmpty(final String anObject) {
        return anObject == null || "".equals(anObject);
    }
    
    private void initDescription(final String description, final String s, final String[] array) {
        this.description = description;
        if (!"".equals(s) && this.bundle != null) {
            this.description = this.bundle.getString(s);
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            final String longestName;
            if ((longestName = array[i]).length() > this.longestName.length()) {
                this.longestName = longestName;
            }
        }
    }
    
    private void init(final Object object, final Parameterized parameterized, final ResourceBundle bundle, final JCommander jCommander) {
        this.object = object;
        this.parameterized = parameterized;
        this.bundle = bundle;
        if (this.bundle == null) {
            this.bundle = this.findResourceBundle(object);
        }
        this.jCommander = jCommander;
        if (this.parameterAnnotation != null) {
            String s;
            if (Enum.class.isAssignableFrom(parameterized.getType()) && this.parameterAnnotation.description().isEmpty()) {
                s = "Options: " + EnumSet.allOf(parameterized.getType());
            }
            else {
                s = this.parameterAnnotation.description();
            }
            this.initDescription(s, this.parameterAnnotation.descriptionKey(), this.parameterAnnotation.names());
        }
        else {
            if (this.dynamicParameterAnnotation == null) {
                throw new AssertionError((Object)"Shound never happen");
            }
            this.initDescription(this.dynamicParameterAnnotation.description(), this.dynamicParameterAnnotation.descriptionKey(), this.dynamicParameterAnnotation.names());
        }
        try {
            this.defaultObject = parameterized.get(object);
        }
        catch (Exception ex) {}
        if (this.defaultObject != null && this.parameterAnnotation != null) {
            this.validateDefaultValues(this.parameterAnnotation.names());
        }
    }
    
    private void validateDefaultValues(final String[] array) {
        this.validateValueParameter((array.length > 0) ? array[0] : "", this.defaultObject);
    }
    
    public String getLongestName() {
        return this.longestName;
    }
    
    public Object getDefault() {
        return this.defaultObject;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public Object getObject() {
        return this.object;
    }
    
    public String getNames() {
        final StringBuilder sb = new StringBuilder();
        final String[] names = this.wrappedParameter.names();
        for (int i = 0; i < names.length; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(names[i]);
        }
        return sb.toString();
    }
    
    public WrappedParameter getParameter() {
        return this.wrappedParameter;
    }
    
    public Parameterized getParameterized() {
        return this.parameterized;
    }
    
    private boolean isMultiOption() {
        final Class<?> type;
        return (type = this.parameterized.getType()).equals(List.class) || type.equals(Set.class) || this.parameterized.isDynamicParameter();
    }
    
    public void addValue(final String s) {
        this.addValue(s, false);
    }
    
    public boolean isAssigned() {
        return this.assigned;
    }
    
    public void setAssigned(final boolean assigned) {
        this.assigned = assigned;
    }
    
    public void addValue(final String s, final boolean b) {
        this.addValue(null, s, b, true, -1);
    }
    
    Object addValue(String str, final String str2, final boolean b, final boolean b2, final int n) {
        p("Adding " + (b ? "default " : "") + "value:" + str2 + " to parameter:" + this.parameterized.getName());
        if (str == null) {
            str = this.wrappedParameter.names()[0];
        }
        if ((n == 0 && this.assigned && !this.isMultiOption() && !this.jCommander.isParameterOverwritingAllowed()) || this.isNonOverwritableForced()) {
            throw new ParameterException("Can only specify option " + str + " once.");
        }
        if (b2) {
            this.validateParameter(str, str2);
        }
        final Class<?> type = this.parameterized.getType();
        final Object convertValue = this.jCommander.convertValue(this.getParameterized(), this.getParameterized().getType(), str, str2);
        if (b2) {
            this.validateValueParameter(str, convertValue);
        }
        Object handleSubParameters;
        if (Collection.class.isAssignableFrom(type)) {
            Collection<Object> collection;
            if ((collection = (Collection<Object>)this.parameterized.get(this.object)) == null || this.fieldIsSetForTheFirstTime(b)) {
                collection = this.newCollection(type);
                this.parameterized.set(this.object, collection);
            }
            if (convertValue instanceof Collection) {
                collection.addAll((Collection<?>)convertValue);
            }
            else {
                collection.add(convertValue);
            }
            handleSubParameters = collection;
        }
        else {
            final List<SubParameterIndex> subParameters;
            if (!(subParameters = this.findSubParameters(type)).isEmpty()) {
                handleSubParameters = this.handleSubParameters(str2, n, type, subParameters);
            }
            else {
                this.wrappedParameter.addValue(this.parameterized, this.object, convertValue);
                handleSubParameters = convertValue;
            }
        }
        if (!b) {
            this.assigned = true;
        }
        return handleSubParameters;
    }
    
    private Object handleSubParameters(final String s, final int n, final Class<?> obj, final List<SubParameterIndex> list) {
        SubParameterIndex subParameterIndex = null;
        final Iterator<SubParameterIndex> iterator = list.iterator();
        while (iterator.hasNext()) {
            final SubParameterIndex subParameterIndex2;
            if ((subParameterIndex2 = iterator.next()).order == n) {
                subParameterIndex = subParameterIndex2;
                break;
            }
        }
        if (subParameterIndex != null) {
            Object o = this.parameterized.get(this.object);
            try {
                if (o == null) {
                    o = obj.newInstance();
                    this.parameterized.set(this.object, o);
                }
                this.wrappedParameter.addValue(this.parameterized, o, s, subParameterIndex.field);
                return o;
            }
            catch (InstantiationException | IllegalAccessException ex) {
                final Object o2;
                throw new ParameterException("Couldn't instantiate " + obj, (Throwable)o2);
            }
            throw new ParameterException("Couldn't find where to assign parameter " + s + " in " + obj);
        }
        throw new ParameterException("Couldn't find where to assign parameter " + s + " in " + obj);
    }
    
    public Parameter getParameterAnnotation() {
        return this.parameterAnnotation;
    }
    
    private List<SubParameterIndex> findSubParameters(final Class<?> clazz) {
        final ArrayList<SubParameterIndex> list = new ArrayList<SubParameterIndex>();
        Field[] declaredFields;
        for (int length = (declaredFields = clazz.getDeclaredFields()).length, i = 0; i < length; ++i) {
            final Field field;
            final SubParameter annotation;
            if ((annotation = (field = declaredFields[i]).getAnnotation(SubParameter.class)) != null) {
                list.add(new SubParameterIndex(annotation.order(), field));
            }
        }
        return list;
    }
    
    private void validateParameter(final String s, final String s2) {
        final Class<? extends IParameterValidator>[] validateWith;
        if ((validateWith = this.wrappedParameter.validateWith()) != null && validateWith.length > 0) {
            Class<? extends IParameterValidator>[] array;
            for (int length = (array = validateWith).length, i = 0; i < length; ++i) {
                validateParameter(this, array[i], s, s2);
            }
        }
    }
    
    void validateValueParameter(final String s, final Object o) {
        final Class<? extends IValueValidator>[] validateValueWith;
        if ((validateValueWith = this.wrappedParameter.validateValueWith()) != null && validateValueWith.length > 0) {
            Class<? extends IValueValidator>[] array;
            for (int length = (array = validateValueWith).length, i = 0; i < length; ++i) {
                validateValueParameter(array[i], s, o);
            }
        }
    }
    
    public static void validateValueParameter(final Class<? extends IValueValidator> obj, final String str, final Object obj2) {
        try {
            if (obj != NoValueValidator.class) {
                p("Validating value parameter:" + str + " value:" + obj2 + " validator:" + obj);
            }
            obj.newInstance().validate(str, obj2);
        }
        catch (InstantiationException | IllegalAccessException ex) {
            final Object obj3;
            throw new ParameterException("Can't instantiate validator:" + obj3);
        }
    }
    
    public static void validateParameter(final ParameterDescription parameterDescription, final Class<? extends IParameterValidator> obj, final String str, final String str2) {
        try {
            if (obj != NoValidator.class) {
                p("Validating parameter:" + str + " value:" + str2 + " validator:" + obj);
            }
            obj.newInstance().validate(str, str2);
            if (IParameterValidator2.class.isAssignableFrom(obj)) {
                ((IParameterValidator2)obj.newInstance()).validate(str, str2, parameterDescription);
            }
        }
        catch (InstantiationException | IllegalAccessException ex3) {
            final Object obj2;
            throw new ParameterException("Can't instantiate validator:" + obj2);
        }
        catch (ParameterException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            throw new ParameterException(ex2);
        }
    }
    
    private Collection<Object> newCollection(final Class<?> clazz) {
        if (SortedSet.class.isAssignableFrom(clazz)) {
            return new TreeSet<Object>();
        }
        if (LinkedHashSet.class.isAssignableFrom(clazz)) {
            return new LinkedHashSet<Object>();
        }
        if (Set.class.isAssignableFrom(clazz)) {
            return new HashSet<Object>();
        }
        if (List.class.isAssignableFrom(clazz)) {
            return new ArrayList<Object>();
        }
        throw new ParameterException("Parameters of Collection type '" + clazz.getSimpleName() + "' are not supported. Please use List or Set instead.");
    }
    
    private boolean fieldIsSetForTheFirstTime(final boolean b) {
        return !b && !this.assigned;
    }
    
    private static void p(final String str) {
        if (System.getProperty("jcommander.debug") != null) {
            JCommander.getConsole().println("[ParameterDescription] " + str);
        }
    }
    
    @Override
    public String toString() {
        return "[ParameterDescription " + this.parameterized.getName() + "]";
    }
    
    public boolean isDynamicParameter() {
        return this.dynamicParameterAnnotation != null;
    }
    
    public boolean isHelp() {
        return this.wrappedParameter.isHelp();
    }
    
    public boolean isNonOverwritableForced() {
        return this.wrappedParameter.isNonOverwritableForced();
    }
    
    class SubParameterIndex
    {
        int order;
        Field field;
        
        public SubParameterIndex(final int order, final Field field) {
            this.order = -1;
            this.order = order;
            this.field = field;
        }
    }
}
