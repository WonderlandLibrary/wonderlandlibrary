/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Callback;
import com.sun.jna.FromNativeContext;
import com.sun.jna.FromNativeConverter;
import com.sun.jna.IntegerType;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.NativeString;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.StructureReadContext;
import com.sun.jna.StructureWriteContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.Union;
import com.sun.jna.WString;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.Buffer;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.zip.Adler32;

public abstract class Structure {
    private static final boolean REVERSE_FIELDS;
    private static final boolean REQUIRES_FIELD_ORDER;
    static final boolean isPPC;
    static final boolean isSPARC;
    static final boolean isARM;
    public static final int ALIGN_DEFAULT = 0;
    public static final int ALIGN_NONE = 1;
    public static final int ALIGN_GNUC = 2;
    public static final int ALIGN_MSVC = 3;
    static final int MAX_GNUC_ALIGNMENT;
    protected static final int CALCULATE_SIZE = -1;
    static final Map layoutInfo;
    private Pointer memory;
    private int size = -1;
    private int alignType;
    private int structAlignment;
    private Map structFields;
    private final Map nativeStrings = new HashMap();
    private TypeMapper typeMapper;
    private long typeInfo;
    private List fieldOrder;
    private boolean autoRead = true;
    private boolean autoWrite = true;
    private Structure[] array;
    private static final ThreadLocal reads;
    private static final ThreadLocal busy;
    static /* synthetic */ Class class$java$lang$Void;

    protected Structure() {
        this((Pointer)null);
    }

    protected Structure(TypeMapper mapper) {
        this(null, 0, mapper);
    }

    protected Structure(Pointer p2) {
        this(p2, 0);
    }

    protected Structure(Pointer p2, int alignType) {
        this(p2, alignType, null);
    }

    protected Structure(Pointer p2, int alignType, TypeMapper mapper) {
        this.setAlignType(alignType);
        this.setTypeMapper(mapper);
        if (p2 != null) {
            this.useMemory(p2);
        } else {
            this.allocateMemory(-1);
        }
    }

    Map fields() {
        return this.structFields;
    }

    TypeMapper getTypeMapper() {
        return this.typeMapper;
    }

    protected void setTypeMapper(TypeMapper mapper) {
        Class<?> declaring;
        if (mapper == null && (declaring = this.getClass().getDeclaringClass()) != null) {
            mapper = Native.getTypeMapper(declaring);
        }
        this.typeMapper = mapper;
        this.size = -1;
        if (this.memory instanceof AutoAllocated) {
            this.memory = null;
        }
    }

    protected void setAlignType(int alignType) {
        if (alignType == 0) {
            Class<?> declaring = this.getClass().getDeclaringClass();
            if (declaring != null) {
                alignType = Native.getStructureAlignment(declaring);
            }
            if (alignType == 0) {
                alignType = Platform.isWindows() ? 3 : 2;
            }
        }
        this.alignType = alignType;
        this.size = -1;
        if (this.memory instanceof AutoAllocated) {
            this.memory = null;
        }
    }

    protected Memory autoAllocate(int size) {
        return new AutoAllocated(size);
    }

    protected void useMemory(Pointer m2) {
        this.useMemory(m2, 0);
    }

    protected void useMemory(Pointer m2, int offset) {
        try {
            this.memory = m2.share(offset);
            if (this.size == -1) {
                this.size = this.calculateSize(false);
            }
            if (this.size != -1) {
                this.memory = m2.share(offset, this.size);
            }
            this.array = null;
        }
        catch (IndexOutOfBoundsException e2) {
            throw new IllegalArgumentException("Structure exceeds provided memory bounds");
        }
    }

    protected void ensureAllocated() {
        this.ensureAllocated(false);
    }

    private void ensureAllocated(boolean avoidFFIType) {
        if (this.memory == null) {
            this.allocateMemory(avoidFFIType);
        } else if (this.size == -1) {
            this.size = this.calculateSize(true, avoidFFIType);
        }
    }

    protected void allocateMemory() {
        this.allocateMemory(false);
    }

    private void allocateMemory(boolean avoidFFIType) {
        this.allocateMemory(this.calculateSize(true, avoidFFIType));
    }

    protected void allocateMemory(int size) {
        if (size == -1) {
            size = this.calculateSize(false);
        } else if (size <= 0) {
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
        if (this.size == -1) {
            this.size = this.calculateSize(true);
        }
        return this.size;
    }

    public void clear() {
        this.memory.clear(this.size());
    }

    public Pointer getPointer() {
        this.ensureAllocated();
        return this.memory;
    }

    static Set busy() {
        return (Set)busy.get();
    }

    static Map reading() {
        return (Map)reads.get();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void read() {
        this.ensureAllocated();
        if (Structure.busy().contains(this)) {
            return;
        }
        Structure.busy().add(this);
        if (this instanceof ByReference) {
            Structure.reading().put(this.getPointer(), this);
        }
        try {
            Iterator i2 = this.fields().values().iterator();
            while (i2.hasNext()) {
                this.readField((StructField)i2.next());
            }
        }
        finally {
            Structure.busy().remove(this);
            if (Structure.reading().get(this.getPointer()) == this) {
                Structure.reading().remove(this.getPointer());
            }
        }
    }

    protected int fieldOffset(String name) {
        this.ensureAllocated();
        StructField f2 = (StructField)this.fields().get(name);
        if (f2 == null) {
            throw new IllegalArgumentException("No such field: " + name);
        }
        return f2.offset;
    }

    public Object readField(String name) {
        this.ensureAllocated();
        StructField f2 = (StructField)this.fields().get(name);
        if (f2 == null) {
            throw new IllegalArgumentException("No such field: " + name);
        }
        return this.readField(f2);
    }

    Object getField(StructField structField) {
        try {
            return structField.field.get(this);
        }
        catch (Exception e2) {
            throw new Error("Exception reading field '" + structField.name + "' in " + this.getClass() + ": " + e2);
        }
    }

    void setField(StructField structField, Object value) {
        this.setField(structField, value, false);
    }

    void setField(StructField structField, Object value, boolean overrideFinal) {
        try {
            structField.field.set(this, value);
        }
        catch (IllegalAccessException e2) {
            int modifiers = structField.field.getModifiers();
            if (Modifier.isFinal(modifiers)) {
                if (overrideFinal) {
                    throw new UnsupportedOperationException("This VM does not support Structures with final fields (field '" + structField.name + "' within " + this.getClass() + ")");
                }
                throw new UnsupportedOperationException("Attempt to write to read-only field '" + structField.name + "' within " + this.getClass());
            }
            throw new Error("Unexpectedly unable to write to field '" + structField.name + "' within " + this.getClass() + ": " + e2);
        }
    }

    static Structure updateStructureByReference(Class type, Structure s2, Pointer address) {
        if (address == null) {
            s2 = null;
        } else {
            if (s2 == null || !address.equals(s2.getPointer())) {
                Structure s1 = (Structure)Structure.reading().get(address);
                if (s1 != null && type.equals(s1.getClass())) {
                    s2 = s1;
                } else {
                    s2 = Structure.newInstance(type);
                    s2.useMemory(address);
                }
            }
            s2.autoRead();
        }
        return s2;
    }

    Object readField(StructField structField) {
        int offset = structField.offset;
        Class fieldType = structField.type;
        FromNativeConverter readConverter = structField.readConverter;
        if (readConverter != null) {
            fieldType = readConverter.nativeType();
        }
        Object currentValue = Structure.class.isAssignableFrom(fieldType) || Callback.class.isAssignableFrom(fieldType) || Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(fieldType) || Pointer.class.isAssignableFrom(fieldType) || NativeMapped.class.isAssignableFrom(fieldType) || fieldType.isArray() ? this.getField(structField) : null;
        Object result = this.memory.getValue(offset, fieldType, currentValue);
        if (readConverter != null) {
            result = readConverter.fromNative(result, structField.context);
        }
        this.setField(structField, result, true);
        return result;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void write() {
        this.ensureAllocated();
        if (this instanceof ByValue) {
            this.getTypeInfo();
        }
        if (Structure.busy().contains(this)) {
            return;
        }
        Structure.busy().add(this);
        try {
            Iterator i2 = this.fields().values().iterator();
            while (i2.hasNext()) {
                StructField sf = (StructField)i2.next();
                if (sf.isVolatile) continue;
                this.writeField(sf);
            }
        }
        finally {
            Structure.busy().remove(this);
        }
    }

    public void writeField(String name) {
        this.ensureAllocated();
        StructField f2 = (StructField)this.fields().get(name);
        if (f2 == null) {
            throw new IllegalArgumentException("No such field: " + name);
        }
        this.writeField(f2);
    }

    public void writeField(String name, Object value) {
        this.ensureAllocated();
        StructField f2 = (StructField)this.fields().get(name);
        if (f2 == null) {
            throw new IllegalArgumentException("No such field: " + name);
        }
        this.setField(f2, value);
        this.writeField(f2);
    }

    void writeField(StructField structField) {
        if (structField.isReadOnly) {
            return;
        }
        int offset = structField.offset;
        Object value = this.getField(structField);
        Class fieldType = structField.type;
        ToNativeConverter converter = structField.writeConverter;
        if (converter != null) {
            value = converter.toNative(value, new StructureWriteContext(this, structField.field));
            fieldType = converter.nativeType();
        }
        if (String.class == fieldType || WString.class == fieldType) {
            boolean wide;
            boolean bl = wide = fieldType == WString.class;
            if (value != null) {
                NativeString nativeString = new NativeString(value.toString(), wide);
                this.nativeStrings.put(structField.name, nativeString);
                value = nativeString.getPointer();
            } else {
                value = null;
                this.nativeStrings.remove(structField.name);
            }
        }
        try {
            this.memory.setValue(offset, value, fieldType);
        }
        catch (IllegalArgumentException e2) {
            String msg = "Structure field \"" + structField.name + "\" was declared as " + structField.type + (structField.type == fieldType ? "" : " (native type " + fieldType + ")") + ", which is not supported within a Structure";
            throw new IllegalArgumentException(msg);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean hasFieldOrder() {
        Structure structure = this;
        synchronized (structure) {
            return this.fieldOrder != null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected List getFieldOrder() {
        Structure structure = this;
        synchronized (structure) {
            if (this.fieldOrder == null) {
                this.fieldOrder = new ArrayList();
            }
            return this.fieldOrder;
        }
    }

    protected void setFieldOrder(String[] fields) {
        this.getFieldOrder().addAll(Arrays.asList(fields));
        this.size = -1;
        if (this.memory instanceof AutoAllocated) {
            this.memory = null;
        }
    }

    protected void sortFields(List fields, List names) {
        block0: for (int i2 = 0; i2 < names.size(); ++i2) {
            String name = (String)names.get(i2);
            for (int f2 = 0; f2 < fields.size(); ++f2) {
                Field field = (Field)fields.get(f2);
                if (!name.equals(field.getName())) continue;
                Collections.swap(fields, i2, f2);
                continue block0;
            }
        }
    }

    protected List getFields(boolean force) {
        ArrayList flist = new ArrayList();
        Class<?> cls = this.getClass();
        while (!cls.equals(class$com$sun$jna$Structure == null ? Structure.class$("com.sun.jna.Structure") : class$com$sun$jna$Structure)) {
            ArrayList<Field> classFields = new ArrayList<Field>();
            Field[] fields = cls.getDeclaredFields();
            for (int i2 = 0; i2 < fields.length; ++i2) {
                int modifiers = fields[i2].getModifiers();
                if (Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers)) continue;
                classFields.add(fields[i2]);
            }
            if (REVERSE_FIELDS) {
                Collections.reverse(classFields);
            }
            flist.addAll(0, classFields);
            cls = cls.getSuperclass();
        }
        if (REQUIRES_FIELD_ORDER || this.hasFieldOrder()) {
            List fieldOrder = this.getFieldOrder();
            if (fieldOrder.size() < flist.size()) {
                if (force) {
                    throw new Error("This VM does not store fields in a predictable order; you must use Structure.setFieldOrder to explicitly indicate the field order: " + System.getProperty("java.vendor") + ", " + System.getProperty("java.version"));
                }
                return null;
            }
            this.sortFields(flist, fieldOrder);
        }
        return flist;
    }

    private synchronized boolean fieldOrderMatch(List fieldOrder) {
        return this.fieldOrder == fieldOrder || this.fieldOrder != null && ((Object)this.fieldOrder).equals(fieldOrder);
    }

    private int calculateSize(boolean force) {
        return this.calculateSize(force, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    int calculateSize(boolean force, boolean avoidFFIType) {
        LayoutInfo info;
        boolean needsInit = true;
        Map map = layoutInfo;
        synchronized (map) {
            info = (LayoutInfo)layoutInfo.get(this.getClass());
        }
        if (info == null || this.alignType != info.alignType || this.typeMapper != info.typeMapper || !this.fieldOrderMatch(info.fieldOrder)) {
            info = this.deriveLayout(force, avoidFFIType);
            needsInit = false;
        }
        if (info != null) {
            this.structAlignment = info.alignment;
            this.structFields = info.fields;
            info.alignType = this.alignType;
            info.typeMapper = this.typeMapper;
            info.fieldOrder = this.fieldOrder;
            if (!info.variable) {
                map = layoutInfo;
                synchronized (map) {
                    layoutInfo.put(this.getClass(), info);
                }
            }
            if (needsInit) {
                this.initializeFields();
            }
            return info.size;
        }
        return -1;
    }

    private LayoutInfo deriveLayout(boolean force, boolean avoidFFIType) {
        LayoutInfo info = new LayoutInfo();
        int calculatedSize = 0;
        List fields = this.getFields(force);
        if (fields == null) {
            return null;
        }
        boolean firstField = true;
        Iterator i2 = fields.iterator();
        while (i2.hasNext()) {
            Field field = (Field)i2.next();
            int modifiers = field.getModifiers();
            Class type = field.getType();
            if (type.isArray()) {
                info.variable = true;
            }
            StructField structField = new StructField();
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
            if ((class$com$sun$jna$Callback == null ? Structure.class$("com.sun.jna.Callback") : class$com$sun$jna$Callback).isAssignableFrom(type) && !type.isInterface()) {
                throw new IllegalArgumentException("Structure Callback field '" + field.getName() + "' must be an interface");
            }
            if (type.isArray() && (class$com$sun$jna$Structure == null ? Structure.class$("com.sun.jna.Structure") : class$com$sun$jna$Structure).equals(type.getComponentType())) {
                String msg = "Nested Structure arrays must use a derived Structure type so that the size of the elements can be determined";
                throw new IllegalArgumentException(msg);
            }
            int fieldAlignment = 1;
            if (Modifier.isPublic(field.getModifiers())) {
                Object value = this.getField(structField);
                if (value == null && type.isArray()) {
                    if (force) {
                        throw new IllegalStateException("Array fields must be initialized");
                    }
                    return null;
                }
                Class nativeType = type;
                if ((class$com$sun$jna$NativeMapped == null ? Structure.class$("com.sun.jna.NativeMapped") : class$com$sun$jna$NativeMapped).isAssignableFrom(type)) {
                    NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
                    nativeType = tc.nativeType();
                    structField.writeConverter = tc;
                    structField.readConverter = tc;
                    structField.context = new StructureReadContext(this, field);
                } else if (this.typeMapper != null) {
                    ToNativeConverter writeConverter = this.typeMapper.getToNativeConverter(type);
                    FromNativeConverter readConverter = this.typeMapper.getFromNativeConverter(type);
                    if (writeConverter != null && readConverter != null) {
                        nativeType = (value = writeConverter.toNative(value, new StructureWriteContext(this, structField.field))) != null ? value.getClass() : (class$com$sun$jna$Pointer == null ? Structure.class$("com.sun.jna.Pointer") : class$com$sun$jna$Pointer);
                        structField.writeConverter = writeConverter;
                        structField.readConverter = readConverter;
                        structField.context = new StructureReadContext(this, field);
                    } else if (writeConverter != null || readConverter != null) {
                        String msg = "Structures require bidirectional type conversion for " + type;
                        throw new IllegalArgumentException(msg);
                    }
                }
                if (value == null) {
                    value = this.initializeField(structField, type);
                }
                try {
                    structField.size = this.getNativeSize(nativeType, value);
                    fieldAlignment = this.getNativeAlignment(nativeType, value, firstField);
                }
                catch (IllegalArgumentException e2) {
                    if (!force && this.typeMapper == null) {
                        return null;
                    }
                    String msg = "Invalid Structure field in " + this.getClass() + ", field name '" + structField.name + "', " + structField.type + ": " + e2.getMessage();
                    throw new IllegalArgumentException(msg);
                }
                info.alignment = Math.max(info.alignment, fieldAlignment);
                if (calculatedSize % fieldAlignment != 0) {
                    calculatedSize += fieldAlignment - calculatedSize % fieldAlignment;
                }
                structField.offset = calculatedSize;
                calculatedSize += structField.size;
                info.fields.put(structField.name, structField);
            }
            firstField = false;
        }
        if (calculatedSize > 0) {
            int size = this.calculateAlignedSize(calculatedSize, info.alignment);
            if (this instanceof ByValue && !avoidFFIType) {
                this.getTypeInfo();
            }
            if (this.memory != null && !(this.memory instanceof AutoAllocated)) {
                this.memory = this.memory.share(0L, size);
            }
            info.size = size;
            return info;
        }
        throw new IllegalArgumentException("Structure " + this.getClass() + " has unknown size (ensure " + "all fields are public)");
    }

    private void initializeFields() {
        Iterator i2 = this.fields().values().iterator();
        while (i2.hasNext()) {
            StructField f2 = (StructField)i2.next();
            this.initializeField(f2, f2.type);
        }
    }

    private Object initializeField(StructField structField, Class type) {
        Object value = null;
        if (Structure.class.isAssignableFrom(type) && !ByReference.class.isAssignableFrom(type)) {
            try {
                value = Structure.newInstance(type);
                this.setField(structField, value);
            }
            catch (IllegalArgumentException e2) {
                String msg = "Can't determine size of nested structure: " + e2.getMessage();
                throw new IllegalArgumentException(msg);
            }
        } else if (NativeMapped.class.isAssignableFrom(type)) {
            NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
            value = tc.defaultValue();
            this.setField(structField, value);
        }
        return value;
    }

    int calculateAlignedSize(int calculatedSize) {
        return this.calculateAlignedSize(calculatedSize, this.structAlignment);
    }

    private int calculateAlignedSize(int calculatedSize, int alignment) {
        if (this.alignType != 1 && calculatedSize % alignment != 0) {
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

    protected int getNativeAlignment(Class type, Object value, boolean isFirstElement) {
        int alignment = 1;
        if (NativeMapped.class.isAssignableFrom(type)) {
            NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
            type = tc.nativeType();
            value = tc.toNative(value, new ToNativeContext());
        }
        int size = Native.getNativeSize(type, value);
        if (type.isPrimitive() || Long.class == type || Integer.class == type || Short.class == type || Character.class == type || Byte.class == type || Boolean.class == type || Float.class == type || Double.class == type) {
            alignment = size;
        } else if (Pointer.class == type || Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(type) || Callback.class.isAssignableFrom(type) || WString.class == type || String.class == type) {
            alignment = Pointer.SIZE;
        } else if (Structure.class.isAssignableFrom(type)) {
            if (ByReference.class.isAssignableFrom(type)) {
                alignment = Pointer.SIZE;
            } else {
                if (value == null) {
                    value = Structure.newInstance(type);
                }
                alignment = ((Structure)value).getStructAlignment();
            }
        } else if (type.isArray()) {
            alignment = this.getNativeAlignment(type.getComponentType(), null, isFirstElement);
        } else {
            throw new IllegalArgumentException("Type " + type + " has unknown " + "native alignment");
        }
        if (this.alignType == 1) {
            alignment = 1;
        } else if (this.alignType == 3) {
            alignment = Math.min(8, alignment);
        } else if (!(this.alignType != 2 || isFirstElement && Platform.isMac() && isPPC)) {
            alignment = Math.min(MAX_GNUC_ALIGNMENT, alignment);
        }
        return alignment;
    }

    public String toString() {
        return this.toString(Boolean.getBoolean("jna.dump_memory"));
    }

    public String toString(boolean debug) {
        return this.toString(0, true, true);
    }

    private String format(Class type) {
        String s2 = type.getName();
        int dot = s2.lastIndexOf(".");
        return s2.substring(dot + 1);
    }

    private String toString(int indent, boolean showContents, boolean dumpMemory) {
        this.ensureAllocated();
        String LS = System.getProperty("line.separator");
        String name = this.format(this.getClass()) + "(" + this.getPointer() + ")";
        if (!(this.getPointer() instanceof Memory)) {
            name = name + " (" + this.size() + " bytes)";
        }
        String prefix = "";
        for (int idx = 0; idx < indent; ++idx) {
            prefix = prefix + "  ";
        }
        String contents = LS;
        if (!showContents) {
            contents = "...}";
        } else {
            Iterator i2 = this.fields().values().iterator();
            while (i2.hasNext()) {
                StructField sf = (StructField)i2.next();
                Object value = this.getField(sf);
                String type = this.format(sf.type);
                String index = "";
                contents = contents + prefix;
                if (sf.type.isArray() && value != null) {
                    type = this.format(sf.type.getComponentType());
                    index = "[" + Array.getLength(value) + "]";
                }
                contents = contents + "  " + type + " " + sf.name + index + "@" + Integer.toHexString(sf.offset);
                if (value instanceof Structure) {
                    value = ((Structure)value).toString(indent + 1, !(value instanceof ByReference), dumpMemory);
                }
                contents = contents + "=";
                contents = value instanceof Long ? contents + Long.toHexString((Long)value) : (value instanceof Integer ? contents + Integer.toHexString((Integer)value) : (value instanceof Short ? contents + Integer.toHexString(((Short)value).shortValue()) : (value instanceof Byte ? contents + Integer.toHexString(((Byte)value).byteValue()) : contents + String.valueOf(value).trim())));
                contents = contents + LS;
                if (i2.hasNext()) continue;
                contents = contents + prefix + "}";
            }
        }
        if (indent == 0 && dumpMemory) {
            int BYTES_PER_ROW = 4;
            contents = contents + LS + "memory dump" + LS;
            byte[] buf = this.getPointer().getByteArray(0L, this.size());
            for (int i3 = 0; i3 < buf.length; ++i3) {
                if (i3 % 4 == 0) {
                    contents = contents + "[";
                }
                if (buf[i3] >= 0 && buf[i3] < 16) {
                    contents = contents + "0";
                }
                contents = contents + Integer.toHexString(buf[i3] & 0xFF);
                if (i3 % 4 != 3 || i3 >= buf.length - 1) continue;
                contents = contents + "]" + LS;
            }
            contents = contents + "]";
        }
        return name + " {" + contents;
    }

    public Structure[] toArray(Structure[] array) {
        this.ensureAllocated();
        if (this.memory instanceof AutoAllocated) {
            Memory m2 = (Memory)this.memory;
            int requiredSize = array.length * this.size();
            if (m2.size() < (long)requiredSize) {
                this.useMemory(this.autoAllocate(requiredSize));
            }
        }
        array[0] = this;
        int size = this.size();
        for (int i2 = 1; i2 < array.length; ++i2) {
            array[i2] = Structure.newInstance(this.getClass());
            array[i2].useMemory(this.memory.share(i2 * size, size));
            array[i2].read();
        }
        if (!(this instanceof ByValue)) {
            this.array = array;
        }
        return array;
    }

    public Structure[] toArray(int size) {
        return this.toArray((Structure[])Array.newInstance(this.getClass(), size));
    }

    private Class baseClass() {
        if ((this instanceof ByReference || this instanceof ByValue) && Structure.class.isAssignableFrom(this.getClass().getSuperclass())) {
            return this.getClass().getSuperclass();
        }
        return this.getClass();
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof Structure)) {
            return false;
        }
        if (o2.getClass() != this.getClass() && ((Structure)o2).baseClass() != this.baseClass()) {
            return false;
        }
        Structure s2 = (Structure)o2;
        if (s2.getPointer().equals(this.getPointer())) {
            return true;
        }
        if (s2.size() == this.size()) {
            this.clear();
            this.write();
            byte[] buf = this.getPointer().getByteArray(0L, this.size());
            s2.clear();
            s2.write();
            byte[] sbuf = s2.getPointer().getByteArray(0L, s2.size());
            return Arrays.equals(buf, sbuf);
        }
        return false;
    }

    public int hashCode() {
        this.clear();
        this.write();
        Adler32 code = new Adler32();
        code.update(this.getPointer().getByteArray(0L, this.size()));
        return (int)code.getValue();
    }

    protected void cacheTypeInfo(Pointer p2) {
        this.typeInfo = p2.peer;
    }

    protected Pointer getFieldTypeInfo(StructField f2) {
        ToNativeConverter nc;
        Class type = f2.type;
        Object value = this.getField(f2);
        if (this.typeMapper != null && (nc = this.typeMapper.getToNativeConverter(type)) != null) {
            type = nc.nativeType();
            value = nc.toNative(value, new ToNativeContext());
        }
        return FFIType.get(value, type);
    }

    Pointer getTypeInfo() {
        Pointer p2 = Structure.getTypeInfo(this);
        this.cacheTypeInfo(p2);
        return p2;
    }

    public void setAutoSynch(boolean auto) {
        this.setAutoRead(auto);
        this.setAutoWrite(auto);
    }

    public void setAutoRead(boolean auto) {
        this.autoRead = auto;
    }

    public boolean getAutoRead() {
        return this.autoRead;
    }

    public void setAutoWrite(boolean auto) {
        this.autoWrite = auto;
    }

    public boolean getAutoWrite() {
        return this.autoWrite;
    }

    static Pointer getTypeInfo(Object obj) {
        return FFIType.get(obj);
    }

    public static Structure newInstance(Class type) throws IllegalArgumentException {
        try {
            Structure s2 = (Structure)type.newInstance();
            if (s2 instanceof ByValue) {
                s2.allocateMemory();
            }
            return s2;
        }
        catch (InstantiationException e2) {
            String msg = "Can't instantiate " + type + " (" + e2 + ")";
            throw new IllegalArgumentException(msg);
        }
        catch (IllegalAccessException e3) {
            String msg = "Instantiation of " + type + " not allowed, is it public? (" + e3 + ")";
            throw new IllegalArgumentException(msg);
        }
    }

    private static void structureArrayCheck(Structure[] ss) {
        Pointer base = ss[0].getPointer();
        int size = ss[0].size();
        for (int si = 1; si < ss.length; ++si) {
            if (ss[si].getPointer().peer == base.peer + (long)(size * si)) continue;
            String msg = "Structure array elements must use contiguous memory (bad backing address at Structure array index " + si + ")";
            throw new IllegalArgumentException(msg);
        }
    }

    public static void autoRead(Structure[] ss) {
        Structure.structureArrayCheck(ss);
        if (ss[0].array == ss) {
            ss[0].autoRead();
        } else {
            for (int si = 0; si < ss.length; ++si) {
                ss[si].autoRead();
            }
        }
    }

    public void autoRead() {
        if (this.getAutoRead()) {
            this.read();
            if (this.array != null) {
                for (int i2 = 1; i2 < this.array.length; ++i2) {
                    this.array[i2].autoRead();
                }
            }
        }
    }

    public static void autoWrite(Structure[] ss) {
        Structure.structureArrayCheck(ss);
        if (ss[0].array == ss) {
            ss[0].autoWrite();
        } else {
            for (int si = 0; si < ss.length; ++si) {
                ss[si].autoWrite();
            }
        }
    }

    public void autoWrite() {
        if (this.getAutoWrite()) {
            this.write();
            if (this.array != null) {
                for (int i2 = 1; i2 < this.array.length; ++i2) {
                    this.array[i2].autoWrite();
                }
            }
        }
    }

    protected int getNativeSize(Class nativeType, Object value) {
        return Native.getNativeSize(nativeType, value);
    }

    static {
        Field[] fields = MemberOrder.class.getFields();
        ArrayList<String> names = new ArrayList<String>();
        for (int i2 = 0; i2 < fields.length; ++i2) {
            names.add(fields[i2].getName());
        }
        List<String> expected = Arrays.asList(MemberOrder.FIELDS);
        ArrayList<String> reversed = new ArrayList<String>(expected);
        Collections.reverse(reversed);
        REVERSE_FIELDS = ((Object)names).equals(reversed);
        REQUIRES_FIELD_ORDER = !((Object)names).equals(expected) && !REVERSE_FIELDS;
        String arch = System.getProperty("os.arch").toLowerCase();
        isPPC = "ppc".equals(arch) || "powerpc".equals(arch);
        isSPARC = "sparc".equals(arch);
        isARM = "arm".equals(arch);
        MAX_GNUC_ALIGNMENT = isSPARC || (isPPC || isARM) && Platform.isLinux() ? 8 : Native.LONG_SIZE;
        layoutInfo = new WeakHashMap();
        reads = new ThreadLocal(){

            protected synchronized Object initialValue() {
                return new HashMap();
            }
        };
        busy = new ThreadLocal(){

            protected synchronized Object initialValue() {
                return new StructureSet();
            }

            class StructureSet
            extends AbstractCollection
            implements Set {
                private Structure[] elements;
                private int count;

                StructureSet() {
                }

                private void ensureCapacity(int size) {
                    if (this.elements == null) {
                        this.elements = new Structure[size * 3 / 2];
                    } else if (this.elements.length < size) {
                        Structure[] e2 = new Structure[size * 3 / 2];
                        System.arraycopy(this.elements, 0, e2, 0, this.elements.length);
                        this.elements = e2;
                    }
                }

                public int size() {
                    return this.count;
                }

                public boolean contains(Object o2) {
                    return this.indexOf(o2) != -1;
                }

                public boolean add(Object o2) {
                    if (!this.contains(o2)) {
                        this.ensureCapacity(this.count + 1);
                        this.elements[this.count++] = (Structure)o2;
                    }
                    return true;
                }

                private int indexOf(Object o2) {
                    Structure s1 = (Structure)o2;
                    for (int i2 = 0; i2 < this.count; ++i2) {
                        Structure s2 = this.elements[i2];
                        if (s1 != s2 && (s1.getClass() != s2.getClass() || s1.size() != s2.size() || !s1.getPointer().equals(s2.getPointer()))) continue;
                        return i2;
                    }
                    return -1;
                }

                public boolean remove(Object o2) {
                    int idx = this.indexOf(o2);
                    if (idx != -1) {
                        if (--this.count > 0) {
                            this.elements[idx] = this.elements[this.count];
                            this.elements[this.count] = null;
                        }
                        return true;
                    }
                    return false;
                }

                public Iterator iterator() {
                    Structure[] e2 = new Structure[this.count];
                    if (this.count > 0) {
                        System.arraycopy(this.elements, 0, e2, 0, this.count);
                    }
                    return Arrays.asList(e2).iterator();
                }
            }
        };
    }

    private class AutoAllocated
    extends Memory {
        public AutoAllocated(int size) {
            super(size);
            super.clear();
        }
    }

    static class FFIType
    extends Structure {
        private static Map typeInfoMap = new WeakHashMap();
        private static final int FFI_TYPE_STRUCT = 13;
        public size_t size;
        public short alignment;
        public short type = (short)13;
        public Pointer elements;

        private FFIType(Structure ref) {
            Pointer[] els;
            ref.ensureAllocated(true);
            if (ref instanceof Union) {
                StructField sf = ((Union)ref).biggestField;
                els = new Pointer[]{FFIType.get(ref.getField(sf), sf.type), null};
            } else {
                els = new Pointer[ref.fields().size() + 1];
                int idx = 0;
                Iterator i2 = ref.fields().values().iterator();
                while (i2.hasNext()) {
                    StructField sf = (StructField)i2.next();
                    els[idx++] = ref.getFieldTypeInfo(sf);
                }
            }
            this.init(els);
        }

        private FFIType(Object array, Class type) {
            int length = Array.getLength(array);
            Pointer[] els = new Pointer[length + 1];
            Pointer p2 = FFIType.get(null, type.getComponentType());
            for (int i2 = 0; i2 < length; ++i2) {
                els[i2] = p2;
            }
            this.init(els);
        }

        private void init(Pointer[] els) {
            this.elements = new Memory(Pointer.SIZE * els.length);
            this.elements.write(0L, els, 0, els.length);
            this.write();
        }

        static Pointer get(Object obj) {
            if (obj == null) {
                return FFITypes.ffi_type_pointer;
            }
            if (obj instanceof Class) {
                return FFIType.get(null, (Class)obj);
            }
            return FFIType.get(obj, obj.getClass());
        }

        private static Pointer get(Object obj, Class cls) {
            ToNativeConverter nc;
            TypeMapper mapper = Native.getTypeMapper(cls);
            if (mapper != null && (nc = mapper.getToNativeConverter(cls)) != null) {
                cls = nc.nativeType();
            }
            Map map = typeInfoMap;
            synchronized (map) {
                Object o2 = typeInfoMap.get(cls);
                if (o2 instanceof Pointer) {
                    return (Pointer)o2;
                }
                if (o2 instanceof FFIType) {
                    return ((FFIType)o2).getPointer();
                }
                if (Platform.HAS_BUFFERS && (class$java$nio$Buffer == null ? (class$java$nio$Buffer = Structure.class$("java.nio.Buffer")) : class$java$nio$Buffer).isAssignableFrom(cls) || (class$com$sun$jna$Callback == null ? (class$com$sun$jna$Callback = Structure.class$("com.sun.jna.Callback")) : class$com$sun$jna$Callback).isAssignableFrom(cls)) {
                    typeInfoMap.put(cls, FFITypes.ffi_type_pointer);
                    return FFITypes.ffi_type_pointer;
                }
                if ((class$com$sun$jna$Structure == null ? (class$com$sun$jna$Structure = Structure.class$("com.sun.jna.Structure")) : class$com$sun$jna$Structure).isAssignableFrom(cls)) {
                    if (obj == null) {
                        obj = FFIType.newInstance(cls);
                    }
                    if ((class$com$sun$jna$Structure$ByReference == null ? (class$com$sun$jna$Structure$ByReference = Structure.class$("com.sun.jna.Structure$ByReference")) : class$com$sun$jna$Structure$ByReference).isAssignableFrom(cls)) {
                        typeInfoMap.put(cls, FFITypes.ffi_type_pointer);
                        return FFITypes.ffi_type_pointer;
                    }
                    FFIType type = new FFIType((Structure)obj);
                    typeInfoMap.put(cls, type);
                    return type.getPointer();
                }
                if ((class$com$sun$jna$NativeMapped == null ? (class$com$sun$jna$NativeMapped = Structure.class$("com.sun.jna.NativeMapped")) : class$com$sun$jna$NativeMapped).isAssignableFrom(cls)) {
                    NativeMappedConverter c2 = NativeMappedConverter.getInstance(cls);
                    return FFIType.get(c2.toNative(obj, new ToNativeContext()), c2.nativeType());
                }
                if (cls.isArray()) {
                    FFIType type = new FFIType(obj, cls);
                    typeInfoMap.put(obj, type);
                    return type.getPointer();
                }
                throw new IllegalArgumentException("Unsupported Structure field type " + cls);
            }
        }

        static {
            if (Native.POINTER_SIZE == 0) {
                throw new Error("Native library not initialized");
            }
            if (FFITypes.ffi_type_void == null) {
                throw new Error("FFI types not initialized");
            }
            typeInfoMap.put(Void.TYPE, FFITypes.ffi_type_void);
            typeInfoMap.put(class$java$lang$Void == null ? (class$java$lang$Void = Structure.class$("java.lang.Void")) : class$java$lang$Void, FFITypes.ffi_type_void);
            typeInfoMap.put(Float.TYPE, FFITypes.ffi_type_float);
            typeInfoMap.put(class$java$lang$Float == null ? (class$java$lang$Float = Structure.class$("java.lang.Float")) : class$java$lang$Float, FFITypes.ffi_type_float);
            typeInfoMap.put(Double.TYPE, FFITypes.ffi_type_double);
            typeInfoMap.put(class$java$lang$Double == null ? (class$java$lang$Double = Structure.class$("java.lang.Double")) : class$java$lang$Double, FFITypes.ffi_type_double);
            typeInfoMap.put(Long.TYPE, FFITypes.ffi_type_sint64);
            typeInfoMap.put(class$java$lang$Long == null ? (class$java$lang$Long = Structure.class$("java.lang.Long")) : class$java$lang$Long, FFITypes.ffi_type_sint64);
            typeInfoMap.put(Integer.TYPE, FFITypes.ffi_type_sint32);
            typeInfoMap.put(class$java$lang$Integer == null ? (class$java$lang$Integer = Structure.class$("java.lang.Integer")) : class$java$lang$Integer, FFITypes.ffi_type_sint32);
            typeInfoMap.put(Short.TYPE, FFITypes.ffi_type_sint16);
            typeInfoMap.put(class$java$lang$Short == null ? (class$java$lang$Short = Structure.class$("java.lang.Short")) : class$java$lang$Short, FFITypes.ffi_type_sint16);
            Pointer ctype = Native.WCHAR_SIZE == 2 ? FFITypes.ffi_type_uint16 : FFITypes.ffi_type_uint32;
            typeInfoMap.put(Character.TYPE, ctype);
            typeInfoMap.put(class$java$lang$Character == null ? (class$java$lang$Character = Structure.class$("java.lang.Character")) : class$java$lang$Character, ctype);
            typeInfoMap.put(Byte.TYPE, FFITypes.ffi_type_sint8);
            typeInfoMap.put(class$java$lang$Byte == null ? (class$java$lang$Byte = Structure.class$("java.lang.Byte")) : class$java$lang$Byte, FFITypes.ffi_type_sint8);
            typeInfoMap.put(class$com$sun$jna$Pointer == null ? (class$com$sun$jna$Pointer = Structure.class$("com.sun.jna.Pointer")) : class$com$sun$jna$Pointer, FFITypes.ffi_type_pointer);
            typeInfoMap.put(class$java$lang$String == null ? (class$java$lang$String = Structure.class$("java.lang.String")) : class$java$lang$String, FFITypes.ffi_type_pointer);
            typeInfoMap.put(class$com$sun$jna$WString == null ? (class$com$sun$jna$WString = Structure.class$("com.sun.jna.WString")) : class$com$sun$jna$WString, FFITypes.ffi_type_pointer);
            typeInfoMap.put(Boolean.TYPE, FFITypes.ffi_type_uint32);
            typeInfoMap.put(class$java$lang$Boolean == null ? (class$java$lang$Boolean = Structure.class$("java.lang.Boolean")) : class$java$lang$Boolean, FFITypes.ffi_type_uint32);
        }

        private static class FFITypes {
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

            private FFITypes() {
            }
        }

        public static class size_t
        extends IntegerType {
            public size_t() {
                this(0L);
            }

            public size_t(long value) {
                super(Native.POINTER_SIZE, value);
            }
        }
    }

    class StructField {
        public String name;
        public Class type;
        public Field field;
        public int size = -1;
        public int offset = -1;
        public boolean isVolatile;
        public boolean isReadOnly;
        public FromNativeConverter readConverter;
        public ToNativeConverter writeConverter;
        public FromNativeContext context;

        StructField() {
        }

        public String toString() {
            return this.name + "@" + this.offset + "[" + this.size + "] (" + this.type + ")";
        }
    }

    private class LayoutInfo {
        int size = -1;
        int alignment = 1;
        Map fields = Collections.synchronizedMap(new LinkedHashMap());
        int alignType = 0;
        TypeMapper typeMapper;
        List fieldOrder;
        boolean variable;

        private LayoutInfo() {
        }
    }

    private static class MemberOrder {
        private static final String[] FIELDS = new String[]{"first", "second", "middle", "penultimate", "last"};
        public int first;
        public int second;
        public int middle;
        public int penultimate;
        public int last;

        private MemberOrder() {
        }
    }

    public static interface ByReference {
    }

    public static interface ByValue {
    }
}

