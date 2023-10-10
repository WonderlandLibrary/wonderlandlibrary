// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode;

import javassist.CtClass;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class SignatureAttribute extends AttributeInfo
{
    public static final String tag = "Signature";
    
    SignatureAttribute(final ConstPool cp, final int n, final DataInputStream in) throws IOException {
        super(cp, n, in);
    }
    
    public SignatureAttribute(final ConstPool cp, final String signature) {
        super(cp, "Signature");
        final int index = cp.addUtf8Info(signature);
        final byte[] bvalue = { (byte)(index >>> 8), (byte)index };
        this.set(bvalue);
    }
    
    public String getSignature() {
        return this.getConstPool().getUtf8Info(ByteArray.readU16bit(this.get(), 0));
    }
    
    public void setSignature(final String sig) {
        final int index = this.getConstPool().addUtf8Info(sig);
        ByteArray.write16bit(index, this.info, 0);
    }
    
    public AttributeInfo copy(final ConstPool newCp, final Map classnames) {
        return new SignatureAttribute(newCp, this.getSignature());
    }
    
    void renameClass(final String oldname, final String newname) {
        final String sig = renameClass(this.getSignature(), oldname, newname);
        this.setSignature(sig);
    }
    
    void renameClass(final Map classnames) {
        final String sig = renameClass(this.getSignature(), classnames);
        this.setSignature(sig);
    }
    
    static String renameClass(final String desc, final String oldname, final String newname) {
        if (desc.indexOf(oldname) < 0) {
            return desc;
        }
        final StringBuffer newdesc = new StringBuffer();
        int head = 0;
        int i = 0;
        while (true) {
            final int j = desc.indexOf(76, i);
            if (j < 0) {
                break;
            }
            int k = j;
            int p = 0;
            boolean match = true;
            char c;
            try {
                final int len = oldname.length();
                while (isNamePart(c = desc.charAt(++k))) {
                    if (p >= len || c != oldname.charAt(p++)) {
                        match = false;
                    }
                }
            }
            catch (IndexOutOfBoundsException e) {
                break;
            }
            i = k + 1;
            if (!match || p != oldname.length()) {
                continue;
            }
            newdesc.append(desc.substring(head, j));
            newdesc.append('L');
            newdesc.append(newname);
            newdesc.append(c);
            head = i;
        }
        if (head == 0) {
            return desc;
        }
        final int len2 = desc.length();
        if (head < len2) {
            newdesc.append(desc.substring(head, len2));
        }
        return newdesc.toString();
    }
    
    static String renameClass(final String desc, final Map map) {
        if (map == null) {
            return desc;
        }
        final StringBuffer newdesc = new StringBuffer();
        int head = 0;
        int i = 0;
        while (true) {
            final int j = desc.indexOf(76, i);
            if (j < 0) {
                break;
            }
            final StringBuffer nameBuf = new StringBuffer();
            int k = j;
            char c;
            try {
                while (isNamePart(c = desc.charAt(++k))) {
                    nameBuf.append(c);
                }
            }
            catch (IndexOutOfBoundsException e) {
                break;
            }
            i = k + 1;
            final String name = nameBuf.toString();
            final String name2 = map.get(name);
            if (name2 == null) {
                continue;
            }
            newdesc.append(desc.substring(head, j));
            newdesc.append('L');
            newdesc.append(name2);
            newdesc.append(c);
            head = i;
        }
        if (head == 0) {
            return desc;
        }
        final int len = desc.length();
        if (head < len) {
            newdesc.append(desc.substring(head, len));
        }
        return newdesc.toString();
    }
    
    private static boolean isNamePart(final int c) {
        return c != 59 && c != 60;
    }
    
    public static ClassSignature toClassSignature(final String sig) throws BadBytecode {
        try {
            return parseSig(sig);
        }
        catch (IndexOutOfBoundsException e) {
            throw error(sig);
        }
    }
    
    public static MethodSignature toMethodSignature(final String sig) throws BadBytecode {
        try {
            return parseMethodSig(sig);
        }
        catch (IndexOutOfBoundsException e) {
            throw error(sig);
        }
    }
    
    public static ObjectType toFieldSignature(final String sig) throws BadBytecode {
        try {
            return parseObjectType(sig, new Cursor(), false);
        }
        catch (IndexOutOfBoundsException e) {
            throw error(sig);
        }
    }
    
    private static ClassSignature parseSig(final String sig) throws BadBytecode, IndexOutOfBoundsException {
        final Cursor cur = new Cursor();
        final TypeParameter[] tp = parseTypeParams(sig, cur);
        final ClassType superClass = parseClassType(sig, cur);
        final int sigLen = sig.length();
        final ArrayList ifArray = new ArrayList();
        while (cur.position < sigLen && sig.charAt(cur.position) == 'L') {
            ifArray.add(parseClassType(sig, cur));
        }
        final ClassType[] ifs = ifArray.toArray(new ClassType[ifArray.size()]);
        return new ClassSignature(tp, superClass, ifs);
    }
    
    private static MethodSignature parseMethodSig(final String sig) throws BadBytecode {
        final Cursor cur = new Cursor();
        final TypeParameter[] tp = parseTypeParams(sig, cur);
        if (sig.charAt(cur.position++) != '(') {
            throw error(sig);
        }
        final ArrayList params = new ArrayList();
        while (sig.charAt(cur.position) != ')') {
            final Type t = parseType(sig, cur);
            params.add(t);
        }
        final Cursor cursor = cur;
        ++cursor.position;
        final Type ret = parseType(sig, cur);
        final int sigLen = sig.length();
        final ArrayList exceptions = new ArrayList();
        while (cur.position < sigLen && sig.charAt(cur.position) == '^') {
            final Cursor cursor2 = cur;
            ++cursor2.position;
            final ObjectType t2 = parseObjectType(sig, cur, false);
            if (t2 instanceof ArrayType) {
                throw error(sig);
            }
            exceptions.add(t2);
        }
        final Type[] p = params.toArray(new Type[params.size()]);
        final ObjectType[] ex = exceptions.toArray(new ObjectType[exceptions.size()]);
        return new MethodSignature(tp, p, ret, ex);
    }
    
    private static TypeParameter[] parseTypeParams(final String sig, final Cursor cur) throws BadBytecode {
        final ArrayList typeParam = new ArrayList();
        if (sig.charAt(cur.position) == '<') {
            ++cur.position;
            while (sig.charAt(cur.position) != '>') {
                final int nameBegin = cur.position;
                final int nameEnd = cur.indexOf(sig, 58);
                final ObjectType classBound = parseObjectType(sig, cur, true);
                final ArrayList ifBound = new ArrayList();
                while (sig.charAt(cur.position) == ':') {
                    ++cur.position;
                    final ObjectType t = parseObjectType(sig, cur, false);
                    ifBound.add(t);
                }
                final TypeParameter p = new TypeParameter(sig, nameBegin, nameEnd, classBound, ifBound.toArray(new ObjectType[ifBound.size()]));
                typeParam.add(p);
            }
            ++cur.position;
        }
        return typeParam.toArray(new TypeParameter[typeParam.size()]);
    }
    
    private static ObjectType parseObjectType(final String sig, final Cursor c, final boolean dontThrow) throws BadBytecode {
        final int begin = c.position;
        switch (sig.charAt(begin)) {
            case 'L': {
                return parseClassType2(sig, c, null);
            }
            case 'T': {
                final int i = c.indexOf(sig, 59);
                return new TypeVariable(sig, begin + 1, i);
            }
            case '[': {
                return parseArray(sig, c);
            }
            default: {
                if (dontThrow) {
                    return null;
                }
                throw error(sig);
            }
        }
    }
    
    private static ClassType parseClassType(final String sig, final Cursor c) throws BadBytecode {
        if (sig.charAt(c.position) == 'L') {
            return parseClassType2(sig, c, null);
        }
        throw error(sig);
    }
    
    private static ClassType parseClassType2(final String sig, final Cursor c, final ClassType parent) throws BadBytecode {
        final int start = ++c.position;
        char t;
        do {
            t = sig.charAt(c.position++);
        } while (t != '$' && t != '<' && t != ';');
        final int end = c.position - 1;
        TypeArgument[] targs;
        if (t == '<') {
            targs = parseTypeArgs(sig, c);
            t = sig.charAt(c.position++);
        }
        else {
            targs = null;
        }
        final ClassType thisClass = ClassType.make(sig, start, end, targs, parent);
        if (t == '$') {
            --c.position;
            return parseClassType2(sig, c, thisClass);
        }
        return thisClass;
    }
    
    private static TypeArgument[] parseTypeArgs(final String sig, final Cursor c) throws BadBytecode {
        final ArrayList args = new ArrayList();
        char t;
        while ((t = sig.charAt(c.position++)) != '>') {
            TypeArgument ta;
            if (t == '*') {
                ta = new TypeArgument(null, '*');
            }
            else {
                if (t != '+' && t != '-') {
                    t = ' ';
                    --c.position;
                }
                ta = new TypeArgument(parseObjectType(sig, c, false), t);
            }
            args.add(ta);
        }
        return args.toArray(new TypeArgument[args.size()]);
    }
    
    private static ObjectType parseArray(final String sig, final Cursor c) throws BadBytecode {
        int dim = 1;
        while (sig.charAt(++c.position) == '[') {
            ++dim;
        }
        return new ArrayType(dim, parseType(sig, c));
    }
    
    private static Type parseType(final String sig, final Cursor c) throws BadBytecode {
        Type t = parseObjectType(sig, c, true);
        if (t == null) {
            t = new BaseType(sig.charAt(c.position++));
        }
        return t;
    }
    
    private static BadBytecode error(final String sig) {
        return new BadBytecode("bad signature: " + sig);
    }
    
    private static class Cursor
    {
        int position;
        
        private Cursor() {
            this.position = 0;
        }
        
        int indexOf(final String s, final int ch) throws BadBytecode {
            final int i = s.indexOf(ch, this.position);
            if (i < 0) {
                throw error(s);
            }
            this.position = i + 1;
            return i;
        }
    }
    
    public static class ClassSignature
    {
        TypeParameter[] params;
        ClassType superClass;
        ClassType[] interfaces;
        
        ClassSignature(final TypeParameter[] p, final ClassType s, final ClassType[] i) {
            this.params = p;
            this.superClass = s;
            this.interfaces = i;
        }
        
        public TypeParameter[] getParameters() {
            return this.params;
        }
        
        public ClassType getSuperClass() {
            return this.superClass;
        }
        
        public ClassType[] getInterfaces() {
            return this.interfaces;
        }
        
        public String toString() {
            final StringBuffer sbuf = new StringBuffer();
            TypeParameter.toString(sbuf, this.params);
            sbuf.append(" extends ").append(this.superClass);
            if (this.interfaces.length > 0) {
                sbuf.append(" implements ");
                Type.toString(sbuf, this.interfaces);
            }
            return sbuf.toString();
        }
    }
    
    public static class MethodSignature
    {
        TypeParameter[] typeParams;
        Type[] params;
        Type retType;
        ObjectType[] exceptions;
        
        MethodSignature(final TypeParameter[] tp, final Type[] p, final Type ret, final ObjectType[] ex) {
            this.typeParams = tp;
            this.params = p;
            this.retType = ret;
            this.exceptions = ex;
        }
        
        public TypeParameter[] getTypeParameters() {
            return this.typeParams;
        }
        
        public Type[] getParameterTypes() {
            return this.params;
        }
        
        public Type getReturnType() {
            return this.retType;
        }
        
        public ObjectType[] getExceptionTypes() {
            return this.exceptions;
        }
        
        public String toString() {
            final StringBuffer sbuf = new StringBuffer();
            TypeParameter.toString(sbuf, this.typeParams);
            sbuf.append(" (");
            Type.toString(sbuf, this.params);
            sbuf.append(") ");
            sbuf.append(this.retType);
            if (this.exceptions.length > 0) {
                sbuf.append(" throws ");
                Type.toString(sbuf, this.exceptions);
            }
            return sbuf.toString();
        }
    }
    
    public static class TypeParameter
    {
        String name;
        ObjectType superClass;
        ObjectType[] superInterfaces;
        
        TypeParameter(final String sig, final int nb, final int ne, final ObjectType sc, final ObjectType[] si) {
            this.name = sig.substring(nb, ne);
            this.superClass = sc;
            this.superInterfaces = si;
        }
        
        public String getName() {
            return this.name;
        }
        
        public ObjectType getClassBound() {
            return this.superClass;
        }
        
        public ObjectType[] getInterfaceBound() {
            return this.superInterfaces;
        }
        
        public String toString() {
            final StringBuffer sbuf = new StringBuffer(this.getName());
            if (this.superClass != null) {
                sbuf.append(" extends ").append(this.superClass.toString());
            }
            final int len = this.superInterfaces.length;
            if (len > 0) {
                for (int i = 0; i < len; ++i) {
                    if (i > 0 || this.superClass != null) {
                        sbuf.append(" & ");
                    }
                    else {
                        sbuf.append(" extends ");
                    }
                    sbuf.append(this.superInterfaces[i].toString());
                }
            }
            return sbuf.toString();
        }
        
        static void toString(final StringBuffer sbuf, final TypeParameter[] tp) {
            sbuf.append('<');
            for (int i = 0; i < tp.length; ++i) {
                if (i > 0) {
                    sbuf.append(", ");
                }
                sbuf.append(tp[i]);
            }
            sbuf.append('>');
        }
    }
    
    public static class TypeArgument
    {
        ObjectType arg;
        char wildcard;
        
        TypeArgument(final ObjectType a, final char w) {
            this.arg = a;
            this.wildcard = w;
        }
        
        public char getKind() {
            return this.wildcard;
        }
        
        public boolean isWildcard() {
            return this.wildcard != ' ';
        }
        
        public ObjectType getType() {
            return this.arg;
        }
        
        public String toString() {
            if (this.wildcard == '*') {
                return "?";
            }
            final String type = this.arg.toString();
            if (this.wildcard == ' ') {
                return type;
            }
            if (this.wildcard == '+') {
                return "? extends " + type;
            }
            return "? super " + type;
        }
    }
    
    public abstract static class Type
    {
        static void toString(final StringBuffer sbuf, final Type[] ts) {
            for (int i = 0; i < ts.length; ++i) {
                if (i > 0) {
                    sbuf.append(", ");
                }
                sbuf.append(ts[i]);
            }
        }
    }
    
    public static class BaseType extends Type
    {
        char descriptor;
        
        BaseType(final char c) {
            this.descriptor = c;
        }
        
        public char getDescriptor() {
            return this.descriptor;
        }
        
        public CtClass getCtlass() {
            return Descriptor.toPrimitiveClass(this.descriptor);
        }
        
        public String toString() {
            return Descriptor.toClassName(Character.toString(this.descriptor));
        }
    }
    
    public abstract static class ObjectType extends Type
    {
    }
    
    public static class ClassType extends ObjectType
    {
        String name;
        TypeArgument[] arguments;
        
        static ClassType make(final String s, final int b, final int e, final TypeArgument[] targs, final ClassType parent) {
            if (parent == null) {
                return new ClassType(s, b, e, targs);
            }
            return new NestedClassType(s, b, e, targs, parent);
        }
        
        ClassType(final String signature, final int begin, final int end, final TypeArgument[] targs) {
            this.name = signature.substring(begin, end).replace('/', '.');
            this.arguments = targs;
        }
        
        public String getName() {
            return this.name;
        }
        
        public TypeArgument[] getTypeArguments() {
            return this.arguments;
        }
        
        public ClassType getDeclaringClass() {
            return null;
        }
        
        public String toString() {
            final StringBuffer sbuf = new StringBuffer();
            final ClassType parent = this.getDeclaringClass();
            if (parent != null) {
                sbuf.append(parent.toString()).append('.');
            }
            sbuf.append(this.name);
            if (this.arguments != null) {
                sbuf.append('<');
                for (int n = this.arguments.length, i = 0; i < n; ++i) {
                    if (i > 0) {
                        sbuf.append(", ");
                    }
                    sbuf.append(this.arguments[i].toString());
                }
                sbuf.append('>');
            }
            return sbuf.toString();
        }
    }
    
    public static class NestedClassType extends ClassType
    {
        ClassType parent;
        
        NestedClassType(final String s, final int b, final int e, final TypeArgument[] targs, final ClassType p) {
            super(s, b, e, targs);
            this.parent = p;
        }
        
        public ClassType getDeclaringClass() {
            return this.parent;
        }
    }
    
    public static class ArrayType extends ObjectType
    {
        int dim;
        Type componentType;
        
        public ArrayType(final int d, final Type comp) {
            this.dim = d;
            this.componentType = comp;
        }
        
        public int getDimension() {
            return this.dim;
        }
        
        public Type getComponentType() {
            return this.componentType;
        }
        
        public String toString() {
            final StringBuffer sbuf = new StringBuffer(this.componentType.toString());
            for (int i = 0; i < this.dim; ++i) {
                sbuf.append("[]");
            }
            return sbuf.toString();
        }
    }
    
    public static class TypeVariable extends ObjectType
    {
        String name;
        
        TypeVariable(final String sig, final int begin, final int end) {
            this.name = sig.substring(begin, end);
        }
        
        public String getName() {
            return this.name;
        }
        
        public String toString() {
            return this.name;
        }
    }
}
