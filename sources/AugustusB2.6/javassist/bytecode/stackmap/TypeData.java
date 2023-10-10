// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode.stackmap;

import javassist.NotFoundException;
import javassist.CtClass;
import java.util.ArrayList;
import javassist.bytecode.ConstPool;
import javassist.bytecode.BadBytecode;
import javassist.ClassPool;

public abstract class TypeData
{
    protected TypeData() {
    }
    
    public abstract void merge(final TypeData p0);
    
    static void setType(final TypeData td, final String className, final ClassPool cp) throws BadBytecode {
        if (td == TypeTag.TOP) {
            throw new BadBytecode("unset variable");
        }
        td.setType(className, cp);
    }
    
    public abstract boolean equals(final Object p0);
    
    public abstract int getTypeTag();
    
    public abstract int getTypeData(final ConstPool p0);
    
    public TypeData getSelf() {
        return this;
    }
    
    public abstract TypeData copy();
    
    public abstract boolean isObjectType();
    
    public boolean is2WordType() {
        return false;
    }
    
    public boolean isNullType() {
        return false;
    }
    
    public abstract String getName() throws BadBytecode;
    
    protected abstract void setType(final String p0, final ClassPool p1) throws BadBytecode;
    
    public abstract void evalExpectedType(final ClassPool p0) throws BadBytecode;
    
    public abstract String getExpected() throws BadBytecode;
    
    protected static class BasicType extends TypeData
    {
        private String name;
        private int typeTag;
        
        public BasicType(final String type, final int tag) {
            this.name = type;
            this.typeTag = tag;
        }
        
        public void merge(final TypeData neighbor) {
        }
        
        public boolean equals(final Object obj) {
            return this == obj;
        }
        
        public int getTypeTag() {
            return this.typeTag;
        }
        
        public int getTypeData(final ConstPool cp) {
            return 0;
        }
        
        public boolean isObjectType() {
            return false;
        }
        
        public boolean is2WordType() {
            return this.typeTag == 4 || this.typeTag == 3;
        }
        
        public TypeData copy() {
            return this;
        }
        
        public void evalExpectedType(final ClassPool cp) throws BadBytecode {
        }
        
        public String getExpected() throws BadBytecode {
            return this.name;
        }
        
        public String getName() {
            return this.name;
        }
        
        protected void setType(final String s, final ClassPool cp) throws BadBytecode {
            throw new BadBytecode("conflict: " + this.name + " and " + s);
        }
        
        public String toString() {
            return this.name;
        }
    }
    
    protected abstract static class TypeName extends TypeData
    {
        protected ArrayList equivalences;
        protected String expectedName;
        private CtClass cache;
        private boolean evalDone;
        
        protected TypeName() {
            (this.equivalences = new ArrayList()).add(this);
            this.expectedName = null;
            this.cache = null;
            this.evalDone = false;
        }
        
        public void merge(final TypeData neighbor) {
            if (this == neighbor) {
                return;
            }
            if (!(neighbor instanceof TypeName)) {
                return;
            }
            final TypeName neighbor2 = (TypeName)neighbor;
            final ArrayList list = this.equivalences;
            final ArrayList list2 = neighbor2.equivalences;
            if (list == list2) {
                return;
            }
            for (int n = list2.size(), i = 0; i < n; ++i) {
                final TypeName tn = list2.get(i);
                add(list, tn);
                tn.equivalences = list;
            }
        }
        
        private static void add(final ArrayList list, final TypeData td) {
            for (int n = list.size(), i = 0; i < n; ++i) {
                if (list.get(i) == td) {
                    return;
                }
            }
            list.add(td);
        }
        
        public int getTypeTag() {
            return 7;
        }
        
        public int getTypeData(final ConstPool cp) {
            String type;
            try {
                type = this.getExpected();
            }
            catch (BadBytecode e) {
                throw new RuntimeException("fatal error: ", e);
            }
            return this.getTypeData2(cp, type);
        }
        
        protected int getTypeData2(final ConstPool cp, final String type) {
            return cp.addClassInfo(type);
        }
        
        public boolean equals(final Object obj) {
            if (obj instanceof TypeName) {
                try {
                    final TypeName tn = (TypeName)obj;
                    return this.getExpected().equals(tn.getExpected());
                }
                catch (BadBytecode badBytecode) {}
            }
            return false;
        }
        
        public boolean isObjectType() {
            return true;
        }
        
        protected void setType(final String typeName, final ClassPool cp) throws BadBytecode {
            if (this.update(cp, this.expectedName, typeName)) {
                this.expectedName = typeName;
            }
        }
        
        public void evalExpectedType(final ClassPool cp) throws BadBytecode {
            if (this.evalDone) {
                return;
            }
            final ArrayList equiv = this.equivalences;
            final int n = equiv.size();
            String name = this.evalExpectedType2(equiv, n);
            if (name == null) {
                name = this.expectedName;
                for (int i = 0; i < n; ++i) {
                    final TypeData td = equiv.get(i);
                    if (td instanceof TypeName) {
                        final TypeName tn = (TypeName)td;
                        if (this.update(cp, name, tn.expectedName)) {
                            name = tn.expectedName;
                        }
                    }
                }
            }
            for (int i = 0; i < n; ++i) {
                final TypeData td = equiv.get(i);
                if (td instanceof TypeName) {
                    final TypeName tn = (TypeName)td;
                    tn.expectedName = name;
                    tn.cache = null;
                    tn.evalDone = true;
                }
            }
        }
        
        private String evalExpectedType2(final ArrayList equiv, final int n) throws BadBytecode {
            String origName = null;
            for (int i = 0; i < n; ++i) {
                final TypeData td = equiv.get(i);
                if (!td.isNullType()) {
                    if (origName == null) {
                        origName = td.getName();
                    }
                    else if (!origName.equals(td.getName())) {
                        return null;
                    }
                }
            }
            return origName;
        }
        
        protected boolean isTypeName() {
            return true;
        }
        
        private boolean update(final ClassPool cp, final String oldName, final String typeName) throws BadBytecode {
            if (typeName == null) {
                return false;
            }
            if (oldName == null) {
                return true;
            }
            if (oldName.equals(typeName)) {
                return false;
            }
            if (typeName.charAt(0) == '[' && oldName.equals("[Ljava.lang.Object;")) {
                return true;
            }
            try {
                if (this.cache == null) {
                    this.cache = cp.get(oldName);
                }
                final CtClass cache2 = cp.get(typeName);
                if (cache2.subtypeOf(this.cache)) {
                    this.cache = cache2;
                    return true;
                }
                return false;
            }
            catch (NotFoundException e) {
                throw new BadBytecode("cannot find " + e.getMessage());
            }
        }
        
        public String getExpected() throws BadBytecode {
            final ArrayList equiv = this.equivalences;
            if (equiv.size() == 1) {
                return this.getName();
            }
            final String en = this.expectedName;
            if (en == null) {
                return "java.lang.Object";
            }
            return en;
        }
        
        public String toString() {
            try {
                final String en = this.expectedName;
                if (en != null) {
                    return en;
                }
                final String name = this.getName();
                if (this.equivalences.size() == 1) {
                    return name;
                }
                return name + "?";
            }
            catch (BadBytecode e) {
                return "<" + e.getMessage() + ">";
            }
        }
    }
    
    public static class ClassName extends TypeName
    {
        private String name;
        
        public ClassName(final String n) {
            this.name = n;
        }
        
        public TypeData copy() {
            return new ClassName(this.name);
        }
        
        public String getName() {
            return this.name;
        }
    }
    
    public static class NullType extends ClassName
    {
        public NullType() {
            super("null");
        }
        
        public TypeData copy() {
            return new NullType();
        }
        
        public boolean isNullType() {
            return true;
        }
        
        public int getTypeTag() {
            try {
                if ("null".equals(this.getExpected())) {
                    return 5;
                }
                return super.getTypeTag();
            }
            catch (BadBytecode e) {
                throw new RuntimeException("fatal error: ", e);
            }
        }
        
        protected int getTypeData2(final ConstPool cp, final String type) {
            if ("null".equals(type)) {
                return 0;
            }
            return super.getTypeData2(cp, type);
        }
        
        public String getExpected() throws BadBytecode {
            final String en = this.expectedName;
            if (en == null) {
                return "java.lang.Object";
            }
            return en;
        }
    }
    
    public static class ArrayElement extends TypeName
    {
        TypeData array;
        
        public ArrayElement(final TypeData a) {
            this.array = a;
        }
        
        public TypeData copy() {
            return new ArrayElement(this.array);
        }
        
        protected void setType(final String typeName, final ClassPool cp) throws BadBytecode {
            super.setType(typeName, cp);
            this.array.setType(getArrayType(typeName), cp);
        }
        
        public String getName() throws BadBytecode {
            final String name = this.array.getName();
            if (name.length() > 1 && name.charAt(0) == '[') {
                final char c = name.charAt(1);
                if (c == 'L') {
                    return name.substring(2, name.length() - 1).replace('/', '.');
                }
                if (c == '[') {
                    return name.substring(1);
                }
            }
            throw new BadBytecode("bad array type for AALOAD: " + name);
        }
        
        public static String getArrayType(final String elementType) {
            if (elementType.charAt(0) == '[') {
                return "[" + elementType;
            }
            return "[L" + elementType.replace('.', '/') + ";";
        }
        
        public static String getElementType(final String arrayType) {
            final char c = arrayType.charAt(1);
            if (c == 'L') {
                return arrayType.substring(2, arrayType.length() - 1).replace('/', '.');
            }
            if (c == '[') {
                return arrayType.substring(1);
            }
            return arrayType;
        }
    }
    
    public static class UninitData extends TypeData
    {
        String className;
        int offset;
        boolean initialized;
        
        UninitData(final int offset, final String className) {
            this.className = className;
            this.offset = offset;
            this.initialized = false;
        }
        
        public void merge(final TypeData neighbor) {
        }
        
        public int getTypeTag() {
            return 8;
        }
        
        public int getTypeData(final ConstPool cp) {
            return this.offset;
        }
        
        public boolean equals(final Object obj) {
            if (obj instanceof UninitData) {
                final UninitData ud = (UninitData)obj;
                return this.offset == ud.offset && this.className.equals(ud.className);
            }
            return false;
        }
        
        public TypeData getSelf() {
            if (this.initialized) {
                return this.copy();
            }
            return this;
        }
        
        public TypeData copy() {
            return new ClassName(this.className);
        }
        
        public boolean isObjectType() {
            return true;
        }
        
        protected void setType(final String typeName, final ClassPool cp) throws BadBytecode {
            this.initialized = true;
        }
        
        public void evalExpectedType(final ClassPool cp) throws BadBytecode {
        }
        
        public String getName() {
            return this.className;
        }
        
        public String getExpected() {
            return this.className;
        }
        
        public String toString() {
            return "uninit:" + this.className + "@" + this.offset;
        }
    }
    
    public static class UninitThis extends UninitData
    {
        UninitThis(final String className) {
            super(-1, className);
        }
        
        public int getTypeTag() {
            return 6;
        }
        
        public int getTypeData(final ConstPool cp) {
            return 0;
        }
        
        public boolean equals(final Object obj) {
            return obj instanceof UninitThis;
        }
        
        public String toString() {
            return "uninit:this";
        }
    }
}
