package com.sun.jna;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

public class Function extends Pointer {
  public static final int MAX_NARGS = 256;
  
  public static final int C_CONVENTION = 0;
  
  public static final int ALT_CONVENTION = 1;
  
  private static final int MASK_CC = 3;
  
  public static final int THROW_LAST_ERROR = 4;
  
  static final Integer INTEGER_TRUE = new Integer(-1);
  
  static final Integer INTEGER_FALSE = new Integer(0);
  
  private NativeLibrary library;
  
  private final String functionName;
  
  int callFlags;
  
  final Map options;
  
  static final String OPTION_INVOKING_METHOD = "invoking-method";
  
  static Class array$Lcom$sun$jna$Structure$ByReference;
  
  static Class array$Lcom$sun$jna$Structure;
  
  static Class array$Ljava$lang$String;
  
  static Class array$Lcom$sun$jna$WString;
  
  static Class array$Lcom$sun$jna$Pointer;
  
  static Class array$Lcom$sun$jna$NativeMapped;
  
  public static Function getFunction(String libraryName, String functionName) {
    return NativeLibrary.getInstance(libraryName).getFunction(functionName);
  }
  
  public static Function getFunction(String libraryName, String functionName, int callFlags) {
    return NativeLibrary.getInstance(libraryName).getFunction(functionName, callFlags);
  }
  
  public static Function getFunction(Pointer p) {
    return getFunction(p, 0);
  }
  
  public static Function getFunction(Pointer p, int callFlags) {
    return new Function(p, callFlags);
  }
  
  Function(NativeLibrary library, String functionName, int callFlags) {
    checkCallingConvention(callFlags & 0x3);
    if (functionName == null)
      throw new NullPointerException("Function name must not be null"); 
    this.library = library;
    this.functionName = functionName;
    this.callFlags = callFlags;
    this.options = library.options;
    try {
      this.peer = library.getSymbolAddress(functionName);
    } catch (UnsatisfiedLinkError e) {
      throw new UnsatisfiedLinkError("Error looking up function '" + functionName + "': " + e.getMessage());
    } 
  }
  
  Function(Pointer functionAddress, int callFlags) {
    checkCallingConvention(callFlags & 0x3);
    if (functionAddress == null || functionAddress.peer == 0L)
      throw new NullPointerException("Function address may not be null"); 
    this.functionName = functionAddress.toString();
    this.callFlags = callFlags;
    this.peer = functionAddress.peer;
    this.options = Collections.EMPTY_MAP;
  }
  
  private void checkCallingConvention(int convention) throws IllegalArgumentException {
    switch (convention) {
      case 0:
      case 1:
        return;
    } 
    throw new IllegalArgumentException("Unrecognized calling convention: " + convention);
  }
  
  public String getName() {
    return this.functionName;
  }
  
  public int getCallingConvention() {
    return this.callFlags & 0x3;
  }
  
  public Object invoke(Class returnType, Object[] inArgs) {
    return invoke(returnType, inArgs, this.options);
  }
  
  public Object invoke(Class returnType, Object[] inArgs, Map options) {
    Object[] args = new Object[0];
    if (inArgs != null) {
      if (inArgs.length > 256)
        throw new UnsupportedOperationException("Maximum argument count is 256"); 
      args = new Object[inArgs.length];
      System.arraycopy(inArgs, 0, args, 0, args.length);
    } 
    TypeMapper mapper = (TypeMapper)options.get("type-mapper");
    Method invokingMethod = (Method)options.get("invoking-method");
    boolean allowObjects = Boolean.TRUE.equals(options.get("allow-objects"));
    for (int i = 0; i < args.length; i++)
      args[i] = convertArgument(args, i, invokingMethod, mapper, allowObjects); 
    Class nativeType = returnType;
    FromNativeConverter resultConverter = null;
    if (NativeMapped.class.isAssignableFrom(returnType)) {
      NativeMappedConverter tc = NativeMappedConverter.getInstance(returnType);
      resultConverter = tc;
      nativeType = tc.nativeType();
    } else if (mapper != null) {
      resultConverter = mapper.getFromNativeConverter(returnType);
      if (resultConverter != null)
        nativeType = resultConverter.nativeType(); 
    } 
    Object result = invoke(args, nativeType, allowObjects);
    if (resultConverter != null) {
      FromNativeContext context;
      if (invokingMethod != null) {
        context = new MethodResultContext(returnType, this, inArgs, invokingMethod);
      } else {
        context = new FunctionResultContext(returnType, this, inArgs);
      } 
      result = resultConverter.fromNative(result, context);
    } 
    if (inArgs != null)
      for (int j = 0; j < inArgs.length; j++) {
        Object inArg = inArgs[j];
        if (inArg != null)
          if (inArg instanceof Structure) {
            if (!(inArg instanceof Structure.ByValue))
              ((Structure)inArg).autoRead(); 
          } else if (args[j] instanceof PostCallRead) {
            ((PostCallRead)args[j]).read();
            if (args[j] instanceof PointerArray) {
              PointerArray array = (PointerArray)args[j];
              if (((array$Lcom$sun$jna$Structure$ByReference == null) ? (array$Lcom$sun$jna$Structure$ByReference = class$("[Lcom.sun.jna.Structure$ByReference;")) : array$Lcom$sun$jna$Structure$ByReference).isAssignableFrom(inArg.getClass())) {
                Class type = inArg.getClass().getComponentType();
                Structure[] ss = (Structure[])inArg;
                for (int si = 0; si < ss.length; si++) {
                  Pointer p = array.getPointer((Pointer.SIZE * si));
                  ss[si] = Structure.updateStructureByReference(type, ss[si], p);
                } 
              } 
            } 
          } else if (((array$Lcom$sun$jna$Structure == null) ? (array$Lcom$sun$jna$Structure = class$("[Lcom.sun.jna.Structure;")) : array$Lcom$sun$jna$Structure).isAssignableFrom(inArg.getClass())) {
            Structure.autoRead((Structure[])inArg);
          }  
      }  
    return result;
  }
  
  Object invoke(Object[] args, Class returnType, boolean allowObjects) {
    Object result = null;
    if (returnType == null || returnType == void.class || returnType == Void.class) {
      Native.invokeVoid(this.peer, this.callFlags, args);
      result = null;
    } else if (returnType == boolean.class || returnType == Boolean.class) {
      result = valueOf((Native.invokeInt(this.peer, this.callFlags, args) != 0));
    } else if (returnType == byte.class || returnType == Byte.class) {
      result = new Byte((byte)Native.invokeInt(this.peer, this.callFlags, args));
    } else if (returnType == short.class || returnType == Short.class) {
      result = new Short((short)Native.invokeInt(this.peer, this.callFlags, args));
    } else if (returnType == char.class || returnType == Character.class) {
      result = new Character((char)Native.invokeInt(this.peer, this.callFlags, args));
    } else if (returnType == int.class || returnType == Integer.class) {
      result = new Integer(Native.invokeInt(this.peer, this.callFlags, args));
    } else if (returnType == long.class || returnType == Long.class) {
      result = new Long(Native.invokeLong(this.peer, this.callFlags, args));
    } else if (returnType == float.class || returnType == Float.class) {
      result = new Float(Native.invokeFloat(this.peer, this.callFlags, args));
    } else if (returnType == double.class || returnType == Double.class) {
      result = new Double(Native.invokeDouble(this.peer, this.callFlags, args));
    } else if (returnType == String.class) {
      result = invokeString(this.callFlags, args, false);
    } else if (returnType == WString.class) {
      String s = invokeString(this.callFlags, args, true);
      if (s != null)
        result = new WString(s); 
    } else {
      if (Pointer.class.isAssignableFrom(returnType))
        return invokePointer(this.callFlags, args); 
      if (Structure.class.isAssignableFrom(returnType)) {
        if (Structure.ByValue.class.isAssignableFrom(returnType)) {
          Structure s = Native.invokeStructure(this.peer, this.callFlags, args, Structure.newInstance(returnType));
          s.autoRead();
          result = s;
        } else {
          result = invokePointer(this.callFlags, args);
          if (result != null) {
            Structure s = Structure.newInstance(returnType);
            s.useMemory((Pointer)result);
            s.autoRead();
            result = s;
          } 
        } 
      } else if (Callback.class.isAssignableFrom(returnType)) {
        result = invokePointer(this.callFlags, args);
        if (result != null)
          result = CallbackReference.getCallback(returnType, (Pointer)result); 
      } else if (returnType == ((array$Ljava$lang$String == null) ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String)) {
        Pointer p = invokePointer(this.callFlags, args);
        if (p != null)
          result = p.getStringArray(0L); 
      } else if (returnType == ((array$Lcom$sun$jna$WString == null) ? (array$Lcom$sun$jna$WString = class$("[Lcom.sun.jna.WString;")) : array$Lcom$sun$jna$WString)) {
        Pointer p = invokePointer(this.callFlags, args);
        if (p != null) {
          String[] arr = p.getStringArray(0L, true);
          WString[] warr = new WString[arr.length];
          for (int i = 0; i < arr.length; i++)
            warr[i] = new WString(arr[i]); 
          result = warr;
        } 
      } else if (returnType == ((array$Lcom$sun$jna$Pointer == null) ? (array$Lcom$sun$jna$Pointer = class$("[Lcom.sun.jna.Pointer;")) : array$Lcom$sun$jna$Pointer)) {
        Pointer p = invokePointer(this.callFlags, args);
        if (p != null)
          result = p.getPointerArray(0L); 
      } else if (allowObjects) {
        result = Native.invokeObject(this.peer, this.callFlags, args);
        if (result != null && !returnType.isAssignableFrom(result.getClass()))
          throw new ClassCastException("Return type " + returnType + " does not match result " + result.getClass()); 
      } else {
        throw new IllegalArgumentException("Unsupported return type " + returnType + " in function " + getName());
      } 
    } 
    return result;
  }
  
  private Pointer invokePointer(int callFlags, Object[] args) {
    long ptr = Native.invokePointer(this.peer, callFlags, args);
    return (ptr == 0L) ? null : new Pointer(ptr);
  }
  
  private Object convertArgument(Object[] args, int index, Method invokingMethod, TypeMapper mapper, boolean allowObjects) {
    Object arg = args[index];
    if (arg != null) {
      Class type = arg.getClass();
      ToNativeConverter converter = null;
      if (NativeMapped.class.isAssignableFrom(type)) {
        converter = NativeMappedConverter.getInstance(type);
      } else if (mapper != null) {
        converter = mapper.getToNativeConverter(type);
      } 
      if (converter != null) {
        ToNativeContext context;
        if (invokingMethod != null) {
          context = new MethodParameterContext(this, args, index, invokingMethod);
        } else {
          context = new FunctionParameterContext(this, args, index);
        } 
        arg = converter.toNative(arg, context);
      } 
    } 
    if (arg == null || isPrimitiveArray(arg.getClass()))
      return arg; 
    Class argClass = arg.getClass();
    if (arg instanceof Structure) {
      Structure struct = (Structure)arg;
      struct.autoWrite();
      if (struct instanceof Structure.ByValue) {
        Class ptype = struct.getClass();
        if (invokingMethod != null) {
          Class[] ptypes = invokingMethod.getParameterTypes();
          if (isVarArgs(invokingMethod)) {
            if (index < ptypes.length - 1) {
              ptype = ptypes[index];
            } else {
              Class etype = ptypes[ptypes.length - 1].getComponentType();
              if (etype != Object.class)
                ptype = etype; 
            } 
          } else {
            ptype = ptypes[index];
          } 
        } 
        if (Structure.ByValue.class.isAssignableFrom(ptype))
          return struct; 
      } 
      return struct.getPointer();
    } 
    if (arg instanceof Callback)
      return CallbackReference.getFunctionPointer((Callback)arg); 
    if (arg instanceof String)
      return (new NativeString((String)arg, false)).getPointer(); 
    if (arg instanceof WString)
      return (new NativeString(arg.toString(), true)).getPointer(); 
    if (arg instanceof Boolean)
      return Boolean.TRUE.equals(arg) ? INTEGER_TRUE : INTEGER_FALSE; 
    if (((array$Ljava$lang$String == null) ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String) == argClass)
      return new StringArray((String[])arg); 
    if (((array$Lcom$sun$jna$WString == null) ? (array$Lcom$sun$jna$WString = class$("[Lcom.sun.jna.WString;")) : array$Lcom$sun$jna$WString) == argClass)
      return new StringArray((WString[])arg); 
    if (((array$Lcom$sun$jna$Pointer == null) ? (array$Lcom$sun$jna$Pointer = class$("[Lcom.sun.jna.Pointer;")) : array$Lcom$sun$jna$Pointer) == argClass)
      return new PointerArray((Pointer[])arg); 
    if (((array$Lcom$sun$jna$NativeMapped == null) ? (array$Lcom$sun$jna$NativeMapped = class$("[Lcom.sun.jna.NativeMapped;")) : array$Lcom$sun$jna$NativeMapped).isAssignableFrom(argClass))
      return new NativeMappedArray((NativeMapped[])arg); 
    if (((array$Lcom$sun$jna$Structure == null) ? (array$Lcom$sun$jna$Structure = class$("[Lcom.sun.jna.Structure;")) : array$Lcom$sun$jna$Structure).isAssignableFrom(argClass)) {
      Structure[] ss = (Structure[])arg;
      Class type = argClass.getComponentType();
      boolean byRef = Structure.ByReference.class.isAssignableFrom(type);
      if (byRef) {
        Pointer[] pointers = new Pointer[ss.length + 1];
        for (int i = 0; i < ss.length; i++)
          pointers[i] = (ss[i] != null) ? ss[i].getPointer() : null; 
        return new PointerArray(pointers);
      } 
      if (ss.length == 0)
        throw new IllegalArgumentException("Structure array must have non-zero length"); 
      if (ss[0] == null) {
        Structure.newInstance(type).toArray(ss);
        return ss[0].getPointer();
      } 
      Structure.autoWrite(ss);
      return ss[0].getPointer();
    } 
    if (argClass.isArray())
      throw new IllegalArgumentException("Unsupported array argument type: " + argClass.getComponentType()); 
    if (allowObjects)
      return arg; 
    if (!Native.isSupportedNativeType(arg.getClass()))
      throw new IllegalArgumentException("Unsupported argument type " + arg.getClass().getName() + " at parameter " + index + " of function " + getName()); 
    return arg;
  }
  
  private boolean isPrimitiveArray(Class argClass) {
    return (argClass.isArray() && argClass.getComponentType().isPrimitive());
  }
  
  public void invoke(Object[] args) {
    invoke(Void.class, args);
  }
  
  private String invokeString(int callFlags, Object[] args, boolean wide) {
    Pointer ptr = invokePointer(callFlags, args);
    String s = null;
    if (ptr != null)
      if (wide) {
        s = ptr.getString(0L, wide);
      } else {
        s = ptr.getString(0L);
      }  
    return s;
  }
  
  public String toString() {
    if (this.library != null)
      return "native function " + this.functionName + "(" + this.library.getName() + ")@0x" + Long.toHexString(this.peer); 
    return "native function@0x" + Long.toHexString(this.peer);
  }
  
  public Object invokeObject(Object[] args) {
    return invoke(Object.class, args);
  }
  
  public Pointer invokePointer(Object[] args) {
    return (Pointer)invoke(Pointer.class, args);
  }
  
  public String invokeString(Object[] args, boolean wide) {
    Object o = invoke(wide ? WString.class : String.class, args);
    return (o != null) ? o.toString() : null;
  }
  
  public int invokeInt(Object[] args) {
    return ((Integer)invoke(Integer.class, args)).intValue();
  }
  
  public long invokeLong(Object[] args) {
    return ((Long)invoke(Long.class, args)).longValue();
  }
  
  public float invokeFloat(Object[] args) {
    return ((Float)invoke(Float.class, args)).floatValue();
  }
  
  public double invokeDouble(Object[] args) {
    return ((Double)invoke(Double.class, args)).doubleValue();
  }
  
  public void invokeVoid(Object[] args) {
    invoke(Void.class, args);
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (o == null)
      return false; 
    if (o.getClass() == getClass()) {
      Function other = (Function)o;
      return (other.callFlags == this.callFlags && other.options.equals(this.options) && other.peer == this.peer);
    } 
    return false;
  }
  
  public int hashCode() {
    return this.callFlags + this.options.hashCode() + super.hashCode();
  }
  
  static Object[] concatenateVarArgs(Object[] inArgs) {
    if (inArgs != null && inArgs.length > 0) {
      Object lastArg = inArgs[inArgs.length - 1];
      Class argType = (lastArg != null) ? lastArg.getClass() : null;
      if (argType != null && argType.isArray()) {
        Object[] varArgs = (Object[])lastArg;
        Object[] fullArgs = new Object[inArgs.length + varArgs.length];
        System.arraycopy(inArgs, 0, fullArgs, 0, inArgs.length - 1);
        System.arraycopy(varArgs, 0, fullArgs, inArgs.length - 1, varArgs.length);
        fullArgs[fullArgs.length - 1] = null;
        inArgs = fullArgs;
      } 
    } 
    return inArgs;
  }
  
  static boolean isVarArgs(Method m) {
    try {
      Method v = m.getClass().getMethod("isVarArgs", new Class[0]);
      return Boolean.TRUE.equals(v.invoke(m, new Object[0]));
    } catch (SecurityException e) {
    
    } catch (NoSuchMethodException e) {
    
    } catch (IllegalArgumentException e) {
    
    } catch (IllegalAccessException e) {
    
    } catch (InvocationTargetException e) {}
    return false;
  }
  
  public static interface PostCallRead {
    void read();
  }
  
  private static class NativeMappedArray extends Memory implements PostCallRead {
    private final NativeMapped[] original;
    
    public NativeMappedArray(NativeMapped[] arg) {
      super(Native.getNativeSize(arg.getClass(), arg));
      this.original = arg;
      setValue(0L, this.original, this.original.getClass());
    }
    
    public void read() {
      getValue(0L, this.original.getClass(), this.original);
    }
  }
  
  private static class PointerArray extends Memory implements PostCallRead {
    private final Pointer[] original;
    
    public PointerArray(Pointer[] arg) {
      super((Pointer.SIZE * (arg.length + 1)));
      this.original = arg;
      for (int i = 0; i < arg.length; i++)
        setPointer((i * Pointer.SIZE), arg[i]); 
      setPointer((Pointer.SIZE * arg.length), (Pointer)null);
    }
    
    public void read() {
      read(0L, this.original, 0, this.original.length);
    }
  }
  
  static Boolean valueOf(boolean b) {
    return b ? Boolean.TRUE : Boolean.FALSE;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\Function.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */