package com.sun.jna;

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
  
  private static class MemberOrder {
    private static final String[] FIELDS = new String[] { "first", "second", "middle", "penultimate", "last" };
    
    public int first;
    
    public int second;
    
    public int middle;
    
    public int penultimate;
    
    public int last;
  }
  
  static {
    Field[] fields = MemberOrder.class.getFields();
    List names = new ArrayList();
    for (int i = 0; i < fields.length; i++)
      names.add(fields[i].getName()); 
    List expected = Arrays.asList(MemberOrder.FIELDS);
    List reversed = new ArrayList(expected);
    Collections.reverse(reversed);
    REVERSE_FIELDS = names.equals(reversed);
    REQUIRES_FIELD_ORDER = (!names.equals(expected) && !REVERSE_FIELDS);
    String arch = System.getProperty("os.arch").toLowerCase();
    isPPC = ("ppc".equals(arch) || "powerpc".equals(arch));
    isSPARC = "sparc".equals(arch);
    isARM = "arm".equals(arch);
  }
  
  static final int MAX_GNUC_ALIGNMENT = (isSPARC || ((isPPC || isARM) && Platform.isLinux())) ? 8 : Native.LONG_SIZE;
  
  protected static final int CALCULATE_SIZE = -1;
  
  static final Map layoutInfo = new WeakHashMap();
  
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
  
  protected Structure() {
    this((Pointer)null);
  }
  
  protected Structure(TypeMapper mapper) {
    this((Pointer)null, 0, mapper);
  }
  
  protected Structure(Pointer p) {
    this(p, 0);
  }
  
  protected Structure(Pointer p, int alignType) {
    this(p, alignType, null);
  }
  
  protected Structure(Pointer p, int alignType, TypeMapper mapper) {
    setAlignType(alignType);
    setTypeMapper(mapper);
    if (p != null) {
      useMemory(p);
    } else {
      allocateMemory(-1);
    } 
  }
  
  Map fields() {
    return this.structFields;
  }
  
  TypeMapper getTypeMapper() {
    return this.typeMapper;
  }
  
  protected void setTypeMapper(TypeMapper mapper) {
    if (mapper == null) {
      Class declaring = getClass().getDeclaringClass();
      if (declaring != null)
        mapper = Native.getTypeMapper(declaring); 
    } 
    this.typeMapper = mapper;
    this.size = -1;
    if (this.memory instanceof AutoAllocated)
      this.memory = null; 
  }
  
  protected void setAlignType(int alignType) {
    if (alignType == 0) {
      Class declaring = getClass().getDeclaringClass();
      if (declaring != null)
        alignType = Native.getStructureAlignment(declaring); 
      if (alignType == 0)
        if (Platform.isWindows()) {
          alignType = 3;
        } else {
          alignType = 2;
        }  
    } 
    this.alignType = alignType;
    this.size = -1;
    if (this.memory instanceof AutoAllocated)
      this.memory = null; 
  }
  
  protected Memory autoAllocate(int size) {
    return new AutoAllocated(size);
  }
  
  protected void useMemory(Pointer m) {
    useMemory(m, 0);
  }
  
  protected void useMemory(Pointer m, int offset) {
    try {
      this.memory = m.share(offset);
      if (this.size == -1)
        this.size = calculateSize(false); 
      if (this.size != -1)
        this.memory = m.share(offset, this.size); 
      this.array = null;
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Structure exceeds provided memory bounds");
    } 
  }
  
  protected void ensureAllocated() {
    ensureAllocated(false);
  }
  
  private void ensureAllocated(boolean avoidFFIType) {
    if (this.memory == null) {
      allocateMemory(avoidFFIType);
    } else if (this.size == -1) {
      this.size = calculateSize(true, avoidFFIType);
    } 
  }
  
  protected void allocateMemory() {
    allocateMemory(false);
  }
  
  private void allocateMemory(boolean avoidFFIType) {
    allocateMemory(calculateSize(true, avoidFFIType));
  }
  
  protected void allocateMemory(int size) {
    if (size == -1) {
      size = calculateSize(false);
    } else if (size <= 0) {
      throw new IllegalArgumentException("Structure size must be greater than zero: " + size);
    } 
    if (size != -1) {
      if (this.memory == null || this.memory instanceof AutoAllocated)
        this.memory = autoAllocate(size); 
      this.size = size;
    } 
  }
  
  public int size() {
    ensureAllocated();
    if (this.size == -1)
      this.size = calculateSize(true); 
    return this.size;
  }
  
  public void clear() {
    this.memory.clear(size());
  }
  
  public Pointer getPointer() {
    ensureAllocated();
    return this.memory;
  }
  
  private static final ThreadLocal reads = new ThreadLocal() {
      protected synchronized Object initialValue() {
        return new HashMap();
      }
    };
  
  private static final ThreadLocal busy = new ThreadLocal() {
      class StructureSet extends AbstractCollection implements Set {
        private Structure[] elements;
        
        private int count;
        
        private final Structure.null this$0;
        
        private void ensureCapacity(int size) {
          if (this.elements == null) {
            this.elements = new Structure[size * 3 / 2];
          } else if (this.elements.length < size) {
            Structure[] e = new Structure[size * 3 / 2];
            System.arraycopy(this.elements, 0, e, 0, this.elements.length);
            this.elements = e;
          } 
        }
        
        public int size() {
          return this.count;
        }
        
        public boolean contains(Object o) {
          return (indexOf(o) != -1);
        }
        
        public boolean add(Object o) {
          if (!contains(o)) {
            ensureCapacity(this.count + 1);
            this.elements[this.count++] = (Structure)o;
          } 
          return true;
        }
        
        private int indexOf(Object o) {
          Structure s1 = (Structure)o;
          for (int i = 0; i < this.count; i++) {
            Structure s2 = this.elements[i];
            if (s1 == s2 || (s1.getClass() == s2.getClass() && s1.size() == s2.size() && s1.getPointer().equals(s2.getPointer())))
              return i; 
          } 
          return -1;
        }
        
        public boolean remove(Object o) {
          int idx = indexOf(o);
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
          Structure[] e = new Structure[this.count];
          if (this.count > 0)
            System.arraycopy(this.elements, 0, e, 0, this.count); 
          return Arrays.<Structure>asList(e).iterator();
        }
      }
      
      protected synchronized Object initialValue() {
        return new StructureSet();
      }
    };
  
  static Class class$java$lang$Void;
  
  static Set busy() {
    return busy.get();
  }
  
  static Map reading() {
    return reads.get();
  }
  
  public void read() {
    ensureAllocated();
    if (busy().contains(this))
      return; 
    busy().add(this);
    if (this instanceof ByReference)
      reading().put(getPointer(), this); 
    try {
      for (Iterator i = fields().values().iterator(); i.hasNext();)
        readField(i.next()); 
    } finally {
      busy().remove(this);
      if (reading().get(getPointer()) == this)
        reading().remove(getPointer()); 
    } 
  }
  
  protected int fieldOffset(String name) {
    ensureAllocated();
    StructField f = (StructField)fields().get(name);
    if (f == null)
      throw new IllegalArgumentException("No such field: " + name); 
    return f.offset;
  }
  
  public Object readField(String name) {
    ensureAllocated();
    StructField f = (StructField)fields().get(name);
    if (f == null)
      throw new IllegalArgumentException("No such field: " + name); 
    return readField(f);
  }
  
  Object getField(StructField structField) {
    try {
      return structField.field.get(this);
    } catch (Exception e) {
      throw new Error("Exception reading field '" + structField.name + "' in " + getClass() + ": " + e);
    } 
  }
  
  void setField(StructField structField, Object value) {
    setField(structField, value, false);
  }
  
  void setField(StructField structField, Object value, boolean overrideFinal) {
    try {
      structField.field.set(this, value);
    } catch (IllegalAccessException e) {
      int modifiers = structField.field.getModifiers();
      if (Modifier.isFinal(modifiers)) {
        if (overrideFinal)
          throw new UnsupportedOperationException("This VM does not support Structures with final fields (field '" + structField.name + "' within " + getClass() + ")"); 
        throw new UnsupportedOperationException("Attempt to write to read-only field '" + structField.name + "' within " + getClass());
      } 
      throw new Error("Unexpectedly unable to write to field '" + structField.name + "' within " + getClass() + ": " + e);
    } 
  }
  
  static Structure updateStructureByReference(Class type, Structure s, Pointer address) {
    if (address == null) {
      s = null;
    } else {
      if (s == null || !address.equals(s.getPointer())) {
        Structure s1 = (Structure)reading().get(address);
        if (s1 != null && type.equals(s1.getClass())) {
          s = s1;
        } else {
          s = newInstance(type);
          s.useMemory(address);
        } 
      } 
      s.autoRead();
    } 
    return s;
  }
  
  Object readField(StructField structField) {
    int offset = structField.offset;
    Class fieldType = structField.type;
    FromNativeConverter readConverter = structField.readConverter;
    if (readConverter != null)
      fieldType = readConverter.nativeType(); 
    Object currentValue = (Structure.class.isAssignableFrom(fieldType) || Callback.class.isAssignableFrom(fieldType) || (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(fieldType)) || Pointer.class.isAssignableFrom(fieldType) || NativeMapped.class.isAssignableFrom(fieldType) || fieldType.isArray()) ? getField(structField) : null;
    Object result = this.memory.getValue(offset, fieldType, currentValue);
    if (readConverter != null)
      result = readConverter.fromNative(result, structField.context); 
    setField(structField, result, true);
    return result;
  }
  
  public void write() {
    ensureAllocated();
    if (this instanceof ByValue)
      getTypeInfo(); 
    if (busy().contains(this))
      return; 
    busy().add(this);
    try {
      for (Iterator i = fields().values().iterator(); i.hasNext(); ) {
        StructField sf = i.next();
        if (!sf.isVolatile)
          writeField(sf); 
      } 
    } finally {
      busy().remove(this);
    } 
  }
  
  public void writeField(String name) {
    ensureAllocated();
    StructField f = (StructField)fields().get(name);
    if (f == null)
      throw new IllegalArgumentException("No such field: " + name); 
    writeField(f);
  }
  
  public void writeField(String name, Object value) {
    ensureAllocated();
    StructField f = (StructField)fields().get(name);
    if (f == null)
      throw new IllegalArgumentException("No such field: " + name); 
    setField(f, value);
    writeField(f);
  }
  
  void writeField(StructField structField) {
    if (structField.isReadOnly)
      return; 
    int offset = structField.offset;
    Object value = getField(structField);
    Class fieldType = structField.type;
    ToNativeConverter converter = structField.writeConverter;
    if (converter != null) {
      value = converter.toNative(value, new StructureWriteContext(this, structField.field));
      fieldType = converter.nativeType();
    } 
    if (String.class == fieldType || WString.class == fieldType) {
      boolean wide = (fieldType == WString.class);
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
    } catch (IllegalArgumentException e) {
      String msg = "Structure field \"" + structField.name + "\" was declared as " + structField.type + ((structField.type == fieldType) ? "" : (" (native type " + fieldType + ")")) + ", which is not supported within a Structure";
      throw new IllegalArgumentException(msg);
    } 
  }
  
  private boolean hasFieldOrder() {
    synchronized (this) {
      return (this.fieldOrder != null);
    } 
  }
  
  protected List getFieldOrder() {
    synchronized (this) {
      if (this.fieldOrder == null)
        this.fieldOrder = new ArrayList(); 
      return this.fieldOrder;
    } 
  }
  
  protected void setFieldOrder(String[] fields) {
    getFieldOrder().addAll(Arrays.asList(fields));
    this.size = -1;
    if (this.memory instanceof AutoAllocated)
      this.memory = null; 
  }
  
  protected void sortFields(List fields, List names) {
    for (int i = 0; i < names.size(); i++) {
      String name = names.get(i);
      for (int f = 0; f < fields.size(); f++) {
        Field field = fields.get(f);
        if (name.equals(field.getName())) {
          Collections.swap(fields, i, f);
          break;
        } 
      } 
    } 
  }
  
  protected List getFields(boolean force) {
    List flist = new ArrayList();
    Class cls = getClass();
    for (; !cls.equals(Structure.class); 
      cls = cls.getSuperclass()) {
      List classFields = new ArrayList();
      Field[] fields = cls.getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
        int modifiers = fields[i].getModifiers();
        if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
          classFields.add(fields[i]); 
      } 
      if (REVERSE_FIELDS)
        Collections.reverse(classFields); 
      flist.addAll(0, classFields);
    } 
    if (REQUIRES_FIELD_ORDER || hasFieldOrder()) {
      List fieldOrder = getFieldOrder();
      if (fieldOrder.size() < flist.size()) {
        if (force)
          throw new Error("This VM does not store fields in a predictable order; you must use Structure.setFieldOrder to explicitly indicate the field order: " + System.getProperty("java.vendor") + ", " + System.getProperty("java.version")); 
        return null;
      } 
      sortFields(flist, fieldOrder);
    } 
    return flist;
  }
  
  private synchronized boolean fieldOrderMatch(List fieldOrder) {
    return (this.fieldOrder == fieldOrder || (this.fieldOrder != null && this.fieldOrder.equals(fieldOrder)));
  }
  
  private int calculateSize(boolean force) {
    return calculateSize(force, false);
  }
  
  int calculateSize(boolean force, boolean avoidFFIType) {
    LayoutInfo info;
    boolean needsInit = true;
    synchronized (layoutInfo) {
      info = (LayoutInfo)layoutInfo.get(getClass());
    } 
    if (info == null || this.alignType != info.alignType || this.typeMapper != info.typeMapper || !fieldOrderMatch(info.fieldOrder)) {
      info = deriveLayout(force, avoidFFIType);
      needsInit = false;
    } 
    if (info != null) {
      this.structAlignment = info.alignment;
      this.structFields = info.fields;
      info.alignType = this.alignType;
      info.typeMapper = this.typeMapper;
      info.fieldOrder = this.fieldOrder;
      if (!info.variable)
        synchronized (layoutInfo) {
          layoutInfo.put(getClass(), info);
        }  
      if (needsInit)
        initializeFields(); 
      return info.size;
    } 
    return -1;
  }
  
  private class LayoutInfo {
    private LayoutInfo() {}
    
    int size = -1;
    
    int alignment = 1;
    
    Map fields = Collections.synchronizedMap(new LinkedHashMap());
    
    int alignType = 0;
    
    TypeMapper typeMapper;
    
    List fieldOrder;
    
    boolean variable;
    
    private final Structure this$0;
  }
  
  private LayoutInfo deriveLayout(boolean force, boolean avoidFFIType) {
    LayoutInfo info = new LayoutInfo();
    int calculatedSize = 0;
    List fields = getFields(force);
    if (fields == null)
      return null; 
    boolean firstField = true;
    for (Iterator i = fields.iterator(); i.hasNext(); firstField = false) {
      Field field = i.next();
      int modifiers = field.getModifiers();
      Class type = field.getType();
      if (type.isArray())
        info.variable = true; 
      StructField structField = new StructField();
      structField.isVolatile = Modifier.isVolatile(modifiers);
      structField.isReadOnly = Modifier.isFinal(modifiers);
      if (structField.isReadOnly) {
        if (!Platform.RO_FIELDS)
          throw new IllegalArgumentException("This VM does not support read-only fields (field '" + field.getName() + "' within " + getClass() + ")"); 
        field.setAccessible(true);
      } 
      structField.field = field;
      structField.name = field.getName();
      structField.type = type;
      if (Callback.class.isAssignableFrom(type) && !type.isInterface())
        throw new IllegalArgumentException("Structure Callback field '" + field.getName() + "' must be an interface"); 
      if (type.isArray() && Structure.class.equals(type.getComponentType())) {
        String msg = "Nested Structure arrays must use a derived Structure type so that the size of the elements can be determined";
        throw new IllegalArgumentException(msg);
      } 
      int fieldAlignment = 1;
      if (Modifier.isPublic(field.getModifiers())) {
        Object value = getField(structField);
        if (value == null && type.isArray()) {
          if (force)
            throw new IllegalStateException("Array fields must be initialized"); 
          return null;
        } 
        Class nativeType = type;
        if (NativeMapped.class.isAssignableFrom(type)) {
          NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
          nativeType = tc.nativeType();
          structField.writeConverter = tc;
          structField.readConverter = tc;
          structField.context = new StructureReadContext(this, field);
        } else if (this.typeMapper != null) {
          ToNativeConverter writeConverter = this.typeMapper.getToNativeConverter(type);
          FromNativeConverter readConverter = this.typeMapper.getFromNativeConverter(type);
          if (writeConverter != null && readConverter != null) {
            value = writeConverter.toNative(value, new StructureWriteContext(this, structField.field));
            nativeType = (value != null) ? value.getClass() : Pointer.class;
            structField.writeConverter = writeConverter;
            structField.readConverter = readConverter;
            structField.context = new StructureReadContext(this, field);
          } else if (writeConverter != null || readConverter != null) {
            String msg = "Structures require bidirectional type conversion for " + type;
            throw new IllegalArgumentException(msg);
          } 
        } 
        if (value == null)
          value = initializeField(structField, type); 
        try {
          structField.size = getNativeSize(nativeType, value);
          fieldAlignment = getNativeAlignment(nativeType, value, firstField);
        } catch (IllegalArgumentException e) {
          if (!force && this.typeMapper == null)
            return null; 
          String msg = "Invalid Structure field in " + getClass() + ", field name '" + structField.name + "', " + structField.type + ": " + e.getMessage();
          throw new IllegalArgumentException(msg);
        } 
        info.alignment = Math.max(info.alignment, fieldAlignment);
        if (calculatedSize % fieldAlignment != 0)
          calculatedSize += fieldAlignment - calculatedSize % fieldAlignment; 
        structField.offset = calculatedSize;
        calculatedSize += structField.size;
        info.fields.put(structField.name, structField);
      } 
    } 
    if (calculatedSize > 0) {
      int size = calculateAlignedSize(calculatedSize, info.alignment);
      if (this instanceof ByValue && !avoidFFIType)
        getTypeInfo(); 
      if (this.memory != null && !(this.memory instanceof AutoAllocated))
        this.memory = this.memory.share(0L, size); 
      info.size = size;
      return info;
    } 
    throw new IllegalArgumentException("Structure " + getClass() + " has unknown size (ensure " + "all fields are public)");
  }
  
  private void initializeFields() {
    for (Iterator i = fields().values().iterator(); i.hasNext(); ) {
      StructField f = i.next();
      initializeField(f, f.type);
    } 
  }
  
  private Object initializeField(StructField structField, Class type) {
    Object value = null;
    if (Structure.class.isAssignableFrom(type) && !ByReference.class.isAssignableFrom(type)) {
      try {
        value = newInstance(type);
        setField(structField, value);
      } catch (IllegalArgumentException e) {
        String msg = "Can't determine size of nested structure: " + e.getMessage();
        throw new IllegalArgumentException(msg);
      } 
    } else if (NativeMapped.class.isAssignableFrom(type)) {
      NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
      value = tc.defaultValue();
      setField(structField, value);
    } 
    return value;
  }
  
  int calculateAlignedSize(int calculatedSize) {
    return calculateAlignedSize(calculatedSize, this.structAlignment);
  }
  
  private int calculateAlignedSize(int calculatedSize, int alignment) {
    if (this.alignType != 1 && 
      calculatedSize % alignment != 0)
      calculatedSize += alignment - calculatedSize % alignment; 
    return calculatedSize;
  }
  
  protected int getStructAlignment() {
    if (this.size == -1)
      calculateSize(true); 
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
    } else if (Pointer.class == type || (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(type)) || Callback.class.isAssignableFrom(type) || WString.class == type || String.class == type) {
      alignment = Pointer.SIZE;
    } else if (Structure.class.isAssignableFrom(type)) {
      if (ByReference.class.isAssignableFrom(type)) {
        alignment = Pointer.SIZE;
      } else {
        if (value == null)
          value = newInstance(type); 
        alignment = ((Structure)value).getStructAlignment();
      } 
    } else if (type.isArray()) {
      alignment = getNativeAlignment(type.getComponentType(), null, isFirstElement);
    } else {
      throw new IllegalArgumentException("Type " + type + " has unknown " + "native alignment");
    } 
    if (this.alignType == 1) {
      alignment = 1;
    } else if (this.alignType == 3) {
      alignment = Math.min(8, alignment);
    } else if (this.alignType == 2) {
      if (!isFirstElement || !Platform.isMac() || !isPPC)
        alignment = Math.min(MAX_GNUC_ALIGNMENT, alignment); 
    } 
    return alignment;
  }
  
  public String toString() {
    return toString(Boolean.getBoolean("jna.dump_memory"));
  }
  
  public String toString(boolean debug) {
    return toString(0, true, true);
  }
  
  private String format(Class type) {
    String s = type.getName();
    int dot = s.lastIndexOf(".");
    return s.substring(dot + 1);
  }
  
  private String toString(int indent, boolean showContents, boolean dumpMemory) {
    ensureAllocated();
    String LS = System.getProperty("line.separator");
    String name = format(getClass()) + "(" + getPointer() + ")";
    if (!(getPointer() instanceof Memory))
      name = name + " (" + size() + " bytes)"; 
    String prefix = "";
    for (int idx = 0; idx < indent; idx++)
      prefix = prefix + "  "; 
    String contents = LS;
    if (!showContents) {
      contents = "...}";
    } else {
      for (Iterator i = fields().values().iterator(); i.hasNext(); ) {
        StructField sf = i.next();
        Object value = getField(sf);
        String type = format(sf.type);
        String index = "";
        contents = contents + prefix;
        if (sf.type.isArray() && value != null) {
          type = format(sf.type.getComponentType());
          index = "[" + Array.getLength(value) + "]";
        } 
        contents = contents + "  " + type + " " + sf.name + index + "@" + Integer.toHexString(sf.offset);
        if (value instanceof Structure)
          value = ((Structure)value).toString(indent + 1, !(value instanceof ByReference), dumpMemory); 
        contents = contents + "=";
        if (value instanceof Long) {
          contents = contents + Long.toHexString(((Long)value).longValue());
        } else if (value instanceof Integer) {
          contents = contents + Integer.toHexString(((Integer)value).intValue());
        } else if (value instanceof Short) {
          contents = contents + Integer.toHexString(((Short)value).shortValue());
        } else if (value instanceof Byte) {
          contents = contents + Integer.toHexString(((Byte)value).byteValue());
        } else {
          contents = contents + String.valueOf(value).trim();
        } 
        contents = contents + LS;
        if (!i.hasNext())
          contents = contents + prefix + "}"; 
      } 
    } 
    if (indent == 0 && dumpMemory) {
      int BYTES_PER_ROW = 4;
      contents = contents + LS + "memory dump" + LS;
      byte[] buf = getPointer().getByteArray(0L, size());
      for (int i = 0; i < buf.length; i++) {
        if (i % 4 == 0)
          contents = contents + "["; 
        if (buf[i] >= 0 && buf[i] < 16)
          contents = contents + "0"; 
        contents = contents + Integer.toHexString(buf[i] & 0xFF);
        if (i % 4 == 3 && i < buf.length - 1)
          contents = contents + "]" + LS; 
      } 
      contents = contents + "]";
    } 
    return name + " {" + contents;
  }
  
  public Structure[] toArray(Structure[] array) {
    ensureAllocated();
    if (this.memory instanceof AutoAllocated) {
      Memory m = (Memory)this.memory;
      int requiredSize = array.length * size();
      if (m.size() < requiredSize)
        useMemory(autoAllocate(requiredSize)); 
    } 
    array[0] = this;
    int size = size();
    for (int i = 1; i < array.length; i++) {
      array[i] = newInstance(getClass());
      array[i].useMemory(this.memory.share((i * size), size));
      array[i].read();
    } 
    if (!(this instanceof ByValue))
      this.array = array; 
    return array;
  }
  
  public Structure[] toArray(int size) {
    return toArray((Structure[])Array.newInstance(getClass(), size));
  }
  
  private Class baseClass() {
    if ((this instanceof ByReference || this instanceof ByValue) && Structure.class.isAssignableFrom(getClass().getSuperclass()))
      return getClass().getSuperclass(); 
    return getClass();
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof Structure))
      return false; 
    if (o.getClass() != getClass() && ((Structure)o).baseClass() != baseClass())
      return false; 
    Structure s = (Structure)o;
    if (s.getPointer().equals(getPointer()))
      return true; 
    if (s.size() == size()) {
      clear();
      write();
      byte[] buf = getPointer().getByteArray(0L, size());
      s.clear();
      s.write();
      byte[] sbuf = s.getPointer().getByteArray(0L, s.size());
      return Arrays.equals(buf, sbuf);
    } 
    return false;
  }
  
  public int hashCode() {
    clear();
    write();
    Adler32 code = new Adler32();
    code.update(getPointer().getByteArray(0L, size()));
    return (int)code.getValue();
  }
  
  protected void cacheTypeInfo(Pointer p) {
    this.typeInfo = p.peer;
  }
  
  protected Pointer getFieldTypeInfo(StructField f) {
    Class type = f.type;
    Object value = getField(f);
    if (this.typeMapper != null) {
      ToNativeConverter nc = this.typeMapper.getToNativeConverter(type);
      if (nc != null) {
        type = nc.nativeType();
        value = nc.toNative(value, new ToNativeContext());
      } 
    } 
    return 
      
      FFIType.get(value, type);
  }
  
  Pointer getTypeInfo() {
    Pointer p = getTypeInfo(this);
    cacheTypeInfo(p);
    return p;
  }
  
  public void setAutoSynch(boolean auto) {
    setAutoRead(auto);
    setAutoWrite(auto);
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
      Structure s = type.newInstance();
      if (s instanceof ByValue)
        s.allocateMemory(); 
      return s;
    } catch (InstantiationException e) {
      String msg = "Can't instantiate " + type + " (" + e + ")";
      throw new IllegalArgumentException(msg);
    } catch (IllegalAccessException e) {
      String msg = "Instantiation of " + type + " not allowed, is it public? (" + e + ")";
      throw new IllegalArgumentException(msg);
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
    
    private final Structure this$0;
    
    public String toString() {
      return this.name + "@" + this.offset + "[" + this.size + "] (" + this.type + ")";
    }
  }
  
  static class FFIType extends Structure {
    public static class size_t extends IntegerType {
      public size_t() {
        this(0L);
      }
      
      public size_t(long value) {
        super(Native.POINTER_SIZE, value);
      }
    }
    
    private static Map typeInfoMap = new WeakHashMap();
    
    private static final int FFI_TYPE_STRUCT = 13;
    
    public size_t size;
    
    public short alignment;
    
    public short type;
    
    public Pointer elements;
    
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
    }
    
    static {
      if (Native.POINTER_SIZE == 0)
        throw new Error("Native library not initialized"); 
      if (FFITypes.ffi_type_void == null)
        throw new Error("FFI types not initialized"); 
      typeInfoMap.put(void.class, FFITypes.ffi_type_void);
      typeInfoMap.put((Structure.class$java$lang$Void == null) ? (Structure.class$java$lang$Void = Structure.class$("java.lang.Void")) : Structure.class$java$lang$Void, FFITypes.ffi_type_void);
      typeInfoMap.put(float.class, FFITypes.ffi_type_float);
      typeInfoMap.put((Structure.class$java$lang$Float == null) ? (Structure.class$java$lang$Float = Structure.class$("java.lang.Float")) : Structure.class$java$lang$Float, FFITypes.ffi_type_float);
      typeInfoMap.put(double.class, FFITypes.ffi_type_double);
      typeInfoMap.put((Structure.class$java$lang$Double == null) ? (Structure.class$java$lang$Double = Structure.class$("java.lang.Double")) : Structure.class$java$lang$Double, FFITypes.ffi_type_double);
      typeInfoMap.put(long.class, FFITypes.ffi_type_sint64);
      typeInfoMap.put((Structure.class$java$lang$Long == null) ? (Structure.class$java$lang$Long = Structure.class$("java.lang.Long")) : Structure.class$java$lang$Long, FFITypes.ffi_type_sint64);
      typeInfoMap.put(int.class, FFITypes.ffi_type_sint32);
      typeInfoMap.put((Structure.class$java$lang$Integer == null) ? (Structure.class$java$lang$Integer = Structure.class$("java.lang.Integer")) : Structure.class$java$lang$Integer, FFITypes.ffi_type_sint32);
      typeInfoMap.put(short.class, FFITypes.ffi_type_sint16);
      typeInfoMap.put((Structure.class$java$lang$Short == null) ? (Structure.class$java$lang$Short = Structure.class$("java.lang.Short")) : Structure.class$java$lang$Short, FFITypes.ffi_type_sint16);
      Pointer ctype = (Native.WCHAR_SIZE == 2) ? FFITypes.ffi_type_uint16 : FFITypes.ffi_type_uint32;
      typeInfoMap.put(char.class, ctype);
      typeInfoMap.put((Structure.class$java$lang$Character == null) ? (Structure.class$java$lang$Character = Structure.class$("java.lang.Character")) : Structure.class$java$lang$Character, ctype);
      typeInfoMap.put(byte.class, FFITypes.ffi_type_sint8);
      typeInfoMap.put((Structure.class$java$lang$Byte == null) ? (Structure.class$java$lang$Byte = Structure.class$("java.lang.Byte")) : Structure.class$java$lang$Byte, FFITypes.ffi_type_sint8);
      typeInfoMap.put((Structure.class$com$sun$jna$Pointer == null) ? (Structure.class$com$sun$jna$Pointer = Structure.class$("com.sun.jna.Pointer")) : Structure.class$com$sun$jna$Pointer, FFITypes.ffi_type_pointer);
      typeInfoMap.put((Structure.class$java$lang$String == null) ? (Structure.class$java$lang$String = Structure.class$("java.lang.String")) : Structure.class$java$lang$String, FFITypes.ffi_type_pointer);
      typeInfoMap.put((Structure.class$com$sun$jna$WString == null) ? (Structure.class$com$sun$jna$WString = Structure.class$("com.sun.jna.WString")) : Structure.class$com$sun$jna$WString, FFITypes.ffi_type_pointer);
      typeInfoMap.put(boolean.class, FFITypes.ffi_type_uint32);
      typeInfoMap.put((Structure.class$java$lang$Boolean == null) ? (Structure.class$java$lang$Boolean = Structure.class$("java.lang.Boolean")) : Structure.class$java$lang$Boolean, FFITypes.ffi_type_uint32);
    }
    
    private FFIType(Structure ref) {
      Pointer[] els;
      this.type = 13;
      ref.ensureAllocated(true);
      if (ref instanceof Union) {
        Structure.StructField sf = ((Union)ref).biggestField;
        els = new Pointer[] { get(ref.getField(sf), sf.type), null };
      } else {
        els = new Pointer[ref.fields().size() + 1];
        int idx = 0;
        for (Iterator i = ref.fields().values().iterator(); i.hasNext(); ) {
          Structure.StructField sf = i.next();
          els[idx++] = ref.getFieldTypeInfo(sf);
        } 
      } 
      init(els);
    }
    
    private FFIType(Object array, Class type) {
      this.type = 13;
      int length = Array.getLength(array);
      Pointer[] els = new Pointer[length + 1];
      Pointer p = get((Object)null, type.getComponentType());
      for (int i = 0; i < length; i++)
        els[i] = p; 
      init(els);
    }
    
    private void init(Pointer[] els) {
      this.elements = new Memory((Pointer.SIZE * els.length));
      this.elements.write(0L, els, 0, els.length);
      write();
    }
    
    static Pointer get(Object obj) {
      if (obj == null)
        return FFITypes.ffi_type_pointer; 
      if (obj instanceof Class)
        return get((Object)null, (Class)obj); 
      return get(obj, obj.getClass());
    }
    
    private static Pointer get(Object obj, Class cls) {
      TypeMapper mapper = Native.getTypeMapper(cls);
      if (mapper != null) {
        ToNativeConverter nc = mapper.getToNativeConverter(cls);
        if (nc != null)
          cls = nc.nativeType(); 
      } 
      synchronized (typeInfoMap) {
        Object o = typeInfoMap.get(cls);
        if (o instanceof Pointer)
          return (Pointer)o; 
        if (o instanceof FFIType)
          return ((FFIType)o).getPointer(); 
        if ((Platform.HAS_BUFFERS && ((Structure.class$java$nio$Buffer == null) ? (Structure.class$java$nio$Buffer = Structure.class$("java.nio.Buffer")) : Structure.class$java$nio$Buffer).isAssignableFrom(cls)) || ((Structure.class$com$sun$jna$Callback == null) ? (Structure.class$com$sun$jna$Callback = Structure.class$("com.sun.jna.Callback")) : Structure.class$com$sun$jna$Callback).isAssignableFrom(cls)) {
          typeInfoMap.put(cls, FFITypes.ffi_type_pointer);
          return FFITypes.ffi_type_pointer;
        } 
        if (((Structure.class$com$sun$jna$Structure == null) ? (Structure.class$com$sun$jna$Structure = Structure.class$("com.sun.jna.Structure")) : Structure.class$com$sun$jna$Structure).isAssignableFrom(cls)) {
          if (obj == null)
            obj = newInstance(cls); 
          if (((Structure.class$com$sun$jna$Structure$ByReference == null) ? (Structure.class$com$sun$jna$Structure$ByReference = Structure.class$("com.sun.jna.Structure$ByReference")) : Structure.class$com$sun$jna$Structure$ByReference).isAssignableFrom(cls)) {
            typeInfoMap.put(cls, FFITypes.ffi_type_pointer);
            return FFITypes.ffi_type_pointer;
          } 
          FFIType type = new FFIType((Structure)obj);
          typeInfoMap.put(cls, type);
          return type.getPointer();
        } 
        if (((Structure.class$com$sun$jna$NativeMapped == null) ? (Structure.class$com$sun$jna$NativeMapped = Structure.class$("com.sun.jna.NativeMapped")) : Structure.class$com$sun$jna$NativeMapped).isAssignableFrom(cls)) {
          NativeMappedConverter c = NativeMappedConverter.getInstance(cls);
          return get(c.toNative(obj, new ToNativeContext()), c.nativeType());
        } 
        if (cls.isArray()) {
          FFIType type = new FFIType(obj, cls);
          typeInfoMap.put(obj, type);
          return type.getPointer();
        } 
        throw new IllegalArgumentException("Unsupported Structure field type " + cls);
      } 
    }
  }
  
  private class AutoAllocated extends Memory {
    private final Structure this$0;
    
    public AutoAllocated(int size) {
      super(size);
      clear();
    }
  }
  
  private static void structureArrayCheck(Structure[] ss) {
    Pointer base = ss[0].getPointer();
    int size = ss[0].size();
    for (int si = 1; si < ss.length; si++) {
      if ((ss[si].getPointer()).peer != base.peer + (size * si)) {
        String msg = "Structure array elements must use contiguous memory (bad backing address at Structure array index " + si + ")";
        throw new IllegalArgumentException(msg);
      } 
    } 
  }
  
  public static void autoRead(Structure[] ss) {
    structureArrayCheck(ss);
    if ((ss[0]).array == ss) {
      ss[0].autoRead();
    } else {
      for (int si = 0; si < ss.length; si++)
        ss[si].autoRead(); 
    } 
  }
  
  public void autoRead() {
    if (getAutoRead()) {
      read();
      if (this.array != null)
        for (int i = 1; i < this.array.length; i++)
          this.array[i].autoRead();  
    } 
  }
  
  public static void autoWrite(Structure[] ss) {
    structureArrayCheck(ss);
    if ((ss[0]).array == ss) {
      ss[0].autoWrite();
    } else {
      for (int si = 0; si < ss.length; si++)
        ss[si].autoWrite(); 
    } 
  }
  
  public void autoWrite() {
    if (getAutoWrite()) {
      write();
      if (this.array != null)
        for (int i = 1; i < this.array.length; i++)
          this.array[i].autoWrite();  
    } 
  }
  
  protected int getNativeSize(Class nativeType, Object value) {
    return Native.getNativeSize(nativeType, value);
  }
  
  public static interface ByReference {}
  
  public static interface ByValue {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\Structure.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */