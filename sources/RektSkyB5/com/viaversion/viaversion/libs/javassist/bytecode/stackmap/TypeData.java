/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.bytecode.stackmap;

import com.viaversion.viaversion.libs.javassist.ClassPool;
import com.viaversion.viaversion.libs.javassist.CtClass;
import com.viaversion.viaversion.libs.javassist.NotFoundException;
import com.viaversion.viaversion.libs.javassist.bytecode.BadBytecode;
import com.viaversion.viaversion.libs.javassist.bytecode.ConstPool;
import com.viaversion.viaversion.libs.javassist.bytecode.Descriptor;
import com.viaversion.viaversion.libs.javassist.bytecode.stackmap.TypeTag;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class TypeData {
    public static TypeData[] make(int size) {
        TypeData[] array = new TypeData[size];
        for (int i2 = 0; i2 < size; ++i2) {
            array[i2] = TypeTag.TOP;
        }
        return array;
    }

    protected TypeData() {
    }

    private static void setType(TypeData td, String className, ClassPool cp) throws BadBytecode {
        td.setType(className, cp);
    }

    public abstract int getTypeTag();

    public abstract int getTypeData(ConstPool var1);

    public TypeData join() {
        return new TypeVar(this);
    }

    public abstract BasicType isBasicType();

    public abstract boolean is2WordType();

    public boolean isNullType() {
        return false;
    }

    public boolean isUninit() {
        return false;
    }

    public abstract boolean eq(TypeData var1);

    public abstract String getName();

    public abstract void setType(String var1, ClassPool var2) throws BadBytecode;

    public abstract TypeData getArrayType(int var1) throws NotFoundException;

    public int dfs(List<TypeData> order, int index, ClassPool cp) throws NotFoundException {
        return index;
    }

    protected TypeVar toTypeVar(int dim) {
        return null;
    }

    public void constructorCalled(int offset) {
    }

    public String toString() {
        return super.toString() + "(" + this.toString2(new HashSet<TypeData>()) + ")";
    }

    abstract String toString2(Set<TypeData> var1);

    public static CtClass commonSuperClassEx(CtClass one, CtClass two) throws NotFoundException {
        if (one == two) {
            return one;
        }
        if (one.isArray() && two.isArray()) {
            CtClass ele2;
            CtClass ele1 = one.getComponentType();
            CtClass element = TypeData.commonSuperClassEx(ele1, ele2 = two.getComponentType());
            if (element == ele1) {
                return one;
            }
            if (element == ele2) {
                return two;
            }
            return one.getClassPool().get(element == null ? "java.lang.Object" : element.getName() + "[]");
        }
        if (one.isPrimitive() || two.isPrimitive()) {
            return null;
        }
        if (one.isArray() || two.isArray()) {
            return one.getClassPool().get("java.lang.Object");
        }
        return TypeData.commonSuperClass(one, two);
    }

    public static CtClass commonSuperClass(CtClass one, CtClass two) throws NotFoundException {
        CtClass shallow;
        CtClass deep = one;
        CtClass backupShallow = shallow = two;
        CtClass backupDeep = deep;
        while (true) {
            if (TypeData.eq(deep, shallow) && deep.getSuperclass() != null) {
                return deep;
            }
            CtClass deepSuper = deep.getSuperclass();
            CtClass shallowSuper = shallow.getSuperclass();
            if (shallowSuper == null) {
                shallow = backupShallow;
                break;
            }
            if (deepSuper == null) {
                deep = backupDeep;
                backupDeep = backupShallow;
                backupShallow = deep;
                deep = shallow;
                shallow = backupShallow;
                break;
            }
            deep = deepSuper;
            shallow = shallowSuper;
        }
        while ((deep = deep.getSuperclass()) != null) {
            backupDeep = backupDeep.getSuperclass();
        }
        deep = backupDeep;
        while (!TypeData.eq(deep, shallow)) {
            deep = deep.getSuperclass();
            shallow = shallow.getSuperclass();
        }
        return deep;
    }

    static boolean eq(CtClass one, CtClass two) {
        return one == two || one != null && two != null && one.getName().equals(two.getName());
    }

    public static void aastore(TypeData array, TypeData value, ClassPool cp) throws BadBytecode {
        if (array instanceof AbsTypeVar && !value.isNullType()) {
            ((AbsTypeVar)array).merge(ArrayType.make(value));
        }
        if (value instanceof AbsTypeVar) {
            if (array instanceof AbsTypeVar) {
                ArrayElement.make(array);
            } else if (array instanceof ClassName) {
                if (!array.isNullType()) {
                    String type = ArrayElement.typeName(array.getName());
                    value.setType(type, cp);
                }
            } else {
                throw new BadBytecode("bad AASTORE: " + array);
            }
        }
    }

    public static class UninitThis
    extends UninitData {
        UninitThis(String className) {
            super(-1, className);
        }

        @Override
        public UninitData copy() {
            return new UninitThis(this.getName());
        }

        @Override
        public int getTypeTag() {
            return 6;
        }

        @Override
        public int getTypeData(ConstPool cp) {
            return 0;
        }

        @Override
        String toString2(Set<TypeData> set) {
            return "uninit:this";
        }
    }

    public static class UninitData
    extends ClassName {
        int offset;
        boolean initialized;

        UninitData(int offset, String className) {
            super(className);
            this.offset = offset;
            this.initialized = false;
        }

        public UninitData copy() {
            return new UninitData(this.offset, this.getName());
        }

        @Override
        public int getTypeTag() {
            return 8;
        }

        @Override
        public int getTypeData(ConstPool cp) {
            return this.offset;
        }

        @Override
        public TypeData join() {
            if (this.initialized) {
                return new TypeVar(new ClassName(this.getName()));
            }
            return new UninitTypeVar(this.copy());
        }

        @Override
        public boolean isUninit() {
            return true;
        }

        @Override
        public boolean eq(TypeData d2) {
            if (d2 instanceof UninitData) {
                UninitData ud = (UninitData)d2;
                return this.offset == ud.offset && this.getName().equals(ud.getName());
            }
            return false;
        }

        public int offset() {
            return this.offset;
        }

        @Override
        public void constructorCalled(int offset) {
            if (offset == this.offset) {
                this.initialized = true;
            }
        }

        @Override
        String toString2(Set<TypeData> set) {
            return this.getName() + "," + this.offset;
        }
    }

    public static class NullType
    extends ClassName {
        public NullType() {
            super("null-type");
        }

        @Override
        public int getTypeTag() {
            return 5;
        }

        @Override
        public boolean isNullType() {
            return true;
        }

        @Override
        public int getTypeData(ConstPool cp) {
            return 0;
        }

        @Override
        public TypeData getArrayType(int dim) {
            return this;
        }
    }

    public static class ClassName
    extends TypeData {
        private String name;

        public ClassName(String n2) {
            this.name = n2;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public BasicType isBasicType() {
            return null;
        }

        @Override
        public boolean is2WordType() {
            return false;
        }

        @Override
        public int getTypeTag() {
            return 7;
        }

        @Override
        public int getTypeData(ConstPool cp) {
            return cp.addClassInfo(this.getName());
        }

        @Override
        public boolean eq(TypeData d2) {
            if (d2.isUninit()) {
                return d2.eq(this);
            }
            return this.name.equals(d2.getName());
        }

        @Override
        public void setType(String typeName, ClassPool cp) throws BadBytecode {
        }

        @Override
        public TypeData getArrayType(int dim) throws NotFoundException {
            if (dim == 0) {
                return this;
            }
            if (dim > 0) {
                char[] dimType = new char[dim];
                for (int i2 = 0; i2 < dim; ++i2) {
                    dimType[i2] = 91;
                }
                String elementType = this.getName();
                if (elementType.charAt(0) != '[') {
                    elementType = "L" + elementType.replace('.', '/') + ";";
                }
                return new ClassName(new String(dimType) + elementType);
            }
            for (int i3 = 0; i3 < -dim; ++i3) {
                if (this.name.charAt(i3) == '[') continue;
                throw new NotFoundException("no " + dim + " dimensional array type: " + this.getName());
            }
            char type = this.name.charAt(-dim);
            if (type == '[') {
                return new ClassName(this.name.substring(-dim));
            }
            if (type == 'L') {
                return new ClassName(this.name.substring(-dim + 1, this.name.length() - 1).replace('/', '.'));
            }
            if (type == TypeTag.DOUBLE.decodedName) {
                return TypeTag.DOUBLE;
            }
            if (type == TypeTag.FLOAT.decodedName) {
                return TypeTag.FLOAT;
            }
            if (type == TypeTag.LONG.decodedName) {
                return TypeTag.LONG;
            }
            return TypeTag.INTEGER;
        }

        @Override
        String toString2(Set<TypeData> set) {
            return this.name;
        }
    }

    public static class UninitTypeVar
    extends AbsTypeVar {
        protected TypeData type;

        public UninitTypeVar(UninitData t2) {
            this.type = t2;
        }

        @Override
        public int getTypeTag() {
            return this.type.getTypeTag();
        }

        @Override
        public int getTypeData(ConstPool cp) {
            return this.type.getTypeData(cp);
        }

        @Override
        public BasicType isBasicType() {
            return this.type.isBasicType();
        }

        @Override
        public boolean is2WordType() {
            return this.type.is2WordType();
        }

        @Override
        public boolean isUninit() {
            return this.type.isUninit();
        }

        @Override
        public boolean eq(TypeData d2) {
            return this.type.eq(d2);
        }

        @Override
        public String getName() {
            return this.type.getName();
        }

        @Override
        protected TypeVar toTypeVar(int dim) {
            return null;
        }

        @Override
        public TypeData join() {
            return this.type.join();
        }

        @Override
        public void setType(String s2, ClassPool cp) throws BadBytecode {
            this.type.setType(s2, cp);
        }

        @Override
        public void merge(TypeData t2) {
            if (!t2.eq(this.type)) {
                this.type = TypeTag.TOP;
            }
        }

        @Override
        public void constructorCalled(int offset) {
            this.type.constructorCalled(offset);
        }

        public int offset() {
            if (this.type instanceof UninitData) {
                return ((UninitData)this.type).offset;
            }
            throw new RuntimeException("not available");
        }

        @Override
        public TypeData getArrayType(int dim) throws NotFoundException {
            return this.type.getArrayType(dim);
        }

        @Override
        String toString2(Set<TypeData> set) {
            return "";
        }
    }

    public static class ArrayElement
    extends AbsTypeVar {
        private AbsTypeVar array;

        private ArrayElement(AbsTypeVar a2) {
            this.array = a2;
        }

        public static TypeData make(TypeData array) throws BadBytecode {
            if (array instanceof ArrayType) {
                return ((ArrayType)array).elementType();
            }
            if (array instanceof AbsTypeVar) {
                return new ArrayElement((AbsTypeVar)array);
            }
            if (array instanceof ClassName && !array.isNullType()) {
                return new ClassName(ArrayElement.typeName(array.getName()));
            }
            throw new BadBytecode("bad AASTORE: " + array);
        }

        @Override
        public void merge(TypeData t2) {
            try {
                if (!t2.isNullType()) {
                    this.array.merge(ArrayType.make(t2));
                }
            }
            catch (BadBytecode e2) {
                throw new RuntimeException("fatal: " + e2);
            }
        }

        @Override
        public String getName() {
            return ArrayElement.typeName(this.array.getName());
        }

        public AbsTypeVar arrayType() {
            return this.array;
        }

        @Override
        public BasicType isBasicType() {
            return null;
        }

        @Override
        public boolean is2WordType() {
            return false;
        }

        private static String typeName(String arrayType) {
            if (arrayType.length() > 1 && arrayType.charAt(0) == '[') {
                char c2 = arrayType.charAt(1);
                if (c2 == 'L') {
                    return arrayType.substring(2, arrayType.length() - 1).replace('/', '.');
                }
                if (c2 == '[') {
                    return arrayType.substring(1);
                }
            }
            return "java.lang.Object";
        }

        @Override
        public void setType(String s2, ClassPool cp) throws BadBytecode {
            this.array.setType(ArrayType.typeName(s2), cp);
        }

        @Override
        protected TypeVar toTypeVar(int dim) {
            return this.array.toTypeVar(dim - 1);
        }

        @Override
        public TypeData getArrayType(int dim) throws NotFoundException {
            return this.array.getArrayType(dim - 1);
        }

        @Override
        public int dfs(List<TypeData> order, int index, ClassPool cp) throws NotFoundException {
            return this.array.dfs(order, index, cp);
        }

        @Override
        String toString2(Set<TypeData> set) {
            return "*" + this.array.toString2(set);
        }
    }

    public static class ArrayType
    extends AbsTypeVar {
        private AbsTypeVar element;

        private ArrayType(AbsTypeVar elementType) {
            this.element = elementType;
        }

        static TypeData make(TypeData element) throws BadBytecode {
            if (element instanceof ArrayElement) {
                return ((ArrayElement)element).arrayType();
            }
            if (element instanceof AbsTypeVar) {
                return new ArrayType((AbsTypeVar)element);
            }
            if (element instanceof ClassName && !element.isNullType()) {
                return new ClassName(ArrayType.typeName(element.getName()));
            }
            throw new BadBytecode("bad AASTORE: " + element);
        }

        @Override
        public void merge(TypeData t2) {
            try {
                if (!t2.isNullType()) {
                    this.element.merge(ArrayElement.make(t2));
                }
            }
            catch (BadBytecode e2) {
                throw new RuntimeException("fatal: " + e2);
            }
        }

        @Override
        public String getName() {
            return ArrayType.typeName(this.element.getName());
        }

        public AbsTypeVar elementType() {
            return this.element;
        }

        @Override
        public BasicType isBasicType() {
            return null;
        }

        @Override
        public boolean is2WordType() {
            return false;
        }

        public static String typeName(String elementType) {
            if (elementType.charAt(0) == '[') {
                return "[" + elementType;
            }
            return "[L" + elementType.replace('.', '/') + ";";
        }

        @Override
        public void setType(String s2, ClassPool cp) throws BadBytecode {
            this.element.setType(ArrayElement.typeName(s2), cp);
        }

        @Override
        protected TypeVar toTypeVar(int dim) {
            return this.element.toTypeVar(dim + 1);
        }

        @Override
        public TypeData getArrayType(int dim) throws NotFoundException {
            return this.element.getArrayType(dim + 1);
        }

        @Override
        public int dfs(List<TypeData> order, int index, ClassPool cp) throws NotFoundException {
            return this.element.dfs(order, index, cp);
        }

        @Override
        String toString2(Set<TypeData> set) {
            return "[" + this.element.toString2(set);
        }
    }

    public static class TypeVar
    extends AbsTypeVar {
        protected List<TypeData> lowers = new ArrayList<TypeData>(2);
        protected List<TypeData> usedBy = new ArrayList<TypeData>(2);
        protected List<String> uppers = null;
        protected String fixedType;
        private boolean is2WordType;
        private int visited = 0;
        private int smallest = 0;
        private boolean inList = false;
        private int dimension = 0;

        public TypeVar(TypeData t2) {
            this.merge(t2);
            this.fixedType = null;
            this.is2WordType = t2.is2WordType();
        }

        @Override
        public String getName() {
            if (this.fixedType == null) {
                return this.lowers.get(0).getName();
            }
            return this.fixedType;
        }

        @Override
        public BasicType isBasicType() {
            if (this.fixedType == null) {
                return this.lowers.get(0).isBasicType();
            }
            return null;
        }

        @Override
        public boolean is2WordType() {
            if (this.fixedType == null) {
                return this.is2WordType;
            }
            return false;
        }

        @Override
        public boolean isNullType() {
            if (this.fixedType == null) {
                return this.lowers.get(0).isNullType();
            }
            return false;
        }

        @Override
        public boolean isUninit() {
            if (this.fixedType == null) {
                return this.lowers.get(0).isUninit();
            }
            return false;
        }

        @Override
        public void merge(TypeData t2) {
            this.lowers.add(t2);
            if (t2 instanceof TypeVar) {
                ((TypeVar)t2).usedBy.add(this);
            }
        }

        @Override
        public int getTypeTag() {
            if (this.fixedType == null) {
                return this.lowers.get(0).getTypeTag();
            }
            return super.getTypeTag();
        }

        @Override
        public int getTypeData(ConstPool cp) {
            if (this.fixedType == null) {
                return this.lowers.get(0).getTypeData(cp);
            }
            return super.getTypeData(cp);
        }

        @Override
        public void setType(String typeName, ClassPool cp) throws BadBytecode {
            if (this.uppers == null) {
                this.uppers = new ArrayList<String>();
            }
            this.uppers.add(typeName);
        }

        @Override
        protected TypeVar toTypeVar(int dim) {
            this.dimension = dim;
            return this;
        }

        @Override
        public TypeData getArrayType(int dim) throws NotFoundException {
            if (dim == 0) {
                return this;
            }
            BasicType bt = this.isBasicType();
            if (bt == null) {
                if (this.isNullType()) {
                    return new NullType();
                }
                return new ClassName(this.getName()).getArrayType(dim);
            }
            return bt.getArrayType(dim);
        }

        @Override
        public int dfs(List<TypeData> preOrder, int index, ClassPool cp) throws NotFoundException {
            if (this.visited > 0) {
                return index;
            }
            this.visited = this.smallest = ++index;
            preOrder.add(this);
            this.inList = true;
            int n2 = this.lowers.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                TypeVar child = this.lowers.get(i2).toTypeVar(this.dimension);
                if (child == null) continue;
                if (child.visited == 0) {
                    index = child.dfs(preOrder, index, cp);
                    if (child.smallest >= this.smallest) continue;
                    this.smallest = child.smallest;
                    continue;
                }
                if (!child.inList || child.visited >= this.smallest) continue;
                this.smallest = child.visited;
            }
            if (this.visited == this.smallest) {
                TypeVar cv;
                ArrayList<TypeData> scc = new ArrayList<TypeData>();
                do {
                    cv = (TypeVar)preOrder.remove(preOrder.size() - 1);
                    cv.inList = false;
                    scc.add(cv);
                } while (cv != this);
                this.fixTypes(scc, cp);
            }
            return index;
        }

        private void fixTypes(List<TypeData> scc, ClassPool cp) throws NotFoundException {
            HashSet<String> lowersSet = new HashSet<String>();
            boolean isBasicType = false;
            TypeData kind = null;
            int size = scc.size();
            block0: for (int i2 = 0; i2 < size; ++i2) {
                TypeVar tvar = (TypeVar)scc.get(i2);
                List<TypeData> tds = tvar.lowers;
                int size2 = tds.size();
                for (int j2 = 0; j2 < size2; ++j2) {
                    TypeData td = tds.get(j2);
                    TypeData d2 = td.getArrayType(tvar.dimension);
                    BasicType bt = d2.isBasicType();
                    if (kind == null) {
                        if (bt == null) {
                            isBasicType = false;
                            kind = d2;
                            if (d2.isUninit()) {
                                continue block0;
                            }
                        } else {
                            isBasicType = true;
                            kind = bt;
                        }
                    } else if (bt == null && isBasicType || bt != null && kind != bt) {
                        isBasicType = true;
                        kind = TypeTag.TOP;
                        continue block0;
                    }
                    if (bt != null || d2.isNullType()) continue;
                    lowersSet.add(d2.getName());
                }
            }
            if (isBasicType) {
                this.is2WordType = kind.is2WordType();
                this.fixTypes1(scc, kind);
            } else {
                String typeName = this.fixTypes2(scc, lowersSet, cp);
                this.fixTypes1(scc, new ClassName(typeName));
            }
        }

        private void fixTypes1(List<TypeData> scc, TypeData kind) throws NotFoundException {
            int size = scc.size();
            for (int i2 = 0; i2 < size; ++i2) {
                TypeVar cv = (TypeVar)scc.get(i2);
                TypeData kind2 = kind.getArrayType(-cv.dimension);
                if (kind2.isBasicType() == null) {
                    cv.fixedType = kind2.getName();
                    continue;
                }
                cv.lowers.clear();
                cv.lowers.add(kind2);
                cv.is2WordType = kind2.is2WordType();
            }
        }

        private String fixTypes2(List<TypeData> scc, Set<String> lowersSet, ClassPool cp) throws NotFoundException {
            Iterator<String> it = lowersSet.iterator();
            if (lowersSet.size() == 0) {
                return null;
            }
            if (lowersSet.size() == 1) {
                return it.next();
            }
            CtClass cc = cp.get(it.next());
            while (it.hasNext()) {
                cc = TypeVar.commonSuperClassEx(cc, cp.get(it.next()));
            }
            if (cc.getSuperclass() == null || TypeVar.isObjectArray(cc)) {
                cc = this.fixByUppers(scc, cp, new HashSet<TypeData>(), cc);
            }
            if (cc.isArray()) {
                return Descriptor.toJvmName(cc);
            }
            return cc.getName();
        }

        private static boolean isObjectArray(CtClass cc) throws NotFoundException {
            return cc.isArray() && cc.getComponentType().getSuperclass() == null;
        }

        private CtClass fixByUppers(List<TypeData> users, ClassPool cp, Set<TypeData> visited, CtClass type) throws NotFoundException {
            if (users == null) {
                return type;
            }
            int size = users.size();
            for (int i2 = 0; i2 < size; ++i2) {
                TypeVar t2 = (TypeVar)users.get(i2);
                if (!visited.add(t2)) {
                    return type;
                }
                if (t2.uppers != null) {
                    int s2 = t2.uppers.size();
                    for (int k2 = 0; k2 < s2; ++k2) {
                        CtClass cc = cp.get(t2.uppers.get(k2));
                        if (!cc.subtypeOf(type)) continue;
                        type = cc;
                    }
                }
                type = this.fixByUppers(t2.usedBy, cp, visited, type);
            }
            return type;
        }

        @Override
        String toString2(Set<TypeData> hash) {
            TypeData e2;
            hash.add(this);
            if (this.lowers.size() > 0 && (e2 = this.lowers.get(0)) != null && !hash.contains(e2)) {
                return e2.toString2(hash);
            }
            return "?";
        }
    }

    public static abstract class AbsTypeVar
    extends TypeData {
        public abstract void merge(TypeData var1);

        @Override
        public int getTypeTag() {
            return 7;
        }

        @Override
        public int getTypeData(ConstPool cp) {
            return cp.addClassInfo(this.getName());
        }

        @Override
        public boolean eq(TypeData d2) {
            if (d2.isUninit()) {
                return d2.eq(this);
            }
            return this.getName().equals(d2.getName());
        }
    }

    protected static class BasicType
    extends TypeData {
        private String name;
        private int typeTag;
        private char decodedName;

        public BasicType(String type, int tag, char decoded) {
            this.name = type;
            this.typeTag = tag;
            this.decodedName = decoded;
        }

        @Override
        public int getTypeTag() {
            return this.typeTag;
        }

        @Override
        public int getTypeData(ConstPool cp) {
            return 0;
        }

        @Override
        public TypeData join() {
            if (this == TypeTag.TOP) {
                return this;
            }
            return super.join();
        }

        @Override
        public BasicType isBasicType() {
            return this;
        }

        @Override
        public boolean is2WordType() {
            return this.typeTag == 4 || this.typeTag == 3;
        }

        @Override
        public boolean eq(TypeData d2) {
            return this == d2;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public char getDecodedName() {
            return this.decodedName;
        }

        @Override
        public void setType(String s2, ClassPool cp) throws BadBytecode {
            throw new BadBytecode("conflict: " + this.name + " and " + s2);
        }

        @Override
        public TypeData getArrayType(int dim) throws NotFoundException {
            if (this == TypeTag.TOP) {
                return this;
            }
            if (dim < 0) {
                throw new NotFoundException("no element type: " + this.name);
            }
            if (dim == 0) {
                return this;
            }
            char[] name = new char[dim + 1];
            for (int i2 = 0; i2 < dim; ++i2) {
                name[i2] = 91;
            }
            name[dim] = this.decodedName;
            return new ClassName(new String(name));
        }

        @Override
        String toString2(Set<TypeData> set) {
            return this.name;
        }
    }
}

