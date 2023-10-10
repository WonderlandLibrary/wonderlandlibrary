// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.bytecode.analysis;

import java.util.IdentityHashMap;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import com.viaversion.viaversion.libs.javassist.NotFoundException;
import com.viaversion.viaversion.libs.javassist.ClassPool;
import java.util.Map;
import com.viaversion.viaversion.libs.javassist.CtClass;

public class Type
{
    private final CtClass clazz;
    private final boolean special;
    private static final Map<CtClass, Type> prims;
    public static final Type DOUBLE;
    public static final Type BOOLEAN;
    public static final Type LONG;
    public static final Type CHAR;
    public static final Type BYTE;
    public static final Type SHORT;
    public static final Type INTEGER;
    public static final Type FLOAT;
    public static final Type VOID;
    public static final Type UNINIT;
    public static final Type RETURN_ADDRESS;
    public static final Type TOP;
    public static final Type BOGUS;
    public static final Type OBJECT;
    public static final Type SERIALIZABLE;
    public static final Type CLONEABLE;
    public static final Type THROWABLE;
    
    public static Type get(final CtClass clazz) {
        final Type type = Type.prims.get(clazz);
        return (type != null) ? type : new Type(clazz);
    }
    
    private static Type lookupType(final String name) {
        try {
            return new Type(ClassPool.getDefault().get(name));
        }
        catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    Type(final CtClass clazz) {
        this(clazz, false);
    }
    
    private Type(final CtClass clazz, final boolean special) {
        this.clazz = clazz;
        this.special = special;
    }
    
    boolean popChanged() {
        return false;
    }
    
    public int getSize() {
        return (this.clazz == CtClass.doubleType || this.clazz == CtClass.longType || this == Type.TOP) ? 2 : 1;
    }
    
    public CtClass getCtClass() {
        return this.clazz;
    }
    
    public boolean isReference() {
        return !this.special && (this.clazz == null || !this.clazz.isPrimitive());
    }
    
    public boolean isSpecial() {
        return this.special;
    }
    
    public boolean isArray() {
        return this.clazz != null && this.clazz.isArray();
    }
    
    public int getDimensions() {
        if (!this.isArray()) {
            return 0;
        }
        String name;
        int pos;
        int count;
        for (name = this.clazz.getName(), pos = name.length() - 1, count = 0; name.charAt(pos) == ']'; pos -= 2, ++count) {}
        return count;
    }
    
    public Type getComponent() {
        if (this.clazz == null || !this.clazz.isArray()) {
            return null;
        }
        CtClass component;
        try {
            component = this.clazz.getComponentType();
        }
        catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        final Type type = Type.prims.get(component);
        return (type != null) ? type : new Type(component);
    }
    
    public boolean isAssignableFrom(final Type type) {
        if (this == type) {
            return true;
        }
        if ((type == Type.UNINIT && this.isReference()) || (this == Type.UNINIT && type.isReference())) {
            return true;
        }
        if (type instanceof MultiType) {
            return ((MultiType)type).isAssignableTo(this);
        }
        if (type instanceof MultiArrayType) {
            return ((MultiArrayType)type).isAssignableTo(this);
        }
        if (this.clazz == null || this.clazz.isPrimitive()) {
            return false;
        }
        try {
            return type.clazz.subtypeOf(this.clazz);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public Type merge(final Type type) {
        if (type == this) {
            return this;
        }
        if (type == null) {
            return this;
        }
        if (type == Type.UNINIT) {
            return this;
        }
        if (this == Type.UNINIT) {
            return type;
        }
        if (!type.isReference() || !this.isReference()) {
            return Type.BOGUS;
        }
        if (type instanceof MultiType) {
            return type.merge(this);
        }
        if (type.isArray() && this.isArray()) {
            return this.mergeArray(type);
        }
        try {
            return this.mergeClasses(type);
        }
        catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    Type getRootComponent(Type type) {
        while (type.isArray()) {
            type = type.getComponent();
        }
        return type;
    }
    
    private Type createArray(final Type rootComponent, final int dims) {
        if (rootComponent instanceof MultiType) {
            return new MultiArrayType((MultiType)rootComponent, dims);
        }
        final String name = this.arrayName(rootComponent.clazz.getName(), dims);
        Type type;
        try {
            type = get(this.getClassPool(rootComponent).get(name));
        }
        catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        return type;
    }
    
    String arrayName(String component, final int dims) {
        int i = component.length();
        final int size = i + dims * 2;
        final char[] string = new char[size];
        component.getChars(0, i, string, 0);
        while (i < size) {
            string[i++] = '[';
            string[i++] = ']';
        }
        component = new String(string);
        return component;
    }
    
    private ClassPool getClassPool(final Type rootComponent) {
        final ClassPool pool = rootComponent.clazz.getClassPool();
        return (pool != null) ? pool : ClassPool.getDefault();
    }
    
    private Type mergeArray(final Type type) {
        final Type typeRoot = this.getRootComponent(type);
        final Type thisRoot = this.getRootComponent(this);
        final int typeDims = type.getDimensions();
        final int thisDims = this.getDimensions();
        if (typeDims == thisDims) {
            final Type mergedComponent = thisRoot.merge(typeRoot);
            if (mergedComponent == Type.BOGUS) {
                return Type.OBJECT;
            }
            return this.createArray(mergedComponent, thisDims);
        }
        else {
            Type targetRoot;
            int targetDims;
            if (typeDims < thisDims) {
                targetRoot = typeRoot;
                targetDims = typeDims;
            }
            else {
                targetRoot = thisRoot;
                targetDims = thisDims;
            }
            if (eq(Type.CLONEABLE.clazz, targetRoot.clazz) || eq(Type.SERIALIZABLE.clazz, targetRoot.clazz)) {
                return this.createArray(targetRoot, targetDims);
            }
            return this.createArray(Type.OBJECT, targetDims);
        }
    }
    
    private static CtClass findCommonSuperClass(final CtClass one, final CtClass two) throws NotFoundException {
        CtClass deep = one;
        CtClass backupShallow;
        CtClass shallow = backupShallow = two;
        CtClass backupDeep = deep;
        while (!eq(deep, shallow) || deep.getSuperclass() == null) {
            final CtClass deepSuper = deep.getSuperclass();
            final CtClass shallowSuper = shallow.getSuperclass();
            if (shallowSuper == null) {
                shallow = backupShallow;
            }
            else {
                if (deepSuper != null) {
                    deep = deepSuper;
                    shallow = shallowSuper;
                    continue;
                }
                deep = backupDeep;
                backupDeep = backupShallow;
                backupShallow = deep;
                deep = shallow;
                shallow = backupShallow;
            }
            while (true) {
                deep = deep.getSuperclass();
                if (deep == null) {
                    break;
                }
                backupDeep = backupDeep.getSuperclass();
            }
            for (deep = backupDeep; !eq(deep, shallow); deep = deep.getSuperclass(), shallow = shallow.getSuperclass()) {}
            return deep;
        }
        return deep;
    }
    
    private Type mergeClasses(final Type type) throws NotFoundException {
        final CtClass superClass = findCommonSuperClass(this.clazz, type.clazz);
        if (superClass.getSuperclass() == null) {
            final Map<String, CtClass> interfaces = this.findCommonInterfaces(type);
            if (interfaces.size() == 1) {
                return new Type(interfaces.values().iterator().next());
            }
            if (interfaces.size() > 1) {
                return new MultiType(interfaces);
            }
            return new Type(superClass);
        }
        else {
            final Map<String, CtClass> commonDeclared = this.findExclusiveDeclaredInterfaces(type, superClass);
            if (commonDeclared.size() > 0) {
                return new MultiType(commonDeclared, new Type(superClass));
            }
            return new Type(superClass);
        }
    }
    
    private Map<String, CtClass> findCommonInterfaces(final Type type) {
        final Map<String, CtClass> typeMap = this.getAllInterfaces(type.clazz, null);
        final Map<String, CtClass> thisMap = this.getAllInterfaces(this.clazz, null);
        return this.findCommonInterfaces(typeMap, thisMap);
    }
    
    private Map<String, CtClass> findExclusiveDeclaredInterfaces(final Type type, final CtClass exclude) {
        final Map<String, CtClass> typeMap = this.getDeclaredInterfaces(type.clazz, null);
        final Map<String, CtClass> thisMap = this.getDeclaredInterfaces(this.clazz, null);
        final Map<String, CtClass> excludeMap = this.getAllInterfaces(exclude, null);
        for (final String intf : excludeMap.keySet()) {
            typeMap.remove(intf);
            thisMap.remove(intf);
        }
        return this.findCommonInterfaces(typeMap, thisMap);
    }
    
    Map<String, CtClass> findCommonInterfaces(final Map<String, CtClass> typeMap, Map<String, CtClass> alterMap) {
        if (alterMap == null) {
            alterMap = new HashMap<String, CtClass>();
        }
        if (typeMap == null || typeMap.isEmpty()) {
            alterMap.clear();
        }
        final Iterator<String> it = alterMap.keySet().iterator();
        while (it.hasNext()) {
            final String name = it.next();
            if (!typeMap.containsKey(name)) {
                it.remove();
            }
        }
        final Collection<CtClass> interfaces = new ArrayList<CtClass>();
        for (final CtClass intf : alterMap.values()) {
            try {
                interfaces.addAll(Arrays.asList(intf.getInterfaces()));
            }
            catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        for (final CtClass c : interfaces) {
            alterMap.remove(c.getName());
        }
        return alterMap;
    }
    
    Map<String, CtClass> getAllInterfaces(CtClass clazz, Map<String, CtClass> map) {
        if (map == null) {
            map = new HashMap<String, CtClass>();
        }
        if (clazz.isInterface()) {
            map.put(clazz.getName(), clazz);
        }
        do {
            try {
                final CtClass[] interfaces2;
                final CtClass[] interfaces = interfaces2 = clazz.getInterfaces();
                for (final CtClass intf : interfaces2) {
                    map.put(intf.getName(), intf);
                    this.getAllInterfaces(intf, map);
                }
                clazz = clazz.getSuperclass();
            }
            catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        } while (clazz != null);
        return map;
    }
    
    Map<String, CtClass> getDeclaredInterfaces(final CtClass clazz, Map<String, CtClass> map) {
        if (map == null) {
            map = new HashMap<String, CtClass>();
        }
        if (clazz.isInterface()) {
            map.put(clazz.getName(), clazz);
        }
        CtClass[] interfaces;
        try {
            interfaces = clazz.getInterfaces();
        }
        catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        for (final CtClass intf : interfaces) {
            map.put(intf.getName(), intf);
            this.getDeclaredInterfaces(intf, map);
        }
        return map;
    }
    
    @Override
    public int hashCode() {
        return this.getClass().hashCode() + this.clazz.hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Type && o.getClass() == this.getClass() && eq(this.clazz, ((Type)o).clazz);
    }
    
    static boolean eq(final CtClass one, final CtClass two) {
        return one == two || (one != null && two != null && one.getName().equals(two.getName()));
    }
    
    @Override
    public String toString() {
        if (this == Type.BOGUS) {
            return "BOGUS";
        }
        if (this == Type.UNINIT) {
            return "UNINIT";
        }
        if (this == Type.RETURN_ADDRESS) {
            return "RETURN ADDRESS";
        }
        if (this == Type.TOP) {
            return "TOP";
        }
        return (this.clazz == null) ? "null" : this.clazz.getName();
    }
    
    static {
        prims = new IdentityHashMap<CtClass, Type>();
        DOUBLE = new Type(CtClass.doubleType);
        BOOLEAN = new Type(CtClass.booleanType);
        LONG = new Type(CtClass.longType);
        CHAR = new Type(CtClass.charType);
        BYTE = new Type(CtClass.byteType);
        SHORT = new Type(CtClass.shortType);
        INTEGER = new Type(CtClass.intType);
        FLOAT = new Type(CtClass.floatType);
        VOID = new Type(CtClass.voidType);
        UNINIT = new Type(null);
        RETURN_ADDRESS = new Type(null, true);
        TOP = new Type(null, true);
        BOGUS = new Type(null, true);
        OBJECT = lookupType("java.lang.Object");
        SERIALIZABLE = lookupType("java.io.Serializable");
        CLONEABLE = lookupType("java.lang.Cloneable");
        THROWABLE = lookupType("java.lang.Throwable");
        Type.prims.put(CtClass.doubleType, Type.DOUBLE);
        Type.prims.put(CtClass.longType, Type.LONG);
        Type.prims.put(CtClass.charType, Type.CHAR);
        Type.prims.put(CtClass.shortType, Type.SHORT);
        Type.prims.put(CtClass.intType, Type.INTEGER);
        Type.prims.put(CtClass.floatType, Type.FLOAT);
        Type.prims.put(CtClass.byteType, Type.BYTE);
        Type.prims.put(CtClass.booleanType, Type.BOOLEAN);
        Type.prims.put(CtClass.voidType, Type.VOID);
    }
}
