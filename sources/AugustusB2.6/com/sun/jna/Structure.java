// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.AbstractCollection;
import java.util.WeakHashMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.Arrays;
import java.util.LinkedList;
import java.nio.Buffer;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class Structure
{
    private static final Logger LOG;
    public static final int ALIGN_DEFAULT = 0;
    public static final int ALIGN_NONE = 1;
    public static final int ALIGN_GNUC = 2;
    public static final int ALIGN_MSVC = 3;
    protected static final int CALCULATE_SIZE = -1;
    static final Map<Class<?>, LayoutInfo> layoutInfo;
    static final Map<Class<?>, List<String>> fieldOrder;
    private Pointer memory;
    private int size;
    private int alignType;
    private String encoding;
    private int actualAlignType;
    private int structAlignment;
    private Map<String, StructField> structFields;
    private final Map<String, NativeStringTracking> nativeStrings;
    private TypeMapper typeMapper;
    private long typeInfo;
    private boolean autoRead;
    private boolean autoWrite;
    private Structure[] array;
    private boolean readCalled;
    private static final ThreadLocal<Map<Pointer, Structure>> reads;
    private static final ThreadLocal<Set<Structure>> busy;
    private static final Pointer PLACEHOLDER_MEMORY;
    
    protected Structure() {
        this(0);
    }
    
    protected Structure(final TypeMapper mapper) {
        this(null, 0, mapper);
    }
    
    protected Structure(final int alignType) {
        this(null, alignType);
    }
    
    protected Structure(final int alignType, final TypeMapper mapper) {
        this(null, alignType, mapper);
    }
    
    protected Structure(final Pointer p) {
        this(p, 0);
    }
    
    protected Structure(final Pointer p, final int alignType) {
        this(p, alignType, null);
    }
    
    protected Structure(final Pointer p, final int alignType, final TypeMapper mapper) {
        this.size = -1;
        this.nativeStrings = new HashMap<String, NativeStringTracking>(8);
        this.autoRead = true;
        this.autoWrite = true;
        this.setAlignType(alignType);
        this.setStringEncoding(Native.getStringEncoding(this.getClass()));
        this.initializeTypeMapper(mapper);
        this.validateFields();
        if (p != null) {
            this.useMemory(p, 0, true);
        }
        else {
            this.allocateMemory(-1);
        }
        this.initializeFields();
    }
    
    Map<String, StructField> fields() {
        return this.structFields;
    }
    
    TypeMapper getTypeMapper() {
        return this.typeMapper;
    }
    
    private void initializeTypeMapper(TypeMapper mapper) {
        if (mapper == null) {
            mapper = Native.getTypeMapper(this.getClass());
        }
        this.typeMapper = mapper;
        this.layoutChanged();
    }
    
    private void layoutChanged() {
        if (this.size != -1) {
            this.size = -1;
            if (this.memory instanceof AutoAllocated) {
                this.memory = null;
            }
            this.ensureAllocated();
        }
    }
    
    protected void setStringEncoding(final String encoding) {
        this.encoding = encoding;
    }
    
    protected String getStringEncoding() {
        return this.encoding;
    }
    
    protected void setAlignType(int alignType) {
        this.alignType = alignType;
        if (alignType == 0) {
            alignType = Native.getStructureAlignment(this.getClass());
            if (alignType == 0) {
                if (Platform.isWindows()) {
                    alignType = 3;
                }
                else {
                    alignType = 2;
                }
            }
        }
        this.actualAlignType = alignType;
        this.layoutChanged();
    }
    
    protected Memory autoAllocate(final int size) {
        return new AutoAllocated(size);
    }
    
    protected void useMemory(final Pointer m) {
        this.useMemory(m, 0);
    }
    
    protected void useMemory(final Pointer m, final int offset) {
        this.useMemory(m, offset, false);
    }
    
    void useMemory(final Pointer m, final int offset, final boolean force) {
        try {
            this.nativeStrings.clear();
            if (this instanceof ByValue && !force) {
                final byte[] buf = new byte[this.size()];
                m.read(0L, buf, 0, buf.length);
                this.memory.write(0L, buf, 0, buf.length);
            }
            else {
                if (this.size == -1) {
                    this.size = this.calculateSize(false);
                }
                if (this.size != -1) {
                    this.memory = m.share(offset, this.size);
                }
                else {
                    this.memory = m.share(offset);
                }
            }
            this.array = null;
            this.readCalled = false;
        }
        catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Structure exceeds provided memory bounds", e);
        }
    }
    
    protected void ensureAllocated() {
        this.ensureAllocated(false);
    }
    
    private void ensureAllocated(final boolean avoidFFIType) {
        if (this.memory == null) {
            this.allocateMemory(avoidFFIType);
        }
        else if (this.size == -1) {
            this.size = this.calculateSize(true, avoidFFIType);
            if (!(this.memory instanceof AutoAllocated)) {
                try {
                    this.memory = this.memory.share(0L, this.size);
                }
                catch (IndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Structure exceeds provided memory bounds", e);
                }
            }
        }
    }
    
    protected void allocateMemory() {
        this.allocateMemory(false);
    }
    
    private void allocateMemory(final boolean avoidFFIType) {
        this.allocateMemory(this.calculateSize(true, avoidFFIType));
    }
    
    protected void allocateMemory(int size) {
        if (size == -1) {
            size = this.calculateSize(false);
        }
        else if (size <= 0) {
            throw new IllegalArgumentException("Structure size must be greater than zero: " + size);
        }
        if (size != -1) {
            if (this.memory == null || this.memory instanceof AutoAllocated) {
                this.memory = this.autoAllocate(size);
            }
            this.size = size;
        }
    }
    
    public int size() {
        this.ensureAllocated();
        return this.size;
    }
    
    public void clear() {
        this.ensureAllocated();
        this.nativeStrings.clear();
        this.memory.clear(this.size());
    }
    
    public Pointer getPointer() {
        this.ensureAllocated();
        return this.memory;
    }
    
    static Set<Structure> busy() {
        return Structure.busy.get();
    }
    
    static Map<Pointer, Structure> reading() {
        return Structure.reads.get();
    }
    
    void conditionalAutoRead() {
        if (!this.readCalled) {
            this.autoRead();
        }
    }
    
    public void read() {
        if (this.memory == Structure.PLACEHOLDER_MEMORY) {
            return;
        }
        this.readCalled = true;
        this.ensureAllocated();
        if (!busy().add(this)) {
            return;
        }
        if (this instanceof ByReference) {
            reading().put(this.getPointer(), this);
        }
        try {
            for (final StructField structField : this.fields().values()) {
                this.readField(structField);
            }
        }
        finally {
            busy().remove(this);
            if (this instanceof ByReference && reading().get(this.getPointer()) == this) {
                reading().remove(this.getPointer());
            }
        }
    }
    
    protected int fieldOffset(final String name) {
        this.ensureAllocated();
        final StructField f = this.fields().get(name);
        if (f == null) {
            throw new IllegalArgumentException("No such field: " + name);
        }
        return f.offset;
    }
    
    public Object readField(final String name) {
        this.ensureAllocated();
        final StructField f = this.fields().get(name);
        if (f == null) {
            throw new IllegalArgumentException("No such field: " + name);
        }
        return this.readField(f);
    }
    
    Object getFieldValue(final Field field) {
        try {
            return field.get(this);
        }
        catch (Exception e) {
            throw new Error("Exception reading field '" + field.getName() + "' in " + this.getClass(), e);
        }
    }
    
    void setFieldValue(final Field field, final Object value) {
        this.setFieldValue(field, value, false);
    }
    
    private void setFieldValue(final Field field, final Object value, final boolean overrideFinal) {
        try {
            field.set(this, value);
        }
        catch (IllegalAccessException e) {
            final int modifiers = field.getModifiers();
            if (!Modifier.isFinal(modifiers)) {
                throw new Error("Unexpectedly unable to write to field '" + field.getName() + "' within " + this.getClass(), e);
            }
            if (overrideFinal) {
                throw new UnsupportedOperationException("This VM does not support Structures with final fields (field '" + field.getName() + "' within " + this.getClass() + ")", e);
            }
            throw new UnsupportedOperationException("Attempt to write to read-only field '" + field.getName() + "' within " + this.getClass(), e);
        }
    }
    
    static <T extends Structure> T updateStructureByReference(final Class<T> type, T s, final Pointer address) {
        if (address == null) {
            s = null;
        }
        else if (s == null || !address.equals(s.getPointer())) {
            final Structure s2 = reading().get(address);
            if (s2 != null && type.equals(s2.getClass())) {
                s = (T)s2;
                s.autoRead();
            }
            else {
                s = newInstance(type, address);
                s.conditionalAutoRead();
            }
        }
        else {
            s.autoRead();
        }
        return s;
    }
    
    protected Object readField(final StructField structField) {
        final int offset = structField.offset;
        Class<?> fieldType = structField.type;
        final FromNativeConverter readConverter = structField.readConverter;
        if (readConverter != null) {
            fieldType = readConverter.nativeType();
        }
        final Object currentValue = (Structure.class.isAssignableFrom(fieldType) || Callback.class.isAssignableFrom(fieldType) || (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(fieldType)) || Pointer.class.isAssignableFrom(fieldType) || NativeMapped.class.isAssignableFrom(fieldType) || fieldType.isArray()) ? this.getFieldValue(structField.field) : null;
        Object result;
        if (fieldType == String.class) {
            final Pointer p = this.memory.getPointer(offset);
            result = ((p == null) ? null : p.getString(0L, this.encoding));
        }
        else {
            result = this.memory.getValue(offset, fieldType, currentValue);
        }
        if (readConverter != null) {
            result = readConverter.fromNative(result, structField.context);
            if (currentValue != null && currentValue.equals(result)) {
                result = currentValue;
            }
        }
        if (fieldType.equals(String.class) || fieldType.equals(WString.class)) {
            if (result != null) {
                final NativeStringTracking current = new NativeStringTracking(result);
                final NativeStringTracking previous = this.nativeStrings.put(structField.name, current);
                if (previous != null) {
                    current.peer = previous.peer;
                }
            }
            else {
                this.nativeStrings.remove(structField.name);
            }
        }
        this.setFieldValue(structField.field, result, true);
        return result;
    }
    
    public void write() {
        if (this.memory == Structure.PLACEHOLDER_MEMORY) {
            return;
        }
        this.ensureAllocated();
        if (this instanceof ByValue) {
            this.getTypeInfo();
        }
        if (!busy().add(this)) {
            return;
        }
        try {
            for (final StructField sf : this.fields().values()) {
                if (!sf.isVolatile) {
                    this.writeField(sf);
                }
            }
        }
        finally {
            busy().remove(this);
        }
    }
    
    public void writeField(final String name) {
        this.ensureAllocated();
        final StructField f = this.fields().get(name);
        if (f == null) {
            throw new IllegalArgumentException("No such field: " + name);
        }
        this.writeField(f);
    }
    
    public void writeField(final String name, final Object value) {
        this.ensureAllocated();
        final StructField structField = this.fields().get(name);
        if (structField == null) {
            throw new IllegalArgumentException("No such field: " + name);
        }
        this.setFieldValue(structField.field, value);
        this.writeField(structField, value);
    }
    
    protected void writeField(final StructField structField) {
        if (structField.isReadOnly) {
            return;
        }
        final Object value = this.getFieldValue(structField.field);
        this.writeField(structField, value);
    }
    
    private void writeField(final StructField structField, Object value) {
        final int offset = structField.offset;
        Class<?> fieldType = structField.type;
        final ToNativeConverter converter = structField.writeConverter;
        if (converter != null) {
            value = converter.toNative(value, new StructureWriteContext(this, structField.field));
            fieldType = converter.nativeType();
        }
        if (String.class == fieldType || WString.class == fieldType) {
            if (value != null) {
                final NativeStringTracking current = new NativeStringTracking(value);
                final NativeStringTracking previous = this.nativeStrings.put(structField.name, current);
                if (previous != null && value.equals(previous.value)) {
                    current.peer = previous.peer;
                    return;
                }
                final boolean wide = fieldType == WString.class;
                final NativeString nativeString = wide ? new NativeString(value.toString(), true) : new NativeString(value.toString(), this.encoding);
                current.peer = nativeString;
                value = nativeString.getPointer();
            }
            else {
                this.nativeStrings.remove(structField.name);
            }
        }
        try {
            this.memory.setValue(offset, value, fieldType);
        }
        catch (IllegalArgumentException e) {
            final String msg = "Structure field \"" + structField.name + "\" was declared as " + structField.type + ((structField.type == fieldType) ? "" : (" (native type " + fieldType + ")")) + ", which is not supported within a Structure";
            throw new IllegalArgumentException(msg, e);
        }
    }
    
    protected List<String> getFieldOrder() {
        final List<String> fields = new LinkedList<String>();
        for (Class<?> clazz = this.getClass(); clazz != Structure.class; clazz = clazz.getSuperclass()) {
            final FieldOrder order = clazz.getAnnotation(FieldOrder.class);
            if (order != null) {
                fields.addAll(0, Arrays.asList(order.value()));
            }
        }
        return Collections.unmodifiableList((List<? extends String>)fields);
    }
    
    protected void sortFields(final List<Field> fields, final List<String> names) {
        for (int i = 0; i < names.size(); ++i) {
            final String name = names.get(i);
            for (int f = 0; f < fields.size(); ++f) {
                final Field field = fields.get(f);
                if (name.equals(field.getName())) {
                    Collections.swap(fields, i, f);
                    break;
                }
            }
        }
    }
    
    protected List<Field> getFieldList() {
        final List<Field> flist = new ArrayList<Field>();
        for (Class<?> cls = this.getClass(); !cls.equals(Structure.class); cls = cls.getSuperclass()) {
            final List<Field> classFields = new ArrayList<Field>();
            final Field[] fields = cls.getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                final int modifiers = fields[i].getModifiers();
                if (!Modifier.isStatic(modifiers)) {
                    if (Modifier.isPublic(modifiers)) {
                        classFields.add(fields[i]);
                    }
                }
            }
            flist.addAll(0, classFields);
        }
        return flist;
    }
    
    private List<String> fieldOrder() {
        final Class<?> clazz = this.getClass();
        synchronized (Structure.fieldOrder) {
            List<String> list = Structure.fieldOrder.get(clazz);
            if (list == null) {
                list = this.getFieldOrder();
                Structure.fieldOrder.put(clazz, list);
            }
            return list;
        }
    }
    
    public static List<String> createFieldsOrder(final List<String> baseFields, final String... extraFields) {
        return createFieldsOrder(baseFields, Arrays.asList(extraFields));
    }
    
    public static List<String> createFieldsOrder(final List<String> baseFields, final List<String> extraFields) {
        final List<String> fields = new ArrayList<String>(baseFields.size() + extraFields.size());
        fields.addAll(baseFields);
        fields.addAll(extraFields);
        return Collections.unmodifiableList((List<? extends String>)fields);
    }
    
    public static List<String> createFieldsOrder(final String field) {
        return Collections.unmodifiableList((List<? extends String>)Collections.singletonList((T)field));
    }
    
    public static List<String> createFieldsOrder(final String... fields) {
        return Collections.unmodifiableList((List<? extends String>)Arrays.asList((T[])fields));
    }
    
    private static <T extends Comparable<T>> List<T> sort(final Collection<? extends T> c) {
        final List<T> list = new ArrayList<T>(c);
        Collections.sort(list);
        return list;
    }
    
    protected List<Field> getFields(final boolean force) {
        final List<Field> flist = this.getFieldList();
        final Set<String> names = new HashSet<String>();
        for (final Field f : flist) {
            names.add(f.getName());
        }
        final List<String> fieldOrder = this.fieldOrder();
        if (fieldOrder.size() != flist.size() && flist.size() > 1) {
            if (force) {
                throw new Error("Structure.getFieldOrder() on " + this.getClass() + ((fieldOrder.size() < flist.size()) ? " does not provide enough" : " provides too many") + " names [" + fieldOrder.size() + "] (" + sort((Collection<? extends Comparable>)fieldOrder) + ") to match declared fields [" + flist.size() + "] (" + sort((Collection<? extends Comparable>)names) + ")");
            }
            return null;
        }
        else {
            final Set<String> orderedNames = new HashSet<String>(fieldOrder);
            if (!orderedNames.equals(names)) {
                throw new Error("Structure.getFieldOrder() on " + this.getClass() + " returns names (" + sort((Collection<? extends Comparable>)fieldOrder) + ") which do not match declared field names (" + sort((Collection<? extends Comparable>)names) + ")");
            }
            this.sortFields(flist, fieldOrder);
            return flist;
        }
    }
    
    protected int calculateSize(final boolean force) {
        return this.calculateSize(force, false);
    }
    
    static int size(final Class<? extends Structure> type) {
        return size(type, (Structure)null);
    }
    
    static <T extends Structure> int size(final Class<T> type, T value) {
        final LayoutInfo info;
        synchronized (Structure.layoutInfo) {
            info = Structure.layoutInfo.get(type);
        }
        int sz = (info != null && !info.variable) ? info.size : -1;
        if (sz == -1) {
            if (value == null) {
                value = newInstance(type, Structure.PLACEHOLDER_MEMORY);
            }
            sz = value.size();
        }
        return sz;
    }
    
    int calculateSize(final boolean force, final boolean avoidFFIType) {
        int size = -1;
        final Class<?> clazz = this.getClass();
        LayoutInfo info;
        synchronized (Structure.layoutInfo) {
            info = Structure.layoutInfo.get(clazz);
        }
        if (info == null || this.alignType != info.alignType || this.typeMapper != info.typeMapper) {
            info = this.deriveLayout(force, avoidFFIType);
        }
        if (info != null) {
            this.structAlignment = info.alignment;
            this.structFields = info.fields;
            if (!info.variable) {
                synchronized (Structure.layoutInfo) {
                    if (!Structure.layoutInfo.containsKey(clazz) || this.alignType != 0 || this.typeMapper != null) {
                        Structure.layoutInfo.put(clazz, info);
                    }
                }
            }
            size = info.size;
        }
        return size;
    }
    
    private void validateField(final String name, final Class<?> type) {
        if (this.typeMapper != null) {
            final ToNativeConverter toNative = this.typeMapper.getToNativeConverter(type);
            if (toNative != null) {
                this.validateField(name, toNative.nativeType());
                return;
            }
        }
        if (type.isArray()) {
            this.validateField(name, type.getComponentType());
        }
        else {
            try {
                this.getNativeSize(type);
            }
            catch (IllegalArgumentException e) {
                final String msg = "Invalid Structure field in " + this.getClass() + ", field name '" + name + "' (" + type + "): " + e.getMessage();
                throw new IllegalArgumentException(msg, e);
            }
        }
    }
    
    private void validateFields() {
        final List<Field> fields = this.getFieldList();
        for (final Field f : fields) {
            this.validateField(f.getName(), f.getType());
        }
    }
    
    private LayoutInfo deriveLayout(final boolean force, final boolean avoidFFIType) {
        int calculatedSize = 0;
        final List<Field> fields = this.getFields(force);
        if (fields == null) {
            return null;
        }
        final LayoutInfo info = new LayoutInfo();
        info.alignType = this.alignType;
        info.typeMapper = this.typeMapper;
        boolean firstField = true;
        for (final Field field : fields) {
            final int modifiers = field.getModifiers();
            final Class<?> type = field.getType();
            if (type.isArray()) {
                info.variable = true;
            }
            final StructField structField = new StructField();
            structField.isVolatile = Modifier.isVolatile(modifiers);
            structField.isReadOnly = Modifier.isFinal(modifiers);
            if (structField.isReadOnly) {
                if (!Platform.RO_FIELDS) {
                    throw new IllegalArgumentException("This VM does not support read-only fields (field '" + field.getName() + "' within " + this.getClass() + ")");
                }
                field.setAccessible(true);
            }
            structField.field = field;
            structField.name = field.getName();
            structField.type = type;
            if (Callback.class.isAssignableFrom(type) && !type.isInterface()) {
                throw new IllegalArgumentException("Structure Callback field '" + field.getName() + "' must be an interface");
            }
            if (type.isArray() && Structure.class.equals(type.getComponentType())) {
                final String msg = "Nested Structure arrays must use a derived Structure type so that the size of the elements can be determined";
                throw new IllegalArgumentException(msg);
            }
            int fieldAlignment = 1;
            if (Modifier.isPublic(field.getModifiers())) {
                Object value = this.getFieldValue(structField.field);
                if (value == null && type.isArray()) {
                    if (force) {
                        throw new IllegalStateException("Array fields must be initialized");
                    }
                    return null;
                }
                else {
                    Class<?> nativeType = type;
                    if (NativeMapped.class.isAssignableFrom(type)) {
                        final NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
                        nativeType = tc.nativeType();
                        structField.writeConverter = tc;
                        structField.readConverter = tc;
                        structField.context = new StructureReadContext(this, field);
                    }
                    else if (this.typeMapper != null) {
                        final ToNativeConverter writeConverter = this.typeMapper.getToNativeConverter(type);
                        final FromNativeConverter readConverter = this.typeMapper.getFromNativeConverter(type);
                        if (writeConverter != null && readConverter != null) {
                            value = writeConverter.toNative(value, new StructureWriteContext(this, structField.field));
                            nativeType = ((value != null) ? value.getClass() : Pointer.class);
                            structField.writeConverter = writeConverter;
                            structField.readConverter = readConverter;
                            structField.context = new StructureReadContext(this, field);
                        }
                        else if (writeConverter != null || readConverter != null) {
                            final String msg2 = "Structures require bidirectional type conversion for " + type;
                            throw new IllegalArgumentException(msg2);
                        }
                    }
                    if (value == null) {
                        value = this.initializeField(structField.field, type);
                    }
                    try {
                        structField.size = this.getNativeSize(nativeType, value);
                        fieldAlignment = this.getNativeAlignment(nativeType, value, firstField);
                    }
                    catch (IllegalArgumentException e) {
                        if (!force && this.typeMapper == null) {
                            return null;
                        }
                        final String msg3 = "Invalid Structure field in " + this.getClass() + ", field name '" + structField.name + "' (" + structField.type + "): " + e.getMessage();
                        throw new IllegalArgumentException(msg3, e);
                    }
                    if (fieldAlignment == 0) {
                        throw new Error("Field alignment is zero for field '" + structField.name + "' within " + this.getClass());
                    }
                    info.alignment = Math.max(info.alignment, fieldAlignment);
                    if (calculatedSize % fieldAlignment != 0) {
                        calculatedSize += fieldAlignment - calculatedSize % fieldAlignment;
                    }
                    if (this instanceof Union) {
                        structField.offset = 0;
                        calculatedSize = Math.max(calculatedSize, structField.size);
                    }
                    else {
                        structField.offset = calculatedSize;
                        calculatedSize += structField.size;
                    }
                    info.fields.put(structField.name, structField);
                }
            }
            firstField = false;
        }
        if (calculatedSize > 0) {
            final int size = this.addPadding(calculatedSize, info.alignment);
            if (this instanceof ByValue && !avoidFFIType) {
                this.getTypeInfo();
            }
            info.size = size;
            return info;
        }
        throw new IllegalArgumentException("Structure " + this.getClass() + " has unknown or zero size (ensure all fields are public)");
    }
    
    private void initializeFields() {
        final List<Field> flist = this.getFieldList();
        for (final Field f : flist) {
            try {
                final Object o = f.get(this);
                if (o != null) {
                    continue;
                }
                this.initializeField(f, f.getType());
            }
            catch (Exception e) {
                throw new Error("Exception reading field '" + f.getName() + "' in " + this.getClass(), e);
            }
        }
    }
    
    private Object initializeField(final Field field, final Class<?> type) {
        Object value = null;
        if (Structure.class.isAssignableFrom(type) && !ByReference.class.isAssignableFrom(type)) {
            try {
                value = newInstance(type, Structure.PLACEHOLDER_MEMORY);
                this.setFieldValue(field, value);
                return value;
            }
            catch (IllegalArgumentException e) {
                final String msg = "Can't determine size of nested structure";
                throw new IllegalArgumentException(msg, e);
            }
        }
        if (NativeMapped.class.isAssignableFrom(type)) {
            final NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
            value = tc.defaultValue();
            this.setFieldValue(field, value);
        }
        return value;
    }
    
    private int addPadding(final int calculatedSize) {
        return this.addPadding(calculatedSize, this.structAlignment);
    }
    
    private int addPadding(int calculatedSize, final int alignment) {
        if (this.actualAlignType != 1 && calculatedSize % alignment != 0) {
            calculatedSize += alignment - calculatedSize % alignment;
        }
        return calculatedSize;
    }
    
    protected int getStructAlignment() {
        if (this.size == -1) {
            this.calculateSize(true);
        }
        return this.structAlignment;
    }
    
    protected int getNativeAlignment(Class<?> type, Object value, final boolean isFirstElement) {
        int alignment = 1;
        if (NativeMapped.class.isAssignableFrom(type)) {
            final NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
            type = tc.nativeType();
            value = tc.toNative(value, new ToNativeContext());
        }
        final int size = Native.getNativeSize(type, value);
        if (type.isPrimitive() || Long.class == type || Integer.class == type || Short.class == type || Character.class == type || Byte.class == type || Boolean.class == type || Float.class == type || Double.class == type) {
            alignment = size;
        }
        else if ((Pointer.class.isAssignableFrom(type) && !Function.class.isAssignableFrom(type)) || (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(type)) || Callback.class.isAssignableFrom(type) || WString.class == type || String.class == type) {
            alignment = Native.POINTER_SIZE;
        }
        else if (Structure.class.isAssignableFrom(type)) {
            if (ByReference.class.isAssignableFrom(type)) {
                alignment = Native.POINTER_SIZE;
            }
            else {
                if (value == null) {
                    value = newInstance(type, Structure.PLACEHOLDER_MEMORY);
                }
                alignment = ((Structure)value).getStructAlignment();
            }
        }
        else {
            if (!type.isArray()) {
                throw new IllegalArgumentException("Type " + type + " has unknown native alignment");
            }
            alignment = this.getNativeAlignment(type.getComponentType(), null, isFirstElement);
        }
        if (this.actualAlignType == 1) {
            alignment = 1;
        }
        else if (this.actualAlignType == 3) {
            alignment = Math.min(8, alignment);
        }
        else if (this.actualAlignType == 2) {
            if (!isFirstElement || !Platform.isMac() || !Platform.isPPC()) {
                alignment = Math.min(Native.MAX_ALIGNMENT, alignment);
            }
            if (!isFirstElement && Platform.isAIX() && (type == Double.TYPE || type == Double.class)) {
                alignment = 4;
            }
        }
        return alignment;
    }
    
    @Override
    public String toString() {
        return this.toString(Boolean.getBoolean("jna.dump_memory"));
    }
    
    public String toString(final boolean debug) {
        return this.toString(0, true, debug);
    }
    
    private String format(final Class<?> type) {
        final String s = type.getName();
        final int dot = s.lastIndexOf(".");
        return s.substring(dot + 1);
    }
    
    private String toString(final int indent, final boolean showContents, final boolean dumpMemory) {
        this.ensureAllocated();
        final String LS = System.getProperty("line.separator");
        String name = this.format(this.getClass()) + "(" + this.getPointer() + ")";
        if (!(this.getPointer() instanceof Memory)) {
            name = name + " (" + this.size() + " bytes)";
        }
        String prefix = "";
        for (int idx = 0; idx < indent; ++idx) {
            prefix += "  ";
        }
        String contents = LS;
        if (!showContents) {
            contents = "...}";
        }
        else {
            final Iterator<StructField> i = this.fields().values().iterator();
            while (i.hasNext()) {
                final StructField sf = i.next();
                Object value = this.getFieldValue(sf.field);
                String type = this.format(sf.type);
                String index = "";
                contents += prefix;
                if (sf.type.isArray() && value != null) {
                    type = this.format(sf.type.getComponentType());
                    index = "[" + Array.getLength(value) + "]";
                }
                contents += String.format("  %s %s%s@0x%X", type, sf.name, index, sf.offset);
                if (value instanceof Structure) {
                    value = ((Structure)value).toString(indent + 1, !(value instanceof ByReference), dumpMemory);
                }
                contents += "=";
                if (value instanceof Long) {
                    contents += String.format("0x%08X", (Long)value);
                }
                else if (value instanceof Integer) {
                    contents += String.format("0x%04X", (Integer)value);
                }
                else if (value instanceof Short) {
                    contents += String.format("0x%02X", (Short)value);
                }
                else if (value instanceof Byte) {
                    contents += String.format("0x%01X", (Byte)value);
                }
                else {
                    contents += String.valueOf(value).trim();
                }
                contents += LS;
                if (!i.hasNext()) {
                    contents = contents + prefix + "}";
                }
            }
        }
        if (indent == 0 && dumpMemory) {
            final int BYTES_PER_ROW = 4;
            contents = contents + LS + "memory dump" + LS;
            final byte[] buf = this.getPointer().getByteArray(0L, this.size());
            for (int j = 0; j < buf.length; ++j) {
                if (j % 4 == 0) {
                    contents += "[";
                }
                if (buf[j] >= 0 && buf[j] < 16) {
                    contents += "0";
                }
                contents += Integer.toHexString(buf[j] & 0xFF);
                if (j % 4 == 3 && j < buf.length - 1) {
                    contents = contents + "]" + LS;
                }
            }
            contents += "]";
        }
        return name + " {" + contents;
    }
    
    public Structure[] toArray(final Structure[] array) {
        this.ensureAllocated();
        if (this.memory instanceof AutoAllocated) {
            final Memory m = (Memory)this.memory;
            final int requiredSize = array.length * this.size();
            if (m.size() < requiredSize) {
                this.useMemory(this.autoAllocate(requiredSize));
            }
        }
        array[0] = this;
        final int size = this.size();
        for (int i = 1; i < array.length; ++i) {
            (array[i] = newInstance(this.getClass(), this.memory.share(i * size, size))).conditionalAutoRead();
        }
        if (!(this instanceof ByValue)) {
            this.array = array;
        }
        return array;
    }
    
    public Structure[] toArray(final int size) {
        return this.toArray((Structure[])Array.newInstance(this.getClass(), size));
    }
    
    private Class<?> baseClass() {
        if ((this instanceof ByReference || this instanceof ByValue) && Structure.class.isAssignableFrom(this.getClass().getSuperclass())) {
            return this.getClass().getSuperclass();
        }
        return this.getClass();
    }
    
    public boolean dataEquals(final Structure s) {
        return this.dataEquals(s, false);
    }
    
    public boolean dataEquals(final Structure s, final boolean clear) {
        if (clear) {
            s.getPointer().clear(s.size());
            s.write();
            this.getPointer().clear(this.size());
            this.write();
        }
        final byte[] data = s.getPointer().getByteArray(0L, s.size());
        final byte[] ref = this.getPointer().getByteArray(0L, this.size());
        if (data.length == ref.length) {
            for (int i = 0; i < data.length; ++i) {
                if (data[i] != ref[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Structure && o.getClass() == this.getClass() && ((Structure)o).getPointer().equals(this.getPointer());
    }
    
    @Override
    public int hashCode() {
        final Pointer p = this.getPointer();
        if (p != null) {
            return this.getPointer().hashCode();
        }
        return this.getClass().hashCode();
    }
    
    protected void cacheTypeInfo(final Pointer p) {
        this.typeInfo = p.peer;
    }
    
    FFIType getFieldTypeInfo(final StructField f) {
        Class<?> type = f.type;
        Object value = this.getFieldValue(f.field);
        if (this.typeMapper != null) {
            final ToNativeConverter nc = this.typeMapper.getToNativeConverter(type);
            if (nc != null) {
                type = nc.nativeType();
                value = nc.toNative(value, new ToNativeContext());
            }
        }
        return get(value, type);
    }
    
    Pointer getTypeInfo() {
        final Pointer p = getTypeInfo(this).getPointer();
        this.cacheTypeInfo(p);
        return p;
    }
    
    public void setAutoSynch(final boolean auto) {
        this.setAutoRead(auto);
        this.setAutoWrite(auto);
    }
    
    public void setAutoRead(final boolean auto) {
        this.autoRead = auto;
    }
    
    public boolean getAutoRead() {
        return this.autoRead;
    }
    
    public void setAutoWrite(final boolean auto) {
        this.autoWrite = auto;
    }
    
    public boolean getAutoWrite() {
        return this.autoWrite;
    }
    
    static FFIType getTypeInfo(final Object obj) {
        return FFIType.get(obj);
    }
    
    private static <T extends Structure> T newInstance(final Class<T> type, final long init) {
        try {
            final T s = newInstance(type, (init == 0L) ? Structure.PLACEHOLDER_MEMORY : new Pointer(init));
            if (init != 0L) {
                s.conditionalAutoRead();
            }
            return s;
        }
        catch (Throwable e) {
            Structure.LOG.log(Level.WARNING, "JNA: Error creating structure", e);
            return null;
        }
    }
    
    public static <T extends Structure> T newInstance(final Class<T> type, final Pointer init) throws IllegalArgumentException {
        try {
            final Constructor<T> ctor = getPointerConstructor(type);
            if (ctor != null) {
                return ctor.newInstance(init);
            }
        }
        catch (SecurityException ex) {}
        catch (InstantiationException e) {
            final String msg = "Can't instantiate " + type;
            throw new IllegalArgumentException(msg, e);
        }
        catch (IllegalAccessException e2) {
            final String msg = "Instantiation of " + type + " (Pointer) not allowed, is it public?";
            throw new IllegalArgumentException(msg, e2);
        }
        catch (InvocationTargetException e3) {
            final String msg = "Exception thrown while instantiating an instance of " + type;
            throw new IllegalArgumentException(msg, e3);
        }
        final T s = newInstance(type);
        if (init != Structure.PLACEHOLDER_MEMORY) {
            s.useMemory(init);
        }
        return s;
    }
    
    public static <T extends Structure> T newInstance(final Class<T> type) throws IllegalArgumentException {
        final T s = Klass.newInstance(type);
        if (s instanceof ByValue) {
            s.allocateMemory();
        }
        return s;
    }
    
    private static <T> Constructor<T> getPointerConstructor(final Class<T> type) {
        for (final Constructor constructor : type.getConstructors()) {
            final Class[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == 1 && parameterTypes[0].equals(Pointer.class)) {
                return (Constructor<T>)constructor;
            }
        }
        return null;
    }
    
    private static void structureArrayCheck(final Structure[] ss) {
        if (ByReference[].class.isAssignableFrom(ss.getClass())) {
            return;
        }
        final Pointer base = ss[0].getPointer();
        final int size = ss[0].size();
        for (int si = 1; si < ss.length; ++si) {
            if (ss[si].getPointer().peer != base.peer + size * si) {
                final String msg = "Structure array elements must use contiguous memory (bad backing address at Structure array index " + si + ")";
                throw new IllegalArgumentException(msg);
            }
        }
    }
    
    public static void autoRead(final Structure[] ss) {
        structureArrayCheck(ss);
        if (ss[0].array == ss) {
            ss[0].autoRead();
        }
        else {
            for (int si = 0; si < ss.length; ++si) {
                if (ss[si] != null) {
                    ss[si].autoRead();
                }
            }
        }
    }
    
    public void autoRead() {
        if (this.getAutoRead()) {
            this.read();
            if (this.array != null) {
                for (int i = 1; i < this.array.length; ++i) {
                    this.array[i].autoRead();
                }
            }
        }
    }
    
    public static void autoWrite(final Structure[] ss) {
        structureArrayCheck(ss);
        if (ss[0].array == ss) {
            ss[0].autoWrite();
        }
        else {
            for (int si = 0; si < ss.length; ++si) {
                if (ss[si] != null) {
                    ss[si].autoWrite();
                }
            }
        }
    }
    
    public void autoWrite() {
        if (this.getAutoWrite()) {
            this.write();
            if (this.array != null) {
                for (int i = 1; i < this.array.length; ++i) {
                    this.array[i].autoWrite();
                }
            }
        }
    }
    
    protected int getNativeSize(final Class<?> nativeType) {
        return this.getNativeSize(nativeType, null);
    }
    
    protected int getNativeSize(final Class<?> nativeType, final Object value) {
        return Native.getNativeSize(nativeType, value);
    }
    
    static void validate(final Class<? extends Structure> cls) {
        try {
            cls.getConstructor((Class<?>[])new Class[0]);
            return;
        }
        catch (NoSuchMethodException ex) {}
        catch (SecurityException ex2) {}
        throw new IllegalArgumentException("No suitable constructor found for class: " + cls.getName());
    }
    
    static {
        LOG = Logger.getLogger(Structure.class.getName());
        layoutInfo = new WeakHashMap<Class<?>, LayoutInfo>();
        fieldOrder = new WeakHashMap<Class<?>, List<String>>();
        reads = new ThreadLocal<Map<Pointer, Structure>>() {
            @Override
            protected synchronized Map<Pointer, Structure> initialValue() {
                return new HashMap<Pointer, Structure>();
            }
        };
        busy = new ThreadLocal<Set<Structure>>() {
            @Override
            protected synchronized Set<Structure> initialValue() {
                return new StructureSet();
            }
        };
        PLACEHOLDER_MEMORY = new Pointer(0L) {
            @Override
            public Pointer share(final long offset, final long sz) {
                return this;
            }
        };
    }
    
    private static class NativeStringTracking
    {
        private final Object value;
        private NativeString peer;
        
        NativeStringTracking(final Object lastValue) {
            this.value = lastValue;
        }
    }
    
    static class StructureSet extends AbstractCollection<Structure> implements Set<Structure>
    {
        Structure[] elements;
        private int count;
        
        private void ensureCapacity(final int size) {
            if (this.elements == null) {
                this.elements = new Structure[size * 3 / 2];
            }
            else if (this.elements.length < size) {
                final Structure[] e = new Structure[size * 3 / 2];
                System.arraycopy(this.elements, 0, e, 0, this.elements.length);
                this.elements = e;
            }
        }
        
        public Structure[] getElements() {
            return this.elements;
        }
        
        @Override
        public int size() {
            return this.count;
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.indexOf((Structure)o) != -1;
        }
        
        @Override
        public boolean add(final Structure o) {
            if (!this.contains(o)) {
                this.ensureCapacity(this.count + 1);
                this.elements[this.count++] = o;
                return true;
            }
            return false;
        }
        
        private int indexOf(final Structure s1) {
            for (int i = 0; i < this.count; ++i) {
                final Structure s2 = this.elements[i];
                if (s1 == s2 || (s1.getClass() == s2.getClass() && s1.size() == s2.size() && s1.getPointer().equals(s2.getPointer()))) {
                    return i;
                }
            }
            return -1;
        }
        
        @Override
        public boolean remove(final Object o) {
            final int idx = this.indexOf((Structure)o);
            if (idx != -1) {
                if (--this.count >= 0) {
                    this.elements[idx] = this.elements[this.count];
                    this.elements[this.count] = null;
                }
                return true;
            }
            return false;
        }
        
        @Override
        public Iterator<Structure> iterator() {
            final Structure[] e = new Structure[this.count];
            if (this.count > 0) {
                System.arraycopy(this.elements, 0, e, 0, this.count);
            }
            return Arrays.asList(e).iterator();
        }
    }
    
    private static class LayoutInfo
    {
        private int size;
        private int alignment;
        private final Map<String, StructField> fields;
        private int alignType;
        private TypeMapper typeMapper;
        private boolean variable;
        
        private LayoutInfo() {
            this.size = -1;
            this.alignment = 1;
            this.fields = Collections.synchronizedMap(new LinkedHashMap<String, StructField>());
            this.alignType = 0;
        }
    }
    
    protected static class StructField
    {
        public String name;
        public Class<?> type;
        public Field field;
        public int size;
        public int offset;
        public boolean isVolatile;
        public boolean isReadOnly;
        public FromNativeConverter readConverter;
        public ToNativeConverter writeConverter;
        public FromNativeContext context;
        
        protected StructField() {
            this.size = -1;
            this.offset = -1;
        }
        
        @Override
        public String toString() {
            return this.name + "@" + this.offset + "[" + this.size + "] (" + this.type + ")";
        }
    }
    
    @FieldOrder({ "size", "alignment", "type", "elements" })
    static class FFIType extends Structure
    {
        private static final Map<Class, FFIType> typeInfoMap;
        private static final Map<Class, FFIType> unionHelper;
        private static final Map<Pointer, FFIType> ffiTypeInfo;
        private static final int FFI_TYPE_STRUCT = 13;
        public size_t size;
        public short alignment;
        public short type;
        public Pointer elements;
        
        private static boolean isIntegerType(final FFIType type) {
            final Pointer typePointer = type.getPointer();
            return typePointer.equals(FFITypes.ffi_type_uint8) || typePointer.equals(FFITypes.ffi_type_sint8) || typePointer.equals(FFITypes.ffi_type_uint16) || typePointer.equals(FFITypes.ffi_type_sint16) || typePointer.equals(FFITypes.ffi_type_uint32) || typePointer.equals(FFITypes.ffi_type_sint32) || typePointer.equals(FFITypes.ffi_type_uint64) || typePointer.equals(FFITypes.ffi_type_sint64) || typePointer.equals(FFITypes.ffi_type_pointer);
        }
        
        private static boolean isFloatType(final FFIType type) {
            final Pointer typePointer = type.getPointer();
            return typePointer.equals(FFITypes.ffi_type_float) || typePointer.equals(FFITypes.ffi_type_double);
        }
        
        public FFIType(final FFIType reference) {
            this.type = 13;
            this.size = reference.size;
            this.alignment = reference.alignment;
            this.type = reference.type;
            this.elements = reference.elements;
        }
        
        public FFIType() {
            this.type = 13;
        }
        
        public FFIType(final Structure ref) {
            this.type = 13;
            ref.ensureAllocated(true);
            Pointer[] els;
            if (ref instanceof Union) {
                FFIType unionType = null;
                int size = 0;
                boolean hasInteger = false;
                for (final StructField sf : ref.fields().values()) {
                    final FFIType type = ref.getFieldTypeInfo(sf);
                    if (isIntegerType(type)) {
                        hasInteger = true;
                    }
                    if (unionType == null || size < sf.size || (size == sf.size && Structure.class.isAssignableFrom(sf.type))) {
                        unionType = type;
                        size = sf.size;
                    }
                }
                if (((Platform.isIntel() && Platform.is64Bit() && !Platform.isWindows()) || Platform.isARM()) && hasInteger && isFloatType(unionType)) {
                    unionType = new FFIType(unionType);
                    if (unionType.size.intValue() == 4) {
                        unionType.type = FFIType.ffiTypeInfo.get(FFITypes.ffi_type_uint32).type;
                    }
                    else if (unionType.size.intValue() == 8) {
                        unionType.type = FFIType.ffiTypeInfo.get(FFITypes.ffi_type_uint64).type;
                    }
                    unionType.write();
                }
                els = new Pointer[] { unionType.getPointer(), null };
                FFIType.unionHelper.put(ref.getClass(), unionType);
            }
            else {
                els = new Pointer[ref.fields().size() + 1];
                int idx = 0;
                for (final StructField sf2 : ref.fields().values()) {
                    els[idx++] = ref.getFieldTypeInfo(sf2).getPointer();
                }
            }
            this.init(els);
            this.write();
        }
        
        public FFIType(final Object array, final Class<?> type) {
            this.type = 13;
            final int length = Array.getLength(array);
            final Pointer[] els = new Pointer[length + 1];
            final Pointer p = get(null, type.getComponentType()).getPointer();
            for (int i = 0; i < length; ++i) {
                els[i] = p;
            }
            this.init(els);
            this.write();
        }
        
        private void init(final Pointer[] els) {
            (this.elements = new Memory(Native.POINTER_SIZE * els.length)).write(0L, els, 0, els.length);
            this.write();
        }
        
        static FFIType get(final Object obj) {
            if (obj == null) {
                return FFIType.typeInfoMap.get(Pointer.class);
            }
            if (obj instanceof Class) {
                return get(null, (Class<?>)obj);
            }
            return get(obj, obj.getClass());
        }
        
        private static FFIType get(Object obj, Class<?> cls) {
            final TypeMapper mapper = Native.getTypeMapper(cls);
            if (mapper != null) {
                final ToNativeConverter nc = mapper.getToNativeConverter(cls);
                if (nc != null) {
                    cls = nc.nativeType();
                }
            }
            synchronized (FFIType.typeInfoMap) {
                final FFIType o = FFIType.typeInfoMap.get(cls);
                if (o != null) {
                    return o;
                }
                if ((Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(cls)) || Callback.class.isAssignableFrom(cls)) {
                    FFIType.typeInfoMap.put(cls, FFIType.typeInfoMap.get(Pointer.class));
                    return FFIType.typeInfoMap.get(Pointer.class);
                }
                if (Structure.class.isAssignableFrom(cls)) {
                    if (obj == null) {
                        obj = Structure.newInstance(cls, Structure.PLACEHOLDER_MEMORY);
                    }
                    if (ByReference.class.isAssignableFrom(cls)) {
                        FFIType.typeInfoMap.put(cls, FFIType.typeInfoMap.get(Pointer.class));
                        return FFIType.typeInfoMap.get(Pointer.class);
                    }
                    final FFIType type = new FFIType((Structure)obj);
                    FFIType.typeInfoMap.put(cls, type);
                    return type;
                }
                else {
                    if (NativeMapped.class.isAssignableFrom(cls)) {
                        final NativeMappedConverter c = NativeMappedConverter.getInstance(cls);
                        return get(c.toNative(obj, new ToNativeContext()), c.nativeType());
                    }
                    if (cls.isArray()) {
                        final FFIType type = new FFIType(obj, cls);
                        FFIType.typeInfoMap.put(cls, type);
                        return type;
                    }
                    throw new IllegalArgumentException("Unsupported type " + cls);
                }
            }
        }
        
        static {
            typeInfoMap = new WeakHashMap<Class, FFIType>();
            unionHelper = new WeakHashMap<Class, FFIType>();
            ffiTypeInfo = new HashMap<Pointer, FFIType>();
            if (Native.POINTER_SIZE == 0) {
                throw new Error("Native library not initialized");
            }
            if (FFITypes.ffi_type_void == null) {
                throw new Error("FFI types not initialized");
            }
            FFIType.ffiTypeInfo.put(FFITypes.ffi_type_void, Structure.newInstance(FFIType.class, FFITypes.ffi_type_void));
            FFIType.ffiTypeInfo.put(FFITypes.ffi_type_float, Structure.newInstance(FFIType.class, FFITypes.ffi_type_float));
            FFIType.ffiTypeInfo.put(FFITypes.ffi_type_double, Structure.newInstance(FFIType.class, FFITypes.ffi_type_double));
            FFIType.ffiTypeInfo.put(FFITypes.ffi_type_longdouble, Structure.newInstance(FFIType.class, FFITypes.ffi_type_longdouble));
            FFIType.ffiTypeInfo.put(FFITypes.ffi_type_uint8, Structure.newInstance(FFIType.class, FFITypes.ffi_type_uint8));
            FFIType.ffiTypeInfo.put(FFITypes.ffi_type_sint8, Structure.newInstance(FFIType.class, FFITypes.ffi_type_sint8));
            FFIType.ffiTypeInfo.put(FFITypes.ffi_type_uint16, Structure.newInstance(FFIType.class, FFITypes.ffi_type_uint16));
            FFIType.ffiTypeInfo.put(FFITypes.ffi_type_sint16, Structure.newInstance(FFIType.class, FFITypes.ffi_type_sint16));
            FFIType.ffiTypeInfo.put(FFITypes.ffi_type_uint32, Structure.newInstance(FFIType.class, FFITypes.ffi_type_uint32));
            FFIType.ffiTypeInfo.put(FFITypes.ffi_type_sint32, Structure.newInstance(FFIType.class, FFITypes.ffi_type_sint32));
            FFIType.ffiTypeInfo.put(FFITypes.ffi_type_uint64, Structure.newInstance(FFIType.class, FFITypes.ffi_type_uint64));
            FFIType.ffiTypeInfo.put(FFITypes.ffi_type_sint64, Structure.newInstance(FFIType.class, FFITypes.ffi_type_sint64));
            FFIType.ffiTypeInfo.put(FFITypes.ffi_type_pointer, Structure.newInstance(FFIType.class, FFITypes.ffi_type_pointer));
            for (final FFIType f : FFIType.ffiTypeInfo.values()) {
                f.read();
            }
            FFIType.typeInfoMap.put(Void.TYPE, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_void));
            FFIType.typeInfoMap.put(Void.class, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_void));
            FFIType.typeInfoMap.put(Float.TYPE, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_float));
            FFIType.typeInfoMap.put(Float.class, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_float));
            FFIType.typeInfoMap.put(Double.TYPE, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_double));
            FFIType.typeInfoMap.put(Double.class, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_double));
            FFIType.typeInfoMap.put(Long.TYPE, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_sint64));
            FFIType.typeInfoMap.put(Long.class, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_sint64));
            FFIType.typeInfoMap.put(Integer.TYPE, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_sint32));
            FFIType.typeInfoMap.put(Integer.class, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_sint32));
            FFIType.typeInfoMap.put(Short.TYPE, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_sint16));
            FFIType.typeInfoMap.put(Short.class, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_sint16));
            final FFIType ctype = (Native.WCHAR_SIZE == 2) ? FFIType.ffiTypeInfo.get(FFITypes.ffi_type_uint16) : FFIType.ffiTypeInfo.get(FFITypes.ffi_type_uint32);
            FFIType.typeInfoMap.put(Character.TYPE, ctype);
            FFIType.typeInfoMap.put(Character.class, ctype);
            FFIType.typeInfoMap.put(Byte.TYPE, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_sint8));
            FFIType.typeInfoMap.put(Byte.class, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_sint8));
            FFIType.typeInfoMap.put(Pointer.class, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_pointer));
            FFIType.typeInfoMap.put(String.class, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_pointer));
            FFIType.typeInfoMap.put(WString.class, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_pointer));
            FFIType.typeInfoMap.put(Boolean.TYPE, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_uint32));
            FFIType.typeInfoMap.put(Boolean.class, FFIType.ffiTypeInfo.get(FFITypes.ffi_type_uint32));
        }
        
        public static class size_t extends IntegerType
        {
            private static final long serialVersionUID = 1L;
            
            public size_t() {
                this(0L);
            }
            
            public size_t(final long value) {
                super(Native.SIZE_T_SIZE, value);
            }
        }
        
        private static class FFITypes
        {
            private static Pointer ffi_type_void;
            private static Pointer ffi_type_float;
            private static Pointer ffi_type_double;
            private static Pointer ffi_type_longdouble;
            private static Pointer ffi_type_uint8;
            private static Pointer ffi_type_sint8;
            private static Pointer ffi_type_uint16;
            private static Pointer ffi_type_sint16;
            private static Pointer ffi_type_uint32;
            private static Pointer ffi_type_sint32;
            private static Pointer ffi_type_uint64;
            private static Pointer ffi_type_sint64;
            private static Pointer ffi_type_pointer;
        }
    }
    
    private static class AutoAllocated extends Memory
    {
        public AutoAllocated(final int size) {
            super(size);
            super.clear();
        }
        
        @Override
        public String toString() {
            return "auto-" + super.toString();
        }
    }
    
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface FieldOrder {
        String[] value();
    }
    
    public interface ByReference
    {
    }
    
    public interface ByValue
    {
    }
}
